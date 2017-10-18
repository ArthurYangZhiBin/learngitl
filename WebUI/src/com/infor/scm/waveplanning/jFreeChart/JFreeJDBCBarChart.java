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
package com.infor.scm.waveplanning.jFreeChart;

import java.awt.Color;
import java.awt.Paint;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class JFreeJDBCBarChart implements JFreeJDBCChartInterface{
	public JFreeChart createJFreeChart(JFreeChartReportObject input){
		Connection conn = null;		
		DefaultCategoryDataset data = null;		
		JFreeChart chart = null;
		boolean dataFound = false;		
		
		try{
		System.out.println("@---$it is  in JDBC Bar Chart******");
		
			conn = input.getConnection();			
			data = new JDBCCategoryDataset(conn);			
			((JDBCCategoryDataset)data).executeQuery(input.getReportSql());			
			
			for(int i = 0; i < data.getColumnCount(); i++) {							
				if (data.getValue(0,i).doubleValue() > 0.0) {					
					dataFound = true;
				}
			}			
			if (dataFound) {
				if(input.isDate()){
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					DateFormat dateFormatShort = DateFormat.getDateInstance(input.getDateFormat(), WPUserUtil.getUserLocale(input.getState()));  
					DateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
					Iterator columnKeyItr = data.getColumnKeys().iterator();	
					//jp.answerlink.278975.begin
					HashMap<String, Integer> tempHolder = new HashMap<String, Integer>();
					//jp.answerlink.278975.end

					while(columnKeyItr.hasNext()){
						Iterator rowKeyItr = data.getRowKeys().iterator();
						Comparable colKey = (Comparable)columnKeyItr.next();						
						
						while(rowKeyItr.hasNext()){
							Comparable rowKey = (Comparable)rowKeyItr.next();		
							Number columnValue = data.getValue(rowKey, colKey);
							try {            		
								Date parsedDate = dateFormatLong.parse(colKey.toString());            		
								String formattedDate = dateFormatShort.format(parsedDate); 
								//jp.answerlink.278975.begin
								//dataset.addValue(columnValue, rowKey.toString(), formattedDate);
								Integer numberObj = tempHolder.get(formattedDate);
								if( numberObj != null){
										Integer newNumberObj = new Integer(numberObj.intValue()+columnValue.intValue());
										tempHolder.put(formattedDate, newNumberObj);
										//dataset.setValue(formattedDate,newNumberObj.intValue());
										dataset.addValue(newNumberObj, rowKey.toString(), formattedDate);
										
								}else{
										tempHolder.put(formattedDate, new Integer(columnValue.intValue()));
										//dataset.setValue(formattedDate,columnValue.intValue());
										dataset.addValue(columnValue, rowKey.toString(), formattedDate);
								}
								//jp.answerlink.278975.end


							} catch (ParseException e) {   	
								dataset.addValue(columnValue, rowKey.toString(), colKey.toString());
							}
						}
					}
					data = dataset;
				}
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
						input.getXAxisLabel(),      //domain axis label
						input.getYAxisLabel(),      //range axis label
						data,                       // data of Bar Chart
						input.getOrientation(),   //orientation
						false,                      //include legends
						true,                       //tooltips
						false);                     //urls
				
				chart.setBorderVisible(input.isBorderVisible());			
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