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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
//import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class PopulateSupplierInfo extends com.epiphany.shr.ui.action.ActionExtensionBase {
	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
//		final String ATTRIBUTE_LABEL = "Supplier Name";
		RuntimeFormInterface form = context.getState().getCurrentRuntimeForm();
		String supplierName = form.getFormWidgetByName("SUPPLIERNAME").getDisplayValue();
		supplierName = isNull(supplierName) ? supplierName : supplierName.toUpperCase();
		int size = 0;
//		String displayValue = context.getSourceWidget().getDisplayValue().toString();
		BioCollectionBean ipSupplierCollection = null;
		BioCollectionBean storerCollection = null;
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set("SUPPLIERNAME", supplierName);
		}else{
			BioBean bio = (BioBean)focus;
			bio.set("SUPPLIERNAME", supplierName);
		}
		try{
			String queryString = "wm_ipsupplier.SupplierName = '"+supplierName+"'";
//			_log.debug("LOG_SYSTEM_OUT","queryString = "+ queryString,100L);
			Query query = new Query("wm_ipsupplier",queryString,null);
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
			ipSupplierCollection = uowb.getBioCollectionBean(query);
			size = ipSupplierCollection.size();
			if(size == 0){
				queryString = "wm_storer.STORERKEY = '" + supplierName + "' AND  wm_storer.TYPE = '5'";
//				_log.debug("LOG_SYSTEM_OUT","queryString = "+ queryString,100L);
				query = new Query("wm_storer",queryString,null);
				storerCollection = uowb.getBioCollectionBean(query);
				size = storerCollection.size();
				if(size != 0){
					BioBean storerBio = storerCollection.get("0");
					setWidget(storerBio, form, "ADDRESS1", "SUPPLIERADDRESS1");
					setWidget(storerBio, form, "ADDRESS2", "SUPPLIERADDRESS2");
					setWidget(storerBio, form, "CITY", "SUPPLIERCITY");
					setWidget(storerBio, form, "STATE", "SUPPLIERSTATE");
					setWidget(storerBio, form, "ZIP", "SUPPLIERZIP");
					setWidget(storerBio, form, "PHONE1", "SUPPLIERPHONE");
					setWidget(storerBio, form, "COUNTRY", "SUPPLIERCOUNTRY");
				}else{
					form.getFormWidgetByName("SUPPLIERADDRESS1").setValue(" ");
					form.getFormWidgetByName("SUPPLIERADDRESS2").setValue(" ");
					form.getFormWidgetByName("SUPPLIERCITY").setValue(" ");
					form.getFormWidgetByName("SUPPLIERSTATE").setValue(" ");
					form.getFormWidgetByName("SUPPLIERZIP").setValue(" ");
					form.getFormWidgetByName("SUPPLIERPHONE").setValue(" ");
					form.getFormWidgetByName("SUPPLIERCOUNTRY").setValue(" ");
				}
			}else{
				int i=0;
				String ipSupplierkey_max = ipSupplierCollection.max("SupplierKey").toString();
				String ipSupplierkey_i = "";
				for ( i=0; i< ipSupplierCollection.size(); i++) {
					BioBean ipSupplier_i = (BioBean)ipSupplierCollection.elementAt(i);
					ipSupplierkey_i = ipSupplier_i.getValue("SupplierKey").toString();
					if (ipSupplierkey_max.equals(ipSupplierkey_i)){
						form.getFormWidgetByName("SUPPLIERKEY").setValue(isEmpty(ipSupplier_i, "SupplierKey"));
						form.getFormWidgetByName("SUPPLIERADDRESS1").setValue(isEmpty(ipSupplier_i, "SupplierAddress1"));
						form.getFormWidgetByName("SUPPLIERADDRESS2").setValue(isEmpty(ipSupplier_i, "SupplierAddress2"));
						form.getFormWidgetByName("SUPPLIERCITY").setValue(isEmpty(ipSupplier_i, "SupplierCity"));
						form.getFormWidgetByName("SUPPLIERSTATE").setValue(isEmpty(ipSupplier_i, "SupplierState"));
						form.getFormWidgetByName("SUPPLIERZIP").setValue(isEmpty(ipSupplier_i, "SupplierZip"));
						form.getFormWidgetByName("SUPPLIERPHONE").setValue(isEmpty(ipSupplier_i, "SupplierPhone"));
						form.getFormWidgetByName("SUPPLIERCOUNTRY").setValue(isEmpty(ipSupplier_i, "SupplierCountry"));
					}
				}
			}  	  
		}catch(EpiException e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		} 
//		if (size == 0) {
//			RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
//			String WidgetName = context.getSourceWidget().getName();
//			String[] Param = new String[2];
//			Param[0]= displayValue;
//			Param[1] = ATTRIBUTE_LABEL;
//			throw new FieldException(FormName, WidgetName,"NotValidEntry", Param);
//		}
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
	
	private void setWidget(BioBean bio, RuntimeFormInterface form, String getName, String setName){
		if (bio.get(getName) != null){
			form.getFormWidgetByName(setName).setValue(bio.get(getName).toString());	
		}else{
			form.getFormWidgetByName(setName).setValue(" ");
		}
	}
	
	private boolean isNull(String value){
		if(value==null){
			return true;
		}
		return false;
	}
	
	private String isEmpty(BioBean bio, String name){
		Object obj = bio.getValue(name);
		if(obj==null){
			return " ";
		}else{
			return obj.toString();
		}
	}
}