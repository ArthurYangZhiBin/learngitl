 /******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/



package com.ssaglobal.scm.wms.wm_serial_discrepancy.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class IntransitSerialNumberButtonPreRender extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {


	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 05/17/2011 FW  Added Serial Transaction Discrepancy Screen (Def311963)
	 */
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) { 

	    try {
   		 	RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
   		 	while(!(shellForm.getName().equals("wms_list_shell"))){
   		 		shellForm = shellForm.getParentForm(state);
   		 	}
   		 	
   			SlotInterface slot1 = shellForm.getSubSlot("list_slot_1");
   			RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
   			
   			SlotInterface slot2 = shellForm.getSubSlot("list_slot_2");
   			RuntimeListFormInterface detailListForm = (RuntimeListFormInterface)state.getRuntimeForm(slot2, null);

   			Double inventoryQty = null;

   			//Error will be raised by getFormWidgetByName("QTY") if inventoryQty = serial count and header record will be removed 
   			try {
   	   			inventoryQty = Double.parseDouble(headerForm.getFormWidgetByName("QTY").getValue().toString());
   			} catch(Exception e) {
   	        	//disable 'update serail' button
   				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
   				return RET_CANCEL;
   			}
   	   		
   			BioCollectionBean serailInventoryBio = (BioCollectionBean)detailListForm.getFocus();
   	   		 	
   	   		int serialcount = serailInventoryBio.size();
   	   			
   	   		if (inventoryQty.intValue() > serialcount ) {
   	   	   	 	widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
   	   		}
        } catch(Exception e) {
            
            // Handle Exceptions 
		    e.printStackTrace();
		    return RET_CANCEL;
		    
	    } 
	    
	    return RET_CONTINUE;
		
	}
}

