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
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class LimitVendorToDefault extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitVendorToDefault.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		if(ownerLockFlag == null || ownerLockFlag.intValue() == 0)
			return RET_CONTINUE;
		
		ArrayList widgetNames = (ArrayList)getParameter("WidgetNames");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		String formName = (String)getParameter("FormName");		
		boolean allowBlanks = getParameterBoolean("allowBlanks");
		RuntimeNormalForm form = null;
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param WidgetNames:"+widgetNames,100L);
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param FormName:"+formName,100L);
				
		
		//Form name is required
		if(formName != null && formName.length() > 0)
			form = (RuntimeNormalForm)FormUtil.findForm(state.getCurrentRuntimeForm(), "", formName,tabs , state);
		
		if(form == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","form not found",100L);
			return RET_CANCEL;
		}
	
		
		//If no widget names were passed in then limit all widgets associated with a STORERKEY
		//Bio attribute
		if(widgetNames == null)
			widgetNames = new ArrayList();
		if(widgetNames.size() == 0){
			Iterator widgetItr = form.getFormWidgets();
			
			while (widgetItr.hasNext()){
				RuntimeWidget widget = (RuntimeWidget)widgetItr.next();
				if(widget.getAttribute() != null && widget.getAttribute().equalsIgnoreCase("storerkey"))
					widgetNames.add(widget.getName());
			}
		}
		
		
		
		//Check each widget. If its value is something other than the selected Owner pre-filter
		//then display an error message.
		//String ownerDefault = WSDefaultsUtil.getDefaultStorer(state);		
		String failedWidgets = "";
		//Krishna Kuchipudi: Commented the below code for the issue SCM-00000-05555. Now Even Owner is locked, user will be able to update Vendor Record.
		/*
		DataBean focus = form.getFocus();
		for(int i = 0; i < widgetNames.size(); i++){
			String ownerValue = (String)focus.getValue((String)widgetNames.get(i));
			if(ownerValue == null)
				ownerValue = "";
			if(ownerValue.equals("") && allowBlanks)
				continue;
			if(!WSDefaultsUtil.canUserAccessVendor(ownerValue, state)){
				if(failedWidgets.length() != 0)
					failedWidgets +=", ";
					
				failedWidgets += form.getFormWidgetByName((String)widgetNames.get(i)).getLabel("label",state.getUser().getLocale()).replaceAll(":","");			
			}
		}
		
		if(failedWidgets.length() > 0){
			String args[] = new String[2]; 
			ArrayList lockedOwners = WSDefaultsUtil.getLockedVendors(state);
			args[0] = "";
			for(int i = 0; i < lockedOwners.size(); i++){
				if(i == 0)
					args[0] += lockedOwners.get(i);
				else
					args[0] += ", " + lockedOwners.get(i);
			}
			args[1] = failedWidgets;
			String errorMsg = getTextMessage("WMEXP_LIMIT_OWNER_DATA_ENTRY",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} */
		 //		Krishna Kuchipudi: The above code is Commented  the issue SCM-00000-05555
		return RET_CONTINUE;	
	}
}
