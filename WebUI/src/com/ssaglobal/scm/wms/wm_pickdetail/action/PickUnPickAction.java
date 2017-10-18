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

package com.ssaglobal.scm.wms.wm_pickdetail.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
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

public class PickUnPickAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickUnPickAction.class);

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

		//try to put something in session
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);

		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface tempHeaderForm = state.getRuntimeForm(listSlot, null);

		RuntimeListFormInterface listForm = null;
		if (tempHeaderForm.isListForm())
		{

			listForm = (RuntimeListFormInterface) tempHeaderForm;
		}
		else
		{

		}
		DataBean listFormFocus = listForm.getFocus();
		BioCollectionBean listFormCollection = null;
		if (listFormFocus.isBioCollection())
		{
			listFormCollection = (BioCollectionBean) listFormFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + listFormCollection.size(), SuggestedCategory.NONE);
		}

		ArrayList items = listForm.getAllSelectedItems();

		//03/18/2010 FW:  Changed code to define objects and do uow.saveUOW(true) outside for loop for memory spike issue (Defect217952) -- Start
		//try
		//{
			//if (items.isEmpty())
			if ((items==null) || items.isEmpty())
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		//} catch (NullPointerException e)
		//{
			//throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		//}

		//iterate items
		BioBean selectedItem = null;
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			//BioBean selectedItem = (BioBean) it.next();
			selectedItem = (BioBean) it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "! Items Arraylist" + selectedItem.getValue("CASEID"), SuggestedCategory.NONE);
			int statusValue = Integer.parseInt(selectedItem.getValue("STATUS").toString());
			_log.debug("LOG_DEBUG_EXTENSION", "!@ Status " + statusValue, SuggestedCategory.NONE);

			if ((statusValue >= 5) && (statusValue < 9))
			{
				_log.debug("LOG_DEBUG_EXTENSION", ")(*Setting status to 0", SuggestedCategory.NONE);
				selectedItem.setValue("STATUS", new Integer(0));
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Attempting to save", SuggestedCategory.NONE);
				//uow.saveUOW(true);

			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Unable to Unpick", SuggestedCategory.NONE);
			}
		}
		uow.saveUOW(true);
		//03/18/2010 FW:  Changed code to define objects and do uow.saveUOW(true) outside for loop for memory spike issue (Defect217952) -- End

		//		
		//		_log.debug("LOG_DEBUG_EXTENSION", "Putting in session " + selectedItem.getValue("PICKDETAILKEY"), SuggestedCategory.NONE);
		//		session.setAttribute("item", selectedItem);
		uow.clearState();
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

		//		try
		//		{
		//			_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@# Start of PickUnPickAction - Modal", SuggestedCategory.NONE);
		//			//try to put something in session
		//			StateInterface state = ctx.getState();
		//			HttpSession session = state.getRequest().getSession();
		//			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//
		//			//Retrieve from session
		//			_log.debug("LOG_DEBUG_EXTENSION", "Trying to retrieve from session", SuggestedCategory.NONE);
		//			BioBean selectedItem = (BioBean) session.getAttribute("item");
		//			session.removeAttribute("item");
		//			_log.debug("LOG_DEBUG_EXTENSION", "Item retrieved " + selectedItem.getValue("PICKDETAILKEY"), SuggestedCategory.NONE);
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
