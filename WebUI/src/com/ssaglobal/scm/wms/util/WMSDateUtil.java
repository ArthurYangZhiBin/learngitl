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

//Import 3rd party packages and classes
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import org.apache.commons.configuration.Configuration;

//Import Epiphany packages and classes
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.base.Config;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class WMSDateUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WMSDateUtil.class);

	public static String getEJBDateFormatAsString()
	{
		Config conf = SsaAccessBase.getConfig("cognosClient", "defaults");
		Configuration wmDateConfig = conf.getConfiguration();
		_log.debug("LOG_DEBUG_EXTENSION", "*****::::" + wmDateConfig.getString("parm"), SuggestedCategory.NONE);

		String ejbDateFormat = wmDateConfig.getString("parm");
		_log.debug("LOG_DEBUG_EXTENSION", "ejbDateFormat = " + ejbDateFormat, SuggestedCategory.NONE);

		return ejbDateFormat;
	}

	public static SimpleDateFormat getEJBDateFormat()
	{
		Config conf = SsaAccessBase.getConfig("cognosClient", "defaults");
		Configuration wmDateConfig = conf.getConfiguration();
		_log.debug("LOG_DEBUG_EXTENSION", "*****::::" + wmDateConfig.getString("parm"), SuggestedCategory.NONE);

		String ejbDateFormat = wmDateConfig.getString("parm");
		_log.debug("LOG_DEBUG_EXTENSION", "ejbDateFormat = " + ejbDateFormat, SuggestedCategory.NONE);

		SimpleDateFormat ejbFormat = new SimpleDateFormat(ejbDateFormat.toString());
		return ejbFormat;
	}
	
	public static GregorianCalendar setCurrentTime(Object calVal){
		GregorianCalendar now = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		if(calVal != null){
			//If not null, then set current time.
			GregorianCalendar effDate = (GregorianCalendar)calVal;
			now.set(GregorianCalendar.DATE, effDate.get(GregorianCalendar.DATE));
			now.set(GregorianCalendar.MONTH, effDate.get(GregorianCalendar.MONTH));
			now.set(GregorianCalendar.YEAR, effDate.get(GregorianCalendar.YEAR));
		}
		return now;
	}
}
