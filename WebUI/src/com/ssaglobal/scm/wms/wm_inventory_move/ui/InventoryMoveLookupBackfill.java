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

package com.ssaglobal.scm.wms.wm_inventory_move.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
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

public class InventoryMoveLookupBackfill extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryMoveLookupBackfill.class);
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Inventory Move Lookup Backfill", SuggestedCategory.NONE);
		StateInterface state = ctx.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		//Modal Form
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "MODAL" + "\n", SuggestedCategory.NONE);
		RuntimeListFormInterface modalListForm = (RuntimeListFormInterface) ctx.getModalBodyForm(0);
		_log.debug("LOG_DEBUG_EXTENSION", modalListForm.getName(), SuggestedCategory.NONE);
		BioRef selectedModal = modalListForm.getSelectedListItem();
		BioBean selectedModalBean = uow.getBioBean(selectedModal);
		Object modalLoc = selectedModalBean.getValue("LOC");
		_log.debug("LOG_DEBUG_EXTENSION", "From MODAL LOC -  " + modalLoc, SuggestedCategory.NONE);

		//Source Form
		//TODO: Figure out how to set the display
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "LIST" + "\n", SuggestedCategory.NONE);
		RuntimeListFormInterface sourceListForm = (RuntimeListFormInterface) ctx.getSourceForm();
		_log.debug("LOG_DEBUG_EXTENSION", sourceListForm.getName(), SuggestedCategory.NONE);
		BioRef selected = sourceListForm.getSelectedListItem();
		BioBean selectedBean = uow.getBioBean(selected);
		_log.debug("LOG_DEBUG_EXTENSION", "Row - " + selectedBean.getValue("STORERKEY") + " " + selectedBean.getValue("SKU") + " "
				+ selectedBean.getValue("LOC"),  SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "SETTING VALUE ON LIST ROW" + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "Before/To Location " + selectedBean.getValue("TOLOCATION"), SuggestedCategory.NONE);
		selectedBean.setValue("TOLOCATION", modalLoc);
		_log.debug("LOG_DEBUG_EXTENSION", "After/To Location " + selectedBean.getValue("TOLOCATION"), SuggestedCategory.NONE);


		RuntimeFormWidgetInterface toLocWidget = sourceListForm.getFormWidgetByName("TOLOCATION");
		_log.debug("LOG_DEBUG_EXTENSION", "WBefore/To Location " + toLocWidget.getValue(), SuggestedCategory.NONE);
		toLocWidget.setDisplayValue((String)modalLoc);
		_log.debug("LOG_DEBUG_EXTENSION", "WAfter/To Location " + toLocWidget.getDisplayValue(), SuggestedCategory.NONE);

		
		
		String[] names = ctx.getTargetWidgetNames();
		for (int i = 0; i < names.length; i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!" + names[i], SuggestedCategory.NONE);
		}

		//Try to go from BioCollection
		BioCollectionBean targetFocus = (BioCollectionBean) ctx.getTargetFocus();
		Object lot = selectedBean.getValue("LOT");
		Object loc = selectedBean.getValue("LOC");
		Object id = selectedBean.getValue("ID");
		_log.debug("LOG_DEBUG_EXTENSION", "Key " + lot + " " + loc + " " + id, SuggestedCategory.NONE);
		Query collectionQuery = new Query("wm_lotxlocxid", "wm_lotxlocxid.LOT='" + lot + "' AND wm_lotxlocxid.LOC='"
				+ loc + "' AND wm_lotxlocxid.ID='" + id + "'", "");
		Bio bioTest = targetFocus.filterFirst(collectionQuery);
		_log.debug("LOG_DEBUG_EXTENSION", "Before/To Location " + bioTest.get("TOLOCATION"), SuggestedCategory.NONE);
		bioTest.set("TOLOCATION", modalLoc);
		_log.debug("LOG_DEBUG_EXTENSION", "After/To Location " + bioTest.get("TOLOCATION"), SuggestedCategory.NONE);

	

		return RET_CONTINUE;
	}
}
