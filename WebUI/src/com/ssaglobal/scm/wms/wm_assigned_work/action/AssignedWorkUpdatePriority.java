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


package com.ssaglobal.scm.wms.wm_assigned_work.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AssignedWorkUpdatePriority extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignedWorkUpdatePriority.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_assigned_work_template", "wm_assigned_work_list_view", state);

		ArrayList<BioBean> allSelectedItems = listForm.getAllSelectedItems();
		if (allSelectedItems == null) {
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		String userId = (String) userCtx.get("logInUserId");
		for (int i = 0; i < allSelectedItems.size(); i++) {
			BioBean selectedItem = allSelectedItems.get(i);
			String assignmentNumber = BioAttributeUtil.getString(selectedItem, "ASSIGNMENTNUMBER");
			String waveKey = BioAttributeUtil.getString(selectedItem, "WAVEKEY");
			String userid = BioAttributeUtil.getString(selectedItem, "USERID");
			int priority = BioAttributeUtil.getInt(selectedItem, "PRIORITY");
			int oldPriority = ((Integer) selectedItem.getValue("PRIORITY", true)).intValue();
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkUpdatePriority_execute", "Assignment " + assignmentNumber + " " + priority, SuggestedCategory.NONE);
			//update USERACTIVITY table
			//status = 1
			String awQuery = "wm_useractivity_bio.STATUS = '1' and wm_useractivity_bio.ASSIGNMENTNUMBER = '" +
								assignmentNumber +
								"' and wm_useractivity_bio.WAVEKEY = '" +
								waveKey +
								"' and wm_useractivity_bio.USERID = '" +
								userid +
								"' and wm_useractivity_bio.PRIORITY = '" +
								oldPriority +
								"'";
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkUpdatePriority_execute", awQuery, SuggestedCategory.NONE);
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_useractivity_bio", awQuery, null));
			for (int j = 0; j < rs.size(); j++) {
				BioBean userActivity = rs.get("" + j);
				_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkUpdatePriority_execute", "Updating " + BioAttributeUtil.getString(userActivity, "USERACTIVITYKEY") + " to priority " + priority,
						SuggestedCategory.NONE);
				userActivity.setValue("PRIORITY", priority);
				userActivity.save();
			}

			selectedItem.save();
		}

		uow.saveUOW(false);
		listForm.setSelectedItems(null);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is
	 * fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

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
