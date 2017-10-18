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

import java.sql.Connection;
import java.text.AttributedString;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.text.DateFormatter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class JFreeJDBCPieChart implements JFreeJDBCChartInterface{
	public JFreeChart createJFreeChart(JFreeChartReportObject input){
		Connection conn = null;
		DefaultPieDataset data = null;		
		JFreeChart chart = null;		
		boolean dataFound = false;		
		
		try{
			System.out.println("@---$it is  in JDBC Pie Chart******");
			
			conn = input.getConnection();
			data = new JDBCPieDataset(conn);
			((JDBCPieDataset)data).executeQuery(input.getReportSql());			
			for(int i = 0; i < data.getItemCount(); i++) {				
				if (data.getValue(i).intValue() > 0) {					
					dataFound = true;
				}
			}			
			if (dataFound) {
				if(input.isDate()){
					DefaultPieDataset dataset = new DefaultPieDataset();
					DateFormat dateFormatShort = DateFormat.getDateInstance(input.getDateFormat(), WPUserUtil.getUserLocale(input.getState()));  
					DateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
					Iterator columnKeyItr = data.getKeys().iterator();	
					HashMap<String, Integer> tempHolder = new HashMap<String, Integer>();
					int s = 0;					
					while(columnKeyItr.hasNext()){
						Comparable colKey = (Comparable)columnKeyItr.next();						
						Number columnValue = data.getValue(colKey);
						Date parsedDate = dateFormatLong.parse(colKey.toString());            		
						String formattedDate = dateFormatShort.format(parsedDate);
						Integer numberObj = tempHolder.get(formattedDate);
						if( numberObj != null){
								Integer newNumberObj = new Integer(numberObj.intValue()+columnValue.intValue());
								tempHolder.put(formattedDate, newNumberObj);
								dataset.setValue(formattedDate,newNumberObj.intValue());
						}else{
								tempHolder.put(formattedDate, new Integer(columnValue.intValue()));
								dataset.setValue(formattedDate,columnValue.intValue());								
						}
					}
					data = dataset;
				}
				chart = ChartFactory.createPieChart(
						input.getChartName(),  //titile
						data,                  //data
						false,                 //legend
						true,                  //tooltips
						false);                //URLS
				
				chart.setBorderVisible(input.isBorderVisible());
				PiePlot plot = (PiePlot)chart.getPlot();
				chart.setBackgroundPaint(plot.getBackgroundPaint());								
				NumberFormat np = NumberFormat.getPercentInstance(); 
				np.setMinimumFractionDigits(2);					
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
						input.getLabelFormat(), NumberFormat.getInstance(), np));								
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

