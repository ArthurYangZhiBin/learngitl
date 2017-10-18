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
package com.ssaglobal.scm.wms.formextensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class EnforcePrecisionOnList extends FormExtensionBase
{
	public EnforcePrecisionOnList()
	{
	}
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EnforcePrecisionOnList.class);
		

	/**
	 * 02/20/2009	AW		Machine:2296026 SDIS:SCM-00000-06431
	 * 						The values read from the DB are in decimal format.
	 * 						If the locale is pt the decimal values ares replaced by commas 
	 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 * 03/22/2009	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3
	 */	
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiDataException{
		RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
		ArrayList widgets = (ArrayList)getParameter("widgets");
		ArrayList types = (ArrayList)getParameter("types");					
		boolean didUpdate = false;
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","widgets:"+widgets,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","types:"+types,100L);	
//		AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		BioCollection bioColl= null;
		DataBean bean = context.getState().getFocus();
		if(bean instanceof BioCollection)
		{
			bioColl = (BioCollection)bean;
		}
		RuntimeFormWidgetInterface widget= null;
		//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

		for (int i = 0; i < listRows.length; i++)
		{		
			for(int j = 0; j < widgets.size(); j++){	
				
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Getting Row Widgets...",100L);				
				widget = listRows[i].getFormWidgetByName((String)widgets.get(j));		//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				
				//If widget is not found on the front end then continue
				if(widget == null)
					continue;
				
				String type = (String)types.get(j);
				
				//Get precision from Config File
				String fractionDigits = null;
				if(type.equals("QTY"))
					fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");
				else if (type.equals("CURRENCY"))
					fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
				
				
				String doTruncate = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("doTruncate");
				int fractionDigitsInt = 0;
				try {
					fractionDigitsInt = Integer.parseInt(fractionDigits);
				} catch (NumberFormatException e1) {
					_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
					continue;
				}
				
				//Since we are getting the display value of the widget, it can have non-numeric characters
				// like a comma. These must be removed so that they can be wrapped in Double objects
				
				// RM 7316. Error when viewing Drop ID created as part of Rainbow Pallet receipt
				if( widget.getDisplayValue() == null ) 
					continue;
				// RM			
				
				NumberFormat decimalFormatter = LocaleUtil.getNumberFormat(type, 0, 0); //AW edit Machine:2296026 SDIS:SCM-00000-06431
				
				//Assert value can be parsed as a number.
				Number parsedWidgetValue = null;

//				String val = (bioColl.elementAt(i)).get(widget.getAttribute()).toString(); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				String val = widget.getDisplayValue();
//				AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530 start
				try {
					parsedWidgetValue = decimalFormatter.parse(LocaleUtil.checkLocaleAndParseQty(val, type)); //AW Infor365:217417
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530 end
	
								
				
				if(parsedWidgetValue == null || parsedWidgetValue.toString().length() == 0)
					continue;
				
				String oldValueStr = parsedWidgetValue.toString();
				
				//Assert value can be parsed as a double.
				try {
					Double.parseDouble(oldValueStr);
				} catch (NumberFormatException e1) {
					continue;
				}
				BigDecimal oldValue = new BigDecimal(oldValueStr);
								
				
				//If number has no decimal then just run it through a formatter				
				if(oldValue.doubleValue() - oldValue.intValue()  == 0){
					String formattedValue = decimalFormatter.format(oldValue.doubleValue());
					widget.setDisplayValue(formattedValue);
					continue;
				}
											
				BigDecimal truncatedValue = truncate(oldValue,fractionDigitsInt);
				_log.debug("LOG_DEBUG_EXTENSION", "Original Value:"+oldValueStr , SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Truncated Value:"+truncatedValue , SuggestedCategory.NONE);
				
				
				
				//If Truncated Value is less than Original value then Truncate or Round based on config value																	
				if(oldValue.compareTo(truncatedValue) != 0 ){
					_log.debug("LOG_DEBUG_EXTENSION", oldValue + " is not equal to "+truncatedValue , SuggestedCategory.NONE);
					if(!didUpdate){						
						_log.debug("LOG_DEBUG_EXTENSION", "Updating Focus!!!", SuggestedCategory.NONE);
						try {
							updateFocusData(context,form,widgets,doTruncate,fractionDigitsInt, LocaleUtil.getCurrencyLocale(), type); //AW Infor365:217417 03/22/10
						} catch (EpiException e) {
							_log.error("LOG_DEBUG_EXTENSION", "Database Update Failed", SuggestedCategory.NONE);
							_log.error("LOG_DEBUG_EXTENSION", e.getErrorMessage(), SuggestedCategory.NONE);
						}
						_log.debug("LOG_DEBUG_EXTENSION", "Done Updating Focus!!!", SuggestedCategory.NONE);
						didUpdate = true;
					}					
					BigDecimal finalValue = null;
					if(doTruncate == null || !doTruncate.equalsIgnoreCase("true")){
						try {
							_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounding Value " + oldValueStr, SuggestedCategory.NONE);
							finalValue = round(oldValue,fractionDigitsInt);
							_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounded Value = " + finalValue, SuggestedCategory.NONE);
						} catch (NumberFormatException e) {							
							e.printStackTrace();
							_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
							continue;
						}						
					}
					//Truncating
					else{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncating Value " + oldValueStr, SuggestedCategory.NONE);
						finalValue = truncatedValue;
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncated Value = " + finalValue, SuggestedCategory.NONE);					
					}
										
					String formattedValue = decimalFormatter.format(finalValue.doubleValue());
					widget.setDisplayValue(formattedValue);					
					
				}
				else{
					String formattedValue = decimalFormatter.format(truncatedValue.doubleValue());
					widget.setDisplayValue(formattedValue);						
				}				
			}
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Getting Row...",100L);			
		}
		
		
		return RET_CONTINUE;
	}	
	
	
	//Iterates a biocollection looking for data to update by truncating or rounding.
	private void updateFocusData(UIRenderContext context, RuntimeListFormInterface form, 
			ArrayList widgets, String doTruncate, int fractionDigits, Locale locale,
			String type) throws  EpiException{
		BioCollectionBean focus = (BioCollectionBean)form.getFocus();
			_log.debug("LOG_DEBUG_EXTENSION", "Entering updateFocusData()...", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "Getting focus from form:" + form.getName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "Got focus:" + focus, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "focus type:" + focus.getBioTypeName(), SuggestedCategory.NONE);
			ArrayList otherBioAttr = (ArrayList)getParameter("otherBioAttr");
			//If temp bios are being used then exit
			if(focus.getBioTypeName().equalsIgnoreCase("wm_multifacbal"))
				return;

			NumberFormat decimalFormatter = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);			//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

			for(int z = 0; z < focus.size(); z++){				
				BioBean record = context.getState().getDefaultUnitOfWork().getBioBean(focus.elementAt(z).getBioRef());	
				for(int a = 0; a < widgets.size(); a++){
					String widgetName = (String)widgets.get(a);				
					_log.debug("LOG_DEBUG_EXTENSION", "Updating Focus Record " + z, SuggestedCategory.NONE);	
					//AW Infor365:217417	
					if(updateAttribute( record, widgetName, doTruncate, fractionDigits, decimalFormatter, locale, type)){
						updateDependantData(context, form, doTruncate, fractionDigits, record.getBioRef(), locale, type); //AW Infor365:217417
					}
				}
				
				if(otherBioAttr != null){
					for(int a = 0; a < otherBioAttr.size(); a++){
						String bioAttrName = (String)otherBioAttr.get(a);				
						_log.debug("LOG_DEBUG_EXTENSION", "Updating Focus Record " + z, SuggestedCategory.NONE);				
						//AW Infor365:217417
						if(updateAttribute( record, bioAttrName, doTruncate, fractionDigits, decimalFormatter, locale, type)){
							updateDependantData(context, form, doTruncate, fractionDigits, record.getBioRef(), locale, type); //AW Infor365: 217417
						}
					}
				}

		}
			
		context.getState().getDefaultUnitOfWork().saveUOW();
		
		_log.debug("LOG_DEBUG_EXTENSION", "Leaving updateFocusData()...", SuggestedCategory.NONE);
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


private void updateDependantData(UIRenderContext context, RuntimeListFormInterface form, 
		String doTruncate, int fractionDigits, BioRef bioRef, Locale locale,
		String type) throws  EpiException{
	
	//Update For Receipt Reversal Dependent Data
	if(form.getName().equals("wm_receiptreversaldetail_new_list_view") ||
		form.getName().equals("wm_receiptreversaldetail_existing_list_view")){
		updateReceiptReversalDependantData(context, form, doTruncate, bioRef, locale, type);
	}
	
}
private void updateReceiptReversalDependantData(UIRenderContext context, RuntimeListFormInterface form, 
		String doTruncate, BioRef bioRef, Locale locale, String type) throws  EpiException{
	
	StateInterface state = context.getState(); 
	UnitOfWorkBean uow = state.getDefaultUnitOfWork();
	String qtyFractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");	
	String currencyFractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
	
	
//	AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
	NumberFormat qtyFormatter = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
	NumberFormat currencyFormatter = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0);
	//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

	
	int qtyFractionDigitsInt = Integer.parseInt(qtyFractionDigits);
	int currencyFractionDigitsInt = Integer.parseInt(currencyFractionDigits);
	
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
	
	//Arrays containing the names of all Qty and Currency fields in the receipt reversal table that will be updated.
	//Just doing QTYRECEIVED for now so that I don't risk breaking anything else.
	String qtyFields[] = {"QTYRECEIVED"};
	String currencyFields[] = {};
		
	//Update Quantity fields
	for(int i = 0; i < qtyFields.length; i++){
		updateAttribute(receiptDetailsRecord, qtyFields[i], doTruncate, qtyFractionDigitsInt, qtyFormatter, locale, type); //AW Infor365:217417 03/22/10
	}
	
	//Update Currency fields
	for(int i = 0; i < currencyFields.length; i++){
		updateAttribute(receiptDetailsRecord, currencyFields[i], doTruncate, currencyFractionDigitsInt, currencyFormatter, locale, type); //AW Infor365:217417 03/22/10
	}
	receiptDetailsRecord.getUnitOfWork().save();
}

