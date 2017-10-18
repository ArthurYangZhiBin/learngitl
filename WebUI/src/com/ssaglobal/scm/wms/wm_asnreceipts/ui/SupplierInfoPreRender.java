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
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;

public class SupplierInfoPreRender extends FormExtensionBase{
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws UserException {	
//		_log.debug("LOG_SYSTEM_OUT","In side PreRenderForm for ASN RECEIPTS Details",100L);
		try {
			// Add your code here to process the event
			if(!form.getFocus().isTempBio()){
				BioCollectionBean ipSupplierCollection = null;
				BioCollectionBean ipSupplierCollectionMax = null;
				BioBean ReceiptDetailBioBean = (BioBean)form.getFocus();

//HC
				String item = ReceiptDetailBioBean.getString("SKU").toString();
				String owner = ReceiptDetailBioBean.getString("STORERKEY").toString();
				
				String qry = "sku.STORERKEY='"+owner+"' AND sku.SKU ='"+item+"'";

				Query query1 = new Query("sku", qry, null);		
				BioCollectionBean itemBio = context.getState().getDefaultUnitOfWork().getBioCollectionBean(query1);
				if (!(isNull(itemBio, "SKUTYPE").equalsIgnoreCase("0"))){
					Object supplierName = ReceiptDetailBioBean.getValue("SupplierName");
					if(supplierName != null){
						UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
						ipSupplierCollection = querySupplierName(supplierName, uowb);//uowb.getBioCollectionBean(BioQuery);
						if(ipSupplierCollection.size() != 0){
							ipSupplierCollectionMax = querySupplierKeyMax(ipSupplierCollection, uowb);	
//							_log.debug("LOG_SYSTEM_OUT","ipSupplierCollectionMax.size() = "+ ipSupplierCollectionMax.size(),100L);
							setDisplays(form, ipSupplierCollectionMax.get("0"));
						}else{
							String queryString = "wm_storer.STORERKEY = '"+supplierName.toString()+"'";
//							_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ queryString,100L);
							Query query = new Query("wm_storer", queryString, null);
							BioCollectionBean supplierCollection = uowb.getBioCollectionBean(query);
							if(supplierCollection.size() != 0){
								BioBean supplier = supplierCollection.get("0");
								setWidget(supplier, form, "ADDRESS1", "SUPPLIERADDRESS1");
								setWidget(supplier, form, "ADDRESS2", "SUPPLIERADDRESS2");
								setWidget(supplier, form, "CITY", "SUPPLIERCITY");
								setWidget(supplier, form, "STATE", "SUPPLIERSTATE");
								setWidget(supplier, form, "ZIP", "SUPPLIERZIP");
								setWidget(supplier, form, "PHONE1", "SUPPLIERPHONE");
								setWidget(supplier, form, "COUNTRY", "SUPPLIERCOUNTRY");
							}
						}
					}						
				}else{
					form.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}				
			
//HC

			}
		}catch(Exception e){     
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 	
		return RET_CONTINUE;
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeFormExtendedInterface form) throws UserException {
//		_log.debug("LOG_SYSTEM_OUT","In side PreRenderForm for ASN RECEIPTS Details",100L);
		try{// Add your code here to process the event
			if(!form.getFocus().isTempBio()){
				BioCollectionBean ipSupplierCollection = null;
				BioCollectionBean ipSupplierCollectionMax = null;
				BioBean ReceiptDetailBioBean = (BioBean)form.getFocus();
//				HC
				String item = ReceiptDetailBioBean.getString("SKU").toString();
				String owner = ReceiptDetailBioBean.getString("STORERKEY").toString();
				
				String qry = "sku.STORERKEY='"+owner+"' AND sku.SKU ='"+item+"'";

				Query query1 = new Query("sku", qry, null);		
				BioCollectionBean itemBio = context.getState().getDefaultUnitOfWork().getBioCollectionBean(query1);
				if (!(isNull(itemBio, "SKUTYPE").equalsIgnoreCase("0"))){
					Object supplierName = ReceiptDetailBioBean.getValue("SupplierName");
					if(supplierName != null){
						UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
						form.getFormWidgetByName("SUPPLIERKEY").setDisplayValue(supplierName.toString());
						ipSupplierCollection = querySupplierName(supplierName, uowb);					
						ipSupplierCollectionMax = querySupplierKeyMax(ipSupplierCollection, uowb);			
//						_log.debug("LOG_SYSTEM_OUT","ipSupplierCollectionMax.size() = "+ ipSupplierCollectionMax.size(),100L);
						if(ipSupplierCollectionMax.size() != 0){
							setDisplays(form, ipSupplierCollectionMax.get("0"));
						}
					}
				}else{
					form.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
				}

					

			}
		}catch(Exception e){     
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 	
		return RET_CONTINUE;
	}
	
	private void setWidget(BioBean bio, RuntimeFormExtendedInterface form, String getName, String setName){
		if (bio.get(getName) != null){
			form.getFormWidgetByName(setName).setValue(bio.get(getName).toString());	
		}else{
			form.getFormWidgetByName(setName).setValue(" ");
		}
	}
	
	private BioCollectionBean querySupplierName(Object supplierName, UnitOfWorkBean uowb){
		String queryString = "wm_ipsupplier.SupplierName = '"+supplierName.toString()+"'";
		Query query = new Query("wm_ipsupplier", queryString, null);
		return uowb.getBioCollectionBean(query);
	}
	
	private BioCollectionBean querySupplierKeyMax(BioCollectionBean supplierCollection, UnitOfWorkBean uowb) throws EpiDataException{
		String queryString = "wm_ipsupplier.SupplierKey = '"+supplierCollection.max("SupplierKey").toString()+"'";
		Query query = new Query("wm_ipsupplier", queryString, null);
		return uowb.getBioCollectionBean(query);
	}
	
	private String checkNull(BioBean bio, String name){
		return bio.get(name) != null ? bio.get(name).toString() : " ";
	}
	
	private void setDisplays(RuntimeFormExtendedInterface form, BioBean bio){
		form.getFormWidgetByName("SUPPLIERKEY").setDisplayValue(checkNull(bio, "SupplierKey"));
		form.getFormWidgetByName("SUPPLIERADDRESS1").setDisplayValue(checkNull(bio, "SupplierAddress1"));
		form.getFormWidgetByName("SUPPLIERADDRESS2").setDisplayValue(checkNull(bio, "SupplierAddress2"));
		form.getFormWidgetByName("SUPPLIERCITY").setDisplayValue(checkNull(bio, "SupplierCity"));
		form.getFormWidgetByName("SUPPLIERCOUNTRY").setDisplayValue(checkNull(bio, "SupplierCountry"));
		form.getFormWidgetByName("SUPPLIERPHONE").setDisplayValue(checkNull(bio, "SupplierPhone"));
		form.getFormWidgetByName("SUPPLIERSTATE").setDisplayValue(checkNull(bio, "SupplierState"));
		form.getFormWidgetByName("SUPPLIERZIP").setDisplayValue(checkNull(bio, "SupplierZip"));
	}

	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}
}