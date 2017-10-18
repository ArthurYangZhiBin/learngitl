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
package com.ssaglobal.scm.wms.util;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.util.internationalization.element.BasicNumberDisplay;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;

public class LocaleUtil{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LocaleUtil.class);
	public static final String TYPE_QTY = "QTY";
	public static final String TYPE_CURR = "CURRENCY";
	
	/**     
     * 02/20/2009	AW		Machine:2296026 SDIS:SCM-00000-06431 
     * 						Replacing the decimals to commas if the locale is portuguese.
     * 03/23/2009	AW		Machine:2296026 SDIS:SCM-00000-06431 
     * 						Modified extension to handle grouping
	 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method parses the input qty to the locales format
	 * 03/22/2009	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3
     */
	public static String checkLocaleAndParseQty(String qty, String type) {	
		String newQty = null;  
		try{
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(getCurrencyLocale());
			NumberFormat nf_US = NumberFormat.getInstance(Locale.US);
			char decimalSeperator = dfs.getDecimalSeparator();
			char groupingSeperator = dfs.getGroupingSeparator(); //AW
			qty = qty.replaceAll("\u00a0", "");
			int indx = qty.indexOf(Character.toString(decimalSeperator));
			String fractionDigits = null; //AW Infor365:217417
			
			if(indx < 0 )
			{
				Number num =nf_US.parse(qty);
				//AW Infor365:217417 start
				if(type.equals("QTY"))
					fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");
				else if (type.equals("CURRENCY"))
					fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
				
				newQty = BasicNumberDisplay.formatNumber(Double.parseDouble(num.toString()), getCurrencyLocale(), Integer.parseInt(fractionDigits));
				//AW Infor365:217417 end				
			}
			else newQty = qty;
	
			
			newQty.replaceAll(" ", "\u00a0");

		return newQty;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }


	/**
	 * 
	 * @param locale
	 * @param qty
	 * @return
	 * 03/23/2009	AW		Machine:2296026 SDIS:SCM-00000-06431
	 * 						Initial version
	 *						Added in this method to revert qty values back to decimals 
	 *						for backend operations.  
	 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method formats the number back to decimal format(US) 						
	 */
	
	
	public static String resetQtyToDecimalForBackend(String qty)
	{		
		try{
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(getCurrencyLocale());			
			char decimalSeperator = dfs.getDecimalSeparator();
			
			int indx = qty.indexOf(Character.toString(decimalSeperator));
			
			if(decimalSeperator != '.' && indx > 0)
			{
				Number valWithDecimal = BasicNumberDisplay.parseNumber(qty, getCurrencyLocale());
				return Double.toString(valWithDecimal.doubleValue());
			}
			else
				return qty;
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
		
	}

	/*
	 * 04/23/2009	AW		Initial version
	 * 						SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method parses gets the currency locale
     *
	 */
	public static Locale getCurrencyLocale()
	{
		try
		{
			String langLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getPreference("number_currency_locale");
			int _index = langLocale.indexOf("_");
			langLocale = langLocale.substring(0, _index);
			MetaDataAccess mda = MetaDataAccess.getInstance();
			LocaleInterface locale = mda.getLocale(langLocale);
			
			return locale.getJavaLocale();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		 
	}
	/*
	 * 04/23/2009	AW		Initial version
	 * 						SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method parses gets the user locale
     *
	 */	
	
	public static Locale getUserLocale()
	{
	try{	
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale); 
		return locale.getJavaLocale(); 
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
	}
	/*
	 * 04/23/2009	AW		Initial version
	 * 						SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method formats the qty
  	 * 03/22/2009	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3
	 */
	public static String formatValues(String qty, String type)
	{
		    NumberFormat nf = getNumberFormat(type, 0, 0);
			try {
				return nf.format(nf.parse(LocaleUtil.checkLocaleAndParseQty(qty, type))); //AW Infor365: 217417
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} //02/20/2009 AW Machine:2296026 SDIS:SCM-00000-06431   			
			
	}
	/*
	 * 04/23/2009	AW		Initial version
	 * 						SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
	 *						This method creates a NumberFormat Object
     *
	 */
	public static NumberFormat getNumberFormat(String type, int maxDigits, int minDigits)
	{
		String fractionDigits = null;
		NumberFormat nf = NumberFormat.getInstance(getCurrencyLocale());
		if(type.equals(TYPE_QTY))
			fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("QtyDecimalPrecision");
		else if (type.equals(TYPE_CURR))
			fractionDigits = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("CurrencyDecimalPrecision");
		int fractionDigitsInt = 0;
		try {
			fractionDigitsInt = Integer.parseInt(fractionDigits);
		} catch (NumberFormatException e1) {
			_log.error("LOG_DEBUG_EXTENSION", "!@# Value In Config File Unparseable:" + fractionDigits, SuggestedCategory.NONE);
		}
		
		
		//		Format
		int maxFractionDigits = fractionDigitsInt;
		int minFractionDigits = fractionDigitsInt;
		int maxIntegerDigits = (maxDigits > 0) ? maxDigits: nf.getMaximumIntegerDigits();
		int minIntegerDigits = (minDigits > 0) ? minDigits: nf.getMinimumIntegerDigits();
		boolean grouping = true;		
		
		nf.setMaximumFractionDigits(maxFractionDigits);
		nf.setMinimumFractionDigits(minFractionDigits);

		nf.setMaximumIntegerDigits(maxIntegerDigits);
		nf.setMinimumIntegerDigits(minIntegerDigits);

		nf.setGroupingUsed(grouping);
		
		return nf;
	}
	

}
