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


import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class GetTaskDetailForASN extends ActionExtensionBase {


    /**
     * Executes when "Add Existing" button is clicked.
     * @param context
     * @param result
     * @return RET_CONTINUE if there is no exception
     * @throws UserException if price list is null
     */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(GetTaskDetailForASN.class);
    protected int execute(ActionContext context, ActionResult result) throws EpiException {

    	//Get the focus bean
        //check if its a QBE bean
        //If its QBE bean, then get the value of the pricelist attribute and check its null or not
        //if its null, then throw a user exception, else continue.
    	StateInterface state = context.getState();
    	
    	RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
    	_log.debug("LOG_SYSTEM_OUT","Current Form = "+ currentForm.getName().toString() ,100L);
    	RuntimeFormInterface ShellForm = (currentForm.getParentForm(state));
    	_log.debug("LOG_SYSTEM_OUT","tabGroupShellForm Form = "+ ShellForm.getName().toString() ,100L);
    	SlotInterface Slot1 = ShellForm.getSubSlot("list_slot_1");
    	_log.debug("LOG_SYSTEM_OUT","tabGroupSlot Form = "+ Slot1.getName().toString() ,100L);
    	RuntimeFormInterface tabGrpForm = state.getRuntimeForm(Slot1, null);
    	_log.debug("LOG_SYSTEM_OUT","tabGrpForm Form = "+ tabGrpForm.getName().toString() ,100L);
    	DataBean objDataBeanReceipt = tabGrpForm.getFocus();
		String receiptKey= null;
    	if(objDataBeanReceipt instanceof BioBean)
        {
        	BioBean objBioBeanReceipt = (BioBean)objDataBeanReceipt;
        	receiptKey = (String)objBioBeanReceipt.get("RECEIPTKEY");
        	_log.debug("LOG_SYSTEM_OUT","In side IF BioBean RECEIPTKEY = "+ receiptKey ,100L);
        }
    	_log.debug("LOG_SYSTEM_OUT","RECEIPTKEY = "+ receiptKey ,100L);
        if (receiptKey == null){
        	throw new UserException ("EXP_CHECK_PRICELIST_FOR_ORDER", "You should select pricelist before adding products.", new Object[]{});
        }else{
        	
        	UnitOfWork uow=objDataBeanReceipt.getUnitOfWorkBean().getUOW();
        	HelperBio helperBio = uow.createHelperBio("wm_taskdetail");
        	helperBio.useForQBE();
        	helperBio.set("SOURCEKEY",receiptKey);
        	Query prods  = helperBio.getQuery();
        	_log.debug("LOG_SYSTEM_OUT","Query  = "+ prods.toString() ,100L);
        	BioCollection resultColl = objDataBeanReceipt.getUnitOfWorkBean().getBioCollectionBean(prods);
        	result.setFocus((DataBean)resultColl);
        	return RET_CONTINUE;
        }
       
    }
}




