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

package com.ssaglobal.scm.wms.wm_check_pack.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.wm_pickdetail.ui.SOCatchWeightCatchDataDefaultValues;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackGetSerials extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(CheckPackGetSerials.class);

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
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {

		StateInterface state = context.getState();

		RuntimeFormInterface listForm = state.getCurrentRuntimeForm()
				.getParentForm(state);

		ArrayList<BioBean> selectedPicks = ((RuntimeListForm) listForm)
				.getSelectedItems();
		if (selectedPicks == null || selectedPicks.size() == 0) {
			throw new UserException("WMEXP_DF_NOROWSSELECTED", new Object[] {});
		}
		if (selectedPicks.size() != 1) {
			throw new UserException("WMEXP_MAX_ONE", new Object[] {});
		}
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		List<String> queries = new ArrayList<String>();

		// Query for existing lotxiddetail records or create new lotxiddetail
		// records for the selected items
		for (BioBean pick : selectedPicks) {
			//Check it Item req Serials
			String sku = (String) pick.getValue("SKU");
			String owner = (String) pick.getValue("STORERKEY");
			BioCollectionBean rs1 = tuow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + owner + "' and sku.SKU = '" + sku + "'", null));
			BioBean skuBioBean = rs1.get("" + 0);
			String ocdflag = skuBioBean.getString("OCDFLAG");
			String sNumEndToEnd = skuBioBean.getString("SNUM_ENDTOEND");
			if(!"1".equals(sNumEndToEnd) && !"1".equals(ocdflag)) {
				throw new UserException("WMEXP_SN_NOT_REQ", new Object[]{sku});
			}
			//
			String orderKey = (String) pick.getValue("ORDERKEY");
			String orderLine = (String) pick.getValue("ORDERLINENUMBER");
			String pickDetailKey = (String) pick.getValue("PICKDETAILKEY");
			// for each pick, see if there are serial number records
			String query = "(lotxiddetail.IOFLAG = 'O' and lotxiddetail.SOURCEKEY = '"
					+ orderKey
					+ "' and lotxiddetail.SOURCELINENUMBER = '"
					+ orderLine
					+ "' and lotxiddetail.IOFLAG = 'O'"
					+ " and lotxiddetail.PICKDETAILKEY = '"
					+ pickDetailKey
					+ "')";
			log.debug("CheckPackGetSerials_execute", "Query " + query,
					SuggestedCategory.APP_EXTENSION);
			queries.add(query);
			BioCollectionBean rs = tuow.getBioCollectionBean(new Query(
					"lotxiddetail", query, null));

			Double serialQty = BioAttributeUtil.getDouble(pick, "QTY");
			// create the difference between records needed and records found
			createSerials(pick, serialQty.intValue() - rs.size(), uow);
		}

		// Query
		String finalQuery = StringUtils.join(queries.iterator(), " or ");
		log.debug("CheckPackGetSerials_execute", "Serial query " + finalQuery,
				SuggestedCategory.APP_EXTENSION);
		BioCollectionBean rs = uow.getBioCollectionBean(new Query(
				"lotxiddetail", finalQuery,
				"lotxiddetail.LOTXIDKEY, lotxiddetail.LOTXIDLINENUMBER"));
		result.setFocus(rs);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private void createSerials(BioBean pick, Integer numOfSerials,
			UnitOfWorkBean uow) throws EpiException {
		String orderKey = (String) pick.getValue("ORDERKEY");
		String orderLine = (String) pick.getValue("ORDERLINENUMBER");
		String pickDetailKey = (String) pick.getValue("PICKDETAILKEY");
		log.debug("CheckPackGetSerials_createSerials", "Going to create "
				+ numOfSerials + " serials for " + pickDetailKey,
				SuggestedCategory.APP_EXTENSION);

		for (int i = 0; i < numOfSerials; i++) {
			QBEBioBean newCWCD = uow.getQBEBioWithDefaults("lotxiddetail");
			SOCatchWeightCatchDataDefaultValues soCatchWeightCatchDataDefaultValues = new SOCatchWeightCatchDataDefaultValues();
			soCatchWeightCatchDataDefaultValues.setNewValues(uow, pick,
					orderKey, orderLine, pickDetailKey, newCWCD);
			newCWCD.setValue("WGT", 0);
			log.debug("LOG_DEBUG_EXTENSION_CatchWeightDuplicate_execute",
					"Going to Save "
							+ BioUtil.getBioAsString(newCWCD, uow
									.getBioMetadata(newCWCD.getDataType())),
					SuggestedCategory.NONE);
			uow.saveUOW(true);
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
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

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
