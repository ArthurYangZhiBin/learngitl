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
package com.ssaglobal.scm.wms.wm_mbol.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;

public class MBOLDetailValidation extends ActionExtensionBase{
	
	public MBOLDetailValidation(){
	}
	
	protected int execute(ActionContext context, ActionResult result){
		//common variables
		DataBean focus = null;
		QBEBioBean qbe = null;
		BioBean bio = null;
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		focus = form.getFocus();
		String widget1 = "CONTAINERKEY";
		String widget2 = "ORDERKEY";
		String widget3 = "PALLETKEY";
		Object value = null;
		
		//changes values for an unsaved bio
		if(focus instanceof QBEBioBean){
			qbe = (QBEBioBean) focus;
			try{	
				//changes CONTAINERKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget1))){
					qbe.set(widget1, value);
				}
				//changes ORDERKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget2))){					
					qbe.set(widget2, value);
				}
				//changes PALLETKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget3))){				
					qbe.set(widget3, value);
				}
			}
			//Exception handling
			catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		//changes values for a saved bio
		else{
			bio = (BioBean) focus;
			try{	
				//changes CONTAINERKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget1))){
					bio.set(widget1, value);
				}
				//changes ORDERKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget2))){				
					bio.set(widget2, value);
				}
				//changes PALLETKEY to default value when it wasn't selected
				if(!(widgetName.equals(widget3))){			
					bio.set(widget3, value);
				}
			}
			//Exception handling
			catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		return RET_CONTINUE;
	}
}