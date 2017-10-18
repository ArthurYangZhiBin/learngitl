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
import java.util.List;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
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
public class PopulateDependentDropdownCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateDependentDropdownCCF.class);

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
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		String dependentWidgetName = getParameterString("DEPENDENT_WIDGET");
		boolean setDefault = getParameterBoolean("SET_DEFAULT");
		boolean setEA = getParameterBoolean("SET_EA");

		RuntimeFormWidgetInterface dependentWidget = form.getFormWidgetByName(dependentWidgetName);

		//retrieve dependent value
		Object tempValue = params.get("fieldValue");
		String dependentValue = null;
		if (tempValue != null)
		{
			dependentValue = tempValue.toString();
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Dependent Value " + dependentValue, SuggestedCategory.NONE);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Dependent Value is null", SuggestedCategory.NONE);
			setDropdownToNothing(dependentWidget);
			return RET_CONTINUE;
		}

		//retrieve dropdown contents
		List depVal = new ArrayList();
		depVal.add(dependentValue);

		List[] labelsAndValues = null;

		labelsAndValues = dependentWidget.getDropdownContentsLabelsAndValues(depVal);
		int eaLocation = Integer.MIN_VALUE;
		_log.debug("LOG_DEBUG_EXTENSION", "!@# List of Labels and Values-----", SuggestedCategory.NONE);
		for (int i = 0; i < labelsAndValues[1].size(); i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Index " + i + ", Label: " + labelsAndValues[0].get(i) + " Value: "
					+ labelsAndValues[1].get(i) + "\n", SuggestedCategory.NONE);
			//if value equals EA
			//remember position for setting the default value
			_log.debug("LOG_DEBUG_EXTENSION", "!$$ Comparing " + i + " - " + labelsAndValues[1].get(i) + " and EA", SuggestedCategory.NONE);
			if (labelsAndValues[1].get(i).toString().equalsIgnoreCase("EA"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Setting eaLocation " + i, SuggestedCategory.NONE);
				eaLocation = i;
			}
		}

		//populate dropdown based on dependent value
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Populating Dropdown With New Values", SuggestedCategory.NONE);
		setDomain(dependentWidget, labelsAndValues[0], labelsAndValues[1]);
		setProperty(dependentWidget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
		if (setDefault == true)
		{
			setDropdownToFirstValue(dependentWidget, labelsAndValues);
		}
		else if (setEA == true)
		{
			if (eaLocation != Integer.MIN_VALUE)
			{
				setDropdownToEa(dependentWidget, labelsAndValues, eaLocation);
			}
			else
			{
				setDropdownToFirstValue(dependentWidget, labelsAndValues);
			}
		}
		else
		{
			setDropdownToNothing(dependentWidget);
		}
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Trying to set widget active", SuggestedCategory.NONE);
		setProperty(dependentWidget, RuntimeFormInterface.PROP_READONLY, "false");

		return RET_CONTINUE;

	}

	private void setDropdownToEa(RuntimeFormWidgetInterface dependentWidget, List[] labelsAndValues, int eaLocation)
	{
		//set dropdown to EA
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Setting dropdown to Default EA " + labelsAndValues[1].get(eaLocation), SuggestedCategory.NONE);
		setValue(dependentWidget, (String) labelsAndValues[1].get(eaLocation));
	}

	private void setDropdownToFirstValue(RuntimeFormWidgetInterface dependentWidget, List[] labelsAndValues)
	{
		//set dropdown to 1st value
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Setting dropdown to " + labelsAndValues[1].get(0), SuggestedCategory.NONE);
		setValue(dependentWidget, (String) labelsAndValues[1].get(0));
	}

	private void setDropdownToNothing(RuntimeFormWidgetInterface dependentWidget)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Setting dropdown to nothing", SuggestedCategory.NONE);
		setValue(dependentWidget, "");
		setValue(dependentWidget, null);
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
