/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class QuerySO extends ActionExtensionBase{

	//Form static Strings
	private static String SLOT_1 = "list_slot_1"; 
	private static String SHELL_FORM = "wms_list_shell";
	private static String TABGROUP_SLOT = "tbgrp_slot";
	private static String TAB_0 = "tab 0";
	
	//Query static Strings
	private static String ORDER_TABLE = "wm_orders";
	private static String OWNER = "STORERKEY";
	private static String STATUS = "STATUS";
	private static String SHIPPED = "95";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//Get the storer from the Header
		String value = null;
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		while(!(shellForm.getName().equals(SHELL_FORM))){
			shellForm = shellForm.getParentForm(state);
		}
//		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+shellForm.getName(),100L);
		SlotInterface slot1 = shellForm.getSubSlot(SLOT_1);
//		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+slot1.getName(),100L);
		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
//		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+headerForm.getName(),100L);
		SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TABGROUP_SLOT);
//		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+headerTbgrpSlot.getName(),100L);
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB_0);			
//		_log.debug("LOG_SYSTEM_OUT","FINDING UNCAUGHT EXCEPTION: "+normalHeaderForm.getName(),100L);
		value = normalHeaderForm.getFormWidgetByName(OWNER).getDisplayValue();

		//Executes only if detail form is a new screen
		String qry = "";
		
		// 2012-08-22
		// Modified by Will Pu
		// '已上传SAP'的及'外部取消'类型的单据也要拿到
		if (value != null && !"".equalsIgnoreCase(value) && !" ".equalsIgnoreCase(value)) {
			qry = ORDER_TABLE + "." + OWNER + " = '" + value.toUpperCase() + "' AND (" + ORDER_TABLE + "." + STATUS + " = '" + SHIPPED + "' ";
			qry += "OR " + ORDER_TABLE + "." + STATUS + " = '96' OR " + ORDER_TABLE + "." + STATUS + " = '98')";
		} else {
			qry = "(" + ORDER_TABLE + "." + STATUS + " = '" + SHIPPED + "' ";
			qry += " OR " + ORDER_TABLE + "." + STATUS + " = '96' OR " + ORDER_TABLE + "." + STATUS + " = '98')";
		}
		
		// 2012-07-03
		// Modified by Will Pu
		// 已经被填充过的SO单号将被过滤掉
		List<String> pokeys = new ArrayList<String>();
		
		HttpSession session = context.getState().getRequest().getSession();
		
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String query = "SELECT DISTINCT POKEY FROM RECEIPTDETAIL WHERE POKEY IS NOT NULL AND POKEY <> ' '";
		
    	try {
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		
    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);
    		
    		while (resultSet.next()) {
    			pokeys.add(resultSet.getString(1));
    		}
    		
    		resultSet.close();
    	} catch (SQLException e) {			
			e.printStackTrace();
			throw new UserException("", new Object[0]);
		}
    	
    	for (String pokey : pokeys) {
    		if (pokey.trim().equals("")) {
    			continue;
    		}
    		
    		qry += " AND " + ORDER_TABLE + ".ORDERKEY != '" + pokey.trim() + "'";
    	}
		
//		_log.debug("LOG_SYSTEM_OUT","This is the query: "+qry,100L);
		Query loadBiosQry = new Query(ORDER_TABLE, qry, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		if (value == null){
			newFocus.setEmptyList(true);
		}
		result.setFocus(newFocus);
		return RET_CONTINUE;
		}
}