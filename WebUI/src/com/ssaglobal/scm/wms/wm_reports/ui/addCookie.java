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
package com.ssaglobal.scm.wms.wm_reports.ui;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_preallocatestrategy.ValidatePreallocateStrategyCode;

public class addCookie extends com.epiphany.shr.ui.action.ActionExtensionBase {


	protected static ILoggerCategory _log = LoggerFactory.getInstance(addCookie.class);

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {


	       try {
//	    	 get user login Id from userContext
	    	   String userLoginId = (String)EpnyServiceManagerServer.getInstance().getUserContext().get("logInUserId");
	    	   _log.debug("LOG_SYSTEM_OUT","**************UserID = "+userLoginId,100L);
	    	   StateInterface state = context.getState();
	    	   HttpServletResponse res = state.getResponse();
	    	   _log.debug("LOG_SYSTEM_OUT","\n\nadding cookie...\n\n",100L);
	    	   Cookie myCookie = new Cookie ( "TRUSTED_SIGNON_USER" , userLoginId);
	    	   myCookie.setPath("/");
	    	   _log.debug("LOG_SYSTEM_OUT","cookie domain:" + SSOManager.getCookieDomain(),100L);
	    	   myCookie.setDomain(SSOManager.getCookieDomain());
	    	   res.addCookie (myCookie);
	    	   
	        } catch(Exception e) {
	            
	            // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	       } 
	       
	       return RET_CONTINUE;
	    }
}

