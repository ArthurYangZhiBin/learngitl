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
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SerialInventorySaveFilter extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerialInventorySaveFilter.class);
	
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

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		//Get state
		StateInterface state = context.getState();
		
		RuntimeFormInterface form = state.getCurrentRuntimeForm().getParentForm(state); //state.getCurrentRuntimeForm();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();

		//Extension called from Search Form - save the current filter into the usercontext
		userContext.put(USER_CONTEXT_KEY_OWNER_START,form.getFormWidgetByName("OWNERSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_OWNER_END,form.getFormWidgetByName("OWNEREND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ITEM_START,form.getFormWidgetByName("ITEMSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ITEM_END,form.getFormWidgetByName("ITEMEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LOT_START,form.getFormWidgetByName("LOTSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LOT_END,form.getFormWidgetByName("LOTEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LPN_START,form.getFormWidgetByName("LPNSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LPN_END,form.getFormWidgetByName("LPNEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LOCATION_START,form.getFormWidgetByName("LOCATIONSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LOCATION_END,form.getFormWidgetByName("LOCATIONEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_SERIAL_START,form.getFormWidgetByName("SERIALSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_SERIAL_END,form.getFormWidgetByName("SERIALEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA2_START,form.getFormWidgetByName("DATA2START").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA2_END,form.getFormWidgetByName("DATA2END").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA3_START,form.getFormWidgetByName("DATA3START").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA3_END,form.getFormWidgetByName("DATA3END").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA4_START,form.getFormWidgetByName("DATA4START").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA4_END,form.getFormWidgetByName("DATA4END").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA5_START,form.getFormWidgetByName("DATA5START").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_DATA5_END,form.getFormWidgetByName("DATA5END").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LONGSERIAL_START,form.getFormWidgetByName("LONGSERIALSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_LONGSERIAL_END,form.getFormWidgetByName("LONGSERIALEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_WEIGHT_START,form.getFormWidgetByName("WEIGHTSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_WEIGHT_END,form.getFormWidgetByName("WEIGHTEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_QTY_START,form.getFormWidgetByName("QTYSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_QTY_END,form.getFormWidgetByName("QTYEND").getDisplayValue());

		return RET_CONTINUE;
	}
}
