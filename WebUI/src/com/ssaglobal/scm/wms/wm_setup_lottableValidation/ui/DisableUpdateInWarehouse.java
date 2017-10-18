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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class DisableUpdateInWarehouse  extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableUpdateInWarehouse.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing DisableUpdateInWarehouse",100L);
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
				_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Facility: " +isEnterprise,100L);
			} catch (java.lang.NullPointerException e)
			{
				isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
				_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Facility Session: " +isEnterprise,100L);
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

		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting DisableUpdateInWarehouse",100L);
		return RET_CONTINUE;

	}

	private void handleWarehousePermissions(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","In handleWarehousePermissions",100L);
		StateInterface state = context.getState();

		// For Item
		String formName = form.getName();

		if (formName.endsWith("Toolbar"))
		{
			_log.debug("LOG_SYSTEM_OUT","\n'''Form is of type Toolbar, Disabling New button",100L);
			String newButtonName = getParameterString("NEWBUTTON");
			if ( newButtonName != null)
			{
				RuntimeFormWidgetInterface newButton = form.getFormWidgetByName(newButtonName);
				newButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}

			String saveButtonName = getParameterString("SAVEBUTTON");
			if ( saveButtonName != null)
			{
				_log.debug("LOG_SYSTEM_OUT","\nDisabling Save button",100L);
				RuntimeFormWidgetInterface saveButton = form.getFormWidgetByName(saveButtonName);
				saveButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}
			
			String deleteButtonName = getParameterString("DELETEBUTTON");
			if ( deleteButtonName != null)
			{
				_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Disabling Delete button",100L);
				RuntimeFormWidgetInterface newButton = form.getFormWidgetByName(deleteButtonName);
				newButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Disabling Input Form",100L);
			//form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, true);
		  	Iterator widgets = form.getFormWidgets();
		  		while (widgets.hasNext() ) {
		  			Object obj = widgets.next();
		  			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)obj;
		  			//_log.debug("LOG_SYSTEM_OUT","\n\n^^^^^Disabling: " +widget.getName(),100L);
		  			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Leaving handleWarehousePermissions",100L);

	}

	private void handleEnterprisePermissions(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","In handleEnterprisePermissions",100L);
		StateInterface state = context.getState();

		// For Item
		String formName = form.getName();

		if (formName.endsWith("Toolbar"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Enabling New button",100L);			
			String newButtonName = getParameterString("NEWBUTTON");
			if ( newButtonName != null)
			{
				RuntimeFormWidgetInterface newButton = form.getFormWidgetByName(newButtonName);
				newButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}

			//String saveButtonName = getParameterString("SAVEBUTTON");					
			//String deleteButtonName = getParameterString("DELETEBUTTON");
			
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Enabling Input Form",100L);
			form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Leaving handleEnterprisePermissions",100L);
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

