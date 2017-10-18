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
package com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InventoryReqToCountPostSave extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryReqToCountPostSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Executing InventoryReqToCountPostSave",100L);
	  	
	  	StateInterface state = context.getState();
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

	  	DataBean focus = detailForm.getFocus();
	  	
	  	if(!(focus.isTempBio()))
	  	detailForm.getFormWidgetByName("REQUESTKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
	  	
	  	 _log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Exiting InventoryReqToCountPostSave",100L);
	  	return RET_CONTINUE;
	}
}
