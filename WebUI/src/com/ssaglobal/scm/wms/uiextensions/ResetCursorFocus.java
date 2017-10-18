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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ResetCursorFocus extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ResetCursorFocus.class);
	public ResetCursorFocus()
	{
	}
	
	protected int execute(ActionContext context, ActionResult result)
	throws UserException
	{			
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Executing ResetCursorFocus",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList otherForms = (ArrayList)getParameter("otherForms");
		String widgetName = (String) getParameter("widget");		
		ArrayList tabNames = (ArrayList) getParameter("tabs");				
		shellForm = shellForm == null || shellForm.length() == 0?"wms_list_shell":shellForm;				
		RuntimeFormInterface targetForm = null;	
		if(targetFormName == null){
			//using current form as targetForm
			targetForm = context.getSourceWidget().getForm();
			_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","using current form as targetForm " + targetForm.getName(),100L);	
			if(widgetName == null)
			{
				//use the current widget as the target
				widgetName = context.getSourceWidget().getName();
				_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","using current widget as target " + widgetName,100L);	
			}
		}
		else {
			if(tabNames == null || tabNames.size() == 0)
				targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,context.getState());
			else
				targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,tabNames,context.getState());
		}
		
		RuntimeFormInterface sourceForm = context.getSourceWidget().getForm();
		for(Iterator it = sourceForm.getFormWidgets(); it.hasNext(); ) {
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)it.next();
			widget.setProperty("cursor focus widget","false");
		}
		
		Iterator widgetItr = targetForm.getFormWidgets();
		if(widgetItr != null){
			while(widgetItr.hasNext()){
				RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
				//_log.debug("LOG_SYSTEM_OUT","\n\nWidget:"+widget.getName()+"  cursor focus widget:"+widget.getProperty("cursor focus widget")+"\n\n",100L);
				widget.setProperty("cursor focus widget","false");
			}
		}
		
		if(otherForms != null && otherForms.size() > 0){			
			for(int i = 0; i < otherForms.size(); i++){
				RuntimeFormInterface otherForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,(String)otherForms.get(i),tabNames,context.getState());
				if(otherForm != null){					
					widgetItr = otherForm.getFormWidgets();
					if(widgetItr != null){
						while(widgetItr.hasNext()){							
							RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();							
							widget.setProperty("cursor focus widget","false");							
						}
					}
				}
			}		
		}
		RuntimeFormWidgetInterface widget = targetForm.getFormWidgetByName(widgetName);		
		widget.setProperty("cursor focus widget","true");		
		return RET_CONTINUE;
	}
	
	
}