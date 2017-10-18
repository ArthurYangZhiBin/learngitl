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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class MassInternalTransferExecuteTransfersAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassInternalTransferExecuteTransfersAction.class);

	String contextVariable;

	String contextVariableArgs;

	private boolean buildTransferArray(final ActionContext context, final EpnyUserContext userContext, final RuntimeListFormInterface imListForm, final ArrayList<MassInternalTransferTempDataObject> ArrayTransfers) throws UserException, EpiDataInvalidAttrException
	{
		final StateInterface state = context.getState();
		LocaleInterface locale = state.getLocale();
		//Selected BioRefs from current list view
		final ArrayList<BioBean> selectedTransfers = imListForm.getAllSelectedItems();
		if (isNull(selectedTransfers))
		{
			//show error
			userContext.put(contextVariable, "WMEXP_NONE_SELECTED");
			context.setNavigation("msgNavigation");
			return false;
		}
		_log.debug("LOG_DEBUG_EXTENSION", "****** selectTransfers = " + selectedTransfers.size(), SuggestedCategory.NONE);

		final MassInternalTransferMassUpdateDataObject MassUpdateAction = (MassInternalTransferMassUpdateDataObject) SessionUtil.getInteractionSessionAttribute(
				MassInternalTransferMassUpdateAction.MASS_INTERNAL_TRANSFER_MASS_UPDATE, state);
		SessionUtil.setInteractionSessionAttribute(MassInternalTransferMassUpdateAction.MASS_INTERNAL_TRANSFER_MASS_UPDATE, null, state);

		BioBean selectedTransfer;
		for (final Iterator<BioBean> it = selectedTransfers.iterator(); it.hasNext();)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "****** in the for loop  ", SuggestedCategory.NONE);
			selectedTransfer = it.next();

			_log.debug("LOG_DEBUG_EXTENSION", "###! ID " + selectedTransfer.getValue("ID"), SuggestedCategory.NONE);

			final MassInternalTransferTempDataObject transfer = new MassInternalTransferTempDataObject();

			String ownerLabel = imListForm.getFormWidgetByName("STORERKEY").getLabel("label", locale);
			if (isValidOwner(transfer, BioAttributeUtil.getString(selectedTransfer, "STORERKEY"), ownerLabel, imListForm.getFocus(),
					userContext, context) == false)
			{
				//show error
				return false;
			}

			String itemLabel = imListForm.getFormWidgetByName("SKU").getLabel("label", locale);
			if (isValidSKU(transfer, BioAttributeUtil.getString(selectedTransfer, "SKU"), itemLabel, imListForm.getFocus(), userContext, context) == false)
			{
				//show error
				return false;
			}

			if (isValidItemOwnerComb(transfer, selectedTransfer.getValue("SKU").toString(), selectedTransfer.getValue("STORERKEY").toString(), itemLabel, ownerLabel, userContext, context) == false)
			{
				//show error
				return false;
			}

			transfer.setFromstorerkey(selectedTransfer.getValue("STORERKEY", true) == null ? null : selectedTransfer.getValue("STORERKEY", true).toString().toUpperCase());
			transfer.setFromsku(selectedTransfer.getValue("SKU", true) == null ? null : selectedTransfer.getValue("SKU", true).toString().toUpperCase());
			transfer.setFrompackkey(getPack(transfer.getFromstorerkey(), transfer.getFromsku(), state));

			transfer.setId(selectedTransfer.getValue("ID") == null ? null : selectedTransfer.getValue("ID").toString().toUpperCase());
			transfer.setLot(selectedTransfer.getValue("LOT") == null ? null : selectedTransfer.getValue("LOT").toString().toUpperCase());
			transfer.setLoc(selectedTransfer.getValue("LOC") == null ? null : selectedTransfer.getValue("LOC").toString().toUpperCase());
			transfer.setQty(selectedTransfer.getValue("QTY") == null ? null : selectedTransfer.getValue("QTY"));

			// On Mass Update, MassUpdateAction Data Object contains lottable attributes and values selected for Mass Update.  
			// Lottable attributes not selected = null and should be set to list form values.

			boolean updated = false;

			if (MassUpdateAction != null)
			{
				if (MassUpdateAction.getMassUpdateProcessed() != null)
				{

					if (MassUpdateAction.getStorerkey() != null)
					{
						transfer.setTostorerkey(MassUpdateAction.getStorerkey());
						if (selectedTransfer.hasBeenUpdated("STORERKEY"))
						{
							transfer.setTostorerkey(selectedTransfer.getValue("STORERKEY") == null ? null : selectedTransfer.getValue("STORERKEY").toString().toUpperCase());
						}
					}
					else
					{
						transfer.setTostorerkey(selectedTransfer.getValue("STORERKEY") == null ? null : selectedTransfer.getValue("STORERKEY").toString().toUpperCase());
					}

					if (MassUpdateAction.getSku() != null)
					{
						transfer.setTosku(MassUpdateAction.getSku());
						if (selectedTransfer.hasBeenUpdated("SKU"))
						{
							transfer.setTosku(selectedTransfer.getValue("SKU") == null ? null : selectedTransfer.getValue("SKU").toString().toUpperCase());
						}
					}
					else
					{
						transfer.setTosku(selectedTransfer.getValue("SKU") == null ? null : selectedTransfer.getValue("SKU").toString().toUpperCase());
					}

					if (MassUpdateAction.getLottable01() != null)
					{
						transfer.setLottable01(MassUpdateAction.getLottable01());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE01"))
						{
							transfer.setLottable01(selectedTransfer.getValue("LOTTABLE01") == null ? null : selectedTransfer.getValue("LOTTABLE01").toString());
						}
					}
					else
					{
						transfer.setLottable01(selectedTransfer.getValue("LOTTABLE01") == null ? null : selectedTransfer.getValue("LOTTABLE01").toString());
					}

					if (MassUpdateAction.getLottable02() != null)
					{
						transfer.setLottable02(MassUpdateAction.getLottable02());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE02"))
						{
							transfer.setLottable02(selectedTransfer.getValue("LOTTABLE02") == null ? null : selectedTransfer.getValue("LOTTABLE02").toString());
						}
					}
					else
					{
						transfer.setLottable02(selectedTransfer.getValue("LOTTABLE02") == null ? null : selectedTransfer.getValue("LOTTABLE02").toString());
					}

					if (MassUpdateAction.getLottable03() != null)
					{
						transfer.setLottable03(MassUpdateAction.getLottable03());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE03"))
						{
							transfer.setLottable03(selectedTransfer.getValue("LOTTABLE03") == null ? null : selectedTransfer.getValue("LOTTABLE03").toString());
						}
					}
					else
					{
						transfer.setLottable03(selectedTransfer.getValue("LOTTABLE03") == null ? null : selectedTransfer.getValue("LOTTABLE03").toString());
					}

					if (MassUpdateAction.getLottable04() != null)
					{
						transfer.setLottable04(MassUpdateAction.getLottable04());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE04"))
						{
							transfer.setLottable04((Calendar) selectedTransfer.getValue("LOTTABLE04"));
						}
					}
					else
					{
						transfer.setLottable04((Calendar) selectedTransfer.getValue("LOTTABLE04"));
					}

					if (MassUpdateAction.getLottable05() != null)
					{
						transfer.setLottable05(MassUpdateAction.getLottable05());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE05"))
						{
							transfer.setLottable05((Calendar) selectedTransfer.getValue("LOTTABLE05"));
						}
					}
					else
					{
						transfer.setLottable05((Calendar) selectedTransfer.getValue("LOTTABLE05"));
					}

					if (MassUpdateAction.getLottable06() != null)
					{
						transfer.setLottable06(MassUpdateAction.getLottable06());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE06"))
						{
							transfer.setLottable06(selectedTransfer.getValue("LOTTABLE06") == null ? null : selectedTransfer.getValue("LOTTABLE06").toString());
						}
					}
					else
					{
						transfer.setLottable06(selectedTransfer.getValue("LOTTABLE06") == null ? null : selectedTransfer.getValue("LOTTABLE06").toString());
					}

					if (MassUpdateAction.getLottable07() != null)
					{
						transfer.setLottable07(MassUpdateAction.getLottable07());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE07"))
						{
							transfer.setLottable07(selectedTransfer.getValue("LOTTABLE07") == null ? null : selectedTransfer.getValue("LOTTABLE07").toString());
						}
					}
					else
					{
						transfer.setLottable07(selectedTransfer.getValue("LOTTABLE07") == null ? null : selectedTransfer.getValue("LOTTABLE07").toString());
					}

					if (MassUpdateAction.getLottable08() != null)
					{
						transfer.setLottable08(MassUpdateAction.getLottable08());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE08"))
						{
							transfer.setLottable08(selectedTransfer.getValue("LOTTABLE08") == null ? null : selectedTransfer.getValue("LOTTABLE08").toString());
						}
					}
					else
					{
						transfer.setLottable08(selectedTransfer.getValue("LOTTABLE08") == null ? null : selectedTransfer.getValue("LOTTABLE08").toString());
					}

					if (MassUpdateAction.getLottable09() != null)
					{
						transfer.setLottable09(MassUpdateAction.getLottable09());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE09"))
						{
							transfer.setLottable09(selectedTransfer.getValue("LOTTABLE09") == null ? null : selectedTransfer.getValue("LOTTABLE09").toString());
						}
					}
					else
					{
						transfer.setLottable09(selectedTransfer.getValue("LOTTABLE09") == null ? null : selectedTransfer.getValue("LOTTABLE09").toString());
					}

					if (MassUpdateAction.getLottable10() != null)
					{
						transfer.setLottable10(MassUpdateAction.getLottable10());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE10"))
						{
							transfer.setLottable10(selectedTransfer.getValue("LOTTABLE10") == null ? null : selectedTransfer.getValue("LOTTABLE10").toString());
						}
					}
					else
					{
						transfer.setLottable10(selectedTransfer.getValue("LOTTABLE10") == null ? null : selectedTransfer.getValue("LOTTABLE10").toString());
					}

					if (MassUpdateAction.getLottable11() != null)
					{
						transfer.setLottable11(MassUpdateAction.getLottable04());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE11"))
						{
							transfer.setLottable11((Calendar) selectedTransfer.getValue("LOTTABLE11"));
						}
					}
					else
					{
						transfer.setLottable11((Calendar) selectedTransfer.getValue("LOTTABLE11"));
					}

					if (MassUpdateAction.getLottable12() != null)
					{
						transfer.setLottable12(MassUpdateAction.getLottable12());
						if (selectedTransfer.hasBeenUpdated("LOTTABLE12"))
						{
							transfer.setLottable12((Calendar) selectedTransfer.getValue("LOTTABLE12"));
						}
					}
					else
					{
						transfer.setLottable12((Calendar) selectedTransfer.getValue("LOTTABLE12"));
					}

					updated = true;
				}

			}
			else
			{
				transfer.setTostorerkey(selectedTransfer.getValue("STORERKEY") == null ? null : selectedTransfer.getValue("STORERKEY").toString().toUpperCase());
				if (selectedTransfer.hasBeenUpdated("STORERKEY"))
				{
					updated = true;
				}

				transfer.setTosku(selectedTransfer.getValue("SKU") == null ? null : selectedTransfer.getValue("SKU").toString().toUpperCase());
				if (selectedTransfer.hasBeenUpdated("SKU"))
				{
					updated = true;
				}

				transfer.setLottable01(selectedTransfer.getValue("LOTTABLE01") == null ? null : selectedTransfer.getValue("LOTTABLE01").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE01"))
				{
					updated = true;
				}

				transfer.setLottable02(selectedTransfer.getValue("LOTTABLE02") == null ? null : selectedTransfer.getValue("LOTTABLE02").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE02"))
				{
					updated = true;
				}

				transfer.setLottable03(selectedTransfer.getValue("LOTTABLE03") == null ? null : selectedTransfer.getValue("LOTTABLE03").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE03"))
				{
					updated = true;
				}

				if (!isNull(selectedTransfer.getValue("LOTTABLE04")))
				{
					transfer.setLottable04((Calendar) selectedTransfer.getValue("LOTTABLE04"));
					if (selectedTransfer.hasBeenUpdated("LOTTABLE04"))
					{
						updated = true;
					}
				}

				if (!isNull(selectedTransfer.getValue("LOTTABLE05")))
				{
					transfer.setLottable05((Calendar) selectedTransfer.getValue("LOTTABLE05"));
					if (selectedTransfer.hasBeenUpdated("LOTTABLE05"))
					{
						updated = true;
					}
				}

				transfer.setLottable06(selectedTransfer.getValue("LOTTABLE06") == null ? null : selectedTransfer.getValue("LOTTABLE06").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE06"))
				{
					updated = true;
				}

				transfer.setLottable07(selectedTransfer.getValue("LOTTABLE07") == null ? null : selectedTransfer.getValue("LOTTABLE07").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE07"))
				{
					updated = true;
				}

				transfer.setLottable08(selectedTransfer.getValue("LOTTABLE08") == null ? null : selectedTransfer.getValue("LOTTABLE08").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE08"))
				{
					updated = true;
				}

				transfer.setLottable09(selectedTransfer.getValue("LOTTABLE09") == null ? null : selectedTransfer.getValue("LOTTABLE09").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE09"))
				{
					updated = true;
				}

				transfer.setLottable10(selectedTransfer.getValue("LOTTABLE10") == null ? null : selectedTransfer.getValue("LOTTABLE10").toString());
				if (selectedTransfer.hasBeenUpdated("LOTTABLE10"))
				{
					updated = true;
				}

				if (!isNull(selectedTransfer.getValue("LOTTABLE11")))
				{
					transfer.setLottable11((Calendar) selectedTransfer.getValue("LOTTABLE11"));
					if (selectedTransfer.hasBeenUpdated("LOTTABLE11"))
					{
						updated = true;
					}
				}

				if (!isNull(selectedTransfer.getValue("LOTTABLE12")))
				{
					transfer.setLottable12((Calendar) selectedTransfer.getValue("LOTTABLE12"));
					if (selectedTransfer.hasBeenUpdated("LOTTABLE12"))
					{
						updated = true;
					}
				}
			}

			transfer.setTopackkey(getPack(transfer.getTostorerkey(), transfer.getTosku(), state));

			//02/21/2011 FW  Added code to insert mastunit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- Start 
			transfer.setMasterUnit(getMasterUnit(transfer.getFrompackkey(), state));
			//02/21/2011 FW  Added code to insert mastunit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- End

			if (updated)
			{
				ArrayTransfers.add(transfer);
			}

		}

		return true;
	}

	private String getPack(String storerkey, String sku, StateInterface state)
	{
		String pack = null;
		UnitOfWorkBean tuow = state.getTempUnitOfWork();

		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + storerkey + "'" + " and " + " sku.SKU = '" + sku + "'", null));
		try
		{
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean skuBean = rs.get("" + i);
				pack = BioAttributeUtil.getString(skuBean, "PACKKEY");
			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_MassInternalTransferExecuteTransfersAction_getPack", e.getErrorMessage(), SuggestedCategory.NONE);
		}

		return pack;
	}

	private String calendarToString(final Calendar calendar)
	{
		final DateFormat prettyTime = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);

		return calendar == null ? "" : prettyTime.format(calendar.getTime());
	}

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The context for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException 
	 */
	@Override
	protected int execute(final ActionContext context, final ActionResult result) throws EpiException
	{
		final EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of InternalTransferMassExecuteTransfersAction", SuggestedCategory.NONE);

		final StateInterface state = context.getState();
		final String interactionID = state.getInteractionId();
		final String contextVariablePrefix = "INTERNALTRANSFERERROR";
		contextVariable = contextVariablePrefix + interactionID;
		contextVariableArgs = contextVariablePrefix + "ARGS" + interactionID;

		final UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		final RuntimeListFormInterface imListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_internal_transfer_mass_list_view", state);
		_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + imListForm.getName(), SuggestedCategory.NONE);

		//	 	Get dataobject from MassUpdate Action, which contains lottable attributes and values selected for Mass Update.

		final ArrayList<MassInternalTransferTempDataObject> arrayTransfers = new ArrayList<MassInternalTransferTempDataObject>();

		//  Loop through selected transfers, set Transfer temp data object, add to ArrayTransfers		
		final boolean buildTransferArraySuccessful = buildTransferArray(context, userContext, imListForm, arrayTransfers);
		if (buildTransferArraySuccessful == false)
		{
			return RET_CONTINUE;
		}

		// Clear UnitofWork to prevent wm_internal_transfer_mass bio (lotattribute data) to be updated directly by UI.
		uow.clearState();
		// We have to force a save to clear the state completeley
		try
		{
			uow.saveUOW(false);
		} catch (final EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Save Transfers
		final boolean saveTransfersSuccessful = saveTransfers(context, userContext, uow, arrayTransfers);
		if (saveTransfersSuccessful == false)
		{
			return RET_CONTINUE;
		}

		uow.clearState();
		imListForm.setSelectedItems(null);
		result.setFocus(imListForm.getFocus());
		return RET_CONTINUE;
	}

	private String generateNewNumber(int size) throws EpiException
	{
		String lineNumber = "";
		_log.debug("LOG_DEBUG_EXTENSION", "The Max is = " + size, SuggestedCategory.NONE);
		size += 1;
		if (size < 10000)
		{
			lineNumber += "0";
			if (size < 1000)
			{
				lineNumber += "0";
				if (size < 100)
				{
					lineNumber += "0";
					if (size < 10)
					{
						lineNumber += "0";
					}
				}
			}
		}
		lineNumber += size;
		return lineNumber;
	}

	private boolean isNull(final Object attributeValue)
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

	private boolean isValidItemOwnerComb(final MassInternalTransferTempDataObject data, final String Item, final String Owner, final String itemLabel, final String ownerLabel, final EpnyUserContext userContext, final ActionContext context) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_BOM", "In isValidLocation", 100L);
		try
		{
			final String item = Item;
			final String storer = Owner;

			final String sql = "select * from SKU where storerkey='" + storer + "' and sku='" + item + "'";

			final EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);

			if (dataObject.getCount() < 1)
			{
				final String[] param = new String[4];
				param[0] = Item;
				param[1] = Owner;
				param[2] = itemLabel;
				param[3] = ownerLabel;
				userContext.put(contextVariable, "WMEXP_SKU_OWNER_COMB");
				userContext.put(contextVariableArgs, param);
				context.setNavigation("msgNavigation");
				return false;
			}
		} catch (final DPException e)
		{
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM", "Leaving isValidLocation", 100L);
		return true;
	}

	private boolean isValidOwner(final MassInternalTransferTempDataObject data, String owner, final String label, final DataBean focus, final EpnyUserContext userContext, final ActionContext context) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_BOM", "In isValidOwner", 100L);
		try
		{
			if (!(owner == null) && !owner.equalsIgnoreCase(""))
			{
				owner = owner.toUpperCase();
				final String sql = "select * from STORER where type='1' and storerkey='" + owner + "'";
				final EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
				if (dataObject.getCount() < 1)
				{
					final String[] param = new String[2];
					param[0] = owner;
					param[1] = label;
					userContext.put(contextVariable, "WMEXP_INV_OWNER");
					userContext.put(contextVariableArgs, param);
					context.setNavigation("msgNavigation");
					return false;
				}
			}
			else
			{
				final String[] param = new String[1];
				param[0] = label;
				userContext.put(contextVariable, "WMEXP_REQ");
				userContext.put(contextVariableArgs, param);
				context.setNavigation("msgNavigation");
				return false;
			}
		} catch (final DPException e)
		{
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM", "In isValidOwner", 100L);
		return true;
	}

	private boolean isValidSKU(final MassInternalTransferTempDataObject data, String item, final String label, final DataBean focus, final EpnyUserContext userContext, final ActionContext context) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_BOM", "In isValidSKU", 100L);

		try
		{
			if (!(item == null) && !item.equalsIgnoreCase(""))
			{
				item = item.toUpperCase();
				final String sql = "select * from SKU where sku='" + item + "'";
				final EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
				if (dataObject.getCount() < 1)
				{
					final String[] param = new String[2];
					param[0] = item.toUpperCase();
					param[1] = label;
					userContext.put(contextVariable, "WMEXP_INV_OWNER");
					userContext.put(contextVariableArgs, param);
					context.setNavigation("msgNavigation");
					return false;
				}
			}
			else
			{
				final String[] param = new String[1];
				param[0] = label;
				userContext.put(contextVariable, "WMEXP_REQ");
				userContext.put(contextVariableArgs, param);
				context.setNavigation("msgNavigation");
				return false;
			}
		} catch (final DPException e)
		{
			e.printStackTrace();
		}

		_log.debug("LOG_DEBUG_EXTENSION_BOM", "Leaving isValidSKU", 100L);
		return true;
	}

	private boolean saveTransfers(final ActionContext context, final EpnyUserContext userContext, final UnitOfWorkBean uow, final ArrayList<MassInternalTransferTempDataObject> arrayTransfers) throws EpiException
	{
		BioBean headerBioBean = null;
		final ArrayList<BioBean> detailBeans = new ArrayList<BioBean>();
		int detailcounter = 1;
		_log.debug("LOG_DEBUG_EXTENSION", "****** detailcounter = " + detailcounter, SuggestedCategory.NONE);
		String transferkey = "";

		int maxLineNumber = 0;
		String newLineNumber = null;

		/* Modifications were done as part of Mass Internal Transfer Catch Weight tracking for 3 PL WM913
		 * Phani S Dec 10th 2009.
		 */
		CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();//		 WM 9 3PL Enhancements - Catch weights -Phani
		String ownerVal = "";
		String itemVal  = "";
		String locVal   = "";
		String lotVal   = "";
		String idVal    = "";
		String enabledAdvCatWght = "0";//		 WM 9 3PL Enhancements - Catch weights -Phani
		double grossWgtDb  = 0.0;//		 WM 9 3PL Enhancements - Catch weights -Phani
		double netWgtDb    = 0.0;//		 WM 9 3PL Enhancements - Catch weights -Phani
		double tareWgtDb   = 0.0;//		 WM 9 3PL Enhancements - Catch weights -Phani
//		 WM 9 3PL Enhancements - Catch weights -Phani -END

		// Loop through ArrayTransfers, set Internal Transfer Header (Bio "wm_internal_transfer") and Internal Transfer Detail (Bio "wm_internaltransferdetail") biobeans and process
		for (final MassInternalTransferTempDataObject tempTransfer : arrayTransfers)
		{
			final String id = tempTransfer.getId().toString();

			if (detailcounter == 1)
			{
				transferkey = new KeyGenBioWrapper().getKey("TRANSFER");
				_log.debug("LOG_DEBUG_EXTENSION", "****** transferkey = " + transferkey, SuggestedCategory.NONE);

				_log.debug("LOG_DEBUG_EXTENSION", "creating internal transfer header ******", SuggestedCategory.NONE);

				headerBioBean = uow.getNewBio("wm_internal_transfer");
				headerBioBean.setValue("FROMSTORERKEY", tempTransfer.getFromstorerkey().toString());
				headerBioBean.setValue("TOSTORERKEY", tempTransfer.getTostorerkey().toString());
				headerBioBean.setValue("TYPE", "STORER");
				headerBioBean.setValue("TRANSFERKEY", transferkey);
				headerBioBean.setValue("GENERATEHOCHARGES", "0");
				headerBioBean.setValue("GENERATEIS_HICHARGES", "0");
			}

			final BioBean detailBioBean = uow.getNewBio("wm_internaltransferdetail");

			_log.debug("LOG_DEBUG_EXTENSION", "inserting internal transfer detail ******", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n\n inserting NEW detail ******", SuggestedCategory.NONE);

			newLineNumber = generateNewNumber(maxLineNumber);

			detailBioBean.setValue("TRANSFERLINENUMBER", newLineNumber.toString());
			_log.debug("LOG_DEBUG_EXTENSION", "transferlinenumber set = " + newLineNumber.toString(), SuggestedCategory.NONE);

			ownerVal = tempTransfer.getFromstorerkey().toString();

			detailBioBean.setValue("FROMSTORERKEY", ownerVal);
			_log.debug("LOG_DEBUG_EXTENSION", "fromstorerkey set = " + ownerVal, SuggestedCategory.NONE);
			detailBioBean.setValue("TOSTORERKEY", tempTransfer.getTostorerkey().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "tostorerkey set = " + tempTransfer.getTostorerkey().toString(), SuggestedCategory.NONE);

			detailBioBean.setValue("TRANSFERKEY", transferkey);
			_log.debug("LOG_DEBUG_EXTENSION", "transferkey set ******", SuggestedCategory.NONE);

			itemVal = tempTransfer.getFromsku().toString();
			detailBioBean.setValue("FROMSKU", itemVal);
			_log.debug("LOG_DEBUG_EXTENSION", "fromSku set = " + itemVal, SuggestedCategory.NONE);

			detailBioBean.setValue("TOSKU", tempTransfer.getTosku().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "tosku set = " + tempTransfer.getTosku().toString(), SuggestedCategory.NONE);
			
			detailBioBean.setValue("FROMPACKKEY", tempTransfer.getFrompackkey());
			_log.debug("LOG_DEBUG_EXTENSION", "fromPackkey set = " + tempTransfer.getFrompackkey(), SuggestedCategory.NONE);
			detailBioBean.setValue("TOPACKKEY", tempTransfer.getTopackkey());
			_log.debug("LOG_DEBUG_EXTENSION", "toPackkey set = " + tempTransfer.getTopackkey(), SuggestedCategory.NONE);

			locVal = tempTransfer.getLoc().toString();

			detailBioBean.setValue("FROMLOC", tempTransfer.getLoc().toString());
			detailBioBean.setValue("TOLOC", tempTransfer.getLoc().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "loc = " + tempTransfer.getLoc().toString(), SuggestedCategory.NONE);

			lotVal = tempTransfer.getLot().toString();

			detailBioBean.setValue("FROMLOT", tempTransfer.getLot().toString());
			//detailBioBean.setValue("TOLOT", tempTransfer.getLot().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lot = " + tempTransfer.getLot().toString(), SuggestedCategory.NONE);


            idVal = id;

			detailBioBean.setValue("FROMID", id);
			detailBioBean.setValue("TOID", id);
			_log.debug("LOG_DEBUG_EXTENSION", "id = " + id, SuggestedCategory.NONE);
			 
			detailBioBean.setValue("FROMQTY", tempTransfer.getQty());
			detailBioBean.setValue("TOQTY", tempTransfer.getQty());
			_log.debug("LOG_DEBUG_EXTENSION", "fromqty = " + tempTransfer.getQty(), SuggestedCategory.NONE);

			//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- Start 
			detailBioBean.setValue("FROMUOM", tempTransfer.getMasterUnit());
			detailBioBean.setValue("TOUOM", tempTransfer.getMasterUnit());
			_log.debug("LOG_DEBUG_EXTENSION", "uom = " + tempTransfer.getMasterUnit(), SuggestedCategory.NONE);
						
			//detailBioBean.setValue("FROMUOM", "EA");
			//detailBioBean.setValue("TOUOM", "EA");
			//_log.debug("LOG_DEBUG_EXTENSION", "uom = " + "EA", SuggestedCategory.NONE);
			//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- End

			detailBioBean.setValue("LOTTABLE01", tempTransfer.getLottable01().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable01 = " + tempTransfer.getLottable01().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE02", tempTransfer.getLottable02().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable02 = " + tempTransfer.getLottable02().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE03", tempTransfer.getLottable03().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable03 = " + tempTransfer.getLottable03().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE04", tempTransfer.getLottable04());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable04 = " + calendarToString(tempTransfer.getLottable04()), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE05", tempTransfer.getLottable05());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable05 = " + calendarToString(tempTransfer.getLottable05()), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE06", tempTransfer.getLottable06().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable06 = " + tempTransfer.getLottable06().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE07", tempTransfer.getLottable07().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable07 = " + tempTransfer.getLottable07().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE08", tempTransfer.getLottable08().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable08 = " + tempTransfer.getLottable08().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE09", tempTransfer.getLottable09().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable09 = " + tempTransfer.getLottable09().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE10", tempTransfer.getLottable10().toString());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable10 = " + tempTransfer.getLottable10().toString(), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE11", tempTransfer.getLottable11());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable11 = " + calendarToString(tempTransfer.getLottable11()), SuggestedCategory.NONE);
			detailBioBean.setValue("LOTTABLE12", tempTransfer.getLottable12());
			_log.debug("LOG_DEBUG_EXTENSION", "lottable12 = " + calendarToString(tempTransfer.getLottable12()), SuggestedCategory.NONE);
