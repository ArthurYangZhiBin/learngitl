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
package com.ssaglobal.scm.wms.wm_order_bio.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;

public class MassShipFinalPopupFormPreRender extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassShipFinalPopupFormPreRender.class);
	public MassShipFinalPopupFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{					
		RuntimeFormWidgetInterface widgetLabel = form.getFormWidgetByName("POPUPLABEL");					
		Object failedItemsObj = context.getState().getRequest().getSession().getAttribute("MASSSHIPFAILEDCOUNT");
		Object failedItemsMessageObj = context.getState().getRequest().getSession().getAttribute("MASSSHIPFAILEDMESSAGE");
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPFPFPR","Got MASSSHIPFAILEDCOUNT From Session:"+failedItemsObj,100L);		
		context.getState().getRequest().getSession().removeAttribute("MASSSHIPFAILEDCOUNT");
		context.getState().getRequest().getSession().removeAttribute("MASSSHIPFAILEDMESSAGE");
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPFPFPR","Removing MASSSHIPFAILEDCOUNT From Session...",100L);
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPFPFPR","Testing MASSSHIPFAILEDCOUNT for NULL:"+failedItemsObj,100L);		
		if(failedItemsObj == null){			
			String args[] = new String[0];							
			String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);				
		}
		Integer failedItemsSize = (Integer)failedItemsObj;
		//ArrayList selectedItems = (ArrayList)selectedItemsObj;
		String[] args = new String[1];
		args[0]=failedItemsSize.toString();
		String msg = getTextMessage("WMTXT_MASSSHIPORDERS_CONF_A",args,context.getState().getLocale());
		if(failedItemsMessageObj != null){
			form.getFormWidgetByName("ERROR").setDisplayValue(failedItemsMessageObj.toString());
			//msg += "\r\n"+failedItemsMessageObj.toString(); 
		}
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,msg);
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,msg);
		
		return RET_CONTINUE;
	}
	
}