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
package com.ssaglobal.scm.wms.wm_routing.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_releasecyclecount.ui.PreSaveReleaseCycleCount;

public class GetPKValOnPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(GetPKValOnPreRender.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws EpiException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing GetPKValOnPreRender",100L);
		_log.debug("LOG_SYSTEM_OUT","\n\n******* In GetPKValOnPreRender ",100L);
		
   			String toField = getParameter("ToWidget").toString();
   			String fromField = getParameter("FromWidget").toString();
   			//String headerOrDetail = getParameter("HeaderOrDetail").toString();
   			
   
		DataBean focus = form.getFocus();
		if(focus instanceof QBEBioBean)
		{
			focus= (QBEBioBean)focus;
		}
		
		if(focus.isTempBio())
		{
		String fromVal = (String)focus.getValue(fromField);
		focus.setValue(toField, fromVal);
		}
	
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting GetPKValOnPreRender",100L);
		return RET_CONTINUE;
	}
}
