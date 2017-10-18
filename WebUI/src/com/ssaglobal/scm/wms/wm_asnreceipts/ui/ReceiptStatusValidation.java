package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class ReceiptStatusValidation extends ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		String receiptkey = "";
		
		StateInterface state = context.getState();
		
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		 
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		 
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		
		DataBean headerFocus = headerForm.getFocus();

		RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);

		BioBean headerBioBean = null;
		
		try {
			if (headerFocus.isTempBio()) {
				headerBioBean = uowb.getNewBio((QBEBioBean) headerFocus);				
				
				DataBean detailFocus = detailForm.getFocus();
				
				if (detailFocus.isTempBio()) {
					receiptkey = (String) headerForm.getFormWidgetByName("RECEIPTKEY").getValue();
				}
			} else {
				headerBioBean = (BioBean) headerFocus;
				
				receiptkey = headerBioBean.get("RECEIPTKEY").toString();
			}
		} catch (EpiException e) {
			// Do nothing
		}

		String asnstatus = "";
		
		HttpSession session = context.getState().getRequest().getSession();

		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();

		String query = "SELECT STATUS FROM RECEIPT WHERE RECEIPTKEY = '" + receiptkey + "'";

		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

			ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);

			if (rs.next()) {
				asnstatus = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(" ", new Object[]{});
		}
		
		if (asnstatus != null && asnstatus.equals("11")) {
			throw new UserException("已上传SAP的ASN单据不允许修改操作!", new Object[]{});
		}
		
		return RET_CONTINUE;
	}
}