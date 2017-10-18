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
package com.infor.scm.waveplanning.wp_waveworkbench.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;

public class WaveworkbenchToggleHelp extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveworkbenchToggleHelp.class);

	protected int execute(ActionContext context, ActionResult arg1) throws EpiException {
		boolean doDisplay = getParameterBoolean("doDisplay");
		StateInterface state = context.getState();	

		RuntimeFormInterface toolbarForm = (RuntimeFormInterface)WPFormUtil.findForm(
				state.getCurrentRuntimeForm(), "", "wp_list_shell_waveworkbench Toolbar", state);		
		
		RuntimeFormWidgetInterface helpWidget = toolbarForm.getFormWidgetByName("HELP");
			
		if(doDisplay) {
			helpWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
		}
		else {
			helpWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}

		return RET_CONTINUE;
	}
}
