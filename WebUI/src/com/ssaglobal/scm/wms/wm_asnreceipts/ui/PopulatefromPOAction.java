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
import java.util.TreeSet;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class PopulatefromPOAction extends ActionExtensionBase {
	protected static String TAB_0 = "tab 0";
	protected static String TAB_GROUP_SLOT = "tbgrp_slot";
	protected static String SLOT_1 = "list_slot_1";
	protected static String SLOT_2 = "list_slot_2";
	protected static String SETTINGS_TABLE = "wm_system_settings";
	/****************************************************************************
	 * Dec-03-2008 Krishna Kuchipudi: Modified createOrderAndOrderLineItems method, for the issue SDIS SCM-00000-05930
	 ****************************************************************************/

	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		//Get the shell
		RuntimeFormInterface tabGroupShellForm = (context.getSourceForm().getParentForm(context.getState()));
		try{
			//Perform action
			result.setFocus(createOrderAndOrderLineItems(context, result, tabGroupShellForm));
		}catch(Exception e){
			e.printStackTrace();
		}
		return RET_CONTINUE;
	}

	private DataBean createOrderAndOrderLineItems(ModalActionContext context, ActionResult result, RuntimeFormInterface tabGroupShellForm) throws EpiException {
        /**
         * MODIFICATION HISTORY
         * 10-14-2008  SCM  Updated set arguments to auto-fill UDF fields from PO (Machine#2044002_SDIS#05070)
         * Dec-03-2008 Krishna Kuchipudi: Modified for the issue SDIS SCM-00000-05930
         * 04/16/2009	HC	SCM-00000-05753
         * 					Get the tarrif key from the SKU and populate it in receiptdetail.

         */
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioBean receiptBioBean = null;
		BioBean receiptDetailBioBean = null;
		int maxLineNumber = 0;
		RuntimeListFormInterface poListForm = (RuntimeListFormInterface)context.getModalBodyForm(0);

		SlotInterface headerSlot = tabGroupShellForm.getSubSlot(SLOT_1);		
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		SlotInterface tabGroupSlot = headerForm.getSubSlot(TAB_GROUP_SLOT);
		RuntimeFormInterface receiptForm = state.getRuntimeForm(tabGroupSlot, TAB_0);

		String newLineNumber = null;
		boolean firstLine = true;
		QBEBioBean receiptDetailQBEBioBean = null;
		SlotInterface detailSlot = tabGroupShellForm.getSubSlot(SLOT_2);		
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		//SCM-00000-03768.b
		if(receiptForm.getFocus() instanceof QBEBioBean){
			SlotInterface detailTabGroupSlot = detailForm.getSubSlot(TAB_GROUP_SLOT);
			RuntimeFormInterface receiptDetailForm = state.getRuntimeForm(detailTabGroupSlot, TAB_0);
			receiptDetailQBEBioBean = (QBEBioBean)receiptDetailForm.getFocus();
		}
		//SCM-00000-03768.e

		//Create Order Line Items
		BioBean bio = uowb.getBioBean(poListForm.getSelectedListItem());
		if(receiptForm.getFocus() instanceof QBEBioBean){
			//Create Order Bio
//			receiptBioBean = createOrder(uowb, receiptForm, bio.getValue("STORERKEY"));
			receiptBioBean = createOrder(uowb, receiptForm, bio);
			maxLineNumber = 0;
		}else{
			receiptBioBean = (BioBean)receiptForm.getFocus();
			maxLineNumber = getMaxLineNumber(receiptBioBean);
		}

		BioCollectionBean currentBCBean = (BioCollectionBean)bio.get("PODETAIL");
		for(int index=0; index<currentBCBean.size(); index++){   	
			BioBean poDetailBio = (BioBean)currentBCBean.elementAt(index);
			//SCM-00000-03768 - start    if (firstLine){	
			if((firstLine) & (receiptForm.getFocus() instanceof QBEBioBean)){			
				//SCM-00000-03768 - end
				receiptDetailBioBean = uowb.getNewBio(receiptDetailQBEBioBean);
				firstLine = false;
			}else{
				receiptDetailBioBean = uowb.getNewBio("receiptdetail");	
			}
			newLineNumber = generateNewNumber(maxLineNumber, uowb);
			receiptDetailBioBean.set("RECEIPTKEY", receiptBioBean.get("RECEIPTKEY"));
			receiptDetailBioBean.set("RECEIPTLINENUMBER", newLineNumber);	
			receiptDetailBioBean.set("SKU", poDetailBio.get("SKU"));
//			receiptDetailBioBean.set("SKU_DESC", poDetailBio.get("SKUDESCRIPTION"));
			receiptDetailBioBean.set("PACKKEY", poDetailBio.get("PACKKEY"));
			receiptDetailBioBean.set("STORERKEY", poDetailBio.get("STORERKEY"));
			receiptDetailBioBean.set("UOM", poDetailBio.get("UOM"));
			/* Krishna Kuchipudi: SCM-00000-05930 related changes will start from here.  
        	 * Earler whenever Populate from PO feature is used, the Expected qty is equal to initial order qty.
        	 * After this modification the excepted qty is QtyOrdered - QtyReceived. 
        	 */
        	//receiptDetailBioBean.set("QTYEXPECTED", POLine.get("QTYORDERED")); Commented this line
        	double QtyReceived = Double.parseDouble(poDetailBio.get("QTYRECEIVED").toString());
        	double QtyOrdered = Double.parseDouble(poDetailBio.get("QTYORDERED").toString()); 	
        	double QtyStillRequired = QtyOrdered - QtyReceived ;
        	if(QtyStillRequired >= 0.0)
        	{receiptDetailBioBean.set("QTYEXPECTED", QtyStillRequired);}
        	else
        	{receiptDetailBioBean.set("QTYEXPECTED", "0.0");}
        	//Krishna Kuchipudi: SCM-00000-05930 related changes ends here.
			//receiptDetailBioBean.set("QTYEXPECTED", poDetailBio.get("QTYORDERED"));
			receiptDetailBioBean.set("QTYRECEIVED", "0.0");
			receiptDetailBioBean.set("QCREQUIRED", poDetailBio.get("QCREQUIRED"));
			receiptDetailBioBean.set("QCAUTOADJUST", poDetailBio.get("QCAUTOADJUST"));
			receiptDetailBioBean.set("TOLOC", WSDefaultsUtil.getDefaultRecieveLoc(state));
			receiptDetailBioBean.set("POKEY", poDetailBio.get("POKEY"));
			receiptDetailBioBean.set("POLineNumber", poDetailBio.get("POLINENUMBER"));
        	//10-14-2008 SCM Updated set arguments to auto-fill UDF fields from PO (Machine#2044002_SDIS#05070) - Start
			receiptDetailBioBean.set("SUSR1", poDetailBio.get("SUSR1"));
			receiptDetailBioBean.set("SUSR2", poDetailBio.get("SUSR2"));
			receiptDetailBioBean.set("SUSR3", poDetailBio.get("SUSR3"));
			receiptDetailBioBean.set("SUSR4", poDetailBio.get("SUSR4"));
			receiptDetailBioBean.set("SUSR5", poDetailBio.get("SUSR5"));
        	//10-14-2008 SCM Updated set arguments to auto-fill UDF fields from PO (Machine#2044002_SDIS#05070) - End
			receiptDetailBioBean.set("TARIFFKEY", getTarrifFromSku(poDetailBio.get("STORERKEY").toString(), poDetailBio.get("SKU").toString(), context ));		//SCM-00000-05753
			receiptDetailBioBean.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
			
			
			receiptDetailBioBean.set("REFERENCETYPE", "PurchaseOrder");
			receiptDetailBioBean.set("REFERENCEDOCUMENT", poDetailBio.getValue("EXTERNPOKEY"));
			receiptDetailBioBean.set("REFERENCELOCATION", poDetailBio.getValue("SOURCELOCATION"));
			receiptDetailBioBean.set("REFERENCEVERSION", poDetailBio.getValue("SOURCEVERSION"));		
			receiptDetailBioBean.set("REFERENCELINE", poDetailBio.getValue("EXTERNLINENO"));		
			
			receiptBioBean.addToBioCollection("RECEIPTDETAILS", receiptDetailBioBean);
			maxLineNumber+=1;
		}
		uowb.saveUOW(true);
		return (DataBean)receiptBioBean;
	}

    //SCM-00000-05753.b
    private String getTarrifFromSku(String storer, String sku, ModalActionContext context )throws EpiException{
    	
    	String qry = "sku.STORERKEY='"+storer+"' AND sku.SKU='"+sku+"'";
    	UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
    	Query query = new Query("sku", qry, null);		
		BioCollectionBean itemBio = uow.getBioCollectionBean(query);
		return itemBio.get("0").get("TARIFFKEY").toString();
  	}
    //SCM-00000-05753.e
	
	private BioBean createOrder(UnitOfWorkBean uowb, RuntimeFormInterface headerForm, BioBean bio) throws EpiException {
		QBEBioBean receiptQBEBioBean = (QBEBioBean)headerForm.getFocus();
		Object expDate = headerForm.getFormWidgetByName("EXPECTEDRECEIPTDATE").getValue();
		BioBean receiptBioBean = uowb.getNewBio(receiptQBEBioBean);
		receiptBioBean.set("RECEIPTID", GUIDFactory.getGUIDStatic());
		receiptBioBean.set("STORERKEY", bio.getValue("STORERKEY"));
		receiptBioBean.set("EXPECTEDRECEIPTDATE", expDate);
		receiptBioBean.set("REFERENCETYPE", "PurchaseOrder");
		receiptBioBean.set("REFERENCEDOCUMENT", bio.getValue("EXTERNPOKEY"));
		receiptBioBean.set("REFERENCELOCATION", bio.getValue("SOURCELOCATION"));
		receiptBioBean.set("REFERENCEVERSION", bio.getValue("SOURCEVERSION"));		

		//12/20/2010 FW:  Added code to populate supplier (Incident4199975_Defect 294868) -- Start
		receiptBioBean.set("SUPPLIERCODE", bio.getValue("SELLERNAME"));
		receiptBioBean.set("SUPPLIERNAME", bio.getValue("SELLERNAME"));
		receiptBioBean.set("SHIPFROMADDRESSLINE1", bio.getValue("SELLERADDRESS1"));
		receiptBioBean.set("SHIPFROMADDRESSLINE2", bio.getValue("SELLERADDRESS2"));
		receiptBioBean.set("SHIPFROMADDRESSLINE3", bio.getValue("SELLERADDRESS3"));
		receiptBioBean.set("SHIPFROMADDRESSLINE4", bio.getValue("SELLERADDRESS4"));
		receiptBioBean.set("SHIPFROMCITY", bio.getValue("SELLERCITY"));
		receiptBioBean.set("SHIPFROMSTATE", bio.getValue("SELLERSTATE"));
		receiptBioBean.set("SHIPFROMZIP", bio.getValue("SELLERZIP"));
		//12/20/2010 FW:  Added code to populate supplier (Incident4199975_Defect 294868) -- End

		return receiptBioBean;
	}

	private int getMaxLineNumber(BioBean objBioBeanOrder)throws EpiException{
		Object max = null;
		BioCollectionBean ReceiptDetailBiocollection = (BioCollectionBean)objBioBeanOrder.get("RECEIPTDETAILS");
		if(ReceiptDetailBiocollection.size()!=0){
			max = findMax(ReceiptDetailBiocollection, "RECEIPTLINENUMBER");
		}else{
			max = "0";
		}
		int size = Integer.parseInt(max.toString());
		return size;
	}

	private String generateNewNumber(int size, UnitOfWorkBean uowb)throws EpiException{
		String lineNumber = "";
		size+=1;
		String zeroPadding=null;
		String queryString = SETTINGS_TABLE+".CONFIGKEY = 'ZEROPADDEDKEYS'";
		Query bioQuery = new Query(SETTINGS_TABLE, queryString, null);
		BioCollectionBean bcBean = uowb.getBioCollectionBean(bioQuery);
		try{
			zeroPadding = bcBean.elementAt(0).get("NSQLVALUE").toString();
		}catch(EpiDataException e1){
			e1.printStackTrace();
		}
		if(zeroPadding.equalsIgnoreCase("1")){
			if(size<10000){
				lineNumber+="0";
				if(size<1000){
					lineNumber+="0";
					if(size<100){
						lineNumber+="0";
						if(size<10){
							lineNumber+="0";
						}
					}
				}
			}
		}

		lineNumber+=size;
		return lineNumber;
	}

	private long findMax(BioCollection bioCollection, String field) throws EpiDataException{
		TreeSet<Long> tree = new TreeSet<Long>();
		for(int index = 0; index<bioCollection.size(); index++){				
			Object bioFieldValueObj = bioCollection.elementAt(index).get(field);
			try{
				long tempBioLongValue = Long.parseLong(bioFieldValueObj.toString());
				Long bioFieldValue = new Long(tempBioLongValue);
				tree.add(bioFieldValue);
			}catch(NumberFormatException e1){					
			}
		}
		return ((Long)tree.last()).longValue();
	}
}