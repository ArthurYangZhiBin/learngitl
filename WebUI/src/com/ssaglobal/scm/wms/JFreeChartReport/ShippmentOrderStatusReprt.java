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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.JFreeChartReport.Datasource.WebuiJFreeChartDatasource;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;



public class ShippmentOrderStatusReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShippmentOrderStatusReprt.class);
	   @Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
	 try {		   
			SimpleDateFormat mssqlFormat = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat oraFormat = new SimpleDateFormat("yyyy-MM-dd");
 	   		StateInterface state = context.getState();  	   		 
  	   		HttpSession session = state.getRequest().getSession();
  	   		String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
  	   		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
  	   	    RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("shipmentOrder_iframe");
  	   	    RuntimeFormWidgetInterface chartTypeDropdown = currentForm.getFormWidgetByName("shpordcharttype");
  	   	    RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("shipmentorder_message_label");
  	   	    
  	   	   
  	   	    
			JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
			JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
			String reportSelectClause  = "select T0.C0 Order_Status, T0.C1 Total_Orders ";
  	   	    String reportFromClause  = "FROM (select ORDERSTATUSSETUP.DESCRIPTION C0, count(ORDERS.ORDERKEY) C1 from ORDERSTATUSSETUP ORDERSTATUSSETUP INNER JOIN ORDERS ORDERS on ORDERS.STATUS = ORDERSTATUSSETUP.CODE ";
  	   	    String reportWhereClause  = "";
  	   	    String reportGroupByClause  = "group by ORDERSTATUSSETUP.DESCRIPTION) T0";
 /*SCM-00000-03750 	   	    
  	   	    if(WSDefaultsUtil.isOwnerLocked(state)){
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		reportWhereClause += " AND ORDERS.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			reportWhereClause += " ,'"+lockedOwners.get(i)+"'";
  	   	    		}
  	   	    		reportWhereClause += " ) ";
  	   	    	}  	
  	   	    	ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
	   	    	if(lockedCustomers != null && lockedCustomers.size() > 0){
	   	    		reportWhereClause += " AND (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";
	   	    		for(int i = 1; i < lockedCustomers.size(); i++){
	   	    			reportWhereClause += " , '"+lockedCustomers.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = '' OR ORDERS.CONSIGNEEKEY = ' ') ";
	   	    	}
	   	    	ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
	   	    	if(lockedCarriers != null && lockedCarriers.size() > 0){
	   	    		reportWhereClause += " AND (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
	   	    		for(int i = 1; i < lockedCarriers.size(); i++){
	   	    			reportWhereClause += " , '"+lockedCarriers.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = '' OR ORDERS.CarrierCode = ' ') ";
	   	    	}
	   	    	ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
	   	    	if(lockedBillTo != null && lockedBillTo.size() > 0){
	   	    		reportWhereClause += " AND (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
	   	    		for(int i = 1; i < lockedBillTo.size(); i++){
	   	    			reportWhereClause += " , '"+lockedBillTo.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = '' OR ORDERS.BILLTOKEY = ' ') ";
	   	    	}
  	   	    }
  	   	    SCM-00000-03750*/
//SCM-00000-03750.b
  	   	    if(WSDefaultsUtil.isOwnerLocked(state)){
  	   	    	boolean found = false;
  	   	    	
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		reportWhereClause += "WHERE ORDERS.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			reportWhereClause += " ,'"+lockedOwners.get(i)+"'";
  	   	    		}
  	   	    		reportWhereClause += " ) ";
  	   	    		found = true;
  	   	    	}  	
  	   	    	ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
	   	    	if(lockedCustomers != null && lockedCustomers.size() > 0){
	   	    		if (found){
		   	    		reportWhereClause += " AND (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";	   	    			
	   	    		}else{
		   	    		reportWhereClause += "WHERE (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";
	   	    		}
	   	    		for(int i = 1; i < lockedCustomers.size(); i++){
	   	    			reportWhereClause += " , '"+lockedCustomers.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = '' OR ORDERS.CONSIGNEEKEY = ' ') ";
	   	    		found = true;
	   	    	}
	   	    	ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
	   	    	if(lockedCarriers != null && lockedCarriers.size() > 0){
	   	    		if (found){
		   	    		reportWhereClause += " AND (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";	   	    			
	   	    		}else{
		   	    		reportWhereClause += "WHERE (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
	   	    		}
	   	    		for(int i = 1; i < lockedCarriers.size(); i++){
	   	    			reportWhereClause += " , '"+lockedCarriers.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = '' OR ORDERS.CarrierCode = ' ') ";
	   	    		found = true;
	   	    	}
	   	    	ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
	   	    	if(lockedBillTo != null && lockedBillTo.size() > 0){
	   	    		if (found){
		   	    		reportWhereClause += " AND (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
	   	    		}else{
		   	    		reportWhereClause += "WHERE (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
	   	    		}
	   	    		for(int i = 1; i < lockedBillTo.size(); i++){
	   	    			reportWhereClause += " , '"+lockedBillTo.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = '' OR ORDERS.BILLTOKEY = ' ') ";
	   	    	}
	   	    }
