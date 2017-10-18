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
package com.ssaglobal.scm.wms.wm_thru_order;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ConvertFlowThruConfPopupFormPreRender extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertFlowThruConfPopupFormPreRender.class);
	public ConvertFlowThruConfPopupFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{				
		_log.debug("LOG_DEBUG_EXTENSION_CONVERTFTOPFPR","Executing ConvertFlowThruConfPopupFormPreRender",100L);		
		RuntimeFormWidgetInterface widgetLabel = form.getFormWidgetByName("POPUPLABEL");					
		Object orderKeyObj = context.getState().getRequest().getSession().getAttribute("CONVERTFTOORDERKEY");		
		_log.debug("LOG_DEBUG_EXTENSION_CONVERTFTOPFPR","Got CONVERT_FLOWTHRU_ORDERKEY:"+orderKeyObj,100L);
		if(orderKeyObj == null){
			_log.debug("LOG_DEBUG_EXTENSION_CONVERTFTOPFPR","Exiting ConvertFlowThruConfPopupFormPreRender",100L);			
			String args[] = new String[0];							
			String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);				
		}
		String orderKey = (String)orderKeyObj;		
		String[] args = new String[1];
		args[0]=orderKey;
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_FLOWTHRU_CONF",args,context.getState().getLocale()));
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_FLOWTHRU_CONF",args,context.getState().getLocale()));
		_log.debug("LOG_DEBUG_EXTENSION_CONVERTFTOPFPR","Exiting ConvertFlowThruConfPopupFormPreRender",100L);
		return RET_CONTINUE;
	}
	
}