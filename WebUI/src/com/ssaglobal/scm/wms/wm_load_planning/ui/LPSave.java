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

package com.ssaglobal.scm.wms.wm_load_planning.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LPSave extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String OUTBOUNDLANE = "OUTBOUNDLANE";

	private static final String EXTERNALLOADID = "EXTERNALLOADID";

	private static final String STOP = "STOP";

	private static final String ROUTE = "ROUTE";

	private static final String WM_ORDERS_ORDERKEY_PREAMBLE = "wm_orders.ORDERKEY = '";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPSave.class);

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

		boolean saveChanges = false;

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		RuntimeFormInterface parent = form.getParentForm(state);

		RuntimeFormInterface headerForm = FormUtil.findForm(form, parent.getName(), "wm_load_planning_list_view", state);
		if (headerForm.isListForm()) {
			RuntimeListForm headerListForm = (RuntimeListForm) headerForm;
			BioCollectionBean headerListFocus = (BioCollectionBean) headerListForm.getFocus();
			// Perform validation for each item
			for (int i = 0; i < headerListFocus.size(); i++) {
				BioBean lpResult = headerListFocus.get("" + i);

				if (lpResult.hasBeenUpdated(ROUTE) || lpResult.hasBeenUpdated(STOP) || lpResult.hasBeenUpdated(EXTERNALLOADID) || lpResult.hasBeenUpdated(OUTBOUNDLANE)) {
					saveChanges = true;

					// get Order
					final String orderKey = lpResult.getString("ORDERKEY");
					String queryStmt = WM_ORDERS_ORDERKEY_PREAMBLE + orderKey + "' ";
					Query query = new Query("wm_orders", queryStmt, null);
					BioCollectionBean orders = uow.getBioCollectionBean(query);
					if (orders.size() <= 0) {
						String parameters[] = new String[1];
						parameters[0] = null;
						throw new FormException("WMEXP_ORDER_NOTFOUND", parameters);

					} else {
						BioBean order = orders.get("0");
						if (lpResult.hasBeenUpdated(ROUTE)) {
							String route = preventNull(lpResult.getString(ROUTE));
							order.set(ROUTE, route);
						}
						if (lpResult.hasBeenUpdated(STOP)) {
							String stop = preventNull(lpResult.getString(STOP));
							order.set(STOP, stop);
						}
						if (lpResult.hasBeenUpdated(EXTERNALLOADID)) {
							String extLoadId = lpResult.getString(EXTERNALLOADID) == null ? "" : lpResult.getString(EXTERNALLOADID);
							order.set(EXTERNALLOADID, extLoadId);
						}
						if (lpResult.hasBeenUpdated(OUTBOUNDLANE)) {
							String outboundLane = lpResult.getString(OUTBOUNDLANE);
							outboundLaneValidation(outboundLane, lpResult, uow);
						}
					}

				}
			}
		}
		SlotInterface detailSlot = parent.getSubSlot("wm_load_planning_template_slot2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

		if (detailForm != null && !"Blank".equalsIgnoreCase(detailForm.getName())) {
			String orderkey = detailForm.getFormWidgetByName("ORDERKEY").getDisplayValue();
			String queryStmt = WM_ORDERS_ORDERKEY_PREAMBLE + orderkey + "' ";
			Query query = new Query("wm_orders", queryStmt, null);
			BioCollectionBean orders = uow.getBioCollectionBean(query);
			if (orders.size() <= 0) {
				String parameters[] = new String[1];
				parameters[0] = null;
				throw new FormException("WMEXP_ORDER_NOTFOUND", parameters);

			} else {
				BioBean order = orders.get("0");
				BioBean loadplanning = (BioBean) detailForm.getFocus();
				// Bugaware 8572. Load Creation - not able to save data updates
				// Route and Stop cannot be null
				String route = preventNull(detailForm.getFormWidgetByName(ROUTE).getDisplayValue());//
				String stop = preventNull(detailForm.getFormWidgetByName(STOP).getDisplayValue());//
				String externalloadid = detailForm.getFormWidgetByName(EXTERNALLOADID).getDisplayValue() == null ? "" : detailForm.getFormWidgetByName(EXTERNALLOADID).getDisplayValue();

				_log.info(	"LOG_INFO_EXTENSION_LPSave_execute",
							"[LPSave] route:" + route + " stop:" + stop + " externalloadid:" + externalloadid,
							SuggestedCategory.NONE);

				// Some weird stuff was happening with nulls
				order.set(ROUTE, route);
				loadplanning.set(ROUTE, route);

				order.set(STOP, stop);
				loadplanning.set(STOP, stop);

				order.set(EXTERNALLOADID, externalloadid);
				loadplanning.set(EXTERNALLOADID, externalloadid);

				// LoadPlanning

				String outboundlane = detailForm.getFormWidgetByName(OUTBOUNDLANE).getDisplayValue();
				outboundLaneValidation(outboundlane, loadplanning, uow);

			}
		}

		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_LPSave_execute", e.getErrorMessage(), SuggestedCategory.NONE);
			throw new FormException("WMEXP_SAVE_FAILED", null);
		}

		// result.setFocus()
		_log.info("LOG_INFO_EXTENSION_LPSave_execute", "[LPSave] end", SuggestedCategory.NONE);
		return RET_CONTINUE;

	}

	private void outboundLaneValidation(String outboundlane, BioBean loadplanning, UnitOfWorkBean uow) throws EpiDataException, FormException {
		if (outboundlane != null && outboundlane.compareToIgnoreCase("") != 0) {
			String queryLocStmt = "wm_location.LOC = '" + outboundlane + "' ";
			Query queryLoc = new Query("wm_location", queryLocStmt, null);
			BioCollectionBean locs = uow.getBioCollectionBean(queryLoc);
			if (locs.size() == 0) {
				String parameters[] = new String[1];
				parameters[0] = outboundlane;
				throw new FormException("WMEXP_LANEKEY_VALIDATION", parameters);
			} else {
				loadplanning.set(OUTBOUNDLANE, outboundlane);
			}

		}
	}

	private String preventNull(String string) {
		if (StringUtils.isEmpty(string)) {
			return " ";
		} else {
			return string;
		}
	}

}
