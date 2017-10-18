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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class ASNDetailUpdateLPNOnChange extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result){
		//Upon changing the LPN on the ASN Detail Record
		//Update any new LPN Details with appropriate LPN
		StateInterface state = context.getState();
//		QBEBioBean focus = (QBEBioBean)state.getCurrentRuntimeForm().getFocus();
		DataBean focus = state.getCurrentRuntimeForm().getFocus();
		RuntimeFormInterface tabForm = state.getCurrentRuntimeForm();
		while(!tabForm.getName().equals("wms_tbgrp_shell")){
			tabForm = tabForm.getParentForm(state);
		}
		SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
		SlotInterface toggleSlot = state.getRuntimeForm(tabSlot, "tab 6").getSubSlot("wm_LPNDETAIL");
		int formNo = state.getSelectedFormNumber(toggleSlot);
		if(formNo==1){
			String lpn = focus.getValue("TOID")==null ? null : focus.getValue("TOID").toString();
			RuntimeFormInterface lpnForm = state.getRuntimeForm(toggleSlot ,formNo);
			QBEBioBean qbe = (QBEBioBean)lpnForm.getFocus();
			qbe.set("PALLETID", lpn);
			lpnForm.getFormWidgetByName("PALLETID").setDisplayValue(lpn);
		}
		return RET_CONTINUE;
	}	
}