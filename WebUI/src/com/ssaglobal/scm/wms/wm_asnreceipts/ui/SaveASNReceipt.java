/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

//Import 3rd party packages and classes
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.base.Config;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveASNReceipt.
 */
public class SaveASNReceipt extends ActionExtensionBase {
	
	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveASNReceipt.class);
	
	/** The error param. */
	static String[] errorParam = new String[3];
	
	//SRG Begin: Incident4334565_Defect302819
	String sessionVariable;
	String sessionObjectValue;
	//SRG End: Incident4334565_Defect302819
	
	/* (non-Javadoc)
	 * @see com.epiphany.shr.ui.action.ActionExtensionBase#execute(com.epiphany.shr.ui.action.ActionContext, com.epiphany.shr.ui.action.ActionResult)
	 */
	@Override
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {		
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		String detailBiocollection = "RECEIPTDETAILS";

		String detailListTabName = "wm_receiptdetail_list_view";
		ArrayList parms = new ArrayList(); 		
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shell_list_asn_receipts Toolbar", state);									//get the toolbar
		String error = toolbar.getError();
		if ( error != null )
		{

			//
			// if we set the header error in the CatchWeightDataReceiptValidation extension
			// we don't want to process this receipt. Execute the proper navigation so the
			// receipt qty reset gets displayed to the user.
			//
			context.setNavigation("clickEventNormal");			
			return RET_CONTINUE;
		}

		RuntimeFormInterface shellForm = toolbar.getParentForm(state);									//get the Shell form
		DataBean headerFocus = state.getRuntimeForm(shellForm.getSubSlot(shellSlot1), null).getFocus();	//Get the header form focus

		RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot(shellSlot2), null);	//Get the form at slot2
		RuntimeFormInterface detailTab= null;										// this holds the toggle form slot content
		RuntimeListFormInterface detailListTab = null;		//Detail List
		if (detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view"))
		{	//if the slot is populated by toggle form then		
			//detailTab = state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), detailFormTab);
			int formNum = state.getSelectedFormNumber(detailForm.getSubSlot(toggleFormSlot));
			detailTab = state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), formNum);
			detailListTab = (RuntimeListFormInterface) state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), detailListTabName);
			parms.add("wm_receiptdetail_detail_view");
		}
		SlotInterface tabGroupSlot = getTabGroupSlot(state);
		RuntimeFormInterface lpnDetailForm = null, supplierInfoForm = null, asnDetail1 = null;
		DataBean lpnDetailFocus = null, detailFocus = null; // DataBean CatchWgDataFocus = getCatchWgDataFocus(state, tabGroupSlot);	
		QBEBioBean lpnDetailQBEBioBean = null, detailQBEBioBean = null; //		QBEBioBean CatchWgDataQBEBioBean = null;
		BioBean headerBioBean = null, detailBioBean = null, lpnDetailBioBean = null;
		Object objRecDetailLPN = null, packkey = null, toloc = null, tariffKey = null, pokey = null;
		String asnReceiptKey = "", recDetailType = "", receiptLinenumber = "", receiptOwner = "", sku = "", lpnpackkey = "",
		uom = "", lpnuom = "", lpn= " ", expectedQty="", lpnexpectedQty="", receivedQty="", lpnReceivedQty="", receiptDetailOwner = "";		//		String qtyReceived = "";
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - Start
		String headerTemperature1 = "";
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End
		
		double dexpqty, dlpnexpqty, drecqty, dlpnrecqty;
		boolean lpnIsNew = false;
		boolean cwcdIsNew = false;
		parms.add("tab 6");
		parms.add("wm_lpndetail_detail_view");
		lpnDetailForm = FormUtil.findForm(toolbar, "wms_list_shell", "wm_LPN_detail_view", parms, state);
		if (lpnDetailForm != null)
		{
			lpnDetailFocus = lpnDetailForm.getFocus();			
		}

		//RM.S Serial Number - Catch Weight Catch Data
		RuntimeFormInterface cwcdForm = null;
		DataBean cwcdFocus = null;
		if(!headerFocus.isBioCollection()) 
		{			
			parms.clear();
			parms.add("wm_receiptdetail_detail_view");
			parms.add("tab 7");
			parms.add("catchweight_detail_view");
			cwcdForm = FormUtil.findForm(toolbar, "wms_list_shell", "wm_asndetail_catchwgdata_view", parms, state);
			
			if (cwcdForm != null) 
			{
				cwcdFocus = cwcdForm.getFocus();
			}
		
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End
			parms.clear();
			parms.add("tab 3");
			RuntimeFormInterface cawcdForm = FormUtil.findForm(toolbar, "wms_list_shell", "receipt_carrier_tab_view", parms, state);
			String carrier = cawcdForm.getFormWidgetByName("CARRIERKEY").getDisplayValue() == null || cawcdForm.getFormWidgetByName("CARRIERKEY").getDisplayValue().toString().matches("\\s*") ? "" : cawcdForm.getFormWidgetByName("CARRIERKEY").getDisplayValue().toString();
			String trailer = cawcdForm.getFormWidgetByName("TrailerNumber").getDisplayValue() == null || cawcdForm.getFormWidgetByName("TrailerNumber").getDisplayValue().toString().matches("\\s*") ? "" : cawcdForm.getFormWidgetByName("TrailerNumber").getDisplayValue().toString();
			if(!trailer.equalsIgnoreCase(""))
			{
				if(carrier.equalsIgnoreCase(""))
				{
					throw new UserException("Carrier cannot be empty when Trailer is associated with ASN ", new Object[1]);
				}
			}
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End
		
		//RM.E
		}
		//RM 273529
		boolean createNewBio = false;
		
		//RM Bugaware 8024 Added the ability to save from the main list view
		boolean savingFromMainASNList = false;
		ArrayList<String> asnReceiptKeys = new ArrayList<String>();
		if(headerFocus.isBioCollection()) 
		{			
			savingFromMainASNList = true;

			BioCollectionBean listBioCollectionBean = (BioCollectionBean) headerFocus;
			//SRG Begin: Incident4334565_Defect302819
			RuntimeListFormInterface headerListTab = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot(shellSlot1), null);
			String interactionID = state.getInteractionId();		
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			HttpSession session = context.getState().getRequest().getSession();
			//This value is set in the ASNReceiptHeaderListPreRender extension since this cannot be obtained in this extension.
			sessionObjectValue = (String)session.getAttribute(sessionVariable);
			
			int pageStart = Integer.parseInt(sessionObjectValue);
			int pageSize = headerListTab.getWindowSize();				
			int totalListSize = listBioCollectionBean.size();  //Total size of the item list
			int totPrevItems = (totalListSize - pageStart); //Total no of items in the previous pages
			int startValue = 0;
			int endValue = 0;
			if (totPrevItems > pageSize) {
				startValue = pageStart;
				endValue = pageStart + pageSize;
			}
			else { //Last page in the ASN header list
				startValue = pageStart;
				endValue = totalListSize;
			}			
			//for(int i = 0; i < listBioCollectionBean.size(); i++)
			for(int i = startValue; i < endValue; i++)
            //SRG End: Incident4334565_Defect302819 
			{
				BioBean listRowBean = listBioCollectionBean.get("" + i);
				boolean rowHasBeenUpdated = false;

				if(listRowBean.hasBeenUpdated("CARRIERKEY") == true) 
				{
					rowHasBeenUpdated = true;
					String carrierKey = listRowBean.getValue("CARRIERKEY") == null || listRowBean.getValue("CARRIERKEY").toString().matches("\\s*") ? "" : listRowBean.getValue("CARRIERKEY").toString();
					if(carrierKey.length() > 0) 
					{
						updateCarrierInfo(context.getState(), listRowBean, carrierKey);
					}
				}

				if(listRowBean.hasBeenUpdated("CARRIERREFERENCE") == true) 
				{
					rowHasBeenUpdated = true;
					String carrierRef = listRowBean.getValue("CARRIERREFERENCE") == null || listRowBean.getValue("CARRIERREFERENCE").toString().matches("\\s*") ? "" : listRowBean.getValue("CARRIERREFERENCE").toString();
					if(carrierRef.length() > 0) 
					{
						carrierRef = carrierRef.toUpperCase();
						listRowBean.setValue("CARRIERREFERENCE", carrierRef);
					}
				}


				if(listRowBean.hasBeenUpdated("WAREHOUSEREFERENCE") == true) 
				{
					rowHasBeenUpdated = true;
					String warehouseRef = listRowBean.getValue("WAREHOUSEREFERENCE") == null || listRowBean.getValue("WAREHOUSEREFERENCE").toString().matches("\\s*") ? "" : listRowBean.getValue("WAREHOUSEREFERENCE").toString();
					if(warehouseRef.length() > 0) 
					{
						warehouseRef = warehouseRef.toUpperCase();
						listRowBean.setValue("WAREHOUSEREFERENCE", warehouseRef);
					}
				}
				if(listRowBean.hasBeenUpdated("expectedreceiptdate") == true) 
				{
					rowHasBeenUpdated = true;
				}

				if(rowHasBeenUpdated == true) 
				{
					final String receiptKey = (String) listRowBean.getValue("RECEIPTKEY");
					_log.debug("LOG_DEBUG_EXTENSION_SaveASNReceipt_execute", "ASN " + receiptKey + "has been Updated", SuggestedCategory.NONE);
					asnReceiptKeys.add(receiptKey);
				}
			}
		} // end saving from asn header list view
		else
		{
			//saving from asn header detail view
			if(headerFocus.isTempBio()) 
			{//it is for insert header
				//moving this BioBean creation to the end
				//headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);
				detailFocus = detailForm.getFocus();
				asnDetail1 = state.getRuntimeForm(tabGroupSlot, "tab 0");
				supplierInfoForm = state.getRuntimeForm(tabGroupSlot, "tab 4");
				RuntimeFormInterface udfForm = state.getRuntimeForm(tabGroupSlot, "tab 2");
				asnKeyDuplicationCheck(headerFocus.getValue("RECEIPTKEY").toString());
				detailQBEBioBean = (QBEBioBean)detailFocus;
				detailQBEBioBean.set("TYPE", headerFocus.getValue("TYPE"));
				detailQBEBioBean.set("REASONCODE","");
				if (detailQBEBioBean.getValue("TOID") == null)
				{
					// 2012-09-02
				    // Modified by Will Pu
					// 新增ASN明细时, 如果LPN为空, 则自动使用当前ASN单号+新增ASN明细行号来填充LPN字段
					detailQBEBioBean.set("TOID", headerFocus.getValue("RECEIPTKEY").toString().trim() + 
							detailQBEBioBean.get("RECEIPTLINENUMBER").toString().trim());
				}//also moving this to the end
				//headerFocus.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
				receiptOwner = headerFocus.getValue("STORERKEY").toString();

				//jpuente Start				
				((QBEBioBean) headerFocus).set("RECEIPTID", GUIDFactory.getGUIDStatic());
				detailQBEBioBean.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
				//jpuente End
				
//defect1192.b
				if(headerFocus.getValue("ALLOWAUTORECEIPT")== null)
				{
					headerFocus.setValue("ALLOWAUTORECEIPT","0");
				}
//defect1192.e				
				//Validate Door
				validateDoor(state, headerFocus);

				recDetailType = detailQBEBioBean.getValue("TYPE").toString();
				objRecDetailLPN =detailQBEBioBean.getValue("TOID");
				receiptLinenumber = detailQBEBioBean.get("RECEIPTLINENUMBER").toString();
				receiptDetailOwner = BioAttributeUtil.getString(detailQBEBioBean, "STORERKEY");
				sku = detailQBEBioBean.get("SKU").toString();
				pokey= detailQBEBioBean.getValue("POKEY");
				tariffKey = detailQBEBioBean.getValue("TARIFFKEY");
				validateTariffkey (tariffKey, context);
				packkey = packKeyValidation(context.getState(), asnDetail1,
						detailQBEBioBean);

				toLocValidation(context.getState(), detailQBEBioBean);

				uom = detailQBEBioBean.getValue("UOM").toString();
				if(detailQBEBioBean.getValue("TOID") != null)
				{
					lpn = detailQBEBioBean.getValue("TOID").toString();
				}
				expectedQty = asnDetail1.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
				receivedQty = asnDetail1.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();

				/* SATEESH - WM 9 3PL ENHANCEMENTS - WEIGHT TRACKING ENHANCEMENT -start
				 * Added by sateesh billa--starts
				 * for gross,net,tare weight validation
				 */
				double	grosswgt=0;
				double	netwgt=0;
				double	tarewgt=0;
				//recalculate weights
				CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();


				String enabledAdvCatWght=helper.isAdvCatchWeightEnabled(receiptDetailOwner,sku);
				if((enabledAdvCatWght!=null)&&(enabledAdvCatWght.equalsIgnoreCase("1")))
				{
					QBEBioBean qbe = (QBEBioBean)asnDetail1.getFocus();
					grosswgt = BioAttributeUtil.getDouble(qbe, "GROSSWGT");
					netwgt = BioAttributeUtil.getDouble(qbe, "NETWGT");
					tarewgt = BioAttributeUtil.getDouble(qbe, "TAREWGT");
					
					
/*					if(asnDetail1.getFormWidgetByName("GROSSWGT").getDisplayValue()!=null)
						grosswgt=new Double (asnDetail1.getFormWidgetByName("GROSSWGT").getDisplayValue()).doubleValue();
					if(asnDetail1.getFormWidgetByName("NETWGT").getDisplayValue()!=null)
						netwgt=new Double (asnDetail1.getFormWidgetByName("NETWGT").getDisplayValue()).doubleValue();
					if(asnDetail1.getFormWidgetByName("TAREWGT").getDisplayValue()!=null)
						tarewgt=new Double (asnDetail1.getFormWidgetByName("TAREWGT").getDisplayValue()).doubleValue();
*/
					if(!(grosswgt==netwgt+tarewgt))
					{
						throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", new Object[]{});
					}
				}
				/*
				 *  SATEESH - WM 9 3PL ENHANCEMENTS - WEIGHT TRACKING ENHANCEMENT--ENDS
				 * for gross,net,tare weight validation
				 */

				if(expectedQty == null) 
				{
					throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
				}

				if(receivedQty ==null)
				{
					throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
				}

				dexpqty = NumericValidationCCF.parseNumber(expectedQty);
				drecqty = NumericValidationCCF.parseNumber(receivedQty);

				if((! NumericValidationCCF.isNumber(expectedQty)) ||(dexpqty < 0)) 
				{
					throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
				}

				if((! NumericValidationCCF.isNumber(receivedQty)) || (drecqty < 0))
				{
					throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
				}

				if(! (recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
				{
					//AW SDIS: SCM-00000-06871 Machine: 2353530 start				
					detailQBEBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, expectedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					detailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, receivedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					//AW SDIS: SCM-00000-06871 Machine: 2353530 end
				}
				//				HC

				double cubicMeter = BioAttributeUtil.getDouble(detailQBEBioBean, "CUBICMETER");
				if (cubicMeter < 0)
				{
					String[] parameters = new String[2];
					parameters[0] = udfForm.getFormWidgetByName("CUBICMETER").getLabel("label", state.getLocale());
					parameters[1] = String.valueOf(cubicMeter);
					throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
				}
				double hundredWeight = BioAttributeUtil.getDouble(detailQBEBioBean, "HUNDREDWEIGHT");
				if (hundredWeight < 0)
				{
					String[] parameters = new String[2];
					parameters[0] = udfForm.getFormWidgetByName("HUNDREDWEIGHT").getLabel("label", state.getLocale());
					parameters[1] = String.valueOf(hundredWeight);
					throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
				}

				if(lpnDetailFocus instanceof QBEBioBean)
				{
					lpnDetailQBEBioBean = (QBEBioBean) lpnDetailFocus;
					lpnDetailQBEBioBean.set("RECEIPTKEY", headerFocus.getValue("RECEIPTKEY"));

					lpnuom = lpnDetailQBEBioBean.getValue("UOM").toString();
					lpnpackkey = lpnDetailQBEBioBean.getValue("PACKKEY").toString();

					lpnexpectedQty = lpnDetailForm.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
					lpnReceivedQty = lpnDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();
					if(lpnexpectedQty == null) 
					{
						throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
					}
					if(lpnReceivedQty ==null) 
					{
						throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
					}
					dlpnexpqty = NumericValidationCCF.parseNumber(lpnexpectedQty);
					dlpnrecqty = NumericValidationCCF.parseNumber(lpnReceivedQty);

					if ((!NumericValidationCCF.isNumber(lpnexpectedQty)) ||(dlpnexpqty < 0)) 
					{
						throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
					}

					if ((!NumericValidationCCF.isNumber(lpnReceivedQty)) || (dlpnrecqty < 0))
					{
						throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
					}

					//					AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start
					lpnDetailQBEBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnexpectedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					lpnDetailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnReceivedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					//					AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 end

					validateLPNDetail(lpnDetailFocus,headerFocus,context);
					detailQBEBioBean.addBioCollectionLink("LPNDETAILS", lpnDetailQBEBioBean);
				}
				//				HC

				//RM.S Serial Number - Catch Weight Catch Data
				//adding cwcd to cwcd biocollection
				if (cwcdFocus instanceof QBEBioBean)
				{
					//new RECEIPT, new DETAIL, new CWCD
					QBEBioBean cwcdQBE = (QBEBioBean) cwcdFocus;
					//validation
					cwcdValidationRequired(uowb, cwcdQBE, detailQBEBioBean);
					cwcdValidation(uowb, cwcdQBE, detailQBEBioBean);
					//validate SerialNumber
					cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
					//fix bug# 9177
					if(cwcdQBE.getValue("LOT") == null || "".equalsIgnoreCase(cwcdQBE.getValue("LOT").toString())){
						cwcdQBE.setValue("LOT", " ");
					}
					//end //fix bug# 9177
					detailQBEBioBean.addBioCollectionLink("LOTXIDDETAIL", cwcdQBE);
				}//RM.E	
				else if (cwcdFocus != null)
				{
					BioBean cwcdBean = (BioBean) cwcdFocus;
					if (cwcdBean.getUpdatedAttributes().size() > 0)
					{
						cwcdValidationRequired(uowb, cwcdBean, detailQBEBioBean);

						for (int i=0; i< cwcdBean.getUpdatedAttributes().size(); i++){
							String AttrUpdated = cwcdBean.getUpdatedAttributes().get(i).toString();
							if (AttrUpdated.equalsIgnoreCase("IOTHER1"))
							{
								cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
							}
						}
					}
				}
				//all validations have passed (hopefully) so it's now safe to make the BIOCOLLECTION link
				//previously this was done at the beginning, and this would cause an issue where if an exception it was thrown, and the user
				//saves again - 2 headerBioBean's were being created. this would cause issues.
				
				//RM 273529
				createNewBio = true;
				asnReceiptKey = headerFocus.getValue("RECEIPTKEY").toString();
				
			}
			else
			{//it is for update header
				headerBioBean = (BioBean)headerFocus;
				
				//Validate Door
				validateDoor(state, headerBioBean);

				if(!detailTab.getName().equals("wms_ASN_Line_List_View"))
				{
					detailFocus = detailTab.getFocus();
					SlotInterface detailTabGrpSlot = detailTab.getSubSlot("tbgrp_slot");
					supplierInfoForm = state.getRuntimeForm(detailTabGrpSlot, "tab 4");
					asnDetail1 = state.getRuntimeForm(detailTabGrpSlot, "tab 0");
					RuntimeFormInterface udfForm = state.getRuntimeForm(detailTabGrpSlot, "tab 2");

					expectedQty = asnDetail1.getFormWidgetByName("EXPECTEDQTY").getValue().toString();
					_log.debug("LOG_SYSTEM_OUT","EXP QTY = "+ expectedQty,100L);
					receivedQty = asnDetail1.getFormWidgetByName("RECEIVEDQTY").getValue().toString();	
					_log.debug("LOG_SYSTEM_OUT","REC QTY = "+ receivedQty,100L);
					if(expectedQty == null) 
					{
						throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
					}

					if(receivedQty ==null)
					{
						throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
					}
					dexpqty = NumericValidationCCF.parseNumber(expectedQty);
					drecqty = NumericValidationCCF.parseNumber(receivedQty);

					if((! NumericValidationCCF.isNumber(expectedQty)) ||(dexpqty < 0)) 
					{
						throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
					}

					if((! NumericValidationCCF.isNumber(receivedQty)) || (drecqty < 0))
					{
						throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
					}

					double cubicMeter = BioAttributeUtil.getDouble(detailFocus, "CUBICMETER");
					if (cubicMeter < 0)
					{
						String[] parameters = new String[2];
						parameters[0] = udfForm.getFormWidgetByName("CUBICMETER").getLabel("label", state.getLocale());
						parameters[1] = String.valueOf(cubicMeter);
						throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
					}
					double hundredWeight = BioAttributeUtil.getDouble(detailFocus, "HUNDREDWEIGHT");
					if (hundredWeight < 0)
					{
						String[] parameters = new String[2];
						parameters[0] = udfForm.getFormWidgetByName("HUNDREDWEIGHT").getLabel("label", state.getLocale());
						parameters[1] = String.valueOf(hundredWeight);
						throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
					}

					receiptOwner = headerBioBean.getValue("STORERKEY").toString();
					if(detailFocus.isTempBio())
					{
						//new asn line
						detailQBEBioBean = (QBEBioBean)detailFocus;
						detailQBEBioBean.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
						detailQBEBioBean.set("TYPE",headerBioBean.getValue("TYPE"));
						// 2012-09-02
					    // Modified by Will Pu
						// 新增ASN明细时, 如果LPN为空, 则自动使用当前ASN单号+新增ASN明细行号来填充LPN字段
						if(detailQBEBioBean.getValue("TOID") == null)
						{
							detailQBEBioBean.set("TOID", headerFocus.getValue("RECEIPTKEY").toString().trim() + 
									detailQBEBioBean.get("RECEIPTLINENUMBER").toString().trim());
						}

						headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
						recDetailType = detailQBEBioBean.getValue("TYPE").toString();
						objRecDetailLPN =detailQBEBioBean.getValue("TOID");
						receiptLinenumber = detailQBEBioBean.get("RECEIPTLINENUMBER").toString();
						receiptDetailOwner = BioAttributeUtil.getString(detailQBEBioBean, "STORERKEY");
						sku = detailQBEBioBean.get("SKU").toString();
						pokey= detailQBEBioBean.getValue("POKEY");
						tariffKey = detailQBEBioBean.getValue("TARIFFKEY");
						validateTariffkey (tariffKey, context);
						packkey = detailQBEBioBean.getValue("PACKKEY");

						/*
						 *  SATEESH - WM 9 3PL ENHANCEMENTS - WEIGHT TRACKING ENHANCEMENT--starts
						 * for gross,net,tare weight validation
						 */
						double	grosswgt=0;
						double	netwgt=0;
						double	tarewgt=0;

						if(detailQBEBioBean.getValue("GROSSWGT")!=null)
						{
							System.out.println("\n\n PRITHVI : "+detailQBEBioBean.getValue("GROSSWGT").toString());
							//grosswgt= ((Double) (detailQBEBioBean.getValue("GROSSWGT").)).doubleValue();
							grosswgt= Double.parseDouble(detailQBEBioBean.getValue("GROSSWGT").toString());
						}
						if(detailQBEBioBean.getValue("NETWGT")!=null)
							netwgt= Double.parseDouble(detailQBEBioBean.getValue("NETWGT").toString());
						if(detailQBEBioBean.getValue("TAREWGT")!=null)
							tarewgt= Double.parseDouble(detailQBEBioBean.getValue("TAREWGT").toString());


						if(!(grosswgt==netwgt+tarewgt))
						{
							String[] errorParam = new String[1];
							errorParam[0]= "null";
							throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", errorParam);
						}
						/*
						 *  SATEESH - WM 9 3PL ENHANCEMENTS - WEIGHT TRACKING ENHANCEMENT--ends
						 * for gross,net,tare weight validation
						 */


						if(packkey == null)
						{
							errorParam[0]= packkey.toString();
							asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
							detailQBEBioBean.set("PACKKEY","STD");
							throw new UserException("WMEXP_Pack_Validation", errorParam);
						}
						else if(!validateKey(packkey,"wm_pack", "PACKKEY", context.getState()))
						{
							errorParam[0]= packkey.toString();
							asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
							detailQBEBioBean.set("PACKKEY","STD");
							throw new UserException("WMEXP_Pack_Validation", errorParam);
						}

						toloc = detailQBEBioBean.getValue("TOLOC");
						if(toloc == null)
						{
							errorParam[0]= "null";
							throw new UserException("WMEXP_PICK_TOLOC", errorParam);
						}
						else if(!validateKey(toloc,"wm_location", "LOC", context.getState()))
						{
							errorParam[0]= toloc.toString();
							throw new UserException("WMEXP_PICK_TOLOC", errorParam);
						}

						uom = detailQBEBioBean.getValue("UOM").toString();
						if(detailQBEBioBean.getValue("TOID") != null)
						{
							lpn = detailQBEBioBean.getValue("TOID").toString();
						}

						if(! (recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
						{
							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start
							detailQBEBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA, expectedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							detailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA, receivedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start			
						}
						//						HC

						if(lpnDetailFocus instanceof QBEBioBean)
						{
							lpnDetailQBEBioBean = (QBEBioBean) lpnDetailFocus;
							lpnDetailQBEBioBean.set("RECEIPTKEY",headerBioBean.getValue("RECEIPTKEY"));

							lpnuom = lpnDetailQBEBioBean.getValue("UOM").toString();
							lpnpackkey = lpnDetailQBEBioBean.getValue("PACKKEY").toString();

							lpnexpectedQty = lpnDetailForm.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
							lpnReceivedQty = lpnDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();
							if(lpnexpectedQty == null) 
							{
								throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
							}

							if(lpnReceivedQty ==null) 
							{
								throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
							}
							dlpnexpqty = NumericValidationCCF.parseNumber(lpnexpectedQty);
							dlpnrecqty = NumericValidationCCF.parseNumber(lpnReceivedQty);

							if ((!NumericValidationCCF.isNumber(lpnexpectedQty)) ||(dlpnexpqty < 0)) 
							{
								throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
							}

							if ((!NumericValidationCCF.isNumber(lpnReceivedQty)) || (dlpnrecqty < 0))
							{
								throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
							}

							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 	
							lpnDetailQBEBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnexpectedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							lpnDetailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnReceivedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 

							validateLPNDetail(lpnDetailFocus,headerFocus,context);
							detailQBEBioBean.addBioCollectionLink("LPNDETAILS", lpnDetailQBEBioBean);
						}
						//						HC	

						//RM.S Serial Number - Catch Weight Catch Data
						//adding cwcd to cwcd biocollection
						if (cwcdFocus instanceof QBEBioBean)
						{
							//existing RECEIPT, new DETAIL, new CWCD
							QBEBioBean cwcdQBE = (QBEBioBean) cwcdFocus;
							//validation
							cwcdValidationRequired(uowb, cwcdQBE, detailQBEBioBean);
							cwcdValidation(uowb, cwcdQBE, detailQBEBioBean);
							//validate SerialNumber
							cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
							//fix bug# 9177
							if(cwcdQBE.getValue("LOT") == null || "".equalsIgnoreCase(cwcdQBE.getValue("LOT").toString())){
								cwcdQBE.setValue("LOT", " ");
							}
							// end fix bug# 9177
							detailQBEBioBean.addBioCollectionLink("LOTXIDDETAIL", cwcdQBE);
						}
						else if (cwcdFocus != null)
						{
							BioBean cwcdBean = (BioBean) cwcdFocus;
							if (cwcdBean.getUpdatedAttributes().size() > 0)
							{
								cwcdValidationRequired(uowb, cwcdBean, detailQBEBioBean);

								for (int i=0; i< cwcdBean.getUpdatedAttributes().size(); i++){
									String AttrUpdated = cwcdBean.getUpdatedAttributes().get(i).toString();
									if (AttrUpdated.equalsIgnoreCase("IOTHER1"))
									{
										cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
									}
								}
							}
						}
						//RM.E
					}
					else
					{
						//existing asn line
						detailBioBean = (BioBean)detailFocus;
						recDetailType = detailBioBean.getValue("TYPE").toString();
						objRecDetailLPN =detailBioBean.getValue("TOID");
						receiptLinenumber = detailBioBean.get("RECEIPTLINENUMBER").toString();
						receiptDetailOwner = BioAttributeUtil.getString(detailBioBean, "STORERKEY");
						sku = detailBioBean.get("SKU").toString();
						pokey= detailBioBean.getValue("POKEY");
						tariffKey = detailBioBean.getValue("TARIFFKEY");
						validateTariffkey (tariffKey, context);
						packkey = detailBioBean.getValue("PACKKEY");
						if(packkey == null)
						{
							errorParam[0]= "null";
							asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
							detailBioBean.set("PACKKEY","STD");
							throw new UserException("WMEXP_Pack_Validation", errorParam);
						}
						else if(!validateKey(packkey,"wm_pack", "PACKKEY", context.getState()))
						{
							errorParam[0]= packkey.toString();
							asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
							detailBioBean.set("PACKKEY","STD");
							throw new UserException("WMEXP_Pack_Validation", errorParam);
						}

						toloc = detailBioBean.getValue("TOLOC");
						if(toloc == null)
						{
							errorParam[0]= "null";
							throw new UserException("WMEXP_PICK_TOLOC", errorParam);
						}
						else if(!validateKey(toloc,"wm_location", "LOC", context.getState()))
						{
							errorParam[0]= toloc.toString();
							throw new UserException("WMEXP_PICK_TOLOC", errorParam);
						}

						uom = detailBioBean.getValue("UOM").toString();


						/*
						 * Added by sateesh billa-3PL Enhancements-starts
						 * for gross,net,tare weight validation
						 */
						double	grosswgt=0;
						double	netwgt=0;
						double	tarewgt=0;
						if(detailBioBean.getValue("GROSSWGT")!=null)
							grosswgt= ((Double) (detailBioBean.getValue("GROSSWGT"))).doubleValue();
						if(detailBioBean.getValue("NETWGT")!=null)
							netwgt= ((Double) (detailBioBean.getValue("NETWGT"))).doubleValue();
						if(detailBioBean.getValue("TAREWGT")!=null)
							tarewgt= ((Double) (detailBioBean.getValue("TAREWGT"))).doubleValue();


						if(!(grosswgt==netwgt+tarewgt))
						{
							String[] errorParam = new String[1];
							errorParam[0]= "null";
							throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", errorParam);
						}
						/*
						 * Added by sateesh billa--3PL Enhancements--ends
						 * for gross,net,tare weight validation
						 */



						if(lpnDetailFocus instanceof QBEBioBean)
						{
							lpnDetailQBEBioBean = (QBEBioBean)lpnDetailFocus;
							detailBioBean.addBioCollectionLink("LPNDETAILS", lpnDetailQBEBioBean);
							if(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5"))
							{
								lpnDetailQBEBioBean.set("RECEIPTKEY",detailBioBean.getValue("RECEIPTKEY"));
								lpnuom = lpnDetailQBEBioBean.getValue("UOM").toString();
								lpnpackkey = lpnDetailQBEBioBean.getValue("PACKKEY").toString();
								lpnexpectedQty = lpnDetailForm.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
								lpnReceivedQty = lpnDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();

								if(lpnexpectedQty == null)
								{
									throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
								}
								if(lpnReceivedQty ==null)
								{
									throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
								}
								dlpnexpqty = NumericValidationCCF.parseNumber(lpnexpectedQty);
								dlpnrecqty = NumericValidationCCF.parseNumber(lpnReceivedQty);

								if(!NumericValidationCCF.isNumber(lpnexpectedQty) ||dlpnexpqty<0)
								{
									throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
								}

								if(!NumericValidationCCF.isNumber(lpnReceivedQty) || dlpnrecqty<0)
								{
									throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
								}

								//								AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start
								//jp.answerlink.212525.begin
								lpnDetailQBEBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA,lpnexpectedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
								lpnDetailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnReceivedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
								//jp.answerlink.212525.end
								//								AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 end

								validateLPNDetail(lpnDetailFocus,headerFocus,context);
								detailBioBean.addBioCollectionLink("LPNDETAILS", lpnDetailQBEBioBean);
								lpnIsNew = true;
							}	
						}
						else if(lpnDetailFocus instanceof BioBean)
						{
							detailBioBean = (BioBean)detailFocus;
							lpnDetailBioBean = (BioBean) lpnDetailFocus;
							lpnuom = lpnDetailBioBean.getValue("UOM").toString();
							lpnpackkey = lpnDetailBioBean.getValue("PACKKEY").toString();
							lpnexpectedQty = lpnDetailForm.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
							lpnReceivedQty = lpnDetailForm.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();
							if(lpnexpectedQty == null) 
							{
								throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
							}
							if(lpnReceivedQty ==null)
							{
								throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
							}
							dlpnexpqty = NumericValidationCCF.parseNumber(lpnexpectedQty);
							dlpnrecqty = NumericValidationCCF.parseNumber(lpnReceivedQty);

							if(!NumericValidationCCF.isNumber(lpnexpectedQty) ||dlpnexpqty<0){
								throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
							}
							if(!NumericValidationCCF.isNumber(lpnReceivedQty) || dlpnrecqty<0){
								throw new UserException("WMEXP_RECQTY_NAN", new Object[]{});
							}

							//							AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start
							lpnDetailBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA,lpnexpectedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							lpnDetailBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),lpnuom, UOMMappingUtil.UOM_EA, lpnReceivedQty, lpnpackkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							//							AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 end

							validateLPNDetail(lpnDetailFocus,headerFocus,context);
						}
						if(detailBioBean.getValue("TOID") != null)
						{
							lpn= detailBioBean.getValue("TOID").toString();
						}
						if(! (recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
						{
							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 
							//jp.answerlink.295662.begin
							//Updated Bio quantities only if they are different from original value.
							//
							
							String numberStrQtyExpected = new Double(NumericValidationCCF.parseNumber(expectedQty)).toString();
							String numberStrQtyReceived = new Double(NumericValidationCCF.parseNumber(receivedQty)).toString();
							String convertedQtyExpected  = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, numberStrQtyExpected, packkey.toString(), state, UOMMappingUtil.uowNull, true);
							String convertedQtyReceived = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, numberStrQtyReceived, packkey.toString(), state, UOMMappingUtil.uowNull, true);
												
//							String convertedQtyExpected  = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, expectedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true);
//							String convertedQtyReceived = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, receivedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true);
							
							
							BigDecimal savedQtyExpected = new BigDecimal(BioAttributeUtil.getDouble(detailBioBean, "QTYEXPECTED", true));//new BigDecimal(expectedQty);
							BigDecimal savedQtyReceived = new BigDecimal(BioAttributeUtil.getDouble(detailBioBean, "QTYRECEIVED", true));//new BigDecimal(receivedQty); 

							if(savedQtyExpected.compareTo(new BigDecimal(convertedQtyExpected))==0){
								_log.debug("SaveASNReceipt", "Expected Qty - They are identical", 100L);
							}else{
								detailBioBean.set("QTYEXPECTED",convertedQtyExpected);	
							}
							
							if(savedQtyReceived.compareTo(new BigDecimal(convertedQtyReceived))==0){
								_log.debug("SaveASNReceipt", "Received Qty - They are identical", 100L);
							}else{
								detailBioBean.set("QTYRECEIVED",convertedQtyReceived);	
							}

							//detailBioBean.set("QTYEXPECTED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA,expectedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							//detailBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA,receivedQty, packkey.toString(), state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
							//jp.answerlink.295662.end
							
							//AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 

						}

						//RM.S Serial Number - Catch Weight Catch Data
						//adding cwcd to cwcd biocollection
						if (cwcdFocus instanceof QBEBioBean)
						{
							//existing RECEIPT, existing DETAIL, new CWCD
							QBEBioBean cwcdQBE = (QBEBioBean) cwcdFocus;
							//validation					
							cwcdValidationRequired(uowb, cwcdQBE, detailBioBean);
							cwcdValidation(uowb, cwcdQBE, detailBioBean);
							//fix bug# 9177
							if(cwcdQBE.getValue("LOT") == null || "".equalsIgnoreCase(cwcdQBE.getValue("LOT").toString())){
								cwcdQBE.setValue("LOT", " ");
							}
							//end fix bug# 9177
							//validate SerialNumber
							cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
							detailBioBean.addBioCollectionLink("LOTXIDDETAIL", cwcdQBE);
							cwcdIsNew = true;
						}
						else if (cwcdFocus != null)
						{
							BioBean cwcdBean = (BioBean) cwcdFocus;
							if (cwcdBean.getUpdatedAttributes().size() > 0)
							{
								cwcdValidationRequired(uowb, cwcdBean, detailBioBean);
								//existing RECEIPT, existing DETAIL, existing CWCD
								//validation
								cwcdIsNew = true;

								for (int i=0; i< cwcdBean.getUpdatedAttributes().size(); i++){
									String AttrUpdated = cwcdBean.getUpdatedAttributes().get(i).toString();
									if (AttrUpdated.equalsIgnoreCase("IOTHER1"))
									{
										cwcdValidateSerialNumber(cwcdFocus, receiptDetailOwner, sku);
									}
								}
							}
						}
						//RM.E
					}
				}

				if(detailListTab.getName().equals("wms_ASN_Line_List_View")) 
				{
					verifyReceiptDetailList(context, detailListTab,headerFocus);
				}
			}
		}

		//************************** LPN Validation**************************
		if(detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view"))
		{	//if the slot is populated by toggle form then
			if(!detailTab.getName().equals("wms_ASN_Line_List_View"))
			{
				if(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")){
					if(objRecDetailLPN == null || objRecDetailLPN.toString().equals(" "))
					{
						//jp.answerlink.212525.begin
						uowb.clearState();
						//jp.answerlink.212525.end
						throw new UserException("WMEXP_LPN_REQUIRED", new Object[]{});
					}
					else
					{		
						if(recDetailType.equalsIgnoreCase("4"))
						{
							if(objRecDetailLPN.toString().length() != 18)
							{
								errorParam[0]= receiptLinenumber;
								throw new UserException("WMEXP_LPN_LENGTH_01", errorParam);
							}

							if(!sscccompliant(objRecDetailLPN.toString()))
							{
								errorParam[0]= objRecDetailLPN.toString(); 
								errorParam[1]= receiptLinenumber;
								throw new UserException("WMWXP_LPN_NOTSSCC_01", errorParam);
							}
						}
						validateReceiptDetailLPN(detailFocus, context);
					}
				}				

				// 2012-07-03
				// Modified by Will Pu
				// 自SO填充的订单不需要做PO检验
				if (!"2".equals(recDetailType)) {
					validatePOKey(pokey, context.getState());
				}
				
				validateQCResults(detailFocus,context);
				isNullLottable(detailFocus);

				if(!(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
				{
					String LottableValidationKey = getFieldValuefromSku(receiptDetailOwner,sku,"LOTTABLEVALIDATIONKEY",context);
					BioCollectionBean LottableValidationCollection = getlottableValidation(LottableValidationKey, context);
					checkLottableValidations(LottableValidationCollection,headerFocus, detailFocus, context);
				}				

				String ReceiptValidationKey = getFieldValuefromSku(receiptDetailOwner,sku,"RECEIPTVALIDATIONTEMPLATE",context);
				receiptValidations(ReceiptValidationKey, pokey, receiptLinenumber, sku, context);

				asnReceiptKey = headerBioBean.getValue("RECEIPTKEY").toString();
			}
		}
		else
		{
			if(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5"))
			{
				if(objRecDetailLPN == null || objRecDetailLPN.toString().equals(" "))
				{
					//jp.answerlink.212525.begin
					uowb.clearState();
					//jp.answerlink.212525.end
					throw new UserException("WMEXP_LPN_REQUIRED", new Object[]{});
				}
				else
				{		
					if(recDetailType.equalsIgnoreCase("4"))
					{
						if(objRecDetailLPN.toString().length() != 18)
						{
							errorParam[0]= receiptLinenumber;
							throw new UserException("WMEXP_LPN_LENGTH_01", errorParam);
						}

						if(!sscccompliant(objRecDetailLPN.toString()))
						{
							errorParam[0]= objRecDetailLPN.toString(); 
							errorParam[1]= receiptLinenumber;
							throw new UserException("WMWXP_LPN_NOTSSCC_01", errorParam);
						}
					}
					validateReceiptDetailLPN(detailFocus, context);
				}
			}

			validatePOKey (pokey, context.getState());

			//RM 8024 Added check to prevent checking if saving from List
			if(detailFocus != null) 
			{
				validateQCResults(detailFocus,context);
			}


			//RM 8024 Added check to prevent checking if saving from List
			if(detailFocus != null) 
			{
				if(!(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
				{
					String LottableValidationKey = getFieldValuefromSku(receiptDetailOwner,sku,"LOTTABLEVALIDATIONKEY",context);
					BioCollectionBean LottableValidationCollection = getlottableValidation(LottableValidationKey, context);
					checkLottableValidations(LottableValidationCollection,headerFocus, detailFocus, context);
				}
			}

			String ReceiptValidationKey = getFieldValuefromSku(receiptDetailOwner,sku,"RECEIPTVALIDATIONTEMPLATE",context);
			receiptValidations(ReceiptValidationKey, pokey, receiptLinenumber, sku, context);

			if(headerBioBean != null && headerBioBean.getValue("RECEIPTKEY") != null) 
			{
				asnReceiptKey = headerBioBean.getValue("RECEIPTKEY").toString();
			}
		}

		dateCodeModsProcess(context, detailFocus, sku, receiptDetailOwner);

		//RM 273529
		if(createNewBio == true) {
			headerBioBean = uowb.getNewBio((QBEBioBean) headerFocus);
			headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
		}

		try
		{			
			uowb.saveUOW(true);
		}
		catch (UnitOfWorkException e)
		{
			Throwable nested = (e).findDeepestNestedException();
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				throwUserException(e, reasonCode, null);
			}
		}

		if(savingFromMainASNList == false) 
		{
			asnReceiptKey = headerBioBean.getValue("RECEIPTKEY").toString();
			updateASNStatus(asnReceiptKey);
			
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - Start
			if(!((headerBioBean.getValue("TEMPERATURE") == null) || (headerBioBean.getValue("TEMPERATURE") == ""))){

				headerTemperature1 = headerBioBean.getValue("TEMPERATURE").toString();
				if((!(headerTemperature1.length() == 0) || (headerTemperature1 == null) || (headerTemperature1 == ""))){
					StateInterface state1 = context.getState();
					UnitOfWorkBean uowb1 = state1.getDefaultUnitOfWork();
					BioCollectionBean rs = uowb1.getBioCollectionBean(new Query("receiptdetail", "receiptdetail.RECEIPTKEY = '" + asnReceiptKey+ "'"+" and receiptdetail.TEMPERATURE is null", null));


					try
					{
						for (int i = 0; i < rs.size(); i++) {
							BioBean receiptdetail = rs.get("" + i);
							receiptdetail.setValue("TEMPERATURE",headerTemperature1);
							receiptdetail.save();	
						}
						uowb.saveUOW();
					}
					catch(EpiException ex)
					{
						throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
					}

				}
			}
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End

			if(detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view"))
			{	//if the slot is populated by toggle form then
				if(!(detailTab.getName().equals("wms_ASN_Line_List_View")))
				{
					updateSupplierInfo(supplierInfoForm, asnReceiptKey, receiptLinenumber, lpn, context);
				}
			}
			else
			{
				updateSupplierInfo(supplierInfoForm, asnReceiptKey, receiptLinenumber, lpn, context);
			}
		}
		else 
		{
			for(String receiptKey : asnReceiptKeys)
			{
				_log.debug("LOG_DEBUG_EXTENSION_SaveASNReceipt_execute", "Calling Update Status SP on " + receiptKey, SuggestedCategory.NONE);
				updateASNStatus(receiptKey);
			}
		}

		if(savingFromMainASNList == false) 
		{
			setNavigation(lpnIsNew, cwcdIsNew, context);
			uowb.clearState();
			//List isn't being refreshed
			BioCollectionBean rs = uowb.getBioCollectionBean(new Query("receipt", "receipt.RECEIPTKEY = '" + headerBioBean.getValue("RECEIPTKEY") + "'", null));
			for(int i = 0; i < rs.size(); i++)
			{
				BioBean bioBean = rs.get("" + i);
				result.setFocus(bioBean);
			}
		}
		else 
		{
			_log.debug("LOG_DEBUG_EXTENSION_SaveASNReceipt_execute", "Navigating to Receipt List View", SuggestedCategory.NONE);
			context.setNavigation("clickEventListSave");
			uowb.clearState();
			result.setFocus(headerBioBean);
		}

		// 2012-07-20
		// Modified by Will Pu
		// ���޸�ASNͷ��Ϣ�п��غ�, ͬ������Ϣ����ϸ��
		String receiptkey = headerFocus.getValue("RECEIPTKEY").toString();
		String locaddress = headerFocus.getValue("LOCADDRESS") == null ? null : headerFocus.getValue("LOCADDRESS").toString();
		
		if (locaddress == null || locaddress.trim().equals("")) {
			return RET_CONTINUE;
		}
		
		HttpSession session = context.getState().getRequest().getSession();

		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String query = "UPDATE RECEIPTDETAIL SET LOTTABLE02 = '" + locaddress + "' WHERE RECEIPTKEY = '" + receiptkey + "'";
		
    	try {
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		
    		appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
    	} catch (SQLException e) {			
		}
		
		return RET_CONTINUE;
	}

	private void validateDoor(StateInterface state, DataBean headerFocus) throws EpiDataException, UserException {
		String door = org.apache.commons.lang.StringUtils.upperCase(BioAttributeUtil.getString(headerFocus, "DOOR"));
		if(org.apache.commons.lang.StringUtils.isNotBlank(door))
		{
			String query = "wm_location.LOC = '" + door + "' and ( wm_location.LOCATIONTYPE = 'DOOR' OR wm_location.LOCATIONTYPE = 'STAGED' )";
			BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_location", query, null));
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
			if (rs.size() < 1)
			{
				String[] parameters = new String[1];
				parameters[0] = door;
				throw new UserException("WMEXP_INVALID_DOOR", parameters);
			}
			headerFocus.setValue("DOOR", door);
		}
		
		
	}

	public Object packKeyValidation(StateInterface state,
			RuntimeFormInterface asnDetail1, DataBean receiptDetail)
			throws UserException, EpiException {
		Object packkey;
		packkey = receiptDetail.getValue("PACKKEY");
		if (packkey == null)
		{
			errorParam[0]= "null";
			asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
			receiptDetail.setValue("PACKKEY","STD");
			throw new UserException("WMEXP_Pack_Validation", errorParam);
		}
		else if(!(validateKey(packkey,"wm_pack", "PACKKEY", state)))
		{
			errorParam[0]= packkey.toString();
			asnDetail1.getFormWidgetByName("PACKKEY").setDisplayValue("STD");		//set to STD if invalid pack
			receiptDetail.setValue("PACKKEY","STD");
			throw new UserException("WMEXP_Pack_Validation", errorParam);
		}
		return packkey;
	}

	/**
	 * Date code mods process.
	 *
	 * @param context the context
	 * @param detailFocus the detail focus
	 * @param sku the sku
	 * @param receiptDetailOwner the receipt detail owner
	 * @throws EpiException the epi exception
	 */
	public void dateCodeModsProcess(ActionContext context,
			DataBean detailFocus, String sku, String receiptDetailOwner)
			throws EpiException {
		// *******************************************************
		// DATE CODE MODS
		//
		// This next block perform mfg/expire date updates based
		// on lottable04, lottable05, lottable11, and lottable12.
		//
		// NOTE: If the following rules change, be sure to update 
		// RFReceiveBase.java to apply the same rules to the RF.
		// *******************************************************
		if(detailFocus != null) 
		{
			Object lottable04 = null, lottable05 = null, lottable11 = null, lottable12 = null;
			Object recvQtyObj = null;
			String receiveQtyStr = "0.0";
			if(detailFocus instanceof QBEBioBean){
				QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
				lottable04= qbeDetailFocus.getValue("LOTTABLE04");
				lottable05 = qbeDetailFocus.getValue("LOTTABLE05");
				lottable11 = qbeDetailFocus.getValue("LOTTABLE11");
				lottable12 = qbeDetailFocus.getValue("LOTTABLE12");
				recvQtyObj = qbeDetailFocus.getValue("RECEIVEDQTY");			
			}else if(detailFocus instanceof BioBean){
				BioBean bioDetailFocus = (BioBean)detailFocus;
				lottable04= bioDetailFocus.getValue("LOTTABLE04");
				lottable05 = bioDetailFocus.getValue("LOTTABLE05");
				lottable11 = bioDetailFocus.getValue("LOTTABLE11");
				lottable12 = bioDetailFocus.getValue("LOTTABLE12");
				recvQtyObj = bioDetailFocus.getValue("RECEIVEDQTY");
			}

			if ( recvQtyObj != null )
			{
				receiveQtyStr = LocaleUtil.resetQtyToDecimalForBackend(recvQtyObj.toString()); //AW 
			}
			NumberFormat nf = NumberFormat.getInstance(LocaleUtil.getCurrencyLocale());
			Double doubQtyReceived = null;
			try {
				doubQtyReceived = nf.parse(receiveQtyStr).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ( doubQtyReceived > 0)
			{
				// get the intervals from the item master
				//
				int toExpireDays = Integer.parseInt(getFieldValuefromSku(receiptDetailOwner,sku,"TOEXPIREDAYS",context));
				int toDeliverByDays = Integer.parseInt(getFieldValuefromSku(receiptDetailOwner,sku,"TODELIVERBYDAYS",context));
				int toBestByDays = Integer.parseInt(getFieldValuefromSku(receiptDetailOwner,sku,"TOBESTBYDAYS",context));

				// first check to see if we have an mfg date.  if so,
				// adjust the other dates based on the mfg dates and the
				// appropriate intervals, unless a date is already provided.
				//
				if (lottable04 != null)
				{

					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );

				}

				// next check to see if we have a expiration date.  if so,
				// first set the mfg date, then adjust the other dates based 
				// on the mfg date and the appropriate intervals, unless a 
				// date is already provided.
				//
				if (lottable05 != null )
				{
					lottable04 = adjustLottableDays(lottable05, lottable04, toExpireDays, false );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );
				}

				// next check to see if we have an best by date...
				//
				if (lottable12 != null )
				{
					lottable04 = adjustLottableDays(lottable12, lottable04, toBestByDays, false );
					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
				}

				// finally check to see if we have a delivery by date...
				//
				if (lottable11 != null )
				{
					lottable04 = adjustLottableDays(lottable11, lottable04, toDeliverByDays, false );
					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );
				}

				if(detailFocus instanceof QBEBioBean){
					QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
					qbeDetailFocus.setValue("LOTTABLE04", lottable04);
					qbeDetailFocus.setValue("LOTTABLE05", lottable05);
					qbeDetailFocus.setValue("LOTTABLE11", lottable11);
					qbeDetailFocus.setValue("LOTTABLE12", lottable12);
				}else if(detailFocus instanceof BioBean){
					BioBean bioDetailFocus = (BioBean)detailFocus;
					bioDetailFocus.setValue("LOTTABLE04", lottable04);
					bioDetailFocus.setValue("LOTTABLE05", lottable05);
					bioDetailFocus.setValue("LOTTABLE11", lottable11);
					bioDetailFocus.setValue("LOTTABLE12", lottable12);
				}
			}
		}
	}

	/**
	 * To loc validation.
	 *
	 * @param state the state
	 * @param detailQBEBioBean the detail qbe bio bean
	 * @throws UserException the user exception
	 * @throws EpiException the epi exception
	 */
	public void toLocValidation(StateInterface state, DataBean detailQBEBioBean)
			throws UserException, EpiException {
		Object toloc;
		toloc = detailQBEBioBean.getValue("TOLOC");
		if (toloc == null) {
			errorParam[0] = "null";
			throw new UserException("WMEXP_PICK_TOLOC", errorParam);
		} else if (!(validateKey(toloc, "wm_location", "LOC", state))) {
			errorParam[0] = toloc.toString();
			throw new UserException("WMEXP_PICK_TOLOC", errorParam);
		}
	}

	/**
	 * Update carrier info.
	 *
	 * @param state the state
	 * @param receipt the receipt
	 * @param carrierKey the carrier key
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	public static void updateCarrierInfo(StateInterface state, DataBean receipt,
			String carrierKey) throws EpiDataException, UserException {
		carrierKey = carrierKey.toUpperCase();
		//validate carrier
		final String type = "3";
		final String bio = "wm_storer";
		final String attribute = "STORERKEY";
		String sQueryString = bio + "." + attribute + " = '" + carrierKey + "'" + " AND " + bio + ".TYPE = " + type;
		Query BioQuery = new Query(bio,sQueryString,null);
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
		int size = listCollection.size();
		if  (size != 0) 
		{
			//populate carrier info
			receipt.setValue("CARRIERADDRESS1",  listCollection.get("0").getValue("ADDRESS1"));
			receipt.setValue("CARRIERADDRESS2",  listCollection.get("0").getValue("ADDRESS2"));
			receipt.setValue("CARRIERCITY",  listCollection.get("0").getValue("CITY"));
			receipt.setValue("CarrierCountry",  listCollection.get("0").getValue("COUNTRY"));
			receipt.setValue("CARRIERSTATE",  listCollection.get("0").getValue("STATE"));
			receipt.setValue("CARRIERZIP",  listCollection.get("0").getValue("ZIP"));
			receipt.setValue("CarrierPhone",  listCollection.get("0").getValue("PHONE1"));
			receipt.setValue("CARRIERNAME",  listCollection.get("0").getValue("COMPANY"));
		}
		else 
		{
			receipt.setValue("CARRIERKEY","");
			receipt.setValue("CARRIERADDRESS1",  "");
			receipt.setValue("CARRIERADDRESS2",  "");
			receipt.setValue("CARRIERCITY",  "");
			receipt.setValue("CarrierCountry", "");
			receipt.setValue("CARRIERSTATE",  "");
			receipt.setValue("CARRIERZIP",  "");
			receipt.setValue("CarrierPhone", "");
			receipt.setValue("CARRIERNAME",  "");
			throw new UserException("WMEXP_INVALID_CARRIER", new Object[]{carrierKey});
		}

		receipt.setValue("CARRIERKEY", carrierKey);
	}
	
	public static void updateSupplierInfo(StateInterface state, DataBean receipt,
			String supplierCode, String type) throws EpiDataException, UserException {
		supplierCode = supplierCode.toUpperCase();
		//validate carrier
		final String bio = "wm_storer";
		final String attribute = "STORERKEY";
		String sQueryString = bio + "." + attribute + " = '" + supplierCode + "'" + " AND " + bio + ".TYPE = " + type;
		Query BioQuery = new Query(bio,sQueryString,null);
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
		int size = listCollection.size();
		if  (size != 0) 
		{
			//populate carrier info
			receipt.setValue("ShipFromAddressLine1",  listCollection.get("0").getValue("ADDRESS1"));
			receipt.setValue("ShipFromAddressLine2",  listCollection.get("0").getValue("ADDRESS2"));
			receipt.setValue("ShipFromCity",  listCollection.get("0").getValue("CITY"));
			receipt.setValue("CarrierCountry",  listCollection.get("0").getValue("COUNTRY"));
			receipt.setValue("ShipFromState",  listCollection.get("0").getValue("STATE"));
			receipt.setValue("ShipFromZip",  listCollection.get("0").getValue("ZIP"));
			receipt.setValue("ShipFromPhone",  listCollection.get("0").getValue("PHONE1"));
			receipt.setValue("SupplierName",  listCollection.get("0").getValue("COMPANY"));
		}
		else 
		{
			receipt.setValue("SupplierCode","");
			receipt.setValue("ShipFromAddressLine1",  "");
			receipt.setValue("ShipFromAddressLine2",  "");
			receipt.setValue("ShipFromCity",  "");
			receipt.setValue("CarrierCountry", "");
			receipt.setValue("ShipFromState",  "");
			receipt.setValue("ShipFromZip",  "");
			receipt.setValue("ShipFromPhone", "");
			receipt.setValue("SupplierName",  "");
			throw new UserException("WMEXP_INVALID_SUPPLIER", new Object[]{supplierCode});
		}

		receipt.setValue("SupplierCode", supplierCode);
	}

	/**
	 * Cwcd validation required.
	 *
	 * @param uow the uow
	 * @param cwcdBean the cwcd bean
	 * @param detailBean the detail bean
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	private void cwcdValidationRequired(UnitOfWorkBean uow, DataBean cwcdBean, DataBean detailBean) throws UserException, EpiDataException {

		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '"
				+ detailBean.getValue("STORERKEY")
				+ "' and sku.SKU = '"
				+ detailBean.getValue("SKU")
				+ "'", null));
		if (rs.size() == 0) 
		{
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { detailBean.getValue("SKU"), detailBean.getValue("STORERKEY") });
		}

		String icdflag = "0";
		String iother1 = "";
		String iother2 = "";
		String iother3 = "";
		String iother4 = "";
		String iother5 = "";
		String e2eflag = "0";

		Object icdflagObj = rs.get("0").getValue("ICDFLAG");
		Object iother1Obj = rs.get("0").getValue("ICDLABEL1");
		Object iother2Obj = rs.get("0").getValue("ICDLABEL2");
		Object iother3Obj = rs.get("0").getValue("ICDLABEL3");
		Object iother4Obj = rs.get("0").getValue("ICDLABEL4");
		Object iother5Obj = rs.get("0").getValue("ICDLABEL5");
		Object e2eflagObj = rs.get("0").getValue("SNUM_ENDTOEND");

		if (icdflagObj != null)
			icdflag = icdflagObj.toString();
		if (iother1Obj != null)
			iother1 = iother1Obj.toString();
		if (iother2Obj != null)
			iother2 = iother2Obj.toString();
		if (iother3Obj != null)
			iother3 = iother3Obj.toString();
		if (iother4Obj != null)
			iother4 = iother4Obj.toString();
		if (iother5Obj != null)
			iother5 = iother5Obj.toString();
		if (e2eflagObj != null)
			e2eflag = e2eflagObj.toString();

		if ( e2eflag.equalsIgnoreCase("1") || 
				(icdflag.equalsIgnoreCase("1") && iother1.trim().length() > 0) )
		{
			Object serObj = cwcdBean.getValue("SERIALNUMBERLONG");
			if (serObj == null || serObj.toString().trim().length() == 0)
			{
				throw new UserException("WMEXP_SN_REQUIRED", new String[] { " " });
			}
		}
		if (icdflag.equalsIgnoreCase("1") && iother2.trim().length() > 0)
		{
			Object dataObj = cwcdBean.getValue("IOTHER2");
			if (dataObj == null || dataObj.toString().trim().length() == 0)
			{
				throw new UserException("WMEXP_IOTHER2_REQUIRED", new String[] { " " });
			}
		}
		if (icdflag.equalsIgnoreCase("1") && iother3.trim().length() > 0)
		{
			Object dataObj = cwcdBean.getValue("IOTHER3");
			if (dataObj == null || dataObj.toString().trim().length() == 0)
			{
				throw new UserException("WMEXP_IOTHER3_REQUIRED", new String[] { " " });
			}
		}
		if (icdflag.equalsIgnoreCase("1") && iother4.trim().length() > 0)
		{
			Object dataObj = cwcdBean.getValue("IOTHER4");
			if (dataObj == null || dataObj.toString().trim().length() == 0)
			{
				throw new UserException("WMEXP_IOTHER4_REQUIRED", new String[] { " " });
			}
		}
		if (icdflag.equalsIgnoreCase("1") && iother5.trim().length() > 0)
		{
			Object dataObj = cwcdBean.getValue("IOTHER5");
			if (dataObj == null || dataObj.toString().trim().length() == 0)
			{
				throw new UserException("WMEXP_IOTHER5_REQUIRED", new String[] { " " });
			}
		}
	}

	/**
	 * Cwcd validation.
	 *
	 * @param uow the uow
	 * @param cwcdQBE the cwcd qbe
	 * @param detailQBEBioBean the detail qbe bio bean
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	private void cwcdValidation(UnitOfWorkBean uow, DataBean cwcdQBE, DataBean detailQBEBioBean) throws UserException, EpiDataException {
		// ensure correct values are saved
		if (detailQBEBioBean.getValue("TOID") != null) 
		{
			cwcdQBE.setValue("ID", detailQBEBioBean.getValue("TOID"));
		}
		cwcdQBE.setValue("SKU", detailQBEBioBean.getValue("SKU"));

		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '"
				+ detailQBEBioBean.getValue("STORERKEY")
				+ "' and sku.SKU = '"
				+ detailQBEBioBean.getValue("SKU")
				+ "'", null));
		if (rs.size() == 0) 
		{
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { detailQBEBioBean.getValue("SKU"), detailQBEBioBean.getValue("STORERKEY") });
		}
		cwcdQBE.setValue("CAPTUREBY", rs.get("0").getValue("ICWBY"));
	}

	/**
	 * Verify receipt detail list.
	 *
	 * @param context the context
	 * @param detailListTab the detail list tab
	 * @param headerFocus the header focus
	 * @throws EpiDataException the epi data exception
	 * @throws EpiDataInvalidAttrException the epi data invalid attr exception
	 * @throws UserException the user exception
	 * @throws EpiException the epi exception
	 */
	private void verifyReceiptDetailList(ActionContext context, RuntimeListFormInterface detailListTab, DataBean headerFocus) throws EpiDataException, EpiDataInvalidAttrException, UserException, EpiException 
	{		
		BioCollectionBean detailListCollection = (BioCollectionBean) detailListTab.getFocus();
		StateInterface state = context.getState(); //SRG: Incident4334565_Defect302819
		try {
			//SRG Begin: Incident4334565_Defect302819			
			BioCollectionBean listFocus = (BioCollectionBean) detailListTab.getFocus();
			String interactionID = state.getInteractionId();		
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			HttpSession session = context.getState().getRequest().getSession();
			//This value is set in the ASNReceiptLineListPreRender extension since this cannot be obtained in this extension.
			sessionObjectValue = (String)session.getAttribute(sessionVariable);
			
			int pageStart = Integer.parseInt(sessionObjectValue);
			int pageSize = detailListTab.getWindowSize();				
			int totalListSize = listFocus.size();  //Total size of the item list
			int totPrevItems = (totalListSize - pageStart); //Total no of items in the previous pages
			int startValue = 0;
			int endValue = 0;
			if (totPrevItems > pageSize) {
				startValue = pageStart;
				endValue = pageStart + pageSize;
			}
			else { //Last page in the ASN detail list
				startValue = pageStart;
				endValue = totalListSize;
			}			
			//for(int i = 0; i < detailListCollection.size(); i++)
			for(int i = startValue; i < endValue; i++)
            //SRG End: Incident4334565_Defect302819
			{
				BioBean detailBio = detailListCollection.get("" + i);
				final String recDetailType = detailBio.getValue("TYPE").toString();
				String owner = detailBio.getValue("STORERKEY").toString();
				String sku = detailBio.getValue("SKU").toString();

				if(detailBio.hasBeenUpdated("TOID"))
				{
					//LPN Validation
					String receiptLinenumber = (String) detailBio.get("RECEIPTLINENUMBER");
					Object objRecDetailLPN = detailBio.getValue("TOID");
					if(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5"))
					{
						if(objRecDetailLPN == null || objRecDetailLPN.toString().equals(" "))
						{
							throw new UserException("WMEXP_LPN_REQUIRED", new Object[]{});
						}
						else
						{		
							if(recDetailType.equalsIgnoreCase("4"))
							{
								if(objRecDetailLPN.toString().length() != 18)
								{
									errorParam[0]= receiptLinenumber;
									throw new UserException("WMEXP_LPN_LENGTH_01", errorParam);
								}
								if(!sscccompliant(objRecDetailLPN.toString()))
								{
									errorParam[0]= objRecDetailLPN.toString(); 
									errorParam[1]= receiptLinenumber;
									throw new UserException("WMWXP_LPN_NOTSSCC_01", errorParam);
								}
							}
							validateReceiptDetailLPN(detailBio, context);
						}
					}

					String lpnValue = detailBio.getValue("TOID") == null || detailBio.getValue("TOID").toString().matches("\\s*") ? null : detailBio.getValue("TOID").toString().toUpperCase();
					if (lpnValue != null) {
						// ensures Upper Case
						detailBio.setValue("TOID", lpnValue);
					}
				}

				if(detailBio.hasBeenUpdated("TOLOC"))
				{
					String toLocValue = detailBio.getValue("TOLOC") == null || detailBio.getValue("TOLOC").toString().matches("\\s*") ? null : detailBio.getValue("TOLOC").toString().toUpperCase();
					if(toLocValue == null)
					{
						errorParam[0]= "null";
						throw new UserException("WMEXP_PICK_TOLOC", errorParam);
					}
					else if(!(validateKey(toLocValue,"wm_location", "LOC", context.getState())))
					{
						errorParam[0]= toLocValue.toString();
						throw new UserException("WMEXP_PICK_TOLOC", errorParam);
					}

					//ensures Upper Case
					detailBio.setValue("TOLOC", toLocValue);
				}

				if(detailBio.hasBeenUpdated("POKEY"))
				{
					String poKeyValue = detailBio.getValue("POKEY") == null || detailBio.getValue("POKEY").toString().matches("\\s*") ? null : detailBio.getValue("POKEY").toString().toUpperCase();
					validatePOKey(poKeyValue, context.getState());
					//ensures Upper Case
					detailBio.setValue("POKEY", poKeyValue);
				}

				//��ѯSKU�Ƿ�������α�ʶ		kaiw	06/04/2012
				String receiptkey = (String)detailBio.getValue("RECEIPTKEY");
				String receiptlinenumber = (String)detailBio.getValue("RECEIPTLINENUMBER");
				
				int rowcount = 0;
				String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
				String theSQL = "SELECT COUNT(1) FROM RECEIPTDETAIL, SKU WHERE RECEIPTDETAIL.RECEIPTKEY = '"+receiptkey+"' AND RECEIPTDETAIL.RECEIPTLINENUMBER = '"+receiptlinenumber+"' AND RECEIPTDETAIL.STORERKEY=SKU.STORERKEY AND RECEIPTDETAIL.SKU=SKU.SKU AND NVL(SKU.SUSR6, '0')='1' AND NVL(RECEIPTDETAIL.LOTTABLE03, ' ')=' ' ";
		    	try 
		    	{
		    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
		    		ResultSet resultSet = appAccess.getResultSet(facilityName.toUpperCase(), theSQL, new Object[0]);
		    		if(resultSet.next()){
		    			rowcount = resultSet.getInt(1);
		    		}
		    		resultSet.close();
		    	} 
		    	catch (SQLException e) 
		    	{			
					e.printStackTrace();
					throw new UserException("WMEXP_RECEIPT_DUP_DELETE", new Object[1]);
				}
				
				//kaiw	05/16/2012		����Ǳ����ջ�,��ô����������04, 05, 11
				if(detailBio.hasBeenUpdated("QTYRECEIVED")){
					if(detailBio.getValue("LOTTABLE04")==null){
						detailBio.setValue("LOTTABLE04", new GregorianCalendar());
					}
					if(detailBio.getValue("LOTTABLE05")==null){
						GregorianCalendar gc = new GregorianCalendar();
						gc.add(GregorianCalendar.MONTH, -1);
						detailBio.setValue("LOTTABLE05", gc);
					}
					if(detailBio.getValue("LOTTABLE11")==null){
						detailBio.setValue("LOTTABLE11", new GregorianCalendar());
					}
					detailBio.setValue("LOTTABLE01", receiptkey+receiptlinenumber);				//����������01
					if(headerFocus.getValue("LOCADDRESS")!=null && !"".equals((String)headerFocus.getValue("LOCADDRESS"))){
						detailBio.setValue("LOTTABLE02", headerFocus.getValue("LOCADDRESS"));	//���ÿ���
					}
					if(rowcount == 1){															//���SKU��־���ù�Ӧ��
						detailBio.setValue("LOTTABLE03", headerFocus.getValue("SUPPLIERCODE"));
					}
				}
				
				validateQCResults(detailBio,context);
				if(!(recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5")))
				{
					String LottableValidationKey = getFieldValuefromSku(owner,sku,"LOTTABLEVALIDATIONKEY",context);
					BioCollectionBean LottableValidationCollection = getlottableValidation(LottableValidationKey, context);
					checkLottableValidations(LottableValidationCollection, headerFocus,detailBio, context);
				}

			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
	}


	/**
	 * Gets the tab group slot.
	 *
	 * @param state the state
	 * @return the tab group slot
	 */
	private SlotInterface getTabGroupSlot(StateInterface state)
	{
		//Common 
		RuntimeFormInterface shellToolbar = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shell_list_asn_receipts Toolbar", state);
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		return tabGroupSlot;
	}


	/**
	 * Update asn status.
	 *
	 * @param asnReceiptKey the asn receipt key
	 * @throws EpiException the epi exception
	 */
	public void updateASNStatus(String asnReceiptKey)throws EpiException
	{
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(asnReceiptKey));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPUPDATEASNSTATUS");
		try
		{
			WmsWebuiActionsImpl.doAction(actionProperties);
		}
		catch(WebuiException e)
		{
			throw new UserException(e.getMessage(), new Object[]{});
		}
	}

	/**
	 * Validate receipt detail lpn.
	 *
	 * @param detailFocus the detail focus
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	static public void validateReceiptDetailLPN(DataBean detailFocus, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		String NSQLValue = "";
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'DisAllowDuplicateIdsOnRFRcpt')";
		Query bioQuery = new Query("wm_system_settings",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if(listCollection.size()!= 0){
			NSQLValue = listCollection.get("0").get("NSQLVALUE").toString();
		}
		String l_owner = "";
		String l_toid = "";
		String l_sku = "";
		String sQueryString1="";
		Query BioQuery1= null;
		BioCollectionBean lotxlocxidCollection = null;
		if(detailFocus instanceof QBEBioBean){
			QBEBioBean receiptQBEBioBean =(QBEBioBean)detailFocus;
			if(receiptQBEBioBean.getValue("TOID") == null){
				receiptQBEBioBean.set("TOID", " ");
			}
			l_owner = BioAttributeUtil.getString(receiptQBEBioBean, "STORERKEY");
			l_toid = receiptQBEBioBean.get("TOID").toString();
			l_sku = receiptQBEBioBean.get("SKU").toString();
			if(NSQLValue.equalsIgnoreCase("1")){
				sQueryString1 = "(receiptdetail.TOID = '"+l_toid+"')";
				BioQuery1 = new Query("receiptdetail",sQueryString1,null);

				uowb = context.getState().getDefaultUnitOfWork();
				lotxlocxidCollection = uowb.getBioCollectionBean(BioQuery1);
				if(lotxlocxidCollection.size()> 0){
					errorParam[0]= l_toid;
					errorParam[1] = "";
					throw new UserException("WMEXP_LPN_DUPLICATE", errorParam);
				}
			}
			String AllowCommingledLPN = getAllowCommingledLPN(l_owner,context);
			if(AllowCommingledLPN.equalsIgnoreCase("0")){
				lotxlocxidCollection= null;
				sQueryString1 = "(wm_lotxlocxid_ib.ID = '"+l_toid+"' AND wm_lotxlocxid_ib.QTY > 0 AND (wm_lotxlocxid_ib.STORERKEY != '"
				+l_owner+"' OR wm_lotxlocxid_ib.SKU != '" +l_sku+ "'))";
				BioQuery1 = new Query("wm_lotxlocxid_ib",sQueryString1,null);
				uowb = context.getState().getDefaultUnitOfWork();
				lotxlocxidCollection = uowb.getBioCollectionBean(BioQuery1);
				if(lotxlocxidCollection.size()> 0){
					errorParam[0]= l_toid;
					errorParam[1] = l_owner;
					errorParam[2] = l_sku;
					throw new UserException("WMEXP_LPN_DUPLICATE_01", errorParam);
				}
			}
		}
	}

	/**
	 * Gets the allow commingled lpn.
	 *
	 * @param owner the owner
	 * @param context the context
	 * @return the allow commingled lpn
	 * @throws EpiException the epi exception
	 */
	static private String getAllowCommingledLPN(String owner, ActionContext context) throws EpiException {
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_storer.STORERKEY = '" + owner + "' AND  wm_storer.TYPE = '1')";
		Query bioQuery = new Query("wm_storer",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if (listCollection.size()!= 0){
			return listCollection.get("0").get("ALLOWCOMMINGLEDLPN").toString();
		}else{
			return "";
		}
	}

	/**
	 * Sscccompliant.
	 *
	 * @param recDetailLPN the rec detail lpn
	 * @return true, if successful
	 * @throws EpiException the epi exception
	 */
	private boolean sscccompliant(String recDetailLPN)throws EpiException{
		String lpn_17 = recDetailLPN.substring(0,17);
		String Checkdigit = "";
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(lpn_17));
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPLPNCHECKDIGIT01");
		try{
			EXEDataObject ObjCheckdigit = WmsWebuiActionsImpl.doAction(actionProperties);
			Checkdigit = ObjCheckdigit.getAttribValue(new TextData("checkdigit")).toString();
		}catch(WebuiException e){
			throw new UserException(e.getMessage(), new Object[]{});
		}
		return Checkdigit.equals(recDetailLPN.substring(17));
	}

	/**
	 * Receipt validations.
	 *
	 * @param ReceiptValidationKey the receipt validation key
	 * @param pokey the pokey
	 * @param receiptLinenumber the receipt linenumber
	 * @param sku the sku
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	public void receiptValidations(String ReceiptValidationKey, Object pokey, String receiptLinenumber, String sku, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		String receiptWithoutPO = "";
		String sQueryString = "(wm_receiptvalidation.RECEIPTVALIDATIONKEY = '" + ReceiptValidationKey + "')";
		Query bioQuery = new Query("wm_receiptvalidation",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if (listCollection.size()!= 0){
			receiptWithoutPO = listCollection.get("0").get("RECEIPTWITHOUTPO").toString();
		}
		if (pokey == null) {
			if (receiptWithoutPO.equals("9")){
				errorParam[0]= receiptLinenumber;
				errorParam[1] = sku;
				throw new UserException("WMEXP_RECEIPTVALIDATION_POREQUIRED", errorParam);
			}
		}
	}

	/**
	 * Gets the field valuefrom sku.
	 *
	 * @param owner the owner
	 * @param sku the sku
	 * @param FieldName the field name
	 * @param context the context
	 * @return the field valuefrom sku
	 * @throws EpiException the epi exception
	 */
	public String getFieldValuefromSku(String owner, String sku, String FieldName, ActionContext context) throws EpiException {
		BioCollectionBean listCollection = null;
		String sQueryString = "( sku.STORERKEY = '" + owner + "' AND  sku.SKU = '"+sku+"')";
		Query bioQuery = new Query("sku",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if (listCollection.size()!= 0){
			return listCollection.get("0").get(FieldName).toString();
		}else{
			return "";
		}
	}



	/**
	 * Update supplier info.
	 *
	 * @param supplierInfoForm the supplier info form
	 * @param receiptKey the receipt key
	 * @param receiptLineNumber the receipt line number
	 * @param lpn the lpn
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	private void updateSupplierInfo(RuntimeFormInterface supplierInfoForm, String receiptKey, 
			String receiptLineNumber, String lpn, ActionContext context) throws EpiException{
		if(supplierInfoForm.getFormWidgetByName("SUPPLIERNAME").getValue() != null){
			String supplierName = supplierInfoForm.getFormWidgetByName("SUPPLIERNAME").getDisplayValue();
			String suppAddress1 = null, suppAddress2 = null, suppCity = null, 
			suppState = null, suppZip = null, suppCountry = null, suppPhone1 = null; 


			BioCollectionBean ipSupplierCollection = null;
			BioCollectionBean ipSupplierCollectionMax=null;
			UnitOfWorkBean uowb =context.getState().getDefaultUnitOfWork();

			String ipSupplierQueryStr = "(wm_ipsupplier.SupplierName = '"+supplierName+"')";
			Query ipSupplierQuery = new Query("wm_ipsupplier",ipSupplierQueryStr,null);
			ipSupplierCollection = uowb.getBioCollectionBean(ipSupplierQuery);
			int size = ipSupplierCollection.size();
			if(size != 0){
				String sQueryString1 = "(wm_ipsupplier.SupplierKey = '"+ipSupplierCollection.max("SupplierKey").toString()+"')";
				Query BioQuery1 = new Query("wm_ipsupplier",sQueryString1,null);
				ipSupplierCollectionMax = uowb.getBioCollectionBean(BioQuery1);
			}

			suppAddress1 = supplierInfoForm.getFormWidgetByName("SUPPLIERADDRESS1").getDisplayValue();
			if (suppAddress1 != null){
				if (suppAddress1.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppAddress1 = ipSupplierCollectionMax.get("0").get("SupplierAddress1").toString();
					}
				}
			}
			suppAddress2 = supplierInfoForm.getFormWidgetByName("SUPPLIERADDRESS2").getDisplayValue();
			if (suppAddress2 != null){
				if (suppAddress2.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppAddress2 = ipSupplierCollectionMax.get("0").get("SupplierAddress2").toString();	
					}				
				}
			}
			suppCity = supplierInfoForm.getFormWidgetByName("SUPPLIERCITY").getDisplayValue();
			if (suppCity != null){
				if (suppCity.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppCity = ipSupplierCollectionMax.get("0").get("SupplierCity").toString();	
					}
				}
			}
			suppState = supplierInfoForm.getFormWidgetByName("SUPPLIERSTATE").getDisplayValue();
			if (suppState != null){
				if (suppState.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppState = ipSupplierCollectionMax.get("0").get("SupplierState").toString();
					}
				}
			}
			suppZip = supplierInfoForm.getFormWidgetByName("SUPPLIERZIP").getDisplayValue();
			if (suppZip != null){
				if (suppZip.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppZip = ipSupplierCollectionMax.get("0").get("SupplierZip").toString();
					}
				}
			}
			suppCountry = supplierInfoForm.getFormWidgetByName("SUPPLIERCOUNTRY").getDisplayValue();
			if (suppCountry != null){
				if (suppCountry.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppCountry = ipSupplierCollectionMax.get("0").get("SupplierCountry").toString();	
					}
				}
			}
			suppPhone1 = supplierInfoForm.getFormWidgetByName("SUPPLIERPHONE").getDisplayValue();
			if (suppPhone1 != null){
				if (suppPhone1.trim().equalsIgnoreCase("")){
					if(size != 0){
						suppPhone1 = ipSupplierCollectionMax.get("0").get("SupplierPhone").toString();	
					}
				}
			}
			_log.debug("LOG_SYSTEM_OUT","supplierName = "+supplierName,100L);
			_log.debug("LOG_SYSTEM_OUT","suppAddress1 = "+suppAddress1,100L);
			_log.debug("LOG_SYSTEM_OUT","suppAddress2 = "+suppAddress2,100L);
			_log.debug("LOG_SYSTEM_OUT","suppCity = "+suppCity,100L);
			_log.debug("LOG_SYSTEM_OUT","suppState = "+suppState,100L);
			_log.debug("LOG_SYSTEM_OUT","suppZip = "+suppZip,100L);
			_log.debug("LOG_SYSTEM_OUT","suppCountry = "+suppCountry,100L);
			_log.debug("LOG_SYSTEM_OUT","suppPhone1 = "+suppPhone1,100L);

			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(receiptKey));
			parms.add(new TextData(receiptLineNumber));
			parms.add(new TextData(supplierName));
			parms.add(new TextData(suppAddress1));
			parms.add(new TextData(suppAddress2));
			parms.add(new TextData(suppCity));
			parms.add(new TextData(suppState));
			parms.add(new TextData(suppZip));
			parms.add(new TextData(suppCountry));
			parms.add(new TextData(suppPhone1));
			parms.add(new TextData(lpn));
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName("NSPUPDATESUPPLIERINFO");
			try{
				EXEDataObject ObjSupplierKey = WmsWebuiActionsImpl.doAction(actionProperties);
				String SupplierKey = ObjSupplierKey.getAttribValue(new TextData("supplierkey")).toString();
				supplierInfoForm.getFormWidgetByName("SUPPLIERKEY").setValue(SupplierKey);
			}catch(WebuiException e){
				throw new UserException(e.getMessage(), new Object[]{});
			}

		}
	}

	/**
	 * Validate po key.
	 *
	 * @param pokey the pokey
	 * @param state TODO
	 * @throws EpiException the epi exception
	 */
	public void validatePOKey(Object pokey, StateInterface state)throws EpiException {
		if(pokey != null){
			if(!pokey.toString().trim().equalsIgnoreCase("")){
				BioCollectionBean listCollection = null;
				String poKeyVal = pokey.toString().trim();
				String sQueryString = "(wm_po.POKEY = '"+poKeyVal+"')";
				Query bioQuery = new Query("wm_po",sQueryString,null);
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				listCollection = uowb.getBioCollectionBean(bioQuery);
				if(listCollection.size()== 0){
					errorParam[0]= poKeyVal;
					throw new UserException("WMEXP_INVALID_PO", errorParam);
				}
			}
		}
	}

	/**
	 * Validate qc results.
	 *
	 * @param detailFocus the detail focus
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	private void validateQCResults(DataBean detailFocus, ActionContext context)throws EpiException {
		Object qcQtyInspected = "", qcQtyRejected = "", qtyReceived = "", qcRejReason = "";
		int intQtyReceived, intQCQtyInspected, intQCQtyRejected;
		if(detailFocus instanceof QBEBioBean){
			QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
			qcQtyInspected = qbeDetailFocus.getValue("QCQTYINSPECTED");
			qtyReceived = qbeDetailFocus.getValue("QTYRECEIVED");
			qcQtyRejected = qbeDetailFocus.getValue("QCQTYREJECTED");
			qcRejReason = qbeDetailFocus.getValue("QCREJREASON");
		}else if(detailFocus instanceof BioBean){
			BioBean bioDetailFocus = (BioBean)detailFocus;
			qcQtyInspected = bioDetailFocus.getValue("QCQTYINSPECTED");
			qtyReceived = bioDetailFocus.getValue("QTYRECEIVED");
			qcQtyRejected = bioDetailFocus.getValue("QCQTYREJECTED");
			qcRejReason = bioDetailFocus.getValue("QCREJREASON");
		}
		intQCQtyInspected = qcQtyInspected == null ? 0 : (int) new Double(qcQtyInspected.toString()).doubleValue() ;
		intQtyReceived = qtyReceived == null ? 0 : (int) new Double(qtyReceived.toString()).doubleValue();
		intQCQtyRejected = qcQtyRejected == null ? 0 : (int) new Double(qcQtyRejected.toString()).doubleValue();

		//Qty Inspected Validations
		if(intQCQtyInspected < 0){
			errorParam[0]= "Qty Inspected";
			throw new UserException("WMEXP_QTYGREATEREQUALZERO", errorParam);
		}
		if(intQCQtyInspected > intQtyReceived){
			errorParam[0]= "Qty Inspected";
			errorParam[1]= "Qty Received";
			throw new UserException("WMEXP_LESSEQUAL", errorParam);
		}
		if(intQCQtyRejected > intQCQtyInspected){
			errorParam[0]= "Qty Rejected";
			errorParam[1]= "Qty Inspected";
			throw new UserException("WMEXP_LESSEQUAL", errorParam);
		}

		//Qty Rejected Validations
		if(intQCQtyRejected < 0){
			errorParam[0]= "Qty Rejected";
			throw new UserException("WMEXP_QTYGREATEREQUALZERO", errorParam);
		}
		if(intQCQtyRejected > intQtyReceived){
			errorParam[0]= "Qty Rejected";
			errorParam[1]= "Qty Received";
			throw new UserException("WMEXP_LESSEQUAL", errorParam);
		}
		if(intQCQtyRejected > intQCQtyInspected){
			errorParam[0]= "Qty Rejected";
			errorParam[1]= "Qty Inspected";
			throw new UserException("WMEXP_LESSEQUAL", errorParam);
		}
		if(intQCQtyRejected > 0){
			if(qcRejReason == null){
				errorParam[0]= "QC Reason Code";
				throw new UserException("WMEXP_REQFIELD", errorParam);
			}
		}
		if(qcRejReason != null){
			if(intQCQtyRejected <= 0){
				errorParam[0]= "Qty Rejected";
				throw new UserException("WMEXP_REQFIELD", errorParam);
			}
		}
	}

	/**
	 * Gets the lottable validation.
	 *
	 * @param LottableValidationKey the lottable validation key
	 * @param context the context
	 * @return the lottable validation
	 * @throws EpiException the epi exception
	 */
	public BioCollectionBean getlottableValidation(String LottableValidationKey, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_lottablevalidation.LOTTABLEVALIDATIONKEY = '" + LottableValidationKey + "')";
		Query bioQuery = new Query("wm_lottablevalidation",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if(listCollection.size()!= 0){
			return listCollection;
		}
		return null;
	}

	/**
	 * Check lottable validations.
	 *
	 * @param LottableValidationCollection the lottable validation collection
	 * @param headerFocus the header focus
	 * @param detailFocus the detail focus
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	public void checkLottableValidations(BioCollectionBean LottableValidationCollection,DataBean headerFocus, DataBean detailFocus, ActionContext context)throws EpiException 
	{
		Object[] olottable = new Object[10];
		String[] lottable = new String[10];
		//Subodh: Start: Machine# 2395386, SDIS# SCM-00000-07060
		Config conf  = SsaAccessBase.getConfig("cognosClient","defaults");
		Configuration wmDateConfig = conf.getConfiguration();		
		String ejbDateFormat = wmDateConfig.getString("parm");
		_log.debug("LOG_SYSTEM_OUT","ejbDateFormat = "+ ejbDateFormat,100L);
		SimpleDateFormat ejbFormat = new SimpleDateFormat(ejbDateFormat.toString());
		//Subodh: End: Machine# 2395386, SDIS# SCM-00000-07060
		String receiptLineNumber = "", owner= "", sku = "", type = "", qtyReceived = "", allowautoreceipt = "";
		String lottablValidationKey	= LottableValidationCollection.get("0").getValue("LOTTABLEVALIDATIONKEY").toString();
		int intQtyReceived ;
		if(headerFocus instanceof QBEBioBean)
		{
			QBEBioBean qbeHeaderFocus = (QBEBioBean)headerFocus;
			type = qbeHeaderFocus.get("TYPE").toString();
			if(qbeHeaderFocus.get("ALLOWAUTORECEIPT")== null)
			{
				allowautoreceipt="0";
				qbeHeaderFocus.set("ALLOWAUTORECEIPT","0");
			}
			else
			{
				allowautoreceipt = qbeHeaderFocus.get("ALLOWAUTORECEIPT").toString();	
			}
		}
		else if(headerFocus instanceof BioBean)
		{
			BioBean BioheaderFocus = (BioBean)headerFocus;
			type = BioheaderFocus.get("TYPE").toString();
			if(BioheaderFocus.get("ALLOWAUTORECEIPT")== null)
			{
				allowautoreceipt="0";
				BioheaderFocus.set("ALLOWAUTORECEIPT","0");
			}
			else
			{
				allowautoreceipt = BioheaderFocus.get("ALLOWAUTORECEIPT").toString();	
			}
		}

		if(detailFocus instanceof QBEBioBean)
		{
			QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
			olottable[0] = qbeDetailFocus.getValue("LOTTABLE01");
			olottable[1] = qbeDetailFocus.getValue("LOTTABLE02");
			olottable[2] = qbeDetailFocus.getValue("LOTTABLE03");
			olottable[3] = qbeDetailFocus.getValue("LOTTABLE04");
			olottable[4] = qbeDetailFocus.getValue("LOTTABLE05");
			olottable[5] = qbeDetailFocus.getValue("LOTTABLE06");
			olottable[6] = qbeDetailFocus.getValue("LOTTABLE07");
			olottable[7] = qbeDetailFocus.getValue("LOTTABLE08");
			olottable[8] = qbeDetailFocus.getValue("LOTTABLE09");
			olottable[9] = qbeDetailFocus.getValue("LOTTABLE10");
			receiptLineNumber = qbeDetailFocus.getValue("RECEIPTLINENUMBER").toString();
			owner = qbeDetailFocus.getValue("STORERKEY").toString();
			sku = qbeDetailFocus.getValue("SKU").toString();
			qtyReceived = qbeDetailFocus.getValue("QTYRECEIVED").toString();
		}
		else if(detailFocus instanceof BioBean)
		{
			BioBean bioDetailFocus = (BioBean)detailFocus;
			olottable[0] = bioDetailFocus.getValue("LOTTABLE01");
			olottable[1] = bioDetailFocus.getValue("LOTTABLE02");
			olottable[2] = bioDetailFocus.getValue("LOTTABLE03");
			olottable[3] = bioDetailFocus.getValue("LOTTABLE04");
			olottable[4] = bioDetailFocus.getValue("LOTTABLE05");
			olottable[5] = bioDetailFocus.getValue("LOTTABLE06");
			olottable[6] = bioDetailFocus.getValue("LOTTABLE07");
			olottable[7] = bioDetailFocus.getValue("LOTTABLE08");
			olottable[8] = bioDetailFocus.getValue("LOTTABLE09");
			olottable[9] = bioDetailFocus.getValue("LOTTABLE10");
			receiptLineNumber = bioDetailFocus.getValue("RECEIPTLINENUMBER").toString();
			owner = bioDetailFocus.getValue("STORERKEY").toString();
			sku = bioDetailFocus.getValue("SKU").toString();
			qtyReceived = bioDetailFocus.getValue("QTYRECEIVED").toString();
		}

		intQtyReceived = (int) new Double (qtyReceived).doubleValue();
		if ((intQtyReceived != 0) || (allowautoreceipt.equalsIgnoreCase("1")))
		{
			String lot1man = LottableValidationCollection.get("0").getValue("LOTTABLE01ONRFRECEIPTMANDATORY").toString();
			String lot2man = LottableValidationCollection.get("0").getValue("LOTTABLE02ONRFRECEIPTMANDATORY").toString();
			String lot3man = LottableValidationCollection.get("0").getValue("LOTTABLE03ONRFRECEIPTMANDATORY").toString();
			String lot4man = LottableValidationCollection.get("0").getValue("LOTTABLE04ONRFRECEIPTMANDATORY").toString();
			String lot5man = LottableValidationCollection.get("0").getValue("LOTTABLE05ONRFRECEIPTMANDATORY").toString();
			String lot6man = LottableValidationCollection.get("0").getValue("LOTTABLE06ONRFRECEIPTMANDATORY").toString();
			String lot7man = LottableValidationCollection.get("0").getValue("LOTTABLE07ONRFRECEIPTMANDATORY").toString();
			String lot8man = LottableValidationCollection.get("0").getValue("LOTTABLE08ONRFRECEIPTMANDATORY").toString();
			String lot9man = LottableValidationCollection.get("0").getValue("LOTTABLE09ONRFRECEIPTMANDATORY").toString();
			String lot10man = LottableValidationCollection.get("0").getValue("LOTTABLE10ONRFRECEIPTMANDATORY").toString();

			Array parms = new Array(); 
			boolean expfound = false;
			if(lot1man.equalsIgnoreCase("1") && (olottable[0] == null || olottable[0].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable01");
			}
			if(lot2man.equalsIgnoreCase("1") && (olottable[1] == null || olottable[1].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable02");
			}
			if(lot3man.equalsIgnoreCase("1") && (olottable[2] == null || olottable[2].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable03");
			}
			if(lot4man.equalsIgnoreCase("1") && (olottable[3] == null || olottable[3].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable04");
			}
			if(lot5man.equalsIgnoreCase("1") && (olottable[4] == null || olottable[4].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable05");
			}
			if(lot6man.equalsIgnoreCase("1") && (olottable[5] == null || olottable[5].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable06");
			}
			if(lot7man.equalsIgnoreCase("1") && (olottable[6] == null || olottable[6].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable07");
			}
			if(lot8man.equalsIgnoreCase("1") && (olottable[7] == null || olottable[7].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable08");			
			}
			if(lot9man.equalsIgnoreCase("1") && (olottable[8] == null || olottable[8].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable09");
			}
			if(lot10man.equalsIgnoreCase("1") && (olottable[9] == null || olottable[9].toString().trim().equals("")))
			{
				expfound = true;
				parms.add("Lottable10");
			}
			if(expfound)
			{
				String missingLottables="";
				for(int i=1; i<=parms.size() ; i++){
					missingLottables = i == parms.size() ? missingLottables + parms.get(i) + " " : missingLottables + parms.get(i) + ", ";
				}
				UserInterface user = context.getState().getUser();
				LocaleInterface locale = user.getLocale();
				String strMsg = "";
				errorParam[0]= receiptLineNumber;
				errorParam[1] = owner;
				errorParam[2] = sku;
				strMsg = type.equalsIgnoreCase("4") || type.equalsIgnoreCase("5") ? getTextMessage("WMEXP_LOTTABLE_LPNDETAIL_REQ", errorParam, locale) 
						: getTextMessage("WMEXP_LOTTABLE_REQUIRED", errorParam, locale);
				strMsg = missingLottables + strMsg;
				throw new UserException(strMsg, new Object[]{});			
			}
		}

		lottable[0] = olottable[0]!=null ? olottable[0].toString() : "";
		lottable[1] = olottable[1]!=null ? olottable[1].toString() : "";
		lottable[2] = olottable[2]!=null ? olottable[2].toString() : "";
		//Subodh: Start: Machine# 2395386, SDIS# SCM-00000-07060
		//lottable[3] = olottable[3]!=null ? olottable[3].toString() : "";
		//lottable[4] = olottable[4]!=null ? olottable[4].toString() : "";
		lottable[3] = olottable[3]!=null ? ejbFormat.format(((Calendar)olottable[3]).getTime()) : "";
		lottable[4] = olottable[4]!=null ? ejbFormat.format(((Calendar)olottable[4]).getTime()) : "";
		//Subodh: End: Machine# 2395386, SDIS# SCM-00000-07060
		lottable[5] = olottable[5]!=null ? olottable[5].toString() : "";
		lottable[6] = olottable[6]!=null ? olottable[6].toString() : "";
		lottable[7] = olottable[7]!=null ? olottable[7].toString() : "";
		lottable[8] = olottable[8]!=null ? olottable[8].toString() : "";
		lottable[9] = olottable[9]!=null ? olottable[9].toString() : "";

		if(intQtyReceived != 0)
		{
			BioCollectionBean lottableValidationDetailColl =  getlottableValidationDetail( lottablValidationKey, context);
			String OnReceiptCopyPackKey = getFieldValuefromSku(owner,sku,"ONRECEIPTCOPYPACKKEY",context);
			processLottables(lottableValidationDetailColl, owner, sku, lottable, OnReceiptCopyPackKey);
		}
	}

	/**
	 * Gets the lottable validation detail.
	 *
	 * @param LottableValidationKey the lottable validation key
	 * @param context the context
	 * @return the lottable validation detail
	 * @throws EpiException the epi exception
	 */
	private BioCollectionBean getlottableValidationDetail(String LottableValidationKey, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_lottablevalidationdetail.LOTTABLEVALIDATIONKEY = '"+LottableValidationKey+"')";
		Query bioQuery = new Query("wm_lottablevalidationdetail",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if(listCollection.size()!= 0){
			return listCollection;
		}
		return null;
	}

	/**
	 * Process lottables.
	 *
	 * @param lottableValidationDetailColl the lottable validation detail coll
	 * @param owner the owner
	 * @param sku the sku
	 * @param lottable the lottable
	 * @param OnReceiptCopyPackKey the on receipt copy pack key
	 * @throws EpiException the epi exception
	 */
	private void processLottables(BioCollectionBean lottableValidationDetailColl, String owner, 
			String sku,	String[] lottable, String OnReceiptCopyPackKey)throws EpiException {
		int numOfLottables = 10;
		int k=0;
		String lottableName = "";
		String[] parsedResult = new String[20];

		for(int i=0; i< lottableValidationDetailColl.size(); i++){
			parsedResult[0]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE01RECEIPTVALIDATION").toString();
			parsedResult[1]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE02RECEIPTVALIDATION").toString();
			parsedResult[2]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE03RECEIPTVALIDATION").toString();
			parsedResult[3]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE04RECEIPTVALIDATION").toString();
			parsedResult[4]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE05RECEIPTVALIDATION").toString();
			parsedResult[5]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE06RECEIPTVALIDATION").toString();
			parsedResult[6]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE07RECEIPTVALIDATION").toString();
			parsedResult[7]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE08RECEIPTVALIDATION").toString();
			parsedResult[8]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE09RECEIPTVALIDATION").toString();
			parsedResult[9]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE10RECEIPTVALIDATION").toString();
			parsedResult[10]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE01RECEIPTCONVERSION").toString();
			parsedResult[11]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE02RECEIPTCONVERSION").toString();
			parsedResult[12]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE03RECEIPTCONVERSION").toString();
			parsedResult[13]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE04RECEIPTCONVERSION").toString();
			parsedResult[14]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE05RECEIPTCONVERSION").toString();
			parsedResult[15]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE06RECEIPTCONVERSION").toString();
			parsedResult[16]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE07RECEIPTCONVERSION").toString();
			parsedResult[17]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE08RECEIPTCONVERSION").toString();
			parsedResult[18]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE09RECEIPTCONVERSION").toString();
			parsedResult[19]= lottableValidationDetailColl.elementAt(i).get("LOTTABLE10RECEIPTCONVERSION").toString();

			for(int j=0; j< (numOfLottables*2)-1; j++){
				String sprocname = parsedResult[j];
				if(!sprocname.equalsIgnoreCase("NONE")){
					k = j >= numOfLottables ? (j-numOfLottables)+1 : j+1 ;
					lottableName = k < 10 ? "Lottable0" + k : "Lottable" + k;
					if(lottableName.equalsIgnoreCase("Lottable01") && OnReceiptCopyPackKey.equalsIgnoreCase("1")){
						sprocname = "NONE";
					}

					//					Call Stored procedure for the Lottable.
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					parms.add(new TextData("TRUE"));
					parms.add(new TextData(lottableName));
					parms.add(new TextData(lottable[0]));
					parms.add(new TextData(lottable[1]));
					parms.add(new TextData(lottable[2]));
					parms.add(new TextData(lottable[3]));
					parms.add(new TextData(lottable[4]));
					parms.add(new TextData(lottable[5]));
					parms.add(new TextData(lottable[6]));
					parms.add(new TextData(lottable[7]));
					parms.add(new TextData(lottable[8]));
					parms.add(new TextData(lottable[9]));
					parms.add(new TextData(owner));
					parms.add(new TextData(sku));

					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName(sprocname);
					try{
						WmsWebuiActionsImpl.doAction(actionProperties);
					}catch(WebuiException e){
						throw new UserException(e.getMessage(), new Object[]{});
					}
				}
			}
		}
	}

	/**
	 * Validate lpn detail.
	 *
	 * @param lpnDetailFocus the lpn detail focus
	 * @param headerFocus the header focus
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	private void validateLPNDetail(DataBean lpnDetailFocus,DataBean headerFocus, ActionContext context)throws EpiException {
		//LPNDetail Focus
		if(lpnDetailFocus instanceof QBEBioBean){
			lpnDetailFocus = lpnDetailFocus;
		}else if(lpnDetailFocus instanceof BioBean){
			lpnDetailFocus = lpnDetailFocus;
		}
		//if(headerFocus){
		String l_id = lpnDetailFocus.getValue("ID").toString();
		String l_lpndetailkey = lpnDetailFocus.getValue("LPNDETAILKEY").toString();
		String l_receiptkey = lpnDetailFocus.getValue("RECEIPTKEY").toString();
		String l_qtyreceived = lpnDetailFocus.getValue("QTYRECEIVED").toString();
		String owner = lpnDetailFocus.getValue("STORERKEY").toString();
		String sku = lpnDetailFocus.getValue("SKU").toString();
		Double doubQtyReceived = new Double (l_qtyreceived);
		int intQtyReceived = (int)doubQtyReceived.doubleValue();

		BioCollectionBean listCollection = null;
		String DupCaseId = "";
		String sQueryString = "(wm_storer.STORERKEY = '" + owner + "' AND  wm_storer.TYPE = '1')";
		Query bioQuery = new Query("wm_storer",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		listCollection = uowb.getBioCollectionBean(bioQuery);
		if(listCollection.size()!= 0){
			DupCaseId = listCollection.get("0").get("DUPCASEID").toString();
		}
		if(intQtyReceived > 0){
			if(!DupCaseId.equalsIgnoreCase("0")){
				if(DupCaseId.equalsIgnoreCase("1")){
					sQueryString = "(lpndetail.RECEIPTKEY = '"+ l_receiptkey +"' AND (lpndetail.ID = '"+l_id+"' OR lpndetail.PALLETID = '"
					+l_id+"') AND lpndetail.QTYRECEIVED > 0 AND lpndetail.STORERKEY = '"+owner+"' AND lpndetail.LPNDETAILKEY != '"+l_lpndetailkey+"')";
				}else if (DupCaseId.equalsIgnoreCase("2")){
					sQueryString = "((lpndetail.ID = '"+l_id+"' OR lpndetail.PALLETID = '"+l_id+"') AND lpndetail.QTYRECEIVED > 0 AND lpndetail.STORERKEY = '"
					+owner+"' AND lpndetail.LPNDETAILKEY != '"+l_lpndetailkey+"')";
				}
				Query lpnDetailQuery = new Query("lpndetail",sQueryString,null);
				listCollection = uowb.getBioCollectionBean(lpnDetailQuery);
				if(listCollection.size()!= 0){		//validate duplicate caseids
					errorParam[0]= l_id;
					throw new UserException("WMEXP_RECEIPT_DUPCASEIDONRECEIPT", errorParam);
				}
			}
			String LottableValidationKey = getFieldValuefromSku(owner,sku,"LOTTABLEVALIDATIONKEY",context);
			BioCollectionBean LottableValidationCollection = getlottableValidation(LottableValidationKey, context);
			checkLottableValidations(LottableValidationCollection,headerFocus, lpnDetailFocus, context);
		}
	}

	/**
	 * Asn key duplication check.
	 *
	 * @param ASNKey the aSN key
	 * @throws DPException the dP exception
	 * @throws UserException the user exception
	 */
	public void asnKeyDuplicationCheck(String ASNKey) throws DPException, UserException {
		String query = "SELECT * FROM RECEIPT WHERE (RECEIPTKEY = '" + ASNKey + "')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if(results.getRowCount() >= 1){
			errorParam[0] = ASNKey;
			throw new UserException("WMEXP_DUP_RECEIPTKEY", errorParam);
		}
	}

	/**
	 * Validate tariffkey.
	 *
	 * @param tariffkey the tariffkey
	 * @param context the context
	 * @throws EpiException the epi exception
	 */
	private void validateTariffkey (Object tariffkey, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		if(tariffkey == null){
			errorParam[0]= "null";
			throw new UserException("WM_EXP_INVALID_TARRIF", errorParam);
		}else{
			String tariffkeyVal = tariffkey.toString().trim();
			String sQueryString = "(wm_tariff.TARIFFKEY = '"+tariffkeyVal+"')";
			Query bioQuery = new Query("wm_tariff",sQueryString,null);
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
			listCollection = uowb.getBioCollectionBean(bioQuery);
			if(listCollection.size()== 0){
				errorParam[0]= tariffkeyVal;
				throw new UserException("WM_EXP_INVALID_TARRIF", errorParam);
			}
		}
	}

	/**
	 * Validate key.
	 *
	 * @param key the key
	 * @param bio the bio
	 * @param attribute the attribute
	 * @param state the state
	 * @return true, if successful
	 * @throws EpiException the epi exception
	 */
	private boolean validateKey(Object key,String bio, String attribute, StateInterface state)throws EpiException {
		// Query Bio to see if Attribute exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;
		boolean found = false;
		try{
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			queryStatement = bio + "." + attribute + " = '" + key.toString() + "'";
			query = new Query(bio, queryStatement, null);
			results = uowb.getBioCollectionBean(query);
		}catch(Exception e){
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}

		// If BioCollection size equals 0, return RET_CANCEL
		if(results.size() == 0){
			found = false;
		}else{
			found = true;
		}
		return found;
	}

	/**
	 * Colon strip.
	 *
	 * @param label the label
	 * @return the string
	 */
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}

	/**
	 * Read label.
	 *
	 * @param widget the widget
	 * @return the string
	 */
	public String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}

	/**
	 * Sets the null lottable.
	 *
	 * @param qbe the qbe
	 * @param value the value
	 */
	private void setNullLottable(QBEBioBean qbe, String value){
		if(qbe.getValue(value)==null){
			qbe.set(value, " ");
		}
	}

	/**
	 * Sets the null lottable.
	 *
	 * @param bio the bio
	 * @param value the value
	 */
	private void setNullLottable(BioBean bio, String value){
		if(bio.getValue(value)==null){
			bio.set(value, " ");
		}
	}

	/**
	 * Checks if is null lottable.
	 *
	 * @param detailFocus the detail focus
	 */
	public void isNullLottable(DataBean detailFocus){
		if(detailFocus instanceof QBEBioBean){
			QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
			setNullLottable(qbeDetailFocus, "LOTTABLE01");
			setNullLottable(qbeDetailFocus, "LOTTABLE02");
			setNullLottable(qbeDetailFocus, "LOTTABLE03");
			//			setNullLottable(qbeDetailFocus, "LOTTABLE04");
			//			setNullLottable(qbeDetailFocus, "LOTTABLE05");
			setNullLottable(qbeDetailFocus, "LOTTABLE06");
			setNullLottable(qbeDetailFocus, "LOTTABLE07");
			setNullLottable(qbeDetailFocus, "LOTTABLE08");
			setNullLottable(qbeDetailFocus, "LOTTABLE09");
			setNullLottable(qbeDetailFocus, "LOTTABLE10");
		}else if(detailFocus instanceof BioBean){
			BioBean bioDetailFocus = (BioBean)detailFocus;
			setNullLottable(bioDetailFocus, "LOTTABLE01");
			setNullLottable(bioDetailFocus, "LOTTABLE02");
			setNullLottable(bioDetailFocus, "LOTTABLE03");
			//			setNullLottable(bioDetailFocus, "LOTTABLE04");
			//			setNullLottable(bioDetailFocus, "LOTTABLE05");
			setNullLottable(bioDetailFocus, "LOTTABLE06");
			setNullLottable(bioDetailFocus, "LOTTABLE07");
			setNullLottable(bioDetailFocus, "LOTTABLE08");
			setNullLottable(bioDetailFocus, "LOTTABLE09");
			setNullLottable(bioDetailFocus, "LOTTABLE10");
		}
	}

	/**
	 * Sets the navigation.
	 *
	 * @param lpnIsNew the lpn is new
	 * @param cwcdIsNew the cwcd is new
	 * @param context the context
	 */
	private void setNavigation(boolean lpnIsNew, boolean cwcdIsNew, ActionContext context)
	{
		if(lpnIsNew){
			context.setNavigation("clickEventLPNNew");
		}
		else if (cwcdIsNew)	{
			context.setNavigation("clickEventCWCDNew");
		}
		else
		{
			context.setNavigation("clickEventNormal");
		}
	}

	/**
	 * Cwcd validate serial number.
	 *
	 * @param cwcdFocus the cwcd focus
	 * @param owner the owner
	 * @param sku the sku
	 * @throws UserException the user exception
	 */
	private void cwcdValidateSerialNumber(DataBean cwcdFocus, String owner, String sku) throws UserException
	{
		if (cwcdFocus.getValue("SERIALNUMBERLONG") != null)
		{
			String serialNumberLong = (String) cwcdFocus.getValue("SERIALNUMBERLONG");
			ArrayList<?> serials = null;

			try
			{
				serials = getSerials(owner, sku, serialNumberLong);
			} 
			catch (RuntimeException e)
			{				
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_SaveASNReceipt_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
			}

			if (serials == null || serials.size() == 0)
			{
				throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
			}

			if ( isSerialDuplicate(((SerialNoDTO) serials.get(0)).getSerialnumber(), owner, sku) )
			{
				throw new UserException("WMEXP_DUPLICATE_SERIAL", new String[] { serialNumberLong });
			}

			//Set IOTHER1 with parsed Serial
			cwcdFocus.setValue("IOTHER1", ((SerialNoDTO) serials.get(0)).getSerialnumber());
		}
	}


	/**
	 * Checks if is serial duplicate.
	 *
	 * @param serialNumber the serial number
	 * @param storerkey the storerkey
	 * @param sku the sku
	 * @return true, if is serial duplicate
	 */
	public boolean isSerialDuplicate(String serialNumber, String storerkey, String sku)
	{
		boolean duplicateFound = false;

		try
		{

			SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);   

			if (skuConf.getICD1Unique().equalsIgnoreCase("1"))
			{
				String sql = " SELECT 1 FROM LOTXIDDETAIL, LOTXIDHEADER " +
				" WHERE lotxidheader.Storerkey = '"+storerkey+"' "+
				"AND lotxidheader.Sku = '"+sku+"' "+
				"AND lotxiddetail.IOTHER1 = '"+serialNumber+"' "+
				"AND lotxidheader.lotxidkey = lotxiddetail.lotxidkey ";

				EXEDataObject results = WmsWebuiValidationSelectImpl.select(sql);
				if (results.getRowCount() > 0){
					duplicateFound = true;
				}
			}

		} catch (DPException e) {
			e.printStackTrace();
			return false;
		}

		return duplicateFound;
	}


	/**
	 * Gets the serials.
	 *
	 * @param storerkey the storerkey
	 * @param sku the sku
	 * @param serial the serial
	 * @return the serials
	 */
	public ArrayList<?> getSerials(String storerkey, String sku, String serial)
	{	
		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);        

		SerialNumberObj serialNumber = new SerialNumberObj(skuConf);

		serialNumber.setStorerkey(storerkey);

		serialNumber.setSku(sku);

		ArrayList<?> list = serialNumber.getValidSerialNos(serial);

		return list;	
	}

	// adjust the toDate by the toDays interval, starting from fromDate.
	// if the addDays is true, increase; else decrease.
	//
	/**
	 * Adjust lottable days.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param toDays the to days
	 * @param addDays the add days
	 * @return the object
	 */
	public Object adjustLottableDays( Object fromDate, Object toDate, int toDays, boolean addDays)
	{

		if ( toDate == null && toDays > 0 )
		{
			GregorianCalendar gcFromDate, gcToDate;
			gcFromDate = (GregorianCalendar)fromDate;
			gcToDate = (GregorianCalendar)toDate;
			gcToDate = (GregorianCalendar)gcFromDate.clone();

			if ( addDays )
				gcToDate.add(GregorianCalendar.DATE, toDays);
			else
				gcToDate.add(GregorianCalendar.DATE, toDays * -1 );

			toDate = gcToDate;
		}

		return toDate;
	}
}