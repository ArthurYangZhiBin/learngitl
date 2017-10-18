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
package com.ssaglobal.scm.wms.multifacilityt_balances_charts;

import java.awt.Color;
import java.awt.Paint;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
//import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.jdbc.JDBCPieDataset;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
//import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
//import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
//import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.JFreeChartReport.JFreeChartReportObject;
//import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCBarChart;
//import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCChartInterface;
//import com.ssaglobal.scm.wms.JFreeChartReport.JFreeJDBCPieChart;
import com.ssaglobal.scm.wms.JFreeChartReport.Datasource.WebuiJFreeChartDatasource;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.MFDBIdentifier;
import com.ssaglobal.scm.wms.wm_multi_faclity_balance.MultiFacilityBalancesSearch;
//import org.jfree.chart.renderer.category.CategoryItemRenderer;
//import org.jfree.chart.renderer.category.BarRenderer3D;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class MultiFacilityBalancesChart extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesChart.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {
		try{		   
			StateInterface state = context.getState();
			DefaultPieDataset pieDataset= new DefaultPieDataset();
			DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();
			String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("iframe_slot");
			RuntimeFormWidgetInterface widget1 = currentForm.getFormWidgetByName("barchartiframe");
			RuntimeFormWidgetInterface levelDropdown = currentForm.getFormWidgetByName("level");
//			RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("asn_message_label");
//			JFreeJDBCChartInterface pieChart = new JFreeJDBCPieChart();
//			JFreeJDBCChartInterface barChart = new JFreeJDBCBarChart();
			HttpSession session = state.getRequest().getSession();
			WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();
			RuntimeFormExtendedInterface currentform = (RuntimeFormExtendedInterface)form;

			String facility = (String)session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase());
//			_log.debug("LOG_SYSTEM_OUT","facility="+facility+"  conn="+conn,100L);
			
			//get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = new SQLDPConnectionFactory();
			String oahome = appAccess.getValue("webUIConfig","OAHome");
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app";
					
			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("MFBPie");
			input.setChartName("Multi Facility Balances");
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql("");
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(250);
			input.setChartWidth(250);

			JFreeChartReportObject input1 = new JFreeChartReportObject();
			input1.setTypeName("MFBBar");
			input1.setChartName("Multi Facility Balances");
			input1.setUserId(userId);
			input1.setConnection(conn);
			input1.setReportSql("");
			input1.setHtmlImageMapTmpPath(path);
			input1.setSessionId(context.getState().getRequest().getSession().getId());
			input1.setHtmlImageMapTmpFileName();
			input1.setPngImageMapTmpFileName();
			input1.setChartHeight(250);
			input1.setChartWidth(250);

			Object dropdownValue = session.getAttribute("LEVEL");	
			if(dropdownValue != null){
				levelDropdown.setValue(dropdownValue.toString());		    	
			}
			state.getRequest().getSession().removeAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_STORER_KEY);

			if(levelDropdown.getValue().toString().equalsIgnoreCase("")||levelDropdown.getValue().toString().equalsIgnoreCase("0")){
				session.setAttribute("LEVEL", "0");
				HashMap dataset= MultiFacilityBalancesEnterprise(context);
//				barChartDataset = generateBarChartDataset(dataset);
//				createMFCBarChart(input1, barChartDataset);
				pieDataset = generatePieDataset(dataset);
				createMFJFreeChart(input, pieDataset);
				widget1.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,"true");
				currentform.setFocus(context.getState(),"list_slot","",currentform.getFocus(), "wm_muti_facility_balance_ent_toggle_form");
  	   	    }
  	   	    if(levelDropdown.getValue().toString().equalsIgnoreCase("1")){
  	   	    	HashMap dataset= MultiFacilityBalancesDivision(context);
  	   	    	barChartDataset = generateBarChartDataset(dataset);
  	   	    	createMFCBarChart(input1, barChartDataset);
  	   	    	pieDataset = generatePieDataset(dataset);
  	   	    	createMFJFreeChart(input, pieDataset);
 	  	   	   	currentform.setFocus(context.getState(),"list_slot","",currentform.getFocus(), "wm_muti_facility_balance_division_toggle_form");
  	   	    }
  	   	    if(levelDropdown.getValue().toString().equalsIgnoreCase("2")){
  	   	    	HashMap dataset= MultiFacilityBalancesDC(context);
  	   	    	barChartDataset = generateBarChartDataset(dataset);
  	   	    	createMFCBarChart(input1, barChartDataset);
  	   	    	pieDataset = generatePieDataset(dataset);
 	  	   	   	createMFJFreeChart(input, pieDataset);
 	  	   	   	currentform.setFocus(context.getState(),"list_slot","",currentform.getFocus(), "wm_muti_facility_balance_distribution_center_toggle_form");
  	   	    }
  	   	    if(levelDropdown.getValue().toString().equalsIgnoreCase("-1")){
  	   	    	HashMap dataset= MultiFacilityBalancesWarehouse(context);
  	   	    	barChartDataset = generateBarChartDataset(dataset);
  	   	    	createMFCBarChart(input1, barChartDataset);
  	   	    	pieDataset = generatePieDataset(dataset);
 	  	   	   	createMFJFreeChart(input, pieDataset);
 	  	   	   	currentform.setFocus(context.getState(),"list_slot","",currentform.getFocus(), "wm_muti_facility_balance_search_toggle_form_2");
  	   	    }
  	   	    conn.close();
