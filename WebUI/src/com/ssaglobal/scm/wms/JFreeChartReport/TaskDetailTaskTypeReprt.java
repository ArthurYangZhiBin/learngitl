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
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
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


    
public class TaskDetailTaskTypeReprt  extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDetailTaskTypeReprt.class);
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException 
	{
		try 
		{		 	
			StateInterface state = context.getState();  	   		 
			String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("cube_iframe");
			RuntimeFormWidgetInterface chartTypeDropdown = currentForm.getFormWidgetByName("cubecharttype");
			RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("cube_message_label");

			JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
			JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
			String reportSelectClause  = "SELECT DISTINCT lkp.DESCRIPTION as task_type, COUNT(tk.TASKDETAILKEY) TaskDetail_Count ";
			String reportFromClause  = "FROM TASKDETAIL tk, CODELKUP lkp ";
			String reportWhereClause  = "WHERE tk.TASKTYPE = lkp.CODE AND lkp.LISTNAME = 'TASKTYPE' ";
			String reportGroupByClause  = "GROUP BY tk.TASKTYPE, lkp.DESCRIPTION ";

	   	    if(WSDefaultsUtil.isOwnerLocked(state))
			{
	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
	   	    		reportWhereClause += "AND tk.STORERKEY IN( '"+lockedOwners.get(0)+"' ";
	   	    		for(int i = 1; i < lockedOwners.size(); i++){
	   	    			reportWhereClause += " , '"+lockedOwners.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += " ) ";
	   	    	}
			}
	   	    
	   	    // Only show TASKDETAIL records that are STATUS==0 ...
	   	    reportWhereClause += "AND tk.STATUS = '0' ";
	   	    
			String reportSql =  reportSelectClause + reportFromClause + reportWhereClause + reportGroupByClause;				
			_log.debug("LOG_SYSTEM_OUT","\nTaskDetailTaskTypeReprt/tktyp SQL:\n"+reportSql+"\n\n",100L);
			
			WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
			HttpSession session = state.getRequest().getSession();
			
			Object dropdownValue = session.getAttribute("cubecharttype");		    
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
			input.setTypeName("tktyp");
			input.setChartName(getTextMessage("WM_JFC_OPEN_TASKS", new Object[] {}, state.getLocale()));
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
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		return RET_CONTINUE;
	}

}

