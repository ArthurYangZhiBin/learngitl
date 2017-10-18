/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_carrier.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * TODO Document SPSRatingFormPrerender class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class SPSRatingFormPrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {

		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();

		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add("tab 0");
		RuntimeFormInterface generalForm = FormUtil.findForm(currentForm, "wm_list_shell_carrier", "wm_carrier_general_view", tabIdentifiers, state);
		Object spsEnabledObj = context.getState().getRequest().getSession().getAttribute("SPS_ENABLED");
		String spsEnabled = spsEnabledObj == null?"":spsEnabledObj.toString();
		DataBean carrierFocus = generalForm.getFocus();
		if (carrierFocus.isBio() || carrierFocus instanceof QBEBioBean) {
			if("0".equalsIgnoreCase(spsEnabled)){//not enabled, diable rating tab
				currentForm.getFormWidgetByName("ACTWGTORSYSTEMWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				currentForm.getFormWidgetByName("SCAC_CODE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				currentForm.getFormWidgetByName("SMALLORLTL").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				currentForm.getFormWidgetByName("SPSTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}else{
				currentForm.getFormWidgetByName("ACTWGTORSYSTEMWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				currentForm.getFormWidgetByName("SCAC_CODE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				currentForm.getFormWidgetByName("SMALLORLTL").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				currentForm.getFormWidgetByName("SPSTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
		}


		return RET_CONTINUE;
	}

}
