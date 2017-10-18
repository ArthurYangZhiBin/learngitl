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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
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

public class FacilityTransferItemLookupQuery extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityTransferItemLookupQuery.class);
	//Specific query for an item lookup that only returns the selected owner 
	protected int execute(ActionContext context, ActionResult result)throws EpiException{

		String[] parameters = new String[1];
		parameters[0] = "Owner";
		String qry = "sku.STORERKEY = '";
		StateInterface state = context.getState();
		String shellSlot1 = "slot1";
		String listShellForm = "wm_list_shell_facilitytransfer";
		String value = null;
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
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, "Tab0");
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+normalHeaderForm.getName(),100L);
		value = normalHeaderForm.getFormWidgetByName("STORERKEY").getDisplayValue();
		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+value,100L);
		qry = qry + value + "'";
		Query loadBiosQry = new Query("sku", qry, null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		if(newFocus.size()<1){
			throw new FormException("SKU_VALIDATION", parameters);
		}
		result.setFocus(newFocus);		

		return RET_CONTINUE;
	}
}