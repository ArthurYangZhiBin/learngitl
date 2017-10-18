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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ReassignAction extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReassignAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ReassignAction",100L);		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();		
		ArrayList tabs = new ArrayList();
		tabs.add("tab 0");
		tabs.add("tab 1");
		tabs.add("tab 2");
		//Get Header and Detail Forms		
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_order_detail_list_form",tabs,state);
		RuntimeFormInterface unitsForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_outbound_units_list_form",tabs,state);
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_detail_form",state);
		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Header Form:Null",100L);			
		
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Detail Form:Null",100L);
		
		if(unitsForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Units Form:"+unitsForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Units Form:Null",100L);
		
		if(detailForm == null && unitsForm == null){			
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","List forms are null...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);			
			String args[] = new String[0]; 												
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		ArrayList selectedItems = new ArrayList();
		if(detailForm != null && ((RuntimeListFormInterface)detailForm).getSelectedItems() != null){
			selectedItems = ((RuntimeListFormInterface)detailForm).getSelectedItems();
		}
		if(unitsForm != null && ((RuntimeListFormInterface)unitsForm).getSelectedItems() != null){
			selectedItems.addAll(((RuntimeListFormInterface)unitsForm).getSelectedItems());
		}
		if(selectedItems.size() == 0){			
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Nothing Selected...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
			String args[] = new String[0]; 												
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}										
		
		try {
			if(headerForm != null){				
				Object routeObj = headerForm.getFormWidgetByName("ROUTE");
				Object loadIdObj = headerForm.getFormWidgetByName("LOADID");
							
				String route = routeObj == null || ((RuntimeWidget)routeObj).getValue() == null?"":((RuntimeWidget)routeObj).getValue().toString();
				String loadId = loadIdObj == null || ((RuntimeWidget)loadIdObj).getValue() == null?"":((RuntimeWidget)loadIdObj).getValue().toString();
				
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","routeObj:"+routeObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","loadIdObj:"+loadIdObj,100L);
				
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","route:"+route,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","loadId:"+loadId,100L);
				
				//validate modal form fields		
				if(detailForm != null ){
					selectedItems = ((RuntimeListFormInterface)detailForm).getSelectedItems();
					if(selectedItems != null && selectedItems.size() > 0){
						Object toRouteObj = session.getAttribute("REASSIGN_TO_ROUTE");
						Object toLoadIdObj = session.getAttribute("REASSIGN_TO_LOAD");
						Object toStopObj = session.getAttribute("REASSIGN_TO_STOP");
						Object fromStopObj = session.getAttribute("LOADSTOPID");
						
						String toRoute = toRouteObj == null ?"":toRouteObj.toString();
						String toLoadId = toLoadIdObj == null ?"":toLoadIdObj.toString();
						String toStop = toStopObj == null ?"":toStopObj.toString();
						String fromStop = fromStopObj == null ?"":fromStopObj.toString();
						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toRouteObj:"+toRouteObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toLoadIdObj:"+toLoadIdObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toStopObj:"+toStopObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","fromStopObj"+fromStopObj,100L);
						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toRoute:"+toRoute,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toLoadId:"+toLoadId,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toStop:"+toStop,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","fromStop:"+fromStop,100L);						
						
						//LoadId and Stop are required
						if(toLoadId.length() == 0){				
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"To Load Id\" Is Missing...",100L);											
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);							
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(toStop.length() == 0){															
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"To Stop\" Is Missing...",100L);
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(route.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Route\" Is Missing...",100L);												
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(loadId.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Load Id\" Is Missing...",100L);													
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(fromStop.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Stop\" Is Missing...",100L);											
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						
						for(int i = 0; i < selectedItems.size(); i++){
							Bio bio = (Bio)selectedItems.get(i);
							String detailId = bio.getString("LOADORDERDETAILID"); //AW Machine2058551_SDIS05162 edited attribute name
							WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
							Array parms = new Array(); 
							parms.add(new TextData(route));
							parms.add(new TextData(loadId));
							parms.add(new TextData(fromStop));
							parms.add(new TextData(detailId));
							parms.add(new TextData(toRoute));
							parms.add(new TextData(toLoadId));
							parms.add(new TextData(toStop));					
							actionProperties.setProcedureParameters(parms);
							actionProperties.setProcedureName("nspReassignLoadOrderDetail");
							try {
								EXEDataObject procResult = WmsWebuiActionsImpl.doAction(actionProperties);						
							} catch (Exception e) {
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Error Occured...",100L);															
								e.printStackTrace();
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);							
								String args[] = new String[0]; 
								String errorMsg = e.getLocalizedMessage();
								throw new UserException(errorMsg,new Object[0]);
							}
						}
						((RuntimeListFormInterface)detailForm).setSelectedItems(null);
					}
				}
				
				if(unitsForm != null ){
					selectedItems = ((RuntimeListFormInterface)unitsForm).getSelectedItems();
					if(selectedItems != null && selectedItems.size() > 0){
						Object toRouteObj = session.getAttribute("REASSIGN_TO_ROUTE");
						Object toLoadIdObj = session.getAttribute("REASSIGN_TO_LOAD");
						Object toStopObj = session.getAttribute("REASSIGN_TO_STOP");
						Object fromStopObj = session.getAttribute("LOADSTOPID");
						
						String toRoute = toRouteObj == null ?"":toRouteObj.toString();
						String toLoadId = toLoadIdObj == null ?"":toLoadIdObj.toString();
						String toStop = toStopObj == null ?"":toStopObj.toString();
						String fromStop = fromStopObj == null ?"":fromStopObj.toString();
						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toRouteObj:"+toRouteObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toLoadIdObj:"+toLoadIdObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toStopObj:"+toStopObj,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","fromStopObj:"+fromStopObj,100L);
						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toRoute:"+toRoute,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toLoadId:"+toLoadId,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","toStop:"+toStop,100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","fromStop:"+fromStop,100L);
						
						//LoadId and Stop are required
						if(toLoadId.length() == 0){				
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"To Load Id\" Is Missing...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);							
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(toStop.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"To Stop\" Is Missing...",100L);										
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(route.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Route\" Is Missing...",100L);													
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(loadId.length() == 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Load Id\" Is Missing...",100L);														
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						if(fromStop.length() == 0){											
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Required Field \"From Stop\" Is Missing...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);				
						}
						
						for(int i = 0; i < selectedItems.size(); i++){
							Bio bio = (Bio)selectedItems.get(i);
							String detailId = bio.getString("LOADUNITDETAILID");
							WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
							Array parms = new Array(); 
							parms.add(new TextData(route));
							parms.add(new TextData(loadId));
							parms.add(new TextData(fromStop));
							parms.add(new TextData(detailId));
							parms.add(new TextData(toRoute));
							parms.add(new TextData(toLoadId));
							parms.add(new TextData(toStop));					
							actionProperties.setProcedureParameters(parms);
							actionProperties.setProcedureName("nspReassignLoadUnit");
							try {
								EXEDataObject procResult = WmsWebuiActionsImpl.doAction(actionProperties);						
							} catch (Exception e) {
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Error Occured...",100L);								
								e.printStackTrace();
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);							
								String args[] = new String[0]; 
								String errorMsg = e.getLocalizedMessage();
								throw new UserException(errorMsg,new Object[0]);
							}
						}
						((RuntimeListFormInterface)unitsForm).setSelectedItems(null);
					}
				}
			}
		} catch (EpiDataException e) {
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Error Occured...",100L);			
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);							
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 
		session.removeAttribute("REASSIGN_TO_ROUTE");
		session.removeAttribute("REASSIGN_TO_LOAD");
		session.removeAttribute("REASSIGN_TO_STOP");
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ReassignAction",100L);
		return RET_CONTINUE;
		
	}	
}