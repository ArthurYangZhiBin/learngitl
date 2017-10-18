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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.util.CCFUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension, SendAllWidgetsValuesExtensionBase
 */
public class NumericValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(NumericValidationCCF.class);
	private static String CURRENCY_OR_SCIENTIFIC = "[$]?[-+]{0,1}[\\d.,]*[eE]?[-]?[\\d]*";
	
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION", "Start of NonNegativeValidationCCF for ", SuggestedCategory.NONE);
		String tempValue = params.get("fieldValue").toString();
		_log.debug("LOG_DEBUG_EXTENSION", formWidget.getName() + ": " + tempValue + "\n", SuggestedCategory.NONE);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n Value - " + widgetValue, SuggestedCategory.NONE);
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
		try{
			double widgetValue = nf.parse(tempValue.toString()).doubleValue();
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Validating Widget: " + formWidget.getName() + " Value: " + widgetValue, SuggestedCategory.NONE);
		}catch(NumberFormatException e){
			setErrorMessage(formWidget, CCFUtil.constructErrorMessage("EXP_NUMBER_PARSE", new String[] {}));
			return RET_CANCEL;
		}catch(ParseException e){
			setErrorMessage(formWidget, CCFUtil.constructErrorMessage("EXP_NUMBER_PARSE", new String[] {}));
			return RET_CANCEL;
		}
		setErrorMessage(formWidget, "");
		return RET_CONTINUE;
	}

	public static double parseCurrency(String value){
		double widgetValue;
		try{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0);
			widgetValue = nf.parse(value.toString()).doubleValue();
		}catch(ParseException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Exception Caught, trying to parse as Currency", SuggestedCategory.NONE);
			NumberFormat nfc = NumberFormat.getCurrencyInstance();
			try{
				widgetValue = nfc.parse(value.toString()).doubleValue();
			}catch(ParseException e1){
				return Double.NaN;
			}
		}catch(NumberFormatException e){
			return Double.NaN;
		}

		if(!(value.toString().matches(CURRENCY_OR_SCIENTIFIC))){
			return Double.NaN;
		}
		return widgetValue;
	}

	public static double parseCurrency(String value, String name) throws UserException {
		double widgetValue;
		try{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
			widgetValue = nf.parse(value.toString()).doubleValue();

		}catch(ParseException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Exception Caught, trying to parse as Currency", SuggestedCategory.NONE);
			NumberFormat nfc = NumberFormat.getCurrencyInstance();
			try{
				widgetValue = nfc.parse(value.toString()).doubleValue();
			}catch(ParseException e1){
				String[] parameters = new String[2];
				parameters[0] = value;
				parameters[1] = name;
				throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);
			}
		}catch(NumberFormatException e){
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);

		}

		if(value.toString().matches(CURRENCY_OR_SCIENTIFIC)){
			//_log.debug("LOG_DEBUG_EXTENSION", "!@@ Value passed by regex", SuggestedCategory.NONE);
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "!@@ Throwing Exception by regex", SuggestedCategory.NONE);
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);
		}
		return widgetValue;
	}

	/**
	 * @deprecated Use {@link FormValidation#parseNumber(String,String)} instead
	 */
	public static double parseNumber(String value, String name) throws UserException{
		return FormValidation.parseNumber(value, name);
	}

	public static double parseNumber(String value){
		double widgetValue;
		try{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
			nf.setMaximumFractionDigits(40);
			nf.setMaximumIntegerDigits(40);
			widgetValue = nf.parse(value.toString()).doubleValue();
		}catch(ParseException e){
			return Double.NaN;
		}catch(NumberFormatException e){
			return Double.NaN;
		}

		if(!(value.toString().matches(CURRENCY_OR_SCIENTIFIC))){
			return Double.NaN;
		}
		return widgetValue;
	}

	public static boolean isNumber(String value){
		//Calls parseNumber
		if(Double.isNaN(NumericValidationCCF.parseNumber(value))){
			return false;
		}
		return true;
	}

	public static boolean isCurrency(String value){
		//Calls parseCurrency
		double parseCurrency = NumericValidationCCF.parseCurrency(value);
		if(Double.isNaN(parseCurrency)){
			return false;
		}
		return true;
	}
}