package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

/**
 * Created 2012-07-06
 * @author Will Pu
 * 国际订单下载装箱单
 */
public class LoadPackInfos extends ActionExtensionBase {
	private static String WIDGET_NAME = "PackNoticeNo";
	private static String SHELL_FORM = "wms_list_shell";
	private static String SLOT_1 = "list_slot_1";
	private static String TABGROUP_SLOT = "tbgrp_slot";
	private static String TAB_0 = "tab 0";
	
	private final static String PROC_NAME = "NSPLOADPACKINFOS";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		StateInterface state = context.getState();
		
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		
		String packNoticeNo = "", receiptkey = "";
		
		packNoticeNo = shellForm.getFormWidgetByName(WIDGET_NAME).getValue().toString();
		
		while (!(shellForm.getName().equals(SHELL_FORM))) {
			shellForm = shellForm.getParentForm(state);
		}
		
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot(SLOT_1), null);
		
		SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TABGROUP_SLOT);
		
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB_0);			
		
		receiptkey = normalHeaderForm.getFormWidgetByName("RECEIPTKEY").getDisplayValue();
		
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		
		Array parms = new Array();
		
		parms.add(new TextData(receiptkey));
		parms.add(new TextData(packNoticeNo));
		
		actionProperties.setProcedureName(PROC_NAME);
		actionProperties.setProcedureParameters(parms);
		
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);	
		} catch (Exception e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}
		
		result.setFocus(shellForm.getFocus());
		
		return RET_CONTINUE;
	}
}