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

// Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class ItemPreventDelete extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemPreventDelete.class);
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
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface itemForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_list_view", state);

		RuntimeListFormInterface itemList = null;
		if(itemForm.isListForm()){
			itemList = (RuntimeListFormInterface) itemForm;
		}else{
			return RET_CANCEL;
		}

		DataBean listFormFocus = itemList.getFocus();
		BioCollectionBean itemCollection = null;
		if(listFormFocus.isBioCollection()){
			itemCollection = (BioCollectionBean) listFormFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: "+itemCollection.size(), SuggestedCategory.NONE);
		}

		ArrayList items = itemList.getAllSelectedItems();
		if(items!=null){
			//iterate items
			for(Iterator item = items.iterator(); item.hasNext();){
				BioBean temp = (BioBean) item.next();
				_log.debug("LOG_DEBUG_EXTENSION", "! Items Arraylist"+temp.getValue("STORERKEY"), SuggestedCategory.NONE);
			}

			BioBean selectedItem = (BioBean) items.get(0);
			String item = selectedItem.getValue("SKU") == null ? null : selectedItem.getValue("SKU").toString().toUpperCase();
			String owner = selectedItem.getValue("STORERKEY") == null ? null : selectedItem.getValue("STORERKEY").toString().toUpperCase();

			//query SKUXLOC (Assign Locations)
			String queryLoc = "SELECT * FROM SKUXLOC WHERE (SKU = '"+item+"') AND (STORERKEY = '"+owner+"')";
			_log.debug("LOG_DEBUG_EXTENSION", queryLoc, SuggestedCategory.NONE);
			EXEDataObject resultsLot = WmsWebuiValidationSelectImpl.select(queryLoc);
			if(resultsLot.getRowCount() > 0){
				throw new UserException("WMEXP_ITEM_DELETE_LOC", new Object[] {});
			}

			//Query ALTSKU
			String queryAlt = "SELECT * FROM ALTSKU WHERE (SKU = '"+item+"') AND (STORERKEY = '"+owner+"')";
			_log.debug("LOG_DEBUG_EXTENSION", queryAlt, SuggestedCategory.NONE);
			EXEDataObject resultsAlt = WmsWebuiValidationSelectImpl.select(queryAlt);
			if(resultsAlt.getRowCount() > 0){
				throw new UserException("WMEXP_ITEM_DELETE_ALT", new Object[] {});
			}

			//Query SUBSTITUTESKU
			String querySub = "SELECT * FROM SUBSTITUTESKU WHERE (SKU = '"+item+"') AND (STORERKEY = '"+owner+"')";
			_log.debug("LOG_DEBUG_EXTENSION", querySub, SuggestedCategory.NONE);
			EXEDataObject resultsSub = WmsWebuiValidationSelectImpl.select(querySub);
			if(resultsSub.getRowCount() > 0){
				throw new UserException("WMEXP_ITEM_DELETE_SUB", new Object[] {});
			}
		}else{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException{
		try{
			// Add your code here to process the event
		} catch(Exception e){
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}