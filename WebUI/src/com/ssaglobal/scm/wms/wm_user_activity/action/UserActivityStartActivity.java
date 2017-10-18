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
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityStartActivity extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private static final String _USERACTIVITY_STATUS_ASSIGNED = "2";

	private static final String _ACTIVITY_ACTYPE_INDIRECT = "1";

	private static final String _ACTIVITY_ACTYPE_DIRECT = "2";

	private static final String _USERACTIVITY_STATUS_COMPLETE = "9";

	private static final String _USERACTIVITY_STATUS_NOTSTARTED = "0";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityStartActivity.class);
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
		
			// Get current UserAttendanceKey
			Object userAttendanceKeyObject = timeInResults.max("USERATTENDANCEKEY");
			
			// Ensure user isn't assigned to multiple activities
			//query USERACTIVITY for status InProcess
			
			
			

			RuntimeFormWidgetInterface indirectActivityWidget = form.getFormWidgetByName("INDIRECTACTIVITY");
			String activity = (String) indirectActivityWidget.getValue();

			if (!StringUtils.isEmpty(activity)) {
				
				// make activity record
				/*
				 * Based on the Activity selected, the following information should be retrieved based on the ACTYPE of
				 * the selected Activity: 
				 * Status: 
				 * 	If the ACTYPE of the Activity is INDIRECT. The system will retrieve
				 * the INDIRECTACTIVITY.DURATION value for the matching INDIRECTACTIVITY.ACTIVITY the user entered. Then
				 * calculate the USERACTIVITY.ENDTIME. Calculation will take the screens Start Activity time and add the
				 * INDIRECTACTIVITY.DURATION to determine the USERACTIVITY.ENDTIME. The USERACTIVITY.STATUS = ‘COMPLETE’
				 * 	If the ACTYPE of Activity is DIRECT, the USERACTIVITY.ENDTIME = “00/00/0000 00:00:00”. The
				 * USERACTIVITY.STATUS = ‘ASSIGNED’.
				 * 
				 * Once the information is retrieved, the extension will insert a record into the USERACTIVITY table
				 * TYPE = Activity field 
				 * STARTTIME = Current Time 
				 * ENDTIME = (see above) 
				 * USER = Username logged in 
				 * ACTYPE = ACTYPE equal the INDIRECTACTIVITY.ACTYPE from the Activity 
				 * USERATTTENDANCEKEY = USERATTENDANCE.USERATTENDANCEKEY where the USERID = user logged in and the USERATTENDANCE.STATUS =
				 * ‘ACTIVE’. 
				 * STATUS = (see above)
				 */

				tuow.clearState();
				// Activity = PK, and in drop down
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_indirectactivity", "wm_indirectactivity.ACTIVITY = '" + activity + "'", null));
				if (rs.size() != 1) {
					new UserException("WMEXP_INV_SKU", new Object[] { activity, StringUtils.removeTrailingColon(FormUtil.getWidgetLabel(context, indirectActivityWidget)) });
				}
				BioBean actBean = null;
				for (int i = 0; i < rs.size(); i++) {
					actBean = rs.get("" + i);
				}
				// ACTYPE, where 1=Indirect and 2=Direct
				Calendar startTime = GregorianCalendar.getInstance();
				Calendar endTime = (Calendar) startTime.clone();
				//Useractivity Status
				/*
				 * 0	Not Started
					1	InProcess
					2	Assigned
					9	Completed
				 */
				String status = _USERACTIVITY_STATUS_NOTSTARTED;
				String actType = BioAttributeUtil.getString(actBean, "ACTYPE");
				if (_ACTIVITY_ACTYPE_INDIRECT.equals(actType)) {
					// Indirect
					int duration = BioAttributeUtil.getInt(actBean, "DURATION"); //in minutes
					endTime.add(Calendar.MINUTE, duration);
					//Status = Complete
					status = _USERACTIVITY_STATUS_COMPLETE;

				} else if (_ACTIVITY_ACTYPE_DIRECT.equals(actType)) {
					// Direct
					//end time = 00/00/0000 00:00:00
					//TODO: impossible to set to 00/00/0000 00:00:00
					
					//status = Assigned
					status = _USERACTIVITY_STATUS_ASSIGNED;

				} else {
					_log.error(	"LOG_ERROR_EXTENSION_UserActivityStartActivity_execute",
								"ActType " + actType + " is not handled",
								SuggestedCategory.NONE);
					throw new UserException("WMEXP_SYS_EXP", new Object[] {});
				}
				
				
				//Save Record
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				uow.clearState();
				BioBean newUA = uow.getNewBio("wm_useractivity_bio");
				newUA.set("USERACTIVITYKEY", new KeyGenBioWrapper().getKey("USERACTIVITYKEY"));
				newUA.set("TYPE", activity);
				newUA.set("STARTTIME", startTime);
				newUA.set("ENDTIME", endTime);
				newUA.set("USERID", userName);
				newUA.set("ACTYPE", actType);
				newUA.set("USERATTENDANCEKEY", userAttendanceKeyObject);
				newUA.set("STATUS", status);
				newUA.save();
				uow.saveUOW();

			} else {
				// Exception
				throw new UserException("WMEXP_REQFIELD", new Object[] { StringUtils.removeTrailingColon(FormUtil.getWidgetLabel(context, indirectActivityWidget)) });
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