private boolean updateAttribute( Bio record, String attrName, String doTruncate, int fractionDigits, 
		NumberFormat decimalFormatter, Locale locale, String type) throws EpiException{
	String widgetName = attrName;						
	BigDecimal finalValue = null;
	BigDecimal oldValue = null;
	String widgetValue = null;								
	Object bioAttrValue = null;
	
	try {
		bioAttrValue = record.get(widgetName);						
	} catch (RuntimeException e1) {
		e1.printStackTrace();
		_log.error("LOG_DEBUG_EXTENSION", "receiptDetailsRecord has no attribute:" + widgetName, SuggestedCategory.NONE);
		return false;
	}
	
	try {
		oldValue = bioAttrValue == null?null:new BigDecimal(LocaleUtil.checkLocaleAndParseQty(bioAttrValue.toString(), type));//AW 03/22/10 Infor365:217417
	} catch (RuntimeException e1) {
		_log.error("LOG_DEBUG_EXTENSION", "Attribute value is not a number:" + bioAttrValue.toString(), SuggestedCategory.NONE);
	}
	if(oldValue == null){
		_log.debug("LOG_DEBUG_EXTENSION", "Focus value is NULL", SuggestedCategory.NONE);
		return false;
	}

	widgetValue = oldValue.toString();

	try {
		Double.parseDouble(widgetValue);
	} catch (NumberFormatException e1) {
		return false;
	}

	//If number has no decimal then no formatting is necessary				
	if(oldValue.doubleValue() - oldValue.intValue()  == 0){
		return false;
	}												
	BigDecimal truncatedValue = truncate(oldValue,fractionDigits);		
	
	//Rounding
	if(doTruncate == null || !doTruncate.equalsIgnoreCase("true")){
		try {
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Rounding Value " + widgetValue, SuggestedCategory.NONE);
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
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncating Value " + widgetValue, SuggestedCategory.NONE);
		finalValue = truncatedValue;
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Truncated Value = " + finalValue, SuggestedCategory.NONE);					
	}								
		
	String formattedValue = decimalFormatter.format(finalValue.doubleValue());
	if(bioAttrValue instanceof String){
		BigDecimal formattedValueBD = new BigDecimal(formattedValue);
		BigDecimal originalBD = new BigDecimal((String)bioAttrValue);
		if(formattedValueBD.compareTo(originalBD) != 0){			
			record.set(widgetName, formattedValue);
			return true;
		}
	}
	else if(bioAttrValue instanceof BigDecimal){
		BigDecimal formattedValueBD = new BigDecimal(formattedValue);
		if(formattedValueBD.compareTo((BigDecimal)bioAttrValue) != 0){				
			record.set(widgetName, formattedValueBD);
			return true;
		}
	}
	return false;
}
}