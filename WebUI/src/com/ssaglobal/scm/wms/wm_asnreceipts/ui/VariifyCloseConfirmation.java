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

import java.text.MessageFormat;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;

public class VariifyCloseConfirmation extends FormWidgetExtensionBase
{
	
    public VariifyCloseConfirmation()
    {
    }

    protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
        throws EpiException
    {
        if(((EpnyControllerState)state).isOldRequest())
            return 0;
        UserInterface user = state.getUser();
        LocaleInterface locale = user.getLocale();
        String disconnectedUsrStr = " ";
        String strMsg = widget.getLabel("label", locale);

        String arguments[] = {getCount(state)};
        strMsg = getTextMessage("WMEXP_CONFIRM_VARIFYCLOSE",arguments,locale);
        MessageFormat mf = new MessageFormat(strMsg);
        mf.setLocale(locale.getJavaLocale());
        StringBuffer result = new StringBuffer();
        mf.format(arguments, result, null);
        widget.setLabel("label", result.toString());
        return 0;
    }

    private String getCount(StateInterface state)
        throws EpiException
    {
    	String count;
    	EpnyControllerState EpnyContState = (EpnyControllerState) state;
        HttpSession session = EpnyContState.getRequest().getSession();
		if (session.getAttribute(ValidateVarifyCloseAction.RECORDCOUNT) == null){
			count= "0";
		}
		else{
			count = session.getAttribute(ValidateVarifyCloseAction.RECORDCOUNT).toString();
		}
        return count;
    }

    public static final String DISCONNECTED_LABEL_KEY = "TXT_DISCONNECTED_USER";
    public static final String REGISTRATIONMODE = "SelfRegisterMode";
    public static final String REGISTRATION_AUTOMATIC = "automatic";
    public static final String REGISTRATION_PROMPT = "prompt";
    public static final String REGISTRATION_RESTRICTED = "restricted";
}
