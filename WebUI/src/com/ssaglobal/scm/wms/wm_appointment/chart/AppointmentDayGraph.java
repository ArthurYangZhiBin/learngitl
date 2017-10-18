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
import java.sql.SQLException;
import java.text.DateFormat;
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
import com.epiphany.shr.metadata.interfaces.SlotInterface;
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
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentDayChartType;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentSessionObject;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentDayGraph extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppointmentDayGraph.class);

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
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
			SlotInterface slot = currentForm.getSlot();
			RuntimeFormWidgetInterface graphIFrame = currentForm.getFormWidgetByName("GRAPH");

			String doorStart = doorStartWidget.getDisplayValue();
			String doorEnd = doorEndWidget.getDisplayValue();
			
			AppointmentSessionObject appObj = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, state);

			Date date = appObj.getDateCal().getTime();
			java.text.DateFormat shortDF = SimpleDateFormat.getDateInstance(DateFormat.SHORT, state.getUser().getLocale().getJavaLocale());
			
			AppointmentDayChartType chartType = getChartType(state.getSelectedFormNumber(slot));

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

			input.setChartName(shortDF.format(date) + AppointmentUtil.doorFilterTitle(doorStart, doorEnd));
			input.setUserId(userId);
			input.setConnection(conn);

			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(250);
			input.setChartWidth(1075);

			String xAxisLabel;
			if (chartType.equals(AppointmentDayChartType.HOURCHART)) // Day Tab
			{
				input.setTypeName("app_day_hour");
				input.setDateTimeXAxis(true);
				input.setSeries(false);
				xAxisLabel = "POC_JFC_APP_HOUR";
			}
			else
			//Door Tab
			{
				input.setTypeName("app_day_door");
				input.setSeries(false);
				xAxisLabel = "POC_JFC_APP_DOOR";
			}
			input.setState(state);
			input.setDomainAxisLabel(getTextMessage(xAxisLabel, new Object[] {}, state.getLocale()));
			input.setRangeAxisLabel(getTextMessage("POC_JFC_APP_APPOINTMENTS", new Object[] {}, state.getLocale()));

			CategoryDataset data = getReportDataSet(date, doorStart, doorEnd, input, chartType, state);
			input.setDataset(data);
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

	

	private AppointmentDayChartType getChartType(int selectedFormNumber)
	{
		if (selectedFormNumber == 0) // Day Tab
		{
			return AppointmentDayChartType.HOURCHART;
		}
		else
		//Door Tab
		{
			return AppointmentDayChartType.DOORCHART;
		}

	}

	private CategoryDataset getReportDataSet(Date date, String doorStart, String doorEnd, JFreeChartReportObject input, AppointmentDayChartType chartType, StateInterface state) throws SQLException
	{
		//		if (chartType.equals(AppointmentDayChartType.HOURCHART) ) // Day Tab
		//		{
		//			reportSQL = "SELECT DATEADD(HH, DATEDIFF(HH, 0, USERSTARTDATEANDTIME), 0) AS USERSTARTDATE, COUNT(*) APPOINTMENTS " + "FROM APPOINTMENT " + "WHERE USERSTARTDATEANDTIME >= '"
		//					+ mssqlFormat.format(date) + " 00:00:00' " + "AND USERSTARTDATEANDTIME <= '" + mssqlFormat.format(date) + " 23:59:59' "
		//					+ "GROUP BY DATEADD(HH, DATEDIFF(HH, 0, USERSTARTDATEANDTIME), 0) ";
		//			
		//		}
		//		else
		//		//Dock Tab
		//		{
		//			reportSQL = "SELECT DOOR, COUNT(*) APPOINTMENTS " + "FROM APPOINTMENT " + "WHERE USERSTARTDATEANDTIME >= '" + mssqlFormat.format(date) + " 00:00:00' "
		//					+ "AND USERSTARTDATEANDTIME <= '" + mssqlFormat.format(date) + " 23:59:59' " + "GROUP BY DOOR ";
		//			
		//		}
		DateFormat bioDF = new SimpleDateFormat("yyyy/MM/dd");
		String queryString = "wm_appointment.GMTSTARTDATEANDTIME >= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(date) + " 00:00:00'] and "
				+ "wm_appointment.GMTSTARTDATEANDTIME <= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(date) + " 23:59:59']";
		if (!StringUtils.isEmpty(doorStart))
		{
			queryString += " AND wm_appointment.DOOR >= '" + doorStart + "' ";
		}
		if (!StringUtils.isEmpty(doorEnd))
		{
			queryString += " AND wm_appointment.DOOR <= '" + doorEnd + "' ";
		}
		
		Query query = new Query("wm_appointment", queryString, "wm_appointment.GMTSTARTDATEANDTIME DESC");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (chartType.equals(AppointmentDayChartType.HOURCHART))
		{
			Map<String, Integer> dayMap = getDayMap();
			//Query APPOINTMENT
			try
			{
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				BioCollectionBean rs = tuow.getBioCollectionBean(query);
				DateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
				DateFormat shortTime = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, UserUtil.getLocale(state));
				for (int i = 0; i < rs.size(); i++)
				{
					BioBean row = rs.get("" + i);
					Calendar startCal = (Calendar) row.get("GMTSTARTDATEANDTIME");
					//remove Minute value to put it in the right bucket
					startCal.set(Calendar.MINUTE, 0);
					//increment value
					String key = hourMinuteFormat.format(startCal.getTime());
					int existingValue = 0;
					if (dayMap.containsKey(key))
					{
						existingValue = dayMap.get(key);
					}
					existingValue++;
					dayMap.put(key, existingValue);
				}

				for (String key : dayMap.keySet())
				{
					String timeValue = key;
					Date parsedTime;
					try
					{
						parsedTime = hourMinuteFormat.parse(key);
						timeValue = shortTime.format(parsedTime);
					} catch (ParseException e)
					{
						_log.error("LOG_ERROR_EXTENSION_AppointmentDayGraph_getReportDataSet", e.getMessage(), SuggestedCategory.NONE);
					}

					dataset.addValue(dayMap.get(key), "", timeValue);
				}

			} catch (EpiDataException e)
			{
				_log.error("LOG_ERROR_EXTENSION_AppointmentDayGraph_getReportDataSet", e.getErrorMessage(), SuggestedCategory.NONE);
			}
		}
		else
		{
			Map<String, Integer> doorMap = getDoorMap(state, doorStart, doorEnd);

			try
			{
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				BioCollectionBean rs = tuow.getBioCollectionBean(query);
				for (int i = 0; i < rs.size(); i++)
				{
					BioBean row = rs.get("" + i);
					String door = (String) row.get("DOOR");

					//increment value
					String key = door;
					int existingValue = 0;
					if (doorMap.containsKey(key))
					{
						existingValue = doorMap.get(key);
					}
					existingValue++;
					doorMap.put(key, existingValue);
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
		}
		return dataset;
	}

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

	private Map<String, Integer> getDayMap()
	{
		Map<String, Integer> dayMap = new TreeMap<String, Integer>();
		Calendar utilCal = Calendar.getInstance();
		utilCal.set(2000, Calendar.JANUARY, 1, 0, 0);
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < 24; i++)
		{
			utilCal.set(Calendar.HOUR_OF_DAY, i);
			dayMap.put(timeFormat.format(utilCal.getTime()), 0);
		}
		return dayMap;
	}

}
