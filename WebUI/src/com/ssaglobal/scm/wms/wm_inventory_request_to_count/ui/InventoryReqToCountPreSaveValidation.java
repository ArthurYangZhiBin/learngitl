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
package com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InventoryReqToCountPreSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryReqToCountPreSaveValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Executing InventoryReqToCountPreSaveValidation",100L);
		InventoryReqToCountDataObject data = new InventoryReqToCountDataObject();
			
	  	StateInterface state = context.getState();
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	  	
	    DataBean detailFocus = detailForm.getFocus();

	    boolean isNewDetail = false;
		if(detailFocus.isTempBio()){
			isNewDetail = true;
		}
	    
	    data.setProcessFlag((String)detailFocus.getValue("PROCESSFLAG"));
	    
	    Object objOwnerSt = detailFocus.getValue("STORERKEYSTART");
		if( objOwnerSt != null && !objOwnerSt.toString().equalsIgnoreCase("")){
			data.setOwnerStart(objOwnerSt.toString());
		}
		else
		{data.setOwnerStart("0");
		 detailFocus.setValue("STORERKEYSTART", "0" );
		}
	    
	    Object objOwnerEnd = detailFocus.getValue("STORERKEYEND");
		if( objOwnerEnd != null && !objOwnerEnd.toString().equalsIgnoreCase("")){
			data.setOwnerEnd(objOwnerEnd.toString());
		}
		else{data.setOwnerEnd("ZZZZZZZZZZZZZZZ");
		 detailFocus.setValue("STORERKEYEND", "ZZZZZZZZZZZZZZZ" );
		}
		
	    Object objItemStart = detailFocus.getValue("SKUSTART");
		if( objItemStart != null && !objItemStart.toString().equalsIgnoreCase("")){
			data.setItemStart(objItemStart.toString());
		}
		else{data.setItemStart("0");
		detailFocus.setValue("SKUSTART", "0" );
		}
		
	    Object objItemEnd = detailFocus.getValue("SKUEND");
		if( objItemEnd != null && !objItemEnd.toString().equalsIgnoreCase("")){
			data.setItemEnd(objItemEnd.toString());
		}
		else {data.setItemEnd("ZZZZZZZZZZZZZZZZZZZZ");
		detailFocus.setValue("SKUEND", "ZZZZZZZZZZZZZZZZZZZZ" );
		}
		
	    Object objLocStart = detailFocus.getValue("LOCSTART");
		if( objLocStart != null && !objLocStart.toString().equalsIgnoreCase("")){
			data.setLocStart(objLocStart.toString());
		}
		else {data.setLocStart("0");
		detailFocus.setValue("LOCSTART", "0" );
		}
		
	    Object objLocEnd = detailFocus.getValue("LOCEND");
		if( objLocEnd != null && !objLocEnd.toString().equalsIgnoreCase("")){
			data.setLocEnd(objLocEnd.toString());
		}
		else{data.setLocEnd("ZZZZZZZZZZ");
		detailFocus.setValue("LOCEND", "ZZZZZZZZZZ" );
		}
		
	    Object objZoneStart = detailFocus.getValue("ZONESTART");
		if( objZoneStart != null && !objZoneStart.toString().equalsIgnoreCase("")){
			data.setZoneStart(objZoneStart.toString());
		}
		else{data.setZoneStart("0");
		detailFocus.setValue("ZONESTART", "0" );
		}
		
	    Object objZoneEnd = detailFocus.getValue("ZONEEND");
		if( objZoneEnd != null && !objZoneEnd.toString().equalsIgnoreCase("")){
			data.setZoneEnd(objZoneEnd.toString());
		}
		else{data.setZoneEnd("ZZZZZZZZZZ");
		detailFocus.setValue("ZONEEND", "ZZZZZZZZZZ" );
		}
		
	    Object objAreaStart = detailFocus.getValue("AREASTART");
		if( objAreaStart != null && !objAreaStart.toString().equalsIgnoreCase("")){
			data.setAreaStart(objAreaStart.toString());
		}
		else{data.setAreaStart("0");
		detailFocus.setValue("AREASTART", "0" );
		}
		
	    Object objAreaEnd = detailFocus.getValue("AREAEND");
		if( objAreaEnd != null && !objAreaEnd.toString().equalsIgnoreCase("")){
			data.setAreaEnd(objAreaEnd.toString());
		}
		else{data.setAreaEnd("ZZZZZZZZZZ");
		detailFocus.setValue("AREAEND", "ZZZZZZZZZZ" );
		}
		
		//validate ownerSt
		boolean validate = false;
		try{
			String str = "START";
			String defaultVal = "0";
			validate = InventoryReqToCountUtil.isValidOwner(data, str, defaultVal);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getOwnerStart();
			throw new UserException("WMEXP_OWNER_VALIDATION", param);			
		}	
		
		//validate ownerEnd
		validate = false;
		try{
			String str = "END";
			String defaultVal = "ZZZZZZZZZZZZZZZ";
			validate = InventoryReqToCountUtil.isValidOwner(data, str, defaultVal);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getOwnerEnd();
			throw new UserException("WMEXP_OWNER_VALIDATION", param);			
		}	
		
		//validate ItemSt
		validate = false;
		try{
			String str = "START";
			String defaultVal = "0";
			validate = InventoryReqToCountUtil.isValidItem(data, str, defaultVal);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getItemStart();
			throw new UserException("WMEXP_INVALID_ITEM", param);			
		}	
		
