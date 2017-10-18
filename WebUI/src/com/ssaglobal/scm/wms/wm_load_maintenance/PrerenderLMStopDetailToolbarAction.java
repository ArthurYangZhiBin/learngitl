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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class PrerenderLMStopDetailToolbarAction extends FormExtensionBase{	

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrerenderLMStopDetailToolbarAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_PRERENLMSTOPTOOLBAR","Executing PrerenderLMStopDetailToolbarAction",100L);			
		StateInterface state = context.getState();
//		Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_detail_form",state);		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_PRERENLMSTOPTOOLBAR","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_PRERENLMSTOPTOOLBAR","Found Header Form:Null",100L);			
//		Get Header and Detail Forms
		if(headerForm != null && headerForm.getFocus().isTempBio())
			form.getFormWidgetByName("Return to List View").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
		else
			form.getFormWidgetByName("Return to List View").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
		_log.debug("LOG_DEBUG_EXTENSION_PRERENLMSTOPTOOLBAR","Exiting PrerenderLMStopDetailToolbarAction",100L);		
		return RET_CONTINUE;
	}	
}