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
import org.jfree.chart.plot.PlotOrientation;

import com.epiphany.shr.ui.state.StateInterface;


public class JFreeChartReportObject {
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
	private String userId="";
	private String chartName="";
	private String typeName = "";
	private String xAxisLabel = "";
	private String yAxisLabel = "";
	private PlotOrientation orientation = PlotOrientation.VERTICAL;
	private boolean borderVisible = true;
	private boolean isDate = false;
	private int dateFormat = DateFormat.SHORT;
	private String labelFormat = "{0} = {2}";	
	private StateInterface state = null;
	
	public String getLabelFormat() {
		return labelFormat;
	}
	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}
	public int getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(int dateFormat) {
		this.dateFormat = dateFormat;
	}
	public boolean isDate() {
		return isDate;
	}
	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}
	public boolean isBorderVisible() {
		return borderVisible;
	}
	public void setBorderVisible(boolean borderVisible) {
		this.borderVisible = borderVisible;
	}
	public String getReportSql(){
		return this.reportSql;
	}
	public void setReportSql(String reportSql){
		this.reportSql = reportSql;
	}
	public String getDbConnectionURL(){
		return this.dbConnectionURL;
	}
	public void setDbConnectionURL(String dbConnectionURL){
		this.dbConnectionURL = dbConnectionURL;
	}
	public Connection getConnection(){
		return this.conn;
	}
	public void setConnection(Connection dbConnection){
		this.conn = dbConnection;
	}
	public String getTypeName(){
		return this.typeName;
	}
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	public String getHtmlImageMapTmpPath(){
		return this.htmlImageMapTmpPath;
	}
	public void setHtmlImageMapTmpPath(String htmlImageMapTmpPath){
		this.htmlImageMapTmpPath = htmlImageMapTmpPath;
	}
	public String getHtmlImageMapTmpFileName (){		
		return this.htmlImageMapTmpFileName;
	}
	public void setHtmlImageMapTmpFileName(){
		this.htmlImageMapTmpFileName = this.userId+"_"+this.typeName+"_"+this.sessionId+"_"+System.currentTimeMillis()+".html" ;
	}
	public String getPngImageMapTmpFileName (){		
		return this.pngImageMapTmpFileName;
	}
	public void setPngImageMapTmpFileName(){
		this.pngImageMapTmpFileName = this.userId+"_"+this.typeName+"_"+this.sessionId+"_"+System.currentTimeMillis()+".png" ;
	}
	public String getSessionId(){
		return this.sessionId;
	}
	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}
	public JFreeChart getChart(){
		return this.chart;
	}
	public void setChart(JFreeChart chart){
		this.chart = chart;
	}
	public int getChartWidth(){
		return this.chartWidth;
	}
	public void setChartWidth(int chartWidth){
		this.chartWidth = chartWidth;
	}
	public int getChartHeight(){
		return this.chartHeight;
	}
	public void setChartHeight(int chartHeight){
		this.chartHeight = chartHeight;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getChartName(){
		return this.chartName;
	}
	public void setChartName(String chartName){
		this.chartName= chartName;
	}
	public void createChart(){		
		OutputStream outHtml = null;
		FileOutputStream out = null;
		try{		
			System.out.println("\n\nlog1\n\n");
			//clear the temp files for this session
			String fileSeparator = System.getProperties().getProperty("file.separator");
			System.out.println("\n\nlog2:"+fileSeparator+"\n\n");
			File tempFolder = new File(this.getHtmlImageMapTmpPath()+fileSeparator+"jfreechartHTMLTmp");
			System.out.println("\n\nlog3:"+this.getHtmlImageMapTmpPath()+fileSeparator+"jfreechartHTMLTmp"+"\n\n");
			File [] files = tempFolder.listFiles();
			System.out.println("\n\nlog4\n\n");
			File tempFile = null;
			System.out.println("\n\nlog5\n\n");
			String tempFileName = "";
			System.out.println("\n\nlog6\n\n");
			int size = files.length;
			System.out.println("\n\nlog7\n\n");
			if(size != 0){
				System.out.println("\n\nlog8\n\n");
				for(int i=0; i<size; i++){
					System.out.println("\n\nlog9\n\n");
					tempFile = files[i];		
					System.out.println("\n\nlog10\n\n");
					tempFileName = tempFile.getName();
					System.out.println("\n\nlog11\n\n");
					if(tempFileName.startsWith(this.getUserId() + "_" + this.getTypeName())){
						System.out.println("\n\nlog12\n\n");
						tempFile.delete();
					}					
				}
			}
			System.out.println("\n\nlog13\n\n");
	        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
	        
			out = new FileOutputStream(this.getHtmlImageMapTmpPath()+fileSeparator+"jfreechartHTMLTmp"+fileSeparator+this.getPngImageMapTmpFileName());
			System.out.println("After PNG FIle name = "+ this.getPngImageMapTmpFileName());
			ChartUtilities.writeChartAsPNG(out, this.chart, this.chartWidth, this.chartHeight, info);
			Collection entityCollection = info.getEntityCollection().getEntities();
			Iterator it = entityCollection.iterator();
			ChartEntity ce = null;		
			String tooltip ="";			
			if (!typeName.equalsIgnoreCase("cube")) {			
				while(it.hasNext()){
					ce = (ChartEntity)it.next();
					if (ce instanceof PieSectionEntity) {					
						tooltip = ce.getToolTipText();
						System.out.println("tooltip="+tooltip);
						ce.setURLText("javascript:openRequest('"+getPieTooltipDesc(tooltip)+"')");					
					}
					else if (ce instanceof CategoryItemEntity) {										
						tooltip = ce.getToolTipText();
						System.out.println("tooltip="+tooltip);
						String statusDesc = getBarTooltipDesc(tooltip);					
						String toolTipText = statusDesc + " " + "=" + " " + getBarTooltipValue(tooltip);					
						ce.setToolTipText(toolTipText);										
						ce.setURLText("javascript:openRequest('"+statusDesc+"')");
					}
				}
			}			
			else if (typeName.equalsIgnoreCase("cube")) {				
				while(it.hasNext()){
					ce = (ChartEntity)it.next();
					if (ce instanceof CategoryItemEntity) {						
						tooltip = ce.getToolTipText();
						System.out.println("tooltip="+tooltip);
						String statusDesc = getBarTooltipDesc(tooltip);					
						String toolTipText = statusDesc + " " + "=" + " " + getBarTooltipValue(tooltip);					
						ce.setToolTipText(toolTipText);					
					}
				}
			}
			
	        // write an HTML page incorporating the image with an image map
			
			File htmlFile = new File(this.getHtmlImageMapTmpPath()+fileSeparator+"jfreechartHTMLTmp"+fileSeparator+this.getHtmlImageMapTmpFileName());
			outHtml = new BufferedOutputStream(new FileOutputStream(htmlFile));
	        PrintWriter writer = new PrintWriter(outHtml);
	        writer.println("<HTML>");
	        writer.println("<HEAD><TITLE></TITLE>");	        
	        StringBuffer openRequest = new StringBuffer();
	        if (!typeName.equalsIgnoreCase("cube")) {
	        	openRequest.append("<script type='text/javascript'> ");
	        	openRequest.append("function openRequest(tooltip){");
	        	openRequest.append(" if(parent && parent.gEpnyStagingFrameManager){");
	        	openRequest.append("parent.gEpnyStagingFrameManager.userRequestJS(");
	        }
//	        openRequest.append("'afname', 'wm_JFreeChart_Extension_tmp',");
	        if (typeName.equalsIgnoreCase("graphFilter")) {
		        openRequest.append("'afname', 'wp_graphical_filters_iframe_action',");
	        	openRequest.append("'afwname', 'wp_graphical_filters_click',");
	        	
	        }
	        else if( typeName.equalsIgnoreCase("wave")) {
	        	openRequest.append("'afname', 'wp_wavemgmt_wavemaint_graph_action',");
	        	openRequest.append("'afwname', 'wp_wavemgmt_wavemaint_click',");
	        }
	        else if( typeName.equalsIgnoreCase("testChart")) {
	        	openRequest.append("'afname', 'wp_test_drill_through_form',");
	        	openRequest.append("'afwname', 'wp_test_drill_through_click',");
	        }
//	        else if (typeName.equalsIgnoreCase("pkdet")){
//		        openRequest.append("'afname', 'wm_JFreeChart_Extension_tmp',");
//	        	openRequest.append("'afwname', 'pickdetail_extension',");
//	        }
//	        else if (typeName.equalsIgnoreCase("asn")){
//		        openRequest.append("'afname', 'wm_inbound_charts_action_view',");
//	        	openRequest.append("'afwname', 'wm_asn_click',");
//	        }
//	        else if (typeName.equalsIgnoreCase("po")){
//		        openRequest.append("'afname', 'wm_inbound_charts_action_view',");
//	        	openRequest.append("'afwname', 'wm_po_click',");
//	        }
//	        else if (typeName.equalsIgnoreCase("tkdet")){
//		        openRequest.append("'afname', 'wm_execution_charts_action_view',");
//	        	openRequest.append("'afwname', 'wm_taskdetail_click',");
//	        }
//	        else if (typeName.equalsIgnoreCase("MFBPie") || typeName.equalsIgnoreCase("MFBBar")){
//		        openRequest.append("'afname', 'wm_multi_facility_charts_action',");
//	        	openRequest.append("'afwname', 'wm_multifacility_click',");
//	        }

	     //   if (!typeName.equalsIgnoreCase("cube")) {
	        	openRequest.append("'tooltip', tooltip");
	        	openRequest.append(");");
	        	openRequest.append("}else{alert('This can only be used when running in an OA application');}");
	        	openRequest.append("}");
	        	openRequest.append("</script>");	        
	        	writer.println(openRequest.toString());
	   //     }
			writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
	        writer.println("</HEAD>");
	        writer.println("<BODY>");
	        writer.println("<IMG SRC=\""+this.getPngImageMapTmpFileName()+"\" "
	                + "WIDTH=\""+this.chartWidth+"\" HEIGHT=\""+this.chartHeight+"\" BORDER=\"0\" USEMAP=\"#chart\">");
	        ChartUtilities.writeImageMap(writer, "chart", info, false);
	        writer.println("</BODY>");
	        writer.println("</HTML>");
	        writer.flush();
	        writer.close();
		
	        outHtml.close();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e) {
		     e.printStackTrace();		              
		}
		finally{
			try{
				if(outHtml != null){
					outHtml.close();
				}
				if(out != null){
					out.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	private String getPieTooltipDesc(String tooltip){
		int index = tooltip.indexOf("=");
		return tooltip.substring(0, index).trim();
	}
	
	private String getBarTooltipDesc(String tooltip){
		int startIndex = tooltip.indexOf(",");
		int endIndex = tooltip.indexOf(")");
		return tooltip.substring(startIndex + 2, endIndex).trim();
	}
	
	private String getBarTooltipValue(String tooltip){
		int startIndex = tooltip.indexOf("=");
		int endIndex = tooltip.length();		
		return tooltip.substring(startIndex + 2, endIndex).trim();
	}
	public void setXAxisLabel(String xAxisLabel)
	{
		this.xAxisLabel = xAxisLabel;
	}
	public String getXAxisLabel()
	{
		return xAxisLabel;
	}
	public void setYAxisLabel(String yAxisLabel)
	{
		this.yAxisLabel = yAxisLabel;
	}
	public String getYAxisLabel()
	{
		return yAxisLabel;
	}
	public void setOrientation(PlotOrientation orientation)
	{
		this.orientation = orientation;
	}
	public PlotOrientation getOrientation()
	{
		return orientation;
	}
	public StateInterface getState() {
		return state;
	}
	public void setState(StateInterface state) {
		this.state = state;
	}
	
}
