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

public class InventoryBalanceComputedAttributesLocation implements ComputedAttributeSupport{
		public Object get(Bio bio, String attributeName, boolean isOldValue)
					throws EpiDataException{			
			double locCubicCapacity = 0.0;
			double cubeOnHand = 0.0;
			Object locCubicCapacityObj = bio.get("CUBECAPACITY");
			Object cubeOnHandObj = bio.get("CUBEONHAND");
			if(locCubicCapacityObj == null)
				return "0.0";
			if(cubeOnHandObj == null)
				return "0.0";
			locCubicCapacity = new Double(bio.get("CUBECAPACITY").toString()).doubleValue();
			cubeOnHand = new Double(bio.get("CUBEONHAND").toString()).doubleValue();
			if("cubeavaliable".equalsIgnoreCase(attributeName)){							
				return new Double(locCubicCapacity - cubeOnHand);
			}			
			if("percentusedloc".equalsIgnoreCase(attributeName)){				
				if(locCubicCapacity != 0.0){					
					String percent = (cubeOnHand*100)/locCubicCapacity +"";						
					if(percent.indexOf(".") == -1)
						percent += ".";					
					percent+="00";									
					percent = percent.substring(0,percent.indexOf("."))+percent.substring(percent.indexOf("."),percent.indexOf(".")+3)+"%";					
					return percent;
				}			
				else if(cubeOnHand == 0.0){					
					String percent = "0.0";						
					if(percent.indexOf(".") == -1)
						percent += ".";					
					percent+="00";									
					percent = percent.substring(0,percent.indexOf("."))+percent.substring(percent.indexOf("."),percent.indexOf(".")+3)+"%";					
					return percent;
				}else{
					return "";
				}
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