//			_log.debug("LOG_SYSTEM_OUT","HTML NAME = "+ input.getHtmlImageMapTmpFileName() ,100L);
			widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());
			widget1.setProperty("src", "jfreechartHTMLTmp/"+input1.getHtmlImageMapTmpFileName());
		}catch(Exception e){
			e.printStackTrace();
			return RET_CANCEL;          
		}
		return RET_CONTINUE;
	}
	
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException {
		try{		   
			StateInterface state = context.getState();
			DefaultPieDataset pieDataset= new DefaultPieDataset();
			DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();
			String userId = (String)context.getServiceManager().getUserContext().get("logInUserId");
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName("iframe_slot");
			RuntimeFormWidgetInterface widget1 = currentForm.getFormWidgetByName("barchartiframe");

			RuntimeFormWidgetInterface levelDropdown = currentForm.getFormWidgetByName("level");
//			RuntimeFormWidgetInterface LabelWidget = currentForm.getFormWidgetByName("asn_message_label");
			HttpSession session = state.getRequest().getSession();
			WebuiJFreeChartDatasource ds = new WebuiJFreeChartDatasource();

			String facility = (String)session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			Connection conn = ds.getConnection(facility.toUpperCase());
//			_log.debug("LOG_SYSTEM_OUT","facility="+facility+"  conn="+conn,100L);
				
			//get chart temp path
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = new SQLDPConnectionFactory();
			String oahome = appAccess.getValue("webUIConfig","OAHome");
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app";
			
			JFreeChartReportObject input = new JFreeChartReportObject();
			input.setTypeName("MFBPie");
			input.setChartName("Multi Facility Balances");
			input.setUserId(userId);
			input.setConnection(conn);
			input.setReportSql("");
			input.setHtmlImageMapTmpPath(path);
			input.setSessionId(context.getState().getRequest().getSession().getId());
			input.setHtmlImageMapTmpFileName();
			input.setPngImageMapTmpFileName();
			input.setChartHeight(250);
			input.setChartWidth(250);
   	    
			JFreeChartReportObject input1 = new JFreeChartReportObject();
			input1.setTypeName("MFBBar");
			input1.setChartName("Multi Facility Balances");
			input1.setUserId(userId);
			input1.setConnection(conn);
			input1.setReportSql("");
			input1.setHtmlImageMapTmpPath(path);
			input1.setSessionId(context.getState().getRequest().getSession().getId());
			input1.setHtmlImageMapTmpFileName();
			input1.setPngImageMapTmpFileName();
			input1.setChartHeight(250);
			input1.setChartWidth(250);
			Object dropdownValue = session.getAttribute("LEVEL");
			if(dropdownValue != null){
				levelDropdown.setValue(dropdownValue.toString());		    	
			}
			state.getRequest().getSession().removeAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_STORER_KEY);
			if(levelDropdown.getValue().toString().equalsIgnoreCase("")||levelDropdown.getValue().toString().equalsIgnoreCase("0")){
				session.setAttribute("LEVEL", "0");
				HashMap dataset= MultiFacilityBalancesEnterprise(context);
//				barChartDataset = generateBarChartDataset(dataset);
//				createMFCBarChart(input1, barChartDataset);
				pieDataset = generatePieDataset(dataset);
				createMFJFreeChart(input, pieDataset);
				widget1.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,"true");
				form.setFocus(context.getState(),"list_slot","",form.getFocus(), "wm_muti_facility_balance_ent_toggle_form");
			}
			if (levelDropdown.getValue().toString().equalsIgnoreCase("1"))  {
				HashMap dataset= MultiFacilityBalancesDivision(context);
				barChartDataset = generateBarChartDataset(dataset);
				createMFCBarChart(input1, barChartDataset);
				pieDataset = generatePieDataset(dataset);
				createMFJFreeChart(input, pieDataset);
				form.setFocus(context.getState(),"list_slot","",form.getFocus(), "wm_muti_facility_balance_division_toggle_form");
			}
			if(levelDropdown.getValue().toString().equalsIgnoreCase("2")){
				HashMap dataset= MultiFacilityBalancesDC(context);
				barChartDataset = generateBarChartDataset(dataset);
				createMFCBarChart(input1, barChartDataset);
				pieDataset = generatePieDataset(dataset);
				createMFJFreeChart(input, pieDataset);
				form.setFocus(context.getState(),"list_slot","",form.getFocus(), "wm_muti_facility_balance_distribution_center_toggle_form");
			}
			if(levelDropdown.getValue().toString().equalsIgnoreCase("-1")){
				HashMap dataset= MultiFacilityBalancesWarehouse(context);
				barChartDataset = generateBarChartDataset(dataset);
				createMFCBarChart(input1, barChartDataset);
				pieDataset = generatePieDataset(dataset);
				createMFJFreeChart(input, pieDataset);
				form.setFocus(context.getState(),"list_slot","",form.getFocus(), "wm_muti_facility_balance_search_toggle_form_2");
			}
			conn.close();
