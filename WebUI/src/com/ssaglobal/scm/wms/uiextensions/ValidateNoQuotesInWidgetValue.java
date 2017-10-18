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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateNoQuotesInWidgetValue extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateNoQuotesInWidgetValue.class);
	private final static boolean allowSingleQuotes = false;
	private final static boolean allowDoubleQuotes = false; ; //AW 03/31/2009 Bugaware:8735
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Executing ValidateNoQuotesInWidgetValue",100L);		
		if(allowDoubleQuotes)
			_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Ignoring double quotes...",100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Blocking double quotes...",100L);			
		if(allowSingleQuotes)
			_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Ignoring single quotes...",100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Blocking single quotes...",100L);			
		StateInterface state = context.getState();	
		ArrayList tabs = (ArrayList)getParameter("tabs");					//Tabs names needed by the findForm() function to get forms in "formNames" if necessary.
		ArrayList formNames = (ArrayList)getParameter("formNames"); 		//Names of forms containing widgets to validate.
		ArrayList widgetNames = (ArrayList)getParameter("widgetNames");		//Names of widgets to check for quotes	
		String shellForm = (String)getParameter("shellForm");
		
		if(shellForm == null || shellForm.length() == 0)
			shellForm = "wms_list_shell";
				
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Parameter tabs:"+tabs,100L);
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Parameter formNames:"+formNames,100L);
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Parameter widgetNames:"+widgetNames,100L);
		
		//Validate Parameters
		if(formNames == null || formNames.size() == 0){
			throw new InvalidParameterException("formNames cannot be empty");
		}
		if(widgetNames == null || widgetNames.size() == 0){
			throw new InvalidParameterException("widgetNames cannot be empty");
		}
		
		//iterate forms...
		Iterator formItr = formNames.iterator();
		ArrayList badWidgets = new ArrayList();
		while(formItr.hasNext()){
			String formName = (String)formItr.next();
			RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,formName,tabs,state);
			if(form != null){
			//look for widgets on forms...
				for(int i = 0; i < widgetNames.size(); i++){
					String widgetName = (String)widgetNames.get(i);
					RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
					
					//If widget found then validate, else assume widget is on another form...
					if(widget != null){
						String widgetData = widget.getValue() == null?"":(String)widget.getValue();
						if((!allowDoubleQuotes && widgetData.indexOf("\"") != -1) ||
								(!allowSingleQuotes && widgetData.indexOf("'") != -1)){							
							_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Quotes Found In Widget:"+widgetName,100L);
							badWidgets.add(widget.getLabel("label",context.getState().getUser().getLocale()).replaceAll(":",""));							
						}
					}
				}
			}
		}
		
		if(badWidgets.size() > 0){
			String widgetLabelStr = "";
			for(int i = 0; i < badWidgets.size(); i++){
				String args[] = new String[0];
				String widgetLabel = getTextMessage((String)badWidgets.get(i),args,state.getLocale());
				if(widgetLabelStr.length() > 0)
					widgetLabelStr += ", "+widgetLabel;
				else
					widgetLabelStr += widgetLabel;
			}
			_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Leaving ValidateNoQuotesInWidgetValue",100L);			
			String args[] = new String[1]; 						
			args[0] = widgetLabelStr;
			String errorMsg = getTextMessage("WMEXP_NO_QUOTES_ALLOWED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","No Quotes Found...",100L);
		_log.debug("LOG_DEBUG_EXTENSION_VNQIW","Leaving ValidateNoQuotesInWidgetValue",100L);
		return RET_CONTINUE;
		
	}	
}