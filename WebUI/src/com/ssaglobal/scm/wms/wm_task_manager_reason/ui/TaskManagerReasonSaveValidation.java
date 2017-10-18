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

package com.ssaglobal.scm.wms.wm_task_manager_reason.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TaskManagerReasonSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskManagerReasonSaveValidation.class);
	String serverType;

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

		serverType = getDBServerType(context, state);
		_log.debug("LOG_DEBUG_EXTENSION_TaskManagerReasonSaveValidation_execute", "ServerType " + serverType, SuggestedCategory.NONE);
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface reasonForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_task_manager_reason_detail_view", state);
		if (!isNull(reasonForm)) {
			TMReason reasonValidation = new TMReason(reasonForm, state);
			reasonValidation.run();
		} else {
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	private String getDBServerType(ActionContext context, StateInterface state) {
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		HttpSession session = state.getRequest().getSession();

		//O90
		//MSS
		String serverType = "O90";
		serverType = (String) session.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
		if (serverType == null) {
			serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
		}
		return serverType;
	}

	class TMReason extends FormValidation {
		String key;

		public TMReason(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
			key = focus.getValue("TASKMANAGERREASONKEY") == null ? null
					: focus.getValue("TASKMANAGERREASONKEY").toString().toUpperCase();
		}

		public void run() throws EpiDataException, UserException {
			if (isInsert) {
				reasonDuplication();
			}
			checkNull(focus); //Subodh: Machine# 2187985, SDIS# SCM-00000-05861
			locValidation("TOLOC");
			greaterThanOrEqualToZeroValidation("INTERVAL");
			completeStatusValidation(focus);  //03/16/2011 FW:  Added logic to check task's complete status (Incident4331607_Defect302828)
		}

		//Subodh: Added new method to check null values. Machine# 2187985, SDIS# SCM-00000-05861
		public void checkNull(DataBean focus) {

			String emptyString = " ";
			if ("MSS".equals(serverType)) {
				emptyString = "";
			}
			_log.debug("LOG_DEBUG_EXTENSION_TaskManagerReasonSaveValidation.TMReason_checkNull", "ServerTYpe " + serverType + " and emtpyString |" + emptyString + "|", SuggestedCategory.NONE);
			if (focus.getValue("DESCR") == null) {
				focus.setValue("DESCR", emptyString);
			}
			if (focus.getValue("IDHOLDKEY") == null) {
				focus.setValue("IDHOLDKEY", emptyString);
			}
			if (focus.getValue("TOLOC") == null) {
				focus.setValue("TOLOC", emptyString);
			}
			if (focus.getValue("LOCHOLDKEY") == null) {
				focus.setValue("LOCHOLDKEY", emptyString);
			}
			if (focus.getValue("TASKSTATUS") == null) {
				focus.setValue("TASKSTATUS", emptyString);
			}
		}

		void reasonDuplication() throws DPException, UserException {

			String query = "SELECT * FROM TASKMANAGERREASON WHERE (TASKMANAGERREASONKEY = '" + key + "')";
			_log.debug("LOG_DEBUG_EXTENSION", query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1) {
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("TASKMANAGERREASONKEY");
				parameters[1] = key;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}
	}

	private boolean isNull(Object attributeValue) {

		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}

	}
	
	//03/16/2011 FW:  Added logic to check task's complete status (Incident4331607_Defect302828) -- Start
	public void completeStatusValidation(DataBean focus) throws DPException, UserException {  
		//If task's status is set to be 'complete' or 'cancelled' then REMOVETASKFROMUSERQUEUE can't be set to 'Y' 
		_log.debug("LOG_DEBUG_EXTENSION_completeStatusValidation", "ServerType " + serverType, SuggestedCategory.NONE);
		if (focus.getValue("REMOVETASKFROMUSERQUEUE").toString().equalsIgnoreCase("1") && (focus.getValue("TASKSTATUS").toString().equalsIgnoreCase("9") || focus.getValue("TASKSTATUS").toString().equalsIgnoreCase("X"))){
			throw new UserException("WMEXP_DEFAULT_COMPLETESTATUS", new Object[] {});
		}
	}
	//03/16/2011 FW:  Added logic to check task's complete status (Incident4331607_Defect302828) -- End
}
