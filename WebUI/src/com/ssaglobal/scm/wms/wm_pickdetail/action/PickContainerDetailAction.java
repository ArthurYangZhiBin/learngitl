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
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
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

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickContainerDetailAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickContainerDetailAction.class);

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

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		//Get List Focus
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
		try
		{
			if (items.isEmpty())
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		_log.debug("LOG_DEBUG_EXTENSION", "Arraylist Size: " + items.size(), SuggestedCategory.NONE);
		if (items.size() != 1)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Invalid results from selected items", SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		BioBean selectedItem = (BioBean) items.get(0);
		_log.debug("LOG_DEBUG_EXTENSION", "! From PICKDETAIL -  " + selectedItem.getValue("PICKDETAILKEY"), SuggestedCategory.NONE);

		//Query
		String pickDetailKey = selectedItem.getValue("PICKDETAILKEY") == null ? null
				: selectedItem.getValue("PICKDETAILKEY").toString().toUpperCase();

		String query = "wm_labelcontainerdetail.PICKDETAILKEY = '" + pickDetailKey + "'";
		Query modalQuery = new Query("wm_labelcontainerdetail", query, null);

		BioCollectionBean modalCollection = uow.getBioCollectionBean(modalQuery);
		_log.debug("LOG_DEBUG_EXTENSION", "BioColelction Size: " + modalCollection.size(), SuggestedCategory.NONE);
		if (modalCollection.size() != 1)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Invalid results from query", SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		BioBean modalBean = modalCollection.get("0");
		_log.debug("LOG_DEBUG_EXTENSION", "! From LABELCONTAINERDETAIL - " + modalBean.getValue("PICKDETAILKEY"), SuggestedCategory.NONE);

		result.setFocus(modalBean);
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
