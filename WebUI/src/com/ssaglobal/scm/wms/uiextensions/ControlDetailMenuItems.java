/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */

package com.ssaglobal.scm.wms.uiextensions;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ControlDetailMenuItems extends
		com.epiphany.shr.ui.view.customization.MenuExtensionBase {
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ControlDetailMenuItems.class);

	/**
	 * The code within the execute method will be run on the FormRender.
	 * <P>
	 * 
	 * @param state
	 *            The StateInterface for this extension
	 * @param menu
	 *            the menu that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeMenuInterface menu) {

		try {
			// Add your code here to add menu items
			// Process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * The code within the execute method will be run on the FormRender. events.
	 * 
	 * @param state
	 *            the state of the user's navigation
	 * @param menuItem
	 *            the menu item that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state,
			RuntimeMenuItemInterface menuItem) {

		try {
			String slot = getParameterString("Slot");
			boolean hideOnDetail = getParameterBoolean("HideOnDetail");
			boolean hideOnList = getParameterBoolean("HideOnList");
			// Ensure proper context for focus
			SlotInterface toggleSlot = state.getCurrentRuntimeForm()
					.getParentForm(state).getSubSlot(slot);
			int selectedFormNo = state.getSelectedFormNumber(toggleSlot);

			// handling special case
			try {
				if ("SAVE".equals(state.getActionObject().getName())) {
					//after save, the form should be a list
					selectedFormNo = 0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("ControlDetailMenuItems_execute", StringUtils
						.getStackTraceAsString(e),
						SuggestedCategory.APP_EXTENSION);
			}
			// RuntimeFormInterface currentForm =
			// state.getRuntimeForm(toggleSlot, selectedFormNo);
			// this is terrible but seems to work
			switch (selectedFormNo) {
			// List Form
			case 0:
				menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN,
						hideOnList == true ? true : false);
				break;
			// Detail Form
			case 1:
				menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN,
						hideOnDetail == true ? true : false);
				break;
			}

			//			

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
