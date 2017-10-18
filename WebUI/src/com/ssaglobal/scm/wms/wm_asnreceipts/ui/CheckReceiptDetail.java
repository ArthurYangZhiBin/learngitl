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

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class CheckReceiptDetail extends ActionExtensionBase {

    /**
     * @param context
     * @param result
     * @return int
     * @throws EpiException
     */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckReceiptDetail.class);
    protected int execute(ActionContext context, ActionResult result) throws EpiException {
    	StateInterface state = context.getState();
        RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
        RuntimeFormInterface toggleForm = (toolbar.getParentForm(state));
        SlotInterface toggleSlot = toggleForm.getSubSlot("wm_receiptdetail_toggle");
		RuntimeFormInterface ListTab = state.getRuntimeForm(toggleSlot, "wm_receiptdetail_list_view");
		if (state.getSelectedFormNumber(toggleSlot) == 0)
		{
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)ListTab;
			_log.debug("LOG_SYSTEM_OUT","Header List Form = "+ headerListForm.getName(),100L);
			ArrayList items;
			items = headerListForm.getAllSelectedItems();
			if(items == null || items.size() == 0)
			{
				UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
	 	   		throw UsrExcp;
			}
			else if (items.size() > 1)
			{
				UserException UsrExcp = new UserException("WMEXP_MULTIPLE_RECORDS", new Object[]{});
	 	   		throw UsrExcp;
			}
			else
			{
				BioBean bio= null;
				Iterator bioBeanIter = items.iterator();
				for(; bioBeanIter.hasNext();){
					bio = (BioBean)bioBeanIter.next();
					String receiptKey = bio.get("RECEIPTKEY").toString();
					String receiptLineNumber = bio.get("RECEIPTLINENUMBER").toString();
					
					HttpSession session = state.getRequest().getSession();
					session.setAttribute("RECEIPTKEY", receiptKey);
					session.setAttribute("RECEIPTLINENUMBER", receiptLineNumber);
				}
			}
		}
		else
		{
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_receiptdetail_detail_view");
			DataBean detailFocus = detailTab.getFocus();
			BioBean bio;
			if (detailFocus instanceof BioBean) 
			{
				bio = (BioBean) detailFocus;
				String receiptKey = bio.getValue("RECEIPTKEY").toString();
				String receiptLineNumber = bio.get("RECEIPTLINENUMBER").toString();

				HttpSession session = state.getRequest().getSession();
				session.setAttribute("RECEIPTKEY", receiptKey);
				session.setAttribute("RECEIPTLINENUMBER", receiptLineNumber);
			}
			else
			{
			
				UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
	 	   		throw UsrExcp;
			}
		}

        return RET_CONTINUE;
    }
}
