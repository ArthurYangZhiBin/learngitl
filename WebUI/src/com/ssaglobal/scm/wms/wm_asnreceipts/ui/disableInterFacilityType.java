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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class disableInterFacilityType extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	private static final String _INTERFACILITY_TYPE = "3";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(disableInterFacilityType.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION_disableInterFacilityType_execute", "Start of Disable Rec Type",
				SuggestedCategory.NONE);
		//retrieve value of the dependent attribute

		final String DROPDOWN_NAME = "TYPE";
		_log.debug("LOG_DEBUG_EXTENSION_disableInterFacilityType_execute", "Dropdown " + DROPDOWN_NAME,
				SuggestedCategory.NONE);

		//retrieve dropdown contents
		RuntimeFormWidgetInterface dropdownWidget = form.getFormWidgetByName(DROPDOWN_NAME);
		//get dependent dropdown current values

		List[] labelsAndValues = null;
		try
		{
			labelsAndValues = dropdownWidget.getDropdownContents().getValuesAndLabels(
					dropdownWidget.getDropdownContext());

			HashMap allWidgetsAndValues = getWidgetsValues(params);
			Object currentValue = allWidgetsAndValues.get(form.getFormWidgetByName(DROPDOWN_NAME));

			//Remove InterFacility Transfer Type
			if (labelsAndValues[0].contains(_INTERFACILITY_TYPE) == true)
			{
				int indexOf = labelsAndValues[0].indexOf(_INTERFACILITY_TYPE);
				labelsAndValues[0].remove(indexOf);
				labelsAndValues[1].remove(indexOf);
			}
			//			_log.debug("LOG_DEBUG_EXTENSION_disableInterFacilityType_execute", "List of Labels and Values-----",
			//					SuggestedCategory.NONE);
			//			for (int i = 0; i < labelsAndValues[1].size(); i++)
			//			{
			//				_log.debug("LOG_DEBUG_EXTENSION_disableInterFacilityType_execute", "Count " + i + ", Label: "
			//						+ labelsAndValues[0].get(i) + " Value: " + labelsAndValues[1].get(i), SuggestedCategory.NONE);
			//				if (labelsAndValues[0].get(i).equals("3"))
			//				{
			//					labelsAndValues[0].remove(i);
			//					labelsAndValues[1].remove(i);
			//				}
			//			}
			setDomain(dropdownWidget, labelsAndValues[0], labelsAndValues[1]);
			if (currentValue != null)
			{
				setValue(dropdownWidget, currentValue.toString());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_disableInterFacilityType_execute", "Bad Dependent Value",
					SuggestedCategory.NONE);
			//throw new FieldException(form, DROPDOWN_NAME, "Bad Dependent Value", new Object[] {});
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}
