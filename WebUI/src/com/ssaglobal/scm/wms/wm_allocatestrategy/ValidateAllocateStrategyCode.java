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
package com.ssaglobal.scm.wms.wm_allocatestrategy;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;


public class ValidateAllocateStrategyCode extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{			
		StateInterface state = context.getState();			
		RuntimeFormInterface toolBar = context.getSourceWidget().getForm();		
		RuntimeFormInterface shellForm = toolBar.getParentForm(state);			
		SlotInterface shellSlot1 = shellForm.getSubSlot("list_slot_1");		//HC
		SlotInterface shellSlot2 = shellForm.getSubSlot("list_slot_2");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellSlot1, null);
		RuntimeFormInterface detailForm = state.getRuntimeForm(shellSlot2, null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	
		if(detailForm != null && detailForm.getName().equals("wm_allocatestrategydetail_toggle_slot")){
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_allocatestrategydetail_toggle_slot");
			detailForm = state.getRuntimeForm(toggleSlot, "Detail");
		}		
		String allocateStrategyKey = headerForm.getFormWidgetByName("ALLOCATESTRATEGYKEY").getDisplayValue();
		if(headerForm.getFocus().isTempBio()){																			
			Query loadBiosQry = new Query("wm_allocatestrategy", "wm_allocatestrategy.ALLOCATESTRATEGYKEY = '"+allocateStrategyKey.toUpperCase()+"'", null);											
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					String args[] = {allocateStrategyKey}; 
					String errorMsg = getTextMessage("WMEXP_ALLOCATESTRAT_DUP_CODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		if(detailForm != null && !detailForm.isListForm() && detailForm.getFocus() != null && detailForm.getFocus().isTempBio()){
			try {				
				String allocateStrategyDetailKey = detailForm.getFormWidgetByName("ALLOCATESTRATEGYLINENUMBER").getDisplayValue();
				Query loadBiosQry = new Query("wm_allocatestrategydetail", "wm_allocatestrategydetail.ALLOCATESTRATEGYKEY = '"+allocateStrategyKey.toUpperCase()+"' AND wm_allocatestrategydetail.ALLOCATESTRATEGYLINENUMBER = '"+allocateStrategyDetailKey.toUpperCase()+"'", null);
								
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);																	
				if(bioCollection.size() > 0){
					String args[] = {allocateStrategyDetailKey}; 
					String errorMsg = getTextMessage("WMEXP_DUP_STEPNUMBER",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		
		//SCM-00000-8603: user is allowed to manually key in the pick code; 
		// validate it against the uom field
		//
		if(detailForm != null && !detailForm.isListForm() && detailForm.getFocus() != null )
		{
			try
			{
				String pickCode = detailForm.getFormWidgetByName("PICKCODE").getDisplayValue();
				String strategyCode = detailForm.getFormWidgetByName("UOM").getDisplayValue();
				String qryString = "wm_strategy_code.STRATEGYTYPE='ALLOC' and wm_strategy_code.STRATEGYCODE='"+strategyCode+"' and wm_strategy_code.STRATEGYPICKCODE='"+pickCode+"'";
				Query qry = new Query("wm_strategy_code", qryString, null);
				BioCollectionBean codeList = uow.getBioCollectionBean(qry);
				
				if(codeList.size()<1)
				{
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_INVALID_PICKCODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				// if we reach this point, update the DESCR based on PICKCODE
				//
				BioBean codeDescBio = (BioBean)codeList.elementAt(0);
				String codeDesc = codeDescBio.getString("DESCRIPTION");
	  	   	    DataBean detailFocus = detailForm.getFocus();
	 			detailFocus.setValue("DESCR", codeDesc);
			} 
			
			catch (EpiDataException e) 
			{			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
		return RET_CONTINUE;
		
	}	
}