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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.DisableLottables11_12;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNEntryPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	private static String KEYTEMPLATE = "00000";

	/**
	 * Called in response to the pre-render event on a form in a modal window.
	 * Write code to customize the properties of a form. This code is
	 * re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderForm(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event
			if ("wm_entry_receipt_view".equals(form.getName())) {
				receiptFormPreRender(context, form);
			} else if ("wm_entry_receiptdetail_view".equals(form.getName())) {
				receiptLineFormPreRender(context, form);
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Receipt form pre render.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 */
	private void receiptFormPreRender(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) {
		if (form.getFocus().isTempBio()) {
			List<String> ignoreList = new ArrayList<String>();
			ignoreList.add("STORERKEY");
			ignoreList.add("STATUS");

			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"STORERKEY"))) {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "true");

			} else {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "false");
			}
		}

	}

	/**
	 * Receipt line form pre render.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 * @throws EpiException 
	 */
	private void receiptLineFormPreRender(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {
		if (form.getFocus().isTempBio()) {
			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"RECEIPTLINENUMBER"))) {
				generateLineNumber(context, form);
			}

			RuntimeFormInterface headerForm = ASNEntryUtil
					.getASNHeaderForm(context);
			if (headerForm != null && ASNEntryUtil.savingDetailOnly(context)) {
				form.getFocus().setValue(
						"RECEIPTKEY",
						BioAttributeUtil.getString(headerForm.getFocus(),
								"RECEIPTKEY"));

				if (StringUtils.isEmpty(BioAttributeUtil.getString(form
						.getFocus(), "STORERKEY"))) {
					form.getFocus().setValue(
							"STORERKEY",
							BioAttributeUtil.getString(headerForm.getFocus(),
									"STORERKEY"));
				}
			}

			List<String> ignoreList = new ArrayList<String>();
			ignoreList.add("STORERKEY");
			ignoreList.add("SKU");
			ignoreList.add("RECEIPTLINENUMBER");
			if (StringUtils.isEmpty(BioAttributeUtil.getString(form.getFocus(),
					"STORERKEY"))
					|| StringUtils.isEmpty(BioAttributeUtil.getString(form
							.getFocus(), "SKU"))) {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "true");

			} else {
				FormUtil.massWidgetPropertyControl(form, ignoreList,
						RuntimeFormWidgetInterface.PROP_READONLY, "false");
			}
			
			//Lottables
			DisableLottables11_12 disableLottables11_12 = new DisableLottables11_12();
			disableLottables11_12.disableLottable11_12(context.getState());
		}
	}

	private void generateLineNumber(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiDataException {
		String zeroPadding = null;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
		Query bioQuery = new Query("wm_system_settings", sQueryString, null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
		try {
			zeroPadding = selCollection.elementAt(0).get("NSQLVALUE")
					.toString();
		} catch (EpiDataException e1) {
			e1.printStackTrace();
		}
		DecimalFormat template = null;
		if (zeroPadding.equalsIgnoreCase("0")) {
			template = new DecimalFormat("0");
		} else {
			template = new DecimalFormat(KEYTEMPLATE);
		}

		long size = 0;

		// adding new line to existing asn
		RuntimeFormInterface headerForm = ASNEntryUtil
				.getASNHeaderForm(context);
		if (headerForm != null) {
			DataBean receiptFocus = headerForm.getFocus();
			BioCollectionBean rs = (BioCollectionBean) receiptFocus
					.getValue("RECEIPTDETAILS");
			if (rs == null || rs.size() == 0) {
				size = 0;
			} else {
				String max = (String) rs.max("RECEIPTLINENUMBER");
				size = Long.parseLong(max);
			}
		}

		String key = template.format(++size);

		form.getFocus().setValue("RECEIPTLINENUMBER", key);
	}

}
