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
 * (c) COPYRIGHT 2010 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PopulateSupplierInfoHeader extends com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param widget The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateCarrierInfo.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		DataBean focus = context.getState().getCurrentRuntimeForm().getFocus();
		final String type = ReturnPartyQueryAction.getStorerType(focus);
		final String name = "Supplier";
		final String bio = "wm_storer";
		final String attribute = "STORERKEY";
		int size = 0;
		String displayValue;
		Object objDisplayValue = context.getSourceWidget().getDisplayValue();
		if (objDisplayValue == null){
			displayValue = "";
		}else
		{
			displayValue = objDisplayValue.toString();
			RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
			try {
				String sQueryString = bio + "." + attribute + " = '" + displayValue + "'" + " AND " + bio + ".TYPE = " + type;
				_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
				Query BioQuery = new Query(bio,sQueryString,null);
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
				size = listCollection.size();
				if  (size != 0){
					populateSupplierInfo(FormName , listCollection, result);
				}
			} catch(EpiException e) {
				// Handle Exceptions 
				e.printStackTrace();
				return RET_CANCEL;
			} 
			if (size == 0) {
				context.getSourceWidget().setDisplayValue("");
				clearBadCarrierInfo (FormName,result);
				String WidgetName = context.getSourceWidget().getName();
				String[] ErrorParem = new String[2];
				ErrorParem[0]= displayValue;
				ErrorParem[1] = name;
				_log.debug("LOG_SYSTEM_OUT","Invalid entry = " + name,100L);
				FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
				throw UsrExcp;
			}
		}
		return RET_CONTINUE;
	}
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			// Add your code here to process the event

		} catch(Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}
	public void populateSupplierInfo(RuntimeFormInterface form, BioCollectionBean listCollection, ActionResult result)throws EpiException {

		if (form.getFocus().isTempBio()){
			QBEBioBean receiptQBEBioBean = (QBEBioBean)form.getFocus();
			receiptQBEBioBean.set("SUPPLIERCODE",  listCollection.get("0").getValue("STORERKEY"));
			receiptQBEBioBean.set("SUPPLIERNAME",  listCollection.get("0").getValue("COMPANY"));
			receiptQBEBioBean.set("ShipFromAddressLine1",  listCollection.get("0").getValue("ADDRESS1"));
			receiptQBEBioBean.set("ShipFromAddressLine2",  listCollection.get("0").getValue("ADDRESS2"));
			receiptQBEBioBean.set("ShipFromCity",  listCollection.get("0").getValue("CITY"));
			receiptQBEBioBean.set("ShipFromState",  listCollection.get("0").getValue("STATE"));
			receiptQBEBioBean.set("ShipFromZip",  listCollection.get("0").getValue("ZIP"));
			receiptQBEBioBean.set("ShipFromPhone",  listCollection.get("0").getValue("PHONE1"));
//Incident3906418_Defect278753.b
    		receiptQBEBioBean.set("ShipFromAddressLine3",  listCollection.get("0").getValue("ADDRESS3"));
    		receiptQBEBioBean.set("ShipFromAddressLine4",  listCollection.get("0").getValue("ADDRESS4"));
    		receiptQBEBioBean.set("ShipFromISOCountry",  listCollection.get("0").getValue("ISOCNTRYCODE"));
    		receiptQBEBioBean.set("ShipFromContact",  listCollection.get("0").getValue("CONTACT1"));
    		receiptQBEBioBean.set("ShipFromEmail",  listCollection.get("0").getValue("EMAIL1"));
//Incident3906418_Defect278753.e
			
			result.setFocus(receiptQBEBioBean);
		}else{
			BioBean receiptBioBean = (BioBean)form.getFocus();
			receiptBioBean.set("SUPPLIERCODE",  listCollection.get("0").getValue("STORERKEY"));
			receiptBioBean.set("SUPPLIERNAME",  listCollection.get("0").getValue("COMPANY"));
			receiptBioBean.set("ShipFromAddressLine1",  listCollection.get("0").getValue("ADDRESS1"));
			receiptBioBean.set("ShipFromAddressLine2",  listCollection.get("0").getValue("ADDRESS2"));
			receiptBioBean.set("ShipFromCity",  listCollection.get("0").getValue("CITY"));
			receiptBioBean.set("ShipFromState",  listCollection.get("0").getValue("STATE"));
			receiptBioBean.set("ShipFromZip",  listCollection.get("0").getValue("ZIP"));
			receiptBioBean.set("ShipFromPhone",  listCollection.get("0").getValue("PHONE1"));
//Incident3906418_Defect278753.b
    		receiptBioBean.set("ShipFromAddressLine3",  listCollection.get("0").getValue("ADDRESS3"));
    		receiptBioBean.set("ShipFromAddressLine4",  listCollection.get("0").getValue("ADDRESS4"));
    		receiptBioBean.set("ShipFromISOCountry",  listCollection.get("0").getValue("ISOCNTRYCODE"));
    		receiptBioBean.set("ShipFromContact",  listCollection.get("0").getValue("CONTACT1"));
    		receiptBioBean.set("ShipFromEmail",  listCollection.get("0").getValue("EMAIL1"));
//Incident3906418_Defect278753.e			
			result.setFocus(receiptBioBean);
		}
	}
	public void clearBadCarrierInfo (RuntimeFormInterface form, ActionResult result)throws EpiException {
		if (form.getFocus().isTempBio()){
			QBEBioBean receiptQBEBioBean = (QBEBioBean)form.getFocus();
			receiptQBEBioBean.set("SUPPLIERCODE","");
			receiptQBEBioBean.set("SUPPLIERNAME","");
			receiptQBEBioBean.set("ShipFromAddressLine1",  "");
			receiptQBEBioBean.set("ShipFromAddressLine2",  "");
			receiptQBEBioBean.set("ShipFromCity",  "");
			receiptQBEBioBean.set("ShipFromState",  "");
			receiptQBEBioBean.set("ShipFromZip",  "");
			receiptQBEBioBean.set("ShipFromPhone",  "");
//Incident3906418_Defect278753.b
    		receiptQBEBioBean.set("ShipFromAddressLine3", "");
    		receiptQBEBioBean.set("ShipFromAddressLine4", "");
    		receiptQBEBioBean.set("ShipFromISOCountry", "");
    		receiptQBEBioBean.set("ShipFromContact", "");
    		receiptQBEBioBean.set("ShipFromEmail", "");
//Incident3906418_Defect278753.e			
			result.setFocus(receiptQBEBioBean);
		}else{
			BioBean receiptBioBean = (BioBean)form.getFocus();
			receiptBioBean.set("SUPPLIERCODE","");
			receiptBioBean.set("SUPPLIERNAME",  "");
			receiptBioBean.set("ShipFromAddressLine1",  "");
			receiptBioBean.set("ShipFromAddressLine2",  "");
			receiptBioBean.set("ShipFromCity",  "");
			receiptBioBean.set("ShipFromState",  "");
			receiptBioBean.set("ShipFromZip",  "");
			receiptBioBean.set("ShipFromPhone",  "");
//Incident3906418_Defect278753.b
    		receiptBioBean.set("ShipFromAddressLine3", "");
    		receiptBioBean.set("ShipFromAddressLine4", "");
    		receiptBioBean.set("ShipFromISOCountry", "");
    		receiptBioBean.set("ShipFromContact", "");
    		receiptBioBean.set("ShipFromEmail", "");
//Incident3906418_Defect278753.e			
			result.setFocus(receiptBioBean);
		}
	}

}

