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
package com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_inventory_move.ui.InventoryMoveSetListMarker;


public class InventoryReqToCountUtil {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryReqToCountUtil.class);

	public static boolean isValidOwner(InventoryReqToCountDataObject data, String stORend, String defVal)throws DPException{
		String owner = "";
		
		owner = (stORend.equals("START") ? data.getOwnerStart() : data.getOwnerEnd());
		_log.debug("LOG_SYSTEM_OUT","\n\n owner is " +owner,100L);
		String sql ="SELECT * FROM storer " 
			 		+ "WHERE (storerkey = '" + owner + "') "
			 		+ "AND (type = '1') ";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0 || owner.equalsIgnoreCase(defVal)){
			return true;
		}else{
			return false;
		}
   }
	public static boolean isValidItem(InventoryReqToCountDataObject data, String stORend, String defVal)throws DPException{
		String sku = "";
		
		sku = (stORend.equals("START") ? data.getItemStart() : data.getItemEnd());

		String sql ="select * from SKU where SKU='"+sku+"'";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0 || sku.equalsIgnoreCase(defVal)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isValidLocation(InventoryReqToCountDataObject data, String stORend, String defVal)throws DPException{
			String loc = "";
			
			loc = (stORend.equals("START") ? data.getLocStart() : data.getLocEnd());

			String sql ="select * from LOC where LOC='"+loc+"'";
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()> 0 || loc.equalsIgnoreCase(defVal)){
				return true;	
			}
			else{
				return false;
			}
		}
	
	
	public static boolean isValidArea(InventoryReqToCountDataObject data, String stORend, String defVal)throws DPException{
		String area = "";
		
		area = (stORend.equals("START") ? data.getAreaStart() : data.getAreaEnd());

		String sql ="select * from AREA where AREAKEY='"+area+"'";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0 || area.equalsIgnoreCase(defVal)){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValidPutawayzone(InventoryReqToCountDataObject data, String stORend, String defVal)throws DPException{
		String zone = "";
		
		zone = (stORend.equals("START") ? data.getZoneStart() : data.getZoneEnd());

		String sql ="select * from PUTAWAYZONE where PUTAWAYZONE='"+zone+"'";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0 || zone.equalsIgnoreCase(defVal)){
			return true;
		}else{
			return false;
			
		}
	}
}