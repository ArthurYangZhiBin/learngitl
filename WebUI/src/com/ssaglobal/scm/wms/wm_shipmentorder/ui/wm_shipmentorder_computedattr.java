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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.text.NumberFormat;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.util.LocaleUtil; //AW

public class wm_shipmentorder_computedattr
    implements ComputedAttributeSupport
{

    public wm_shipmentorder_computedattr()
    {
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
        
    {
    	NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0,0);

    	double zero = 0;
    	if(!(bio.get("STATUS").equals("00"))){
    		BioCollection orderdetailCollection = bio.getBioCollection("ORDER_DETAIL");
    		Object objorgSum = orderdetailCollection.sum("ORIGINALQTY");
    		Object objadjSum = orderdetailCollection.sum("ADJUSTEDQTY");
    		try{
    			double originalqty = (new Double(objorgSum.toString())).doubleValue();
        		double adjustedqty = (new Double(objadjSum.toString())).doubleValue();
        		//SM 10/08/07 - ISSUE SCM-00000-03080: Total Quantity not calculating correctly (Changed from subtraction to addition)        		
        		String formattedValue = nf.format(originalqty + adjustedqty);
        		//SM 10/08/07: End Update
        		return (formattedValue);
    		}catch(NullPointerException e){
    			return nf.format(zero);
    		}
    	}
    	else{
    		return nf.format(zero);
    	}
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
