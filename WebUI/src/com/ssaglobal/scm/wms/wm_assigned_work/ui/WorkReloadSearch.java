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

package com.ssaglobal.scm.wms.wm_assigned_work.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class WorkReloadSearch extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	

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
		
		ArrayList widgets = (ArrayList) getParameter("WIDGETS");

		String contextVar = getParameterString("CONTEXTVAR");
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		
		if(context.getActionObject().getName().endsWith("menuitem"))
		{
			//clear context and return
			userContext.remove(contextVar);
			return RET_CONTINUE;
		}
		
		AWSearch searchP = (AWSearch) userContext.get(contextVar);
		if (searchP == null)
		{
			return RET_CONTINUE;
		}
		//Wavekey
		form.getFormWidgetByName((String) widgets.get(0)).setDisplayValue(searchP.getWaveKey());
		//Assn#
		form.getFormWidgetByName((String) widgets.get(1)).setDisplayValue(searchP.getAssignmentNumber());
		//Order#
		form.getFormWidgetByName((String) widgets.get(2)).setDisplayValue(searchP.getShipmentOrderNumber());
		//Route
		form.getFormWidgetByName((String) widgets.get(3)).setDisplayValue(searchP.getRoute());
		//Stop
		form.getFormWidgetByName((String) widgets.get(4)).setDisplayValue(searchP.getStop());
		if (widgets.size() == 6)
		{
			//Userid
			form.getFormWidgetByName((String) widgets.get(5)).setDisplayValue(searchP.getUserid());
		}

		return RET_CONTINUE;
	}

}
