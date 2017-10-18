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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemFactory;
import com.epiphany.shr.ui.view.RuntimeMenuFactory;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.ui.view.RuntimeMenuElementInterface;
import com.ssaglobal.scm.wms.wm_inventory_transaction.ui.ViewSourceDocUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class IPDisableActionMenuItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase
{

	/**
	 * The code within the execute method will be run on the FormRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param menu the menu that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(IPDisableActionMenuItems.class);

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
		_log.debug("LOG_SYSTEM_OUT","\n\t" + "IPDisableActionMenuItems" + "\n",100L);
		try
		{

			// Add your code here to process the event
			_log.debug("LOG_SYSTEM_OUT","Menu " + menuItem.getName(),100L);
			RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			_log.debug("LOG_SYSTEM_OUT","-Header Form " + headerForm.getName(),100L);
			
			if (headerForm.getName().equalsIgnoreCase("wm_invoice_processing_index_view")) //blank
			{
				menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
				
			}
			else
			{
				menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
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
