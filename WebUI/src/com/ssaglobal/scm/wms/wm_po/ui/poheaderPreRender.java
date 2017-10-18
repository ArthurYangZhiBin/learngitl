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

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;

public class poheaderPreRender extends FormExtensionBase{
	
	public poheaderPreRender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		return RET_CONTINUE;
		
	}
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws UserException {
		StateInterface state = context.getState();
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface poBuyerForm = getPOtab(form, state, "tab 1");
		

		RuntimeFormWidgetInterface pokey = form.getFormWidgetByName("POKEY");
		RuntimeFormWidgetInterface buyer = form.getFormWidgetByName("STORERKEY");
//		RuntimeFormWidgetInterface buyer_lookup = form.getFormWidgetByName("buyer_lookup");
		
		try {
	           // Add your code here to process the event
			if (widgetName.equals("NEW"))
			{
				pokey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				buyer.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//				buyer_lookup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
				}
			else{
				String status = form.getFormWidgetByName("STATUS").getValue().toString();
				pokey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				buyer.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//				buyer_lookup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(poBuyerForm);
					disableFormWidgets(getPOtab(form, state, "tab 2"));
					disableFormWidgets(getPOtab(form, state, "tab 3"));
					disableFormWidgets(getPOtab(form, state, "tab 4"));
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
}
