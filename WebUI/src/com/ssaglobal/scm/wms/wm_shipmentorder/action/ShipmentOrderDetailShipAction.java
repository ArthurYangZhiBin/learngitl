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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
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

public class ShipmentOrderDetailShipAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The Constant SOD_SHIP. */
	private static final String SOD_SHIP = "SOD_SHIP";
	
	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ShipmentOrderDetailShipAction.class);
	
	/**
	 * Before ship.
	 *
	 * @param context the context
	 * @throws EpiDataException the epi data exception
	 */
	private void beforeShip(ActionContext context) throws EpiDataException {
		StateInterface state = context.getState();
		RuntimeFormInterface form = FormUtil.findForm(state
				.getCurrentRuntimeForm(), "",
				"wm_shipmentorder_lines_list_view", state);
		DataBean currentFocus = form.getFocus();

		if (currentFocus.isBioCollection()) {

			List<BioRef> orderLines = new ArrayList<BioRef>();
			RuntimeListFormInterface list = (RuntimeListFormInterface) form;
			List<BioBean> selectedOrderDetails = list.getSelectedItems();
			if (selectedOrderDetails != null && selectedOrderDetails.size() > 0) {
				// should ship selectedOrdersLines?
				for (int i = 0; i < selectedOrderDetails.size(); i++) {
					BioBean orderLine = selectedOrderDetails.get(i);
					boolean addOrder = shouldShipOrderDetail(orderLine);
					if (addOrder == true) {
						orderLines.add(orderLine.getBioRef());
					}

				}
			}
			SessionUtil.setInteractionSessionAttribute(SOD_SHIP, orderLines,
					state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_MESSAGE,
					"WMTXT_SOD_SHIP_LIST", state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_ARGS,
					new Object[] { orderLines.size() }, state);
			list.setSelectedItems(null);
			context.setNavigation("listMessage");
		}

	}

	/**
	 * Clear session variables.
	 *
	 * @param context the context
	 */
	private void clearSessionVariables(ActionContext context) {
		StateInterface state = context.getState();
		SessionUtil.setInteractionSessionAttribute(SOD_SHIP, null, state);
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
			beforeShip(context);

		} else {
			// calling from modal closing
			ship(context);
			clearSessionVariables(context);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Ship.
	 *
	 * @param context the context
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void ship(ActionContext context) throws EpiDataException, UserException {
		StateInterface state = context.getState();
		Object orderLineObj = SessionUtil.getInteractionSessionAttribute(
				SOD_SHIP, state);
		SessionUtil.setInteractionSessionAttribute(SOD_SHIP, null, state);
		if (orderLineObj != null) {
			// call SP
			if (orderLineObj instanceof List) {
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				uow.clearState();
				for (BioRef orderLineRef : (List<BioRef>) orderLineObj) {
					BioBean bioBean = uow.getBioBean(orderLineRef);
					BioCollectionBean pickDetails = (BioCollectionBean) bioBean
							.getValue("PICKDETAIL");

					if (pickDetails != null) {
						pickDetails.filterInPlace(new Query("wm_pickdetail",
								"wm_pickdetail.STATUS < '9'", null));
						log.info("ShipmentOrderPickAction_execute",
								"Pickdetail count " + pickDetails.size(),
								SuggestedCategory.APP_EXTENSION);
						for (int i = 0; i < pickDetails.size(); i++) {
							BioBean pickDetail = pickDetails.get("" + i);
							pickDetail.setValue("STATUS", "9");
							pickDetail.save();
						}
					}
				}
				try {
					uow.saveUOW(false);
				} catch (EpiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Throwable nested = e.findDeepestNestedException();
					if(nested instanceof ServiceObjectException)
					{
						throw new UserException(nested.getMessage(), new Object[] {});
					}
					else
					{
						throw new UserException(e.getMessage(), new Object[] {});
					}
				}
				//clear selected orders
				RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shipmentorder_lines_list_view", state);
				((RuntimeListFormInterface)listForm).setSelectedItems(null);
				context.setNavigation("listRefresh");
			}
		} else {
			// error
			log.error("ShipmentOrderDetailPickAction_pick",
					"Bad data came out of session",
					SuggestedCategory.APP_EXTENSION);
		}

	}

	/**
	 * Should ship order detail.
	 *
	 * @param currentFocus the current focus
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean shouldShipOrderDetail(BioBean currentFocus) throws EpiDataException {
		boolean pickOrder = false;
		
		BioCollectionBean pickDetails = (BioCollectionBean) currentFocus
				.getValue("PICKDETAIL");
		if (pickDetails != null) {
			pickDetails.filterInPlace(new Query("wm_pickdetail",
					"wm_pickdetail.STATUS < '9'", null));
			log.info("ShipmentOrderDetailPickAction_execute",
					"Pickdetail count " + pickDetails.size(),
					SuggestedCategory.APP_EXTENSION);
			if (pickDetails.size() > 0) {
				pickOrder = true;
			}
		}
		return pickOrder;
	}

}
