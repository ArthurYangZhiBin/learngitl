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

package com.ssaglobal.scm.wms.wm_pickdetail.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_pickdetail.ui.PickSplitPickPreRender;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickSplitPickAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(PickSplitPickAction.class);

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
		RuntimeFormInterface listForm = getListForm(state, context);
		ArrayList selectedPicks = ((RuntimeListFormInterface) listForm)
				.getSelectedItems();
		if (selectedPicks != null && selectedPicks.size() == 1) {
			for (int i = 0; i < selectedPicks.size(); i++) {
				BioBean pick = (BioBean) selectedPicks.get(i);
				String status = BioAttributeUtil.getString(pick, "STATUS");
				if ("9".compareTo(status) <= 0) {
					throw new UserException("WMEXP_SPLIT_PICK_STATUS9",
							new String[] {});
				}
				result.setFocus(pick);
			}
		} else {
			throw new UserException("WMEXP_MAX_ONE", new String[] {});
		}
		((RuntimeListFormInterface) listForm).setSelectedItems(null);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private RuntimeFormInterface getListForm(StateInterface state, ActionContext context) {
		if("SO_PD_SplitPick".equals(context.getActionObject().getName()))
		{
			ArrayList<String> tabs = new ArrayList<String>();
			tabs.add("tab 8");
			RuntimeFormInterface listForm = FormUtil.findForm(state
					.getCurrentRuntimeForm(), "", "wm_shipmentorder_pickdetail_list_view", tabs, state);
			return listForm;
		}else
		{
			RuntimeFormInterface listForm = FormUtil.findForm(state
				.getCurrentRuntimeForm(), "", "wm_pickdetail_list_view", state);
			return listForm;
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

		try {
			splitPick(ctx, args);
		} catch (RuntimeException e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Split pick.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param args
	 *            the args
	 * @throws EpiException
	 */
	private void splitPick(ModalActionContext ctx, ActionResult args)
			throws EpiException {
		StateInterface state = ctx.getState();
		RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
		RuntimeFormInterface splitPickForm = FormUtil.findForm(modalBodyForm,
				"", "wm_pickdetail_split_header_view", state);
		RuntimeFormWidgetInterface newLPNWidget = splitPickForm
				.getFormWidgetByName("NEWLPN");
		RuntimeFormWidgetInterface qtyToMoveWidget = splitPickForm
				.getFormWidgetByName("QTYTOMOVE");
		String newLpn = newLPNWidget.getDisplayValue();
		DataBean pickDetail = modalBodyForm.getFocus();
		int qtyToMove = ((Double) NumericValidationCCF
				.parseNumber(qtyToMoveWidget.getDisplayValue())).intValue();
		String lpn = BioAttributeUtil.getString(pickDetail, "ID");
		double qty = BioAttributeUtil.getDouble(pickDetail, "QTY");
		// validation
		if (StringUtils.isEmpty(newLpn)) {
			log.error("PickSplitPickAction_splitPick", "New LPN is blank",
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_PICK_SPLIT_LPN_REQ", new String[] {});
		}

		if (lpn.equals(newLpn)) {
			log.error("PickSplitPickAction_splitPick",
					"New LPN is same as original LPN " + newLpn + " " + lpn,
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_PICK_SPLIT_LPN_EQ", new String[] {});
		}
		if (qtyToMove == 0 || qtyToMove == qty) {
			log.error("PickSplitPickAction_splitPick",
					"Qty To Move is either equal to 0 or same as original "
							+ qtyToMove + " " + qty,
					SuggestedCategory.APP_EXTENSION);
			throw new UserException("WMEXP_PICK_SPLIT_QTY",
					new Object[] { qty });
		}

		if (PickSplitPickPreRender.hasSerials(pickDetail)) {
			// validate that the user has selected the right number of serials
			int numOfSerialsSelected = calculate(ctx, args);
			if (numOfSerialsSelected != qtyToMove) {
				throw new UserException("WMEXP_PICK_SPLIT_SERIALS_REQ",
						new Object[] { qtyToMove, numOfSerialsSelected });
			}

			// insert serials in serial move table
			ArrayList serials = getSelectedSerials(ctx, args);
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			for (int i = 0; i < serials.size(); i++) {
				BioBean serial = (BioBean) serials.get(i);
				QBEBioBean serialMove = uow
						.getQBEBioWithDefaults("wm_serialmove");
				serialMove.set("STORERKEY", BioAttributeUtil.getString(serial,
						"STORERKEY"));
				serialMove
						.set("SKU", BioAttributeUtil.getString(serial, "SKU"));
				serialMove
						.set("LOT", BioAttributeUtil.getString(serial, "LOT"));
				serialMove
						.set("LOC", BioAttributeUtil.getString(serial, "LOC"));
				serialMove.set("ID", BioAttributeUtil.getString(serial, "ID"));
				serialMove.set("SERIALNUMBER", BioAttributeUtil.getString(
						serial, "SERIALNUMBER"));
				serialMove.save();
			}

			uow.saveUOW(true);
		}

		log.debug("PickSplitPickAction_splitPick", "Calling SP " + newLpn + " "
				+ qtyToMove, SuggestedCategory.APP_EXTENSION);
		
		Array params = new Array();
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		actionProperties.setProcedureParameters(params);
		params.add(new TextData(BioAttributeUtil.getString(pickDetail, "PICKDETAILKEY")));
		params.add(new TextData(newLpn));
		params.add(new TextData(qtyToMove));
		actionProperties.setProcedureName("SplitPickDetail");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}

	}

	private ArrayList getSelectedSerials(ModalActionContext ctx,
			ActionResult args) {
		StateInterface state = ctx.getState();
		RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
		RuntimeFormInterface serialsForm = FormUtil.findForm(modalBodyForm, "",
				"wm_pickdetail_split_serials_list_view", state);
		ArrayList selectedItems = ((RuntimeListFormInterface) serialsForm)
				.getAllSelectedItems();
		return selectedItems;
	}

	/**
	 * Calculate.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param args
	 *            the args
	 * @return the int
	 */
	private int calculate(ModalActionContext ctx, ActionResult args) {
		ArrayList selectedItems = getSelectedSerials(ctx, args);
		if (selectedItems == null) {
			return 0;
		}
		return selectedItems.size();

	}
}
