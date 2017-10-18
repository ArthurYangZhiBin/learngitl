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

import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

public class WOMRouteOpsPreRenderWidget extends FormWidgetExtensionBase{
	private final static String STATUS = "STATUS";
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		DataBean focus = state.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			String status = qbe.get(STATUS).toString();
			if(status.equals("12") || status.equals("98")){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
		}else{
			BioBean bio = (BioBean)focus;
			String status = bio.get(STATUS).toString();
			if(status.equals("12") || status.equals("98")){
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}
}