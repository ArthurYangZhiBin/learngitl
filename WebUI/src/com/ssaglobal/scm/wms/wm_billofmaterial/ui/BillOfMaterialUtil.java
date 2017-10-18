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
package com.ssaglobal.scm.wms.wm_billofmaterial.ui;

import java.text.NumberFormat;
import java.text.ParseException;

import com.epiphany.shr.data.dp.exception.DPException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class BillOfMaterialUtil {
/*	public static boolean isValidItemOwnerComb(BillOfMaterialDataObject data, String Item, String Owner)throws DPException{
		_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidLocation ******\n\n",100L);
		
		String item= Item;
		String storer= Owner;
				
		String sql ="select * from SKU where storerkey='"+storer+"' and sku='"+item+"'";
		_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidItem: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValidOwner(BillOfMaterialDataObject data, String Owner)throws DPException{
		_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidOwner ******\n\n",100L);
		String owner = Owner;
		String sql ="select * from STORER where type='1' and storerkey='"+owner+"'";
		_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidOwner: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValidSKU(BillOfMaterialDataObject data, String Item)throws DPException{
		_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidOwner ******\n\n",100L);
		String sku = Item;
		String sql ="select * from SKU where sku='"+sku+"'";
		_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidSKU: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isDupPK(BillOfMaterialDataObject data)throws DPException{
		_log.debug("LOG_SYSTEM_OUT","\n\n***** In isDupPK ******\n\n",100L);
		String PK = data.getBomKey();
		String sql ="select * from BOMHDRDEFN where bomhdrdefnkey='"+PK+"'";
		_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isDupPK: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	*/	

}
