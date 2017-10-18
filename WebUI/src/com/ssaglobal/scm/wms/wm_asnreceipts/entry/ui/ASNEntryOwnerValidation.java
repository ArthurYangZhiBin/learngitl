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

package com.ssaglobal.scm.wms.wm_asnreceipts.entry.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNEntryOwnerValidation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ASNEntryOwnerValidation.class);

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

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
	}

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
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		RuntimeFormInterface currentForm = ctx.getState()
				.getCurrentRuntimeForm();

		if ("wm_entry_receipt_view".equals(currentForm.getName())) {
			receiptOwner(ctx, currentForm);

		} else {
			receiptDetailOwner(ctx, currentForm);
		}

		return RET_CONTINUE;

	}

	/**
	 * Receipt detail owner.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param currentForm
	 *            the current form
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws FieldException
	 *             the field exception
	 * @throws UserException
	 *             the user exception
	 */
	private void receiptDetailOwner(ModalActionContext ctx,
			RuntimeFormInterface currentForm) throws EpiDataException,
			FieldException, UserException {
		final String BIO = "wm_storer";
		final String TYPE = "1";
		DataBean receiptFocus = currentForm.getFocus();


		String owner = BioAttributeUtil.getString(receiptFocus, "STORERKEY");

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
			
			receiptFocus.setValue("SKU", "");
		}

	}

	/**
	 * Receipt owner.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param currentForm
	 *            the current form
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws FieldException
	 *             the field exception
	 * @throws UserException
	 *             the user exception
	 */
	private void receiptOwner(ModalActionContext ctx,
			RuntimeFormInterface currentForm) throws EpiDataException,
			FieldException, UserException {
		final String BIO = "wm_storer";
		final String TYPE = "1";
		String receiptType = null;
		DataBean receiptFocus = currentForm.getFocus();
		receiptType = BioAttributeUtil.getString(receiptFocus, "TYPE");

		String owner = BioAttributeUtil.getString(receiptFocus, "STORERKEY");

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
			if (receiptType == null) {
				throw new UserException("WMEXP_VALIDATETYPE_1", new Object[] {});

			}
			if ("4".equals(receiptType) || "5".equals(receiptType)) {

				log.debug("LOG_SYSTEM_OUT", "trackInventoryBy from storer = "
						+ rs.get("0").get("TRACKINVENTORYBY").toString(), 100L);
				for (int i = 0; i < rs.size(); i++) {
					receiptFocus.setValue("TRACKINVENTORYBY", BioAttributeUtil
							.getString(rs.get("" + i), "TRACKINVENTORYBY"));
				}

			}

			// set value in Detail
			// ASNEntryUtil.getDetailFocus(ctx).setValue("STORERKEY", owner);

		}
	}
}
