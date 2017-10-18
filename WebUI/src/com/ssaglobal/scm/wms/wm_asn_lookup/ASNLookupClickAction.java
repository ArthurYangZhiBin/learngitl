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
package com.ssaglobal.scm.wms.wm_asn_lookup;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ASNLookupClickAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNLookupClickAction.class);
	protected int execute(ActionContext context, ActionResult result){
		_log.debug("LOG_SYSTEM_OUT","*******it is in Link Item Click$$*****",100L);
		try
		{
			DataBean receiptdetailBio = result.getFocus();
			String qry = "receipt.RECEIPTKEY = '" + receiptdetailBio.getValue("RECEIPTKEY") +"'" ;			
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("receipt",qry,null));			
			result.setFocus(collection);
			return RET_CONTINUE;
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}

}
}
