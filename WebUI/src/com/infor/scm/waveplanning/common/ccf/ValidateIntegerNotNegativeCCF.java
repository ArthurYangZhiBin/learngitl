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

package com.infor.scm.waveplanning.common.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class ValidateIntegerNotNegativeCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateIntegerNotNegativeCCF.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException 
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{		
		_log.debug("LOG_DEBUG_EXTENSION", "Executing ValidateIntegerNotNullCCF...", SuggestedCategory.NONE);
		ArrayList widgets = getParameter("widgets")==null?new ArrayList():(ArrayList)getParameter("widgets");
		Object formName = getParameter("formName");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		HashMap widgetValues = getWidgetsValues(params);
		_log.debug("LOG_DEBUG_EXTENSION", "got parameter widgets:"+widgets, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "got parameter formName:"+formName, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "got parameter tabs:"+tabs, SuggestedCategory.NONE);
		
		//Get form named by "formName" if a form name is given, else default to current form.
		RuntimeFormInterface widgetContainerForm = form;
		if(formName != null && formName.toString().length() > 0){
			_log.debug("LOG_DEBUG_EXTENSION", "Searching for form:"+formName, SuggestedCategory.NONE);
			form = WPFormUtil.findForm(form, "", formName.toString(),tabs, state);
			if(form == null){
				_log.debug("LOG_DEBUG_EXTENSION", "Form not found... exiting!", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION", "No form name passed in... defaulting to form:"+form.getName(), SuggestedCategory.NONE);
		}		
		//Iterate widgets and validate that... if they contain a value, it is a positive integer.
		boolean errorOccured = false;
		if(widgets.size() > 0){			
			NumberFormat nf = NumberFormat.getInstance(state.getLocale().getJavaLocale());
			nf.setGroupingUsed(true);
			Number valueNumber = null;
			for(int i = 0; i < widgets.size(); i++){
				String widgetName = widgets.get(i).toString();
				_log.debug("LOG_DEBUG_EXTENSION", "Getting widget -"+widgetName+"- from form...", SuggestedCategory.NONE);
				RuntimeFormWidgetInterface widget = widgetContainerForm.getFormWidgetByName(widgetName);
				if(widget != null){
					_log.debug("LOG_DEBUG_EXTENSION", "Widget found...", SuggestedCategory.NONE);
					_log.debug("LOG_DEBUG_EXTENSION", "Getting widget value...", SuggestedCategory.NONE);					
					String widgetValue = (String)widgetValues.get(widget);
					_log.debug("LOG_DEBUG_EXTENSION", "Got widget value:"+widgetValue, SuggestedCategory.NONE);					
					if(widgetValue != null && widgetValue.length() > 0){
						try {							
							valueNumber = nf.parse(widgetValue);
							if(Integer.parseInt(valueNumber.toString()) < 0){
								_log.debug("LOG_DEBUG_EXTENSION", "Widget value is negative...", SuggestedCategory.NONE);
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WPEXP_NON_NEGATIVE",args,state.getLocale());
								setErrorMessage(widget, errorMsg);
								errorOccured = true;
							}
						} catch (NumberFormatException e) {
							_log.debug("LOG_DEBUG_EXTENSION", "Widget value is not an integer...", SuggestedCategory.NONE);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WPEXP_NOT_AN_INT",args,state.getLocale());
							setErrorMessage(widget, errorMsg);
							errorOccured = true;
						} catch (ParseException e) {
							_log.debug("LOG_DEBUG_EXTENSION", "Widget value is not an integer...", SuggestedCategory.NONE);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WPEXP_NOT_AN_INT",args,state.getLocale());
							setErrorMessage(widget, errorMsg);
							errorOccured = true;
						}
					}
					else{
						_log.debug("LOG_DEBUG_EXTENSION", "Widget value is blank... no validation done", SuggestedCategory.NONE);
					}
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION", "Widget NOT found...", SuggestedCategory.NONE);
				}
			}
		}
		//If no widgets passed in, default behavior is to validate the widget that triggered this extension
		else{
			NumberFormat nf = NumberFormat.getInstance(state.getLocale().getJavaLocale());
			nf.setGroupingUsed(true);
			Number valueNumber = null;
			_log.debug("LOG_DEBUG_EXTENSION", "No widgets passed in... Validating the widget that triggered this extension", SuggestedCategory.NONE);
			if(formWidget != null){
				_log.debug("LOG_DEBUG_EXTENSION", "Widget found...", SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Getting widget value...", SuggestedCategory.NONE);
				String widgetValue = formWidget.getDisplayValue();
				_log.debug("LOG_DEBUG_EXTENSION", "Got widget value:"+widgetValue, SuggestedCategory.NONE);
				if(widgetValue != null && widgetValue.length() > 0){
					try {
						valueNumber = nf.parse(widgetValue);
						if(Integer.parseInt(valueNumber.toString()) < 0){
							_log.debug("LOG_DEBUG_EXTENSION", "Widget value is negative...", SuggestedCategory.NONE);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WPEXP_NON_NEGATIVE",args,state.getLocale());
							setErrorMessage(formWidget, errorMsg);
							errorOccured = true;
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION", "Widget value is not an integer...", SuggestedCategory.NONE);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WPEXP_NOT_AN_INT",args,state.getLocale());
						setErrorMessage(formWidget, errorMsg);
						errorOccured = true;
					} catch (ParseException e) {
						_log.debug("LOG_DEBUG_EXTENSION", "Widget value is not an integer...", SuggestedCategory.NONE);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WPEXP_NOT_AN_INT",args,state.getLocale());
						setErrorMessage(formWidget, errorMsg);
						errorOccured = true;
					}
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION", "Widget value is blank... no validation done", SuggestedCategory.NONE);
				}
			}
		}
		if(errorOccured)
			return RET_CANCEL;
		else
			return RET_CONTINUE;

	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
