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
//import com.epiphany.shr.data.bio.Query;
//import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.util.*;;

public class ShipmentOrderDetailTotalQty implements ComputedAttributeSupport{
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		double zero = 0;
		String name = attributeName.substring(5);
		BioCollection bc = bio.getBioCollection("ORDER_DETAIL");
		try{
			if(bc.size()>0){
				Object total = bc.sum(name);
				return nf.format(total);
			}else{
				return nf.format(zero);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
			}
		}catch(NullPointerException e){
			return nf.format(zero);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		}
	}
	
	public boolean supportsSet(String bioTypeName, String attributeName){
		return true;
	}
	
	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue){	
	}
}