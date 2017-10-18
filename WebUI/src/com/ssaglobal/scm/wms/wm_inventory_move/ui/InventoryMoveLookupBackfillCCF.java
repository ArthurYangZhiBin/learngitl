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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ModalCCFActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class InventoryMoveLookupBackfillCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryMoveLookupBackfillCCF.class);

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

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "InventoryMoveLookupBackfillCCF" + "\n", SuggestedCategory.NONE);

		_log.debug("LOG_DEBUG_EXTENSION", "Listing Paramters-----", SuggestedCategory.NONE);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_DEBUG_EXTENSION", key.toString() + " " + value.toString(), SuggestedCategory.NONE);
		}

		//Modal Form
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "MODAL" + "\n", SuggestedCategory.NONE);
		ModalCCFActionContext ctx = getModalCCFActionContext();
		UnitOfWorkBean uowb = ctx.getState().getDefaultUnitOfWork();
		RuntimeListFormInterface modalListForm = (RuntimeListFormInterface) form;
		_log.debug("LOG_DEBUG_EXTENSION", modalListForm.getName(), SuggestedCategory.NONE);
		BioRef selectedModal = modalListForm.getSelectedListItem();
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + selectedModal.getBioRefString() + "\n", SuggestedCategory.NONE);
		BioBean selectedModalBean = uowb.getBioBean(selectedModal);
		Object modalLoc = selectedModalBean.getValue("LOC");
		_log.debug("LOG_DEBUG_EXTENSION", "From MODAL LOC -  " + modalLoc, SuggestedCategory.NONE);

		//Source Form
		//TODO: Figure out how to set the display
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "LIST" + "\n", SuggestedCategory.NONE);
		RuntimeListFormInterface sourceListForm = (RuntimeListFormInterface) ctx.getSourceForm();
		_log.debug("LOG_DEBUG_EXTENSION", sourceListForm.getName(), SuggestedCategory.NONE);
		BioRef selected = sourceListForm.getSelectedListItem();
		BioBean selectedBean = uowb.getBioBean(selected);
		_log.debug("LOG_DEBUG_EXTENSION", "Row - " + selectedBean.getValue("STORERKEY") + " "
				+ selectedBean.getValue("SKU") + " " + selectedBean.getValue("LOC"), SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "SETTING VALUE ON LIST ROW" + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "Before/To Location " + selectedBean.getValue("TOLOCATION"), SuggestedCategory.NONE);
		selectedBean.setValue("TOLOCATION", modalLoc);
		_log.debug("LOG_DEBUG_EXTENSION", "After/To Location " + selectedBean.getValue("TOLOCATION"), SuggestedCategory.NONE);

		RuntimeFormWidgetInterface toLocWidget = sourceListForm.getFormWidgetByName("TOLOCATION");
		_log.debug("LOG_DEBUG_EXTENSION", "WBefore/To Location " + toLocWidget.getValue(), SuggestedCategory.NONE);
		toLocWidget.setDisplayValue((String) modalLoc);
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
