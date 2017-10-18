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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



public class MultiFacSetupAddChildAction extends SaveAction
{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacSetupAddChildAction.class);    

    protected int execute(ActionContext context, ActionResult result)
        throws UserException
    {
    	_log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","Executing MultiFacSetupAddChildAction",100L);    	
        StateInterface state = context.getState();
        UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
        RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_multi_facility_setup_cascading_form",state);
        RuntimeFormInterface detailform = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_multi_facility_setup_detail_form",state);
        if(form == null){   
        	_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","form is null...",100L);
        	String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
        }
        if(detailform == null || detailform.getFocus() == null){   
        	_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","detailform is null...",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_MULTI_FAC_SETUP_MUST_CREATE_NEW_RECORD",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
        }        
        if(!detailform.getFocus().isTempBio()){
        	try {
				uow.saveUOW(true);
				uow.clearState();
				result.setFocus(detailform.getFocus());
				return RET_CONTINUE;
			} catch (EpiException e) {				
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
        }
        QBEBioBean newRecord = (QBEBioBean)detailform.getFocus();
        
//8050.b       
    	if (newRecord.getValue("LEVELNUM").toString().equalsIgnoreCase("-1")){ 		//if the level is Warehosue then only validate the facility against pl_db
            if (! validateFacility(newRecord.getValue("NAME").toString(),uow)){
            	String args[] = new String[1]; 
            	args[0]= newRecord.getValue("NAME").toString();
            	_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","Invalid Facility Name...",100L);
    			String errorMsg = getTextMessage("WMEXP_NOTACTIVE_FACILITY",args,state.getLocale());
    			throw new UserException(errorMsg,new Object[0]);
            }
    	}
//8050.e
        
        RuntimeMenuFormWidgetInterface widgetFacility = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("TreeMenu");
        if(widgetFacility == null){    
        	_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","widget is null...",100L);
        	String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
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
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 
		if(facilityRecord == null){    
        	_log.error("LOG_ERROR_EXTENSION_MULTIFACSETUPPROP","facilityRecord is null...",100L);        	
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
        }
						
		newRecord.set("PARENTNESTID",facilityRecord.get("NESTID"));
		Query qry = new Query("wm_facilitynest","","wm_facilitynest.NESTID");
		Integer nestId = new Integer(0);
		synchronized (this) {
			BioCollectionBean bc = uow.getBioCollectionBean(qry);
			try {
				if(bc != null && bc.size() > 0){
					Bio lastRecord = bc.elementAt(bc.size() - 1);
					Integer largestNestId = (Integer)lastRecord.get("NESTID");
					nestId = new Integer(largestNestId.intValue() + 1);
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			newRecord.set("NESTID",nestId);
			try {				
				uow.saveUOW(true);
				uow.clearState();
				result.setFocus(newRecord);
			}catch(UnitOfWorkException e){
				Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
				if(nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();										
					throw new UserException(reasonCode,new Object[0]);
				}
				else
				{
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				
			}
			catch (EpiException e) {				
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
        _log.debug("LOG_DEBUG_EXTENSION_MULTIFACSETUPPROP","Exiting MultiFacSetupAddChildAction",100L);
        return RET_CONTINUE;
    }
//8050.b
    public boolean validateFacility(String facilityName, UnitOfWorkBean uow ){
    	Query qry = new Query("wm_pl_db","wm_pl_db.isActive = '1' AND wm_pl_db.db_name = '"+facilityName+"'", null);
    	BioCollectionBean bc = uow.getBioCollectionBean(qry);
		try {
			if (bc.size()==1){
				return true;
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
//8050.e
}
