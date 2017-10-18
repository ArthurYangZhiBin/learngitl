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

//Import 3rd party packages and classes
import java.util.Iterator;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.*;

public class ASNReceiptDetailPreRender extends FormExtensionBase{
	public ASNReceiptDetailPreRender(){
	}

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException{
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface tabGroupShellForm = (form.getParentForm(context.getState()));
		SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");

		RuntimeFormWidgetInterface pokey = form.getFormWidgetByName("POKEY");
		RuntimeFormWidgetInterface storerKey = form.getFormWidgetByName("STORERKEY");
		RuntimeFormWidgetInterface sku = form.getFormWidgetByName("SKU");
		RuntimeFormWidgetInterface packkey = form.getFormWidgetByName("PACKKEY");
		RuntimeFormWidgetInterface toid = form.getFormWidgetByName("TOID");
		RuntimeFormWidgetInterface reasonCode = form.getFormWidgetByName("REASONCODE");

		//Lottables Form and widgets
		RuntimeFormInterface lottableTab = context.getState().getRuntimeForm(tabGroupSlot, "tab 1");
		RuntimeFormWidgetInterface[] lottable = new RuntimeFormWidgetInterface[10];
		initArray(lottable, lottableTab, "LOTTABLE", true);

		//QC Results Form qand widgets
		RuntimeFormInterface qcresultsTab = context.getState().getRuntimeForm(tabGroupSlot, "tab 3");
		RuntimeFormWidgetInterface qcStatus = qcresultsTab.getFormWidgetByName("QCSTATUS");
		RuntimeFormWidgetInterface qcQtyInspected = qcresultsTab.getFormWidgetByName("QCQTYINSPECTED");

		try {
			if (widgetName.equals("NEW")) {
				pokey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
				//Set Default Owner
				RuntimeFormInterface receiptForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(), "", "receipt_detail_view", context.getState());
				DataBean receiptFocus = receiptForm.getFocus();
				storerKey.setDisplayValue(BioAttributeUtil.getString(receiptFocus, "STORERKEY"));
				form.getFocus().setValue("STORERKEY", BioAttributeUtil.getString(receiptFocus, "STORERKEY"));
				
			} else {
				//				start 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				DataBean bio = form.getFocus();				
				if(!bio.isTempBio())
				{	//end 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					BioBean Receiptdetail = (BioBean)form.getFocus();
					String recDetailType = Receiptdetail.getValue("TYPE").toString();
					String status = form.getFormWidgetByName("STATUS").getValue().toString();
					String qtyReceived = form.getFormWidgetByName("QTYRECEIVED").getValue().toString();
					String qcQtyInspectedVal = qcQtyInspected.getValue().toString();
					Double doubQcQtyInspected = new Double (qcQtyInspectedVal);
					Double doubQtyReceived = new Double (qtyReceived);
					int intQtyReceived = (int)doubQtyReceived.doubleValue();
					int intQcQtyInspected = (int)doubQcQtyInspected.doubleValue();

					if (status.equalsIgnoreCase("9")){//bug 535:if received, all weight fields should not be editable
						form.getFormWidgetByName("GROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						form.getFormWidgetByName("NETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						form.getFormWidgetByName("TAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
					
					if (! status.equalsIgnoreCase("0")){
						toid.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						setArrayProperty(lottable, Boolean.TRUE);
					}
					if (status.equalsIgnoreCase("11")){
						form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, true);
					} else {
						form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);
					}
					if ((intQtyReceived > 0)){
						packkey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						if((status.equalsIgnoreCase("9"))|| (status.equalsIgnoreCase("5"))) {
							sku.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
							storerKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						}
					}
					if (recDetailType.equals("4")|| recDetailType.equals("5")){
						/*					expectedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					receivedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					tariffKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					if (MatchLottable.getValue().toString().equalsIgnoreCase("1")){
						lottable01.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);						
					}
					lottable02.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable03.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable04.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable05.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable06.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable07.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable08.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable09.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					lottable10.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					suser1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					suser2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					suser3.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					suser4.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					suser5.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					notes.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE); */
					}
					if (toid.getValue() == null){
						reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
					if (((qcStatus.getValue().toString().equalsIgnoreCase("C"))&&
							(intQcQtyInspected == intQtyReceived))||
							(qcStatus.getValue().toString().equalsIgnoreCase("N")) &&
							(intQtyReceived == 0)){
						qcStatus.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
					if ((qcStatus.getValue().toString().equalsIgnoreCase("C"))&&
							(intQcQtyInspected == intQtyReceived)){
						qcQtyInspected.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
				}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			}
			//			AW start 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			DataBean focus = form.getFocus(); 
			UnitOfWorkBean uow =context.getState().getDefaultUnitOfWork();
			String pack = null;
			if(focus.isTempBio())
			{
				QBEBioBean qbe = (QBEBioBean)focus;

				pack = qbe.getValue("PACKKEY")!=null ? qbe.getValue("PACKKEY").toString() : null;		
				if(pack !=null)
				{	
					String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uow);
					qbe.set("UOM", uom3);
				}
			}

			//AW end 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

		} catch(Exception e) {     
			e.printStackTrace();
			return RET_CANCEL;          
		} 	
		return RET_CONTINUE;
	}

	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		try {
		} catch(Exception e) { 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		return RET_CONTINUE;
	}

	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws UserException {
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		RuntimeFormInterface tabGroupShellForm = (form.getParentForm(context.getState()));
		SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");
		//Get all the forms
		RuntimeFormInterface lottableTab = context.getState().getRuntimeForm(tabGroupSlot, "tab 1");
		RuntimeFormInterface udfNotes = context.getState().getRuntimeForm(tabGroupSlot, "tab 2");
		RuntimeFormInterface qcResults = context.getState().getRuntimeForm(tabGroupSlot, "tab 3");
		RuntimeFormInterface supplier = context.getState().getRuntimeForm(tabGroupSlot, "tab 4");
		RuntimeFormInterface returns = context.getState().getRuntimeForm(tabGroupSlot, "tab 5");
		RuntimeFormInterface LPNDetailForm = context.getState().getRuntimeForm(tabGroupSlot, "tab 6");
		RuntimeFormInterface catchweightData = context.getState().getRuntimeForm(tabGroupSlot, "tab 7");
		RuntimeFormWidgetInterface pokey = form.getFormWidgetByName("POKEY");
		RuntimeFormWidgetInterface sku = form.getFormWidgetByName("SKU");
		RuntimeFormWidgetInterface storerKey = form.getFormWidgetByName("STORERKEY");
		RuntimeFormWidgetInterface packkey = form.getFormWidgetByName("PACKKEY");
		RuntimeFormWidgetInterface expectedQty = form.getFormWidgetByName("EXPECTEDQTY");
		RuntimeFormWidgetInterface receivedQty = form.getFormWidgetByName("RECEIVEDQTY");
		RuntimeFormWidgetInterface toid = form.getFormWidgetByName("TOID");
		RuntimeFormWidgetInterface reasonCode = form.getFormWidgetByName("REASONCODE");
		RuntimeFormWidgetInterface tariffKey = form.getFormWidgetByName("TARIFFKEY");
		RuntimeFormInterface currentForm = (RuntimeFormInterface) form;
		//Lottables Form and widgets

		RuntimeFormWidgetInterface[] lottable = new RuntimeFormWidgetInterface[10];
		initArray(lottable, lottableTab, "LOTTABLE", true);

		//UDF Notes Form and widgets
		RuntimeFormWidgetInterface[] suser = new RuntimeFormWidgetInterface[5];
		initArray(suser, udfNotes, "SUSR", false);
		RuntimeFormWidgetInterface notes = udfNotes.getFormWidgetByName("NOTES");
		//QC Results Form qand widgets

		RuntimeFormWidgetInterface qcStatus = qcResults.getFormWidgetByName("QCSTATUS");
		RuntimeFormWidgetInterface qcQtyInspected = qcResults.getFormWidgetByName("QCQTYINSPECTED");
		// LPNDetail List view Form

		try {
			DataBean detailFocus = form.getFocus();
			if (detailFocus instanceof QBEBioBean) {
				RuntimeFormInterface headerForm = FormUtil.findForm(currentForm,"wms_list_shell","receipt_detail_view",context.getState());
				DataBean headerFocus = headerForm.getFocus();
				String recType = headerFocus.getValue("TYPE").toString();
				if (recType.equals("2")){
					returns.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);
				}else{
					returns.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (recType.equals("4")|| recType.equals("5")){
					expectedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					receivedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					tariffKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					setArrayProperty(lottable, Boolean.TRUE);
					setArrayProperty(suser, Boolean.TRUE);
					notes.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					LPNDetailForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);
				} else {
					expectedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					receivedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					tariffKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					setArrayProperty(lottable, Boolean.FALSE);
					setArrayProperty(suser, Boolean.FALSE);
					notes.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					LPNDetailForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}	
			} else {
				String recDetailType ;
				String qcQtyInspectedVal;
				String qtyReceived;
				String qtyExpected;
				String uom;
				String packKey;
				String recDetailQcstatus;
				BioBean skubio=null;
				BioBean ReceiptdetailBioBean = (BioBean)detailFocus;
				recDetailType = ReceiptdetailBioBean.getValue("TYPE").toString();
				qcQtyInspectedVal = ReceiptdetailBioBean.getValue("QCQTYINSPECTED").toString();
				qtyReceived = ReceiptdetailBioBean.getValue("QTYRECEIVED").toString();
				qtyExpected = ReceiptdetailBioBean.getValue("QTYEXPECTED").toString();
				uom = ReceiptdetailBioBean.getValue("UOM").toString();
				packKey = ReceiptdetailBioBean.getValue("PACKKEY").toString();
				recDetailQcstatus = ReceiptdetailBioBean.getValue("QCSTATUS").toString();

				String status = form.getFormWidgetByName("STATUS").getValue().toString();
				Double doubQcQtyInspected = null;
				Double doubQtyReceived = null;
				int intQtyReceived = 0;
				int intQcQtyInspected =0;
				doubQcQtyInspected = new Double (qcQtyInspectedVal);
				intQcQtyInspected = (int)doubQcQtyInspected.doubleValue();

				doubQtyReceived = new Double (qtyReceived);
				intQtyReceived = (int)doubQtyReceived.doubleValue();

				String screenReceivedQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qtyReceived, packKey, context.getState(),UOMMappingUtil.uowNull, true);	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				String screenExpectedQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qtyExpected, packKey, context.getState(), UOMMappingUtil.uowNull, true); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				form.getFormWidgetByName("EXPECTEDQTY").setValue(screenExpectedQty);
				form.getFormWidgetByName("RECEIVEDQTY").setValue(screenReceivedQty);

								
				if (status.equalsIgnoreCase("9")){//bug 535:if received, all weight fields should not be editable
					form.getFormWidgetByName("GROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("NETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("TAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}

				if (! status.equalsIgnoreCase("0")){
					toid.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					setArrayProperty(lottable, Boolean.TRUE);
				}
				if ((intQtyReceived > 0)){
					packkey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					pokey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					if((status.equalsIgnoreCase("9"))|| (status.equalsIgnoreCase("5"))) {
						sku.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						storerKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
				}
				if (recDetailType.equals("4")|| recDetailType.equals("5")){
					expectedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					receivedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					tariffKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					setArrayProperty(lottable, Boolean.TRUE);
					setArrayProperty(suser, Boolean.TRUE);
					notes.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					LPNDetailForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);
				}else{
					expectedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					receivedQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					tariffKey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					setArrayProperty(lottable, Boolean.FALSE);
					setArrayProperty(suser, Boolean.FALSE);
					notes.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					LPNDetailForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}	
				if (recDetailType.equals("2")){
					returns.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);
				}else{
					returns.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (toid.getValue() == null){
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}else {
					reasonCode.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (((recDetailQcstatus.equalsIgnoreCase("C"))&&(intQcQtyInspected == intQtyReceived))||
						(recDetailQcstatus.equalsIgnoreCase("N")) && (intQtyReceived == 0)){
					qcStatus.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}else{
					qcStatus.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
				if ((recDetailQcstatus.equalsIgnoreCase("C"))&& (intQcQtyInspected == intQtyReceived)){
					qcQtyInspected.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}else{
					qcQtyInspected.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
				skubio = (BioBean)ReceiptdetailBioBean.get("SKUBIO");
				if (skubio.getValue("DESCR") != null){
					form.getFormWidgetByName("SKUDESC").setDisplayValue(skubio.getValue("DESCR").toString());					
				}
				RuntimeFormWidgetInterface LottableRequired = currentForm.getFormWidgetByName("LOTTABLESREQ");
				RuntimeFormWidgetInterface CatchWeightDataRequired = currentForm.getFormWidgetByName("CATCHWDREQ");
				String LottableValidationKey = skubio.getValue("LOTTABLEVALIDATIONKEY").toString();
				if (LottableRequiredCheck(LottableValidationKey, context)){
					LottableRequired.setValue("1");
				}else{
					LottableRequired.setValue("0");
				}
				String icwflag = skubio.getValue("ICWFLAG").toString();
				String icdflag = skubio.getValue("ICDFLAG").toString();
				if ((icwflag.equalsIgnoreCase("1")) || (icdflag.equalsIgnoreCase("1"))){
					CatchWeightDataRequired.setValue("1");
				}else{
					CatchWeightDataRequired.setValue("0");
				}

				if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(lottableTab);
					disableFormWidgets(udfNotes);
					disableFormWidgets(qcResults);
					disableFormWidgets(supplier);
					disableFormWidgets(returns);
					LPNDetailForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
					catchweightData.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}
			}
			//			catchweightData.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
		} catch(Exception e) {      
			e.printStackTrace();
			return RET_CANCEL;          
		} 	
		uow.clearState();
		return RET_CONTINUE;
	}


	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}

	private void disableFormWidgets(RuntimeFormInterface form) {
		for (Iterator it = form.getFormWidgets(); it.hasNext();) {
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}

	public static boolean LottableRequiredCheck(String LottableValidationKey, UIRenderContext context)throws EpiException{
		BioCollectionBean collection = getlottableValidation(LottableValidationKey, context);
		for(int i=0; i<10; i++){
			//Set lottable name
			String name = i>8 ? "LOTTABLE"+(i+1)+"ONRFRECEIPTMANDATORY" : "LOTTABLE0"+(i+1)+"ONRFRECEIPTMANDATORY";
			if(collection.get("0").getValue(name).toString().equalsIgnoreCase("1")){
				return true;
			}
		}
		return false;
	}

	private static BioCollectionBean getlottableValidation(String LottableValidationKey, UIRenderContext context)throws EpiException {
		String queryString = "(wm_lottablevalidation.LOTTABLEVALIDATIONKEY = '" + LottableValidationKey + "')";
		Query BioQuery = new Query("wm_lottablevalidation", queryString, null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
		if (listCollection.size()!= 0){
			return listCollection;
		} else{
			return null;
		}
	}

	public void populateSupplierInfo(UIRenderContext context, RuntimeFormExtendedInterface form){
		try {
			if(!(form.getFocus() instanceof QBEBioBean)) {
				BioCollectionBean ipSupplierCollection = null;
				BioCollectionBean ipSupplierCollectionMax = null;
				BioBean ReceiptDetailBioBean = (BioBean)form.getFocus();
				Object SupplierName = ReceiptDetailBioBean.getValue("SupplierName");
				if (SupplierName != null){
					String sQueryString = "(wm_ipsupplier.SupplierName = '"+SupplierName.toString()+"')";
					Query BioQuery = new Query("wm_ipsupplier",sQueryString,null);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					ipSupplierCollection = uow.getBioCollectionBean(BioQuery);
					if (ipSupplierCollection.size() != 0) {
						String sQueryString1 = "(wm_ipsupplier.SupplierKey = '"+ipSupplierCollection.max("SupplierKey").toString()+"')";
						Query BioQuery1 = new Query("wm_ipsupplier",sQueryString1,null);
						ipSupplierCollectionMax = uow.getBioCollectionBean(BioQuery1);

						form.getFormWidgetByName("SUPPLIERKEY").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierKey").toString());
						form.getFormWidgetByName("SUPPLIERADDRESS1").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierAddress1").toString());
						form.getFormWidgetByName("SUPPLIERADDRESS2").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierAddress2").toString());
						form.getFormWidgetByName("SUPPLIERCITY").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierCity").toString());
						form.getFormWidgetByName("SUPPLIERCOUNTRY").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierCountry").toString());
						form.getFormWidgetByName("SUPPLIERPHONE").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierPhone").toString());
						form.getFormWidgetByName("SUPPLIERSTATE").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierState").toString());
						form.getFormWidgetByName("SUPPLIERZIP").setDisplayValue(ipSupplierCollectionMax.get("0").get("SupplierZip").toString());
					}else{
						String sQueryString1 = "(wm_storer.STORERKEY = '"+SupplierName.toString()+"')";
						Query BioQuery1 = new Query("wm_storer",sQueryString1,null);
						BioCollectionBean supplierCollection = uow.getBioCollectionBean(BioQuery1);
						if (supplierCollection.size() != 0) {
							checkNullInfo(form, supplierCollection, "SUPPLIERADDRESS1", "ADDRESS1");
							checkNullInfo(form, supplierCollection, "SUPPLIERADDRESS2", "ADDRESS2");
							checkNullInfo(form, supplierCollection, "SUPPLIERCITY", "CITY");
							checkNullInfo(form, supplierCollection, "SUPPLIERSTATE", "STATE");
							checkNullInfo(form, supplierCollection, "SUPPLIERZIP", "ZIP");
							checkNullInfo(form, supplierCollection, "SUPPLIERPHONE", "PHONE1");
							checkNullInfo(form, supplierCollection, "SUPPLIERCOUNTRY", "COUNTRY");
						}
					}
				}
			}
		} catch(Exception e) {     
			e.printStackTrace();
		}
	}

	private void checkNullInfo(RuntimeFormExtendedInterface form, BioCollectionBean collection, String setWidget, String getWidget) throws EpiException{
		if (collection.get("0").get(getWidget) != null){
			form.getFormWidgetByName(setWidget).setValue(collection.get("0").get(getWidget).toString());	
		}else{
			form.getFormWidgetByName(setWidget).setValue("");
		}
	}

	private void initArray(RuntimeFormWidgetInterface[] array, RuntimeFormInterface form, String base, boolean isLottable){
		String name;
		for(int i=0; i<array.length; i++){
			if(isLottable){
				name = i>8 ? base+(i+1) : base+"0"+(i+1);
			} else {
				name = base+(i+1);
			}
			array[i] = form.getFormWidgetByName(name);
		}
	}

	private void setArrayProperty(RuntimeFormWidgetInterface[] array, Boolean bool){
		for(int i=0; i<array.length; i++){
			array[i].setProperty(RuntimeFormWidgetInterface.PROP_READONLY, bool);
		}
	}
}