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

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
public class SORefreshStrategies extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SORefreshStrategies.class);
	private final static String SHELL_SLOT = "list_slot_1";

	private final static String TAB_GROUP_SLOT = "tbgrp_slot";

	private final static String TAB_0 = "tab 0";

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		//Initialize local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null).getSubSlot(TAB_GROUP_SLOT), TAB_0);
		String orderKey = ((BioBean) header.getFocus()).get("ORDERKEY").toString();
		String seller = ((BioBean) header.getFocus()).get("STORERKEY").toString();
  
		//Find related detail records
		String queryString = "wm_orderdetail.ORDERKEY = '" + orderKey + "'";
		Query query = new Query("wm_orderdetail", queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(query);
		for (int index = 0; index < list.size(); index++)
		{
			//Find item
			Bio temp = list.elementAt(index);
			String item = temp.get("SKU").toString();
			
			queryString = "sku.SKU = '" + item + "' and sku.STORERKEY = '" + seller +"'";
			
			query = new Query("sku", queryString, null);
			BioCollectionBean skuList = uowb.getBioCollectionBean(query);
			
			//Find strategy key
			Bio skuRecord = skuList.elementAt(0);
			String strategyKey = skuRecord.get("STRATEGYKEY").toString();
			
			
			
			queryString = "wm_strategy.STRATEGYKEY = '" + strategyKey + "'";
			query = new Query("wm_strategy", queryString, null);
			BioCollectionBean strategyList = uowb.getBioCollectionBean(query);
			Bio strategy = strategyList.elementAt(0);
			
			
			temp.set("ALLOCATESTRATEGYKEY", strategy.get("ALLOCATESTRATEGYKEY"));
			temp.set("PREALLOCATESTRATEGYKEY", strategy.get("PREALLOCATESTRATEGYKEY"));
			_log.debug("LOG_DEBUG_EXTENSION", "UPDATED ORDER DETAIL " + (index + 1), SuggestedCategory.NONE);
		}
		
		uowb.saveUOW();
		return RET_CONTINUE;
	}
}