//		validate ItemEnd
		validate = false;
		try{
			String str = "END";
			String defaultVal = "ZZZZZZZZZZZZZZZZZZZZ";
			validate = InventoryReqToCountUtil.isValidItem(data, str, defaultVal);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getItemEnd();
			throw new UserException("WMEXP_INVALID_ITEM", param);			
		}	

		//validate LocSt
		validate = false;		
		try {
			String str = "START";
			String defaultVal = "0";
			validate = InventoryReqToCountUtil.isValidLocation(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );	
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getLocStart();
			throw new UserException("WMEXP_INVALID_LOC", param);
			}
		
		//validate LocEnd
		validate = false;	
		try {
			String str = "END";
			String defaultVal = "ZZZZZZZZZZ";
			validate = InventoryReqToCountUtil.isValidLocation(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );	
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getLocEnd();
			throw new UserException("WMEXP_INVALID_LOC", param);
			}
		
		//validate ZoneSt
		validate = false;
		try {
			String str = "START";
			String defaultVal = "0";
			validate = InventoryReqToCountUtil.isValidPutawayzone(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );		
		}
		if(!validate){
		String [] param = new String[1];
		param[0] = data.getZoneStart();
		throw new UserException("WMEXP_INVALID_ZONE", param);
		}
		
		//validate ZoneSt
		validate = false;		
		try {
			String str = "END";
			String defaultVal = "ZZZZZZZZZZ";
			validate = InventoryReqToCountUtil.isValidPutawayzone(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );	
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = data.getZoneEnd();
			throw new UserException("WMEXP_INVALID_ZONE", param);
			}
		

		
		
		//validate AreaSt
		validate = false;
		try {
			String str = "START";
			String defaultVal = "0";
			validate = InventoryReqToCountUtil.isValidArea(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );		
		}
		if(!validate){
		String [] param = new String[1];
		param[0] = data.getAreaStart();
		throw new UserException("WMEXP_INVALID_AREA", param);
		}
		
		//validate AreaEnd
		validate = false;
		try {
			String str = "END";
			String defaultVal = "ZZZZZZZZZZ";
			validate = InventoryReqToCountUtil.isValidArea(data, str, defaultVal);
		} catch (DPException dpE) {
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );		
		}
		if(!validate){
		String [] param = new String[1];
		param[0] = data.getAreaEnd();
		throw new UserException("WMEXP_INVALID_AREA", param);
		}
		
		
		/*
		//if both Owner start and Owner end are the same, Item Start/End should be the same
		if(data.getOwnerStart().equals(data.getOwnerEnd()))
		{
			if(!(data.getItemStart().equals(data.getItemEnd())))
			{
				//throw error
				String[] param = new String[2];
				param[0] = data.getItemStart();
				param[1] = data.getItemEnd();
				throw new UserException("WMEXP_ITEMS_NOT_SAME", param);
			}
		}
		*/
		
		_log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Exiting InventoryReqToCountPreSaveValidation",100L);
		return RET_CONTINUE;
	}

}
