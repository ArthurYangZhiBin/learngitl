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

package com.ssaglobal.scm.wms.wm_cyclecount.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
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
public class UpdateAdjustedQtyCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UpdateAdjustedQtyCCF.class);

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
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		//retrieve QTY, SYSQTY, ADJQTY
		Object tempQty = widgetNamesAndValues.get("QTY");
		Object tempSysQty = widgetNamesAndValues.get("SYSQTY");
		Object tempAdjQty = widgetNamesAndValues.get("ADJQTY");
		if (isEmpty(tempQty) || isEmpty(tempSysQty))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Either QTY, SYSQTY is null, doing nothing", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}

		double qty = Double.parseDouble(tempQty.toString());
		double sysQty = Double.parseDouble(tempSysQty.toString());
		double adjQty = Double.parseDouble(tempAdjQty.toString());

		double newAdjustedQtyValue = qty - sysQty;
		_log.debug("LOG_DEBUG_EXTENSION", "/// New Adjust Qty = " + newAdjustedQtyValue, SuggestedCategory.NONE);
		if (newAdjustedQtyValue < 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Cannot take ADJQTY below 0", SuggestedCategory.NONE);
		}
		setValue(form.getFormWidgetByName("ADJQTY"), String.valueOf(newAdjustedQtyValue));

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;

	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}

	private void listParams(HashMap params)
	{
		//		List Params
		_log.debug("LOG_DEBUG_EXTENSION", "Listing Paramters-----", SuggestedCategory.NONE);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_DEBUG_EXTENSION", key.toString() + " " + value.toString(), SuggestedCategory.NONE);
		}
	}

	private boolean isNull(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException
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
