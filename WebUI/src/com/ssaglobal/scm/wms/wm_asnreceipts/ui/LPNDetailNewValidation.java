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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.SaveASNReceipt;

public class LPNDetailNewValidation extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		//Validate LPN exists for current ASN Detail record
		StateInterface state = context.getState();
		RuntimeFormInterface tabForm = context.getState().getCurrentRuntimeForm();
		while(!tabForm.getName().equals("wms_tbgrp_shell")){
			tabForm = tabForm.getParentForm(state);
		}
		DataBean focus = state.getRuntimeForm(tabForm.getSubSlot("tbgrp_slot"), "tab 0").getFocus();  
		Object lpn;
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			lpn = qbe.get("TOID");
			//Validate non-duplicate LPN when necessary 
			RuntimeFormInterface shellForm = tabForm.getParentForm(state);
			while(!shellForm.getName().equals("wms_list_shell")){
				shellForm = shellForm.getParentForm(state);
			}
			SaveASNReceipt.validateReceiptDetailLPN(focus, context);
		}else{
			BioBean bio = (BioBean)focus;
			lpn = bio.get("TOID");
		}
		if(lpn==null){
			throw new UserException("WMEXP_NEW_LPN_VALIDATION", new Object[0]);
		}
		return RET_CONTINUE;
	}
}