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
package com.ssaglobal.scm.wms.wm_zone.ui;

import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;

public class ZoneEFZPreRender extends FormWidgetExtensionBase{
	protected int execute(StateInterface state, RuntimeFormWidgetInterface newWidget){
		RuntimeFormInterface newForm = newWidget.getForm();
		DataBean focus = newForm.getFocus();
		if(focus.isTempBio()){
			QBEBioBean bio = (QBEBioBean)focus;
			RuntimeFormInterface tabForm = newForm.getParentForm(state).getParentForm(state);
			RuntimeFormWidgetInterface widget = state.getRuntimeForm(tabForm.getSubSlot("tbgrp_slot"), "tab 0").getFormWidgetByName("PUTAWAYZONE");
			if(widget!=null){
				bio.set(newWidget.getName(), widget.getDisplayValue());	
			}else{
				bio.set(newWidget.getName(), "");
			}
		}
		return RET_CONTINUE;
	}
	
}