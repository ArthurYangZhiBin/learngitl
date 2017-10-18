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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;

public class ShipmentOrderPickDetailDisableRow extends FormExtensionBase {
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		try{
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++){
				//Get List Widget
				RuntimeFormWidgetInterface status = listRows[i].getFormWidgetByName("STATUS"); // 9 
				RuntimeFormWidgetInterface quantity = listRows[i].getFormWidgetByName("QTY"); // 9
				RuntimeFormWidgetInterface dropID = listRows[i].getFormWidgetByName("DROPID"); // 9

				//Get Values
				String statusValue = status.getDisplayValue();

				if (statusValue.equalsIgnoreCase("Shipped") ){
					status.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					quantity.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					dropID.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}
				else{
					status.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					quantity.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					dropID.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}
			}
		} catch (Exception e){

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}