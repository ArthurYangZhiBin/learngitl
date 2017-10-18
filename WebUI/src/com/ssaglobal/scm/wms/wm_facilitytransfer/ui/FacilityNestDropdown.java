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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.uiextensions.ComputedDomFacility;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ASNGeneratePutaway;

public class FacilityNestDropdown extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityNestDropdown.class);
	public FacilityNestDropdown()
	{
	}
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Executing FacilityNestDropdown",100L);
		StateInterface state = context.getState();
    	EpnyControllerState EpnyContState = (EpnyControllerState) state;
        HttpSession session = EpnyContState.getRequest().getSession();
        String facility = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String qrystring = "wm_facilitynest.NAME != '"+facility+"' AND wm_facilitynest.LEVELNUM = '-1'";
		Query loadBiosQryA = new Query("wm_facilitynest", qrystring, "");									
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollectionBean facilityNest = uow.getBioCollectionBean(loadBiosQryA);		
		if(facilityNest != null){
			for(int i = 0; i < facilityNest.size(); i++){
				Bio facilityBio = facilityNest.elementAt(i);
				value.add(facilityBio.get("NESTID").toString());
//HC				labels.add(facilityBio.get("NAME").toString());
				labels.add(facilityBio.get("DESCRIPTION").toString());
				_log.debug("LOG_SYSTEM_OUT","VALUE = "+ facilityBio.get("NESTID"),100L);
//HC				_log.debug("LOG_SYSTEM_OUT","LABELS = "+ facilityBio.get("NAME"),100L);
				_log.debug("LOG_SYSTEM_OUT","LABELS = "+ facilityBio.get("DESCRIPTION"),100L);		
			}
		}	
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Leaving FacilityNestDropdown",100L);		
		return RET_CONTINUE;
	}
}
