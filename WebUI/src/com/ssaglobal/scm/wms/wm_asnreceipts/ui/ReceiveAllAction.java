package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.genericdbms.DBHelper;
import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.synnex.utils.SimpleJDBCUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class ReceiveAllAction extends ListSelector {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiveAllAction.class);

	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT", "\n\nExecuting Action1 \n\n", 100L);
		HttpSession session = context.getState().getRequest().getSession();
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = context.getSourceForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT", "headerForm" + shellForm.getName(), 100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		_log.debug("LOG_SYSTEM_OUT", "header Focuss :" + headerFocus, 100L);
		_log.debug("LOG_SYSTEM_OUT", "Iam in Modal window OK button", 100L);
		BioBean bio = (BioBean) headerFocus;
		String ReceiptKey = bio.getValue("RECEIPTKEY").toString();
		checkAsn(state, ReceiptKey);
		String ReceiptType = bio.getValue("TYPE").toString();
		_log.debug("LOG_SYSTEM_OUT", "\n\nRECEIPTKEY from the form view:" + ReceiptKey + "\n\n", 100L);
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array();
		parms.add(new TextData(ReceiptKey));
		parms.add(new TextData(ReceiptType));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPRECEIVEALL");
		if (session.getAttribute("hasError") != null) {
			String hasError = null;
			hasError = session.getAttribute("hasError").toString();
			if ((hasError != null) && (hasError.equals("true"))) {
				session.removeAttribute("hasError");
				result.setFocus(headerFocus);
				return 0;
			}
		}
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			e.getMessage();
			session.setAttribute("hasError", "true");
			UserException UsrExcp = new UserException(e.getMessage(), new Object[0]);
			throw UsrExcp;
		}
		result.setFocus(headerFocus);
		return 0;
	}

	public void checkAsn(StateInterface state, String receiptkey) throws UserException {
		System.out.println("receipt All:qa");
		Connection qqConnection = null;
		PreparedStatement qqPrepStmt = null;
		ResultSet qqResultSet = null;
		DataSource dataSource = SimpleJDBCUtil.getDataSource(state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString());
		try {
			qqConnection = dataSource.getConnection();
			qqPrepStmt = qqConnection.prepareStatement(" SELECT c_isqa FROM receipt WHERE receiptkey = ? and status < 9 and type='FU' ");
			DBHelper.setValue(qqPrepStmt, 1, receiptkey);
			qqResultSet = qqPrepStmt.executeQuery();
			String qa = "0";
			if (qqResultSet.next()) {
				qa = qqResultSet.getString(1);
				if ("0".equals(qa)) {
					throw new UserException("asn单："+receiptkey+"QA没有放行", new Object[] {}); 
				}
			}
		} catch (Exception e) {
			 throw new UserException("asn单："+receiptkey+"QA没有放行", new Object[] {}); 
		} finally {
			SimpleJDBCUtil.release(qqConnection, qqPrepStmt, qqResultSet);
		}

	}

}