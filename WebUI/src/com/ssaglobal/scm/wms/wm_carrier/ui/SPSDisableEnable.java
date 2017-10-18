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

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.SaveASNReceipt;

/**
 * TODO Document SPSDisableEnable class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class SPSDisableEnable extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SPSDisableEnable.class);
	
	/* (non-Javadoc)
	 * @see com.epiphany.shr.ui.action.ActionExtensionBase#execute(com.epiphany.shr.ui.action.ActionContext, com.epiphany.shr.ui.action.ActionResult)
	 */
	@Override
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
		RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
		RuntimeFormWidgetInterface spsWidget = currentForm.getFormWidgetByName("KSHIP_CARRIER");
		String value = spsWidget.getValue().toString();
		RuntimeFormWidgetInterface scacWidget = currentForm.getFormWidgetByName("SCAC_CODE");
		
//		DataBean focus = currentForm.getFocus();
//		focus.setValue(attributeName, null);

		if ("1".equalsIgnoreCase(value)) {
			scacWidget.setDisplayValue("");
			scacWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			scacWidget.setLabel("label", "Small Parcel SCAC");
			throw new UserException("SPS_WARNING",new Object[0]);
		} else {
			scacWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			scacWidget.setLabel("label", "Carrier SCAC");
		}
		context.getState().getRequest().getSession().setAttribute("SPS_ENABLED", value);
		return RET_CONTINUE;
	}

}
