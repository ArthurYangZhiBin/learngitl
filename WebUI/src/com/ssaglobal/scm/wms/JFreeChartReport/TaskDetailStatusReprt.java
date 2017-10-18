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



public class TaskDetailStatusReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDetailStatusReprt.class);
	   @Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
	 try {		   
			SimpleDateFormat mssqlFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat oraFormat = new SimpleDateFormat("yyyy-MM-dd");
  	   		StateInterface state = context.getState();  
  	   		String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
  	   		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
  	   	    RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("taskdetail_iframe");
  	   	    RuntimeFormWidgetInterface chartTypeDropdown = currentForm.getFormWidgetByName("taskdetcharttype");
  	   	    RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("taskdetail_message_label");
  	   	    
  	   	    JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
  	   	    JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
  	   	    String reportSelectClause  = "SELECT DISTINCT lkp.DESCRIPTION Task_Status, COUNT(tk.TASKDETAILKEY) TaskDetail_Count ";
	   	    String reportFromClause  = "FROM TASKDETAIL tk,  CODELKUP lkp ";
	   	    String reportWhereClause  = "WHERE tk.STATUS = lkp.CODE AND lkp.LISTNAME = 'TMSTATUS' ";
	   	    String reportGroupByClause  = "GROUP BY tk.STATUS, lkp.DESCRIPTION";
	   	    
	   	    if(WSDefaultsUtil.isOwnerLocked(state)){
	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
	   	    		reportWhereClause += " AND tk.STORERKEY IN( '"+lockedOwners.get(0)+"' ";
	   	    		for(int i = 1; i < lockedOwners.size(); i++){
	   	    			reportWhereClause += " , '"+lockedOwners.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += " ) ";
	   	    	}  	   	    	
	   	    	
	   	    	
	   	    	String LDOrPKTaskFilterClause = getLDPKTaskTypeFilterClause(state, false);
	   	    	String PATaskFilterClause = getPATaskTypeFilterClause(state, false);
	   	    	//Clause to filter out tasks based on Order if task is of type LD or PK (requested by Julie) 
	   	    	if(LDOrPKTaskFilterClause.length() > 0)
	   	    		reportWhereClause += " AND (tk.TASKDETAILKEY NOT IN ("+LDOrPKTaskFilterClause+")) ";
	   	    	//Clause to filter out tasks based on ASN if task is of type PA (requested by Julie)
	   	    	if(PATaskFilterClause.length() > 0)
	   	    		reportWhereClause += " AND (tk.TASKDETAILKEY NOT IN ("+PATaskFilterClause+")) ";	   	    	
	   	    	
	   	    }
	   	    
	   	    HttpSession session = state.getRequest().getSession();
  	   	    Calendar endDate = GregorianCalendar.getInstance();  	   	    
  	   	    if ( session.getAttribute("TDwm_sdate_param") != null )
  	   	    {
  	   	    	endDate = (Calendar) ((Calendar) session.getAttribute("TDwm_sdate_param")).clone();
  	   	    }
 	   	    String numDays = "1";  	   	    	
 	   	    if ( session.getAttribute("TDwm_days_param") != null )
   	    	{   	    		
   	    		numDays = session.getAttribute("TDwm_days_param").toString();   	    		
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

 	   	    
 	   	    Calendar startDate = (Calendar) endDate.clone();
			// hack to get the current day inclusive
			// if you don't specify the time, the range is from start date 00:00:00 - end date 00:00:00.
			// since in this case the end date is specified, it'll look like start date 00:00:00 - end date + 1 00:00:00
			endDate.add(Calendar.DATE, 1);
  	   	    startDate.add(Calendar.DATE, Integer.parseInt(numDays) * -1+1);

			if("MSS".equalsIgnoreCase(serverType)){
				sDate = mssqlFormat.format(startDate.getTime());
				eDate = mssqlFormat.format(endDate.getTime());
			}else if("O90".equalsIgnoreCase(serverType)){
				sDate = "to_date('"+oraFormat.format(startDate.getTime())+"', 'YYYY-MM-DD')";
				eDate = "to_date('"+oraFormat.format(endDate.getTime())+"', 'YYYY-MM-DD')";
			}
  	   	    
//  	   	    String eDate = Integer.toString(endDate.get(Calendar.MONTH)+1)+"/"+Integer.toString(endDate.get(Calendar.DATE))+"/"+Integer.toString(endDate.get(Calendar.YEAR));
//  	   	    String sDate = Integer.toString(startDate.get(Calendar.MONTH)+1)+"/"+Integer.toString(startDate.get(Calendar.DATE))+"/"+Integer.toString(startDate.get(Calendar.YEAR));

			if("MSS".equalsIgnoreCase(serverType)){
  	   	    	reportWhereClause = reportWhereClause + " AND tk.ADDDATE BETWEEN '" + sDate + "' AND '" + eDate + "' ";
			}else if("O90".equalsIgnoreCase(serverType)){
  	   	    	reportWhereClause = reportWhereClause + " AND tk.ADDDATE BETWEEN " + sDate + " AND " + eDate + " ";				
			}
   	   	    
  	   	    String reportSql =  reportSelectClause+reportFromClause+reportWhereClause+reportGroupByClause;
	   	    _log.debug("LOG_SYSTEM_OUT","\n\nsql:"+reportSql+"\n\n",100L);

	   	    WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
		    Object dropdownValue = session.getAttribute("taskdetcharttype");		    
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
			input.setTypeName("tkdet");
			input.setChartName(getTextMessage("WM_JFC_TASK_DETAIL_STATUS", new Object[] {}, state.getLocale()));
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql(reportSql);
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(450);
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
   
	   private String getLDPKTaskTypeFilterClause(StateInterface state, boolean isDPE){
		   String sql = "";
		   String orderFilterSql = getOrderSql(state, isDPE);
		   if(orderFilterSql.length() == 0)
			   return "";
		   if(isDPE){
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE (TASKDETAIL.TASKTYPE = \\'LD\\' OR TASKDETAIL.TASKTYPE = \\'PK\\') AND (TASKDETAIL.ORDERKEY NOT IN ("+orderFilterSql+") OR TASKDETAIL.ORDERKEY IS NULL OR TASKDETAIL.ORDERKEY = \\'\\' OR TASKDETAIL.ORDERKEY = \\' \\')";
		   }
		   else{
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE (TASKDETAIL.TASKTYPE = 'LD' OR TASKDETAIL.TASKTYPE = 'PK') AND (TASKDETAIL.ORDERKEY NOT IN ("+orderFilterSql+") OR TASKDETAIL.ORDERKEY IS NULL OR TASKDETAIL.ORDERKEY = '' OR TASKDETAIL.ORDERKEY = ' ' )";
		   }
		   return sql;
	   }
	   private String getPATaskTypeFilterClause(StateInterface state, boolean isDPE){
		   String sql = "";
		   String receiptFilterSql = getReceiptSql(state, isDPE);
		   if(receiptFilterSql.length() == 0)
			   return "";
		   if(isDPE){
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE TASKDETAIL.TASKTYPE = \\'PA\\' AND (TASKDETAIL.SOURCEKEY NOT IN ("+receiptFilterSql+") OR TASKDETAIL.SOURCEKEY IS NULL OR TASKDETAIL.SOURCEKEY = \\'\\' OR TASKDETAIL.SOURCEKEY = \\' \\')";
		   }
		   else{
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE TASKDETAIL.TASKTYPE = 'PA' AND (TASKDETAIL.SOURCEKEY NOT IN ("+receiptFilterSql+") OR TASKDETAIL.SOURCEKEY IS NULL OR TASKDETAIL.SOURCEKEY = '' OR TASKDETAIL.SOURCEKEY = ' ')";
		   }
		   return sql;
	   }
	   
	   private String getReceiptSql(StateInterface state, boolean isDPE){
			
		   	String serverType = (String) state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_TYPE);
			String sql = "";
			ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);			
			ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
			ArrayList lockedVendors = WSDefaultsUtil.getLockedVendors(state);
			if( (lockedOwners != null && lockedOwners.size() > 0) ||
					(lockedCarriers != null && lockedCarriers.size() > 0) ||
					(lockedVendors != null && lockedVendors.size() > 0)){

				if(!isDPE){
					if (serverType.equalsIgnoreCase("MSS"))
					{
						sql = "SELECT RECEIPT.RECEIPTKEY + RECEIPTDETAIL.RECEIPTLINENUMBER FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					else{
						sql = "SELECT CONCAT(RECEIPT.RECEIPTKEY,RECEIPTDETAIL.RECEIPTLINENUMBER) FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (RECEIPT.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , '"+lockedOwners.get(i)+"'";
						}
						sql += ") ) ";
					}					
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0))
							sql += " AND (RECEIPT.CARRIERKEY IN ( '"+lockedCarriers.get(0)+"' ";
						else
							sql += "  (RECEIPT.CARRIERKEY IN ( '"+lockedCarriers.get(0)+"' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , '"+lockedCarriers.get(i)+"'";
						}
						sql += ") OR RECEIPT.CARRIERKEY IS NULL OR RECEIPT.CARRIERKEY = '' OR RECEIPT.CARRIERKEY = ' ') ";
					}  	   	    		
					if(lockedVendors != null && lockedVendors.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCarriers != null && lockedCarriers.size() > 0))
							sql += " AND (RECEIPT.EXTERNALRECEIPTKEY2 IN ( '"+lockedVendors.get(0)+"' ";
						else
							sql += " (RECEIPT.EXTERNALRECEIPTKEY2 IN ( '"+lockedVendors.get(0)+"' ";

						for(int i = 1; i < lockedVendors.size(); i++){
							sql += " , '"+lockedVendors.get(i)+"'";
						}
						sql += ") OR RECEIPT.EXTERNALRECEIPTKEY2 IS NULL OR RECEIPT.EXTERNALRECEIPTKEY2 = '' OR RECEIPT.EXTERNALRECEIPTKEY2 = ' ') ";
					}
					
				}
				else{
					if (serverType.equalsIgnoreCase("MSS"))
					{
						sql = "SELECT RECEIPT.RECEIPTKEY + RECEIPTDETAIL.RECEIPTLINENUMBER FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					else{
						sql = "SELECT CONCAT(RECEIPT.RECEIPTKEY,RECEIPTDETAIL.RECEIPTLINENUMBER) FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (RECEIPT.STORERKEY IN ( \\'"+lockedOwners.get(0)+"\\' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , \\'"+lockedOwners.get(i)+"\\'";
						}
						sql += ") ) ";
					}					
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0))
							sql += " AND (RECEIPT.CARRIERKEY IN ( \\'"+lockedCarriers.get(0)+"\\' ";
						else
							sql += "  (RECEIPT.CARRIERKEY IN ( \\'"+lockedCarriers.get(0)+"\\' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , \\'"+lockedCarriers.get(i)+"\\'";
						}
						sql += ") OR RECEIPT.CARRIERKEY IS NULL OR RECEIPT.CARRIERKEY = \\'\\' OR RECEIPT.CARRIERKEY = \\' \\') ";
					}  	   	    		
					if(lockedVendors != null && lockedVendors.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCarriers != null && lockedCarriers.size() > 0))
							sql += " AND (RECEIPT.EXTERNALRECEIPTKEY2 IN ( \\'"+lockedVendors.get(0)+"\\' ";
						else
							sql += " (RECEIPT.EXTERNALRECEIPTKEY2 IN ( \\'"+lockedVendors.get(0)+"\\' ";

						for(int i = 1; i < lockedVendors.size(); i++){
							sql += " , \\'"+lockedVendors.get(i)+"\\'";
						}
						sql += ") OR RECEIPT.EXTERNALRECEIPTKEY2 IS NULL OR RECEIPT.EXTERNALRECEIPTKEY2 = \\'\\' OR RECEIPT.EXTERNALRECEIPTKEY2 = \\' \\') ";
					}
				}
			}
			
			return sql;
	   }
	   
	   private String getOrderSql(StateInterface state, boolean isDPE){
			
			String sql = "";
			ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
			ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
			ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
			ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
			if( (lockedCustomers != null && lockedCustomers.size() > 0) ||
					(lockedCarriers != null && lockedCarriers.size() > 0) ||
					(lockedBillTo != null && lockedBillTo.size() > 0) ||
					(lockedOwners != null && lockedOwners.size() > 0)){

				if(!isDPE){
					sql = "SELECT ORDERS.ORDERKEY FROM ORDERS WHERE ";
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (ORDERS.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , '"+lockedOwners.get(i)+"'";
						}
						sql += ") ) ";
					}
					if(lockedCustomers != null && lockedCustomers.size() > 0){
						if(lockedOwners != null && lockedOwners.size() > 0)
							sql += " AND (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";
						else
							sql += "  (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";						
						for(int i = 1; i < lockedCustomers.size(); i++){
							sql += " , '"+lockedCustomers.get(i)+"'";
						}
						sql += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = '' OR ORDERS.CONSIGNEEKEY = ' ') ";
					}
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCustomers != null && lockedCustomers.size() > 0)	)
							sql += " AND (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
						else
							sql += "  (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , '"+lockedCarriers.get(i)+"'";
						}
						sql += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = '' OR ORDERS.CarrierCode = ' ') ";
					}  	   	    		
					if(lockedBillTo != null && lockedBillTo.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
								(lockedCustomers != null && lockedCustomers.size() > 0) ||
								(lockedCarriers != null && lockedCarriers.size() > 0))

							sql += " AND (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
						else
							sql += " (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";

						for(int i = 1; i < lockedBillTo.size(); i++){
							sql += " , '"+lockedBillTo.get(i)+"'";
						}
						sql += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = '' OR ORDERS.BILLTOKEY = ' ') ";
					}
					
				}
				else{
					sql = "SELECT ORDERS.ORDERKEY FROM ORDERS WHERE ";
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (ORDERS.STORERKEY IN ( \\'"+lockedOwners.get(0)+"\\' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , \\'"+lockedOwners.get(i)+"\\'";
						}
						sql += ") ) ";
					}
					if(lockedCustomers != null && lockedCustomers.size() > 0){
						if(lockedOwners != null && lockedOwners.size() > 0)
							sql += " AND (ORDERS.CONSIGNEEKEY IN ( \\'"+lockedCustomers.get(0)+"\\' ";
						else
							sql += "  (ORDERS.CONSIGNEEKEY IN ( \\'"+lockedCustomers.get(0)+"\\' ";	
						for(int i = 1; i < lockedCustomers.size(); i++){
							sql += " , \\'"+lockedCustomers.get(i)+"\\'";
						}
						sql += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = \\'\\' OR ORDERS.CONSIGNEEKEY = \\' \\') ";
					}
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCustomers != null && lockedCustomers.size() > 0)	)
								sql += " AND (ORDERS.CarrierCode IN ( \\'"+lockedCarriers.get(0)+"\\' ";
							else
								sql += "  (ORDERS.CarrierCode IN ( \\'"+lockedCarriers.get(0)+"\\' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , \\'"+lockedCarriers.get(i)+"\\'";
						}
						sql += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = \\'\\' OR ORDERS.CarrierCode = \\' \\') ";
					}  	   	    		
					if(lockedBillTo != null && lockedBillTo.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
								(lockedCustomers != null && lockedCustomers.size() > 0) ||
								(lockedCarriers != null && lockedCarriers.size() > 0))

							sql += " AND (ORDERS.BILLTOKEY IN ( \\'"+lockedBillTo.get(0)+"\\' ";
						else
							sql += " (ORDERS.BILLTOKEY IN ( \\'"+lockedBillTo.get(0)+"\\' ";

						for(int i = 1; i < lockedBillTo.size(); i++){
							sql += " , '"+lockedBillTo.get(i)+"'";
						}
						sql += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = \\'\\' OR ORDERS.BILLTOKEY = \\' \\') ";
					}
					
				}
			}
			
			
			return sql;
		}
	   
}
