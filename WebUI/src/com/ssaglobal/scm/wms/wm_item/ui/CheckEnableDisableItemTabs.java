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

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;

/**
* This class used Enable Disable inbound receipt and outbound shipment 
* depending upon the AdvCatchWeight Enable or not
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class CheckEnableDisableItemTabs extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
//Added for 3PL Enhancements - Catch weights tracking
    /**
     * This class is used to Enable Disable Fields depending upon the item adv catch weight configuration 
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

       try {
    	   boolean isNewItem = false;
			String Storer = "";
			String Item  =  "";
			boolean advCatchWeightChecked=false;
 	   
            // retrieving state information
	   		StateInterface state = context.getState();
			String interationID = state.getInteractionId().toString();
	   		// retrieving session information
			HttpSession session = state.getRequest().getSession();
			DataBean focus = form.getFocus();
			if (focus instanceof BioBean) {
				focus = (BioBean) focus;
				Storer = focus.getValue("STORERKEY").toString();
				Item  =  focus.getValue("SKU").toString();
			} else if (focus instanceof QBEBioBean) {
				focus = (QBEBioBean) focus;
				isNewItem = true;
			}
			//3PL Enhancements -AdvCatchWeight Enable ord not -START
			//3PL Enhancements -retrieving enabledadv catchweight value
			Object cwFlag = focus.getValue("enableadvcwgt");
			
			if((isUnchecked(cwFlag))){
				advCatchWeightChecked = false;
			}else{
				advCatchWeightChecked = true;
			}
		
		   //"advCatchWeightSess"+Storer+Item+interationID
			//to handle the tab issue added storer,item and interaction id to the session variable
			if( (session.getAttribute("advCatchWeightSess"+Storer+Item+interationID)!=null) && (((String)session.getAttribute("advCatchWeightSess"+Storer+Item+interationID)).equalsIgnoreCase("enabled")))
			{advCatchWeightChecked =true;}
			if( (session.getAttribute("advCatchWeightSess"+Storer+Item+interationID)!=null) && (((String)session.getAttribute("advCatchWeightSess"+Storer+Item+interationID)).equalsIgnoreCase("disabled")))
			{advCatchWeightChecked =false;}
			
			
			if(advCatchWeightChecked)
			{	
				if(form.getName().equalsIgnoreCase("wm_item_weightdata_detail_view"))
				{
					//disabling inbound receipt form ICWFLAG
					SlotInterface inboundSlot=form.getSubSlot("inbound receipt");
					RuntimeNormalFormInterface inboundForm=(RuntimeNormalFormInterface) state.getRuntimeForm(inboundSlot,"wm_item_weightdata_inboundreceipts_detail_view");
					inboundForm.getFormWidgetByName("ICWFLAG").setDisplayValue("0");
					inboundForm.getFormWidgetByName("ICWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					//disabling outbound shipment form OCWFLAG
					SlotInterface outboundSlot=form.getSubSlot("outbound shipments");
					RuntimeNormalFormInterface outboundForm=(RuntimeNormalFormInterface) state.getRuntimeForm(outboundSlot,"wm_item_weightdata_outboundshipments_detail_view");
					outboundForm.getFormWidgetByName("OCWFLAG").setDisplayValue("0");
					outboundForm.getFormWidgetByName("OCWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				//The following if condition code commented by Prithvi - 3/22/2010 - We should never manipulate enableadvcwgt thru code like this.
				
				/*if(form.getName().equalsIgnoreCase("wm_item_advweight_tracking_detail_view"))
				{
					form.getFormWidgetByName("enableadvcwgt").setDisplayValue("1");
					focus.setValue("enableadvcwgt","1");
				}*/

				
			}	
			else
			{	
				//advcatch weight flag is disabled
				if(form.getName().equalsIgnoreCase("wm_item_weightdata_detail_view"))
				{
					//enabling inbound receipt form ICWFLAG
					SlotInterface inboundSlot=form.getSubSlot("inbound receipt");
					RuntimeNormalFormInterface inboundForm=(RuntimeNormalFormInterface) state.getRuntimeForm(inboundSlot,"wm_item_weightdata_inboundreceipts_detail_view");
					inboundForm.getFormWidgetByName("ICWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					//enabling outbound shipment form OCWFLAG
					SlotInterface outboundSlot=form.getSubSlot("outbound shipments");
					RuntimeNormalFormInterface outboundForm=(RuntimeNormalFormInterface) state.getRuntimeForm(outboundSlot,"wm_item_weightdata_outboundshipments_detail_view");					
					outboundForm.getFormWidgetByName("OCWFLAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
				//The following if condition code commented by Prithvi - 3/22/2010 - We should never manipulate enableadvcwgt thru code like this.
				/*if(form.getName().equalsIgnoreCase("wm_item_advweight_tracking_detail_view"))
				{
					form.getFormWidgetByName("enableadvcwgt").setDisplayValue("0");
					focus.setValue("enableadvcwgt","0");
				}*/
				
			}	
			
			
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
	/*
	 * This method is used to check whether check box is checked or not
	 * 
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
	 * This method is used to check whether attribute value empty or not
	 * 
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
     * Called in response to the pre-render event on a form in a modal window. Write code
     * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
     * to the end user
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the modifySubSlot event on a form. Write code
     * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
     * whether the form is re-displayed to the user or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }


    /**
     * Called in response to the modifySubSlot event on a form in a modal window. Write code
     * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
     * whether the form is re-displayed to the user or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the setFocusInForm event on a form. Write code
     * to change the focus of this form. This code is executed everytime irrespective of whether the form
     * is being redisplayed or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the setFocusInForm event on a form in a modal window. Write code
     * to change the focus of this form. This code is executed everytime irrespective of whether the form
     * is being redisplayed or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the pre-render event on a list form. Write code
     * to customize the properties of a list form dynamically, change the bio collection being displayed
     * in the form or filter the bio collection
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the pre-render event on a list form in a modal dialog. Write code
     * to customize the properties of a list form dynamically, change the bio collection being displayed
     * in the form or filter the bio collection
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
     * service information and modal dialog context
     * @param form the form that is about to be rendered
     */
    protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }


    /**
     * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
     * to customize the display values of a list form
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
     * to customize the display values of a list form
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
     * service information and modal dialog context
     * @param form the form that is about to be rendered
     */
    protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

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

