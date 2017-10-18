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
package com.ssaglobal.scm.wms.wm_customer.ui;

//Import 3rd party packages and classes
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class SetDeliveryOrder extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		//订单号
		String orderkey = currentForm.getFormWidgetByName("ORDERKEY").getDisplayValue();
		//快递面单号
		String deliveryOrder = currentForm.getFormWidgetByName("DELIVERYORDER").getDisplayValue();
		HttpSession session = context.getState().getRequest().getSession();
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String update = "update orders set PRONUMBER=? where orderkey=?";
		int count = 0;
		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			count = appAccess.executeUpdate(facilityName.toUpperCase(), update, new Object[]{deliveryOrder,orderkey});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[0]);
		}
		if(count == 0){
			throw new UserException("未找到该订单，请核对！", new Object[0]);
		}else{
			currentForm.getFormWidgetByName("DELIVERYORDER").setDisplayValue("");
			currentForm.getFormWidgetByName("ORDERKEY").setDisplayValue("");
//			throw new UserException("面单绑定成功！", new Object[0]);
		}
		
		return RET_CONTINUE;
	}
}