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
package com.infor.scm.waveplanning.common.ui;

import java.text.MessageFormat;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;



public class WPSetMessageOnModal extends FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPSetMessageOnModal.class);

	public WPSetMessageOnModal()
	{
	}

	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Executing SetMessageOnModal", SuggestedCategory.NONE);
		try
		{
			
			if (((EpnyControllerState) state).isOldRequest())
				return 0;
			
			
			
			String textMsgName= getParameter("TXTName").toString();
			String sessionName= getParameter("SessionName").toString();
			
			_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Session Name: " +sessionName, SuggestedCategory.NONE);
			UserInterface user = state.getUser();
			LocaleInterface locale = user.getLocale();
			String strMsg = widget.getLabel("label", locale);
			String[] args = new String[1];

			try
			{
				args[0] = state.getServiceManager().getUserContext().get(sessionName).toString();				
				_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Synchronizing" +args[0], SuggestedCategory.NONE);
			} catch (NullPointerException e)
			{
				System.out.println("\n\n *******In null for get Total Charges : ");
				_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Message is blank", SuggestedCategory.NONE);
				args[0] = "";
			}
			
			strMsg = getTextMessage(textMsgName, args, locale);
			MessageFormat mf = new MessageFormat(strMsg);
			mf.setLocale(locale.getJavaLocale());
			StringBuffer result = new StringBuffer();
			mf.format(args, result, null);
			widget.setLabel("label", result.toString());
			state.getServiceManager().getUserContext().remove(sessionName);
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Exiting SetMessageOnModal", SuggestedCategory.NONE);
		
		return RET_CONTINUE;
	}
}
