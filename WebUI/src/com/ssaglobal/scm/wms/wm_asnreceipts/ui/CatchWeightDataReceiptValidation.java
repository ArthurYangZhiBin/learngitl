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

package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DecimalData;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.CWCDListState;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.CWCDState;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.CWCDUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ItemFlags;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ReceiptDetailListState;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ReceiptDetailState;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ReceiptState;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.SerialKeyUniqueError;

public class CatchWeightDataReceiptValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	public static final String ASN_CATCHWEIGHTDATA_PROMPT_ERROR = "ASN_CATCHWEIGHTDATA_PROMPT_ERROR";

	public static ILoggerCategory _log = LoggerFactory.getInstance(CatchWeightDataReceiptValidation.class);

	private boolean foundErrors;

	private boolean throwError;

	private boolean throwWarning;
	
	private boolean advCWgtThrowError = false; //SRG: Begin: 09/29/10: Quality Center: Defect# 88	

	private HashSet<String> errorItemSet;

	private boolean uniqueError;

	private HashSet<SerialKeyUniqueError> uniqueSerialSet;

	private NumberFormat nf;

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
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		
		StateInterface state = context.getState();
		
		RuntimeFormInterface toolbarHeader = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbarHeader.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1

		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus

		
		
//		StateInterface state = context.getState();
		if(!headerFocus.isBioCollection()){//fix bug 533
			UnitOfWorkBean uow = state.getTempUnitOfWork();
	
			foundErrors = false;
			throwError = false;
			throwWarning = false;
			advCWgtThrowError = false; //SRG: Begin: 09/20/10: Quality Center: Defect# 88
			errorItemSet = new HashSet<String>();
	
			uniqueError = false;
			uniqueSerialSet = new HashSet<SerialKeyUniqueError>();
	
			ReceiptState receiptState = ReceiptState.NONE;
			ReceiptDetailState receiptDetailState = ReceiptDetailState.NONE;
			ReceiptDetailListState receiptDetailListState = ReceiptDetailListState.NONE;
			CWCDState cwcdState = CWCDState.NONE;
			CWCDListState cwcdListState = CWCDListState.NONE;
	
			RuntimeFormInterface receiptForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "receipt_detail_view", state);
			DataBean receiptFocus = null;
			if (receiptForm != null)
			{
				_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt Form " + receiptForm.getName(), SuggestedCategory.NONE);
				receiptFocus = receiptForm.getFocus();
			}
	
			ArrayList<String> detailTabs = new ArrayList<String>();
			detailTabs.add("wm_receiptdetail_detail_view");
			detailTabs.add("tab 0");
			RuntimeFormInterface receiptDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_ASN_Line_Detail_view", detailTabs, state);
			DataBean receiptDetailFocus = null;
			if (receiptDetailForm != null)
			{
				_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt Detail Form " + receiptDetailForm.getName(), SuggestedCategory.NONE);
				receiptDetailFocus = receiptDetailForm.getFocus();
			}
	
			ArrayList<String> listTabs = new ArrayList<String>();
			listTabs.add("wm_receiptdetail_list_view");
			RuntimeFormInterface receiptDetailListForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_ASN_Line_List_View", listTabs, state);
			DataBean receiptDetailListFocus = null;
			if (receiptDetailListForm != null)
			{
				_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt Detail Form " + receiptDetailListForm.getName(), SuggestedCategory.NONE);
				receiptDetailListFocus = receiptDetailListForm.getFocus();
			}
	
			ArrayList<String> cwcdDetailTabs = new ArrayList<String>();
			cwcdDetailTabs.add("wm_receiptdetail_detail_view");
			cwcdDetailTabs.add("tab 7");
			cwcdDetailTabs.add("catchweight_detail_view");
			RuntimeFormInterface cwcdForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_asndetail_catchwgdata_view", cwcdDetailTabs, state);
			DataBean cwcdDetailFocus = null;
			if (cwcdForm != null)
			{
				_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "CWCD Detail From " + cwcdForm.getName(), SuggestedCategory.NONE);
				cwcdDetailFocus = cwcdForm.getFocus();
			}
	
			ArrayList<String> cwcdListTabs = new ArrayList<String>();
			cwcdListTabs.add("wm_receiptdetail_detail_view");
			cwcdListTabs.add("tab 7");
			cwcdListTabs.add("catchweight_list_view");
			RuntimeFormInterface cwcdListForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_asndetail_catchweight_list_view", cwcdListTabs, state);
			DataBean cwcdListFocus = null;
			if (cwcdListForm != null)
			{
				_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "CWCD List From " + cwcdListForm.getName(), SuggestedCategory.NONE);
				cwcdListFocus = cwcdListForm.getFocus();
			}
	
			// This set of code is intended to make knowing what state the UI is in when the save button was pressed, "easy"
			if (receiptFocus != null && receiptFocus.isTempBio())
			{
				//New ASN
				receiptState = ReceiptState.NEW;
	
				if (receiptDetailFocus != null && receiptDetailFocus.isTempBio())
				{
					//New ReceiptDetail
					receiptDetailState = ReceiptDetailState.NEW;
	
					if (cwcdDetailFocus != null && cwcdDetailFocus.isTempBio())
					{
						//New CWCD
						cwcdState = CWCDState.NEW;
					}
					else
					{
						//No CWCD
						cwcdState = CWCDState.NONE;
					}
				}
				else
				{
					//No ReceiptDetail
					receiptDetailState = ReceiptDetailState.NONE;
				}
			}
			else if (receiptFocus != null && receiptFocus.isBio())
			{
				//Existing ASN
				receiptState = ReceiptState.UPDATE;
	
				if (receiptDetailFocus != null && receiptDetailFocus.isTempBio())
				{
					//New ReceiptDetail
					receiptDetailState = ReceiptDetailState.NEW;
	
					if (cwcdDetailFocus != null && cwcdDetailFocus.isTempBio())
					{
						//New CWCD
						cwcdState = CWCDState.NEW;
					}
					else
					{
						//No CWCD
						cwcdState = CWCDState.NONE;
					}
				}
				else if (receiptDetailFocus != null && receiptDetailFocus.isBio())
				{
					//Existing ReceiptDetail
					receiptDetailState = ReceiptDetailState.UPDATE;
	
					//Take a look at the BIOCOLLECTION, receiptdetail.LOTXIDDETAIL
	
					if (cwcdDetailFocus != null && cwcdDetailFocus.isTempBio())
					{
						//New CWCD
						cwcdState = CWCDState.NEW;
					}
					else if (cwcdDetailFocus != null && cwcdDetailFocus.isBio())
					{
						//Existing CWCD
						cwcdState = CWCDState.UPDATE;
					}
					else
					{
						//No CWCD
						cwcdState = CWCDState.NONE;
					}
	
					if (cwcdListFocus != null && cwcdListFocus.isBioCollection())
					{
						//CWCD List
						cwcdListState = CWCDListState.EXISTS;
					}
					else
					{
						//No CWCD List Viewable
						cwcdListState = CWCDListState.NONE;
						cwcdListFocus = (DataBean) receiptDetailFocus.getValue("LOTXIDDETAIL");
						if (cwcdListFocus != null && cwcdListFocus.isBioCollection())
						{
							//CWCD List Focus found
							cwcdListState = CWCDListState.EXISTS;
						}
					}
				}
				else
				{
					//No ReceiptDetail
					receiptDetailState = ReceiptDetailState.NONE;
				}
	
				if (receiptDetailListFocus != null && receiptDetailListFocus.isBioCollection())
				{
					//ReceiptDetail List
					receiptDetailListState = ReceiptDetailListState.EXISTS;
				}
				else
				{
					//No ReceiptDetail List
					receiptDetailListState = ReceiptDetailListState.NONE;
				}
			}
			else
			{
				//No ASN
				receiptState = ReceiptState.NONE;
			}
	
			boolean delaySNumCaptureFlag = CWCDUtil.getDelaySNumCaptureFlag(uow);
	
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt " + receiptState.toString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt Detail " + receiptDetailState.toString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_execute", "Receipt Detail List " + receiptDetailListState.toString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_execute", "CWCD Detail " + cwcdState.toString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_execute", "CWCD List Detail " + cwcdListState.toString(), SuggestedCategory.NONE);
	
			nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
	
			//cwcd can be entered in several way
			//- New CWCD from a New ReceiptDetail
			//- New CWCD from an Existing ReceiptDetail
			//- Existing CWCD from an Existing ReceiptDetail
			//- And maybe - CWCD List from an Existing ReceiptDetail
	
			// Chart - From where you can press save
			/*
			Receipt	|	ReceiptDetail	|	CWCD	|	Account for CWCD List
			N		|	N				|	N		|	-						d
			E		|	N				|	N		|	-						d
			E		|	E				|	N		|	Y						d
			E		|	E				|	E		|	Y						d
			E		|	List			|	- 		|	Y						d
			*/
	
			//QTYRECEIVED - bio
			//RECEIVEDQTY - form
			if (receiptState == ReceiptState.NEW && receiptDetailState == ReceiptDetailState.NEW)
			{
				boolean qtyBeingReceived = false;
				ItemFlags itemFlag = ItemFlags.getItemFlag(uow, receiptDetailFocus);
	
				double qtyReceived = 0;
				try
				{
					//This value is already converted to UOMQty
					qtyReceived = nf.parse(receiptDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue()).doubleValue();
	
				} 
				catch (ParseException e)
				{				
					e.printStackTrace();
				}
				if (qtyReceived > 0)
				{
					qtyBeingReceived = true;
				}
				//has QTYRECEIVED been updated
				if (qtyBeingReceived == true)
				{
					//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
					if (itemFlag.isEnableAdvCWgt()) {
						checkForMissingAdvCWgtInfo(receiptDetailFocus, itemFlag);
					}
					if (advCWgtThrowError == false) { //No need to go thru the rest of the code if advCWgtThrowError = true
					//SRG 09/29/10: Quality Center: Defect# 88 --- End
						if ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true) || (itemFlag.isEnd2endFlag() == true))
						{
							String storer = receiptDetailFocus.getValue("STORERKEY").toString();
							String sku = receiptDetailFocus.getValue("SKU").toString();
							String fromuom = receiptDetailFocus.getValue("UOM").toString();
							String packkey = receiptDetailFocus.getValue("PACKKEY").toString();
	
							int actualCWCD = countCWCDRecords(cwcdState, cwcdListState, cwcdDetailFocus, cwcdListFocus);
							int expectedCWCD;
							try
							{
								expectedCWCD = processTotalLineNumber(storer, sku, fromuom, packkey, String.valueOf(qtyReceived), uow, state);
							} 
							catch (ParseException e)
							{						
								e.printStackTrace();
								_log.error("LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
								expectedCWCD = (int) qtyReceived;
							}
	
							checkForMissingCWCDInfo(receiptDetailFocus, delaySNumCaptureFlag, actualCWCD, expectedCWCD);
						}
					}
	
					// If SNum_EndToEnd is ON and ICD1Unique, validate that Serial does not exist for Owner and Item
					if (itemFlag.isEnd2endFlag() == true && itemFlag.isIcd1uniqueFlag() == true)
					{
						if (cwcdState != CWCDState.NONE)
						{
							//No other CWCD records exist that can be received, so just query the SERIALINVENTORY Table for this record
							checkSNUniqueness(uow, receiptFocus, receiptDetailFocus, cwcdDetailFocus);
						}
					}
				}
			}
			//do i need to account for any existing receiptdetails?
			//i'm thinking no - because if they are recieving anything on this new line - they can't change the rec qty on another line
			if (receiptState == ReceiptState.UPDATE && receiptDetailState == ReceiptDetailState.NEW)
			{
				boolean qtyBeingReceived = false;
				ItemFlags itemFlag = ItemFlags.getItemFlag(uow, receiptDetailFocus);
	
				double qtyReceived = 0;
				try
				{
					//This value is already converted to UOMQty
					qtyReceived = nf.parse(receiptDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue()).doubleValue();
	
				} 
				catch (ParseException e)
				{				
					e.printStackTrace();
				}
				
				if (qtyReceived > 0)
				{
					qtyBeingReceived = true;
				}
	
				//has QTYRECEIVED been updated
				if (qtyBeingReceived == true)
				{
					//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
					if (itemFlag.isEnableAdvCWgt()) {
						checkForMissingAdvCWgtInfo(receiptDetailFocus, itemFlag);
					}
					if (advCWgtThrowError == false) { //No need to go thru the rest of the code if advCWgtThrowError = true
					//SRG 09/29/10: Quality Center: Defect# 88 --- End
						if ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true) || (itemFlag.isEnd2endFlag() == true))
						{
	
							String storer = receiptDetailFocus.getValue("STORERKEY").toString();
							String sku = receiptDetailFocus.getValue("SKU").toString();
							String fromuom = receiptDetailFocus.getValue("UOM").toString();
							String packkey = receiptDetailFocus.getValue("PACKKEY").toString();
	
							int actualCWCD = countCWCDRecords(cwcdState, cwcdListState, cwcdDetailFocus, cwcdListFocus);
							int expectedCWCD;
							try
							{
								expectedCWCD = processTotalLineNumber(storer, sku, fromuom, packkey, String.valueOf(qtyReceived), uow, state);
							} 
							catch (ParseException e)
							{						
								e.printStackTrace();
								_log.error("LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
								expectedCWCD = (int) qtyReceived;
							}
	
							checkForMissingCWCDInfo(receiptDetailFocus, delaySNumCaptureFlag, actualCWCD, expectedCWCD);
						}
					}
	
					// If SNum_EndToEnd is ON and ICD1Unique, validate that Serial does not exist for Owner and Item
					if (itemFlag.isEnd2endFlag() == true && itemFlag.isIcd1uniqueFlag() == true)
					{
						if (cwcdState != CWCDState.NONE)
						{
							checkSNUniqueness(uow, receiptFocus, receiptDetailFocus, cwcdDetailFocus);
						}
	
					}
				}
	
			}
			if (receiptState == ReceiptState.UPDATE && receiptDetailState == ReceiptDetailState.UPDATE)
			{
				boolean qtyBeingReceived = false;
				ItemFlags itemFlag = ItemFlags.getItemFlag(uow, receiptDetailFocus);
				double qtyReceivedFromForm = 0;
				double qtyReceivedFromBio = 0;
				try
				{
					//need to get RECEIVEDQTY from form (UOMQty)
					//and compare with BIO QTYRECEIVED, after converting to UOMQty
					//if they are different, qty is being received
					qtyReceivedFromBio = nf.parse(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, receiptDetailFocus.getValue("UOM").toString(), (receiptDetailFocus).getValue("QTYRECEIVED").toString(), (receiptDetailFocus).getValue("PACKKEY").toString(), state,UOMMappingUtil.uowNull, true)).doubleValue();
					qtyReceivedFromForm = nf.parse(receiptDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue()).doubleValue();
					if (qtyReceivedFromForm != qtyReceivedFromBio)
					{
						receiptDetailFocus.setValue("RECEIVEDQTY", new Double(qtyReceivedFromForm));
						qtyBeingReceived = true;
						
					}
	
				} 
				catch (ParseException e)
				{				
					e.printStackTrace();
				}
				//has QTYRECEIVED been updated
				if (qtyBeingReceived == true)
				{
					//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
					if (itemFlag.isEnableAdvCWgt()) {
						checkForMissingAdvCWgtInfo(receiptDetailFocus, itemFlag);
					}
					if (advCWgtThrowError == false) { //No need to go thru the rest of the code if advCWgtThrowError = true
					//SRG 09/29/10: Quality Center: Defect# 88 --- End
						if ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true) || (itemFlag.isEnd2endFlag() == true))
						{
							String storer = receiptDetailFocus.getValue("STORERKEY").toString();
							String sku = receiptDetailFocus.getValue("SKU").toString();
							String fromuom = receiptDetailFocus.getValue("UOM").toString();
							String packkey = receiptDetailFocus.getValue("PACKKEY").toString();
	
							//i need to account for if there is a new cwcd record and a list
							int actualCWCD = countCWCDRecords(cwcdState, cwcdListState, cwcdDetailFocus, cwcdListFocus);
							int expectedCWCD;
							try
							{
								expectedCWCD = processTotalLineNumber(storer, sku, fromuom, packkey, String.valueOf(qtyReceivedFromForm), uow, state);
							} 
							catch (ParseException e)
							{						
								e.printStackTrace();
								_log.error("LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
								expectedCWCD = (int) qtyReceivedFromForm;
							}
	
							checkForMissingCWCDInfo(receiptDetailFocus, delaySNumCaptureFlag, actualCWCD, expectedCWCD);
						}
					}
	
					// If SNum_EndToEnd is ON and ICD1Unique, validate that Serial does not exist for Owner and Item
					if (itemFlag.isEnd2endFlag() == true && itemFlag.isIcd1uniqueFlag() == true)
					{
						//possible to have other CWCD records
						if (cwcdState != CWCDState.NONE)
						{
							checkSNUniqueness(uow, receiptFocus, receiptDetailFocus, cwcdDetailFocus);
						}
	
						//get cwcd biocollection
						if (cwcdListState == CWCDListState.EXISTS)
						{
							for (int i = 0; i < ((BioCollectionBean) cwcdListFocus).size(); i++)
							{
								BioBean cwcdListItem = ((BioCollectionBean) cwcdListFocus).get("" + i);
								checkSNUniqueness(uow, receiptFocus, receiptDetailFocus, cwcdListItem);
							}
						}
	
					}
				}
	
			}
			if (receiptState == ReceiptState.UPDATE && receiptDetailListState == ReceiptDetailListState.EXISTS)
			{
				try {
					for (int i = 0; i < ((BioCollectionBean) receiptDetailListFocus).size(); i++)
					{
						//need to see if a line is being received
						boolean qtyBeingReceived = false;
						//there might be a possibility of checking twice - because RECEIPTDETAILLISTSTATE can be set to exist when saving an EXISTING ASN
						BioBean singleReceiptDetailFocus = ((BioCollectionBean) receiptDetailListFocus).get("" + i);
						//need to compare the values for QTYRECEIVED, RECEIVEDQTY
						//convert to UOMQty
						String uom = singleReceiptDetailFocus.getValue("UOM").toString();
						String pack = singleReceiptDetailFocus.getValue("PACKKEY").toString();
						double currentQtyReceived = 0;
						double dbQtyReceived = 0;
						try
						{
							currentQtyReceived = nf.parse(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,((BigDecimal) singleReceiptDetailFocus.getValue("QTYRECEIVED")).toPlainString(), pack, state, UOMMappingUtil.uowNull, true)).doubleValue();
							dbQtyReceived = nf.parse(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, ((BigDecimal) singleReceiptDetailFocus.getValue("QTYRECEIVED", true)).toPlainString(), pack, state, UOMMappingUtil.uowNull, true)).doubleValue();
						} 
						catch (ParseException e1)
						{					
							e1.printStackTrace();
						}
						
						if (currentQtyReceived != dbQtyReceived)
						{
							qtyBeingReceived = true;
						}
						ItemFlags itemFlag = ItemFlags.getItemFlag(uow, singleReceiptDetailFocus);
						//has QTYRECEIVED been updated
						if (qtyBeingReceived == true)
						{
							//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
							if (itemFlag.isEnableAdvCWgt()) {
								checkForMissingAdvCWgtInfo(singleReceiptDetailFocus, itemFlag);
							}
							if (advCWgtThrowError == false) { //No need to go thru the rest of the code if advCWgtThrowError = true
							//SRG 09/29/10: Quality Center: Defect# 88 --- End
								if ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true) || (itemFlag.isEnd2endFlag() == true))
								{
	
									String storer = singleReceiptDetailFocus.getValue("STORERKEY").toString();
									String sku = singleReceiptDetailFocus.getValue("SKU").toString();
									String fromuom = singleReceiptDetailFocus.getValue("UOM").toString();
									String packkey = singleReceiptDetailFocus.getValue("PACKKEY").toString();
	
									int actualCWCD = countCWCDRecords(singleReceiptDetailFocus);
									int expectedCWCD;
									try
									{
										expectedCWCD = processTotalLineNumber(storer, sku, fromuom, packkey, String.valueOf(currentQtyReceived), uow, state);
									} 
									catch (ParseException e)
									{							
										e.printStackTrace();
										_log.error("LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
										expectedCWCD = (int) currentQtyReceived;
									}
	
									checkForMissingCWCDInfo(singleReceiptDetailFocus, delaySNumCaptureFlag, actualCWCD, expectedCWCD);
								}
							}
	
							// If SNum_EndToEnd is ON and ICD1Unique, validate that Serial does not exist for Owner and Item
							if (itemFlag.isEnd2endFlag() == true && itemFlag.isIcd1uniqueFlag() == true)
							{
								BioCollectionBean cwcdListFocusBioCollection = (BioCollectionBean) singleReceiptDetailFocus.getValue("LOTXIDDETAIL");
								if (cwcdListFocusBioCollection != null)
								{
									for (int j = 0; j < cwcdListFocusBioCollection.size(); j++)
									{
										BioBean cwcdListItem = ((BioCollectionBean) cwcdListFocusBioCollection).get("" + j);
										checkSNUniqueness(uow, receiptFocus, singleReceiptDetailFocus, cwcdListItem);
									}
								}
							}
						}
					}
				} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
				}
			}
			//Examine the ReceiptDetail record
			//Get the Item and then the ICWFLAG, ICDFLAG, and SNum_EndToEnd flags
			//if (ICWFLAG or ICDFLAG) || (SNum_EndToEnd) 
			// Check the CWCD Info
			// if System Flag DELAYSNCAPTURE
			//  if CWCD Info is incomplete or missing
			//   navigate to Warning
			// else
			//  if CWCD Info is incomplete or missing
			//   throw error		
			//If SNum_EndToEnd is ON and ICD1Unique, validate that Serial does not exist for Owner and Item
			
			//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
			if (advCWgtThrowError) {
				// set the error text just like if we threw an exception but continue processing
				// so the pages re-display properly.
				RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
				toolbar.setError("WMEXP_ADVCW_REQUIRE_ERROR", new Object[] { errorItemSet.toString().substring(1, errorItemSet.toString().length() - 1) });
				return RET_CONTINUE;
			}
			//SRG 09/29/10: Quality Center: Defect# 88 --- End
	
			// Keep track of Items that fail, and save them for throwing the error/warning at the very end
			if (foundErrors == true && uniqueError == true)
			{
				throw new UserException("WMEXP_CWCD_UNIQUE", new Object[] { uniqueSerialSet.toString().substring(1, uniqueSerialSet.toString().length() - 1) });
			}
	
			if (foundErrors == true && throwError == true)
			{
				// 
				// reset the qty to receive until all the serials are entered.
				//
				if ( receiptDetailFocus != null )
				{
					Object tempUOM = receiptDetailFocus.getValue("UOM");
					Object tempQTY = receiptDetailFocus.getValue("QTYRECEIVED");
					Object tempPACKKEY = receiptDetailFocus.getValue("PACKKEY");
					
					String temp = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, tempUOM.toString(), tempQTY.toString(), tempPACKKEY.toString(), state, UOMMappingUtil.uowNull, true);
					receiptDetailFocus.setValue("RECEIVEDQTY", temp);
					receiptDetailForm.getFormWidgetByName("RECEIVEDQTY").setDisplayValue(temp);
				}
				else
				{
					for (int i = 0; i < ((BioCollectionBean) receiptDetailListFocus).size(); i++)
					{					
						BioBean singleReceiptDetailFocus = ((BioCollectionBean) receiptDetailListFocus).get("" + i);
	
						Object tempUOM = singleReceiptDetailFocus.getValue("UOM");
						Object tempQTY = singleReceiptDetailFocus.getValue("QTYRECEIVED");
						Object tempPACKKEY = singleReceiptDetailFocus.getValue("PACKKEY");
						
						String temp = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, tempUOM.toString(), tempQTY.toString(), tempPACKKEY.toString(), state, UOMMappingUtil.uowNull, true);
						singleReceiptDetailFocus.setValue("RECEIVEDQTY", temp);
					}
				}
				
				// set the error text just like if we threw an exception but continue processing
				// so the pages re-display properly.
				RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
				toolbar.setError("WMEXP_CWCD_REQUIRE_ERROR", new Object[] { errorItemSet.toString().substring(1, errorItemSet.toString().length() - 1) });
	
				//throw new UserException("WMEXP_CWCD_REQUIRE_ERROR", new Object[] { errorItemSet.toString().substring(1, errorItemSet.toString().length() - 1) });
				return RET_CONTINUE;
			}
	
			if (foundErrors == true && throwWarning == true)
			{			
				//set navigation
				SessionUtil.setInteractionSessionAttribute(ASN_CATCHWEIGHTDATA_PROMPT_ERROR, errorItemSet, state);
				context.setNavigation("clickEventIncompleteCWCD");
				return RET_CANCEL_EXTENSIONS;
			}	
		}

		return RET_CONTINUE;
	}

	private void checkForMissingCWCDInfo(DataBean receiptDetailFocus, boolean delaySNumCaptureFlag, int actualCWCD, int expectedCWCD)
	{
		if (delaySNumCaptureFlag == true)
		{
			_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "ActualCWCD " + actualCWCD + " " + "Expected CWCD " + expectedCWCD, SuggestedCategory.NONE);
			//warning
			if (actualCWCD < expectedCWCD)
			{
				foundErrors = true;
				throwWarning = true;
				errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
			}
		}
		else
		{
			_log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute", "ActualCWCD " + actualCWCD + " " + "Expected CWCD " + expectedCWCD, SuggestedCategory.NONE);
			//error
			if (actualCWCD < expectedCWCD)
			{
				foundErrors = true;
				throwError = true;
				errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
			}
		}
	}

	private void checkSNUniqueness(UnitOfWorkBean uow, DataBean receiptFocus, DataBean receiptDetailFocus, DataBean cwcdDetailFocus) throws EpiDataException
	{
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_serialinventory", "wm_serialinventory.STORERKEY = '" + receiptFocus.getValue("STORERKEY") + "' and wm_serialinventory.SKU = '"
				+ receiptDetailFocus.getValue("SKU") + "' and wm_serialinventory.SERIALNUMBER = '" + cwcdDetailFocus.getValue("IOTHER1") + "'", null));
		if (rs.size() > 0)
		{
			foundErrors = true;
			uniqueError = true;
			uniqueSerialSet.add(new SerialKeyUniqueError(receiptDetailFocus.getValue("SKU").toString(), receiptDetailFocus.getValue("RECEIPTLINENUMBER").toString(), receiptFocus.getValue("STORERKEY")
					.toString(), cwcdDetailFocus.getValue("IOTHER1").toString()));
		}
	}
	//copied from RFReceiveBase.ProcessTotalLineNumberOutputParam
	private int processTotalLineNumber(String storer, String sku, String fromuom, String packkey, String currentQtyReceived, UnitOfWorkBean uow, StateInterface state) throws EpiException, ParseException
	{
		double eAqty = 0;

		double casecnt = 0;

		double pallet = 0;

		double eaQtyReceived = nf.parse((UOMMappingUtil.convertUOMQty(fromuom, UOMMappingUtil.UOM_EA, currentQtyReceived, packkey, state, UOMMappingUtil.uowNull, true))).doubleValue();

		int iwcBy = 0;

		int totallinenumber = 0;

		DecimalData qtyrec1 = new DecimalData();

		DecimalData packKey1 = new DecimalData();

		totallinenumber = 0;

		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + storer + "' and sku.SKU = '" + sku + "'", null));
		if (rs.size() == 0)
		{
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { sku, storer });
		}
		try
		{
			iwcBy = nf.parse(rs.get("0").getValue("ICWBY").toString()).intValue();
		} catch (ParseException e)
		{			
			e.printStackTrace();
		}

		rs = uow.getBioCollectionBean(new Query("wm_pack", "wm_pack.PACKKEY = '" + packkey + "'", null));
		if (rs.size() == 0)
		{
			throw new UserException("WMEXP_INVAID_PACK", new Object[] { packkey });
		}
		try
		{
			casecnt = nf.parse(rs.get("0").getValue("CASECNT").toString()).doubleValue();
		} catch (ParseException e)
		{			
			e.printStackTrace();
		}
		try
		{
			eAqty = nf.parse(rs.get("0").getValue("QTY").toString()).doubleValue();
		} catch (ParseException e)
		{			
			e.printStackTrace();
		}
		try
		{
			pallet = nf.parse(rs.get("0").getValue("PALLET").toString()).doubleValue();
		} catch (ParseException e)
		{			
			e.printStackTrace();
		}

		qtyrec1.setValue(eaQtyReceived);

		if (iwcBy == 0)
		{
			//SKU.ICWBY = EA
			if (eAqty == 0)
			{
				eAqty = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / eAqty));
			packKey1.setValue(eAqty);
		}
		else if (iwcBy == 1)
		{
			//SKU.ICWBY = CS
			if (casecnt == 0)
			{
				casecnt = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / casecnt));
			packKey1.setValue(casecnt);
		}
		else if (iwcBy == 2)
		{
			//PL
			if (pallet == 0)
			{
				pallet = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / pallet));
			packKey1.setValue(pallet);
		}

		qtyrec1.divide(qtyrec1, packKey1);

		if ((qtyrec1.getValue() - totallinenumber) > 0)
		{
			totallinenumber = totallinenumber + 1;
		}

		if (totallinenumber == 0)
		{
			totallinenumber = 1;
		}

		return totallinenumber;
	}

	private int countCWCDRecords(CWCDState cwcdState, CWCDListState cwcdListState, DataBean cwcdDetailFocus, DataBean cwcdListFocus) throws EpiDataException
	{
		int cwcdCount = 0;
		//count num of CWCD Records
		if (cwcdState == CWCDState.NEW)
		{
			//a new lotxiddetail record will not be linked in the biocollection
			cwcdCount++;
		}
		if (cwcdListState == CWCDListState.EXISTS)
		{
			cwcdCount += ((BioCollectionBean) cwcdListFocus).size();
		}
		else
		{
			//this is in case the biocollectionlist isnt available (which should never happen)
			if (cwcdState == CWCDState.UPDATE)
			{
				cwcdCount++;
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_countCWCDRecords_execute", "Num of CWCD Records " + cwcdCount, SuggestedCategory.NONE);
		return cwcdCount;
	}

	private int countCWCDRecords(BioBean singleReceiptDetailFocus) throws EpiDataException
	{
		int cwcdCount = 0;
		//get cwcdCount from the receiptDetail Record
		BioCollectionBean cwcdListFocus = (BioCollectionBean) singleReceiptDetailFocus.getValue("LOTXIDDETAIL");
		if (cwcdListFocus != null)
		{
			cwcdCount = cwcdListFocus.size();
		}

		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_countCWCDRecords", "Num of CWCD Records " + cwcdCount, SuggestedCategory.NONE);

		return cwcdCount;
	}
	
	//SRG 09/29/10 Quality Center: Defect# 88: New method to check for missing AdvCatchWgt info	 
	private void checkForMissingAdvCWgtInfo(DataBean receiptDetailFocus, ItemFlags itemFlag) {
		try {
			if (itemFlag.isCatchGrossWgt()) {
				if (receiptDetailFocus.getValue("GROSSWGT") != null) {						
					double grossWgt1 = nf.parse(receiptDetailFocus.getValue("GROSSWGT").toString()).doubleValue();
					if (grossWgt1 <= 0) {
						advCWgtThrowError = true;
						errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());						
					}
				}
				else {
					advCWgtThrowError = true;
					errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());					
				}
			}
			if (itemFlag.isCatchNetWgt()) {
				if (receiptDetailFocus.getValue("NETWGT") != null) {						
					double netWgt1 = nf.parse(receiptDetailFocus.getValue("NETWGT").toString()).doubleValue();
					if (netWgt1 <= 0) {
						advCWgtThrowError = true;
						errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());						
					}
				}
				else {
					advCWgtThrowError = true;
					errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());					
				}
			}
			if (itemFlag.isCatchTareWgt()) {
				if (receiptDetailFocus.getValue("TAREWGT") != null) {						
					double tareWgt1 = nf.parse(receiptDetailFocus.getValue("TAREWGT").toString()).doubleValue();
					if (tareWgt1 <= 0) {
						advCWgtThrowError = true;
						errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());						
					}
				}
				else {
					advCWgtThrowError = true;
					errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());					
				}
			}
		}		
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
