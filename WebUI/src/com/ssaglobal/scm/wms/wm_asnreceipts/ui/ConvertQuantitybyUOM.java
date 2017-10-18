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
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ConvertQuantitybyUOM extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertQuantitybyUOM.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
	   
	   
	   DataBean headerBean = context.getState().getCurrentRuntimeForm().getFocus();
	   
	   if (headerBean instanceof BioBean){

		   String PackKeyWidget = (String)getParameter("PACKKEY_WIDGET");
		   ArrayList ScreenwidgetNames = (ArrayList) getParameter("ScreenQtyWidgetName");
		   ArrayList BiowidgetNames = (ArrayList) getParameter("BioQtyWidgetName");

		   RuntimeFormInterface CurrentForm = context.getState().getCurrentRuntimeForm();
		   String packKey = CurrentForm.getFormWidgetByName(PackKeyWidget).getDisplayValue();
		   String uom = context.getSourceWidget().getValue().toString();
		   for(int i = 0; i < ScreenwidgetNames.size(); i++){
			   String qty = headerBean.getValue(BiowidgetNames.get(i).toString()).toString();
			   _log.debug("LOG_SYSTEM_OUT","Qty from the bio = "+ qty,100L);
//			   String Qty = CurrentForm.getFormWidgetByName(BiowidgetNames.get(i).toString()).getValue().toString();
//			   _log.debug("LOG_SYSTEM_OUT","Qty to convert = "+ Qty,100L);
			   if (! qty.equalsIgnoreCase("0")){
				   String ConvExpQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qty, packKey,context.getState(), UOMMappingUtil.uowNull, true); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				   CurrentForm.getFormWidgetByName(ScreenwidgetNames.get(i).toString()).setValue(ConvExpQty);
			   }
			}
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
