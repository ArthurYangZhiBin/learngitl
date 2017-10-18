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
 


package com.ssaglobal.scm.wms.wm_user_activity.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class UserActivityValidateUser extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {



protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityValidateUser.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
         * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
         * @exception EpiException 
	 */
	@Override
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
		throws EpiException {
		
		HashMap allWidgetsAndValues = getWidgetsValues(params);
		String userName = (String) allWidgetsAndValues.get(form.getFormWidgetByName("USERNAME"));
		String password = params.get("fieldValue").toString();
		_log.info("LOG_INFO_EXTENSION_UserActivityValidateUser_execute", "Validating User", SuggestedCategory.NONE);
		if (ValidateCredentials.validateUser(userName, password) == false) {
			_log.error(	"LOG_ERROR_EXTENSION_UserActivityValidateUser_execute",
						"Validation Failed",
						SuggestedCategory.NONE);
			setValue(formWidget, "");
			setFormErrorMessage(getTextMessage("EXP_LOGIN_FAILED", new Object[] {}, state.getLocale()));
			return RET_CANCEL;
			// clear password
			// show error
			// throw new UserException("EXP_LOGIN_FAILED", new Object[] {});
		} else {
			_log.info(	"LOG_INFO_EXTENSION_UserActivityValidateUser_execute",
						"Validation Passed",
						SuggestedCategory.NONE);
			setFormErrorMessage("");
		}
		
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
			
		return RET_CONTINUE;
	
	}
}
