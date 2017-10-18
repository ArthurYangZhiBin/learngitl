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
package com.ssaglobal.scm.wms.navigation;


import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SetIntoUserContextAction extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetIntoUserContextAction.class);
	   protected int execute(ActionContext context, ActionResult result) throws EpiException {
	        EpnyControllerState state = (EpnyControllerState) context.getState();
	        HttpSession session = state.getRequest().getSession();
	        EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	        initSessionContextData(state);
	        userContext.put(SetIntoHttpSessionAction.DB_CONNECTION, session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION));
	        userContext.put(SetIntoHttpSessionAction.DB_USERID, session.getAttribute(SetIntoHttpSessionAction.DB_USERID));
	        userContext.put(SetIntoHttpSessionAction.DB_PASSWORD, session.getAttribute(SetIntoHttpSessionAction.DB_PASSWORD));
	        userContext.put(SetIntoHttpSessionAction.DB_DATABASE, session.getAttribute(SetIntoHttpSessionAction.DB_DATABASE));
	        userContext.put(SetIntoHttpSessionAction.DB_ISENTERPRISE, session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE));
	        userContext.put(SetIntoHttpSessionAction.DB_TYPE, session.getAttribute(SetIntoHttpSessionAction.DB_TYPE));
	        userContext.put(SetIntoHttpSessionAction.DB_LOCALE, session.getAttribute(SetIntoHttpSessionAction.DB_LOCALE));
	        _log.debug("LOG_SYSTEM_OUT","[DataSource is :" + session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION),100L);
	        _log.debug("LOG_SYSTEM_OUT","[Databse Name is :" + session.getAttribute(SetIntoHttpSessionAction.DB_DATABASE),100L);
	        _log.debug("LOG_SYSTEM_OUT","[UserID is :" + session.getAttribute(SetIntoHttpSessionAction.DB_USERID),100L);
	        _log.debug("LOG_SYSTEM_OUT","[Password is :" + session.getAttribute(SetIntoHttpSessionAction.DB_PASSWORD),100L);
	        _log.debug("LOG_SYSTEM_OUT","[ISENTERPRISE is :" + session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE),100L);
	        _log.debug("LOG_SYSTEM_OUT","[TYPE is :" + session.getAttribute(SetIntoHttpSessionAction.DB_TYPE),100L);
	        _log.debug("LOG_SYSTEM_OUT","[LOCALE is :" + session.getAttribute(SetIntoHttpSessionAction.DB_LOCALE),100L);
	        
	        return RET_CONTINUE;
	    }

	static public void initSessionContextData(EpnyControllerState state) {
		HttpSession session = state.getRequest().getSession();
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION) == null)
		{
			//refresh session
			session.setAttribute(SetIntoHttpSessionAction.DB_CONNECTION, userContext.get(SetIntoHttpSessionAction.DB_CONNECTION));
			session.setAttribute(SetIntoHttpSessionAction.DB_USERID,userContext.get(SetIntoHttpSessionAction.DB_USERID));
			session.setAttribute(SetIntoHttpSessionAction.DB_PASSWORD,userContext.get(SetIntoHttpSessionAction.DB_PASSWORD));
			session.setAttribute(SetIntoHttpSessionAction.DB_DATABASE,userContext.get(SetIntoHttpSessionAction.DB_DATABASE));
			session.setAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE,userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE));
			session.setAttribute(SetIntoHttpSessionAction.DB_SERVER, userContext.get(SetIntoHttpSessionAction.DB_SERVER));
			session.setAttribute(SetIntoHttpSessionAction.DB_TYPE, userContext.get(SetIntoHttpSessionAction.DB_TYPE));
			session.setAttribute(SetIntoHttpSessionAction.DB_LOCALE, userContext.get(SetIntoHttpSessionAction.DB_LOCALE));

		}
		
	}
}

