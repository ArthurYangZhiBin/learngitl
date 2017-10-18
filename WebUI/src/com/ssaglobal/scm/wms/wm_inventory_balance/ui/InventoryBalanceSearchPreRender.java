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
package com.ssaglobal.scm.wms.wm_inventory_balance.ui;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class InventoryBalanceSearchPreRender extends FormExtensionBase{	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {	
		form.getFormWidgetByName("EXTERNALLOTEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("EXTERNALLOTSTART").setDisplayValue("0");
		form.getFormWidgetByName("LOCEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOCSTART").setDisplayValue("0");
		form.getFormWidgetByName("LOTEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("LOTSTART").setDisplayValue("0");
		form.getFormWidgetByName("LPNEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZ");
		form.getFormWidgetByName("QTYMAX").setDisplayValue("999999999");
		form.getFormWidgetByName("QTYMIN").setDisplayValue("1");
		form.getFormWidgetByName("SKUEND").setDisplayValue("ZZZZZZZZZZ");
		form.getFormWidgetByName("SKUSTART").setDisplayValue("0");
		//if(!WSDefaultsUtil.isOwnerLocked(context.getState())){
			form.getFormWidgetByName("STORERKEYEND").setDisplayValue("ZZZZZZZZZZ");
			form.getFormWidgetByName("STORERKEYSTART").setDisplayValue("0");

			
			form.getFormWidgetByName("LOTTABLE01END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE02END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE03END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE06END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE07END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE08END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE09END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("LOTTABLE10END").setDisplayValue("ZZZZZZZZZZZZZZZ");
			
			
			
//		}
//		else{
//			form.getFormWidgetByName("STORERKEYEND").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
//			form.getFormWidgetByName("STORERKEYSTART").setDisplayValue(WSDefaultsUtil.getPreFilterValueByType("STORER", context.getState()));
//		}
		return RET_CONTINUE;
	}
	
	
}