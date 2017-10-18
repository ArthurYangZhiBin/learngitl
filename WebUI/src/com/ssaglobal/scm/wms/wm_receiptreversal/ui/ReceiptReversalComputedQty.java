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
package com.ssaglobal.scm.wms.wm_receiptreversal.ui;


import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.util.*;

public class ReceiptReversalComputedQty implements ComputedAttributeSupport {

	//Static attribute names
	private static final String UOM = "UOM";
	private static final String PACK = "PACKKEY";
	private static final String QTY = "QTY";
		
    public ReceiptReversalComputedQty(){
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException{
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
    	String uom = bio.get(UOM).toString();
    	String qty = bio.get(QTY).toString();
    	String pack = bio.get(PACK).toString();
		if(uom.equals(UOMMappingUtil.getPACKUOM3Val(pack))){
			return LocaleUtil.formatValues(qty, LocaleUtil.TYPE_QTY);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		}else{
			return LocaleUtil.formatValues(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, qty, pack, UOMMappingUtil.stateNull, uow, false),LocaleUtil.TYPE_QTY);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}
    }

    public boolean supportsSet(String bioTypeName, String attributeName){
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag) throws EpiDataException{
    }

}