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
package com.infor.scm.waveplanning.wp_test.ui;

import java.sql.Connection;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.jFreeChart.JFreeChartReportObject;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCBarChart;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCChartInterface;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCPieChart;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.ssaglobal.SsaAccessBase;



public class TestFilterReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	   protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
	 try {		   
  	   		StateInterface state = context.getState();
  	   		 
  	   		String userId = (String)state.getServiceManager().getUserContext().get("logInUserId");
  	   		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
  	   		
  	   	    RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("IFRAME");
  	   	    String chartTypeDropdown = "";
  	   	    
  	   	    RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("NODATAMSG");
  	   	    JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
  	   	    JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
  	   	    
  	   	    String reportSql =  "SELECT STORERKEY, count(ORDERKEY) FROM wp_orderheader WHERE STORERKEY IS NOT NULL GROUP BY STORERKEY";	   	    

  	   	    
  	   	    WPJFreeChartDatasource ds = new WPJFreeChartDatasource();   		        		
		    
  	   	    
			String schema = "wms4000";
			Connection conn = ds.getConnection(schema);
			
			//get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");	
			
			
			String oahome = "C:\\infor";						
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app";
			
			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("testChart");						
			input.setChartName("");						
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql(reportSql);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(560);
			input.setChartWidth(595);
			
//			if (chartTypeDropdown.equals("")) {
				if (pieChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {								
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
//			}
//			if (chartTypeDropdown.equals("0")) {
//				if (pieChart.createJFreeChart(input) == null ) {				
//					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
//				}
//				else {				
//					System.out.println("\n\nChart3\n\n");
//					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
//				}
//			}
//			if (chartTypeDropdown.equals("1")) {
//				if (barChart.createJFreeChart(input) == null ) {				
//					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
//				}
//				else {			
//					System.out.println("\n\nChart4\n\n");
//					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
//				}
//			}
	  } catch(Exception e) {
	     e.printStackTrace();
	     return RET_CANCEL;          
	  }	  
	  return RET_CONTINUE;
	}   
}