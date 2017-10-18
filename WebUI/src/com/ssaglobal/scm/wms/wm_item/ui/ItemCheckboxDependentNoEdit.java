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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemCheckboxDependentNoEdit extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemCheckboxDependentNoEdit.class);
	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		
		
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		String checkboxName;
		String checkboxValue = null;
		ArrayList dependentAttributes;
		try
		{
			
			// Get Handle on Form
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();

			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}
			else
			{
				currentFormFocus = (QBEBioBean) currentFormFocus;
			}
			// Get Checkbox Attribute
			checkboxName = getParameterString("CHECKBOX");
			Object tempValue = currentFormFocus.getValue(checkboxName);
			// Sanitize tempValue
			
			if (tempValue != null)
			{
				checkboxValue = tempValue.toString();
				if( checkboxValue.equals(""))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName + " is EMPTY, Treating as Unchecked", SuggestedCategory.NONE);
					checkboxValue = "0";
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName + " is " + checkboxValue, SuggestedCategory.NONE);
				}
				
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName + " is null, Treating as Unchecked", SuggestedCategory.NONE);
				checkboxValue = "0";
			}
			// Get Dependent Attributes
			_log.debug("LOG_DEBUG_EXTENSION", "//// Dependent Attributes", SuggestedCategory.NONE);
			dependentAttributes = (ArrayList) getParameter("DEPENDENTATTR");
			for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Widget" + it.next().toString(), SuggestedCategory.NONE);
			}

			// If Checkbox Unchecked (N), disable input on widgets,
			// else enable input on widgets"
			_log.debug("LOG_DEBUG_EXTENSION", "\n//// Performing Actions", SuggestedCategory.NONE);
			if ((checkboxValue.equals("0")) || (checkboxValue.equals("N"))) // Unchecked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue + " Disabling Widgets", SuggestedCategory.NONE);
				disableInputOnWidgets(form, dependentAttributes);
			}
			else if ((checkboxValue.equals("1")) || (checkboxValue.equals("Y"))) // Checked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is Checked, Value: " + checkboxValue + " Enabling Widgets", SuggestedCategory.NONE);
				enableInputOnWidgets(form, dependentAttributes);
			}
			else if (checkboxValue == null)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is null, Disabling Input on Widgets", SuggestedCategory.NONE);
				disableInputOnWidgets(form, dependentAttributes);
			}
			else
			{
				throw new EpiException("EXP_VALUE", "Invalid Value for " + checkboxName + " : " + checkboxValue);
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void enableInputOnWidgets(RuntimeFormInterface currentForm, ArrayList dependentAttributes)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Marking Widgets WRITABLE", SuggestedCategory.NONE);
		for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(o.toString());
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		}

	}

	private void disableInputOnWidgets(RuntimeFormInterface currentForm, ArrayList dependentAttributes)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Marking Widgets READONLY", SuggestedCategory.NONE);
		for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(o.toString());
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}

}
