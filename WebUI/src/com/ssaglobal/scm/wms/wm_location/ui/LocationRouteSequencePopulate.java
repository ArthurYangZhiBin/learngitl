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
package com.ssaglobal.scm.wms.wm_location.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.exceptions.UserException;

public class LocationRouteSequencePopulate extends ActionExtensionBase{
	private final static String LOGICAL_LOCATION = "LOGICALLOCATION";
	private final static String LOCATION = "LOC";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		//Pattern pattern = Pattern.compile("[a-zA-Z_0-9\\s]*\\w+");
		Pattern pattern = Pattern.compile("[^'\",\\\\<>&; ]*"); //disallow ' " , \ < > & ;
		QBEBioBean focus = (QBEBioBean)context.getState().getFocus();
		String temp = focus.get(LOCATION).toString();
		temp = temp.toUpperCase();
		Matcher matcher = pattern.matcher(temp);
		if(matcher.matches()){
			focus.set(LOCATION, temp);
			focus.set(LOGICAL_LOCATION, temp);
			result.setFocus(focus);
		}else{
			focus.set(LOCATION, "");
			result.setFocus(focus);
			String widgetLabels = context.getState().getCurrentRuntimeForm().getFormWidgetByName(LOCATION).getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");
			String args[] = {widgetLabels}; 
			String errorMsg = getTextMessage("WMEXP_SPECIAL_CHARS_IN_KEY",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;
	}
}