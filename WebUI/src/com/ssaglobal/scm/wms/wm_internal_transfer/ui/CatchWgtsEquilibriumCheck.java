/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/

package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CatchWgtsEquilibriumCheck extends
		com.epiphany.shr.ui.action.ActionExtensionBase {
//	A new class written as part of 3PL enhancements - Catchweights
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		
		/*
		 * This is written as part of 3 PL WM913 enhancements.
		 * Phani S Dec 04 2009.
		 * This is called while saving Internal Transfer record.
		 */

		System.out.println("%%%%%%%%%%%%%%%%%%%");
		System.out.println("CATCH WGT EQUILIBRIUM CHECK");
		
		
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	/*	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
	    RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		System.out.println("DFDFDF" + headerForm.getName());	
		DataBean headerFocus = headerForm.getFocus();
		String ownerVal = (String) headerFocus.getValue("FROMSTORERKEY");
		
	   	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null); */
		
		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 0");
		RuntimeFormInterface headerForm = FormUtil.findForm(toolbar,
				"wms_list_shell", "wm_internal_transfer_detail_view",
				tabIdentifiers, state);
		System.out.println("HEADER FORM" + headerForm.getName());// for header form fields
		
		/*if (!(detailForm.getName().equals("wm_internal_transfer_detail_detail_view"))){
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_internal_transfer_detail_toggle_slot"), "Detail"  );
			
			DataBean detailFormFocus = detailForm.getFocus();
			System.out.println("DFDFDF" + detailForm.getName());
			String frmSku = (String) detailFormFocus.getValue("TOSKU");
			System.out.println("DEDEDE" + frmSku);
			
			context.setNavigation("clickEvent1870");

	    }	*/
		//context.setNavigation("clickEvent1870");
		
		return super.execute(context, result);
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
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
