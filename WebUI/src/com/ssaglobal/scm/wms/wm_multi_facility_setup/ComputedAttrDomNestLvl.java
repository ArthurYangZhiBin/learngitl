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

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.jobscheduler.JobSchedulerHelper;
import com.epiphany.shr.jobscheduler.beans.ScheduleGroup;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.quartz.JobDetail;
import org.quartz.Scheduler;

public class ComputedAttrDomNestLvl extends AttributeDomainExtensionBase
{
	
	public ComputedAttrDomNestLvl()
	{
	}
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ComputedAttrDomNestLvl.class);
	protected int execute(DropdownContentsContext context, List value, List labels)
	throws EpiException
	{		
		_log.debug("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","Executing ComputedAttrDomNestLvl",100L);		
		StateInterface state = context.getState();
		try
		{
			
			RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_multi_facility_setup_cascading_form",state);
			RuntimeFormInterface detailform = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_multi_facility_setup_detail_form",state);
			
			if(form == null){   
	        	_log.error("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","form is null...",100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
	        }
			RuntimeMenuFormWidgetInterface widgetFacility = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("TreeMenu");
	        if(widgetFacility == null){    
	        	_log.error("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","widget is null...",100L);
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
				_log.error("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","exception...",100L,e1);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);		
			} 
			if(facilityRecord == null){    
	        	_log.error("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","facilityRecord is null...",100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
	        }
			String selectedNestLevel = facilityRecord.get("LEVELNUM").toString();
			Query loadBiosQry = null;			
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();		
			if(detailform.getFocus().isTempBio())
				loadBiosQry = new Query("codelkup","codelkup.LISTNAME = 'NESTLEVEL' AND (codelkup.CODE > '"+selectedNestLevel+"' OR codelkup.CODE = '-1')","");
			else
				loadBiosQry = new Query("codelkup","codelkup.LISTNAME = 'NESTLEVEL' AND (codelkup.CODE >= '"+selectedNestLevel+"' OR codelkup.CODE = '-1')","");
			_log.debug("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","Query:"+loadBiosQry.getQueryExpression(),100L);			
			_log.debug("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","Size:"+loadBiosQry.getQueryExpression(),100L);
			BioCollectionBean bioCollection = uow.getBioCollectionBean(loadBiosQry);					
			_log.debug("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","Size:"+bioCollection.size(),100L);
			for(int i = 0; i < bioCollection.size(); i++){
				labels.add(bioCollection.elementAt(i).get("DESCRIPTION").toString());
				value.add(bioCollection.elementAt(i).get("CODE").toString());				
			}
		}
		catch(Exception e)
		{
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_COMPUTEDATRDOMNSTLVL","Leaving ComputedAttrDomNestLvl",100L);		
		return 0;
	}
}
