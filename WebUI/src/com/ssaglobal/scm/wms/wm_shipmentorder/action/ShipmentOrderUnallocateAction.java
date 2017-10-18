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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class ShipmentOrderUnallocateAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {
	
	/** The Constant SO_UNALLOCATE. */
	private static final String SO_UNALLOCATE = "SO_UNALLOCATE";
	
	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ShipmentOrderUnallocateAction.class);

	/**
	 * Before unallocate.
	 *
	 * @param context the context
	 * @throws EpiDataException the epi data exception
	 */
	private void beforeUnallocate(ActionContext context)
			throws EpiDataException {
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getRuntimeForm(context
				.getSourceWidget().getForm().getParentForm(state).getSubSlot(
						"list_slot_1"), null);
		DataBean currentFocus = form.getFocus();

		if (currentFocus.isBioCollection()) {

			List<String> orders = new ArrayList<String>();
			RuntimeListFormInterface list = (RuntimeListFormInterface) form;
			List<BioBean> selectedOrders = list.getSelectedItems();
			if (selectedOrders != null && selectedOrders.size() > 0) {
				// should allocate selectedOrders?
				for (int i = 0; i < selectedOrders.size(); i++) {
					BioBean order = selectedOrders.get(i);
					orders.add(BioAttributeUtil
								.getString(order, "ORDERKEY")); 

				}
			}
			SessionUtil.setInteractionSessionAttribute(SO_UNALLOCATE, orders,
					state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_MESSAGE,
					"WMTXT_SO_UNALLOCATE_LIST", state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_ARGS,
					new Object[] { orders.size() }, state);
			list.setSelectedItems(null);
			context.setNavigation("listMessage");
		} else {
			SessionUtil
					.setInteractionSessionAttribute(SO_UNALLOCATE,
							BioAttributeUtil
									.getString(currentFocus, "ORDERKEY"), state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_MESSAGE,
					"WMTXT_SO_UNALLOCATE_SINGLE", state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_ARGS, new Object[] {},
					state);
			context.setNavigation("singleMessage");
		}
	}

	/**
	 * Clear session variables.
	 *
	 * @param context the context
	 */
	private void clearSessionVariables(ActionContext context) {
		StateInterface state = context.getState();
		SessionUtil.setInteractionSessionAttribute(SO_UNALLOCATE, null, state);
		SessionUtil.setInteractionSessionAttribute(
				ShipmentOrderMessagePrerender.SO_MESSAGE, null, state);
		SessionUtil.setInteractionSessionAttribute(
				ShipmentOrderMessagePrerender.SO_ARGS, new Object[] {}, state);

	}

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

		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		if (sourceWidget != null && sourceWidget instanceof RuntimeMenuWidget) {
			// calling from menu
			// clearSession
			clearSessionVariables(context);	
			beforeUnallocate(context);

		} else {
			// calling from modal closing
			unallocate(context);
			clearSessionVariables(context);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}



	/**
	 * Unallocate.
	 *
	 * @param context the context
	 * @throws UserException the user exception
	 */
	private void unallocate(ActionContext context) throws UserException {
		StateInterface state = context.getState();
		Object ordersObj = SessionUtil.getInteractionSessionAttribute(
				SO_UNALLOCATE, state);
		SessionUtil.setInteractionSessionAttribute(SO_UNALLOCATE, null, state);
		if (ordersObj != null) {
			// call SP
			if (ordersObj instanceof List) {
				for (String orderkey : (List<String>) ordersObj) {
					unallocateOrder(orderkey);
				}
				//clear selected orders
				RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shipmentorder_list_view", state);
				((RuntimeListFormInterface)listForm).setSelectedItems(null);
				
				context.setNavigation("listRefresh");
			} else {
				unallocateOrder((String) ordersObj);
				context.setNavigation("singleRefresh");
			}
		} else {
			// error
		}

	}

	/**
	 * Unallocate order.
	 *
	 * @param orderkey the orderkey
	 * @throws UserException the user exception
	 */
	private void unallocateOrder(String orderkey) throws UserException {
		log.info("ShipmentOrderUnallocateAction_unallocate", "Unallocating "
				+ orderkey, SuggestedCategory.APP_EXTENSION);
		Array parms = new Array();
		parms.add(new TextData(orderkey));
		parms.add(new TextData("ALL"));
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPUNALLOCATEORDERS");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}

}