//			_log.debug("LOG_SYSTEM_OUT","HTML NAME = "+ input.getHtmlImageMapTmpFileName() ,100L);
			widget.setProperty("src", "jfreechartHTMLTmp/"+input.getHtmlImageMapTmpFileName());
			widget1.setProperty("src", "jfreechartHTMLTmp/"+input1.getHtmlImageMapTmpFileName());
		}catch(Exception e){
			e.printStackTrace();
			return RET_CANCEL;          
		}	  
		return RET_CONTINUE;
	}
	
	public HashMap MultiFacilityBalancesEnterprise(UIRenderContext context) throws UserException{
		HashMap dataset= new HashMap();
//		String storerFilter = "", skuFilter = "";
		StateInterface state = context.getState();
		HashMap warehouseDBTable = new HashMap();  			//Stores warehouse -> database schema mappings
		HashMap warehouseDescriptions = new HashMap();
		ArrayList warehouseAry = new ArrayList();
//		int counter = 0;
		try{
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '-1'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
			if(warehouseCollection != null && warehouseCollection.size() > 0){
				for(int i = 0; i < warehouseCollection.size(); i++){
					Bio bio = warehouseCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ warehouseCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					warehouseAry.add(bio.get("NESTID").toString());									
				}
			}
			if(warehouseAry != null && warehouseAry.size() > 0){
//				Place one database name per warehouse in hash map
				for(int k = 0; k < warehouseAry.size(); k++){
					String nestId = (String)warehouseAry.get(k);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.NESTID = '"+nestId+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
					BioCollection warehouseCollection1 = uow.getBioCollectionBean(loadBiosQry1);
					if(warehouseCollection1 != null && warehouseCollection1.size() > 0){
						for(int i = 0; i < warehouseCollection1.size(); i++){
							Bio warehouse = warehouseCollection1.elementAt(i);
							if(!warehouseDescriptions.containsKey(nestId))
								warehouseDescriptions.put(nestId,warehouse.get("DESCRIPTION"));
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from warehouse...",100L);							
							warehouseDBTable.put(nestId,warehouse.get("NAME"));
						}
					}
				}
			}	
		}catch(EpiDataException e){
			e.printStackTrace();
		}
//		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
//		try {
////			HelperBio helper = state.getDefaultUnitOfWork().getUOW().createHelperBio("wm_multifacbal");
//		} catch (EpiDataException e1) {
//			e1.printStackTrace();
//			String args[] = new String[0]; 
//			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
//			throw new UserException(errorMsg,new Object[0]);
//		}

		//Populate Warehouse Records
//		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,warehouseDescriptions);
		state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_ENT);
		Iterator nestIdItr = warehouseDBTable.keySet().iterator();
		if(warehouseDBTable.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses For Enterprise...",100L);					
			//get warehouse description to populate the warehouse column with...														
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 						
			Array parmsDBList = new Array(MFDBIdentifier.class);	
			if(!WSDefaultsUtil.isOwnerLocked(state))
				parms.add(new TextData(""));
			else
				parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));				
			
			parms.add(new TextData(""));								
			parms.add(new TextData(""));						
			parms.add(new TextData("%"));			
			String warehouseNestId = "";
			ArrayList dbList = new ArrayList();
			while (nestIdItr.hasNext()){
				warehouseNestId = (String)nestIdItr.next();
				MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
				singleDBIdentifier.tag("source");
				singleDBIdentifier.connectionId((String)warehouseDBTable.get(warehouseNestId));								
				dbList.add((String)warehouseDBTable.get(warehouseNestId));
				parmsDBList.add(singleDBIdentifier);
			}		
			HashMap dbTable = new HashMap();
			HashMap DescTable = new HashMap();
			dbTable.put("1",dbList);
			DescTable.put("1","Enterprise");
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,dbTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,DescTable);
			actionProperties.setProcedureParameters(parms);						
			actionProperties.setProcedureName("INVBAL");
			actionProperties.setDbNames(parmsDBList);																					
			try {				
				EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);			
				dataset.put("Enterprise", new Integer(collection.getRowCount()));
			} catch (Exception e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}

		}	
		return dataset;
   }
	   
	public HashMap MultiFacilityBalancesDivision(UIRenderContext context) throws UserException{
		HashMap dataset= new HashMap();
//		String storerFilter = "", skuFilter = "";
		StateInterface state = context.getState();
		HashMap divisionDBTable = new HashMap(); 			//Stores division -> database schema mappings
		HashMap divisionDescriptions = new HashMap();
//		boolean areDivisionWarehousesPresent = false;
		ArrayList divisionAry = new ArrayList();
//		int counter = 0;
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE DEVISION",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '1'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			BioCollection divisionCollection = uow.getBioCollectionBean(loadBiosQry);
			if(divisionCollection != null && divisionCollection.size() > 0){
				for(int i = 0; i < divisionCollection.size(); i++){
					Bio bio = divisionCollection.elementAt(i);
					String nestid = bio.get("NESTID").toString();
					String description = bio.get("DESCRIPTION").toString();
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ divisionCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ nestid,100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					divisionAry.add(nestid);		
					divisionDescriptions.put(nestid,description);
				}
//				_log.debug("LOG_SYSTEM_OUT","DIVISION ARY = "+divisionAry,100L);
//				_log.debug("LOG_SYSTEM_OUT","divisionDescriptions ARY = "+divisionDescriptions,100L);
//				_log.debug("LOG_SYSTEM_OUT","divisionDescriptions = "+divisionDescriptions.get("5"),100L);
			}
			if(divisionAry != null && divisionAry.size() > 0){
				// Place one array list of database names per division in hash map
//				_log.debug("LOG_SYSTEM_OUT","divisionAry.SIZE = "+divisionAry.size(),100L);
				for(int i = 0; i < divisionAry.size(); i++){
					ArrayList databaseList = new ArrayList();
					String nestId = (String)divisionAry.get(i);
//					_log.debug("LOG_SYSTEM_OUT","divisionAry.nestid = "+nestId,100L);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
					BioCollection distributionCollection = uow.getBioCollectionBean(loadBiosQry1);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Query:"+loadBiosQry1.getQueryExpression(),100L);
//					_log.debug("LOG_SYSTEM_OUT","distribution collection size = "+distributionCollection.size(),100L);
					if(distributionCollection != null && distributionCollection.size() > 0){
						for(int j = 0; j < distributionCollection.size(); j++){							
							Bio bio = distributionCollection.elementAt(j);									
							if(bio.get("LEVELNUM").toString().equals("-1")){								
								databaseList.add(bio.get("NAME"));
//								areDivisionWarehousesPresent = true;
								continue;
							}							
							loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+bio.get("NESTID")+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
							BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry);
							if(warehouseCollection != null && warehouseCollection.size() > 0){
								for(int k = 0; k < warehouseCollection.size(); k++){										
									Bio warehouse = warehouseCollection.elementAt(k);		
									_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from dc...",100L);									
									databaseList.add(warehouse.get("NAME"));
//									areDivisionWarehousesPresent = true;
								}
							}
						}
					}
					divisionDBTable.put(nestId,databaseList);
//					_log.debug("LOG_SYSTEM_OUT","divisionDBTable ARY = "+divisionDBTable,100L);
				}
			}
		}catch(EpiDataException e){
			e.printStackTrace();
		}

