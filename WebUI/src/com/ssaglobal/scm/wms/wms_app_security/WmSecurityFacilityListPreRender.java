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



public class WmSecurityFacilityListPreRender extends FormExtensionBase
{	
    public WmSecurityFacilityListPreRender()
    {
    }
    protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityFacilityListPreRender.class);
    protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws UserException {
    	_log.debug("LOG_DEBUG_EXTENSION_WMSECFACLISTPREREN","Executing WmSecurityFacilityListPreRender",100L);    	
    	StateInterface state = context.getState();
    	String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
    	if(roleId == null){
    		String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
    	}
    	
    	BioCollectionBean facilityList = null;
		try {
//			facilityList = (BioCollectionBean)form.getFocus();
			facilityList = ((BioCollectionBean)form.getFocus()).filterBean(new Query("wm_pl_db_security","wm_pl_db_security.isActive = '1'","wm_pl_db_security.db_name"));		//8050
    		if(facilityList == null)
    			facilityList = uowb.getBioCollectionBean(new Query("wm_pl_db_security","wm_pl_db_security.isActive = '1'","wm_pl_db_security.db_name"));						//8050
			BioCollectionBean selectedFacilities = uowb.getBioCollectionBean(new Query("wm_facilityrolemapping","wm_facilityrolemapping.ROLEID = '"+roleId.toUpperCase()+"'",""));			
			_log.debug("LOG_DEBUG_EXTENSION_WMSECFACLISTPREREN","facilityList:"+facilityList,100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECFACLISTPREREN","facilityList size:"+facilityList.size(),100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECFACLISTPREREN","selectedFacilities:"+selectedFacilities,100L);
			_log.debug("LOG_DEBUG_EXTENSION_WMSECFACLISTPREREN","selectedFacilities size:"+selectedFacilities.size(),100L);
			ArrayList selectedFacilitiesList = new ArrayList(selectedFacilities.size());
			ArrayList savedSelectedFacilities = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistFacilities.SESS_KEY_FAC_ADD_LIST);
			ArrayList savedRemovedFacilities = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistFacilities.SESS_KEY_FAC_DEL_LIST);
			if(savedSelectedFacilities == null)
				savedSelectedFacilities = new ArrayList();
			if(savedRemovedFacilities == null)
				savedRemovedFacilities = new ArrayList();
			
			for(int i = 0; i < savedSelectedFacilities.size(); i++){
				if(!selectedFacilitiesList.contains(savedSelectedFacilities.get(i)) && ! savedRemovedFacilities.contains(savedSelectedFacilities.get(i))){
					selectedFacilitiesList.add(savedSelectedFacilities.get(i));
				}
			}
			
			for(int i = 0; i < selectedFacilities.size(); i++){
				if(savedRemovedFacilities.size() == 0 && savedSelectedFacilities.size() == 0)
					selectedFacilitiesList.add(selectedFacilities.elementAt(i).get("FACILITYID"));
				else if(!selectedFacilitiesList.contains(selectedFacilities.elementAt(i).get("FACILITYID")) && ! savedRemovedFacilities.contains(selectedFacilities.elementAt(i).get("FACILITYID"))){
					selectedFacilitiesList.add(selectedFacilities.elementAt(i).get("FACILITYID"));
				}
			}
			
			//Check the boxes that correspond to the selected profiles.
			for(int i = 0; i < facilityList.size(); i++){
				if(selectedFacilitiesList.contains(facilityList.elementAt(i).get("db_key"))){
					facilityList.elementAt(i).set("ISSELECTED","1");
				}
				else{
					facilityList.elementAt(i).set("ISSELECTED","0");
				}
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		form.setFocus(facilityList);
    	return RET_CONTINUE;
    }
        
}
