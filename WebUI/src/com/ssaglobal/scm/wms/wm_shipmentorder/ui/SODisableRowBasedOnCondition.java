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
 

package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class SODisableRowBasedOnCondition extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

    /**
     * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
     * to customize the display values of a list form
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    @Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++) {
				RuntimeFormWidgetInterface status = listRows[i].getFormWidgetByName("STATUS");
				RuntimeFormWidgetInterface type = listRows[i].getFormWidgetByName("TYPE");
				RuntimeFormWidgetInterface priority = listRows[i].getFormWidgetByName("PRIORITY");

				RuntimeFormWidgetInterface statusV = listRows[i].getFormWidgetByName("STATUSVALUE");
				RuntimeFormWidgetInterface typeV = listRows[i].getFormWidgetByName("TYPEVALUE");
				// if status = 95 disable priority type
				// if type = 20 disable priority type

				String statusValue = statusV.getDisplayValue();
				String typeValue = typeV.getDisplayValue();
				
				if ("95".equals(statusValue) || "20".equals(typeValue)) {
					type.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					priority.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}
			}
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}

