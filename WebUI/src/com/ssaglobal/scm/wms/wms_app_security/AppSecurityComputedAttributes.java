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
package com.ssaglobal.scm.wms.wms_app_security;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class AppSecurityComputedAttributes implements ComputedAttributeSupport{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppSecurityComputedAttributes.class);
		public Object get(Bio bio, String attributeName, boolean isOldValue)
					throws EpiDataException{	
			_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Executing AppSecurityComputedAttributes",100L);			
			BioCollection labels = bio.getBioCollection("labels");
			_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Got "+labels.size()+" labels...",100L);			
			for(int i = 0; i < labels.size(); i++){
				Bio label = labels.elementAt(i);
				_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Got label type "+label.get("label_type_lookup_id")+" With Value:"+label.get("short_text")+"...",100L);				
				if(label.get("label_type_lookup_id") != null && ((String)label.get("label_type_lookup_id")).equalsIgnoreCase("label")){
					_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Returning:"+label.get("short_text")+"...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Exiting AppSecurityComputedAttributes",100L);					
					return label.get("short_text");
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Returning null...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_APPSECCOMPATTR","Exiting AppSecurityComputedAttributes",100L);
			return null;
		}
		public boolean supportsSet(String bioTypeName, String attributeName){
			return true;
		}
		public void set(Bio bio, String attributeName, Object attributeValue,
						boolean isOldValue) throws EpiDataException{
			return;
		}
}
