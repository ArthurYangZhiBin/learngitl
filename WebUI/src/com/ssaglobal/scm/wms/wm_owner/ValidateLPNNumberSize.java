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
package com.ssaglobal.scm.wms.wm_owner;

import java.util.HashMap;

import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import java.lang.Integer;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;

public class ValidateLPNNumberSize extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {
	
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)throws EpiException 
	{
		final String ATTRIBUTE_LABEL = getParameterString("ATTRIBUTE_LABEL");
		Integer LPNLength_value = new Integer(form.getFormWidgetByName("LPNLENGTH").getDisplayValue());
		int WidgetValueLength = params.get("fieldValue").toString().length();
		RuntimeFormWidgetInterface AppId_widget = form.getFormWidgetByName("APPLICATIONID");
		   RuntimeFormWidgetInterface lpnlength_widget = form.getFormWidgetByName("LPNLENGTH");
		   if (LPNLength_value.intValue() != WidgetValueLength ){
			   setErrorMessage(formWidget, "Invalid Value, Length must be "+ LPNLength_value.intValue() + "digits long");
				return RET_CANCEL;
				}
		   else{
			   setErrorMessage(formWidget, "");
		   }
			   
	       return RET_CONTINUE;
		}
	}