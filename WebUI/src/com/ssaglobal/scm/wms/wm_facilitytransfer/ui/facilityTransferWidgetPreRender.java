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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class facilityTransferWidgetPreRender extends FormWidgetExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(facilityTransferWidgetPreRender.class);
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException{

		//Boolean flags that determine which conditions disable the widget
		boolean statusEquals95 = getParameterBoolean("statusEquals95");
		boolean statusEquals9 = getParameterBoolean("statusEquals9");
		boolean typeEquals20 = getParameterBoolean("typeEquals20");
		boolean shippedAndOpenQty = getParameterBoolean("shippedAndOpenQty");
		boolean isSubstitute = getParameterBoolean("isSubstitute");
		boolean qtyAllocated = getParameterBoolean("qtyAllocated");
		boolean qtyPicked = getParameterBoolean("qtyPicked");
		boolean externalLot = getParameterBoolean("externalLot");
		boolean shippedQtyOnly = getParameterBoolean("shippedQtyOnly");
//		boolean allocateduomqty = getParameterBoolean("allocateduomqty");
		boolean done = false;
		
		DataBean focus = state.getFocus();
		if(widget.getName().equals("NEW") || widget.getName().equals("SAVE")){
			RuntimeFormInterface shellForm = widget.getForm().getParentForm(state).getParentForm(state);
			_log.debug("LOG_SYSTEM_OUT","Previous line result: "+shellForm.getName(),100L);
			RuntimeFormInterface headerForm = null;
			if(shellForm.getName().equals("wm_list_shell_facilitytransfer")){
				headerForm = state.getRuntimeForm(shellForm.getSubSlot("slot1"), null);
			}else{
				while(!(shellForm.getName().equals("wm_tabgrp_shell_facilitytransfer"))){
					shellForm = shellForm.getParentForm(state);
				}
				headerForm = state.getRuntimeForm(shellForm.getSubSlot("tbgrp_slot"), "Tab0");
			}
			_log.debug("LOG_SYSTEM_OUT","Previous line result: "+headerForm.getName(),100L);
			DataBean headerFocus = headerForm.getFocus();
			String statusWidget = headerFocus.getValue("STATUS").toString();
			_log.debug("LOG_SYSTEM_OUT","Previous line result: "+statusWidget,100L);
			if(statusWidget.equals("95")){
				done = disableWidget(widget);
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
			if(statusEquals9 && !done){
				RuntimeFormWidgetInterface statusWidget = widget.getForm().getFormWidgetByName("STATUS");
				String status = statusWidget.getValue().toString();
				if(status.equals("9")){
					done = disableWidget(widget);
				}
			}
			if(typeEquals20 && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				_log.debug("LOG_SYSTEM_OUT","tabForm = "+ tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","tabSlot = "+ tabSlot.getName(),100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
				_log.debug("LOG_SYSTEM_OUT","headerForm = "+ headerForm.getName(),100L);
				RuntimeFormWidgetInterface typeWidget = headerForm.getFormWidgetByName("TYPE");
				String type = typeWidget.getValue().toString();
				if(type.equals("20")){
					done = disableWidget(widget);
				}
			}
			if(shippedAndOpenQty && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				_log.debug("LOG_SYSTEM_OUT","2Form name retrieved: "+tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","2Form slot name: "+tabSlot.getName(),100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
				RuntimeFormWidgetInterface openqtyWidget = headerForm.getFormWidgetByName("OPENQTY");
				double openqty ;
				if (openqtyWidget.getValue().equals("")){
					openqty = 0;
				}else {
					openqty= Double.parseDouble(openqtyWidget.getValue().toString());
				}
				RuntimeFormWidgetInterface shipedqtyWidget = headerForm.getFormWidgetByName("SHIPPEDQTY");
				double shipedqty; 
				if (shipedqtyWidget.getValue().equals("")){
					shipedqty = 0;
				}else {
					shipedqty = Double.parseDouble(shipedqtyWidget.getValue().toString());
				}

				if(openqty==0 && shipedqty>0){
					done = disableWidget(widget);
				}
			}
			if(isSubstitute && !done){
				String issubstitute = null;
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean)focus;
					issubstitute = qbe.get("ISSUBSTITUTE").toString();
				}else{
					BioBean bio = (BioBean)focus;
					issubstitute = bio.get("ISSUBSTITUTE").toString();
				}
				if(!(issubstitute.equals("0"))){
					done = disableWidget(widget);
				}
			}
			if(qtyAllocated && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				_log.debug("LOG_SYSTEM_OUT","4Form name retrieved: "+tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","4Form slot name: "+tabSlot.getName(),100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
				RuntimeFormWidgetInterface qtyAllocatedWidget = headerForm.getFormWidgetByName("QTYALLOCATED");
				double d_qtyAllocated; 
				if (qtyAllocatedWidget.getValue().equals("")){
					d_qtyAllocated = 0;
				}else {
					d_qtyAllocated = Double.parseDouble(qtyAllocatedWidget.getValue().toString());
				}

				if(d_qtyAllocated >0){
					done = disableWidget(widget);
				}
			}
			if(qtyPicked && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				_log.debug("LOG_SYSTEM_OUT","5Form name retrieved: "+tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","5Form slot name: "+tabSlot.getName(),100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
				_log.debug("LOG_SYSTEM_OUT","Check after here",100L);
				RuntimeFormWidgetInterface qtyPickedWidget = headerForm.getFormWidgetByName("QTYPICKED");
				double d_qtyPicked = 0;
				_log.debug("LOG_SYSTEM_OUT","Check after here",100L);	
				if(qtyPickedWidget!=null){
					d_qtyPicked = Double.parseDouble(qtyPickedWidget.getValue().toString());
				}
				_log.debug("LOG_SYSTEM_OUT","Check after here",100L);
				if(d_qtyPicked >0){
					_log.debug("LOG_SYSTEM_OUT","Check after here",100L);
					done = disableWidget(widget);
				}
			}
			if(externalLot && !done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
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
				_log.debug("LOG_SYSTEM_OUT","7Form name retrieved: "+tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","7Form slot name: "+tabSlot.getName,100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface statusWidget = headerForm.getFormWidgetByName("ALLOCATEDUOMQTY");
				double status = Double.parseDouble(statusWidget.getValue().toString());
				if(status!=0){
					done = disableWidget(widget);
				}
			}*/
			if(shippedQtyOnly && ! done){
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				_log.debug("LOG_SYSTEM_OUT","8Form name retrieved: "+tabForm.getName(),100L);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				_log.debug("LOG_SYSTEM_OUT","8Form slot name: "+tabSlot.getName(),100L);
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "Tab0");
				RuntimeFormWidgetInterface shippedQtyWidget = headerForm.getFormWidgetByName("SHIPPEDQTY");
				double shipedqty; 
				if (shippedQtyWidget.getValue().equals("")){
					shipedqty = 0;
				}else {
					shipedqty = Double.parseDouble(shippedQtyWidget.getValue().toString());
				}

				if(shipedqty>0){
					done = disableWidget(widget);
				}
			}
		}
		return RET_CONTINUE;
	}
	
	protected boolean disableWidget(RuntimeFormWidgetInterface widget){
		//Hides lookups and buttons
		if(widget.getType().equals("image button") || widget.getName().equals("NEW")){
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		//Sets all other widgets to read only
		else{
			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		return true;
	}
}