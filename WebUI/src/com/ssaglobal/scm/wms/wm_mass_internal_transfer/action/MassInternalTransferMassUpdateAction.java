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

package com.ssaglobal.scm.wms.wm_mass_internal_transfer.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Calendar;

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
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class MassInternalTransferMassUpdateAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	public static final String MASS_INTERNAL_TRANSFER_MASS_UPDATE = "MASS_INTERNAL_TRANSFER_MASS_UPDATE";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassInternalTransferMassUpdateAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException 
	 */

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		RuntimeListFormInterface imListForm;

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of InternalTransferMassMassUpdateAction", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "****** In InternalTransferMassMassUpdateAction execute !!!", SuggestedCategory.NONE);

		StateInterface state = context.getState();

		imListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_internal_transfer_mass_list_view", state);
		_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + imListForm.getName(), SuggestedCategory.NONE);

		BioCollectionBean listFocus = (BioCollectionBean) imListForm.getFocus();
		_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** BioCollectionBean ListFocus", SuggestedCategory.NONE);

		//  Mass Update based off lottable attribute updates on first row of filter result.  Updated value set for column / all rows.

		BioBean bio = listFocus.get("" + 0);
		_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** ListFocus.elementAt(0)", SuggestedCategory.NONE);

		String updated_storerkey = null;
		String updated_sku = null;
		String updated_lottable01 = null;
		String updated_lottable02 = null;
		String updated_lottable03 = null;
		Calendar updated_lottable04 = null;
		Calendar updated_lottable05 = null;
		String updated_lottable06 = null;
		String updated_lottable07 = null;
		String updated_lottable08 = null;
		String updated_lottable09 = null;
		String updated_lottable10 = null;
		Calendar updated_lottable11 = null;
		Calendar updated_lottable12 = null;

		if (bio.hasBeenUpdated("STORERKEY"))
		{
			updated_storerkey = bio.get("STORERKEY").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_storerkey = " + updated_storerkey, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("SKU"))
		{
			updated_sku = BioAttributeUtil.getStringNoNull(bio, "SKU");//bio.get("SKU").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_sku = " + updated_sku, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE01"))
		{
			updated_lottable01 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE01");//bio.get("LOTTABLE01").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable01 = " + updated_lottable01, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE02"))
		{
			updated_lottable02 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE02");//bio.get("LOTTABLE02").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable02 = " + updated_lottable02, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE03"))
		{
			updated_lottable03 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE03");//bio.get("LOTTABLE03").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable03 = " + updated_lottable03, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE04"))
		{
			updated_lottable04 = (Calendar) bio.get("LOTTABLE04");
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable04 = " + updated_lottable04, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE05"))
		{
			updated_lottable05 = (Calendar) bio.get("LOTTABLE05");
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable05 = " + updated_lottable05, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE06"))
		{
			updated_lottable06 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE06");//bio.get("LOTTABLE06").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable06 = " + updated_lottable06, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE07"))
		{
			updated_lottable07 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE07");//bio.get("LOTTABLE07").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable07 = " + updated_lottable07, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE08"))
		{
			updated_lottable08 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE08");//bio.get("LOTTABLE08").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable08 = " + updated_lottable08, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE09"))
		{
			updated_lottable09 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE09");//bio.get("LOTTABLE09").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable09 = " + updated_lottable09, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE10"))
		{
			updated_lottable10 = BioAttributeUtil.getStringNoNull(bio, "LOTTABLE10");//bio.get("LOTTABLE10").toString();
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable10 = " + updated_lottable10, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE11"))
		{
			updated_lottable11 = (Calendar) bio.get("LOTTABLE11");
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable11 = " + updated_lottable11, SuggestedCategory.NONE);
		}
		if (bio.hasBeenUpdated("LOTTABLE12"))
		{
			updated_lottable12 = (Calendar) bio.get("LOTTABLE12");
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bio.get updated_lottable12 = " + updated_lottable12, SuggestedCategory.NONE);
		}

		if (updated_storerkey == null && updated_sku == null && updated_lottable01 == null && updated_lottable02 == null && updated_lottable03 == null && updated_lottable04 == null
				&& updated_lottable05 == null && updated_lottable06 == null && updated_lottable07 == null && updated_lottable08 == null && updated_lottable09 == null && updated_lottable10 == null
				&& updated_lottable11 == null && updated_lottable12 == null)
		{

			//  No lottable attribute updates on first row, Return		

			//			userContext.put(contextVariable, "WMEXP_NONE_SELECTED");
			//			context.setNavigation("menuClickEvent583");

			return RET_CONTINUE;
		}

		//  Set MassUpdateAction data object and then set as Interaction Session Attribute.  
		//  This Session Attribute will be used by Finalize Transfers (InternalTransferMassExecuteTransfersAction)

		MassInternalTransferMassUpdateDataObject MassUpdateAction = new MassInternalTransferMassUpdateDataObject();

		MassUpdateAction.setStorerkey(updated_storerkey);
		MassUpdateAction.setSku(updated_sku);
		MassUpdateAction.setLottable01(updated_lottable01);
		MassUpdateAction.setLottable02(updated_lottable02);
		MassUpdateAction.setLottable03(updated_lottable03);
		MassUpdateAction.setLottable04(updated_lottable04);
		MassUpdateAction.setLottable05(updated_lottable05);
		MassUpdateAction.setLottable06(updated_lottable06);
		MassUpdateAction.setLottable07(updated_lottable07);
		MassUpdateAction.setLottable08(updated_lottable08);
		MassUpdateAction.setLottable09(updated_lottable09);
		MassUpdateAction.setLottable10(updated_lottable10);
		MassUpdateAction.setLottable11(updated_lottable11);
		MassUpdateAction.setLottable12(updated_lottable12);
		MassUpdateAction.setMassUpdateProcessed("1");

		SessionUtil.setInteractionSessionAttribute(MASS_INTERNAL_TRANSFER_MASS_UPDATE, MassUpdateAction, state);

		// Loop through ListFocus ... If lottable attribute of first row has been updated, set biobean.
		// Updated value set for column / all rows.

		for (int i = 0; i < listFocus.size(); i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** In for loop ListFocus.size", SuggestedCategory.NONE);
			BioBean bioelement = listFocus.get("" + i);
			_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement = ListFocus.elementAt", SuggestedCategory.NONE);
			if (updated_storerkey != null)
			{
				bioelement.setValue("STORERKEY", updated_storerkey);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set STORERKEY = " + updated_storerkey, SuggestedCategory.NONE);
			}
			if (updated_sku != null)
			{
				bioelement.setValue("SKU", updated_sku);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set SKU = " + updated_sku, SuggestedCategory.NONE);
			}
			if (updated_lottable01 != null)
			{
				bioelement.setValue("LOTTABLE01", updated_lottable01);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE01 = " + updated_lottable01, SuggestedCategory.NONE);
			}
			if (updated_lottable02 != null)
			{
				bioelement.setValue("LOTTABLE02", updated_lottable02);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE02 = " + updated_lottable02, SuggestedCategory.NONE);
			}
			if (updated_lottable03 != null)
			{
				bioelement.setValue("LOTTABLE03", updated_lottable03);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE03 = " + updated_lottable03, SuggestedCategory.NONE);
			}
			if (updated_lottable04 != null)
			{
				bioelement.setValue("LOTTABLE04", updated_lottable04);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE04 = " + updated_lottable04, SuggestedCategory.NONE);
			}
			if (updated_lottable05 != null)
			{
				bioelement.setValue("LOTTABLE05", updated_lottable05);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE05 = " + updated_lottable05, SuggestedCategory.NONE);
			}
			if (updated_lottable06 != null)
			{
				bioelement.setValue("LOTTABLE06", updated_lottable06);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE06 = " + updated_lottable06, SuggestedCategory.NONE);
			}
			if (updated_lottable07 != null)
			{
				bioelement.setValue("LOTTABLE07", updated_lottable07);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE07 = " + updated_lottable07, SuggestedCategory.NONE);
			}
			if (updated_lottable08 != null)
			{
				bioelement.setValue("LOTTABLE08", updated_lottable08);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE08 = " + updated_lottable08, SuggestedCategory.NONE);
			}
			if (updated_lottable09 != null)
			{
				bioelement.setValue("LOTTABLE09", updated_lottable09);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE09 = " + updated_lottable09, SuggestedCategory.NONE);
			}
			if (updated_lottable10 != null)
			{
				bioelement.setValue("LOTTABLE10", updated_lottable10);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE10 = " + updated_lottable10, SuggestedCategory.NONE);
			}
			if (updated_lottable11 != null)
			{
				bioelement.setValue("LOTTABLE11", updated_lottable11);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE11 = " + updated_lottable11, SuggestedCategory.NONE);
			}
			if (updated_lottable12 != null)
			{
				bioelement.setValue("LOTTABLE12", updated_lottable12);
				_log.debug("LOG_DEBUG_EXTENSION_MassInternalTransferMassUpdateAction_execute", "**** bioelement.set LOTTABLE12 = " + updated_lottable12, SuggestedCategory.NONE);
			}
			bioelement.save();

		}

		imListForm.setFocus(listFocus);

		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue)
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

}
