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

package com.ssaglobal.scm.wms.wm_load_planning.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
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

public class LPRemoveRowAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPRemoveRowAction.class);
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

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
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
	protected int execute(ModalActionContext context, ActionResult result)
			throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of LPRemoveRowAction", SuggestedCategory.NONE);
		try
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface toolbarForm = context.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			

			RuntimeListForm headerListForm = null;
			if (headerForm.isListForm())
			{
				headerListForm = (RuntimeListForm) headerForm;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Not a list form", SuggestedCategory.NONE);
				return RET_CANCEL;
			}

			DataBean headerListFocus = headerListForm.getFocus();

			BioCollectionBean headerListFormCollection = null;
			if (headerListFocus.isBioCollection())
			{
				headerListFormCollection = (BioCollectionBean) headerListFocus;
				_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + headerListFormCollection.size(), SuggestedCategory.NONE);
			}

			ArrayList items = headerListForm.getAllSelectedItems();
			ArrayList itemsToFilter = new ArrayList();
			try
			{
			//iterate items
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedLoad = (BioBean) it.next();
				String loadPlanningKey = selectedLoad.getValue("LOADPLANNINGKEY").toString().toUpperCase();

				_log.debug("LOG_DEBUG_EXTENSION", "! Selected Item +++ LoadPlanningKey" + loadPlanningKey, SuggestedCategory.NONE);
				itemsToFilter.add(loadPlanningKey);
			}
			}catch(NullPointerException e)
			{
				//throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
				return RET_CONTINUE;
			}
			//prepare Query
			Query query;
			//Query query = new Query(BIO, queryStatement, null);

			StringBuffer queryStatement = new StringBuffer();
			for (int i = 0; i < itemsToFilter.size(); i++)
			{
				if (i >= 1)
				{
					queryStatement.append(" and ");
				}

				queryStatement.append(" wm_loadplanning.LOADPLANNINGKEY != '" + (String) itemsToFilter.get(i) + "' ");

			}
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY " + queryStatement, SuggestedCategory.NONE);

			//filter
			query = new Query("wm_loadplanning", queryStatement.toString(), null);
			BioCollectionBean filteredCollection =  (BioCollectionBean) headerListFormCollection.filter(query);
			_log.debug("LOG_DEBUG_EXTENSION", "Size of filtered " + filteredCollection.size(), SuggestedCategory.NONE);

			_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting Changes", SuggestedCategory.NONE);
			headerListForm.setFocus(filteredCollection);
			result.setFocus(filteredCollection);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
