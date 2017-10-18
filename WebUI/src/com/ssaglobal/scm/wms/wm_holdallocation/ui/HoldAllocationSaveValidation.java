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


package com.ssaglobal.scm.wms.wm_holdallocation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;



public class HoldAllocationSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private static String BIOCLASSNAME = "wm_holdallocationmatrix";
  
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate

	   
	   StateInterface state = context.getState();
	   
	   RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
	   RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
	   SlotInterface headerSlot = shellForm.getSubSlot("list_slot_2");				//Get slot2
	   RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot2
	   DataBean focus = headerForm.getFocus();								//Get the header form focus
	   String orderType = (String)focus.getValue("ORDERTYPE");
	   String holdCode = (String)focus.getValue("STATUSCODE");
	   
	   if(focus.isTempBio())
		   holdAllocationDuplicationCheck(state, orderType, holdCode);
	   
      return RET_CONTINUE;
   }
 
   private void holdAllocationDuplicationCheck(StateInterface state, String orderType, String holdCode)
   throws UserException, EpiDataException{
	   UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
	   
	   if(orderType==null)
		   return;
	   
	   String whereClause = BIOCLASSNAME+".ORDERTYPE='"  + orderType+ 
	   				"' AND " +  BIOCLASSNAME + ".STATUSCODE ='" + holdCode+"'";
	   Query query  = new Query(BIOCLASSNAME,whereClause, "");
	   
	   BioCollectionBean holdAllocationList = uowb.getBioCollectionBean(query);
	
		   if(holdAllocationList!=null && holdAllocationList.size()>0){
			   throw new UserException("WMEXP_DUPLICATE_HOLDALLOCATION", new Object[0]);
		   }
	
   }
}
