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


package com.ssaglobal.scm.wms.wm_adjustment.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CheckLPN extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
   protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckLPN.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate


		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		
		//RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface shellForm =FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_list_shell", state);
		//_log.debug("LOG_SYSTEM_OUT","[CheckLPN]:shellForm:"+shellForm.getName(),100L);
		//_log.debug("LOG_SYSTEM_OUT","[CheckLPN]:shellFormParent:"+shellForm.getParentForm(state).getName(),100L);
		
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		DataBean headerFocus = headerForm.getFocus();
		RuntimeFormInterface detailToggle = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);
		RuntimeFormInterface detailForm = null;
		
		if(headerFocus.isTempBio()){
			detailForm = detailToggle;
		}else{
			int active = state.getSelectedFormNumber(detailToggle.getSubSlot("wm_adjustmentdetail_toggle_slot"));
			if(active==0){
				context.getSourceWidget().setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				throw new FormException("WMEXP_NO_SAVE_AVAILABLE", null);
			}
			detailForm = state.getRuntimeForm(detailToggle.getSubSlot("wm_adjustmentdetail_toggle_slot"), "Detail");

		}
		//Verify lot, loc, and id
		String lot = detailForm.getFormWidgetByName("LOT").getDisplayValue();
		String loc = detailForm.getFormWidgetByName("LOC").getDisplayValue();
		String parameters[]= new String[1];
		String table = "wm_lotxlocxid";
		String id = detailForm.getFormWidgetByName("ID").getDisplayValue();
		if(id==null){
			id="";
		}
		String queryString = table+".LOT='"+lot+"' and "+table+".LOC='"+loc+"' and "+table+".ID='"+id+"'";
		Query qry = new Query(table, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if(list.size()<1){
			if(id.equals("")){
				id=null;
			}
			parameters[0]=lot+", "+loc+", "+id;
			
			//jpuente Begin
			_log.debug("LOG_SYSTEM_OUT","[CheckLPN]:clickEvent216",100L);
			   HttpSession session = state.getRequest().getSession();
			   session.setAttribute("SAVELPN","N");
			   context.setNavigation("changeEvent216");
			   return RET_CONTINUE;


			//jpuente End

		
			
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
