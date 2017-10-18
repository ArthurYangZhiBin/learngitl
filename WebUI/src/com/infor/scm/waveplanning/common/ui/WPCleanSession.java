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
package com.infor.scm.waveplanning.common.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WPCleanSession extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPCleanSession.class);
	public WPCleanSession()
    {
    }

	protected int execute(ActionContext context, ActionResult result)
    throws UserException
    {					
		_log.debug("LOG_DEBUG_EXTENSION_WPCleanSession","Executing CleanSession",100L);		
		ArrayList sessionAttributeNames = (ArrayList) getParameter("sessionAttributeNames");		
			
		for(int i = 0; i < sessionAttributeNames.size(); i++){						
			_log.debug("LOG_DEBUG_EXTENSION_WPCleanSession","Removing Attribute:"+sessionAttributeNames.get(i),100L);
			context.getState().getRequest().getSession().removeAttribute(sessionAttributeNames.get(i).toString());
		}
		_log.debug("LOG_DEBUG_EXTENSION_WPCleanSession","Exiting WPCleanSession",100L);				
		return RET_CONTINUE;
	}
		
	
}