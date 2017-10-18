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

package com.ssaglobal.scm.wms.wm_receiptvalidation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ReceiptValidationNoEditBasedOnCheckbox extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptValidationNoEditBasedOnCheckbox.class);

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		String checkboxName = getParameterString("CHECKBOX");
		String checkboxValue;
		String widgetName = getParameterString("LINKED_WIDGET");
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Checkbox: " + checkboxName, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Widget: " + widgetName, SuggestedCategory.NONE);
		try
		{
			StateInterface state = context.getState();
			DataBean currentFormFocus = form.getFocus();

			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = ((BioBean) currentFormFocus);
			}

			//retrieve value of checkbox
			Object tempvalue = currentFormFocus.getValue(checkboxName);
			if (tempvalue != null)
			{
				checkboxValue = tempvalue.toString();
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Checkbox " + checkboxName + " Value " + checkboxValue, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Checkbox " + checkboxName
						+ " not set, defaulting to Unchecked", SuggestedCategory.NONE);
				checkboxValue = "0";
			}

			if (checkboxValue.equals("1")) //Checked
			{
				//				if checkbox, checked, make widget editable
				enableInputOnWidget(widgetName, form);
			}
			else
			//Unchecked
			{
				//				else make widget uneditable
				disableInputOnWidget(widgetName, form);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void disableInputOnWidget(String widgetName, RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Disabling Input on Widget " + widgetName, SuggestedCategory.NONE);
		RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
		widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	}

	private void enableInputOnWidget(String widgetName, RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Enabling Input on Widget " + widgetName, SuggestedCategory.NONE);
		RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
		Object tempValue = widget.getDisplayValue();
		if (tempValue == null)
		{
			widget.setDisplayValue("0");
		}
		widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);

	}
}
