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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackSaveValidation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(CheckPackSaveValidation.class);

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
			StateInterface state = ctx.getState();

			RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);

			if (modalBodyForm.isListForm()) {
				RuntimeListFormInterface serialList = (RuntimeListFormInterface) modalBodyForm;
				DataBean serialListBean = serialList.getFocus();
				if (serialListBean.isBioCollection()) {
					BioCollectionBean serialCollection = (BioCollectionBean) serialListBean;
					for (int i = 0; i < serialCollection.size(); i++) {
						BioBean cwcd = serialCollection.get("" + i);
						Object snlObject = cwcd.getValue("SERIALNUMBERLONG");
						if (snlObject != null
								&& !StringUtils.isEmpty(snlObject.toString())) {

							String serialNumberLong = (String) snlObject;

							ArrayList<?> serials = null;

							try {
								String pickDetailKey = BioAttributeUtil
										.getString(cwcd, "PICKDETAILKEY");

								BioCollectionBean rs = state
										.getTempUnitOfWork()
										.getBioCollectionBean(
												new Query("wm_pickdetail",
														"wm_pickdetail.PICKDETAILKEY = '"
																+ pickDetailKey
																+ "'", null));
								if (rs.size() == 0) {
									continue;
								}
								BioBean pickDetailFocus = rs.get("" + 0);

								String sku = (String) pickDetailFocus
										.getValue("SKU");
								String owner = (String) pickDetailFocus
										.getValue("STORERKEY");

								serials = getSerials(owner, sku,
										serialNumberLong);
								
								SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(owner, sku);
								if ("1".equals(skuConf.getSNum_EndToEnd())) {

									String lot = BioAttributeUtil.getString(
											pickDetailFocus, "LOT");
									String loc = BioAttributeUtil.getString(
											pickDetailFocus, "LOC");
									String id = BioAttributeUtil.getString(
											pickDetailFocus, "ID");

									SerialInventoryDAO siDAO = new SerialInventoryDAO();
									if (!siDAO.isAvailable(state, owner, sku,
											lot, loc, id,
											((SerialNoDTO) serials.get(0))
													.getSerialnumber())) {
										throw new UserException(
												"WMEXP_SN_NOT_EXISTS",
												new String[] { serialNumberLong });
									}
								}

							} catch (RuntimeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.error(
										"LOG_ERROR_EXTENSION_CatchWeightValidateSerialNumber_execute",
										StringUtils.getStackTraceAsString(e),
										SuggestedCategory.NONE);
								throw new UserException("WMEXP_SN_FORMAT",
										new String[] { serialNumberLong });
							}

							if (serials == null || serials.size() == 0) {
								throw new UserException("WMEXP_SN_FORMAT",
										new String[] { serialNumberLong });
							}
							// Set IOTHER1 with parsed Serial
							cwcd.setValue("OOTHER1", ((SerialNoDTO) serials
									.get(0)).getSerialnumber());

						}

					}
				}
			}
			
			if("SAVEANDCLOSE".equals(ctx.getActionObject().getName())){
				state.closeModal(false);
			}
			
		} catch (RuntimeException e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		

		return RET_CONTINUE;
	}

	public ArrayList<?> getSerials(String storerkey, String sku, String serial) {

		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);

		SerialNumberObj serialNumber = new SerialNumberObj(skuConf);

		serialNumber.setStorerkey(storerkey);

		serialNumber.setSku(sku);

		ArrayList<?> list = serialNumber.getValidSerialNos(serial);

		return list;
	}
}
