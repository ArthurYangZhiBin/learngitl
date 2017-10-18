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

package com.ssaglobal.scm.wms.wm_releasecyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
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

public class CycleCountRptAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(CycleCountRptAction.class);

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
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		HashMap<String, Object> paramsAndValues = new HashMap<String, Object>();
		paramsAndValues.put("&p_ReportStart", "0");
		paramsAndValues.put("&p_ReportEnd", "ZZZZZZZZZZZZZZZ");
		// select all owners
		paramsAndValues.put("&p_OwnerFrom", allOwners(uow));
		paramsAndValues.put("&p_ItemStart", "0");
		paramsAndValues.put("&p_ItemEnd", "ZZZZZZZZZZZZZZZ");
		paramsAndValues.put("&p_ZoneStart", "0");
		paramsAndValues.put("&p_ZoneEnd", "ZZZZZZZZZZZZZZZ");

		String report_url = ReportUtil.retrieveReportURL(state, "CRPT83",
				paramsAndValues);

		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put("REPORTURL", report_url);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private ArrayList<String> allOwners(UnitOfWorkBean uow) {
		ArrayList<String> allOwners = new ArrayList<String>();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_storer",
				"wm_storer.TYPE = '1'", null));
		try {
			for (int i = 0; i < rs.size(); i++) {
				allOwners.add(BioAttributeUtil.getString(rs.get("" + i),
						"STORERKEY"));
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("CycleCountRptAction_allOwners", StringUtils
					.getStackTraceAsString(e), SuggestedCategory.APP_EXTENSION);
		}
		return allOwners;
	}

}
