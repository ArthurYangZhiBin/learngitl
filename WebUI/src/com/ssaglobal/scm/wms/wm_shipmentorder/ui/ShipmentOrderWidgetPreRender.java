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

import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;

public class ShipmentOrderWidgetPreRender extends FormWidgetExtensionBase{
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException{

		//Boolean flags that determine which conditions disable the widget
		boolean statusEquals95 = getParameterBoolean("statusEquals95");
		boolean typeEquals20 = getParameterBoolean("typeEquals20");
		boolean shippedAndOpenQty = getParameterBoolean("shippedAndOpenQty");
		boolean isSubstitute = getParameterBoolean("isSubstitute");
		boolean qtyAllocated = getParameterBoolean("qtyAllocated");
		boolean qtyPicked = getParameterBoolean("qtyPicked");
		boolean externalLot = getParameterBoolean("externalLot");
		boolean shippedQtyOnly = getParameterBoolean("shippedQtyOnly");
//		boolean allocateduomqty = getParameterBoolean("allocateduomqty");
		boolean showMinShipPercent = getParameterBoolean("showMinShipPercent");
		boolean isPackLocation = getParameterBoolean("isPackLocation");
		boolean done = false;
		
		DataBean focus = state.getFocus();
		if(widget.getName().equals("NEW") || widget.getName().equals("SAVE")){
			RuntimeFormInterface shellForm = widget.getForm().getParentForm(state).getParentForm(state);
			RuntimeFormInterface headerForm = null;
			if(shellForm.getName().equals("wm_list_shell_shipmentorder")){
				headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
			}else{
				while(!(shellForm.getName().equals("wm_tbgrp_shell_shipmentorder"))){
					shellForm = shellForm.getParentForm(state);
				}
				headerForm = state.getRuntimeForm(shellForm.getSubSlot("tbgrp_slot"), "tab 0");
			}
			DataBean headerFocus = headerForm.getFocus();
			Object value = headerFocus.getValue("STATUS");
			if(value!=null){
				String statusWidget = value.toString();
				if(statusWidget.equals("95")){
					done = disableWidget(widget);
				}
			}
		}
		if(focus.isBio()){
			if(statusEquals95){
				RuntimeFormWidgetInterface statusWidget = widget.getForm().getFormWidgetByName("STATUS");
				String status = statusWidget.getValue().toString();
				if(status.equals("95")){
					done = disableWidget(widget);
				}
			}
			if(typeEquals20 && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface typeWidget = headerForm.getFormWidgetByName("TYPE");
				String type = typeWidget.getValue().toString();
				if(type.equals("20")){
					done = disableWidget(widget);
				}
			}
			if(shippedAndOpenQty && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface openQtyWidget = headerForm.getFormWidgetByName("OPENQTY");
				double openQty = Double.parseDouble(openQtyWidget.getValue().toString());
				RuntimeFormWidgetInterface shippedQtyWidget = headerForm.getFormWidgetByName("SHIPPEDQTY");
				double shippedQty = Double.parseDouble(shippedQtyWidget.getValue().toString());
				if(openQty==0 && shippedQty>0){
					done = disableWidget(widget);
				}
			}
			if(isSubstitute && !done){
				String isSubstituteValue = null;
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean)focus;
					isSubstituteValue = qbe.get("ISSUBSTITUTE").toString();
				}else{
					BioBean bio = (BioBean)focus;
					isSubstituteValue = bio.get("ISSUBSTITUTE").toString();
				}
				if(!(isSubstituteValue.equals("0"))){
					done = disableWidget(widget);
				}
			}
			if(qtyAllocated && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface qtyAllocatedWidget = headerForm.getFormWidgetByName("QTYALLOCATED");
				double qtyAllocatedValue = Double.parseDouble(qtyAllocatedWidget.getValue().toString());
				if(qtyAllocatedValue>0){
					done = disableWidget(widget);
				}
			}
			if(qtyPicked && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface qtyPickedWidget = headerForm.getFormWidgetByName("QTYPICKED");
				double qtyPickedValue = 0;
				if(qtyPickedWidget!=null){
					qtyPickedValue = Double.parseDouble(qtyPickedWidget.getValue().toString());
				}
				if(qtyPickedValue>0){
					done = disableWidget(widget);
				}
			}
			if(externalLot && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				String temp = null;
				try{
					if(temp==headerForm.getFormWidgetByName("EXTERNALLOT").getDisplayValue()){
						//Leave unlocked
					}else if(headerForm.getFormWidgetByName("EXTERNALLOT").getDisplayValue().equalsIgnoreCase("")){
						//Leave unlocked
					}
					else{
						//Disable widget
						done = disableWidget(widget);

					}
				}catch(Exception e){
					e.printStackTrace();
					return RET_CANCEL;
				}

			}
/*			if(allocateduomqty && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface statusWidget = headerForm.getFormWidgetByName("ALLOCATEDUOMQTY");
				double status = Double.parseDouble(statusWidget.getValue().toString());
				if(status!=0){
					done = disableWidget(widget);
				}
			}*/
			if(shippedQtyOnly && ! done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface shippedQtyWidget = headerForm.getFormWidgetByName("SHIPPEDQTY");
				double shippedQty = Double.parseDouble(shippedQtyWidget.getValue().toString());
				if(shippedQty>0){
					done = disableWidget(widget);
				}
			}
		}
		//AW 03/29/10 Incident3253437_Defect217130
/*		if(showMinShipPercent && !done){
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			String queryString = "wm_system_settings.CONFIGKEY='ERPINTERFACE'";
			Query qry = new Query("wm_system_settings", queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			String toggle = list.get("0").get("NSQLVALUE").toString();
			if(toggle.equals("0")){
				done = disableWidget(widget);
			}
		}*/
		if(isPackLocation && !done){
			String value = widget.getForm().getFormWidgetByName("ENABLEPACKING").getDisplayValue();
			if(value.equalsIgnoreCase("1")){
				if(widget.getType().equals("image button") || widget.getName().equals("NEW") || widget.getName().equals("MINSHIPPERCENT")){
					widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}
				//Sets all other widgets to read only
				else{
					widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					widget.setProperty("input type", "required");
				}
			}else{
				done = disableWidget(widget);
				widget.setProperty("input type", "normal");
			}
		}
		// Amar: Merged the code changes done for fixing the SDIS issue SCM-00000-05674 Machine 2137502 
		if(widget.getName().equalsIgnoreCase("ALLOCATESTRATEGYKEY") || widget.getName().equalsIgnoreCase("PREALLOCATESTRATEGYKEY")) {
			
			RuntimeFormWidgetInterface statusWidget = widget.getForm().getFormWidgetByName("STATUS");
			String status = statusWidget.getValue().toString();
			if(status.equals("02") || status.equals("04")|| status.equals("09")|| status.equals("19")){				
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);				
			} else {
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);				
			}				
		}
		// Amar: End of code changes done for fixing the SDIS issue SCM-00000-05674 Machine 2137502 
		return RET_CONTINUE;
	}
	
	protected boolean disableWidget(RuntimeFormWidgetInterface widget){
		//Hides lookups and buttons
		if(widget.getType().equals("image button") || widget.getName().equals("NEW") || widget.getName().equals("MINSHIPPERCENT")){
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		//Sets all other widgets to read only
		else{
			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		return true;
	}
}