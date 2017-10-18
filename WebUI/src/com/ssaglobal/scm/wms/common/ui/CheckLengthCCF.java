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
public class CheckLengthCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckLengthCCF.class);

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
		
		_log.debug("LOG_SYSTEM_OUT","\n\n!@#!@ Start of CheckLengthCCF ",100L);
		String tempLength = formWidget.getProperty("length") != null ? formWidget.getProperty("length").toString() : "4";
		int lengthProperty = 0;
		try
		{
			lengthProperty = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0).parse(tempLength).intValue();
		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		_log.debug("LOG_SYSTEM_OUT","!@# Length " + lengthProperty,100L);

		String value = null;
		try
		{
			value = params.get("fieldValue").toString();
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Empty Value, doing nothing",100L);
			return RET_CONTINUE;
		}

		if (value.matches("\\s*"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Empty Value, doing nothing",100L);
			return RET_CONTINUE;
		}

		value = value.trim();

		if (value.length() > lengthProperty)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# value : " + value + " too long: " + value.length(),100L);
			setErrorMessage(formWidget, " Too many characters in field, must be less than or equal to " + lengthProperty);
			return RET_CANCEL;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Value passes length check",100L);
			setErrorMessage(formWidget, "");
			return RET_CONTINUE;
		}

//		if (value.matches("[\\d\\w]+"))
//		{
//			_log.debug("LOG_SYSTEM_OUT","!@# Value passes length check",100L);
//			setErrorMessage(formWidget, "");
//		}
//		else
//		{
//			_log.debug("LOG_SYSTEM_OUT","!@# invalid characters found",100L);
//			setErrorMessage(formWidget, "Invalid characters in field. Only alphanumeric characters are allowed");
//		}

		//		NumberFormat nf = NumberFormat.getInstance();
		//		double widgetValue;
		//		try
		//		{
		//			widgetValue = nf.parse(value).doubleValue();
		//		} catch (NumberFormatException e)
		//		{
		//			setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
		//			return RET_CANCEL;
		//		} catch (ParseException e)
		//		{
		//			setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
		//			return RET_CANCEL;
		//		}
		//
		//		//Perform Comparisons
		//		double min;
		//		double max;
		//		try
		//		{
		//			min = nf.parse(minValue).doubleValue();
		//			max = nf.parse(maxValue).doubleValue();
		//		} catch (ParseException e)
		//		{
		//			e.printStackTrace();
		//			return RET_CANCEL;
		//		}
		//
		//		if ((widgetValue >= min) && (widgetValue <= max))
		//		{
		//			_log.debug("LOG_SYSTEM_OUT","!! Value within range",100L);
		//		}
		//		else
		//		{
		//			nf.setMaximumFractionDigits(0);
		//			nf.setMaximumIntegerDigits(20);
		//			nf.setGroupingUsed(false);
		//			setErrorMessage(formWidget, "Value must be between " + nf.format(min) + " and " + nf.format(max));
		//			return RET_CANCEL;
		//		}

		

	}
}
