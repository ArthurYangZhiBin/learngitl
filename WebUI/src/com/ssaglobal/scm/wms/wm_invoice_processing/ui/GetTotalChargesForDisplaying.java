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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.text.MessageFormat;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class GetTotalChargesForDisplaying extends FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(GetTotalChargesForDisplaying.class);

	public GetTotalChargesForDisplaying()
	{
	}

	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing GetTotalChargesForDisplaying",100L);
		try
		{
			if (((EpnyControllerState) state).isOldRequest())
				return 0;
			UserInterface user = state.getUser();
			LocaleInterface locale = user.getLocale();
			String strMsg = widget.getLabel("label", locale);

			String[] args = new String[1];
			try
			{
				args[0] = state.getServiceManager().getUserContext().get("InvoiceTot").toString();
			} catch (NullPointerException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION_GetTotalChargesForDisplaying", "InvoiceTot from the Session is null",
							SuggestedCategory.NONE);
				args[0] = "0";
			}
			strMsg = getTextMessage("WMTXT_TOTALCHARGES", args, locale);
			MessageFormat mf = new MessageFormat(strMsg);
			mf.setLocale(locale.getJavaLocale());
			StringBuffer result = new StringBuffer();
			mf.format(args, result, null);
			widget.setLabel("label", result.toString());

			state.getServiceManager().getUserContext().remove("InvoiceTot");
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting GetTotalChargesForDisplaying",100L);
		return 0;
	}
}