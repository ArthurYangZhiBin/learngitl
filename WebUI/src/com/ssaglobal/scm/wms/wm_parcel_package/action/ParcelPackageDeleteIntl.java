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

package com.ssaglobal.scm.wms.wm_parcel_package.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ParcelPackageDeleteIntl extends
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

		StateInterface state = context.getState();
		RuntimeFormInterface packageList = FormUtil.findForm(state
				.getCurrentRuntimeForm(), "wms_list_shell",
				"wm_parcel_package_list_view", state);
		Set<String> idsToDelete = new HashSet<String>();
		if (packageList != null && packageList.isListForm()) {
			ArrayList<BioBean> selectedPackages = ((RuntimeListFormInterface) packageList)
					.getSelectedItems();
			if (selectedPackages != null) {
				for (int i = 0; i < selectedPackages.size(); i++) {
					BioBean pPackage = selectedPackages.get(i);
					int caseStatus = BioAttributeUtil.getInt(pPackage,
							"CASESTATUS");
					if (caseStatus == 3) {
						idsToDelete.add(BioAttributeUtil.getString(pPackage,
								"CASEID"));
					}
				}
			}
		}
		if (idsToDelete.size() == 0) {
			throw new UserException("WMEXP_SPS_SELECT_HELD", new Object[] {});
		}

		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array();

		parms.add(new TextData(StringUtils.join(idsToDelete.iterator(), ",")));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("SPSDeleteInternational");
		EXEDataObject spResult = null;
		try {
			spResult = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
		DataValue errorMsg = spResult.getAttribValue(new TextData("Error"));
		if(errorMsg != null && !StringUtils.isBlank(errorMsg.getAsString())){
			throw new UserException("WMEXP_SPS_NO_DATA_HELD", new Object[]{});
		}
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
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
