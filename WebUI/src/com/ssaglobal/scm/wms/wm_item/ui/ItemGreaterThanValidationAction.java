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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
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

public class ItemGreaterThanValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemGreaterThanValidationAction.class);

	private static final String SERIAL_NUMBER_START = "SERIALNUMBERSTART";

	private static final String SERIAL_NUMBER_NEXT = "SERIALNUMBERNEXT";

	private static final String SERIAL_NUMBER_END = "SERIALNUMBEREND";

	private static final String SERIAL_NUMBER_START_NAME = "Serial Number Start";

	private static final String SERIAL_NUMBER_NEXT_NAME = "Serial Number Next";

	private static final String SERIAL_NUMBER_END_NAME = "Serial Number End";

	//	 arrays to hold exceptions
	private String[] exceptionWidgets = new String[2];

	private String[] exceptionMessages = new String[2];

	private String[][] exceptionParameters = new String[2][];

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
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{

		// Widgets
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();

		RuntimeFormWidgetInterface serialNumberStart = form.getFormWidgetByName(SERIAL_NUMBER_START);
		RuntimeFormWidgetInterface serialNumberNext = form.getFormWidgetByName(SERIAL_NUMBER_NEXT);
		RuntimeFormWidgetInterface serialNumberEnd = form.getFormWidgetByName(SERIAL_NUMBER_END);
		
		//compare start to 0
		greaterThanZero(serialNumberStart);
		//compare next to start
		//greaterThan(serialNumberNext, serialNumberStart, SERIAL_NUMBER_START_NAME);
		//compare end to next
		//greaterThan(serialNumberEnd, serialNumberNext, SERIAL_NUMBER_NEXT_NAME);

		throwExceptions();
		return RET_CONTINUE;
	}

	private void throwExceptions()
	{
		//for(int i = 0 ; i < )
		
	}

	private void greaterThan(RuntimeFormWidgetInterface greaterWidget, RuntimeFormWidgetInterface lesserWidget, String lesserWidgetName)
	{
		

		_log.debug("LOG_SYSTEM_OUT","\n!@# Should be " + greaterWidget.getName() + " > " + lesserWidget.getName(),100L);
		//retrieve values
		Object tempGreaterValue = greaterWidget.getValue();
		Object tempLesserValue = lesserWidget.getValue();

		if ((tempGreaterValue == null) || (tempLesserValue == null))
		{
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Values Undeclared, doing Nothing",100L);
			return;
		}

		int greaterValue = ((Integer) tempGreaterValue).intValue();
		int lesserValue = ((Integer) tempLesserValue).intValue();


		//compare
		_log.debug("LOG_SYSTEM_OUT","\n!@# Comparing" + greaterValue + " > " + lesserValue,100L);
		if (greaterValue > lesserValue)
		{
			_log.debug("LOG_SYSTEM_OUT","Values Verified",100L);

		}
		else
		{
			String[] parameters = new String[1];
			parameters[0] = lesserWidget.getName();
			//_log.debug("LOG_SYSTEM_OUT","!@#" + thisWidget.getName() + " should be greater than " + otherWidget.getName(),100L);
			//throw new FieldException(form, thisWidget.getName(), "GREATER_VALIDATION", parameters);
		}

	}

	private void greaterThanZero(RuntimeFormWidgetInterface sourceWidget)
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n~!@ START OF greaterThanZero VALIDATION for " + sourceWidget.getName(),100L);

		Object tempValue = sourceWidget.getValue();
		if (tempValue != null)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Value is not null" + tempValue.toString(),100L);
		}
		double sourceWidgetValue = Double.parseDouble(tempValue.toString());

		_log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + sourceWidgetValue + " Value: " + sourceWidgetValue,100L);

		// If value < 0, return RET_CANCEL
		if (sourceWidgetValue < 0)
		{
			_log.debug("LOG_SYSTEM_OUT","//// greaterThanZero Validation Failed",100L);
			String[] parameters = new String[0];
			addException(sourceWidget, "NONNEGATIVE_VALIDATION", parameters);
			sourceWidget.setError("Invalid Value, Must Be Greater Than or Equal to 0", new Object[] {});
			//throw new FieldException(form, sourceWidgetName, ERROR_MESSAGE, parameters);
			//throw new FormException(ERROR_MESSAGE, parameters);

		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// greaterThanZero Validation Passed - " + sourceWidget.getName() + ": " + sourceWidgetValue,100L);
		}

	}

	private void addException(RuntimeFormWidgetInterface sourceWidget, String errorMessage, String[] parameters)
	{
//		int index = exceptionWidgets.length;
//		exceptionWidgets[index] = sourceWidget.getName();
//		exceptionMessages[index] = errorMessage;
//		exceptionParameters[index] = parameters;

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
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException
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
