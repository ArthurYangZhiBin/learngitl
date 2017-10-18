/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

/**
 * TODO Document SendOrderToTM class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class SendOrderToTM extends ActionExtensionBase{
	private final static String NAV_SINGLE = "singleSelectMCE";
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		String tabGroupSlot = "tbgrp_slot";
		String listSlot1 = "list_slot_1";
		String tabZero = "tab 0";
		RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(listSlot1), null).getSubSlot(tabGroupSlot), tabZero);
		//RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(context.getSourceForm().getParentForm(state).getSubSlot("list_slot_1"), null).getSubSlot("tbgrp_slot"), "tab 0");
		DataBean focus = headerForm.getFocus();
		//defect1250.b
		if (focus instanceof QBEBioBean){
			throw new UserException("WMEXP_SO_SAVE_FIRST", new Object[] {});
		}
		//defect1250.e
		focus.setValue("CARRIERROUTESTATUS", "NEW");
		uow.saveUOW(false);		
		uow.clearState(); 
		context.setNavigation(NAV_SINGLE);
		return RET_CONTINUE;
	}
}
