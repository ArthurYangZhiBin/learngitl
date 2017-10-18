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


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;


import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.objects.generated.np.DefaultValue;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.util.LocaleUtil;

/**
 * 
 * Extension that formats decimal numbers so that their precision reflects what is set in the
 * configuration file WebUIConfig.xml
 * 
 * This extension, along with the EnforcePrecisionOnList extension provide the "Decimal Suppression"
 * functionality discussed in the WM 9x admin guide.
 * 
 * This extension will also update the database if it changes a number's value when formatting it.
 * EXAMPLE: if the number 100.22222 is encountered and the user has configured the global
 * 			precision to be 3 then the formatted number is 100.222 which is less than 100.22222
 * 			in this case the database will be updated with the value 100.222
 *
 */
public class EnforceDefaultValuePrecision extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{
	/*
	 * ************************************************************************************************
	 * 								MODIFICATION HISTORY
	 * ************************************************************************************************
	 * MOD1 9/5/07 	Updated this class so that it takes the value for decimal precision from the config
	 * 				file WebUIConfig.xml instead of the parameter MAX/MIN_FRACTION_DIGITS.
	 * 				Also added code that updates the database if a number's value is different after formatting.
	 * ************************************************************************************************
	 * MOD2 9/17/07 Changed logic so that formatted value is placed in the bio if this is a new record. 
	 * 				This is to ensure the fields are made dirty just incase the user does not click on them.
	 * ************************************************************************************************
	 * MOD3 9/20/07 Fixed logic issue that was causing the replacement of Widget default values. 
	 * 				This was the cause of the default being 0 in bugaware 7234
	 * ************************************************************************************************
	 * MOD4 9/24/07 Removed logic that was depended on having a '.' used for the separator between 
	 * 				the integer and fraction portion of the number. This was necessary for internationalization.
	 * 				Originally the decimal formatting was done by converting the decimal numbers to
	 * 				strings, formatting the string and converting the string back to a Double object.
	 * 				Strings were used due to the quirks of working with floating point primitives.
	 * 				E.G. the number 100.78 was saving to the database as 100.777777777777777777777 when 
	 * 					 using double primitives and the Double wrapper. 
	 * 
	 * 				However this relied on the decimal number having a '.' character in it which was 
	 * 				not acceptable due to internationalization concerns.
	 * 
	 *  			After some experimentation with different object wrappers, the BigDecimal was found
	 *  			to offer the precision formatting necessary and it did not produce unusual results
	 *  			when saving to the database (like in the case of the Double wrapper). Thus this 
	 *  			class was changed so that from beginning to end BigDecimal objects are used. 
	 * ************************************************************************************************
	 * MOD5 10/1/07 Added ignoreFocus parameter so that the caller can opt out of having the focus updated. 
	 * 				This should be used if the widget to which this extension is attached represents 
	 * 				non-savable data e.g. a column in a DB view.
	 * 
	 * 				This was added as a result of Bugaware 7339
	 * ************************************************************************************************
	 * MOD6 10/4/07 Changed so that a BigDecimal object is set into the focus instead of a Double.
	 * 				This was in response to BugAware 7435 which was regarding the Flow Thru Order screen specifically.
	 * 				However the correct fix was here since OA uses the BigDecimal object to interact with the database.
	 * 				If code exists elsewhere that expects a Double after this extension is executed then it will need
	 * 				to be altered to use BigDecimal (which will also make it consistent with OA's functionality) 
	 * ************************************************************************************************ 
	 * MOD7 10/8/07 Added logic to prevent a Receipt Reversal from trying to reverse a quantity greater than
	 * 				that which was received. This could occur if a receipt was done while the decimal precision was
	 * 				set at 5 for a quantity of 10.88888 if the precision is then set to 3 and a user navigates to the
	 * 				Receipt Reversal screen they will see an adjustment QTY of -10.889 which is greater than the
	 * 				quantity received. 
	 * 				
	 * 				The logic added will update the QTYRECEIVED in the RECEIPTDETAIL table (by rounding or truncating) if this 
	 * 				class changes the value of the adjustment quantity while formatting it. 
	 * 
	 * ************************************************************************************************ 
	 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
     *						Changed code to allow qty values for the Currency 
     *         				Locale something other than dollar.
	 * 03/22/2009	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3
	 *
	 * ************************************************************************************************
	 * 								NOTES
	 * ************************************************************************************************
	 * Note 1: 	There is a block of code in the execute() function whose purpose is to populate the
	 * 			widget with its formatted default value.
	 * 
	 * 			This code should ONLY execute if the widget's current value has not been set by the user
	 * 			or by other code and if the record is new.
	 * 
	 * 			To accomplish this a condition statement is used to test if the widget's value is blank,
	 * 			if the widget's underlying record is new and if the value of the widget's bio attribute 
	 * 			has been set already.
	 * 
	 * 			This code is working properly, however there is a use case that makes it seem as though the
	 * 			code is breaking:
	 * 				1. User creates a new record
	 * 				2. User blanks out a field 
	 * 				3. User causes a screen refresh (e.g. by changing a widget that has an onChange extension on it)
	 * 
	 * 			In this use case the field that the user blanks out is repopulated with the 
	 * 			default value (if there is a default value) this is actually an OA bug that is causing a "blank"
	 * 			value to not be sent to the back-end for number widgets. Instead of the "blank" value that the 
	 * 			user entered, the value contained in the widget as of the last form rendering is sent to the back end.
	 * 			Thus it appears as though the code here is repopulating the widget when it is OA sending the wrong value.
	 * 			A PI has been created for this - PI 105756
	 ***************************************************************************************************  		
	 * 			
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EnforceDefaultValuePrecision.class);


	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
	{				
					
		_log.debug("LOG_DEBUG_EXTENSION", "!@#!@# Start of EnforceDefaultValuePrecision", SuggestedCategory.NONE);
		
		String fractionDigits = null;
		String type = getParameterString("TYPE");
		if(type.equals("QTY"))
			fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");
		else if (type.equals("CURRENCY"))
			fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
		int fractionDigitsInt = 0;
		try {
			fractionDigitsInt = Integer.parseInt(fractionDigits);
		} catch (NumberFormatException e1) {
			_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		int maxIntegerDigits = (getParameter("MAX_INTEGER_DIGITS") != null) ? getParameterInt("MAX_INTEGER_DIGITS")
				: 0;
		int minIntegerDigits = (getParameter("MIN_INTEGER_DIGITS") != null) ? getParameterInt("MIN_INTEGER_DIGITS")
				: 0;

		
		NumberFormat nf = LocaleUtil.getNumberFormat(type, maxIntegerDigits, minIntegerDigits); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

				
		//printParametersPassedIn();

		String defaultValue = (getParameter("DEFAULT_VALUE") != null) ? getParameterString("DEFAULT_VALUE") : "0";
		
		//Added ignoreFocus so that the caller can opt out of having the focus updated. This should be used if the widget to which 
		//this extension is attached represents non-savable data e.g. a column in a DB view.
		//This was added as a result of Bugaware 7339
		boolean ignoreFocus = (getParameter("IGNORE_FOCUS") != null) ? getParameterBoolean("IGNORE_FOCUS") : Boolean.FALSE.booleanValue();
		DefaultValue widgetDefaultProp = (DefaultValue)widget.getProperty("Default Value");		 

		String doTruncate = (String)SsaAccessBase.getConfig("webUI","webUIConfig").getValue("doTruncate");
		
		boolean isEmpty = false;
		boolean isTempBio = widget.getForm().getFocus().isTempBio();
		if(isTempBio && ((QBEBioBean)state.getFocus()).isEmpty())
			isEmpty = true;
		String bioAttr = getParameter("BIO_ATTR") == null || getParameter("BIO_ATTR").toString().length() == 0 ? ""
				: getParameter("BIO_ATTR").toString();

		

		try
		{
			Object tempValue = null;
			if(widget.getForm().isListForm())
				tempValue = widget.getDisplayValue();
			else
				tempValue = widget.getValue();
									
			// If widget has no value and this is a new record then populate the widget with a default value if one is available.
			// If no default value is available then populate with a 0
			// If the value has been set already by the user or other code then do nothing
			//
			//Checking the focus attribute for "set" is meant to keep the default value from repopulating a widget that the user has made blank
			//Unfortunately this is not working due to OA not sending the user's blank value which results in the widget's previous value persisting.
			//
			//See NOTE 1 in the NOTES section above
			if (tempValue == null && state.getFocus().isTempBio() && !((QBEBioBean)state.getFocus()).isSet(widget.getAttribute()))				
			{	
				_log.debug("LOG_DEBUG_EXTENSION", "Value uninitialized, setting to default value", SuggestedCategory.NONE);
								
				tempValue = defaultValue;		
				
				if(widgetDefaultProp != null && widgetDefaultProp.getDefaultValueName() != null && widgetDefaultProp.getDefaultValueName().length() > 0){
					tempValue = getValueOfWidgetDefault(widgetDefaultProp);					
					_log.debug("LOG_DEBUG_EXTENSION", "Using widget default:"+tempValue, SuggestedCategory.NONE);
				}
				
			}
			if (tempValue == null || isEmpty(tempValue.toString()))
			{
				_log.debug("LOG_DEBUG_EXTENSION_EnforceDefaultValuePrecision", "Value is empty", SuggestedCategory.NONE);				
				return RET_CONTINUE;
			}
			Number unformattedValue = nf.parse(LocaleUtil.checkLocaleAndParseQty(tempValue.toString(), type)); //AW Infor365:217417 03/22/10
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Unformatted Value " + unformattedValue, SuggestedCategory.NONE);
			

			String formattedValue = nf.format(unformattedValue);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Formatted Value " + formattedValue, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "Getting Here...", SuggestedCategory.NONE);		
			
			if(unformattedValue.doubleValue() - unformattedValue.intValue()  == 0){				
				_log.debug("LOG_DEBUG_EXTENSION", "No decimal found. Skipping new functionality...", SuggestedCategory.NONE);				
				widget.setDisplayValue(formattedValue.toString());
				if (bioAttr.length() > 0){					
					//Changed from Double wrapper to BigDecimal
					//See MOD6 in modifications section above 
					widget.getForm().getFocus().setValue(bioAttr, new BigDecimal(formattedValue.toString()));
				}
				else if(widget.getForm().getFocus() != null && widget.getForm().getFocus().isTempBio() && !ignoreFocus){
					//Changed from Double wrapper to BigDecimal
					//See MOD6 in modifications section above 
					String attribute = widget.getAttribute(); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
					if(attribute != null ){ //AW 04/23/2009 SDIS:SCM-00000-06871 
						widget.getForm().getFocus().setValue(attribute, formattedValue); 
					}//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530 
				}
				if(isEmpty)
					((QBEBioBean)state.getFocus()).setEmpty();
			}
			else{				
				//Keep formatted Int for legacy functionality
				_log.debug("LOG_DEBUG_EXTENSION", "Executing new functionality...", SuggestedCategory.NONE);				
				NumberFormat decimalFormatter = LocaleUtil.getNumberFormat(type, maxIntegerDigits, minIntegerDigits);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
			
				BigDecimal originalValue = new BigDecimal(unformattedValue.toString());
				BigDecimal truncatedValue = truncate(originalValue, fractionDigitsInt);				
				
				_log.debug("LOG_DEBUG_EXTENSION", "Original Value:"+originalValue , SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Truncated Value:"+truncatedValue , SuggestedCategory.NONE);
				
				//If Truncated Value is less than Original value then Truncate or Round based on config value				
									
				if(originalValue.compareTo(truncatedValue) != 0 ){
					_log.debug("LOG_DEBUG_EXTENSION", originalValue + " is not equal to "+truncatedValue , SuggestedCategory.NONE);
					if(!widget.getForm().getFocus().isTempBio() && !ignoreFocus){						
						_log.debug("LOG_DEBUG_EXTENSION", "Updating Focus!!!", SuggestedCategory.NONE);
						try {							
							updateFocusData(state,widget.getForm(),widget.getName(),doTruncate,fractionDigitsInt);
						} catch (EpiException e) {
							_log.error("LOG_DEBUG_EXTENSION", "Database Update Failed", SuggestedCategory.NONE);
							_log.error("LOG_DEBUG_EXTENSION", e.getErrorMessage(), SuggestedCategory.NONE);
						}
						_log.debug("LOG_DEBUG_EXTENSION", "Done Updating Focus!!!", SuggestedCategory.NONE);						
					}					
					BigDecimal finalValue = null;
					if(doTruncate == null || !doTruncate.equalsIgnoreCase("true")){
						try {
							_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounding Value " + originalValue, SuggestedCategory.NONE);
							finalValue = round(originalValue,fractionDigitsInt);
							_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounded Value = " + finalValue, SuggestedCategory.NONE);
						} catch (NumberFormatException e) {							
							e.printStackTrace();
							_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
							return RET_CONTINUE;
						}						
					}
					//Truncating
					else{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncating Value " + originalValue, SuggestedCategory.NONE);
						finalValue = truncatedValue;
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncated Value = " + finalValue, SuggestedCategory.NONE);					
					}
										
					formattedValue = decimalFormatter.format(finalValue.doubleValue());				
					widget.setDisplayValue(formattedValue);
					if(widget.getForm().getFocus() != null && widget.getForm().getFocus().isTempBio() && !ignoreFocus){	
						//Changed from Double wrapper to BigDecimal
						//See MOD6 in modifications section above 
						String attribute = widget.getAttribute(); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
						if(attribute != null){
							widget.getForm().getFocus().setValue(attribute, new BigDecimal(formattedValue));
						}//AW 04/23/2009 SDIS:SCM-00000-06871 
					}
					if(isEmpty)
						((QBEBioBean)state.getFocus()).setEmpty();
					
				}
				else{
					formattedValue = decimalFormatter.format(truncatedValue.doubleValue());
					widget.setDisplayValue(formattedValue);
					if(widget.getForm().getFocus() != null && widget.getForm().getFocus().isTempBio() && !ignoreFocus){		
						//Changed from Double wrapper to BigDecimal
						//See MOD6 in modifications section above 
						String attribute = widget.getAttribute(); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
						if(attribute != null){ //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
							widget.getForm().getFocus().setValue(attribute, formattedValue);
						}//AW 04/23/2009 SDIS:SCM-00000-06871 
					}
					if(isEmpty)
						((QBEBioBean)state.getFocus()).setEmpty();
				}
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;

		}

		return RET_CONTINUE;

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

	
	
	private BigDecimal round(BigDecimal oldValue,int fractionDigits){
		
		if(oldValue == null)
			return null;
		BigDecimal unformattedNumber = new BigDecimal(oldValue.toString());
		unformattedNumber = unformattedNumber.movePointRight(fractionDigits);
		long temp = Math.round(unformattedNumber.doubleValue());
		unformattedNumber = new BigDecimal(temp);
		unformattedNumber = unformattedNumber.movePointLeft(fractionDigits);		
		return unformattedNumber;	
		}
		
	private BigDecimal truncate(BigDecimal oldValue,int fractionDigits){
			
			if(oldValue == null)
				return null;
			BigDecimal unformattedNumber = new BigDecimal(oldValue.toString());
			unformattedNumber = unformattedNumber.movePointRight(fractionDigits);
			BigInteger temp = unformattedNumber.toBigInteger();
			unformattedNumber = new BigDecimal(temp);
			unformattedNumber = unformattedNumber.movePointLeft(fractionDigits);		
			return unformattedNumber;	
		}
	private void updateFocusData(StateInterface state, RuntimeFormInterface form, String widgetName,String doTruncate, int fractionDigits) throws  EpiException{
		DataBean focus = (DataBean)form.getFocus();
		if(focus == null)
			return;
		_log.debug("LOG_DEBUG_EXTENSION", "Entering updateFocusData()...", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "Getting focus from form:" + form.getName(), SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "Got focus:" + focus, SuggestedCategory.NONE);		
		ArrayList otherBioAttr = (ArrayList)getParameter("otherBioAttr");
		
		if(updateAttribute(form.getFocus(), widgetName, doTruncate, fractionDigits))
			updateDependantData(state, widgetName, form, doTruncate, fractionDigits, ((BioBean)form.getFocus()).getBioRef());
		if(otherBioAttr != null){
			for(int a = 0; a < otherBioAttr.size(); a++){
				String bioAttrName = (String)otherBioAttr.get(a);				
				_log.debug("LOG_DEBUG_EXTENSION", "Updating Focus Record " + a, SuggestedCategory.NONE);				
				if(updateAttribute( form.getFocus(), bioAttrName, doTruncate, fractionDigits)){
					updateDependantData(state, bioAttrName,form, doTruncate, fractionDigits,((BioBean)form.getFocus()).getBioRef());
				}
			}
		}
		state.getDefaultUnitOfWork().saveUOW();

		_log.debug("LOG_DEBUG_EXTENSION", "Leaving updateFocusData()...", SuggestedCategory.NONE);
	}

	/*
	 * Attempts to get the value of the given DefaultValue object.
	 * Checks the following and returns the first non-null value it finds:
	 * decimal_value, int_value, string_value
	 */
	private Object getValueOfWidgetDefault(DefaultValue defaultObj){
				
		if (defaultObj.getProperty(DefaultValue.DECIMAL_VALUE) != null)
			return defaultObj.getProperty(DefaultValue.DECIMAL_VALUE);
		if (defaultObj.getProperty(DefaultValue.INTEGER_VALUE) != null)
			return defaultObj.getProperty(DefaultValue.INTEGER_VALUE);
		if (defaultObj.getProperty(DefaultValue.STRING_VALUE) != null)
			return defaultObj.getProperty(DefaultValue.STRING_VALUE);
		return "";
	}
	
