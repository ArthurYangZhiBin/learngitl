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
package com.ssaglobal.scm.wms.wm_po.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

public class savePOAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		try
		{
			StateInterface state = context.getState();
			//Get Focii
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormInterface poFrom = getpoForm(state);
			DataBean poFocus = poFrom.getFocus();	

			RuntimeFormInterface poDetailFrom = getpoDetailForm(state);
			RuntimeFormWidgetInterface orderQtyWidget = poDetailFrom.getFormWidgetByName("ORDEREDQTY");

			DataBean poDetailFocus = poDetailFrom.getFocus();	
			

			BioBean poBioBean = null;
			QBEBioBean poDetailQBEBioBean = null;
			BioBean poDetailBioBean = null;
			String poStatus = "";
			String pokey = "";
			String orderedQty="";
			String receivedQty="";
			String packkey = "";
			String uom = "";
			double dordqty;
			String[] params = new String[1];
			if (poFocus.isTempBio())
			{
				//Inserting New Parent
//				_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting New Parent",100L);
				poBioBean = uow.getNewBio((QBEBioBean) poFocus);
				POkeyDuplicationCheck(poBioBean.getValue("POKEY").toString());
				poDetailQBEBioBean = (QBEBioBean) poDetailFocus;
				poDetailQBEBioBean.set("STORERKEY",poBioBean.getValue("STORERKEY"));
				poBioBean.addBioCollectionLink("PODETAIL", (QBEBioBean) poDetailQBEBioBean);
				poBioBean.set("POID", GUIDFactory.getGUIDStatic());
				poDetailQBEBioBean.set("PODETAILID", GUIDFactory.getGUIDStatic());
				packkey = poDetailQBEBioBean.getValue("PACKKEY").toString();
				uom = poDetailQBEBioBean.getValue("UOM").toString();

				orderedQty = orderQtyWidget.getDisplayValue();
				receivedQty = poDetailFrom.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();
				if (orderedQty == null){
					params[0] = colonStrip(readLabel(orderQtyWidget));
					throw new UserException("WMEXP_GREATER_THAN_ZERO", params);
				}
				else if(!NumericValidationCCF.isNumber(orderedQty)){
					throw new UserException("WMEXP_ORDQTY_NAN", params);
				}
				dordqty = NumericValidationCCF.parseNumber(orderedQty);
				if (dordqty <= 0){
					params[0] = colonStrip(readLabel(orderQtyWidget));
		 	   		throw new UserException("WMEXP_GREATER_THAN_ZERO", params);
				}
				poDetailQBEBioBean.set("QTYORDERED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA,orderedQty, packkey, state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				poDetailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA,receivedQty, packkey, state, UOMMappingUtil.uowNull, true));	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

			}
			else
			{
				//Updating Parent
				poBioBean = (BioBean)poFocus;
				if(!(poDetailFrom.getName().equals("Blank"))){
					orderedQty = poDetailFrom.getFormWidgetByName("ORDEREDQTY").getDisplayValue();
					receivedQty = poDetailFrom.getFormWidgetByName("RECEIVEDQTY").getDisplayValue();
					if (orderedQty == null){
						params[0] = colonStrip(readLabel(orderQtyWidget));
			 	   		throw new UserException("WMEXP_GREATER_THAN_ZERO", params);
					}
					else if(!NumericValidationCCF.isNumber(orderedQty)){
						throw new UserException("WMEXP_ORDQTY_NAN", params);
					}
					dordqty = NumericValidationCCF.parseNumber(orderedQty);
					if (dordqty <= 0){
						params[0] = colonStrip(readLabel(orderQtyWidget));
						UserException UsrExcp = new UserException("WMEXP_GREATER_THAN_ZERO", params);
			 	   		throw UsrExcp;
					}
					if (poDetailFocus.isTempBio()){

						poDetailQBEBioBean = (QBEBioBean) poDetailFocus;
						poDetailQBEBioBean.set("STORERKEY",poBioBean.getValue("STORERKEY"));
						poBioBean.addBioCollectionLink("PODETAIL", (QBEBioBean) poDetailQBEBioBean);
						packkey = poDetailQBEBioBean.getValue("PACKKEY").toString();
						uom = poDetailQBEBioBean.getValue("UOM").toString();
						poDetailQBEBioBean.set("QTYORDERED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, orderedQty, packkey, state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
						poDetailQBEBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA,receivedQty, packkey, state, UOMMappingUtil.uowNull, true)); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
						poDetailQBEBioBean.set("PODETAILID", GUIDFactory.getGUIDStatic());

					}
					else{
						//updating Detail\
						poDetailBioBean = (BioBean)poDetailFocus;
//						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating PO Detail",100L);
						packkey = poDetailBioBean.getValue("PACKKEY").toString();
						uom = poDetailBioBean.getValue("UOM").toString();

						poDetailBioBean.set("QTYORDERED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, orderedQty, packkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
						poDetailBioBean.set("QTYRECEIVED",UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, receivedQty, packkey, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530


					}
	
				}
			}
			poStatus = poBioBean.getValue("STATUS").toString();
			pokey = poBioBean.getValue("POKEY").toString();
			String[] ErrorParem = new String[1];
 	   		ErrorParem[0]= pokey;
			if (poStatus.equalsIgnoreCase("11")){
	 	   		UserException UsrExcp = new UserException("WMEXP_ASNPOCLOSE", ErrorParem);
	 	   		throw UsrExcp;
			}
			if (poStatus.equalsIgnoreCase("15")){
	 	   		UserException UsrExcp = new UserException("WMEXP_ASNPOVERIFIEDCLOSE", ErrorParem);
	 	   		throw UsrExcp;
			}
			if (poStatus.equalsIgnoreCase("20")){
	 	   		UserException UsrExcp = new UserException("WMWXP_ASNPOCANCEL", ErrorParem);
	 	   		throw UsrExcp;
			}
			uow.saveUOW(true);
			uow.clearState();
		    result.setFocus(poBioBean);

		}
		catch (RuntimeException e)
		{
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private RuntimeFormInterface getpoForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
//		DataBean parentFocus = parentForm.getFocus();
		return parentForm;
	}

	private RuntimeFormInterface getpoDetailForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		RuntimeFormInterface parentForm= null;
		if (detailForm.getName().equalsIgnoreCase("wm_podetail_toggle_view")){
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_po_toggle");		
//			_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
			parentForm = state.getRuntimeForm(toggleSlot, "wm_podetail_detail_view");
//			_log.debug("LOG_SYSTEM_OUT","detailTab = "+ parentForm.getName(),100L);
			if(!(parentForm.getName().equals("Blank"))){
				SlotInterface tabGroupSlot = parentForm.getSubSlot("tbgrp_slot");
//				_log.debug("LOG_SYSTEM_OUT","tabGroupSlot = "+ tabGroupSlot.getName(),100L);
				parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
//				_log.debug("LOG_SYSTEM_OUT","parentForm = "+ parentForm.getName(),100L);
			}


		}else{
			SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
			parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
		}
//		DataBean parentFocus = parentForm.getFocus();
		return parentForm;
	}
	
	
	void POkeyDuplicationCheck(String POKey) throws DPException, UserException
	{

		String query = "SELECT * FROM PO WHERE (POKEY = '" + POKey + "')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() >= 1)
		{
			String[] parameters = new String[1];
			parameters[0] = POKey;
			throw new UserException("WMEXP_DUP_POKEY", parameters);
		}
	}
	
	public String colonStrip(String label){
		return 	label.substring(0, label.length()-1);
	}
	
	public String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}
