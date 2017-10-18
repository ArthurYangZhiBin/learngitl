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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

//Import 3rd party packages and classes
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.SessionUtil;

public class InternalTransferSerialNumberCalc extends ActionExtensionBase{
	protected static String SELECTED = "SELECTED";
	protected static String QTYSELECT = "QTYSELECT";
	
	protected int execute(ActionContext context, ActionResult result){
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getCurrentRuntimeForm().getParentForm(state);
		ArrayList selected = listForm.getAllSelectedItems();
		Double qty = 0.0;
		for(int index=0; index<selected.size(); index++){
			BioBean bio = (BioBean)selected.get(index);
			qty += Double.parseDouble(bio.get("QTY").toString());
			selected.set(index, bio.getBioRef().getBioRefString());
		}
		SessionUtil.setInteractionSessionAttribute(SELECTED, selected, state);
		SessionUtil.setInteractionSessionAttribute(QTYSELECT, ""+qty, state);
		return RET_CONTINUE;
	}
}