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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

public class ShipmentOrderLabelPreRender extends FormExtensionBase{
	private final static String IS_ONE = "IS_ONE";
	private final static String RFIDFLAG = "RFIDFLAG";
	private final static String ASSIGN = "ASSIGNMENT";
	private final static String STD = "STANDARD";
	private final static String RFID = "RFID";
	private final static String COPIES = "COPIES";
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		StateInterface state = context.getState();
		RuntimeFormWidgetInterface widget;
		HttpSession session = state.getRequest().getSession();
		//Set assignment render context
		String flag = session.getAttribute(IS_ONE).toString();
		if(flag.equalsIgnoreCase("0")){
			widget = form.getFormWidgetByName(ASSIGN);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		//Set dropdown render context
		String isRFID = session.getAttribute(RFIDFLAG).toString();
		if(isRFID.equalsIgnoreCase("1")){
			widget = form.getFormWidgetByName(STD);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}else{
			widget = form.getFormWidgetByName(RFID);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		//Set default copies
		widget = form.getFormWidgetByName(COPIES);
		widget.setValue("1");
		return RET_CONTINUE;
	}
}