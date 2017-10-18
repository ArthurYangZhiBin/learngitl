/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_multi_faclity_balance;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.MFDBIdentifier;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class MultiFacilityBalancesSearch extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesSearch.class);
	public static final String MULTI_FAC_BAL_DB_TABLE_KEY = "multi.fac.bal.db.tab";
	public static final String MULTI_FAC_BAL_SKU_KEY = "multi.fac.bal.db.sku";
	public static final String MULTI_FAC_BAL_STORER_KEY = "multi.fac.bal.db.storer";
	public static final String MULTI_FAC_BAL_NAME_KEY = "multi.fac.bal.db.name";
	public static final String MULTI_FAC_BAL_SELECTED_LEVEL_KEY = "multi.fac.bal.db.level";
	public static final String MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION = "multi.fac.bal.db.division";
	public static final String MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER = "multi.fac.bal.db.dist.center";
	public static final String MULTI_FAC_BAL_SELECTED_LEVEL_WHSE = "multi.fac.bal.db.whse";
	public static final String MULTI_FAC_BAL_SELECTED_LEVEL_ENT = "multi.fac.bal.db.ent";
	public static final String MULTI_FAC_BAL_SELECTED_STORER_KEY = "multi.fac.bal.sel.storer";
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Executing MultiFacilityBalancesSearch",100L);				
		String searchType = getParameter("searchType").toString();					
		StateInterface state = context.getState();						
		state.getRequest().getSession().removeAttribute(MULTI_FAC_BAL_SELECTED_STORER_KEY);
		RuntimeFormInterface searchForm = context.getSourceWidget().getForm();
		searchForm = searchForm.getParentForm(state);	
		RuntimeFormWidgetInterface skuFilterObj = searchForm.getFormWidgetByName("SKU");
		String skuFilter = skuFilterObj == null || skuFilterObj.getDisplayValue() == null?"":skuFilterObj.getDisplayValue();
		RuntimeFormWidgetInterface storerFilterObj = searchForm.getFormWidgetByName("STORER");
		String storerFilter = storerFilterObj == null || storerFilterObj.getDisplayValue() == null?"":storerFilterObj.getDisplayValue();
		String enterprise = (String)searchForm.getFormWidgetByName("ENTERPRISE").getValue();
		Object warehouseobj = searchForm.getFormWidgetByName("WAREHOUSE");
		Object divisionobj = searchForm.getFormWidgetByName("DIVISION");
		Object distributionCenterObj = searchForm.getFormWidgetByName("DISTRIBUTIONCENTER");			
		ArrayList distributionCenterAry = null;
		ArrayList divisionAry = null;
		ArrayList warehouseAry = null;
		boolean isFacilitySelected = false;					
		
		//Assert user has selected something...
		if(enterprise == null || !enterprise.equals("1")){			
			if(distributionCenterObj == null || ((RuntimeFormWidgetInterface)distributionCenterObj).getValue() == null || ((ArrayList)((RuntimeFormWidgetInterface)distributionCenterObj).getValue()).size() == 0){				
				if(divisionobj == null || ((RuntimeFormWidgetInterface)divisionobj).getValue() == null || ((ArrayList)((RuntimeFormWidgetInterface)divisionobj).getValue()).size() == 0){					
					if(warehouseobj == null || ((RuntimeFormWidgetInterface)warehouseobj).getValue() == null || ((ArrayList)((RuntimeFormWidgetInterface)warehouseobj).getValue()).size() == 0){						
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}
		}
		
		if(enterprise != null && enterprise.equals("1")){				
			isFacilitySelected = true;
		}
		
		//Get arrays containing user selections for Division DC and Warehouse. If selections are made at more than
		//One level raise error.
		if(distributionCenterObj != null && ((RuntimeFormWidgetInterface)distributionCenterObj).getValue() != null){				
			distributionCenterAry = (ArrayList)((RuntimeFormWidgetInterface)distributionCenterObj).getValue();
			if(distributionCenterAry.size() > 0 && isFacilitySelected ){
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_MULTI_FAC_BAL_ONLY_ONE_LVL",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			else if(distributionCenterAry.size() > 0){					
				isFacilitySelected = true;
			}
		}
		if(divisionobj != null && ((RuntimeFormWidgetInterface)divisionobj).getValue() != null){
			divisionAry = (ArrayList)((RuntimeFormWidgetInterface)divisionobj).getValue();
			if(divisionAry.size() > 0 && isFacilitySelected ){
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_MULTI_FAC_BAL_ONLY_ONE_LVL",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			else if(divisionAry.size() > 0){					
				isFacilitySelected = true;
			}
		}
		if(warehouseobj != null && ((RuntimeFormWidgetInterface)warehouseobj).getValue() != null){
			warehouseAry = (ArrayList)((RuntimeFormWidgetInterface)warehouseobj).getValue();
			if(warehouseAry.size() > 0 && isFacilitySelected ){
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_MULTI_FAC_BAL_ONLY_ONE_LVL",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			else if(warehouseAry.size() > 0){					
				isFacilitySelected = true;
			}
		}
		
		HashMap warehouseDBTable = new HashMap();  			//Stores warehouse -> database schema mappings
		HashMap divisionDBTable = new HashMap(); 			//Stores division -> database schema mappings
		HashMap disributionCenterDBTable = new HashMap(); 	//Stores distribution ceter -> database schema mappings		
		HashMap warehouseDescriptions = new HashMap();
		HashMap distributionCenterDescriptions = new HashMap();
		HashMap divisionDescriptions = new HashMap();
		boolean areDivisionWarehousesPresent = false;
		boolean areDistCenterWarehousesPresent = false;
		try {
			//If enterprise is selected then load dist center, division, and warehouse arys...
			if(enterprise.equals("1")){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '-1'", null);
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int i = 0; i < warehouseCollection.size(); i++){
							Bio bio = warehouseCollection.elementAt(i);
							
							if(!warehouseAry.contains(bio.get("NESTID").toString())){		
								_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
								warehouseAry.add(bio.get("NESTID").toString());									
							}								
						}
					}
				} catch (EpiDataException e) {
					e.printStackTrace();
				}
			}
			
			if(divisionAry != null && divisionAry.size() > 0){
				// Place one array list of database names per division in hash map
				for(int i = 0; i < divisionAry.size(); i++){					
					ArrayList databaseList = new ArrayList();
					String nestId = (String)divisionAry.get(i);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "");
					BioCollection distributionCollection = uow.getBioCollectionBean(loadBiosQry);					
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Query:"+loadBiosQry.getQueryExpression(),100L);					
					if(distributionCollection != null && distributionCollection.size() > 0){
						for(int j = 0; j < distributionCollection.size(); j++){							
							Bio bio = distributionCollection.elementAt(j);									
							if(bio.get("LEVELNUM").toString().equals("-1")){								
								databaseList.add(bio.get("NAME"));	
								areDivisionWarehousesPresent = true;
								continue;
							}							
							loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+bio.get("NESTID")+"'", "");
							BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
							if(warehouseCollection != null && warehouseCollection.size() > 0){
								for(int k = 0; k < warehouseCollection.size(); k++){										
									Bio warehouse = warehouseCollection.elementAt(k);		
									_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from dc...",100L);									
									databaseList.add(warehouse.get("NAME"));	
									areDivisionWarehousesPresent = true;
								}
							}
						}
					}
					divisionDBTable.put(nestId,databaseList);
				}
			}
			if(distributionCenterAry != null && distributionCenterAry.size() > 0){					
//				Place one array list of database names per distribution center in hash map
				for(int j = 0; j < distributionCenterAry.size(); j++){						
					ArrayList databaseList = new ArrayList();
					String nestId = (String)distributionCenterAry.get(j);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "");
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Qry:"+loadBiosQry.getQueryExpression(),100L);					
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int k = 0; k < warehouseCollection.size(); k++){
							Bio warehouse = warehouseCollection.elementAt(k);																
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from div...",100L);							
							databaseList.add(warehouse.get("NAME"));	
							areDistCenterWarehousesPresent = true;
						}
					}
					disributionCenterDBTable.put(nestId,databaseList);
				}
			}				
			if(warehouseAry != null && warehouseAry.size() > 0){
//				Place one database name per warehouse in hash map
				for(int k = 0; k < warehouseAry.size(); k++){
					String nestId = (String)warehouseAry.get(k);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.NESTID = '"+nestId+"'", "");
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int i = 0; i < warehouseCollection.size(); i++){
							Bio warehouse = warehouseCollection.elementAt(i);
							if(!warehouseDescriptions.containsKey(nestId))
								warehouseDescriptions.put(nestId,warehouse.get("DESCRIPTION"));
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from warehouse...",100L);							
							warehouseDBTable.put(nestId,warehouse.get("NAME"));
						}
					}
				}
			}
			
		} catch (EpiDataException e2) {
			e2.printStackTrace();
		}	
		
		try {
			//Load Description Tables to decrease database hits...
			if(areDistCenterWarehousesPresent){
				String qry = "";			
				for(int i = 0; i < distributionCenterAry.size(); i++){
					String nestId = (String)distributionCenterAry.get(i);				
					if(qry.length() == 0){
						qry += "wm_facilitynest_warehouse_mfb.NESTID = '" + nestId + "'";
					}					
					else{
						qry += " OR " + "wm_facilitynest_warehouse_mfb.NESTID = '" + nestId + "'";
					}
				}
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb",qry, null);
				BioCollection distributionCenterCollection = uow.getBioCollectionBean(loadBiosQry);
				if(distributionCenterCollection != null && distributionCenterCollection.size() > 0){
					for(int i = 0; i < distributionCenterCollection.size(); i++){
						Bio record = distributionCenterCollection.elementAt(i);							
						distributionCenterDescriptions.put(record.get("NESTID").toString(),record.get("DESCRIPTION"));
					}
				}
			}
			if(areDivisionWarehousesPresent){
				String qry = "";			
				for(int i = 0; i < divisionAry.size(); i++){
					String nestId = (String)divisionAry.get(i);				
					if(qry.length() == 0){
						qry += "wm_facilitynest_warehouse_mfb.NESTID = '" + nestId + "'";
					}					
					else{
						qry += " OR " + "wm_facilitynest_warehouse_mfb.NESTID = '" + nestId + "'";
					}
				}
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb",qry, null);
				BioCollection divisionCollection = uow.getBioCollectionBean(loadBiosQry);
				if(divisionCollection != null && divisionCollection.size() > 0){
					for(int i = 0; i < divisionCollection.size(); i++){
						Bio record = divisionCollection.elementAt(i);
						divisionDescriptions.put(record.get("NESTID").toString(),record.get("DESCRIPTION"));
					}
				}
			}		
		} catch (EpiDataException e2) {
			e2.printStackTrace();
		}
				