//		Populate Division Records
		if(divisionDBTable.size() > 0){
			Iterator nestIdItr = divisionDBTable.keySet().iterator();
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,divisionDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,divisionDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION);

			//Iterate Divisions...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Divisions...",100L);					
				//get division description to populate the DIVISION column with...
				String divisionNestId = (String)nestIdItr.next();
//				_log.debug("LOG_SYSTEM_OUT","divisionNestId = "+divisionNestId,100L);					
				String divisionName = (String)divisionDescriptions.get(divisionNestId);
//				_log.debug("LOG_SYSTEM_OUT","divisionName = "+divisionName,100L);
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division Name:"+divisionName+" For Key:"+divisionNestId,100L);					
				ArrayList divisionDatabaseList = (ArrayList)divisionDBTable.get(divisionNestId);
 
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Got Division DB List:"+divisionDatabaseList+" For Key:"+divisionNestId,100L);					
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				Array parmsDBList = new Array(MFDBIdentifier.class);						
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("%"));
				
//				_log.debug("LOG_SYSTEM_OUT","division dB size = "+divisionDatabaseList.size(),100L);
				for(int i = 0; i < divisionDatabaseList.size(); i++){							
					MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
					singleDBIdentifier.tag("source");
					singleDBIdentifier.connectionId((String)divisionDatabaseList.get(i));			               
					parmsDBList.add(singleDBIdentifier);
				}
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("INVBAL");
				actionProperties.setDbNames(parmsDBList);
				try {
					EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);					
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","collection size:"+collection.getRowCount(),100L);						
					dataset.put(divisionName, new Integer(collection.getRowCount()));
				}catch (Exception e) {
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}		
		}
		return dataset;
	}
	
	public HashMap MultiFacilityBalancesDC(UIRenderContext context) throws UserException{
		StateInterface state = context.getState();
//		String storerFilter = "", skuFilter = "";
		HashMap dataset= new HashMap();
		HashMap disributionCenterDBTable = new HashMap(); 	//Stores distribution ceter -> database schema mappings		
		HashMap distributionCenterDescriptions = new HashMap();
//		boolean areDistCenterWarehousesPresent = false;
		ArrayList distributionCenterAry = new ArrayList();
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE DISTRIBUTION CENTER",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '2'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			BioCollection dcCollection = uow.getBioCollectionBean(loadBiosQry);
			if(dcCollection != null && dcCollection.size() > 0){
				for(int i = 0; i < dcCollection.size(); i++){
					Bio bio = dcCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ dcCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					distributionCenterAry.add(bio.get("NESTID").toString());		
					distributionCenterDescriptions.put(bio.get("NESTID").toString(),bio.get("DESCRIPTION").toString());
				}
			}
			if(distributionCenterAry != null && distributionCenterAry.size() > 0){					
//				Place one array list of database names per distribution center in hash map
				for(int j = 0; j < distributionCenterAry.size(); j++){						
					ArrayList databaseList = new ArrayList();
					String nestId = (String)distributionCenterAry.get(j);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.PARENTNESTID = '"+nestId+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Qry:"+loadBiosQry1.getQueryExpression(),100L);					
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry1);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int k = 0; k < warehouseCollection.size(); k++){
							Bio warehouse = warehouseCollection.elementAt(k);																
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from div...",100L);							
							databaseList.add(warehouse.get("NAME"));	
//							areDistCenterWarehousesPresent = true;
						}
					}
					disributionCenterDBTable.put(nestId,databaseList);
				}
			}				
		}catch(EpiDataException e){
			e.printStackTrace();
		}
			
