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


import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ReceiptValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptValidationAction.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		String detailFormTab = "wm_receiptdetail_detail_view";
		String owner = null;
		String sku = null;
		Object pokey = null;
		String receiptline = null;
		QBEBioBean headerQBEBioBean = null;
		QBEBioBean detailQBEBioBean = null;
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1
		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);				//Set slot 2
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	//Get the form at slot2
		_log.debug("LOG_SYSTEM_OUT","Detail Form = " + detailForm.getName(),100L);
		RuntimeFormInterface detailTab= null;										// this holds the toggle form slot content
		if (detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view")){	//if the slot is populated by toggle form then
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);		
			_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
			detailTab = state.getRuntimeForm(toggleSlot, detailFormTab);
			_log.debug("LOG_SYSTEM_OUT","detailTab = "+ detailTab.getName(),100L);
		}
		
		
		if (headerFocus instanceof QBEBioBean){
			headerQBEBioBean = (QBEBioBean)headerFocus;
			detailQBEBioBean = (QBEBioBean)detailForm.getFocus();
			owner = headerQBEBioBean.getValue("STORERKEY").toString();
			sku = detailQBEBioBean.getValue("SKU").toString();
			pokey = detailQBEBioBean.getValue("POKEY");
			receiptline = detailQBEBioBean.getValue("RECEIPTLINENUMBER").toString();
		}
		
		String ReceiptValidationKey = getFieldValuefromSku(owner,sku,"RECEIPTVALIDATIONTEMPLATE",context);
		receiptValidations(ReceiptValidationKey, pokey, receiptline, sku, context);

		return RET_CONTINUE;
		
	}
	private String getFieldValuefromSku(String Owner, String sku, String FieldName, ActionContext context) throws EpiException {
		BioCollectionBean listCollection = null;
		String FieldValue = "";
		String sQueryString = "(wm_sku.STORERKEY = '" + Owner + "' AND  wm_sku.SKU = '"+sku+"')";
   	   	_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
   	   	Query BioQuery = new Query("wm_sku",sQueryString,null);
   	   	UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
   	   	listCollection = uow.getBioCollectionBean(BioQuery);
   	   	if (listCollection.size()!= 0){
   	   	FieldValue = listCollection.get("0").get(FieldName).toString();
   	   		return FieldValue;
   	   	}
   	   	else{
      		return "";
   	   	}
   	 
	}
	private void receiptValidations(String ReceiptValidationKey, Object pokey, String receiptLinenumber, String sku, ActionContext context)throws EpiException {
		BioCollectionBean listCollection = null;
		String receiptWithoutPO = "";
		String receiptWithoutASN = "";
		String commodityNotOnPO = "";
		String commodityNotOnASN = "";
		String lPNNotOnCaseLevelASN = "";
		String receiptWithoutLPN = "";
		String performQtyValidation = "";
		String overageMessage = "";
		String shortageMessage = "";
		String overageOverride = "";
		Object overageOveridePercent = null;
		Object overageHardErrorPercent = null;
		String overageHardError = "";
		String shortageOverride = "";
		Object shortageOverridePercent = "";
		String sQueryString = "(wm_receiptvalidation.RECEIPTVALIDATIONKEY = '" + ReceiptValidationKey + "')";
   	   	_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
   	   	Query BioQuery = new Query("wm_receiptvalidation",sQueryString,null);
   	   	UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
   	   	listCollection = uow.getBioCollectionBean(BioQuery);
   	   	if (listCollection.size()!= 0){
   	   		receiptWithoutPO = listCollection.get("0").get("RECEIPTWITHOUTPO").toString();
   			receiptWithoutASN = listCollection.get("0").get("RECEIPTWITHOUTASN").toString();
   			commodityNotOnPO = listCollection.get("0").get("COMMODITYNOTONPO").toString();
   			commodityNotOnASN = listCollection.get("0").get("COMMODITYNOTONASN").toString();
   			lPNNotOnCaseLevelASN = listCollection.get("0").get("LPNNOTONCASELEVELASN").toString();
   			receiptWithoutLPN = listCollection.get("0").get("RECEIPTWITHOUTLPN").toString();
   			performQtyValidation = listCollection.get("0").get("PERFORMQTYVALIDATION").toString();
   			overageMessage = listCollection.get("0").get("OVERAGEMESSAGE").toString();
   			shortageMessage = listCollection.get("0").get("SHORTAGEMESSAGE").toString();
   			overageOverride = listCollection.get("0").get("OVERAGEOVERRIDE").toString();
   			overageOveridePercent = listCollection.get("0").get("OVERAGEOVERIDEPERCENT");
   			overageHardError = listCollection.get("0").get("OVERAGEHARDERROR").toString();
   			overageHardErrorPercent = listCollection.get("0").get("OVERAGEHARDERRORPERCENT");
   			shortageOverride = listCollection.get("0").get("SHORTAGEOVERRIDE").toString();
   			shortageOverridePercent = listCollection.get("0").get("SHORTAGEOVERRIDEPERCENT");
   	   	}
   	   	if (pokey == null) {
   	   		if (receiptWithoutPO.equals("9")){
				String[] ErrorParem = new String[2];
				ErrorParem[0]= receiptLinenumber;
				ErrorParem[1] = sku;
				UserException UsrExcp = new UserException("WMEXP_RECEIPTVALIDATION_POREQUIRED", ErrorParem);
				throw UsrExcp;
   	   		}
   	   		else if (receiptWithoutPO.equals("5")){
   	   			
   	   		}
   	   	}
	}

}
