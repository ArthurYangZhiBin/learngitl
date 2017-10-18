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

package com.ssaglobal.scm.wms.wm_shipmentorder.reports;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemFactory;
import com.epiphany.shr.ui.view.RuntimeMenuFactory;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.ui.view.RuntimeMenuElementInterface;
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

public class HideMenuItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(HideMenuItems.class);

	/**
	 * The code within the execute method will be run on the FormRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param menu the menu that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeMenuInterface menu)
	{

		try
		{
			// Add your code here to add menu items  
			// Process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * The code within the execute method will be run on the FormRender.
	 *   events.
	 * @param state the state of the user's navigation
	 * @param menuItem the  menu item that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem)
	{

		DataBean focus = null;
		String slot = getParameter("SLOT") == null ? "list_slot_1" : getParameterString("SLOT");

		focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(slot), null).getFocus();
		_log.debug("LOG_DEBUG_EXTENSION_HideMenuItems", "Menu Item " + menuItem.getName() + ", Bean Type: "
				+ focus.getBeanType(), SuggestedCategory.NONE);

		if (focus.isBioCollection() || focus.isTempBio())
		{
			menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
		}

		return RET_CONTINUE;
	}

}
