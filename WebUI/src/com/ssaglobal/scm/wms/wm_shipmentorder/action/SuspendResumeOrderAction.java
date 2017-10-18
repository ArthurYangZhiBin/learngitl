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

package com.ssaglobal.scm.wms.wm_shipmentorder.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SuspendResumeOrderAction extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String _SUSPEND_OFF_MENU = "SuspendOff";

	private static final String _SUSPEND_ON_MENU = "SuspendOn";

	private static final String NAV_MULTIPLE = "multipleSuspend";

	private static final String NAV_SINGLE = "singleSuspend";

	private static final String _PK_ORDERKEY = "ORDERKEY";

	private static final String _SUSPEND_OFF = "0";

	private static final String _SUSPEND_ON = "1";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SuspendResumeOrderAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		// 2 questions to ask

		// 1. suspend or resume?
		// 2. list or detail in slot 1?

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String sourceWidget = context.getActionObject().getName();

		if (_SUSPEND_ON_MENU.equalsIgnoreCase(sourceWidget)) {
			// Suspend
			if (isSlot1AListForm(context, state)) {

				RuntimeFormInterface slot1Form = state.getRuntimeForm(	context.getSourceWidget().getForm().getParentForm(state).getSubSlot("list_slot_1"),
																		null);
				RuntimeListFormInterface listForm = (RuntimeListFormInterface) slot1Form;
				ArrayList<BioBean> allSelectedOrders = listForm.getAllSelectedItems();
				if (allSelectedOrders != null) {
					ArrayList<String> orderKeys = new ArrayList<String>();
					for (BioBean selectedOrder : allSelectedOrders) {
						orderKeys.add(BioAttributeUtil.getString(selectedOrder, _PK_ORDERKEY));
					}

					String query = generateQuery(orderKeys);
					Query orderKeyQuery = new Query("wm_orders", query, null);
					updateOrders(uow, orderKeyQuery, _SUSPEND_ON);
					listForm.setSelectedItems(null);
				}
				// Set Navigation to List Nav
				context.setNavigation(NAV_MULTIPLE);

			} else {
				// Suspend Order
				// Get Focus
				String orderKey = getOrderKeyFromNormalForm(context, state);
				// Query to get Order Bio
				Query orderKeyQuery = new Query("wm_orders", "wm_orders.ORDERKEY = '" + orderKey + "'", null);
				updateOrders(uow, orderKeyQuery, _SUSPEND_ON);
				// Set Navigation to Normal Nav
				context.setNavigation(NAV_SINGLE);
			}
		} else if (_SUSPEND_OFF_MENU.equalsIgnoreCase(sourceWidget)) {
			// Resume
			if (isSlot1AListForm(context, state)) {

				RuntimeFormInterface slot1Form = state.getRuntimeForm(	context.getSourceWidget().getForm().getParentForm(state).getSubSlot("list_slot_1"),
																		null);
				RuntimeListFormInterface listForm = (RuntimeListFormInterface) slot1Form;
				ArrayList<BioBean> allSelectedOrders = listForm.getAllSelectedItems();
				if (allSelectedOrders != null) {
					ArrayList<String> orderKeys = new ArrayList<String>();
					for (BioBean selectedOrder : allSelectedOrders) {
						orderKeys.add(BioAttributeUtil.getString(selectedOrder, _PK_ORDERKEY));
					}

					String query = generateQuery(orderKeys);
					Query orderKeyQuery = new Query("wm_orders", query, null);
					updateOrders(uow, orderKeyQuery, _SUSPEND_OFF);
					listForm.setSelectedItems(null);
				}
				// Set Navigation to List Nav
				context.setNavigation(NAV_MULTIPLE);

			} else {
				String orderKey = getOrderKeyFromNormalForm(context, state);
				Query orderKeyQuery = new Query("wm_orders", "wm_orders.ORDERKEY = '" + orderKey + "'", null);
				updateOrders(uow, orderKeyQuery, _SUSPEND_OFF);
				context.setNavigation(NAV_SINGLE);
			}
		} else {
			// Do Nothing
			return RET_CANCEL;
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private String generateQuery(ArrayList<String> orderKeys) {
		String query = "";
		for (int i = 0; i < orderKeys.size(); i++) {
			if (i > 0) {
				query += " OR ";
			}
			query += " wm_orders.ORDERKEY = '" + orderKeys.get(i) + "' ";
		}
		return query;
	}

	private void updateOrders(UnitOfWorkBean uow, Query orderKeyQuery, String suspendValue) throws EpiDataException, UserException, EpiException {
		BioCollectionBean rs = uow.getBioCollectionBean(orderKeyQuery);
		for (int i = 0; i < rs.size(); i++) {
			BioBean order = rs.get("" + i);
			order.setValue("SuspendedIndicator", suspendValue);
			order.save();
		}
		try {
			uow.saveUOW();
		} catch (EpiException e) {
			e.printStackTrace();
			Throwable deepestNestedException = e.findDeepestNestedException();
			if (deepestNestedException instanceof ServiceObjectException) {
				String reasonCode = deepestNestedException.getMessage();
				_log.error("LOG_ERROR_EXTENSION_SuspendOrders_execute", reasonCode, SuggestedCategory.NONE);
				throw new UserException(reasonCode, new Object[] {});
			} else {
				_log.error("LOG_ERROR_EXTENSION_SuspendOrders_execute", e.getMessage(), SuggestedCategory.NONE);
				throw e;
			}

		}
	}

	private String getOrderKeyFromNormalForm(ActionContext context, StateInterface state) throws UserException {
		RuntimeFormInterface slot1Form = state.getRuntimeForm(	context.getSourceWidget().getForm().getParentForm(state).getSubSlot("list_slot_1"),
																null);
		DataBean focus = slot1Form.getFocus();
		if (focus.isTempBio()) {
			throw new UserException("WMEXP_SO_SUSPEND_NEW", new Object[] {});
		}
		// Get Orderkey
		String orderKey = BioAttributeUtil.getString(focus, _PK_ORDERKEY);
		return orderKey;
	}

	private boolean isSlot1AListForm(ActionContext context, StateInterface state) {
		RuntimeFormInterface slot1Form = state.getRuntimeForm(	context.getSourceWidget().getForm().getParentForm(state).getSubSlot("list_slot_1"),
																null);
		if (slot1Form.isListForm()) {
			return true;
		} else {
			return false;
		}
	}

}
