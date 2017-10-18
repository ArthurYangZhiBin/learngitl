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

package com.ssaglobal.scm.wms.common.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.util.LocaleUtil;

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
public class CheckMinMaxCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckMinMaxCCF.class);
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n!@#!@ Start of CheckMinMaxCCF",100L);

		String minValue = formWidget.getProperty("MinValue") != null ? formWidget.getProperty("MinValue").toString() : "0";
		String maxValue = formWidget.getProperty("MaxValue") != null ? formWidget.getProperty("MaxValue").toString()
				: "274877906943";
		_log.debug("LOG_SYSTEM_OUT","!@# Min " + minValue,100L);
		_log.debug("LOG_SYSTEM_OUT","!@# Max " + maxValue,100L);

		String value = null;
		try
		{
			value = params.get("fieldValue").toString();
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Empty Value, doing nothing",100L);
			return RET_CONTINUE;
		}
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		double widgetValue;
		try
		{
			widgetValue = nf.parse(value).doubleValue();
		} catch (NumberFormatException e)
		{
			setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		} catch (ParseException e)
		{
			setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
			return RET_CANCEL;
		}

		//Perform Comparisons
		double min;
		double max;
		try
		{
			min = nf.parse(minValue).doubleValue();
			max = nf.parse(maxValue).doubleValue();
		} catch (ParseException e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}

		if ((widgetValue >= min) && (widgetValue <= max))
		{
			_log.debug("LOG_SYSTEM_OUT","!! Value within range",100L);
		}
		else
		{
			nf.setMaximumFractionDigits(0);
			nf.setMaximumIntegerDigits(20);
			nf.setGroupingUsed(false);
			setErrorMessage(formWidget, "Value must be between " + nf.format(min) + " and " + nf.format(max));
			return RET_CANCEL;
		}

		setErrorMessage(formWidget, "");
		return RET_CONTINUE;

	}
}
