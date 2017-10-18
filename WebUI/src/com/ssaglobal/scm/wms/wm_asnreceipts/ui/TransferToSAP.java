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

public class TransferToSAP extends com.epiphany.shr.ui.action.ActionExtensionBase {
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
		
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		//根据STORERKEY和SKU汇总,获取需要更改预期量的记录
    	List list = this.getStorerkeyAndSku(facilityName, receiptkey);	
    	if(list!=null){
    		for(int i=0;i<list.size();i++){
    			HashMap map = (HashMap)list.get(i);
    			String storerkey = map.get("STORERKEY")+"";
    			String sku = map.get("SKU")+"";
    			Double totalQty = (Double)map.get("QTY");
    			
    			//获取明细行及预期量需要扣减的数量
    			List detailList = this.getDetail(facilityName, receiptkey, storerkey, sku);
    			if(detailList!=null){
    				for(int k=0;k<detailList.size();k++){
    					HashMap detailMap = (HashMap)detailList.get(k);
    					String receiptlinenumber = detailMap.get("RECEIPTLINENUMBER")+"";
    					Double detailQty = (Double)detailMap.get("QTY");
    					
    					if(detailQty>totalQty){
    						detailQty = totalQty;
    					}
    					
    					//更新收货明细行
    					this.updateReceiptdetail(facilityName, receiptkey, receiptlinenumber, detailQty);
    					//扣减数量
    					totalQty -= detailQty;
    					if(totalQty<=0){
    						break;
    					}
    				}
    			}
    			
    		}
    		//更新收货单状态
    		this.updateReceiptStatus(facilityName, receiptkey);
    	}
    	return RET_CONTINUE;
	}
	
	public List getStorerkeyAndSku(String facilityName, String receiptkey) throws UserException{
		List list = new ArrayList();
		//根据storerkey和sku获取汇总的预期量和已收量
		String theSQL = "SELECT STORERKEY, SKU, SUM(QTYEXPECTED)-SUM(QTYRECEIVED) QTY FROM RECEIPTDETAIL WHERE RECEIPTKEY='"+receiptkey+"' GROUP BY SKU, STORERKEY HAVING SUM(QTYEXPECTED)>SUM(QTYRECEIVED)";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), theSQL, new Object[0]);
    		while(resultSet.next()){
    			HashMap map = new HashMap();
    			map.put("STORERKEY", resultSet.getString("STORERKEY"));
    			map.put("SKU", resultSet.getString("SKU"));
    			map.put("QTY", resultSet.getDouble("QTY"));
    			list.add(map);
    		}
    		resultSet.close();
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_TR", new Object[0]);
		}
    	
    	return list;
	}
	
	public List getDetail(String facilityName, String receiptkey, String storerkey, String sku) throws UserException{
		List list = new ArrayList();
		//根据storerkey和sku获取汇总的预期量和已收量
		String theSQL = "SELECT RECEIPTLINENUMBER, (QTYEXPECTED-QTYRECEIVED) QTY FROM RECEIPTDETAIL WHERE RECEIPTKEY='"+receiptkey+"' AND STORERKEY='"+storerkey+"' AND SKU='"+sku+"' AND QTYEXPECTED>QTYRECEIVED ";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), theSQL, new Object[0]);
    		while(resultSet.next()){
    			HashMap map = new HashMap();
    			map.put("RECEIPTLINENUMBER", resultSet.getString("RECEIPTLINENUMBER"));
    			map.put("QTY", resultSet.getDouble("QTY"));
    			list.add(map);
    		}
    		resultSet.close();
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_TR", new Object[0]);
		}
    	
    	return list;
	}
	
	public void updateReceiptdetail(String facilityName, String receiptkey, String receiptlinenumber, Double qty) throws UserException{
		String sql = "UPDATE RECEIPTDETAIL SET QTYEXPECTED=QTYEXPECTED-? WHERE RECEIPTKEY=? AND RECEIPTLINENUMBER=? ";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		appAccess.executeUpdate(facilityName.toUpperCase(), sql, new Object[]{qty, receiptkey, receiptlinenumber});
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_TR", new Object[1]);
		}
	}
	
	public void updateReceiptStatus(String facilityName, String receiptkey) throws UserException{
		String sql = "UPDATE RECEIPT SET STATUS='9' WHERE RECEIPTKEY=?";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		appAccess.executeUpdate(facilityName.toUpperCase(), sql, new Object[]{receiptkey});
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_TR", new Object[1]);
		}
	}
}
