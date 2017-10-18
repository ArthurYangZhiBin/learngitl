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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

import java.lang.Integer;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;

public class OwnerLPNLengthValidation extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerLPNLengthValidation.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","!@# Start of OwnerLPNLengthValidation",100L);
		final String widgetName = (getParameter("WIDGETNAME") != null) ? getParameterString("WIDGETNAME")
				: params.get("formWidgetName").toString();
		_log.debug("LOG_SYSTEM_OUT","! on " + widgetName ,100L);

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		int lpnLengthValue = Integer.parseInt(widgetNamesAndValues.get("LPNLENGTH").toString());
		try
		{
			int widgetValueLength = widgetNamesAndValues.get(widgetName).toString().trim().length();

			RuntimeFormWidgetInterface errorWidget = form.getFormWidgetByName(widgetName);
			
			if (lpnLengthValue != widgetValueLength)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# Setting Error message, invalid length",100L);
				setErrorMessage(errorWidget, "Invalid Value, Length must be " + lpnLengthValue + " digits long");
				return RET_CONTINUE; //Process other values
			}
			else
			{
				setErrorMessage(errorWidget, "");
				return RET_CONTINUE;
			}
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve widget value, not set, doing nothing",100L);
			return RET_CONTINUE;
		}

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
}