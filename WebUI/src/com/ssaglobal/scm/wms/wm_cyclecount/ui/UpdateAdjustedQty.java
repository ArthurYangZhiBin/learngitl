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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Detail - New Record - When user enters a value in the Quantity Counted field,
 * the Adjusted Quantity updates to the differnence of Qty Counted and System
 * Count
 * 
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UpdateAdjustedQty extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UpdateAdjustedQty.class);

	public UpdateAdjustedQty()
	{
		_log.info("EXP_1", "UpdateQtyCounted has been instantiated...", SuggestedCategory.NONE);

	}

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		double qtyCountedValue;
		double adjustedQtyValue;
		double systemCountValue;

		try
		{
			// Get Handle on Form
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}

			// Get Values of Quantity Counted, Adjusted Quantity, System Count
			Object tempQtyCountedValue = null;
			Object tempAdjustedQtyValue = null;
			Object tempSystemCountValue = null;

			tempQtyCountedValue = currentFormFocus.getValue("QTY");
			tempAdjustedQtyValue = currentFormFocus.getValue("ADJQTY");
			tempSystemCountValue = currentFormFocus.getValue("SYSQTY");

			if ((tempSystemCountValue == null) || (tempAdjustedQtyValue == null) || (tempQtyCountedValue == null))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/// Values not set or this is a brand new record, Doing Nothing", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "??? tempQtyCountedValue " + tempQtyCountedValue.getClass().getName(), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "??? tempAdjustedQtyValue "
						+ tempAdjustedQtyValue.getClass().getName(), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "??? tempSystemCountValue "
						+ tempSystemCountValue.getClass().getName(), SuggestedCategory.NONE);
				qtyCountedValue = Double.parseDouble(tempQtyCountedValue.toString());
				adjustedQtyValue = Double.parseDouble(tempAdjustedQtyValue.toString());
				systemCountValue = Double.parseDouble(tempSystemCountValue.toString());
				//System.out.println("/// Qty Counted: " + qtyCountedValue + " System Count: " + systemCountValue
				//		+ " Adjusted Qty: " + adjustedQtyValue);
				_log.debug("LOG_DEBUG_EXTENSION", "/// Qty Counted: " + qtyCountedValue + " System Count: " + systemCountValue	+ " Adjusted Qty: " + adjustedQtyValue, SuggestedCategory.NONE);
			}
			double newAdjustedQtyValue = qtyCountedValue - systemCountValue;
			_log.debug("LOG_DEBUG_EXTENSION", "/// New Adjust Qty = " + newAdjustedQtyValue, SuggestedCategory.NONE);

			// Set new Adjusted Qty Value
			currentFormFocus.setValue("ADJQTY", new Double(newAdjustedQtyValue));

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

}
