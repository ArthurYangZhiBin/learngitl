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

import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
//import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
//import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ValidateBeforeSave extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
//		_log.debug("LOG_SYSTEM_OUT","it is in HeaderDetailSave******DDDDkkkkkkkkkkkvvvvvv",100L); 		
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		
		StateInterface state = context.getState();
//		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1

		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		if(headerFocus.isTempBio()){												
			if(headerFocus.getValue("TYPE") != null){
				if(headerFocus.getValue("TYPE").toString().equals("3")){
					throw new UserException("WMEXP_VALIDATETYPE_2", new Object[]{});
				}
			}
		}
		//SRG.b
		//Charge Header
		else{
			if(!headerFocus.isBioCollection()){//bug 533
				ArrayList<String> ChargeListTabs = new ArrayList<String>();
				ChargeListTabs.add("tab 9");
				RuntimeFormInterface ChargeListForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_receipt_billing_chargecodes_list_view", ChargeListTabs, state);
				if((ChargeListForm.getFocus()instanceof BioCollectionBean)){
					BioCollectionBean chargeBioCol = (BioCollectionBean)ChargeListForm.getFocus();
					for (int i = 0; i < chargeBioCol.size(); i++){
						BioBean chargeRecord = chargeBioCol.get("" + i);
						if (chargeRecord.hasBeenUpdated("BILL_TO_CUST")){
							verifyStorer((String)chargeRecord.get("BILL_TO_CUST",false),4);
							chargeRecord.set("BILL_TO_CUST", chargeRecord.get("BILL_TO_CUST",false).toString().toUpperCase());
						}
					}
				}
				RuntimeFormInterface detailTab= null;
				RuntimeFormInterface ChargeDetailListForm = null;
				RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot(shellSlot2), null);		
				int formNum = state.getSelectedFormNumber(detailForm.getSubSlot(toggleFormSlot));		
				detailTab = state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), formNum);		
				if(!(detailTab.getFocus()instanceof BioCollectionBean)){
					SlotInterface detailTabGrpSlot = detailTab.getSubSlot("tbgrp_slot");
					ChargeDetailListForm = state.getRuntimeForm(detailTabGrpSlot, "tab 9");
					if(ChargeDetailListForm.getFocus() instanceof BioCollectionBean){
						BioCollectionBean chargeDetailBioCol = (BioCollectionBean)ChargeDetailListForm.getFocus();
						for (int i = 0; i < chargeDetailBioCol.size(); i++){
							BioBean chargeDetRecord = chargeDetailBioCol.get("" + i);
							if (chargeDetRecord.hasBeenUpdated("BILL_TO_CUST")){
								verifyStorer((String)chargeDetRecord.get("BILL_TO_CUST",false),4);
								chargeDetRecord.set("BILL_TO_CUST", chargeDetRecord.get("BILL_TO_CUST",false).toString().toUpperCase());				
							}
						}
					}
				}
			}
		}
		//SRG.e
		result.setFocus(headerFocus);
		return RET_CONTINUE;
	}
	//SRG.b
	public void verifyStorer(String attributeValue, int type) throws DPException, UserException {
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM STORER WHERE (STORERKEY = '"+attributeValue+"') AND (TYPE = '"+type+"')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() != 1) {
			String[] parameters = new String[1];
			parameters[0] = attributeValue;
			throw new UserException("WMEXP_INVALID_BILLTOCUST", parameters);
		}
	}
	//SRG.e
}
