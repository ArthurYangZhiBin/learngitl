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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.util.*;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InternalTransferComputedAttr implements ComputedAttributeSupport {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferComputedAttr.class);

	public InternalTransferComputedAttr()
    {
    }
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
    	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
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
		if("UOMADJFROMQTY".equalsIgnoreCase(attributeName)){
			String qty = bio.get("FROMQTY").toString();
			_log.debug("LOG_SYSTEM_OUT","**********QTY before conversion*******"+ qty,100L);
			String uom = bio.get("FROMUOM").toString();
			String packkey = bio.get("FROMPACKKEY").toString();
			Object objqtyExpConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qty, packkey, UOMMappingUtil.stateNull, uow, false); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			try
			{
				return LocaleUtil.formatValues(objqtyExpConv.toString(), LocaleUtil.TYPE_QTY);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
			}catch (Exception e)
			{
				// Handle Exceptions 
				e.printStackTrace();
			}
		}
		if("UOMADJTOQTY".equalsIgnoreCase(attributeName)){
			String qty = bio.get("TOQTY").toString();
			_log.debug("LOG_SYSTEM_OUT","**********QTY before conversion*******"+ qty,100L);
			String uom = bio.get("TOUOM").toString();
			String packkey = bio.get("TOPACKKEY").toString();
			Object objqtyExpConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qty, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			try
			{
				return LocaleUtil.formatValues(objqtyExpConv.toString(), LocaleUtil.TYPE_QTY); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
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
	public void set(Bio bio1, String s, Object obj, boolean flag)throws EpiDataException
	{
	}

}
