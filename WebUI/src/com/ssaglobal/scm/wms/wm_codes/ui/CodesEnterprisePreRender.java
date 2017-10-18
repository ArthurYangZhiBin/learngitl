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
package com.ssaglobal.scm.wms.wm_codes.ui;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * TODO Document CodesEnterprisePreRender class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class CodesEnterprisePreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CodesEnterprisePreRender.class);
	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	private static final String IS_ENTERPRISE_CODE = "1";
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
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
		DataBean focus = form.getFocus();
		Object enterpriseObj = focus.getValue("ENTERPRISECODE");
		if (!isEnterprise.equals("1") && !focus.isTempBio()){//only for warehouse user and not from new button
			if(enterpriseObj != null){
				if(IS_ENTERPRISE_CODE.equalsIgnoreCase(enterpriseObj.toString())){
					form.getFormWidgetByName("DESCRIPTION").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					form.getFormWidgetByName("LANGUAGE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					form.getFormWidgetByName("LISTNAME").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					form.getFormWidgetByName("PARENTCODE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					form.getFormWidgetByName("PARENTLIST").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
//					form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, true);

				}else{
					form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);					
				}
			}
		}
		
		return RET_CONTINUE;
	}
	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{
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
		RuntimeFormInterface headerForm = FormUtil.findForm(form, "wm_list_shell", "wm_codes_detail_view", state);
		DataBean focus = headerForm.getFocus();
		
		RuntimeFormInterface toggleToolbar = FormUtil.findForm(form, "wm_codesdetail_toggle_slot", "wm_codesdetail_toggle_slot Toolbar", state);
		Object enterpriseObj = focus.getValue("ENTERPRISECODE");
		if (!isEnterprise.equals("1")){//only for warehouse user
			if(enterpriseObj != null){
				if(IS_ENTERPRISE_CODE.equalsIgnoreCase(enterpriseObj.toString())){
					toggleToolbar.getFormWidgetByName("NEW").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					toggleToolbar.getFormWidgetByName("DELETE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, true);
				}else{
					toggleToolbar.getFormWidgetByName("NEW").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					toggleToolbar.getFormWidgetByName("DELETE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);
					
				}
			}
		}
		
		return RET_CONTINUE;
	}
}
