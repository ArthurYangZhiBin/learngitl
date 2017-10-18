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
package com.ssaglobal.scm.wms.wm_demand_allocation.ui;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class DemandAllocationSearchPreRender extends FormExtensionBase{	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {				
		if(!WSDefaultsUtil.isOwnerLocked(context.getState())){
			form.getFormWidgetByName("STORERKEYEND").setDisplayValue("ZZZZZZZZZZ");
			form.getFormWidgetByName("STORERKEYEND").setValue("ZZZZZZZZZZ");
			form.getFormWidgetByName("STORERKEYSTART").setDisplayValue("0");
			form.getFormWidgetByName("STORERKEYSTART").setValue("0");
		}
		else{
			form.getFormWidgetByName("STORERKEYEND").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			form.getFormWidgetByName("STORERKEYEND").setValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			form.getFormWidgetByName("STORERKEYSTART").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			form.getFormWidgetByName("STORERKEYSTART").setValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
		}
		return RET_CONTINUE;
	}
	
	
}