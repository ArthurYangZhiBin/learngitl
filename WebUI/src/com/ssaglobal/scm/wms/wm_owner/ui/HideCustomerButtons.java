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
package com.ssaglobal.scm.wms.wm_owner.ui;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
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

public class HideCustomerButtons  extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{


		try
		{
			EpnyControllerState state = (EpnyControllerState) context.getState();
			HttpSession session = state.getRequest().getSession();

			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

			String formName = form.getName();
			
			if (formName.endsWith("Toolbar"))
			{
				RuntimeFormInterface listForm = form.getParentForm(state);
				RuntimeFormInterface toggleForm = listForm.getParentForm(state);
				RuntimeFormInterface shellForm = toggleForm.getParentForm(state);
				String shellFormName = shellForm.getName();

				if (shellFormName.equalsIgnoreCase("wm_tbgrp_shell_customer"))
				{
					RuntimeFormWidgetInterface newButton = form.getFormWidgetByName("NEW");
					newButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	
					RuntimeFormWidgetInterface deleteButton = form.getFormWidgetByName("DELETE");
					deleteButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
				}
			}
			else
			{
				RuntimeFormInterface toggleForm = form.getParentForm(state);
				RuntimeFormInterface shellForm = toggleForm.getParentForm(state);
				String shellFormName = shellForm.getName();

				if (shellFormName.equalsIgnoreCase("wm_tbgrp_shell_customer"))
				{
				  	Iterator widgets = form.getFormWidgets();
			  		while (widgets.hasNext() ) 
			  		{
			  			Object obj = widgets.next();
			  			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)obj;
			  			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			  		}
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


	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}

