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

package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
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

public class ReturnPartyQueryAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ReturnPartyQueryAction.class);

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

		// determine parameters
		StateInterface state = context.getState();
		DataBean focus = state.getCurrentRuntimeForm().getFocus();

		String storerType = getStorerType(focus);
		Query query = new Query("wm_storer", "wm_storer.TYPE = '" + storerType
				+ "'", null);
		log.debug("ReturnPartyQueryAction_execute",
				"Storer query " + query.getQueryExpression(),
				SuggestedCategory.APP_EXTENSION);
		BioCollectionBean rs = state.getDefaultUnitOfWork()
				.getBioCollectionBean(query);
		result.setFocus(rs);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	public static String getStorerType(DataBean focus) {
		String sourceBio = focus.getDataType();
		if ("receipt".equals(sourceBio)) {
			log.debug("ReturnPartyQueryAction_execute", "Receipt",
					SuggestedCategory.APP_EXTENSION);
			// ASN Receipt
			String type = BioAttributeUtil.getString(focus, "TYPE");
			log.debug("ReturnPartyQueryAction_execute", "Type = " + type,
					SuggestedCategory.APP_EXTENSION);
			if ("2".equals(type)) { //Customer Returns
				// returns query
				// 2
				log.debug("ReturnPartyQueryAction_getStorerType",
						"Storer type is 2", SuggestedCategory.APP_EXTENSION);
				return "2";
			} else {
				// normal query
				// 12
				log.debug("ReturnPartyQueryAction_getStorerType",
						"Storer type is 12", SuggestedCategory.APP_EXTENSION);
				return "12";
			}
		} else {
			// SO
			String type = BioAttributeUtil.getString(focus, "TYPE");
			log.debug("ReturnPartyQueryAction_getStorerType", "Type = " + type,
					SuggestedCategory.APP_EXTENSION);
			if ("4".equals(type)) { //Supplier RMA
				//returns order
				log.debug("ReturnPartyQueryAction_getStorerType",
						"Storer type is 12", SuggestedCategory.APP_EXTENSION);
				return "12";
			} else {
				//normal order
				log.debug("ReturnPartyQueryAction_getStorerType",
						"Storer type is 2", SuggestedCategory.APP_EXTENSION);
				return "2";
			}
		}
	}
}
