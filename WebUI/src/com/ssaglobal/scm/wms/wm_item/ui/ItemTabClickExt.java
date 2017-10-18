/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
* This Extension is called when user clicks on item tab
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ItemTabClickExt extends com.epiphany.shr.ui.action.ActionExtensionBase {

//Added for WM 9 3PL Enhancements - Catch weights tracking - Sateesh Billa
   /**
    * This class enables disable inbound receipt ,outbound shipment forms
    * <P>
    * @param context The ActionContext for this extension
    * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
    *
    * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
    *
    * @exception EpiException
    */
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

       try {
           // Add your code here to process the event
	   		StateInterface state = context.getState();		
			HttpSession session = state.getRequest().getSession();
			

			DataBean focus = context.getState().getFocus();
			if(focus.isTempBio()){
				RuntimeFormInterface form = context.getSourceWidget().getForm();
				QBEBioBean qbe = (QBEBioBean)focus;
			
			boolean advCatchWeightChecked=false; 
			
			Object cwFlag = focus.getValue("enableadvcwgt");			
			if((isUnchecked(cwFlag))){
				advCatchWeightChecked = false;
			}else{
				advCatchWeightChecked = true;
			}
			
			if((session.getAttribute("advCatchWeightSess")!=null)&&(((String)session.getAttribute("advCatchWeightSess")).equalsIgnoreCase("enabled"))) advCatchWeightChecked =true;
			if((session.getAttribute("advCatchWeightSess")!=null)&&(((String)session.getAttribute("advCatchWeightSess")).equalsIgnoreCase("disabled"))) advCatchWeightChecked =false;
			
			
			/*
			if(advCatchWeightChecked) form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, true);
			else form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);
			*/

			if(advCatchWeightChecked)
			{	
				//advcatch weight checked/enabled
				
				if(form.getFormWidgetByName("ICWFLAG")!=null)
				{
				//disable the inbound receipt form	as adv catch weight is enable
				form.getFormWidgetByName("ICWFLAG").setValue("0");
				form.getFormWidgetByName("ICWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				if(form.getFormWidgetByName("OCWFLAG")!=null)
				{
				//disable the outbound shipment form as adv catch weight is enable	
				form.getFormWidgetByName("OCWFLAG").setValue("0");				
				form.getFormWidgetByName("OCWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
			}	
			else
			{	
				if(form.getFormWidgetByName("ICWFLAG")!=null)
				{
				//enable the inbound receipt form	as adv catch weight is disabled	
				form.getFormWidgetByName("ICWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
				if(form.getFormWidgetByName("OCWFLAG")!=null)
				{
				//enable the outbound shipment form	as adv catch weight is disabled	
				form.getFormWidgetByName("OCWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
			}	
			
			}	
			
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
   }
   
   /*
    * This method checks the given checkbox is checked or unchecked
    */
	public boolean isUnchecked(Object attributeValue) {
		if(!isEmpty(attributeValue)){
			if (attributeValue.toString().matches("[0Nn]")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
   /*
    * This method checks the attribute value empty or not
    */
	
	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
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
