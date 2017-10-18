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
package com.ssaglobal.scm.wms.wm_flow_thru_lane;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateFlowThruLane extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateFlowThruLane.class);
	
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Executing ValidateFlowThruLane",100L);		
		StateInterface state = context.getState();			
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_flow_thru_lane_detail_view",state);		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Found Header Form:Null",100L);			
		
		if(headerForm != null){
			Object doorObj = headerForm.getFormWidgetByName("DOOR");
			Object storerObj = headerForm.getFormWidgetByName("LANEASSG1");
			Object skuObj = headerForm.getFormWidgetByName("LANEASSG2");
			Object laneKeyObj = headerForm.getFormWidgetByName("LANEKEY");
			Object laneTypeObj = headerForm.getFormWidgetByName("LANETYPE");
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Storer:"+storerObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Sku:"+skuObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","DOOR:"+doorObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","LANEKEY:"+laneKeyObj,100L);
			
			if(doorObj != null){				
				String door = ((RuntimeWidget)doorObj).getDisplayValue();
				if(door != null){
					door = door.toUpperCase();
					//storer = storer.trim();
					if(door.length() > 0){
						Query loadBiosQry = new Query("wm_loc_ftl", "wm_loc_ftl.LOC = '"+door+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 						
								args[0] = door;
								String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						loadBiosQry = new Query("wm_loc_ftl", "(wm_loc_ftl.LOCATIONTYPE = 'STAGED' OR wm_loc_ftl.LOCATIONTYPE = 'DOOR') AND wm_loc_ftl.LOC = '" + door + "'", "");
						uow = context.getState().getDefaultUnitOfWork();									
						bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 						
								args[0] = door;
								String errorMsg = getTextMessage("WMEXP_LOC_MUST_BE_STAGED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
			}
			String storer = "";
			if(storerObj != null){				
				storer = ((RuntimeWidget)storerObj).getDisplayValue();
				if(storer != null){
					storer = storer.toUpperCase();
					//storer = storer.trim();
					if(storer.length() > 0){
						Query loadBiosQry = new Query("wm_storer_ac", "wm_storer_ac.STORERKEY = '"+storer+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 						
								args[0] = storer;
								String errorMsg = getTextMessage("WMEXP_INVALID_STORER",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
			}
			String sku = "";
			if(skuObj != null){				
				sku = ((RuntimeWidget)skuObj).getDisplayValue();
				if(sku != null){
					sku = sku.toUpperCase();
					//sku = sku.trim();
					if(sku.length() > 0){
						if(storer == null || storer.length() == 0){
							String args[] = new String[0]; 							
							String errorMsg = getTextMessage("WMEXP_LANEASSIGN1_EMPTY",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						Query loadBiosQry = new Query("wm_sku_ac", "wm_sku_ac.SKU = '"+sku+"' AND wm_sku_ac.STORERKEY = '"+storer+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[2]; 
								args[0] = sku;	
								args[1] = storer;	
								String errorMsg = getTextMessage("WMEXP_SKU_MUST_BELONG_TO_STORER",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
			}
			if(laneKeyObj != null){				
				String laneKey = ((RuntimeWidget)laneKeyObj).getDisplayValue();
				if(laneKey != null){
					laneKey = laneKey.toUpperCase();
					//storer = storer.trim();
					if(laneKey.length() > 0){
						Query loadBiosQry = new Query("wm_loc_ftl", "wm_loc_ftl.LOC = '"+laneKey+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						try {
							if(bioCollection.size() == 0){							
								String args[] = new String[1]; 						
								args[0] = laneKey;
								String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						if(headerForm.getFocus().isTempBio()){
							loadBiosQry = new Query("wm_lane_ftl", "wm_lane_ftl.LANEKEY = '"+laneKey+"'", "");
							uow = context.getState().getDefaultUnitOfWork();									
							bioCollection = uow.getBioCollectionBean(loadBiosQry);
							try {
								if(bioCollection.size() > 0){							
									String args[] = new String[1]; 						
									args[0] = laneKey;
									String errorMsg = getTextMessage("WMEXP_LANEKEY_IN_USE",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (EpiDataException e) {
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}
			}			
			if(laneTypeObj != null){
				laneTypeObj = ((RuntimeWidget)laneTypeObj).getValue();
				if(laneTypeObj != null){
					String laneType = laneTypeObj.toString();
					if(laneType != null){	
						laneType = laneType.toUpperCase();
						if(laneType.length() > 0){								
							if(laneType.equals("1") && (storer == null || storer.length() == 0 || sku == null || sku.length() == 0)){							
								String args[] = new String[0]; 														
								String errorMsg = getTextMessage("WMEXP_BOTH_LANEASSIGN_REQUIRED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);													
							}
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();					
							String storerType = "";
							String queryStatement = null;
							Query query = null;
							BioCollection results = null;
							try
							{											
								if(storer == null || storer.length() == 0)
									return RET_CONTINUE;					
								if(laneType.equals("2") || laneType.equals("3")){
									storerType = "2";
								}else if(laneType.equals("1")){
									storerType = "1";
								}
								if(storerType.length() > 0){
									queryStatement = "wm_storer.STORERKEY = '" + storer + "'" + " AND wm_storer.TYPE = " + storerType;								
									query = new Query("wm_storer", queryStatement, null);
									results = uow.getBioCollectionBean(query);
									if (results.size() == 0)
									{
										String args[] = new String[1]; 						
										args[0] = storer;
										if(storerType.equals("1")){
											String errorMsg = getTextMessage("WMEXP_INVALID_STORER_MUST_BE_1",args,state.getLocale());
											throw new UserException(errorMsg,new Object[0]);
										}
										else{
											String errorMsg = getTextMessage("WMEXP_INVALID_STORER_MUST_BE_2",args,state.getLocale());
											throw new UserException(errorMsg,new Object[0]);
										}
										
									}								
								}
							} catch (EpiDataException e)
							{
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEFLOWTHRULANE","Leaving ValidateFlowThruLane",100L);		
		return RET_CONTINUE;
		
	}
}