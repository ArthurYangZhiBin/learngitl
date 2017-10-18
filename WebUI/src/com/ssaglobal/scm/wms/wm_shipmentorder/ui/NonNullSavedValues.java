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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
//Krishna Kuchipudi: SCM-00000-06032: This import statement (com.epiphany.shr.metadata.interfaces.SlotInterface) is added

public class NonNullSavedValues extends ActionExtensionBase{
	public NonNullSavedValues(){
	}
	/*
	 * Krishna Kuchipudi: SCM-00000-06032: Modified execute method for SCM-000000-06032 changes.
	 * In Oracle, '' is treated as null and will not get inserted if the 
	 * DB field is a Not null field. DropId is a not null field. 
	 * 
     */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		/*
		 * Dec-03-2008 Krishna Kuchipudi: SCM-00000-06032: Modified this method for SCM-000000-06032 changes.
		 *  
	     */
		StateInterface state = context.getState();
		RuntimeFormInterface headerForm, detailForm, toggleForm, shellForm, miscForm,
			shipToForm, billToForm, lottableForm, loadingForm;
		//SM 07/24/07 Issue 6893: Modified form retrieval to correspond to allocation save validation changes	
		shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		//end update
		headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		// Bugaware 8569. Shipment Order usability - fields not editable from list view
		if (headerForm.getFocus() != null && headerForm.getFocus() instanceof BioCollectionBean) {
			// saving from initial list view
			// nothing to do here
			return RET_CONTINUE;
		}
		// End Bugaware 8569
		shipToForm = state.getRuntimeForm(headerForm.getSubSlot("tbgrp_slot"), "tab 1");
		billToForm = state.getRuntimeForm(headerForm.getSubSlot("tbgrp_slot"), "tab 2");
		miscForm = state.getRuntimeForm(headerForm.getSubSlot("tbgrp_slot"), "tab 3");
		loadingForm = state.getRuntimeForm(headerForm.getSubSlot("tbgrp_slot"), "tab 5");
		toggleForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);	

		if(toggleForm.getName().equals("wm_shipmentorderdetail_toggle_view")){
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_shipmentorderdetail_toggle_view"), "wm_shipmentorderdetail_detail_view");
		}else{
			detailForm=toggleForm;
		}
		if(!(detailForm.getName().equals("Blank"))){
			DataBean detailFocus = detailForm.getFocus();
			lottableForm = state.getRuntimeForm(detailForm.getSubSlot("tbgrp_slot"), "tab 2");
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE01"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE02"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE03"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE06"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE07"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE08"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE09"), detailFocus);
			checkNull(lottableForm.getFormWidgetByName("LOTTABLE10"), detailFocus);
		}

		DataBean headerFocus = headerForm.getFocus();
		checkNull(miscForm.getFormWidgetByName("PACKINGLOCATION"), headerFocus);
		checkNull(loadingForm.getFormWidgetByName("ROUTE"), headerFocus);
		checkNull(loadingForm.getFormWidgetByName("DOOR"), headerFocus);
		//jp.sdis.7317.begin
		//checkNull(loadingForm.getFormWidgetByName("STOP"), headerFocus);
		//jp.sdis.7317.end
		checkNull(billToForm.getFormWidgetByName("BILLTOKEY"), headerFocus);
		checkNull(shipToForm.getFormWidgetByName("CONSIGNEEKEY"), headerFocus);
		/*
		 * Krishna Kuchipudi: SCM-00000-06032: Added the below code for SCM-000000-06032 changes.
		 * In Oracle, '' is treated as null and will not get inserted if the 
		 * DB field is a Not null field. DropId is a not null field in pickdetail table. 
		 * So Added the below code, so " " will get inserted in DropId if user didnt select any value in UI.
		 * SCM-00000-06032  related changes starts now
	     */
		RuntimeFormInterface pdtoggleForm = null, pdForm = null;
		pdtoggleForm = state.getRuntimeForm(headerForm.getSubSlot("tbgrp_slot"), "tab 8");
		if(pdtoggleForm != null)
		{
			SlotInterface pdToggleSlot = pdtoggleForm.getSubSlot("wm_shipmentorder_pickdetail_toggle_view");
			if(pdToggleSlot != null)
			{
				RuntimeFormInterface tempForm1 = state.getRuntimeForm(pdToggleSlot, "wm_shipmentorderdetail_detail_view");
				if (tempForm1 != null)
				{
					SlotInterface tempSlot1 = tempForm1.getSubSlot("tbgrp_slot");
					if(tempSlot1 != null)
						pdForm = state.getRuntimeForm(tempSlot1, "tab 1");
				}					
			}			
		}			
		if(pdForm != null)
		{
			String value = pdForm.getFormWidgetByName("DROPID").getDisplayValue();
			DataBean focus = pdForm.getFocus();			
			if(value == null || value.trim().length() == 0)
				focus.setValue("DROPID", " ");			
		}
		 //Krishna Kuchipudi: SCM-00000-06032  related changes end now
		return RET_CONTINUE;
	}
	
	public void checkNull(RuntimeFormWidgetInterface widget, DataBean focus){
		QBEBioBean qbe = null;
		BioBean bio = null;
		if(focus.isTempBio()){
			qbe = (QBEBioBean)focus;
			if(widget.getDisplayValue()==null){
				qbe.set(widget.getName(), " ");
			}
		}else{
			bio = (BioBean)focus;
			if(widget.getDisplayValue()==null){
				bio.set(widget.getName(), " ");
			}
		}

	}

}