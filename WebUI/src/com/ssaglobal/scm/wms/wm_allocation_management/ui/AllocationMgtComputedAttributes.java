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
package com.ssaglobal.scm.wms.wm_allocation_management.ui;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.ssaglobal.scm.wms.util.LocaleUtil; //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

public class AllocationMgtComputedAttributes implements ComputedAttributeSupport{
		public Object get(Bio bio, String attributeName, boolean isOldValue)
					throws EpiDataException{			
			double originalQty = 0.0;
			double shippedQty = 0.0;
			double diff = 0.0;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		
			if("REMAINS".equalsIgnoreCase(attributeName)){
				originalQty = new Double(bio.get("ORIGINALQTY").toString()).doubleValue();
				shippedQty = new Double(bio.get("SHIPPEDQTY").toString()).doubleValue();
				diff = originalQty - shippedQty;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				return Double.parseDouble(LocaleUtil.formatValues(Double.toString(diff), LocaleUtil.TYPE_QTY));//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
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
