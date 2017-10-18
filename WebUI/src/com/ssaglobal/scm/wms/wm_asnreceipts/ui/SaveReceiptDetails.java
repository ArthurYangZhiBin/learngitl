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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;


import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


public class SaveReceiptDetails extends ActionExtensionBase {

    /**
     * @param context
     * @return result
     * @throws EpiException
     */
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
        
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		String receiptKey = session.getAttribute("RECEIPTKEY").toString();
		String receiptLineNumber = session.getAttribute("RECEIPTLINENUMBER").toString();
		
		String whseid = "0";
    	String selectMaxLine = "SELECT whseid FROM receiptdetail WHERE receiptdetail.RECEIPTKEY = '"+receiptKey+"' " +
    							"AND receiptdetail.RECEIPTLINENUMBER = '"+receiptLineNumber+"' ";
		EXEDataObject whseIdCollection = null;		
		try {
			whseIdCollection = WmsWebuiValidationSelectImpl.select(selectMaxLine);		
			if(whseIdCollection != null && whseIdCollection.getRowCount() > 0){
				whseid = whseIdCollection.getAttribValue(new TextData("whseid")).toString();
			}	
		} catch (DPException e2) {
		}	
		
		try
		{
			WmsWebuiValidationUpdateImpl.update("UPDATE RECEIPTDETAIL SET WHSEID = '"+whseid+"' WHERE WHSEID = 'DUP' AND RECEIPTKEY = '"+receiptKey+"' ");	
		} 
		catch (DPException e1)
		{
			e1.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_DUP_SAVE", new Object[1]);
		}


		String query = "receiptdetail.RECEIPTKEY = '"+receiptKey+"' ";
		
		Query receiptQuery = new Query("receiptdetail", query , null);
        BioCollectionBean results = uowb.getBioCollectionBean(receiptQuery);
        result.setFocus(results);


		session.removeAttribute("RECEIPTKEY");
		session.removeAttribute("RECEIPTLINENUMBER");

        return RET_CONTINUE;
    }
    
}