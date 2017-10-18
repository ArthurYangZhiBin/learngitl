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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintLaborClear extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

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

		StateInterface state = getCCFActionContext().getState();
		RuntimeFormInterface laborForm = form.getParentForm(state);

		ArrayList<RuntimeFormWidgetInterface> hours = new ArrayList<RuntimeFormWidgetInterface>(4);
		hours.add(laborForm.getFormWidgetByName("HOURSEA"));
		hours.add(laborForm.getFormWidgetByName("HOURSCS"));
		hours.add(laborForm.getFormWidgetByName("HOURSPL"));
		hours.add(laborForm.getFormWidgetByName("HOURSTOTAL"));

		ArrayList<RuntimeFormWidgetInterface> workers = new ArrayList<RuntimeFormWidgetInterface>(4);
		workers.add(laborForm.getFormWidgetByName("WORKERSEA"));
		workers.add(laborForm.getFormWidgetByName("WORKERSCS"));
		workers.add(laborForm.getFormWidgetByName("WORKERSPL"));
		workers.add(laborForm.getFormWidgetByName("WORKERSTOTAL"));

		ArrayList<RuntimeFormWidgetInterface> errors = new ArrayList<RuntimeFormWidgetInterface>(3);
		errors.add(laborForm.getFormWidgetByName("TOTALEACHES"));
		errors.add(laborForm.getFormWidgetByName("TOTALCASES"));
		errors.add(laborForm.getFormWidgetByName("TOTALPALLETS"));

		String action = formWidget.getName();

		if ("CLEAR ALL".equals(action))
		{
			for (int i = 0; i < hours.size(); i++)
			{
				setValue((RuntimeFormWidgetInterface) hours.get(i), "");
			}

			for (int i = 0; i < workers.size(); i++)
			{
				setValue((RuntimeFormWidgetInterface) workers.get(i), "");
			}
		}
		else if ("CLEAR HOURS".equals(action))
		{
			for (int i = 0; i < hours.size(); i++)
			{
				setValue((RuntimeFormWidgetInterface) hours.get(i), "");
			}
		}
		else if ("CLEAR WORKERS".equals(action))
		{
			for (int i = 0; i < workers.size(); i++)
			{
				setValue((RuntimeFormWidgetInterface) workers.get(i), "");
			}
		}

		for (int i = 0; i < errors.size(); i++)
		{
			setErrorMessage((RuntimeFormWidgetInterface) errors.get(i), "");
		}

		return RET_CONTINUE;

	}
}
