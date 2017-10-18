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

/**
 * TODO Document PersistantCatchWeightDataFormPrerender class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class PersistantCatchWeightDataFormPrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	private static final String IS_CHECKED = "1";
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		RuntimeFormWidgetInterface IBSUMCWFLG = form.getFormWidgetByName("IBSUMCWFLG");
		RuntimeFormWidgetInterface OBSUMCWFLG = form.getFormWidgetByName("OBSUMCWFLG");
		RuntimeFormWidgetInterface SHOWRFCWONTRANS = form.getFormWidgetByName("SHOWRFCWONTRANS");

		
		
		RuntimeFormWidgetInterface catchgrosswgt = form.getFormWidgetByName("catchgrosswgt");
		RuntimeFormWidgetInterface stdgrosswgt = form.getFormWidgetByName("stdgrosswgt1");
		RuntimeFormWidgetInterface catchnetwgt = form.getFormWidgetByName("catchnetwgt");
		RuntimeFormWidgetInterface stdnetwgt = form.getFormWidgetByName("stdnetwgt1");
		RuntimeFormWidgetInterface catchtarewgt = form.getFormWidgetByName("catchtarewgt");
		RuntimeFormWidgetInterface tarewgt = form.getFormWidgetByName("tarewgt1");

		RuntimeFormWidgetInterface stduom = form.getFormWidgetByName("stduom");
		RuntimeFormWidgetInterface defaultRF = form.getFormWidgetByName("ZERODEFAULTWGTFORPICK");
		
		DataBean focus = form.getFocus();
		Object snunEndToEndObj = focus.getValue("SNUM_ENDTOEND");
		Object icwflagObj = focus.getValue("ICWFLAG");
		String isICWFLG = "0";
		Object ocwflagObj = focus.getValue("OCWFLAG");
		String isOCWFLG = "0";
		if(!this.isEmpty(ocwflagObj)){
			isOCWFLG = ocwflagObj.toString();
		}
		if(!this.isEmpty(icwflagObj)){
			isICWFLG = icwflagObj.toString();
		}
		String isSNUM_ENDTOEND = "0";
		if(!this.isEmpty(snunEndToEndObj)){
			isSNUM_ENDTOEND = snunEndToEndObj.toString();
		}
		
		
		
		
		if(IS_CHECKED.equalsIgnoreCase(isSNUM_ENDTOEND) 
				&& IS_CHECKED.equalsIgnoreCase(isICWFLG)){
//			focus.setValue("IBSUMCWFLG", "0");
//			focus.setValue("OBSUMCWFLG", "0");
//			focus.setValue("SHOWRFCWONTRANS", "0");
			IBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			OBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			SHOWRFCWONTRANS.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

			catchgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			stdgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			
			catchnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			stdnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			
			catchtarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			tarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);	
			
			stduom.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			defaultRF.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		
		}else{				
			String isIBSUMCWFLG = this.isEmpty(focus.getValue("IBSUMCWFLG"))?"0":focus.getValue("IBSUMCWFLG").toString();
			String isOBSUMCWFLG = this.isEmpty(focus.getValue("OBSUMCWFLG"))?"0":focus.getValue("OBSUMCWFLG").toString();
			String isSHOWRFCWONTRANS = this.isEmpty(focus.getValue("SHOWRFCWONTRANS"))?"0":focus.getValue("SHOWRFCWONTRANS").toString();
			if(IS_CHECKED.equalsIgnoreCase(isICWFLG)){
//				focus.setValue("IBSUMCWFLG", "0");
				IBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);					
			}else{
				IBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
			if(IS_CHECKED.equalsIgnoreCase(isOCWFLG)){
//				focus.setValue("OBSUMCWFLG", "0");
				OBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);					
			}else{
				OBSUMCWFLG.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
			if(!IS_CHECKED.equalsIgnoreCase(isIBSUMCWFLG) 
					&& !IS_CHECKED.equalsIgnoreCase(isOBSUMCWFLG)
					&& !IS_CHECKED.equalsIgnoreCase(isSHOWRFCWONTRANS)){
				catchgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				stdgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				
				catchnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				stdnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				
				catchtarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				tarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);	
				
				stduom.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				defaultRF.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				
			}else{
				catchgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				stdgrosswgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				
				catchnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				stdnetwgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				
				catchtarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				tarewgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);	
				
				stduom.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				defaultRF.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				
				
			}
		}
		return RET_CONTINUE;
	}
	
	
	boolean isEmpty(Object attributeValue) {
		if (attributeValue == null) {
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else if (attributeValue.toString().matches("null")) {
			return true;
		} else {
			return false;
		}
	}
}
