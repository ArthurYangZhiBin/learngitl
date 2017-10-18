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

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Arrays;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class JFreeJDBCPieChart implements JFreeJDBCChartInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JFreeJDBCPieChart.class);
	public JFreeChart createJFreeChart(JFreeChartReportObject input){
		Connection conn = null;
		JDBCPieDataset data = null;
		JFreeChart chart = null;		
		boolean dataFound = false;		
		
		try{
			_log.debug("LOG_SYSTEM_OUT","@---$it is  in JDBC Pie Chart******",100L);
			
			conn = input.getConnection();
			data = new JDBCPieDataset(conn);
			data.executeQuery(input.getReportSql());			
					
			for(int i = 0; i < data.getItemCount(); i++) {				
				if (data.getValue(i).intValue() > 0) {					
					dataFound = true;
				}
			}			
			if (dataFound) {
				chart = ChartFactory.createPieChart(
						input.getChartName(),  //titile
						data,                  //data
						false,                 //legend
						true,                  //tooltips
						false);                //URLS
				
				chart.setBorderVisible(true);
				if(StringUtils.isNotBlank(input.getSubTitle())) {
					TextTitle subtitle = new TextTitle(input.getSubTitle());
			        chart.addSubtitle(subtitle);
				}
				PiePlot plot = (PiePlot)chart.getPlot();
				chart.setBackgroundPaint(plot.getBackgroundPaint());				
				NumberFormat np = NumberFormat.getPercentInstance(); 
				np.setMinimumFractionDigits(2);					
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
						"{0} = {2}", NumberFormat.getInstance(), np));				
				plot.setOutlinePaint(null);
				plot.setOutlineStroke(null);
				input.setChart(chart);
				input.createChart();				
			}			
			conn.close();			
		}catch(Exception e){
			e.printStackTrace();			
			System.err.println(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if (dataFound == true) {			
			return chart;
		}
		else {			
			return null;
		}
	}
}

