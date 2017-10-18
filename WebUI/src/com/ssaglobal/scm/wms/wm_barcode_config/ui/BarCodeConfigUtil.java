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
package com.ssaglobal.scm.wms.wm_barcode_config.ui;

import com.ssaglobal.scm.wms.datalayer.*;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class BarCodeConfigUtil {

protected static ILoggerCategory _log = LoggerFactory.getInstance(BarCodeConfigUtil.class);
		
public static boolean isValidKey(BarcodeConfigDataObject data)throws DPException{
			_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidKey ******\n\n",100L);
			String key = data.getbarcodeConfigKey();
			String sql ="select * from barcodeconfig where barcodeconfigkey='"+key+"'";
			_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidKey: " +sql,100L);
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()> 0){
				return true;
			}else{
				return false;
			}
		}
public static boolean isValidAI(BarcodeConfigDataObject data) throws DPException{
			_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidAI ******\n\n",100L);
			String ai = data.getappIdentifier();
		
			String sql ="select * from barcodeconfigdetail where ai='"+ai+"'";
			_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidAI: " +sql,100L);
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()> 0){
				return true;
			}else{
				return false;
			}
		}
	
	
	
}
