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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.reporting;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class SetReportPrintingURL extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetReportPrintingURL.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		try
		{
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			String reportUrl = (String) userCtx.get("REPORTINGURL");
			_log.debug("LOG_DEBUG_EXTENSION_SetReportingURL", "Reporting URL from context: " + reportUrl,
						SuggestedCategory.NONE);
			RuntimeFormWidgetInterface reportFrame = form.getFormWidgetByName("report");
			reportFrame.setProperty(RuntimeFormWidgetInterface.PROP_SRC, reportUrl);
			_log.debug("LOG_DEBUG_EXTENSION_SetReportingURL", "Reporting URL from widget: "
					+ reportFrame.getProperty(RuntimeFormWidgetInterface.PROP_SRC), SuggestedCategory.NONE);

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
