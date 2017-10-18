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
package com.ssaglobal.scm.wms.wm_cycleclass.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

/**
 * TODO Document CycleClassSupervisorDisableEnable class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class CycleClassSupervisorDisableEnable extends ActionExtensionBase{
	private static final String IS_SUPERVISOR="1";
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
		RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
		RuntimeFormWidgetInterface isSupervisorWidget = currentForm.getFormWidgetByName("SUPERVISORCNT");
		String isSupervisorStr = isSupervisorWidget.getValue().toString();
		RuntimeFormWidgetInterface spcv = currentForm.getFormWidgetByName("SUPPOSCNTCHG");
		RuntimeFormWidgetInterface sncv = currentForm.getFormWidgetByName("SUPNEGCNTCHG");
		RuntimeFormWidgetInterface svpv = currentForm.getFormWidgetByName("SUPPOSDOLCHG");
		RuntimeFormWidgetInterface svnv = currentForm.getFormWidgetByName("SUPNEGDOLCHG");
		
		if (IS_SUPERVISOR.equalsIgnoreCase(isSupervisorStr)) {
			spcv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			sncv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			svpv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			svnv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
		} else {
			spcv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			sncv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			svpv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			svnv.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
		return RET_CONTINUE;
	}
}