//		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Dist Center Table :"+distributionCenterDescriptions,100L);
//		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Division Table Size:"+divisionDescriptions,100L);		
		
		//Populate Division, Distribution Center, Warehouse, and Enterprise records for "Item" search
		if(searchType.equalsIgnoreCase("ITEM")){
			ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
			HelperBio helper = null;
			final UnitOfWorkBean uowb = state.getTempUnitOfWork();
			final UnitOfWork uow = uowb.getUOW();
			try {
				helper = uow.createHelperBio("wm_multifacbal");
			} catch (EpiDataException e1) {
				e1.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			//Populate Division Records
			if(divisionDBTable.size() > 0){
				Iterator nestIdItr = divisionDBTable.keySet().iterator();
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_DB_TABLE_KEY,divisionDBTable);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_NAME_KEY,divisionDescriptions);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SKU_KEY,skuFilter);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_STORER_KEY,storerFilter);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION);
				int counter = 0;
				//Iterate Divisions...
				while (nestIdItr.hasNext()){
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Divisions...",100L);					
					//get division description to populate the DIVISION column with...
					String divisionNestId = (String)nestIdItr.next();
					String divisionName = (String)divisionDescriptions.get(divisionNestId);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division Name:"+divisionName+" For Key:"+divisionNestId,100L);					
					ArrayList divisionDatabaseList = (ArrayList)divisionDBTable.get(divisionNestId);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division DB List:"+divisionDatabaseList+" For Key:"+divisionNestId,100L);					
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					Array parmsDBList = new Array(MFDBIdentifier.class);						
					if(storerFilter.equals("")){
						if(!WSDefaultsUtil.isOwnerLocked(state))
							parms.add(new TextData(""));
						else
							parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
					}else{
						parms.add(new TextData(storerFilter.toUpperCase()));
					}
	
					parms.add(new TextData(""));
					parms.add(new TextData(""));
					if(skuFilter.length() == 0)
						parms.add(new TextData("%"));
					else
						parms.add(new TextData(skuFilter.toUpperCase()));
					for(int i = 0; i < divisionDatabaseList.size(); i++){							
						MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
						singleDBIdentifier.tag("source");
						singleDBIdentifier.connectionId((String)divisionDatabaseList.get(i));			               
						parmsDBList.add(singleDBIdentifier);
					}
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("INVBAL");
					actionProperties.setDbNames(parmsDBList);
					try {
						EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);
						_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","collection size:"+collection.getRowCount(),100L);						
						Bio bio = null;								
						for(int i = 1; i < collection.getRowCount() + 1; i++){	
							collection.setRow(i);
							bio = uow.createBio(helper);											
							Object attrValue = collection.getAttribValue(new TextData("qty"));
							if(attrValue != null)
								bio.set("ONHAND",attrValue.toString());
							else
								bio.set("ONHAND",new Integer(0));								
							attrValue = collection.getAttribValue(new TextData("qtyallocated"));
							if(attrValue != null)
								bio.set("ALLOCATED",attrValue.toString());
							else
								bio.set("ALLOCATED",new Integer(0));								
							attrValue = collection.getAttribValue(new TextData("qtypicked"));
							if(attrValue != null)
								bio.set("PICKED",attrValue.toString());
							else
								bio.set("PICKED",new Integer(0));								
							
//							SM 08/21/07 ISSUE SCM-00000-02824: No in-transit qty available
							attrValue = collection.getAttribValue(new TextData("qtyintransit"));
							if(attrValue != null)
								bio.set("QTYINTRANSIT",attrValue.toString());
							else
								bio.set("QTYINTRANSIT",new Integer(0));
//							SM 08/21/07 End edit
							
							setAvaliable(bio);
							attrValue = collection.getAttribValue(new TextData("StorerKey"));
							if(attrValue != null)
								bio.set("OWNER",attrValue.toString());
							else
								bio.set("OWNER","");
							bio.set("ITEM","");								
							attrValue = collection.getAttribValue(new TextData("SKU"));
							if(attrValue != null)
								bio.set("ITEM",attrValue.toString());
							else
								bio.set("ITEM","");														
							bio.set("SERIALKEY",new Integer(counter++));								
							bio.set("WAREHOUSE","");
							bio.set("DISTRIBUTIONCENTER","");
							bio.set("DIVISION",divisionName);
							tempBioCollRefArray.add(bio.getBioRef());								
						}
					}catch (Exception e) {
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}		
			}
			
