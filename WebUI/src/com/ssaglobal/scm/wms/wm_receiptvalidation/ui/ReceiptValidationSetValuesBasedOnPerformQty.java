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
import java.util.ArrayList;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class ReceiptValidationSetValuesBasedOnPerformQty extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptValidationSetValuesBasedOnPerformQty.class);
	private static String PERFORM_QTY_VALIDATION = "PERFORMQTYVALIDATION";

	private static String OVERAGE_HARD_ERROR = "OVERAGEHARDERROR";

	private static String OVERAGE_HARD_ERROR_PERCENT = "OVERAGEHARDERRORPERCENT";

	private static String OVERAGE_MESSAGE = "OVERAGEMESSAGE";

	private static String OVERAGE_OVERIDE_PERCENT = "OVERAGEOVERIDEPERCENT";

	private static String OVERAGE_OVERRIDE = "OVERAGEOVERRIDE";

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

		//		 Building ArrayList of widget names
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
			RuntimeFormInterface form = state.getCurrentRuntimeForm();
			String performqtyvalidationValue = null;
			DataBean currentFormFocus = form.getFocus();

			Object tempValue;
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}
			else if (currentFormFocus instanceof QBEBioBean)
			{
				currentFormFocus = (QBEBioBean) currentFormFocus;
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
				currentFormFocus.setValue(OVERAGE_MESSAGE, "0");

				currentFormFocus.setValue(OVERAGE_OVERRIDE, "0");

				currentFormFocus.setValue(OVERAGE_HARD_ERROR, "0");

				//jp.answerlink.149565.begin
				//Changed value from NULL to zero
				currentFormFocus.setValue(OVERAGE_OVERIDE_PERCENT, "0");

				currentFormFocus.setValue(OVERAGE_HARD_ERROR_PERCENT, "0");
				//jp.answerlink.149565.end
				
//				form.getFormWidgetByName(OVERAGE_MESSAGE).setDisplayValue("0");
//				form.getFormWidgetByName(OVERAGE_OVERRIDE).setDisplayValue("0");
//				form.getFormWidgetByName(OVERAGE_HARD_ERROR).setDisplayValue("0");
//				form.getFormWidgetByName(OVERAGE_OVERIDE_PERCENT).setDisplayValue("0");
//				form.getFormWidgetByName(OVERAGE_HARD_ERROR_PERCENT).setDisplayValue("0");

			}
			else if (performqtyvalidationValue.equals("1"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/////Value of " + PERFORM_QTY_VALIDATION + ": " + performqtyvalidationValue, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting " + OVERAGE_MESSAGE + " to Yes", SuggestedCategory.NONE);
				currentFormFocus.setValue(OVERAGE_MESSAGE, "1");
				
//				form.getFormWidgetByName(OVERAGE_MESSAGE).setDisplayValue("1");

			}
			else
			{
				throw new EpiException("EXP_INVALID_VALUE", "PERFORMQTYVALIDATION has an invalid value");
			}
			result.setFocus(currentFormFocus);

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
