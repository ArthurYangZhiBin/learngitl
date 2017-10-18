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


package com.ssaglobal.scm.wms.wm_physical_parameters.ui;


import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class SetCompleteFlag extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetCompleteFlag.class);
	
	public static final String CLEAR_PHYSICAL_COMPLETE = "CLEAR_PHYSICAL_COMPLETE"; 

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

    	_log.debug("LOG_EXTENSION", "jpdebug:SetCompleteFlag:START", 100L);
    	
       try {
    	   StateInterface state = ctx.getState();
    	   HttpSession session = state.getRequest().getSession();
           session.setAttribute(CLEAR_PHYSICAL_COMPLETE, new Boolean(true));
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