//			Populate Distribution Center Records
		if(disributionCenterDBTable.size() > 0){
			Iterator nestIdItr = disributionCenterDBTable.keySet().iterator();
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,disributionCenterDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,distributionCenterDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER);
//			int counter = 0;
			//Iterate Distribution Centers...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Dist Centers...",100L);					
				//get distribution center description to populate the Distribution Center column with...
				String distributionCenterNestId = (String)nestIdItr.next();
				String distributionCenterName = (String)distributionCenterDescriptions.get(distributionCenterNestId);						
				ArrayList distributionCenterList = (ArrayList)disributionCenterDBTable.get(distributionCenterNestId);						
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				Array parmsDBList = new Array(MFDBIdentifier.class);					
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("%"));
				
				for(int i = 0; i < distributionCenterList.size(); i++){							
					MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
					singleDBIdentifier.tag("source");
					singleDBIdentifier.connectionId((String)distributionCenterList.get(i));			               
					parmsDBList.add(singleDBIdentifier);
				}												
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("INVBAL");
				actionProperties.setDbNames(parmsDBList);
				try {
					EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);					
					dataset.put(distributionCenterName, new Integer(collection.getRowCount()));
				} catch (Exception e) {
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
	   return dataset;
   }
	   
	public HashMap MultiFacilityBalancesWarehouse(UIRenderContext context) throws UserException{
		StateInterface state = context.getState();
//		String storerFilter = "", skuFilter = "";
		HashMap dataset= new HashMap();
		HashMap warehouseDBTable = new HashMap();  			//Stores warehouse -> database schema mappings		
		HashMap warehouseDescriptions = new HashMap();
//		boolean areDistCenterWarehousesPresent = false;
		ArrayList warehouseAry = new ArrayList();
		try{
//			_log.debug("LOG_SYSTEM_OUT","INSIDE WAREHOUSE",100L);
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Enterprise selected...",100L);					
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			Query loadBiosQry = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.LEVELNUM = '-1'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
			BioCollection dcCollection = uow.getBioCollectionBean(loadBiosQry);
			if(dcCollection != null && dcCollection.size() > 0){
				for(int i = 0; i < dcCollection.size(); i++){
					Bio bio = dcCollection.elementAt(i);
//					_log.debug("LOG_SYSTEM_OUT","SIZE = "+ dcCollection.size(),100L);
//					_log.debug("LOG_SYSTEM_OUT","NEST ID = "+ bio.get("NESTID").toString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Adding warehouse from enterprise...",100L);								
					warehouseAry.add(bio.get("NESTID").toString());		
					warehouseDescriptions.put(bio.get("NESTID").toString(),bio.get("DESCRIPTION").toString());
				}
			}
			if(warehouseAry != null && warehouseAry.size() > 0){
//				Place one database name per warehouse in hash map
				for(int k = 0; k < warehouseAry.size(); k++){
					String nestId = (String)warehouseAry.get(k);
					Query loadBiosQry1 = new Query("wm_facilitynest_warehouse_mfb", "wm_facilitynest_warehouse_mfb.NESTID = '"+nestId+"'", "wm_facilitynest_warehouse_mfb.DESCRIPTION ASC");
					BioCollection warehouseCollection = uow.getBioCollectionBean(loadBiosQry1);
					if(warehouseCollection != null && warehouseCollection.size() > 0){
						for(int i = 0; i < warehouseCollection.size(); i++){
							Bio warehouse = warehouseCollection.elementAt(i);
							if(!warehouseDescriptions.containsKey(nestId))
								warehouseDescriptions.put(nestId,warehouse.get("DESCRIPTION"));
							_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","got warehouse:"+warehouse.get("NAME")+" from warehouse...",100L);							
							warehouseDBTable.put(nestId,warehouse.get("NAME"));
						}
					}
				}
			}				
		} catch (EpiDataException e) {
			e.printStackTrace();
		}
			
//		Populate Warehouse Records
		if(warehouseDBTable.size() > 0){
			Iterator nestIdItr = warehouseDBTable.keySet().iterator();
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_DB_TABLE_KEY,warehouseDBTable);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_NAME_KEY,warehouseDescriptions);
			state.getRequest().getSession().setAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY,MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_WHSE);	

