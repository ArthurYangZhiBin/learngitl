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
package com.ssaglobal.scm.wms.wm_job_management.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;

public class JMPreRender extends FormExtensionBase{
	
	public JMPreRender(){
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		
		if (form.getName().equalsIgnoreCase("wm_job_management_detail_detail_view")){
			RuntimeFormWidgetInterface status = form.getFormWidgetByName("STATUS");
			RuntimeFormWidgetInterface uncombineCheck = form.getFormWidgetByName("UNCOMBINE");
			if(status.getValue().toString().equals("51")){
				uncombineCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
			else{
				uncombineCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			}           
		}
		try {
	           // Add your code here to process the event
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;
	}
}