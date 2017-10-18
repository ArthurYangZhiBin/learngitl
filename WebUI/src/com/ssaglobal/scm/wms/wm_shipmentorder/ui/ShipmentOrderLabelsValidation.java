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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class ShipmentOrderLabelsValidation extends ActionExtensionBase{
	private final static String shellSlot1 = "list_slot_1";
	private final static String ORDER = "ORDERKEY";
	private final static String RFIDFLAG = "RFIDFLAG";
	private final static String IS_ONE = "IS_ONE";
	private final static String MESSAGE = "SO_LABEL_MESSAGE";
	private final static String LABEL_MESSAGE = "WMEXP_LABEL_PRINT";
	private final static String ERROR_MESSAGE_LIST_FORM = "WMEXP_LIST_FORM_BLOCK";
	protected int execute(ActionContext context, ActionResult result) throws FormException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = context.getSourceWidget().getForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(shellSlot1), null);
		if(header.isListForm()){
			//Is list: throw error message
			throw new FormException(ERROR_MESSAGE_LIST_FORM, null);
		}else{
			//Set order number message and action object switch into session
			HttpSession session = state.getRequest().getSession();
			BioBean bio = (BioBean)header.getFocus();
			//Set Orderkey and Order Number message to session
			String order = bio.get(ORDER).toString();
			session.setAttribute(ORDER, order);
			
			String base = getTextMessage(LABEL_MESSAGE, new Object[] {order}, state.getLocale());
			session.setAttribute(MESSAGE, base);
			//Set rfidflag to session
			String flag = bio.get(RFIDFLAG).toString();
			session.setAttribute(RFIDFLAG, flag);
			//Set is_one atttribute to session
			if(context.getActionObject().getName().equals("Labels by Assignment")){
				session.setAttribute(IS_ONE, "0");
			}else{
				session.setAttribute(IS_ONE, "1");
			}
		}
		return RET_CONTINUE;
	}
}