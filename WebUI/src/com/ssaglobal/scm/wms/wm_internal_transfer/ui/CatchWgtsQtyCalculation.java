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

package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CatchWgtsQtyCalculation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {
//	A new class written as part of 3PL enhancements - Catchweights
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

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		
		/*
		 * This is written as part of 3 PL WM913 enhancements.
		 * Phani S Dec 04 2009.
		 * This piece of code calculates weights on tabbing out From Sku Qty field.
		 */

		System.out.println("################################################");
		System.out.println("#########  CatchWgtsQtyCalculation #############");

		try {
			RuntimeFormInterface currentForm = null;
			StateInterface state = context.getState();
			DataBean focus = state.getFocus();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormInterface subForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface detailForm = subForm.getParentForm(state);
			RuntimeFormInterface shellForm = null;
			shellForm = detailForm.getParentForm(state);

			//System.out.println("&&&&&&& *****" + subForm.getName()); // from

			ArrayList tabIdentifiers = new ArrayList();
			tabIdentifiers.clear();
			tabIdentifiers.add("tab 0");
			RuntimeFormInterface headerForm = FormUtil.findForm(detailForm,
					"wms_list_shell", "wm_internal_transfer_detail_view",
					tabIdentifiers, state);
			System.out.println("HEADER FORM" + headerForm.getName());
			DataBean headerFocus = headerForm.getFocus();

			String ownerVal = (String) headerFocus.getValue("FROMSTORERKEY");

			String frmSkuQty = subForm.getFormWidgetByName("FROMQTY")
					.getDisplayValue();
			String frmSku = subForm.getFormWidgetByName("FROMSKU")
					.getDisplayValue();
			String frmLot = subForm.getFormWidgetByName("FROMLOT")
					.getDisplayValue();
			String frmLoc = subForm.getFormWidgetByName("FROMLOC")
					.getDisplayValue();
			String frmLpn = subForm.getFormWidgetByName("FROMID")
					.getDisplayValue();
			String frmUom = subForm.getFormWidgetByName("FROMUOM")
					.getDisplayValue();
			String frmPack = subForm.getFormWidgetByName("FROMPACKKEY")
					.getDisplayValue();

			double frmSkuDqty = Double.parseDouble(frmSkuQty);
			Double frmGrossWgt = new Double(0);
			Double frmNetWgt = new Double(0);
			Double frmTareWgt = new Double(0);

			double gross = 0;
			double net = 0;
			double tare = 0;

			/*	System.out.println("&&&&&&& *****" + frmSkuQty);
			 System.out.println("&&&&&&& *****" + frmSku);
			 System.out.println("&&&&&&& *****" + frmLot);
			 System.out.println("&&&&&&& *****" + frmLoc);
			 System.out.println("&&&&&&& *****" + frmLpn);
			 System.out.println("&&&&&&& *****" + frmUom);
			 System.out.println("&&&&&&& *****" + frmPack); */

			CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();

			String enabledAdvCatWght = helper.isAdvCatchWeightEnabled(ownerVal,
					frmSku);

			HashMap qtyXwgts = null;

			if ((enabledAdvCatWght != null)
					&& (enabledAdvCatWght.equalsIgnoreCase("1"))) {
				qtyXwgts = helper.getCalculatedWeightsLPN(ownerVal, frmSku,
						frmLot, frmLoc, frmLpn, frmSkuDqty, frmUom, frmPack);

				frmGrossWgt = (Double) qtyXwgts.get("GROSSWEIGHT");
				frmNetWgt = (Double) qtyXwgts.get("NETWEIGHT");
				frmTareWgt = (Double) qtyXwgts.get("TAREWEIGHT");

				if (focus.isTempBio()) {
					QBEBioBean qbe = (QBEBioBean) focus;
					context.getState().getCurrentRuntimeForm();
					qbe.set("GROSSWGT", frmGrossWgt);
					qbe.set("NETWGT", frmNetWgt);
					qbe.set("TAREWGT", frmTareWgt);

				} else {
					BioBean bio = (BioBean) focus;
					bio.set("GROSSWGT", frmGrossWgt);
					bio.set("NETWGT", frmNetWgt);
					bio.set("TAREWGT", frmTareWgt);
				}

				if ((frmGrossWgt != null)
						&& (frmGrossWgt.toString().equals("NaN"))) {
					frmGrossWgt = 0.00;

				}

				if ((frmNetWgt != null) && (frmNetWgt.toString().equals("NaN"))) {
					frmNetWgt = 0.00;
				}

				if ((frmTareWgt != null)
						&& (frmTareWgt.toString().equals("NaN"))) {
					frmTareWgt = 0.00;
				}

				gross = NumericValidationCCF.parseNumber(frmGrossWgt + "");
				net = NumericValidationCCF.parseNumber(frmNetWgt + "");
				tare = NumericValidationCCF.parseNumber(frmTareWgt + "");

				subForm.getFormWidgetByName("GROSSWGT").setDisplayValue(
						gross + "");

				subForm.getFormWidgetByName("NETWGT")
						.setDisplayValue(net + "");

				subForm.getFormWidgetByName("TAREWGT").setDisplayValue(
						tare + "");

			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return super.execute(context, result);
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext}
	 * exposes information about the event, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
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
