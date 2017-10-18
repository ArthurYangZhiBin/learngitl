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

package com.ssaglobal.scm.wms.wm_user_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityPopupPrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	/**
	 

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

		try
		{
			String sourceWidgetName = context.getSourceWidget().getName();
			if (sourceWidgetName.equals("COMPLETE"))
			{
				//Change Label

				form.getFormWidgetByName("Prompt").setLabel(
															RuntimeFormWidgetInterface.LABEL_VALUE,
															getTextMessage("WMEXP_UA_COMPLETE", new Object[] {},
																			context.getState().getLocale()));
				//Enable Text
				form.getFormWidgetByName("Display").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
				//Get Value from Form
				String assnNumber = context.getSourceWidget().getForm().getFormWidgetByName("ASSIGNMENTNUMBER").getDisplayValue();
				//Set Text
				form.getFormWidgetByName("Display").setDisplayValue(assnNumber);
			}
			else if (sourceWidgetName.equals("CREATE"))
			{
				//Change Label

				form.getFormWidgetByName("Prompt").setLabel(
															RuntimeFormWidgetInterface.LABEL_VALUE,
															getTextMessage("WMEXP_UA_CREATE", new Object[] {},
																			context.getState().getLocale()));
				//Disable Text
				form.getFormWidgetByName("Display").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			}
			else if (sourceWidgetName.equals("OUTFORDAY"))
			{
				//				Change Label

				form.getFormWidgetByName("Prompt").setLabel(
															RuntimeFormWidgetInterface.LABEL_VALUE,
															getTextMessage("WMEXP_UA_LOGOUT", new Object[] {},
																			context.getState().getLocale()));
				//Disable Text
				form.getFormWidgetByName("Display").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);

			}
			else if (sourceWidgetName.equals("INFORDAY"))
			{
				//Change Label

				form.getFormWidgetByName("Prompt").setLabel(
															RuntimeFormWidgetInterface.LABEL_VALUE,
															getTextMessage("WMEXP_UA_EXTENDED", new Object[] {},
																			context.getState().getLocale()));
				//Disable Text
				form.getFormWidgetByName("Display").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
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
