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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;

public class ShipmentOrderPickDetailNewAutoFill extends FormExtensionBase{
	private final static String ORDER_KEY = "ORDERKEY";
	private final static String STATUS = "STATUS";
	private final static String DROPID = "DROPID";
	private final static String WM_TBGRP_SHELL_SHIPMENTORDER = "wm_tbgrp_shell_shipmentorder";
	private final static String WM_LIST_SHELL_PICKDETAIL = "wm_list_shell_pickdetail";
	private final static String WM_SHIPMENTORDER_PICKDETAIL_TOGGLE_VIEW = "wm_shipmentorder_pickdetail_toggle_view";
	private final static String WM_PICKDETAIL_DETAIL_VIEW = "wm_pickdetail_detail_view"; 
	private final static String WM_PICKDETAIL_LOADNOTES_VIEW ="wm_pickdetail_loadnotes_view";
	
	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		StateInterface state = context.getState();
		RuntimeFormInterface parent = form.getParentForm(state).getParentForm(state);
		if(parent.getName().equals(WM_SHIPMENTORDER_PICKDETAIL_TOGGLE_VIEW) && state.getFocus().isTempBio()){
			
			//jp.answerlink.216293.begin
			if (form.getName().equalsIgnoreCase(WM_PICKDETAIL_DETAIL_VIEW)){
				while(!parent.getName().equals(WM_TBGRP_SHELL_SHIPMENTORDER)){
					parent = parent.getParentForm(state);
				}
				RuntimeFormInterface soDetail = state.getRuntimeForm(parent.getSubSlot("tbgrp_slot"), "tab 0");
				String orderKey = soDetail.getFormWidgetByName(ORDER_KEY).getDisplayValue();
				QBEBioBean qbe = (QBEBioBean)form.getFocus();
				qbe.set(ORDER_KEY, orderKey);
				form.getFormWidgetByName(ORDER_KEY).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName(STATUS).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}else if (form.getName().equalsIgnoreCase(WM_PICKDETAIL_LOADNOTES_VIEW)){
				form.getFormWidgetByName(DROPID).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
			//jp.answerlink.216293.end
		}
		
		if(parent.getName().equals(WM_LIST_SHELL_PICKDETAIL) && state.getFocus().isTempBio()){
			if (form.getName().equalsIgnoreCase(WM_PICKDETAIL_DETAIL_VIEW)){
				form.getFormWidgetByName(STATUS).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}else if (form.getName().equalsIgnoreCase(WM_PICKDETAIL_LOADNOTES_VIEW)){
				form.getFormWidgetByName(DROPID).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
		}
		
		return RET_CONTINUE;
	}
}