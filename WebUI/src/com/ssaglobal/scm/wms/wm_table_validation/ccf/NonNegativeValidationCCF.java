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

package com.ssaglobal.scm.wms.wm_table_validation.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

import com.ssaglobal.scm.wms.util.CCFUtil;
import com.ssaglobal.scm.wms.wm_table_validation.bio.TableValidation;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;

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
 * not a CCFSendAllWidgetsValuesExtension extension, SendAllWidgetsValuesExtensionBase
 */
public class NonNegativeValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(NonNegativeValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{
		listParams(params);
		System.out.print("\n\n!@# Start of NonNegativeValidationCCF for ");
		String tempValue = null;
		try
		{
			tempValue = params.get("fieldValue").toString();
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Exception caught, value blank, doing nothing",100L);
			return RET_CANCEL;
		}
		System.out.print(formWidget.getName() + ": " + tempValue + "\n");
		
		if (!isEmpty(tempValue) && (NumericValidationCCF.isNumber(tempValue.toString()) == false))
		{
			//throw exception
			setErrorMessage(formWidget, CCFUtil.constructErrorMessage("EXP_NUMBER_PARSE", new String[] {}));
			return RET_CANCEL;
		}

		//if it passes numericValidation, you can parse the number
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		if (Double.isNaN(value))
		{
			//throw exception
			setErrorMessage(formWidget, CCFUtil.constructErrorMessage("EXP_NUMBER_PARSE", new String[] {}));
			return RET_CANCEL;
		}
		else if (value < 0)
		{
			setErrorMessage(formWidget, CCFUtil.constructErrorMessage("NONNEGATIVE_VALIDATION", new String[] {}));
			return RET_CANCEL;
		}
		setErrorMessage(formWidget, "");
		return RET_CONTINUE;

		/*
		 double widgetValue = 0;

		 try
		 {
		 NumberFormat nf = NumberFormat.getInstance();
		 widgetValue = nf.parse(tempValue.toString()).doubleValue();

		 } catch (ParseException e)
		 {
		 _log.debug("LOG_SYSTEM_OUT","!@# Exception Caught, trying to parse as Currency",100L);
		 NumberFormat nfc = NumberFormat.getCurrencyInstance();
		 try
		 {
		 widgetValue = nfc.parse(tempValue.toString()).doubleValue();
		 } catch (ParseException e1)
		 {
		 String[] parameters = prepareParameters(tempValue);
		 throw new EpiException("WMEXP_NUMBER_PARSE", "EXP_NUMBER_PARSE", parameters);
		 //setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
		 //return RET_CANCEL;
		 }
		 } catch (NumberFormatException e)
		 {
		 String[] parameters = prepareParameters(tempValue);
		 throw new EpiException("WMEXP_NUMBER_PARSE", "EXP_NUMBER_PARSE", parameters);
		 //setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
		 //return RET_CANCEL;
		 }
		 //^\$?[-+]?\$?[,\d]*.?\d+$
		 //(\$?[-+]?|[-+]?\$?)[,\d]*\.?[0-9]*

		 if (tempValue.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*"))
		 {
		 _log.debug("LOG_SYSTEM_OUT","!@@ Value passed by regex",100L);
		 }
		 else
		 {
		 _log.debug("LOG_SYSTEM_OUT","!@@ Throwing Exception by regex",100L);
		 String[] parameters = prepareParameters(tempValue);
		 throw new EpiException("WMEXP_NUMBER_PARSE", "EXP_NUMBER_PARSE", parameters);
		 //			setErrorMessage(formWidget, "Invalid characters in numeric field. Only numbers are allowed in a numeric field");
		 //			return RET_CANCEL;
		 }

		 _log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + formWidget.getName() + " Value: " + widgetValue,100L);

		 // If value < 0, return RET_CANCEL
		 if (widgetValue < 0)
		 {
		 _log.debug("LOG_SYSTEM_OUT","//// NonNegativeValidationCCF Validation Failed",100L);
		 String[] parameters = prepareParameters(tempValue);
		 throw new EpiException("WMEXP_NONNEGATIVE_VALIDATION", "WMEXP_NONNEGATIVE_VALIDATION", parameters);
		 //setErrorMessage(formWidget, "Invalid Value, Must Be Greater Than or Equal to 0");
		 //return RET_CANCEL;

		 }
		 else
		 {
		 _log.debug("LOG_SYSTEM_OUT","//// NonNegativeValidationCCF Validation Passed - " + formWidget.getName() + " : " + tempValue,100L);
		 //setErrorMessage(formWidget, "");
		 return RET_CONTINUE;
		 }
		 */
	}

	private String[] prepareParameters(String tempValue)
	{
		String[] parameters = new String[1];
		parameters[0] = tempValue;
		return parameters;
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

	private boolean isEmpty(Object attributeValue)
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

	/**
	 * @deprecated Use {@link FormValidation#isNegative(String,String)} instead
	 */
	static public boolean isNegative(String value, String name) throws EpiException
	{
		return FormValidation.isNegative(value, name);
	}

}
