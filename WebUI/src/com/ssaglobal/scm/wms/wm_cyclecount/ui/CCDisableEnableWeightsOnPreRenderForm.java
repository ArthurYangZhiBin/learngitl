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
// 16/Dec/2009 Seshu - 3PL Enhancements Catch Weight Cycle Count Changes 
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class CCDisableEnableWeightsOnPreRenderForm extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

    /**
     * Called in response to the pre-render event on a form. Write code
     * to customize the properties of a form. All code that initializes the properties of a form that is
     * being displayed to a user for the first time belong here. This is not executed even if the form
     * is re-displayed to the end user on subsequent actions.
     *
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

    	try {
	            // Add your code here to process the event
    		// 16/Dec/2009 Seshu - A new class added for 3PL Enhancements Catch Weight Cycle Count Changes added
	     	   DataBean focus = form.getFocus();
	     	   
	     	   if(focus instanceof QBEBioBean)
	     	   {
	     		   form.getFormWidgetByName("ACTUALGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	     		   form.getFormWidgetByName("ACTUALNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	     		   form.getFormWidgetByName("ACTUALTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	     		   form.getFormWidgetByName("ADJGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	     		   form.getFormWidgetByName("ADJNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	     		   form.getFormWidgetByName("ADJTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);  	
	     		   return RET_CONTINUE;
	     	   }
	     	   
	     	   String sku = focus.getValue("SKU").toString();
	     	   String owner = focus.getValue("STORERKEY").toString();
	     	   String query = "SELECT IBSUMCWFLG FROM SKU WHERE STORERKEY = '" + owner + "' and SKU = '" + sku + "'";   		
			   EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			   
			   DataValue val=results.getAttribValue(new TextData("IBSUMCWFLG"));   		   
			   String status = focus.getValue("STATUS").toString();
			   
			   if(val==null || val.toString().equalsIgnoreCase("0"))
			   {
				   	form.getFormWidgetByName("ACTUALGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	 	   			form.getFormWidgetByName("ACTUALNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	 	   			form.getFormWidgetByName("ACTUALTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	 	   			form.getFormWidgetByName("ADJGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	 	   			form.getFormWidgetByName("ADJNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
	 	   			form.getFormWidgetByName("ADJTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);  			   
			   }
			   if(status.equalsIgnoreCase("9"))
			   {
				   form.getFormWidgetByName("ACTUALGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	 	   			form.getFormWidgetByName("ACTUALNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	 	   			form.getFormWidgetByName("ACTUALTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	 	   			form.getFormWidgetByName("ADJGROSSWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	 	   			form.getFormWidgetByName("ADJNETWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	 	   			form.getFormWidgetByName("ADJTAREWGT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			   }
     	   
         } catch(Exception e) {
             
             // Handle Exceptions 
           e.printStackTrace();
           return RET_CANCEL;          
        } 
        
        return RET_CONTINUE;
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