//			 WM 9 3PL Enhancements - Catch weights -Phani -start
			enabledAdvCatWght = helper.isAdvCatchWeightEnabled(ownerVal,
					itemVal);

			if ((enabledAdvCatWght != null)
					&& (enabledAdvCatWght.equalsIgnoreCase("1"))) {

				EXEDataObject results = null;

				String query = "SELECT SUM(GROSSWGT) AS GROSSWGT, SUM(NETWGT) AS NETWGT, " +
		           		"SUM(TAREWGT) AS TAREWGT FROM LOTXLOCXID WHERE STORERKEY = '"+ownerVal+ "' AND " +
		           		"SKU ='"+itemVal+"' AND " + "LOC = '"+locVal+"' AND " + "LOT = '"+lotVal+ "' AND " +
		           		"ID = '"+idVal+"'";


				try {
					results = WmsWebuiValidationSelectImpl.select(query);
				} catch (Exception e) {
					e.printStackTrace();
				}

				DataValue grossWgtVal = results.getAttribValue(new TextData(
						"GROSSWGT"));
				DataValue netWgtVal = results.getAttribValue(new TextData(
						"NETWGT"));
				DataValue tareWgtVal = results.getAttribValue(new TextData(
						"TAREWGT"));

				if (!grossWgtVal.getNull())
					grossWgtDb = new Double(grossWgtVal.toString())
							.doubleValue();
				if (!netWgtVal.getNull())
					netWgtDb = new Double(netWgtVal.toString()).doubleValue();
				if (!tareWgtVal.getNull())
					tareWgtDb = new Double(tareWgtVal.toString()).doubleValue();

				detailBioBean.setValue("GROSSWGT", grossWgtDb);
				detailBioBean.setValue("NETWGT", netWgtDb);
				detailBioBean.setValue("TAREWGT", tareWgtDb);
			} else {
				detailBioBean.setValue("GROSSWGT", grossWgtDb);
				detailBioBean.setValue("NETWGT", netWgtDb);
				detailBioBean.setValue("TAREWGT", tareWgtDb);
			}
