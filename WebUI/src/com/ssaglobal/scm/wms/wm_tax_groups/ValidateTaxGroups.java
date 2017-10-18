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
package com.ssaglobal.scm.wms.wm_tax_groups;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateTaxGroups extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateTaxGroups.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Executing ValidateTaxGroups",100L);			
		StateInterface state = context.getState();					
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_tax_group_header_detail_view",state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Found Header Form:Null",100L);			
		ArrayList tabList = new ArrayList();
		tabList.add("wm_tax_group_detail");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_tax_group_detail_detail_view",tabList,state);
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Found Detail Form:Null",100L);			
		if(headerForm.getFocus().isTempBio()){			
			String taxGroupKey = headerForm.getFormWidgetByName("TAXGROUPKEY").getDisplayValue();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Checking for duplicate key:"+taxGroupKey,100L);			
			Query loadBiosQry = new Query("wm_taxgroup", "wm_taxgroup.TAXGROUPKEY = '"+taxGroupKey.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","key in use...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Exiting ValidateTaxGroups",100L);					
					String args[] = {taxGroupKey}; 
					String errorMsg = getTextMessage("WMEXP_TAXGROUP_DUP_CODE_A",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		if(detailForm != null){
			if(detailForm.getFocus().isTempBio()){
				String taxGroupKey = headerForm.getFormWidgetByName("TAXGROUPKEY").getDisplayValue();
				String taxRateKey = detailForm.getFormWidgetByName("TAXRATEKEY").getValue().toString();				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Checking for duplicate key:"+taxRateKey+","+taxGroupKey,100L);
				Query loadBiosQry = new Query("wm_taxgroupdetail", "wm_taxgroupdetail.TAXGROUPKEY = '"+taxGroupKey.toUpperCase()+"' and wm_taxgroupdetail.TAXRATEKEY = '"+taxRateKey.toUpperCase()+"'", null);				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
				try {
					if(bioCollection.size() > 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","key in use...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Exiting ValidateTaxGroups",100L);
						String args[] = {taxGroupKey,taxRateKey}; 
						String errorMsg = getTextMessage("WMEXP_TAXGROUP_DUP_CODE_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {			
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETAXGRP","Exiting ValidateTaxGroups",100L);
		return RET_CONTINUE;
		
	}	
}