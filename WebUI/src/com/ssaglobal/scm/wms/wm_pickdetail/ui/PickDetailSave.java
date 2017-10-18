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

package com.ssaglobal.scm.wms.wm_pickdetail.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.internal.ServiceObjectException;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickDetailSave extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickDetailSave.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @throws WebuiException 
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException	{
		return save(context, result);
	}

	private int save(ActionContext context, ActionResult result) throws UserException	{
		DataBean listFormFocus = null;

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		//Save List
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");
		listFormFocus = state.getRuntimeForm(listSlot, null).getFocus();
		
//		BioCollectionBean listFormBean = null;
		if (listFormFocus.isBioCollection())		{
			try			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: "
						+ ((BioCollectionBean) listFormFocus).size(), SuggestedCategory.NONE);
			} catch (EpiDataException e)	{
				e.printStackTrace();
			}
//			listFormBean = (BioCollectionBean) listFormFocus;
		}

		// Save Detail
		try	{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n\n~!@#$%^&*()_+ Saving Detail", SuggestedCategory.NONE);
			SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
			RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
			_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);

			//Get TabGroup Slot
			SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");

			//Get Parent Focus
			RuntimeFormInterface parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + parentForm.getName(), SuggestedCategory.NONE);
			DataBean parentFocus = parentForm.getFocus();

			//				//Get Child Focus
			//				RuntimeFormInterface catchWeightForm = state.getRuntimeForm(tabGroupSlot, "tab 1");
			//				_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + catchWeightForm.getName(), SuggestedCategory.NONE);
			//				_log.debug("LOG_DEBUG_EXTENSION", "\nSubslots", SuggestedCategory.NONE);
			//				List subSlots = catchWeightForm.getSubSlots();
			//				for (Iterator it = subSlots.iterator(); it.hasNext();)
			//				{
			//					RuntimeSlot slot = (RuntimeSlot) it.next();
			//					_log.debug("LOG_DEBUG_EXTENSION", "1 " + slot.getName(), SuggestedCategory.NONE);
			//				}
			//				SlotInterface catchWeightToggleSlot = catchWeightForm.getSubSlot("wm_pickdetail_toggle_slot");
			//				DataBean catchWeightDetailFocus = null;
			//				try
			//				{
			//					RuntimeFormInterface catchWeightDetailForm = state.getRuntimeForm(catchWeightToggleSlot, "pickdetail_detail_view");
			//					catchWeightDetailFocus = catchWeightDetailForm.getFocus();
			//
			//				} catch (NullPointerException e)
			//				{
			//					_log.debug("LOG_DEBUG_EXTENSION", "!@# No CatchWeight Data " + e.getMessage(), SuggestedCategory.NONE);
			//
			//				}

			//Set Up Relations
//			BioBean parentBioBean = null;

			if (parentFocus.isTempBio()){
				//New Parent
//				try
//				{
//					parentBioBean = uow.getNewBio((QBEBioBean) parentFocus);
//				} catch (EpiException e)
//				{
//					e.printStackTrace();
//					_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
//				}
				_log.debug("LOG_DEBUG_EXTENSION", "!@# New Parent", SuggestedCategory.NONE);
				//					if (catchWeightDetailFocus != null)
				//					{
				//						//Add Link
				//						parentBioBean.addBioCollectionLink("LOTXIDDETAIL", (QBEBioBean) catchWeightDetailFocus);
				//					}
			}
			else if (parentFocus.isBio()){
				//Updating Parent
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Updating Parent", SuggestedCategory.NONE);
//				parentBioBean = (BioBean) parentFocus;

				//					if ((catchWeightDetailFocus != null))
				//					{
				//						if (catchWeightDetailFocus.isTempBio())
				//						{
				//							_log.debug("LOG_DEBUG_EXTENSION", "@@@@Inserting CatchWeight", SuggestedCategory.NONE);
				//							parentBioBean.addBioCollectionLink("LOTXIDDETAIL", (QBEBioBean) catchWeightDetailFocus);
				//						}
				//						else
				//						{
				//							_log.debug("LOG_DEBUG_EXTENSION", "@@@@Updating CatchWeight", SuggestedCategory.NONE);
				//						}
				//					}
				//					else
				//					{
				//						_log.debug("LOG_DEBUG_EXTENSION", "!@# Skipping CatchWeight", SuggestedCategory.NONE);
				//					}
			}else	{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Parent unknown", SuggestedCategory.NONE);
			}
		} catch (NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Caught " + e.getMessage() + " - No detail to save \n\n\n", SuggestedCategory.NONE);
		}
		try	{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Attempting to save", SuggestedCategory.NONE);
			uow.saveUOW(true);
			uow.clearState();
			//			uow.getNewBioCollection(arg0, arg1, arg2)
			uow.getUOW().close();
		} catch (UnitOfWorkException e)	{
			// Handle Exceptions

			Throwable nested = (e).findDeepestNestedException();
			_log.debug("LOG_DEBUG_EXTENSION", "Nested " + nested.getClass().getName(), SuggestedCategory.NONE);
			if (nested instanceof ServiceObjectException){
				Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
				_log.debug("LOG_DEBUG_EXTENSION", "Throwing Exception", SuggestedCategory.NONE);
				String exceptionMessage = nested.getMessage();
				Matcher matcher = errorPattern.matcher(exceptionMessage);
				if (matcher.find())	{
					exceptionMessage = matcher.group(1);
				}
				throw new UserException(exceptionMessage, new Object[] {});
			}
			return RET_CANCEL;
		} catch (EpiException e){
			e.printStackTrace();
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_PickDetailSave", e.toString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		} finally{
			result.setFocus(listFormFocus);
		}
		return RET_CONTINUE;
	}
}
