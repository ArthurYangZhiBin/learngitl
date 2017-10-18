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
package com.ssaglobal.scm.wms.wm_multi_faclity_balance;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class MultiFacilityBalancesSearchRender extends FormExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesSearchRender.class);
	
	protected int preRenderForm(UIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALLISTREN","Executing MultiFacilityBalancesSearchRender",100L);		
		StateInterface state = context.getState();		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean warehouses = uow.getBioCollectionBean(new Query("wm_facilitynest_warehouse_mfb","wm_facilitynest_warehouse_mfb.LEVELNUM = -1",""));		
		if(warehouses != null && warehouses.size() > 0){
			form.getFormWidgetByName("ENTERPRISE").setProperty(RuntimeWidget.PROP_READONLY, Boolean.FALSE);
			return RET_CONTINUE;
		}
		
		form.getFormWidgetByName("ENTERPRISE").setProperty(RuntimeWidget.PROP_READONLY, Boolean.TRUE);
		return RET_CONTINUE;
	}
	
}