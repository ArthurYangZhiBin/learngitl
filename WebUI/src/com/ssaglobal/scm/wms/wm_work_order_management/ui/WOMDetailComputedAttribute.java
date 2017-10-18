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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;

public class WOMDetailComputedAttribute implements ComputedAttributeSupport
{
	public Object get(Bio bio, String field, boolean isOldValue) throws EpiDataException
	{
		double divisor = 1;
		double dividend = 0;
		
		if("EFFICIENCY".equals(field))
		{
			if(!(bio.get("STANDARDTIME").toString().equals("0")))
			{
				divisor = new Double(bio.get("STANDARDTIME").toString()).doubleValue();
				dividend = new Double(bio.get("ACTUALTIME").toString()).doubleValue();
			}
			return new Double(dividend/divisor);
		}
		return null;
	}
	public boolean supportsSet(String bioTypeName, String attributeName)
	{
		return true;
	}	
	public void set(Bio bio, String attributeName, Object attributeValue,
			boolean isOldValue) throws EpiDataException
	{
		return;
	}
}