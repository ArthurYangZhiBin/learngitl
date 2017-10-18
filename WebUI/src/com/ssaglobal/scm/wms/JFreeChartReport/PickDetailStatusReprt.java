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



public class PickDetailStatusReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickDetailStatusReprt.class);
	   @Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
	 try {		   
			SimpleDateFormat mssqlFormat = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat oraFormat = new SimpleDateFormat("yyyy-MM-dd");
  	   		StateInterface state = context.getState();
  	   		HttpSession session = state.getRequest().getSession();
  	   		String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
  	   		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
  	   	    RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("pickdetail_iframe");
  	   	    RuntimeFormWidgetInterface chartTypeDropdown = currentForm.getFormWidgetByName("pickdetcharttype");
  	   	    RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("pickdetail_message_label");
   	   	    
  	   	    
 	   	       		   	    
  	   	    JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
  	   	    JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
  	   	    String reportSelectClause  = "SELECT DISTINCT lkp.DESCRIPTION Pick_Status, COUNT(pk.PICKDETAILKEY) PickDetail_Count ";
	   	    String reportFromClause  = "FROM PICKDETAIL pk,  CODELKUP lkp ";
	   	    String reportWhereClause  = "WHERE pk.STATUS = lkp.CODE AND lkp.LISTNAME = 'ORDRSTATUS' ";
	   	    String reportGroupByClause  = "GROUP BY pk.STATUS, lkp.DESCRIPTION";
	   	    
  	   	    if(WSDefaultsUtil.isOwnerLocked(state)){
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		reportWhereClause += " AND pk.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			reportWhereClause += " , '"+lockedOwners.get(i)+"'";
  	   	    		}
  	   	    		reportWhereClause += " ) ";
  	   	    	}  	   	  
  	   	    	ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
  	   	    	ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
  	   	    	ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
  	   	    	if( (lockedCustomers != null && lockedCustomers.size() > 0) ||
  	   	    		(lockedCarriers != null && lockedCarriers.size() > 0) ||
  	   	    		(lockedBillTo != null && lockedBillTo.size() > 0)){
  	   	    		
  	   	    		reportWhereClause += " AND pk.ORDERKEY IN ( SELECT ORDERKEY FROM ORDERS WHERE ";

  	   	    		if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    			reportWhereClause += " ORDERS.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    			for(int i = 1; i < lockedOwners.size(); i++){
  	   	    				reportWhereClause += " , '"+lockedOwners.get(i)+"'";
  	   	    			}
  	   	    			reportWhereClause += " ) ";
  	   	    		}
  	   	    		if(lockedCustomers != null && lockedCustomers.size() > 0){
  	   	    			if(lockedOwners != null && lockedOwners.size() > 0)
	   	    				reportWhereClause += " AND (ORDERS.CONSIGNEEKEY IN ( '"+lockedCarriers.get(0)+"' ";
	   	    			else
	   	    				reportWhereClause += "  (ORDERS.CONSIGNEEKEY IN ( '"+lockedCarriers.get(0)+"' ";
  	   	    			for(int i = 1; i < lockedCustomers.size(); i++){
  	   	    				reportWhereClause += " , '"+lockedCustomers.get(i)+"'";
  	   	    			}
  	   	    			reportWhereClause += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = '' OR ORDERS.CONSIGNEEKEY = ' ') ";
  	   	    		}
  	   	    		if(lockedCarriers != null && lockedCarriers.size() > 0){
  	   	    			if((lockedCustomers != null && lockedCustomers.size() > 0) ||
  	   	    				(lockedOwners != null && lockedOwners.size() > 0))
  	   	    				reportWhereClause += " AND (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
  	   	    			else
  	   	    				reportWhereClause += "  (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
  	   	    			
  	   	    			for(int i = 1; i < lockedCarriers.size(); i++){
  	   	    				reportWhereClause += " , '"+lockedCarriers.get(i)+"'";
  	   	    			}
  	   	    			reportWhereClause += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = '' OR ORDERS.CarrierCode = ' ') ";
  	   	    		}  	   	    		
  	   	    		if(lockedBillTo != null && lockedBillTo.size() > 0){
  	   	    			
  	   	    			if((lockedCustomers != null && lockedCustomers.size() > 0) ||
     	    				(lockedCarriers != null && lockedCarriers.size() > 0) ||
     	    				(lockedOwners != null && lockedOwners.size() > 0))
  	   	    				
  	   	    				reportWhereClause += " AND (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
  	   	    			else
  	   	    				reportWhereClause += " (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";

  	   	    			for(int i = 1; i < lockedBillTo.size(); i++){
  	   	    				reportWhereClause += " , '"+lockedBillTo.get(i)+"'";
  	   	    			}
  	   	    			reportWhereClause += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = '' OR ORDERS.BILLTOKEY = ' ') ";
  	   	    		}
  	   	    		reportWhereClause += ")";
  	   	    		
  	   	    	}
  	   	    }
  	   	    
  	   	    // Bugaware 8581. Chart graphs - logic and ascetic changes
 			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
  			HttpSession sessionUser = state.getRequest().getSession();
  			String serverType = (String) sessionUser.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
  			if (serverType == null)
  			{
  				serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
  			}
  			String eDate ="", sDate="";
  			
  			
  			
			Calendar endDate = GregorianCalendar.getInstance();
			if (session.getAttribute("PDwm_sdate_param") != null) {
				endDate = (Calendar) ((Calendar) session.getAttribute("PDwm_sdate_param")).clone();
			}

			String numDays = "1";
			if (session.getAttribute("PDwm_days_param") != null) {
				numDays = session.getAttribute("PDwm_days_param").toString();
			}
  	   	    
  	   	    
  	   	    Calendar startDate = (Calendar) endDate.clone();
			// hack to get the current day inclusive
			// if you don't specify the time, the range is from start date 00:00:00 - end date 00:00:00.
			// since in this case the end date is specified, it'll look like start date 00:00:00 - end date + 1 00:00:00
			endDate.add(Calendar.DATE, 1);
			startDate.add(Calendar.DATE, Integer.parseInt(numDays) * -1+1);
			
