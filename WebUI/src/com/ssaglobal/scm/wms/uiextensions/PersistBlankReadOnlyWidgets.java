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
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class PersistBlankReadOnlyWidgets extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PersistBlankReadOnlyWidgets.class);	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
				
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
		while(formItr.hasNext()){
			String formName = (String)formItr.next();
			RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,formName,tabs,state);			
			//NOTE:Not creating logic for list forms			
			if(form != null && !form.isListForm()){
			//look for widgets on forms...
				for(int i = 0; i < widgetNames.size(); i++){					
					String widgetName = (String)widgetNames.get(i);
					RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
					_log.debug("LOG_SYSTEM_OUT","widget:"+widget,100L);
					_log.debug("LOG_SYSTEM_OUT","widget.getProperty(RuntimeFormWidgetInterface.PROP_READONLY):"+widget.getProperty(RuntimeFormWidgetInterface.PROP_READONLY),100L);
					//If widget found then put value in bio
					if(widget != null && widget.getProperty(RuntimeFormWidgetInterface.PROP_READONLY).toString().equals("1")){
						_log.debug("LOG_SYSTEM_OUT","\n\nSetting Value\n\n",100L);
						Object widgetData = widget.getValue();
						DataBean focus = form.getFocus();
						String widgetDispVal = widget.getDisplayValue();
						focus.setValue(widget.getAttribute(),null);						
//						if(widgetData instanceof String){						
//							_log.debug("LOG_SYSTEM_OUT","\n\nSetting "+widget.getAttribute()+" To "+widgetDispVal+"\n\n",100L);
//							focus.setValue(widget.getAttribute(),widgetDispVal);
//						}
//						else if(widgetData instanceof Integer){
//							focus.setValue(widget.getAttribute(),new Integer(widgetDispVal));
//						}
//						else if(widgetData instanceof BigDecimal){
//							focus.setValue(widget.getAttribute(),new BigDecimal(widgetDispVal));
//						}
//						else if(widgetData instanceof GregorianCalendar){
//							SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
//							try {
//								focus.setValue(widget.getAttribute(),dateFormat.format(java.text.DateFormat.getInstance().parse(widgetDispVal)));
//							} catch (ParseException e) {								
//								e.printStackTrace();						
//							}							
//						}
					}
				}
			}
		}							
		return RET_CONTINUE;
		
	}	
}