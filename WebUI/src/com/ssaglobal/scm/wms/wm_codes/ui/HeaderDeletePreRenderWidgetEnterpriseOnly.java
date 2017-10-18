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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class HeaderDeletePreRenderWidgetEnterpriseOnly extends FormWidgetExtensionBase{
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget ){
		Object isEnterprise = null;
		HttpSession session = state.getRequest().getSession();
		EpnyUserContext userContext = state.getServiceManager().getUserContext();
		
		isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);
		if(isEnterprise==null){
			isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE);
		}
		
		if(isEnterprise.toString().equals("1")){
			DataBean headerFocus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_1"), null).getFocus();
			if(headerFocus.isBioCollection()){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			}else{
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
		}

		return RET_CONTINUE;
	}
}