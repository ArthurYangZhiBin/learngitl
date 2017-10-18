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
package com.ssaglobal.scm.wms.wm_pickdetail.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;

public class PickDetailResetWidgets extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result){
		//
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		
		//Find list of widgets from metadata
		ArrayList widgets = (ArrayList)getParameter("WIDGETS");

		//Set source value to upper case (Rationalization)
		Object source = context.getSourceWidget().getValue();
		source = isNull(source) ? " " : source.toString().toUpperCase() ;
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(context.getSourceWidget().getName(), source);
		}else{
			BioBean bio = (BioBean)focus;
			bio.set(context.getSourceWidget().getName(), source);
		}
		
		//Clear widgets named in metadata
		for(int i=0; i<widgets.size(); i++){
			String widgetName = (String)widgets.get(i);
			clearWidget(focus, widgetName);
		}
		return RET_CONTINUE;
	}
	
	private boolean isNull(Object value){
		 if(value==null){
			 return true;
		 }else if(value.toString().matches("\\s*")){
			 return true;
		 }else{
			 return false;
		 }
	 }
	
	private void clearWidget(DataBean focus, String widgetName){
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(widgetName, "");
		}else{
			BioBean bio = (BioBean)focus;
			bio.set(widgetName, "");
		}
	}
}