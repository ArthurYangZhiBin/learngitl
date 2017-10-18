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
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class LoadLPNfromReceiptDetailOnNew extends FormExtensionBase{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException{
		DataBean currentFormFocus = context.getState().getFocus();
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface ToggleForm = form.getParentForm(context.getState());
		RuntimeFormInterface tabGroupForm = (ToggleForm.getParentForm(context.getState()));
		SlotInterface tabGroupSlot = tabGroupForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface receiptDetailForm = context.getState().getRuntimeForm(tabGroupSlot, "tab 0");
		DataBean receiptDetailFocus = receiptDetailForm.getFocus();
		if(receiptDetailFocus instanceof BioBean){
			receiptDetailFocus = (BioBean)receiptDetailFocus;
		} else if(receiptDetailFocus instanceof QBEBioBean){
			receiptDetailFocus = (QBEBioBean)receiptDetailFocus;
		}
		RuntimeFormInterface headerForm = FormUtil.findForm(form,"wms_list_shell","receipt_detail_view",context.getState());
		DataBean headerFocus = headerForm.getFocus();
		if(headerFocus instanceof BioBean){
			headerFocus = (BioBean)headerFocus;
		} else if(headerFocus instanceof QBEBioBean){
			headerFocus = (QBEBioBean)headerFocus;
		}
		
		String receiptLineNum = receiptDetailFocus.getValue("RECEIPTLINENUMBER").toString();
		String toid = "";
		toid = receiptDetailFocus.getValue("TOID").toString();
		String owner = receiptDetailFocus.getValue("STORERKEY").toString();
		String item = receiptDetailFocus.getValue("SKU").toString();
		String packKey = receiptDetailFocus.getValue("PACKKEY").toString();
		String toloc = receiptDetailFocus.getValue("TOLOC").toString();
		String uom = receiptDetailFocus.getValue("UOM").toString();
		try {
			//Add your code here to process the event
			if(widgetName.equals("NEW")) {
				KeyGenBioWrapper lpnDetailKey = new KeyGenBioWrapper();
				currentFormFocus.setValue("LPNDETAILKEY",lpnDetailKey.getKey("LPNDETAILKEY"));
				currentFormFocus.setValue("RECEIPTLINENUMBER",receiptLineNum);
				currentFormFocus.setValue("PALLETID",toid);
				currentFormFocus.setValue("STORERKEY",owner);
				currentFormFocus.setValue("SKU",item);
				currentFormFocus.setValue("PACKKEY",packKey);
				currentFormFocus.setValue("TOLOC",toloc);
				currentFormFocus.setValue("UOM",uom);
			} else {
				String expectedQty = form.getFocus().getValue("QTYEXPECTED").toString();
				String receivedQty = form.getFocus().getValue("QTYRECEIVED").toString();
				String LPNpackKey = form.getFocus().getValue("PACKKEY").toString();
				String LPNuom = form.getFocus().getValue("UOM").toString();
				form.getFormWidgetByName("EXPECTEDQTY").setValue(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,LPNuom,expectedQty,LPNpackKey,context.getState(),UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				form.getFormWidgetByName("RECEIVEDQTY").setValue(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,LPNuom,receivedQty,LPNpackKey, context.getState(), UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

			}
		} catch(Exception e) {     
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 	
		return RET_CONTINUE;
	}
	
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws UserException {
		return RET_CONTINUE;
	}
	
}