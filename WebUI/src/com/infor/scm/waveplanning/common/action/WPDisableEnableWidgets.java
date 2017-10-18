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
package com.infor.scm.waveplanning.common.action;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;

public class WPDisableEnableWidgets extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPDisableEnableWidgets.class);
	public WPDisableEnableWidgets()
    {
    }

	protected int execute(ActionContext context, ActionResult result)
    throws UserException
    {			
		String shellForm = (String)getParameter("shellForm");
		String toolBarForm = (String)getParameter("toolBarForm");
		ArrayList widgetNames = (ArrayList) getParameter("widgetstodisable");
		ArrayList doDisableAry = (ArrayList) getParameter("dodisable");
		
		RuntimeFormInterface toolBar = WPFormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,toolBarForm,context.getState());
		
		if(toolBar == null)
			return RET_CONTINUE;
		
		for(int i = 0; i < widgetNames.size(); i++){					
			RuntimeFormWidgetInterface button = toolBar.getFormWidgetByName(widgetNames.get(i).toString());			
			
			if(button == null)
				continue;
			
			String doDisable = (String)doDisableAry.get(i);
			if(doDisable.equalsIgnoreCase("true"))
			{
				//button.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY,true);
				//button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,"true");
				System.out.println("\n\nDisable new code!:"+button.getName()+"\n\n");
				_log.debug("LOG_DEBUG_EXTENSION_DISABLEENABLEWIDGETS","Disabling:"+widgetNames.get(i),100L);				
			}
			else
			{
				button.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY,false);
				System.out.println("\n\nenable:"+button.getName()+"\n\n");
				_log.debug("LOG_DEBUG_EXTENSION_DISABLEENABLEWIDGETS","Enabling:"+widgetNames.get(i),100L);				
			}
		}
		
		return RET_CONTINUE;
	}
		
	
}