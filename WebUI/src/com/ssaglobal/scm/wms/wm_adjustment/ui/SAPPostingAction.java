package com.ssaglobal.scm.wms.wm_adjustment.ui;

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

public class SAPPostingAction extends ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		String adjustmentkey = "";
		
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
					adjustmentkey = (String) headerForm.getFormWidgetByName("ADJUSTMENTKEY").getValue();
				}
			} else {
				headerBioBean = (BioBean) headerFocus;
				
				adjustmentkey = headerBioBean.get("ADJUSTMENTKEY").toString();
			}
		} catch (EpiException e) {
			throw new UserException("SAP过账异常!", new Object[]{});
		}

		HttpSession session = context.getState().getRequest().getSession();

		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();

		String query = "SELECT ADJUSTSAP FROM ADJUSTMENT WHERE ADJUSTMENTKEY = '" + adjustmentkey + "'";

		String isPosting = "0";

		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

			ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);

			if (rs.next()) {
				isPosting = rs.getString(1);
			} else {
				throw new UserException("请先保存调整单", new Object[0]);
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("SAP过账异常!", new Object[0]);
		}
		
		isPosting = (isPosting == null) ? "0" : isPosting;
		
		if (isPosting.equals("1")) {
			throw new UserException("调整单等待上传SAP, 请勿操作", new Object[0]);
		} else {
			query = "UPDATE ADJUSTMENT SET ADJUSTSAP = '1' WHERE ADJUSTMENTKEY = '" + adjustmentkey + "'";

			try {
				SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

				appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException("", new Object[0]);
			}
		}

		return RET_CONTINUE;
	}
}