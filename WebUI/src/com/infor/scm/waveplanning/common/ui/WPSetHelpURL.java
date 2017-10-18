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

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class WPSetHelpURL extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPSetHelpURL.class);

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_SetBioOnNew", "***Executing SetHelpURL", SuggestedCategory.NONE);
		try
		{
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			String helpUrl = (String) userCtx.get("HELPURL");
			System.out.println("\nHelp URL: " +helpUrl);
			_log.debug("LOG_DEBUG_EXTENSION_SetHelpURL", "Help URL from context: " + helpUrl,
						SuggestedCategory.NONE);
			String widgetName = getParameterString("WidgetName");
			RuntimeFormWidgetInterface reportFrame = form.getFormWidgetByName(widgetName);
			reportFrame.setProperty(RuntimeFormWidgetInterface.PROP_SRC, helpUrl);
			_log.debug("LOG_DEBUG_EXTENSION_SetHelpURL", "Help URL from widget: "
					+ reportFrame.getProperty(RuntimeFormWidgetInterface.PROP_SRC), SuggestedCategory.NONE);

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		_log.debug("LOG_DEBUG_EXTENSION_SetHelpURL", "***Exiting SetHelpURL", SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

}
