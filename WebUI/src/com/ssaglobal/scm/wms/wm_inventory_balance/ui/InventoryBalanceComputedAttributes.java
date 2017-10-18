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

public class InventoryBalanceComputedAttributes implements ComputedAttributeSupport{
		public Object get(Bio bio, String attributeName, boolean isOldValue)
					throws EpiDataException{			
			double preAvailable = 0.0;
			double qtyHold = 0.0;
		
			if("qtyavailable".equalsIgnoreCase(attributeName)){
				preAvailable = new Double(bio.get("AVAILABLE").toString()).doubleValue();
				qtyHold = new Double(bio.get("QTYHOLD").toString()).doubleValue();
				return new Double(preAvailable - qtyHold);
			}
			
			//mark ma added *************************
			if("available".equalsIgnoreCase(attributeName)){
				double qty = new Double(bio.get("qty").toString()).doubleValue();
				double qtyAllocated = new Double(bio.get("qtyallocated").toString()).doubleValue();
				double qtyPicked = new Double(bio.get("qtypicked").toString()).doubleValue();
				double qtyOnhold = new Double(bio.get("qtyonhold").toString()).doubleValue();
				double qtyPreallocated = new Double(bio.get("qtypreallocated").toString()).doubleValue();
				return new Double(qty-qtyAllocated-qtyPicked-qtyOnhold-qtyPreallocated);
			}
			
			//end ***********************************
			
			
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
