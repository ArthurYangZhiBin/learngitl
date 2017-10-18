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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DecimalData;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
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
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.CWCDUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.ItemFlags;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.SerialKeyUniqueError;

public class ValidationASNReceiveAll extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory
			.getInstance(ValidationASNReceiveAll.class);

	public static final String RECEIVE_CWCD_WARNING_ITEMS = "ASN_CWCD_WARNING_ITEMS";

	public static final String RECEIVE_CWCD_SHOW_WARNING = "ASN_CWCD_SHOW_WARNING";

	public static String RECEIVEALLCOUNT = "receiveall";

	public static String TABLE = "sku";

	public static String ITEM = "SKU";

	private NumberFormat nf;
	
	//flags
	
	//SRG: Begin: 09/20/10: Quality Center: Defect# 111
	boolean foundErrors = false;
	boolean throwWarning = false;
	boolean throwError = false;
	boolean uniqueError = false;
	//SRG: End: 09/20/10: Quality Center: Defect# 111
	
	boolean advCWgtThrowError = false; //SRG: Begin: 09/30/10: Quality Center: Defect# 88	

	/**
	 * RM 9/8/08 Going to basically remove Stuart(SM)'s changes to ALWAYS block
	 * save if Catch Weight/Data is required Adding new modifications for Serial
	 * Number mod for 9.1 Enterprise
	 * 
	 * @param context
	 *            the context
	 * @param result
	 *            the result
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {

		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state
				.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); // AW
		boolean delaySNumCaptureFlag = CWCDUtil.getDelaySNumCaptureFlag(uowb);
		if (headerFocus instanceof BioCollection) {
			// List Form
			ArrayList<String[]> receipts = new ArrayList<String[]>();

			ArrayList<BioBean> selectedReceipts = ((RuntimeListFormInterface) (headerForm))
					.getSelectedItems();
			if (selectedReceipts != null && selectedReceipts.size() > 0) {
				for (BioBean r : selectedReceipts) {
					//SRG: 09/20/10: Quality Center: Defect# 111: Declared below boolean variables at class level
					foundErrors = false;
					throwWarning = false;
					throwError = false;
					HashSet<String> errorItemSet = new HashSet<String>();
					uniqueError = false;
					HashSet<SerialKeyUniqueError> uniqueSerialSet = new HashSet<SerialKeyUniqueError>();
					int count = 0;
					//SRG: 09/20/10: Quality Center: Defect# 111: Removed passing of boolean variables since they are declared at class level
					count = validateReceiptReceiveAll(state, uowb, r,
							delaySNumCaptureFlag, errorItemSet, uniqueSerialSet, count);
					if (count != 0) {
						String type = BioAttributeUtil.getString(r, "TYPE");
						boolean lottableError = false;
//Issue: Mult ASN in list view RECEIVE ALL only receives one of them
//						Removing catch to bubble up exception						
//						try {
							LottableValidations.validateDetails(uowb, r, (type
									.equalsIgnoreCase("4") || type
									.equalsIgnoreCase("5")));
//						} catch (EpiException e) {
//							lottableError = true;
//						}
						if (foundErrors == false && throwError == false
								&& throwWarning == false
								&& lottableError == false) {
							_log.debug("ValidationASNReceiveAll", "Adding "
									+ BioAttributeUtil.getString(r,
											"RECEIPTKEY"),
									SuggestedCategory.APP_EXTENSION);
							receipts
									.add(new String[] {
											BioAttributeUtil.getString(r,
													"RECEIPTKEY"), type });
						}
					}

				}
				
				for (String[] receiptInfo : receipts) {
					Array params = new Array();
					params.add(new TextData(receiptInfo[0]));
					params.add(new TextData(receiptInfo[1]));
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("NSPRECEIVEALL");
					try {
						_log.debug("ValidationASNReceiveAll", "Receiving " + receiptInfo[0], SuggestedCategory.NONE);
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (WebuiException e) {
						// TODO Auto-generated catch block
						_log.error("ValidationASNReceiveAll", e.getMessage(), SuggestedCategory.NONE);
						_log.error("ValidationASNReceiveAll", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
						
						throw new UserException(e.getMessage(), new Object[] {});

					}
				}
				
			}
			else
			{
				//no records selected
				throw new UserException("WMEXP_NONE_SELECTED", new Object[]{});
			}

			context.setNavigation("menuClickEvent756");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			uowb.clearState();
//			BioCollectionBean rs = uow.getBioCollectionBean(((BioCollection) headerFocus).getQuery());
//			result.setFocus(rs);
			((RuntimeListFormInterface) (headerForm)).setSelectedItems(null);
		} else {
			BioBean headerBio = (BioBean) headerFocus;

			// CWCD Flags
			//SRG: 09/20/10: Quality Center: Defect# 111: Declared below boolean variables at class level
			foundErrors = false;
			throwWarning = false;
			throwError = false;
			HashSet<String> errorItemSet = new HashSet<String>();
			uniqueError = false;
			HashSet<SerialKeyUniqueError> uniqueSerialSet = new HashSet<SerialKeyUniqueError>();
			int count = 0;
			
			//SRG: 09/20/10: Quality Center: Defect# 111: Removed passing of boolean variables since they are declared at class level
			count = validateReceiptReceiveAll(state, uowb, headerBio,
					delaySNumCaptureFlag, errorItemSet, uniqueSerialSet, count);
			if (count == 0) {
				String[] errorParam = new String[1];
				errorParam[0] = headerBio.get("RECEIPTKEY").toString();
				throw new UserException("WMEXP_ASNRECEIVEALL_NOLINE",
						errorParam);
			}
			String type = headerBio.get("TYPE").toString();
			LottableValidations.validateDetails(uowb, headerBio, (type
					.equalsIgnoreCase("4") || type.equalsIgnoreCase("5")));
			
			//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
			if (advCWgtThrowError) {
				throw new UserException("WMEXP_ADVCW_REQUIRE_ERROR",
						new Object[] { errorItemSet.toString().substring(1,
								errorItemSet.toString().length() - 1) });
			}
			//SRG 09/29/10: Quality Center: Defect# 88 --- End

			if (foundErrors == true && uniqueError == true) {
				throw new UserException("WMEXP_CWCD_UNIQUE",
						new Object[] { uniqueSerialSet.toString().substring(1,
								uniqueSerialSet.toString().length() - 1) });
			}

			if (foundErrors == true && throwError == true) {
				throw new UserException("WMEXP_CWCD_REQUIRE_ERROR",
						new Object[] { errorItemSet.toString().substring(1,
								errorItemSet.toString().length() - 1) });
			}

			// RM 9/8/08
			// //SM 10/04/07 ISSUE 7304: No validation for Catch Weight/Data
			// items
			// if(params[0]!=null) {
			// throw new UserException("WMEXP_ASNRECEIPT_CWD_REQ", params);
			// }
			// //SM 10/04/07 ISSUE 7304: END UPDATE
			// RM 9/8/08

			String strCount = count + "";
			SessionUtil.setInteractionSessionAttribute(RECEIVEALLCOUNT,
					strCount, state);
			SessionUtil.setInteractionSessionAttribute(
					RECEIVE_CWCD_SHOW_WARNING, throwWarning, state);
			SessionUtil.setInteractionSessionAttribute(
					RECEIVE_CWCD_WARNING_ITEMS, errorItemSet, state);
			// session.setAttribute(RECEIVEALLCOUNT, strCount);
			context.setNavigation("menuClickEvent320");
		}
		return RET_CONTINUE;
	}

	/**
	 * Validate receipt.
	 * 
	 * @param state
	 *            the state
	 * @param uowb
	 *            the uowb
	 * @param headerBio
	 *            the header bio
	 * @param delaySNumCaptureFlag
	 *            the delay s num capture flag
	 * @param foundErrors
	 *            the found errors
	 * @param throwWarning
	 *            the throw warning
	 * @param throwError
	 *            the throw error
	 * @param errorItemSet
	 *            the error item set
	 * @param uniqueError
	 *            the unique error
	 * @param uniqueSerialSet
	 *            the unique serial set
	 * @param count
	 *            the count
	 * @return the int
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 *             the user exception
	 * @throws EpiException
	 *             the epi exception
	 */
	/*
	 * SRG 08/24/10 Defect# 282686: A new detail list is generated taking into account two checks. The first one  
	 * is qtyReceived is less than qtyExpected and the second is to check if receipt detail line is enabled for
	 * capturing catch weight data or serails. By doing this looping thru each receipt detail which are not 
	 * enabled for capturing catch weight and serial data can be avoided.
	 * 
	 * SRG 09/20/10 Quality Center: Defect# 111: Removed passing of boolean variables since they are declared 
	 * at class level
	 */
	private int validateReceiptReceiveAll(StateInterface state,
			UnitOfWorkBean uowb, BioBean headerBio,
			boolean delaySNumCaptureFlag, HashSet<String> errorItemSet, 
			HashSet<SerialKeyUniqueError> uniqueSerialSet, int count)
	throws EpiDataException, UserException, EpiException {
		BioCollectionBean detailList = (BioCollectionBean) headerBio
		.get("RECEIPTDETAILS");

		//SRG Defect# 282686: Begin
		BioCollectionBean filteredList = detailList.filterBean(new Query("receiptdetail", "receiptdetail.QTYRECEIVED < receiptdetail.QTYEXPECTED", null));
		//BioCollectionBean filteredList_flags = filteredList.filterBean(new Query("receiptdetail", "receiptdetail.SKUBIO.ICWFLAG = '1' OR receiptdetail.SKUBIO.ICDFLAG = '1' OR receiptdetail.SKUBIO.SNUM_ENDTOEND = 1 OR receiptdetail.SKUBIO.ICD1UNIQUE = '1' ", null));
		BioCollectionBean filteredList_flags = filteredList.filterBean(new Query("receiptdetail", "receiptdetail.SKUBIO.ICWFLAG = '1' OR receiptdetail.SKUBIO.ICDFLAG = '1' OR receiptdetail.SKUBIO.SNUM_ENDTOEND = 1 OR receiptdetail.SKUBIO.ICD1UNIQUE = '1' OR receiptdetail.SKUBIO.IBSUMCWFLG = '1' ", null)); //SRG Quality Center: Defect# 88
		if (filteredList.size() > 0) {
			count = filteredList.size();
		}
		//SRG Defect# 282686: End

		//for (int j = 0; j < detailList.size(); j++) {  //SRG Defect# 282686
		for (int j = 0; j < filteredList_flags.size(); j++) {	//SRG Defect# 282686
			BioBean asnDetailLineBio = (BioBean) filteredList_flags.elementAt(j);
			// this qty is in UOM Qty
			String expectedQty = asnDetailLineBio.get("EXPECTEDQTY").toString();
			String receivedQty = asnDetailLineBio.get("RECEIVEDQTY").toString();
			try {

				// 01/05/2010 FW: Changed intQtyExpected/intQtyReceived to
				// double from int in order to receive qty that is less than
				// 1
				// (Incident2873428_Defect150052) -- Start
				/*
				 * int intQtyExpected = nf.parse(expectedQty).intValue(); int
				 * intQtyReceived = nf.parse(receivedQty).intValue();
				 */
				double intQtyExpected = nf.parse(expectedQty).doubleValue();
				double intQtyReceived = nf.parse(receivedQty).doubleValue();
				// 01/05/2010 FW: Changed QtyExpected/QtyReceived to double
				// from
				// int in order to receive qty that is less than 1
				// (Incident2873428_Defect150052) -- End

				//if (intQtyReceived < intQtyExpected) {  //SRG Defect# 282686
				//count = count + 1;  //SRG Defect# 282686

				//
				ItemFlags itemFlag = ItemFlags.getItemFlag(uowb, asnDetailLineBio);
				
				//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
				if (itemFlag.isEnableAdvCWgt()) {
					checkForMissingAdvCWgtInfo(asnDetailLineBio, itemFlag, errorItemSet);
				}
				if (advCWgtThrowError == false) { //No need to go thru the rest of the code if advCWgtThrowError = true
				//SRG 09/29/10: Quality Center: Defect# 88 --- End

					if ((itemFlag.isIcwFlag() == true || itemFlag.isIcdFlag() == true)
							|| (itemFlag.isEnd2endFlag() == true)) {

						String storer = asnDetailLineBio.getValue("STORERKEY")
						.toString();
						String sku = asnDetailLineBio.getValue("SKU")
						.toString();
						String fromuom = asnDetailLineBio.getValue("UOM")
						.toString();
						String packkey = asnDetailLineBio.getValue("PACKKEY")
						.toString();

						// 01/05/2010 FW: Changed expectedCWCD/expectedCWCD
						// to
						// double from int in order to receive qty that is
						// less
						// than 1 (Incident2873428_Defect150052) -- Start
						/*
						 * int actualCWCD = countCWCDRecords(asnDetailLineBio);
						 * int expectedCWCD;
						 */
						double actualCWCD = countCWCDRecords(asnDetailLineBio);
						double expectedCWCD;
						// 01/05/2010 FW: Changed expectedCWCD/expectedCWCD
						// to
						// double from int in order to receive qty that is
						// less
						// than 1 (Incident2873428_Defect150052) -- End

						try {
							expectedCWCD = processTotalLineNumber(storer, sku,
									fromuom, packkey, String
									.valueOf(intQtyExpected), uowb,
									state);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							_log
							.error(
									"LOG_ERROR_EXTENSION_CatchWeightDataReceiptValidation_execute",
									StringUtils
									.getStackTraceAsString(e),
									SuggestedCategory.NONE);
							expectedCWCD = intQtyExpected;
						}

						checkForMissingCWCDInfo(asnDetailLineBio,
								delaySNumCaptureFlag, actualCWCD, expectedCWCD,							
								errorItemSet);
						// if (delaySNumCaptureFlag == true) {
						// int numCWCD = countCWCDRecords(asnDetailLineBio);
						// // warning
						// if (numCWCD < intQtyExpected) {
						// foundErrors = true;
						// throwWarning = true;
						// errorItemSet.add(asnDetailLineBio.getValue("SKU").toString());
						// }
						// } else {
						// int numCWCD = countCWCDRecords(asnDetailLineBio);
						// // error
						// if (numCWCD < intQtyExpected) {
						// foundErrors = true;
						// throwError = true;
						// errorItemSet.add(asnDetailLineBio.getValue("SKU").toString());
						// }
						// }
					}
				}

				// If SNum_EndToEnd is ON and ICD1Unique, validate that
				// Serial does not exist for Owner and Item
				if (itemFlag.isEnd2endFlag() == true
						&& itemFlag.isIcd1uniqueFlag() == true) {
					BioCollectionBean cwcdListFocusBioCollection = (BioCollectionBean) asnDetailLineBio
					.getValue("LOTXIDDETAIL");
					if (cwcdListFocusBioCollection != null) {
						// check serial number uniqueness for the cwcd
						// records associated with this line
						for (int k = 0; k < cwcdListFocusBioCollection
						.size(); k++) {
							BioBean cwcdListItem = (cwcdListFocusBioCollection)
							.get("" + k);
							checkSNUniqueness(uowb, headerBio,
									asnDetailLineBio, cwcdListItem,									
									uniqueSerialSet);
							// BioCollectionBean rs =
							// uowb.getBioCollectionBean(new
							// Query("wm_serialinventory",
							// "wm_serialinventory.STORERKEY = '" +
							// headerBio.getValue("STORERKEY")
							// + "' and wm_serialinventory.SKU = '" +
							// asnDetailLineBio.getValue("SKU") +
							// "' and wm_serialinventory.SERIALNUMBER = '"
							// + cwcdListItem.getValue("IOTHER1") + "'",
							// null));
							// if (rs.size() > 0)
							// {
							// foundErrors = true;
							// uniqueError = true;
							// uniqueSerialSet.add(new
							// SerialKeyUniqueError(asnDetailLineBio.getValue("SKU").toString(),
							// asnDetailLineBio.getValue("RECEIPTLINENUMBER").toString(),
							// headerBio
							// .getValue("STORERKEY").toString(),
							// cwcdListItem.getValue("IOTHER1").toString()));
							// }
						}
					}
				}

				// RM 9/8/08 SN Mod
				// //SM 10/04/07 ISSUE 7304: No validation for Catch
				// Weight/Data items
				// //Check catch weight/data flags
				// String itemValue =
				// asnDetailLineBio.get(ITEM).toString();
				// String queryString =
				// TABLE+"."+ITEM+"='"+itemValue+"'";
				// Query qry = new Query(TABLE, queryString, null);
				// BioCollectionBean list =
				// uowb.getBioCollectionBean(qry);
				// BioBean itemBio = (BioBean)list.elementAt(0);
				// String cwFlag = itemBio.get("CWFLAG")==null ? "0" :
				// itemBio.get("CWFLAG").toString();
				// String icwFlag = itemBio.get("ICWFLAG").toString();
				// String icdFlag = itemBio.get("ICDFLAG").toString();
				// if(cwFlag.equals("1") || icwFlag.equals("1") ||
				// icdFlag.equals("1")){
				// params[0] = params[0]==null ? itemValue :
				// params[0]+", "+itemValue;
				// }
				// //SM 10/04/07 ISSUE 7304: END UPDATE
				// RM 9/8/08 SN Mod
				//} SRG Defect# 282686
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * Count cwcd records.
	 * 
	 * @param singleReceiptDetailFocus
	 *            the single receipt detail focus
	 * @return the int
	 * @throws EpiDataException
	 *             the epi data exception
	 */
	private int countCWCDRecords(BioBean singleReceiptDetailFocus)
			throws EpiDataException {
		int cwcdCount = 0;
		// get cwcdCount from the receiptDetail Record
		BioCollectionBean cwcdListFocus = (BioCollectionBean) singleReceiptDetailFocus
				.getValue("LOTXIDDETAIL");
		if (cwcdListFocus != null) {
			cwcdCount = cwcdListFocus.size();
		}

		_log
				.debug(
						"LOG_DEBUG_EXTENSION_CatchWeightDataReceiptValidation_countCWCDRecords",
						"Num of CWCD Records " + cwcdCount,
						SuggestedCategory.NONE);

		return cwcdCount;
	}

	// refactored code
	// private void checkForMissingCWCDInfo(BioBean receiptDetailFocus, boolean
	// delaySNumCaptureFlag, int actualCWCD, int expectedCWCD)
	// {
	// if (delaySNumCaptureFlag == true)
	// {
	// _log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute",
	// "ActualCWCD " + actualCWCD + " " + "Expected CWCD " + expectedCWCD,
	// SuggestedCategory.NONE);
	// //warning
	// if (actualCWCD < expectedCWCD)
	// {
	// foundErrors = true;
	// throwWarning = true;
	// errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
	// }
	// }
	// else
	// {
	// _log.info("LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute",
	// "ActualCWCD " + actualCWCD + " " + "Expected CWCD " + expectedCWCD,
	// SuggestedCategory.NONE);
	// //error
	// if (actualCWCD < expectedCWCD)
	// {
	// foundErrors = true;
	// throwError = true;
	// errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
	// }
	// }
	// }

	// 01/05/2010 FW: Changed expectedCWCD/expectedCWCD to double from int in
	// order to receive qty that is less than 1 (Incident2873428_Defect150052)
	// -- Start
	// private void checkForMissingCWCDInfo(DataBean receiptDetailFocus, boolean
	// delaySNumCaptureFlag, int actualCWCD, int expectedCWCD)
	/**
	 * Check for missing cwcd info.
	 * 
	 * @param receiptDetailFocus
	 *            the receipt detail focus
	 * @param delaySNumCaptureFlag
	 *            the delay s num capture flag
	 * @param actualCWCD
	 *            the actual cwcd
	 * @param expectedCWCD
	 *            the expected cwcd
	 * @param foundErrors
	 *            the found errors
	 * @param throwWarning
	 *            the throw warning
	 * @param throwError
	 *            the throw error
	 * @param errorItemSet
	 *            the error item set
	 */
	/*	 
	 * SRG 09/20/10 Quality Center: Defect# 111: Removed passing of boolean variables since they are 
	 * declared at class level
	 */
	private void checkForMissingCWCDInfo(DataBean receiptDetailFocus,
			boolean delaySNumCaptureFlag, double actualCWCD,
			double expectedCWCD, HashSet<String> errorItemSet)
	// 01/05/2010 FW: Changed expectedCWCD/expectedCWCD to double from int in
	// order to receive qty that is less than 1 (Incident2873428_Defect150052)
	// -- End

	{
		if (delaySNumCaptureFlag == true) {
			_log
					.info(
							"LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute",
							"ActualCWCD " + actualCWCD + " " + "Expected CWCD "
									+ expectedCWCD, SuggestedCategory.NONE);
			// warning
			if (actualCWCD < expectedCWCD) {
				foundErrors = true;
				throwWarning = true;
				errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
			}
		} else {
			_log
					.info(
							"LOG_INFO_EXTENSION_CatchWeightDataReceiptValidation_execute",
							"ActualCWCD " + actualCWCD + " " + "Expected CWCD "
									+ expectedCWCD, SuggestedCategory.NONE);
			// error
			if (actualCWCD < expectedCWCD) {
				foundErrors = true;
				throwError = true;
				errorItemSet.add(receiptDetailFocus.getValue("SKU").toString());
			}
		}
	}

	/**
	 * Check sn uniqueness.
	 * 
	 * @param uow
	 *            the uow
	 * @param receiptFocus
	 *            the receipt focus
	 * @param receiptDetailFocus
	 *            the receipt detail focus
	 * @param cwcdDetailFocus
	 *            the cwcd detail focus
	 * @param foundErrors
	 *            the found errors
	 * @param uniqueError
	 *            the unique error
	 * @param uniqueSerialSet
	 *            the unique serial set
	 * @throws EpiDataException
	 *             the epi data exception
	 */
	/*	 
	 * SRG 09/20/10 Quality Center: Defect# 111: Removed passing of boolean variables since they are 
	 * declared at class level
	 */
	private void checkSNUniqueness(UnitOfWorkBean uow, DataBean receiptFocus,
			DataBean receiptDetailFocus, DataBean cwcdDetailFocus,			
			HashSet<SerialKeyUniqueError> uniqueSerialSet)
			throws EpiDataException {
		BioCollectionBean rs = uow.getBioCollectionBean(new Query(
				"wm_serialinventory", "wm_serialinventory.STORERKEY = '"
						+ receiptFocus.getValue("STORERKEY")
						+ "' and wm_serialinventory.SKU = '"
						+ receiptDetailFocus.getValue("SKU")
						+ "' and wm_serialinventory.SERIALNUMBER = '"
						+ cwcdDetailFocus.getValue("IOTHER1") + "'", null));
		if (rs.size() > 0) {
			foundErrors = true;
			uniqueError = true;
			uniqueSerialSet.add(new SerialKeyUniqueError(receiptDetailFocus
					.getValue("SKU").toString(), receiptDetailFocus.getValue(
					"RECEIPTLINENUMBER").toString(), receiptFocus.getValue(
					"STORERKEY").toString(), cwcdDetailFocus
					.getValue("IOTHER1").toString()));
		}
	}

	// copied from RFReceiveBase.ProcessTotalLineNumberOutputParam
	/**
	 * Process total line number.
	 * 
	 * @param storer
	 *            the storer
	 * @param sku
	 *            the sku
	 * @param fromuom
	 *            the fromuom
	 * @param packkey
	 *            the packkey
	 * @param currentQtyReceived
	 *            the current qty received
	 * @param uow
	 *            the uow
	 * @param state
	 *            the state
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 * @throws ParseException
	 *             the parse exception
	 */
	private int processTotalLineNumber(String storer, String sku,
			String fromuom, String packkey, String currentQtyReceived,
			UnitOfWorkBean uow, StateInterface state) throws EpiException,
			ParseException {
		double eAqty = 0;

		double casecnt = 0;

		double pallet = 0;

		double eaQtyReceived = nf.parse(
				(UOMMappingUtil.convertUOMQty(fromuom, UOMMappingUtil.UOM_EA,
						currentQtyReceived, packkey, state,
						UOMMappingUtil.uowNull, true))).doubleValue();

		int iwcBy = 0;

		int totallinenumber = 0;

		DecimalData qtyrec1 = new DecimalData();

		DecimalData packKey1 = new DecimalData();

		totallinenumber = 0;

		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku",
				"sku.STORERKEY = '" + storer + "' and sku.SKU = '" + sku + "'",
				null));
		if (rs.size() == 0) {
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { sku,
					storer });
		}
		try {
			iwcBy = nf.parse(rs.get("0").getValue("ICWBY").toString())
					.intValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rs = uow.getBioCollectionBean(new Query("wm_pack",
				"wm_pack.PACKKEY = '" + packkey + "'", null));
		if (rs.size() == 0) {
			throw new UserException("WMEXP_INVAID_PACK",
					new Object[] { packkey });
		}
		try {
			casecnt = nf.parse(rs.get("0").getValue("CASECNT").toString())
					.doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			eAqty = nf.parse(rs.get("0").getValue("QTY").toString())
					.doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pallet = nf.parse(rs.get("0").getValue("PALLET").toString())
					.doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		qtyrec1.setValue(eaQtyReceived);

		if (iwcBy == 0) {
			// SKU.ICWBY = EA
			if (eAqty == 0) {
				eAqty = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / eAqty));
			packKey1.setValue(eAqty);
		} else if (iwcBy == 1) {
			// SKU.ICWBY = CS
			if (casecnt == 0) {
				casecnt = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / casecnt));
			packKey1.setValue(casecnt);
		} else if (iwcBy == 2) {
			// PL
			if (pallet == 0) {
				pallet = 1;
			}

			totallinenumber = ((int) (eaQtyReceived / pallet));
			packKey1.setValue(pallet);
		}

		qtyrec1.divide(qtyrec1, packKey1);

		if ((qtyrec1.getValue() - totallinenumber) > 0) {
			totallinenumber = totallinenumber + 1;
		}

		if (totallinenumber == 0) {
			totallinenumber = 1;
		}

		return totallinenumber;
	}
	
	
	//SRG 09/29/10 Quality Center: Defect# 88: New method to check for missing AdvCatchWgt info	 
	private void checkForMissingAdvCWgtInfo(DataBean receiptDetailFocus,
			ItemFlags itemFlag, HashSet<String> errorItemSet) {
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