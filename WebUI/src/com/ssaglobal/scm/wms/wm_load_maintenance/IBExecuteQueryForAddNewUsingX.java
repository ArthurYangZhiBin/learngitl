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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;


public class IBExecuteQueryForAddNewUsingX extends ActionExtensionBase{

	protected int execute(ActionContext context, ActionResult result){	
		 	String action = (String)getParameter("action");
		 	Query loadBiosQry = null;
		 	if(action == null){		 		
		 		throw new EpiRuntimeException("", "parameter ACTION cannot be null");
		 	}
		 	if(action.equals("SHIPMENTORDERS")){
		 		String qry = "DPE('SQL','( @[wm_orders_lm_add_using_ship.STATUS] <> \\'95\\' AND (@[wm_orders_lm_add_using_ship.LOADID] IS NULL OR @[wm_orders_lm_add_using_ship.LOADID] = \\'\\' OR @[wm_orders_lm_add_using_ship.LOADID] = \\' \\') AND @[wm_orders_lm_add_using_ship.ORDERKEY] NOT IN (select pickdetail.orderkey from pickdetail))')";
		 		loadBiosQry = new Query("wm_orders_lm_add_using_ship", qry, null);
		 	}else if(action.equals("FLOWTHRUORDER")){
		 		String qry = "DPE('SQL','((@[wm_xorders_lm_add_using_fto.LOADID] IS NULL OR @[wm_xorders_lm_add_using_fto.LOADID] = \\'\\' OR @[wm_xorders_lm_add_using_fto.LOADID] = \\' \\') AND @[wm_xorders_lm_add_using_fto.ORDERKEY] NOT IN (select pickdetail.orderkey from pickdetail))')";
		 		loadBiosQry = new Query("wm_xorders_lm_add_using_fto", qry, null);
		 	}else if(action.equals("TRANSSHIP")){
		 		String qry = "DPE('SQL','(@[wm_transship_lm.STATUS] <> \\'9\\' AND (@[wm_transship_lm.LOADID] IS NULL OR @[wm_transship_lm.LOADID] = \\'\\' OR @[wm_transship_lm.LOADID] = \\' \\') AND NOT EXISTS (select * from loadorderdetail where loadorderdetail.transshipcontainerid = @[wm_transship_lm.CONTAINERID]))')";
		 		loadBiosQry = new Query("wm_transship_lm", qry, null);
		 	}else if(action.equals("TRANSSHIPASN")){
		 		String qry = "DPE('SQL','(@[wm_transasn_lm.STATUS] <> \\'9\\' AND @[wm_transasn_lm.VERIFYFLG] = \\'1\\' AND NOT EXISTS (select * from loadorderdetail where loadorderdetail.transshipcontainerid = @[wm_transasn_lm.PALLETID]) AND NOT EXISTS (select * from loadorderdetail where loadorderdetail.transasnkey = @[wm_transasn_lm.TRANSASNKEY]))')";
		 		loadBiosQry = new Query("wm_transasn_lm", qry, null);
		 	}else{
		 		throw new EpiRuntimeException("", "parameter ACTION is invalid, value is:"+action);
		 	}
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();							
			result.setFocus((DataBean)uow.getBioCollectionBean(loadBiosQry));
			return RET_CONTINUE;

	}
}