package com.synnex.inbound;

// Import 3rd party packages and classes
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.synnex.utils.SimpleJDBCUtil;

public class ValidationASNQA extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidationASNQA.class);

	@Override
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		String receiptkeys = "";
		String wmwhseID = state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
		DataSource dataSource = SimpleJDBCUtil.getDataSource(state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString());
		String userid = state.getUser().getName();
		
		if (headerFocus instanceof BioCollection) {
			// List Form
			ArrayList<BioBean> selectedReceipts = ((RuntimeListFormInterface) (headerForm)).getSelectedItems();
			if (selectedReceipts != null && selectedReceipts.size() > 0) {
				for (BioBean r : selectedReceipts) {
					receiptkeys = receiptkeys + BioAttributeUtil.getString(r, "RECEIPTKEY") + ",";
				}
				receiptkeys = receiptkeys.substring(0, receiptkeys.length() - 1);
			}
			else
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[]{});
			}
			
			Connection qqConnection = null;
	    	CallableStatement cs = null;
	    	try {
	    		qqConnection = dataSource.getConnection();
	    		cs = qqConnection.prepareCall("{ call "+wmwhseID+".sp_qacheck_record(?,?,?,?,?,?) }");
	    		int i = 0;
    			cs.setString(++i, receiptkeys);
    			cs.setString(++i, wmwhseID);
    			cs.setString(++i, userid);
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.execute();
	    	}catch (Exception e) {
				// TODO: handle exception
	    		e.printStackTrace();
    			throw new DBResourceException(e);
			} finally {
				SimpleJDBCUtil.release(qqConnection, null, null);
			}
			
			context.setNavigation("clickEvent3152");
			uowb.clearState();
			((RuntimeListFormInterface) (headerForm)).setSelectedItems(null);
		} else {
			BioBean headerBio = (BioBean) headerFocus;
			receiptkeys = headerBio.get("RECEIPTKEY").toString();
			Connection qqConnection = null;
	    	CallableStatement cs = null;
	    	try {
	    		qqConnection = dataSource.getConnection();
	    		cs = qqConnection.prepareCall("{ call "+wmwhseID+".sp_qacheck_record(?,?,?,?,?,?) }");
	    		int i = 0;
    			cs.setString(++i, receiptkeys);
    			cs.setString(++i, wmwhseID);
    			cs.setString(++i, userid); 
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.registerOutParameter(++i,java.sql.Types.VARCHAR);
    			cs.execute();
	    	}catch (Exception e) {
				// TODO: handle exception
	    		e.printStackTrace();
    			throw new DBResourceException(e);
			} finally {
				SimpleJDBCUtil.release(qqConnection, null, null);
			}
			context.setNavigation("clickEvent3152"); //menuClickEvent320
		}
		
		return RET_CONTINUE; 
	}

}