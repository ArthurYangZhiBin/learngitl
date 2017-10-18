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

import com.agileitp.forte.framework.DataValue;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class BOMSave extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BillOfMaterialPreDelete.class);
	protected int execute( ActionContext context, ActionResult result ) throws UserException {

		 _log.debug("LOG_DEBUG_EXTENSION_BOM","Executing BOMSave",100L);
		BillOfMaterialDataObject obj = new BillOfMaterialDataObject();
		
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_billofmaterialdetail_toggle_slot";
		String detailBiocollection = "BILLOFMATERIALDETAIL";
		String detailFormTab = "Detail";
		boolean isNewSubOwner = false;
		boolean isNewSubItem = false;
		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1
		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);				//Set slot 2
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	//Get the form at slot2
		_log.debug("LOG_SYSTEM_OUT","***Toggle Form = " + detailForm.getName(),100L);
		RuntimeFormInterface detailTab= null;		
		if (detailForm.getName().equalsIgnoreCase(toggleFormSlot)){	//if the slot is populated by toggle form then
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);		
			detailTab = state.getRuntimeForm(toggleSlot, "Detail");
		}
		SlotInterface tabGroupSlot = getTabSlot(state, detailForm);		

		if((!tabGroupSlot.getName().equals("wm_billofmaterialdetail_toggle_slot")))
		{
		RuntimeFormInterface detailLottableForm = state.getRuntimeForm(tabGroupSlot, "tab 1");
		RuntimeFormInterface detailBOMForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
		
		DataBean detailFocus = null;		
		BioBean headerBioBean = null;
		BioBean detailBioBean = null;
		Object ObjRecDetailLpn = null;
		QBEBioBean detailQBEBioBean = null; 
		RuntimeFormInterface RoutingDetail = null;
		DataBean hdrFocus= null;
		
		
		DataBean BOMDetailFocus = detailBOMForm.getFocus();
		if(BOMDetailFocus.isTempBio())
		{
			BOMDetailFocus = (QBEBioBean)BOMDetailFocus;
		}
		else
		{
			BOMDetailFocus = (BioBean)BOMDetailFocus;
		}
		
		hdrFocus= headerForm.getFocus();
		if(hdrFocus.isTempBio())
		{
			hdrFocus= (QBEBioBean)hdrFocus;
		}
		else
		{
			hdrFocus= (BioBean)hdrFocus;
		}
		
		
		DataBean detailLottableFocus = detailLottableForm.getFocus();
		
		//populate data object
		obj.setBomKey((String)hdrFocus.getValue("BOMHDRDEFNKEY"));
		obj.setParentStorer((String)hdrFocus.getValue("PARENTSTORERKEY"));
		obj.setParentSKU((String)hdrFocus.getValue("PARENTSKU"));
		
		
		obj.setStorer((String)BOMDetailFocus.getValue("STORERKEY"));
		obj.setItem((String)BOMDetailFocus.getValue("SKU"));
		//obj.setreqQty((String)detailFocus.getValue("QTYREQUIRED"));
		obj.setreqQty(detailBOMForm.getFormWidgetByName("QTYREQUIRED").getDisplayValue());
		
		Object subStorer = BOMDetailFocus.getValue("SUBSTSTORERKEY");
		if( subStorer != null && !subStorer.toString().equalsIgnoreCase("")){
			obj.setSubOwner(subStorer.toString().toUpperCase());
			isNewSubOwner= true;
		}

		Object subItem = BOMDetailFocus.getValue("SUBSTSKU");
		if( subItem != null && !subItem.toString().equalsIgnoreCase("")){
			obj.setSubItem(subItem.toString().toUpperCase());
			isNewSubItem= true;
		}
		
		
		checkNullLottables(detailLottableForm, BOMDetailFocus);
		
		if(headerFocus.isTempBio()){
			isDupPK(obj);
			}
		
			isValidOwner(obj, obj.getParentStorer(), "Parent Owner", headerFocus, "PARENTSTORERKEY");
			isValidSKU(obj, obj.getParentSKU(), "Parent Item", headerFocus, "PARENTSKU");
			isValidItemOwnerComb(obj, obj.getParentSKU(), obj.getParentStorer(), "Parent Item", "Parent Owner");

			
			//detail Form validations
		
			if(detailBOMForm.getName().equals("wm_billofmaterialdetail_detail_view"))
			{
			isValidOwner(obj, obj.getStorer(), "Owner", BOMDetailFocus, "STORERKEY");
			isValidSKU(obj, obj.getItem(), "Component", BOMDetailFocus, "SKU");
			if(!(obj.getItem() == null) && !(obj.getStorer() == null))
			{
			isValidItemOwnerComb(obj, obj.getItem(), obj.getStorer(), "Component", "Owner" );
			}
			//detailFocus.setValue("STORERKEY", obj.getStorer().toUpperCase());
			//detailFocus.setValue("SKU", obj.getItem());
		
		if(isNewSubOwner){
			isValidOwner(obj, obj.getSubOwner(), "Substitute Owner", BOMDetailFocus, "SUBSTSTORERKEY");			
				if(isNewSubItem){
				isValidSKU(obj, obj.getSubItem(), "Substitute Component", BOMDetailFocus, "SUBSTSKU");
				
	
				isValidItemOwnerComb(obj, obj.getSubItem(), obj.getSubOwner(), "Substitute Component","Substitute Owner" );
				
				//detailFocus.setValue("SUBSTSTORERKEY", obj.getSubOwner().toUpperCase());
				//detailFocus.setValue("SUBSTSKU", obj.getSubItem().toUpperCase());

			}
			}
			
			
			try {
			isGreaterThanZero(obj);
			}catch(DPException e) {
				e.printStackTrace();
			}
		
		
	if (headerFocus.isTempBio()) 
	{//it is for insert header
			try {
				headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
			} catch (EpiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			detailFocus = detailForm.getFocus();
	
			
			detailQBEBioBean = (QBEBioBean)BOMDetailFocus;			
			headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
			if( (detailLottableFocus instanceof QBEBioBean))
			{
				//CompDetailQBEBioBean = (QBEBioBean) detailLottableFocus;
	
			
			}
	}
		
		
	else {//it is for update header
		headerBioBean = (BioBean)headerFocus;
		if(!(detailTab.getName().equals("Blank"))){
			detailFocus = detailBOMForm.getFocus();
			//_log.debug("LOG_SYSTEM_OUT","***detailTab = "+ detailTab.getName(),100L);
			//SlotInterface detailTabGrpSlot = detailTab.getSubSlot("tbgrp_slot");
			//_log.debug("LOG_SYSTEM_OUT","***detailTabGrpSlot = "+ detailTabGrpSlot.getName(),100L);
			//RoutingDetail = state.getRuntimeForm(detailTabGrpSlot, "tab 0");
			}
			
			if (detailFocus.isTempBio()) 
			   {
					detailQBEBioBean = (QBEBioBean)detailFocus;
					//validateDetail(detailQBEBioBean);
					headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
			   } 
			else {
					detailBioBean = (BioBean)detailFocus;
				
				}
		}
		 	
		
		
		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uow.clearState();
		result.setFocus(headerBioBean);
		}	
		
			}	
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Exiting BOMSave",100L);
			return RET_CONTINUE;
	}





	private int checkNullLottables(RuntimeFormInterface form, DataBean detailFocus) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In checkNullLottables",100L);
		String widgetName= "";
		QBEBioBean qbeBean = null;
		BioBean bioBean = null;
		RuntimeFormWidgetInterface widget= null;

		
	    for (int i=1; i<=10; i++)
	    {
	    	if(i == 10)
	    	{
		    	widgetName = "LOTTABLE" + Integer.toString(i);
	    	}
	    	else
	    	{
		    	widgetName = "LOTTABLE0" + Integer.toString(i);
	    	}
	    	
	    	widget= form.getFormWidgetByName(widgetName);
	    
	    	if(!widgetName.equals("LOTTABLE04") && !widgetName.equals("LOTTABLE05"))
	    	{
			if(detailFocus.isTempBio())
			   {
				 qbeBean = (QBEBioBean)detailFocus;
					if(widget.getDisplayValue()==null)
					{
						qbeBean.set(widgetName, " ");
					}
				}
			else
				{					
					bioBean = (BioBean)detailFocus;	
					if(widget.getDisplayValue()==null)
					{
						bioBean.set(widgetName, " ");
					}	
				}
	    	}
	    		 
	    }
	    _log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving checkNullLottables",100L);
		return RET_CONTINUE;
	}





	private SlotInterface getTabSlot(StateInterface state, RuntimeFormInterface detailForm) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In getTabSlot",100L);
		DataBean focus = null;
		RuntimeFormInterface form = null;
		SlotInterface tabSlot = null;
		try
		{
			if (detailForm.getName().equalsIgnoreCase("wm_billofmaterialdetail_toggle_slot")){
				SlotInterface RoutingDetailSlot = detailForm.getSubSlot("wm_billofmaterialdetail_toggle_slot");
				RuntimeFormInterface tabgrpForm = state.getRuntimeForm(RoutingDetailSlot, "Detail");
				if(!tabgrpForm.getName().equals("Blank"))
				{
				SlotInterface tabGrpSlot = tabgrpForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface detailLottForm = state.getRuntimeForm(tabGrpSlot, "tab 1");
				RuntimeFormInterface detailBOMForm = state.getRuntimeForm(tabGrpSlot, "tab 0");
				//SlotInterface LottDetailToggleSlot = BOMdetailForm.getSubSlot("wm_billofmaterialdetail_toggle_slot");
				//_log.debug("LOG_SYSTEM_OUT","***getTabSlot LottDetailToggleSlot = " + LottDetailToggleSlot.getName(),100L);
				//RuntimeFormInterface LottDetailForm = state.getRuntimeForm(LottDetailToggleSlot, "Detail");
				//_log.debug("LOG_SYSTEM_OUT","***getTabSlot LottDetailFormName = " + LottDetailForm.getName(),100L);

				
				//DataBean LottDetailDetailFocus = LottDetailForm.getFocus();
				//focus= LottDetailDetailFocus;
				form = detailLottForm;
				tabSlot= tabGrpSlot;
				
				}
				else
				{
					tabSlot= RoutingDetailSlot;
				}
			
				}
			else if(detailForm.getName().equalsIgnoreCase("wms_tbgrp_shell"))
			{
				
				SlotInterface tabGrpSlot = detailForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface LottDetailForm = state.getRuntimeForm(tabGrpSlot, "tab 1");
				RuntimeFormInterface detailBOMForm = state.getRuntimeForm(tabGrpSlot, "tab 0");

				
				DataBean DetailDetailFocus =LottDetailForm.getFocus();
				focus= DetailDetailFocus;
				form = LottDetailForm;
				tabSlot = tabGrpSlot;
				}
		} catch (NullPointerException e)
		{
			//e.printStackTrace();
			return null;
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving getTabSlot",100L);		
		return tabSlot;
	}

	private SlotInterface getTabGroupSlot(StateInterface state) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In getTabGroupSlot",100L);
		//Common 
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving getTabGroupSlot",100L);
		return tabGroupSlot;
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
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidOwner",100L);
	}
	private void isGreaterThanZero(BillOfMaterialDataObject data)throws DPException, UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isGreaterThankZero",100L);
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
				
				
				if (qty.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*"))
				{
				//_log.debug("LOG_SYSTEM_OUT","!@@ Value passed by regex",100L);
				}
				else
				{
				//_log.debug("LOG_SYSTEM_OUT","!@@ Throwing Exception by regex",100L);
				throw new DPException("System_Error");
				}
			}
			if (value < 0.0)
			{
				String [] param = new String[1];
				param[0] = data.getreqQty();
				throw new UserException("WMEXP_NEG_QTY", param);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isGreaterThankZero",100L);
	}
	private void isValidItemOwnerComb(BillOfMaterialDataObject data, String Item, String Owner, String itemLabel, String ownerLabel)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_BOM","In isValidLocation",100L);
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
		_log.debug("LOG_DEBUG_EXTENSION_BOM","Leaving isValidLocation",100L);
	}
	
}




