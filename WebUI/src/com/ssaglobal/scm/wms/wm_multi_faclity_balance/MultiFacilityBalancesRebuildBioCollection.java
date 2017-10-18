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
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.MFDBIdentifier;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class MultiFacilityBalancesRebuildBioCollection extends ActionExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesRebuildBioCollection.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALREBUILDBIO","Executing MultiFacilityBalancesRebuildBioCollection",100L);		
		StateInterface state = context.getState();		
		HttpSession session = state.getRequest().getSession();
		HashMap dbTable = (HashMap)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY);		
		HashMap descTable = (HashMap)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY);
		String skuFilter = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SKU_KEY);
		String storerFilter = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY);
		String selectedLevel = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY);
		String selectedStorer = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_STORER_KEY);
		int counter = 0;
		if(getParameterBoolean("isItemLevel") && selectedStorer != null){
			storerFilter = selectedStorer;
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
		
		//Populate Division Records
		if(dbTable.size() > 0){
			Iterator nestIdItr = dbTable.keySet().iterator();					
			//Iterate Divisions...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALREBUILDBIO","Iterating...",100L);						
				String nestId = (String)nestIdItr.next();
				String name = (String)descTable.get(nestId);
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALREBUILDBIO","Got Name:"+name+" For Key:"+nestId,100L);				
				ArrayList databaseList = null;
				String warehouseDb = null;
				if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_WHSE))
					warehouseDb = (String)dbTable.get(nestId);
				else
					databaseList = (ArrayList)dbTable.get(nestId);
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
				
				if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_WHSE)){
					MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
					singleDBIdentifier.tag("source");
					singleDBIdentifier.connectionId(warehouseDb);			               
					parmsDBList.add(singleDBIdentifier);
				} else {
					for(int i = 0; i < databaseList.size(); i++){							
						MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
						singleDBIdentifier.tag("source");
						singleDBIdentifier.connectionId((String)databaseList.get(i));			               
						parmsDBList.add(singleDBIdentifier);
					}
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
						
//						SM 08/21/07 ISSUE SCM-00000-02824: No in-transit qty available
						attrValue = collection.getAttribValue(new TextData("qtyintransit"));
						if(attrValue != null)
							bio.set("QTYINTRANSIT",attrValue.toString());
						else
							bio.set("QTYINTRANSIT",new Integer(0));
//						SM 08/21/07 End edit	
						
						MultiFacilityBalancesSearch.setAvaliable(bio);
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
						
						if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION)){
							bio.set("DIVISION",name);
						} else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER)){
							bio.set("DISTRIBUTIONCENTER",name);
						} else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_WHSE)){
							bio.set("WAREHOUSE",name);
						}
						tempBioCollRefArray.add(bio.getBioRef());								
					}
				}catch (Exception e) {
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
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