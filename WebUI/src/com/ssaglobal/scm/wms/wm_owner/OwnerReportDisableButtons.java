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
package com.ssaglobal.scm.wms.wm_owner;

import java.util.Iterator;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.formextensions.CheckPermissionAndDisableWidgets;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class OwnerReportDisableButtons extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPermissionAndDisableWidgets.class);
/*	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","***** in list form*****",100L);
		StateInterface state = context.getState();
		String dbConn = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
				
		if(dbConn.equalsIgnoreCase("enterprise")){
			Iterator widgetItr = form.getFormWidgets();
			while(widgetItr.hasNext()){
				RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}
*/	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {
		StateInterface state = context.getState();
		String dbConn = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
				
		if(dbConn.equalsIgnoreCase("enterprise")){
			Iterator widgetItr = form.getFormWidgets();
			while(widgetItr.hasNext()){
				RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
