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

package com.ssaglobal.scm.wms.wm_maintain_replenishments.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class MaintainReplenishmentsDeleteAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{


		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																							state.getCurrentRuntimeForm(),
																							"wms_list_shell",
																							"wm_maintain_replenishment_list_view",
																							state);
		ArrayList items = listForm.getAllSelectedItems();
		if (isZero(items))
		{
			
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		
		
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		

		try
		{
			StateInterface state = ctx.getState();
			state.closeModal(true);
			//Delete is handled by ListDeleteAction

			//			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			//			_log.debug("LOG_SYSTEM_OUT","Modal Form " + modalForm.getName(),100L);
			//			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			//			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			//			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			//			RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);
			//			_log.debug("LOG_SYSTEM_OUT","Header Form " + listForm.getName(),100L);
			//			
			//			ArrayList items = listForm.getAllSelectedItems();
			//			for (Iterator it = items.iterator(); it.hasNext();)
			//			{
			//
			//				BioBean selectedRecord = (BioBean) it.next();
			//				selectedRecord.delete();
			//			}
			//			UnitOfWorkBean uow = state.getTempUnitOfWork();
			//			uow.saveUOW(true);
			//			uow.clearState();
			//			args.setFocus(listForm.getFocus());
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	boolean isZero(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (((ArrayList) attributeValue).size() == 0)
		{
			return false;
		}
		else
		{
			return false;
		}
	}
}
