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
package com.ssaglobal.scm.wms.wm_view_inventory_hold.computed;

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;

public class ViewInventoryHoldQtyAvailable implements ComputedAttributeSupport {

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		BigDecimal qty = (BigDecimal) bio.get("QTY");
		BigDecimal qtyAllocated = (BigDecimal) bio.get("QTYALLOCATED");
		BigDecimal qtyPicked = (BigDecimal) bio.get("QTYPICKED");
		// TODO Auto-generated method stub
		return qty.subtract(qtyAllocated).subtract(qtyPicked);
	}

	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String bioTypeName, String attributeName) {
		// TODO Auto-generated method stub
		return false;
	}

}
