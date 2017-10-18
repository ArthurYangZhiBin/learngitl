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
package com.ssaglobal.scm.wms.wm_item.computed;

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class ItemAssignLocationQtyAvailable implements ComputedAttributeSupport {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationQtyAvailable.class);

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		try {
			BigDecimal qty = (BigDecimal) bio.get("QTY");
			BigDecimal qtyAllocated = (BigDecimal) bio.get("QTYALLOCATED");
			BigDecimal qtyPicked = (BigDecimal) bio.get("QTYPICKED");
			BigDecimal qtyAvailable = qty.subtract(qtyAllocated).subtract(qtyPicked);
			return qtyAvailable;
		} catch (RuntimeException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_ItemAssignLocationQtyAvailable_get", e.getLocalizedMessage(),
					SuggestedCategory.NONE);
			return null;
		}

	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
