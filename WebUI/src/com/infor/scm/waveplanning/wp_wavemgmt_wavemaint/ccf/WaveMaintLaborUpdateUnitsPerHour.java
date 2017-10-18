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
import java.text.ParseException;
import java.util.HashMap;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
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
public class WaveMaintLaborUpdateUnitsPerHour extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintLaborUpdateUnitsPerHour.class);

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

		String callingWidget = formWidget.getName();
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		double rate = getDouble(params.get("fieldValue"));
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		if ("PIECEPICKRATE".equals(callingWidget))
		{
			double totalEa = getDouble(widgetNamesAndValues.get("TOTALEACHES"));
			if (totalEa == 0 || rate == 0)
			{
				setValue(form.getFormWidgetByName("ESTHOURSEA"), "");
			}
			else
			{
				setValue(form.getFormWidgetByName("ESTHOURSEA"), "" + nf.format(totalEa / rate));
			}
		}
		else if ("CASEPICKRATE".equals(callingWidget))
		{
			double totalCs = getDouble(widgetNamesAndValues.get("TOTALCASES"));
			if (totalCs == 0 || rate == 0)
			{
				setValue(form.getFormWidgetByName("ESTHOURSCS"), "");
			}
			else
			{
				setValue(form.getFormWidgetByName("ESTHOURSCS"), "" + nf.format(totalCs / rate));
			}
		}
		else if ("PALLETPICKRATE".equals(callingWidget))
		{
			double totalPl = getDouble(widgetNamesAndValues.get("TOTALPALLETS"));
			if (totalPl == 0 || rate == 0)
			{
				setValue(form.getFormWidgetByName("ESTHOURSPL"), "");
			}
			else
			{
				setValue(form.getFormWidgetByName("ESTHOURSPL"), "" + nf.format(totalPl / rate));
			}
		}

		return RET_CONTINUE;

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
				NumberFormat nf = NumberFormat.getInstance();
				Number parsed = nf.parse(value.toString());
				return parsed.doubleValue();
				//return Double.parseDouble(value.toString());
			} catch (NumberFormatException e)
			{

				return 0;
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
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
}
