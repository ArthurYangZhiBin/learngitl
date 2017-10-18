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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class ValidateStopSelected extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateStopSelected.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATESTOPSEL","Executing ValidateStopSelected",100L);		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
		String loadStopId = (String)session.getAttribute("LOADSTOPID");
		if(loadStopId == null || loadStopId.length() == 0){			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATESTOPSEL","Load Stop Id Has Not Been Selected...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATESTOPSEL","Exiting ValidateStopSelected",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_LOAD_STOP_NOT_SEL",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATESTOPSEL","Exiting ValidateStopSelected",100L);
		return RET_CONTINUE;
		
	}	
}