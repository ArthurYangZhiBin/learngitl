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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


public class HoldsByLotSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {


	protected static ILoggerCategory _log = LoggerFactory.getInstance(HoldsByLotSaveValidation.class);
	
	private static String BIOATTRIBUTE_LOT = "LOT";
	
	   
   /**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {


    	StateInterface state = ctx.getState();
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
    	RuntimeFormInterface toolbarForm =  state.getCurrentRuntimeForm();
    	RuntimeListFormInterface listForm = (RuntimeListFormInterface)toolbarForm.getParentForm(state);
    	RuntimeFormInterface ihFooter = ctx.getModalFooterForm();//SRG
    	ArrayList selected = listForm.getSelectedItems();
    	
    	// 2012-08-14
    	// Modified by Will Pu
    	// ¶³½á´úÂë´æ´¢´úÂë£¬²»´æ´¢ÃèÊö×Ö¶Î
    	String holdCode = toolbarForm.getFormWidgetByName("HoldCode").getValue().toString();
    	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
    	String comment;
    	//SRG
		if(ihFooter.getFormWidgetByName("Comment") == null)
		{
			comment = " ";
		}else
		{
			comment =  ihFooter.getFormWidgetByName("Comment").getValue().toString(); //SRG
		}
    	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
    	validateDataEntry(holdCode, selected);
    	
    	//validateLots(uowb, selected, holdCode);
    	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
    	validateLots(uowb, selected, holdCode, comment);
    	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
       return RET_CONTINUE;
    }
    
    private void validateDataEntry(String holdCode, ArrayList selected) throws UserException{
    	if(holdCode==null || holdCode.trim().length()==0){
    		throw new UserException("WMEXP_NOT_SELECTED_HOLDCODE", new Object[0]);
    	}
    	if(selected==null || selected.size()==0){
    		throw new UserException("WMEXP_NOT_SELECTED_ITEMS", new String[]{"Lots"});
    	}
    	_log.debug("LOG_SYSTEM_OUT","[HoldsByLotSaveValidation]Number of selected items:" +selected.size(),100L);
    	
    }
    
    /*Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Added Comments argument to validateLots*/
    private void validateLots(UnitOfWorkBean uowb, ArrayList selected , String holdCode, String comment)throws UserException, EpiDataException{
    	for(int idx = 0; idx < selected.size(); idx++){
    		Bio lotBio = (Bio)selected.get(idx);
    		if(lotBio==null){
    			_log.debug("LOG_SYSTEM_OUT","[HoldsByLotSaveValidation]lotBio is null",100L);
    			continue;
    		}
    		
    		
    		String lot = (String)lotBio.get(BIOATTRIBUTE_LOT);
    		
    		_log.debug("LOG_SYSTEM_OUT","[HoldsByLotSaveValidation]Lot:"+lot,100L);
    		InventoryHoldHelper helper = new InventoryHoldHelper();
    		String hold = helper.getInventoryHoldHold(uowb, BIOATTRIBUTE_LOT, lot, holdCode);
    		if(hold==null ||hold.equalsIgnoreCase("0")){
    			hold="1";
    			
    			//helper.runInventoryHoldSP(null, lot, null, null,holdCode, hold );
    			//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
    			helper.runInventoryHoldSP(null, lot, null, null,holdCode, hold,comment);
    			//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
    		}
    		
    		//String holdCodeStatus = getHoldCodeStatus();
    	}

    }
    
   
    
}
