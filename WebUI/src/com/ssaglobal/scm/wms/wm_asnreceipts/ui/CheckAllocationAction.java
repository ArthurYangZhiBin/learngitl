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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class CheckAllocationAction extends ActionExtensionBase {

    /**
     * @param context
     * @param result
     * @return int
     * @throws EpiException
     */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckAllocationAction.class);
    protected int execute(ActionContext context, ActionResult result) throws EpiException {
    	StateInterface state = context.getState();
        RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();    //gets the toolbar
        RuntimeFormInterface toggleForm = (toolbar.getParentForm(state));
        _log.debug("LOG_SYSTEM_OUT","Source Form is = "+ toolbar.getName(),100L);
        _log.debug("LOG_SYSTEM_OUT","Source Parent Form is = "+ toolbar.getParentForm(state).getName(),100L);
        SlotInterface toggleSlot = toggleForm.getSubSlot("wm_receiptdetail_toggle");
		_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
		RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_receiptdetail_detail_view");
		RuntimeFormInterface ListTab = state.getRuntimeForm(toggleSlot, "wm_receiptdetail_list_view");
		_log.debug("LOG_SYSTEM_OUT","detailTab = "+ detailTab.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","ListTab = "+ ListTab.getName(),100L);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioBean bio= null;
		if (state.getSelectedFormNumber(toggleSlot) == 0){
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)ListTab;
			_log.debug("LOG_SYSTEM_OUT","Header List Form = "+ headerListForm.getName(),100L);
			ArrayList items;
			items = headerListForm.getAllSelectedItems();
			if (items != null ){
				if(items.size() == 0){
					UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
		 	   		throw UsrExcp;
				}else{
					if (items.size() > 1){
						UserException UsrExcp = new UserException("WMEXP_MULTIPLE_RECORDS", new Object[]{});
			 	   		throw UsrExcp;
					}
					Iterator bioBeanIter = items.iterator();
					for(; bioBeanIter.hasNext();){
						bio = (BioBean)bioBeanIter.next();
						ValidatePO(bio,result,uow);
					}
				}
			}else{
				UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
	 	   		throw UsrExcp;
			}

			
		}else{
			bio = (BioBean)detailTab.getFocus();
			ValidatePO(bio,result,uow);
		}
        try{

        }catch(Exception e){
            e.printStackTrace();
        }

        return RET_CONTINUE;
        //        return super.execute(context, result);
    }
    public void ValidatePO(BioBean bio, ActionResult result, UnitOfWorkBean uow) throws EpiException{
		if (bio.get("POKEY") != null){
			if (bio.get("POKEY").toString().equalsIgnoreCase("0")){
				UserException UsrExcp = new UserException("WMEXP_NOCHECKALLOCATION", new Object[]{});
	 	   		throw UsrExcp;
			}else{
				String xOrderQry = "wm_vwmallocationmgt.POKEY='"+bio.get("POKEY")+"' AND wm_vwmallocationmgt.RECEIPTKEY='"+bio.get("RECEIPTKEY")+"'";
				_log.debug("LOG_SYSTEM_OUT","This is Item query: "+xOrderQry,100L);
				Query xOrderQuery = new Query("wm_vwmallocationmgt", xOrderQry, null);		
				BioCollectionBean xOrdersBio = uow.getBioCollectionBean(xOrderQuery);
				if ((xOrdersBio == null)||(xOrdersBio.size()<1)){
					String[] parameters = new String[1];
					parameters[0]= bio.get("POKEY").toString();
					UserException UsrExcp = new UserException("WMEXP_NOCHECKALLOCATION_1", parameters);
		 	   		throw UsrExcp;
				}else{
					result.setFocus((DataBean)xOrdersBio);	
				}
			}
		}else{
			UserException UsrExcp = new UserException("WMEXP_NOCHECKALLOCATION", new Object[]{});
 	   		throw UsrExcp;
		}
    }
}
