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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrdersEntryOwnerValidation extends
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

		RuntimeFormInterface currentForm = ctx.getState()
				.getCurrentRuntimeForm();

		if ("wm_entry_orders_view".equals(currentForm.getName())) {
			orderOwner(ctx, currentForm);

		} else {
			orderDetailOwner(ctx, currentForm);
		}

		return RET_CONTINUE;
	}

	/**
	 * Order owner.
	 *
	 * @param ctx the ctx
	 * @param currentForm the current form
	 * @throws EpiDataException the epi data exception
	 * @throws FieldException the field exception
	 */
	private void orderDetailOwner(ModalActionContext ctx,
			RuntimeFormInterface currentForm) throws EpiDataException, FieldException {
		final String BIO = "wm_storer";
		final String TYPE = "1";
		DataBean orderFocus = currentForm.getFocus();


		String owner = BioAttributeUtil.getString(orderFocus, "STORERKEY");

		String sQueryString = "(wm_storer.STORERKEY = '" + owner
				+ "' AND  wm_storer.TYPE = '" + TYPE + "')";

		Query bioQuery = new Query(BIO, sQueryString, null);
		UnitOfWorkBean uow = ctx.getState().getTempUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(bioQuery);

		if (rs.size() == 0) {

			throw new FieldException(currentForm, currentForm
					.getFormWidgetByName("STORERKEY").getName(),
					"WMEXP_OWNER_VALID", new String[] { owner });

		} else {
			
			orderFocus.setValue("SKU", "");
		}

	}

	/**
	 * Order detail owner.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param currentForm
	 *            the current form
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws FieldException
	 *             the field exception
	 */
	private void orderOwner(ModalActionContext ctx,
			RuntimeFormInterface currentForm) throws EpiDataException,
			FieldException {
		final String BIO = "wm_storer";
		final String TYPE = "1";
		DataBean ownerFocus = currentForm.getFocus();

		String owner = BioAttributeUtil.getString(ownerFocus, "STORERKEY");

		String sQueryString = "(wm_storer.STORERKEY = '" + owner
				+ "' AND  wm_storer.TYPE = '" + TYPE + "')";

		Query bioQuery = new Query(BIO, sQueryString, null);
		UnitOfWorkBean uow = ctx.getState().getTempUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(bioQuery);
		if (rs.size() == 0) {

			throw new FieldException(currentForm, currentForm
					.getFormWidgetByName("STORERKEY").getName(),
					"WMEXP_OWNER_VALID", new String[] { owner });

		} else {
			for (int i = 0; i < rs.size(); i++) {
				ownerFocus.setValue("ENABLEPACKING", rs.get("" + 0).getValue(
						"ENABLEPACKINGDEFAULT"));
				ownerFocus.setValue("PACKINGLOCATION", rs.get("" + 0).getValue(
						"DEFAULTPACKINGLOCATION"));
			}

		}

	}
}
