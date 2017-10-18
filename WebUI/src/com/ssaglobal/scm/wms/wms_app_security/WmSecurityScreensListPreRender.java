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


package com.ssaglobal.scm.wms.wms_app_security;

import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.extensions.ExtensionBaseclass;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;



public class WmSecurityScreensListPreRender extends FormExtensionBase
{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityScreensListPreRender.class);
    public WmSecurityScreensListPreRender()
    {
    }

    protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws UserException {
    	_log.debug("LOG_DEBUG_EXTENSION_WMSECSCRLISTPREREN","Executing WmSecurityScreensListPreRender",100L);    	
    	StateInterface state = context.getState();
    	String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
    	if(roleId == null){
    		String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
    	}
    	
    	BioCollectionBean profileList = null;
		try {
			profileList = (BioCollectionBean)form.getFocus();
    		if(profileList == null)
    			profileList = uowb.getBioCollectionBean(new Query("wm_meta_user_profile","","wm_meta_user_profile.user_profile_name"));
			BioCollectionBean selectedProfiles = uowb.getBioCollectionBean(new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_role_id = '"+roleId.toUpperCase()+"'",""));
			_log.debug("LOG_DEBUG_EXTENSION_WMSECSCRLISTPREREN","profileList:"+profileList,100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECSCRLISTPREREN","profileList size:"+profileList.size(),100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECSCRLISTPREREN","selectedProfiles:"+selectedProfiles,100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECSCRLISTPREREN","selectedProfiles size:"+selectedProfiles.size(),100L);			
			ArrayList selectedProfilesList = new ArrayList(selectedProfiles.size());
			ArrayList savedSelectedProfiles = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistProfiles.SESS_KEY_PROF_ADD_LIST);
			ArrayList savedRemovedProfiles = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistProfiles.SESS_KEY_PROF_DEL_LIST);
			if(savedSelectedProfiles == null)
				savedSelectedProfiles = new ArrayList();
			if(savedRemovedProfiles == null)
				savedRemovedProfiles = new ArrayList();
			
			for(int i = 0; i < savedSelectedProfiles.size(); i++){
				if(!selectedProfilesList.contains(savedSelectedProfiles.get(i)) && ! savedRemovedProfiles.contains(savedSelectedProfiles.get(i))){
					selectedProfilesList.add(savedSelectedProfiles.get(i));
				}
			}
			
			for(int i = 0; i < selectedProfiles.size(); i++){				
				if(savedRemovedProfiles.size() == 0 && savedSelectedProfiles.size() == 0)
					selectedProfilesList.add(selectedProfiles.elementAt(i).get("user_profile_id"));				
				else if(!selectedProfilesList.contains(selectedProfiles.elementAt(i).get("user_profile_id")) && ! savedRemovedProfiles.contains(selectedProfiles.elementAt(i).get("user_profile_id"))){
					selectedProfilesList.add(selectedProfiles.elementAt(i).get("user_profile_id"));
				}				
			}
			
			//Check the boxes that correspond to the selected profiles.
			for(int i = 0; i < profileList.size(); i++){
				if(selectedProfilesList.contains(profileList.elementAt(i).get("user_profile_id"))){
					profileList.elementAt(i).set("is_selected","1");
				}
				else{
					profileList.elementAt(i).set("is_selected","0");
				}
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		form.setFocus(profileList);
    	return RET_CONTINUE;
    }
        
}
