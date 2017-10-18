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
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_query_builder.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;

/**
 * TODO Document SwitchRefreshButton class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class SwitchRefreshButton extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected int preRenderForm(UIRenderContext context,RuntimeNormalFormInterface form) throws EpiException {
			StateInterface state = context.getState();
			RuntimeFormInterface parentForm = form.getParentForm(state);
			String slotName = parentForm.getParentForm(state).getName();
			if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
				form.getFormWidgetByName("QBREFRESH").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
				form.getFormWidgetByName("REFRESH").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			}else{//it is running from wave maintenance wave detail page
				form.getFormWidgetByName("QBREFRESH").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
				form.getFormWidgetByName("REFRESH").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);				
			}


		return RET_CONTINUE;
	}
}
