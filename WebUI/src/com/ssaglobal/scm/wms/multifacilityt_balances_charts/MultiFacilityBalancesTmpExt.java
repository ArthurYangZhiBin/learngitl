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
package com.ssaglobal.scm.wms.multifacilityt_balances_charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

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
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.MFDBIdentifier;
import com.ssaglobal.scm.wms.wm_multi_faclity_balance.MultiFacilityBalancesSearch;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class MultiFacilityBalancesTmpExt extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesSearch.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		try{		   
			String tooltip = context.getState().getURLParameter("tooltip");
//			_log.debug("LOG_SYSTEM_OUT","tooltip = "+ tooltip,100L);
			StateInterface state = context.getState();

			HttpSession session = state.getRequest().getSession();
			Object dropdownValue = session.getAttribute("LEVEL");
			state.getRequest().getSession().removeAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_STORER_KEY);
			if(session.getAttribute("LEVEL") == null){
				dropdownValue = "";
			}
  	
			if(dropdownValue.toString().equalsIgnoreCase("")||dropdownValue.toString().equalsIgnoreCase("0")){
				session.setAttribute("LEVEL", "0");
//defect410.b
//				result.setFocus(MultiFacilityBalancesEnterprise(context));
				DataBean db = MultiFacilityBalancesEnterprise(context);
				if (db == null){
					session.setAttribute("MF_MESSAGE", "WMEXP_MF_NOT_SET");
					context.setNavigation("menuClickEvent833");
				}else{
					result.setFocus(db);
				}
//defect410.e				
			}
			if(dropdownValue.toString().equalsIgnoreCase("1")){
//defect410.b
//				result.setFocus(MultiFacilityBalancesDivision(context,tooltip));
				DataBean db = MultiFacilityBalancesDivision(context,tooltip);
				if (db == null){
					session.setAttribute("MF_MESSAGE", "WMEXP_MF_NOT_SET");
					context.setNavigation("menuClickEvent833");
				}else{
					result.setFocus(db);
				}
//defect410.e
			}
			if(dropdownValue.toString().equalsIgnoreCase("2")){
//defect410.b
//				result.setFocus(MultiFacilityBalancesDC(context,tooltip));
				DataBean db = MultiFacilityBalancesDC(context,tooltip);
				if (db == null){
					session.setAttribute("MF_MESSAGE", "WMEXP_MF_NOT_SET");
					context.setNavigation("menuClickEvent833");
				}else{
					result.setFocus(db);
				}
//defect410.e
			}
			if (dropdownValue.toString().equalsIgnoreCase("-1")){
				//jp 8925.begin
				_log.debug("LOG_SYSTEM_OUT","jpdebug:inside MultiFacilityBalancesTmpExt",100L);

				DataBean dataBean =MultiFacilityBalancesWarehouse(context,tooltip); 
				//session.setAttribute("MFDataBean", dataBean);

				//result.setFocus(MultiFacilityBalancesWarehouse(context,tooltip));
//defect410.b
				if (dataBean == null){
					session.setAttribute("MF_MESSAGE", "WMEXP_MF_NOT_SET");
					context.setNavigation("menuClickEvent833");
				}else{
					result.setFocus(dataBean);
				}
//defect410.e
				//jp 8925.end
			}
		}catch(Exception e){
			e.printStackTrace();
			return RET_CANCEL;          
		}	  
		return RET_CONTINUE;
	}   

	public DataBean MultiFacilityBalancesEnterprise(UIRenderContext context) throws UserException{
		String storerFilter = "";
		String skuFilter = "";
		StateInterface state = context.getState();
		HashMap warehouseDBTable = new HashMap();  			//Stores warehouse -> database schema mappings
		HashMap warehouseDescriptions = new HashMap();
		ArrayList warehouseAry = new ArrayList();
		int counter = 0;
		DataBean db = null;
		try{
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '-1'", null);
//			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.DESCRIPTION = 'Company'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
			if(warehouseCollection != null && warehouseCollection.size() > 0){
				for(int i = 0; i < warehouseCollection.size(); i++){
					Bio bio = warehouseCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ warehouseCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					warehouseAry.add(bio.get("NESTID").toString());									
				}
			}
			if(warehouseAry != null && warehouseAry.size() > 0){
//				Place one database name per warehouse in hash map
				for(int k = 0; k < warehouseAry.size(); k++){
					String nestId = (String)warehouseAry.get(k);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.NESTID = '"+nestId+"'", "");
					BioCollection warehouseCollection1 = uow.getBioCollectionBean(loadBiosQry1);
					if(warehouseCollection1 != null && warehouseCollection1.size() > 0){
						for(int i = 0; i < warehouseCollection1.size(); i++){
							Bio warehouse = warehouseCollection1.elementAt(i);
							if(!warehouseDescriptions.containsKey(nestId))
								warehouseDescriptions.put(nestId,warehouse.get("DESCRIPTION"));
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from warehouse...",100L);							
							warehouseDBTable.put(nestId,warehouse.get("NAME"));
						}
					}
				}
			}
		}catch(EpiDataException e){
			e.printStackTrace();
		}
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
		HelperBio helper = null;
		final UnitOfWorkBean uowb = state.getTempUnitOfWork();
		final UnitOfWork uow = uowb.getUOW();
		try{
			helper = uow.createHelperBio("wm_multifacbal");
		}catch(EpiDataException e1){
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//Populate Warehouse Records
//		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,warehouseDescriptions);
//		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,warehouseDBTable);
		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SKU_KEY,skuFilter);
		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY,storerFilter);
		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_ENT);
 
		Iterator nestIdItr = warehouseDBTable.keySet().iterator();
		if(warehouseDBTable.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses For Enterprise...",100L);					
			//get warehouse description to populate the warehouse column with...														
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 						
			Array parmsDBList = new Array(MFDBIdentifier.class);						
			if(!WSDefaultsUtil.isOwnerLocked(state))
				parms.add(new TextData(""));
			else
				parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));			
			parms.add(new TextData(""));								
			parms.add(new TextData(""));						
			parms.add(new TextData("%"));
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
			HashMap DescTable = new HashMap();
			dbTable.put("1",dbList);
			DescTable.put("1","Enterprise");
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,dbTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,DescTable);
			actionProperties.setProcedureParameters(parms);						
			actionProperties.setProcedureName("INVBAL");
			actionProperties.setDbNames(parmsDBList);																					
			try {
				EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);					