/*			eDate = Integer.toString(endDate.get(Calendar.MONTH) + 1) + "/" + Integer.toString(endDate.get(Calendar.DATE)) + "/" + Integer.toString(endDate.get(Calendar.YEAR));			
			sDate = Integer.toString(startDate.get(Calendar.MONTH) + 1) + "/" + Integer.toString(startDate.get(Calendar.DATE)) + "/" + Integer.toString(startDate.get(Calendar.YEAR));
*/ 
			if("MSS".equalsIgnoreCase(serverType)){
				sDate = mssqlFormat.format(startDate.getTime());
				eDate = mssqlFormat.format(endDate.getTime());
			}else if("O90".equalsIgnoreCase(serverType)){
				sDate = "to_date('"+oraFormat.format(startDate.getTime())+"', 'YYYY-MM-DD')";
				eDate = "to_date('"+oraFormat.format(endDate.getTime())+"', 'YYYY-MM-DD')";
			}
			
			if("MSS".equalsIgnoreCase(serverType)){
					reportWhereClause = reportWhereClause+" AND pk.ADDDATE BETWEEN '"+sDate+"' AND '"+eDate+"' ";
			}else if("O90".equalsIgnoreCase(serverType)){
				reportWhereClause = reportWhereClause+" AND pk.ADDDATE BETWEEN "+sDate+" AND "+eDate+" ";				
			}
   	   	    
  	   	    String reportSql =  reportSelectClause+reportFromClause+reportWhereClause+reportGroupByClause;
  	   	    _log.debug("LOG_SYSTEM_OUT","REPORTSQL : "+reportSql,100L);
  	   	    
  	   	    WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
		    
		    Object dropdownValue = session.getAttribute("pickdetcharttype");		    
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
			input.setTypeName("pkdet");
			input.setChartName(getTextMessage("WM_JFC_PICK_DETAIL_STATUS", new Object[] {}, state.getLocale()));
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
				if (barChart.createJFreeChart(input) == null ) {				
					LabelWidget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				}
				else {				
					widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());
				}
			}
			else if (chartTypeDropdown.getValue().toString().equalsIgnoreCase("0")) {
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
