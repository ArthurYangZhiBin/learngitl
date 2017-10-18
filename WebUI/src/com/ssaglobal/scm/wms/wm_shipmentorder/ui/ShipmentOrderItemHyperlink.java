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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.BioRef;

public class ShipmentOrderItemHyperlink extends ActionExtensionBase{
	private final static String TABLE = "wm_inventorybalanceslotxlocxlpn";
	private final static String OWNER = "STORERKEY";
	private final static String ITEM =  "SKU";
	private final static String QTY = "qty";
	private final static String OWNER_SMALL = "storerkey";
	private final static String ITEM_SMALL = "sku";
	private final static String ERROR_MESSAGE = "WMEXP_NODATAFORCHART";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String bioRefString = state.getBucketValueString("listTagBucket");
        BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
        BioBean bioBean = uowb.getBioBean(bioRef);
        String owner = (String) bioBean.get(OWNER);
        String item = (String) bioBean.get(ITEM);
		String queryString = TABLE+"."+OWNER_SMALL+"='"+owner+"' and "+TABLE+"."+ITEM_SMALL+"='"+item+"' and "+TABLE+"."+QTY+"!='0'";
		Query qry = new Query(TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if(list.size()<1){
			throw new FormException(ERROR_MESSAGE, null);
		}
		result.setFocus(list);
		return RET_CONTINUE;
	}
}