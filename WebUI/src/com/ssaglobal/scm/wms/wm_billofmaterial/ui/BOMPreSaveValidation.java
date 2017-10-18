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
package com.ssaglobal.scm.wms.wm_billofmaterial.ui;


import java.text.NumberFormat;
import java.text.ParseException;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_conditional_validation.ui.ConditionalValidationDataObject;

public class BOMPreSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BOMPreSaveValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_BOM","Executing  BOMPreSaveValidation",100L);
		BillOfMaterialDataObject obj = new BillOfMaterialDataObject();
		
		StateInterface state = context.getState();	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewSubOwner = false;
		boolean isNewSubItem = false;
		
		if (headerFocus instanceof BioBean)
		{
			headerFocus = (BioBean)headerFocus;
		}

//ExportToXLS		
		//populate data object
		obj.setBomKey((String)headerFocus.getValue("BOMHDRDEFNKEY"));
		obj.setParentStorer((String)headerFocus.getValue("PARENTSTORERKEY"));
		obj.setParentSKU((String)headerFocus.getValue("PARENTSKU"));
		
	

		//get detail form
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	
	    if (!detailForm.getName().equals("wm_billofmaterialdetail_detail_view"))
	    {
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_billofmaterialdetail_toggle_slot"), "Detail"  );	
	    }		
		
	    DataBean detailFocus= null;
	    if(detailForm.getName().equals("wm_billofmaterialdetail_detail_view"))
	    {
		detailFocus = detailForm.getFocus();
		
		if (detailFocus instanceof BioBean)
			detailFocus = (BioBean)detailFocus;
		else if(detailFocus instanceof QBEBioBean)
			detailFocus = (QBEBioBean)detailFocus;
		
	
		obj.setStorer((String)detailFocus.getValue("STORERKEY"));
		obj.setItem((String)detailFocus.getValue("SKU"));
		//obj.setreqQty((String)detailFocus.getValue("QTYREQUIRED"));
		obj.setreqQty(detailForm.getFormWidgetByName("QTYREQUIRED").getDisplayValue());
		
		Object subStorer = detailFocus.getValue("SUBSTSTORERKEY");
		if( subStorer != null && !subStorer.toString().equalsIgnoreCase("")){
			obj.setSubOwner(subStorer.toString().toUpperCase());
			isNewSubOwner= true;
		}

		Object subItem = detailFocus.getValue("SUBSTSKU");
		if( subItem != null && !subItem.toString().equalsIgnoreCase("")){
			obj.setSubItem(subItem.toString().toUpperCase());
			isNewSubItem= true;
		}

	    }			
	/********************************************************************************
	 * VALIDATIONS
	 ********************************************************************************/		
		
		if(headerFocus.isTempBio()){
			isDupPK(obj);
			}
		
		
		
		 _log.debug("LOG_DEBUG_EXTENSION_BOM","Validating Parent Owner",100L);
			isValidOwner(obj, obj.getParentStorer(), "Parent Owner", headerFocus, "PARENTSTORERKEY");

			_log.debug("LOG_DEBUG_EXTENSION_BOM","Validating Parent Item",100L);
			isValidSKU(obj, obj.getParentSKU(), "Parent Item", headerFocus, "PARENTSKU");
	
			_log.debug("LOG_DEBUG_EXTENSION_BOM","Validating Item- Owner Comb",100L);
			isValidItemOwnerComb(obj, obj.getParentSKU(), obj.getParentStorer(), "Parent Item", "Parent Owner");

			
			//detail Form validations
		
			if(detailForm.getName().equals("wm_billofmaterialdetail_detail_view"))
			{
				_log.debug("LOG_DEBUG_EXTENSION_BOM","Validating Owner",100L);
			isValidOwner(obj, obj.getStorer(), "Owner", detailFocus, "STORERKEY");
			
	
			_log.debug("LOG_DEBUG_EXTENSION_BOM","Validating Item",100L);
			isValidSKU(obj, obj.getItem(), "Component", detailFocus, "SKU");
			
			
			if(!(obj.getItem() == null) && !(obj.getStorer() == null))
			{
			isValidItemOwnerComb(obj, obj.getItem(), obj.getStorer(), "Component", "Owner" );
			}
			//detailFocus.setValue("STORERKEY", obj.getStorer().toUpperCase());
			//detailFocus.setValue("SKU", obj.getItem());
		
		if(isNewSubOwner){
			isValidOwner(obj, obj.getSubOwner(), "Substitute Owner", detailFocus, "SUBSTSTORERKEY");			
				if(isNewSubItem){
				isValidSKU(obj, obj.getSubItem(), "Substitute Component", detailFocus, "SUBSTSKU");
				
	
				isValidItemOwnerComb(obj, obj.getSubItem(), obj.getSubOwner(), "Substitute Component","Substitute Owner" );
				
			}
			}
			
			
			try {
			isGreaterThanZero(obj);
			}catch(DPException e) {
				e.printStackTrace();
			}
			}
			
			_log.debug("LOG_DEBUG_EXTENSION_BOM","Exiting  BOMPreSaveValidation",100L);
		return RET_CONTINUE;
		
}

	private void isDupPK(BillOfMaterialDataObject data)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isDupPK",100L);
		try {
		String PK = data.getBomKey();
		String sql ="select * from BOMHDRDEFN where bomhdrdefnkey='"+PK+"'";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			String [] param = new String[1];
			param[0] = data.getBomKey();
			throw new UserException("WMEXP_DUP_BOM_NAME", param);
		}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isDupPK",100L);
	}
	private void isValidSKU(BillOfMaterialDataObject data, String Item, String str, DataBean focus, String widget)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidSKU",100L);
		try {
			if(!(Item == null) && !Item.equalsIgnoreCase(""))
			{
				String sku = Item.toUpperCase();
				String sql ="select * from SKU where sku='"+sku+"'";
				EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
					if(dataObject.getCount()< 1){
						String [] param = new String[2];
						param[0] = Item.toUpperCase();
						param[1] = str;
						throw new UserException("WMEXP_INV_SKU", param);
					}
									
						focus.setValue(widget, Item.toUpperCase());
			}
			}
		 catch (DPException e) {
			e.printStackTrace();
		}

			_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isValidSKU",100L);
	}
	private void isValidOwner(BillOfMaterialDataObject data, String Owner, String str, DataBean focus, String widget)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidOwner",100L);
		try {
			if(!(Owner == null) && !Owner.equalsIgnoreCase(""))
			{
				String owner = Owner;
				String sql ="select * from STORER where type='1' and storerkey='"+owner+"'";
				EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
				if(dataObject.getCount()<1){
					String [] param = new String[2];
					param[0] = Owner;
					param[1]= str;
					throw new UserException("WMEXP_INV_OWNER", param);
		}			
					focus.setValue(widget, Owner.toUpperCase());			
		}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isValidOwner",100L);
	}
	private void isGreaterThanZero(BillOfMaterialDataObject data)throws DPException, UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isGreaterThanZero",100L);
		String qty = data.getreqQty();
		double value = 0;
		int flag =0;

		if(!(qty == null) && !qty.toString().equalsIgnoreCase(""))
		{
			try
			{
				NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
				value = nf.parse(qty.toString()).doubleValue();
			} catch (ParseException e)
			{
				NumberFormat nfc = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0); //AW
				try
				{
				value = nfc.parse(qty.toString()).doubleValue();
				} catch (ParseException e1)
				{
					throw new DPException("System_Error");
				}
				
				
				if (!qty.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*"))
				{
				_log.debug("LOG_SYSTEM_OUT","!@@ Throwing Exception by regex",100L);
				throw new DPException("System_Error");
				}
			}
			if (value < 1)
			{
				String [] param = new String[1];
				param[0] = data.getreqQty();
				throw new UserException("WMEXP_NEG_QTY", param);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isGreaterThanZero",100L);
	}
	private void isValidItemOwnerComb(BillOfMaterialDataObject data, String Item, String Owner, String itemLabel, String ownerLabel)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidItemOwnerComb",100L);
		try {			
		String item= Item;
		String storer= Owner;
				
		String sql ="select * from SKU where storerkey='"+storer+"' and sku='"+item+"'";
		
		EXEDataObject	dataObject = WmsWebuiValidationSelectImpl.select(sql);
	
		if(dataObject.getCount()< 1){
			String [] param = new String[4];
			param[0] = Item;
			param[1] = Owner;
			param[2] = itemLabel;
			param[3] = ownerLabel;
			
			throw new UserException("WMEXP_SKU_OWNER_COMB", param);	
		}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidItemOwnerComb",100L);
	}
	
}
