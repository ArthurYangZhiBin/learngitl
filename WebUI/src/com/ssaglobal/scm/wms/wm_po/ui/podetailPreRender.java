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
package com.ssaglobal.scm.wms.wm_po.ui;

import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class podetailPreRender extends FormExtensionBase{
	
	public podetailPreRender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		return RET_CONTINUE;
		
	}
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws UserException {
		StateInterface state = context.getState();
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface udfForm = getPOtab(form, state, "tab 1");
		try {
	           // Add your code here to process the event
			if (widgetName.equals("NEW"))
			{
				
				}
			else{
				
				String status = form.getFormWidgetByName("STATUS").getValue().toString();
				BioBean poDetail = (BioBean)form.getFocus();
				String qtyOrdered = poDetail.getValue("QTYORDERED").toString();
				String qtyReceived = poDetail.getValue("QTYRECEIVED").toString();
				String uom = poDetail.getValue("UOM").toString();
				String packKey = poDetail.getValue("PACKKEY").toString();
				String screenOrderedQty = convertUOMQty(uom,qtyOrdered, packKey);	
				String screenReceivedQty = convertUOMQty(uom,qtyReceived, packKey);
				form.getFormWidgetByName("ORDEREDQTY").setValue(screenOrderedQty);
				form.getFormWidgetByName("RECEIVEDQTY").setValue(screenReceivedQty);


				if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(udfForm);
				}
			}
			
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;
	}
	private RuntimeFormInterface getPOtab(RuntimeFormExtendedInterface form, StateInterface state, String tab){
		RuntimeFormInterface tabGroupShellForm = (form.getParentForm(state));
		SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface formtab = state.getRuntimeForm(tabGroupSlot, tab);
		return formtab;
		
	}
	private void disableFormWidgets(RuntimeFormInterface form)
	{
//		_log.debug("LOG_SYSTEM_OUT","!@# Disabling Form Widgets",100L);
		for (Iterator it = form.getFormWidgets(); it.hasNext();)
		{
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}
	public String convertUOMQty(String touom, String unitQty, String packKey){
//		_log.debug("LOG_SYSTEM_OUT","Executing NSPUOMCONV stored procedure",100L);
		String convQty = "";
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(unitQty));	//from Qty
		parms.add(new TextData("EA"));		// from UOM
		parms.add(new TextData(touom));		// to UOM
		parms.add(new TextData(packKey));	//Pack key
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPUOMCONV");
		try {
			EXEDataObject ObjConvQty = WmsWebuiActionsImpl.doAction(actionProperties);
			convQty = ObjConvQty.getAttribValue(new TextData("toqty")).toString();
//			_log.debug("LOG_SYSTEM_OUT","Converted qty =" + convQty,100L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convQty;
	}
}
