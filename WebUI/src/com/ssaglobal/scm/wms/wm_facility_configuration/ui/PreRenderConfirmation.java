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
package com.ssaglobal.scm.wms.wm_facility_configuration.ui;

import java.text.MessageFormat;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ASNGeneratePutaway;

public class PreRenderConfirmation extends FormExtensionBase{

    public PreRenderConfirmation()
    {
    }

    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException{
    	StateInterface state = context.getState();
    	RuntimeFormWidgetInterface msg1 = form.getFormWidgetByName("message1");
        RuntimeFormWidgetInterface msg3 = form.getFormWidgetByName("message3");
        getMessage(state,"ERRORMSG1",msg1);
        getMessage(state,"ERRORMSG3",msg3);
        return 0;
    }

    private void getMessage(StateInterface state, String err, RuntimeFormWidgetInterface widget)
        throws EpiException
    {
        UserInterface user = state.getUser();
        LocaleInterface locale = user.getLocale();

    	String MsgCode;
    	EpnyControllerState EpnyContState = (EpnyControllerState) state;
        HttpSession session = EpnyContState.getRequest().getSession();
		if (session.getAttribute(err) == null){
			MsgCode= " ";
			widget.setLabel("label", " ");
		}
		else{
			MsgCode = session.getAttribute(err).toString();
	        String strMsg = getTextMessage(MsgCode,new Object[]{},locale);
	        widget.setLabel("label", strMsg);

		}
		session.removeAttribute(err);
    }
}
