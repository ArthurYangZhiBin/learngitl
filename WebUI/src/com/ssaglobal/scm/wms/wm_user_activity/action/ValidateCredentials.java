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

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.epiphany.shr.authn.EpnyAuthenticationService;
import com.epiphany.shr.authn.EpnyAuthenticationServiceHome;
import com.epiphany.shr.util.exceptions.EpiSecurityException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.naming.NamingService;

public class ValidateCredentials {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateCredentials.class);

	public static boolean validateUser(String userName, String password) {
		try {
			Context initialCtx = NamingService.getInitialContext();
			Object objRef = initialCtx.lookup("EpnyAuthenticationService");
			EpnyAuthenticationServiceHome home = (EpnyAuthenticationServiceHome) PortableRemoteObject.narrow(	objRef,
																												com.epiphany.shr.authn.EpnyAuthenticationServiceHome.class);
			EpnyAuthenticationService authnService = home.create();
			com.epiphany.shr.sf.util.EpnyUserContext epnyUserCtx = authnService.getCredentials(userName, password, null);
			authnService.remove();
		} catch (EpiSecurityException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_ValidateCredentials_validateUser", e.getMessage(), SuggestedCategory.NONE);
			_log.error(	"LOG_ERROR_EXTENSION_ValidateCredentials_validateUser",
						ExceptionUtils.getFullStackTrace(e),
						SuggestedCategory.NONE);
			return false;
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_ValidateCredentials_validateUser", e2.getMessage(), SuggestedCategory.NONE);
			_log.error(	"LOG_ERROR_EXTENSION_ValidateCredentials_validateUser",
						ExceptionUtils.getFullStackTrace(e2),
						SuggestedCategory.NONE);
			return false;
		}

		return true;
	}

}
