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

package com.ssaglobal.scm.wms.wm_inventory_holds.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class EditableHeaderOnlySave extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EditableHeaderOnlySave.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws UserException 
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{

		DataBean listFormFocus = null;

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//Save List
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);

		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface tempHeaderForm = state.getRuntimeForm(listSlot, null);

		RuntimeListFormInterface listForm = null;
		if (tempHeaderForm.isListForm())
		{

			listForm = (RuntimeListFormInterface) tempHeaderForm;
		}

		listFormFocus = listForm.getFocus();
		if (listFormFocus.isBioCollection())
		{
			//			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: "
			//					+ ((BioCollectionBean) listFormFocus).size(), SuggestedCategory.NONE);
			BioCollectionBean listFormBean = (BioCollectionBean) listFormFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Attempting to save", SuggestedCategory.NONE);
			try
			{
				uow.saveUOW(true);
				uow.clearState();
			} catch (UnitOfWorkException ex)
			{
				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				if (nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();
					throw new UserException(reasonCode, new Object[] {});
				}

			} catch (EpiDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			} catch (EpiException e)
			{
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", e.getStackTraceAsString(), SuggestedCategory.NONE);

				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}

		}

		// Save Detail - Is this even needed?

		_log.debug("LOG_DEBUG_EXTENSION", "Saving Detail", SuggestedCategory.NONE);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);
		if (detailForm.getName().equals("Blank"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_EditableHeaderOnlySave", "No Detail to save", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		DataBean detailFormFocus = detailForm.getFocus();

		if (detailFormFocus.isBio())
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Updating an old record", SuggestedCategory.NONE);
		}
		else if (detailFormFocus.isTempBio())
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Inserting a new record", SuggestedCategory.NONE);
			try
			{
				uow.saveUOW(true);
			} catch (UnitOfWorkException ex)
			{
				ex.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", ex.getStackTraceAsString(), SuggestedCategory.NONE);
				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", "\tNested " + nested.getClass().getName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", "\tMessage " + nested.getMessage(), SuggestedCategory.NONE);

				if (nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();
					throw new UserException(reasonCode, new Object[] {});

				}
				else
				{
					throwUserException(ex, "ERROR_SAVING_BIO", null);
				}

			} catch (EpiException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_EditableHeaderOnlySave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			uow.clearState();
		}
		

		result.setFocus(listFormFocus);
		return RET_CONTINUE;

	}

}
