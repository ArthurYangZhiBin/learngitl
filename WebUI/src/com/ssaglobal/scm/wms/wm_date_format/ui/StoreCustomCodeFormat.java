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


package com.ssaglobal.scm.wms.wm_date_format.ui;

import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import javax.servlet.http.HttpSession;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class StoreCustomCodeFormat extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(StoreCustomCodeFormat.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
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
    protected int execute(ModalActionContext context, ActionResult args) throws EpiException 
    {
    	_log.debug("LOG_SYSTEM_OUT","Modal store extension fired!",100L);
    	StateInterface state = context.getState();
    	RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
    	_log.debug("LOG_SYSTEM_OUT",toolbarForm.getName(),100L);
    	RuntimeFormInterface parentForm = toolbarForm.getParentForm(state);
    	_log.debug("LOG_SYSTEM_OUT",parentForm.getName(),100L);
    	SlotInterface slot = parentForm.getSubSlot("wm_date_format_custom_toggle_slot");
     	RuntimeFormInterface listForm = state.getRuntimeForm(slot, null);
    	_log.debug("LOG_SYSTEM_OUT",listForm.getName(),100L);
    	
		ArrayList select = new ArrayList();
		if ( listForm.isListForm() )
		{
			RuntimeListFormInterface headerList = (RuntimeListFormInterface)listForm;
			select = headerList.getAllSelectedItems();
			if (select != null && select.size() == 1)
			{
				BioBean row = (BioBean)select.get(0);
				HttpSession session = state.getRequest().getSession();			
				session.setAttribute("CUSTOMCODE", row.getValue("CUSTOMCODEFORMAT"));
			}
			else if (select != null && select.size() > 1)
			{
				throw new UserException("WMEXP_DF_MULTROWSELECTED", new Object[0]);
			}
			else
			{
				throw new UserException("WMEXP_DF_NOROWSSELECTED", new Object[0]);
			}
		}	

		return RET_CONTINUE;
    }
}
