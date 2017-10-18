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
import java.sql.ResultSet;
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

public class BatchSetDeliveryOrder extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		//�������
		String pickLot = currentForm.getFormWidgetByName("PICKLOT").getDisplayValue();
		//����浥��
		String deliveryOrder = currentForm.getFormWidgetByName("DELIVERYORDER").getDisplayValue();
		//��Ҫ�󶨵Ķ�������
		String qty = currentForm.getFormWidgetByName("QTY").getDisplayValue();
		currentForm.getFormWidgetByName("QTY").getValue();
		HttpSession session = context.getState().getRequest().getSession();
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String query = "select distinct t.orderkey FROM taskdetail t where t.message02 = ? order by orderkey";
		String update = "update orders set PRONUMBER=? where orderkey=?";
		ResultSet rs = null;
		try {
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[]{pickLot});
			int maxCount = 0;
			try{
				maxCount = Integer.valueOf(qty);
			}catch (Exception e ){
				currentForm.getFormWidgetByName("QTY").setDisplayValue("");
				throw new RuntimeException("����["+maxCount+"]��ʽ����ȷ��");
			}
			int count = 0;
			while(rs.next()){
				if(count < maxCount){
					appAccess.executeUpdate(facilityName.toUpperCase(), update, new Object[]{deliveryOrder,rs.getString(1)});
					//�浥�ŵ���
					try{
						String temp = "000000000000"+(Integer.parseInt(deliveryOrder)+1);
						deliveryOrder = temp.substring(temp.length()-10);
					}catch (Exception e ){
						currentForm.getFormWidgetByName("DELIVERYORDER").setDisplayValue("");
						throw new RuntimeException("�浥��["+deliveryOrder+"]��ʽ����ȷ��ֻ�ܰ������֣�");
					}
					count++;
				}else{
					break;
				}
			}
			if(count == 0){
				currentForm.getFormWidgetByName("PICKLOT").setDisplayValue("");
				currentForm.getFormWidgetByName("QTY").setDisplayValue("");
				throw new RuntimeException("�������["+pickLot+"]�����ڣ�");
			}
		}catch (Exception e) {
			throw new UserException(e.getMessage(), new Object[0]);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		currentForm.getFormWidgetByName("PICKLOT").setDisplayValue("");
		currentForm.getFormWidgetByName("QTY").setDisplayValue("");
		currentForm.getFormWidgetByName("DELIVERYORDER").setDisplayValue("");
		return RET_CONTINUE;
	}
}