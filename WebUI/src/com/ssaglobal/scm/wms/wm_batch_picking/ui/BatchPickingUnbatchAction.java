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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class BatchPickingUnbatchAction extends ActionExtensionBase{
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String UNBATCH = "UNBATCHORDER";
	private final static String WAVE = "WAVEKEY";

	protected int execute(ActionContext context, ActionResult result) throws UserException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		String waveKey = header.getFormWidgetByName(WAVE).getDisplayValue();
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(waveKey));	
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName(UNBATCH);
		try{
			WmsWebuiActionsImpl.doAction(actionProperties);	
		}catch (WebuiException e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}
		return RET_CONTINUE;
	}
}