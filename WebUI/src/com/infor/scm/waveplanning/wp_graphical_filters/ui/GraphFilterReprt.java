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
package com.infor.scm.waveplanning.wp_graphical_filters.ui;

import java.sql.Connection;
import java.util.ArrayList;

import org.jfree.chart.plot.PlotOrientation;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.jFreeChart.JFreeChartReportObject;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCBarChart;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCChartInterface;
import com.infor.scm.waveplanning.jFreeChart.JFreeJDBCPieChart;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.infor.scm.waveplanning.wp_graphical_filters.action.WPGraphicalFilterProceed;
import com.infor.scm.waveplanning.wp_graphical_filters.util.WPGraphFilterUtil;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;



public class GraphFilterReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {
System.out.println("******* it is in GraphFilterReprt ***************");

		try {		   
			StateInterface state = context.getState();  	   		
			String userId = WPUserUtil.getUserId(state);
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			String chartTypeDropdown = "";
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("IFRAME");  	   	   
			RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("NODATAMSG");
			RuntimeFormWidgetInterface chartTypeWidget = currentForm.getFormWidgetByName("CHARTTYPE");
			JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
			JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();

			//mark ma added **************************************************************************
			Query queryBuilderTmp = new Query("querybuildertemp","querybuildertemp.INTERACTIONID = '"+state.getInteractionId()+"'","");
			BioCollectionBean queryBuilderTmpBC = state.getDefaultUnitOfWork().getBioCollectionBean(queryBuilderTmp);
			QueryBuilderInputObj orderLimitInput = new QueryBuilderInputObj();
			orderLimitInput.setMinEachOrderCube(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALCUBESTART")));
			orderLimitInput.setMaxEachOrderCube(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALCUBEEND")));
			orderLimitInput.setMinEachOrderQty(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALQTYSTART")));
			orderLimitInput.setMaxEachOrderQty(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALQTYEND")));
			orderLimitInput.setMinEachOrderLines(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALLINESSTART")));
			orderLimitInput.setMaxEachOrderLines(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALLINESEND")));
			orderLimitInput.setMinEachOrderWeight(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALWEIGHTSTART")));
			orderLimitInput.setMaxEachOrderWeight(Double.parseDouble(queryBuilderTmpBC.elementAt(0).getString("TOTALWEIGHTEND")));
			//end*************************************************************************************
			
			
			ArrayList criterion = (ArrayList)WPUserUtil.getInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_BASE_CRITERION, state);  	   	    
			Query qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"'","");
			BioCollectionBean criteriaCollection = state.getDefaultUnitOfWork().getBioCollectionBean(qry);
			String reportSql =  WPGraphFilterUtil.createQuery(criterion,criteriaCollection,state, orderLimitInput, true);	   	    
			

			WPJFreeChartDatasource ds = new WPJFreeChartDatasource();

			Object dropdownValue = WPUserUtil.getInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_GRAPH_TYPE, state);

			if(dropdownValue != null){
				chartTypeWidget.setValue(dropdownValue.toString());
				if(dropdownValue.toString().equalsIgnoreCase("pie")){
					chartTypeDropdown= "0";			    		
				}
				else if(dropdownValue.toString().equalsIgnoreCase("vertBar")){
					chartTypeDropdown = "1";
				}
				else if(dropdownValue.toString().equalsIgnoreCase("horBar")){
					chartTypeDropdown = "2";
				}		    	    
			}

//			String schema = "wms4000";
			String facility = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase()); 

			//get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");			
			String oahome = SsaAccessBase.getConfig("wavePlanning","waveplanningConfig").getValue("OAHome");						
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app";

			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("graphFilter");						
			input.setChartName("");						
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql(reportSql);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(472);
			input.setChartWidth(615);
			input.setBorderVisible(false);
			input.setDate(criterionContainsDate(criterion));	
			input.setState(state);
			if (chartTypeDropdown.equals("")) {
				if (pieChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {								
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
			else if (chartTypeDropdown.equals("0")) {
				if (pieChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {									
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
			else if (chartTypeDropdown.equals("1")) {
				if (barChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {								
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
			else if (chartTypeDropdown.equals("2")) {
				input.setOrientation(PlotOrientation.HORIZONTAL);
				if (barChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {							
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return RET_CANCEL;          
		}	  
		return RET_CONTINUE;
	}   

	private boolean criterionContainsDate(ArrayList criterionList){
		String criterion = (String)criterionList.get(criterionList.size() - 1);
		return WPGraphFilterUtil.columnIsDate(criterion);
	}
}