//			 WM 9 3PL Enhancements - Catch weights -Phani -end

			detailBeans.add(detailBioBean);
			detailcounter++;
			maxLineNumber += 1;

		}

		_log.debug("LOG_DEBUG_EXTENSION", "inserting header ******", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "Using Keygen", SuggestedCategory.NONE);

		try
		{
			uow.saveUOW(true);
		} catch (final UnitOfWorkException e)
		{
			// Handle Exceptions
			final Throwable nested = (e).findDeepestNestedException();
			if (nested instanceof ServiceObjectException)
			{
				final Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
				String exceptionMessage = nested.getMessage();
				final Matcher matcher = errorPattern.matcher(exceptionMessage);
				if (matcher.find())
				{
					exceptionMessage = matcher.group(1);
				}
				//throw new UserException(exceptionMessage, new Object[] {});

				//show error
				userContext.put(contextVariable, exceptionMessage);
				context.setNavigation("msgNavigation");

				return false;
			}
		}

		if (detailcounter > 1)
		{
			// Update Internal Transfer Detail to Status = 9
			//jp.answerlink.293085.begin

			Query query = new Query("wm_internal_transfer","wm_internal_transfer.TRANSFERKEY='"+transferkey+"'", null); 
			
			BioBean transferBio = (BioBean)uow.getBioCollectionBean(query).elementAt(0);
			BioCollectionBean transferDetails = (BioCollectionBean)transferBio.get("INTERNALTRANSFERDETAIL");
			for(int idx=0; idx < transferDetails.size(); idx++){
				BioBean transferDetail = (BioBean)transferDetails.elementAt(idx);
				transferDetail.setValue("STATUS", "9");
			}
			/*

			for (final Object element : detailBeans)
			{
				final BioBean detail = (BioBean) element;
				detail.setValue("STATUS", "9");
			}
			*/
			//jp.answerlink.293085.end
			
			try
			{
				uow.saveUOW(true);
			} catch (final UnitOfWorkException e)
			{
				// Handle Exceptions
				final Throwable nested = (e).findDeepestNestedException();
				if (nested instanceof ServiceObjectException)
				{
					final Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
					String exceptionMessage = nested.getMessage();
					final Matcher matcher = errorPattern.matcher(exceptionMessage);
					if (matcher.find())
					{
						exceptionMessage = matcher.group(1);
					}
					//show error
					userContext.put(contextVariable, exceptionMessage);
					context.setNavigation("msgNavigation");

					return false;
				}
			}

			//				uow.saveUOW(true);
			_log.debug("LOG_DEBUG_EXTENSION", "internal transfer detail status updated to 9 ******", SuggestedCategory.NONE);

			// Update Internal Transfer Header to Status = 9
			headerBioBean.setValue("STATUS", "9");

			try
			{
				uow.saveUOW(true);
			} catch (final UnitOfWorkException e)
			{
				// Handle Exceptions
				final Throwable nested = (e).findDeepestNestedException();
				if (nested instanceof ServiceObjectException)
				{
					final Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
					String exceptionMessage = nested.getMessage();
					final Matcher matcher = errorPattern.matcher(exceptionMessage);
					if (matcher.find())
					{
						exceptionMessage = matcher.group(1);
					}
					//show error
					userContext.put(contextVariable, exceptionMessage);
					context.setNavigation("msgNavigation");
					return false;
				}
			}
			//				uow.saveUOW(true);
			_log.debug("LOG_DEBUG_EXTENSION", "internal transfer header status updated to 9 ******", SuggestedCategory.NONE);
		}
		return true;
	}

	//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- Start 
	private String getMasterUnit(String packkey, StateInterface state)
	{
		String masterUnit = null;

		UnitOfWorkBean tuow = state.getTempUnitOfWork();

		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_pack", "wm_pack.PACKKEY = '" + packkey + "'", null));
		try
		{
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean packBean = rs.get("" + i);
				masterUnit = BioAttributeUtil.getString(packBean, "PACKUOM3");
			}
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION", "Can't get masterUnit of packkey = " + packkey, SuggestedCategory.NONE);
		}
		
		return masterUnit;
	}
	//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- End 
}
