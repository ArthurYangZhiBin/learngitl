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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.infor.scm.waveplanning.wp_graphical_filters.util.WPGraphFilterUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;


public class WPGraphicalFilterAddCriterionOnChange extends ActionExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterAddCriterionOnChange.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
System.out.println("****** grapsh 2222********");
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);		
		StateInterface state = context.getState();		
		RuntimeFormInterface addCriterionDDForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_graphical_filters_charts_add_filter_normal",state);						
		RuntimeListFormInterface addCriterionListForm = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_graphical_filters_charts_add_filter_list",state);
		if(addCriterionDDForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Criteria Normal Form:"+addCriterionDDForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Criteria Normal Form:Null",100L);			

		if(addCriterionListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Criteria List Form:"+addCriterionListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Criteria List Form:Null",100L);			


		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		ArrayList selectedItems = addCriterionListForm.getAllSelectedItems();
		if(selectedItems == null || selectedItems.size() == 0){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		for(int i = 0; i < selectedItems.size(); i++){
			BioBean selectedItem = (BioBean)selectedItems.get(i);
			selectedItem.set("ISSELECTED", "1");
			selectedItem.save();
		}
		try {
			uow.saveUOW();
		} catch (EpiException e2) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e2.getMessage(),100L);
			e2.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//get widgets				
		RuntimeFormWidgetInterface addCriterionWidget = addCriterionDDForm.getFormWidgetByName("NEWFILTERTYPE");		

		//get widget values
		String criterion = (String)addCriterionWidget.getValue();		
		ArrayList selectedCriterion = (ArrayList)WPUserUtil.getInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_BASE_CRITERION, state);
		selectedCriterion.add(criterion);		

		//Set values in interaction session
		WPUserUtil.setInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_BASE_CRITERION, selectedCriterion, state);		

		//Update Temp Table		
		Query qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"'","");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		try {
			if(criteriaCollection != null){		
				for(int i = 0; i < criteriaCollection.size(); i++){
					criteriaCollection.elementAt(i).set("DODISPLAY", "0");					
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

		WPJFreeChartDatasource ds = new WPJFreeChartDatasource();
		List orders = null;


		try {
			String query = WPGraphFilterUtil.createQuery(selectedCriterion, criteriaCollection, state, null);//createQuery(selectedCriterion,criteriaCollection,state);			
			String facility = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			orders = ds.runQuery(facility.toUpperCase(), query, new Object [0]);
//			orders = ds.runQuery("wms4000", query, new Object [0]);
		} catch (SQLException e1) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e1.getMessage(),100L);
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getMessage(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		try {
			WPGraphFilterUtil.updateGraphicalFilterTempTable(orders, criterion, uow, state);
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
		} catch (Exception e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			//_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getMessage(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		qry = new Query("wp_graphicalfilter_temp", "wp_graphicalfilter_temp.INTERACTIONID = '" + state.getInteractionId() + "' AND wp_graphicalfilter_temp.DODISPLAY = '1'", "wp_graphicalfilter_temp.ADDITIONALFILTER ASC");
		result.setFocus(uow.getBioCollectionBean(qry));
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
