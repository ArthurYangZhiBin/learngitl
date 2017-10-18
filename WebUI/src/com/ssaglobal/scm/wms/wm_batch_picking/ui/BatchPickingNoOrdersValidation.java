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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;

public class BatchPickingNoOrdersValidation extends ActionExtensionBase{
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String TOGGLE_SLOT = "wm_batchpickingdetail_toggle_slot";
	private final static String TAB_ID = "List";
	private final static String BATCHFLAG = "BATCHFLAG";
	private final static String BATCH = "Batch Criteria";
	private final static String STATUS = "STATUS";
	private final static String ERROR_MESSAGE = "WMEXP_BATCH_NOORDERS";
	private final static String ERROR_MESSAGE_BATCHED = "WMEXP_BATCHED";
	private final static String ERROR_MESSAGE_UNBATCH_LOCKED = "WMEXP_UNBATCH_LOCKED";
	private final static String ERROR_MESSAGE_UNBATCHED = "WMEXP_UNBATCHED";
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		boolean isBatch = false;
		String source = context.getActionObject().getName();
		if(source.equals(BATCH)){
			isBatch = true;
		}
		RuntimeFormInterface toggleShell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface shell = toggleShell.getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		BioBean bio = (BioBean)header.getFocus();
		String flag = bio.get(BATCHFLAG).toString();
		if(isBatch){
			RuntimeFormInterface detailList = state.getRuntimeForm(toggleShell.getSubSlot(TOGGLE_SLOT), TAB_ID);
			BioCollectionBean list = (BioCollectionBean)detailList.getFocus();
			if(list.size()<1){
				throw new FormException(ERROR_MESSAGE, null);
			}
			if(flag.equals("1")){
				throw new FormException(ERROR_MESSAGE_BATCHED, null);
			}
		}else{
			String status = bio.get(STATUS).toString();
			if(status.equals("1") || status.equals("9")){
				throw new FormException(ERROR_MESSAGE_UNBATCH_LOCKED, null);
			}
			if(flag.equals("0")){
				throw new FormException(ERROR_MESSAGE_UNBATCHED, null);
			}
		}
		return RET_CONTINUE;
	}
}