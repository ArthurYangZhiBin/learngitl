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
package com.ssaglobal.scm.wms.wm_user_order_performance.computed;

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.QueryableComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.bio.BioType;

public class UOPPerformancePercent implements QueryableComputedAttributeSupport {

	public String expression(String path, BioType bio, String attributeName) {
		return path + ".PERFORMANCE * 100";
	}

	public boolean supportsExpression(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		BigDecimal performanceObject = bio.get("PERFORMANCE") instanceof BigDecimal ? (BigDecimal) bio.get("PERFORMANCE") : new BigDecimal(0);
		BigDecimal per = new BigDecimal(100);

		return performanceObject.multiply(per);
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
