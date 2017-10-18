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
package com.ssaglobal.scm.wms.formextensions;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;

public class ClearSpaceOnRender extends FormExtensionBase{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		DataBean focus = (DataBean)form.getFocus();
		if(focus.isBio()){
			BioBean bio = (BioBean)focus;
			ArrayList widgets = (ArrayList)getParameter("widgetNames");
			for(int index=0; index<widgets.size(); index++){
				String widgetName = (String)widgets.get(index);
				String value = (String) bio.get(widgetName);
				//_log.debug("LOG_SYSTEM_OUT","Bio value: /"+bio.get(widgetName)+"/",100L);
				try{
					if(value.matches("\\s*")){
						bio.set(widgetName, "");					
					}
				}catch(NullPointerException e){
					return RET_CONTINUE;
				}
				//_log.debug("LOG_SYSTEM_OUT","NEW BIO VALUE: /"+bio.get(widgetName)+"/",100L);
			}
		}
		return RET_CONTINUE;
	}
}