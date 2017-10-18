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
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.element.Bucket;
import com.epiphany.shr.ui.element.BucketFactory;
import com.epiphany.shr.ui.element.UIBeanBucket;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.GenericEpnyStateImpl;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.taggen.FormGen;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;



public class WmSecurityRoleListFormPrerenderAction extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityRoleListFormPrerenderAction.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","Executing WmSecurityRoleListFormPrerenderAction",100L);		
		StateInterface state = context.getState();       
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();       
		try
		{                      
			String objectId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
			_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","objectId:"+objectId,100L);
			if(objectId != null && objectId.length() > 0){								
        		BioCollectionBean profileList = (BioCollectionBean)form.getFocus();
        		if(profileList == null)
        			profileList = uowb.getBioCollectionBean(new Query("wm_meta_user_profile","","wm_meta_user_profile.user_profile_name"));
        		BioCollectionBean selectedProfiles = uowb.getBioCollectionBean(new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_role_id = '"+objectId.toUpperCase()+"'",""));
        		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","profileList:"+profileList,100L);
        		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","profileList size:"+profileList.size(),100L);
        		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","selectedProfiles:"+selectedProfiles,100L);
        		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","selectedProfiles size:"+selectedProfiles.size(),100L);        		
        		ArrayList selectedProfilesList = new ArrayList(selectedProfiles.size());
        		for(int i = 0; i < selectedProfiles.size(); i++){
        			selectedProfilesList.add(selectedProfiles.elementAt(i).get("user_profile_id"));
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
				form.setFocus(profileList);
			}
					
	}
	catch(BioNotFoundException bioEx)
	{            
		throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
	} catch (EpiDataException e) {        	
		e.printStackTrace();
		return RET_CANCEL;
	}
	_log.debug("LOG_DEBUG_EXTENSION_WMSECROLLSTFRMPREREN","Exiting WmSecurityRoleListFormPrerenderAction",100L);	
	return RET_CONTINUE;
	
}

}