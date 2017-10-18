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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_shipmentorder.flowthru.ui.FlowThruPreRender;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrdersEntryPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	/** The KEYTEMPLATE. */
	private static String KEYTEMPLATE = "00000";

	/**
	 * Generate line number.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws NumberFormatException
	 *             the number format exception
	 */
	private void generateLineNumber(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiDataException,
			NumberFormatException {
		String zeroPadding = null;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
		Query bioQuery = new Query("wm_system_settings", sQueryString, null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
		try {
			zeroPadding = selCollection.elementAt(0).get("NSQLVALUE")
					.toString();
		} catch (EpiDataException e1) {
			e1.printStackTrace();
		}
		DecimalFormat template = null;
		if (zeroPadding.equalsIgnoreCase("0")) {
			template = new DecimalFormat("0");
		} else {
			template = new DecimalFormat(KEYTEMPLATE);
		}

		long size = 0;

		// adding new line to existing asn
		RuntimeFormInterface headerForm = OrdersEntryUtil
				.getOrdersHeaderForm(context);
		if (headerForm != null && OrdersEntryUtil.savingDetailOnly(context)) {
			DataBean receiptFocus = headerForm.getFocus();
			BioCollectionBean rs = (BioCollectionBean) receiptFocus
					.getValue("ORDER_DETAIL");
			if (rs == null || rs.size() == 0) {
				size = 0;
			} else {
				String max = (String) rs.max("ORDERLINENUMBER");
				size = Long.parseLong(max);
			}
		}

		String key = template.format(++size);

		form.getFocus().setValue("ORDERLINENUMBER", key);

	}

	/**
	 * Order detail form pre render.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws NumberFormatException
	 *             the number format exception
	 */
	private void orderDetailFormPreRender(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiDataException,
			NumberFormatException {

		if (form.getFocus().isTempBio()) {
			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"ORDERLINENUMBER"))) {
				generateLineNumber(context, form);
			}

			RuntimeFormInterface headerForm = OrdersEntryUtil
					.getOrdersHeaderForm(context);
			if (headerForm != null && OrdersEntryUtil.savingDetailOnly(context)) {
				form.getFocus().setValue(
						"ORDERKEY",
						BioAttributeUtil.getString(headerForm.getFocus(),
								"ORDERKEY"));

				if (StringUtils.isEmpty(BioAttributeUtil.getString(form
						.getFocus(), "STORERKEY"))) {
					form.getFocus().setValue(
							"STORERKEY",
							BioAttributeUtil.getString(headerForm.getFocus(),
									"STORERKEY"));
				}
			}

			List<String> ignoreList = new ArrayList<String>();
			ignoreList.add("STORERKEY");
			ignoreList.add("SKU");
			ignoreList.add("ORDERLINENUMBER");
			ignoreList.add("QTY");
			ignoreList.add("QTYPREALLOCATED");
			ignoreList.add("TOTALALLOCATED");
			ignoreList.add("QTYPICKED");
			ignoreList.add("QTYONHOLD");
			ignoreList.add("TOTALAVAILABLE");

			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"STORERKEY"))
					|| StringUtils.isEmpty(BioAttributeUtil.getString(form
							.getFocus(), "SKU"))) {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "true");

			} else {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "false");

				// show inventory
				showInventory(form, context.getState());
			}

		}

	}

	/**
	 * Show inventory.
	 * 
	 * @param form
	 *            the form
	 * @param state
	 *            the state
	 * @throws EpiDataException
	 *             the epi data exception
	 */
	private void showInventory(RuntimeNormalFormInterface form,
			StateInterface state) throws EpiDataException {
		DataBean focus = form.getFocus();
		String storerKey = BioAttributeUtil.getString(focus, "STORERKEY");
		String sku = BioAttributeUtil.getString(focus, "SKU");

		BioCollectionBean rs = state
				.getDefaultUnitOfWork()
				.getBioCollectionBean(
						new Query(
								"wm_VSKUINVENTORYAVAILABLE",
								"wm_VSKUINVENTORYAVAILABLE.STORERKEY = '"
										+ storerKey
										+ "' and wm_VSKUINVENTORYAVAILABLE.SKU = '"
										+ sku + "'", null));
		if (rs.size() > 0) {
			for (int i = 0; i < rs.size(); i++) {
				BioBean inventoryAvailable = rs.get("" + i);
				NumberFormat nf = LocaleUtil.getNumberFormat(
						LocaleUtil.TYPE_QTY, 0, 0);

				/*
				 * QTY QTYPREALLOCATED TOTALALLOCATED QTYPICKED QTYONHOLD
				 * TOTALAVAILABLE
				 */
				setInventoryWidget(form, inventoryAvailable, nf, "QTY");
				setInventoryWidget(form, inventoryAvailable, nf,
						"QTYPREALLOCATED");
				setInventoryWidget(form, inventoryAvailable, nf,
						"TOTALALLOCATED");
				setInventoryWidget(form, inventoryAvailable, nf, "QTYPICKED");
				setInventoryWidget(form, inventoryAvailable, nf, "QTYONHOLD");
				setInventoryWidget(form, inventoryAvailable, nf,
						"TOTALAVAILABLE");
			}
		}
	}

	/**
	 * Sets the inventory widget.
	 * 
	 * @param form
	 *            the form
	 * @param inventoryAvailable
	 *            the inventory available
	 * @param nf
	 *            the nf
	 * @param widget
	 *            the widget
	 */
	private void setInventoryWidget(RuntimeNormalFormInterface form,
			BioBean inventoryAvailable, NumberFormat nf, String widget) {
		form.getFormWidgetByName(widget).setDisplayValue(
				nf.format(BioAttributeUtil
						.getDouble(inventoryAvailable, widget)));
	}

	/**
	 * Order form pre render.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 */
	private void orderFormPreRender(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) {
		if (form.getFocus().isTempBio()) {
			List<String> ignoreList = new ArrayList<String>();
			ignoreList.add("STORERKEY");

			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"STORERKEY"))) {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "true");

			} else {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "false");
			}

			boolean isFlowThruScreen = FlowThruPreRender
					.determineIsFlowThruScreen(context.getState());
			if (isFlowThruScreen) {
				DataBean focus = form.getFocus();
				focus.setValue("TYPE", "91");
				form.getFormWidgetByName("TYPE").setBooleanProperty(
						RuntimeFormWidgetInterface.PROP_READONLY, true);

				// set required
				RuntimeFormWidgetInterface poKey = form
						.getFormWidgetByName("POKEY");
				RuntimeFormWidgetInterface consigneeKey = form
						.getFormWidgetByName("CONSIGNEEKEY");
				FormUtil.setWidgetAsRequired(poKey);
				FormUtil.setWidgetAsRequired(consigneeKey);
			}

		}

	}

	/**
	 * Called in response to the pre-render event on a form in a modal window.
	 * Write code to customize the properties of a form. This code is
	 * re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface,
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 *             {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *             service
	 */
	@Override
	protected int preRenderForm(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			if ("wm_entry_orders_view".equals(form.getName())) {
				orderFormPreRender(context, form);
			} else if ("wm_entry_orderdetail_view".equals(form.getName())) {
				orderDetailFormPreRender(context, form);
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
