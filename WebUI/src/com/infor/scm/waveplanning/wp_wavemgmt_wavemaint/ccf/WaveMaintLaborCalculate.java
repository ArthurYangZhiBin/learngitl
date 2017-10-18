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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
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

public class WaveMaintLaborCalculate extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintLaborCalculate.class);

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
		StateInterface state = getCCFActionContext().getState();
		RuntimeFormInterface laborForm = form.getParentForm(state);

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		RuntimeFormWidgetInterface error = laborForm.getFormWidgetByName("ERROR");

		RuntimeFormWidgetInterface hoursEa = laborForm.getFormWidgetByName("HOURSEA");
		RuntimeFormWidgetInterface hoursCs = laborForm.getFormWidgetByName("HOURSCS");
		RuntimeFormWidgetInterface hoursPl = laborForm.getFormWidgetByName("HOURSPL");
		RuntimeFormWidgetInterface hoursT = laborForm.getFormWidgetByName("HOURSTOTAL");

		RuntimeFormWidgetInterface workersEa = laborForm.getFormWidgetByName("WORKERSEA");
		RuntimeFormWidgetInterface workersCs = laborForm.getFormWidgetByName("WORKERSCS");
		RuntimeFormWidgetInterface workersPl = laborForm.getFormWidgetByName("WORKERSPL");
		RuntimeFormWidgetInterface workersT = laborForm.getFormWidgetByName("WORKERSTOTAL");

		double estEa = getDouble(widgetNamesAndValues.get("ESTHOURSEA"));
		double estPl = getDouble(widgetNamesAndValues.get("ESTHOURSPL"));
		double estCs = getDouble(widgetNamesAndValues.get("ESTHOURSCS"));

		//Check that only Workers or only Hours are filled in for each line
		if (!isEmpty(widgetNamesAndValues.get("HOURSEA")) && !isEmpty(widgetNamesAndValues.get("WORKERSEA")))
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALEACHES"), constructErrorMessage(
					"WPEXP_LABOR_WORKERHOUR", new String[] {}));
			return RET_CONTINUE;
		}
		else
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALEACHES"), "");
		}

		if (!isEmpty(widgetNamesAndValues.get("HOURSCS")) && !isEmpty(widgetNamesAndValues.get("WORKERSCS")))
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALCASES"), constructErrorMessage(
					"WPEXP_LABOR_WORKERHOUR", new String[] {}));
			return RET_CONTINUE;
		}
		else
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALCASES"), "");
		}

		if (!isEmpty(widgetNamesAndValues.get("HOURSPL")) && !isEmpty(widgetNamesAndValues.get("WORKERSPL")))
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALPALLETS"), constructErrorMessage(
					"WPEXP_LABOR_WORKERHOUR", new String[] {}));
			return RET_CONTINUE;
		}
		else
		{
			setErrorMessage(laborForm.getFormWidgetByName("TOTALPALLETS"), "");
		}

		//For each line, calculate either the Hour or Worker

		double workersEaVal = 0;
		double hoursEaVal = 0;
		if (isEmpty(widgetNamesAndValues.get("HOURSEA")) && !isEmpty(widgetNamesAndValues.get("WORKERSEA")))
		{
			workersEaVal = getDouble(widgetNamesAndValues.get("WORKERSEA"));
			hoursEaVal = estEa / workersEaVal;
			setValue(hoursEa, "" + nf.format(hoursEaVal));

		}
		if (isEmpty(widgetNamesAndValues.get("WORKERSEA")) && !isEmpty(widgetNamesAndValues.get("HOURSEA")))
		{
			hoursEaVal = getDouble(widgetNamesAndValues.get("HOURSEA"));
			workersEaVal = estEa / hoursEaVal;
			setValue(workersEa, "" + nf.format(workersEaVal));

		}

		double workersCsVal = 0;
		double hoursCsVal = 0;
		if (isEmpty(widgetNamesAndValues.get("HOURSCS")) && !isEmpty(widgetNamesAndValues.get("WORKERSCS")))
		{
			workersCsVal = getDouble(widgetNamesAndValues.get("WORKERSCS"));
			hoursCsVal = estCs / workersCsVal;
			setValue(hoursCs, "" + nf.format(hoursCsVal));

		}
		if (isEmpty(widgetNamesAndValues.get("WORKERSCS")) && !isEmpty(widgetNamesAndValues.get("HOURSCS")))
		{
			hoursCsVal = getDouble(widgetNamesAndValues.get("HOURSCS"));
			workersCsVal = estCs / hoursCsVal;
			setValue(workersCs, "" + nf.format(workersCsVal));

		}

		double workersPlVal = 0;
		double hoursPlVal = 0;
		if (isEmpty(widgetNamesAndValues.get("HOURSPL")) && !isEmpty(widgetNamesAndValues.get("WORKERSPL")))
		{
			workersPlVal = getDouble(widgetNamesAndValues.get("WORKERSPL"));
			hoursPlVal = estPl / workersPlVal;
			setValue(hoursPl, "" + nf.format(hoursPlVal));

		}
		if (isEmpty(widgetNamesAndValues.get("WORKERSPL")) && !isEmpty(widgetNamesAndValues.get("HOURSPL")))
		{
			hoursPlVal = getDouble(widgetNamesAndValues.get("HOURSPL"));
			workersPlVal = estPl / hoursPlVal;
			setValue(workersPl, "" + nf.format(workersPlVal));

		}

		//Sum
		double hoursSum = hoursCsVal + hoursEaVal + hoursPlVal;
		double workersSum = workersCsVal + workersEaVal + workersPlVal;
		setValue(hoursT, "" + nf.format(hoursSum));
		setValue(workersT, "" + nf.format(workersSum));

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;

	}

	public String constructErrorMessage(String errorMessage, String[] parameters)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		String localizedErrorMessage = mda.getLocalizedTextMessage(errorMessage, parameters, locale);

		return localizedErrorMessage;
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

	protected boolean isEmpty(Object attributeValue)
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

	private double getDouble(Object value)
	{

		if (value == null)
		{
			return 0;
		}
		else
		{
			try
			{
				return Double.parseDouble(value.toString());
			} catch (NumberFormatException e)
			{

				return 0;
			}
		}
	}
}
