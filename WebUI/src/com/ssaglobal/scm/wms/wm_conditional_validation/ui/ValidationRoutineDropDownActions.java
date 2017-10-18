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
package com.ssaglobal.scm.wms.wm_conditional_validation.ui;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.wm_item.ui.ItemAssignLocationsTypeDropdownAction;


public class ValidationRoutineDropDownActions extends
com.epiphany.shr.ui.action.ActionExtensionBase{

	private static String VALID_ROUTINE = "VALIDATIONROUTINE";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationsTypeDropdownAction.class);
  

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
			RuntimeFormWidgetInterface widgetAssisted = currentForm.getFormWidgetByName("REGRFPICK");
			RuntimeFormWidgetInterface widgetDirected = currentForm.getFormWidgetByName("TMPICK");
			
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}

			Object tempValue = currentFormFocus.getValue(VALID_ROUTINE);
			String validRoutineValue = null;
			if (tempValue != null)
			{
				validRoutineValue = tempValue.toString();
				_log.debug("LOG_SYSTEM_OUT","/// " + VALID_ROUTINE + " Dropdown, Value : " + validRoutineValue,100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","/// " + VALID_ROUTINE + " Dropdown, Value is null",100L);
			}

			_log.debug("LOG_SYSTEM_OUT","\n//// Performing Actions",100L);

			
			if (validRoutineValue.equals("POSTPIC01"))
			{
	
				widgetAssisted.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false); //Editable
				widgetDirected.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false); //Editable			
			}
			else 
			{
				widgetAssisted.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true); //Not Editable		

				// Nothing for ALLOW_FROM_PIECE
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
