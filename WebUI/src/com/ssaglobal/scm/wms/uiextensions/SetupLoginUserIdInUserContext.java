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
package com.ssaglobal.scm.wms.uiextensions;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.UserBean;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;


public class SetupLoginUserIdInUserContext extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(SetupLoginUserIdInUserContext.class);

    public SetupLoginUserIdInUserContext() { 
        _log.info("EXP_1","SetupLoginUserIdInHttpRequest Instantiated!!!",  SuggestedCategory.NONE);
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
        EpnyControllerState state = (EpnyControllerState) context.getState();
        HttpSession session = state.getRequest().getSession();
        session.removeValue(SetIntoHttpSessionAction.DB_CONNECTION.toString());
		StateInterface stateInterface = context.getState();
		UserInterface userInterface = stateInterface.getUser();
		UserBean userBean = (UserBean)userInterface;
		String userName = userBean.getUserName();
		if(userName == null || "".equals(userName) ){
			WmsUmsInterface users = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			 userName = users.getCurrentUserId(state);
		}
		context.getServiceManager().getUserContext().put("logInUserId", userName);
		return RET_CONTINUE;
}
}