//				Array arrtNames = collection.getAttribNames();						
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
										
					//SM 10/14/07 ISSUE SCM-00000-02824: No in-transit qty available	
					attrValue = collection.getAttribValue(new TextData("qtyintransit"));
					if(attrValue != null)
						bio.set("QTYINTRANSIT",attrValue.toString());
					else
						bio.set("QTYINTRANSIT",new Integer(0));		
					//SM 10/14/07 End edit						
					setAvaliable(bio);
					attrValue = collection.getAttribValue(new TextData("StorerKey"));							
					if(attrValue != null)
						bio.set("OWNER",attrValue.toString());
					else
						bio.set("OWNER","");
					
//					_log.debug("LOG_SYSTEM_OUT","\n\nOwner:"+attrValue.toString()+"\n\n",100L);
					
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
					BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
					db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));					
				}
			} catch (Exception e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}	
		return db;
	}
  
	public DataBean MultiFacilityBalancesDivision(UIRenderContext context, String tooltip) throws UserException{
//		DefaultPieDataset dataset= new DefaultPieDataset();
		String storerFilter = "", skuFilter = "";

		StateInterface state = context.getState();
		HashMap divisionDBTable = new HashMap(); 			//Stores division -> database schema mappings
		HashMap divisionDescriptions = new HashMap();
//		boolean areDivisionWarehousesPresent = false;
		ArrayList divisionAry = new ArrayList();
		DataBean db = null;
		int counter = 0;
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE DIVISION",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = null;
			if (tooltip != null){
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.DESCRIPTION = '"+tooltip+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			}else{
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '1'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			}
//			_log.debug("LOG_SYSTEM_OUT","loadBiosQry = "+ loadBiosQry.getQueryExpression(),100L);
			BioCollection divisionCollection = uow.getBioCollectionBean(loadBiosQry);
			if(divisionCollection != null && divisionCollection.size() > 0){
				for(int i = 0; i < divisionCollection.size(); i++){
					Bio bio = divisionCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ divisionCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					divisionAry.add(bio.get("NESTID").toString());		
					divisionDescriptions.put(bio.get("NESTID").toString(),bio.get("DESCRIPTION").toString());
				}
//				_log.debug("LOG_SYSTEM_OUT","DIVISION ARY = "+divisionAry,100L);
//				_log.debug("LOG_SYSTEM_OUT","divisionDescriptions ARY = "+divisionDescriptions,100L);
//				_log.debug("LOG_SYSTEM_OUT","divisionDescriptions = "+divisionDescriptions.get("5"),100L);
			}
			if(divisionAry != null && divisionAry.size() > 0){
				// Place one array list of database names per division in hash map
//				_log.debug("LOG_SYSTEM_OUT","divisionAry.SIZE = "+divisionAry.size(),100L);
				for(int i = 0; i < divisionAry.size(); i++){
					ArrayList databaseList = new ArrayList();
					String nestId = (String)divisionAry.get(i);
//					_log.debug("LOG_SYSTEM_OUT","divisionAry.nestid = "+nestId,100L);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
					BioCollection distributionCollection = uow.getBioCollectionBean(loadBiosQry1);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Query:"+loadBiosQry1.getQueryExpression(),100L);
//					_log.debug("LOG_SYSTEM_OUT","distribution collection size = "+distributionCollection.size(),100L);
					if(distributionCollection != null && distributionCollection.size() > 0){
						for(int j = 0; j < distributionCollection.size(); j++){							
							Bio bio = distributionCollection.elementAt(j);									
							if(bio.get("LEVELNUM").toString().equals("-1")){								
								databaseList.add(bio.get("NAME"));
//								areDivisionWarehousesPresent = true;
								continue;
							}							
							loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+bio.get("NESTID")+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
							BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
							if(warehouseCollection != null && warehouseCollection.size() > 0){
								for(int k = 0; k < warehouseCollection.size(); k++){										
									Bio warehouse = warehouseCollection.elementAt(k);		
									_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from dc...",100L);									
									databaseList.add(warehouse.get("NAME"));
//									areDivisionWarehousesPresent = true;
								}
							}
						}
					}
					divisionDBTable.put(nestId,databaseList);
//					_log.debug("LOG_SYSTEM_OUT","divisionDBTable ARY = "+divisionDBTable,100L);
				}
			}	
		}catch(EpiDataException e){
			e.printStackTrace();
		}
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
		HelperBio helper = null;
		final UnitOfWorkBean uowb = state.getTempUnitOfWork();
		final UnitOfWork uow = uowb.getUOW();
		try {
			helper = uow.createHelperBio("wm_multifacbal");
		}catch(EpiDataException e1){
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//Populate Division Records
		if(divisionDBTable.size() > 0){
			Iterator nestIdItr = divisionDBTable.keySet().iterator();
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,divisionDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,divisionDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SKU_KEY,skuFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY,storerFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION);

			//Iterate Divisions...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Divisions...",100L);					
				//get division description to populate the DIVISION column with...
				String divisionNestId = (String)nestIdItr.next();
//				_log.debug("LOG_SYSTEM_OUT","divisionNestId = "+divisionNestId,100L);					
				String divisionName = (String)divisionDescriptions.get(divisionNestId);
//				_log.debug("LOG_SYSTEM_OUT","divisionName = "+divisionName,100L);
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division Name:"+divisionName+" For Key:"+divisionNestId,100L);					
				ArrayList divisionDatabaseList = (ArrayList)divisionDBTable.get(divisionNestId);

				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division DB List:"+divisionDatabaseList+" For Key:"+divisionNestId,100L);					
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				Array parmsDBList = new Array(MFDBIdentifier.class);						
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("%"));
//				_log.debug("LOG_SYSTEM_OUT","division dB size = "+divisionDatabaseList.size(),100L);
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
						
						//SM 10/14/07 ISSUE SCM-00000-02824: No in-transit qty available	
						attrValue = collection.getAttribValue(new TextData("qtyintransit"));
						if(attrValue != null)
							bio.set("QTYINTRANSIT",attrValue.toString());
						else
							bio.set("QTYINTRANSIT",new Integer(0));		
						//SM 10/14/07 End edit	
						
