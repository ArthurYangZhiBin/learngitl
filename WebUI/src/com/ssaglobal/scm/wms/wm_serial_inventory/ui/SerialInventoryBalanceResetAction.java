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
package com.ssaglobal.scm.wms.wm_serial_inventory.ui;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class SerialInventoryBalanceResetAction extends ActionExtensionBase{   
	private static String USER_CONTEXT_KEY_OWNER_START = "user.ctx.key.ownerstart";
	private static String USER_CONTEXT_KEY_OWNER_END = "user.ctx.key.ownerend";
	private static String USER_CONTEXT_KEY_ITEM_START = "user.ctx.key.itemstart";
	private static String USER_CONTEXT_KEY_ITEM_END = "user.ctx.key.itemend";
	private static String USER_CONTEXT_KEY_LOT_START = "user.ctx.key.lotstart";
	private static String USER_CONTEXT_KEY_LOT_END = "user.ctx.key.lotend";
	private static String USER_CONTEXT_KEY_LPN_START = "user.ctx.key.lpnstart";
	private static String USER_CONTEXT_KEY_LPN_END = "user.ctx.key.lpnend";
	private static String USER_CONTEXT_KEY_LOCATION_START = "user.ctx.key.locationstart";
	private static String USER_CONTEXT_KEY_LOCATION_END = "user.ctx.key.locationend";
	private static String USER_CONTEXT_KEY_SERIAL_START = "user.ctx.key.serialstart";
	private static String USER_CONTEXT_KEY_SERIAL_END = "user.ctx.key.serialend";
	private static String USER_CONTEXT_KEY_DATA2_START = "user.ctx.key.data2start";
	private static String USER_CONTEXT_KEY_DATA2_END = "user.ctx.key.data2end";
	private static String USER_CONTEXT_KEY_DATA3_START = "user.ctx.key.data3start";
	private static String USER_CONTEXT_KEY_DATA3_END = "user.ctx.key.data3end";
	private static String USER_CONTEXT_KEY_DATA4_START = "user.ctx.key.data4start";
	private static String USER_CONTEXT_KEY_DATA4_END = "user.ctx.key.data4end";
	private static String USER_CONTEXT_KEY_DATA5_START = "user.ctx.key.data5start";
	private static String USER_CONTEXT_KEY_DATA5_END = "user.ctx.key.data5end";
	private static String USER_CONTEXT_KEY_LONGSERIAL_START = "user.ctx.key.longserialstart";
	private static String USER_CONTEXT_KEY_LONGSERIAL_END = "user.ctx.key.longserialend";
	private static String USER_CONTEXT_KEY_WEIGHT_START = "user.ctx.key.weightstart";
	private static String USER_CONTEXT_KEY_WEIGHT_END = "user.ctx.key.weightend";
	private static String USER_CONTEXT_KEY_QTY_START = "user.ctx.key.qtystart";
	private static String USER_CONTEXT_KEY_QTY_END = "user.ctx.key.qtyend";
	protected int execute(ActionContext context, ActionResult result){
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm().getParentForm(state); //state.getCurrentRuntimeForm();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		form.getFormWidgetByName("OWNEREND").setDisplayValue("ZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("OWNERSTART").setDisplayValue("0");
		form.getFormWidgetByName("ITEMEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("ITEMSTART").setDisplayValue("0");
		form.getFormWidgetByName("LOTEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOTSTART").setDisplayValue("0");
		form.getFormWidgetByName("LPNSTART").setDisplayValue("");
		form.getFormWidgetByName("LPNEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("LOCATIONEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOCATIONSTART").setDisplayValue("0");
		form.getFormWidgetByName("SERIALEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("SERIALSTART").setDisplayValue("0");
		form.getFormWidgetByName("DATA2START").setDisplayValue("");
		form.getFormWidgetByName("DATA2END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("DATA3START").setDisplayValue("");
		form.getFormWidgetByName("DATA3END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("DATA4START").setDisplayValue("");
		form.getFormWidgetByName("DATA4END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("DATA5START").setDisplayValue("");
		form.getFormWidgetByName("DATA5END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("LONGSERIALSTART").setDisplayValue("");
		form.getFormWidgetByName("LONGSERIALEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("WEIGHTEND").setDisplayValue("9999999999999999.99999");
		form.getFormWidgetByName("WEIGHTSTART").setDisplayValue("0");
		form.getFormWidgetByName("QTYEND").setDisplayValue("9999999999999999.99999");
		form.getFormWidgetByName("QTYSTART").setDisplayValue("0");
		
		
		userContext.put(USER_CONTEXT_KEY_OWNER_START,"0");
		userContext.put(USER_CONTEXT_KEY_OWNER_END,"ZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_ITEM_START,"0");
		userContext.put(USER_CONTEXT_KEY_ITEM_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_LOT_START,"0");
		userContext.put(USER_CONTEXT_KEY_LOT_END,"ZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_LPN_START,"");
		userContext.put(USER_CONTEXT_KEY_LPN_END,"ZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_LOCATION_START,"0");
		userContext.put(USER_CONTEXT_KEY_LOCATION_END,"ZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_SERIAL_START,"0");
		userContext.put(USER_CONTEXT_KEY_SERIAL_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_DATA2_START,"");
		userContext.put(USER_CONTEXT_KEY_DATA2_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_DATA3_START,"");
		userContext.put(USER_CONTEXT_KEY_DATA3_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_DATA4_START,"");
		userContext.put(USER_CONTEXT_KEY_DATA4_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_DATA5_START,"");
		userContext.put(USER_CONTEXT_KEY_DATA5_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_LONGSERIAL_START,"");
		userContext.put(USER_CONTEXT_KEY_LONGSERIAL_END,"ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		userContext.put(USER_CONTEXT_KEY_WEIGHT_START,"0");
		userContext.put(USER_CONTEXT_KEY_WEIGHT_END,"9999999999999999.99999");
		userContext.put(USER_CONTEXT_KEY_QTY_START,"0");
		userContext.put(USER_CONTEXT_KEY_QTY_END,"9999999999999999.99999");

		return RET_CONTINUE;
	}
	



}
