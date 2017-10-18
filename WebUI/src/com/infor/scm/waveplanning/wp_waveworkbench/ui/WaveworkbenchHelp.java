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
package com.infor.scm.waveplanning.wp_waveworkbench.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

import com.infor.scm.waveplanning.common.WavePlanningUtils;

public class WaveworkbenchHelp extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveworkbenchHelp.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
	
		_log.debug("LOG_DEBUG_EXTENSION_WaveworkbenchHelp", "***Executing WaveworkbenchHelp", SuggestedCategory.NONE);		
		String protocol, onlinehelpUrl, wmsName, alertsFilePath, eventDetailsFilePath, locale ="", filepath="";		
		String url= context.getState().getRequest().getRequestURL().toString();
		alertsFilePath ="enduser/monitoringandalerts/GeneratedAlerts";
		eventDetailsFilePath = "enduser/monitoringandalerts/EventDetails";		
		
		StateInterface state = context.getState();	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("slot1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
		System.out.println(headerForm.getName());
		
		if(headerForm.getName().equalsIgnoreCase("wp_waveworkbench_alerts_list_view")) {
			filepath = alertsFilePath;
		}
		if(headerForm.getName().equalsIgnoreCase("wp_waveworkbench_eventdetail_view")) {
			filepath = eventDetailsFilePath;
		}
		
		wmsName = WavePlanningUtils.wmsName;
		
		if(url !=null && !(url.equalsIgnoreCase("")) && url.charAt(4)=='s')
			protocol="HTTPS";
		else
			protocol="HTTP";
		
			
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		locale = getBaseLocale(userContext);
		_log.debug("LOG_DEBUG_EXTENSION_WaveworkbenchHelp", "***Locale: " +locale, SuggestedCategory.NONE);			
			
		onlinehelpUrl = protocol +"://"+WavePlanningUtils.onlineHelpLocation+"/"+"WavePlanning_OnlineHelp_"+
		locale +"/"+ wmsName + "/" + filepath +".htm";
	
		_log.debug("LOG_DEBUG_EXTENSION_WaveworkbenchHelp", "***Help URL: " +onlinehelpUrl, SuggestedCategory.NONE);	
	
		context.getServiceManager().getUserContext().put("HELPURL", onlinehelpUrl);
	
		_log.debug("LOG_DEBUG_EXTENSION_WMWPOnClickingHelp", "***Exiting WaveworkbenchHelp", SuggestedCategory.NONE);
		return RET_CONTINUE;	
	}

	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
	
}
