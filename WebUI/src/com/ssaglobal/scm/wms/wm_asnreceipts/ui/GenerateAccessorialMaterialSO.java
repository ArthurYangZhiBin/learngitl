package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class GenerateAccessorialMaterialSO  extends com.epiphany.shr.ui.action.ActionExtensionBase{
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		
		StateInterface state = context.getState();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1
		HttpSession session = context.getState().getRequest().getSession();

		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		
		String receiptkey = (String)headerFocus.getValue("RECEIPTKEY");				
		
		int rowcount = 0;
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		// 2012-08-14
		// Modified by Will Pu
		// 8-13新增需求，判断LOTTABLE02是否有值，为空则报错， 否则该值则需要写入到对应的SO
		String lottable02 = "";
		
		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			
    		ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), "SELECT LOTTABLE02 FROM RECEIPTDETAIL WHERE RECEIPTKEY = '" + receiptkey + "'", new Object[0]);
    		
    		if (rs.next()) {
    			lottable02 = rs.getString(1);
    			
    			if (lottable02.isEmpty() || lottable02.equals("")) {
    				throw new UserException("库存地不允许为空!", new Object[0]);
    			}
    		}
    		
    		rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[0]);
		}
		
		//判断是否已生成过辅料
		try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), "SELECT COUNT(1) FROM RECEIPT WHERE RECEIPTKEY='"+receiptkey+"' AND BOMSTATUS=1", new Object[0]);
    		if(resultSet.next()){
    			rowcount = resultSet.getInt(1);
    		}
    		resultSet.close();
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[0]);
		}
    	
    	if(rowcount>0){
    		throw new UserException("WMEXP_RECEIPT_DN", new Object[0]);
    	}
		
		//判断ASN单是否已全部接收
		String theSQL = "SELECT COUNT(1) FROM RECEIPTDETAIL WHERE RECEIPTKEY='"+receiptkey+"' GROUP BY SKU, STORERKEY HAVING SUM(QTYRECEIVED)<SUM(QTYEXPECTED)";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), theSQL, new Object[0]);
    		if(resultSet.next()){
    			rowcount = resultSet.getInt(1);
    		}
    		resultSet.close();
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[0]);
		}
    	
    	if(rowcount>0){//未全部接收
    		throw new UserException("WMEXP_RECEIPT_RCN", new Object[0]);
    	}
    	
    	List list = new ArrayList();
    	//查询需要包装的物料
    	String sql = "SELECT RECEIPT.EXTERNRECEIPTKEY, RECEIPTDETAIL.SKU, RECEIPTDETAIL.LOTTABLE02, SUM(RECEIPTDETAIL.QTYRECEIVED) QTY, RECEIPTDETAIL.STORERKEY, RECEIPTDETAIL.UOM, RECEIPTDETAIL.PACKKEY, " +
    			"STRATEGY.ALLOCATESTRATEGYKEY, STRATEGY.PREALLOCATESTRATEGYKEY  " +
    			"FROM RECEIPTDETAIL, SKU, RECEIPT, STRATEGY " +
    			"WHERE RECEIPT.RECEIPTKEY=RECEIPTDETAIL.RECEIPTKEY AND RECEIPTDETAIL.STORERKEY=SKU.STORERKEY " +
    			"	AND RECEIPTDETAIL.SKU=SKU.SKU AND RECEIPTDETAIL.RECEIPTKEY='"+receiptkey+"' AND SKU.SUSR2='1' " +
    			"	AND SKU.STRATEGYKEY=STRATEGY.STRATEGYKEY AND RECEIPTDETAIL.QTYRECEIVED>0 AND NOT EXISTS(SELECT 1 FROM ORDERDETAIL WHERE EXTERNORDERKEY=RECEIPT.EXTERNRECEIPTKEY AND SKU=RECEIPTDETAIL.SKU AND STORERKEY=RECEIPTDETAIL.STORERKEY)" +
    			"GROUP BY RECEIPT.EXTERNRECEIPTKEY, RECEIPTDETAIL.SKU, RECEIPTDETAIL.LOTTABLE02, RECEIPTDETAIL.STORERKEY, RECEIPTDETAIL.UOM, RECEIPTDETAIL.PACKKEY, STRATEGY.ALLOCATESTRATEGYKEY, STRATEGY.PREALLOCATESTRATEGYKEY ";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), sql, new Object[0]);
    		while(resultSet.next()){
    			HashMap map = new HashMap();
    			map.put("EXTERNRECEIPTKEY", resultSet.getString("EXTERNRECEIPTKEY"));
    			map.put("SKU", resultSet.getString("SKU"));
    			map.put("LOTTABLE02", resultSet.getString("LOTTABLE02"));
    			map.put("QTY", resultSet.getDouble("QTY"));
    			map.put("STORERKEY", resultSet.getString("STORERKEY"));
    			map.put("UOM", resultSet.getString("UOM"));
    			map.put("PACKKEY", resultSet.getString("PACKKEY"));
    			map.put("ALLOCATESTRATEGYKEY", resultSet.getString("ALLOCATESTRATEGYKEY"));
    			map.put("PREALLOCATESTRATEGYKEY", resultSet.getString("PREALLOCATESTRATEGYKEY"));
    			list.add(map);
    			rowcount ++ ;
    		}
    		resultSet.close();
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[0]);
		}
    	
    	if(rowcount == 0){//没有需要包装的物料
    		throw new UserException("WMEXP_RECEIPT_GAMSO1", new Object[0]);
    	}
    	
    	if(list!=null){
    		for(int i=0;i<list.size();i++){
    			HashMap map = (HashMap)list.get(i);
    			String sqlHeader = "INSERT INTO ORDERS(ORDERKEY, STORERKEY, EXTERNORDERKEY, TYPE) VALUES(?, ?, ?, '8')";
    			String orderkey = new KeyGenBioWrapper().getKey("ORDER");
    	    	try 
    	    	{
    	    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    	    		appAccess.executeUpdate(facilityName.toUpperCase(), sqlHeader, new Object[]{orderkey, ""+map.get("STORERKEY"), ""+map.get("EXTERNRECEIPTKEY")});
    	    	} 
    	    	catch (SQLException e) 
    	    	{			
    				e.printStackTrace();
    				throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[1]);
    			}
    	    	
    	    	String sqlDetail = "INSERT INTO ORDERDETAIL(ORDERKEY, ORDERLINENUMBER, EXTERNORDERKEY, EXTERNLINENO, SKU, " +
    	    					"STORERKEY, ORIGINALQTY, OPENQTY, UOM, PACKKEY, " +
    	    					"ALLOCATESTRATEGYKEY, PREALLOCATESTRATEGYKEY, ALLOCATESTRATEGYTYPE, LOTTABLE02) " +
    	    					"VALUES(?, ?, ?, ?, ?,    ?, ?, ?, ?, ?,    ?, ?, '1', ?)";
    	    	
    	    	try 
    	    	{
    	    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    	    		appAccess.executeUpdate(facilityName.toUpperCase(), sqlDetail, 
    	    				new Object[]{orderkey, "00001", "" + map.get("EXTERNRECEIPTKEY"), "00001", "" + map.get("SKU"), 
    	    						map.get("STORERKEY") + "", (Double) map.get("QTY"), (Double) map.get("QTY"), 
    	    						map.get("UOM") + "", map.get("PACKKEY") + "", map.get("ALLOCATESTRATEGYKEY") + "", 
    	    						map.get("PREALLOCATESTRATEGYKEY") + "", map.get("LOTTABLE02").toString()});
    	    	} 
    	    	catch (SQLException e) 
    	    	{			
    				e.printStackTrace();
    				throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[1]);
    			}
    		}
    		
    		//修改辅料生成标记			kaiw	07/03/2012
    		String sqlStr = "UPDATE RECEIPT SET BOMSTATUS=1 WHERE RECEIPTKEY=? ";
			try {
				SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
				appAccess.executeUpdate(facilityName.toUpperCase(), sqlStr, new Object[]{receiptkey});
			} catch (SQLException e) {			
				e.printStackTrace();
				throw new UserException("WMEXP_RECEIPT_GAMSO", new Object[1]);
			}
    	}
    	return RET_CONTINUE;
	}

}
