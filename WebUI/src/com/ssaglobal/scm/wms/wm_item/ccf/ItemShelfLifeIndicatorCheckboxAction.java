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

package com.ssaglobal.scm.wms.wm_item.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class ItemShelfLifeIndicatorCheckboxAction extends
		com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemShelfLifeIndicatorCheckboxAction.class);

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

		String checkboxName;
		String checkboxValue = null;
		ArrayList dependentAttributes;
		try
		{

			// Get Checkbox Attribute
			checkboxName = params.get("formWidgetName").toString();
			Object tempValue = params.get("fieldValue").toString();
			// Sanitize tempValue

			if (tempValue != null)
			{
				checkboxValue = tempValue.toString();
				if (checkboxValue.equals(""))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName
							+ " is EMPTY, Treating as Unchecked", SuggestedCategory.NONE);
					checkboxValue = "0";
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName + " is " + checkboxValue, SuggestedCategory.NONE);
				}

			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Checkbox " + checkboxName
						+ " is null, Treating as Unchecked", SuggestedCategory.NONE);
				checkboxValue = "0";
			}
			dependentAttributes = new ArrayList();
			dependentAttributes.add("SHELFLIFEONRECEIVING");
			dependentAttributes.add("SHELFLIFE");

			// If Checkbox Unchecked (N), disable input on widgets,
			// else enable input on widgets"
			_log.debug("LOG_DEBUG_EXTENSION", "\n//// Performing Actions", SuggestedCategory.NONE);
			if ((checkboxValue.equals("0")) || (checkboxValue.equals("N"))) // Unchecked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue + " Disabling Widgets", SuggestedCategory.NONE);
				//System.out.println("//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue
				//		+ " Disabling Widgets");
				disableInputOnWidgets(form, dependentAttributes);
				setValueToZero(form, dependentAttributes);
			}
			else if ((checkboxValue.equals("1")) || (checkboxValue.equals("Y"))) // Checked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is Checked, Value: "
						+ checkboxValue + " Enabling Widgets", SuggestedCategory.NONE);
				enableInputOnWidgets(form, dependentAttributes);
			}
			else if (checkboxValue == null)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName
						+ " is null, Disabling Input on Widgets", SuggestedCategory.NONE);
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

	private void setValueToZero(RuntimeFormInterface form, ArrayList dependentAttributes)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Setting Inbound/Outbound Shelf Life to 0", SuggestedCategory.NONE);
		for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(o.toString());
			setValue(widget, "0");
		}

	}

	private void enableInputOnWidgets(RuntimeFormInterface currentForm, ArrayList dependentAttributes)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Marking Widgets WRITABLE", SuggestedCategory.NONE);
		for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(o.toString());
			setProperty(widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			//widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
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
			setProperty(widget, RuntimeFormWidgetInterface.PROP_READONLY, "true");
			//widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}
}
