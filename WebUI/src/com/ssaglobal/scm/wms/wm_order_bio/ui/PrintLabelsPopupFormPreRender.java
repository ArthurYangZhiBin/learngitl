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
package com.ssaglobal.scm.wms.wm_order_bio.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;

public class PrintLabelsPopupFormPreRender extends FormExtensionBase
{
	public PrintLabelsPopupFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{							
		StateInterface state = context.getState();	
		RuntimeFormWidgetInterface widgetNonRfidPrinter = form.getFormWidgetByName("NONRFIDPRINTER");
		RuntimeFormWidgetInterface widgetRfidPrinter = form.getFormWidgetByName("RFIDPRINTER");	
		Object isRfidPrinterObj = state.getRequest().getSession().getAttribute("ISRFIDPRINTER");				
		Object isNonRfidPrinterObj = state.getRequest().getSession().getAttribute("ISNONRFIDPRINTER");
		Boolean isRfidPrinter = isRfidPrinterObj==null?Boolean.FALSE:(Boolean)isRfidPrinterObj;
		Boolean isNonRfidPrinter = isNonRfidPrinterObj==null?Boolean.FALSE:(Boolean)isNonRfidPrinterObj;
		state.getRequest().getSession().removeAttribute("ISRFIDPRINTER");
		state.getRequest().getSession().removeAttribute("ISNONRFIDPRINTER");
				
		widgetRfidPrinter.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
		widgetNonRfidPrinter.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
		if(isRfidPrinter.booleanValue()){
			widgetRfidPrinter.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
		}
		if(isNonRfidPrinter.booleanValue()){
			widgetNonRfidPrinter.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
		}		
		return RET_CONTINUE;
	}
	
}