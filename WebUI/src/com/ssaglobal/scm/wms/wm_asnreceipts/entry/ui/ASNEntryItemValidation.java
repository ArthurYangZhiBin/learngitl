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

package com.ssaglobal.scm.wms.wm_asnreceipts.entry.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ASNReceiptDetailPreRender;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ItemFlags;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNEntryItemValidation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String OWNER = "STORERKEY";
	private static final String ITEM = "SKU";
	private static final String TABLE_ITEM = "sku";
	private static String PACK = "PACKKEY";
	private static String TARIFF = "TARIFFKEY";
	private static String UOM = "UOM";
	private static String ERROR_MSG_OWNER = "WMEXP_ENTEROWNER";
	private static String ERROR_MSG_ITEM = "WMEXP_VALIDATESKU";

	private static String LV_REQ = "LOTTABLESREQ";
	private static String CWD_REQ = "CATCHWDREQ";

	private static String ZERO = "0";
	private static String ONE = "1";

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

		StateInterface state = ctx.getState();
		DataBean focus = state.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		String item = BioAttributeUtil.getString(focus, "SKU");
		String owner = BioAttributeUtil.getString(focus, "STORERKEY");

		owner = StringUtils.upperCase(owner);
		item = StringUtils.upperCase(item);
		
		String qry = null;
		Query query = null;
		qry = TABLE_ITEM + "." + OWNER + "='" + owner + "' AND " + TABLE_ITEM
				+ "." + ITEM + "='" + item + "'";
		
		

		// 04/14/2009
		query = new Query(TABLE_ITEM, qry, null);
		BioCollectionBean itemBio = uow.getBioCollectionBean(query);
		if ((itemBio == null) || (itemBio.size() < 1)) {
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); // AW

			String[] parameters = new String[2];
			parameters[0] = item;
			parameters[1] = owner;
			// currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue("");
			throw new UserException(ERROR_MSG_ITEM, parameters);
		} else {
			try {

				focus.setValue(PACK, isNull(itemBio, PACK));
				focus.setValue(TARIFF, isNull(itemBio, TARIFF));
				String packVal = isNull(itemBio, PACK);

				UOMDefaultValue.fillDropdown(state, UOM, packVal); // AW
				RuntimeFormInterface form = state.getCurrentRuntimeForm();
				form.getFormWidgetByName("LOTTABLE01").setLabel("label",isNull(itemBio,"LOTTABLE01LABEL")+":");
				form.getFormWidgetByName("LOTTABLE02").setLabel("label",isNull(itemBio,"LOTTABLE02LABEL")+":");
				form.getFormWidgetByName("LOTTABLE03").setLabel("label",isNull(itemBio,"LOTTABLE03LABEL")+":");
				form.getFormWidgetByName("LOTTABLE04").setLabel("label",isNull(itemBio,"LOTTABLE04LABEL")+":");
				form.getFormWidgetByName("LOTTABLE05").setLabel("label",isNull(itemBio,"LOTTABLE05LABEL")+":");
				form.getFormWidgetByName("LOTTABLE06").setLabel("label",isNull(itemBio,"LOTTABLE06LABEL")+":");
				form.getFormWidgetByName("LOTTABLE07").setLabel("label",isNull(itemBio,"LOTTABLE07LABEL")+":");
				form.getFormWidgetByName("LOTTABLE08").setLabel("label",isNull(itemBio,"LOTTABLE08LABEL")+":");
				form.getFormWidgetByName("LOTTABLE09").setLabel("label",isNull(itemBio,"LOTTABLE09LABEL")+":");
				form.getFormWidgetByName("LOTTABLE10").setLabel("label",isNull(itemBio,"LOTTABLE10LABEL")+":");
				form.getFormWidgetByName("LOTTABLE11").setLabel("label",isNull(itemBio,"LOTTABLE11LABEL")+":");
				form.getFormWidgetByName("LOTTABLE12").setLabel("label",isNull(itemBio,"LOTTABLE12LABEL")+":");

				// currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue(
				// isNull(itemBio, DESCRIPTION));
				// RuntimeFormWidgetInterface lottableReq = currentForm
				// .getFormWidgetByName(LV_REQ);
				// RuntimeFormWidgetInterface cwdReq = currentForm
				// .getFormWidgetByName(CWD_REQ);
				// String lvKey = isNull(itemBio, LV_KEY);
				// if (ASNReceiptDetailPreRender.LottableRequiredCheck(lvKey,
				// context)) {
				// lottableReq.setValue(ONE);
				// } else {
				// lottableReq.setValue(ZERO);
				// }
				// String icwflag = isNull(itemBio, ICW);
				// String icdflag = isNull(itemBio, ICD);
				// if ((icwflag.equalsIgnoreCase(ONE))
				// || (icdflag.equalsIgnoreCase(ONE))) {
				// cwdReq.setValue(ONE);
				// } else {
				// cwdReq.setValue(ZERO);
				// }

			} catch (Exception e) {
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		return RET_CONTINUE;

	}

	public String isNull(BioCollectionBean focus, String widgetName)
			throws EpiException {
		String result = null;
		if (result != focus.get(ZERO).get(widgetName)) {
			result = focus.get(ZERO).get(widgetName).toString();
		}
		return result;
	}
}
