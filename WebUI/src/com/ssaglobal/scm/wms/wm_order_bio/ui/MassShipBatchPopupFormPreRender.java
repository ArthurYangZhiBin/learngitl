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

import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;

public class MassShipBatchPopupFormPreRender extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassShipBatchPopupFormPreRender.class);
	public MassShipBatchPopupFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{					
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPBPFPR","Executing MassShipBatchPopupFormPreRender",100L);		
		RuntimeFormWidgetInterface widgetLabel = form.getFormWidgetByName("POPUPLABEL");					
		Object orderKeyObj = context.getState().getRequest().getSession().getAttribute("MASSSHIPORDERKEY");
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPBPFPR","Got MASS_SHIP_ORDERKEY:"+orderKeyObj,100L);		
		context.getState().getRequest().getSession().removeAttribute("MASSSHIPORDERKEY");
		if(orderKeyObj == null){
			_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPBPFPR","Exiting MassShipBatchPopupFormPreRender",100L);
			String args[] = new String[0];							
			String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);				
		}
		String orderKey = (String)orderKeyObj;
		//ArrayList selectedItems = (ArrayList)selectedItemsObj;
		String[] args = new String[1];
		args[0]=orderKey;
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_MASSSHIPORDERS_CONF_B",args,context.getState().getLocale()));
		widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_MASSSHIPORDERS_CONF_B",args,context.getState().getLocale()));
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPBPFPR","Exiting MassShipBatchPopupFormPreRender",100L);		
		return RET_CONTINUE;
	}
	
}