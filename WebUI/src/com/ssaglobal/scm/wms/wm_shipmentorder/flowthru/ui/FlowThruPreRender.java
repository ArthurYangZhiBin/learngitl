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

package com.ssaglobal.scm.wms.wm_shipmentorder.flowthru.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.List;

import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserContextUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class FlowThruPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(FlowThruPreRender.class);

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderForm(UIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event
			StateInterface state = context.getState();
			boolean isFlowThruScreen = determineIsFlowThruScreen(state);

			String formName = form.getName();
			if ("wm_list_shell_shipmentorder Toolbar".equals(formName)) {
				handleShellListToolbar(isFlowThruScreen, form, context);
			} else if ("wm_shipmentorder_header_view".equals(formName)) {
				handleSOHeaderTab(isFlowThruScreen, form, context);
			} else if ("wm_shipmentorder_loading_view".equals(formName)) {
				handleSOLoadTab(isFlowThruScreen, form, context);
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void handleSOLoadTab(boolean isFlowThruScreen,
			RuntimeNormalFormInterface form, UIRenderContext context) {
		RuntimeFormWidgetInterface containerId = form
				.getFormWidgetByName("CONTAINERID");
		RuntimeFormWidgetInterface receiptKey = form
				.getFormWidgetByName("RECEIPTKEY");
		RuntimeFormWidgetInterface poKey = form.getFormWidgetByName("POKEY");
		RuntimeFormWidgetInterface apportion = form
				.getFormWidgetByName("APPORTION");
		if (isFlowThruScreen == false) {
			// disable fields
			containerId.setBooleanProperty(
					RuntimeFormWidgetInterface.PROP_READONLY, true);
			receiptKey.setBooleanProperty(
					RuntimeFormWidgetInterface.PROP_READONLY, true);
			poKey.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY,
					true);
			apportion.setBooleanProperty(
					RuntimeFormWidgetInterface.PROP_READONLY, true);

		}

	}

	/**
	 * Handle so header tab.
	 * 
	 * @param isFlowThruScreen
	 *            the is flow thru screen
	 * @param form
	 *            the form
	 * @param context
	 *            the context
	 * @throws EpiException
	 */
	private void handleSOHeaderTab(boolean isFlowThruScreen,
			RuntimeNormalFormInterface form, UIRenderContext context)
			throws EpiException {
		RuntimeFormWidgetInterface poKey = form.getFormWidgetByName("POKEY");
		RuntimeFormWidgetInterface consigneeKey = form
				.getFormWidgetByName("CONSIGNEEKEY");
		RuntimeFormWidgetInterface apportion = form
				.getFormWidgetByName("APPORTION");
		if (isFlowThruScreen == true) {
			if (form.getFocus().isTempBio()) {
				// new record
				DataBean focus = form.getFocus();
				focus.setValue("TYPE", "91");
				form.getFormWidgetByName("TYPE").setBooleanProperty(
						RuntimeFormWidgetInterface.PROP_READONLY, true);

				// set required
				FormUtil.setWidgetAsRequired(poKey);
				FormUtil.setWidgetAsRequired(consigneeKey);
				//SRG: Defect# 528: Start: Set apportion flag to "1" for flow thru orders
				focus.setValue("APPORTION", "1");
				//SRG: Defect# 528: End

			}
		} else {
			poKey.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY,
					true);
			consigneeKey.setBooleanProperty(
					RuntimeFormWidgetInterface.PROP_READONLY, true);
			apportion.setBooleanProperty(
					RuntimeFormWidgetInterface.PROP_READONLY, true);

			// if(form.getFocus().isTempBio())
			// {
			// //need to remove 91 from DropDown
			// RuntimeFormWidgetInterface typeDropdown =
			// form.getFormWidgetByName("TYPE");
			// List[] valuesAndLabels =
			// typeDropdown.getDropdownContents().getValuesAndLabels(typeDropdown.getDropdownContext());
			// if (valuesAndLabels[0].contains("91") == true)
			// {
			// int indexOf = valuesAndLabels[0].indexOf("91");
			// valuesAndLabels[0].remove(indexOf);
			// valuesAndLabels[1].remove(indexOf);
			// }
			// }
		}

	}

	public static boolean determineIsFlowThruScreen(StateInterface state) {
		boolean isFlowThruScreen = false;
		Object isFlowThruScreenObject = UserContextUtil
				.getInteractionUserContextAttribute(
						FlowThruToggle.IS_FLOW_THRU_SCREEN, state);
		log.info("FlowThruPreRender_preRenderForm", "Is Flow Thru Screen "
				+ isFlowThruScreen, SuggestedCategory.APP_EXTENSION);
		if (isFlowThruScreenObject == null) {
			isFlowThruScreen = false;
		} else {
			isFlowThruScreen = (Boolean) isFlowThruScreenObject;
		}
		return isFlowThruScreen;
	}

	private void handleShellListToolbar(boolean isFlowThruScreen,
			RuntimeNormalFormInterface form, UIRenderContext context) {

		RuntimeFormWidgetInterface captionWidget = form
				.getFormWidgetByName("wm_list_shell_shipmentorder Toolbar");
		if (isFlowThruScreen == true) {
			String label = getTextMessage("WMLBL_FLOW_THRU_ORDER",
					new Object[] {}, context.getState().getLocale());
			captionWidget.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,
					label);
		} else {
			String label = getTextMessage("WMLBL_SO", new Object[] {}, context
					.getState().getLocale());
			captionWidget.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,
					label);
		}

	}

	/**
	 * Called in response to the pre-render event on a form in a modal window.
	 * Write code to customize the properties of a form. This code is
	 * re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderForm(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifySubSlot event on a form. Write code to
	 * change the contents of the slots in this form. This code is re-executed
	 * everytime irrespective of whether the form is re-displayed to the user or
	 * not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int modifySubSlots(UIRenderContext context,
			RuntimeFormExtendedInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifySubSlot event on a form in a modal
	 * window. Write code to change the contents of the slots in this form. This
	 * code is re-executed everytime irrespective of whether the form is
	 * re-displayed to the user or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int modifySubSlots(ModalUIRenderContext context,
			RuntimeFormExtendedInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the setFocusInForm event on a form. Write code to
	 * change the focus of this form. This code is executed everytime
	 * irrespective of whether the form is being redisplayed or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int setFocusInForm(UIRenderContext context,
			RuntimeFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the setFocusInForm event on a form in a modal
	 * window. Write code to change the focus of this form. This code is
	 * executed everytime irrespective of whether the form is being redisplayed
	 * or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int setFocusInForm(ModalUIRenderContext context,
			RuntimeFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form. Write code to
	 * customize the properties of a list form dynamically, change the bio
	 * collection being displayed in the form or filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderListForm(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form in a modal
	 * dialog. Write code to customize the properties of a list form
	 * dynamically, change the bio collection being displayed in the form or
	 * filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderListForm(ModalUIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form.
	 * Subclasses must override this in order to customize the display values of
	 * a list form
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int modifyListValues(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form in a
	 * modal dialog. Subclasses must override this in order to customize the
	 * display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int modifyListValues(ModalUIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
