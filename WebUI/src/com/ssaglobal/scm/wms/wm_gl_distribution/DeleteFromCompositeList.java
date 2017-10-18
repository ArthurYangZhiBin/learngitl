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
package com.ssaglobal.scm.wms.wm_gl_distribution;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

public class DeleteFromCompositeList extends ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DeleteFromCompositeList.class);
	
	public DeleteFromCompositeList() {
		
		_log.info("EXP_1","DeleteFromList Instantiated!!!",  SuggestedCategory.NONE);
		
	} 
	
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		
		String processStr = (String)getParameter("processName");
		String [] process = new String[1];
		process[0] = processStr;
		
		String headerOrDetail = (String)getParameter("headerOrDetail");
		String toggleFormSlot = (String)getParameter("toggleFormSlot");
		String detailListTab = (String)getParameter("detailListTab");
		String compositeListFormName = (String)getParameter("compositeListForm");
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","behaviour ="+processStr,100L);		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","headerOrDetail ="+headerOrDetail,100L);		
		
		RuntimeListFormInterface listForm = null;
		_log.info("EXP_1","Start processing "+processStr,  SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		if("Header".equalsIgnoreCase(headerOrDetail)){
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if(!headerForm.isListForm()){
				throw new UserException("Delete_From_Header_View",process);
			}
			listForm = (RuntimeListFormInterface)headerForm;
		}else if("Detail".equalsIgnoreCase(headerOrDetail)){
			RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
			SlotInterface toggleSlot = toggleForm.getSubSlot(toggleFormSlot);						
			RuntimeFormInterface detailTabList = state.getRuntimeForm(toggleSlot, detailListTab);		
			if(compositeListFormName != null && compositeListFormName.length() > 0){
				SlotInterface compositeListSlot = detailTabList.getSubSlot(compositeListFormName);				
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","compositeListSlot:"+compositeListSlot,100L);
				Iterator itr = detailTabList.getSubSlotsIterator();
				while(itr.hasNext()){
					SlotInterface tempSlot = (SlotInterface)itr.next();
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Slot:"+tempSlot.getName(),100L);					
				}
				RuntimeFormInterface compositeListForm = state.getRuntimeForm(compositeListSlot, null);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","detail form name="+compositeListForm.getName()+"  is listform="+compositeListForm.isListForm(),100L);				
				if(!compositeListForm.isListForm()){
					throw new UserException("Delete_From_Detail_View",process);
				}
				listForm = (RuntimeListFormInterface)compositeListForm;	
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","detail form name="+detailTabList.getName()+"  is listform="+detailTabList.isListForm(),100L);				
				if(!detailTabList.isListForm()){
					throw new UserException("Delete_From_Detail_View",process);
				}
				listForm = (RuntimeListFormInterface)detailTabList;
			}
		}
		ArrayList selectedItems = listForm.getAllSelectedItems();
		if(selectedItems.size()> 1){
			throw new UserException("Single_Select_Validation",process);
		}
		else{
			if(selectedItems != null && selectedItems.size() > 0)
			{
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				try
				{
					BioBean bio;
					for(; bioBeanIter.hasNext(); bio.delete())
						bio = (BioBean)bioBeanIter.next();
					
					uowb.saveUOW();
//					if(listForms.size() <= 0)
//					listForms = (ArrayList)getTempSpaceHash().get("SELECTED_LIST_FORMS");
//					clearBuckets(listForms);
					result.setSelectedItems(null);
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_DELETING_BIO", null);
				}
			}
		}
		return RET_CONTINUE;
		
	}
}




