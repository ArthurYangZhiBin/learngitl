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
import java.math.BigDecimal;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickDetailSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	StateInterface state;

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickDetailSaveValidation.class);

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

		state = context.getState();

		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);

//		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");

//		RuntimeFormInterface tempHeaderForm = state.getRuntimeForm(listSlot, null);

		//Header Validation
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_pickdetail_list_view", state);
		if (!isNull(listForm))
		{
			BioCollectionBean listFormCollection = (BioCollectionBean) listForm.getFocus();

			for (int i = 0; i < listFormCollection.size(); i++)
			{
				BioBean pickDetailBean = (BioBean) listFormCollection.elementAt(i);

				if (pickDetailBean.hasBeenUpdated("STATUS") || pickDetailBean.hasBeenUpdated("QTYNOSEARCH")
						|| pickDetailBean.hasBeenUpdated("DROPID"))
				{
					//PickOnChangeStatusCCF -- when the user changes status from the list
					changeStatusAction(pickDetailBean);
					_log.debug("LOG_DEBUG_EXTENSION", "Performing STATUS validation on "
							+ pickDetailBean.get("CASEID").toString(), SuggestedCategory.NONE);
					int status = Integer.parseInt(pickDetailBean.get("STATUS").toString());
					if (status < 5)
					{
						String lot = isNull(pickDetailBean.get("LOT")) ? "" : pickDetailBean.get("LOT").toString().toUpperCase();
						String loc = isNull(pickDetailBean.get("LOC")) ? "" : pickDetailBean.get("LOC").toString().toUpperCase();
						String id = isNull(pickDetailBean.get("ID")) ? "" : pickDetailBean.get("ID").toString().toUpperCase();
						statusCombinationValidation(pickDetailBean, lot, loc, id);
					}

					_log.debug("LOG_DEBUG_EXTENSION", "Performing QTY validation on " + pickDetailBean.get("CASEID").toString(), SuggestedCategory.NONE);
					String lot = isNull(pickDetailBean.get("LOT")) ? "" : pickDetailBean.get("LOT").toString().toUpperCase();
					String loc = isNull(pickDetailBean.get("LOC")) ? "" : pickDetailBean.get("LOC").toString().toUpperCase();
					String id = isNull(pickDetailBean.get("ID")) ? "" : pickDetailBean.get("ID").toString().toUpperCase();
					// Quantity Validation
					headerQuantityValidation(pickDetailBean, false, lot, loc, id);
					_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation_execute", "TEST", SuggestedCategory.NONE);
					// Open Qty Validation
					headerOpenQtyValidation(pickDetailBean, false);
					
					//CWCD Validation
					CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.PICK);
					cwcd.cwcdListValidation(pickDetailBean, status);
				}
			}
		}

		//End Header Validation

		//Detail Validation

		_log.debug("LOG_DEBUG_EXTENSION", "\n\nDetail Validation", SuggestedCategory.NONE);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		
		//Get TabGroup Slot
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		if (isNull(tabGroupSlot))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "No Detail to Validate, continuing", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		//Get Detail Tab Focus
		RuntimeFormInterface pickDetailForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
		

		DataBean pickDetailFormFocus = pickDetailForm.getFocus();

		

		boolean isInsert;
		if (pickDetailFormFocus.isTempBio())
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!Insert", SuggestedCategory.NONE);
			isInsert = true;
			//set PickHeaderKey
			setPickHeaderKey(pickDetailFormFocus, pickDetailForm);

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!Update", SuggestedCategory.NONE);
			isInsert = false;
			pickDetailFormFocus = pickDetailFormFocus;

		}

		// Order Validation
		orderValidation(pickDetailFormFocus, pickDetailForm);

		// Status validation
		int status = Integer.parseInt(pickDetailFormFocus.getValue("STATUS").toString());
		String lot = isNull(pickDetailFormFocus.getValue("LOT")) ? ""
				: pickDetailFormFocus.getValue("LOT").toString().toUpperCase();
		String loc = isNull(pickDetailFormFocus.getValue("LOC")) ? ""
				: pickDetailFormFocus.getValue("LOC").toString().toUpperCase();
		String id = isNull(pickDetailFormFocus.getValue("ID")) ? ""
				: pickDetailFormFocus.getValue("ID").toString().toUpperCase();
		if (status < 5)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n!@!# Start of Status Combination Validation", SuggestedCategory.NONE);
			statusCombinationValidation(pickDetailFormFocus, lot, loc, id);
		}
		//Quantity Validation
		quantityValidation(pickDetailForm, isInsert, lot, loc, id);

		// Open Qty Validation
		openQtyValidation(pickDetailFormFocus, isInsert);

		//ToLoc Validation
		toLocValidation(pickDetailFormFocus, pickDetailForm);
		
		//CWCD Validation
		CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.PICK);
		cwcd.cwcdDetailValidation(state, pickDetailFormFocus, status);
		
		return RET_CONTINUE;
	}

	





	private void changeStatusAction(BioBean bean) throws EpiDataException, NumberFormatException, UserException
	{
		int status = 0;
		try
		{
			status = Integer.parseInt(bean.getString("STATUS"));
		} catch (Exception e)
		{
			return;
		}

		int previousStatus = 0;

		previousStatus = Integer.parseInt(bean.getValue("STATUS", true) == null ? "0"
				: bean.getValue("STATUS", true).toString());

		_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "Current Status " + status + ", Previous Status "
				+ previousStatus, SuggestedCategory.NONE);
		;

		if (5 <= status && status < 9 && previousStatus < 5)
		{
			String query = "SELECT CONFIGKEY, NSQLVALUE FROM NSQLCONFIG WHERE CONFIGKEY = 'DOMOVEWHENPICKED' AND NSQLVALUE = '0'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if ((results.getRowCount() == 1))
			{
				//do nothing
				return;
			}

			Object tempToLocValue = bean.getValue("TOLOC");
			Object tempLocValue = bean.getValue("LOC");

			if (isEmpty(tempToLocValue))
			{
				_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", " TOLOC is empty", SuggestedCategory.NONE);
				if (isEmpty(tempLocValue))
				{
					_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "!## LOC is empty", SuggestedCategory.NONE);
				}
				else
				{
					String locValue = tempLocValue == null ? null : tempLocValue.toString().toUpperCase();
					//check type of location
					//"Select count(*) From LOC Where Loc = '" + ls_loc + "' and LocationType = 'PICKTO'"
					query = "SELECT COUNT(*) " + "FROM LOC " + "WHERE LOC.LOC = '" + locValue
							+ "' and LOC.LOCATIONTYPE = 'PICKTO'";
					results = WmsWebuiValidationSelectImpl.select(query);
					_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "Row Count" + results.getRowCount(), SuggestedCategory.NONE);
					if (results.getRowCount() == 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "Raising Error, Loc must be of type PICKTO", SuggestedCategory.NONE);
						;
						throw new UserException("WMEXP_PICK_LOC", "WMEXP_PICK_LOC", new Object[] {});

					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "!Location Not Found", SuggestedCategory.NONE);
					}
				}
			}
			else
			{
				String toLocValue = tempToLocValue.toString();
				_log.debug("LOG_DEBUG_EXTENSION_PickDetailSaveValidation", "Setting TOLOC to LOC", SuggestedCategory.NONE);
				bean.setValue("LOC", toLocValue);
				bean.setValue("TOLOC", "");
			}

		}

	}

	private void headerQuantityValidation(DataBean pickDetailFormFocus, boolean isInsert, String lot, String loc, String id) throws DPException, EpiDataException, UserException
	{
		//Start of Quantity Validation
		//Retrieve Qty and QtyAllocated
		double availableQty = 0;
		String query = "SELECT QTY, QTYALLOCATED FROM LOTXLOCXID " + "WHERE " + "LOT = '" + lot + "' AND " + "LOC = '"
				+ loc + "' AND " + "ID  = '" + id + "' ";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			double qty = Double.parseDouble(results.getAttribValue(1).getAsString());
			double qtyAllocated = Double.parseDouble(results.getAttribValue(2).getAsString());
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Quantity - " + qty + " QuantityAllocated - " + qtyAllocated
					+ "\n", SuggestedCategory.NONE);
			availableQty = qty - qtyAllocated;

		}
		else
		{
			//error
		}
		_log.debug("LOG_DEBUG_EXTENSION", " Available Quantity " + availableQty, SuggestedCategory.NONE);

		if (isInsert)
		{
			//Insert Quantity Validation
			_log.debug("LOG_DEBUG_EXTENSION", " Start of Insert Quantity Validation", SuggestedCategory.NONE);
			double quantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTYNOSEARCH");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION", "Quantity " + quantity, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if (quantity > availableQty)
			{
				//Construct error message
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
		}
		else
		{
			//Update Quantity Validation
			_log.debug("LOG_DEBUG_EXTENSION", " Start of Update Quantity Validation", SuggestedCategory.NONE);
			double quantity = 0;
			double originalQuantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTYNOSEARCH");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION", "Quantity " + quantity, SuggestedCategory.NONE);
				//Retrieve original value
				Object tempOldQuantity = pickDetailFormFocus.getValue("QTYNOSEARCH", true);
				originalQuantity = Double.parseDouble(tempOldQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION", "Old Quantity " + originalQuantity, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Available Quantity " + availableQty, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if (quantity > availableQty + originalQuantity)
			{
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
		}
	}

	private void headerOpenQtyValidation(DataBean pickDetailFormFocus, boolean isInsert) throws DPException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n Start of Open Qty Validation", SuggestedCategory.NONE);
		String orderNumber = pickDetailFormFocus.getValue("ORDERKEY") == null ? null
				: pickDetailFormFocus.getValue("ORDERKEY").toString().toUpperCase();
		String orderLineNumber = pickDetailFormFocus.getValue("ORDERLINENUMBER") == null ? null
				: pickDetailFormFocus.getValue("ORDERLINENUMBER").toString().toUpperCase();
		//Get Open Qty
		String query = "SELECT OPENQTY, ORIGINALQTY, ADJUSTEDQTY FROM ORDERDETAIL " + "WHERE ORDERKEY = '" + orderNumber + "' "
				+ "AND " + "ORDERLINENUMBER = '" + orderLineNumber + "'";
		//_log.debug("LOG_DEBUG_EXTENSION", "//QUERY\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		//jp.answerlink.213883.begin
		//double openQty = 0;
		//double originalQty = 0;
		BigDecimal openQty = new BigDecimal(0);
		BigDecimal originalQty = new BigDecimal(0);
		BigDecimal adjustedQty = new BigDecimal(0);

		//jp.answerlink.213883.end
		if (results.getRowCount() == 1)
		{
			//jp.answerlink.213883.begin
			//openQty = Double.parseDouble(results.getAttribValue(1).getAsString());
			//originalQty = Double.parseDouble(results.getAttribValue(2).getAsString());
			//adjustedQty = Double.parseDouble(results.getAttribValue(3).getAsString());
			openQty = new BigDecimal(results.getAttribValue(1).getAsString());
			originalQty = new BigDecimal(results.getAttribValue(2).getAsString());
			adjustedQty = new BigDecimal(results.getAttribValue(3).getAsString());

			_log.debug("LOG_DEBUG_EXTENSION", "!%% OpenQty " + openQty, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "!%% OriginalQty " + originalQty, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "!%% AdjustedQty " + adjustedQty, SuggestedCategory.NONE);
			//jp.answerlink.213883.end
		}
		else
		{
			//error
		}
		//Get Qty Entered
		//jp.answerlink.213883.begin
		//double qty = Double.parseDouble(pickDetailFormFocus.getValue("QTYNOSEARCH").toString());
		BigDecimal qty = new BigDecimal(pickDetailFormFocus.getValue("QTYNOSEARCH").toString());
		//jp.answerlink.213883.end

		//Get Sum All PickDetail Qty From DB
		query = "SELECT ORDERKEY, ORDERLINENUMBER, SUM(QTY) " + "FROM PICKDETAIL "
				+ "GROUP BY ORDERKEY, ORDERLINENUMBER " + "HAVING " + "(ORDERKEY = '" + orderNumber + "') " + "AND "
				+ "(ORDERLINENUMBER = '" + orderLineNumber + "')";
		//_log.debug("LOG_DEBUG_EXTENSION", "//QUERY\n" + query, SuggestedCategory.NONE);
		results = WmsWebuiValidationSelectImpl.select(query);
		//jp.answerlink.213883.begin
		//double sumQty = 0;
		BigDecimal sumQty = new BigDecimal(0);
		//jp.answerlink.213883.end

		if (results.getRowCount() == 1)
		{
			//jp.answerlink.213883.begin
			//sumQty = Double.parseDouble(results.getAttribValue(3).getAsString());
			sumQty = new BigDecimal(results.getAttribValue(3).getAsString());
			//jp.answerlink.213883.end

			_log.debug("LOG_DEBUG_EXTENSION", "!%% Sum of PickDetailQty " + sumQty, SuggestedCategory.NONE);

		}
		else
		{
			//display error
			//			String[] parameters = new String[2];
			//			parameters[0] = orderNumber;
			//			parameters[1] = orderLineNumber;
			//			throw new UserException("WMEXP_PICK_ORDER_KEY", parameters);
		}
		//jp.answerlink.213883.begin
		//double allQty;
		BigDecimal allQty;
		//double originalQtyEntered=0;
		BigDecimal originalQtyEntered= new BigDecimal(0);
		//jp.answerlink.213883.end

		if (isInsert)
		{
			//jp.answerlink.213883.begin
			//allQty = qty + sumQty; 
			allQty = qty.add(sumQty);
			//jp.answerlink.213883.end



		}
		else
		{
			//jp.answerlink.213883.begin
			//double originalQtyEntered = Double.parseDouble(pickDetailFormFocus.getValue("QTY", true).toString());
			originalQtyEntered = new BigDecimal(pickDetailFormFocus.getValue("QTY", true).toString());
			_log.debug("LOG_DEBUG_EXTENSION", "Old Value " + originalQtyEntered, SuggestedCategory.NONE);
			//allQty = sumQty - originalQtyEntered + qty;
			allQty = sumQty.subtract(originalQtyEntered).add(qty);
			//jp.answerlink.213883.end


		}

		//jp.answerlink.213883.begin
		//		if (allQty > openQty)
		//if (allQty > originalQty)
		if (allQty.compareTo(originalQty.add(adjustedQty)) > 0)
		//jp.answerlink.213883.end
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Raise Error Sum of PICKDETAIL.QTY > ORIGINALQTY + ADJUSTEDQTY " + allQty + " > "
					+ originalQty.add(adjustedQty), SuggestedCategory.NONE);
			//jp.answerlink.212481.begin
			//Reset Qty to original value
			//jp.answerlink.213883.begin
			//if(originalQtyEntered > 0){
			if(originalQtyEntered.compareTo(new BigDecimal(0)) > 0){
				pickDetailFormFocus.setValue("QTYNOSEARCH",new Double(originalQtyEntered.doubleValue()));
			}
			//jp.answerlink.213883.end
			//jp.answerlink.212481.end

			//raise error
			String[] parameters = new String[1];
			throw new UserException("WMEXP_PICK_OPENQTY", parameters);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Sum of PICKDETAIL.QTY < ORIGINALQTY " + allQty + " < " + originalQty, SuggestedCategory.NONE);
		}
		//if new, qty + sumQty
		//if update, sumQty - (original qty) + qty 
	}

	private void quantityValidation(RuntimeFormInterface pickDetailForm, boolean isInsert, String lot, String loc, String id) throws DPException, EpiDataException, UserException
	{
		//Start of Quantity Validation
		DataBean pickDetailFormFocus = pickDetailForm.getFocus();

		//Retrieve Qty and QtyAllocated
		double availableQty = 0;
		String query = "SELECT QTY, QTYALLOCATED FROM LOTXLOCXID " + "WHERE " + "LOT = '" + lot + "' AND " + "LOC = '"
				+ loc + "' AND " + "ID  = '" + id + "' ";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			double qty = Double.parseDouble(results.getAttribValue(1).getAsString());
			double qtyAllocated = Double.parseDouble(results.getAttribValue(2).getAsString());
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Quantity - " + qty + " QuantityAllocated - " + qtyAllocated
					+ "\n", SuggestedCategory.NONE);
			availableQty = qty - qtyAllocated;

		}
		else
		{
			//error
		}
		_log.debug("LOG_DEBUG_EXTENSION", " Available Quantity " + availableQty, SuggestedCategory.NONE);

		if (isInsert)
		{
			//Insert Quantity Validation
			_log.debug("LOG_DEBUG_EXTENSION", " Start of Insert Quantity Validation", SuggestedCategory.NONE);
			double quantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION", "Quantity " + quantity, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if (quantity > availableQty)
			{
				//Construct error message
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
			if(quantity<=0){
				String[] params = new String[1];
				params[0] = colonStrip(readLabel(pickDetailForm.getFormWidgetByName("QTY")));
				throw new UserException("WMEXP_NOT_GREATER_THAN_ZERO", params);
			}
		}
		else
		{
			//Update Quantity Validation
			_log.debug("LOG_DEBUG_EXTENSION", " Start of Update Quantity Validation", SuggestedCategory.NONE);
			double quantity = 0;
			double originalQuantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION", "Quantity " + quantity, SuggestedCategory.NONE);
				//Retrieve original value
				Object tempOldQuantity = pickDetailFormFocus.getValue("QTY", true);
				//jp.answerlink.212481.begin
				if(tempOldQuantity!=null){
					originalQuantity = Double.parseDouble(tempOldQuantity.toString());
				}
				//jp.answerlink.212481.end

				_log.debug("LOG_DEBUG_EXTENSION", "Old Quantity " + originalQuantity, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Available Quantity " + availableQty, SuggestedCategory.NONE);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if (quantity > availableQty + originalQuantity)
			{
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
		}
	}

	private void openQtyValidation(DataBean pickDetailFormFocus, boolean isInsert) throws DPException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n Start of Open Qty Validation", SuggestedCategory.NONE);
		String orderNumber = pickDetailFormFocus.getValue("ORDERKEY") == null ? null
				: pickDetailFormFocus.getValue("ORDERKEY").toString().toUpperCase();
		String orderLineNumber = pickDetailFormFocus.getValue("ORDERLINENUMBER") == null ? null
				: pickDetailFormFocus.getValue("ORDERLINENUMBER").toString().toUpperCase();
		//Get Open Qty
		String query = "SELECT OPENQTY, ORIGINALQTY, ADJUSTEDQTY FROM ORDERDETAIL " + "WHERE ORDERKEY = '" + orderNumber + "' "
				+ "AND " + "ORDERLINENUMBER = '" + orderLineNumber + "'";
		//_log.debug("LOG_DEBUG_EXTENSION", "//QUERY\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		//jp.answerlink.213883.begin
		//double openQty = 0;
		//double originalQty = 0;
		//double adjustedQty = 0; //jp.answerlink.212481
		BigDecimal openQty = new BigDecimal(0);
		BigDecimal originalQty = new BigDecimal(0);
		BigDecimal adjustedQty = new BigDecimal(0);
		//jp.answerlink.213883.end

		if (results.getRowCount() == 1)
		{
			//jp.answerlink.213883.begin
			//openQty = Double.parseDouble(results.getAttribValue(1).getAsString());
			//originalQty = Double.parseDouble(results.getAttribValue(2).getAsString());
			//adjustedQty = Double.parseDouble(results.getAttribValue(3).getAsString()); //jp.answerlink.212481
			openQty = new BigDecimal(results.getAttribValue(1).getAsString());
			originalQty = new BigDecimal(results.getAttribValue(2).getAsString());
			adjustedQty = new BigDecimal(results.getAttribValue(3).getAsString()); //jp.answerlink.212481
			//jp.answerlink.213883.end

			_log.debug("LOG_DEBUG_EXTENSION", "!%% OpenQty " + openQty, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "!%% OriginalQty " + originalQty, SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "!%% AdjustedQty " + adjustedQty, SuggestedCategory.NONE);
		}
		else
		{
			//error
		}
		//Get Qty Entered
		//jp.answerlink.213883.begin
		//double qty = Double.parseDouble(pickDetailFormFocus.getValue("QTY").toString());
		BigDecimal qty = new BigDecimal(pickDetailFormFocus.getValue("QTY").toString());
		//jp.answerlink.213883.end

		//Get Sum All PickDetail Qty From DB
		query = "SELECT ORDERKEY, ORDERLINENUMBER, SUM(QTY) " + "FROM PICKDETAIL "
				+ "GROUP BY ORDERKEY, ORDERLINENUMBER " + "HAVING " + "(ORDERKEY = '" + orderNumber + "') " + "AND "
				+ "(ORDERLINENUMBER = '" + orderLineNumber + "')";
		//_log.debug("LOG_DEBUG_EXTENSION", "//QUERY\n" + query, SuggestedCategory.NONE);
		results = WmsWebuiValidationSelectImpl.select(query);
		//jp.answerlink.213883.begin
		//double sumQty = 0;
		BigDecimal sumQty = new BigDecimal(0);
		//jp.answerlink.213883.end

		if (results.getRowCount() == 1)
		{
			//jp.answerlink.213883.begin
			//sumQty = Double.parseDouble(results.getAttribValue(3).getAsString());
			sumQty = new BigDecimal(results.getAttribValue(3).getAsString());
			//jp.answerlink.213883.end

			_log.debug("LOG_DEBUG_EXTENSION", "!%% Sum of PickDetailQty " + sumQty, SuggestedCategory.NONE);

		}
		else
		{
			//display error
			//			String[] parameters = new String[2];
			//			parameters[0] = orderNumber;
			//			parameters[1] = orderLineNumber;
			//			throw new UserException("WMEXP_PICK_ORDER_KEY", parameters);
		}
		//jp.answerlink.213883.begin
		//double allQty;
		BigDecimal allQty;
		//jp.answerlink.213883.end

		if (isInsert)
		{
			//jp.answerlink.213883.begin
			//allQty = qty + sumQty;
			allQty = qty.add(sumQty);
			//jp.answerlink.213883.end


		}
		else
		{
			//jp.answerlink.212481.begin
			//jp.answerlink.213883.begin
			//double originalQtyEntered = 0;
			//originalQtyEntered = getPickQty(pickDetailFormFocus.getValue("PICKDETAILKEY").toString());
			BigDecimal originalQtyEntered = new BigDecimal(getPickQty(pickDetailFormFocus.getValue("PICKDETAILKEY").toString()));
			_log.debug("LOG_DEBUG_EXTENSION", "PICKDETAILKEY:->" +pickDetailFormFocus.getValue("PICKDETAILKEY").toString(),SuggestedCategory.NONE);
			//jp.answerlink.213883.end
			//jp.answerlink.212481.end

			_log.debug("LOG_DEBUG_EXTENSION", "Old Value " + originalQtyEntered, SuggestedCategory.NONE);

			//jp.answerlink.213883.begin
			//allQty = sumQty - originalQtyEntered + qty;
			allQty = sumQty.subtract(originalQtyEntered).add(qty);
			//jp.answerlink.213883.end


		}

		//if (allQty > openQty)
		//jp.answerlink.213883.begin
		_log.debug("LOG_DEBUG_EXTENSION", "originalQty + adjustedQty:"+ originalQty.add(adjustedQty),100l);
		//if (allQty > (originalQty + adjustedQty))
		if (allQty.compareTo(originalQty.add(adjustedQty)) > 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Raise Error Sum of PICKDETAIL.QTY > ORIGINALQTY + ADJQTY -- " + allQty + " > "
					+ originalQty + " + " + adjustedQty , SuggestedCategory.NONE);

			//raise error
			String[] parameters = new String[1];
			throw new UserException("WMEXP_PICK_OPENQTY", parameters);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Sum of PICKDETAIL.QTY < ORIGINALQTY + ADJQTY" + allQty + " < " + originalQty+ " + " + adjustedQty , SuggestedCategory.NONE);
		}
		//if new, qty + sumQty
		//if update, sumQty - (original qty) + qty 
	}

	private void setPickHeaderKey(DataBean pickDetailFormFocus, RuntimeFormInterface pickDetailForm) throws EpiDataException, UserException, DPException
	{
		_log.debug("LOG_DEBUG_EXTENSION", " Start of PickHeaderKey Generation", SuggestedCategory.NONE);
		Object tempPickHeaderKey = pickDetailFormFocus.getValue("PICKHEADERKEY");
		if (isEmpty(tempPickHeaderKey) && orderValidation(pickDetailFormFocus, pickDetailForm))
		{
			String orderNumber = pickDetailFormFocus.getValue("ORDERKEY") == null ? null
					: pickDetailFormFocus.getValue("ORDERKEY").toString().toUpperCase();
			String query = "SELECT ORDERKEY, PICKHEADERKEY " + "FROM PICKDETAIL " + "WHERE ORDERKEY = '" + orderNumber
					+ "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			String pickHeaderKey;
			if (results.getRowCount() >= 1)
			{
				//Using PickHeaderKey from another record that shares same Order
				_log.debug("LOG_DEBUG_EXTENSION", "Using previous PickHeaderKey", SuggestedCategory.NONE);
				pickHeaderKey = results.getAttribValue(2).getAsString();

			}
			else
			{
				//use keygen
				_log.debug("LOG_DEBUG_EXTENSION", "Using Keygen", SuggestedCategory.NONE);
				pickHeaderKey = new KeyGenBioWrapper().getKey("PICKHEADERKEY");

			}
			_log.debug("LOG_DEBUG_EXTENSION", "!Setting PickHeaderKey to " + pickHeaderKey, SuggestedCategory.NONE);
			pickDetailFormFocus.setValue("PICKHEADERKEY", pickHeaderKey);

		}
	}

	private boolean orderValidation(DataBean pickDetailFormFocus, RuntimeFormInterface pickDetailForm) throws EpiDataException, UserException, DPException
	{
		Object tempOrderValue = pickDetailFormFocus.getValue("ORDERKEY");
		if (isEmpty(tempOrderValue))
		{
			throw new UserException("WMEXP_PICK_ORDER", new Object[] {});
		}
		else
		{
			String orderNumber = tempOrderValue == null ? null : tempOrderValue.toString().toUpperCase();
			String query = "SELECT * FROM ORDERDETAIL WHERE ORDERKEY = '" + orderNumber + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "??Query " + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 0)
			{
				//display error
				String[] parameters = new String[1];
				parameters[0] = orderNumber;
				throw new UserException("WMEXP_PICK_ORDER", parameters);
			}
			else
			{
				//pickDetailForm.getFormWidgetByName("ORDERKEY").setProperty("label image", null);
				_log.debug("LOG_DEBUG_EXTENSION", "Order Validation passed", SuggestedCategory.NONE);
				return true;
			}
		}
	}

	private void toLocValidation(DataBean pickDetailFormFocus, RuntimeFormInterface pickDetailForm) throws EpiDataException, DPException, UserException
	{

		if (isEmpty(pickDetailFormFocus.getValue("TOLOC")) || isEmpty(pickDetailFormFocus.getValue("LOC")))
		{
			_log.debug("LOG_DEBUG_EXTENSION", " Skipping ToLoc Validation", SuggestedCategory.NONE);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", " Beginning ToLoc Validation", SuggestedCategory.NONE);
			String toLoc = pickDetailFormFocus.getValue("TOLOC") == null ? null
					: pickDetailFormFocus.getValue("TOLOC").toString().toUpperCase();
			String loc = pickDetailFormFocus.getValue("LOC") == null ? null
					: pickDetailFormFocus.getValue("LOC").toString().toUpperCase();

			//Query Loc Table, get Location Type
			//Compare with toLoc, should be PickTo or Staged
			//If they do not match throw exception

			//String query = "SELECT LOC,LOCATIONTYPE FROM LOC " + "WHERE LOC = '" + loc + "'";
			String query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC "
					+ "FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE = PUTAWAYZONE.PUTAWAYZONE "
					+ "WHERE LOC.LOC = '" + loc + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1)
			{
				String locationType = results.getAttribValue(2).getAsString();
				_log.debug("LOG_DEBUG_EXTENSION", " LocType " + locationType, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", " ToLoc " + toLoc, SuggestedCategory.NONE);
				if (!(locationType.equalsIgnoreCase(toLoc)))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@ ToLoc Validation Failed", SuggestedCategory.NONE);
					String[] parameters = new String[1];
					parameters[0] = toLoc;
					//pickDetailForm.getFormWidgetByName("TOLOC").setProperty("label image", "images/icon_stop.gif");
					throw new UserException("WMEXP_PICK_TOLOC", parameters);
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@ ToLoc Validation Passed", SuggestedCategory.NONE);
					//pickDetailForm.setProperty("label image", null);
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", " Location Validation Failed", SuggestedCategory.NONE);
			}
		}
	}

	private void statusCombinationValidation(DataBean pickDetailFormFocus, String lot, String loc, String id) throws DPException, UserException
	{
		String query = "SELECT * " + "FROM LOTXLOCXID " + "WHERE " + "LOT = '" + lot + "' AND " + "LOC = '" + loc
				+ "' AND " + "ID  = '" + id + "' ";
		_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 0)
		{
			//display error
			_log.debug("LOG_DEBUG_EXTENSION", "Throwing Combination error", SuggestedCategory.NONE);
			String[] parameters = new String[1];
			Object caseId = pickDetailFormFocus.getValue("CASEID");
			if(caseId == null)
				parameters[0] = "";
			else
				parameters[0] = caseId.toString();

			throw new UserException("WMEXP_PICK_COMBINATION", parameters);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Status Combination Validation passed", SuggestedCategory.NONE);
		}
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
		else
		{
			return false;
		}

	}

	private String colonStrip(String label){
		return 	label.substring(0, label.length()-1);
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
	
	//jp.answerlink.213883.begin
	//Changed signature to return String instead of double type.
	private String getPickQty (String pickdetailkey){
		String pickQty = "0";
		
		String query = "SELECT QTY FROM PICKDETAIL " + 
						"WHERE PICKDETAILKEY = '" + pickdetailkey.trim()+"'";
		

		EXEDataObject results;
		try {
			results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)	{
				
				//pickQty = Double.parseDouble(results.getAttribValue(1).getAsString());
				pickQty = results.getAttribValue(1).getAsString();
			
			}

		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return pickQty;
	}

}
