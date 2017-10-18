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

import java.awt.Color;
import java.awt.Paint;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

import com.ssaglobal.scm.wms.JFreeChartReport.JFreeChartReportObject;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCChartInterface;

public class AppointmentOverviewBarChart implements JFreeJDBCChartInterface
{

	private static final String MIDDLE = "B";

	private static final String BOTTOM = "C";

	private static final String TOP = "A";

	public JFreeChart createJFreeChart(JFreeChartReportObject input)
	{
		Connection conn = null;
		DefaultCategoryDataset data = null;
		JFreeChart chart = null;
		boolean dataFound = false;

		try
		{
			data = (DefaultCategoryDataset) input.getDataset();

			if (data != null)
			{
					dataFound = true;
			
			}
			if (dataFound)
			{
				if (input.isDateXAxis())
				{
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					DateFormat dateFormatShort = DateFormat.getDateInstance(input.getDateFormat(), input.getState().getLocale().getJavaLocale());
					DateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd", input.getState().getLocale().getJavaLocale());
					DateFormat dateFormatDay = new SimpleDateFormat("EEE", input.getState().getLocale().getJavaLocale());
					Iterator columnKeyItr = data.getColumnKeys().iterator();

					while (columnKeyItr.hasNext())
					{
						Iterator rowKeyItr = data.getRowKeys().iterator();
						Comparable colKey = (Comparable) columnKeyItr.next();

						while (rowKeyItr.hasNext())
						{
							Comparable rowKey = (Comparable) rowKeyItr.next();
							Number columnValue = data.getValue(rowKey, colKey);
							try
							{
								Date parsedDate = dateFormatLong.parse(colKey.toString());
								String formattedDate = dateFormatShort.format(parsedDate);
								String formattedDay = dateFormatDay.format(parsedDate);
								dataset.addValue(columnValue, rowKey.toString(), formattedDay + " " + formattedDate);

							} catch (ParseException e)
							{
								dataset.addValue(columnValue, rowKey.toString(), colKey.toString());
							}
						}
					}
					data = dataset;
				}
				if (input.isDateTimeXAxis())
				{
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					DateFormat dateFormatShort = DateFormat.getTimeInstance(input.getDateFormat(), input.getState().getLocale().getJavaLocale());
					DateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Iterator columnKeyItr = data.getColumnKeys().iterator();
					while (columnKeyItr.hasNext())
					{
						Iterator rowKeyItr = data.getRowKeys().iterator();
						Comparable colKey = (Comparable) columnKeyItr.next();

						while (rowKeyItr.hasNext())
						{
							Comparable rowKey = (Comparable) rowKeyItr.next();
							Number columnValue = data.getValue(rowKey, colKey);
							try
							{
								Date parsedDate = dateFormatLong.parse(colKey.toString());
								String formattedDate = dateFormatShort.format(parsedDate);
								dataset.addValue(columnValue, rowKey.toString(), formattedDate);

							} catch (ParseException e)
							{
								dataset.addValue(columnValue, rowKey.toString(), colKey.toString());
							}
						}
					}
					data = dataset;
				}
				if (input.isSeries())
				{
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					//Specific Limit set
					if (input.getLimit() != null)
					{
						int upperLimit = input.getLimit();
						Iterator columnKeyItr = data.getColumnKeys().iterator();
						while (columnKeyItr.hasNext())
						{
							Iterator rowKeyItr = data.getRowKeys().iterator();
							Comparable colKey = (Comparable) columnKeyItr.next();

							while (rowKeyItr.hasNext())
							{
								Comparable rowKey = (Comparable) rowKeyItr.next();
								Number columnValue = data.getValue(rowKey, colKey);
								if (columnValue.doubleValue() >= upperLimit)
								{
									dataset.addValue(columnValue, TOP, colKey);
								}
								else
								{
									dataset.addValue(columnValue, BOTTOM, colKey);
								}
							}
						}
					}
					else
					{

						//Normal Series

						SortedSet<Number> values = new TreeSet<Number>();
						{
							Iterator columnKeyItr = data.getColumnKeys().iterator();
							while (columnKeyItr.hasNext())
							{
								Iterator rowKeyItr = data.getRowKeys().iterator();
								Comparable colKey = (Comparable) columnKeyItr.next();

								while (rowKeyItr.hasNext())
								{
									Comparable rowKey = (Comparable) rowKeyItr.next();
									Number columnValue = data.getValue(rowKey, colKey);
									values.add(columnValue);
								}
							}
						}
						Number max = values.last();
						Number min = values.first();
						double upperLimit = max.doubleValue();
						double lowerLimit = min.doubleValue();
						if (min.doubleValue() > lowerLimit)
						{
							lowerLimit = min.doubleValue();
						}

						Iterator columnKeyItr = data.getColumnKeys().iterator();
						while (columnKeyItr.hasNext())
						{
							Iterator rowKeyItr = data.getRowKeys().iterator();
							Comparable colKey = (Comparable) columnKeyItr.next();

							while (rowKeyItr.hasNext())
							{
								Comparable rowKey = (Comparable) rowKeyItr.next();
								Number columnValue = data.getValue(rowKey, colKey);
								if (columnValue.doubleValue() >= upperLimit)
								{
									dataset.addValue(columnValue, TOP, colKey);
								}
								else if (columnValue.doubleValue() > lowerLimit && columnValue.doubleValue() < upperLimit)
								{
									dataset.addValue(columnValue, MIDDLE, colKey);
								}
								else
								{
									dataset.addValue(columnValue, BOTTOM, colKey);
								}
							}
						}
					}
					data = dataset;
				}
				/*
				 * chart = ChartFactory.createBarChart( input.getChartName(),
				 * //title "Status", //domain axis label "Count", //range axis
				 * label data, // data of Bar Chart PlotOrientation.VERTICAL,
				 * //orientation false, //include legends true, //tooltips
				 * false); //urls
				 */
				if (input.isSeries())
				{
					chart = ChartFactory.createStackedBarChart3D(input.getChartName(), // title
							input.getDomainAxisLabel(), // domain axis label
							input.getRangeAxisLabel(), // range axis label
							data, // data of Bar Chart
							PlotOrientation.VERTICAL, // orientation
							false, // include legends
							true, // tooltips
							false); // urls
				}
				else
				{
					chart = ChartFactory.createBarChart3D(input.getChartName(), // title
							input.getDomainAxisLabel(), // domain axis label
							input.getRangeAxisLabel(), // range axis label
							data, // data of Bar Chart
							PlotOrientation.VERTICAL, // orientation
							false, // include legends
							true, // tooltips
							false); // urls
				}

				chart.setBorderVisible(true);
				CategoryPlot plot = chart.getCategoryPlot();
				plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
				if (input.isDateXAxis())
				{
					plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
				}
				if (input.isDateTimeXAxis())
				{
					plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
				}

				chart.setBackgroundPaint(plot.getBackgroundPaint());
				plot.setOutlinePaint(null);
				plot.setOutlineStroke(null);
				if (!input.isSeries())
				{
					CategoryItemRenderer r = new CustomRenderer(new Paint[] { Color.GRAY });
					//					CategoryItemRenderer r = new CustomRenderer(new Paint[] { Color.red, Color.blue, Color.green, Color.yellow, Color.magenta, Color.cyan, Color.orange, Color.GRAY });
					r.setToolTipGenerator(new StandardCategoryToolTipGenerator());
					plot.setRenderer(r);
				}
				else
				{
					CategoryItemRenderer renderer = plot.getRenderer();
					StackedBarRenderer3D r = (StackedBarRenderer3D) plot.getRenderer();
					r.setMinimumBarLength(2);
					int top = data.getRowIndex(TOP);
					int bottom = data.getRowIndex(BOTTOM);
					int middle = data.getRowIndex(MIDDLE);
					if (top != -1)
					{
						renderer.setSeriesPaint(top, Color.red);
					}
					if (middle != -1)
					{
						renderer.setSeriesPaint(middle, Color.yellow);
					}
					if (bottom != -1)
					{
						renderer.setSeriesPaint(bottom, Color.green);
					}
					renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
					plot.setRenderer(renderer);
				}
				input.setChart(chart);
				input.createChart();
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally
		{
			try
			{
				if (conn != null)
				{
					conn.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (dataFound == true)
		{
			return chart;
		}
		else
		{
			return null;
		}
	}

	/*
	 * / A custom renderer that returns a different color for each item in a
	 * single series.
	 */
	// class CustomRenderer extends BarRenderer {
	class CustomRenderer extends BarRenderer3D
	{

		/** The colors. */
		private final Paint[] colors;

		public CustomRenderer(final Paint[] colors)
		{
			this.colors = colors;
		}

		/**
		 * Returns the paint for an item. Overrides the default behaviour
		 * inherited from AbstractSeriesRenderer.
		 * 
		 * @param row
		 *            the series.
		 * @param column
		 *            the category.
		 * 
		 * @return The item color.
		 */
		@Override
		public Paint getItemPaint(final int row, final int column)
		{

			return this.colors[column % this.colors.length];
		}
	}

}
