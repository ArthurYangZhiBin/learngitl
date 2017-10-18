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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;

public class WOMWorkOrderPreRenderWidget extends FormWidgetExtensionBase{
	private final static String STATUS = "STATUS";
	private final static String CLOSED = "45";
	private final static String CREATED = "50";
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		boolean isQty = getParameterBoolean("isQty");
		boolean disableForClosed = getParameterBoolean("disableForClosed");
		String status = null;
		DataBean focus = state.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			status = qbe.get(STATUS).toString();
		}else{
			BioBean bio = (BioBean)focus;
			status = bio.get(STATUS).toString();
		}
		
		if(isQty){
			if(status.equals(CREATED)){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			}
		}
		
		if(disableForClosed){
			if(status.equals(CLOSED)){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}
}