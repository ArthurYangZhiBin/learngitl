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
package com.infor.scm.waveplanning.wp_graphical_filters.action;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.SetUtils;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.infor.scm.waveplanning.wp_graphical_filters.util.WPGraphFilterUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;


public class WPGraphicalFilterProceed extends SaveAction{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterProceed.class);
	public static String SESSION_KEY_GRAPH_QRY_BASE_CRITERION = "session.key.grph.fltr.base";
	public static String SESSION_KEY_GRAPH_QRY_GRAPH_TYPE = "session.key.grph.fltr.type";
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);	
		StateInterface state = context.getState();
		HashMap shipmentOrderTypeMap = null;
		HashMap ohTypeMap = null;
		RuntimeFormInterface graphicalFilterInitialForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_graphical_filters_initial_form",state);						
		if(graphicalFilterInitialForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Initial Form:"+graphicalFilterInitialForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Initial Form:Null",100L);			


		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		//get widgets				
		RuntimeFormWidgetInterface baseCriterionWidget = graphicalFilterInitialForm.getFormWidgetByName("BASECRITERION");
		RuntimeFormWidgetInterface graphTypeWidget = graphicalFilterInitialForm.getFormWidgetByName("GRAPHTYPE");

		//get widget values
		String baseCriterion = (String)baseCriterionWidget.getValue();
		String graphType = (String)graphTypeWidget.getValue();
		ArrayList selectedCriterion = new ArrayList();
		selectedCriterion.add(baseCriterion);

		//Set values in interaction session
		WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_GRAPH_QRY_BASE_CRITERION, selectedCriterion, state);
		WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_GRAPH_QRY_GRAPH_TYPE, graphType, state);
		//Clean Temp Table
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK,-1);			
		Query qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"' OR wp_graphicalfilter_temp.DATEADDED < @DATE['"+gc.getTimeInMillis()+"']","");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		try {
			if(criteriaCollection != null){		
				for(int i = 0; i < criteriaCollection.size(); i++){
					criteriaCollection.elementAt(i).delete();
				}
			}
			uow.saveUOW();
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//Create record in querybuildertemp table
		RuntimeFormInterface queryBuilderInitialForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_initial_screen",state);
		String uid = WPUserUtil.getUserId(state);
		//get  focus				
		DataBean queryBuilderInitialFocus = queryBuilderInitialForm == null?null:queryBuilderInitialForm.getFocus();	

		//Tag the record with the current interaction ID
		gc = new GregorianCalendar();
		queryBuilderInitialFocus.setValue("INTERACTIONID",state.getInteractionId());	
		queryBuilderInitialFocus.setValue("USERID",uid);						
		queryBuilderInitialFocus.save();		
		if(baseCriterion.equalsIgnoreCase("ITEM")){
//			qry = new Query("wp_orderline", "DPE('SQL','@[wp_orderline.SERIALKEY] IN (" + createQuery(	baseCriterion,
//			                                                                                          	state) + ")')", "");					
			qry = new Query("wm_wp_orderdetail", "DPE('SQL','@[orderdetail.SERIALKEY] IN (" + WPGraphFilterUtil.createQuery(	baseCriterion,
                  	state) + ")')", "");					
		}
		else if(baseCriterion.equalsIgnoreCase("ITEMGRP")){
//			qry = new Query("wp_sku", "DPE('SQL','@[wp_sku.SKU] IN (" + createQuery(baseCriterion, state) + ")')", "");			
			qry = new Query("wm_sku", "DPE('SQL','@[sku.SKU] IN (" + WPGraphFilterUtil.createQuery(baseCriterion, state) + ")')", "");			
		}
		else if(baseCriterion.equalsIgnoreCase("ITEMGRP1")){
//			qry = new Query("wp_sku", "DPE('SQL','@[wp_sku.SKU] IN (" + createQuery(baseCriterion, state) + ")')", "");					
			qry = new Query("wm_sku", "DPE('SQL','@[sku.SKU] IN (" + WPGraphFilterUtil.createQuery(baseCriterion, state) + ")')", "");					
		}
		else{
//			qry = new Query("wp_orderheader", "DPE('SQL','@[wp_orderheader.ORDERKEY] IN (" + createQuery(	baseCriterion,
//			                                                                                             	state) + ")')", "");
			qry = new Query("wm_wp_orders", "DPE('SQL','@[orders.ORDERKEY] IN (" + WPGraphFilterUtil.createQuery(	baseCriterion,
                 	state) + ")')", "");
		}
		WPJFreeChartDatasource ds = new WPJFreeChartDatasource();
		List orders = null;
		try {
			String facility = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			orders = ds.runQuery(facility.toUpperCase(), WPGraphFilterUtil.createQuery(baseCriterion, state), new Object[0]);
//			orders = ds.runQuery("wms4000", createQuery(baseCriterion, state), new Object[0]);
		} catch (SQLException e1) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e1.getMessage(),100L);
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		try {
			  
			WPGraphFilterUtil.updateGraphicalFilterTempTable(orders, baseCriterion, uow, state);
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		qry = new Query("wp_graphicalfilter_temp", "wp_graphicalfilter_temp.INTERACTIONID = '" + state.getInteractionId() + "' AND wp_graphicalfilter_temp.DODISPLAY = '1'", "wp_graphicalfilter_temp.ADDITIONALFILTER ASC");
		result.setFocus(uow.getBioCollectionBean(qry));



		//Clean Out All Records Older Than 1 Day
		gc.add(Calendar.DAY_OF_WEEK,-1);			
		BioCollection bc = null;		
		qry = new Query("querybuildertemp","querybuildertemp.DATEADDED < @DATE['"+gc.getTimeInMillis()+"']","");
		bc = uow.getBioCollectionBean(qry);				
		try {
			if(bc != null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","bc.size():"+bc.size(),100L);
				for(int i = 0; i < bc.size(); i++){					
					bc.elementAt(i).delete();
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		return RET_CONTINUE;	
	}

	public HashMap buildShipmentOrderMap(StateInterface state) throws EpiDataException{
		HashMap shipmentOrderMap = new HashMap();
		BioCollection orderTypes = WPUtil.getCodeLkupCollection("ORDERTYPE", state);
		for(int i = 0; i < orderTypes.size(); i++){
			shipmentOrderMap.put(orderTypes.elementAt(i).get("CODE"), orderTypes.elementAt(i).get("DESCRIPTION"));
		}
		return shipmentOrderMap;
	}

	public HashMap buildOrderHandlingTypeMap(StateInterface state) throws EpiDataException{
		HashMap orderTypeMap = new HashMap();
		BioCollection orderTypes = WPUtil.getCodeLkupCollection("OHT", state);
		for(int i = 0; i < orderTypes.size(); i++){
			orderTypeMap.put(orderTypes.elementAt(i).get("CODE"), orderTypes.elementAt(i).get("DESCRIPTION"));
		}
		return orderTypeMap;
	}
}
