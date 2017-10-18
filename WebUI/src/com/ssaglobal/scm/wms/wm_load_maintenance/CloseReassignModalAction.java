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
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class CloseReassignModalAction extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CloseReassignModalAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Executing CloseReassignModalAction",100L);		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
		
		//Get Modal Forms
		RuntimeFormInterface modalForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_reassign_popup_body",state);		
		
		if(modalForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Found Modal Form:"+modalForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Found Modal Form:Null",100L);						
		
		//validate modal form fields		
		if(modalForm != null){						
			Object toRouteObj = modalForm.getFormWidgetByName("TOROUTE");
			Object toLoadIdObj = modalForm.getFormWidgetByName("TOLOADID");
			Object toStopObj = modalForm.getFormWidgetByName("TOSTOP");
			
			String toRoute = toRouteObj == null || ((RuntimeWidget)toRouteObj).getDisplayValue() == null?"":((RuntimeWidget)toRouteObj).getDisplayValue();
			String toLoadId = toLoadIdObj == null || ((RuntimeWidget)toLoadIdObj).getDisplayValue() == null?"":((RuntimeWidget)toLoadIdObj).getDisplayValue();
			String toStop = toStopObj == null || ((RuntimeWidget)toStopObj).getDisplayValue() == null?"":((RuntimeWidget)toStopObj).getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toRouteObj:"+toRouteObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toLoadIdObj:"+toLoadIdObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toStopObj:"+toStopObj,100L);
			
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toRoute:"+toRoute,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toLoadId:"+toLoadId,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","toStop:"+toStop,100L);
			
			//LoadId and Stop are required
			if(toLoadId.length() == 0){				
				_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Required Field \"Load Id\" Is Missing...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Leaving CloseReassignModalAction",100L);				
				String args[] = new String[0]; 												
				String errorMsg = getTextMessage("WMEXP_MISSING_REQ_FIELD_LOADID",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			if(toStop.length() == 0){				
				_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Required Field \"Stop\" Is Missing...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Leaving CloseReassignModalAction",100L);
				String args[] = new String[0]; 												
				String errorMsg = getTextMessage("WMEXP_MISSING_REQ_FIELD_STOP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
//			Place values in session for the Reassign action
			session.setAttribute("REASSIGN_TO_ROUTE",toRoute);
			session.setAttribute("REASSIGN_TO_LOAD",toLoadId);
			session.setAttribute("REASSIGN_TO_STOP",toStop);
		}

		_log.debug("LOG_DEBUG_EXTENSION_CLOSEREASSMODACT","Leaving CloseReassignModalAction",100L);
		return RET_CONTINUE;
		
	}	
}