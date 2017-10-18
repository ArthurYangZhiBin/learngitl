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
package com.ssaglobal.scm.wms.wm_load_maintenance;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;


public class AddNewUsingXAction extends ListSelector{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddNewUsingXAction.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		
		_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Executing AddNewUsingXAction",100L);			
		StateInterface state = context.getState();											
		String action = (String)getParameter("action");
		RuntimeListFormInterface form = null;
		if(action.equals("CASEID")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_case_id_popup_body_list",context.getState());
		}
		else if(action.equals("XCASEID")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_xcase_id_popup_body",context.getState());
		}
		else if(action.equals("SHIPMENTORDERS")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_shipment_popup_body",context.getState());
		}
		else if(action.equals("FLOWTHRUORDER")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_fto_popup_body",context.getState());
		}
		else if(action.equals("TRANSSHIP")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_transship_popup_body",context.getState());
		}
		else if(action.equals("TRANSSHIPASN")){
			form = (RuntimeListFormInterface)FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"","wm_load_maintenance_add_new_transshipasn_popup_body",context.getState());
		}
		 			
		try {			
			ArrayList selectedItems = null;
			if(form != null)
				selectedItems = form.getSelectedItems();			
			
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				HttpSession session = context.getState().getRequest().getSession();				
				_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","SessionId:"+session.getId(),100L);
				if(session.getAttribute("LOADSTOPID") == null){
					_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Load Stop Id Is Null...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Exiting AddNewUsingXAction",100L);					
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				String loadStopId = (String)session.getAttribute("LOADSTOPID");				
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();		        
				BioBean bio;
				for(; bioBeanIter.hasNext();){            	  
					try {
						bio = (BioBean)bioBeanIter.next();
						String loadOrderDetailId = new KeyGenBioWrapper().getKey("LOADORDERDETAILID");
						//06/25/2010 FW:  Changed logic to call backend's AddIDToLoad.java instead of adding new record from GUI (Incident3832796_Defect274316) -- Start 
						//AddIDToLoad.java is used to add caseid to load from RF screen
						if(action.equals("CASEID") || action.equals("XCASEID")){
						/* old code
						if(action.equals("CASEID")){								
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is CASEID...",100L);
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							String caseId = (String)bio.get("CASEID");
							String storerKey = (String)bio.get("STORERKEY");
							String ohType = (String)bio.get("OrdersOHType");
							String orderKey = (String)bio.get("ORDERKEY");
							String type = BioAttributeUtil.getString(bio, "TYPE");
							String consigneeKey = bio.get("OrdersConsigneeKey")==null?"":(String)bio.get("OrdersConsigneeKey");
							Query loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.CASEID = '"+caseId.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);				        
							if (bioCollection == null || bioCollection.size() < 1) {								
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);							
								newRecord.set("CASEID",caseId);							
								newRecord.set("STORER",storerKey);				        								
								newRecord.set("OHTYPE",ohType);				       							
								newRecord.set("SHIPMENTORDERID",orderKey);				        								
								newRecord.set("CUSTOMER",consigneeKey);
								newRecord.set("TYPE", type);
								newRecord.save();
								_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);
								uowb.saveUOW(true);
								uowb.clearState();				        		
							}
							
						}else if(action.equals("XCASEID")){							
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is XCASEID...",100L);
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							String caseId = (String)bio.get("CASEID");
							String storerKey = (String)bio.get("STORERKEY");
							String ohType = (String)bio.get("XORDEROHType");
							String orderKey = (String)bio.get("ORDERKEY");
							String consigneeKey = bio.get("XORDERConsigneeKey")==null?"":(String)bio.get("XORDERConsigneeKey");	
							Query loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.CASEID = '"+caseId.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);									
							if (bioCollection == null || bioCollection.size() < 1) {
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);
								newRecord.set("CASEID",caseId);																																								
								newRecord.set("OHTYPE",ohType);																								
								newRecord.set("STORER",storerKey);																										
								newRecord.set("CUSTOMER",consigneeKey);																			
								newRecord.set("FLOWORDERID",orderKey);	
								newRecord.save();
								_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);
								uowb.saveUOW(true);
								uowb.clearState();
							}
							*/

							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is CASEID or XCASEID...",100L);
							String caseId = (String)bio.get("CASEID");
							//jp.answerlink.275700.begin
							//Check that DropId has other cases besides current
							String dropId = (String)bio.get("DROPID");
							int caseCount = getCaseCount(uowb, dropId, caseId);
							if (caseCount>1){
								WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
								Array parms = new Array();
								//sendDelimiter,ptcid,userid,taskId,databasename,appflag,recordType,server,
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								parms.add(new TextData(""));
								//loadStopId, caseId, dropId
								parms.add(new TextData(loadStopId));
								parms.add(new TextData(caseId));
								parms.add(new TextData(dropId));
								actionProperties.setProcedureParameters(parms);
								actionProperties.setProcedureName("NSPRFAIL11");

								try {
									EXEDataObject resultDO = WmsWebuiActionsImpl.doAction(actionProperties);
									
									if(resultDO!=null && resultDO.getReturnCode()>0) {
										String errorMsg = resultDO.getAttribValue(new TextData("message")).toString();
										throw new UserException(errorMsg, new Object[]{});
									}
								}  catch (WebuiException e) {
									e.getMessage();
									UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
						 	   		throw UsrExcp;
								}
								parms = null;

							}else{
								//jp.answerlink.275700.end

								Query loadBiosQry = new Query("wm_loadunitdetail", "wm_loadunitdetail.UNITID = '"+caseId.toUpperCase()+"' AND wm_loadunitdetail.LOADSTOPID = '"+loadStopId.toUpperCase()+"'", "");
								UnitOfWorkBean uow = context.getState().getTempUnitOfWork();
								BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);

								if (bioCollection == null || bioCollection.size() < 1) {
									WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
									Array parms = new Array();
									//pass the following parameters to call backend's procedure - "NSPRFAIL10"
									//sendDelimiter,ptcid,userid,taskId,databasename,appflag,recordType,server,
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									//route,stop,date,continue,
									parms.add(new TextData("CallFromLoadGUI"));
									parms.add(new TextData(loadStopId));
									parms.add(new TextData(""));
									parms.add(new TextData(""));

									//arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9,arg10,arg11,arg12,arg13,arg14,arg15,arg16,arg17,arg18,arg19,arg20,arg21,arg22,arg23,arg24,arg25,arg26,arg27,arg28,arg29,arg30
									parms.add(new TextData(caseId));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));

									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));

									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));
									parms.add(new TextData(""));

									actionProperties.setProcedureParameters(parms);
									actionProperties.setProcedureName("NSPRFAIL10");

									try {
										WmsWebuiActionsImpl.doAction(actionProperties);
									}  catch (WebuiException e) {
										// TODO Auto-generated catch block
										e.getMessage();
										UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
										throw UsrExcp;
									}
									parms = null;
									_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);
								}
							}
							//06/25/2010 FW:  Changed logic to call backend's AddIDToLoad.java instead of adding new record from GUI (Incident3832796_Defect274316) -- End 
						}else if(action.equals("SHIPMENTORDERS")){							
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is SHIPMENTORDERS...",100L);
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							String caseId = "";
							String storerKey = (String)bio.get("STORERKEY");
							String ohType = (String)bio.get("OHTYPE");
							String orderKey = (String)bio.get("ORDERKEY");
							String type = BioAttributeUtil.getString(bio, "TYPE");
							String consigneeKey = bio.get("CONSIGNEEKEY")==null?"":(String)bio.get("CONSIGNEEKEY");
							Query loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.SHIPMENTORDERID = '"+orderKey.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);									
							if (bioCollection == null || bioCollection.size() < 1) {
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);				
								newRecord.set("CASEID",caseId);																																								
								newRecord.set("OHTYPE",ohType);																								
								newRecord.set("STORER",storerKey);																										
								newRecord.set("CUSTOMER",consigneeKey);																			
								newRecord.set("SHIPMENTORDERID",orderKey);	
								newRecord.set("TYPE", type);
								newRecord.save();
								_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);
								uowb.saveUOW(true);
								uowb.clearState();
							}
						}else if(action.equals("FLOWTHRUORDER")){	
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is FLOWTHRUORDER...",100L);							
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							String storerKey = (String)bio.get("STORERKEY");
							String ohType = (String)bio.get("OHTYPE");
							String orderKey = (String)bio.get("ORDERKEY");
							String consigneeKey = (String)bio.get("CONSIGNEEKEY");
							Query loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.FLOWORDERID = '"+orderKey.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);		
							
							if (bioCollection == null || bioCollection.size() < 1) {
								newRecord.set("STORER",storerKey);																																								
								newRecord.set("OHTYPE",ohType);																								
								newRecord.set("FLOWORDERID",orderKey);																										
								newRecord.set("CUSTOMER",consigneeKey);																								
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);
								newRecord.save();
								_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);
								uowb.saveUOW(true);
								uowb.clearState();
							}
						}else if(action.equals("TRANSSHIP")){							
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is TRANSSHIP...",100L);
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							Object qtyObj = bio.get("QTY");
							String qty = null;
							if(qtyObj != null)
								qty = qtyObj.toString();
							Object weightObj = bio.get("WEIGHT");
							String weight = null;
							if(weightObj != null)
								weight = weightObj.toString();
							Object cubeObj = bio.get("CUBE");
							String cube = null;
							if(cubeObj != null)
								cube = cubeObj.toString();
							String ohType = (String)bio.get("OHTYPE");				        					        	
							String customerKey = (String)bio.get("CUSTOMERKEY");
							String containerId = (String)bio.get("CONTAINERID");
							Query loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.TRANSSHIPCONTAINERID = '"+containerId.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);								
							if (bioCollection == null || bioCollection.size() < 1) {
								newRecord.set("OUTUNITCUBE",cube);																										
								newRecord.set("OUTUNITS",qty);									
								newRecord.set("OHTYPE",ohType);																								
								newRecord.set("OUTUNITWEIGHT",weight);																										
								newRecord.set("CUSTOMER",customerKey);																	
								newRecord.set("TRANSSHIPCONTAINERID",containerId);		
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);
								newRecord.save();
								_log.debug("LOG_SYSTEM_OUT","Saving new record...",100L);
								uowb.saveUOW(true);
								uowb.clearState();
							}
						}else if(action.equals("TRANSSHIPASN")){
							_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Action is TRANSSHIPASN...",100L);							
							QBEBioBean newRecord = uowb.getQBEBioWithDefaults("wm_loadorderdetail");
							String externTranAsnKey = (String)bio.get("EXTERNTRANASNKEY");
							Object unitsObj = bio.get("UNITS");
							String units = null;
							if(unitsObj != null)
								units = unitsObj.toString();
							Object weightObj = bio.get("WEIGHT");
							String weight = null;
							if(weightObj != null)
								weight = weightObj.toString();
							Object cubeObj = bio.get("CUBE");
							String cube = null;
							if(cubeObj != null)
								cube = cubeObj.toString();
							String ohType = (String)bio.get("OHTYPE");				        	
							String customerKey = (String)bio.get("CUSTOMERKEY");
							String containerId = (String)bio.get("PALLETID");
							String transasnkey = (String)bio.get("TRANSASNKEY");						
							Query loadBiosQry = null;
							if(containerId == null){
								loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.TRANSASNKEY = '"+transasnkey.toUpperCase()+"' ", "");							
							}else{
								loadBiosQry = new Query("wm_loadorderdetail", "wm_loadorderdetail.TRANSASNKEY = '"+transasnkey.toUpperCase()+"' AND wm_loadorderdetail.TRANSSHIPCONTAINERID = '"+containerId.toUpperCase()+"'", "");							
							}				      						
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);									
							if (bioCollection == null || bioCollection.size() < 1) {																				
								newRecord.set("OUTUNITCUBE",cube);																										
								newRecord.set("EXTERNALORDERID",externTranAsnKey);									
								newRecord.set("OHTYPE",ohType);																								
								newRecord.set("OUTUNITWEIGHT",weight);																										
								newRecord.set("CUSTOMER",customerKey);																	
								newRecord.set("TRANSSHIPCONTAINERID",containerId);																		
								newRecord.set("OUTUNITS",units);
								newRecord.set("TRANSASNKEY",transasnkey);		
								newRecord.set("LOADORDERDETAILID",loadOrderDetailId);																	
								newRecord.set("LOADSTOPID",loadStopId);
								newRecord.save();
								_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Saving new record...",100L);								
								uowb.saveUOW(true);
								uowb.clearState();
							}
						}
						//if an exception occurs continue saving records...
					} catch (EpiDataException e) {						
						e.printStackTrace();
					} catch (DataBeanException e) {						
						e.printStackTrace();
					} catch (EpiException e) {						
						e.printStackTrace();
					}
				}
				uowb.saveUOW();				
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Exiting AddNewUsingXAction",100L);
		return RET_CONTINUE;
		
	}
	
	private int getCaseCount(UnitOfWorkBean uowb, String dropId, String caseId){
		int caseCount = 1;
		if(dropId!=null && dropId.trim().length()>0){
			String stmt = "wm_pickdetail_lm.DROPID='" + dropId + "' ";
						//" AND wm_pickdetail_lm.CASEID != '" + caseId + "' ";
			
			Query query = new Query("wm_pickdetail_lm",	stmt, null);
			
			BioCollectionBean pickdetailList = uowb.getBioCollectionBean(query);
			
			try {
				if(pickdetailList!=null  && pickdetailList.size()>0){
					caseCount = pickdetailList.size();
					
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION_ADDNEWUSINGXACTION","Error accesing pickdetailList collection",100L);
			}
		}
		return caseCount;
	}

	
}