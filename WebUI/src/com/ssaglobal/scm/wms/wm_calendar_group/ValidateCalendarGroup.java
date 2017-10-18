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
package com.ssaglobal.scm.wms.wm_calendar_group;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateCalendarGroup extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateCalendarGroup.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Executing ValidateCalendarGroup",100L);			
		StateInterface state = context.getState();					
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_calendar_group_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("wm_calendar_group_details");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_calendar_group_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Found Detail Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Found Detail Form:Null",100L);			
		String calendarGroup = headerForm.getFormWidgetByName("CALENDARGROUP").getDisplayValue();
		if(headerForm.getFocus().isTempBio()){
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Checking for duplicate key:"+calendarGroup,100L);			
			Query loadBiosQry = new Query("wm_calendar", "wm_calendar.CALENDARGROUP = '"+calendarGroup.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","key in use...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Exiting ValidateCalendarGroup",100L);					
					String args[] = {calendarGroup}; 
					String errorMsg = getTextMessage("WMEXP_CALENDARGROUP_DUP_CODE",args,state.getLocale());
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
			Object splitDateObj = detailForm.getFormWidgetByName("SPLITDATE");
			Object periodEndDateObj = detailForm.getFormWidgetByName("PERIODEND");
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","splitDateObj:"+splitDateObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","periodEndDateObj:"+periodEndDateObj,100L);			
			//Validate Percent
			if(splitDateObj != null && periodEndDateObj != null){	
				GregorianCalendar splitDate = (GregorianCalendar)((RuntimeWidget)splitDateObj).getValue();
				GregorianCalendar periodEndDate = (GregorianCalendar)((RuntimeWidget)periodEndDateObj).getValue();
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","splitDate:"+splitDate,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","periodEndDate:"+periodEndDate,100L);				
				if(splitDate != null && periodEndDate != null){
					if(periodEndDate.before(splitDate)){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Exiting ValidateCalendarGroup",100L);						
						String[] args = new String[0];
						String errorMsg = getTextMessage("WMEXP_PERIODEND_BEFORE_SPLIT",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				Query loadBiosQry = new Query("wm_calendardetail", "wm_calendardetail.CALENDARGROUP = '"+calendarGroup.toUpperCase()+"'", null);				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Calendar Detail Records Found...",100L);						
						for(int i = 0; i < bioCollection.size(); i++){
							Bio bio = bioCollection.elementAt(i);
							GregorianCalendar bioPeriodEnd = (GregorianCalendar)bio.get("PERIODEND");
							if(bioPeriodEnd.after(splitDate)){								
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Exiting ValidateCalendarGroup",100L);						
								String[] args = new String[0];
								String errorMsg = getTextMessage("WMEXP_SPLITDATE_BEFORE_PERIODEND",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);								
							}
						}
					}
				} catch (EpiDataException e) {			
					e.printStackTrace();
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Exiting ValidateCalendarGroup",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}								
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATECALENDARGRP","Exiting ValidateCalendarGroup",100L);
		return RET_CONTINUE;
		
	}	
}