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

package com.ssaglobal.scm.wms.wm_check_pack.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;

import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackUtil.Dimension;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackUtil.SPSInfo;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackResultsFormPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackResultsFormPreRender.class);

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the
	 * properties of a form that is
	 * being displayed to a user for the first time belong here. This is not
	 * executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException {

		try {

			// Add your code here to process the event
			StateInterface state = context.getState();
			RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_check_pack_shell", "wm_check_pack_search_form", state);
			RuntimeFormWidgetInterface containerWidget = searchForm.getFormWidgetByName("CONTAINER");
			String container = containerWidget.getDisplayValue().toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Searching for " + container, SuggestedCategory.NONE);
			CheckPackResults results = CheckPackUtil.search(context, container);
			Dimension cartonDimension = CheckPackUtil.getCartonDimensions(context, results);
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", cartonDimension.toString(),
					SuggestedCategory.NONE);
			Dimension contentsDimensions = CheckPackUtil.getContentsDimensions(context, results);
			Double totalWeight = cartonDimension.getWeight() + contentsDimensions.getWeight();
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Weight " + totalWeight, SuggestedCategory.NONE);

			String order = CheckPackUtil.getOrder(results);
			String carrier = CheckPackUtil.getCarrier(context, order);
			SPSInfo spsInfo = CheckPackUtil.getSPSInfo(context, carrier);
			String notes = CheckPackUtil.getNotes(context, order);

			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Order " + order, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Carrier/Svc " + spsInfo.getScacCode() + " " + spsInfo.getSpsType(), SuggestedCategory.NONE);

			NumberFormat qtyFormat = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
			NumberFormat curFormat = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0);

			//update form
			form.getFormWidgetByName("HEIGHT").setValue(qtyFormat.format(cartonDimension.getHeight()));
			//needed for oa trickery
			form.getFormWidgetByName("HEIGHT").setDisplayValue(qtyFormat.format(cartonDimension.getHeight()));
			//
			form.getFormWidgetByName("WIDTH").setValue(qtyFormat.format(cartonDimension.getWidth()));
			form.getFormWidgetByName("LENGTH").setValue(qtyFormat.format(cartonDimension.getLength()));

			if (spsInfo.getActWgtOrSystemWgt() == 1) {
				form.getFormWidgetByName("WEIGHT").setValue(qtyFormat.format(0.0));
			} else {
				form.getFormWidgetByName("WEIGHT").setValue(qtyFormat.format(totalWeight));
			}
			form.getFormWidgetByName("ORDER").setValue(order);
			form.getFormWidgetByName("CARRIER").setValue(spsInfo.getScacCode());
			form.getFormWidgetByName("SERVICES").setValue(spsInfo.getSpsType());
			form.getFormWidgetByName("NOTES").setValue(notes);

			//Defaults
			form.getFormWidgetByName("CONTAINERPACKINGLIST").setValue("0");
			form.getFormWidgetByName("MASTERPACKINGLIST").setValue("0");
			form.getFormWidgetByName("NUM_LABELS").setValue("1");

			//Stored Procedure
			form.getFormWidgetByName("TYPE").setValue(results.getType().toString());
		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a form in a modal window.
	 * Write code
	 * to customize the properties of a form. This code is re-executed everytime
	 * a form is redisplayed
	 * to the end user
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException {

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
	 * Called in response to the modifySubSlot event on a form. Write code
	 * to change the contents of the slots in this form. This code is
	 * re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
			throws EpiException {

		try {
			//			StateInterface state = context.getState();
			//			RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_check_pack_shell", "wm_check_pack_search_form", state);
			//			RuntimeFormWidgetInterface containerWidget = searchForm.getFormWidgetByName("CONTAINER");
			//			String container = containerWidget.getDisplayValue().toUpperCase();
			//			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Searching for " + container, SuggestedCategory.NONE);
			//			CheckPackResults results = CheckPackUtil.search(context, container);
			//			String name = form.getName();
			//			RuntimeFormInterface parentForm = form.getParentForm(state);
			//			SlotInterface subSlot = parentForm.getSubSlot("slot3");
			//
			//			((RuntimeFormExtendedInterface) parentForm).setFocus(state, subSlot, "", results.getPickDetails(), "wm_check_pack_results_pickdetail_form");

		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifySubSlot event on a form in a modal
	 * window. Write code
	 * to change the contents of the slots in this form. This code is
	 * re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form)
			throws EpiException {

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
	 * Called in response to the setFocusInForm event on a form. Write code
	 * to change the focus of this form. This code is executed everytime
	 * irrespective of whether the form
	 * is being redisplayed or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form)
			throws EpiException {

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
	 * window. Write code
	 * to change the focus of this form. This code is executed everytime
	 * irrespective of whether the form
	 * is being redisplayed or not.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form)
			throws EpiException {

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
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio
	 * collection being displayed
	 * in the form or filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException {

		try {
			//			StateInterface state = context.getState();
			//			RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_check_pack_shell", "wm_check_pack_search_form", state);
			//			RuntimeFormWidgetInterface containerWidget = searchForm.getFormWidgetByName("CONTAINER");
			//			String container = containerWidget.getDisplayValue().toUpperCase();
			//			_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Searching for " + container, SuggestedCategory.NONE);
			//			CheckPackResults results = CheckPackUtil.search(context, container);
			//			form.setFocus(results.getPickDetails());
		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form in a modal
	 * dialog. Write code
	 * to customize the properties of a list form dynamically, change the bio
	 * collection being displayed
	 * in the form or filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form)
			throws EpiException {

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
	 * Subclasses must override this in order
	 * to customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException {

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
	 * modal dialog. Subclasses must override this in order
	 * to customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form)
			throws EpiException {

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
