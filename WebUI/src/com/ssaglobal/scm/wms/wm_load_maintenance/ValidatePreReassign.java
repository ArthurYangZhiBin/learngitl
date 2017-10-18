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
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidatePreReassign extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidatePreReassign.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Executing ValidatePreReassign",100L);		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
		ArrayList tabs = new ArrayList();
		tabs.add("tab 0");
		tabs.add("tab 1");
		tabs.add("tab 2");
		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_detail_form",state);
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_order_detail_list_form",tabs,state);
		RuntimeFormInterface unitsForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_outbound_units_list_form",tabs,state);
		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Header Form:Null",100L);			
		
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Detail Form:Null",100L);			
		
		if(unitsForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Units Form:"+unitsForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Found Units Form:Null",100L);			
		
		if(detailForm == null && unitsForm == null){
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","List forms are null...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Leaving ValidatePreReassign",100L);			
			String args[] = new String[0]; 												
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		ArrayList selectedItems = new ArrayList();
		if(detailForm != null && ((RuntimeListFormInterface)detailForm).getSelectedItems() != null){
			selectedItems = ((RuntimeListFormInterface)detailForm).getSelectedItems();
		}
		if(unitsForm != null && ((RuntimeListFormInterface)unitsForm).getSelectedItems() != null){
			selectedItems.addAll(((RuntimeListFormInterface)unitsForm).getSelectedItems());
		}
		if(selectedItems.size() == 0){			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Nothing Selected...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Leaving ValidatePreReassign",100L);
			String args[] = new String[0]; 												
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		String route = null;
		String loadId = null;		
		
		//validate header form fields		
		if(headerForm != null){						
			Object statusObj = headerForm.getFormWidgetByName("STATUS");
			Object routeObj = headerForm.getFormWidgetByName("ROUTE");
			Object loadIdObj = headerForm.getFormWidgetByName("LOADID");
			
			String status = statusObj == null || ((RuntimeWidget)statusObj).getValue() == null?"":((RuntimeWidget)statusObj).getValue().toString();
			route = routeObj == null || ((RuntimeWidget)routeObj).getValue() == null?"":((RuntimeWidget)routeObj).getValue().toString();
			loadId = loadIdObj == null || ((RuntimeWidget)loadIdObj).getValue() == null?"":((RuntimeWidget)loadIdObj).getValue().toString();
						
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","statusObj:"+statusObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","status widget:"+status,100L);
			//Validate Door, if present must be present in LOC table and Type STAGED
			if(status.length() > 0){								
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Validating STATUS is not 9...",100L);
				if(status.equals("9")){					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","STATUS is 9, cannot reassign...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Leaving ValidatePreReassign",100L);
					String args[] = new String[0]; 												
					String errorMsg = getTextMessage("WMEXP_REASSIGN_STATUS_9",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}				
			}			
		}
		
		//Place values in session for the Reassign popup
		session.setAttribute("REASSIGN_FROM_ROUTE",route);
		session.setAttribute("REASSIGN_FROM_LOAD",loadId);
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPREREASSIGN","Leaving ValidatePreReassign",100L);
		return RET_CONTINUE;
		
	}	
}