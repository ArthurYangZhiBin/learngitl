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

package com.ssaglobal.scm.wms.wm_appointment;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;

//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - NOV-12-2010 - End
/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentSave extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppointmentSave.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		context.setNavigation("clickEventSave");
		//RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		//String name = sourceWidget.getName();
		//Navigation navigation = context.getNavigation();
		boolean checkLength = true;
		if (context.getNavigation() == null)
		{
			checkLength = false;
		}
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();


		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_appointment_list", state);
		if (listForm != null && listForm.isListForm())
		{
			BioCollectionBean list = (BioCollectionBean) ((RuntimeListFormInterface) listForm).getFocus();

			for (int i = 0; i < list.size(); i++)
			{
				BioBean row = list.get("" + i);
				//				if(row.getUpdatedAttributes()!= null && row.getUpdatedAttributes().size()>0)
				if (row.hasBeenUpdated("CARRIER"))
				{
					Object attributeValue = row.getValue("CARRIER");
					if (!isEmpty(attributeValue))
					{
						attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
						String type = "3";
						String query = "wm_storer.STORERKEY = '" + attributeValue + "' and wm_storer.TYPE = '" + type + "'";
						BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_storer", query, null));
						_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
						if (rs.size() < 1)
						{
							String[] parameters = new String[3];
							parameters[0] = retrieveLabel(listForm, "CARRIER");
							parameters[1] = attributeValue.toString();
							throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
						}

					}
					else
					{
						throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { retrieveLabel(listForm, "CARRIER") });
					}
				}
				if (row.hasBeenUpdated("DOOR"))
				{
					Object attributeValue = row.getValue("DOOR");
					if (!isEmpty(attributeValue))
					{
						attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
						//String type = "DOOR";
						String query = "wm_location.LOC = '" + attributeValue + "' and ( wm_location.LOCATIONTYPE = 'DOOR' OR wm_location.LOCATIONTYPE = 'STAGED' )";
						BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_location", query, null));
						_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
						if (rs.size() < 1)
						{
							String[] parameters = new String[3];
							parameters[0] = retrieveLabel(listForm, "DOOR");
							parameters[1] = attributeValue.toString();
							throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
						}
					}
					else
					{
						throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { retrieveLabel(listForm, "CARRIER") });
					}

					updateAppointmentDetails(row.getValue("APPOINTMENTDETAIL"));
				}

			}
		}

		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_appointment_detail_view", state);

		if (detailForm != null)
		{
			Appointment appointmentDetailForm = new Appointment(detailForm, state);
			appointmentDetailForm.run();
			//do i need to confirm?
			if (checkLength == true)
			{
				if (appointmentDetailForm.apptIsGreaterThanAnHour() == true)
				{
					EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
					userContext.put("APPT_WARNING" + state.getInteractionId(), "APPT_HOUR_WARNING");
					context.setNavigation("clickEventConfirm");
					return RET_CONTINUE;
				}
			}
			appointmentDetailForm.save();

		}
		uow.saveUOW(true);
		
		
		// REFRESH
		if (listForm != null && listForm.isListForm()) {
			try {
				QBEBioBean filter = ((RuntimeListForm) listForm).getFilterRowBean();
				//convert query to qbe
				BioCollectionBean collection = (BioCollectionBean) uow.getBioCollectionBean(new Query("wm_appointment", null, null));
				BioCollectionBean newCollection = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
				BioCollectionBean newCollectionB = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
				newCollectionB.copyFrom(collection);
				collection.copyFrom(newCollection);
				newCollection = collection;
				filter.setBaseBioCollectionForQuery(newCollectionB);
				newCollection.setQBEBioBean(filter);
				newCollection.filterInPlace(filter.getQueryWithWildcards());
				result.setFocus(newCollection);
			} catch (Exception e) {
				_log.error("Error Filtering","Going to return everything",e);
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_appointment", null, null));
				result.setFocus(rs);
			}
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
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

	String retrieveLabel(RuntimeFormInterface form, String widgetName)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	private void updateAppointmentDetails(Object value) throws EpiDataException
	{
		if (value instanceof BioCollectionBean)
		{
			BioCollectionBean appDetails = (BioCollectionBean) value;
			for (int i = 0; i < appDetails.size(); i++)
			{
				BioBean appDetail = appDetails.get("" + i);
				appDetail.setValue("EDITDATE", new GregorianCalendar());
				appDetail.save();
			}
		}

	}

	private class Appointment extends FormValidation
	{

		public Appointment(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
		}

		public boolean apptIsGreaterThanAnHour()
		{
			Calendar start = (Calendar) BioAttributeUtil.getCalendar(focus, "GMTSTARTDATEANDTIME").clone();
			Calendar end = (Calendar) BioAttributeUtil.getCalendar(focus, "GMTENDDATEANDTIME").clone();
			int slots = AppointmentUtil.calculateSlots(start, end);
			if (slots > 4)
			{
				return true;
			}
			return false;
		}

		public void run() throws EpiException
		{
			carrierValidation("CARRIER");
			doorValidation("DOOR");
			if (isInsert)
			{
				focus.setValue("GUID", GUIDFactory.getGUIDStatic());
			}

			calculateAppointmentDate();
			updateAppointmentDetails(focus.getValue("APPOINTMENTDETAIL"));

		}

		private void doorValidation(String attributeName) throws UserException, EpiDataException
		{
			if (verifyDoorAttribute(attributeName) == false)
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = focus.getValue(attributeName).toString();
				throw new UserException("WMEXP_NOTAVALID", parameters);
			}

		}

		private boolean verifyDoorAttribute(String attributeName) throws EpiDataException
		{
			Object attributeValue = focus.getValue(attributeName);
			if (isEmpty(attributeValue))
			{
				return true; //Do Nothing
			}
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			//String type = "DOOR";
			String query = "wm_location.LOC = '" + attributeValue + "' and ( wm_location.LOCATIONTYPE = 'DOOR' or wm_location.LOCATIONTYPE = 'STAGED' )";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
			BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_location", query, null));
			if (rs.size() == 1)
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

		private void calculateAppointmentDate() throws EpiException
		{
			DateFormat shortF = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.LONG);

			// get elements
			RuntimeFormWidgetInterface startDate = form.getFormWidgetByName("STARTDATE");
			RuntimeFormWidgetInterface startHour = form.getFormWidgetByName("STARTHOUR");
			RuntimeFormWidgetInterface startMin = form.getFormWidgetByName("STARTMIN");

			RuntimeFormWidgetInterface endDate = form.getFormWidgetByName("ENDDATE");
			RuntimeFormWidgetInterface endHour = form.getFormWidgetByName("ENDHOUR");
			RuntimeFormWidgetInterface endMin = form.getFormWidgetByName("ENDMIN");

			TimeZone timeZone = ReportUtil.getTimeZone(state);

			Calendar start = startDate.getCalendarValue();
			start.setTimeZone(timeZone);
			String startHourS = (String) startHour.getValue();
			start.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startHourS));
			String startMinS = (String) startMin.getValue();
			start.set(Calendar.MINUTE, Integer.valueOf(startMinS));
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(start.getTime()), SuggestedCategory.NONE);

			//Default TO to 1hr+
			Calendar end = null;
			String endHourS = (String) endHour.getValue();
			String endMinS = (String) endMin.getValue();
			if (StringUtils.isEmpty(endHourS) || StringUtils.isEmpty(endMinS)) {
				end = (Calendar) start.clone();
				end.add(Calendar.HOUR_OF_DAY, 1);
			} else {
				end = endDate.getCalendarValue();
				end.setTimeZone(timeZone);
				end.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endHourS));
				end.set(Calendar.MINUTE, Integer.valueOf(endMinS));
				end.set(Calendar.SECOND, 0);
				end.set(Calendar.MILLISECOND, 0);
			}
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(end.getTime()), SuggestedCategory.NONE);

			// need to save GMT value
			Calendar gmtStart = (Calendar) start.clone();
			Calendar gmtEnd = (Calendar) end.clone();
			// gmtStart.add(Calendar.MILLISECOND, -1 * startOffSet);
			// gmtEnd.add(Calendar.MILLISECOND, -1 * endOffSet);
			focus.setValue("GMTSTARTDATEANDTIME", gmtStart);
			focus.setValue("GMTENDDATEANDTIME", gmtEnd);

			int startOffSet = timeZone.getOffset(start.getTimeInMillis());
			int endOffSet = timeZone.getOffset(end.getTimeInMillis());
			start.add(Calendar.MILLISECOND, startOffSet);
			end.add(Calendar.MILLISECOND, endOffSet);
			focus.setValue("USERSTARTDATEANDTIME", start);
			focus.setValue("USERENDDATEANDTIME", end);
			focus.setValue("GMTOFFSET", (startOffSet / 1000.0) / 60.0);
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(gmtStart.getTime()), SuggestedCategory.NONE);
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(gmtEnd.getTime()), SuggestedCategory.NONE);
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(start.getTime()), SuggestedCategory.NONE);
			_log.info("LOG_INFO_EXTENSION_AppointmentSave_execute", shortF.format(end.getTime()), SuggestedCategory.NONE);
			// CALCULATE SLOT USAGE (sounds hot)
			int slots = AppointmentUtil.calculateSlots(gmtStart, gmtEnd);
			focus.setValue("SLOTS", slots);

			//validations
			//end > start
			if (gmtEnd.before(gmtStart))
			{
				//exception
				throw new UserException("WMEXP_APPT_INVALID_START_END", new Object[] {});
			}
			if (slots == 0)
			{
				throw new UserException("WMEXP_APPT_INVALID_START_END", new Object[] {});
			}
			//if more than an hour, warning


		}

		private void save()
		{
			focus.save();
		}



	}


}
