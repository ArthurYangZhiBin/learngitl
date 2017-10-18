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
package com.ssaglobal.scm.wms.wm_thru_order;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateThruOrderDelete extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateThruOrderDelete.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ValidateThruOrderDelete",100L);		
		String headerOrDetail = (String)getParameter("headerOrDetail");
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got headerOrDetail:"+headerOrDetail,100L);		
		StateInterface state = context.getState();
		if(headerOrDetail.equalsIgnoreCase("header")){
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_header_list_view",state);
			if(headerListForm != null){
				ArrayList selectedItems = headerListForm.getSelectedItems();
				if(selectedItems == null || selectedItems.size() == 0){
					String args[] = new String[0];							
					String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}else{
					Iterator headerBioBeanIter = selectedItems.iterator();
					String failedDeletes = "";
					BioBean bio;
					
					//iterate thru selected header records and if any related detail records 
					//have processed qty > 0 or shipped qty > 0 then do not allow deletion
					try {
						for(; headerBioBeanIter.hasNext();){            	 
							bio = (BioBean)headerBioBeanIter.next();
							String orderKeyStr = bio.getString("ORDERKEY");	
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got ORDERKEY:"+orderKeyStr,100L);						
							BioCollection detailCollection = bio.getBioCollection("ORDERDETAILS");
							if(detailCollection != null && detailCollection.size() > 0){
								for(int i = 0; i < detailCollection.size(); i++){
									Bio detailBio = detailCollection.elementAt(i);								
									String processedQtyStr = detailBio.getString("PROCESSEDQTY");
									String shippedQtyStr = detailBio.getString("SHIPPEDQTY");
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got PROCESSEDQTY:"+processedQtyStr,100L);
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got SHIPPEDQTY:"+shippedQtyStr,100L);
									double processedQty = 0;
									double shippedQty = 0;
									
									if(processedQtyStr != null && processedQtyStr.length() > 0 ){
										processedQty = Double.parseDouble(processedQtyStr);																	
									}								
									if(shippedQtyStr != null && shippedQtyStr.length() > 0){									
										shippedQty = Double.parseDouble(shippedQtyStr);								
									}
									if(processedQty > 0 || shippedQty > 0){
										if(failedDeletes.length() == 0)
											failedDeletes += orderKeyStr;
										else
											failedDeletes += ","+orderKeyStr;
										break;
									}
								}
							}							
						}
					} catch (EpiDataException e) {							
						e.printStackTrace();
						String args[] = new String[0];							
						String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					} catch (NumberFormatException e) {							
						e.printStackTrace();
						String args[] = new String[0];							
						String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}						
					if(failedDeletes.length() > 0){
						String args[] = new String[1]; 
						args[0] = failedDeletes;							
						String errorMsg = getTextMessage("WMEXP_PROC_SHIP_QTY_GREATER_THAN_0",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}
			else{
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		if(headerOrDetail.equalsIgnoreCase("detail")){
			RuntimeListFormInterface detailListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_detail_list_view",state);
			if(detailListForm != null){
				ArrayList selectedItems = detailListForm.getSelectedItems();
				if(selectedItems != null && selectedItems.size() > 0){
					Iterator bioBeanIter = selectedItems.iterator();
					try
					{
						String failedDeletes = "";
						BioBean bio;							
						for(; bioBeanIter.hasNext();){            	 
							bio = (BioBean)bioBeanIter.next();
							String processedQtyStr = bio.getString("PROCESSEDQTY");
							String shippedQtyStr = bio.getString("SHIPPEDQTY");
							String orderLineNumberStr = bio.getString("ORDERLINENUMBER");								
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got PROCESSEDQTY:"+processedQtyStr,100L);
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got SHIPPEDQTY:"+shippedQtyStr,100L);
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got ORDERLINENUMBER:"+orderLineNumberStr,100L);
							double processedQty = 0;
							double shippedQty = 0;
							
							if(processedQtyStr != null && processedQtyStr.length() > 0 ){
								processedQty = Double.parseDouble(processedQtyStr);																	
							}								
							if(shippedQtyStr != null && shippedQtyStr.length() > 0){									
								shippedQty = Double.parseDouble(shippedQtyStr);								
							}
							if(processedQty > 0 || shippedQty > 0){
								if(failedDeletes.length() == 0)
									failedDeletes += orderLineNumberStr;
								else
									failedDeletes += ","+orderLineNumberStr;
							}
						}
						
						if(failedDeletes.length() > 0){
							String args[] = new String[1]; 
							args[0] = failedDeletes;							
							String errorMsg = getTextMessage("WMEXP_PROC_SHIP_QTY_GREATER_THAN_0",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
					catch(EpiException ex)
					{
						String args[] = new String[0];							
						String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);	
					}
				}
				else{
					String args[] = new String[0];							
					String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			else{
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ValidateThruOrderDelete",100L);		
		return RET_CONTINUE;
		
	}	
}