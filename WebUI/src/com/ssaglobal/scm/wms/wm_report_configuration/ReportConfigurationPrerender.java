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
package com.ssaglobal.scm.wms.wm_report_configuration;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class ReportConfigurationPrerender extends FormExtensionBase {

	public ReportConfigurationPrerender() {
	}

	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException {

		String widgetName = context.getActionObject().getName();
		RuntimeFormWidgetInterface rptID = form.getFormWidgetByName("RPT_ID");
		RuntimeFormWidgetInterface rptTitle = form.getFormWidgetByName("RPT_TITLE");
		RuntimeFormWidgetInterface rptCategoory = form.getFormWidgetByName("CATEGORY_ID");
		RuntimeFormWidgetInterface rptURL = form.getFormWidgetByName("RPT_URL");
		RuntimeFormWidgetInterface birtRptUrl = form.getFormWidgetByName("BIRTRPT_URL");

		try {
			// Add your code here to process the event
			if (widgetName.equals("NEW")) {
				rptID.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptCategoory.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptURL.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptTitle.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
				if (ReportUtil.isBIRT(context.getState()) == true) {
					// Hide Cognos, Show BIRT
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}
				else {
					// Hide BIRT, Show Cognos
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}

			} else {
				rptID.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				rptCategoory.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				rptURL.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptTitle.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				
				
				if (ReportUtil.isBIRT(context.getState()) == true) {
					// Hide Cognos, Show BIRT
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				} else {
					// Hide BIRT, Show Cognos
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}

			}
		} catch (Exception e) {
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws UserException {
		String widgetName = context.getActionObject().getName();
		RuntimeFormWidgetInterface rptID = form.getFormWidgetByName("RPT_ID");
		RuntimeFormWidgetInterface rptTitle = form.getFormWidgetByName("RPT_TITLE");
		RuntimeFormWidgetInterface rptURL = form.getFormWidgetByName("RPT_URL");
		RuntimeFormWidgetInterface rptCategoory = form.getFormWidgetByName("CATEGORY_ID");
		RuntimeFormWidgetInterface birtRptUrl = form.getFormWidgetByName("BIRTRPT_URL");

		try {
			// Add your code here to process the event
			if (widgetName.equals("NEW")) {
				rptID.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptTitle.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptURL.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptCategoory.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
				if (ReportUtil.isBIRT(context.getState()) == true) {
					// Hide Cognos, Show BIRT
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				} else {
					// Hide BIRT, Show Cognos
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}
			} else {
				rptID.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				rptTitle.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				rptURL.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				rptCategoory.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				
				if (ReportUtil.isBIRT(context.getState()) == true) {
					// Hide Cognos, Show BIRT
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				} else {
					// Hide BIRT, Show Cognos
					birtRptUrl.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					rptURL.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
				}

			}
		} catch (Exception e) {
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}
