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

import java.awt.Color;
import java.awt.Paint;
import java.sql.*;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.*;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class JFreeJDBCBarChart implements JFreeJDBCChartInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JFreeJDBCBarChart.class);

	public JFreeChart createJFreeChart(JFreeChartReportObject input){
		Connection conn = null;		
		JDBCCategoryDataset data = null;		
		JFreeChart chart = null;
		boolean dataFound = false;		
		
		try{
		_log.debug("LOG_SYSTEM_OUT","@---$it is  in JDBC Bar Chart******",100L);
		
			conn = input.getConnection();			
			data = new JDBCCategoryDataset(conn);			
			data.executeQuery(input.getReportSql());			
			
			for(int i = 0; i < data.getColumnCount(); i++) {				
				if (data.getValue(0,i).doubleValue() > 0.0) {					
					dataFound = true;
				}
			}			
			if (dataFound) {				
				/*chart = ChartFactory.createBarChart(
							input.getChartName(),       //title
							"Status",                   //domain axis label
							"Count",                    //range axis label
							data,                       // data of Bar Chart
							PlotOrientation.VERTICAL,   //orientation
							false,                      //include legends
							true,                       //tooltips
							false);                     //urls	*/		
				chart = ChartFactory.createBarChart3D(
						input.getChartName(),       //title
						input.getDomainAxisLabel(),	//domain axis label
						input.getRangeAxisLabel(),	//range axis label
						data,                       // data of Bar Chart
						PlotOrientation.VERTICAL,   //orientation
						false,                      //include legends
						true,                       //tooltips
						false);                     //urls
				
				chart.setBorderVisible(true);
				if(StringUtils.isNotBlank(input.getSubTitle())) {
					TextTitle subtitle = new TextTitle(input.getSubTitle());
			        chart.addSubtitle(subtitle);
				}
				//CategoryPlot plot = (CategoryPlot)chart.getPlot();
				CategoryPlot plot = chart.getCategoryPlot();				
				chart.setBackgroundPaint(plot.getBackgroundPaint());
				plot.setOutlinePaint(null);				
				plot.setOutlineStroke(null);
				CategoryItemRenderer r = new CustomRenderer(new Paint[] {Color.red, Color.blue, Color.green, Color.yellow, Color.magenta, Color.cyan, Color.orange,Color.GRAY});	
				r.setToolTipGenerator(new StandardCategoryToolTipGenerator());				
				plot.setRenderer(r);
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
	
	/*
	/**
     * A custom renderer that returns a different color for each item in a single series.
     */
    //class CustomRenderer extends BarRenderer {
	class CustomRenderer extends BarRenderer3D {

        /** The colors. */
        private Paint[] colors;
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    } 

}