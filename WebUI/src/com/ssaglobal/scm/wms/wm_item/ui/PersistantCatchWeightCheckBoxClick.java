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
package com.ssaglobal.scm.wms.wm_item.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
 * TODO Document PersistantCatchWeightCheckBoxClick class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class PersistantCatchWeightCheckBoxClick extends com.epiphany.shr.ui.action.ActionExtensionBase{
	private static final String IS_CHECKED = "1";
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		
		
		RuntimeFormWidgetInterface catchgrosswgt = currentForm.getFormWidgetByName("catchgrosswgt");
		RuntimeFormWidgetInterface stdgrosswgt = currentForm.getFormWidgetByName("stdgrosswgt1");
		RuntimeFormWidgetInterface catchnetwgt = currentForm.getFormWidgetByName("catchnetwgt");
		RuntimeFormWidgetInterface stdnetwgt = currentForm.getFormWidgetByName("stdnetwgt1");
		RuntimeFormWidgetInterface catchtarewgt = currentForm.getFormWidgetByName("catchtarewgt");
		RuntimeFormWidgetInterface tarewgt = currentForm.getFormWidgetByName("tarewgt1");

		RuntimeFormWidgetInterface stduom = currentForm.getFormWidgetByName("stduom");
		RuntimeFormWidgetInterface defaultRF = currentForm.getFormWidgetByName("ZERODEFAULTWGTFORPICK");

		
		
		DataBean focus = currentForm.getFocus();
		String IBSUMCWFLGStr = BioAttributeUtil.getString(focus, "IBSUMCWFLG");;
		String OBSUMCWFLGStr = BioAttributeUtil.getString(focus, "OBSUMCWFLG");
		String SHOWRFCWONTRANSStr = BioAttributeUtil.getString(focus, "SHOWRFCWONTRANS");
		if(IS_CHECKED.equalsIgnoreCase(IBSUMCWFLGStr)
				||IS_CHECKED.equalsIgnoreCase(OBSUMCWFLGStr)
				||IS_CHECKED.equalsIgnoreCase(SHOWRFCWONTRANSStr)){
			catchgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			stdgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			
			catchnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			stdnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			
			catchtarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			tarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);	
			
			stduom.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			defaultRF.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);

		}
		
		
		return RET_CONTINUE;
	}


}
