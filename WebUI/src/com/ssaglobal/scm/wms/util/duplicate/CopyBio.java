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


package com.ssaglobal.scm.wms.util.duplicate;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CopyBio extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
   protected static ILoggerCategory _log = LoggerFactory.getInstance(CopyBio.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

	   RuntimeFormInterface sourceForm = context.getSourceWidget().getForm();
	   //_log.debug("LOG_SYSTEM_OUT","jpuente The form with the Duplicate wigdet is:"+sourceForm.getName(),100L);
	   RuntimeFormInterface tabGroupShellForm = (sourceForm.getParentForm(context.getState()));
	   SlotInterface headerSlot = tabGroupShellForm.getSubSlot("list_slot_1");		//JP
	   RuntimeFormInterface headerForm = context.getState().getRuntimeForm(headerSlot, null);
	   //_log.debug("LOG_SYSTEM_OUT","jpuente Header Form = "+ headerForm.getName(),100L);
		  
	   SlotInterface detailSlot = tabGroupShellForm.getSubSlot("list_slot_2");		//JP
	   RuntimeFormInterface detailForm = context.getState().getRuntimeForm(detailSlot, null);
	   if (detailForm.getName().equalsIgnoreCase("Blank")){
		   throw new FormException("WMEXP_NO_SAVE_AVAILABLE",null);
	   }
		  
	   _log.debug("LOG_SYSTEM_OUT","jpuente detailForm = "+ detailForm.getName(),100L);
		  
		  
	   //Saving the DataBean in the session object
	   DataBean dupDataBean = detailForm.getFocus();
	   //String message = (String) locationDataBean.getValue("loc");
	   //_log.debug("LOG_SYSTEM_OUT","jpuente The Bean loc:"+message,100L);
		  /**
	   PropertyInfo x[];
	   x= dupDataBean.getPropertyInfos();
	   int size =x.length;
	   for (int i=0; i<size; i++){
		   System.out.println("Property name:"+x[i])
	   }
	   **/
	   StateInterface state= context.getState();
	   HttpSession session = state.getRequest().getSession();
	   session.setAttribute("dupDataBean",dupDataBean);
		  
	   return RET_CONTINUE;
	   //return super.execute( context, result );
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
