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

package com.ssaglobal.scm.wms.wm_date_format.ui;

// Import 3rd party packages and classes
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DateFormatDisableWhseMode extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DateFormatDisableWhseMode.class);

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

		return permissionsPreRender(context, form);
	}

	private int permissionsPreRender(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n+++++Starting DateFormatDisableEntMode permissionsPreRender", SuggestedCategory.NONE);
		try
		{
			EpnyControllerState state = (EpnyControllerState) context.getState();
			HttpSession session = state.getRequest().getSession();

			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

			String isEnterprise = null;
			//Is userContext null?

			try
			{
				isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			} catch (java.lang.NullPointerException e)
			{
				isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			}

			if (isEnterprise.equals("1"))
			{
				//enable/disable widgets/forms for Enterprise
				handleEnterprisePermissions(context, form);
			}
			else
			{
				//enable/disable widgets/forms for Warehouse
				handleWarehousePermissions(context, form);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;

	}

	private void handleWarehousePermissions(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		RuntimeFormWidgetInterface DATECODEFORMAT = form.getFormWidgetByName("DATECODEFORMAT");
		DATECODEFORMAT.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		
		RuntimeFormWidgetInterface DATECODEDESC = form.getFormWidgetByName("DATECODEDESC");
		DATECODEDESC.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		
		RuntimeFormWidgetInterface EXAMPLEDATE = form.getFormWidgetByName("EXAMPLEDATE");
		EXAMPLEDATE.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		
		RuntimeFormWidgetInterface CONVERTEDDATE = form.getFormWidgetByName("CONVERTEDDATE");
		CONVERTEDDATE.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	}

	private void handleEnterprisePermissions(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		RuntimeFormWidgetInterface DATECODEFORMAT = form.getFormWidgetByName("DATECODEFORMAT");
		DATECODEFORMAT.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		
		RuntimeFormWidgetInterface DATECODEDESC = form.getFormWidgetByName("DATECODEDESC");
		DATECODEDESC.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		
		RuntimeFormWidgetInterface EXAMPLEDATE = form.getFormWidgetByName("EXAMPLEDATE");
		EXAMPLEDATE.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		
		RuntimeFormWidgetInterface CONVERTEDDATE = form.getFormWidgetByName("CONVERTEDDATE");
		CONVERTEDDATE.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
	}

	/**
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
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

}

