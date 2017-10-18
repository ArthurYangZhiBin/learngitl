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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any
 * parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemAssignLocationsTypeDropdownAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private static String LOCATION_TYPE = "LOCATIONTYPE";

	private static String ALLOW_FROM_BULK = "ALLOWREPLENISHFROMBULK";

	private static String ALLOW_FROM_CASE = "ALLOWREPLENISHFROMCASEPICK";

	private static String ALLOW_FROM_PIECE = "ALLOWREPLENISHFROMPIECEPICK";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationsTypeDropdownAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this
	 *            UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n//// execute",100L);
		try
		{
			_log.debug("LOG_SYSTEM_OUT","\n//// Getting Data",100L);
			// Get Handle on Form
			StateInterface state = context.getState();
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			DataBean currentFormFocus = state.getFocus();
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}

			Object tempValue = currentFormFocus.getValue(LOCATION_TYPE);
			String locationTypeValue = null;
			if (tempValue != null)
			{
				locationTypeValue = tempValue.toString();
				_log.debug("LOG_SYSTEM_OUT","/// " + LOCATION_TYPE + " Dropdown, Value : " + locationTypeValue,100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","/// " + LOCATION_TYPE + " Dropdown, Value is null",100L);
			}

			_log.debug("LOG_SYSTEM_OUT","\n//// Performing Actions",100L);

			if (locationTypeValue.equals("PICK"))
			{
				uncheckAndEnableAllCheckBoxes(currentForm, currentFormFocus);
				
				currentFormFocus.setValue(ALLOW_FROM_BULK, new String("1")); // Checked

				currentFormFocus.setValue(ALLOW_FROM_CASE, new String("1")); // Checked

				currentFormFocus.setValue(ALLOW_FROM_PIECE, new String("0")); // Unchecked
				RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(ALLOW_FROM_PIECE);
				widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true); // Not Editable
			}
			else if (locationTypeValue.equals("CASE"))
			{
				uncheckAndEnableAllCheckBoxes(currentForm, currentFormFocus);

				currentFormFocus.setValue(ALLOW_FROM_BULK, new String("1")); // Checked

				currentFormFocus.setValue(ALLOW_FROM_CASE, new String("0")); // Unchecked
				RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(ALLOW_FROM_CASE);
				widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true); // Not Editable

				// Nothing for ALLOW_FROM_PIECE
			}
			else
			{
				uncheckAndEnableAllCheckBoxes(currentForm, currentFormFocus);
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void uncheckAndEnableAllCheckBoxes(RuntimeFormInterface currentForm, DataBean currentFormFocus)
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n//// Setting the Widgets back to Editable Status and Clearing Checkboxes",100L);
		// Clear Checkboxes
		currentFormFocus.setValue(ALLOW_FROM_BULK, new String("0"));
		currentFormFocus.setValue(ALLOW_FROM_CASE, new String("0"));
		currentFormFocus.setValue(ALLOW_FROM_PIECE, new String("0"));

		RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(ALLOW_FROM_PIECE);
		widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false); // Editable
		widget = currentForm.getFormWidgetByName(ALLOW_FROM_CASE);
		widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false); // Editable
	}

}
