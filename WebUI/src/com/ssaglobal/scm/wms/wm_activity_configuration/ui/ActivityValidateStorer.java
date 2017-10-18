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

package com.ssaglobal.scm.wms.wm_activity_configuration.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
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

public class ActivityValidateStorer extends com.epiphany.shr.ui.action.ActionExtensionBase {

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

		String widgetName = context.getSourceWidget().getName();
		DataBean focus = context.getState().getFocus();
		String storerValue = BioAttributeUtil.getString(focus, context
				.getSourceWidget()
				.getAttribute());
		String errorMessage = null;
		String descriptionWidget = null;
		if (!StringUtils.isEmpty(storerValue)) {
			storerValue = QueryHelper.escape(storerValue);
			storerValue = storerValue.toUpperCase();
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			String query = "wm_storer.STORERKEY = '" + storerValue + "'";
			if ("OWNER".equals(widgetName)) {
				query += " and wm_storer.TYPE = '1'";
				errorMessage = "WMEXP_INVALID_STORER";
				descriptionWidget = "OWNERNAME";
			} else if ("CUSTOMER".equals(widgetName)) {
				query += " and wm_storer.TYPE = '2'";
				errorMessage = "WMEXP_CONSIGNEE_VALIDATION";
				descriptionWidget = "CUSTOMERNAME";
			} else {
				query += " and wm_storer.TYPE = '12'";
				errorMessage = "WMEXP_INVALID_SF";
				descriptionWidget = "SUPPLIERNAME";
			}
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_storer", query, ""));
			if (rs.size() != 1) {
				throw new UserException(errorMessage, new String[] { storerValue });
			}
			for (int i = 0; i < rs.size(); i++) {
				focus.setValue(descriptionWidget, BioAttributeUtil.getString(rs.get("" + i),
						"COMPANY"));
			}
		} else {
			if ("OWNER".equals(widgetName)) {
				descriptionWidget = "OWNERNAME";
			} else if ("CUSTOMER".equals(widgetName)) {
				descriptionWidget = "CUSTOMERNAME";
			} else {
				descriptionWidget = "SUPPLIERNAME";
			}
			focus.setValue(descriptionWidget, "");
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}
}