//						attrValue = collection.getAttribValue(new TextData("qtyavailable"));
//						if(attrValue != null)
//							bio.set("AVALIABLE",attrValue.toString());
//						else
//							bio.set("AVALIABLE",new Integer(0));
//						
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
						BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
						db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));					

					}
				}catch (Exception e) {
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}		
		}
		return db;
	}
	
	public DataBean MultiFacilityBalancesDC(UIRenderContext context, String tooltip) throws UserException{
		StateInterface state = context.getState();
		String storerFilter = "";
		String skuFilter = "";
//		DefaultPieDataset dataset= new DefaultPieDataset();
		HashMap disributionCenterDBTable = new HashMap(); 	//Stores distribution ceter -> database schema mappings		
		HashMap distributionCenterDescriptions = new HashMap();
//		boolean areDistCenterWarehousesPresent = false;
		ArrayList distributionCenterAry = new ArrayList();
		DataBean db = null;
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE DISTRIBUTION CENTER",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = null;
			if(tooltip != null){
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.DESCRIPTION = '"+tooltip+"'", null);
			}else{
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '2'", null);
			}

			BioCollection dcCollection = uow.getBioCollectionBean(loadBiosQry);
			if(dcCollection != null && dcCollection.size() > 0){
				for(int i = 0; i < dcCollection.size(); i++){
					Bio bio = dcCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ dcCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					distributionCenterAry.add(bio.get("NESTID").toString());		
					distributionCenterDescriptions.put(bio.get("NESTID").toString(),bio.get("DESCRIPTION").toString());
				}
			}
			if(distributionCenterAry != null && distributionCenterAry.size() > 0){					
//				Place one array list of database names per distribution center in hash map
				for(int j = 0; j < distributionCenterAry.size(); j++){						
					ArrayList databaseList = new ArrayList();
					String nestId = (String)distributionCenterAry.get(j);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "");
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Qry:"+loadBiosQry1.getQueryExpression(),100L);					
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry1);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int k = 0; k < warehouseCollection.size(); k++){
							Bio warehouse = warehouseCollection.elementAt(k);																
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from div...",100L);							
							databaseList.add(warehouse.get("NAME"));	
//							areDistCenterWarehousesPresent = true;
						}
					}
					disributionCenterDBTable.put(nestId,databaseList);
				}
			}				
		}catch(EpiDataException e){
			e.printStackTrace();
		}
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
		HelperBio helper = null;
		final UnitOfWorkBean uowb = state.getTempUnitOfWork();
		final UnitOfWork uow = uowb.getUOW();
		try {
			helper = uow.createHelperBio("wm_multifacbal");
		}catch(EpiDataException e1){
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

//		Populate Distribution Center Records
		if(disributionCenterDBTable.size() > 0){
			Iterator nestIdItr = disributionCenterDBTable.keySet().iterator();
			int counter = 0;
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,disributionCenterDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,distributionCenterDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SKU_KEY,skuFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY,storerFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER);

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
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("%"));
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
						
						//SM 10/14/07 ISSUE SCM-00000-02824: No in-transit qty available	
						attrValue = collection.getAttribValue(new TextData("qtyintransit"));
						if(attrValue != null)
							bio.set("QTYINTRANSIT",attrValue.toString());
						else
							bio.set("QTYINTRANSIT",new Integer(0));		
						//SM 10/14/07 End edit	
						
