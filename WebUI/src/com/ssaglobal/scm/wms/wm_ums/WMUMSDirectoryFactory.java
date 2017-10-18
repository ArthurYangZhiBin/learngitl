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
package com.ssaglobal.scm.wms.wm_ums;

public class WMUMSDirectoryFactory {
	public static WmsUmsInterface getUsers(String umsDirectory){
		if(WMUMSConstants.NTLM_DIRECTORY_SERVICE.equalsIgnoreCase(umsDirectory)){
			return new NTLMUsers();
		}
		if(WMUMSConstants.DB_DIRECTORY_SERVICE.equalsIgnoreCase(umsDirectory)){
			return new DBDirectoryServerUsers();
		}
		if (WMUMSConstants.ADS_DIRECTORY_SERVICE.equalsIgnoreCase(umsDirectory))
			return new ADSDirectoryServerUsers();
		return null;
	}
}
