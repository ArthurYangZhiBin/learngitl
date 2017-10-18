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
package com.ssaglobal.scm.wms.wm_tariff;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class TariffDetailFormISPWidgetOnChange extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TariffDetailFormISPWidgetOnChange.class);
	public TariffDetailFormISPWidgetOnChange()
    {
    }
	protected int execute(ActionContext context, ActionResult result){	
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing TariffDetailFormISPWidgetOnChange",100L);		
		StateInterface state = context.getState();						
		RuntimeFormInterface form = context.getSourceWidget().getForm();			
		String widgetName = getParameter("widgetName").toString();		
		RuntimeFormWidgetInterface widgetISPeriod = form.getFormWidgetByName("PERIODTYPE");		
		RuntimeFormWidgetInterface widgetRSPeriod = form.getFormWidgetByName("RSPERIODTYPE");
			
		if(widgetName.equals("PERIODTYPE")){
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Setting Session Attribute ISPPERIODTYPE:"+widgetISPeriod.getValue(),100L);			
			state.getRequest().getSession().setAttribute("ISPPERIODTYPE",widgetISPeriod.getValue());
			state.getRequest().getSession().setAttribute("PERIODTYPECHANGED","true");
		}
		if(widgetName.equals("RSPERIODTYPE")){
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Setting Session Attribute ISPRSPERIODTYPE:"+widgetRSPeriod.getValue(),100L);			
			state.getRequest().getSession().setAttribute("ISPRSPERIODTYPE",widgetRSPeriod.getValue());		
			state.getRequest().getSession().setAttribute("RSPERIODTYPECHANGED","true");
		}
	
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting TariffDetailFormISPWidgetOnChange",100L);		
		return RET_CONTINUE;
	}		
		
}