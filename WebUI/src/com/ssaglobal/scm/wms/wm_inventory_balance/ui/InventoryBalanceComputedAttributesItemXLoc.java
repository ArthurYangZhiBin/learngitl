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
package com.ssaglobal.scm.wms.wm_inventory_balance.ui;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;

public class InventoryBalanceComputedAttributesItemXLoc implements ComputedAttributeSupport{
		public Object get(Bio bio, String attributeName, boolean isOldValue)
					throws EpiDataException{			
			double qtyAllocated = 0.0;
			double qtyPicked = 0.0;
			double qtyHold = 0.0;
			double qtyOnHand = 0.0;			
		
			if("qtyavailable".equalsIgnoreCase(attributeName)){
				if(bio.get("QTYALLOCATED") != null)
					qtyAllocated = new Double(bio.get("QTYALLOCATED").toString()).doubleValue();
				if(bio.get("QTYPICKED") != null)
					qtyPicked = new Double(bio.get("QTYPICKED").toString()).doubleValue();
				if(bio.get("QTYHOLD") != null)
					qtyHold = new Double(bio.get("QTYHOLD").toString()).doubleValue();
				if(bio.get("QTY") != null)
					qtyOnHand = new Double(bio.get("QTY").toString()).doubleValue();
				return new Double(qtyOnHand - qtyAllocated - qtyPicked - qtyHold);
			}
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
