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

public class OwnerLPNGreaterThanValidation extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerLPNGreaterThanValidation.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","!@# Start of OwnerLPNGreaterThanValidation",100L);
		final String widgetName = (getParameter("WIDGETNAME") != null) ? getParameterString("WIDGETNAME")
				: params.get("formWidgetName").toString();
		final String greaterThanName = getParameterString("GREATERTHAN");

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		//Retrieve Widget Value
		int widgetValue;
		try
		{
			widgetValue = Integer.parseInt(widgetNamesAndValues.get(widgetName).toString().trim());
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve widget value, not set, doing nothing",100L);
			return RET_CONTINUE;
		}

		//Retrieve Greater Value and Perform Comparison
		int greaterThanValue;
		try
		{
			greaterThanValue = Integer.parseInt(widgetNamesAndValues.get(greaterThanName).toString().trim());
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","!@# Unable to retrieve " + greaterThanName + " value, can't perform comparison ",100L);
			return RET_CONTINUE;
		}

		RuntimeFormWidgetInterface errorWidget = form.getFormWidgetByName(widgetName);
		if (widgetValue > greaterThanValue)
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + widgetName + " " + widgetValue + " > " + greaterThanName + " " + greaterThanValue,100L);
			setErrorMessage(errorWidget, "");
			return RET_CONTINUE;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + widgetName + " " + widgetValue + " < " + greaterThanName + " " + greaterThanValue,100L);
			_log.debug("LOG_SYSTEM_OUT","!@# Setting Error",100L);
			RuntimeFormWidgetInterface greaterThanWidget = form.getFormWidgetByName(greaterThanName);
			final String greaterThanLabel = getParameterString("GREATERTHANLABEL");
			setErrorMessage(errorWidget, "Invalid Value, must be greater than " + greaterThanLabel);
			return RET_CANCEL;
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