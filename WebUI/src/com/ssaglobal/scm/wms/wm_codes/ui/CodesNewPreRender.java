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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CodesNewPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 *             {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *             service
	 */
	private static final String IS_ENTERPRISE_CODE = "1";
	protected int preRenderForm(UIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			EpnyControllerState state = (EpnyControllerState) context.getState();
			HttpSession session = state.getRequest().getSession();

			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
			String isEnterprise = null;
			try
			{
				isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			} catch (java.lang.NullPointerException e)
			{
				isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			}

			
			
//			StateInterface state = context.getState();
			RuntimeFormInterface currentRuntimeForm = state
					.getCurrentRuntimeForm();
			RuntimeFormInterface codesHeader = FormUtil.findForm(
					currentRuntimeForm, "", "wm_codes_detail_view", state);
			DataBean focus = codesHeader.getFocus();
			String editable = BioAttributeUtil.getString(focus, "EDITABLE");
			if ("0".equals(editable)) {
				currentRuntimeForm.getFormWidgetByName("NEW")
						.setBooleanProperty(
								RuntimeFormWidgetInterface.PROP_READONLY, true);
			}


			
			
			
			Object enterpriseObj = focus.getValue("ENTERPRISECODE");
			if (!isEnterprise.equals("1")){//only for warehouse user
				if(enterpriseObj != null){
					if(IS_ENTERPRISE_CODE.equalsIgnoreCase(enterpriseObj.toString())){
						currentRuntimeForm.getFormWidgetByName("NEW")
						.setBooleanProperty(
								RuntimeFormWidgetInterface.PROP_READONLY, true);
						currentRuntimeForm.getFormWidgetByName("DELETE")
						.setBooleanProperty(
								RuntimeFormWidgetInterface.PROP_READONLY, true);
						currentRuntimeForm.getFormWidgetByName("RESET")
						.setBooleanProperty(
								RuntimeFormWidgetInterface.PROP_READONLY, true);
					}
				}
			}

			
		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
