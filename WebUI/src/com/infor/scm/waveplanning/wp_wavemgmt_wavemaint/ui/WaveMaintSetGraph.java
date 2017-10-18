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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

// Import 3rd party packages and classes
import java.sql.Connection;

import org.jfree.chart.plot.PlotOrientation;

import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.jFreeChart.JFreeChartReportObject;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCBarChart;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCPieChart;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.infor.scm.waveplanning.common.WavePlanningConfiguration;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintSetGraph extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintSetGraph.class);

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
		StateInterface state = context.getState();
		try
		{
			RuntimeFormInterface parentForm = form.getParentForm(state);
			RuntimeFormWidgetInterface viewDropDown = parentForm.getFormWidgetByName("VIEWOPTION");
			RuntimeFormWidgetInterface graphIFrame = form.getFormWidgetByName("GRAPH");

			String chartType = (String) viewDropDown.getValue();
			_log.info("LOG_INFO_EXTENSION_WaveMaintSetGraph_preRenderForm", "Graph Type " + chartType, SuggestedCategory.NONE);
			String schema = WPUtil.getSchemaNameOfWarehouseConnection(state);
			Object value = parentForm.getFocus().getValue("WAVEKEY");
			String waveKey = "";
			if(value != null){
				waveKey = value.toString();
			}
			String reportQuery = "SELECT CODELKUP_STATUS.DESCRIPTION,WP_WAVESTATUS.UNITS " + "FROM   WP_WAVESTATUS "
					+ "INNER JOIN (SELECT CODE,DESCRIPTION " + "FROM " + schema + ".CODELKUP CODELKUP "
					+ "WHERE  (LISTNAME = 'WSVIEW')) CODELKUP_STATUS "
					+ "ON WP_WAVESTATUS.STATUS = CODELKUP_STATUS.CODE " + "WHERE  (WP_WAVESTATUS.WAVEKEY = '"
					+ waveKey + "') "
					+ "AND (WP_WAVESTATUS.STATUS IN ('0','1','2','3','4')) " + "AND (WP_WAVESTATUS.WHSEID = '"
					+ BioAttributeUtil.getString(parentForm.getFocus(), "WHSEID") + "') ";

			_log.info("LOG_INFO_EXTENSION_WaveMaintSetGraph_preRenderForm", reportQuery, SuggestedCategory.NONE);
			WPJFreeChartDatasource ds = new WPJFreeChartDatasource();
			JFreeJDBCPieChart pieChart = new JFreeJDBCPieChart();
			JFreeJDBCBarChart barChart = new JFreeJDBCBarChart();
			
			String facility = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase());
//			Connection conn = ds.getConnection("wms4000");
			String oahome = WavePlanningConfiguration.getConfig("wavePlanning", "waveplanningConfig")
					.getValue("OAHome");
			String fileSeparator = System.getProperties().getProperty("file.separator");
			String path = oahome + fileSeparator + "shared" + fileSeparator + "webroot" + fileSeparator + "app";
			String userid = WPUserUtil.getUserId(state);

			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("wave");
			input.setChartName(getTextMessage("WP_JFC_WAVE_STATUS", new Object[] {}, state.getLocale()));// "Wave Status");
			input.setXAxisLabel(getTextMessage("WP_JFC_WAVE_ORDER_STATUS", new Object[] {}, state.getLocale()));// "Wave Order Status");
			input.setYAxisLabel(getTextMessage("WP_JFC_MASS_UNIT", new Object[] {}, state.getLocale()));// "Mass Unit");
			input.setUserId(userid);
			input.setConnection(conn);
			input.setReportSql(reportQuery);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(270-40);
			input.setChartWidth(400-30);
			input.setState(state);

			/*
			INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','WDISPTYPE','0','List View','0','List View','en');
			INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','WDISPTYPE','1','Pie Chart','1','Pie Chart','en');
			INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','WDISPTYPE','2','Horizontal Bar Chart','2','Horizontal Bar Chart','en');
			INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','WDISPTYPE','3','Vertical Bar Chart','3','Vertical Bar Chart','en');

			 */
			if (chartType == null || chartType.equals("") || chartType.equals("1"))
			{
				if (pieChart.createJFreeChart(input) == null)
				{
					//LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else
				{
					graphIFrame.setProperty("src", "jfreechartHTMLTmp/" + input.getHtmlImageMapTmpFileName());
				}
			}
			else if (chartType.equals("2"))
			{
				input.setOrientation(PlotOrientation.HORIZONTAL);
				if (barChart.createJFreeChart(input) == null)
				{
					//LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else
				{
					graphIFrame.setProperty("src", "jfreechartHTMLTmp/" + input.getHtmlImageMapTmpFileName());
				}
			}
			else if (chartType.equals("3"))
			{
				input.setOrientation(PlotOrientation.VERTICAL);
				if (barChart.createJFreeChart(input) == null)
				{
					//LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else
				{
					graphIFrame.setProperty("src", "jfreechartHTMLTmp/" + input.getHtmlImageMapTmpFileName());
				}
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a form in a modal window. Write code
	 * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
	 * to the end user
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
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

	/**
	 * Called in response to the modifySubSlot event on a form. Write code
	 * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
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

	/**
	 * Called in response to the modifySubSlot event on a form in a modal window. Write code
	 * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
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

	/**
	 * Called in response to the setFocusInForm event on a form. Write code
	 * to change the focus of this form. This code is executed everytime irrespective of whether the form
	 * is being redisplayed or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form) throws EpiException
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

	/**
	 * Called in response to the setFocusInForm event on a form in a modal window. Write code
	 * to change the focus of this form. This code is executed everytime irrespective of whether the form
	 * is being redisplayed or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form) throws EpiException
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

	/**
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
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

	/**
	 * Called in response to the pre-render event on a list form in a modal dialog. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
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

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
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

	/**
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
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
