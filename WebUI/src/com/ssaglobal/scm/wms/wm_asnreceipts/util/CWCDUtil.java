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
package com.ssaglobal.scm.wms.wm_asnreceipts.util;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.StringUtils;

public class CWCDUtil {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CWCDUtil.class);

	public static boolean getDelaySNumCaptureFlag(UnitOfWorkBean uow) {
		boolean delaySNumCaptureFlag = false;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'DELAYSNUMCAPTURE')";
		Query bioQuery = new Query("wm_system_settings", sQueryString, null);
		BioCollectionBean selCollection = uow.getBioCollectionBean(bioQuery);
		try {
			delaySNumCaptureFlag = selCollection.elementAt(0).get("NSQLVALUE").toString().equals("1") ? true : false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error(	"LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute",
						StringUtils.getStackTraceAsString(e),
						SuggestedCategory.NONE);
			_log.error(	"LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute",
						"DELAYSNUMCAPTURE not found in db",
						SuggestedCategory.NONE);
			delaySNumCaptureFlag = false;
		}
		return delaySNumCaptureFlag;
	}

}
