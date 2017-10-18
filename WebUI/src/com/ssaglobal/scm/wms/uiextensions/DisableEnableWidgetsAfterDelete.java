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
package com.ssaglobal.scm.wms.uiextensions;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class DisableEnableWidgetsAfterDelete extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableEnableWidgetsAfterDelete.class);
	public DisableEnableWidgetsAfterDelete()
    {
    }

	protected int execute(ActionContext context, ActionResult result)
    throws UserException
    {			
		String shellForm = (String)getParameter("shellForm");
		String toolBarForm = (String)getParameter("toolBarForm");
		String detailForm = (String)getParameter("detailForm");
		ArrayList widgetNames = (ArrayList) getParameter("widgetstodisable");
		ArrayList doDisableAry = (ArrayList) getParameter("dodisable");
		
		RuntimeFormInterface detail = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,detailForm,context.getState());
		
		if(detail != null && detail.getFocus() != null && !detail.getFocus().isTempBio() && detail.getFocus().isBio()){
			BioBean focus = (BioBean)detail.getFocus();
			if(!focus.isDeleted()){
				return RET_CONTINUE;
			}
		}
		
		RuntimeFormInterface toolBar = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,toolBarForm,context.getState());
		
		if(toolBar == null)
			return RET_CONTINUE;
		
		for(int i = 0; i < widgetNames.size(); i++){					
			RuntimeFormWidgetInterface button = toolBar.getFormWidgetByName(widgetNames.get(i).toString());			
			
			if(button == null)
				continue;
			
			String doDisable = (String)doDisableAry.get(i);
			if(doDisable.equalsIgnoreCase("true"))
			{
				button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				_log.debug("LOG_DEBUG_EXTENSION_DISABLEENABLEWIDGETS","Disabling:"+widgetNames.get(i),100L);				
			}
			else
			{
				button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				_log.debug("LOG_DEBUG_EXTENSION_DISABLEENABLEWIDGETS","Enabling:"+widgetNames.get(i),100L);				
			}
		}
		
		return RET_CONTINUE;
	}
		
	
}