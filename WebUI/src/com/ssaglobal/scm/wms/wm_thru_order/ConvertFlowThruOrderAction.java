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


package com.ssaglobal.scm.wms.wm_thru_order;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ConvertFlowThruOrderAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertFlowThruOrderAction.class);
	private static final String SCREEN_NAME_FLOW_THRU_ORDER = "flow.thru.order";
	private static final String SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION = "flow.thru.order.conversion";
	private static final String SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION = "flow.thru.order.allocation";
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Executing ConvertFlowThruOrderAction",100L);		
		StateInterface state = context.getState();	
		String screenName = "";
		
		//Determine what screen user is on based on which forms are found...
		
		//Try To Get Forms From Screen Flow Thru Order...
		RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_header_list_view",state);
		RuntimeListFormInterface detailListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_detail_list_view",state);
		
		if(headerListForm != null || detailListForm != null){
			if(headerListForm != null)
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Header Form:"+headerListForm.getName(),100L);				
			else
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Header Form:Null",100L);				
			if(detailListForm != null)
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:"+detailListForm.getName(),100L);				
			else
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:Null",100L);							
			_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Screen Is Flow Thru Order",100L);
			screenName = SCREEN_NAME_FLOW_THRU_ORDER;
		}
		//Try To Get Forms From Screen Flow Thru Order Conversion...
		else{
			headerListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_flow_thru_order_conversion_header_list_view",state);
			detailListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_flow_thru_order_conversion_detail_list_view",state);
			if(headerListForm != null || detailListForm != null){
				if(headerListForm != null)
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Header Form:"+headerListForm.getName(),100L);				
				else
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Header Form:Null",100L);				
				if(detailListForm != null)
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:"+detailListForm.getName(),100L);				
				else
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:Null",100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Screen Is Flow Thru Order Conversion",100L);				
				screenName = SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION;
			}
			
			//Try To Get Forms From Screen Flow Thru Order Allocation...
			else{				
				detailListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_list_view",state);
				if(detailListForm != null){										
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:"+detailListForm.getName(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Screen Is Flow Thru Order Allocation",100L);												
					screenName = SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION;
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Found Detail Form:NULL",100L);
					_log.error("LOG_DEBUG_EXTENSION_CONVERFTOACTION","ERROR: Screen could not be determined...",100L);																											
					return RET_CANCEL;
				}
			}
		}
		int totalCount = 0;
		boolean isHeaderSelectedListEmpty = true;
		boolean isDetailSelectedListEmpty = true;
		if(headerListForm != null && headerListForm.getSelectedItems() != null && headerListForm.getSelectedItems().size() > 0){
			isHeaderSelectedListEmpty = false;
		}
		if(detailListForm != null && detailListForm.getSelectedItems() != null && detailListForm.getSelectedItems().size() > 0){
			isDetailSelectedListEmpty = false;
		}
		if(isHeaderSelectedListEmpty){
			if(isDetailSelectedListEmpty){
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Cleaning Session...",100L);				
				state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
				state.getRequest().getSession().removeAttribute("didCloseRetainRouteConfModal");
				state.getRequest().getSession().removeAttribute("didCancelRetainRouteModal");				
				state.getRequest().getSession().removeAttribute("CONVERTFTOXORDERKEY");		
				state.getRequest().getSession().removeAttribute("CONVERTFTOLOADID");
				state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			else{
				totalCount += detailListForm.getSelectedItems().size();
			}
		}
		else{
			totalCount += headerListForm.getSelectedItems().size();
		}
		
		if(totalCount > 1){			
			_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);
			state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
			state.getRequest().getSession().removeAttribute("didCloseRetainRouteConfModal");
			state.getRequest().getSession().removeAttribute("didCancelRetainRouteModal");				
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERKEY");	
			state.getRequest().getSession().removeAttribute("CONVERTFTOLOADID");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_MORE_THAN_ONE_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		RuntimeListFormInterface processingForm = null;
		if(!isDetailSelectedListEmpty){
			processingForm = detailListForm;
		}else{
			processingForm = headerListForm;
		}
		Object didCloseInitialConfModalObj = state.getRequest().getSession().getAttribute("didCloseInitialConfModal");
		Object didCloseRetainRouteConfModalObj = state.getRequest().getSession().getAttribute("didCloseRetainRouteConfModal");
		Object didCancelRetainRouteModalObj = state.getRequest().getSession().getAttribute("didCancelRetainRouteModal");
		
		_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Got Objects didCloseInitialConfModalObj:"+didCloseInitialConfModalObj+" And didCloseRetainRouteConfModalObj:"+didCloseRetainRouteConfModalObj+" From Session",100L);		
		state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
		state.getRequest().getSession().removeAttribute("didCloseRetainRouteConfModal");		
		state.getRequest().getSession().removeAttribute("didCancelRetainRouteModal");
		String didCloseInitialConfModal = didCloseInitialConfModalObj==null?"false":didCloseInitialConfModalObj.toString();
		String didCloseRetainRouteConfModal = didCloseRetainRouteConfModalObj==null?"false":didCloseRetainRouteConfModalObj.toString();
		String didCancelRetainRouteModal = didCancelRetainRouteModalObj==null?"false":didCancelRetainRouteModalObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Created Strings didCloseInitialConfModal:"+didCloseInitialConfModal+" And didCloseRetainRouteConfModal:"+didCloseRetainRouteConfModal,100L);		
		if(didCloseInitialConfModal.equals("false") && didCloseRetainRouteConfModal.equals("false") && didCancelRetainRouteModal.equals("false")){
			ArrayList selectedItems = processingForm.getAllSelectedItems();
			_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Selected Items:"+selectedItems,100L);			
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator bioBeanIter = selectedItems.iterator();								
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					bio = (BioBean)bioBeanIter.next();						
					String orderKey = bio.getString("ORDERKEY");
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Got ORDERKEY:"+orderKey,100L);					
					state.getRequest().getSession().removeAttribute("CONVERTFTOORDERKEY");
					state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
					state.getRequest().getSession().setAttribute("CONVERTFTOORDERKEY",orderKey);
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting CONVERTFTOORDERKEY:"+orderKey+" In Session",100L);					
					if(!isDetailSelectedListEmpty){
						String orderLineNumber = bio.getString("ORDERLINENUMBER");
						_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Got ORDERLINENUMBER:"+orderLineNumber,100L);						
						state.getRequest().getSession().setAttribute("CONVERTFTOORDERLINENUMBER",orderLineNumber);
						_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting CONVERTFTOORDERLINENUMBER:"+orderLineNumber+" In Session",100L);						
					}					
				}				
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation menuClickEvent356",100L);					
					context.setNavigation("menuClickEvent356");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation menuClickEvent285",100L);					
					context.setNavigation("menuClickEvent285");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation menuClickEvent279",100L);					
					context.setNavigation("menuClickEvent279");
				}
				return RET_CONTINUE;
				
			}
		}
				
		if(didCloseInitialConfModal.equals("true")){			
			Object orderKeyObj = state.getRequest().getSession().getAttribute("CONVERTFTOORDERKEY");
			Object orderLineNumberObj = state.getRequest().getSession().getAttribute("CONVERTFTOORDERLINENUMBER");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERKEY");
			state.getRequest().getSession().removeAttribute("CONVERTFTOLOADID");
			_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Got Object orderKeyObj:"+orderKeyObj+" From Session",100L);			
			if(orderKeyObj == null ){
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);						
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			Query loadBiosQry = new Query("wm_xorders_to", "wm_xorders_to.ORDERKEY = '"+orderKeyObj.toString().toUpperCase()+"'", "");
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
			Bio bio = bioCollection.elementAt(0);
			Object loadIdObj = bio.get("LOADID");
			String loadId = loadIdObj == null?"":loadIdObj.toString();
			if(loadId.length() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);				
				state.getRequest().getSession().setAttribute("CONVERTFTOORDERKEY",orderKeyObj.toString());
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting CONVERTFTOORDERKEY:"+orderKeyObj.toString()+" In Session",100L);				
				state.getRequest().getSession().setAttribute("CONVERTFTOLOADID",loadId);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting CONVERTFTOLOADID:"+loadId+" In Session",100L);				
				if(!isDetailSelectedListEmpty){
					state.getRequest().getSession().setAttribute("CONVERTFTOORDERLINENUMBER",orderLineNumberObj.toString());
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting CONVERTFTOORDERLINENUMBER:"+orderLineNumberObj.toString()+" In Session",100L);					
				}
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog34",100L);					
					context.setNavigation("closeModalDialog34");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog46",100L);					
					context.setNavigation("closeModalDialog46");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog50",100L);					
					context.setNavigation("closeModalDialog50");
				}				
				return RET_CONTINUE;
			}
			if(!isHeaderSelectedListEmpty){
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 1:"+orderKeyObj.toString(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 2:ALL",100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:"+loadId,100L);
				parms.add(new TextData(orderKeyObj.toString()));
				parms.add(new TextData("ALL"));
				parms.add(new TextData(loadId));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPCONVERTFLOWTHRUORDERS");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				}catch (Exception e) {
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);					
					throw new UserException(e.getLocalizedMessage(),new Object[0]);					
				}
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog35",100L);					
					context.setNavigation("closeModalDialog35");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog47",100L);					
					context.setNavigation("closeModalDialog47");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
