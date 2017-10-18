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


package com.ssaglobal.scm.wms.wm_cxadjustment.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;

;


/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class disableCXAdjustmentActionItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase {
	 
	protected int execute(StateInterface state, RuntimeMenuInterface menu) {
		String shellSlot1 = "list_slot_1";
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		try {
			/*
		   if (headerFocus instanceof BioCollectionBean)
		   {    
			  
			   if (menu.getName().equals("Putaway")){
				   menu.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
			   }
			   
		   }
    	   if (headerFocus instanceof QBEBioBean)
    	   {

    		   if (menu.getName().equals("Putaway")){
    			   menu.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
       		   }
    		   
    	   }
    	   */
    	   } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
	}
	/**
    * The code within the execute method will be run on the FormRender.
    *   events.
    * @param state the state of the user's navigation
    * @param menuItem the  menu item that is about to be rendered
    * @return int RET_CONTINUE, RET_CANCEL
    */
   protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem) { 
		String shellSlot1 = "list_slot_1";
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
			
		DataBean headerFocus = headerForm.getFocus();
		try {
		   if (headerFocus instanceof BioCollectionBean)
		   {    
			   if (menuItem.getName().equals("Reset Inventory")){
				   if(headerForm.isListForm())   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
				   else menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
			   }
			   
		   }
    	   if (headerFocus instanceof QBEBioBean)
    	   {
    		   if (menuItem.getName().equals("Reset Inventory")){
				   if(headerForm.isListForm())   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
				   else menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
    		   }
    		   
    		   
    	   }
    	   } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
   }   
   
}

