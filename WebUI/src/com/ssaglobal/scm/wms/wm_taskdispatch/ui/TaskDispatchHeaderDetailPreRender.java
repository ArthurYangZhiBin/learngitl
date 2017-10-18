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
package com.ssaglobal.scm.wms.wm_taskdispatch.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;

public class TaskDispatchHeaderDetailPreRender extends FormExtensionBase{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException{
		StateInterface state = context.getState();

		//get header data
		RuntimeFormInterface headerForm = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_1"), null);

		if(!(headerForm.getFocus().isTempBio())){
			headerForm.getFormWidgetByName("TTMSTRATEGYKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			headerForm.getFormWidgetByName("INTERLEAVETASKS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			headerForm.getFormWidgetByName("DESCR").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		return RET_CONTINUE;
    }
}