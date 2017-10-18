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

package com.ssaglobal.scm.wms.wm_outbound_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OutboundActivitySaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(OutboundActivitySaveValidation.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		RuntimeFormInterface headerForm = FormUtil.findForm(context
				.getState()
				.getCurrentRuntimeForm(), "wms_list_shell", "wm_obactheader_detail_form", context
				.getState());
		if (headerForm != null) {
			validateOutboundActivity(headerForm.getFocus(), context);
		}

		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("Detail");
		RuntimeFormInterface detailForm = FormUtil.findForm(context
				.getState()
				.getCurrentRuntimeForm(), "wms_list_shell", "wm_obactdetail_detail_form", tabs,
				context.getState());
		if (headerForm != null && detailForm != null) {
			validateOutboundActivityDetail(headerForm.getFocus(), detailForm.getFocus(), context);
		}
		
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}


	/**
	 * Validate outbound activity detail.
	 *
	 * @param headerFocus the header focus
	 * @param detailFocus the detail focus
	 * @param context the context
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void validateOutboundActivityDetail(
			DataBean headerFocus,
			DataBean detailFocus,
			ActionContext context) throws EpiDataException, UserException {
		// check duplicates
		if (detailFocus.isTempBio()) {
			String obactKey = BioAttributeUtil.getString(headerFocus, "OBACTKEY");
			String type = BioAttributeUtil.getString(detailFocus, "TYPE");

			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean rs = uow.getBioCollectionBean(new Query(
					"wm_obactdetail",
					"wm_obactdetail.OBACTKEY = '" + obactKey + "' and wm_obactdetail.TYPE = '"
							+ type + "'",
					null));
			if (rs.size() != 0) {
				throw new UserException("WMEXP_IAC_DETAIL_DUP", new String[] {});
			}
		}

	}

	/**
	 * There can only be 1 record can be created with the only the FACILITY
	 * field completed. (no duplicates)
	 * 
	 * There can only be 1 record with the same FACILITY, STORERKEY, SUPPLIER
	 * combination
	 * 
	 * There can be a record with only FACILITY and STORERKEY completed
	 * (SUPPLIER is blank)
	 * 
	 * There can be a record with only FACILITY and SUPPLIER completed
	 * (STORERKEY is blank)
	 * 
	 * @param focus
	 *            the focus
	 * @param context
	 *            the context
	 * @throws UserException
	 *             the user exception
	 * @throws EpiDataException
	 *             the epi data exception
	 */
	private void validateOutboundActivity(DataBean focus, ActionContext context)
			throws UserException, EpiDataException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		// determine what fields are blank to then check for duplicates

		validateStorer(BioAttributeUtil.getString(focus, "OWNER"), 1, uow);
		validateStorer(BioAttributeUtil.getString(focus, "CUSTOMER"), 2, uow);
		duplicateHeaderCheck(focus, uow);

	}

	/**
	 * Validate storer.
	 * 
	 * @param storerValue
	 *            the storer value
	 * @param type
	 *            the type
	 * @param uow
	 *            the uow
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 *             the user exception
	 */
	private void validateStorer(String storerValue, int type, UnitOfWorkBean uow)
			throws EpiDataException, UserException {
		if (!StringUtils.isEmpty(storerValue)) {
			storerValue = QueryHelper.escape(storerValue);
			storerValue = storerValue.toUpperCase();

			String query = "wm_storer.STORERKEY = '" + storerValue + "' and wm_storer.TYPE = '"
					+ type + "'";

			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_storer", query, ""));
			if (rs.size() != 1) {
				if (type == 1) {
					throw new UserException("WMEXP_INVALID_STORER", new String[] { storerValue });
				} else {
					throw new UserException("WMEXP_CONSIGNEE_VALIDATION", new String[] { storerValue });
				}
			}
		}
	}

	/**
	 * Duplicate header check.
	 * 
	 * @param focus
	 *            the focus
	 * @param uow
	 *            the uow
	 * @throws UserException
	 *             the user exception
	 * @throws EpiDataException
	 *             the epi data exception
	 */
	private void duplicateHeaderCheck(DataBean focus, UnitOfWorkBean uow) throws UserException,
			EpiDataException {
		boolean checkForDuplicates = false;
		if (focus.isTempBio()) {
			checkForDuplicates = true;
		}
		if (focus.isBio()) {
			List updatedAttributes = ((BioBean) focus).getUpdatedAttributes();
			if (updatedAttributes.contains("OWNER") || updatedAttributes.contains("CUSTOMER")) {
				checkForDuplicates = true;
			}
		}
		String query;

		if (checkForDuplicates) {
			String facility = BioAttributeUtil.getString(focus, "FACILITY");
			String owner = BioAttributeUtil.getString(focus, "OWNER");
			String customer = BioAttributeUtil.getString(focus, "CUSTOMER");
			BioCollectionBean rs = null;
			if (!StringUtils.isNull(facility) && StringUtils.isNull(owner)
					&& StringUtils.isNull(customer)) {
				facility = QueryHelper.escape(facility);
				query = "wm_obactheader.FACILITY = '"
						+ facility
						+ "'  and (wm_obactheader.OWNER IS NULL or wm_obactheader.OWNER = '') and (wm_obactheader.CUSTOMER IS NULL or wm_obactheader.CUSTOMER = '')";

			} else if (!StringUtils.isNull(facility) && !StringUtils.isNull(owner)
					&& StringUtils.isNull(customer)) {
				facility = QueryHelper.escape(facility);
				facility = facility.toUpperCase();
				owner = QueryHelper.escape(owner);
				owner = owner.toUpperCase();
				query = "wm_obactheader.FACILITY = '" + facility + "' and wm_obactheader.OWNER = '"
						+ owner
						+ "' and (wm_obactheader.CUSTOMER IS NULL or wm_obactheader.CUSTOMER = '')";

			} else if (!StringUtils.isNull(facility) && StringUtils.isNull(owner)
					&& !StringUtils.isNull(customer)) {
				facility = QueryHelper.escape(facility);
				facility = facility.toUpperCase();
				customer = QueryHelper.escape(customer);
				customer = customer.toUpperCase();
				query = "wm_obactheader.FACILITY = '" + facility
						+ "' and wm_obactheader.CUSTOMER = '" + customer
						+ "' and (wm_obactheader.OWNER IS NULL or wm_obactheader.OWNER = '')";

			} else if (!StringUtils.isNull(facility) && !StringUtils.isNull(owner)
					&& !StringUtils.isNull(customer)) {
				facility = QueryHelper.escape(facility);
				facility = facility.toUpperCase();
				owner = QueryHelper.escape(owner);
				owner = owner.toUpperCase();
				customer = QueryHelper.escape(customer);
				customer = customer.toUpperCase();
				query = "wm_obactheader.FACILITY = '" + facility
						+ "' and wm_obactheader.CUSTOMER = '" + customer
						+ "' and wm_obactheader.OWNER = '" + owner + "'";

			} else {
				throw new UserException("WMEXP_IAC_HEADER_INVALID", new String[] { facility, owner,
						customer });
			}

			if (!focus.isTempBio()) {
				query += " and wm_obactheader.OBACTKEY != '"
						+ BioAttributeUtil.getString(focus, "OBACTKEY") + "'";
			}

			rs = uow.getBioCollectionBean(new Query("wm_obactheader", query, null));

			String[] errorArguments = new String[] { facility,
					StringUtils.isEmpty(owner) ? "" : owner,
					StringUtils.isEmpty(customer) ? "" : customer };

			if (rs != null && rs.size() != 0) {
				log.info("OnboundActivitySaveValidation_validateOnboundActivity", "" + rs.size(),
						SuggestedCategory.APP_EXTENSION);

				throw new UserException("WMEXP_OAC_HEADER_DUP", errorArguments);
			}

		}
	}
	
	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 * 
	 * @param ctx
	 *            the ctx
	 * @param args
	 *            the args
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
