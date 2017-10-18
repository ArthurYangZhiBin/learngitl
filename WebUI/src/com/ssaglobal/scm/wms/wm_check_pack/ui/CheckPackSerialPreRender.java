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

package com.ssaglobal.scm.wms.wm_check_pack.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackSerialPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(CheckPackSerialPreRender.class);

	/**
	 * Called in response to the pre-render event on a list form in a modal
	 * dialog. Write code to customize the properties of a list form
	 * dynamically, change the bio collection being displayed in the form or
	 * filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderListForm(ModalUIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {

		try {
			// look at the sku and see what fields should be enabled
			StateInterface state = context.getState();
			DataBean focus = form.getFocus();
			if (focus != null && focus.isBioCollection()) {
				log.debug("CheckPackSerialPreRender_preRenderListForm",
						"Focus is not null and is a list",
						SuggestedCategory.APP_EXTENSION);
				BioCollectionBean listFocus = (BioCollectionBean) focus;
				if (listFocus.size() > 0) {
					log.debug("CheckPackSerialPreRender_preRenderListForm",
							"Getting first element in list",
							SuggestedCategory.APP_EXTENSION);
					BioBean firstSerial = listFocus.get("" + 0);
					String owner = BioAttributeUtil.getString(
							(DataBean) firstSerial.getValue("LOTXIDHEADER"),
							"STORERKEY");
					String item = BioAttributeUtil
							.getString(firstSerial, "SKU");

					UnitOfWorkBean uow = state.getTempUnitOfWork();
					Query query = new Query("sku", "sku.STORERKEY = '" + owner
							+ "' and sku.SKU = '" + item + "'", null);
					log.debug("CheckPackSerialPreRender_preRenderListForm",
							query.getQueryExpression(),
							SuggestedCategory.APP_EXTENSION);
					BioCollectionBean rs = uow.getBioCollectionBean(query);
					for (int i = 0; i < rs.size(); i++) {
						BioBean itemBio = rs.get("" + i);
						for (int j = 1; j <= 5; j++) {
							String outboudValue = BioAttributeUtil.getString(
									itemBio, "OCDLABEL" + j);
							if (StringUtils.isBlank(outboudValue)) {
								form
										.getFormWidgetByName("OOTHER" + j)
										.setBooleanProperty(
												RuntimeFormWidgetInterface.PROP_READONLY,
												true);
							} else {
								form
										.getFormWidgetByName("OOTHER" + j)
										.setBooleanProperty(
												RuntimeFormWidgetInterface.PROP_READONLY,
												false);
							}
						}

					}
				}
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
