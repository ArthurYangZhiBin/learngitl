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
package com.ssaglobal.scm.wms.wm_billing_services;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateBillingServices extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateBillingServices.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Executing ValidateBillingServices",100L);			
		StateInterface state = context.getState();					
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_billing_services_detail_view",state);
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Found Detail Form:Null",100L);
		if(detailForm.getFocus().isTempBio()){			
			String serviceKey = detailForm.getFormWidgetByName("SERVICEKEY").getDisplayValue();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Checking for duplicate key:"+serviceKey,100L);			
			Query loadBiosQry = new Query("wm_services", "wm_services.SERVICEKEY = '"+serviceKey.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","key in use...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Exiting ValidateBillingServices",100L);										
					String args[] = {serviceKey}; 
					String errorMsg = getTextMessage("WMEXP_BILLSERVICE_DUP_CODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEBILLINGSERV","Exiting ValidateBillingServices",100L);
		return RET_CONTINUE;
		
	}	
}