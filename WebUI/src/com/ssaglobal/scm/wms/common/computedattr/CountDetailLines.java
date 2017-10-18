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
package com.ssaglobal.scm.wms.common.computedattr;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.metadata.objects.bio.FieldMappedAttributeType;
import com.epiphany.shr.metadata.objects.bio.RelMappedAttributeType;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

public class CountDetailLines
    implements ComputedAttributeSupport
{
	
    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
        
    {
    	String Count= "";
    	BioCollection detailCollection = null ;
    	if (attributeName.equalsIgnoreCase("SIZE_RECEIPTDETAIL")){
    		detailCollection = bio.getBioCollection("RECEIPTDETAILS");
    		Count = detailCollection.size()+"";
    	}
    	if (attributeName.equalsIgnoreCase("SIZE_ORDERDETAILS")){
    		detailCollection = bio.getBioCollection("ORDER_DETAIL");
    		Count = detailCollection.size()+"";
    	}
        return (Count);
    	
    }

    public boolean supportsSet(String bioTypeName, String attributeName)
    {
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag)
        throws EpiDataException
    {
    }
}
