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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityTimeInAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityTimeInAction.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();

		//TODO:Login, verify with LDAP
		String userName = form.getFormWidgetByName("USERNAME").getDisplayValue();
		String password = form.getFormWidgetByName("PASSWORD").getDisplayValue();
		boolean login = false;
		if (ValidateCredentials.validateUser(userName, password) == false) {
			throw new UserException("EXP_LOGIN_FAILED", new Object[] {});
		}
		else {
			login = true;
		}
        
		//clear password
		form.getFormWidgetByName("PASSWORD").setValue("");
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
		Calendar ejbDate = null;


		if (login == true)
		{
			
			//Check for active record
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			Query activeRecordQuery = new Query("wm_userattendance","wm_userattendance.USERID = '" + userName + "' and wm_userattendance.STATUS = '1'",null);
			BioCollectionBean timeInResults = tuow.getBioCollectionBean(activeRecordQuery);
			/* Convert to Bio Query 
			String activeQuery = "SELECT COUNT(*) FROM USERATTENDANCE WHERE USERID = '" + userName + "' AND STATUS = '1'";
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + activeQuery + "\n", SuggestedCategory.NONE);

			EXEDataObject activeQueryResults = WmsWebuiValidationSelectImpl.select(activeQuery);
			int activeRecord = activeQueryResults.getAttribValue(1).asTextData().getIntegerValue();
			if (activeQueryResults.getRowCount() <= 0 || activeRecord == 0)
			*/
			if(timeInResults.size() == 0)
			{
				//if no active record found, create one
				String logout = "NO";
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(userName));
				params.add(new TextData(logout));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPLABORUSERLOGIN");
				//run stored procedure
				EXEDataObject loginResults = null;
				try
				{
					loginResults = WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (WebuiException e)
				{
					throw new UserException(e.getMessage(), new Object[] {});
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				displayResults(loginResults);

			}
			//Get TimeIn
			tuow.clearState();
			timeInResults = tuow.getBioCollectionBean(activeRecordQuery);
			if (timeInResults.size() == 0) {
				_log.error(	"LOG_DEBUG_EXTENSION_UserActivityTimeInAction",
							"dateTimeIn is null - no rows returned",
							SuggestedCategory.NONE);
				throw new UserException("WMEXP_UA_ERROR", new Object[] {});
			}
			for (int i = 0; i < timeInResults.size(); i++) {
				BioBean timeIn = timeInResults.get("" + i);
				ejbDate = BioAttributeUtil.getCalendar(timeIn, "DATETIMEIN");

			}
			/*
			 * Changing to Bio Query String timeInQuery = "SELECT DATETIMEIN FROM USERATTENDANCE WHERE USERID = '" +
			 * userName + "' AND STATUS = '1'"; _log.debug("LOG_DEBUG_EXTENSION", "\n\t" + timeInQuery + "\n",
			 * SuggestedCategory.NONE); _log.debug("LOG_DEBUG_EXTENSION_UserActivityTimeInAction", "Performing Query " +
			 * timeInQuery, SuggestedCategory.NONE);
			 * 
			 * EXEDataObject timeInResults; try { timeInResults = WmsWebuiValidationSelectImpl.select(timeInQuery); }
			 * catch (Exception e) { _log.debug("LOG_DEBUG_EXTENSION_UserActivityTimeInAction",
			 * "Results from the timeInQuery is null", SuggestedCategory.NONE); e.printStackTrace(); throw new
			 * UserException("WMEXP_UA_ERROR", new Object[] {}); } String dateTimeIn = null; try { dateTimeIn =
			 * timeInResults.getAttribValue(1).asTextData().getAsString(); } catch (NullPointerException e) {
			 * _log.debug("LOG_DEBUG_EXTENSION_UserActivityTimeInAction", "dateTimeIn is null - no rows returned",
			 * SuggestedCategory.NONE); e.printStackTrace(); throw new UserException("WMEXP_UA_ERROR", new Object[] {});
			 * } _log.debug("LOG_DEBUG_EXTENSION", dateTimeIn, SuggestedCategory.NONE); SimpleDateFormat ejbFormat = new
			 * SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			 * 
			 * ejbDate = null; try { ejbDate = ejbFormat.parse(dateTimeIn); } catch (ParseException e) {
			 * e.printStackTrace(); throw new UserException("WMEXP_UA_ERROR", new Object[] {}); }
			 */
			//Check Extended Period
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(userName));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPUSEREXTENDEDPERIOD");
			//run stored procedure
			EXEDataObject extendedResults = null;
			try
			{
				extendedResults = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e)
			{
				throw new UserException(e.getMessage(), new Object[] {});
			} catch (Exception e1)
			{
				e1.printStackTrace();
				throw new UserException("WMEXP_UA_ERROR", new Object[] {});
			}

			String extendedPeriod = null;
			try
			{
				displayResults(extendedResults);

				extendedPeriod = extendedResults.getAttribValue(1).getAsString();
			} catch (Exception e)
			{
				_log.debug("LOG_DEBUG_EXTENSION_ UserActivityTimeInAction",
							"Results from the SP NSPUSEREXTENDEDPERIOD is Null", SuggestedCategory.NONE);
				e.printStackTrace();
				throw new UserException("WMEXP_UA_ERROR", new Object[] {});
			}
			if (extendedPeriod.equals("TRUE"))
			{
				//prompt
				//set timein into context
				context.getState().getRequest().getSession().setAttribute("UATIMEIN", ejbDate);
				//set navigation
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting Navigation to Popup" + "\n", SuggestedCategory.NONE);
				context.setNavigation("clickEvent924");
				return RET_CONTINUE;

			}
			
		}
		else
		{
			//clear
		}
		//clearall

		//set navigation
		//Set TimeIn
		//TODO: Date is GMT
		_log.debug("LOG_DEBUG_EXTENSION_UserActivityTimeInAction", ejbDate.toString(), SuggestedCategory.NONE);
		focus.setValue("DATETIMEIN", ejbDate);
		form.setError("WMEXP_UA_CLOCK_IN", new Object[] { userName });
		//Clear TimeOut
		focus.setValue("DATETIMEOUT", null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting Navigation to Self" + "\n", SuggestedCategory.NONE);
		context.setNavigation("clickEvent923");

		return RET_CONTINUE;
	}

	/*
	 //set time
	 SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
	 Date currentDateTime = new Date(System.currentTimeMillis());
	 focus.setValue("DATETIMEIN", dateFormat.format(currentDateTime));
	 */
	private void displayResults(EXEDataObject results)
	{
		if (results.getColumnCount() != 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + results.getRowCount() + " x " + results.getColumnCount() + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
				} catch (Exception e)
				{
					_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
				}
			}
			
		}
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
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
