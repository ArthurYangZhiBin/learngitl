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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class LimitFilterRangeToLockedOwners extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitFilterRangeToLockedOwners.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		if(ownerLockFlag == null || ownerLockFlag.intValue() == 0)
			return RET_CONTINUE;
		
		String fromWidgetName = (String)getParameter("FromWidgetName");
		String toWidgetName = (String)getParameter("ToWidgetName");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		String formName = (String)getParameter("FormName");		
		RuntimeFormInterface form = null;
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param fromWidgetName:"+fromWidgetName,100L);
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param toWidgetName:"+toWidgetName,100L);
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param FormName:"+formName,100L);
		
		//Form name is required
		if(formName != null && formName.length() > 0)
			form = (RuntimeFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(), "", formName,tabs , state);
		
		if(form == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","form not found",100L);
			return RET_CANCEL;
		}
	
		
		//If no widget names were passed in then ERROR
		//Bio attribute
		if(fromWidgetName == null || fromWidgetName.length() == 0){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","FromWidgetName is required",100L);
			return RET_CANCEL;
		}
		if(toWidgetName == null || toWidgetName.length() == 0){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","toWidgetName is required",100L);
			return RET_CANCEL;
		}
						
		RuntimeFormWidgetInterface fromWidget = form.getFormWidgetByName(fromWidgetName);
		if(fromWidget == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Widget "+fromWidgetName+" not found",100L);
			return RET_CANCEL;
		}
		RuntimeFormWidgetInterface toWidget = form.getFormWidgetByName(toWidgetName);
		if(fromWidget == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Widget "+toWidgetName+" not found",100L);
			return RET_CANCEL;
		}
//7387-HC.b
		String fromWidgetValue=null;
		String toWidgetValue =null;
		if(!form.isListForm()){			
			if (fromWidget.getValue() == null){
				fromWidgetValue="";
			}else{
				if (fromWidget.getValue().toString().equalsIgnoreCase("")){
					fromWidgetValue = WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState());
				}else{
					fromWidgetValue = fromWidget.getValue().toString();
				}
			}
			
			if (toWidget.getValue() == null){
				toWidgetValue="";
			}else{
				if (toWidget.getValue().toString().equalsIgnoreCase("")){
					toWidgetValue = WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState());
				}else{
					toWidgetValue = toWidget.getValue().toString();
				}
			}
			//7387-HC.e
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got fromWidgetValue:"+fromWidgetValue,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got toWidgetValue:"+toWidgetValue,100L);
		}
		else{
			QBEBioBean filterRow = ((RuntimeListForm)form).getFilterRowBean();	
			fromWidgetValue = (String)filterRow.getValue(fromWidgetName);			
			if(fromWidgetValue == null)
				fromWidgetValue = "";
			toWidgetValue = (String)filterRow.getValue(toWidgetName);			
			if(toWidgetValue == null)
				toWidgetValue = "";
			//7387-HC.e
			String defaultValue = WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState());
			if (fromWidgetValue.equalsIgnoreCase("")){
				fromWidgetValue = defaultValue == null ? "" : defaultValue;
			}
			if (toWidgetValue.equalsIgnoreCase("")){
				toWidgetValue = defaultValue == null ? "" : defaultValue;
			}
			//7387-HC.e
//			filterRow.setValue(fromWidgetName, fromWidgetValue);
//			filterRow.setValue(toWidgetName, toWidgetValue);
		}
//		if (fromWidget.getValue() == null){
//			fromWidgetValue="";
//		}else{
//			if (fromWidget.getValue().toString().equalsIgnoreCase("")){
//				fromWidgetValue = WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState());
//			}else{
//				fromWidgetValue = fromWidget.getValue().toString();
//			}
//		}
//		
//		if (toWidget.getValue() == null){
//			toWidgetValue="";
//		}else{
//			if (toWidget.getValue().toString().equalsIgnoreCase("")){
//				toWidgetValue = WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState());
//			}else{
//				toWidgetValue = toWidget.getValue().toString();
//			}
//		}
//		fromWidgetValue = fromWidget.getValue() == null ? "" : fromWidget.getValue().toString();
//		toWidgetValue = toWidget.getValue() == null ? "" : toWidget.getValue().toString();

		if (fromWidgetValue == null)
			fromWidgetValue = "";
		if (toWidgetValue == null)
			toWidgetValue = "";

		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got fromWidgetValue:"+fromWidgetValue,100L);
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got toWidgetValue:"+toWidgetValue,100L);
		boolean didFail = false;
		
		if("wm_inventory_requestToCount_list_view".equals(formName))
		{
			if("".equals(fromWidgetValue) && "".equals(toWidgetValue))
			{

			}
			else if("".equals(fromWidgetValue) && !"".equals(toWidgetValue))
			{
				if(!WSDefaultsUtil.canUserAccessOwner(toWidgetValue, state))
				{
					didFail = true;
				}
			}
			else if(!"".equals(fromWidgetValue) && "".equals(toWidgetValue))
			{
				if(!WSDefaultsUtil.canUserAccessOwner(fromWidgetValue, state))
				{
					didFail = true;
				}
			}
			else
			{
				if(!WSDefaultsUtil.canUserAccessOwner(fromWidgetValue, state) ||
						!WSDefaultsUtil.canUserAccessOwner(toWidgetValue, state)){			
					didFail = true;
				}
			}
		}
		else
		{

			if("".equals(fromWidgetValue) && "".equals(toWidgetValue))
			{

			}
			else
			{
				if(!WSDefaultsUtil.canUserAccessOwner(fromWidgetValue, state) ||
						!WSDefaultsUtil.canUserAccessOwner(toWidgetValue, state)){			
					didFail = true;
				}
			}
			
			if(!fromWidgetValue.equals(toWidgetValue)){			
				didFail = true;
			}
		}
		
		
		
		
		if(didFail){
			String args[] = new String[3]; 
			ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
			args[0] = "";
			for(int i = 0; i < lockedOwners.size(); i++){
				if(i == 0)
					args[0] += lockedOwners.get(i);
				else
					args[0] += ", " + lockedOwners.get(i);
			}
			args[1] = fromWidget.getLabel("label",state.getUser().getLocale()).replaceAll(":","");
			args[2] = toWidget.getLabel("label",state.getUser().getLocale()).replaceAll(":","");
			String errorMsg = getTextMessage("WMEXP_LIMIT_OWNER_FILTER_RANGE",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		return RET_CONTINUE;	
	}
}
