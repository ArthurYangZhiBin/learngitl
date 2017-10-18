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
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.util.*;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class ShipmentOrderUOMADJComputedAttribute implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderUOMADJComputedAttribute.class);
	private final static String UOM = "UOM";
	private final static String PACK = "PACKKEY";
	private final static double ZERO = 0;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
	
	
    public ShipmentOrderUOMADJComputedAttribute(){
    	
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException{
//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
//    	UnitOfWork uow= null; 
    	
//		try
//		{
////			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
//		} catch (EpiException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end
    	NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0,0);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
    	String actualName = attributeName.substring(6);
    	_log.debug("LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute", actualName, SuggestedCategory.NONE);
    	String value = bio.get(actualName).toString();
    	_log.debug("LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute", value, SuggestedCategory.NONE);
    	String uom = bio.get(UOM).toString();
    	_log.debug("LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute", uom, SuggestedCategory.NONE);
    	String pack = bio.get(PACK).toString();
    	Bio packBio = (Bio) bio.get("PACKBIO");
    	String uom3 = UOMMappingUtil.getPACKUOM3Val(packBio);
    	double temp = Double.parseDouble(commaStrip(value));
    	if(!uom.equalsIgnoreCase(uom3)){
    		if(!value.equalsIgnoreCase("") && temp!=0){       
            	try {
					String test = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(), UOMMappingUtil.UOM_EA, uom, value, pack, packBio);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					return LocaleUtil.formatValues(test, LocaleUtil.TYPE_QTY);//AW Infor365:217417
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }else{
					return nf.format(ZERO);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				
        	}
    	}else{
    		try{    			
    			_log.debug("LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute Formatted val:", LocaleUtil.formatValues(value, LocaleUtil.TYPE_QTY), SuggestedCategory.NONE);
        		return LocaleUtil.formatValues(value, LocaleUtil.TYPE_QTY);    //AW Infor365:217417			
    		}catch(Exception e){
    		}
    	}
    	return null;
    }

    public boolean supportsSet(String bioTypeName, String attributeName) {
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag) throws EpiDataException{
    }
    
	private String commaStrip(String number){
		NumberFormat nf = NumberFormat.getInstance();
		String numberS;
		try
		{
			numberS = nf.parse(number).toString();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
    

}