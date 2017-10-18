package com.ssaglobal.scm.wms.wm_inventory_holds.ui;


import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
 * 
 * @author awassay
 *	AW 09/21/10 Incident:3997553 Defect:283808
 */
public class InventoryHoldListPreRender extends FormExtensionBase{
	String sessionVariable;
	String sessionObjectValue;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryHoldListPreRender.class);
			
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) {	
		
		int winStart = form.getWindowStart();
		_log.debug("LOG_SYSTEM_OUT","WINDOW START: "+winStart,100L);
		String interactionID = context.getState().getInteractionId();
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		sessionObjectValue = "" + winStart;
		HttpSession session = context.getState().getRequest().getSession();
		session.setAttribute(sessionVariable, sessionObjectValue);
		
		return RET_CONTINUE;
	}	
}