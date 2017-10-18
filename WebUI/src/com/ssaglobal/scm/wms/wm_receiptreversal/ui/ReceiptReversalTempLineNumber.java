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

import java.text.DecimalFormat;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ReceiptReversalTempLineNumber implements ComputedAttributeSupport {
	private static final String DEC_FORMAT = "00000";
	
	//Static attribute names
	private static final String KEY = "SERIALKEY";
	
    public ReceiptReversalTempLineNumber(){
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException{
		DecimalFormat decFormat = new DecimalFormat(DEC_FORMAT);
    	String lineNumber = null;
    	int current = Integer.parseInt(bio.get(KEY).toString());
    	String selectString = "SELECT MIN(SERIALKEY) FROM RR_TEMP";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(selectString);
		String baseNo = results.getAttribute(1).value.getAsString();
    	int base = Integer.parseInt(baseNo);
    	int temp = current-base+1;
    	lineNumber = decFormat.format(temp);
    	return lineNumber;
    }

    public boolean supportsSet(String bioTypeName, String attributeName){
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag) throws EpiDataException{
    }
}