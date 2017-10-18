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
    
public class disableASNActionItems extends com.epiphany.shr.ui.view.customization.MenuExtensionBase {
	 
	protected int execute(StateInterface state, RuntimeMenuInterface menu) {
		String shellSlot1 = "list_slot_1";
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		try {
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
			   if (menuItem.getName().equals("Receive All")){
				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
			   }
			   if (menuItem.getName().equals("Populate from PO")){
				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
			   }
			   if (menuItem.getName().equals("Populate from Shipment Order")){
				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
			   }
			   if (menuItem.getName().equals("Putaway")){
				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
			   }
			   
		   }
    	   if (headerFocus instanceof QBEBioBean)
    	   {
    		   if (menuItem.getName().equals("Receive All")){
    			   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
    		   }
    		   if (menuItem.getName().equals("Close ASN/Receipt")){
        		   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
       		   }
    		   if (menuItem.getName().equals("Reopen ASN/Receipt")){
        		   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
       		   }
    		   if (menuItem.getName().equals("Putaway")){
    			   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
       		   }
    		   
    	   }
    	   if (headerFocus instanceof BioBean){
   			BioBean objBioBeanReceipt = (BioBean)headerFocus;
			Integer receiptStatus = new Integer(objBioBeanReceipt.get("STATUS").toString());
    		   if (receiptStatus.intValue() < 11){
    			   if (menuItem.getName().equals("Close ASN/Receipt")){
    				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
    			   }
    			   if (menuItem.getName().equals("Reopen ASN/Receipt")){
   						menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
    			   }
    		   }
    		   if (receiptStatus.intValue() == 11){
    			   if (menuItem.getName().equals("Close ASN/Receipt")){
    				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
    			   }
    			   if (menuItem.getName().equals("Reopen ASN/Receipt")){
   						menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
    			   }
    		   }
    		   if (receiptStatus.intValue() > 11){
    			   if (menuItem.getName().equals("Close ASN/Receipt")){
    				   menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
    			   }
    			   if (menuItem.getName().equals("Reopen ASN/Receipt")){
   						menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
    			   }
    		   }
    		   //05/12/2010 FW:  Added code to hide 'Populate from PO or SO' menu if status > 11 (Incedent3778998_Defect272416) -- Start
			   if (menuItem.getName().equals("Populate from PO")){
						menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
			   }
			   
			   if (menuItem.getName().equals("Populate from Shipment Order")){
						menuItem.setBooleanProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
			   }
			   //05/12/2010 FW:  Added code to hide 'Populate from PO or SO' menu if status > 11 (Incedent3778998_Defect272416) -- End
    	   }
    	   } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
   }   
   
}

