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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class WOMPreAllocateActionValidation extends ActionExtensionBase{
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String STATUS = "STATUS";
	private final static String CREATED = "50";
	private final static String ERROR_MESSAGE = "WMEXP_NO_ACCESS_VIA_FORM_CONTEXT";
	private final static String ERROR_MESSAGE_UNSAVED = "WMEXP_WOM_UNSAVED_BIO";
	private final static String ERROR_MESSAGE_CREATED = "WMEXP_WOM_NOT_CREATED_STATUS";
	protected int execute(ActionContext context, ActionResult result) throws FormException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		DataBean header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null).getFocus();
		if(!header.isBioCollection()){
			if(header.isTempBio()){
				throw new FormException(ERROR_MESSAGE_UNSAVED, null);
			}else{
				BioBean bio = (BioBean)header;
				String status = bio.get(STATUS).toString();
				if(!status.equals(CREATED)){
					throw new FormException(ERROR_MESSAGE_CREATED, null);
				}
			}
		}else{
			throw new FormException(ERROR_MESSAGE, null);
		}
		return RET_CONTINUE;
	}
}