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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

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
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeChartReportObject;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCChartInterface;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentOverviewGraph extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	private static final int DEFAULT_VALUE_DATE_RANGE = 13;

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
			RuntimeFormInterface shellForm = currentForm.getParentForm(state);
			RuntimeFormWidgetInterface selectedDateWidget = shellForm.getFormWidgetByName("DATE");
			RuntimeFormWidgetInterface rangeWidget = shellForm.getFormWidgetByName("RANGE");
			RuntimeFormWidgetInterface graphIFrame = currentForm.getFormWidgetByName("GRAPH");
			RuntimeFormWidgetInterface doorStartWidget = shellForm.getFormWidgetByName("DOORSTART");
			RuntimeFormWidgetInterface doorEndWidget = shellForm.getFormWidgetByName("DOOREND");

			Calendar selectedDate = selectedDateWidget.getCalendarValue();
			if (selectedDate == null)
			{
				selectedDate = new GregorianCalendar(ReportUtil.getTimeZone(state), state.getUser().getLocale().getJavaLocale());
				selectedDateWidget.setCalendarValue(selectedDate);
			}
			int range = StringUtils.isEmpty(((String) rangeWidget.getValue())) ? DEFAULT_VALUE_DATE_RANGE : Integer.valueOf(((String) rangeWidget.getValue()));
			Calendar startDate = (Calendar) selectedDate.clone();
			Calendar endDate = (Calendar) selectedDate.clone();
			endDate.add(Calendar.DATE, range);
			String doorStart = doorStartWidget.getDisplayValue();
			String doorEnd = doorEndWidget.getDisplayValue();

			// MSS
			//			String reportSQL = "SELECT DATEADD(DD, DATEDIFF(DD, 0, USERSTARTDATEANDTIME), 0) AS USERSTARTDATE, COUNT(*) APPOINTMENTS " + "FROM APPOINTMENT " + "WHERE USERSTARTDATEANDTIME >= '"
			//					+ dayFormat.format(startDate.getTime()) + " 00:00:00' " + "AND USERSTARTDATEANDTIME <= '" + dayFormat.format(endDate.getTime()) + " 23:59:59' "
			//					+ "GROUP BY DATEADD(DD, DATEDIFF(DD, 0, USERSTARTDATEANDTIME), 0)" + "ORDER BY DATEADD(DD, DATEDIFF(DD, 0, USERSTARTDATEANDTIME), 0)";
			//			
			DefaultCategoryDataset dataset = getReportDataSet(state, startDate, endDate, doorStart, doorEnd);

			JFreeJDBCChartInterface barChart = new AppointmentOverviewBarChart();
			String userId = (String) context.getServiceManager().getUserContext().get("logInUserId");


			// get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			String oahome = appAccess.getValue("webUIConfig", "OAHome");
			String path = oahome + fileSeparator + "shared" + fileSeparator + "webroot" + fileSeparator + "app";

			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("app_overview");
			input.setChartName(getTextMessage("POC_JFC_APP_OVERVIEW", new Object[] {}, state.getLocale()) + AppointmentUtil.doorFilterTitle(doorStart, doorEnd));
			input.setUserId(userId);
			input.setDataset(dataset);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(162);
			input.setChartWidth(1075);
			input.setDateXAxis(true);
			input.setState(state);
			input.setDomainAxisLabel(getTextMessage("POC_JFC_APP_DAY", new Object[] {}, state.getLocale()));
			input.setRangeAxisLabel(getTextMessage("POC_JFC_APP_APPOINTMENTS", new Object[] {}, state.getLocale()));
			input.setSeries(false);

			input.setDataset(dataset);

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

	private DefaultCategoryDataset getReportDataSet(StateInterface state, Calendar startDate, Calendar endDate, String doorStart, String doorEnd) throws EpiDataException
	{
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat bioDF = new SimpleDateFormat("yyyy/MM/dd");
		String queryString = "wm_appointment.GMTSTARTDATEANDTIME >= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(startDate.getTime()) + " 00:00:00'] and "
				+ "wm_appointment.GMTSTARTDATEANDTIME <= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(endDate.getTime()) + " 23:59:59']";
		if (!StringUtils.isEmpty(doorStart))
		{
			queryString += " AND wm_appointment.DOOR >= '" + doorStart + "'";
		}
		if (!StringUtils.isEmpty(doorEnd))
		{
			queryString += " AND wm_appointment.DOOR <= '" + doorEnd + "'";
		}

		Query query = new Query("wm_appointment", queryString, "wm_appointment.GMTSTARTDATEANDTIME DESC");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rs = tuow.getBioCollectionBean(query);

		Map<String, Integer> daysMap = new TreeMap<String, Integer>();
		for (int i = 0; i < rs.size(); i++)
		{
			BioBean row = rs.get("" + i);
			Calendar startCal = (Calendar) row.get("GMTSTARTDATEANDTIME");
			//remove Minute value to put it in the right bucket
			startCal.set(Calendar.MINUTE, 0);
			//increment value
			String key = dayFormat.format(startCal.getTime());
			int existingValue = 0;
			if (daysMap.containsKey(key))
			{
				existingValue = daysMap.get(key);
			}
			existingValue++;
			daysMap.put(key, existingValue);
		}

		for (String key : daysMap.keySet())
		{
			dataset.addValue(daysMap.get(key), "", key);
		}
		return dataset;
	}
}