	private void updateDependantData(StateInterface state, String widget,RuntimeFormInterface form, String doTruncate, int fractionDigits, BioRef bioRef) throws  EpiException{
		
		//Update For Receipt Reversal Dependent Data
		if(form.getName().equals("wm_receiptreversaldetail_existing_detail_view") ||
			form.getName().equals("wm_receiptreversaldetail_new_detail_view")){
			updateReceiptReversalDependantData(state, widget, doTruncate, bioRef);
		}
		
	}
	
	private void updateReceiptReversalDependantData(StateInterface state, String widget, String doTruncate, BioRef bioRef) throws  EpiException{
		
		//Arrays containing the names of all Qty and Currency fields in the receipt reversal table that will be updated.
		//Just doing QTYRECEIVED for now so that I don't risk breaking anything else.
		String qtyFields[] = {"QTYRECEIVED"};
		String currencyFields[] = {};
		
		if(!widget.equals("NEGUOMQTY")){
			return;
		}		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String qtyFractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");	
		String currencyFractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
		int qtyFractionDigitsInt = 0;	
		int currencyFractionDigitsInt = 0;
		
		try {
			qtyFractionDigitsInt = Integer.parseInt(qtyFractionDigits);
			currencyFractionDigitsInt = Integer.parseInt(currencyFractionDigits);
		} catch (NumberFormatException e) {
			return;
		}
		
		
		//Get receipt reversal record from bio ref passed in
		BioBean rrRecord = uow.getBioBean(bioRef);
		if(rrRecord == null)
			return;
		
		//Get pk info from receipt reversal record (this will be used to look up the associated record in receiptdetails)
		String receiptKey = (String)rrRecord.get("RECEIPTKEY");
		String receiptLineNumber = (String)rrRecord.get("RECEIPTLINENUMBER");
		if(receiptKey == null || receiptLineNumber == null)
			return;
		if(receiptKey.length() == 0 || receiptLineNumber.length() == 0)
			return;
		
		//get receiptdetails record that this receipt reversal record corresponds to
		Query qry = new Query("receiptdetail","receiptdetail.RECEIPTKEY = '"+receiptKey+"' AND receiptdetail.RECEIPTLINENUMBER = '"+receiptLineNumber+"'","");
		BioCollectionBean results = uow.getBioCollectionBean(qry);
		if(results == null || results.size() == 0)
			return;
		Bio receiptDetailsRecord = results.elementAt(0);
		if(receiptDetailsRecord == null)
			return;
		
			
		//Update Quantity fields
		for(int i = 0; i < qtyFields.length; i++){
			updateAttribute(receiptDetailsRecord, qtyFields[i], doTruncate, qtyFractionDigitsInt);
		}
		
		//Update Currency fields
		for(int i = 0; i < currencyFields.length; i++){
			updateAttribute(receiptDetailsRecord, currencyFields[i], doTruncate, currencyFractionDigitsInt);
		}
		receiptDetailsRecord.getUnitOfWork().save();
	}
	
