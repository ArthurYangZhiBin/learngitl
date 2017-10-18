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

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.ssaglobal.SsaAccessBase;

public class WebuiConfigUtil {

	private static String WEBUICONFIG ="webUIConfig";
	
	public static String getParamValue(String paramName){
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String value = appAccess.getValue(WEBUICONFIG,paramName);
    	
		return value;
	}
	
	
	
	public static Integer maxExcelExportRecords(){

		String maxExportRecordsString = WebuiConfigUtil.getParamValue("maxExcelExportRecords");
		if (maxExportRecordsString==null)
			return 0;
		else{
			try{
				int maxExportRecords =Integer.parseInt( maxExportRecordsString);
				if(maxExportRecords > 0)
					return maxExportRecords;
				else
					return 0;
			}catch(NumberFormatException e){
				return 0;
			}
		}
		

	}
}
