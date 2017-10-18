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


package com.ssaglobal.scm.wms.wm_inventory_move.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class InventoryMoveCalculateSerials extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		return RET_CONTINUE;
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
    protected int execute(ModalActionContext context, ActionResult args) throws EpiException 
    {
		try
		{			
			StateInterface state = context.getState();
			RuntimeListFormInterface serialListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_inventory_move_serial_popup_view", "wm_inventory_move_serial_detail_view", state);
			ArrayList selectedSerials = serialListForm.getAllSelectedItems();
			HttpSession session = context.getState().getRequest().getSession();
			if (selectedSerials != null)
			{				
				session.setAttribute( "InventoryMove_QTYSELECTED", Integer.toString(selectedSerials.size()) );
			}
			else
			{
				session.setAttribute("InventoryMove_QTYSELECTED", "0");
			}
		}
		catch ( Exception exc )
		{
			exc.printStackTrace();
			return RET_CANCEL;
		}
		
		return RET_CONTINUE;
    }
}
