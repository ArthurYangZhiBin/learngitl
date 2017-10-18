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
package com.ssaglobal.scm.wms.common.ui;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;

public class SetWidgetToDefault extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result){
		//Overwrites value of widgets on shared form to delineated default value
		//FROM STUDIO
		ArrayList resetWidgets = (ArrayList)getParameter("RESET_WIDGETS");
		ArrayList defaults = (ArrayList)getParameter("DEFAULTS");
		Iterator iterRW = resetWidgets.iterator();
		Iterator iterDef = defaults.iterator();
		while(iterRW.hasNext() && iterDef.hasNext()){
			String widget = (String)iterRW.next();
			String defaultVal = (String)iterDef.next();
			context.getState().getCurrentRuntimeForm().getFormWidgetByName(widget).setDisplayValue(defaultVal);
		}
		return RET_CONTINUE;
	}
}