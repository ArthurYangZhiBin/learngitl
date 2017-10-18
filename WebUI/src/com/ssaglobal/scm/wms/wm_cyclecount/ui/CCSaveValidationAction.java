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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
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

public class CCSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCSaveValidationAction.class);

	StateInterface state;

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

		state = context.getState();

		RuntimeFormInterface shellForm = retrieveShellForm(state);
		RuntimeFormInterface headerDetailForm = null;
		try
		{
			headerDetailForm = retrieveHeaderDetailForm(state, shellForm);
		} catch (NoHeaderFoundException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		RuntimeFormInterface detailForm = retrieveDetailForm(state, shellForm);

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Validations", SuggestedCategory.NONE);
		// Header  - Starting from Save Button
		DataBean headerFocus = headerDetailForm.getFocus();
		boolean headerUpdate = false;
		if (headerFocus instanceof BioBean)
		{
			headerUpdate = true;
			headerFocus = (BioBean) headerFocus;
		}

		//jp Begin
		_log.debug("LOG_SYSTEM_OUT","Step1",100L);
		//jp End
		DataBean detailFocus = null;
		boolean hasDetail = false;
		if (!isNull(detailForm))
		{
			hasDetail = true;
			detailFocus = detailForm.getFocus();
		}
		boolean detailUpdate = false;
		if (detailFocus instanceof BioBean)
		{
			detailUpdate = true;
			detailFocus = (BioBean) detailFocus;
		}

		//Header Validations------------------------------------------------------------------
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Header Validations", SuggestedCategory.NONE);

		//Duplicate Validation
		if (headerUpdate == false)
		{
			String ccKey = ((String) headerFocus.getValue("CCKEY")).toUpperCase();
			String query = "SELECT * FROM CC WHERE CCKEY = '" + ccKey + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[5];
				parameters[0] = headerDetailForm.getFormWidgetByName("CCKEY").getLabel("label", state.getLocale());
				parameters[1] = ccKey;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}
		//jp Begin
		_log.debug("LOG_SYSTEM_OUT","Step2",100L);
		//jp End

		//Required Fields
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Header Required Fields Validation", SuggestedCategory.NONE);
		//headerRequiredFieldsValidation
		Object tempLoc = headerFocus.getValue("LOC");
		Object tempOwner = headerFocus.getValue("STORERKEY");
		Object tempSku = headerFocus.getValue("SKU");

		if (isEmpty(tempOwner) || isEmpty(tempSku))
		{
			//then loc is required
			if (isEmpty(tempLoc))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "+++Throwing Exception", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CC_REQD_FIELDS", "WMEXP_CC_REQD_FIELDS", new Object[] {});
			}
		}
		if (isEmpty(tempLoc))
		{
			//then owner and sku are required
			if (isEmpty(tempOwner) || isEmpty(tempSku))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "+++Throwing Exception", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CC_REQD_FIELDS", "WMEXP_CC_REQD_FIELDS", new Object[] {});
			}
		}

		//Header Loc Validations
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Header Loc Validation", SuggestedCategory.NONE);
		headerLocValidation(headerDetailForm, headerFocus, detailFocus, hasDetail);

		//Header Sku Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Header Sku Validation", SuggestedCategory.NONE);
		headerSkuValidation(headerDetailForm, headerFocus, detailFocus, hasDetail);

		//Header Storer Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of header Storer Validation", SuggestedCategory.NONE);
		headerStorerValidation(headerDetailForm, headerFocus, detailFocus, hasDetail);

		//RequireCycleCountReason Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of requireCycleCountReasonValidation", SuggestedCategory.NONE);
		requireCycleCountReasonValidation(headerFocus);

		//headerStatusValidation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Header Status Validation", SuggestedCategory.NONE);
		headerStatusValidation(headerFocus, detailFocus, hasDetail);

		//Detail Validations------------------------------------------------------------------
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail Validations", SuggestedCategory.NONE);
		if (hasDetail == false)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!**** No detail, returning", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}

		//jp Begin
		_log.debug("LOG_SYSTEM_OUT","Step3",100L);
		//jp End

		//Duplicate Validation
		if (detailUpdate == false)
		{
			String ccKey = ((String) headerFocus.getValue("CCKEY")).toUpperCase();
			String ccKeyDetail = ((String) detailFocus.getValue("CCDETAILKEY")).toUpperCase();
			String query = "SELECT * FROM CCDETAIL WHERE CCKEY = '" + ccKey + "' AND CCDETAILKEY = '" + ccKeyDetail
					+ "'";

			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + query + "\n", SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[5];
				parameters[0] = detailForm.getFormWidgetByName("CCDETAILKEY").getLabel("label", state.getLocale());
				parameters[1] = ccKeyDetail;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

		//jp Begin
		_log.debug("LOG_SYSTEM_OUT","Step4",100L);
		//jp End

		//Detail Owner Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail Owner Validation", SuggestedCategory.NONE);
		detailOwnerValidation(detailForm, headerFocus, detailFocus, hasDetail);

		//Detail Sku Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail SKU Validation", SuggestedCategory.NONE);
		detailSkuValidation(detailForm, headerFocus, detailFocus, hasDetail);

		//Detail Loc Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail Loc Validation", SuggestedCategory.NONE);
		detailLocValidation(detailForm, headerFocus, detailFocus, hasDetail);

		//Detail Id Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail ID Validation", SuggestedCategory.NONE);
		detailIDValidation(detailForm, headerFocus, detailFocus, hasDetail);

		//Detail Lot Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail Lot Validation", SuggestedCategory.NONE);
		detailLotValidation(detailForm, headerFocus, detailFocus, hasDetail);

		//Quantity Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Quantity Validation", SuggestedCategory.NONE);
		quantityValidation(detailForm, detailFocus, hasDetail);

		//Detail Status Validation
		_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Start of Detail Status Validation", SuggestedCategory.NONE);
		detailStatusValidation(headerFocus, detailFocus);

		
		//jp SN Begin
		//Serial Adjusment Qty
		_log.debug("LOG_SYSTEM_OUT","[CCSaveValidationAction]detailForm:"+detailForm.getName(),100L);
		validateSerialAdjQtys(detailForm, state);
		//jp SN End
		return RET_CONTINUE;
	}

	private void validateSerialAdjQtys(RuntimeFormInterface detailForm, StateInterface state) throws UserException{
		SlotInterface serialSlot = detailForm.getSubSlot(CCHelper.SLOT_SERIALINVENTORYLIST);
		RuntimeFormInterface slotForm = state.getRuntimeForm(serialSlot, null);
		DataBean serialFocus = slotForm.getFocus();

		//Just process if serial numbers present to process

		if(serialFocus!=null){
			
			
			Double qtySelected = Double.parseDouble((String)detailForm.getFormWidgetByName("QTYSELECTED").getValue());
			Double qtyToAdjust = ((Double)detailForm.getFormWidgetByName("ADJQTY").getValue()).doubleValue();
			//Double qtyToAdjust = ((BigDecimal)detailForm.getFormWidgetByName("ADJQTY").getValue()).doubleValue();

			
			if (!qtyToAdjust.equals(qtySelected)){
				String[] params = new String[2];
				
				params[0] = detailForm.getFormWidgetByName("QTYSELECTED").getLabel(RuntimeFormWidgetInterface.LABEL_LABEL, null);
				params[0] += qtySelected.toString();
				params[1] = detailForm.getFormWidgetByName("ADJQTY").getLabel(RuntimeFormWidgetInterface.LABEL_LABEL, null);
				params[1] += qtyToAdjust.toString();
				throw new UserException("WMEXP_ADJQTY_MISMATCH", params);
			}
		}
	}

	private void quantityValidation(RuntimeFormInterface detailForm, DataBean detailFocus, boolean hasDetail) throws EpiDataException, EpiException, UserException
	{
		Object tempQty = detailFocus.getValue("QTY");
		if (isEmpty(tempQty))
		{
			//throw exception
		}

		if (!(FormValidation.isNegative(tempQty.toString(), detailForm.getFormWidgetByName("QTY").getLabel("label", state.getLocale()))))
		{
			//jp.Defect.273468.begin
			//Count discrepancy is kept
			//updateAdjustmentQuantity(detailFocus, hasDetail);
			//jp.Defect.273468.end
		}
	}

	private void detailLocValidation(RuntimeFormInterface detailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		//		Validate SKU
		String table = "LOC";
		String attribute = "LOC";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_LOC";
		Object tempDetailAttribute = detailFocus.getValue(attribute);
		String detailAttributeValue = null;
		if (isEmpty(tempDetailAttribute))
		{
			return;
		}
		else
		{
			detailAttributeValue = tempDetailAttribute.toString();
		}
		//Validate Attribute SKU
		validateAttribute(detailForm, table, attribute, detailAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, detailAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end

	}

	private void detailSkuValidation(RuntimeFormInterface detailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		//Validate SKU
		String table = "SKU";
		String attribute = "SKU";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_SKU";
		Object tempDetailAttribute = detailFocus.getValue(attribute);
		String detailAttributeValue = null;
		if (isEmpty(tempDetailAttribute))
		{
			return;
		}
		else
		{
			detailAttributeValue = tempDetailAttribute.toString();
		}
		//Validate Attribute SKU
		validateAttribute(detailForm, table, attribute, detailAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, detailAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end

	}

	private void detailOwnerValidation(RuntimeFormInterface detailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, DPException, UserException
	{
		//Validate Owner
		String table = "STORER";
		String attribute = "STORERKEY";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_STORER";
		Object tempDetailAttribute = detailFocus.getValue(attribute);
		String detailAttributeValue = null;
		if (isEmpty(tempDetailAttribute))
		{
			return;
		}
		else
		{
			detailAttributeValue = tempDetailAttribute.toString();
		}
		//Validate Attribute Storer
		validateOwnerAttribute(detailForm, table, attribute, detailAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, detailAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end
	}

	private void detailLotValidation(RuntimeFormInterface detailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		String table = "LOTATTRIBUTE";
		String attribute = "LOT";
		String detailAttributeValue = null;
		Object tempDetailAttribute = detailFocus.getValue("LOT");
		if (isEmpty(tempDetailAttribute))
		{
			return;
		}
		else
		{
			detailAttributeValue = tempDetailAttribute.toString();
		}
		//Validate Lot
		validateLotAttribute(detailForm, detailFocus, table, attribute, detailAttributeValue);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end

	}

	private void validateLotAttribute(RuntimeFormInterface form, DataBean detailFocus, String table, String attribute, String detailAttributeValue) throws EpiDataException, DPException, UserException
	{

		detailAttributeValue = detailAttributeValue == null ? null : detailAttributeValue.toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE ( " + attribute + " = '" + detailAttributeValue + "') ";
		Object storerKey = detailFocus.getValue("STORERKEY");
		Object sku = detailFocus.getValue("SKU");
		if (!isEmpty(storerKey) && !isEmpty(sku))
		{
			storerKey = storerKey == null ? null : storerKey.toString().toUpperCase();
			sku = sku == null ? null : sku.toString().toUpperCase();
			query += "AND (STORERKEY = '" + storerKey + "') AND (SKU = '" + sku + "')";
		}
		_log.debug("LOG_DEBUG_EXTENSION", query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 0)
		{
			//Invalid Lot, throw error
			String[] parameters = new String[2];
			parameters[0] = form.getFormWidgetByName(attribute).getLabel("label", state.getLocale());
			parameters[1] = detailAttributeValue;
			throw new UserException("WMEXP_INVALID_VALUE", "WMEXP_INVALID_VALUE", parameters);
		}
	}

	private void detailIDValidation(RuntimeFormInterface form, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, DPException, UserException
	{
		String table = "ID";
		String attribute = "ID";
		String detailAttributeValue = null;
		Object tempDetailAttribute = detailFocus.getValue("ID");
		if (isEmpty(tempDetailAttribute))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "))) " + detailAttributeValue, SuggestedCategory.NONE);
			return;
		}
		else
		{
			detailAttributeValue = tempDetailAttribute.toString();
			_log.debug("LOG_DEBUG_EXTENSION", "))) " + detailAttributeValue, SuggestedCategory.NONE);
		}
		//Validate ID
		validateAttribute(form, table, attribute, detailAttributeValue);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end
	}

	private void headerStorerValidation(RuntimeFormInterface headerDetailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		//Validate STORER
		String table = "STORER";
		String attribute = "STORERKEY";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_STORER";
		Object tempHeaderAttribute = headerFocus.getValue(attribute);
		String headerAttributeValue = null;
		if (isEmpty(tempHeaderAttribute))
		{
			return;
		}
		else
		{
			headerAttributeValue = tempHeaderAttribute.toString();
		}
		//Validate Attribute Storer
		validateOwnerAttribute(headerDetailForm, table, attribute, headerAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, headerAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end
	}

	private void validateOwnerAttribute(RuntimeFormInterface form, String table, String attribute, String attributeValue) throws DPException, UserException
	{
		attributeValue = attributeValue == null ? null : attributeValue.toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE (TYPE = '1') AND (" + attribute + " = '" + attributeValue
				+ "')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 0)
		{
			//Invalid Location, throw error
			String[] parameters = new String[2];
			parameters[0] = form.getFormWidgetByName(attribute).getLabel("label", state.getLocale());
			parameters[1] = attributeValue;
			throw new UserException("WMEXP_INVALID_VALUE", "WMEXP_INVALID_VALUE", parameters);
		}

	}

	private void headerSkuValidation(RuntimeFormInterface headerDetailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		//Validate Sku
		String table = "SKU";
		String attribute = "SKU";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_SKU";
		Object tempHeaderAttribute = headerFocus.getValue(attribute);
		String headerAttributeValue = null;
		if (isEmpty(tempHeaderAttribute))
		{
			return;
		}
		else
		{
			headerAttributeValue = tempHeaderAttribute.toString();
		}
		//Validate Attribute Sku
		validateAttribute(headerDetailForm, table, attribute, headerAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, headerAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end
	}

	private void headerLocValidation(RuntimeFormInterface headerDetailForm, DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, DPException, UserException
	{
		// Validate Loc
		String table = "LOC";
		String attribute = "LOC";
		String errorMessage = "WMEXP_CC_HEADER_DETAIL_LOC";
		Object tempHeaderAttribute = headerFocus.getValue(attribute);
		String headerAttributeValue = null;
		if (isEmpty(tempHeaderAttribute))
		{
			return;
		}
		else
		{
			headerAttributeValue = tempHeaderAttribute.toString();
		}
		//Validate Attribute Loc
		validateAttribute(headerDetailForm, table, attribute, headerAttributeValue);
		//Compare with Detail if Needed
		compareHeaderAttributeWithDetailAttribute(detailFocus, hasDetail, headerAttributeValue, attribute, errorMessage);
		//Update System Quantity
		updateSystemQuantity(headerFocus, detailFocus, hasDetail);
		//Update Adjustment Quantity
		//jp.Defect.273468.begin
		//Count discrepancy is kept
		//updateAdjustmentQuantity(detailFocus, hasDetail);
		//jp.Defect.273468.end

	}

	private void updateSystemQuantity(DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, DPException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "### Updating System Quantity", SuggestedCategory.NONE);
		String query = null;
		Object headerSku = headerFocus.getValue("SKU");
		Object headerLoc = headerFocus.getValue("LOC");
		Object headerStorer = headerFocus.getValue("STORERKEY");

		if ((hasDetail == true) && !isNull(headerSku) && !isNull(headerLoc) && !isNull(headerStorer))
		{
			headerSku = headerSku == null ? null : headerSku.toString().toUpperCase();
			headerLoc = headerLoc == null ? null : headerLoc.toString().toUpperCase();
			headerStorer = headerStorer == null ? null : headerStorer.toString().toUpperCase();
			query = "SELECT SUM(QTY) " + "FROM LOTXLOCXID " + "WHERE (SKU = '" + headerSku + "') AND (LOC = '"
					+ headerLoc + "') AND (STORERKEY = '" + headerStorer + "') ";

			Object detailLot = detailFocus.getValue("LOT");
			Object detailId = detailFocus.getValue("ID");
			if (!isNull(detailLot) && !isNull(detailId))
			{
				detailLot = detailLot == null ? null : detailLot.toString().toUpperCase();
				detailId = detailId == null ? null : detailId.toString().toUpperCase();
				query += " AND (LOT = '" + detailLot + "') AND (ID = '" + detailId + "')";
			}

			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1)
			{
				String result = results.getAttribValue(1).getAsString();
				_log.debug("LOG_DEBUG_EXTENSION", result, SuggestedCategory.NONE);
				if (!result.equalsIgnoreCase("N/A"))
				{
					double sysQty = Double.parseDouble(result);
					detailFocus.setValue("SYSQTY", new Double(sysQty));
					_log.debug("LOG_DEBUG_EXTENSION", "New Sys Qty " + sysQty, SuggestedCategory.NONE);
				}
			}
		}
	}

	private void updateAdjustmentQuantity(DataBean detailFocus, boolean hasDetail) throws EpiDataException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "### Updating Adjustment Quantity", SuggestedCategory.NONE);
		if (hasDetail == true)
		{
			Object sysQty = detailFocus.getValue("SYSQTY");
			Object qty = detailFocus.getValue("QTY");
			if (!isNull(sysQty) && !isNull(qty))
			{
				double adjQty = Double.parseDouble(qty.toString()) - Double.parseDouble(sysQty.toString());
				//				if (adjQty < 0)
				//				{
				//					String[] parameters = new String[1];
				//					parameters[0] = String.valueOf(adjQty);
				//					throw new UserException("WMEXP_CC_ADJQTY", "WMEXP_CC_ADJQTY", parameters);
				//				}
				detailFocus.setValue("ADJQTY", new Double(adjQty));
				// 16/Dec/2009 Seshu - 3PL Enhancements Catch Weight Cycle Count Changes - Starts
				Double adjGWT = new Double(0);
				Double adjNWT = new Double(0);
				Double adjTWT = new Double(0);
				HashMap adjustedWgts = null;
				if (adjQty != 0)
				{
					String sku = detailFocus.getValue("SKU").toString();
					String owner = detailFocus.getValue("STORERKEY").toString();
					Object lotObj = detailFocus.getValue("LOT");
					Object idObj = detailFocus.getValue("ID");
					String id = null;
					String lot = null;
					String loc = detailFocus.getValue("LOC").toString();
					if(lotObj == null || lotObj.toString().trim().length() == 0)
						lot = "";
					else
						lot = lotObj.toString();
					if(idObj == null || idObj.toString().trim().length() == 0)
						id = "";
					else
						id = idObj.toString();
					CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
					adjustedWgts = helper.getCalculatedWeightsLPN(owner, sku, loc, lot, id, adjQty);
					adjGWT = (Double)adjustedWgts.get("GROSSWEIGHT");
					adjNWT = (Double)adjustedWgts.get("NETWEIGHT");
					adjTWT = (Double)adjustedWgts.get("TAREWEIGHT");
				}
				detailFocus.setValue("ADJGROSSWGT", adjGWT);
				detailFocus.setValue("ADJNETWGT", adjNWT);
				detailFocus.setValue("ADJTAREWGT", adjTWT);
				// 16/Dec/2009 Seshu - 3PL Enhancements Catch Weight Cycle Count Changes - Ends
				_log.debug("LOG_DEBUG_EXTENSION", "New Adjustment Quantity " + adjQty, SuggestedCategory.NONE);
			}
		}
	}

	private void compareHeaderAttributeWithDetailAttribute(DataBean otherFocus, boolean hasDetail, String attributeValue, String attributeName, String errorMessage) throws EpiDataException, UserException
	{
		if (hasDetail == true)
		{
			Object tempOtherAttribute = otherFocus.getValue(attributeName);
			String otherDetailAttribute = null;
			if (isEmpty(tempOtherAttribute))
			{
				return;
			}
			else
			{
				otherDetailAttribute = tempOtherAttribute.toString();
			}
			_log.debug("LOG_DEBUG_EXTENSION", "---- Comparing " + attributeValue + " and " + otherDetailAttribute, SuggestedCategory.NONE);
			if (!(attributeValue.equalsIgnoreCase(otherDetailAttribute)))
			{
				throw new UserException(errorMessage, errorMessage, new Object[] {});
			}
		}
	}

	private void validateAttribute(RuntimeFormInterface form, String table, String attribute, String attributeValue) throws DPException, UserException
	{
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE " + attribute + " = '" + attributeValue + "'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 0)
		{
			//Invalid Value, throw error
			String[] parameters = new String[2];
			parameters[0] = form.getFormWidgetByName(attribute).getLabel("label", state.getLocale());
			parameters[1] = attributeValue;
			throw new UserException("WMEXP_INVALID_VALUE", "WMEXP_INVALID_VALUE", parameters);
		}
	}

	private void detailStatusValidation(DataBean headerFocus, DataBean detailFocus) throws EpiDataException, DPException, UserException
	{
		//prevent save if there are more than one detail records with status = 9 (Posted) already
		if (!(isNull(detailFocus.getValue("STATUS"))) && detailFocus.getValue("STATUS").toString().equals("9"))
		{
			String ccKeyValue = headerFocus.getValue("CCKEY").toString();
			ccKeyValue = ccKeyValue == null ? null : ccKeyValue.toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION", "CCKEY " + ccKeyValue, SuggestedCategory.NONE);
			String query = "SELECT * FROM CCDETAIL WHERE (STATUS = '9') AND (CCKEY = '" + ccKeyValue + "')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				//block the save
				throw new UserException("WMEXP_CC_DETAIL_STATUS", "WMEXP_CC_DETAIL_STATUS", new Object[] {});
			}
			else
			{
				//allow the save
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Allowing the save, " + results.getRowCount(), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Changing the header to Posted", SuggestedCategory.NONE);
				headerFocus.setValue("STATUS", "9");

			}
		}
	}

	private void headerStatusValidation(DataBean headerFocus, DataBean detailFocus, boolean hasDetail) throws EpiDataException, DPException, UserException
	{
		// if status = 9
		// check all child records, if one has status of 9, then allow change
		_log.debug("LOG_DEBUG_EXTENSION", "!@ Start of Header 9 Validation", SuggestedCategory.NONE);
		if (!(isNull(headerFocus.getValue("STATUS"))) && headerFocus.getValue("STATUS").toString().equals("9"))
		{
			String ccKeyValue = headerFocus.getValue("CCKEY").toString();
			ccKeyValue = ccKeyValue == null ? null : ccKeyValue.toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION", "CCKEY " + ccKeyValue, SuggestedCategory.NONE);
			String query = "SELECT * FROM CC INNER JOIN CCDETAIL ON CC.CCKEY = CCDETAIL.CCKEY WHERE (CCDETAIL.STATUS = '9') AND (CCDETAIL.CCKEY = '"
					+ ccKeyValue + "')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				//allow the save
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Allowing the save, " + results.getRowCount(), SuggestedCategory.NONE);
			}
			else
			{
				//check and see if the detail is set to 9
				if ((hasDetail == true) && !(isNull(detailFocus.getValue("STATUS")))
						&& detailFocus.getValue("STATUS").toString().equals("9"))
				{
					//allow the save
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Allowing the save based on detail " + results.getRowCount(), SuggestedCategory.NONE);
				}
				else
				{
					//restore old value 
					String oldValue = "0";
					if (headerFocus.getValue("STATUS", true).toString().equals("9"))
					{
						oldValue = "0";
					}
					else
					{
						oldValue = headerFocus.getValue("STATUS", true).toString();
					}
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + oldValue + "\n", SuggestedCategory.NONE);
					headerFocus.setValue("STATUS", oldValue);
					//block the save
					throw new UserException("WMEXP_CC_HEADER_STATUS", "WMEXP_CC_HEADER_STATUS", new Object[] {});
				}
			}

		}
	}

	private void requireCycleCountReasonValidation(DataBean headerFocus) throws DPException, EpiDataException, UserException
	{
		String query = "SELECT * " + "FROM NSQLCONFIG "
				+ "WHERE (CONFIGKEY = 'RequireCycleCountReason') AND (NSQLVALUE = '1')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! RequireCycleCountReason Set to 1", SuggestedCategory.NONE);
			if (isNull(headerFocus.getValue("CCADJREASON")))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "CCADJREASON is null, raise error", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CC_ADJREASON", "WMEXP_CC_ADJREASON", new Object[] {});
			}
			else if (headerFocus.getValue("CCADJREASON").toString().equalsIgnoreCase("0"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "CCADJREASON is none, raise error", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CC_ADJREASON", "WMEXP_CC_ADJREASON", new Object[] {});
			}
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! RequireCycleCountReason not Found or Not Set to 1", SuggestedCategory.NONE);
		}
	}

	private RuntimeFormInterface retrieveShellForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//_log.debug("LOG_DEBUG_EXTENSION", "\n1'''Current form  = " + shellToolbar.getName(), SuggestedCategory.NONE);

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n2'''Current form  = " + shellForm.getName(), SuggestedCategory.NONE);
		return shellForm;
	}

	private RuntimeFormInterface retrieveDetailForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		//Detail
		RuntimeFormInterface ccDetailForm = null;
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);
		//jp Begin
		_log.debug("LOG_SYSTEM_OUT","retrieveDetailForm:detailForm:"+detailForm.getName(),100L);
		//jp End
		if (detailForm.getName().equalsIgnoreCase("wm_cyclecount_detail_toggle"))
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "Need to go one deeper", SuggestedCategory.NONE);
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_cyclecount_detail_toggle");

			//jp SN Begin
			//RuntimeFormInterface ccForm = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab");
			
			RuntimeFormInterface ccForm = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab");
			
			//_log.debug("LOG_DEBUG_EXTENSION", "\n4```Current form = " + ccForm.getName(), SuggestedCategory.NONE);
			if (ccForm.getName().equalsIgnoreCase("wm_cyclecount_detail_view"))
			{
				//_log.debug("LOG_DEBUG_EXTENSION", "Found Detail View, assigning", SuggestedCategory.NONE);
				ccDetailForm = ccForm;
			}
			else  if (ccForm.getName().equalsIgnoreCase("wms_tbgrp_shell")) //wms_tbgrp_shell
			{
				_log.debug("LOG_SYSTEM_OUT","tabForm:"+ccForm.getName(),100L);
				//ccForm.getSubSlots()
				for(Iterator itr = ccForm.getSubSlotsIterator(); itr.hasNext();){
					SlotInterface slot =(SlotInterface)itr.next();
					RuntimeFormInterface  form = state.getRuntimeForm(slot, null);
					
					_log.debug("LOG_SYSTEM_OUT","slot:"+slot.getName()+"\t\tform:"+form.getName(),100L);
					
					if(form.getName().equalsIgnoreCase("wm_cyclecount_detail_view")){
						ccDetailForm = form;
						break;
					}
					
					
					
				}
				
				
				
				//jp SN End

				
			}else{
//				_log.debug("LOG_DEBUG_EXTENSION", "List view or Blank, returning null", SuggestedCategory.NONE);
			}
		}
		else
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "Found Detail View, assigning", SuggestedCategory.NONE);
			ccDetailForm = detailForm;

		}
		return ccDetailForm;
	}

	private RuntimeFormInterface retrieveHeaderDetailForm(StateInterface state, RuntimeFormInterface shellForm) throws NoHeaderFoundException
	{
		SlotInterface headerDetailSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface headerDetailForm = state.getRuntimeForm(headerDetailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + headerDetailForm.getName(), SuggestedCategory.NONE);
		if (!headerDetailForm.getName().equals("wm_cyclecount_detail_header_view"))
		{
			throw new NoHeaderFoundException("Cycle Count Header Not Found, Nothing to Save");
		}
		return headerDetailForm;
	}

	private boolean isNull(Object attributeValue) throws EpiDataException
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

	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else if (attributeValue.toString().equalsIgnoreCase("null"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public class NoHeaderFoundException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String message;

		NoHeaderFoundException(String m)
		{
			message = m;
		}

		public String getMessage()
		{
			return message;
		}

	}

}
