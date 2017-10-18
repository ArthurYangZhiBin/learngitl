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
package com.ssaglobal.scm.wms.uiextensions;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;

public class HeaderDetailDeleteDisable extends ActionExtensionBase{
	public HeaderDetailDeleteDisable() {
	} 
	protected int execute(ActionContext context, ActionResult result) throws UserException 
	{
			StateInterface state = context.getState();			 
			RuntimeFormInterface shellListSlot1 = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = shellListSlot1.getParentForm(state);
			SlotInterface shellToolbarSlot = shellForm.getSubSlot("wms_list_shell Toolbar");
			RuntimeFormInterface toolbarForm = state.getRuntimeForm(shellToolbarSlot, null);
			RuntimeFormWidgetInterface delete = toolbarForm.getFormWidgetByName("DELETE");
			delete.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);	
		return RET_CONTINUE;	 
	}

}
