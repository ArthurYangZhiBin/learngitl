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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.batchpicking;

import java.text.MessageFormat;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

public class IgnoreOrderConfirmPrerender extends FormExtensionBase{
	public IgnoreOrderConfirmPrerender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException, UserException{
		
        UserInterface user = context.getState().getUser();
        LocaleInterface locale = user.getLocale();
        RuntimeFormWidgetInterface widget = form.getFormWidgetByName("message");
        String strMsg = widget.getLabel("label", locale);
        String ignoredOrderNumber = context.getState().getRequest().getSession().getAttribute("IGNORED_ORDER_NUMBER").toString();
        String arguments[] = {ignoredOrderNumber};
        strMsg = getTextMessage("WMTXT_CONFIRM_IGNORE_ORDER",arguments,locale);
        MessageFormat mf = new MessageFormat(strMsg);
        mf.setLocale(locale.getJavaLocale());
        StringBuffer result = new StringBuffer();
        mf.format(arguments, result, null);
        widget.setLabel("label", result.toString());
        context.getState().getRequest().getSession().removeAttribute("IGNORED_ORDER_NUMBER");
        return RET_CONTINUE;
		
	}

}
