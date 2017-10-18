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

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ConvertLottable;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ReturnPartyQueryAction;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.SaveASNReceipt;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ItemFlags;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNEntrySaveVaildation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

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
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		// Add your code here to process the event
		boolean processContinue = true;
		if (ASNEntryUtil.savingDetailOnly(ctx)) {
			DataBean receiptDetail = ASNEntryUtil.getDetailFocus(ctx);
			processContinue =receiptDetailValidation(ASNEntryUtil.getASNHeaderForm(ctx)
					.getFocus(), receiptDetail, ctx);
		} else {
			DataBean receipt = ASNEntryUtil.getParentFocus(ctx);
			receiptValidation(receipt, ctx);
			DataBean receiptDetail = ASNEntryUtil.getDetailFocus(ctx);
			processContinue = receiptDetailValidation(receipt, receiptDetail, ctx);
		}
		if(!processContinue){
			return RET_CANCEL_EXTENSIONS; 
		}else{
			return RET_CONTINUE;
		}
	}

	/**
	 * Receipt detail validation.
	 * 
	 * @param receipt
	 *            the receipt
	 * @param receiptDetail
	 *            the receipt detail
	 * @throws EpiException
	 */
	private boolean receiptDetailValidation(DataBean receipt,
			DataBean receiptDetail, ModalActionContext ctx) throws EpiException {

		String owner = BioAttributeUtil.getString(receiptDetail, "STORERKEY");
		String item = BioAttributeUtil.getString(receiptDetail, "SKU");
		if (StringUtils.isEmpty(owner) && StringUtils.isEmpty(item)) {
			return true;
		}
		// if storerkey and sku are blank, skip validation
		receiptDetail.setValue("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
		receiptDetail.setValue("TYPE", receipt.getValue("TYPE"));

		// Loc Field
		setUppercase(receiptDetail, "TOLOC");
		SaveASNReceipt saveASNReceipt = new SaveASNReceipt();
		saveASNReceipt.packKeyValidation(ctx.getState(), ASNEntryUtil
				.getDetailForm(ctx), receiptDetail);
		saveASNReceipt.toLocValidation(ctx.getState(), receiptDetail);

		saveASNReceipt.dateCodeModsProcess(ctx, receiptDetail, item, owner);

		// sanity checking
		// req TOID
		setBlankForRequiredField(receiptDetail, "TOID");
		setUppercase(receiptDetail, "TOID");
		receiptDetail.setValue("PALLETID", BioAttributeUtil.getString(
				receiptDetail, "TOID"));
		advanceCatchWeightCheck(receiptDetail);
		saveASNReceipt.validatePOKey(BioAttributeUtil.getString(receiptDetail,
				"POKEY"), ctx.getState());

		// qty stuff

		RuntimeFormInterface detailForm = ASNEntryUtil.getDetailForm(ctx);
		String expectedQty = detailForm.getFormWidgetByName("QTYEXPECTED")
				.getDisplayValue();
		String receivedQty = detailForm.getFormWidgetByName("QTYRECEIVED")
				.getDisplayValue();

		String pack = BioAttributeUtil.getString(receiptDetail, "PACKKEY");
		String uom = BioAttributeUtil.getString(receiptDetail, "UOM");
		StateInterface state = ctx.getState();
		receiptDetail.setValue("QTYEXPECTED", UOMMappingUtil
				.numberFormaterConverter(LocaleUtil.getCurrencyLocale(), uom,
						UOMMappingUtil.UOM_EA, expectedQty, pack, state,
						UOMMappingUtil.uowNull, true));
		receiptDetail.setValue("QTYRECEIVED", UOMMappingUtil
				.numberFormaterConverter(LocaleUtil.getCurrencyLocale(), uom,
						UOMMappingUtil.UOM_EA, receivedQty, pack, state,
						UOMMappingUtil.uowNull, true));

		ItemFlags itemFlag = ItemFlags.getItemFlag(ctx.getState()
				.getTempUnitOfWork(), receiptDetail);
		
		
		

		// QC Required
		receiptDetail.setValue("QCREQUIRED", "0");

		// Lottable Conversion check
		lottableConversionValidation(ctx, detailForm);

		// More Validations
		String recDetailType = BioAttributeUtil
				.getString(receiptDetail, "TYPE");
		if (!(recDetailType.equalsIgnoreCase("4") || recDetailType
				.equalsIgnoreCase("5"))) {
			String lottableValidationKey = saveASNReceipt.getFieldValuefromSku(
					owner, item, "LOTTABLEVALIDATIONKEY", ctx);
			BioCollectionBean lottableValidationCollection = saveASNReceipt
					.getlottableValidation(lottableValidationKey, ctx);
			saveASNReceipt.checkLottableValidations(
					lottableValidationCollection, receipt, receiptDetail, ctx);
		}

		String receiptValidationKey = saveASNReceipt.getFieldValuefromSku(
				owner, item, "RECEIPTVALIDATIONTEMPLATE", ctx);
		saveASNReceipt.receiptValidations(receiptValidationKey,
				BioAttributeUtil.getString(receiptDetail, "POKEY"),
				BioAttributeUtil.getString(receiptDetail, "RECEIPTLINENUMBER"),
				item, ctx);

		
		//following code modified for bug 312
		if(itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true || itemFlag.isEnd2endFlag() == true){
			if(BioAttributeUtil.getDouble(receiptDetail, "QTYRECEIVED") != 0){
				boolean isDelaysNumCaptureEnabled = ItemFlags.getDelaysNumCaptureFlag(ctx.getState().getTempUnitOfWork());
				if(!isDelaysNumCaptureEnabled){
					throw new UserException("WMEXP_CWCD_REQUIRE_ERROR",new Object[] { item });					
				}else{
					ctx.setNavigation("clickEvent2052");
					return false;
					
				}
			}
		}
		
/*		if (BioAttributeUtil.getDouble(receiptDetail, "QTYRECEIVED") != 0
				&& ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true)
				|| (itemFlag.isEnd2endFlag() == true))) {
			throw new UserException("WMEXP_CWCD_REQUIRE_ERROR",
					new Object[] { item });
		}
*/
		
		return true;

	}

	/**
	 * Lottable conversion validation.
	 *
	 * @param ctx the ctx
	 * @param detailForm the detail form
	 * @throws EpiException the epi exception
	 * @throws EpiDataException the epi data exception
	 */
	private void lottableConversionValidation(ModalActionContext ctx, RuntimeFormInterface detailForm)
			throws EpiException, EpiDataException {
		{
			ConvertLottable convertLottable = new ConvertLottable();
			BioBean sku = convertLottable.getSkuBIO(ctx);

			if (sku == null) {
				return;
			}

			BioBean lottableVal = convertLottable.getLottable(ctx, sku
					.getString("LOTTABLEVALIDATIONKEY"));
			if (lottableVal == null) {
				return;
			}
			
			String[] widgets = new String[]{"01","02","03","06","07","08","09","10"};
			for(String widgetSuffix : widgets) {
				RuntimeFormWidgetInterface widget = detailForm.getFormWidgetByName("LOTTABLE" + widgetSuffix);
				if(!StringUtils.isEmpty(widget.getDisplayValue()))
				{
					convertLottable.checkCodeConvert(ctx, widget.getName(), detailForm, lottableVal);
				}
			}

		}
	}

	private void setBlankForRequiredField(DataBean receiptDetail,
			String attribute) {
		if (StringUtils.isEmpty(BioAttributeUtil.getString(receiptDetail,
				attribute))) {
			receiptDetail.setValue(attribute, " ");
		}
	}

	private void advanceCatchWeightCheck(DataBean receiptDetail)
			throws UserException {
		CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
		String enabledAdvCatWght = helper.isAdvCatchWeightEnabled(
				BioAttributeUtil.getString(receiptDetail, "STORERKEY"),
				BioAttributeUtil.getString(receiptDetail, "SKU"));
		if ((enabledAdvCatWght != null)
				&& (enabledAdvCatWght.equalsIgnoreCase("1"))) {

			double grosswgt = BioAttributeUtil.getDouble(receiptDetail,
					"GROSSWGT");

			double netwgt = BioAttributeUtil
					.getDouble(receiptDetail, "NETWGT");

			double tarewgt = BioAttributeUtil.getDouble(receiptDetail,
					"TAREWGT");

			if (!(grosswgt == netwgt + tarewgt)) {
				throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS",
						new Object[] {});
			}
		}
	}

	/**
	 * Receipt validation.
	 * 
	 * @param receipt
	 *            the receipt
	 * @param ctx
	 * @throws UserException
	 * @throws EpiDataException
	 */
	private void receiptValidation(DataBean receipt, ModalActionContext ctx)
			throws EpiDataException, UserException {
		receipt.setValue("RECEIPTID", GUIDFactory.getGUIDStatic());

		// Uppercase check
		setUppercase(receipt, "CARRIERREFERENCE");
		setUppercase(receipt, "WAREHOUSEREFERENCE");

		// Update Carrier Info
		if (!StringUtils.isEmpty(BioAttributeUtil.getString(receipt,
				"CARRIERKEY"))) {
			setUppercase(receipt, "CARRIERKEY");
			SaveASNReceipt.updateCarrierInfo(ctx.getState(), receipt,
					BioAttributeUtil.getString(receipt, "CARRIERKEY"));
		}

		// Update Carrier Info
		if (!StringUtils.isEmpty(BioAttributeUtil.getString(receipt,
				"SUPPLIERCODE"))) {
			setUppercase(receipt, "SUPPLIERCODE");
			SaveASNReceipt.updateSupplierInfo(ctx.getState(), receipt,
					BioAttributeUtil.getString(receipt, "SupplierCode"), ReturnPartyQueryAction.getStorerType(receipt));
		}

	}

	/**
	 * Sets the uppercase.
	 * 
	 * @param receipt
	 *            the receipt
	 * @param attribute
	 *            the attribute
	 */
	private void setUppercase(DataBean receipt, String attribute) {
		if (!StringUtils
				.isEmpty(BioAttributeUtil.getString(receipt, attribute))) {
			receipt.setValue(attribute, BioAttributeUtil.getString(receipt,
					attribute).toUpperCase());
		}
	}
}
