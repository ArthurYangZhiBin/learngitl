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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.bio.Query;

public class BatchPickingNewDetailPreSave extends ActionExtensionBase{

	private final static String ORDERKEY = "ORDERKEY";
	private final static String WAVE = "WAVEKEY";
	private final static String SO_TABLE = "wm_orders";
	private final static String ERROR_MESSAGE_SO_INVALID = "WMEXP_PICK_ORDER";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface toggle = state.getRuntimeForm(shell.getSubSlot("list_slot_2"), null);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot("list_slot_1"), null);
		RuntimeFormInterface detail = state.getRuntimeForm(toggle.getSubSlot("wm_batchpickingdetail_toggle_slot"), "Detail");
		DataBean focus = detail.getFocus();
		int selectedForm = state.getSelectedFormNumber(toggle.getSubSlot("wm_batchpickingdetail_toggle_slot"));
		if(focus != null && selectedForm == 1){
			if(focus.isTempBio()){
				QBEBioBean qbe = (QBEBioBean)focus;
				String[] parameter = new String[1];
				String waveKey=header.getFormWidgetByName(WAVE).getDisplayValue();
				qbe.set(WAVE, waveKey);
				Object soKey = qbe.get(ORDERKEY);
				String soValue = soKey.toString();
				String queryString = SO_TABLE+"."+ORDERKEY+"='"+soValue.toUpperCase()+"'";
				Query query = new Query(SO_TABLE, queryString, null);
				BioCollectionBean returnedOrders = state.getDefaultUnitOfWork().getBioCollectionBean(query);
				if(returnedOrders.size()!=1){
					parameter[0]=soValue;
					throw new FormException(ERROR_MESSAGE_SO_INVALID, parameter);
				}
			}
		}
		result.setFocus(header.getFocus());
		return RET_CONTINUE;
	}
}