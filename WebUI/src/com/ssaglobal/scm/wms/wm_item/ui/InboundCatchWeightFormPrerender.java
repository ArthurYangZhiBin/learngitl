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

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
 * TODO Document InboundCatchWeightFormPrerender class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class InboundCatchWeightFormPrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	private static final String IS_CHECKED = "1";
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		DataBean focus = form.getFocus();
		String IBSUMCWFLGStr = BioAttributeUtil.getString(focus, "IBSUMCWFLG");
		String isICWFLG =  BioAttributeUtil.getString(focus, "ICWFLAG");
				
		if(IS_CHECKED.equalsIgnoreCase(IBSUMCWFLGStr)){
			if(IS_CHECKED.equalsIgnoreCase(isICWFLG)){
				focus.setValue("ICWFLAG", "0");
			}
			RuntimeFormWidgetInterface inboundCatchWeight = form.getFormWidgetByName("ICWFLAG");
			inboundCatchWeight.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
		
		
		return RET_CONTINUE;
	}	

}
