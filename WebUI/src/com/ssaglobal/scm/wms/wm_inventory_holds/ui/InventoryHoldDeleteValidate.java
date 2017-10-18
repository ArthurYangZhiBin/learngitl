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
package com.ssaglobal.scm.wms.wm_inventory_holds.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.data.error.EpiDataException; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.model.data.UnitOfWorkBean; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_inventory_holds.ui.IHSaveValidationAction.IHForm;
import com.epiphany.shr.util.exceptions.UserException;


public class InventoryHoldDeleteValidate extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryHoldDeleteValidate.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	BioBean selectedHold;
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","@$ It is in inventory hold delete extension **********",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeListFormInterface imListForm = (RuntimeListFormInterface)FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_inventory_hold_list_view", state);
		ArrayList selectedMoves = imListForm.getAllSelectedItems();

		if (isNull(selectedMoves))
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			//return RET_CANCEL; //nothing to process
		}
		//AW 09/21/10 Incident:3997553 Defect:283808 start
		try {
		canInvBeRemovedFromHold(selectedMoves, context);
		}
		catch(Exception e) {
			 throw new UserException("WMEXP_INVENTORY_HOLD_ON_SELECTED", new Object[] {}); 
		}

			
			
		return RET_CONTINUE;
		//AW 09/21/10 Incident:3997553 Defect:283808 end
		}
		
	/**
	 * 
	 * @param selectedMoves
	 * @param context
	 * @throws EpiDataException
	 * @throws UserException
	 * 
	 * AW 09/21/10 Incident:3997553 Defect:283808
	 */
	private void canInvBeRemovedFromHold(ArrayList selectedMoves,
			ActionContext context) throws EpiDataException, UserException {
		// TODO Auto-generated method stub
		String holdStatus="";
		Object location = null;
		BioCollectionBean listCollection = null;
		int size = 0;
		
		for (Iterator it = selectedMoves.iterator(); it.hasNext();)
		{
			selectedHold = (BioBean) it.next();
			holdStatus = (String)selectedHold.get("HOLD");
			location = selectedHold.get("LOC");
			
			if("1".equalsIgnoreCase(holdStatus)){
				throw new UserException("WMEXP_INVENTORY_HOLD_ON_SELECTED", new Object[] {});				
			}

			
			if(!isNull(location) && holdStatus.equalsIgnoreCase("0")){
				if (locationIsOnHold(location, context)){
				String loc = location.toString();
						    
		    		String query = "(wm_inventoryhold_bio.LOC = '" + loc + "' AND  wm_inventoryhold_bio.HOLD = '1')";
		    	   _log.debug("LOG_SYSTEM_OUT","query = "+ query,100L);
		    	   Query bioQuery = new Query("wm_inventoryhold_bio",query,null);
		    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		           listCollection = uow.getBioCollectionBean(bioQuery);
		           size = listCollection.size();
		        
		           if( size > 1) {
		        	   //allow delete
		        	  // return true;
		           }
		           else {
		        	   throw new UserException("WMEXP_INVENTORY_HOLD_ON_SELECTED", new Object[] {});
		        	   //return false;
		           }
			}	
				
				
			}
			
				
				
			}
	}

/**
 * 
 * @param location
 * @param context
 * @return
 * @throws EpiDataException
 * AW 09/21/10 Incident:3997553 Defect:283808
 */
	private boolean locationIsOnHold(Object location, ActionContext context) throws EpiDataException {
		// TODO Auto-generated method stub
		
		String loc = location.toString();
		BioCollectionBean listCollection = null;
		int size = 0;
				    
    		String query = "(wm_location.LOC = '" + loc + "' AND  wm_location.LOCATIONFLAG in ( 'HOLD', 'DAMAGE' ))";
    	   _log.debug("LOG_SYSTEM_OUT","query = "+ query,100L);
    	   Query bioQuery = new Query("wm_location",query,null);
    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
           listCollection = uow.getBioCollectionBean(bioQuery);
           size = listCollection.size();
        
           if( size > 1) {
        	   return true;
           }
           else {
        	   return false;	   
           }
		
		
	}

/**
 * 
 * @param attributeValue
 * @return
 * AW 09/21/10 Incident:3997553 Defect:283808 
 */
	private boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
