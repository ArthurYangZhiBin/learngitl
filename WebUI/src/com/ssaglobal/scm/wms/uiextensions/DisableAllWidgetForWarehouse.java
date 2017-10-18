/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.uiextensions;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * TODO Document DisableAllWidgetForWarehouse class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class DisableAllWidgetForWarehouse extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableAllWidgetForWarehouse.class);
	public DisableAllWidgetForWarehouse()
    {
    }
	private static final String ENTERPRISE = "1";
	protected int execute(ActionContext context, ActionResult result)
    throws UserException
    {			
		String shellForm = (String)getParameter("shellForm");
		String toolBarForm = (String)getParameter("toolBarForm");
		ArrayList widgetNames = (ArrayList) getParameter("widgetstodisable");
		ArrayList doDisableAry = (ArrayList) getParameter("dodisable");
		
		
		EpnyControllerState state = (EpnyControllerState) context.getState();
		HttpSession session = state.getRequest().getSession();

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String isEnterprise = null;
		try
		{
			isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility: " + isEnterprise, SuggestedCategory.NONE);
		} catch (java.lang.NullPointerException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Getting Facility from Session Instead", SuggestedCategory.NONE);
			isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility Session: " + isEnterprise, SuggestedCategory.NONE);
		}

		
		
		RuntimeFormInterface toolBar = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,toolBarForm,context.getState());
		
		if(toolBar == null)
			return RET_CONTINUE;
		
		if(ENTERPRISE.equalsIgnoreCase(isEnterprise)){
			for(int i = 0; i < widgetNames.size(); i++){					
				RuntimeFormWidgetInterface button = toolBar.getFormWidgetByName(widgetNames.get(i).toString());			
				
				if(button == null)
					continue;
				
				String doDisable = (String)doDisableAry.get(i);
				if(doDisable.equalsIgnoreCase("true"))
				{
					button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					_log.debug("LOG_DEBUG_EXTENSION_DisableAllWidgetForWarehouse","Disabling:"+widgetNames.get(i),100L);				
				}
				else
				{
					button.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					_log.debug("LOG_DEBUG_EXTENSION_DisableAllWidgetForWarehouse","Enabling:"+widgetNames.get(i),100L);				
				}
			}
		}
		
		return RET_CONTINUE;
	}
}
