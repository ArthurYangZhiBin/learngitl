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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.logging.*;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.ui.state.StateInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ValidateNoSpace extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateNoSpace.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	
	/**
	 * Modification History:
	 * 03/31/2009	AW	Bugaware: 8735
	 * 					Applying patch created for WMS 902. Removing restriction for all special characters 
	 * 					except single and double quotes. 
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		/*
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Executing ValidateNoSpace", 100L);

		StateInterface state = context.getState();
		ArrayList tabs = (ArrayList) getParameter("tabs"); //Tabs names needed by the findForm() function to get forms in "formNames" if necessary.
		ArrayList formNames = (ArrayList) getParameter("formNames"); //Names of forms containing widgets to validate.
		ArrayList widgetNames = (ArrayList) getParameter("widgetNames"); //Names of widgets to check for quotes	
		String shellForm = (String) getParameter("shellForm");

		if (shellForm == null || shellForm.length() == 0)
			shellForm = "wms_list_shell";

		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Parameter tabs:" + tabs, 100L);
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Parameter formNames:" + formNames, 100L);
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Parameter widgetNames:" + widgetNames, 100L);

		//Validate Parameters
		if (formNames == null || formNames.size() == 0)
		{
			throw new InvalidParameterException("formNames cannot be empty");
		}
		if (widgetNames == null || widgetNames.size() == 0)
		{
			throw new InvalidParameterException("widgetNames cannot be empty");
		}

		//iterate forms...
		Iterator formItr = formNames.iterator();
		ArrayList badWidgets = new ArrayList();
		while (formItr.hasNext())
		{
			String formName = (String) formItr.next();
			RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(), shellForm, formName, tabs,
															state);
			if (form != null)
			{
				_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "++Checking Form " +formName, SuggestedCategory.NONE);
				//look for widgets on forms...
				for (int i = 0; i < widgetNames.size(); i++)
				{
					String widgetName = (String) widgetNames.get(i);
					RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);

					//If widget found then validate, else assume widget is on another form...							
					if (widget != null && (widget.getProperty(widget.PROP_HIDDEN).equals("false") || widget.getProperty(widget.PROP_HIDDEN).toString().equals("0")))
					{
						String widgetData = widget.getValue() == null ? "" : widget.getValue().toString();
						_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", widgetName + " - " + widgetData, SuggestedCategory.NONE);
						if (widgetData.matches("\\s*"))
						{
							_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Blank Widget Found:" + widgetName, 100L);
							badWidgets.add(widget.getLabel("label", context.getState().getUser().getLocale()).replaceAll(
																															":",
																															""));
						}
					}
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "--Unable to find Form " + formName, SuggestedCategory.NONE);
			}
		}

		if (badWidgets.size() > 0)
		{
			String widgetLabelStr = "";
			for (int i = 0; i < badWidgets.size(); i++)
			{
				String args[] = new String[0];
				String widgetLabel = getTextMessage((String) badWidgets.get(i), args, state.getLocale());
				if (widgetLabelStr.length() > 0)
					widgetLabelStr += ", " + widgetLabel;
				else
					widgetLabelStr += widgetLabel;
			}
			_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Leaving ValidateNoSpace", 100L);
			String args[] = new String[1];
			args[0] = widgetLabelStr;
			String errorMsg = getTextMessage("WMEXP_NO_BLANKS_ALLOWED", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "No Blanks Found...", 100L);
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Leaving ValidateNoSpace", 100L);
		*/
		return RET_CONTINUE;
	}

}
