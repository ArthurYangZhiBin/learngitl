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

package com.ssaglobal.scm.wms.wm_inbound_qc.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
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

public class InboundQCSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The _log. */
	protected static ILoggerCategory log = LoggerFactory.getInstance(InboundQCSaveValidation.class);

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

		RuntimeFormInterface listForm = FormUtil.findForm(context
				.getState()
				.getCurrentRuntimeForm(), "wms_list_shell", "wm_inbound_qc_list_view", context
				.getState());
		DataBean focus = listForm.getFocus();
		if (focus instanceof BioCollectionBean) {
			BioCollectionBean listFocus = (BioCollectionBean) focus;
			try {
				for (int i = 0; i < listFocus.size(); i++) {
					BioBean inboundQC = listFocus.get("" + i);
					if (inboundQC.getUpdatedAttributes() != null
							&& inboundQC.getUpdatedAttributes().size() > 0) {
						validateInboundQC(inboundQC, context);
					}
				}
			} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
				e.printStackTrace();
				log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored"
						+ e.getErrorMessage(), SuggestedCategory.NONE);
				log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
			}
		}
		
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Validate inbound qc.
	 *
	 * @param inboundQC the inbound qc
	 * @param context the context
	 * @throws UserException the user exception
	 */
	private void validateInboundQC(BioBean inboundQC, ActionContext context) throws UserException {
		String QC_COMPLETED = "C";
		String receiptKey = BioAttributeUtil.getString(inboundQC, "RECEIPTKEY");
		String receiptLine = BioAttributeUtil.getString(inboundQC, "RECEIPTLINENUMBER");
		double qcQtyInspected = BioAttributeUtil.getDouble(inboundQC, "QCQTYINSPECTED");
		double qcQtyRejected = BioAttributeUtil.getDouble(inboundQC, "QCQTYREJECTED");
		double qtyReceived = BioAttributeUtil.getDouble(inboundQC, "QTYRECEIVED");
		String qcStatus = inboundQC.get("QCSTATUS", true).toString();

		String rejectReason = BioAttributeUtil.getString(inboundQC, "QCREJREASON");
		log.info("LOG_EXTENSION", receiptKey + " " + receiptLine + " " + qcQtyInspected + " "
				+ qcQtyRejected + " " + qtyReceived + " " + rejectReason,
				SuggestedCategory.APP_EXTENSION);
		if (qcQtyInspected < qcQtyRejected) {
			// error
			log
					.error(
							"LOG_EXTENSION",
							"The Quantity Inspected must be greater than or equal to the Quantity Rejected",
							SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_INBOUNDQC_QTYINS_QTYREJ", new Object[]{receiptKey, receiptLine});
		}
		if (QC_COMPLETED.equalsIgnoreCase(qcStatus)) {//QC has completed but user try to edit.
			// error
			log
					.error(
							"LOG_EXTENSION",
							"Can not edit completed QC",
							SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_INBOUNDQC_QCSTATUS", new Object[]{receiptKey, receiptLine});
		}

		if (qcQtyInspected > qtyReceived) {
			// error
			log.error("LOG_EXTENSION",
					"The Quantity Inspected must be less than or equal to the Quantity Received",
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_INBOUNDQC_QTYINS_QTYREC", new Object[]{receiptKey, receiptLine});
		}

		if (qcQtyRejected > 0 && StringUtils.isEmpty(rejectReason)) {
			// error
			log.error("LOG_EXTENSION",
					"If the Quantity Rejected is > 0, A Reject Reason must be selected.",
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_INBOUNDQC_QTYREJ_REJREA", new Object[]{receiptKey, receiptLine});
		}

		if (!StringUtils.isEmpty(rejectReason) && qcQtyRejected <= 0) {
			// error
			log.error("LOG_EXTENSION",
					"If a Reject Reason is selected, the Quantity Rejected must be > 0",
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_INBOUNDQC_QTYREJ_REJREB", new Object[]{receiptKey, receiptLine});
		}

		// set Inspected By
//		inboundQC.setValue("QCUSER", context.getState().getUser().getName());
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		inboundQC.setValue("QCUSER", (userContext.get("logInUserId")).toString());
		
		
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window.
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
