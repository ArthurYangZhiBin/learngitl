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
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class MultiFacilityStoreFacilityInSess extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityStoreFacilityInSess.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{				
		
		HttpSession session = context.getState().getRequest().getSession();
		StateInterface state = context.getState();
        UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
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
                
		BioRef selectedFacilityRef = widgetFacility.getSelectedItem();
		if (selectedFacilityRef == null) {
			throw new UserException("WMEXP_MF_NOT_SELECTED", new Object[]{});
		}
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
		
		try {
			session.setAttribute("SESSION_KEY_MULTI_FAC_SEL_FAC",facilityRecord.getString("NAME"));
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;
		
	}			
}