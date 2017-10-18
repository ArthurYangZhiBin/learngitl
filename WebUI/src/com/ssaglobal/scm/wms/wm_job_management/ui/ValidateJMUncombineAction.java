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
package com.ssaglobal.scm.wms.wm_job_management.ui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ValidateJMUncombineAction extends ListSelector {
	public static String RECORDCOUNT = "recordcount";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateJMUncombineAction.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface detailForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ detailForm.getName(),100L);
		
		SlotInterface toggleSlot = detailForm.getSubSlot("wm_job_management_detail_toggle_slot");		
		_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
		RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "Detail");
		RuntimeFormInterface ListTab = state.getRuntimeForm(toggleSlot, "List");
		_log.debug("LOG_SYSTEM_OUT","detailTab = "+ detailTab.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","ListTab = "+ ListTab.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","Slot ID = "+toggleSlot.getId().toString(),100L);
		_log.debug("LOG_SYSTEM_OUT","Toggle Form focus = "+detailForm.getFocus(),100L);
		if (state.getSelectedFormNumber(toggleSlot) == 0){
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)ListTab;
			_log.debug("LOG_SYSTEM_OUT","Header List Form = "+ headerListForm.getName(),100L);
			ArrayList items;
			items = headerListForm.getAllSelectedItems();
			
			if (items != null ){
				if(items.size() == 0){
					UserException UsrExcp = new UserException("WMEXP_NCHKUNCOMBINE_WARNING_1", new Object[]{});
		 	   		throw UsrExcp;
				}else{
					Iterator bioBeanIter = items.iterator();
					BioBean bio;
					for(; bioBeanIter.hasNext();){
						bio = (BioBean)bioBeanIter.next();
						if (bio.get("ISCOMBINED") != null){
							if (bio.get("ISCOMBINED").toString().equalsIgnoreCase("0")){
								UserException UsrExcp = new UserException("WMEXP_UNCOMBINE_WARNING_0", new Object[]{});
					 	   		throw UsrExcp;
							}
						}
					}
				}
			}else{
				UserException UsrExcp = new UserException("WMEXP_NCHKUNCOMBINE_WARNING_1", new Object[]{});
	 	   		throw UsrExcp;
			}
			
		}else{
			BioBean bio = (BioBean)detailTab.getFocus();
			if (bio.get("ISCOMBINED") != null){
				if (bio.get("ISCOMBINED").toString().equalsIgnoreCase("0")){
					UserException UsrExcp = new UserException("WMEXP_UNCOMBINE_WARNING_0", new Object[]{});
		 	   		throw UsrExcp;
				}
			}
		}
		return RET_CONTINUE;
	}
}

