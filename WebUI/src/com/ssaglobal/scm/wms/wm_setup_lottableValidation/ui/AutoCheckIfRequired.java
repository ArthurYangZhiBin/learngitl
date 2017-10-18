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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
 * Automatically checks "Visible" checkbox if "Required" checkbox is checked
 * Requires the name of the "Visible" or Linked checkbox passed as a parameter
 * <P>
 * 
 * @param LINKEDCHECKBOXNAME
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AutoCheckIfRequired extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AutoCheckIfRequired.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing AutoCheckIfRequired",100L);
		//String requiredCheckboxName = null;
		//String requiredCheckboxValue = null;
		//String linkedVisibleCheckboxName = null;
		//String linkedVisibleCheckboxValue = null;
		StateInterface state = null;
		DataBean currentFormFocus = null;
		RuntimeFormInterface headerForm = null;
		DataBean headerFormFocus = null;
		//String ERROR_MSG = null;

		try
		{
			// Get handle on the form
			state = context.getState();
			currentFormFocus = state.getFocus();
			
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);	
			
			    if (shellForm.getName().equals("wms_list_shell"))
			    {			
				SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
				headerForm = state.getRuntimeForm(headerSlot, null);
				headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();	
			    }
			    
			    RuntimeFormWidgetInterface visibleCheckbox = headerForm.getFormWidgetByName(getParameter("LINKEDCHECKBOXNAME").toString());
			    RuntimeFormWidgetInterface requiredCheckbox= context.getSourceWidget();
			 
			
				if (headerFormFocus instanceof BioBean)
				{
					headerFormFocus = (BioBean) headerFormFocus;
					_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Existing record",100L);
				}
				
				
				if(requiredCheckbox.getValue().equals("1"))
				{
					_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Required has been checked. Visible is being checked",100L);
					headerFormFocus.setValue(visibleCheckbox.getName(), "1");
					visibleCheckbox.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Required has been unchecked",100L);		
					headerFormFocus.setValue(visibleCheckbox.getName(), "0");
					visibleCheckbox.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					
					
				}
				
				_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting AutoCheckIfRequired",100L);
		return RET_CONTINUE;
	}
		catch (Exception e)
		{
			// Handle Exceptions
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exception",100L);
			return RET_CANCEL;
		}

	}
}
