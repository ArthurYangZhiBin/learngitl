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
package com.ssaglobal.scm.wms.wm_inventory_move.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.QueryableComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.bio.BioType;

import java.math.BigDecimal;

public class InventoryMoveComputeAvailQty implements QueryableComputedAttributeSupport
{	
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{	
		BigDecimal qty = (BigDecimal)bio.get("QTY");
		BigDecimal alloc = (BigDecimal)bio.get("QTYALLOCATED");
		BigDecimal picked = (BigDecimal)bio.get("QTYPICKED");
		
		qty = qty.subtract(alloc);
		qty = qty.subtract(picked);
		
		BigDecimal retValue = qty;		
		return retValue;
	}
	
	public boolean supportsSet(String a, String b)
	{
		return true;
	}
	
	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue)
	{
	}

	public String expression(String path, BioType bioType, String attributeName) {
		String result="";
		result="wm_lotxlocxid.QTY-wm_lotxlocxid.QTYALLOCATED-wm_lotxlocxid.QTYPICKED";
		return result;
	}

	public boolean supportsExpression(String arg0, String arg1) {
		return true;
	}

}