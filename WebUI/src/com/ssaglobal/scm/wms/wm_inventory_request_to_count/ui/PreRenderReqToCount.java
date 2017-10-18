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
package com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_billofmaterial.ui.BillOfMaterialPreDelete;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class PreRenderReqToCount extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderReqToCount.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException
    {
	  	 _log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Executing PreRenderReqToCount",100L);
	  	
	  	StateInterface state = context.getState();
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

	  	DataBean focus = detailForm.getFocus();
	  	
	  	if(!(focus.isTempBio())){	  		
	  		if (detailForm.getFormWidgetByName("PROCESSFLAG").getValue().equals("0"))
	  		{
	  		detailForm.getFormWidgetByName("REQUESTKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
	  		detailForm.getFormWidgetByName("STORERKEYSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("STORERKEYEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("SKUSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("SKUEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOCSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOCEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOCLEVELSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOCLEVELEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("ZONESTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("ZONEEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("AREASTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("AREAEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("OWNER_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("OWNER_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("ITEM_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("ITEM_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOC_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
	  		detailForm.getFormWidgetByName("LOC_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
  			
	  		}
	  		else
	  		{
	  			detailForm.getFormWidgetByName("REQUESTKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("STORERKEYSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("STORERKEYEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("SKUSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("SKUEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOCSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOCEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOCLEVELSTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOCLEVELEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("ZONESTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("ZONEEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("AREASTART").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("AREAEND").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("OWNER_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("OWNER_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("ITEM_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("ITEM_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOC_START_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		  		detailForm.getFormWidgetByName("LOC_END_LOOKUP").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
	  			
	  		}
	  		

	  	}
	  	else{
	  		if(WSDefaultsUtil.isOwnerLocked(context.getState())){	  		
	  			form.getFormWidgetByName("STORERKEYEND").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
				form.getFormWidgetByName("STORERKEYSTART").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
				form.getFocus().setValue("STORERKEYEND", WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
				form.getFocus().setValue("STORERKEYSTART", WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			}
	  		else{
	  			form.getFormWidgetByName("STORERKEYEND").setDisplayValue("ZZZZZZZZZZZZZZZ");
				form.getFormWidgetByName("STORERKEYSTART").setDisplayValue("0");
				form.getFocus().setValue("STORERKEYEND", "ZZZZZZZZZZZZZZZ");
				form.getFocus().setValue("STORERKEYSTART", "0");
	  			
	  		}
	  	}
	  	
	 	 _log.debug("LOG_DEBUG_EXTENSION_REQ_TO_COUNT","Exiting PreRenderReqToCount",100L);
		return RET_CONTINUE;
    }
}
