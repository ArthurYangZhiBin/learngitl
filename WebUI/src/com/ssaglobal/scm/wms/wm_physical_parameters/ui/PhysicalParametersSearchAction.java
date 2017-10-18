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
package com.ssaglobal.scm.wms.wm_physical_parameters.ui;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class PhysicalParametersSearchAction extends ActionExtensionBase{	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PhysicalParametersSearchAction.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		DataBean focus = form.getFocus();
		if(WSDefaultsUtil.isOwnerLocked(context.getState())){
			focus.setValue("STORERKEYMAX",WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			focus.setValue("STORERKEYMIN",WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			form.getFormWidgetByName("STORERKEYMAX").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
			form.getFormWidgetByName("STORERKEYMIN").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
		}
		else{
			focus.setValue("STORERKEYMAX","ZZZZZZZZZZ");
			focus.setValue("STORERKEYMIN","0");
		}
		form.setFocus(focus);
		_log.debug("LOG_SYSTEM_OUT","\n\nfocus:"+form.getFocus().getValue("STORERKEYMAX")+"\n\n",100L);
		_log.debug("LOG_SYSTEM_OUT","\n\nwidget:"+form.getFormWidgetByName("STORERKEYMAX").getDisplayValue()+"\n\n",100L);
		
		return RET_CONTINUE;
	}
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {		
		BioCollectionBean focus = (BioCollectionBean)result.getFocus();
			if(WSDefaultsUtil.isOwnerLocked(context.getState())){
				focus.elementAt(0).set("STORERKEYMAX",WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
				focus.elementAt(0).set("STORERKEYMIN",WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));			
			}
			/*else{
				focus.elementAt(0).set("STORERKEYMAX","ZZZZZZZZZZ");
				focus.elementAt(0).set("STORERKEYMIN","0");
			}*/
		return RET_CONTINUE;
	}
	
	
}