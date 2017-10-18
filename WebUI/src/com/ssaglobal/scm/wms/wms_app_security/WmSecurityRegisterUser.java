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


package com.ssaglobal.scm.wms.wms_app_security;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.selfregister.extension.helper.ApplicationUtil;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUID;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



public class WmSecurityRegisterUser extends SaveAction
{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityRegisterUser.class);
	public WmSecurityRegisterUser()
	{
	}
	
	protected int execute(ActionContext context, ActionResult result)
	throws UserException
	{		
		StateInterface state = context.getState();		 		
		RuntimeFormInterface userForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"","dbsso_user_detail_view",state);
		if(userForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Found user Form:"+userForm.getName(),100L);		
		else
			_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Found user Form:Null",100L);			
		try {			
			if(!isUserRegistered(state,userForm.getFocus())){
				_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","User Is Not Registerd Yet",100L);
				DataBean ssoUserRecord = userForm.getFocus();
				_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Got ssoUserRecord:"+ssoUserRecord,100L);
				registerUser(state,ssoUserRecord);

			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","User Is Registerd",100L);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;
	}

	private boolean isUserRegistered(StateInterface state, DataBean focus) throws EpiDataException {
		Query bioQry = new Query("user_data","user_data.user_name = '"+focus.getValue("sso_user_name")+"'",""); 
		return state.getDefaultUnitOfWork().getBioCollectionBean(bioQry).size() > 0;
	}

	private void registerUser(StateInterface state, DataBean ssoUserRecord) throws Exception {
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String defaults[] = getUserDefaults(state, uowb, ssoUserRecord);
        QBEBioBean newUser = uowb.getQBEBioWithDefaults("user_data");
        newUser.setValue("user_name", defaults[0]);
        newUser.setValue("full_name", defaults[1]);
        newUser.setValue("locale_id", defaults[3]);
        newUser.setValue("active_status_lkp", defaults[2]);
        newUser.setValue("email_address", defaults[5]);            
        uowb.saveUOW(true);
		
	}
	private String[] getUserDefaults(StateInterface state, UnitOfWorkBean uowb, DataBean ssoUserRecord)
    throws Exception
{
	_log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Getting user data from User Management screen focus...",100L);
    String user_id = (String)ssoUserRecord.getValue("sso_user_name");
    String full_name = (String)ssoUserRecord.getValue("fully_qualified_id");
    String localeId = null;   
    String email_address = null;
            
    if(localeId == null)
    	localeId = state.getRequest().getLocale().getLanguage();
    if(localeId == null)
    	localeId = state.getUser().getLocale().getName();
    if(localeId == null)
    	localeId = state.getLocale().getName();
    
    GUID activeStatus = new GUID(ApplicationUtil.getLookupIDFromValue("user_data", "active_status_lkp", "active"));
    GUID userType = new GUID(ApplicationUtil.getLookupIDFromValue("user_data", "user_type_lkp", "user_data"));
   
    _log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Got user_id:"+user_id,100L);
    _log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Got full_name:"+full_name,100L);
    _log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Got localeId:"+localeId,100L);
    _log.debug("LOG_DEBUG_EXTENSION_WmSecurityRegisterUser","Got email_address:"+email_address,100L);
    String defaults[] = {
        user_id, full_name, activeStatus.getStringForInsert(), localeId, userType.getStringForInsert(), email_address
    };
    return defaults;
}
}
