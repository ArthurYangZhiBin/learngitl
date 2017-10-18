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

package com.ssaglobal.scm.wms.wm_indirect_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.DateTimeNullable;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListFilterPreQueryAction;
import com.epiphany.shr.ui.action.ListSortAction;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class IndirectActivityCreateIndirectActivity extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(IndirectActivityCreateIndirectActivity.class);
	private static final String USERID = "USERID";

	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

	String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);

	MetaDataAccess mda = MetaDataAccess.getInstance();

	LocaleInterface locale = mda.getLocale(userLocale);

	private static final String USER_ATTENDANCE_KEY = "USERATTENDANCEKEY";

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_indirect_activity_list_view", state);
		ArrayList items;

		try
		{
			items = listForm.getSelectedItems();
			if (isZero(items))
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}

		HashMap uakHash = new HashMap();
		//Validate UserAttendanceKey before opening Popup
		String userLabel = listForm.getFormWidgetByName(USER_ATTENDANCE_KEY).getLabel("label", state.getLocale());
		//

		//
		for (Iterator it = items.iterator(); it.hasNext();)
		{

			BioBean selected = (BioBean) it.next();
			Object dbUAK = selected.get(USER_ATTENDANCE_KEY);
			_log.debug("LOG_DEBUG_EXTENSION", " UAK " + dbUAK + "", SuggestedCategory.NONE);
			Object tempUAK = selected.get("NEWUAK");
			_log.debug("LOG_DEBUG_EXTENSION", " NEW-UAK " + tempUAK + "", SuggestedCategory.NONE);
			String uakValue;

			//if the temp mapped UAK value, use UAK from the DB, else use the user entered UAK value
			//this is because of an OA limitation
			if (isNull(tempUAK))
			{
				uakValue = (String) dbUAK;
			}
			else
			{
				uakValue = (String) tempUAK;
			}
			//validate userattendance key
			userAttendanceKeyValidation(userLabel, selected, uakValue, state);

			_log.debug("LOG_DEBUG_EXTENSION", "Putting into Session " + dbUAK + " , " + uakValue, SuggestedCategory.NONE);

			//set INPUTQTY in the hash based on the primary key of the REPLENISHMENT table
			uakHash.put(dbUAK.toString(), uakValue);
		}
		//putting hash into session
		//state.getRequest().getSession().setAttribute("IAUAK", uakHash);
		userContext.put("IAUAK", uakHash);
		
		return RET_CONTINUE;
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "" + "Start of IndirectActivityCreateIndirectActivity - Modal" + "", SuggestedCategory.NONE);
		StateInterface state = ctx.getState();
		RuntimeFormInterface modalForm = ctx.getModalBodyForm(0);
		_log.debug("LOG_DEBUG_EXTENSION", "" + modalForm.getName() + "", SuggestedCategory.NONE);
		String indirectActivityValue = modalForm.getFormWidgetByName("INDIRECTACTIVITY").getValue().toString();
		String indirectActivityLabel = modalForm.getFormWidgetByName("INDIRECTACTIVITY").getLabel("label", locale);
		Calendar iaStartTime = modalForm.getFormWidgetByName("STARTTIME").getCalendarValue();
		requiredField(modalForm, indirectActivityValue, indirectActivityLabel);
		//String startTime = indirectActivityStartTime.toString();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String startTime = dateFormat.format(iaStartTime.getTime())+ " " +timeFormat.format(iaStartTime.getTime()) ;

		state.closeModal(true);

		_log.debug("LOG_DEBUG_EXTENSION", "" + "Indirect Activity : " + indirectActivityValue + "", SuggestedCategory.NONE);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(ctx.getSourceForm(), "wms_list_shell", "wm_indirect_activity_list_view", state);

		ArrayList items = listForm.getAllSelectedItems();
		//retrieve hash from session
		//HashMap uakHash = (HashMap) state.getRequest().getSession().getAttribute("IAUAK");
		HashMap uakHash = (HashMap) userContext.get("IAUAK");
		//state.getRequest().getSession().removeAttribute("IAUAK");
		userContext.remove("IAUAK");
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selected = (BioBean) it.next();
			String uakKey = (String) selected.getValue(USER_ATTENDANCE_KEY);
			_log.debug("LOG_DEBUG_EXTENSION", " UAK " + uakKey + "", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", " NEW-UAK " + selected.getValue("NEWUAK") + "", SuggestedCategory.NONE);

			//get value from session
			String uakValue = (String) uakHash.get(uakKey);
			uakValue = uakValue == null ? null : uakValue.toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION", " SESSION-UAK " + uakValue + "", SuggestedCategory.NONE);

			String query = "SELECT duration,actype FROM " + "INDIRECTACTIVITY" + " WHERE activity = '" + indirectActivityValue +  "'";
				_log.debug("LOG_DEBUG_EXTENSION", "Query" + query, SuggestedCategory.NONE);
			EXEDataObject dResults = WmsWebuiValidationSelectImpl.select(query);
			String duration = "0";
			String actype = "0";
			if (dResults.getRowCount() == 1)
			{
				DataValue tmpDuration = dResults.getAttribValue(new TextData("duration"));
				if (!tmpDuration.getNull())
				{
					duration = tmpDuration.toString();
				}
				actype = dResults.getAttribValue(new TextData("actype")).toString();
			}
			
			//NSPINDIRECTACTIVITY", {ls_indirectactivity,ls_userid,ls_userattendancekey
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(indirectActivityValue));
			params.add(new TextData(selected.getValue(USERID).toString()));
			params.add(new TextData(uakValue)); //selected.getValue("USERATTENDANCEKEY").toString()));
			params.add(new TextData(startTime));
			params.add(new TextData(duration));
			params.add(new TextData(actype));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPSUPERVISORINDIRECTACTIVITY");

			//run stored procedure
			EXEDataObject results = null;
			try
			{
				results = WmsWebuiActionsImpl.doAction(actionProperties);
				//handle results
				if (results.getColumnCount() != 0)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
					for (int i = 1; i < results.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "
									+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						} catch (NullPointerException e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
							return RET_CANCEL;
						}
					}
					
				}
			} catch (WebuiException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "" + e.getMessage(), SuggestedCategory.NONE);
				//throw new UserException(e.getMessage(), new Object[] {});
				//Set Error Message into Session
				//state.getRequest().getSession().setAttribute("IAERRORMESSAGE", e.getMessage());
				userContext.put("IAERRORMESSAGE", e.getMessage());
				return RET_CONTINUE; //exit
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

		listForm.setSelectedItems(null);
		args.setSelectedItems(null);
		return RET_CONTINUE;
	}

	private void requiredField(RuntimeFormInterface modalForm, String user, String userLabel) throws FieldException
	{
		if (isEmpty(user))
		{
			//throw exception
			String[] parameters = new String[1];
			parameters[0] = removeTrailingColon(userLabel);

			throw new FieldException(modalForm, "INDIRECTACTIVITY", "WMEXP_REQFIELD", parameters);

		}
	}

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	private void userAttendanceKeyValidation(String userLabel, DataBean selected, String uakValue, StateInterface state) throws EpiDataException, UserException
	{
		Object attendanceKeyValue = uakValue;
		Object userIdValue = selected.getValue(USERID);
		if (verifyUAK(attendanceKeyValue, userIdValue) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = userLabel;
			parameters[1] = attendanceKeyValue.toString();
			//state.getRequest().getSession().removeAttribute("IAUAK"); //remove session variable
			userContext.remove("IAUAK");
			throw new UserException("WMEXP_IA_VALIDUAK", parameters);
		}
	}

	protected boolean verifyUAK(Object attendanceKeyValue, Object userIdValue) throws EpiDataException
	{
		if (isEmpty(attendanceKeyValue) || isEmpty(userIdValue))
		{
			return true; //Do Nothing
		}
		String query = "SELECT * FROM " + "USERATTENDANCE" + " WHERE " + USER_ATTENDANCE_KEY + " = '"
				+ attendanceKeyValue.toString().toUpperCase() + "' AND " + " USERID " + " = '" + userIdValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

	protected boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	boolean isZero(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (((ArrayList) attributeValue).size() == 0)
		{
			return false;
		}
		else
		{
			return false;
		}
	}

}
