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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.io.Serializable;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui.PreRenderReqToCount;

public class EnableActionButtonOnRunBillling extends com.epiphany.shr.ui.action.ActionExtensionBase 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EnableActionButtonOnRunBillling.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
	  	_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing EnableActionButtonOnRunBillling",100L);
		String toolBarForm = "wm_list_shell_invoice_processing Toolbar";
		
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();		
		RuntimeFormInterface shellForm = form.getParentForm(state);
		
		RuntimeFormInterface toolBar = FormUtil.findForm(form, shellForm.getName(), toolBarForm, state);
		
		toolBar.getFormWidgetByName("Actions").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
		
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting EnableActionButtonOnRunBillling",100L);
		return RET_CONTINUE;
	}
	
	
}