//						attrValue = collection.getAttribValue(new TextData("qtyavailable"));
//						if(attrValue != null)
//							bio.set("AVALIABLE",attrValue.toString());
//						else
//							bio.set("AVALIABLE",new Integer(0));
						
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
						BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
						db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));					
					}
				}catch(Exception e){
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
		return db;
	}
  
	public DataBean MultiFacilityBalancesWarehouse(UIRenderContext context, String tooltip) throws UserException{
		StateInterface state = context.getState();
		String storerFilter = "", skuFilter = "";

//		DefaultPieDataset dataset= new DefaultPieDataset();
		HashMap warehouseDBTable = new HashMap();  			//Stores warehouse -> database schema mappings		
		HashMap warehouseDescriptions = new HashMap();
//		boolean areDistCenterWarehousesPresent = false;
		ArrayList warehouseAry = new ArrayList();
		DataBean db = null;
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE WAREHOUSE",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = null;
			if (tooltip != null){
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.DESCRIPTION = '"+tooltip+"'", null);
			}else{
				loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '-1'", null);
			}

			BioCollection dcCollection = uow.getBioCollectionBean(loadBiosQry);
			if(dcCollection != null && dcCollection.size() > 0){
				for(int i = 0; i < dcCollection.size(); i++){
					Bio bio = dcCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ dcCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					warehouseAry.add(bio.get("NESTID").toString());		
					warehouseDescriptions.put(bio.get("NESTID").toString(),bio.get("DESCRIPTION").toString());
				}
			}
			if(warehouseAry != null && warehouseAry.size() > 0){
//				Place one database name per warehouse in hash map
				for(int k = 0; k < warehouseAry.size(); k++){
					String nestId = (String)warehouseAry.get(k);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.NESTID = '"+nestId+"'", "");
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry1);
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
		} catch (EpiDataException e) {
			e.printStackTrace();
		}
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

		//Populate Warehouse Records
		if(warehouseDBTable.size() > 0){
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,warehouseDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,warehouseDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SKU_KEY,skuFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY,storerFilter);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_WHSE);	

			Iterator nestIdItr = warehouseDBTable.keySet().iterator();
			int counter = 0;
			//Iterate Warehouses...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses...",100L);					
				//get warehouse description to populate the warehouse column with...
				String warehouseNestId = (String)nestIdItr.next();
				String warehouseName = (String)warehouseDescriptions.get(warehouseNestId);									
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 						
				Array parmsDBList = new Array(MFDBIdentifier.class);	
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
//				_log.debug("LOG_SYSTEM_OUT","Adding Blank Param",100L);
				parms.add(new TextData(""));			
