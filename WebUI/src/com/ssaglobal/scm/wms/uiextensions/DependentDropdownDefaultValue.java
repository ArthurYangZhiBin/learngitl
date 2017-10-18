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

package com.ssaglobal.scm.wms.uiextensions;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DependentDropdownDefaultValue extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		//retrieve value of the dependent attribute
		StateInterface state = context.getState();
		DataBean formFocus = state.getFocus();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();

		final String DROPDOWN_NAME = getParameterString("DROPDOWN");

		String dependentValue = (String) context.getSourceWidget().getValue();
		dependentValue = dependentValue == null ? null : dependentValue.toUpperCase();

		//retrieve dropdown contents
		RuntimeFormWidgetInterface dropdownWidget = form.getFormWidgetByName(DROPDOWN_NAME);
		List depVal = new ArrayList();
		depVal.add(dependentValue);

		//get dependent dropdown current values

		List[] labelsAndValues = null;
		try
		{
			labelsAndValues = dropdownWidget.getDropdownContentsLabelsAndValues(depVal);
			int eaLocation = Integer.MIN_VALUE;
			for (int i = 0; i < labelsAndValues[1].size(); i++)
			{
				//if value equals EA
				//remember position for setting the default value
				if (labelsAndValues[1].get(i).toString().equalsIgnoreCase("EA"))
				{
					eaLocation = i;
				}
			}
			if (eaLocation == Integer.MIN_VALUE)
			{
				// set dropdown to 1st value
				formFocus.setValue(DROPDOWN_NAME, labelsAndValues[1].get(0));
			}
			else
			{
				formFocus.setValue(DROPDOWN_NAME, labelsAndValues[1].get(eaLocation));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			//throw new FieldException(form, DROPDOWN_NAME, "Bad Dependent Value", new Object[] {});
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
