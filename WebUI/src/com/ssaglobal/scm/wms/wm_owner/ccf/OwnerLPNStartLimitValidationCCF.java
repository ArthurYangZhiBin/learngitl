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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

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
public class OwnerLPNStartLimitValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerLPNStartLimitValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","!@# Start of OwnerLPNStartLimitValidationCCF",100L);
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		int lpnStartValue;
		String lpnStartValueString;
		//		 Retrieve LPN Start
		try
		{
			lpnStartValueString = widgetNamesAndValues.get("LPNSTARTNUMBER").toString().trim();
			lpnStartValue = Integer.parseInt(lpnStartValueString);
			_log.debug("LOG_SYSTEM_OUT","!@# LPN Start " + lpnStartValue,100L);
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve lpnStart value, not set. Can't validate",100L);
			return RET_CANCEL;
		}

		//verify that LPN Start Not Equal to 0 and < All 9's
		if( lpnStartValue == 0 )
		{
			_log.debug("LOG_SYSTEM_OUT","!@# LPN Start cannot be 0",100L);
			setErrorMessage(formWidget, "Value cannot be 0");
			return RET_CANCEL;
		}
		
		
		String allNines = returnAllNines(lpnStartValueString);
		if( lpnStartValueString.equals(allNines))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Fails All 9's validation",100L);
			setErrorMessage(formWidget, "Value must not be all 9's");
			return RET_CANCEL;
		}
		setErrorMessage(formWidget, "");
		return RET_CONTINUE;

	}

	private String returnAllNines(String lpnStartValueString)
	{
		StringBuffer allNines = new StringBuffer();
		for( int i = 0; i < lpnStartValueString.length(); i++)
		{
			allNines.append("9");
		}
		
		return (allNines.toString());
		
	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_SYSTEM_OUT","# " + widgetNames[i] + " " + widgetValues[i],100L);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}

	private void listParams(HashMap params)
	{
		//		List Params
		_log.debug("LOG_SYSTEM_OUT","Listing Paramters-----",100L);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_SYSTEM_OUT",key.toString() + " " + value.toString(),100L);
		}
	}
}
