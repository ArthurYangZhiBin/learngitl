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

package com.ssaglobal.scm.wms.wm_appointment.chart;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeChartReportObject;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCChartInterface;
import com.ssaglobal.scm.wms.JFreeChartReport.Datasource.WebuiJFreeChartDatasource;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentSessionObject;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.GraphWidget;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentDetailGraph extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppointmentDetailGraph.class);

	private Map<String, Integer> getDoorMap(StateInterface state, String doorStart, String doorEnd)
	{
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		Map<String, Integer> doorMap = new TreeMap<String, Integer>();
		String query = "NOT(wm_appointment.DOOR IS NULL) ";
		if (!StringUtils.isEmpty(doorStart))
		{
			query += " AND wm_appointment.DOOR >= '" + doorStart + "' ";
		}
		if (!StringUtils.isEmpty(doorEnd))
		{
			query += " AND wm_appointment.DOOR <= '" + doorEnd + "' ";
		}
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_appointment", query, null));
		try
		{
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean row = rs.get("" + i);
				String door = (String) row.get("DOOR");
				doorMap.put(door, 0);
			}
		} catch (EpiDataException e)
		{
			_log.error("LOG_ERROR_EXTENSION_AppointmentDayGraph_getReportDataSet", e.getErrorMessage(), SuggestedCategory.NONE);
		}

		return doorMap;
	}

	private Map<Integer, Integer> getHourMap()
	{
		Map<Integer, Integer> hourMap = new TreeMap<Integer, Integer>();
		for (int i = 0; i < 24; i++)
		{

			hourMap.put(i, 0);
		}
		return hourMap;
	}

	private CategoryDataset getReportDataSet(StateInterface state, Calendar dayCal, Calendar timeCal, String doorStart, String doorEnd)
	{
		Map<String, Integer> doorMap = getDoorMap(state, doorStart, doorEnd);
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		/*query
		select all the appointments for the date and time
		*/
		DateFormat bioDF = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat bioTF = new SimpleDateFormat("HH:mm:ss");
		DateFormat bioF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date start = new Date();
		Date end = new Date();
		try
		{
			start = bioF.parse(bioDF.format(dayCal.getTime()) + " " + bioTF.format(timeCal.getTime()));
			end = bioF.parse(bioDF.format(dayCal.getTime()) + " " + bioTF.format(timeCal.getTime()));
			Calendar endTemp = Calendar.getInstance();
			endTemp.setTime(end);
			endTemp.add(Calendar.HOUR_OF_DAY, 1);
			end = endTemp.getTime();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String startQueryString = "@DATE['yyyy/MM/dd HH:mm:ss','" + bioF.format(start.getTime()) + "']";
		String endQueryString = "@DATE['yyyy/MM/dd HH:mm:ss','" + bioF.format(end.getTime()) + "']";
		//		String queryString = "wm_appointment.GMTSTARTDATEANDTIME >= @DATE['yyyy/MM/dd HH:mm:ss','" + bioF.format(start) + "'] and "
		//				+ "wm_appointment.GMTSTARTDATEANDTIME <= @DATE['yyyy/MM/dd HH:mm:ss','" + bioF.format(end) + "']";
		String queryString = "(wm_appointment.GMTSTARTDATEANDTIME >= " + startQueryString + " and wm_appointment.GMTSTARTDATEANDTIME <= " + endQueryString + ") " 
				+ " OR "
				+ "(wm_appointment.GMTSTARTDATEANDTIME <= " + startQueryString + " and wm_appointment.GMTENDDATEANDTIME >= " + startQueryString 
					+ " and wm_appointment.GMTENDDATEANDTIME <= " + endQueryString + " ) " 
				+ " OR " 
				+ "(wm_appointment.GMTSTARTDATEANDTIME >= " + startQueryString + " and wm_appointment.GMTSTARTDATEANDTIME <= " + endQueryString
					+ " and wm_appointment.GMTENDDATEANDTIME >= " + endQueryString + " ) " 
				+ " OR " 
					+ "(wm_appointment.GMTSTARTDATEANDTIME <= " + startQueryString + " and wm_appointment.GMTENDDATEANDTIME >= " + endQueryString + " ) ";
		if (!StringUtils.isEmpty(doorStart))
		{
			queryString += " AND wm_appointment.DOOR >= '" + doorStart + "' ";
		}
		if (!StringUtils.isEmpty(doorEnd))
		{
			queryString += " AND wm_appointment.DOOR <= '" + doorEnd + "' ";
		}
		_log.info("LOG_INFO_EXTENSION_AppointmentDetailGraph_getReportDataSet", queryString, SuggestedCategory.NONE);
		Query query = new Query("wm_appointment", queryString, "wm_appointment.GMTSTARTDATEANDTIME DESC");

		Calendar queryStart = Calendar.getInstance();
		queryStart.setTime(start);
		Calendar queryEnd = Calendar.getInstance();
		queryEnd.setTime(end);

		try
		{
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			BioCollectionBean rs = tuow.getBioCollectionBean(query);
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean row = rs.get("" + i);
				_log.info("LOG_INFO_EXTENSION_AppointmentDetailGraph_getReportDataSet", "APPT " + row.getValue("APPOINTMENTKEY"), SuggestedCategory.NONE);
				String door = (String) row.get("DOOR");

				Calendar apptStart = (Calendar) ((Calendar) row.getValue("GMTSTARTDATEANDTIME")).clone();
				Calendar apptEnd = (Calendar) ((Calendar) row.getValue("GMTENDDATEANDTIME")).clone();

				//Handle Boundary Conditions
				if (apptStart.before(queryStart))
				{
					apptStart = (Calendar) queryStart.clone();
				}
				if (apptEnd.after(queryEnd))
				{
					apptEnd = (Calendar) queryEnd.clone();
				}

				int slots = 0;

				slots = AppointmentUtil.calculateSlots(apptStart, apptEnd);

				//increment value
				String key = door;
				AppointmentUtil.incrementKeyInMap(doorMap, key, slots);
			}

			for (String key : doorMap.keySet())
			{

				dataset.addValue(doorMap.get(key), "", key);
			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset;
	}

	private CategoryDataset getReportDataSet(StateInterface state, Calendar dayCal, String door)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<Integer, Integer> hourMap = getHourMap();

		/*query
		select all the appointments for the date and door
		*/
		DateFormat bioDF = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat bioF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat hourTF = new SimpleDateFormat("HH");
		DateFormat timeTF = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, UserUtil.getLocale(state));
		DateFormat shortF = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.LONG);

		String startQueryString = "@DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(dayCal.getTime()) + " 00:00:00']";
		String endQueryString = "@DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(dayCal.getTime()) + " 23:59:59']";
		String queryString = "((wm_appointment.GMTSTARTDATEANDTIME >= " + startQueryString + " and wm_appointment.GMTSTARTDATEANDTIME <= " + endQueryString + ") "
						+ " OR "
								+ "(wm_appointment.GMTSTARTDATEANDTIME <= " + startQueryString + " and wm_appointment.GMTENDDATEANDTIME >= " + startQueryString + " and wm_appointment.GMTENDDATEANDTIME <= " + endQueryString + " ) "
								+ " OR "
								+ "(wm_appointment.GMTSTARTDATEANDTIME >= " + startQueryString + " and wm_appointment.GMTSTARTDATEANDTIME <= " + endQueryString + " and wm_appointment.GMTENDDATEANDTIME >= " + endQueryString + " ) "
								+ " OR "
								+ "(wm_appointment.GMTSTARTDATEANDTIME <= " + startQueryString + " and wm_appointment.GMTENDDATEANDTIME >= " + endQueryString + " )) "
				+ " and (wm_appointment.DOOR = '" + door	+ "')";
		_log.info("LOG_INFO_EXTENSION_AppointmentDetailGraph_getReportDataSet", queryString, SuggestedCategory.NONE);
		Query query = new Query("wm_appointment", queryString, " wm_appointment.GMTSTARTDATEANDTIME DESC");

		Calendar queryStartCal = (Calendar) dayCal.clone();
		queryStartCal.set(Calendar.HOUR_OF_DAY, 0);
		queryStartCal.set(Calendar.MINUTE, 0);
		queryStartCal.set(Calendar.SECOND, 0);
		queryStartCal.set(Calendar.MILLISECOND, 0);
		Calendar queryEndCal = (Calendar) dayCal.clone();
		queryEndCal.set(Calendar.HOUR_OF_DAY, 23);
		queryEndCal.set(Calendar.MINUTE, 59);
		queryEndCal.set(Calendar.SECOND, 59);
		queryEndCal.set(Calendar.MILLISECOND, 999);
		try
		{

			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			BioCollectionBean rs = tuow.getBioCollectionBean(query);
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean row = rs.get("" + i);
				_log.info("LOG_INFO_EXTENSION_AppointmentDetailGraph_getReportDataSet", "APPT " + row.getValue("APPOINTMENTKEY"), SuggestedCategory.NONE);
				Calendar start = (Calendar) ((Calendar) row.getValue("GMTSTARTDATEANDTIME")).clone();
				Calendar end = (Calendar) ((Calendar) row.getValue("GMTENDDATEANDTIME")).clone();
				end.set(Calendar.SECOND, 0);
				end.set(Calendar.MILLISECOND, 0);

				//Handle Boundary Conditions
				if (start.before(queryStartCal))
				{
					start = (Calendar) queryStartCal.clone();
				}
				if (end.after(queryEndCal))
				{
					end = (Calendar) queryEndCal.clone();
				}
				
				
				
				
				
				
				Calendar nextHour = (Calendar) start.clone();
				nextHour.set(Calendar.MINUTE, 0);
				nextHour.set(Calendar.SECOND, 0);
				nextHour.set(Calendar.MILLISECOND, 0);
				nextHour.add(Calendar.HOUR_OF_DAY, 1);
				

				
				if (end.after(nextHour))
				{
					//need to do some magic
					//calculate for original hour
					int slots = AppointmentUtil.calculateSlots(start, nextHour);
					String hour = hourTF.format(start.getTime());
					int key = Integer.valueOf(hour);
					AppointmentUtil.incrementKeyInMap(hourMap, key, slots);

					int remainingSlots = AppointmentUtil.calculateSlots(nextHour, end);
					while (remainingSlots > 0)
					{
						//if remaining slots has more than 4
						if (remainingSlots / 4 >= 1)
						{
							key++;
							if (key < 24)
							{
								AppointmentUtil.incrementKeyInMap(hourMap, key, 4);
							}
							remainingSlots -= 4;
						}
						//if the remaining slots has less than 4
						else if (remainingSlots % 4 < 4)
						{
							key++;
							if (key < 24)
							{
								AppointmentUtil.incrementKeyInMap(hourMap, key, remainingSlots);
							}
							remainingSlots -= remainingSlots;
						}
						else
						{
							//this isn't good code
							//safety measure here
							break;
							//							key++;
							//							if (key < 24)
							//							{
							//								AppointmentUtil.incrementKeyInMap(hourMap, key, 4);
							//							}
							//							remainingSlots -= 4;
						}
					}

				}
				else
				{
					int slots = AppointmentUtil.calculateSlots(start, end);

					//increment value
					String hour = hourTF.format(start.getTime());
					int key = Integer.valueOf(hour);

					AppointmentUtil.incrementKeyInMap(hourMap, key, slots);
				}

			}
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(2);

			for (int key : hourMap.keySet())
			{
				try
				{
					Date time = hourTF.parse(nf.format(key));

					dataset.addValue(hourMap.get(key), "", timeTF.format(time));
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					dataset.addValue(hourMap.get(key), "", "" + key);
				}

			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset;

	}

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		try
		{
			StateInterface state = context.getState();

			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = FormUtil.findForm(currentForm, "wm_appointment_shell", "wm_appointment_shell", state);
			RuntimeFormWidgetInterface doorStartWidget = shellForm.getFormWidgetByName("DOORSTART");
			RuntimeFormWidgetInterface doorEndWidget = shellForm.getFormWidgetByName("DOOREND");
			RuntimeFormWidgetInterface graphIFrame = currentForm.getFormWidgetByName("GRAPH");

			AppointmentSessionObject appObj = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, state);
			SessionUtil.setInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, appObj, state);

			//Parameters
			String doorStart = doorStartWidget.getDisplayValue();
			String doorEnd = doorEndWidget.getDisplayValue();
			Calendar dayCal = (Calendar) appObj.getDateCal().clone();
			Calendar timeCal = (Calendar) (appObj.getTimeCal() != null ? appObj.getTimeCal().clone() : null);
			String door = appObj.getDoor();
			GraphWidget sourceWidget = appObj.getWidget();

			JFreeJDBCChartInterface barChart = new AppointmentOverviewBarChart();
			String userId = (String) context.getServiceManager().getUserContext().get("logInUserId");
			HttpSession session = state.getRequest().getSession();
			WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
			String facility = (String) session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase());
			// get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			String oahome = appAccess.getValue("webUIConfig", "OAHome");
			String path = oahome + fileSeparator + "shared" + fileSeparator + "webroot" + fileSeparator + "app";

			JFreeChartReportObject input = new JFreeChartReportObject();

			input.setUserId(userId);
			input.setConnection(conn);

			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(250);
			input.setChartWidth(1075);

			String xAxisLabel = null;
			if (sourceWidget.equals(GraphWidget.SELECTHOUR)) // Slots By Door for Selected Hour
			{
				DateFormat shortTimeDF = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, UserUtil.getLocale(state));
				input.setChartName(shortTimeDF.format(timeCal.getTime()) + AppointmentUtil.doorFilterTitle(doorStart, doorEnd));
				input.setTypeName("app_detail_hour");
				input.setSeries(true);
				input.setLimit(4);
				xAxisLabel = "POC_JFC_APP_DOOR";
				CategoryDataset data = getReportDataSet(state, dayCal, timeCal, doorStart, doorEnd);
				input.setDataset(data);
			}
			else
			{
				input.setChartName(door);
				input.setTypeName("app_detail_door");
				input.setSeries(true);
				input.setLimit(4);
				xAxisLabel = "POC_JFC_APP_HOUR";
				CategoryDataset data = getReportDataSet(state, dayCal, door);
				input.setDataset(data);
				input.setDateTimeXAxis(true);
			}

			input.setState(state);
			input.setDomainAxisLabel(getTextMessage(xAxisLabel, new Object[] {}, state.getLocale()));
			input.setRangeAxisLabel(getTextMessage("POC_JFC_APP_SLOTS", new Object[] {}, state.getLocale()));

			if (barChart.createJFreeChart(input) == null)
			{
				graphIFrame.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
			}
			else
			{
				graphIFrame.setProperty("src", "jfreechartHTMLTmp/" + input.getHtmlImageMapTmpFileName());

			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
