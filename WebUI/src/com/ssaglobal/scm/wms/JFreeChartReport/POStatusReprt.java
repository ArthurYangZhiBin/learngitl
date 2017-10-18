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



public class POStatusReprt extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(POStatusReprt.class);
	   @Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
	 try {		   
  	   		StateInterface state = context.getState(); 
  	   		HttpSession session = state.getRequest().getSession();
  	   		String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
  	   		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
  	   	    RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("po_iframe");
  	   	    RuntimeFormWidgetInterface chartTypeDropdown = currentForm.getFormWidgetByName("pocharttype");
  	   	    RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("po_message_label");
  	   	    JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
  	   	    JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
  	   		String reportSelectClause  = "SELECT DISTINCT lkp.DESCRIPTION Po_Status, COUNT(po.POKEY) Po_Count ";
	   	    String reportFromClause  = "FROM PO po,  CODELKUP lkp ";
	   	    String reportWhereClause  = " po.STATUS = lkp.CODE AND lkp.LISTNAME = 'POSTATUS' ";
	   	    String reportGroupByClause  = "GROUP BY po.STATUS, lkp.DESCRIPTION";
	   	    
	   	    if(WSDefaultsUtil.isOwnerLocked(state)){
	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
	   	    		reportWhereClause += " AND po.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
	   	    		for(int i = 1; i < lockedOwners.size(); i++){
	   	    			reportWhereClause += " ,'"+lockedOwners.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += " ) ";
	   	    	}  
	   	    	ArrayList lockedVendors = WSDefaultsUtil.getLockedVendors(state);
	   	    	if(lockedVendors != null && lockedVendors.size() > 0){
	   	    		reportWhereClause += " AND (po.SELLERNAME IN ( '"+lockedVendors.get(0)+"' ";
	   	    		for(int i = 1; i < lockedVendors.size(); i++){
	   	    			reportWhereClause += " , '"+lockedVendors.get(i)+"'";
	   	    		}
	   	    		reportWhereClause += " )OR po.SELLERNAME IS NULL OR po.SELLERNAME = '' OR po.SELLERNAME = ' ') ";
	   	    	} 
	   	    }
	   	    
	   	    String closeStatus = "1";
   	    	if ( session.getAttribute("_wm_po_closestatus_param") != null )
   	    	{
   	    		closeStatus	 = session.getAttribute("_wm_po_closestatus_param").toString();
   	    	}
   	    	
   	   	    if (reportWhereClause.length() > 0 && ( closeStatus == "0" || closeStatus == "N" ))
  	   	    {
  	   	    	reportWhereClause = " WHERE ( ("+reportWhereClause+") AND po.STATUS NOT IN ('11','15','20')) ";
  	   	    }
  	   	    else
  	   	    {  	   	    
  	   	    	reportWhereClause = " WHERE ("+reportWhereClause+") ";
  	   	    }
  	   	    
	   	    String reportSql =  reportSelectClause+reportFromClause+reportWhereClause+reportGroupByClause;	     

  	   	    WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
		    
		    Object dropdownValue = session.getAttribute("pocharttype");		    
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
			input.setTypeName("po");
			input.setChartName(getTextMessage("WM_JFC_PO_STATUS", new Object[] {}, state.getLocale()));
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