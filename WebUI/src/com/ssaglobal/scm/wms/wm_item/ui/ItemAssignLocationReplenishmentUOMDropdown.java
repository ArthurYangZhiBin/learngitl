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
import java.util.List;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
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

public class ItemAssignLocationReplenishmentUOMDropdown extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationReplenishmentUOMDropdown.class);
	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param widget The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
	{

		

		//retrieve value of the dependent attribute
		DataBean formFocus = state.getFocus();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		
		if(formFocus instanceof BioBean)
		{
			formFocus = (BioBean) formFocus;
		}

		String dependentValue;
		try
		{
			dependentValue = formFocus.getValue("PACKKEY").toString();
		}
		catch( Exception e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!! Exception caught " + e.getMessage(), SuggestedCategory.NONE);
			dependentValue = form.getFormWidgetByName("PACKKEY").getValue().toString();
		}
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Dependent Value " + dependentValue, SuggestedCategory.NONE);
		
		
		
		//retrieve dropdown contents
		
		List depVal = new ArrayList();
		depVal.add(dependentValue);

		//get dependent dropdown current values

		List[] labelsAndValues = null;
		try
		{
			labelsAndValues = widget.getDropdownContentsLabelsAndValues(depVal);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# List of Labels and Values-----", SuggestedCategory.NONE);
			for (int i = 0; i < labelsAndValues[1].size(); i++)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Index " + i + ", Label: " + labelsAndValues[0].get(i) + " Value: "						+ labelsAndValues[1].get(i) + "\n", SuggestedCategory.NONE);
			}

			
			

		} catch (Exception e)
		{
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Bad Dependent Value", SuggestedCategory.NONE);
			//throw new FieldException(form, DROPDOWN_NAME, "Bad Dependent Value", new Object[] {});
			return RET_CANCEL;
		}
		return RET_CONTINUE;

	}
}
