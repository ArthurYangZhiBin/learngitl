package com.synnex.bausch.transfer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.JFreeChartReport.Datasource.WebuiJFreeChartDatasource;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.wm_internal_transfer.action.ConfirmationForAction;

/*
 * Author:Kai.wang
 */
public class GenerateFrozenTransfer extends com.epiphany.shr.ui.action.ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmationForAction.class);
	protected static String FROM_STORER = "FROMSTORERKEY";
	protected static String FROM_ITEM = "FROMSKU";
	protected static String FROM_LOC = "FROMLOC";
	protected static String FROM_LOT = "FROMLOT";
	protected static String FROM_ID = "FROMID";
	protected static String TO_STORER = "TOSTORERKEY";
	protected static String TO_QTY = "TOQTY";
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	protected static String TRANSFER_LINE_NUMBER = "TRANSFERLINENUMBER";
	protected static String STORERKEY = "STORERKEY";
	protected static String ITEM = "SKU";
	protected static String LOT = "LOT";
	protected static String LOC = "LOC";
	protected static String ID = "ID";
	protected static String SERIALNUMBER = "SERIALNUMBER";
	protected static String TDS_LINK = "TRANSFERDETAILSERIAL";
	protected static String QTY = "QTY";
	protected static String DATA2 = "DATA2";
	protected static String DATA3 = "DATA3";
	protected static String DATA4 = "DATA4";
	protected static String DATA5 = "DATA5";
	protected static String NETWEIGHT = "NETWEIGHT";
	protected static String SERIAL_BIO = "wm_serialinventory";  				// physical name on 12/03/2008: serialinventory
	protected static String ITRN_TRANS_SERIAL = "wm_internal_transfer_serial";  // physical name on 12/03/2008: transferdetailserial
	protected static String TABLE = "wm_internal_transfer_serial_temp"; 		// physical name on 12/03/2008: tdstemp
	protected static String LLI_BIO = "wm_lotxlocxid"; 							// physical name on 12/03/2008: lotxlocxid; Prior to 12/03 LLI_BIO=="wm_inventorybalanceslotxlocxlpn"
	
	protected String fromStorer = "";
	protected String fromItem = "";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing GenerateFrozenTransfer- Non Modal",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);

		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting GenerateFrozenTransfer- Non Modal",100L);
		return RET_CONTINUE;
	}

	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing ConfirmationForAction- Modal ",100L);
		StateInterface state = ctx.getState();
		try{
			//state.closeModal(true);
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if(!headerForm.isListForm()){
				throw new UserException("请在列表界面使用该功能", new Object[] {});
			}else{
				RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);
				DataBean listFocus = listForm.getFocus();	
				if(listFocus instanceof BioCollection){  
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Finalizing Record",100L);
					//throw new UserException("成功生成内部转移单", new Object[] {});
					genFrozenTransfer(ctx);
				}
			}
		}catch (RuntimeException e1){
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Runtimg Exception" ,100L);
			e1.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting ConfirmationForAction()" ,100L);
		return RET_CONTINUE;
	}

	public void genFrozenTransfer(ActionContext context) throws UserException{
		String msg = "";
		String userid = context.getState().getUser().getName();
		Connection conn = null;
		CallableStatement proc = null;
		try {
			conn = new WebuiJFreeChartDatasource().getConnection(context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString().toUpperCase());
			conn.setAutoCommit(false);
			proc = conn.prepareCall("{ call SP_GENERATE_FROZEN_TRANSFER(?, ?)  }");
			proc.setString(1, userid);
			proc.registerOutParameter(2, Types.VARCHAR);
			proc.execute();
			msg = proc.getString(2);
			conn.commit();
		}catch (SQLException e) {
			e.printStackTrace();
			if(conn !=null ){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new UserException(e.getMessage(), new Object[] {});
		}finally{
			try {
				if(proc != null){
					proc.close();
				}
				if(conn !=null ){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new UserException(e.getMessage(), new Object[] {});
			}
		}
		if(msg!=null && !msg.startsWith("成功")){
			throw new UserException(msg, new Object[] {});
		}
		
	}

}
