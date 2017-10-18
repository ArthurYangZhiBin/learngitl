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

import java.util.ArrayList;
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

public class OwnerLPNComparisonValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerLPNComparisonValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","!@# Start of OwnerLPNComparisonValidation",100L);

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		final String lpnStartName = "LPN Start Number";
		final String lpnStart = "LPNSTARTNUMBER";
		RuntimeFormWidgetInterface lpnStartWidget = form.getFormWidgetByName(lpnStart);
		int lpnStartValue = 0;
		boolean lpnStartIsNotNull = true;
		ArrayList lpnStartError = new ArrayList();
		
		final String lpnNextName = "LPN Next Number";
		final String lpnNext = "NEXTLPNNUMBER";
		RuntimeFormWidgetInterface lpnNextWidget = form.getFormWidgetByName(lpnNext);
		int lpnNextValue = 0;
		boolean lpnNextIsNotNull = true;
		ArrayList lpnNextError = new ArrayList();

		final String lpnRollbackName = "LPN Rollback Number";
		final String lpnRollback = "LPNROLLBACKNUMBER";
		RuntimeFormWidgetInterface lpnRollbackWidget = form.getFormWidgetByName(lpnRollback);
		int lpnRollbackValue = 0;
		boolean lpnRollbackIsNotNull = true;
		ArrayList lpnRollBackError = new ArrayList();

		// Retrieve LPN Start
		try
		{
			lpnStartValue = Integer.parseInt(widgetNamesAndValues.get(lpnStart).toString().trim());
			_log.debug("LOG_SYSTEM_OUT","!@# LPN Start " + lpnStartValue,100L);
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve lpnStart value, not set",100L);
			lpnStartIsNotNull = false;
		}

		// Retrieve LPN Next
		try
		{
			lpnNextValue = Integer.parseInt(widgetNamesAndValues.get(lpnNext).toString().trim());
			_log.debug("LOG_SYSTEM_OUT","!@# LPN Next " + lpnNextValue,100L);
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve lpnNext value, not set",100L);
			lpnNextIsNotNull = false;
		}

		// Retrieve LPN Rollback
		try
		{
			lpnRollbackValue = Integer.parseInt(widgetNamesAndValues.get(lpnRollback).toString().trim());
			_log.debug("LOG_SYSTEM_OUT","!@# LPN Rollback " + lpnRollbackValue,100L);
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","Unable to retrieve lpnRollback value, not set",100L);
			lpnRollbackIsNotNull = false;
		}

		

		
		
		
		// LPN Next > Start & < Rollback
		if (lpnNextIsNotNull)
		{
			if (lpnStartIsNotNull)
			{
				lpnNextError.add(greaterThan(lpnNextValue, lpnStartValue, lpnNextWidget, lpnStartName));
			}
			if (lpnRollbackIsNotNull)
			{
				lpnNextError.add(lessThan(lpnNextValue, lpnRollbackValue, lpnNextWidget, lpnRollbackName));
			}

		}

		//LPN Rollback > Next & > Start
		if (lpnRollbackIsNotNull)
		{
			if (lpnStartIsNotNull)
			{
				lpnRollBackError.add(greaterThan(lpnRollbackValue, lpnStartValue, lpnRollbackWidget, lpnStartName));
			}
			if (lpnRollbackIsNotNull)
			{
				lpnRollBackError.add(greaterThan(lpnRollbackValue, lpnNextValue, lpnRollbackWidget, lpnNextName));
			}

		}

		setErrorMessage(lpnNextWidget, flattenError(lpnNextError));
		setErrorMessage(lpnRollbackWidget, flattenError(lpnRollBackError));

		return RET_CONTINUE;

	}

	private String flattenError(ArrayList error)
	{
		StringBuffer errorMessage = new StringBuffer();
		int count = 0;
		for (Iterator it = error.iterator(); it.hasNext();)
		{
			String tempError = it.next().toString();
			_log.debug("LOG_SYSTEM_OUT","\nCount " + count + "\t Error " + tempError,100L);
			if (count >= 1 && !tempError.equals(""))
			{
				errorMessage.append(" and ");
			}
			errorMessage.append(tempError);
			if (!tempError.equals(""))
			{
				count++;
			}

		}
		return errorMessage.toString();

	}

	private String lessThan(int lesserValue, int greaterValue, RuntimeFormWidgetInterface lesserWidget, String greaterWidgetName)
	{
		if (lesserValue <= greaterValue)
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + lesserValue + " < " + greaterValue,100L);
			//setErrorMessage(lesserWidget, "");
			return "";
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + lesserValue + " > " + greaterValue,100L);
			_log.debug("LOG_SYSTEM_OUT","!@# Setting Error - less than " + lesserWidget.getName() + " Should be < " + greaterWidgetName,100L);
			//setErrorMessage(lesserWidget, "Invalid Value, must be less than " + greaterWidgetName);
			return "Must be less than " + greaterWidgetName + " ";
		}
	}

	private String greaterThan(int greaterValue, int lesserValue, RuntimeFormWidgetInterface greaterWidget, String lesserWidgetName)
	{
		if (greaterValue >= lesserValue)
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + greaterValue + " > " + lesserValue,100L);
			//setErrorMessage(greaterWidget, "");
			return "";
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@#  " + greaterValue + " < " + lesserValue,100L);
			_log.debug("LOG_SYSTEM_OUT","!@# Setting Error - greater than " + greaterWidget.getName() + " Should be > " + lesserWidgetName,100L);
			//setErrorMessage(greaterWidget, "Invalid Value, must be greater than " + lesserWidgetName);
			return "Must be greater than " + lesserWidgetName + " ";
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