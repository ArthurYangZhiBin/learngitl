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
package com.ssaglobal.scm.wms.wm_hazmatcodes;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;


public class ValidateHazmatCode extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		
		StateInterface state = context.getState();			
		RuntimeFormInterface toolBar = context.getSourceWidget().getForm();		
		RuntimeFormInterface shellForm = toolBar.getParentForm(state);			
		SlotInterface shellSlot = shellForm.getSubSlot("list_slot_2");		//HC		  
		RuntimeFormInterface detailForm = state.getRuntimeForm(shellSlot, null);		
		if(detailForm.getFocus().isTempBio()){
			String hazmatCodesKey = detailForm.getFormWidgetByName("HAZMATCODESKEY").getDisplayValue();																	
			Query loadBiosQry = new Query("wm_hazmatcodes", "wm_hazmatcodes.HAZMATCODESKEY = '"+hazmatCodesKey.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					String args[] = {hazmatCodesKey}; 
					String errorMsg = getTextMessage("WMEXP_HAZMATCODES_DUP_CODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		return RET_CONTINUE;
		
	}	
}