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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class QueryPO extends ActionExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(QueryPO.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
    	EpnyControllerState EpnyContState = (EpnyControllerState) state;
        HttpSession session = EpnyContState.getRequest().getSession();
		//Get the storer from the Headcer
		String shellSlot1 ="list_slot_1";
		String listShellForm = "wms_list_shell";
		String value = null;
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		while(!(shellForm.getName().equals(listShellForm))){
			shellForm = shellForm.getParentForm(state);
		}
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+shellForm.getName(),100L);
		SlotInterface slot1 = shellForm.getSubSlot(shellSlot1);
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+slot1.getName(),100L);
		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+headerForm.getName(),100L);
		SlotInterface headerTbgrpSlot = headerForm.getSubSlot("tbgrp_slot");
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+headerTbgrpSlot.getName(),100L);
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, "tab 0");			
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+normalHeaderForm.getName(),100L);
		value = normalHeaderForm.getFormWidgetByName("STORERKEY").getDisplayValue();
		Object ExpDate = normalHeaderForm.getFocus().getValue("EXPECTEDRECEIPTDATE");
//		Object ExpDate = normalHeaderForm.getFormWidgetByName("EXPECTEDRECEIPTDATE");
		session.setAttribute("EXPDATE",ExpDate);
		_log.debug("LOG_SYSTEM_OUT","STORERKEY = "+ value,100L);
		_log.debug("LOG_SYSTEM_OUT","EXPDATE = "+ ExpDate,100L);
		//Executes only if detail form is a new screen
		String qry = "";
		if (value != null && !"".equalsIgnoreCase(value) &&
				!" ".equalsIgnoreCase(value)){
			qry = "wm_po.STORERKEY = '"+value.toUpperCase()+"' and (wm_po.STATUS != '11' AND wm_po.STATUS != '15' AND wm_po.STATUS != '20' AND wm_po.STATUS != '9')";
		}else{
			qry = "wm_po.STATUS != '11' AND wm_po.STATUS != '15' AND wm_po.STATUS != '20' AND wm_po.STATUS != '9'";
		}
		_log.debug("LOG_SYSTEM_OUT","This is the query: "+qry,100L);
		Query loadBiosQry = new Query("wm_po", qry, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		int size = newFocus.size();
		if (value == null){
			newFocus.setEmptyList(true);
		}
		result.setFocus(newFocus);
		return RET_CONTINUE;
		}
}