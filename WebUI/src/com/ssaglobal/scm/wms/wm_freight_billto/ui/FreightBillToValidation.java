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
package com.ssaglobal.scm.wms.wm_freight_billto.ui;
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;

public class FreightBillToValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FreightBillToValidation.class);

	@Override
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {
		String shellFormName = "wm_list_shell_freight_billto";
		String generalFormTab = "wm_freight_billto_general";
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		//General
		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add("tab 0");
		RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar, shellFormName, generalFormTab, tabIdentifiers, state);
		GeneralTab generalValidation = new GeneralTab(generalForm, state);
		generalValidation.run();
		DataBean storerFocus = generalForm.getFocus();
		storerFocus.setValue("TYPE", "13");
		String freightBillTo = storerFocus.getValue("STORERKEY").toString();
		storerFocus.setValue("STORERKEY", freightBillTo.toUpperCase());

		return RET_CONTINUE;

	}
	class GeneralTab extends Tab {
		String ownerValue;

		public GeneralTab(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
			_log.debug("LOG_SYSTEM_OUT", "!@# Start of General Tab Validation " + form.getName(), 100L);
			ownerValue = focus.getValue("STORERKEY").toString();
		}

		void run() throws DPException, UserException {
			if (isInsert) {
				ownerDuplication();
			}
			alphaNumericValidation("SCAC_CODE");

		}

		private void alphaNumericValidation(String attributeName) throws UserException {
			Object attributeValue = focus.getValue(attributeName);
			if (isNull(attributeValue)) {
				return;
			}
			if (!(attributeValue.toString().matches("^[\\d\\w]*$"))) {
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_ALPHANUM", parameters);
			}

		}

		void ownerDuplication() throws DPException, UserException {

			String query = "SELECT * FROM STORER WHERE (STORERKEY = '" + ownerValue + "' AND TYPE='13')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_DUPLICATE", parameters);
			}
		}

	}
}
