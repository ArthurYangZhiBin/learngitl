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
package com.ssaglobal.scm.wms.uiextensions;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;

public class SingleInputOverThreeFields extends ActionExtensionBase{
	public SingleInputOverThreeFields(){
	}
	
	protected int execute(ActionContext context, ActionResult result){
		//Obtain parameters placed in Studio
		String field1 = getParameterString("FirstField");
		String field2 = getParameterString("SecondField");
		String temp = getParameterString("DefaultedValue");
		Object value = null;
		//Assign retreived parameter to value if not equal to null
		if(!(temp.equalsIgnoreCase("null"))){
			value = temp;
		}
		//Get focus and set bio handlers
		DataBean focus = context.getState().getFocus();
		QBEBioBean qbe = null;
		BioBean bio = null;
		
		//Change values in an unsaved bio
		if(focus.isTempBio()){
			qbe = (QBEBioBean)focus;
			qbe.set(field1, value);
			qbe.set(field2, value);
			
		}//Change values in a saved bio
		else{
			bio = (BioBean)focus;
			bio.set(field1, value);
			bio.set(field2, value);
		}	
		return RET_CONTINUE;
	}
}