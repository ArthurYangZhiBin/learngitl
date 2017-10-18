package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

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

public class PostingRevive extends ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		String orderkey = "";
		
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
					orderkey = (String) headerForm.getFormWidgetByName("ORDERKEY").getValue();
				}
			} else {
				try {
					headerBioBean = (BioBean) headerFocus;
					
					orderkey = headerBioBean.get("ORDERKEY").toString();
				} catch (ClassCastException e) {
					throw new UserException("请打开需要恢复过账的单据进行该操作!", new Object[]{});
				}
			}
		} catch (EpiException e) {
			throw new UserException("恢复过账异常-ManualPosting!", new Object[]{});
		}

		HttpSession session = context.getState().getRequest().getSession();

		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();

		String query = "UPDATE ORDERS SET ORDERGROUP = ' ' WHERE ORDERKEY = '" + orderkey + "' AND ORDERGROUP = '888888'";

		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

			appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("恢复过账异常-ManualPosting!", new Object[]{});
		}
		
		return RET_CONTINUE;
	}
}