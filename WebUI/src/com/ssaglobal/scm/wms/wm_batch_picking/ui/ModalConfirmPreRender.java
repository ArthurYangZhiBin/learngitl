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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;

public class ModalConfirmPreRender extends FormWidgetExtensionBase{
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		//	Initialize local variables
		String ERROR_MESSAGE = getParameterString("sessionAttributeName");
		HttpSession session = state.getRequest().getSession();
		
		String message = session.getAttribute(ERROR_MESSAGE).toString();
		widget.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE, message);
		session.removeAttribute(ERROR_MESSAGE);
		return RET_CONTINUE;
	}
}