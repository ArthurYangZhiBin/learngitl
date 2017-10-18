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
import java.util.Calendar;

import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sso.exception.InvalidSession;
import com.epiphany.shr.sso.exception.SessionExpired;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiSecurityException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.SessionIDUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityRemoveTimeout extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String TIME_CLOCK = "Time Clock";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityRemoveTimeout.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		boolean timeClockUser = false;

		_log.info("Roles", "Roles for Current User", SuggestedCategory.NONE);
		String[] allRolesForCurrentUser = context.getState().getServiceManager().getAllRolesForCurrentUser();
		for (String s : allRolesForCurrentUser) {
			_log.info("Roles", "\t" + s, SuggestedCategory.NONE);
			if (TIME_CLOCK.equals(s)) {
				timeClockUser = true;
			}
		}

		if (timeClockUser == true) {
			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						"Modifying Timeout",
						SuggestedCategory.NONE);
			// modify timeout
			EpnyServiceManager serviceManager = context.getServiceManager();
			EpnyUserContext userContext = serviceManager.getUserContext();
			String ssoToken = (String) userContext.getSecurityHandle();
			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						"Existing Token " + ssoToken,
						SuggestedCategory.NONE);
			SessionIDUtil sessionIDUtil = new SessionIDUtil(context);
			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						sessionIDUtil.ssoTokenInfo(ssoToken),
						SuggestedCategory.NONE);

			Calendar startCal = Calendar.getInstance();

			Calendar endCal = Calendar.getInstance();
			endCal.add(Calendar.DATE, 7);

			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						"Trying to set Start Date " + startCal.getTime(),
						SuggestedCategory.NONE);
			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						"Trying to set End Date " + endCal.getTime(),
						SuggestedCategory.NONE);

			String newToken = ssoToken;
			try {
				newToken = sessionIDUtil.renew(ssoToken, startCal.getTime(), endCal.getTime());
				_log.debug(	"LOG_DEBUG_EXTENSION_UserActivityRemoveTimeout_execute",
							"New Token " + newToken,
							SuggestedCategory.NONE);
				userContext.setSecurityHandle(newToken);
			} catch (InvalidSession e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SessionExpired e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EpiSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			_log.info(	"LOG_INFO_EXTENSION_UserActivityRemoveTimeout_execute",
						sessionIDUtil.ssoTokenInfo(newToken),
						SuggestedCategory.NONE);

			sessionIDUtil.setSessionIDIntoCookie(newToken);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

}