//			int counter = 0;
			//Iterate Warehouses...
			while (nestIdItr.hasNext()){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALSEARCH","Iterating Warehouses...",100L);					
				//get warehouse description to populate the warehouse column with...
				String warehouseNestId = (String)nestIdItr.next();
				String warehouseName = (String)warehouseDescriptions.get(warehouseNestId);									
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 						
				Array parmsDBList = new Array(MFDBIdentifier.class);	
				if(!WSDefaultsUtil.isOwnerLocked(state))
					parms.add(new TextData(""));
				else
					parms.add(new TextData(getLockedOwnersAsDelimitedList(state)));
//				_log.debug("LOG_SYSTEM_OUT","Adding Blank Param",100L);
				parms.add(new TextData(""));			
//				_log.debug("LOG_SYSTEM_OUT","Adding Blank Param",100L);
				parms.add(new TextData(""));						
				parms.add(new TextData("%"));
				
				MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();
//				_log.debug("LOG_SYSTEM_OUT","Setting singleDBIdentifier tag:source",100L);					
				singleDBIdentifier.tag("source");
//				_log.debug("LOG_SYSTEM_OUT","Setting singleDBIdentifier connectionId:"+(String)warehouseDBTable.get(warehouseNestId),100L);
				singleDBIdentifier.connectionId((String)warehouseDBTable.get(warehouseNestId));			                
				parmsDBList.add(singleDBIdentifier);					
				actionProperties.setProcedureParameters(parms);						
				actionProperties.setProcedureName("INVBAL");
				actionProperties.setDbNames(parmsDBList);																					
				try {
					EXEDataObject collection = WmsWebuiActionsImpl.doMFAction(actionProperties);	
					_log.debug("LOG_SYSTEM_OUT","\n\nCalling 4\n\n",100L);
//					Array arrtNames = collection.getAttribNames();
					dataset.put(warehouseName, new Integer(collection.getRowCount()));			
				} catch (Exception e) {
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}	
		}
	   return dataset;
   }
	   
	public JFreeChart createMFJFreeChart(JFreeChartReportObject input, DefaultPieDataset dataset){
		JFreeChart chart = null;		
		boolean dataFound = false;			
		try{
//			_log.debug("LOG_SYSTEM_OUT","@---$it is  in JDBC Pie Chart******",100L);
			
//			create a dataset...
//			DefaultPieDataset dataset = new DefaultPieDataset();
//			dataset.setValue("Category 1", 43.2);
//			dataset.setValue("Category 2", 27.9);
//			dataset.setValue("Category 3", 79.5);
			
			for(int i = 0; i < dataset.getItemCount(); i++) {				
				if (dataset.getValue(i).intValue() > 0) {					
					dataFound = true;
				}
			}			
			if (dataFound) {
				chart = ChartFactory.createPieChart(
						input.getChartName(),	//titile
						dataset,                  //data
						false,                 //legend
						true,                  //tooltips
						false);                //URLS
				
				chart.setBorderVisible(true);
				PiePlot plot = (PiePlot)chart.getPlot();
				chart.setBackgroundPaint(plot.getBackgroundPaint());				
				NumberFormat np = NumberFormat.getPercentInstance(); 
				np.setMinimumFractionDigits(2);					
				plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
						"{0} = {2}", NumberFormat.getInstance(), np));				
				plot.setOutlinePaint(null);
				plot.setOutlineStroke(null);
				input.setChart(chart);
				input.createChart();				
			}			
	
		}catch(Exception e){
			e.printStackTrace();			
			System.err.println(e.getMessage());
		}
		if (dataFound == true) {			
			return chart;
		}
		else {			
			return null;
		}
	}
		
	public JFreeChart createMFCBarChart(JFreeChartReportObject input1,DefaultCategoryDataset dataset){			 
		boolean dataFound = false;
		JFreeChart chart = null;
		try{
//			_log.debug("LOG_SYSTEM_OUT","@---$it is  in JDBC Bar Chart******",100L);
			if(dataset.getColumnCount() > 0) {				
				dataFound = true;
			}
//			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			if (dataFound) {
//				 create the chart...
				chart = ChartFactory.createBarChart(
				input1.getChartName(), // chart title
				"Level", // domain axis label
				"Owner", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL,
				false, // include legend
				true, // tooltips?
				false);
//				 create the chart...
				chart.setBorderVisible(true);			
				CategoryPlot plot = chart.getCategoryPlot();	
				chart.setBackgroundPaint(plot.getBackgroundPaint());
				plot.setOutlinePaint(null);				
				plot.setOutlineStroke(null);
				
				CategoryItemRenderer r = new CustomRenderer(new Paint[] {Color.red, Color.blue, Color.green, Color.yellow, Color.magenta, Color.cyan, Color.orange,Color.GRAY});	
				r.setToolTipGenerator(new StandardCategoryToolTipGenerator());				
				plot.setRenderer(r);
				input1.setChart(chart);				
				input1.createChart();
			}
		}catch(Exception e){
			e.printStackTrace();			
			System.err.println(e.getMessage());
		}
		if (dataFound == true) {			
			return chart;
		}
		else {			
			return null;
		}
	}
		
	public DefaultPieDataset generatePieDataset(HashMap dataset){
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		String key = "";
		int value = 0;
		if(dataset.size() > 0){
			Iterator nestIdItr = dataset.keySet().iterator();
			while (nestIdItr.hasNext()){
				key = nestIdItr.next().toString();
				value = ((Integer)dataset.get(key)).intValue();
				pieDataset.setValue(key,value);
			}
			return pieDataset;
		}else{
			return null;
		}
	}
		
	public DefaultCategoryDataset generateBarChartDataset(HashMap dataset){
		DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();
		String key = "";
		int value = 0;
		if(dataset.size() > 0){
			Iterator nestIdItr = dataset.keySet().iterator();
			while (nestIdItr.hasNext()){
				key = nestIdItr.next().toString();
				value = ((Integer)dataset.get(key)).intValue();
				barChartDataset.addValue(value,key,key);
			}
			return barChartDataset;
		}else{
			return null;
		}
	}
		
	class CustomRenderer extends BarRenderer3D {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** The colors. */
	    private Paint[] colors;
	    public CustomRenderer(final Paint[] colors) {
	        this.colors = colors;
	    }

	    /**
	     * Returns the paint for an item.  Overrides the default behaviour inherited from
	     * AbstractSeriesRenderer.
	     *
	     * @param row  the series.
	     * @param column  the category.
	     *
	     * @return The item color.
	     */
	    public Paint getItemPaint(final int row, final int column) {
	        return this.colors[column % this.colors.length];
	    }
	}
	
	private String getLockedOwnersAsDelimitedList(StateInterface state){
//		if(true)
//			return"HARISH,HARISH";
		String list = WSDefaultsUtil.getLockedOwnersAsDelimetedList(state, ",");
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);	
		if(lockedOwners != null && lockedOwners.size() > 0){
			if(lockedOwners.size() > 1)
				return list;
			else
				return list + "," + list;
		}
		return list;
	}
}
