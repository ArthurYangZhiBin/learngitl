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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreDeleteInternalTransfer extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreDeleteInternalTransfer.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PreDeleteInternalTransfer " ,100L);
		StateInterface state = context.getState();
		
		RuntimeListFormInterface listForm = null;
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		RuntimeFormInterface shellForm = form.getParentForm(state);
		RuntimeFormInterface headerForm;

		if(form.getName().equals("wm_list_shell_internal_transfer Toolbar"))
			headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		else
			headerForm = state.getRuntimeForm(shellForm.getSubSlot("wm_internal_transfer_detail_toggle_slot"), "List");
		
		if(headerForm.isListForm())
			listForm = (RuntimeListFormInterface)headerForm;
		else
			throw new FormException("WMEXP_NOT_LIST", null);

		ArrayList itemsSelected = listForm.getAllSelectedItems();
		if(itemsSelected != null){
			for(int i = 0; i < itemsSelected.size(); i++){
				DataBean bean = (DataBean)(itemsSelected.get(i));
				String statusVal = bean.getValue("STATUS").toString();
				String key= bean.getValue("TRANSFERKEY").toString();
				
				if(statusVal.equals("9")){
					String[] param= new String[1];
					param[0]= key;
					throw new UserException("WMEXP_DISABLE_DELETE_STATUS9", param);
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PreDeleteInternalTransfer " ,100L);
		return RET_CONTINUE;		
	}
}