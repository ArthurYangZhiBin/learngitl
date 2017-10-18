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

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.ssaglobal.SsaAccessBase;


public class UMSUtil {
	public static String getUserId(String domainWithUserId){
		String loginId = domainWithUserId.substring(domainWithUserId.indexOf("\\")+1);
		return loginId;
	}
	
	public static String getADSUserId(String adsUser){
		String loginId = adsUser.substring(adsUser.indexOf("=")+1, adsUser.indexOf(",")).trim();
		return loginId;
	}

	
	
	public static boolean isInDomain(String ssoName, String domain){		
		return ssoName.startsWith(domain);
	}
	
	
	public static String getFirstName(String fullName){
		int index = fullName.lastIndexOf(" ");
		if(index == -1){
			return "";
		}else{
			return fullName.substring(0, index);
		}
	}
	public static String getLastName(String fullName){
		int index = fullName.lastIndexOf(" ");
		if(index == -1){
			return fullName;
		}else{
			return fullName.substring(index+1);
		}
	}
	

	public static String getUserIdForAds(String fullyQulifiedName, String userBaseDn){
		int indexOfBaseDn = fullyQulifiedName.indexOf(userBaseDn);
		if(indexOfBaseDn == -1){
			return "";
		}
		int indexOfComma = fullyQulifiedName.indexOf(",");
		int indexOfEqual = fullyQulifiedName.indexOf("=");
		return fullyQulifiedName.substring(indexOfEqual+1, indexOfComma);
	}
	
}