//					Should not be here
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);								
					String args[] = new String[0];
					String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);	
				}						
			}
			else{
				
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 1:"+orderKeyObj.toString(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 2:"+orderLineNumberObj.toString(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:"+loadId,100L);				
				parms.add(new TextData(orderKeyObj.toString()));
				parms.add(new TextData(orderLineNumberObj.toString()));
				parms.add(new TextData(loadId));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));				
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPCONVERTFLOWTHRUORDERS");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				}catch (Exception e) {							
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);
					throw new UserException(e.getLocalizedMessage(),new Object[0]);					
				}
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog36",100L);					
					context.setNavigation("closeModalDialog36");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog48",100L);					
					context.setNavigation("closeModalDialog48");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation menuClickEvent356",100L);					
					context.setNavigation("closeModalDialog51");
				}				
			}
			result.setSelectedItems(null);
			processingForm.setSelectedItems(null);										
		}	
		if(didCloseRetainRouteConfModal.equals("true") || didCancelRetainRouteModal.equals("true")){			
			Object orderKeyObj = state.getRequest().getSession().getAttribute("CONVERTFTOORDERKEY");
			Object orderLineNumberObj = state.getRequest().getSession().getAttribute("CONVERTFTOORDERLINENUMBER");
			Object loadIdObj = state.getRequest().getSession().getAttribute("CONVERTFTOLOADID");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERKEY");
			state.getRequest().getSession().removeAttribute("CONVERTFTOLOADID");
			_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Got Object orderKeyObj:"+orderKeyObj+" orderLineNumberObj:"+orderLineNumberObj+" loadIdObj:"+loadIdObj+"  From Session",100L);			
			if(orderKeyObj == null || loadIdObj == null){
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);							
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			
			if(!isHeaderSelectedListEmpty){
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 1:"+orderKeyObj.toString(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 2:ALL",100L);								
				parms.add(new TextData(orderKeyObj.toString()));
				parms.add(new TextData("ALL"));
				if(didCloseRetainRouteConfModal.equals("true")){
					parms.add(new TextData(loadIdObj.toString()));					
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:"+loadIdObj.toString(),100L);
				}
				else{
					parms.add(new TextData(""));					
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:",100L);
				}
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));				
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPCONVERTFLOWTHRUORDERS");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				}catch (Exception e) {							
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);					
					throw new UserException(e.getLocalizedMessage(),new Object[0]);					
				}
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog35",100L);					
					context.setNavigation("closeModalDialog35");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog47",100L);					
					context.setNavigation("closeModalDialog47");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
					//Should not be here
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);			
					String args[] = new String[0];
					String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);	
				}				
			}
			else{
				
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				parms.add(new TextData(orderKeyObj.toString()));
				parms.add(new TextData(orderLineNumberObj.toString()));
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 1:"+orderKeyObj.toString(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 2:ALL",100L);					
				if(didCloseRetainRouteConfModal.equals("true")){
					parms.add(new TextData(loadIdObj.toString()));					
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:"+loadIdObj.toString(),100L);
				}
				else{
					parms.add(new TextData(""));					
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Param 3:",100L);
				}
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPCONVERTFLOWTHRUORDERS");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				}catch (Exception e) {							
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);					
					throw new UserException(e.getLocalizedMessage(),new Object[0]);					
				}
				if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog36",100L);					
					context.setNavigation("closeModalDialog36");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_CONVERSION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog48",100L);					
					context.setNavigation("closeModalDialog48");
				}
				else if(screenName.equals(SCREEN_NAME_FLOW_THRU_ORDER_ALLOCATION)){
					_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Setting Navigation closeModalDialog51",100L);					
					context.setNavigation("closeModalDialog51");
				}				
			}
			result.setSelectedItems(null);
			processingForm.setSelectedItems(null);					
		}
		_log.debug("LOG_DEBUG_EXTENSION_CONVERFTOACTION","Exiting ConvertFlowThruOrderAction",100L);		
		return RET_CONTINUE;
		
	}
	
	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		try {
			// Add your code here to process the event
			
		} catch(Exception e) {
			
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		
		return RET_CONTINUE;
	}
}
