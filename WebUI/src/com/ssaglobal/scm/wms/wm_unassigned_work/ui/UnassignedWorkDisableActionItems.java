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

package com.ssaglobal.scm.wms.wm_unassigned_work.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UnassignedWorkDisableActionItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase
{


	/**
	 * The code within the execute method will be run on the FormRender.
	 *   events.
	 * @param state the state of the user's navigation
	 * @param menuItem the  menu item that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	@Override
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem)
	{

		
		try
		{
		
			RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("wm_unassigned_work_template_slot1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		

			if (headerForm.getName().equalsIgnoreCase("wm_unassigned_work_list_view"))
			{
				//on list
				if (menuItem.getName().equalsIgnoreCase("Search"))
				{
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
				}
				
				if (menuItem.getName().equalsIgnoreCase("Make Assignments"))
				{
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
				}

				if (menuItem.getName().equalsIgnoreCase("Update Priority")) {
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
				}
			}
			else if (headerForm.getName().equalsIgnoreCase("wm_unassigned_work_search_view")) //search view
			{
				//on search
				if (menuItem.getName().equalsIgnoreCase("Search"))
				{
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
				}
				
				if (menuItem.getName().equalsIgnoreCase("Make Assignments"))
				{
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
				}

				if (menuItem.getName().equalsIgnoreCase("Update Priority")) {
					menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
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
