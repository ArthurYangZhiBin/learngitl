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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ASNWidgetPreRender extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private final String NORMAL = "changeEvent156";	
	private final String CASELEVELSSCC = "changeEvent155";	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNWidgetPreRender.class);

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
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate
		StateInterface state = context.getState();
		RuntimeFormWidgetInterface recType = context.getSourceWidget(); 
		RuntimeFormInterface form = context.getState().getCurrentRuntimeForm();
		RuntimeFormInterface tabGroupShellForm = (form.getParentForm(context.getState()));
		RuntimeFormInterface shellForm = tabGroupShellForm.getParentForm(context.getState());
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	//Get the form at slot2
		RuntimeFormInterface detailTab= null;										// this holds the toggle form slot content
		RuntimeFormInterface ASNDetail=null;
		RuntimeFormInterface lottableTab= null;
		RuntimeFormInterface udfnotes= null;
		SlotInterface detailTabGrpSlot = null;
		if (detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view")){	//if the slot is populated by toggle form then
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_receiptdetail_toggle");		
			detailTab = state.getRuntimeForm(toggleSlot, "wm_receiptdetail_detail_view");
			detailTabGrpSlot = detailTab.getSubSlot("tbgrp_slot");
		}else{
			detailTabGrpSlot = detailForm.getSubSlot("tbgrp_slot");
		}
		ASNDetail = context.getState().getRuntimeForm(detailTabGrpSlot, "tab 0");
		lottableTab = context.getState().getRuntimeForm(detailTabGrpSlot, "tab 1");
		udfnotes = context.getState().getRuntimeForm(detailTabGrpSlot, "tab 2");
		Object value = recType.getValue();
		if(value!=null){
			if(value.toString().equals("3")){
				
				UserException UsrExcp = new UserException("WMEXP_VALIDATETYPE_2", new Object[]{});
	 	   		throw UsrExcp;
				
			}
			_log.debug("LOG_SYSTEM_OUT","recType = "+value,100L);
			if (value.toString().equals("4")|| value.toString().equals("5")){
				context.setNavigation(CASELEVELSSCC);
			}
		}else{
			UserException UsrExcp = new UserException("WMEXP_VALIDATETYPE_1", new Object[]{});
 	   		throw UsrExcp;
		}

		result.setFocus(form.getFocus());	
		//Get all the forms
		return RET_CONTINUE;
   }
   
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

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
