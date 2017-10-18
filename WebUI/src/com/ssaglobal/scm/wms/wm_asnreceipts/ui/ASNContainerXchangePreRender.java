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

package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

/******************************************************************
 * Programmer		:       Seshabrahmam Yaddanapudi
 * Created			:       01/Dec/2009
 * Purpose	        :       Receipt Container Exchange prerender Extension to 
 * 							generate line number, make some fields readonly etc.
 * Release			:		3PL Enhancements - Container Exchange
 *******************************************************************
 * Modification History
 * 
 *******************************************************************/

// Import 3rd party packages and classes

// Import Epiphany packages and classes 
import java.text.DecimalFormat;
import java.util.TreeSet;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class ASNContainerXchangePreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

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
    	
    	StateInterface state = context.getState(); 
    	RuntimeFormWidgetInterface source = context.getSourceWidget();
    	
    	RuntimeFormInterface toolbar = source.getForm();
    	RuntimeFormInterface listForm = (RuntimeFormInterface)toolbar.getParentForm(state);
 	   	RuntimeFormInterface toggleForm = form.getParentForm(state);
 	   	RuntimeFormInterface tabGroupShellForm = toggleForm.getParentForm(state); 	
 	   	SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");
 	   	RuntimeFormInterface receiptHeaderForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
 	   	
 	   	DataBean headerFocus = receiptHeaderForm.getFocus();
 	   	DataBean detailFocus = form.getFocus();
 	   	DataBean detailListFocus = listForm.getFocus();

 	   	try {
 	   		
    	   if(detailFocus instanceof QBEBioBean )
     	   {
    		   if(source.getName().equalsIgnoreCase("NEW")){
    			       		   
	    		   QBEBioBean qbe = (QBEBioBean) detailFocus;
	    		   qbe.setValue("EXTERNRECEIPTKEY", headerFocus.getValue("EXTERNRECEIPTKEY"));
	    		   detailFocus.setValue("RECEIPTKEY", headerFocus.getValue("RECEIPTKEY"));
	    		   if(detailListFocus instanceof QBEBioBean)
	    		   {
	    			   qbe.setValue("RECEIPTXLINENUMBER", "00001");
	    			   return RET_CONTINUE;
	    		   }
	    		   BioCollectionBean bioFocus = (BioCollectionBean)detailListFocus;
	    		   Object max = null;
	    		   //Find max line number of list view
	    		   try {
						if(bioFocus.size()!=0){						
							long max1 = 0;
							TreeSet tree = new TreeSet();
							for(int i = 0; i < bioFocus.size(); i++){				
								Object bioFieldValueObj = bioFocus.elementAt(i).get("RECEIPTXLINENUMBER");
								try {
									long tempBioLongValue = Long.parseLong(bioFieldValueObj.toString());
									Long bioFieldValue = new Long(tempBioLongValue);
									tree.add(bioFieldValue);
								} catch (NumberFormatException e1) {					
								}
							}
							max1 = ((Long)tree.last()).longValue();						
							max = max1;
						}else{
							max = "0";
						}
					}catch(Exception e){
						e.printStackTrace();
						return RET_CANCEL;
					}			
				// Generate Number
				double size = Double.parseDouble(max.toString());
				size+=1;			
				String key = "";
				String zeroPadding=null;
				String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";			
				Query bioQuery = new Query("wm_system_settings",sQueryString,null);
				UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
				BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
				try {
					zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
				} catch (EpiDataException e1) {				
					e1.printStackTrace();
				}
				DecimalFormat template = null;
				String KEYTEMPLATE = "00000"; 
				if (zeroPadding.equalsIgnoreCase("0")){
					template = new DecimalFormat("0");
				}else{
					template = new DecimalFormat(KEYTEMPLATE);
				}
				if(size>99999){
					form.getFormWidgetByName("RECEIPTXLINENUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					throw new FormException("WMEXP_VALUE_GEN_FAIL", null);
				}else{
					key = template.format(size);
				}
				// Store value
				qbe.setValue("RECEIPTXLINENUMBER", key);
    		   }
			
     	   }
    	   else
    	   {
    		   form.getFormWidgetByName("CARTONIZATIONKEY").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
    		  form.getFormWidgetByName("CARTONIZATIONDESC").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
     		  form.getFormWidgetByName("QUALITY_IN").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
     		  form.getFormWidgetByName("QUALITY_OUT").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
     		  form.getFormWidgetByName("STORERKEY").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
     		   
     		  DataBean focus = form.getFocus();
     		  String cartonKey = focus.getValue("CARTONIZATIONKEY").toString();
     		  BioCollectionBean listCollection = null;
     		  String cartonDesc = "";
     		  try {
     			  String sQueryString = "(wm_cartonization.CARTONIZATIONKEY = '" + cartonKey + "')";    	   
	  	    	  Query BioQuery = new Query("wm_cartonization",sQueryString,null);
	  	    	  UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	  	          listCollection = uow.getBioCollectionBean(BioQuery);	  	
	  	          cartonDesc = listCollection.get("0").get("CARTONDESCRIPTION").toString();
	  	          
     		  } catch(EpiException e) {  	        
	  		        // Handle Exceptions 
	  			    e.printStackTrace();
	  			    return RET_CANCEL;  		    
     		  }
     		  form.getFormWidgetByName("CARTONIZATIONDESC").setDisplayValue(cartonDesc);     		  		   
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

