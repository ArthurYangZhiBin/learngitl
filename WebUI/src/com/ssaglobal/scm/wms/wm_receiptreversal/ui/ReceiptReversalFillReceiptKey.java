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

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;


public class ReceiptReversalFillReceiptKey implements ComputedAttributeSupport {
	//Static table names
	private static final String TABLE = "wm_receiptreversaldetail";
	
	//Static attribute names
	private static final String ADJ = "ADJUSTMENTKEY";
	private static final String ASN = "RECEIPTKEY";
    
	public ReceiptReversalFillReceiptKey(){
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException{
    	String adjKey = bio.get(ADJ).toString();
		String queryString = TABLE+"."+ADJ+"='"+adjKey.toUpperCase()+"'";
		Query qry = new Query(TABLE, queryString, null);
		BioCollection bc = bio.getBioCollection("RECEIPTREVERSALDETAIL");
		Bio first = bc.filterFirst(qry);
		String asnKey = first.get(ASN).toString();
		return asnKey;
    }

    public boolean supportsSet(String bioTypeName, String attributeName){
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag) throws EpiDataException{
    }
}