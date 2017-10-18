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
package com.ssaglobal.scm.wms.wm_putaway_strategy;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;


public class ValidatePutawayStrategyCode extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidatePutawayStrategyCode.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		
		StateInterface state = context.getState();			
		RuntimeFormInterface toolBar = context.getSourceWidget().getForm();		
		RuntimeFormInterface shellForm = toolBar.getParentForm(state);			
		SlotInterface shellSlot1 = shellForm.getSubSlot("list_slot_1");		//HC
		SlotInterface shellSlot2= shellForm.getSubSlot("list_slot_2");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellSlot1, null);
		RuntimeFormInterface detailForm = state.getRuntimeForm(shellSlot2, null);
		if(detailForm != null && detailForm.getName().equals("wm_putaway_strategy_detail_toggle_slot")){
			SlotInterface toggleSlot = detailForm.getSubSlot("pastratdetail_toggle_slot");
			detailForm = state.getRuntimeForm(toggleSlot, "Detail");
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPUTAWAYSTRAT","headerForm:"+headerForm.getName(),100L);
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEPUTAWAYSTRAT","detailForm:"+detailForm.getName(),100L);
		String putawayStrategyKey = headerForm.getFormWidgetByName("PUTAWAYSTRATEGYKEY").getDisplayValue();
		if(headerForm.getFocus().isTempBio()){																			
			Query loadBiosQry = new Query("wm_putaway_strategy", "wm_putaway_strategy.PUTAWAYSTRATEGYKEY = '"+putawayStrategyKey.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					String args[] = {putawayStrategyKey}; 
					String errorMsg = getTextMessage("WMEXP_PUTAWAYSTRAT_DUP_CODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}		
		if(detailForm != null && !detailForm.isListForm() && detailForm.getFocus() != null ){
			try {
				SlotInterface tabgrpFormSlot=detailForm.getSubSlot("tbgrp_slot");
				detailForm = state.getRuntimeForm(tabgrpFormSlot,"tab 0");
				
				//rm Bugaware 6984
				/** Check Straegy
				 * if strategy = if source from, search specific zone | search specific zone | consolidate in specific zone
				 * zone is required
				 * else 
				 * set to space if empty
				 */
				String paType = (String) detailForm.getFocus().getValue("PATYPE");
				String zone = (String) detailForm.getFocus().getValue("ZONE");
				RuntimeFormWidgetInterface zoneWidget = detailForm.getFormWidgetByName("ZONE");
				if (paType != null && ("02".equals(paType) || "04".equals(paType) || "16".equals(paType))) {
					//make sure zone is filled in
					if (isEmpty(zone))	{
						_log.debug("LOG_DEBUG_EXTENSION_ValidatePutawayStrategyCode", "Zone is required", SuggestedCategory.NONE);
						throw new UserException("WMEXP_SPECIAL_NO_NULLS", new String[] { zoneWidget.getLabel("label",
								state.getLocale()).replaceAll(":", "") });
					}
				}
				else {
					//set zone to "" if empty
					if (isEmpty(zone)) {
						_log.debug("LOG_SYSTEM_OUT","Not Required",100L);
						detailForm.getFocus().setValue("ZONE", " ");
					}

				}
				
				//rm
				
				//jp
				if(detailForm.getFocus().isTempBio()){
					//jp
					//String putawayStrategyDetailKey = detailForm.getFormWidgetByName("PUTAWAYSTRATEGYLINENUMBER").getDisplayValue();			
					//Query loadBiosQry = new Query("wm_putaway_strategy_detail", "wm_putaway_strategy_detail.PUTAWAYSTRATEGYKEY = '"+putawayStrategyKey.toUpperCase()+"' AND wm_putaway_strategy_detail.PUTAWAYSTRATEGYLINENUMBER = '"+putawayStrategyDetailKey.toUpperCase()+"'", null);				

					String putawayStrategyDetailStepNumber = detailForm.getFormWidgetByName("STEPNUMBER").getDisplayValue();			
					Query loadBiosQry = new Query("wm_putaway_strategy_detail", "wm_putaway_strategy_detail.PUTAWAYSTRATEGYKEY = '"+putawayStrategyKey.toUpperCase()+"' AND wm_putaway_strategy_detail.STEPNUMBER = '"+putawayStrategyDetailStepNumber.toUpperCase()+"'", null);				

					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);																	
					
					if (Integer.parseInt(putawayStrategyDetailStepNumber) <= 0 ){
						throw new FormException ("WMEXP_NOT_LESS_ZERO",null);
					}
					
					if(bioCollection.size() > 0){
						String args[] = {putawayStrategyDetailStepNumber}; 
						String errorMsg = getTextMessage("WMEXP_DUP_LINENUMBER",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}else{
					String putawayStrategyDetailStepNumber = detailForm.getFormWidgetByName("STEPNUMBER").getDisplayValue();
					String putawayStrategyDetailKey = detailForm.getFormWidgetByName("PUTAWAYSTRATEGYLINENUMBER").getDisplayValue();
					Query loadBiosQry = new Query("wm_putaway_strategy_detail", 
							"wm_putaway_strategy_detail.PUTAWAYSTRATEGYKEY = '"+putawayStrategyKey.toUpperCase()+"' " +
							" AND wm_putaway_strategy_detail.STEPNUMBER = '"+putawayStrategyDetailStepNumber.toUpperCase()+"' " +
							" AND wm_putaway_strategy_detail.PUTAWAYSTRATEGYLINENUMBER != '"+putawayStrategyDetailKey.toUpperCase()+"' ", null);				

					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);																	
					
					if (Integer.parseInt(putawayStrategyDetailStepNumber) <= 0 ){
						throw new FormException ("WMEXP_NOT_LESS_ZERO",null);
					}
					
					if(bioCollection.size() > 0){
						String args[] = {putawayStrategyDetailStepNumber}; 
						String errorMsg = getTextMessage("WMEXP_DUP_LINENUMBER",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
					
				}
					
//				SlotInterface locationFormSlot= detailForm.getSubSlot("FROMTOLOC");		//HC
//				RuntimeFormInterface locationForm = state.getRuntimeForm(locationFormSlot, null);
				String fromLocation = detailForm.getFormWidgetByName("FROMLOC").getDisplayValue();
				String toLocation = detailForm.getFormWidgetByName("TOLOC").getDisplayValue();
				if(fromLocation != null && fromLocation.trim().length() > 0){
					Query loadBiosQry = new Query("wm_loc_ps", "wm_loc_ps.LOC = '"+fromLocation.toUpperCase()+"'", null);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection.size() == 0){
						String args[] = {fromLocation}; 
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}							
				if(toLocation != null && toLocation.trim().length() > 0){
					Query loadBiosQry = new Query("wm_loc_ps", "wm_loc_ps.LOC = '"+toLocation.toUpperCase()+"'", null);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection.size() == 0){
						String args[] = {toLocation}; 
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
					
					loadBiosQry = new Query("wm_loc_ps", "wm_loc_ps.LOC = '"+toLocation.toUpperCase()+"' AND (wm_loc_ps.LOCATIONTYPE = 'PICKTO' OR wm_loc_ps.LOCATIONTYPE = 'STAGED')", null);
					bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection.size() > 0){
						String args[] = {toLocation}; 
						String errorMsg = getTextMessage("WMEXP_CHKPICKTOLOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		return RET_CONTINUE;
		
	}
	
	protected boolean isEmpty(Object attributeValue) {

		if (attributeValue == null) {
			return true;
		}
		else if (attributeValue.toString().matches("\\s*")) {
			return true;
		}
		else {
			return false;
		}

	}
}