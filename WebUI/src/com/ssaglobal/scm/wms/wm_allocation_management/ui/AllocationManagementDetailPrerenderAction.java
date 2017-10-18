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
package com.ssaglobal.scm.wms.wm_allocation_management.ui;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class AllocationManagementDetailPrerenderAction extends FormExtensionBase{	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AllocationManagementDetailPrerenderAction.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTDETAILPRERENDER","Executing AllocationManagementDetailPrerenderAction",100L);		
		Object apportionObj = form.getFocus().getValue("APPORTION");
		String apportion = apportionObj == null?"":apportionObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTDETAILPRERENDER","apportionObj:"+apportionObj,100L);
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTDETAILPRERENDER","apportion:"+apportion,100L);
		if(apportion.length() > 0 && apportion.equals("1")){
			form.getFormWidgetByName("SEQUENCE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
		}
		else{
			form.getFormWidgetByName("SEQUENCE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
		}		
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCMGTDETAILPRERENDER","Exiting AllocationManagementDetailPrerenderAction",100L);		
		return RET_CONTINUE;
	}	
	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		StateInterface state = context.getState();
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_header_detail_view",state);
		Query bioQry = new Query("wm_orderdetail","wm_orderdetail.PO = '"+detailForm.getFocus().getValue("POKEY")+"' AND wm_orderdetail.STORERKEY = '"+detailForm.getFocus().getValue("STORERKEY")+"' AND wm_orderdetail.SKU = '"+detailForm.getFocus().getValue("SKU")+"'","");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean orderDetails = uow.getBioCollectionBean(bioQry);
		form.setFocus(orderDetails);
		return RET_CONTINUE;
	}
}