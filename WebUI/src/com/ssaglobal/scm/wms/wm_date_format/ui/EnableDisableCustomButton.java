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
 

package com.ssaglobal.scm.wms.wm_date_format.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class EnableDisableCustomButton extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EnableDisableCustomButton.class);
	public EnableDisableCustomButton()
    {
    }

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
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		_log.debug("LOG_SYSTEM_OUT",form.getName(),100L);
		
		setDetailEnableDisableProperties(state, form);

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
   		_log.debug("LOG_SYSTEM_OUT"," ******  EnableDisable 2 ",100L);
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }



public static int setDetailEnableDisableProperties( StateInterface state, RuntimeFormInterface form ) throws EpiException {

	RuntimeFormWidgetInterface dateElement = form.getFormWidgetByName("DATEELEMENT");

	String dateElementString = " ";
	if (dateElement.getDisplayValue() != null)
	{
		dateElementString = dateElement.getValue().toString();
		
		if ( dateElementString.compareTo("N") == 0 )
		{
			// if the dateElementString is "NONE", disable the date type dropdown and the custom button,
			// and default the how-to-process dropdown to "V"alidate
			form.getFormWidgetByName("DATETYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			form.getFormWidgetByName("CUSTOM").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			form.getFocus().setValue("DATETYPE", null); // set this field to null; using blank has an inconsistent appearance
			form.getFocus().setValue("HOWTOPROCESS", "V");
		}
		else
		{
			// if dateElementString is either M,D,Y then enable the date type dropdown and the custom button
			// and default the how-to-process dropdown to "C"onversion
			form.getFormWidgetByName("DATETYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			form.getFormWidgetByName("HOWTOPROCESS").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			form.getFocus().setValue("HOWTOPROCESS", "C");
			if ( dateElementString.compareTo("D") == 0 || dateElementString.compareTo("Y") == 0 )
			{
				// for day/year values, set the field type to Numeric and disable the field
				form.getFocus().setValue("FIELDTYPE", "N");
				form.getFormWidgetByName("FIELDTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}
			else
			{
				// for month values, enable the field type so the user can set it to "C"ustom if needed
				form.getFormWidgetByName("FIELDTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
		}
	}
	
	RuntimeFormWidgetInterface dateType = form.getFormWidgetByName("DATETYPE");
	RuntimeFormWidgetInterface fieldType = form.getFormWidgetByName("FIELDTYPE");
	_log.debug("LOG_SYSTEM_OUT","date type is :" +dateType.getDisplayValue()+":",100L);
	
	String dateTypeString = " ";
	if (dateType.getDisplayValue() != null)
		dateTypeString = dateType.getValue().toString();

	String fieldTypeString = " ";
	if (fieldType.getDisplayValue() != null)
		fieldTypeString = fieldType.getValue().toString();
	
	if ( dateElementString.compareTo("N") != 0 ) // date element not set to NONE
	{
		if ( fieldTypeString.compareTo("C") == 0 ) // field type set to Character
		{
			if ( dateTypeString != null && dateTypeString.compareTo("C") == 0 )
			{
				// enable the custom button when the field type is "C"haracter and the datetype is set to "C"ustom
				form.getFormWidgetByName("CUSTOM").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				_log.debug("LOG_SYSTEM_OUT"," ******  EnableDisable ENABLE :" + dateTypeString+":",100L);
			}
			else
			{
				// disable the custom button
				form.getFormWidgetByName("CUSTOM").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				_log.debug("LOG_SYSTEM_OUT"," ******  EnableDisable DISABLE :"+ dateTypeString+":",100L);
			}
		}
		else
		{
			// field type is not "C"haracter, disable the custom button
			form.getFormWidgetByName("CUSTOM").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			_log.debug("LOG_SYSTEM_OUT"," ******  EnableDisable DISABLE :"+ dateTypeString+":",100L);
		}
	}
	return RET_CONTINUE;
	}
}

