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
package com.ssaglobal.scm.wms.wm_receiptreversal.ui;

import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;

public class ReceiptReversalASNPreRender extends FormWidgetExtensionBase{
	//Static table names
	private static final String TABLE = "wm_receiptreversaldetail";
	
	//Static attribute names
	private static final String ADJ = "ADJUSTMENTKEY";
	private static final String ASN = "RECEIPTKEY";
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException{
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus=form.getFocus();
		if(!focus.isTempBio()){
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormWidgetInterface adjustment = form.getFormWidgetByName(ADJ);
			String adjKey = adjustment.getDisplayValue();
			String queryString = TABLE+"."+ADJ+"='"+adjKey.toUpperCase()+"'";
			Query qry = new Query(TABLE, queryString, null);
			BioCollectionBean list = uow.getBioCollectionBean(qry);
			String asnKey = list.get("0").get(ASN).toString();
			widget.setValue(asnKey);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}	
		return RET_CONTINUE;
	}
}