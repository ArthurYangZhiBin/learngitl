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


package com.ssaglobal.scm.wms.wm_user_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.SessionIDUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UAInfoPrinter extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(UAInfoPrinter.class);

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
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {

		try {
			SessionIDUtil sessionIDUtil = new SessionIDUtil(context);
			EpnyServiceManager serviceManager = context.getServiceManager();
			EpnyUserContext userContext = serviceManager.getUserContext();
			String ssoToken = (String) userContext.getSecurityHandle();
			_log.info(	"LOG_INFO_EXTENSION_UAInfoPrinter_preRenderForm",
						sessionIDUtil.ssoTokenInfo(ssoToken),
						SuggestedCategory.NONE);

			Iterator attrItr = userContext.entrySet().iterator();
			for (; attrItr.hasNext();) {
				Map.Entry kvp = (Entry) attrItr.next();
				_log.info("UserContext", kvp.getKey() + " " + kvp.getValue(), SuggestedCategory.NONE);
			}
			HttpSession session = context.getState().getRequest().getSession();
			_log.info(	"LOG_INFO_EXTENSION_UAInfoPrinter_preRenderForm",
						"Session " + session.getId(),
						SuggestedCategory.NONE);
			_log.info(	"LOG_INFO_EXTENSION_UAInfoPrinter_preRenderForm",
						"Session Last Accessed " + session.getLastAccessedTime(),
						SuggestedCategory.NONE);
			_log.info(	"LOG_INFO_EXTENSION_UAInfoPrinter_preRenderForm",
						"Session Inactive Interval" + session.getMaxInactiveInterval(),
						SuggestedCategory.NONE);
			_log.info("LOG_INFO_EXTENSION_UAInfoPrinter_preRenderForm", "Session Attributes", SuggestedCategory.NONE);
			Enumeration attributeNames = session.getAttributeNames();
			// for (; attributeNames.hasMoreElements();) {
			// Object nextElement = attributeNames.nextElement();
			// if (nextElement instanceof String) {
			// try {
			// _log.info( "Session",
			// (String) nextElement + " " + session.getAttribute((String) nextElement),
			// SuggestedCategory.NONE);
			// } catch (IllegalArgumentException e) {
			//
			// }
			// }
			// }
			_log.info("Roles", "Roles for Current User", SuggestedCategory.NONE);
			String[] allRolesForCurrentUser = context.getState().getServiceManager().getAllRolesForCurrentUser();
			for (String s : allRolesForCurrentUser) {
				_log.info("Roles", "\t" + s, SuggestedCategory.NONE);
			}

		} catch (Exception e) {

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
