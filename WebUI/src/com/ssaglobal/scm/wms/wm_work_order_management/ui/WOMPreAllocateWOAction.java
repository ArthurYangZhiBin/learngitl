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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class WOMPreAllocateWOAction extends ActionExtensionBase{
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String WOID = "WORKORDERID";
	private final static String PROC_NAME = "NSPWOPREALLOCATE";
	private final static String ERROR_MESSAGE_STORED_PROC_FAILED = "WMEXP_STORED_PROC_FAILED";
	protected int execute(ActionContext context, ActionResult result) throws FormException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		DataBean header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null).getFocus();
		BioBean bio = (BioBean)header;
		String workOrderID = bio.get(WOID).toString();
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(workOrderID));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName(PROC_NAME);
		try{
			WmsWebuiActionsImpl.doAction(actionProperties);
		}catch(Exception e){
			throw new FormException(ERROR_MESSAGE_STORED_PROC_FAILED, null);
		}
		result.setFocus(bio);
		return RET_CONTINUE;
	}
}