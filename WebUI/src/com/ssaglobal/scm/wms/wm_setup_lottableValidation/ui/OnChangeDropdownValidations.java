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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_task_manager_area.bio.XTextFieldValidation;

public class OnChangeDropdownValidations extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnChangeDropdownValidations.class);
	protected int execute(ActionContext context, ActionResult result)
	throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n***In OnChangeDropdownValidations",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing OnChangeDropdownValidations",100L);
		try{
		// Get Handle on Form
		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		DataBean currentFormFocus = state.getFocus();
				
		RuntimeFormInterface shellForm = currentForm.getParentForm(state);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();
  
		
		if (currentFormFocus instanceof BioBean)
			currentFormFocus = (BioBean) currentFormFocus;
		else if(currentFormFocus instanceof QBEBioBean)
			currentFormFocus = (QBEBioBean) currentFormFocus;

		String reqCheckbox= null;
		String visibleCheckbox= null;
		
		reqCheckbox= (String)getParameter("REQCHECKBOXNAME");
		visibleCheckbox = (String)getParameter("VISIBLECHECKBOXNAME");

		
		RuntimeFormWidgetInterface widgetReq = headerForm.getFormWidgetByName("LOTTABLE01ONRFRECEIPTMANDATORY"); 
		RuntimeFormWidgetInterface widgetVisible = headerForm.getFormWidgetByName(visibleCheckbox);
		
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Req. Checkbox: " +widgetReq.getName(),100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Visible. Checkbox: " +widgetVisible.getName(),100L);
		
		String label = widgetReq.getName().substring(0, 10);
		
	       if (widgetReq.getValue() !=null) 
	        {	    	 
	        	if (widgetReq.getValue().equals(""))
	        			{
	        			headerFormFocus.setValue(widgetReq.getName(), "0");
	        			}
	        }
	        else {headerFormFocus.setValue(widgetReq.getName(), "0");
	        }
	        
	        
	        if (widgetVisible.getValue() !=null) 
	        {
	        	 if(widgetVisible.getValue().equals(""))
	 			 {headerFormFocus.setValue(widgetVisible.getName(), "0");}
	        }
	        else {headerFormFocus.setValue(widgetVisible.getName(), "0");}
		
		
		String validTemp= null;
		String convTemp= null;
			
		validTemp= (String)getParameter("VALIDDROPDOWN");
		convTemp = (String)getParameter("CONVDROPDOWN");
		
		
		Object validDropdown = currentFormFocus.getValue(validTemp);
		Object convDropdown = currentFormFocus.getValue(convTemp);
		
        String valid = null;
        String conv = null;
        
        
        if(validDropdown !=null)
        {
        	if(!validDropdown.equals("NONE"))
        	{
        		valid = validDropdown.toString();
        		checkCheckboxes(currentForm, widgetReq, widgetVisible, label);
        	}
        	else
        	{
        		checkCheckboxes(currentForm, widgetReq, widgetVisible, label);
        	}
        }
        else
        {
        	return RET_CANCEL;
        }
        
        if(convDropdown!=null)
        {
        	if(!convDropdown.equals("NONE"))
        	{
        		conv = convDropdown.toString();
        	}
        }
        else
        {
        	return RET_CANCEL;
        }
        
		}
		catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		
		
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting OnChangeDropdownValidations",100L);
		return RET_CONTINUE;
	}

	private int checkCheckboxes(RuntimeFormInterface currForm, RuntimeFormWidgetInterface req, RuntimeFormWidgetInterface visible, String labelName) 
	{
		// TODO Auto-generated method stub
		_log.debug("LOG_SYSTEM_OUT","\n\n*** In checkCheckboxes " ,100L);
		if(req.getValue().equals("0") || visible.getValue().equals("0"));
		{
			String errorMessage = "****Both required & visible should be checked for" +labelName;
			currForm.setError(errorMessage);
			_log.debug("LOG_SYSTEM_OUT","\n\n** Message has been set",100L);
			return RET_CANCEL;
		}
		
	}
}
