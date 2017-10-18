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
import java.util.ArrayList;
import java.util.Iterator;
// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
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
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ReceiptValidationNoEditBasedOnPerformQty extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	private static String PERFORM_QTY_VALIDATION = "PERFORMQTYVALIDATION";

	private static String OVERAGE_HARD_ERROR = "OVERAGEHARDERROR";

	private static String OVERAGE_HARD_ERROR_PERCENT = "OVERAGEHARDERRORPERCENT";

	private static String OVERAGE_MESSAGE = "OVERAGEMESSAGE";

	private static String OVERAGE_OVERIDE_PERCENT = "OVERAGEOVERIDEPERCENT";

	private static String OVERAGE_OVERRIDE = "OVERAGEOVERRIDE";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptValidationNoEditBasedOnPerformQty.class);

	public ReceiptValidationNoEditBasedOnPerformQty()
	{
		_log.info("EXP_1", "OverageNoEditBasedOnPerformQty has been instantiated...", SuggestedCategory.NONE);

	}

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
		_log.info("EXP_1", "Entering preRenderForm", SuggestedCategory.NONE);
		// Building ArrayList of widget names
		ArrayList overageWidgets = new ArrayList();
		overageWidgets.add(OVERAGE_HARD_ERROR);
		//overageWidgets.add(OVERAGE_HARD_ERROR_PERCENT);
		overageWidgets.add(OVERAGE_MESSAGE);
		//overageWidgets.add(OVERAGE_OVERIDE_PERCENT);
		overageWidgets.add(OVERAGE_OVERRIDE);

		try
		{
			// Get Handle on Form
			StateInterface state = context.getState();
			String performqtyvalidationValue = null;
			DataBean currentFormFocus = form.getFocus();

			Object tempValue;
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = ((BioBean) currentFormFocus);
			}

			tempValue = currentFormFocus.getValue(PERFORM_QTY_VALIDATION);

			if (tempValue != null)
			{
				performqtyvalidationValue = tempValue.toString();
			}
			else
			{
				// PERFORMQTYVALIDATION not set, must be a New record or Yes/No
				// was not selected in the dropdown
				// Behaving as "No"
				_log.debug("LOG_DEBUG_EXTENSION", "//////Setting value to No, PERFORMQTYVALIDATION not set", SuggestedCategory.NONE);
				performqtyvalidationValue = "0";
				// System.out.println("PERFORMQTYVALIDATION not set,
				// returning");
				// return RET_CANCEL;

			}

			// value of PERFORMQTYVALIDATION, 1 = Yes 0 = No
			if (performqtyvalidationValue.equals("0"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/////Value of " + PERFORM_QTY_VALIDATION + ": " + performqtyvalidationValue, SuggestedCategory.NONE);
				disableInputOnWidgets(form, overageWidgets);
				/*				
				 currentFormFocus.setValue(OVERAGE_MESSAGE, "0");
				 currentFormFocus.setValue(OVERAGE_OVERRIDE, "0");
				 currentFormFocus.setValue(OVERAGE_HARD_ERROR, "0");
				 currentFormFocus.setValue(OVERAGE_OVERIDE_PERCENT, null);
				 //form.getFormWidgetByName(OVERAGE_OVERIDE_PERCENT).setDisplayValue(" ");
				 currentFormFocus.setValue(OVERAGE_HARD_ERROR_PERCENT, null);
				 //form.getFormWidgetByName(OVERAGE_HARD_ERROR_PERCENT).setDisplayValue(" ");
				 */
			}
			else if (performqtyvalidationValue.equals("1"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/////Value of " + PERFORM_QTY_VALIDATION + ": " + performqtyvalidationValue, SuggestedCategory.NONE);
				enableInputOnWidgets(form, overageWidgets);
			}
			else
			{
				throw new EpiException("EXP_INVALID_VALUE", "PERFORMQTYVALIDATION has an invalid value");
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void enableInputOnWidgets(RuntimeNormalFormInterface form, ArrayList overageWidgets)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "///// Marking Widgets WRITABLE", SuggestedCategory.NONE);
		for (Iterator it = overageWidgets.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(o.toString());
			widget.setBooleanProperty(widget.PROP_READONLY, false);
		}

	}

	private void disableInputOnWidgets(RuntimeNormalFormInterface form, ArrayList overageWidgets)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "///// Marking Widgets READONLY", SuggestedCategory.NONE);
		for (Iterator it = overageWidgets.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(o.toString());
			widget.setBooleanProperty(widget.PROP_READONLY, true);
		}
	}
}
