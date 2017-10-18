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
package com.ssaglobal.scm.wms.wm_physical_parameters.action;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.exceptions.FormException;

import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class PhysicalParametersPostPhysicalAction extends ActionExtensionBase{
	//Static form reference variables
	private static String SHELL_SLOT = "list_slot_1";
	
	//Static local variables
	private static String ADJUSTMENT = "ADJ";
	private static String ADJ_WIDGET = "POSCHANGEPOSTAS";
	private static String ADJ_VALUE = "A";
	
	//Stored procedure name
	private static String PROC_NAME = "NSP_POST_PHYSICAL";
	
	//Static error message variable
	private static String ERROR_MESSAGE = "WMEXP_POST_PHYSICAL";
	private final static String ERROR_MESSAGE_PROC_FAILED = "WMEXP_STORED_PROC_FAILED";
	
	protected int execute(ActionContext context, ActionResult result) throws FormException{
		//Find passed value
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface searchForm = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		Object post = searchForm.getFormWidgetByName(ADJ_WIDGET).getValue();
		if(post==null){
			throw new FormException(ERROR_MESSAGE, null);
		}else{
			String postValue = post.toString();
			if(postValue.equals(ADJUSTMENT)){
				postValue = ADJ_VALUE;
			}else{
				throw new FormException(ERROR_MESSAGE, null);
			}
			//Execute Stored Procedure
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(postValue));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName(PROC_NAME);
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);	
			}catch(Exception e){
				throw new FormException(ERROR_MESSAGE_PROC_FAILED, null);
			}
		}
	
		return RET_CONTINUE;
	}
}