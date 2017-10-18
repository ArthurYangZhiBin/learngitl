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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_internal_transfer.action.ConfirmationForAction;

public class ASNLabelsPrintLabels extends ActionExtensionBase {

    /**
     * @param context
     * @param result
     * @return int
     * @throws EpiException
     */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNLabelsPrintLabels.class);
    protected int execute(ActionContext context, ActionResult result) throws EpiException {
    	StateInterface state = context.getState();
    	String slotNum= "";
    	
    	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();    //gets the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		   
	  	SlotInterface toggleSlot = shellForm.getSubSlot("wm_receiptdetail_toggle");
		_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
    	_log.debug("LOG_SYSTEM_OUT","toggleSlot Number  ="+state.getSelectedFormNumber(toggleSlot),100L);
    	
    	slotNum="" +state.getSelectedFormNumber(toggleSlot);
    	// put slot number(tab) user is on in context, user by ASNReceiptPrintLabel
    	context.getServiceManager().getUserContext().put("ASNLABELS", slotNum);
    	
    return RET_CONTINUE;	
    }
}