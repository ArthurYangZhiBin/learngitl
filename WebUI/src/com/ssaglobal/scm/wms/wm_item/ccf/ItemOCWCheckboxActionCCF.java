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
import java.util.HashMap;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

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
public class ItemOCWCheckboxActionCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemOCWCheckboxActionCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n@#$@#$Start of ItemOCWCheckboxActionCCF",100L);
		String checkboxName;
		String checkboxValue = null;

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
					_log.debug("LOG_SYSTEM_OUT","\n\n//// Checkbox " + checkboxName + " is EMPTY, Treating as Unchecked",100L);
					checkboxValue = "0";
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n\n//// Checkbox " + checkboxName + " is " + checkboxValue,100L);
				}

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","\n\n//// Checkbox " + checkboxName + " is null, Treating as Unchecked",100L);
				checkboxValue = "0";
			}

			// If Checked - Disable Allow Customer Override and Uncheck. 
			_log.debug("LOG_SYSTEM_OUT","\n//// Performing Actions",100L);
			if ((checkboxValue.equals("0")) || (checkboxValue.equals("N"))) // Unchecked
			{
				_log.debug("LOG_SYSTEM_OUT","//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue	+ " Enabling 'Allow Customer Override' and UnChecking",100L);
				//System.out.println("//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue
				//		+ " Enabling 'Allow Customer Override' and UnChecking");
				RuntimeFormWidgetInterface allowCustomerOverrideWidget = form.getFormWidgetByName("OACOVERRIDE");
				setProperty(allowCustomerOverrideWidget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
				setValue(allowCustomerOverrideWidget, "0");
			}
			else if ((checkboxValue.equals("1")) || (checkboxValue.equals("Y"))) // Checked
			{
				_log.debug("LOG_SYSTEM_OUT","//// Checkbox " + checkboxName + " is Checked, Value: " + checkboxValue + " Disabling 'Allow Customer Override' and Unchecking",100L);
				//System.out.println("//// Checkbox " + checkboxName + " is Checked, Value: " + checkboxValue
				//		+ " Disabling 'Allow Customer Override' and Unchecking");
				RuntimeFormWidgetInterface allowCustomerOverrideWidget = form.getFormWidgetByName("OACOVERRIDE");
				setProperty(allowCustomerOverrideWidget, RuntimeFormWidgetInterface.PROP_READONLY, "true");
				setValue(allowCustomerOverrideWidget, "0");

			}
			else if (checkboxValue == null)
			{
				_log.debug("LOG_SYSTEM_OUT","//// Checkbox " + checkboxName + " is null, Doing Nothing",100L);

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

}
