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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;

public class BatchPickingUneditableAfterSave extends FormWidgetExtensionBase{
	private final static String OS_KEY = "ORDERSELECTIONKEY";
	
	public BatchPickingUneditableAfterSave(){
	}
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		try {
	        // Add your code here to process the event
			DataBean focus = state.getFocus();
			if(!(focus.isTempBio())){
				BioBean bio = (BioBean)focus;
				String value = (String)bio.get(OS_KEY);
				if(value.equalsIgnoreCase("")){
					//Hides lookups
					if(widget.getType().equals("image button")){
						widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
					}
					//Sets all other widgets to read only
					else{
						widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}					
				}
			}    
	    } 
		catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	    } 	
		return RET_CONTINUE;
	}
}