//			Populate Distribution Center Records
			if(disributionCenterDBTable.size() > 0){
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_DB_TABLE_KEY,disributionCenterDBTable);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_NAME_KEY,distributionCenterDescriptions);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SKU_KEY,skuFilter);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_STORER_KEY,storerFilter);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER);
				Iterator nestIdItr = disributionCenterDBTable.keySet().iterator();
				int counter = 0;
				//Iterate Distribution Centers...
				while (nestIdItr.hasNext()){
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Dist Centers...",100L);					
					//get distribution center description to populate the Distribution Center column with...
					String distributionCenterNestId = (String)nestIdItr.next();
					String distributionCenterName = (String)distributionCenterDescriptions.get(distributionCenterNestId);						
					ArrayList distributionCenterList = (ArrayList)disributionCenterDBTable.get(distributionCenterNestId);						
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					Array parmsDBList = new Array(MFDBIdentifier.class);					
					if(storerFilter.equals("")){
						if(!WSDefaultsUtil.isOwnerLocked(state))
							parms.add(new TextData(""));
						else
							parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
					}else{
						parms.add(new TextData(storerFilter.toUpperCase()));
					}
					parms.add(new TextData(""));
					parms.add(new TextData(""));
					if(skuFilter.length() == 0)
						parms.add(new TextData("%"));
					else
						parms.add(new TextData(skuFilter.toUpperCase()));						
					for(int i = 0; i < distributionCenterList.size(); i++){							
						MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
						singleDBIdentifier.tag("source");
						singleDBIdentifier.connectionId((String)distributionCenterList.get(i));			               
						parmsDBList.add(singleDBIdentifier);
					}												
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("INVBAL");
					actionProperties.setDbNames(parmsDBList);
					try {
						EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);							
						Bio bio = null;														
						for(int i = 1; i < collection.getRowCount() + 1; i++){								
							collection.setRow(i);
							bio = uow.createBio(helper);											
							Object attrValue = collection.getAttribValue(new TextData("qty"));
							if(attrValue != null)
								bio.set("ONHAND",attrValue.toString());
							else
								bio.set("ONHAND",new Integer(0));									
							attrValue = collection.getAttribValue(new TextData("qtyallocated"));
							if(attrValue != null)
								bio.set("ALLOCATED",attrValue.toString());
							else
								bio.set("ALLOCATED",new Integer(0));	
														
							attrValue = collection.getAttribValue(new TextData("qtypicked"));
							if(attrValue != null)
								bio.set("PICKED",attrValue.toString());
							else
								bio.set("PICKED",new Integer(0));						
							
//							SM 08/21/07 ISSUE SCM-00000-02824: No in-transit qty available
							attrValue = collection.getAttribValue(new TextData("qtyintransit"));
							if(attrValue != null)
								bio.set("QTYINTRANSIT",attrValue.toString());
							else
								bio.set("QTYINTRANSIT",new Integer(0));
//							SM 08/21/07 End edit
							
							setAvaliable(bio);
							
							attrValue = collection.getAttribValue(new TextData("StorerKey"));
							if(attrValue != null)
								bio.set("OWNER",attrValue.toString());
							else
								bio.set("OWNER","");
														
							bio.set("ITEM","");								
							attrValue = collection.getAttribValue(new TextData("SKU"));
							if(attrValue != null)
								bio.set("ITEM",attrValue.toString());
							else
								bio.set("ITEM","");	
							
							bio.set("SERIALKEY",new Integer(counter++));								
							bio.set("WAREHOUSE","");
							bio.set("DISTRIBUTIONCENTER",distributionCenterName);
							bio.set("DIVISION","");
							tempBioCollRefArray.add(bio.getBioRef());								
						}
					} catch (Exception e) {
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}		
			
			//Populate Warehouse Records
			if(warehouseDBTable.size() > 0){
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_DB_TABLE_KEY,warehouseDBTable);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_NAME_KEY,warehouseDescriptions);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SKU_KEY,skuFilter);
				state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_STORER_KEY,storerFilter);
				if(enterprise != null && enterprise.equals("1")){
					state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MULTI_FAC_BAL_SELECTED_LEVEL_ENT);
				}
				else{
					state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MULTI_FAC_BAL_SELECTED_LEVEL_WHSE);	
				}				
				Iterator nestIdItr = warehouseDBTable.keySet().iterator();
				int counter = 0;
				//Iterate Warehouses...
				if(enterprise != null && enterprise.equals("1")){
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses For Enterprise...",100L);					
					//get warehouse description to populate the warehouse column with...														
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 						
					Array parmsDBList = new Array(MFDBIdentifier.class);						
					if(storerFilter.equals("")){
						if(!WSDefaultsUtil.isOwnerLocked(state))
							parms.add(new TextData(""));
						else
							parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
					}else{
						parms.add(new TextData(storerFilter.toUpperCase()));
					}					
					parms.add(new TextData(""));								
					parms.add(new TextData(""));						
					if(skuFilter.length() == 0){								
						parms.add(new TextData("%"));
					}
					else{							
						parms.add(new TextData(skuFilter.toUpperCase()));						
					}					
					String warehouseNestId = "";
					ArrayList dbList = new ArrayList();
					while (nestIdItr.hasNext()){
						warehouseNestId = (String)nestIdItr.next();
						MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
						singleDBIdentifier.tag("source");
						singleDBIdentifier.connectionId((String)warehouseDBTable.get(warehouseNestId));								
						dbList.add((String)warehouseDBTable.get(warehouseNestId));
						parmsDBList.add(singleDBIdentifier);
					}		
					HashMap dbTable = new HashMap();
					dbTable.put("ENT",dbList);
					state.getRequest().getSession().setAttribute(MULTI_FAC_BAL_DB_TABLE_KEY,dbTable);
					actionProperties.setProcedureParameters(parms);						
					actionProperties.setProcedureName("INVBAL");
					actionProperties.setDbNames(parmsDBList);																					
					try {
						EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);		
//						Array arrtNames = collection.getAttribNames();						
						Bio bio = null;														
						for(int i = 1; i < collection.getRowCount() + 1; i++){	
							collection.setRow(i);
							bio = uow.createBio(helper);	
							
							Object attrValue = collection.getAttribValue(new TextData("qty"));							
							if(attrValue != null)
								bio.set("ONHAND",attrValue.toString());
							else
								bio.set("ONHAND",new Integer(0));		
							
							attrValue = collection.getAttribValue(new TextData("qtyallocated"));							
							if(attrValue != null)
								bio.set("ALLOCATED",attrValue.toString());
							else
								bio.set("ALLOCATED",new Integer(0));					
							
							attrValue = collection.getAttribValue(new TextData("qtypicked"));							
							if(attrValue != null)
								bio.set("PICKED",attrValue.toString());
							else
								bio.set("PICKED",new Integer(0));			
							
//							SM 08/21/07 ISSUE SCM-00000-02824: No in-transit qty available
							attrValue = collection.getAttribValue(new TextData("qtyintransit"));
							if(attrValue != null)
								bio.set("QTYINTRANSIT",attrValue.toString());
							else
								bio.set("QTYINTRANSIT",new Integer(0));
//							SM 08/21/07 End edit
							
							setAvaliable(bio);
							attrValue = collection.getAttribValue(new TextData("StorerKey"));							
							if(attrValue != null)
								bio.set("OWNER",attrValue.toString());
							else
								bio.set("OWNER","");
														
							
							bio.set("ITEM","");								
							attrValue = collection.getAttribValue(new TextData("SKU"));							
							if(attrValue != null)
								bio.set("ITEM",attrValue.toString());
							else
								bio.set("ITEM","");			
							
							bio.set("SERIALKEY",new Integer(counter++));								
							bio.set("WAREHOUSE","");
							bio.set("DISTRIBUTIONCENTER","");
							bio.set("DIVISION","");
							tempBioCollRefArray.add(bio.getBioRef());								
						}
					} catch (Exception e) {
						e.printStackTrace();
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} else {					
					while (nestIdItr.hasNext()){
						_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses...",100L);					
						//get warehouse description to populate the warehouse column with...
						String warehouseNestId = (String)nestIdItr.next();
						String warehouseName = (String)warehouseDescriptions.get(warehouseNestId);									
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 						
						Array parmsDBList = new Array(MFDBIdentifier.class);							
						if(storerFilter.equals("")){
							if(!WSDefaultsUtil.isOwnerLocked(state))
								parms.add(new TextData(""));
							else
								parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
						}else{
							parms.add(new TextData(storerFilter.toUpperCase()));
						}						
						parms.add(new TextData(""));									
						parms.add(new TextData(""));						
						if(skuFilter.length() == 0){									
							parms.add(new TextData("%"));
						} else {							
							parms.add(new TextData(skuFilter.toUpperCase()));							
						}
						
						MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();									
						singleDBIdentifier.tag("source");						
						singleDBIdentifier.connectionId((String)warehouseDBTable.get(warehouseNestId));			                
						parmsDBList.add(singleDBIdentifier);					
						actionProperties.setProcedureParameters(parms);						
						actionProperties.setProcedureName("INVBAL");
						actionProperties.setDbNames(parmsDBList);																					
						try {
							EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);		
//							Array arrtNames = collection.getAttribNames();							
							Bio bio = null;														
							for(int i = 1; i < collection.getRowCount() + 1; i++){	
								collection.setRow(i);
								bio = uow.createBio(helper);	
								
								Object attrValue = collection.getAttribValue(new TextData("qty"));								
								if(attrValue != null)
									bio.set("ONHAND",attrValue.toString());
								else
									bio.set("ONHAND",new Integer(0));		
								
								attrValue = collection.getAttribValue(new TextData("qtyallocated"));								
								if(attrValue != null)
									bio.set("ALLOCATED",attrValue.toString());
								else
									bio.set("ALLOCATED",new Integer(0));					
								
								attrValue = collection.getAttribValue(new TextData("qtypicked"));								
								if(attrValue != null)
									bio.set("PICKED",attrValue.toString());
								else
									bio.set("PICKED",new Integer(0));			
				
//								SM 08/21/07 ISSUE SCM-00000-02824: No in-transit qty available
								attrValue = collection.getAttribValue(new TextData("qtyintransit"));
								if(attrValue != null)
									bio.set("QTYINTRANSIT",attrValue.toString());
								else
									bio.set("QTYINTRANSIT",new Integer(0));
//								SM 08/21/07 End edit
								
								setAvaliable(bio);
								attrValue = collection.getAttribValue(new TextData("StorerKey"));								
								if(attrValue != null)
									bio.set("OWNER",attrValue.toString());
								else
									bio.set("OWNER","");
								
								bio.set("ITEM","");								
								attrValue = collection.getAttribValue(new TextData("SKU"));								
								if(attrValue != null)
									bio.set("ITEM",attrValue.toString());
								else
									bio.set("ITEM","");			
								
								bio.set("SERIALKEY",new Integer(counter++));								
								bio.set("WAREHOUSE",warehouseName);
								bio.set("DISTRIBUTIONCENTER","");
								bio.set("DIVISION","");
								tempBioCollRefArray.add(bio.getBioRef());								
							}
						} catch (Exception e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}	
				}				
			}
			try {										
				BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
				DataBean db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));
				if(db.isBioCollection() && WSDefaultsUtil.isOwnerLocked(state)){
					boolean found = false;
					BioCollectionBean bcb = (BioCollectionBean)db;
					ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
					if(lockedOwners.size()>0){
						for(int i=0; i<lockedOwners.size(); i++){
							if(lockedOwners.get(i).toString().equals(storerFilter.toUpperCase()) || storerFilter.equals("")){
								found=true;
								i=lockedOwners.size();
							}
						}
					}
					if(!found){
						bcb.setEmptyList(true);
					}
					result.setFocus(bcb);
				}else{
					result.setFocus(db);					
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
			}
			return RET_CONTINUE;
		}																					
		return RET_CONTINUE;		
	}			
	
	public static void setAvaliable(Bio bio) throws EpiDataException{
		Object onHandObj = bio.get("ONHAND");
		Object allocatedObj = bio.get("ALLOCATED");
		Object pickedObj = bio.get("PICKED");
		Double onHand = null;
		Double allocated = null;
		Double picked = null;
		
		if(onHandObj == null){
			onHand = new Double(0);
		} else {
			try {
				onHand = new Double(Double.parseDouble(onHandObj.toString()));
			} catch (NumberFormatException e) {
				onHand = new Double(0);
			}
		}
		
		if(allocatedObj == null){
			allocated = new Double(0);
		} else {
			try {
				allocated = new Double(Double.parseDouble(allocatedObj.toString()));
			} catch (NumberFormatException e) {
				allocated = new Double(0);
			}
		}
		
		if(pickedObj == null){
			picked = new Double(0);
		} else {
			try {
				picked = new Double(Double.parseDouble(pickedObj.toString()));
			} catch (NumberFormatException e) {
				picked = new Double(0);
			}
		}
		bio.set("AVALIABLE",new Integer(onHand.intValue() - allocated.intValue() - picked.intValue()));
	}
	
	private String getLockedOwnersAsDelimitedList(StateInterface state){
		String list = WSDefaultsUtil.getLockedOwnersAsDelimetedList(state, ",");
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);	
		if(lockedOwners != null && lockedOwners.size() > 0){
			if(lockedOwners.size() > 1)
				return list;
			else
				return list + "," + list;
		}
		return list;
	}
}