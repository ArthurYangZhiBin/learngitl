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



package com.ssaglobal.scm.wms.wm_pickdetail.ui;
// 16/Dec/2009: Seshu - A new class 3PL Enhancements Catch Weight Outbound Changes added
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CalcPickDetailWgtsOnQtyChange extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	   // 16/Dec/2009: Seshu - 3PL Enhancements Catch Weight Outbound Changes added
	   DataBean focus=context.getSourceWidget().getForm().getFocus();
	   
	   String qtyCounted = focus.getValue("QTY").toString();
	   double qty= new Double(qtyCounted).doubleValue();
	   
	   String sku = focus.getValue("SKU").toString();
	   String owner = focus.getValue("STORERKEY").toString();
	   
	   Object lotObj = focus.getValue("LOT");
	   Object idObj = focus.getValue("ID");
	   String loc = focus.getValue("LOC").toString();
	   String lot = "";
	   String id = "";   
	   
	   if(lotObj !=null && lotObj.toString().trim().length() !=0)
		   lot = lotObj.toString();
	   if(idObj !=null && idObj.toString().trim().length() !=0)
		   id = idObj.toString();   
	   
	   CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
	   String enableAdvCatchWgt = helper.isAdvCatchWeightEnabled(owner, sku);	   
	   
	   if(enableAdvCatchWgt == null || enableAdvCatchWgt.equalsIgnoreCase("0"))
	   {		   
		   return RET_CONTINUE;
	   }
	   HashMap actualWgts = new HashMap();
	   actualWgts = helper.getCalculatedWeightsLPN(owner, sku, loc, lot, id, qty);
	   
	   Double actualGWT = (Double)actualWgts.get("GROSSWEIGHT");
	   Double actualNWT = (Double)actualWgts.get("NETWEIGHT");
	   Double actualTWT = (Double)actualWgts.get("TAREWEIGHT");
	   
	   focus.setValue("GROSSWGT", actualGWT);
	   focus.setValue("NETWGT", actualNWT);
	   focus.setValue("TAREWGT", actualTWT);   
	   
	   result.setFocus(focus);
	   
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
