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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityTimeOutActionPostConfirm extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private static final String _ASSIGNMENT_STATUS_COMPLETE = "0";
	private static final String _ASSIGNMENT_STATUS_ACTIVE = "1";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityTimeOutActionPostConfirm.class);

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
		String response = (String) state.getRequest().getSession().getAttribute("UARESPONSE");
		state.getRequest().getSession().removeAttribute("UARESPONSE");
		if ((response).equalsIgnoreCase("Yes"))
		{
			RuntimeFormInterface form = state.getCurrentRuntimeForm();
			DataBean focus = form.getFocus();

			String userID = form.getFormWidgetByName("USERNAME").getDisplayValue();
			
			// Labor
			// Check if there is any active assignment in Assignment table with Status=ACTIVE,
			// update Assignment.EndTime= CurrentTime and Assignment.status= Complete
			// '1' = ACTIVE

			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_assignment", "wm_assignment.USERID = '" + userID + "' and wm_assignment.STATUS = '" + _ASSIGNMENT_STATUS_ACTIVE + "'", null));
			for (int i = 0; i < rs.size(); i++) {
				BioBean assnRec = rs.get("" + i);
				// set to complete
				assnRec.setValue("STATUS", _ASSIGNMENT_STATUS_COMPLETE);
				// update endtime
				assnRec.setValue("ENDTIME", GregorianCalendar.getInstance());
				assnRec.save();
			}
			uow.saveUOW();
			//
			

			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(userID));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPLABORUSERLOGOUT");
			//				run stored procedure
			EXEDataObject logoutResults = null;
			try
			{
				logoutResults = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e)
			{
				throw new UserException(e.getMessage(), new Object[] {});
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			displayResults(logoutResults);
			String dateTimeOut = logoutResults.getAttribValue(1).asTextData().getAsString();
			_log.debug("LOG_DEBUG_EXTENSION", dateTimeOut, SuggestedCategory.NONE);
			SimpleDateFormat ejbFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			ejbFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
			java.util.Date ejbDate = null;
			try
			{
				ejbDate = ejbFormat.parse(dateTimeOut);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			focus.setValue("DATETIMEIN", null);
			Calendar timeOut = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
			timeOut.setTime(ejbDate);
			_log.debug(	"LOG_DEBUG_EXTENSION_UserActivityTimeOutActionPostConfirm_execute",
			           	timeOut.toString(),
						SuggestedCategory.NONE);
			focus.setValue("DATETIMEOUT", timeOut);
			form.setError("WMEXP_UA_CLOCK_OUT", new Object[] { userID });
			//success
			
		}
		return RET_CONTINUE;
	}

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
					_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
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
