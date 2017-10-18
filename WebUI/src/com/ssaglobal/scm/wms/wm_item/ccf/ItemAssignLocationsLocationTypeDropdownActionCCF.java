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

package com.ssaglobal.scm.wms.wm_item.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class ItemAssignLocationsLocationTypeDropdownActionCCF extends
		com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationsLocationTypeDropdownActionCCF.class);
	private static String LOCATION_TYPE = "LOCATIONTYPE";

	private static String ALLOW_FROM_BULK = "ALLOWREPLENISHFROMBULK";

	private static String ALLOW_FROM_CASE = "ALLOWREPLENISHFROMCASEPICK";

	private static String ALLOW_FROM_PIECE = "ALLOWREPLENISHFROMPIECEPICK";

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException 
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n@#$@#$ Start of ItemAssignLocationsLocationTypeDropdownActionCCF", SuggestedCategory.NONE);
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		try
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n//// Getting Data", SuggestedCategory.NONE);

			Object tempValue = widgetNamesAndValues.get(LOCATION_TYPE);
			String locationTypeValue = null;
			if (tempValue != null)
			{
				locationTypeValue = tempValue.toString();
				_log.debug("LOG_DEBUG_EXTENSION", "/// " + LOCATION_TYPE + " Dropdown, Value : " + locationTypeValue, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/// " + LOCATION_TYPE + " Dropdown, Value is null", SuggestedCategory.NONE);
				uncheckAndEnableAllCheckBoxes(form);
				return RET_CONTINUE;
			}

			_log.debug("LOG_DEBUG_EXTENSION", "\n//// Performing Actions", SuggestedCategory.NONE);

			if (locationTypeValue.equals("PICK"))
			{
				uncheckAndEnableAllCheckBoxes(form);

				setValue(form.getFormWidgetByName(ALLOW_FROM_BULK), new String("1")); //Checked
				setValue(form.getFormWidgetByName(ALLOW_FROM_CASE), new String("1")); //Checked
				setValue(form.getFormWidgetByName(ALLOW_FROM_PIECE), new String("0")); //Unchecked

				setProperty(form.getFormWidgetByName(ALLOW_FROM_PIECE), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
			}
			else if (locationTypeValue.equals("CASE"))
			{
				uncheckAndEnableAllCheckBoxes(form);
				// No action for ALLOW_FROM_PIECE
				setValue(form.getFormWidgetByName(ALLOW_FROM_BULK), new String("1")); //Checked
				setValue(form.getFormWidgetByName(ALLOW_FROM_CASE), new String("0")); //UnChecked

				setProperty(form.getFormWidgetByName(ALLOW_FROM_CASE), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable

			}
			else
			{
				uncheckAndEnableAllCheckBoxes(form);
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void uncheckAndEnableAllCheckBoxes(RuntimeFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Setting the Widgets back to Editable Status and Clearing Checkboxes", SuggestedCategory.NONE);
		// Clear Checkboxes
		RuntimeFormWidgetInterface bulkWidget = form.getFormWidgetByName(ALLOW_FROM_BULK);
		setValue(bulkWidget, new String("0"));
		RuntimeFormWidgetInterface caseWidget = form.getFormWidgetByName(ALLOW_FROM_CASE);
		setValue(caseWidget, new String("0"));
		RuntimeFormWidgetInterface pieceWidget = form.getFormWidgetByName(ALLOW_FROM_PIECE);
		setValue(pieceWidget, new String("0"));

		//Enable Checkboxes
		setProperty(pieceWidget, RuntimeFormWidgetInterface.PROP_READONLY, "false"); // Editable
		setProperty(caseWidget, RuntimeFormWidgetInterface.PROP_READONLY, "false"); // Editable

	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}
}
