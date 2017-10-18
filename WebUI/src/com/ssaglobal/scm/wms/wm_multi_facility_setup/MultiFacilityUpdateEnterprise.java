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
package com.ssaglobal.scm.wms.wm_multi_facility_setup;
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.MFDBIdentifier;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;


public class MultiFacilityUpdateEnterprise extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityUpdateEnterprise.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACUPDTENT","Executing MultiFacilityUpdateEnterprise",100L);		
		StateInterface state = context.getState();
		//Populate Division, Distribution Center, Warehouse, and Enterprise records for "Item" search		    	      
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_multi_facility_setup_cascading_form",state);
		if(form == null){   
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","form is null...",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		RuntimeMenuFormWidgetInterface widgetFacility = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("TreeMenu");
		if(widgetFacility == null){    
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","widget is null...",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		if(widgetFacility.getSelectedItem() == null){
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","nothing selected...",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		BioRef selectedFacilityRef = widgetFacility.getSelectedItem();		
		BioBean facilityRecord = null;
		try {
			facilityRecord = state.getDefaultUnitOfWork().getBioBean(selectedFacilityRef);			
		} catch (BioNotFoundException e1) {	
			e1.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","exception...",100L,e1);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);		
		} 
		if(facilityRecord == null){    
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","facilityRecord is null...",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		if(!facilityRecord.get("LEVELNUM").toString().equals("-1")){
			_log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","non warehouse...",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_MULTI_FAC_SETUP_NON_WHSE",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 					
		Array parmsDBList = new Array(MFDBIdentifier.class);	
		
		parms.add(new TextData(UserUtil.getUserId(state)));
				
		MFDBIdentifier singleDBIdentifier = new MFDBIdentifier();					
		singleDBIdentifier.tag("target");
		_log.debug("LOG_SYSTEM_OUT","(String)facilityRecord.get(\"NAME\")"+(String)facilityRecord.get("NAME"),100L);
		singleDBIdentifier.connectionId((String)facilityRecord.get("NAME"));			               
		parmsDBList.add(singleDBIdentifier);
		
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("UPDATEENTERPRISEDATA");
		actionProperties.setDbNames(parmsDBList);
		try {
			WmsWebuiActionsImpl.doMFAction(actionProperties);																							
			
		}catch (Exception e) {
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}					
		
		return RET_CONTINUE;
		
	}			
}