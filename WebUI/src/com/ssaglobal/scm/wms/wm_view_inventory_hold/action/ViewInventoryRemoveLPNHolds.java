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

package com.ssaglobal.scm.wms.wm_view_inventory_hold.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ViewInventoryRemoveLPNHolds extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ViewInventoryRemoveLPNHolds.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		_log.debug(	"LOG_DEBUG_EXTENSION_ViewInventoryRemoveLPNHolds_execute",
					"Entering execute",
					SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface listForm = toolbar.getParentForm(state);
		if (listForm.isListForm() == true) {
			RuntimeListFormInterface holdsList = (RuntimeListFormInterface) listForm;
			ArrayList<BioBean> selectedHolds = holdsList.getSelectedItems();

			// Validate user has only selected records with Hold type =ID
			// If any records have different hold types show error message “ Only license plates can be removed from
			// this screen”
			// If only License plate (ID) is selected remove hold for selected records by calling Call
			// NSPINVENTORYHOLDRESULTSET with InventoryHoldKey, lpn , status and hold=0
			for (int i = 0; i < selectedHolds.size(); i++) {
				BioBean holdBio = selectedHolds.get(i);
				String holdType = BioAttributeUtil.getString(holdBio, "HOLDTYPE");
				if (!"LPN".equals(holdType)) {
					throw new UserException("WMEXP_HOLD_LPN_REQ", new Object[] {});
				}
			}

			for (int i = 0; i < selectedHolds.size(); i++) {
				// calling SP
				BioBean holdBio = selectedHolds.get(i);
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(BioAttributeUtil.getString(holdBio, "INVENTORYHOLDKEY")));
				params.add(new TextData(""));
				params.add(new TextData(""));
				params.add(new TextData(BioAttributeUtil.getString(holdBio, "ID")));
				params.add(new TextData(BioAttributeUtil.getString(holdBio, "STATUS")));
				params.add(new TextData("0"));
				params.add(new TextData(""));
				
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("nspInventoryHoldResultSet");
				// run stored procedure
				EXEDataObject holdResults = null;
				try {
					holdResults = WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (WebuiException e) {
					_log.debug("LOG_DEBUG_EXTENSION", "" + "CATCH BLOCK 1" + "\n", SuggestedCategory.NONE);
					_log.debug("LOG_DEBUG_EXTENSION", "" + e.getMessage() + "\n", SuggestedCategory.NONE);
					throw new UserException(e.getMessage(), new Object[] {});
				}
				displayResults(holdResults);
			}

			holdsList.setSelectedItems(null);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_ViewInventoryRemoveLPNHolds_execute", "Exiting execute", SuggestedCategory.NONE);
		return RET_CONTINUE;
	}
	
	private void displayResults(EXEDataObject results) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("" + results.getRowCount() + " x " + results.getColumnCount() + "\n");
		if (results.getColumnCount() != 0) {

			pw.println("---Results");
			for (int i = 1; i < results.getColumnCount() + 1; i++) {
				try {
					pw.println(" " + i + " @ " + results.getAttribute(i).name + " " + results.getAttribute(i).value.getAsString());
				} catch (Exception e) {
					pw.println(e.getMessage());
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION", sw.toString(), SuggestedCategory.NONE);
	}

}
