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

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class CatchInboundDataDisableEnable extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CatchInboundDataDisableEnable.class);
	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		
		
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		String checkboxName;
		String checkboxValue = null;
		ArrayList ICDdependentAttributes;
		ArrayList OCDdependentAttributes;
		try
		{
			// Get Handle on Form
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();

			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}
			else
			{
				currentFormFocus = (QBEBioBean) currentFormFocus;
			}
			// Get Checkbox Attribute
			checkboxName = getParameterString("CHECKBOX");
			Object tempValue = currentFormFocus.getValue(checkboxName);
			// Sanitize tempValue
			
			if (tempValue != null)
			{
				checkboxValue = tempValue.toString();
				if( checkboxValue.equals(""))
				{
					checkboxValue = "0";
				}
				
			}
			else
			{
				checkboxValue = "0";
			}
			// Get Dependent Attributes
			ICDdependentAttributes = (ArrayList) getParameter("ICDDEPENDENTATTR");
			OCDdependentAttributes = (ArrayList) getParameter("OCDDEPENDENTATTR");
			RuntimeFormInterface form = state.getCurrentRuntimeForm();
			RuntimeFormInterface inboundForm = state.getRuntimeForm(form.getSubSlot("inbound receipt"), "wm_item_weightdata_inboundreceipts_detail_view");
			RuntimeFormInterface outboundForm = state.getRuntimeForm(form.getSubSlot("outbound shipments"), "wm_item_weightdata_outboundshipments_detail_view");
			if ((checkboxValue.equals("1")) || (checkboxValue.equals("Y"))) // Checked
			{
					currentFormFocus.setValue("ICDFLAG", "1");
					currentFormFocus.setValue("OCDFLAG", "1");
					enableInputOnWidgets(inboundForm, ICDdependentAttributes);
					enableInputOnWidgets(outboundForm, OCDdependentAttributes);
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void enableInputOnWidgets(RuntimeFormInterface currentForm, ArrayList dependentAttributes)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Marking Widgets WRITABLE", SuggestedCategory.NONE);
		_log.debug("LOG_SYSTEM_OUT"," dependentSize="+dependentAttributes.size(),100L);
		for (Iterator it = dependentAttributes.iterator(); it.hasNext();)
		{
			Object o = it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "Widget " + o.toString(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(o.toString());
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		}

	}


}
