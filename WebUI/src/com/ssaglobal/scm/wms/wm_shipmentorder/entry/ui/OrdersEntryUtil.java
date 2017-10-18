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

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class OrdersEntryUtil.
 */
public class OrdersEntryUtil {

	/**
	 * Gets the detail focus.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return the detail focus
	 */
	protected static DataBean getDetailFocus(ModalActionContext ctx) {
		RuntimeFormInterface form = getDetailForm(ctx);
		return form.getFocus();
	}

	/**
	 * Gets the detail form.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return the detail form
	 */
	protected static RuntimeFormInterface getDetailForm(ModalActionContext ctx) {
		RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
		RuntimeFormInterface form = FormUtil.findForm(modalBodyForm, "",
				"wm_entry_orderdetail_view", ctx.getState());
		return form;
	}

	/**
	 * Gets the orders header form.
	 *
	 * @param ctx the ctx
	 * @return the orders header form
	 */
	protected static RuntimeFormInterface getOrdersHeaderForm(
			ModalActionContext ctx) {
		ArrayList tab = new ArrayList();
		tab.add("tab 0");
		RuntimeFormInterface headerForm = FormUtil.findForm(
				ctx.getSourceForm(), "", "wm_shipmentorder_header_view", tab,
				ctx.getState());
		return headerForm;
	}

	/**
	 * Gets the orders header form.
	 *
	 * @param context the context
	 * @return the orders header form
	 */
	protected static RuntimeFormInterface getOrdersHeaderForm(
			ModalUIRenderContext context) {
		ArrayList tab = new ArrayList();
		tab.add("tab 0");
		RuntimeFormInterface headerForm = FormUtil.findForm(context
				.getSourceForm(), "", "wm_shipmentorder_header_view", tab,
				context.getState());
		return headerForm;
	}

	/**
	 * Gets the parent focus.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return the parent focus
	 */
	protected static DataBean getParentFocus(ModalActionContext ctx) {
		RuntimeFormInterface form = getParentForm(ctx);
		return form.getFocus();
	}

	/**
	 * Gets the parent form.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return the parent form
	 */
	protected static RuntimeFormInterface getParentForm(ModalActionContext ctx) {
		RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
		RuntimeFormInterface form = FormUtil.findForm(modalBodyForm, "",
				"wm_entry_orders_view", ctx.getState());
		return form;
	}

	/**
	 * Gets the parent form.
	 *
	 * @param ctx the ctx
	 * @return the parent form
	 */
	protected static RuntimeFormInterface getParentForm(ModalUIRenderContext ctx) {
		RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
		RuntimeFormInterface form = FormUtil.findForm(modalBodyForm, "",
				"wm_entry_orders_view", ctx.getState());
		return form;
	}

	/**
	 * Saving detail only.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return true, if successful
	 */
	protected static boolean savingDetailOnly(ModalActionContext ctx) {
		if ("NEWENTRYDETAIL".equals(ctx.getSourceWidget().getName())) {
			return true;
		}
		if (getParentForm(ctx) == null) {
			return true;
		}
		return false;
	}

	/**
	 * Saving detail only.
	 *
	 * @param context the context
	 * @return true, if successful
	 */
	protected static boolean savingDetailOnly(ModalUIRenderContext context) {
		if ("NEWENTRYDETAIL".equals(context.getSourceWidget().getName())) {
			return true;
		}
		if (getParentForm(context) == null) {
			return true;
		}
		return false;
	}

}
