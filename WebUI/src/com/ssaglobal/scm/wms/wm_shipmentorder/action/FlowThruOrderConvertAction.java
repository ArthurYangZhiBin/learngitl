/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */

package com.ssaglobal.scm.wms.wm_shipmentorder.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderMessagePrerender;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class FlowThruOrderConvertAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {

		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getRuntimeForm(context
				.getSourceWidget().getForm().getParentForm(state).getSubSlot(
						"list_slot_1"), null);
		DataBean currentFocus = form.getFocus();

		if (currentFocus.isBioCollection()) {

			List<String> orders = new ArrayList<String>();
			RuntimeListFormInterface list = (RuntimeListFormInterface) form;
			List<BioBean> selectedItems = list.getSelectedItems();

			if ("wm_vwmallocationmgt".equals(currentFocus.getDataType())) {
				if (selectedItems != null && selectedItems.size() > 0) {
					for (int i = 0; i < selectedItems.size(); i++) {
						BioBean vWmAllocation = selectedItems.get(i);
						convertFTOByPO(vWmAllocation);
					}
				}
			} else {
				if (selectedItems != null && selectedItems.size() > 0) {

					for (int i = 0; i < selectedItems.size(); i++) {
						BioBean order = selectedItems.get(i);
						convertFTO(order);

					}
				}
			}

			list.setSelectedItems(null);
			context.setNavigation("listRefresh");
		} else {
			if ("wm_vwmallocationmgt".equals(currentFocus.getDataType())) {
				convertFTOByPO(currentFocus);
			} else {
				convertFTO(currentFocus);
			}
			context.setNavigation("singleRefresh");
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Convert fto.
	 *
	 * @param currentFocus the current focus
	 * @throws UserException the user exception
	 */
	private void convertFTO(DataBean currentFocus) throws UserException {
		String orderkey = BioAttributeUtil.getString(currentFocus,
				"ORDERKEY");
		String loadId = BioAttributeUtil.getString(currentFocus,
				"LOADID");
		convertAction(orderkey, loadId);
	}

	/**
	 * Convert fto by po.
	 *
	 * @param currentFocus the current focus
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void convertFTOByPO(DataBean currentFocus) throws EpiDataException,
			UserException {
		BioCollectionBean ordersByPO = (BioCollectionBean) currentFocus
				.getValue("ORDERS");
		for (int j = 0; j < ordersByPO.size(); j++) {
			BioBean orderPO = ordersByPO.get("" + j);
			convertFTO(orderPO);
		}
	}

	/**
	 * Convert action.
	 *
	 * @param orderkey the orderkey
	 * @param loadId the load id
	 * @throws UserException the user exception
	 */
	private void convertAction(String orderkey, String loadId)
			throws UserException {
		Array parms = new Array();
		parms.add(new TextData(orderkey));
		parms.add(new TextData("ALL"));
		parms.add(new TextData(loadId));
		parms.add(new TextData("0"));
		parms.add(new TextData("0"));
		parms.add(new TextData("0"));
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPCONVERTFLOWTHRUORDERS");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}

}
