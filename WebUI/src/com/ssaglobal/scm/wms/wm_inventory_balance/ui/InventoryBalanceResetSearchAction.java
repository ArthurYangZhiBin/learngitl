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
package com.ssaglobal.scm.wms.wm_inventory_balance.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class InventoryBalanceResetSearchAction extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result){
		//Get state
		StateInterface state = context.getState();
		
		//Get search form handle from toolbar form and reset search values
		ResetIB(state.getCurrentRuntimeForm().getParentForm(state));

		return RET_CONTINUE;
	}
	
	public void ResetIB(RuntimeFormInterface form){
		//Reset search values
		form.getFormWidgetByName("EXTERNALLOTEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("EXTERNALLOTSTART").setDisplayValue("0");
		form.getFormWidgetByName("LOCEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOCSTART").setDisplayValue("0");
		form.getFormWidgetByName("LOTEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOTSTART").setDisplayValue("0");
		form.getFormWidgetByName("LPNEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("LPNSTART").setDisplayValue("");
		form.getFormWidgetByName("QTYMAX").setDisplayValue("999999999");
		form.getFormWidgetByName("QTYMIN").setDisplayValue("1");
		form.getFormWidgetByName("SKUEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("SKUSTART").setDisplayValue("0");
		form.getFormWidgetByName("STORERKEYEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("STORERKEYSTART").setDisplayValue("0");	
	}
}