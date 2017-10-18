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

package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
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

public class UnReceiveDetailLines extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The Constant STATUS_CLOSED. */
	private static final int STATUS_CLOSED = 11;

	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(UnReceiveDetailLines.class);

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
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		// check status
		RuntimeFormInterface receiptForm = FormUtil.findForm(state
				.getCurrentRuntimeForm(), "", "receipt_detail_view", state);
		DataBean receiptFocus = receiptForm.getFocus();
		String receiptKey = BioAttributeUtil.getString(receiptFocus,
				"RECEIPTKEY");
		log.info("UnReceiveDetailLines_execute", "Unreceiving " + receiptKey,
				SuggestedCategory.APP_EXTENSION);
		int status = Integer.parseInt(BioAttributeUtil.getString(receiptFocus,
				"STATUS"));
		if (status >= STATUS_CLOSED) {
			// do nothing
			return RET_CONTINUE;
		}
		// clear temp table
		clearRrTemp(uow, receiptKey);
		// call sp
		callReceiptReversalSP(receiptKey);

		// create adjustment records
		RuntimeFormInterface receiptDetailForm = retrieveListForm(state);
		if (receiptDetailForm != null
				&& receiptDetailForm instanceof RuntimeListFormInterface) {
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) receiptDetailForm;
			ArrayList<BioBean> selectedItems = listForm.getSelectedItems();
			if (selectedItems != null && selectedItems.size() > 0) {
				// create Adjustment Header
				String adjustmentKey = new KeyGenBioWrapper()
						.getKey("ADJUSTMENT");
				String headerStorer = BioAttributeUtil.getString(receiptFocus,
						"STORERKEY");
				QBEBioBean adjustmentHeader = uow
						.getQBEBioWithDefaults("wm_adjustment");
				adjustmentHeader.setValue("ADJUSTMENTKEY", adjustmentKey);
				adjustmentHeader.setValue("STORERKEY", headerStorer);
				adjustmentHeader.save();
				int adjustmentLine = 0;
				for (int k = 0; k < selectedItems.size(); k++) {

					BioBean receiptLine = selectedItems.get(k);
					QBEBioBean adjustmentDetail = uow
							.getQBEBioWithDefaults("wm_adjustmentdetail");
					String receiptLineNumber = BioAttributeUtil.getString(
							receiptLine, "RECEIPTLINENUMBER");
					BioCollectionBean rs = uow.getBioCollectionBean(new Query(
							"wm_rr_temp", "wm_rr_temp.RECEIPTKEY = '"
									+ receiptKey
									+ "' and wm_rr_temp.RECEIPTLINENUMBER = '"
									+ receiptLineNumber + "'", null));
					BioBean rrTemp = null;
					// create adjustment detail records
					if (rs.size() > 0) {
						for (int j = 0; j < rs.size(); j++) {

							rrTemp = rs.get("" + j);
						}
						adjustmentDetail.setValue("ADJUSTMENTKEY",
								adjustmentKey);
						adjustmentDetail
								.setValue("ADJUSTMENTLINENUMBER",
										CatchWeightDataDefaultValues
												.generateLineNumber(
														adjustmentLine, uow));
						adjustmentDetail.setValue("REASONCODE", "UNRECEIVE");
						adjustmentDetail.setValue("STORERKEY", BioAttributeUtil
								.getString(receiptLine, "STORERKEY"));
						adjustmentDetail.setValue("SKU", BioAttributeUtil
								.getString(receiptLine, "SKU"));
						adjustmentDetail.setValue("PACKKEY", BioAttributeUtil
								.getString(rrTemp, "PACKKEY"));
						adjustmentDetail.setValue("RECEIPTKEY", receiptKey);
						adjustmentDetail.setValue("RECEIPTLINENUMBER",
								receiptLineNumber);
						adjustmentDetail.setValue("UOM", BioAttributeUtil
								.getString(rrTemp, "UOM"));
						adjustmentDetail.setValue("LOT", BioAttributeUtil
								.getString(rrTemp, "LOT"));
						adjustmentDetail.setValue("ID", BioAttributeUtil
								.getString(rrTemp, "TOID"));
						adjustmentDetail.setValue("QTY", BioAttributeUtil
								.getDouble(rrTemp, "QTY")
								* -1);
						adjustmentDetail.setValue("LOC", BioAttributeUtil
								.getString(rrTemp, "TOLOC"));
						adjustmentDetail.setValue("REFERENCEKEY",
								BioAttributeUtil.getString(rrTemp, "ITRNKEY"));
						adjustmentDetail.save();
						adjustmentLine++;
					}
				}
			}
			listForm.setSelectedItems(null);

		}
		uow.saveUOW(true);

		// clear temp table
		clearRrTemp(uow, receiptKey);

		updateASNStatus(receiptKey);

		// requery
		uow.clearState();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("receipt",
				"receipt.RECEIPTKEY = '" + receiptKey + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean bioBean = rs.get("" + i);
			result.setFocus(bioBean);
		}
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Update asn status.
	 * 
	 * @param receiptKey
	 *            the receipt key
	 * @throws UserException
	 *             the user exception
	 */
	private void updateASNStatus(String receiptKey) throws UserException {
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array();
		parms.add(new TextData(receiptKey));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPUPDATEASNSTATUS");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}

	/**
	 * Retrieve list form.
	 * 
	 * @param state
	 *            the state
	 * @return the runtime form interface
	 */
	private RuntimeFormInterface retrieveListForm(StateInterface state) {
		ArrayList<String> listTabs = new ArrayList<String>();
		listTabs.add("wm_receiptdetail_list_view");
		RuntimeFormInterface receiptDetailForm = FormUtil.findForm(state
				.getCurrentRuntimeForm(), "wms_list_shell",
				"wms_ASN_Line_List_View", listTabs, state);
		return receiptDetailForm;
	}

	/**
	 * Call receipt reversal sp.
	 * 
	 * @param receiptKey
	 *            the receipt key
	 * @throws UserException
	 *             the user exception
	 */
	private void callReceiptReversalSP(String receiptKey) throws UserException {
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array();
		parms.add(new TextData(receiptKey));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSP_RECEIPTREVERSAL");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}

	/**
	 * Clear rr temp.
	 * 
	 * @param uow
	 *            the uow
	 * @param receiptKey
	 *            the receipt key
	 * @throws EpiException
	 *             the epi exception
	 */
	private void clearRrTemp(UnitOfWorkBean uow, String receiptKey)
			throws EpiException {
		BioCollectionBean rrTempRs = uow.getBioCollectionBean(new Query(
				"wm_rr_temp", "wm_rr_temp.RECEIPTKEY = '" + receiptKey + "'",
				null));
		for (int i = 0; i < rrTempRs.size(); i++) {
			BioBean rrTemp = rrTempRs.get("" + i);
			rrTemp.delete();
		}
		uow.saveUOW(false);
	}

}
