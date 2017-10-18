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


package com.ssaglobal.scm.wms.wm_qc_verify.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class QCVerifyDisableActionMenuItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifyDisableActionMenuItems.class);
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
		//_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "QCVerifyDisableActionMenuItems!" + "\n", SuggestedCategory.NONE);;
		try
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "Menu " + menuItem.getName(), SuggestedCategory.NONE);;
			RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("Results");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			//_log.debug("LOG_DEBUG_EXTENSION", "-Header Form " + headerForm.getName(), SuggestedCategory.NONE);;

			if (headerForm.getName().equalsIgnoreCase("blank")) //tab view
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
