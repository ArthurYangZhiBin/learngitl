package com.synnex.outbound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.agileitp.forte.genericdbms.DBHelper;
import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.synnex.utils.SimpleJDBCUtil;

public class UpdateQtyExpected extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UpdateQtyExpected.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		Connection qqConnection = null;
		PreparedStatement preStatement = null;
		PreparedStatement preStatement1 = null;
		ResultSet resultSet = null;
		StateInterface state = context.getState();
		String wmwhseID = state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
		DataSource dataSource = SimpleJDBCUtil.getDataSource(state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString());
		String userid = state.getUser().getName();
		String sql = "select s.qty,s.qtyallocated,s.qtyexpected,s.serialkey,s.qtypicked from SKUXLOC s  where s.QTYEXPECTED != (QTYALLOCATED + QTYPICKED - QTY) *(SIGN(QTYALLOCATED + QTYPICKED - QTY) + 1) / 2";
		Double qty = 0.0;
		Double qtyallocated = 0.0;
		Double qtyexpected = 0.0;
		Double qtypicked = 0.0;
		String serialkey = "";
		try {
			qqConnection = dataSource.getConnection();
			preStatement = qqConnection.prepareStatement(sql);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				qty = resultSet.getDouble(1);
				qtyallocated = resultSet.getDouble(2);
				qtyexpected =0.0;
				serialkey = resultSet.getString(4);
				qtypicked = resultSet.getDouble(5);
				String sql2 = "update SKUXLOC set qtyexpected=? where serialkey=?";
				if (qtypicked + qtyallocated - qty > 0) {
					// 超分配修改为0
					qtyexpected = qtypicked + qtyallocated - qty;
				}
				// 更新超分配量
				preStatement1 = qqConnection.prepareStatement(sql2);
				DBHelper.setValue(preStatement1, 1, qtyexpected);
				DBHelper.setValue(preStatement1, 2, serialkey);
				preStatement1.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBResourceException(e);
		} finally {
			SimpleJDBCUtil.release(null, preStatement1, null);
			SimpleJDBCUtil.release(qqConnection, preStatement, resultSet);
		}
		return RET_CONTINUE;
	}
}
