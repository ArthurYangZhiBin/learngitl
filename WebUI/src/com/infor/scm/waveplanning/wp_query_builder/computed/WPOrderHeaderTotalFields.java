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
package com.infor.scm.waveplanning.wp_query_builder.computed;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;

public class WPOrderHeaderTotalFields implements ComputedAttributeSupport
{

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
		BioCollection orderLines = bio.getBioCollection("ORDERLINECOLLECTION");
		if (orderLines.size() != 0)
		{
			if("TOTALALLOCATED".equals(attributeName))
			{
				final Object sum = orderLines.sum("QTYALLOCATED");
				return sum;
			}
			else if("TOTALPICKED".equals(attributeName))
			{
				final Object sum = orderLines.sum("QTYPICKED");
				return sum;
			}
			else if("TOTALSHIPPED".equals(attributeName))
			{
				final Object sum = orderLines.sum("SHIPPEDQTY");
				return sum;
			}
		}
		return null;
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException
	{
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