//SCM-00000-03750.e

			// Bugaware 8581. Chart graphs - logic and ascetic changes
			Calendar endDate = GregorianCalendar.getInstance();
			String numDays = "1";

			if (session.getAttribute("wm_sdate_param") != null) {
				endDate = (Calendar) ((Calendar) session.getAttribute("wm_sdate_param")).clone();
			}

			if (session.getAttribute("wm_days_param") != null) {
				numDays = session.getAttribute("wm_days_param").toString();
			} 	
			
			
  	   	    
  	   	    Calendar startDate = (Calendar) endDate.clone();
			// hack to get the current day inclusive
			// if you don't specify the time, the range is from start date 00:00:00 - end date 00:00:00.
			// since in this case the end date is specified, it'll look like start date 00:00:00 - end date + 1 00:00:00
  			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
  			HttpSession sessionUser = state.getRequest().getSession();
  			String serverType = (String) sessionUser.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
  			if (serverType == null)
  			{
  				serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
  			}
  			String eDate ="", sDate="";
  	   	    
  	   	    
			endDate.add(Calendar.DATE, 1);
			startDate.add(Calendar.DATE, Integer.parseInt(numDays) * -1+1);
			if("MSS".equalsIgnoreCase(serverType)){
				sDate = mssqlFormat.format(startDate.getTime());
				eDate = mssqlFormat.format(endDate.getTime());
			}else if("O90".equalsIgnoreCase(serverType)){
				sDate = "to_date('"+oraFormat.format(startDate.getTime())+"', 'YYYY-MM-DD')";
				eDate = "to_date('"+oraFormat.format(endDate.getTime())+"', 'YYYY-MM-DD')";
			}
 
			
  	   	    if (reportWhereClause.length() > 0)
  	   	    {
  				if("MSS".equalsIgnoreCase(serverType)){
   					reportWhereClause = reportWhereClause+" AND ORDERS.REQUESTEDSHIPDATE BETWEEN '"+eDate+"' AND '"+sDate+"'";
  				}else if("O90".equalsIgnoreCase(serverType)){
   					reportWhereClause = reportWhereClause+" AND ORDERS.REQUESTEDSHIPDATE BETWEEN "+eDate+" AND "+sDate+" ";  				
  				}
  	   	    }
  	   	    else
  	   	    {  	   	    
  				if("MSS".equalsIgnoreCase(serverType)){
  					reportWhereClause = "WHERE ORDERS.REQUESTEDSHIPDATE BETWEEN '" + sDate + "' AND '" + eDate + "' ";
  				}else if("O90".equalsIgnoreCase(serverType)){
  					reportWhereClause = "WHERE ORDERS.REQUESTEDSHIPDATE BETWEEN " + sDate + " AND " + eDate + " ";  				
  				}

  			}
  	   	      	   	    
  	   	    String reportSql =  reportSelectClause+reportFromClause+reportWhereClause+reportGroupByClause;	 
  	   	    _log.debug("LOG_SYSTEM_OUT","#####reportSql = "+ reportSql,100L);

			WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
		    
		    Object dropdownValue = session.getAttribute("shpordcharttype");		    
		    if(dropdownValue != null){
		    	chartTypeDropdown.setValue(dropdownValue.toString());		    	
		    }
		    
			String facility = (String)session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase());
			_log.debug("LOG_SYSTEM_OUT","facility="+facility+"  conn="+conn,100L);
			
			//get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			String oahome = appAccess.getValue("webUIConfig","OAHome");
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app";
			
			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("shord");
			input.setChartName(getTextMessage("WM_JFC_SO_STATUS", new Object[] {}, state.getLocale()));
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql(reportSql);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(475);			
			input.setChartWidth(375);
			input.setDomainAxisLabel(getTextMessage("WM_JFC_STATUS", new Object[] {}, state.getLocale()));
			input.setRangeAxisLabel(getTextMessage("WM_JFC_COUNT", new Object[] {}, state.getLocale()));
			
			if (chartTypeDropdown.getValue().toString().equalsIgnoreCase("")) {
				if (pieChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {				
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
			if (chartTypeDropdown.getValue().toString().equalsIgnoreCase("0")) {
				if (pieChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {				
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());	
				}
			}
			if (chartTypeDropdown.getValue().toString().equalsIgnoreCase("1")) {
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
   

}