//				_log.debug("LOG_SYSTEM_OUT","Adding Blank Param",100L);
				parms.add(new TextData(""));						
				parms.add(new TextData("%"));
				
				MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();
//				_log.debug("LOG_SYSTEM_OUT","Setting singleDBIdentifier tag:source",100L);					
				singleDBIdentifier.tag("source");
//				_log.debug("LOG_SYSTEM_OUT","Setting singleDBIdentifier connectionId:"+(String)warehouseDBTable.get(warehouseNestId),100L);
				singleDBIdentifier.connectionId((String)warehouseDBTable.get(warehouseNestId));			                
				parmsDBList.add(singleDBIdentifier);					
				actionProperties.setProcedureParameters(parms);						
				actionProperties.setProcedureName("INVBAL");
				actionProperties.setDbNames(parmsDBList);																					
				try {
					EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);					
//					Array arrtNames = collection.getAttribNames();
//					for(int i = 0; i <= arrtNames.size(); i++){
//						_log.debug("LOG_SYSTEM_OUT","\n\nAttr"+i+":"+arrtNames.get(i)+"\n\n",100L);
//					}
					Bio bio = null;					
					//jp 8925.begin
					ArrayList<MultifacbalDTO> multifacbalList = new ArrayList<MultifacbalDTO>();
					//jp 8925.end
					for(int i = 1; i < collection.getRowCount() + 1; i++){	
						collection.setRow(i);
						bio = uow.createBio(helper);	
						
						Object attrValue = collection.getAttribValue(new TextData("qty"));
//						_log.debug("LOG_SYSTEM_OUT","got qty:"+attrValue,100L);
						if(attrValue != null)
							bio.set("ONHAND",attrValue.toString());
						else
							bio.set("ONHAND",new Integer(0));		
						
						attrValue = collection.getAttribValue(new TextData("qtyallocated"));
//						_log.debug("LOG_SYSTEM_OUT","got qtyallocated:"+attrValue,100L);
						if(attrValue != null)
							bio.set("ALLOCATED",attrValue.toString());
						else
							bio.set("ALLOCATED",new Integer(0));					
						
						//SM 10/14/07 ISSUE SCM-00000-02824: No in-transit qty available	
						attrValue = collection.getAttribValue(new TextData("qtyintransit"));
						if(attrValue != null)
							bio.set("QTYINTRANSIT",attrValue.toString());
						else
							bio.set("QTYINTRANSIT",new Integer(0));		
						//SM 10/14/07 End edit	
						
						attrValue = collection.getAttribValue(new TextData("qtypicked"));
//						_log.debug("LOG_SYSTEM_OUT","got qtypicked:"+attrValue,100L);
						if(attrValue != null)
							bio.set("PICKED",attrValue.toString());
						else
							bio.set("PICKED",new Integer(0));			
						
//						attrValue = collection.getAttribValue(new TextData("qtyavailable"));
//						_log.debug("LOG_SYSTEM_OUT","got qtyavailable:"+attrValue,100L);
//						if(attrValue != null)
//						bio.set("AVALIABLE",attrValue.toString());
//						else
//						bio.set("AVALIABLE",new Integer(0));	
//						
						setAvaliable(bio);
						attrValue = collection.getAttribValue(new TextData("StorerKey"));
//						_log.debug("LOG_SYSTEM_OUT","got StorerKey:"+attrValue,100L);
						if(attrValue != null)
							bio.set("OWNER",attrValue.toString());
						else
							bio.set("OWNER","");
						
						bio.set("ITEM","");								
						attrValue = collection.getAttribValue(new TextData("SKU"));
//						_log.debug("LOG_SYSTEM_OUT","got SKU:"+attrValue,100L);
						if(attrValue != null)
							bio.set("ITEM",attrValue.toString());
						else
							bio.set("ITEM","");			
						
						bio.set("SERIALKEY",new Integer(counter++));								
						bio.set("WAREHOUSE",warehouseName);
						bio.set("DISTRIBUTIONCENTER","");
						bio.set("DIVISION","");
						tempBioCollRefArray.add(bio.getBioRef());
						BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
						db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));
						
						//jp 8925.begin
						MultifacbalDTO multifacbalDTO = new MultifacbalDTO();
						multifacbalDTO.setOwner(bio.get("OWNER")==null? null:bio.get("OWNER").toString());
						multifacbalDTO.setOnhand(bio.get("ONHAND")==null?null: bio.get("ONHAND").toString());
						multifacbalDTO.setQtyintransit(bio.get("QTYINTRANSIT")==null?null: bio.get("QTYINTRANSIT").toString());
						multifacbalDTO.setPicked(bio.get("PICKED")==null?null: bio.get("PICKED").toString());
						multifacbalDTO.setItem(bio.get("ITEM")==null?null: bio.get("ITEM").toString());
						multifacbalDTO.setAllocated(bio.get("ALLOCATED")==null?null: bio.get("ALLOCATED").toString());
						multifacbalDTO.setWarehouse(bio.get("WAREHOUSE")==null?null: bio.get("WAREHOUSE").toString());
						multifacbalDTO.setDistributioncenter(bio.get("DISTRIBUTIONCENTER")==null?null: bio.get("DISTRIBUTIONCENTER").toString());
						multifacbalDTO.setDistributioncenter(bio.get("DIVISION")==null?null: bio.get("DIVISION").toString());
						multifacbalDTO.setDistributioncenter(bio.get("SERIALKEY")==null?null: bio.get("SERIALKEY").toString());
						multifacbalDTO.setAvaliable(bio.get("AVALIABLE")==null?null: bio.get("AVALIABLE").toString());
						multifacbalList.add(multifacbalDTO);
						//jp 8925.end
					}
					//jp 8925.begin
					state.getRequest().getSession().setAttribute("MFDataBean", multifacbalList);
					//jp 8925.end
				}catch(Exception e){
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
		return db;
	}
  
	public static void setAvaliable(Bio bio) throws EpiDataException{
		Double onHand = setDouble(bio.get("ONHAND"));
		Double allocated = setDouble(bio.get("ALLOCATED"));
		Double picked = setDouble(bio.get("PICKED"));
		bio.set("AVALIABLE",new Integer(onHand.intValue() - allocated.intValue() - picked.intValue()));
	}
	
	private static Double setDouble(Object obj){
		Double value;
		if(obj==null){
			value = new Double(0);
		}else{
			try{
				value = new Double(Double.parseDouble(obj.toString()));
			}catch(NumberFormatException e){
				value = new Double(0);
			}
		}
		return value;
	}
	private String getLockedOwnersAsDelimitedList(StateInterface state){
//		if(true)
//			return"HARISH,HARISH";
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


