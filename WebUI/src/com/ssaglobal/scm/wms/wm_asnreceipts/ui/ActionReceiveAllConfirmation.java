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

import java.text.MessageFormat;
import java.util.HashSet;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.SessionUtil;

public class ActionReceiveAllConfirmation extends FormExtensionBase {

	public ActionReceiveAllConfirmation() {
	}

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException, UserException {

		final StateInterface state = context.getState();
		UserInterface user = state.getUser();
		LocaleInterface locale = user.getLocale();
		RuntimeFormWidgetInterface widget = form.getFormWidgetByName("message");
		String strMsg = widget.getLabel("label", locale);

		String arguments[] = { getCount(state) };
		strMsg = getTextMessage("WMTXT_CONFIRMRECEIVEALL", arguments, locale);
		MessageFormat mf = new MessageFormat(strMsg);
		mf.setLocale(locale.getJavaLocale());
		StringBuffer result = new StringBuffer();
		mf.format(arguments, result, null);
		widget.setLabel("label", result.toString());

		// RM 9/8/08 SN Mod
		if (getShowWarning(state) == true) {
			RuntimeFormWidgetInterface promptWidget = form.getFormWidgetByName("PROMPT");
			promptWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
			HashSet<String> warningItemsSet = (HashSet<String>) SessionUtil.getInteractionSessionAttribute(	ValidationASNReceiveAll.RECEIVE_CWCD_WARNING_ITEMS,
																											state);
			if (warningItemsSet != null) {
				promptWidget.setLabel(	RuntimeFormWidgetInterface.LABEL_LABEL,
										getTextMessage(	"WMEXP_CWCD_REQUIRE_WARNING_RECEIVE",
														new Object[] { warningItemsSet.toString().substring(1,
																											warningItemsSet.toString().length() - 1) },
														state.getLocale()));
			}
		}
		return RET_CONTINUE;

	}

	private boolean getShowWarning(StateInterface state) {
		Boolean showWarning = (Boolean) SessionUtil.getInteractionSessionAttribute(	ValidationASNReceiveAll.RECEIVE_CWCD_SHOW_WARNING,
																					state);
		if (showWarning == null) {
			return false;
		}
		return showWarning;
	}

	private String getCount(StateInterface state) throws EpiException {
		String count;
		EpnyControllerState EpnyContState = (EpnyControllerState) state;
		HttpSession session = EpnyContState.getRequest().getSession();
		final Object receiveAllCount = SessionUtil.getInteractionSessionAttribute(	ValidationASNReceiveAll.RECEIVEALLCOUNT,
																					state);
		// session.getAttribute(ValidationASNReceiveAll.RECEIVEALLCOUNT);
		if (receiveAllCount == null) {
			count = "0";
		} else {
			count = receiveAllCount.toString();
		}
		return count;
	}
}
