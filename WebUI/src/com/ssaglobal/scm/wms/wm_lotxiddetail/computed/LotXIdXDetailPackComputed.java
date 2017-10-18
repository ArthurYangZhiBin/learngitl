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
package com.ssaglobal.scm.wms.wm_lotxiddetail.computed;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.util.BioUtil;

public class LotXIdXDetailPackComputed extends Object implements ComputedAttributeSupport {

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException {
		String sourceKey = BioUtil.getString(bio, "SOURCEKEY");
		//		String sourceLineNumber = BioUtil.getString(bio, "SOURCELINENUMBER");
		String sku = BioUtil.getString(bio, "SKU");
		String ioflag = BioUtil.getString(bio, "IOFLAG");
		String pack = "STD";

		//query based on IOFLAG
		//first, need to get storerkey
		String storerkey = null;
		//second, need to get pack
		UnitOfWork uow = bio.getUnitOfWork();
		if ("I".equals(ioflag)) {
			//inbound
			BioCollection parentRS = uow.findByQuery(new Query("receipt", "receipt" + ".RECEIPTKEY = '" + sourceKey + "'", null));
			for (int i = 0; i < parentRS.size(); i++) {
				storerkey = BioUtil.getString(parentRS.elementAt(i), "STORERKEY");
			}
		} else if ("O".equals(ioflag)) {
			//outbound
			BioCollection parentRS = uow.findByQuery(new Query("wm_orders", "wm_orders" + ".ORDERKEY = '" + sourceKey + "'", null));
			for (int i = 0; i < parentRS.size(); i++) {
				storerkey = BioUtil.getString(parentRS.elementAt(i), "STORERKEY");
			}

		} else {
			//IOFLAG isn't set, return DEFAULT STD
			return pack;
		}
		BioCollection skuRS = uow.findByQuery(new Query("sku", "sku.STORERKEY = '" + storerkey + "' and sku.SKU = '" + sku + "'",
				null));

		for (int i = 0; i < skuRS.size(); i++) {
			pack = BioUtil.getString(skuRS.elementAt(i), "PACKKEY");
		}

		return pack;
	}

	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
