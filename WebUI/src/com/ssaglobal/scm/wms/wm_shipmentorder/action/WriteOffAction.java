package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class WriteOffAction extends ActionExtensionBase {
	private static String SHELL_FORM = "wm_list_shell_facilitytransfer";
	private static String TABGROUP_SLOT = "tbgrp_slot";
	private static String SLOT_1_1 = "list_slot_1";
	private static String SLOT_1_2 = "slot1";
	private static String TAB_0_1 = "tab 0";
	private static String TAB_0_2 = "Tab0";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		// 获取页面当前操作的出货订单单号
		String orderkey = "";
		
		StateInterface state = context.getState();
		
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		
		RuntimeFormInterface headerForm = null;
		
		if (!shell.getName().equals(SHELL_FORM)) {
			headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SLOT_1_1), null).getSubSlot(TABGROUP_SLOT), TAB_0_1);
		} else {
			headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SLOT_1_2), null).getSubSlot(TABGROUP_SLOT), TAB_0_2);
		}
		
		DataBean focus = headerForm.getFocus();
		
		orderkey = focus.getValue("ORDERKEY").toString();
		
		// 更新出多订单状态, 置为98-外部取消
		HttpSession session = context.getState().getRequest().getSession();
		
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String query = "UPDATE ORDERS SET STATUS = '98' WHERE ORDERKEY = '" + orderkey + "'";
		
    	try {
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		
    		appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
    		
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);
    		
    		resultSet.close();
    	} catch (SQLException e) {			
			e.printStackTrace();
			throw new UserException("", new Object[0]);
		}
		
		return RET_CONTINUE;
	}
}