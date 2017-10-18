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
package com.ssaglobal.scm.wms.wm_conditional_validation.ui;

import com.epiphany.shr.data.dp.exception.DPException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ConditionalValidationUtil {
	public static boolean isValidItem(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidLocation ******\n\n",100L);
		String item = data.getItem();
		String storer= data.getStorer();
		String sql ="select * from SKU where storerkey='"+storer+"' and sku='"+item+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidItem: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isValidCustomer(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidCustomer ******\n\n",100L);
		String cust = data.getCustomer();
		String sql ="select * from STORER where type='2' and storerkey='"+cust+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidCustomer: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isValidOwner(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidOwner ******\n\n",100L);
		String owner = data.getStorer();
		String sql ="select * from STORER where type='1' and storerkey='"+owner+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidOwner: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isDupPK(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isDupPK ******\n\n",100L);
		String PK = data.getCondValidKey();
		String sql ="select * from CONDITIONALVALIDATION where validationkey='"+PK+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isDupPK: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	public static boolean isValidComb(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidComb ******\n\n",100L);
		String owner = data.getStorer();
		String cust = data.getCustomer();
		String item = data.getItem();
		String type = data.getType();
		
		String sql ="select * from CONDITIONALVALIDATION where storerkey='"+owner+"'" +
					" and consigneekey='"+cust+"'" +
					" and sku='"+item+"'" +
					"and type='"+type+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidComb: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	public static boolean isGreaterThankZero(ConditionalValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isGreaterThankZero ******\n\n",100L);

			String minSL = data.getMinShelfLife();			
			double value = Double.parseDouble(minSL);
		
		if (value < 0){
			//_log.debug("LOG_SYSTEM_OUT","//// NegativeValidation Validation Failed",100L);
			return false;
			}
			else
			{
			//_log.debug("LOG_SYSTEM_OUT","//// NegativeValidation Validation Passed - " ,100L);
			return true;
			}
	}
}
