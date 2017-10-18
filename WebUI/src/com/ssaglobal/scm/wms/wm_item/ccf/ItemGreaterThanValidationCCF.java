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

package com.ssaglobal.scm.wms.wm_item.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.wm_item.bio.ItemUOMMappingBio;

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
public class ItemGreaterThanValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	private static final String SERIAL_NUMBER_START = "SERIALNUMBERSTART";

	private static final String SERIAL_NUMBER_NEXT = "SERIALNUMBERNEXT";

	private static final String SERIAL_NUMBER_END = "SERIALNUMBEREND";

	private static final String SERIAL_NUMBER_START_NAME = "Serial Number Start";

	private static final String SERIAL_NUMBER_NEXT_NAME = "Serial Number Next";

	private static final String SERIAL_NUMBER_END_NAME = "Serial Number End";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemGreaterThanValidationCCF.class);
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
		//listParams(params);
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		//compare start to 0
		int zeroResult = greaterThanZero(SERIAL_NUMBER_START, form, widgetNamesAndValues);
		//compare next to start
		int firstGreaterResult = greaterThan(SERIAL_NUMBER_NEXT, SERIAL_NUMBER_START, SERIAL_NUMBER_START_NAME, form, widgetNamesAndValues);
		//compare end to next
		int secondGreaterResult = greaterThan(SERIAL_NUMBER_END, SERIAL_NUMBER_NEXT, SERIAL_NUMBER_NEXT_NAME, form, widgetNamesAndValues);

		return getFinalResult(zeroResult, firstGreaterResult, secondGreaterResult);

	}

	private int getFinalResult(int zeroResult, int firstGreaterResult, int secondGreaterResult)
	{
		if (zeroResult != 0)
		{
			return zeroResult;
		}
		else if (firstGreaterResult != 0)
		{
			return firstGreaterResult;
		}
		else if (secondGreaterResult != 0)
		{
			return secondGreaterResult;
		}
		else
		{
			return RET_CONTINUE;
		}
	}

	private int greaterThan(String greaterWidgetName, String lesserWidgetName, String lesserWidgetLabel, RuntimeFormInterface form, HashMap widgetNamesAndValues)
	{
		// TODO Remove the lesserWidgetLabel parameter
		_log.debug("LOG_SYSTEM_OUT","\n!@# Starting Greater Than Validation\nShould be " + greaterWidgetName + " > " + lesserWidgetName,100L);
		RuntimeFormWidgetInterface greaterWidget = form.getFormWidgetByName(greaterWidgetName);
		RuntimeFormWidgetInterface lesserWidget = form.getFormWidgetByName(lesserWidgetName);

		//retrieve values
		Object tempGreaterValue = widgetNamesAndValues.get(greaterWidgetName);
		Object tempLesserValue = widgetNamesAndValues.get(lesserWidgetName);

		if ((tempGreaterValue == null) || (tempLesserValue == null))
		{
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Values Undeclared, doing Nothing",100L);
			//setErrorMessage(greaterWidget, "");
			//setErrorMessage(lesserWidget, "");
			return RET_CONTINUE;
		}

		NumberFormat nf = NumberFormat.getInstance();
		double greaterValue, lesserValue;
		try
		{
			greaterValue = nf.parse(tempGreaterValue.toString()).doubleValue();
			_log.debug("LOG_SYSTEM_OUT","Greater value" + greaterValue,100L);
		} catch (NumberFormatException e)
		{
			setErrorMessage(greaterWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrorMessage(greaterWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		}
		try
		{
			lesserValue = nf.parse(tempLesserValue.toString()).doubleValue();
			_log.debug("LOG_SYSTEM_OUT","Lesser value" + lesserValue,100L);
		} catch (NumberFormatException e)
		{
			setErrorMessage(lesserWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrorMessage(lesserWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		}
		//compare
		_log.debug("LOG_SYSTEM_OUT","\n!@# Comparing " + greaterValue + " > " + lesserValue,100L);
		if (greaterValue > lesserValue)
		{
			_log.debug("LOG_SYSTEM_OUT","Values Verified",100L);
			setErrorMessage(greaterWidget, "");
			//setErrorMessage(lesserWidget, "");
			return RET_CONTINUE;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","Values not verified",100L);
			//setErrorMessage(greaterWidget, "Should be greater than");
			Object tempLabel = lesserWidget.getProperty(RuntimeFormWidgetInterface.LABEL_LABEL);
//			String lesserWidgetLabel = null;
//			if (tempLabel != null)
//			{
//				lesserWidgetLabel = tempLabel.toString();
//			}

			setErrorMessage(greaterWidget, "Should be greater than " + lesserWidgetLabel);
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

	private int greaterThanZero(String sourceWidgetName, RuntimeFormInterface form, HashMap widgetNamesAndValues)
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n~!@ START OF greaterThanZero VALIDATION for " + sourceWidgetName,100L);

		RuntimeFormWidgetInterface sourceWidget = form.getFormWidgetByName(sourceWidgetName);
		Object tempValue = widgetNamesAndValues.get(sourceWidgetName);

		if ((tempValue == null))
		{
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Values Undeclared, doing Nothing",100L);
			//setErrorMessage(sourceWidget, "");
			return RET_CONTINUE;
		}

		NumberFormat nf = NumberFormat.getInstance();
		try
		{
			double sourceWidgetValue = (nf.parse(tempValue.toString())).doubleValue();

			_log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + sourceWidgetValue + " Value: " + sourceWidgetValue,100L);

			// If value < 0, return RET_CANCEL
			if (sourceWidgetValue < 0)
			{
				_log.debug("LOG_SYSTEM_OUT","//// greaterThanZero Validation Failed",100L);
				setErrorMessage(sourceWidget, "Invalid Value, Must Be Greater Than or Equal to 0");
				return RET_CANCEL;

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","//// greaterThanZero Validation Passed - " + sourceWidget.getName() + ": "	+ sourceWidgetValue,100L);
				//System.out.println("//// greaterThanZero Validation Passed - " + sourceWidget.getName() + ": "
				//		+ sourceWidgetValue);
				setErrorMessage(sourceWidget, "");
				return RET_CONTINUE;
			}
		} catch (NumberFormatException e)
		{
			setErrorMessage(sourceWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrorMessage(sourceWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		}

	}
}
