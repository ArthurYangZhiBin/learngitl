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
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
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

public class ItemOCDCheckbox extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemOCDCheckbox.class);
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
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{

		
		String checkboxName;
		String checkboxValue = null;
		RuntimeFormInterface form = context.getState().getCurrentRuntimeForm();

		try
		{
			// Get Checkbox Attribute
			RuntimeFormWidgetInterface checkboxWidget = context.getSourceWidget();
			checkboxName = checkboxWidget.getName();
			String tempValue = checkboxWidget.getDisplayValue();
			// Sanitize tempValue

			if (tempValue != null)
			{
				checkboxValue = tempValue.toString();
				if (checkboxValue.equals(""))
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

			// If Checked - Disable Disallow Entry of Total Weight and Check. 
			_log.debug("LOG_DEBUG_EXTENSION", "\n//// Performing Actions", SuggestedCategory.NONE);
			if ((checkboxValue.equals("0")) || (checkboxValue.equals("N"))) // Unchecked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue
						+ " Enabling 'Disallow Entry of Total Weight' and Unchecking", SuggestedCategory.NONE);
				//System.out.println("//// Checkbox " + checkboxName + " is UnChecked, Value: " + checkboxValue
				//		+ " Enabling 'Disallow Entry of Total Weight' and Unchecking");
				RuntimeFormWidgetInterface disallowEntryOfTotalWeightWidget = form.getFormWidgetByName("ODEWEIGHT");
				disallowEntryOfTotalWeightWidget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, "false");
				disallowEntryOfTotalWeightWidget.setDisplayValue("0");
			}
			else if ((checkboxValue.equals("1")) || (checkboxValue.equals("Y"))) // Checked
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is Checked, Value: " + checkboxValue
						+ " Disabling 'Disallow Entry of Total Weight' and Checking", SuggestedCategory.NONE);
				//System.out.println("//// Checkbox " + checkboxName + " is Checked, Value: " + checkboxValue
				//		+ " Disabling 'Disallow Entry of Total Weight' and Checking");
				RuntimeFormWidgetInterface disallowEntryOfTotalWeightWidget = form.getFormWidgetByName("ODEWEIGHT");
				disallowEntryOfTotalWeightWidget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, "true");
				disallowEntryOfTotalWeightWidget.setDisplayValue("1");

			}
			else if (checkboxValue == null)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "//// Checkbox " + checkboxName + " is null, Doing Nothing", SuggestedCategory.NONE);

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

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
