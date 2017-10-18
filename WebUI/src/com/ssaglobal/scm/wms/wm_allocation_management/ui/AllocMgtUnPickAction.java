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

package com.ssaglobal.scm.wms.wm_allocation_management.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AllocMgtUnPickAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AllocMgtUnPickAction.class);
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
	StateInterface state = context.getState();	
	UnitOfWorkBean uow = state.getDefaultUnitOfWork();
	ArrayList tabList = new ArrayList();		
	tabList.add("tab 1");					
	RuntimeFormInterface pickDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_pick_detail_list",tabList,state);
			
	if(pickDetailForm != null)
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","Found Pick Detail Form:"+pickDetailForm.getName(),100L);		
	else
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","Found Pick Detail Form:Null",100L);		
	
	if(pickDetailForm == null || 
			((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems() == null || 
			((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems().size() == 0){
		String args[] = new String[0];							
		String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
		throw new UserException(errorMsg,new Object[0]);
	}
		ArrayList items = ((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems();
		//iterate items
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean temp = (BioBean) it.next();
			_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","! Items Arraylist" + temp.getValue("CASEID"),100L);			
		}

		BioBean selectedItem = (BioBean) items.get(0);
		int statusValue = Integer.parseInt(selectedItem.getValue("STATUS").toString());
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","!@ Status " + statusValue,100L);		
		
		if( (statusValue >= 5) && (statusValue < 9)) 
		{			
			_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC",")(*Setting status to 0",100L);
			selectedItem.setValue("STATUS", new Integer(0));
			_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","!@# Attempting to save",100L);			
			uow.saveUOW(true);
			uow.clearState();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTUNPIC","Unable to Unpick",100L);			
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
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException
	{

//		try
//		{
//			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Start of PickUnPickAction - Modal",100L);
//			//try to put something in session
//			StateInterface state = ctx.getState();
//			HttpSession session = state.getRequest().getSession();
//			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
//
//			//Retrieve from session
//			_log.debug("LOG_SYSTEM_OUT","Trying to retrieve from session",100L);
//			BioBean selectedItem = (BioBean) session.getAttribute("item");
//			session.removeAttribute("item");
//			_log.debug("LOG_SYSTEM_OUT","Item retrieved " + selectedItem.getValue("PICKDETAILKEY"),100L);
//		} catch (Exception e)
//		{
//
//			// Handle Exceptions 
//			e.printStackTrace();
//			return RET_CANCEL;
//		}

		return RET_CONTINUE;
	}
}
