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


package com.ssaglobal.scm.wms.util.upload;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_item.ui.AddUpdateAtEnterprise;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class HideAtEnterprise extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(HideAtEnterprise.class);
	
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
			EpnyControllerState state = (EpnyControllerState) context.getState();
			HttpSession session = state.getRequest().getSession();

			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

			String isEnterprise = null;
			//Is userContext null?

			try
			{
				isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
				_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility: " + isEnterprise, SuggestedCategory.NONE);
			} catch (java.lang.NullPointerException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n-Getting Facility from Session Instead", SuggestedCategory.NONE);
				isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
				_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility Session: " + isEnterprise, SuggestedCategory.NONE);
			}

			if (isEnterprise.equals("1"))
			{
				//disable Toolbar widgets for Enterprise
				handleEnterprisePermissions(context, form);
			}
			else
			{
				//enable/disable widgets/forms for Warehouse
				handleWarehousePermissions(context, form);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

       return RET_CONTINUE;
    }

    private void handleEnterprisePermissions(UIRenderContext context, RuntimeNormalFormInterface form){
    	List buttonList = (ArrayList)getParameter("ButtonList");
    	for(Iterator itr = buttonList.iterator(); itr.hasNext();){
    		String buttonName = (String)itr.next();
    		if (buttonName!=null){
    			RuntimeFormWidgetInterface button = form.getFormWidgetByName(buttonName);
    			if (button!=null)
    				button.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
    		}
    	}
    }

    private void handleWarehousePermissions(UIRenderContext context, RuntimeNormalFormInterface form){
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

