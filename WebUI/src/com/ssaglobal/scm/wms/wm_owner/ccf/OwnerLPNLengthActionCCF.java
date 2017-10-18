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
package com.ssaglobal.scm.wms.wm_owner.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
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
public class OwnerLPNLengthActionCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerLPNLengthActionCCF.class);

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
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		//retrieve LPN Length
		Object tempValue = params.get("fieldValue");
		if (tempValue == null)
		{
			return RET_CANCEL;
		}
		int lpnLengthValue = Integer.parseInt(tempValue.toString());

		NumberFormat lpnNF = NumberFormat.getIntegerInstance();
		lpnNF.setMinimumIntegerDigits(lpnLengthValue);
		lpnNF.setMaximumIntegerDigits(lpnLengthValue);
		lpnNF.setGroupingUsed(false);
		//set LPN Start
		String formattedLpnStart = lpnNF.format(1);
		setValue(form.getFormWidgetByName("LPNSTARTNUMBER"), formattedLpnStart);

		//set LPN Next
		//retrieve current LPN Next
		//if null, set to 1
		//then format
		int lpnNextNumber;
		try
		{
			lpnNextNumber = Integer.parseInt(widgetNamesAndValues.get("NEXTLPNNUMBER").toString());
		} catch (NullPointerException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Null value for lpn next, setting to 1", SuggestedCategory.NONE);
			lpnNextNumber = 1;
		}
		try
		{
			String formattedLpnNext = lpnNF.format(lpnNextNumber);
			setValue(form.getFormWidgetByName("NEXTLPNNUMBER"), formattedLpnNext);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		//set LPN Rollback
		StringBuffer formattedLpnRollback = new StringBuffer();
		for (int i = 0; i < lpnLengthValue; i++)
		{
			formattedLpnRollback.append("9");
		}
		setValue(form.getFormWidgetByName("LPNROLLBACKNUMBER"), formattedLpnRollback.toString());
		return RET_CONTINUE;

	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
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
}
