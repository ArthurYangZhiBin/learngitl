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
package com.infor.scm.wms.util.validation.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtil{
	private static HashMap 	resourceBundleMap		= new HashMap();
	public static String getResourceBundleMessage(String key, Locale locale){		
		String message = "";
		ResourceBundle rb = null;
		if(resourceBundleMap.get(locale.toString()) == null){
			rb = ResourceBundle.getBundle("com.infor.scm.wms.util.validation.resources.ApplicationResources",locale);
			resourceBundleMap.put(locale.toString(), rb);
		}
		else{
			rb =(ResourceBundle) resourceBundleMap.get(locale.toString());
		}
		message = rb.getString(key);
		return message;
	}
	
	public static String getResourceBundleMessage(String key, Locale locale, String[] parameters){		
		String message = "";
		ResourceBundle rb = null;
		if(resourceBundleMap.get(locale.toString()) == null){
			rb = ResourceBundle.getBundle("com.infor.scm.wms.util.validation.resources.ApplicationResources",locale);
			resourceBundleMap.put(locale.toString(), rb);
		}
		else{
			rb =(ResourceBundle) resourceBundleMap.get(locale.toString());
		}
		message = rb.getString(key);
		for(int i = 0; i < parameters.length; i++){
			message = message.replaceAll("\\{"+i+"\\}", parameters[i]);
		}
		return message;
	}
}