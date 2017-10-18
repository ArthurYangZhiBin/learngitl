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

/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/

package com.ssaglobal.scm.wms.wm_shipmentorder.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_shipmentorder.action.util.ShipmentOrderPickActionUtil;
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

public class ShipmentOrderPickAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ShipmentOrderPickAction.class);
	
	/** The Constant SO_PICK. */
	private static final String SO_PICK = "SO_PICK";

	/**
	 * Before pick.
	 *
	 * @param context the context
	 * @throws EpiDataException the epi data exception
	 */
	private void beforePick(ActionContext context) throws EpiDataException {
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getRuntimeForm(context
				.getSourceWidget().getForm().getParentForm(state).getSubSlot(
						"list_slot_1"), null);
		DataBean currentFocus = form.getFocus();

		if (currentFocus.isBioCollection()) {

			List<BioRef> orders = new ArrayList<BioRef>();
			RuntimeListFormInterface list = (RuntimeListFormInterface) form;
			List<BioBean> selectedOrders = list.getSelectedItems();
			if (selectedOrders != null && selectedOrders.size() > 0) {
				// should pick selectedOrders?
				for (int i = 0; i < selectedOrders.size(); i++) {
					BioBean order = selectedOrders.get(i);
					boolean pickOrder = shouldPickOrder(order);
					if (pickOrder == true) {
						orders.add(order.getBioRef());
					}

				}
			}
			SessionUtil.setInteractionSessionAttribute(SO_PICK, orders, state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_MESSAGE,
					"WMTXT_SO_PICK_LIST", state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_ARGS,
					new Object[] { orders.size() }, state);
			list.setSelectedItems(null);
			context.setNavigation("listMessage");
		} else {
			boolean pickOrder = shouldPickOrder(currentFocus);
			SessionUtil
					.setInteractionSessionAttribute(SO_PICK, ((BioBean)currentFocus).getBioRef(), state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_MESSAGE,
					"WMTXT_SO_PICK_SINGLE", state);
			SessionUtil.setInteractionSessionAttribute(
					ShipmentOrderMessagePrerender.SO_ARGS, new Object[] {},
					state);
			context.setNavigation("singleMessage");
		}

	}

	/**
	 * Clear session variables.
	 * 
	 * @param context
	 *            the context
	 */
	private void clearSessionVariables(ActionContext context) {
		StateInterface state = context.getState();
		SessionUtil.setInteractionSessionAttribute(SO_PICK, null, state);
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
			beforePick(context);

		} else {
			// calling from modal closing
			pick(context);
			clearSessionVariables(context);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Pick.
	 *
	 * @param context the context
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	private void pick(ActionContext context) throws UserException, EpiDataException{
		StateInterface state = context.getState();
		Object ordersObj = SessionUtil.getInteractionSessionAttribute(SO_PICK,
				state);
		SessionUtil.setInteractionSessionAttribute(SO_PICK, null, state);
		if (ordersObj != null) {
			// call SP
			if (ordersObj instanceof List) {
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				uow.clearState();
				for (BioRef orderRef : (List<BioRef>) ordersObj) {
					BioBean bioBean = uow.getBioBean(orderRef);
					BioCollectionBean pickDetails = (BioCollectionBean) bioBean
							.getValue("PICKDETAIL");

					if (pickDetails != null) {
						pickDetails.filterInPlace(new Query("wm_pickdetail",
								"wm_pickdetail.STATUS < '5'", null));
						log.info("ShipmentOrderUnallocateAction_execute",
								"Pickdetail count " + pickDetails.size(),
								SuggestedCategory.APP_EXTENSION);
						for (int i = 0; i < pickDetails.size(); i++) {
							BioBean pickDetail = pickDetails.get("" + i);
							pickDetail.setValue("STATUS", "5");
							ShipmentOrderPickActionUtil.toLocValidation(pickDetail);
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
				RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shipmentorder_list_view", state);
				((RuntimeListFormInterface)listForm).setSelectedItems(null);
				
				context.setNavigation("listRefresh");
			} else {
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				uow.clearState();
				
				BioBean bioBean = uow.getBioBean((BioRef) ordersObj);
				BioCollectionBean pickDetails = (BioCollectionBean) bioBean
						.getValue("PICKDETAIL");

				if (pickDetails != null) {
					pickDetails.filterInPlace(new Query("wm_pickdetail",
							"wm_pickdetail.STATUS < '5'", null));
					log.info("ShipmentOrderUnallocateAction_execute",
							"Pickdetail count " + pickDetails.size(),
							SuggestedCategory.APP_EXTENSION);
					for (int i = 0; i < pickDetails.size(); i++) {
						BioBean pickDetail = pickDetails.get("" + i);
						pickDetail.setValue("STATUS", "5");
						ShipmentOrderPickActionUtil.toLocValidation(pickDetail);
						pickDetail.save();
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
				context.setNavigation("singleRefresh");
			}
		} else {
			// error
		}

	}

	/**
	 * Should pick order.
	 *
	 * @param currentFocus the current focus
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean shouldPickOrder(DataBean currentFocus)
			throws EpiDataException {
		boolean pickOrder = false;

		BioCollectionBean pickDetails = (BioCollectionBean) currentFocus
				.getValue("PICKDETAIL");
		if (pickDetails != null) {
			pickDetails.filterInPlace(new Query("wm_pickdetail",
					"wm_pickdetail.STATUS < '5'", null));
			log.info("ShipmentOrderUnallocateAction_execute",
					"Pickdetail count " + pickDetails.size(),
					SuggestedCategory.APP_EXTENSION);
			if (pickDetails.size() > 0) {
				pickOrder = true;
			}
		}
		return pickOrder;
	}
}
