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


package com.ssaglobal.scm.wms.util.upload;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class SetTemplateIdGeneric extends com.epiphany.shr.ui.action.ActionExtensionBase {


	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetTemplateIdGeneric.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

	   StateInterface state = context.getState();
	   RuntimeFormInterface form = state.getCurrentRuntimeForm();
	   
	   _log.debug("LOG_SYSTEM_OUT","[SetTemplateIdGeneric]\tform:"+form.getName(),100L);
	   
	   String templateId = (String) form.getFormWidgetByName("TEMPLATEID").getValue();
	   
	   HttpSession session = state.getRequest().getSession();
	   
	   session.setAttribute("TEMPLATEID",templateId);
	   
	   _log.debug("LOG_SYSTEM_OUT","[SetTemplateIdGeneric]\ttemplateId"+templateId,100L);
      
	   return RET_CONTINUE;
   }
   
}
