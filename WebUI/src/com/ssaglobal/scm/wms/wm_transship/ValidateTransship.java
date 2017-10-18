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
package com.ssaglobal.scm.wms.wm_transship;
import java.util.ArrayList;

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


public class ValidateTransship extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateTransship.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Executing ValidateTransship",100L);		
		StateInterface state = context.getState();	
		
		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("tab 1");	
		tabList.add("wm_transship_detail_toggle_detail");
		RuntimeFormInterface containerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_header_container_location_view",tabList,state);		
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Header Form:Null",100L);			
		if(containerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Container Form:"+containerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Container Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Found Detail Form:Null",100L);			
		
		//validate header form fields		
		if(headerForm != null){
			Object currentLocObj = headerForm.getFormWidgetByName("CURRENTLOC");
			Object doorObj = headerForm.getFormWidgetByName("DOOR");			
			
			String currentLoc = currentLocObj == null || ((RuntimeWidget)currentLocObj).getDisplayValue() == null?"":((RuntimeWidget)currentLocObj).getDisplayValue();
			String door = doorObj == null || ((RuntimeWidget)doorObj).getDisplayValue() == null?"":((RuntimeWidget)doorObj).getDisplayValue();			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","currentLocObj:"+currentLocObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","doorObj:"+doorObj,100L);			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","currentLoc:"+currentLoc,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","door:"+door,100L);			
			//Validate Current Loc, if present must be present in LOC table
			if(currentLoc.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating CURRENTLOC...",100L);					
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+currentLoc.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","CURRENTLOC "+currentLoc+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);						
						String args[] = new String[1]; 						
						args[0] = currentLoc;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate Door, if present must be present in LOC table and Type STAGED
			if(door.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating DOOR...",100L);
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+door.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","DOOR "+door+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = door;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
					loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "' AND (wm_location.LOCATIONTYPE = 'STAGED' OR wm_location.LOCATIONTYPE = 'DOOR')", "");
					bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","DOOR "+door+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = door;
						String errorMsg = getTextMessage("WMEXP_INVALID_DOOR",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}			
		
//		validate detail form fields		
		if(detailForm != null){
			Object storerObj = detailForm.getFormWidgetByName("STORERKEY");
			Object skuObj = detailForm.getFormWidgetByName("SKU");	
			
			String storer = storerObj == null || ((RuntimeWidget)storerObj).getDisplayValue() == null?"":((RuntimeWidget)storerObj).getDisplayValue();
			String sku = skuObj == null || ((RuntimeWidget)skuObj).getDisplayValue() == null?"":((RuntimeWidget)skuObj).getDisplayValue();			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","storerObj:"+storerObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","skuObj:"+skuObj,100L);			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","storer:"+storer,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","sku:"+sku,100L);						
			
//			Validate Storer Key, if present must be present in STORER table
			if(storer.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating STORERKEY...",100L);					
					Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+storer.toUpperCase()+"' AND wm_storer.TYPE = '1'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","STORERKEY "+storer.toUpperCase()+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = storer;
						String errorMsg = getTextMessage("WMEXP_OWNER_VALID",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
//			Validate SKU Key, if present must be present in SKU table
			if(sku.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating SKU...",100L);					
					Query loadBiosQry = new Query("wm_sku", "wm_sku.SKU = '"+sku.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","SKU "+sku.toUpperCase()+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = sku;
						String errorMsg = getTextMessage("WMEXP_INVAID_SKU",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		
//		validate container form fields		
		if(containerForm != null){
			Object fromLocObj = containerForm.getFormWidgetByName("FROMLOC");
			Object toLocObj = containerForm.getFormWidgetByName("TOLOC");			
			
			String fromLoc = fromLocObj == null || ((RuntimeWidget)fromLocObj).getDisplayValue() == null?"":((RuntimeWidget)fromLocObj).getDisplayValue();
			String toLoc = toLocObj == null || ((RuntimeWidget)toLocObj).getDisplayValue() == null?"":((RuntimeWidget)toLocObj).getDisplayValue();			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","fromLocObj:"+fromLocObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","toLocObj:"+toLocObj,100L);			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","fromLoc:"+fromLoc,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","toLoc:"+toLoc,100L);
			
//			Validate From Loc, if present must be present in LOC table
			if(fromLoc.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating FROMLOC...",100L);
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+fromLoc.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","FROMLOC "+fromLoc.toUpperCase()+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = fromLoc;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
//			Validate To Loc, if present must be present in LOC table
			if(toLoc.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Validating TOLOC...",100L);					
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+toLoc.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","TOLOC "+toLoc.toUpperCase()+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = toLoc;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSSHIP","Leaving ValidateTransship",100L);
		return RET_CONTINUE;
		
	}	
}