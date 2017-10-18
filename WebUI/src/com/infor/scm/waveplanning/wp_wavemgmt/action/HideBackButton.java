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
package com.infor.scm.waveplanning.wp_wavemgmt.action;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * TODO Document HideBackButton class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class HideBackButton extends FormWidgetExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(HideBackButton.class);
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_SetMessageOnModal", "***Executing HideBackButton", SuggestedCategory.NONE);
			String formName = widget.getForm().getName();
			String parentFormName = widget.getForm().getParentForm(state).getName();
			String ppFormName = widget.getForm().getParentForm(state).getParentForm(state).getName();
			if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(ppFormName)){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			}
		
		return RET_CONTINUE;
	}
}