	private boolean updateAttribute( Object record, String attrName, String doTruncate, int fractionDigits) throws EpiException{
		BigDecimal finalValue = null;
		BigDecimal oldValue = null;
		String widgetValue = null;								
		Object bioAttrValue = null;
		//Get old value for this record using Widget name. If BioAttribute and Widget name are not equal 
		//Then we will get no value and we must move on.
		try {
			if(record instanceof Bio)
				bioAttrValue = ((Bio)record).get(attrName);
			else if(record instanceof DataBean)
				bioAttrValue = ((DataBean)record).getValue(attrName);
			else
				return false;
		} catch (RuntimeException e1) {
			e1.printStackTrace();
			_log.error("LOG_DEBUG_EXTENSION", "Focus has no attribute:" + attrName, SuggestedCategory.NONE);
			return false;
		}
		try {
			oldValue = bioAttrValue == null?null:new BigDecimal(bioAttrValue.toString());
		} catch (RuntimeException e1) {
			_log.error("LOG_DEBUG_EXTENSION", "Attribute value is not a number:" + bioAttrValue.toString(), SuggestedCategory.NONE);
		}
		if(oldValue == null){
			_log.debug("LOG_DEBUG_EXTENSION", "Focus value is NULL", SuggestedCategory.NONE);
			return false;
		}
		
				
		//Rounding
		if(doTruncate == null || !doTruncate.equalsIgnoreCase("true")){
			try {
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounding Value " + oldValue, SuggestedCategory.NONE);
				finalValue = round(oldValue,fractionDigits);
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounded Value = " + finalValue, SuggestedCategory.NONE);
			} catch (NumberFormatException e) {							
				e.printStackTrace();
				_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
				return false;
			}						
		}
		//Truncating
		else{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncating Value " + oldValue, SuggestedCategory.NONE);
			finalValue = truncate(oldValue,fractionDigits);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncated Value = " + finalValue, SuggestedCategory.NONE);					
		}															
			
		if(bioAttrValue instanceof String){			
			BigDecimal originalBD = new BigDecimal((String)bioAttrValue);
			if(finalValue.compareTo(originalBD) != 0){		
				if(record instanceof Bio)
					((Bio)record).set(attrName, finalValue);
				else if(record instanceof DataBean)
					((DataBean)record).setValue(attrName, finalValue);
				else
					return false;
				
				return true;
			}
		}
		else if(bioAttrValue instanceof BigDecimal){			
			if(finalValue.compareTo((BigDecimal)bioAttrValue) != 0){				
				if(record instanceof Bio)
					((Bio)record).set(attrName, finalValue);
				else if(record instanceof DataBean)
					((DataBean)record).setValue(attrName, finalValue);
				else
					return false;
				return true;
			}
		}
		return false;
	}
}
