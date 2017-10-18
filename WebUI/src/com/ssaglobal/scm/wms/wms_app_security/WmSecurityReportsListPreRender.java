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



public class WmSecurityReportsListPreRender extends FormExtensionBase
{	
    public WmSecurityReportsListPreRender()
    {
    }
    protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityReportsListPreRender.class);
    protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws UserException {
    	_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing WmSecurityScreensListPreRender",100L);    	
    	StateInterface state = context.getState();
    	String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
    	if(roleId == null){
    		String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
    	}    	
    	BioCollectionBean reportList = null;
		try {
			reportList = (BioCollectionBean)form.getFocus();
    		if(reportList == null)
    			reportList = uowb.getBioCollectionBean(new Query("wm_pbsrpt_reports_security","","wm_pbsrpt_reports_security.RPT_TITLE"));
			BioCollectionBean selectedReports = uowb.getBioCollectionBean(new Query("wm_reportsrolemapping","wm_reportsrolemapping.ROLEID = '"+roleId.toUpperCase()+"'",""));
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","reportList:"+reportList,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","reportList size:"+reportList.size(),100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","selectedReports:"+selectedReports,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","selectedReports size:"+selectedReports.size(),100L);
			ArrayList selectedReportsList = new ArrayList(selectedReports.size());
			ArrayList savedSelectedReports = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistReports.SESS_KEY_REP_ADD_LIST);
			ArrayList savedRemovedReports = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistReports.SESS_KEY_REP_DEL_LIST);
			if(savedSelectedReports == null)
				savedSelectedReports = new ArrayList();
			if(savedRemovedReports == null)
				savedRemovedReports = new ArrayList();
			
			for(int i = 0; i < savedSelectedReports.size(); i++){
				if(!selectedReportsList.contains(savedSelectedReports.get(i)) && ! savedRemovedReports.contains(savedSelectedReports.get(i))){
					selectedReportsList.add(savedSelectedReports.get(i));
				}
			}
			
			for(int i = 0; i < selectedReports.size(); i++){
				if(savedRemovedReports.size() == 0 && savedSelectedReports.size() == 0)
					selectedReportsList.add(selectedReports.elementAt(i).get("REPORTID"));
				else if(!selectedReportsList.contains(selectedReports.elementAt(i).get("REPORTID")) && ! savedRemovedReports.contains(selectedReports.elementAt(i).get("REPORTID"))){
					selectedReportsList.add(selectedReports.elementAt(i).get("REPORTID"));
				}				
			}
			
			//Check the boxes that correspond to the selected profiles.
			for(int i = 0; i < reportList.size(); i++){
				if(selectedReportsList.contains(reportList.elementAt(i).get("RPT_ID"))){
					reportList.elementAt(i).set("ISSELECTED","1");
				}
				else{
					reportList.elementAt(i).set("ISSELECTED","0");
				}
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		form.setFocus(reportList);
    	return RET_CONTINUE;
    }
        
}
