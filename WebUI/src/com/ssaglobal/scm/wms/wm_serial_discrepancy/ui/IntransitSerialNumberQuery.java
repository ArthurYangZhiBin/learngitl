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
package com.ssaglobal.scm.wms.wm_serial_discrepancy.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class IntransitSerialNumberQuery extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(IntransitSerialNumberQuery.class);

	protected int execute(ActionContext context, ActionResult result)throws EpiException{
		//05/17/2011 FW  Added Serial Transaction Discrepancy Screen (Def311963)
		StateInterface state = context.getState();
		
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		while(!(shellForm.getName().equals("wms_list_shell"))){
			shellForm = shellForm.getParentForm(state);
		}
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+shellForm.getName(),100L);
		SlotInterface slot1 = shellForm.getSubSlot("list_slot_1");
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+slot1.getName(),100L);
		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
		
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		
		String qry1 = null;
		
		String storer = headerForm.getFormWidgetByName("STORERKEY").getValue().toString();
		String sku = headerForm.getFormWidgetByName("SKU").getValue().toString();
		String lot = headerForm.getFormWidgetByName("LOT").getValue().toString();
		String loc = headerForm.getFormWidgetByName("LOC").getValue().toString();
		String id = headerForm.getFormWidgetByName("ID").getValue().toString();
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		qry1 =        " wm_itrn.STORERKEY = '" + storer + "'";
		qry1 = qry1 + " AND wm_itrn.SKU =  '" + sku + "'";
		qry1 = qry1 + " AND wm_itrn.LOT =  '" + lot + "'";
		qry1 = qry1 + " AND wm_itrn.TOLOC = '" + loc + "'";
		qry1 = qry1 + " AND wm_itrn.TOID = '" + id + "'";
		qry1 = qry1 + " AND wm_itrn.TRANTYPE = 'MV'";
		qry1 = qry1 + " AND wm_itrn.INTRANSIT = '0'";
		//qry1 = qry1 + " AND wm_itrn.TOLOC = 'INTRANSIT'";
		
		Query itrnQry = new Query("wm_itrn", qry1, "wm_itrn.EDITDATE DESC");  //order by editdate desc
		BioCollectionBean itrnQryFocus = uow.getBioCollectionBean(itrnQry);
		if(itrnQryFocus.size()<1){
			String[] parameters = new String[1];
			parameters[0] = "serial#";
			throw new FormException("EXP_ECS_CANNOT_FIND_FILE", parameters);  //not found in serial#
		}
		
		//Get fromlot, fromloc, fromid from itrn record with intransit = '0'
		String fromlot = itrnQryFocus.get("0").getString("LOT").toString();
		String fromloc = itrnQryFocus.get("0").getString("FROMLOC").toString();
		String fromid = itrnQryFocus.get("0").getString("FROMID").toString();
		String itrnKey = itrnQryFocus.get("0").getString("ITRNKEY").toString();
		
		qry1 = null;
		qry1 =        " wm_serialinventory.STORERKEY = '" + storer + "'";
		qry1 = qry1 + " AND wm_serialinventory.SKU = '" + sku + "'";
		qry1 = qry1 + " AND wm_serialinventory.LOT = '" + fromlot + "'";
		qry1 = qry1 + " AND wm_serialinventory.LOC = '" + fromloc + "'";
		qry1 = qry1 + " AND wm_serialinventory.ID = '" + fromid + "'";

		Query serialinventoryQry = new Query("wm_serialinventory", qry1, null);
		BioCollectionBean serialinventoryFocus = uow.getBioCollectionBean(serialinventoryQry);

		if(serialinventoryFocus.size()<1){
			String[] parameters = new String[1];
			parameters[0] = "Intransit record";
			throw new FormException("EXP_ECS_CANNOT_FIND_FILE", parameters);

		}
		result.setFocus(serialinventoryFocus);		

		return RET_CONTINUE;
		
	}
}