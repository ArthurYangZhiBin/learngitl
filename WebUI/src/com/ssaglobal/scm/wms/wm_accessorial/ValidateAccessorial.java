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
package com.ssaglobal.scm.wms.wm_accessorial;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateAccessorial extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateAccessorial.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Executing ValidateAccessorial",100L);		
		StateInterface state = context.getState();			
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_accessorial_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("wm_thru_order_detail_toggle_view_detail");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_accessorial_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Found Detail Form:Null",100L);			
		if(headerForm != null){
			Object storerObj = headerForm.getFormWidgetByName("STORERKEY");
			Object skuObj = headerForm.getFormWidgetByName("SKU");
			Object accessorialKeyObj = headerForm.getFormWidgetByName("ACCESSORIALKEY");
			Object serviceKeyObj = headerForm.getFormWidgetByName("SERVICEKEY");
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Storer:"+storerObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Sku:"+skuObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","accessorialKeyObj"+accessorialKeyObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","serviceKeyObj"+serviceKeyObj,100L);
			
			//Validate Accessorial Key
			if(headerForm.getFocus().isTempBio()){
				if(accessorialKeyObj != null){				
					String accessorialKey = ((RuntimeWidget)accessorialKeyObj).getDisplayValue();
					if(accessorialKey != null){
						//accessorialKey = accessorialKey.trim();
						if(accessorialKey.length() > 0){
							Query loadBiosQry = new Query("wm_accessorial", "wm_accessorial.ACCESSORIALKEY = '"+accessorialKey.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
							try {
								if(bioCollection.size() > 0){							
									String args[] = new String[1]; 						
									args[0] = accessorialKey;
									String errorMsg = getTextMessage("WMEXP_ACCESSORIALKEY_IN_USE",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (EpiDataException e) {
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}
			}
			
			//Validate Storer
			if(storerObj != null){				
				String storer = ((RuntimeWidget)storerObj).getDisplayValue();
				if(storer != null){
					//storer = storer.trim();
					if(storer.length() > 0){
						Query loadBiosQry = new Query("wm_storer_ac", "wm_storer_ac.STORERKEY = '"+storer.toUpperCase()+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 						
								args[0] = storer;
								String errorMsg = getTextMessage("WMEXP_INVAID_STORER",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
			}
			
			//Validate Item
			if(skuObj != null){
				String sku = ((RuntimeWidget)skuObj).getDisplayValue();
				if(sku != null){
					//sku = sku.trim();
					if(sku.length() > 0){
						Query loadBiosQry = new Query("wm_sku_ac", "wm_sku_ac.SKU = '"+sku.toUpperCase()+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 
								args[0] = sku;						
								String errorMsg = getTextMessage("WMEXP_INVAID_SKU",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						if(storerObj != null){				
							String storer = ((RuntimeWidget)storerObj).getDisplayValue();
							if(storer != null){
								//storer = storer.trim();
								if(storer.length() > 0){
									loadBiosQry = new Query("wm_sku_ac", "wm_sku_ac.SKU = '"+sku+"' AND wm_sku_ac.STORERKEY = '"+storer.toUpperCase()+"'", "");
									uow = context.getState().getDefaultUnitOfWork();									
									bioCollection = uow.getBioCollectionBean(loadBiosQry);
									try {
										if(bioCollection.size() == 0){							
											String args[] = new String[2]; 
											args[0] = sku;
											args[1] = storer;
											String errorMsg = getTextMessage("WMEXP_ITEM_DOES_NOT_BELONG_TO_STORER",args,state.getLocale());
											throw new UserException(errorMsg,new Object[0]);
										}
									} catch (EpiDataException e) {
										e.printStackTrace();
										String args[] = new String[0]; 
										String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
										throw new UserException(errorMsg,new Object[0]);
									}
								}
								else{
									String args[] = new String[1]; 
									args[0] = sku;						
									String errorMsg = getTextMessage("WMEXP_STORER_MUST_BE_PRESENT",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							}else{
								String args[] = new String[1]; 
								args[0] = sku;						
								String errorMsg = getTextMessage("WMEXP_STORER_MUST_BE_PRESENT",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}else{
							String args[] = new String[1]; 
							args[0] = sku;						
							String errorMsg = getTextMessage("WMEXP_STORER_MUST_BE_PRESENT",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
			}
			//Check for violation of unique key STORERKEY,SKU,SERVICEKEY
			String accessorialKey = accessorialKeyObj == null || ((RuntimeWidget)accessorialKeyObj).getDisplayValue() == null?"":((RuntimeWidget)accessorialKeyObj).getDisplayValue();
			String storerKey = storerObj == null || ((RuntimeWidget)storerObj).getDisplayValue() == null?"":((RuntimeWidget)storerObj).getDisplayValue();
			String sku = skuObj == null || ((RuntimeWidget)skuObj).getDisplayValue() == null?"":((RuntimeWidget)skuObj).getDisplayValue();
			String serviceKey = serviceKeyObj == null || ((RuntimeWidget)serviceKeyObj).getValue() == null?"":((RuntimeWidget)serviceKeyObj).getValue().toString();
			if(accessorialKey.length() > 0){
				String qry = "wm_accessorial.ACCESSORIALKEY != '"+accessorialKey.toUpperCase()+"' AND (STORER_CLAUSE) AND (SKU_CLAUSE) AND (SERVICE_CLAUSE)";
				if(storerKey.length() == 0){
					qry = qry.replaceAll("STORER_CLAUSE","wm_accessorial.STORERKEY = '' OR wm_accessorial.STORERKEY IS NULL");
				}
				else{
					qry = qry.replaceAll("STORER_CLAUSE","wm_accessorial.STORERKEY = '"+storerKey.toUpperCase()+"'");
				}
				
				if(sku.length() == 0){
					qry = qry.replaceAll("SKU_CLAUSE","wm_accessorial.SKU = '' OR wm_accessorial.SKU IS NULL");
				}
				else{
					qry = qry.replaceAll("SKU_CLAUSE","wm_accessorial.SKU = '"+sku.toUpperCase()+"'");
				}
				
				if(serviceKey.length() == 0){
					qry = qry.replaceAll("SERVICE_CLAUSE","wm_accessorial.SERVICEKEY = '' OR wm_accessorial.SERVICEKEY IS NULL");
				}
				else{
					qry = qry.replaceAll("SERVICE_CLAUSE","wm_accessorial.SERVICEKEY = '"+serviceKey.toUpperCase()+"'");
				}
				Query loadBiosQry = new Query("wm_accessorial", qry, "");				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);				
				try {					
					if(bioCollection != null && bioCollection.size() > 0){
						String args[] = {bioCollection.elementAt(0).get("ACCESSORIALKEY").toString()}; 
						String errorMsg = getTextMessage("WMEXP_ACCESSORIAL_UNIQUE_KEY_VIOLATION",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEACCESSORIAL","Leaving ValidateAccessorial",100L);		
		return RET_CONTINUE;
		
	}	
}