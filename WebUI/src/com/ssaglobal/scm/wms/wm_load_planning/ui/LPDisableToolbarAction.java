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


package com.ssaglobal.scm.wms.wm_load_planning.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class LPDisableToolbarAction extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPDisableToolbarAction.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {

	   
	   
		StateInterface state = context.getState();
	   	RuntimeFormInterface toolbar;
	   	
	   	//wm_load_planning_list_view Toolbar
	   	RuntimeFormInterface form = state.getCurrentRuntimeForm();
	   	_log.debug("LOG_SYSTEM_OUT","[LPDisableToolbarAction] form:"+form.getName(),100L);
	   	RuntimeFormInterface parent = form.getParentForm(state);
	   	_log.debug("LOG_SYSTEM_OUT","[LPDisableToolbarAction] parent:"+parent.getName(),100L);
	   	RuntimeFormInterface grandpa = parent.getParentForm(state);
	   	_log.debug("LOG_SYSTEM_OUT","[LPDisableToolbarAction] grandpa:"+grandpa.getName(),100L);
	   	
	   	toolbar = state.findForm(grandpa,"wm_load_planning_template Toolbar");
	   	_log.debug("LOG_SYSTEM_OUT","[LPDisableToolbarAction] toolbar:"+toolbar.getName(),100L);
	   	
	   	
	   	toolbar.getFormWidgetByName("SAVE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY,true);

	   	
	   	
	   	return RET_CONTINUE;
   }
 
}
