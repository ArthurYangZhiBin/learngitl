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
import com.epiphany.shr.metadata.objects.Navigator;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
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

public class FlowThruToggle extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	public static final String IS_FLOW_THRU_SCREEN = "FLOWTHRUSCREEN";

	private int determineIfFlowThruOrder(UIRenderContext context) {
		StateInterface state = context.getState();
		boolean flowThruToggle = false;
		try {
			// Add your code here to process the event
			ActionObjectInterface actionObject = context.getActionObject();
			if (actionObject != null
					&& ActionObjectImpl.MENU_ITEM_TYPE == context
							.getActionObject().getType()
					&& ("wm_crossdock_management_menu".equals(actionObject
							.getContainerName()) || "wm_warehouse_documents_menu"
							.equals(actionObject.getContainerName()))) {
				if ("wm_crossdock_management_flowthruorder_so_menuitem"
						.equals(actionObject.getName())) {
					flowThruToggle = true;
				}

				if (flowThruToggle == true) {
					UserContextUtil.setInteractionUserContextAttribute(
							IS_FLOW_THRU_SCREEN, true, state);
				} else {
					UserContextUtil.setInteractionUserContextAttribute(
							IS_FLOW_THRU_SCREEN, false, state);
				}
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epiphany.shr.ui.view.customization.FormExtensionBase#preRenderForm
	 * (com.epiphany.shr.ui.action.UIRenderContext,
	 * com.epiphany.shr.ui.view.RuntimeNormalFormInterface)
	 */
	@Override
	protected int preRenderForm(UIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {
		return determineIfFlowThruOrder(context);
	}

	/**
	 * Pre render list form.
	 * 
	 * @param context
	 *            the context
	 * @param form
	 *            the form
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderListForm(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {
		return determineIfFlowThruOrder(context);
	}

}
