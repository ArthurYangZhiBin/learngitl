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

package com.ssaglobal.scm.wms.wm_view_inventory_hold.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes


import java.util.Calendar;

import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.SessionUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ViewInventoryRestoreFilter extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	private static final String _VIEWHOLD = "VIEWHOLD";
	
	/**
	 * Called in response to the pre-render event on a form. Write code to customize the properties of a form. All code
	 * that initializes the properties of a form that is being displayed to a user for the first time belong here. This
	 * is not executed even if the form is re-displayed to the end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
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
			for (java.util.Iterator<RuntimeFormWidgetInterface> it = form.getFormWidgets(); it.hasNext();) {
				RuntimeFormWidgetInterface widget = it.next();
				
				if (widget.getName().endsWith("START") || widget.getName().endsWith("END")) {
					// retrieve value from session

					Object attribute = SessionUtil.getInteractionSessionAttribute(_VIEWHOLD + widget.getName(), state);

					if (attribute instanceof Calendar) {
						widget.setCalendarValue((Calendar) attribute);
					} else if (attribute instanceof String) {
						widget.setValue((String) attribute);
					} else {
						// do nothing
					}

				}
				
			}
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

	/**
	 * Called in response to the pre-render event on a form in a modal window. Write code to customize the properties of
	 * a form. This code is re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {

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
	 * Called in response to the modifySubSlot event on a form. Write code to change the contents of the slots in this
	 * form. This code is re-executed everytime irrespective of whether the form is re-displayed to the user or not.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException {

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
	 * Called in response to the modifySubSlot event on a form in a modal window. Write code to change the contents of
	 * the slots in this form. This code is re-executed everytime irrespective of whether the form is re-displayed to
	 * the user or not.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException {

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
	 * Called in response to the setFocusInForm event on a form. Write code to change the focus of this form. This code
	 * is executed everytime irrespective of whether the form is being redisplayed or not.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form) throws EpiException {

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
	 * Called in response to the setFocusInForm event on a form in a modal window. Write code to change the focus of
	 * this form. This code is executed everytime irrespective of whether the form is being redisplayed or not.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form) throws EpiException {

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
	 * Called in response to the pre-render event on a list form. Write code to customize the properties of a list form
	 * dynamically, change the bio collection being displayed in the form or filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {

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
	 * Called in response to the pre-render event on a list form in a modal dialog. Write code to customize the
	 * properties of a list form dynamically, change the bio collection being displayed in the form or filter the bio
	 * collection
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException {

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
	 * Called in response to the modifyListValues event on a list form. Subclasses must override this in order to
	 * customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {

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
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses must override this
	 * in order to customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException {

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
