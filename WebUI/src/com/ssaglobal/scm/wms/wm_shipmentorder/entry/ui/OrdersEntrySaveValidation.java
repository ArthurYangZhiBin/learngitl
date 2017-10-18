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


package com.ssaglobal.scm.wms.wm_shipmentorder.entry.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.AutoFillAddress;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderPreSave;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderSaveValidation;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderSaveValidation.SOCarrier;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderSaveValidation.SOHeader;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderSaveValidation.SOMisc;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderSaveValidation.SOShipTo;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrdersEntrySaveValidation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * 
	 * @param ctx
	 *            the ctx
	 * @param args
	 *            the args
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 *             {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 *             <li>{@link com.epiphany.shr.ui.action.ActionResult
	 *             ActionResult} exposes information about the results of the
	 *             action that has occurred, and enables your extension to
	 *             modify them.</li>
	 *             </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		if (OrdersEntryUtil.savingDetailOnly(ctx)) {

			DataBean orderDetail = OrdersEntryUtil.getDetailFocus(ctx);
			orderDetailValidation(OrdersEntryUtil.getOrdersHeaderForm(ctx)
					.getFocus(), orderDetail, ctx);

		} else {
			DataBean order = OrdersEntryUtil.getParentFocus(ctx);
			orderValidation(order, ctx);
			DataBean orderDetail = OrdersEntryUtil.getDetailFocus(ctx);
			orderDetailValidation(order, orderDetail, ctx);
		}

		return RET_CONTINUE;
	}

	/**
	 * Order detail validation.
	 *
	 * @param order the order
	 * @param orderDetail the order detail
	 * @param ctx the ctx
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws EpiException the epi exception
	 */
	private void orderDetailValidation(DataBean order, DataBean orderDetail,
			ModalActionContext ctx) throws EpiDataException, UserException, EpiException {
		String owner = BioAttributeUtil.getString(orderDetail, "STORERKEY");
		String item = BioAttributeUtil.getString(orderDetail, "SKU");
		if (StringUtils.isEmpty(owner) && StringUtils.isEmpty(item)) {
			return;
		}
		
		StateInterface state = ctx.getState();
		RuntimeFormInterface detailForm = OrdersEntryUtil.getDetailForm(ctx);
		new ShipmentOrderPreSave(state).validateOrderDetailFormValues(order, detailForm, detailForm, state.getDefaultUnitOfWork());
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE01");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE02");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE03");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE06");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE07");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE08");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE09");
		BioAttributeUtil.checkNull(orderDetail, "LOTTABLE10");

	}

	/**
	 * Order validation.
	 * 
	 * @param order
	 *            the order
	 * @param ctx
	 *            the ctx
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 *             the user exception
	 */
	private void orderValidation(DataBean order, ModalActionContext ctx)
			throws EpiDataException, UserException {
		order.setValue("ORDERSID", GUIDFactory.getGUIDStatic());

		// capitals
		BioAttributeUtil.setUppercase(order, "POKEY");
		BioAttributeUtil.setUppercase(order, "PACKINGLOCATION");
		BioAttributeUtil.setUppercase(order, "CONSIGNEEKEY");
		BioAttributeUtil.setUppercase(order, "CarrierCode");
		BioAttributeUtil.setUppercase(order, "DOOR");
		BioAttributeUtil.setUppercase(order, "STAGE");

		// nulls
		BioAttributeUtil.checkNull(order, "PACKINGLOCATION");
		BioAttributeUtil.checkNull(order, "ROUTE");
		BioAttributeUtil.checkNull(order, "DOOR");
		BioAttributeUtil.checkNull(order, "BILLTOKEY");
		BioAttributeUtil.checkNull(order, "CONSIGNEEKEY");

		// validations
		RuntimeFormInterface orderForm = OrdersEntryUtil.getParentForm(ctx);
		StateInterface state = ctx.getState();

		SOHeader soHeaderValidation = new ShipmentOrderSaveValidation().new SOHeader(
				orderForm, state);
		soHeaderValidation.run();
		SOMisc soMiscValidation = new ShipmentOrderSaveValidation().new SOMisc(
				orderForm, state);
		soMiscValidation.run();
		SOCarrier soCarrierValidation = new ShipmentOrderSaveValidation().new SOCarrier(
				orderForm, state);
		soCarrierValidation.run();
		SOShipTo soShipToValidation = new ShipmentOrderSaveValidation().new SOShipTo(
				orderForm, state);
		soShipToValidation.run();

		// Location
		if (!StringUtils.isEmpty(BioAttributeUtil.getString(order, "DOOR"))) {
			String location = BioAttributeUtil.getString(order, "DOOR");
			validateStageLocation(state, location, true);
		}

		if (!StringUtils.isEmpty(BioAttributeUtil.getString(order, "STAGE"))) {
			String location = BioAttributeUtil.getString(order, "STAGE");
			validateStageLocation(state, location, false);
		}

		// update Carrier Info
		if (!StringUtils.isEmpty(BioAttributeUtil.getString(order,
				"CONSIGNEEKEY"))) {
			AutoFillAddress autoFillAddress = new AutoFillAddress();
			autoFillAddress.updateStorerInformation(state, order, orderForm
					.getFormWidgetByName("CONSIGNEEKEY"));
		}
		// update Customer Info
		if (!StringUtils.isEmpty(BioAttributeUtil.getString(order,
				"CarrierCode"))) {
			AutoFillAddress autoFillAddress = new AutoFillAddress();
			autoFillAddress.updateStorerInformation(state, order, orderForm
					.getFormWidgetByName("CarrierCode"));
		}
	}

	/**
	 * Validate stage location.
	 * 
	 * @param state
	 *            the state
	 * @param location
	 *            the location
	 * @param door
	 *            the door
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 *             the user exception
	 */
	private void validateStageLocation(StateInterface state, String location,
			boolean door) throws EpiDataException, UserException {
		// Query Bio to see if Attribute exists
		Query query = null;
		BioCollection results = null;

		UnitOfWorkBean uow = state.getTempUnitOfWork();
		String queryStatement;
		if (door) {
			queryStatement = "wm_location.LOC = '"
					+ location
					+ "'"
					+ " AND (wm_location.LOCATIONTYPE = 'STAGED' OR wm_location.LOCATIONTYPE = 'DOOR') ";
		} else {
			queryStatement = "wm_location.LOC = '" + location + "'"
					+ " AND wm_location.LOCATIONTYPE = 'STAGED' ";
		}

		query = new Query("wm_location", queryStatement, null);
		results = uow.getBioCollectionBean(query);

		// If BioCollection size equals 0, return RET_CANCEL
		if (results.size() == 0) {
			throw new UserException("WMEXP_LOC_MUST_BE_STAGED",
					new Object[] { location });
		}
	}
}
