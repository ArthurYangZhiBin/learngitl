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
package com.ssaglobal.scm.wms.shared_app;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuElementInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemFactory;
import com.epiphany.shr.ui.view.customization.MenuExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.ComputedDomFacility;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class FacilityNestMenuPreRender extends MenuExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityNestMenuPreRender.class);
	
	public FacilityNestMenuPreRender(){
	}
	
	protected int execute(StateInterface state, RuntimeMenuInterface menu) throws EpiException {		
				
		//Query loadBiosQryA = new Query("wm_facilitynest_child", "wm_facilitynest_child.LEVELNUM = '0'", "");									
		Query loadBiosQryA = new Query("wm_facilitynest", "wm_facilitynest.LEVELNUM = '0'", "");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollectionBean facilities = uow.getBioCollectionBean(loadBiosQryA);
		_log.debug("LOG_SYSTEM_OUT","BioBean Has children:"+facilities.elementAt(0).getBioCollection("FACILITYCHILDREN").size(),100L);
		
		menu.setFocus(facilities);						
		
		return RET_CONTINUE;		
	}
	
	
	
}