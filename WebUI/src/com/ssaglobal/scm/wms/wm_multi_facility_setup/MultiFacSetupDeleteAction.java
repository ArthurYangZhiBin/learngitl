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

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.util.FormUtil;



public class MultiFacSetupDeleteAction extends ActionExtensionBase
{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacSetupDeleteAction.class);    

    protected int execute(ActionContext context, ActionResult result)
        throws UserException
    {
    	_log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","Executing WmSecurityListSelectAction",100L);    	
        StateInterface state = context.getState();
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
		if(facilityRecord.get("LEVELNUM").toString().equals("0")){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_MULTI_FAC_CANNOT_DEL_CO",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		Query bioQry = new Query("wm_facilitynest","wm_facilitynest.PARENTNESTID = "+facilityRecord.get("NESTID"),"");
		BioCollection bc = state.getDefaultUnitOfWork().getBioCollectionBean(bioQry);
		try {
			if(bc != null && bc.size() > 0){				
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_MULTI_FAC_HAS_CHILD",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","exception...",100L,e1);
			throw new UserException(errorMsg,new Object[0]);
		} 
//		try {								
//			facilityRecord.delete();
//			state.getDefaultUnitOfWork().saveUOW();
//		} catch (EpiException e) {			
//			e.printStackTrace();
//			String args[] = new String[0]; 
//			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
//			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","exception...",100L,e);
//			throw new UserException(errorMsg,new Object[0]);
//		}
		String sql = "DELETE FROM FACILITYNEST WHERE NESTID = "+facilityRecord.get("NESTID");
		try {
			WmsWebuiValidationDeleteImpl.delete(sql);
		} catch (DPException e) {
			e.printStackTrace();			
			_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","exception...",100L,e);
			throw new UserException(e.getLocalizedMessage(),new Object[0]);
		}
        _log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","Exiting WmSecurityListSelectAction",100L);
        return RET_CONTINUE;
    }
}
