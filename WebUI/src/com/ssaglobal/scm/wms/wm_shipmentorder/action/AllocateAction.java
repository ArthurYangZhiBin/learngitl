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
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.Array;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

public class AllocateAction extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
//		Executes stored procedure name:NSPORDERPROCESSING params:orderkey, oskey, docarton, doroute, tblprefix, preallocateonly
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		String tabGroupSlot = "tbgrp_slot";
		String listSlot1 = null;
		String tabZero = null;
		if(shell.getName().equals("wm_list_shell_facilitytransfer")){
			listSlot1 = "slot1";
			tabZero = "Tab0";
		}else{
			listSlot1 = "list_slot_1";
			tabZero = "tab 0";
		}
		RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(listSlot1), null).getSubSlot(tabGroupSlot), tabZero);
		//RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(context.getSourceForm().getParentForm(state).getSubSlot("list_slot_1"), null).getSubSlot("tbgrp_slot"), "tab 0");
		DataBean focus = headerForm.getFocus();
		
		String orderKey=null, osKey="", doCarton="Y", doRoute="N", tblPrefix="", preallocateOnly="N";
		String[] parameters = new String[1];
		parameters[0] = null;
		orderKey = focus.getValue("ORDERKEY").toString();
		String batchFlag = focus.getValue("BATCHFLAG").toString();
		if(batchFlag.equals("1")){
			parameters[0]=orderKey;
		}else{
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			//Store parameters for stored procedure call
			params.add(new TextData(orderKey));
			params.add(new TextData(osKey));
			params.add(new TextData(doCarton));
			params.add(new TextData(doRoute));
			params.add(new TextData(tblPrefix));
			params.add(new TextData(preallocateOnly));
			//Set actionProperties for stored procedure call
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPORDERPROCESSING");
			try{
				//Run stored procedure
				WmsWebuiActionsImpl.doAction(actionProperties);
			}catch(WebuiException e){
				throw new UserException(e.getMessage(), new Object[] {});		
			}
		}
		if(parameters[0]!=null){
			throw new FormException("WMEXP_SO_AllocAction", parameters);
		}
		context.getState().getDefaultUnitOfWork().clearState(); //Clearing the UOW State seems like it forces a reload and updates the display
		return RET_CONTINUE;
	}
	
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException{
		//Executes stored procedure name:NSPORDERPROCESSING params:orderkey, oskey, docarton, doroute, tblprefix, preallocateonly
		StateInterface state = context.getState();
		RuntimeFormInterface shell = context.getSourceForm().getParentForm(state);
		String tabGroupSlot = "tbgrp_slot";
		String listSlot1 = null;
		String tabZero = null;
		if(shell.getName().equals("wm_list_shell_facilitytransfer")){
			listSlot1 = "slot1";
			tabZero = "Tab0";
		}else{
			listSlot1 = "list_slot_1";
			tabZero = "tab 0";
		}
		RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(listSlot1), null).getSubSlot(tabGroupSlot), tabZero);
		//RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(context.getSourceForm().getParentForm(state).getSubSlot("list_slot_1"), null).getSubSlot("tbgrp_slot"), "tab 0");
		DataBean focus = headerForm.getFocus();
		
		String orderKey=null, osKey="", doCarton="Y", doRoute="N", tblPrefix="", preallocateOnly="N";
		String[] parameters = new String[1];
		parameters[0] = null;
		orderKey = focus.getValue("ORDERKEY").toString();
		String batchFlag = focus.getValue("BATCHFLAG").toString();
		if(batchFlag.equals("1")){
			parameters[0]=orderKey;
		}else{
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			//Store parameters for stored procedure call
			params.add(new TextData(orderKey));
			params.add(new TextData(osKey));
			params.add(new TextData(doCarton));
			params.add(new TextData(doRoute));
			params.add(new TextData(tblPrefix));
			params.add(new TextData(preallocateOnly));
			//Set actionProperties for stored procedure call
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPORDERPROCESSING");
			try{
				//Run stored procedure
				WmsWebuiActionsImpl.doAction(actionProperties);
			}catch(WebuiException e){
				throw new UserException(e.getMessage(), new Object[] {});		
			}
		}
		if(parameters[0]!=null){
			throw new FormException("WMEXP_SO_AllocAction", parameters);
		}
		return RET_CONTINUE;
	}
}