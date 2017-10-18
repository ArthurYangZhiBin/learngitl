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

package com.ssaglobal.scm.wms.wm_inventory_move.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class DisplayErrorMessageFromContext extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisplayErrorMessageFromContext.class);

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

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a form in a modal window. Write code
	 * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
	 * to the end user
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		StateInterface state = context.getState();
		String interactionID = state.getInteractionId();

		String errorWidgetName = getParameterString("ERRORWIDGET");
		String contextVariablePrefix = getParameterString("CONTEXTVARIABLE");
		String contextVariable = contextVariablePrefix + interactionID;

		String contextVariableArgs = contextVariablePrefix + "ARGS" + interactionID;

		try
		{
			// Add your code here to process the event
			Object messageIdentifier = userContext.get(contextVariable);
			userContext.remove(contextVariable);
			Object arguments = userContext.get(contextVariableArgs);
			userContext.remove(contextVariableArgs);
			if (messageIdentifier != null)
			{
				String errorMessage = getTextMessage(messageIdentifier.toString(), (Object[]) arguments, context.getState().getLocale());
				if (errorMessage == null)
				{
					form.getFormWidgetByName(errorWidgetName).setLabel(RuntimeFormWidgetInterface.LABEL_VALUE, messageIdentifier.toString());
				}
				else
				{
					form.getFormWidgetByName(errorWidgetName).setLabel(RuntimeFormWidgetInterface.LABEL_VALUE, errorMessage);
				}
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
