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

package com.ssaglobal.scm.wms.wm_user_activity.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityEndActivity extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String _USERACTIVITY_STATUS_ASSIGNED = "2";
	
	private static final String _USERACTIVITY_STATUS_COMPLETED = "9";

	private static final String _ACTIVITY_ACTYPE_DIRECT = "2";

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

		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();

		String userName = form.getFormWidgetByName("USERNAME").getDisplayValue();
		String password = form.getFormWidgetByName("PASSWORD").getDisplayValue();
		boolean login = false;
		if (ValidateCredentials.validateUser(userName, password) == false) {
			throw new UserException("EXP_LOGIN_FAILED", new Object[] {});
		} else {
			login = true;
		}

		// clear password
		form.getFormWidgetByName("PASSWORD").setValue("");

		if (login == true) {

			// Check for active record
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			Query activeRecordQuery = new Query("wm_userattendance", "wm_userattendance.USERID = '" + userName + "' and wm_userattendance.STATUS = '1'", null);
			BioCollectionBean timeInResults = tuow.getBioCollectionBean(activeRecordQuery);
			if (timeInResults.size() == 0) {
				throw new UserException("WMEXP_UA_ACTIVE", new Object[] {});
			}

			RuntimeFormWidgetInterface indirectActivityWidget = form.getFormWidgetByName("INDIRECTACTIVITY");
			String activity = (String) indirectActivityWidget.getValue();

			if (!StringUtils.isEmpty(activity)) {
				// query USERACTIVITY
				/*
				 * System should check to see if the user has a USERACTIVITY record with a status of ‘ASSIGNED’ and
				 * ACTYPE = ‘DIRECT’ and TYPE = INDIRECTACTIVITY.ACTIVITY.
				 */
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_useractivity_bio", "wm_useractivity_bio.STATUS = '" + _USERACTIVITY_STATUS_ASSIGNED + "' and wm_useractivity_bio.ACTYPE = '" + _ACTIVITY_ACTYPE_DIRECT + "' and wm_useractivity_bio.TYPE = '" + activity + "' and wm_useractivity_bio.USERID = '" + userName + "'", null));
				if (rs.size() == 0) {
					// message no activity to end
					form.setError("WMEXP_UA_NO_ACTIVITY", new Object[] {});
				}

				for (int i = 0; i < rs.size(); i++) {
					BioBean uaRecord = rs.get("" + i);
					uaRecord.set("STATUS", _USERACTIVITY_STATUS_COMPLETED);
					uaRecord.set("ENDTIME", GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC")));
					uaRecord.save();
				}

				uow.saveUOW();

			} else {
				// Exception
				throw new UserException("WMEXP_REQFIELD", new Object[] { StringUtils.removeTrailingColon(FormUtil.getWidgetLabel(	context,
																																	indirectActivityWidget)) });
			}
		}

		focus.setValue("DATETIMEIN", null);
		focus.setValue("DATETIMEOUT", null);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}
}
