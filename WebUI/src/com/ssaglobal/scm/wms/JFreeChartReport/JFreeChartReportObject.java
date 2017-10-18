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
package com.ssaglobal.scm.wms.JFreeChartReport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.category.CategoryDataset;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class JFreeChartReportObject
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JFreeChartReportObject.class);

	private String reportSql = null;

	private String dbConnectionURL = null;

	private String htmlImageMapTmpPath = null;

	private String htmlImageMapTmpFileName = "";

	private String pngImageMapTmpFileName = "";

	private String sessionId = "";

	private JFreeChart chart = null;

	private int chartWidth = 0;

	private int chartHeight = 0;

	private Connection conn = null;

	private String userId = "";

	private String chartName = "";
	
	private String subTitle = "";

	private String typeName = "";

	private String domainAxisLabel = "Status";

	private String rangeAxisLabel = "Count";

	private boolean dateXAxis = false;

	private boolean dateTimeXAxis = false;

	private final int dateFormat = DateFormat.SHORT;

	private StateInterface state = null;

	private boolean series = false;
	
	private Integer limit;

	private CategoryDataset dataset;

	public void createChart()
	{
		OutputStream outHtml = null;
		FileOutputStream out = null;
		try
		{
			//clear the temp files for this session
			String fileSeparator = System.getProperties().getProperty("file.separator");
			File tempFolder = new File(this.getHtmlImageMapTmpPath() + fileSeparator + "jfreechartHTMLTmp");
			File[] files = tempFolder.listFiles();
			File tempFile = null;
			String tempFileName = "";
			int size = files.length;
			if (size != 0)
			{
				for (int i = 0; i < size; i++)
				{
					tempFile = files[i];
					tempFileName = tempFile.getName();
					if (tempFileName.startsWith(this.getUserId() + "_" + this.getTypeName()))
					{
						tempFile.delete();
					}
				}
			}

			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

			out = new FileOutputStream(this.getHtmlImageMapTmpPath() + fileSeparator + "jfreechartHTMLTmp" + fileSeparator + this.getPngImageMapTmpFileName());
			_log.debug("LOG_SYSTEM_OUT","After PNG FIle name = " + this.getPngImageMapTmpFileName(),100L);
			ChartUtilities.writeChartAsPNG(out, this.chart, this.chartWidth, this.chartHeight, info);
			Collection entityCollection = info.getEntityCollection().getEntities();
			Iterator it = entityCollection.iterator();
			ChartEntity ce = null;
			String tooltip = "";
			if (!typeName.equalsIgnoreCase("cube"))
			{
				while (it.hasNext())
				{
					ce = (ChartEntity) it.next();
					if (ce instanceof PieSectionEntity)
					{
						tooltip = ce.getToolTipText();
						_log.debug("LOG_SYSTEM_OUT","tooltip=" + tooltip,100L);
						ce.setURLText("javascript:openRequest('" + getPieTooltipDesc(tooltip) + "')");
					}
					else if (ce instanceof CategoryItemEntity)
					{
						tooltip = ce.getToolTipText();
						_log.debug("LOG_SYSTEM_OUT","tooltip=" + tooltip,100L);
						String statusDesc = getBarTooltipDesc(tooltip);
						String toolTipText = statusDesc + " " + "=" + " " + getBarTooltipValue(tooltip);
						ce.setToolTipText(toolTipText);
						ce.setURLText("javascript:openRequest('" + statusDesc + "')");
					}
				}
			}
			else if (typeName.equalsIgnoreCase("cube"))
			{
				while (it.hasNext())
				{
					ce = (ChartEntity) it.next();
					if (ce instanceof CategoryItemEntity)
					{
						tooltip = ce.getToolTipText();
						_log.debug("LOG_SYSTEM_OUT","tooltip=" + tooltip,100L);
						String statusDesc = getBarTooltipDesc(tooltip);
						String toolTipText = statusDesc + " " + "=" + " " + getBarTooltipValue(tooltip);
						ce.setToolTipText(toolTipText);
					}
				}
			}

			// write an HTML page incorporating the image with an image map

			File htmlFile = new File(this.getHtmlImageMapTmpPath() + fileSeparator + "jfreechartHTMLTmp" + fileSeparator + this.getHtmlImageMapTmpFileName());
			outHtml = new BufferedOutputStream(new FileOutputStream(htmlFile));
			PrintWriter writer = new PrintWriter(outHtml);
			writer.println("<HTML>");
			writer.println("<HEAD><TITLE></TITLE>");
			StringBuffer openRequest = new StringBuffer();
			if (!typeName.equalsIgnoreCase("cube"))
			{
				openRequest.append("<script type='text/javascript'> ");
				openRequest.append("function openRequest(tooltip){");
				openRequest.append(" if(parent && parent.gEpnyStagingFrameManager){");
				openRequest.append("parent.gEpnyStagingFrameManager.userRequestJS(");
			}
			//	        openRequest.append("'afname', 'wm_JFreeChart_Extension_tmp',");
			if (typeName.equalsIgnoreCase("shord"))
			{
				openRequest.append("'afname', 'wm_JFreeChart_Extension_tmp',");
				openRequest.append("'afwname', 'shipment_extension',");

			}
			else if (typeName.equalsIgnoreCase("pkdet"))
			{
				openRequest.append("'afname', 'wm_JFreeChart_Extension_tmp',");
				openRequest.append("'afwname', 'pickdetail_extension',");
			}
			else if (typeName.equalsIgnoreCase("asn"))
			{
				openRequest.append("'afname', 'wm_inbound_charts_action_view',");
				openRequest.append("'afwname', 'wm_asn_click',");
			}
			else if (typeName.equalsIgnoreCase("po"))
			{
				openRequest.append("'afname', 'wm_inbound_charts_action_view',");
				openRequest.append("'afwname', 'wm_po_click',");
			}
			else if (typeName.equalsIgnoreCase("tkdet"))
			{
				openRequest.append("'afname', 'wm_execution_charts_action_view',");
				openRequest.append("'afwname', 'wm_taskdetail_click',");
			}
			else if (typeName.equalsIgnoreCase("tktyp"))
			{
				openRequest.append("'afname', 'wm_execution_charts_action_view',");
				openRequest.append("'afwname', 'wm_taskdetail_tasktype_click',");
			}
			else if (typeName.equalsIgnoreCase("MFBPie") || typeName.equalsIgnoreCase("MFBBar"))
			{
				openRequest.append("'afname', 'wm_multi_facility_charts_action',");
				openRequest.append("'afwname', 'wm_multifacility_click',");
			}
			else if (typeName.equalsIgnoreCase("app_overview"))
			{
				openRequest.append("'afname', 'wm_appointment_shell',");
				openRequest.append("'afwname', 'SELECTDAY',");
				openRequest.append("'sourceformpath', 'wm_appointment_shell',");

			}
			else if (typeName.equalsIgnoreCase("app_day_hour"))
			{
				openRequest.append("'afname', 'wm_appointment_shell',");
				openRequest.append("'afwname', 'SELECTHOUR',");
				openRequest.append("'sourceformpath', 'wm_appointment_shell',");

			}
			else if (typeName.equalsIgnoreCase("app_day_door"))
			{
				openRequest.append("'afname', 'wm_appointment_shell',");
				openRequest.append("'afwname', 'SELECTDOOR',");
				openRequest.append("'sourceformpath', 'wm_appointment_shell',");

			}
			else if (typeName.equalsIgnoreCase("app_detail_hour"))
			{
				openRequest.append("'afname', 'wm_appointment_shell',");
				openRequest.append("'afwname', 'SELECTDETAILDOOR',");
				openRequest.append("'sourceformpath', 'wm_appointment_shell',");

			}
			else if (typeName.equalsIgnoreCase("app_detail_door"))
			{
				openRequest.append("'afname', 'wm_appointment_shell',");
				openRequest.append("'afwname', 'SELECTDETAILHOUR',");
				openRequest.append("'sourceformpath', 'wm_appointment_shell',");

			}

			if (!typeName.equalsIgnoreCase("cube"))
			{
				openRequest.append("'tooltip', tooltip");
				openRequest.append(");");
				openRequest.append("}else{alert('This can only be used when running in an OA application');}");
				openRequest.append("}");
				openRequest.append("</script>");
				writer.println(openRequest.toString());
			}
			writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
			writer.println("</HEAD>");
			writer.println("<BODY>");
			writer.println("<IMG SRC=\"" + this.getPngImageMapTmpFileName() + "\" " + "WIDTH=\"" + this.chartWidth + "\" HEIGHT=\"" + this.chartHeight + "\" BORDER=\"0\" USEMAP=\"#chart\">");
			ChartUtilities.writeImageMap(writer, "chart", info, false);
			writer.println("</BODY>");
			writer.println("</HTML>");
			writer.flush();
			writer.close();

			outHtml.close();
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (outHtml != null)
				{
					outHtml.close();
				}
				if (out != null)
				{
					out.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	private String getBarTooltipDesc(String tooltip)
	{
		int startIndex = tooltip.indexOf(",");
		int endIndex = tooltip.indexOf(")");
		return tooltip.substring(startIndex + 2, endIndex).trim();
	}

	private String getBarTooltipValue(String tooltip)
	{
		int startIndex = tooltip.indexOf("=");
		int endIndex = tooltip.length();
		return tooltip.substring(startIndex + 2, endIndex).trim();
	}

	public JFreeChart getChart()
	{
		return this.chart;
	}

	public int getChartHeight()
	{
		return this.chartHeight;
	}

	public String getChartName()
	{
		return this.chartName;
	}

	public int getChartWidth()
	{
		return this.chartWidth;
	}

	public Connection getConnection()
	{
		return this.conn;
	}

	public CategoryDataset getDataset()
	{
		return dataset;
	}

	public int getDateFormat()
	{
		return dateFormat;
	}

	public String getDbConnectionURL()
	{
		return this.dbConnectionURL;
	}

	public String getDomainAxisLabel()
	{
		return domainAxisLabel;
	}

	public String getHtmlImageMapTmpFileName()
	{
		return this.htmlImageMapTmpFileName;
	}

	public String getHtmlImageMapTmpPath()
	{
		return this.htmlImageMapTmpPath;
	}

	private String getPieTooltipDesc(String tooltip)
	{
		int index = tooltip.indexOf("=");
		return tooltip.substring(0, index).trim();
	}

	public String getPngImageMapTmpFileName()
	{
		return this.pngImageMapTmpFileName;
	}

	public String getRangeAxisLabel()
	{
		return rangeAxisLabel;
	}

	public String getReportSql()
	{
		return this.reportSql;
	}

	public String getSessionId()
	{
		return this.sessionId;
	}

	public StateInterface getState()
	{
		return state;
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public boolean isDateTimeXAxis()
	{
		return dateTimeXAxis;
	}

	public boolean isDateXAxis()
	{
		return dateXAxis;
	}

	public boolean isSeries()
	{
		return series;
	}

	public void setChart(JFreeChart chart)
	{
		this.chart = chart;
	}

	public void setChartHeight(int chartHeight)
	{
		this.chartHeight = chartHeight;
	}

	public void setChartName(String chartName)
	{
		this.chartName = chartName;
	}

	public void setChartWidth(int chartWidth)
	{
		this.chartWidth = chartWidth;
	}

	public void setConnection(Connection dbConnection)
	{
		this.conn = dbConnection;
	}

	public void setDataset(CategoryDataset dataset)
	{
		this.dataset = dataset;
	}

	public void setDateTimeXAxis(boolean dateTimeXAxis)
	{
		this.dateTimeXAxis = dateTimeXAxis;
	}

	public void setDateXAxis(boolean dateXAxis)
	{
		this.dateXAxis = dateXAxis;
	}

	public void setDbConnectionURL(String dbConnectionURL)
	{
		this.dbConnectionURL = dbConnectionURL;
	}

	public void setDomainAxisLabel(String domainAxisLabel)
	{
		this.domainAxisLabel = domainAxisLabel;
	}

	public void setHtmlImageMapTmpFileName()
	{
		this.htmlImageMapTmpFileName = this.userId + "_" + this.typeName + "_" + this.sessionId + "_" + System.currentTimeMillis() + ".html";
	}

	public void setHtmlImageMapTmpPath(String htmlImageMapTmpPath)
	{
		this.htmlImageMapTmpPath = htmlImageMapTmpPath;
	}

	public void setPngImageMapTmpFileName()
	{
		this.pngImageMapTmpFileName = this.userId + "_" + this.typeName + "_" + this.sessionId + "_" + System.currentTimeMillis() + ".png";
	}

	public void setRangeAxisLabel(String rangeAxisLabel)
	{
		this.rangeAxisLabel = rangeAxisLabel;
	}

	public void setReportSql(String reportSql)
	{
		this.reportSql = reportSql;
	}

	public void setSeries(boolean series)
	{
		this.series = series;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public void setState(StateInterface state)
	{
		this.state = state;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setLimit(Integer limit)
	{
		this.limit = limit;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}



}
