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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.exceptions.EpiException;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.util.logging.SuggestedCategory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

import com.ssaglobal.scm.wms.util.*;


public class FacilityTransferComputedattr implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityTransferComputedattr.class);
	public FacilityTransferComputedattr()
    {
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
    {
    	NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
    	nf.setMaximumFractionDigits(5);
    	nf.setMinimumFractionDigits(5);

//    	05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
    	UnitOfWork uow= null;     	
		try
		{
			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end

       //Qty Received on the ASN Detail Receipts tab        
       if("CORIGINALQTY".equalsIgnoreCase(attributeName)){
    	   String qtyOriginal = bio.get("ORIGINALQTY").toString();
    	    _log.debug("LOG_SYSTEM_OUT","**********ORIGINALQTY before conversion*******"+ qtyOriginal,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyOrgConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, qtyOriginal, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
   				return LocaleUtil.formatValues(objqtyOrgConv.toString(), LocaleUtil.TYPE_QTY);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }

       //Qty Expected on the ASN Detail Receipts tab        
       if("COPENQTY".equalsIgnoreCase(attributeName)){
    	   String qtyOpen = bio.get("OPENQTY").toString();
    	   _log.debug("LOG_SYSTEM_OUT","**********OPENQTY before conversion*******"+ qtyOpen,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyOpenConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qtyOpen, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
   				return LocaleUtil.formatValues(objqtyOpenConv.toString(),LocaleUtil.TYPE_QTY);
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }

       if("CALLOCATEDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyAllocated = bio.get("QTYALLOCATED").toString();
    	   _log.debug("LOG_SYSTEM_OUT","**********QTYALLOCATED before conversion*******"+ qtyAllocated,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyAllocnConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qtyAllocated, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   { 				
   				return LocaleUtil.formatValues(objqtyAllocnConv.toString(), LocaleUtil.TYPE_QTY);
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }
       if("CSHIPPEDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyShipped = bio.get("SHIPPEDQTY").toString();
    	   _log.debug("LOG_SYSTEM_OUT","**********QTYALLOCATED before conversion*******"+ qtyShipped,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyShippedConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qtyShipped, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
   				return LocaleUtil.formatValues(objqtyShippedConv.toString(), LocaleUtil.TYPE_QTY);
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
