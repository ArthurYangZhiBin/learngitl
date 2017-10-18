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
package com.ssaglobal.scm.wms.wm_task_details.bio;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class TaskDetailsStandardAsHHMMSS implements ComputedAttributeSupport {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDetailsStandardAsHHMMSS.class);

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		// STANDARD is stored as a DECIMAL MINUTE
		// ex 1.86 min = 111.6s = 1min 51.6s
		String localeString = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(localeString);

		String standardAsHHMMSS = "";

		String standardAsDecMinute = BioUtil.getString(bio, "STANDARD");
		_log.debug(	"LOG_DEBUG_EXTENSION_TaskDetailsStandardAsHHMMSS_get",
					"Standard in DecMinute " + standardAsDecMinute,
					SuggestedCategory.NONE);
		standardAsHHMMSS = standardAsDecMinute;

		if (!StringUtils.isEmpty(standardAsDecMinute)) {

			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			Number parse = 0;
			try {
				parse = nf.parse(standardAsDecMinute);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			float standard = parse.floatValue();
			standard *= 60;
			int seconds = Math.round(standard);
			_log.debug("LOG_DEBUG_EXTENSION_TaskDetailsStandardAsHHMMSS_get", "Standard as Seconds " + seconds, SuggestedCategory.NONE);
			DecimalFormat twoDigitFormat = new DecimalFormat("###00");
			standardAsHHMMSS = twoDigitFormat.format(seconds / 3600) + ":" + twoDigitFormat.format((seconds % 3600) / 60) + ":" + twoDigitFormat.format(seconds % 60);
		}

		return standardAsHHMMSS;
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
