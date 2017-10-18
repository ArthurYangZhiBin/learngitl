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
package com.ssaglobal.scm.wms.wm_transship_asn;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class TransshipASNVerifyFlgOnChange extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TransshipASNVerifyFlgOnChange.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Executing TransshipASNVerifyFlgOnChange",100L);		
		StateInterface state = context.getState();	
		
		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_detail_view",state);
		if(headerForm == null)
			headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_new_detail_view",state);		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Found Header Form:Null",100L);			
		
		//validate header form fields		
		try {
			if(headerForm != null && !headerForm.getFocus().isTempBio()){
				RuntimeFormWidgetInterface verifyFlagObj = headerForm.getFormWidgetByName("VERIFYFLG");
				_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","verifyFlagObj:"+verifyFlagObj,100L);			
				String verifyFlag = verifyFlagObj == null?"":verifyFlagObj.getValue().toString();
				if(verifyFlag.equals("2")){
					BioCollection details = ((BioBean)headerForm.getFocus()).getBioCollection("TRANSSHIPASNDETAILS");
					if(details != null){
						//For each detail record expectedQty must equal received - over + short 
						for(int i = 0; i < details.size(); i++){
							Bio bio = details.elementAt(i);
							Object receivedObj = bio.get("Received");
							Object overObj = bio.get("OVERQTY");
							Object shortObj = bio.get("SHORTQTY");
							Object expectedObj = bio.get("QTY");
							_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","receivedObj:"+receivedObj,100L);
							_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","overObj:"+overObj,100L);
							_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","shortObj:"+shortObj,100L);
							_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","expectedObj:"+expectedObj,100L);
							Double recieved = null;
							Double over = null;
							Double shortqty = null;
							Double expected = null;
							try {
								recieved = (receivedObj == null || receivedObj.toString().length() == 0)?new Double(0.0):new Double(receivedObj.toString());							
							} catch (NumberFormatException e) {		
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","received is not a valid number",100L);								
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);								
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TRANS_ASN_REC_BAD",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
							try {						
								over = (overObj == null || overObj.toString().length() == 0)?new Double(0.0):new Double(overObj.toString());						
							} catch (NumberFormatException e) {		
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","over is not a valid number",100L);								
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TRANS_ASN_OVER_BAD",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
							try {							
								shortqty = (shortObj == null || shortObj.toString().length() == 0)?new Double(0.0):new Double(shortObj.toString());
							} catch (NumberFormatException e) {	
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","shortqty is not a valid number",100L);								
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TRANS_ASN_SHORT_BAD",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
							try {							
								expected = (expectedObj == null || expectedObj.toString().length() == 0)?new Double(0.0):new Double(expectedObj.toString());
							} catch (NumberFormatException e) {
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","expected is not a valid number",100L);								
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TRANS_ASN_EXPECTED_BAD",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
							if(!(expected.doubleValue() == (recieved.doubleValue() - over.doubleValue() + shortqty.doubleValue()))){								
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Cannot Change Flag",100L);
								context.getState().getDefaultUnitOfWork().clearState();
								_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);																
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TRANS_ASN_QTY_BAL",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}
			}
		} catch (EpiDataException e) {		
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);
			e.printStackTrace();			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 				
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNVRFYFLG","Leaving TransshipASNVerifyFlgOnChange",100L);
		return RET_CONTINUE;
		
	}	
}