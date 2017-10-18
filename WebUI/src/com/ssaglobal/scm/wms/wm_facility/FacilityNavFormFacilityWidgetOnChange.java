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
package com.ssaglobal.scm.wms.wm_facility;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class FacilityNavFormFacilityWidgetOnChange extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityNavFormFacilityWidgetOnChange.class);
	public FacilityNavFormFacilityWidgetOnChange()
    {
    }
	protected int execute(ActionContext context, ActionResult result){							
		RuntimeFormInterface form = context.getSourceWidget().getForm();		

		RuntimeFormWidgetInterface widgetFacility = form.getFormWidgetByName("Facility");	
		String facilityValue = widgetFacility == null?"":widgetFacility.getValue().toString();
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Faclity value:"+facilityValue,100L);		
		if(facilityValue.equalsIgnoreCase("ENTERPRISE")){			
			Query loadBiosQry = new Query("wm_enterprise_navigation", "", null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			result.setFocus((DataBean)uow.getBioCollectionBean(loadBiosQry));				
		}
		else if(facilityValue.equalsIgnoreCase("WAREHOUSE")){			
			Query loadBiosQry = new Query("wm_warehouse_navigation", "", null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			result.setFocus((DataBean)uow.getBioCollectionBean(loadBiosQry));
		}
		else{			
			Query loadBiosQry = new Query("wm_warehouse_navigation", "", null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			result.setFocus((DataBean)uow.getBioCollectionBean(loadBiosQry));
		}
		
		return RET_CONTINUE;
	}		
		
}