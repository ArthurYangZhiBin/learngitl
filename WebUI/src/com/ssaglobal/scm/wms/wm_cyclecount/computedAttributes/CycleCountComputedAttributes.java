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
package com.ssaglobal.scm.wms.wm_cyclecount.computedAttributes;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;

public class CycleCountComputedAttributes implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CycleCountComputedAttributes.class);
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{

		double adjustedQty = 0.0;
		double price = 0.0;

		if ("extendedCost".equalsIgnoreCase(attributeName))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "***good****** " + bio.get("ADJQTY"), SuggestedCategory.NONE);
			Object adjustedQtyObj = bio.get("ADJQTY");
			if (adjustedQtyObj != null)
			{
				adjustedQty = new Double(adjustedQtyObj.toString()).doubleValue();
			}
			Object priceObj = bio.get("COST");
			if (priceObj != null)
			{
				price = new Double(priceObj.toString()).doubleValue();
			}
			_log.debug("LOG_DEBUG_EXTENSION", "adjQty=" + adjustedQty + "  price=" + price, SuggestedCategory.NONE);
		}
		return new Double(price * adjustedQty);
	}

	public boolean supportsSet(String bioTypeName, String attributeName)
	{
		return true;
	}

	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue) throws EpiDataException
	{
		return;
	}
}
