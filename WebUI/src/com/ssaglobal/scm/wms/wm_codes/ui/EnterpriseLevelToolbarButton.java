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
package com.ssaglobal.scm.wms.wm_codes.ui;

import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import javax.servlet.http.HttpSession;

public class EnterpriseLevelToolbarButton extends FormExtensionBase{
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		
		String nb = "NEW", db = "DELETE";
		Object isEnterprise = null;
		RuntimeFormWidgetInterface newButton = form.getFormWidgetByName(nb);
		RuntimeFormWidgetInterface deleteButton = form.getFormWidgetByName(db);
		
		//Determine database
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		
		isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);
		if(isEnterprise==null){
			isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE);
		}
		
		//Hide new and delete buttons if not in enterprise level
		if(!(isEnterprise.toString().equals("1"))){
			newButton.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			deleteButton.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		return RET_CONTINUE;
	}
}