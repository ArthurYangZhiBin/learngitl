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

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;

public class BatchPickingPreDelete extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiDataException, FormException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shell = context.getSourceWidget().getForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot("list_slot_1"), null);
		String[] parameter = new String[1];
		parameter[0] = "";
		if(header.isListForm()){
			RuntimeListFormInterface list = (RuntimeListFormInterface)header;
			ArrayList selected = list.getSelectedItems();
			for(int index = 0; index<selected.size(); index++){
				BioBean bio = (BioBean)selected.get(index);
				String waveKey = bio.get("WAVEKEY").toString();
				String queryString = "wm_wavedetail.WAVEKEY='"+waveKey.toUpperCase()+"'";
				Query query = new Query("wm_wavedetail", queryString, null);
				BioCollectionBean bc = uowb.getBioCollectionBean(query); 
				if(bc.size()>0){
					if(parameter[0].equalsIgnoreCase("")){
						parameter[0] += waveKey;
					}else{
						parameter[0] += ", "+waveKey;
					}
				}
			}
			if(!parameter[0].equalsIgnoreCase("")){
				throw new FormException("WMEXP_BATCH_PICKING_DELETE", parameter);
			}
		}
		return RET_CONTINUE;
	}
}