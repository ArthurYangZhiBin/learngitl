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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

import java.text.NumberFormat;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class AdjustmentComputedAttr implements ComputedAttributeSupport {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentComputedAttr.class);
	public AdjustmentComputedAttr()
    {
    }
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
    	NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
       	//07/07/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
    	UnitOfWork uow= null; 
    	
		try
		{
			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	//07/07/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end
    	if("UOMADJQTY".equalsIgnoreCase(attributeName)){
			String qty = bio.get("QTY").toString();
			_log.debug("LOG_SYSTEM_OUT","**********QTY before conversion*******"+ qty,100L);
			String uom = bio.get("UOM").toString();
			String packkey = bio.get("PACKKEY").toString();
			Object objqtyExpConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qty, packkey, UOMMappingUtil.stateNull, uow, false);//07/07/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			_log.debug("LOG_SYSTEM_OUT","**********QTY Convertd*******"+ objqtyExpConv.toString(),100L);
			try
			{
				Number unformattedValue = nf.parse(objqtyExpConv.toString());
				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Unformatted Value " + unformattedValue,100L);
				String formattedValue = nf.format(unformattedValue);
				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
				Object objqty = formattedValue;
				return objqty;
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
