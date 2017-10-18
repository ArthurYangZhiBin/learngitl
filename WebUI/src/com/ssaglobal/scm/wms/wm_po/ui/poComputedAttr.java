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
package com.ssaglobal.scm.wms.wm_po.ui;

import java.text.NumberFormat;

import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
public class poComputedAttr implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(poComputedAttr.class);
	public poComputedAttr()
    {
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
    {
    	NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);

    	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
    	UnitOfWork uow= null; 
    	
		try
		{
			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end
       //Qty Ordered on the ASN Detail Receipts tab        
       if("ORDEREDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyExpected = bio.get("QTYORDERED").toString();
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyOrd = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qtyExpected, packkey, UOMMappingUtil.stateNull, uow, false); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
    			Number unformattedValue = nf.parse(LocaleUtil.checkLocaleAndParseQty(objqtyOrd.toString(), LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10
   				String formattedValue = nf.format(unformattedValue);
   				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
   				return formattedValue;
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }

       //Qty Expected on the ASN Detail Receipts tab        
       if("RECEIVEDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyReceived = bio.get("QTYRECEIVED").toString();
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyRec = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qtyReceived, packkey, UOMMappingUtil.stateNull, uow, false); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
   				Number unformattedValue = nf.parse(LocaleUtil.checkLocaleAndParseQty(objqtyRec.toString(), LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10
   				String formattedValue = nf.format(unformattedValue);
   				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
   				return formattedValue;
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }    
       }
       return "";
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
