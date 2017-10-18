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

package com.ssaglobal.scm.wms.wm_inventory_move.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class InventoryMoveClearMovesAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryMoveClearMovesAction.class);
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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of InventoryMoveClearMovesAction", SuggestedCategory.NONE);

		StateInterface state = context.getState();

		RuntimeListFormInterface imListForm = (RuntimeListFormInterface) FormUtil.findForm(	state.getCurrentRuntimeForm(),
																							"wms_list_shell",
																							"wm_inventory_move__view",
																							state);
		String interactionID = state.getInteractionId();
		String contextVariableSuffix = "WINDOWSTART";
		String sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		String sessionObjectValue = (String)session.getAttribute(sessionVariable);
		int winStart = Integer.parseInt(sessionObjectValue);
		
		int winSize = imListForm.getWindowSize();
		BioCollectionBean bcb = (BioCollectionBean)imListForm.getFocus();
		int bcSize = bcb.size();
		int cycle = (bcSize-winStart)<winSize ? (bcSize-winStart) : winSize ;

		for(int index=0; index<cycle; index++)
		{
			BioBean selectedMove = (BioBean)bcb.elementAt(index+winStart);
			//This is a hack. Bugaware Issue 9386. Once you put the null in a required field, OA will freak out. 
			//So putting a space works
			//since this doesn't affect the cursor when the user clicks on To Location
			//and the location is validated by Execute Moves
			selectedMove.setValue("TOLOCATION", " ");
			selectedMove.setValue("TOLPN", null);
			selectedMove.setValue("TOPACK", null);
			selectedMove.setValue("TOUOM", null);
			selectedMove.setValue("QUANTITYTOMOVE", "0");			
		}

		imListForm.setSelectedItems(null);
		result.setFocus(imListForm.getFocus());
		return RET_CONTINUE;
	}
}
