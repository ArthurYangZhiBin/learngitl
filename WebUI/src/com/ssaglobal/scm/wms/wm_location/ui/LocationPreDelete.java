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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.state.StateInterface;
import java.util.ArrayList;

public class LocationPreDelete extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		String table = "wm_skuxloc", widgetName = "LOC";
		String[] parameter = new String[1];
		//Find location value selected for delete
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"),null);
		ArrayList selected = listForm.getAllSelectedItems();
		
		
		if (selected==null || selected.isEmpty() )
			throw new FormException("WMEXP_RECORD_NOT_SEL", null);
		
		
		DataBean bean = (DataBean)selected.get(0);
		String locValue = bean.getValue(widgetName).toString();
		
		//Query against SKUxLOC table for dependent records
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String queryString = table+"."+widgetName+"='"+locValue+"' and ("+table+".QTY>0 or "+table+".QTYEXPECTED>0)";
		Query qry = new Query(table, queryString, null);
		BioCollectionBean list = uow.getBioCollectionBean(qry);
		//If dependent records exist, block delete
		if(list.size()>0){
			parameter[0]=locValue;
			throw new FormException("WMEXP_SO_PDRID", parameter);
		}
		
		return RET_CONTINUE;
	}
}