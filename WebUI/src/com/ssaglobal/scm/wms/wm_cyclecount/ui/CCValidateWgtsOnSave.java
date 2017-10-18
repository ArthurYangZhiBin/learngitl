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



package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// 16/Dec/2009 Seshu - A new class for 3PL Enhancements Catch Weight Cycle Count Changes
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Iterator;

import com.agileitp.forte.framework.DoubleNullable;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
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

public class CCValidateWgtsOnSave extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	   // 16/Dec/2009 Seshu - 3PL Enhancements Catch Weight Cycle Count Changes added
	   StateInterface state = context.getState();
	   RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	   RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	   RuntimeFormInterface detailForm=null;
	   SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");			
	   RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
		
		if(toggleForm.getName().equalsIgnoreCase("wm_cyclecount_detail_toggle"))
		{
			SlotInterface toggleSlot = toggleForm.getSubSlot("wm_cyclecount_detail_toggle"); 
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab"); 		
			
			for(Iterator itr = detailTab.getSubSlotsIterator(); itr.hasNext();){
				SlotInterface slot =(SlotInterface)itr.next();
				RuntimeFormInterface  form2 = state.getRuntimeForm(slot, null);				
				
				if(form2.getName().equalsIgnoreCase("wm_cyclecount_detail_view")){
					detailForm = form2;					
					break;
				}				
			}
		}
		else
			detailForm=toggleForm;
		
		if(detailForm == null)
			return RET_CONTINUE;
		
		DataBean detailFocus = detailForm.getFocus();
		String qty = detailForm.getFormWidgetByName("QTY").getValue().toString();
		String adjQty = detailForm.getFormWidgetByName("ADJQTY").getValue().toString();
		
		String actGWT = detailForm.getFormWidgetByName("ACTUALGROSSWGT").getValue().toString();
		String actNWT = detailForm.getFormWidgetByName("ACTUALNETWGT").getValue().toString();
		String actTWT = detailForm.getFormWidgetByName("ACTUALTAREWGT").getValue().toString();
		String adjGWT = detailForm.getFormWidgetByName("ADJGROSSWGT").getValue().toString();
		String adjNWT = detailForm.getFormWidgetByName("ADJNETWGT").getValue().toString();
		String adjTWT = detailForm.getFormWidgetByName("ADJTAREWGT").getValue().toString();
		
		double qtyCounted = new Double(qty).doubleValue();
		double adjustedQty = new Double(adjQty).doubleValue();
		
		double actualGWT = new Double(actGWT).doubleValue();
		double actualNWT = new Double(actNWT).doubleValue();
		double actualTWT = new Double(actTWT).doubleValue();
		double adjustedGWT = new Double(adjGWT).doubleValue();
		double adjustedNWT = new Double(adjNWT).doubleValue();
		double adjustedTWT = new Double(adjTWT).doubleValue();
		long gwt = Math.round(actualGWT);
        long nwt_twt_sum = Math.round(actualNWT + actualTWT);
		if(gwt == nwt_twt_sum)
		{
			if(!(adjustedGWT == (adjustedNWT + adjustedTWT)))
			{
				adjustedGWT = (actualGWT / qtyCounted) * adjustedQty;
				adjustedNWT = (actualNWT / qtyCounted) * adjustedQty;
				adjustedTWT = (actualTWT / qtyCounted) * adjustedQty;
				
				detailFocus.setValue("ADJGROSSWGT", new Double(adjustedGWT));
				detailFocus.setValue("ADJNETWGT", new Double(adjustedNWT));
				detailFocus.setValue("ADJTAREWGT", new Double(adjustedTWT));				
			}						
		}
		else
		{
			String[] parameters = new String[1];
			throw new UserException("WMEXP_CC_WGTS_EQU", parameters);
		}
	
      return super.execute( context, result );
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
