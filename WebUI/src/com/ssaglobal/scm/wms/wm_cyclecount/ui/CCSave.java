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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentHelper;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CCSave extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCSave.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws UserException 
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		
		_log.debug("LOG_DEBUG_EXTENSION", "it is in HeaderDetailSave******DDDDkkkkkkkkkkkvvvvvv", SuggestedCategory.NONE);
		//Get user entered criteria
		//HC.b
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_cyclecount_detail_toggle";
		String detailBiocollection = "CCDETAIL";
		String detailFormTab = "wm_cyclecount_detail_toggle_tab";
		_log.debug("LOG_DEBUG_EXTENSION", "********shellSlot1=" + shellSlot1, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "********shellSlot2=" + shellSlot2, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "********toggleFormSlot=" + toggleFormSlot, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "********detailBiocollection=" + detailBiocollection, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "********detailFormTab=" + detailFormTab, SuggestedCategory.NONE);
		//HC.e
		//	try{
		StateInterface state = context.getState();
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_list_shell_cyclecount Toolbar", state);
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		//get header data
		//	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1); //HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if (headerForm.isListForm())
		{
			String[] desc = new String[1];
			desc[0] = "";
			throw new UserException("List_Save_Error", desc);
		}
		DataBean headerFocus = headerForm.getFocus();
		
		//get detail data
		//		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2); //HC
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		
		BioBean headerBioBean = null;
		if (headerFocus.isTempBio())
		{//it is for insert header
			_log.debug("LOG_DEBUG_EXTENSION", "inserting header ******", SuggestedCategory.NONE);
			try
			{
				headerBioBean = uow.getNewBio((QBEBioBean) headerFocus);
			} catch (EpiException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_CCSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			
			DataBean detailFocus = detailForm.getFocus();
			//			headerBioBean.addBioCollectionLink("LOADSCHEDULEDETAIL", (QBEBioBean)detailFocus);
			if (detailFocus != null){
				
			
				//jp SN Begin
				_log.debug("LOG_SYSTEM_OUT","[CCSave]detailForm:"+detailForm.getName(),100L);
				linkSerials(detailForm, state, detailFocus);
				//jp SN End
				
				headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus); //HC
			}
		}
		else
		{//it is for update header
			_log.debug("LOG_DEBUG_EXTENSION", "updating header ******", SuggestedCategory.NONE);
			headerBioBean = (BioBean) headerFocus;
			//		    SlotInterface toggleSlot = detailForm.getSubSlot("wm_load_schedule_detail_toggle");
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot); //HC
			//		    RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_load_shcedule_detail_toggle_tab");
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, detailFormTab); //HC
			
			
			//jp SN Begin
			_log.debug("LOG_SYSTEM_OUT","[CCSave]:"+detailTab.getName()+"\n\n\n",100L);
			DataBean detailFocus=null;
			
			if(detailTab.getName().equalsIgnoreCase("wm_cyclecount_detail_view")){
				detailForm = detailTab;
				detailFocus = detailTab.getFocus();
			}else if(detailTab.getName().equalsIgnoreCase("wms_tbgrp_shell")){
				for(Iterator itr = detailTab.getSubSlotsIterator(); itr.hasNext();){
					SlotInterface slot =(SlotInterface)itr.next();
					RuntimeFormInterface  form = state.getRuntimeForm(slot, null);
					
					_log.debug("LOG_SYSTEM_OUT","slot:"+slot.getName()+"\t\tform:"+form.getName(),100L);
					
					if(form.getName().equalsIgnoreCase("wm_cyclecount_detail_view")){
						detailForm = form;
						detailFocus = form.getFocus();
						break;
					}
					
					
					
				}

			}
			//DataBean detailFocus = detailTab.getFocus();
			//jp SN End
			
			//if (detailFocus != null && detailFocus.isTempBio())
			if (detailFocus != null )
			{
				
				//jp SN Begin
				_log.debug("LOG_SYSTEM_OUT","[CCSave]detailForm:"+detailForm.getName(),100L);
				_log.debug("LOG_SYSTEM_OUT","[CCSave]detailFocus bean type:"+detailFocus.getBeanType(),100L);
				linkSerials(detailForm, state, detailFocus);
				//jp SN End
				
				if( detailFocus.isTempBio()){
					//jp Test begin
					
					_log.debug("LOG_SYSTEM_OUT","[CCSave]ccdetailkey:"+((QBEBioBean)detailFocus).get("CCDETAILKEY").toString(),100L);
					
					//jp Test end
					_log.debug("LOG_DEBUG_EXTENSION", "*****detaiFocus is tempbio=" + detailFocus.isTempBio(), SuggestedCategory.NONE);
					_log.debug("LOG_DEBUG_EXTENSION", "inserting detail ******", SuggestedCategory.NONE);
					//			    headerBioBean.addBioCollectionLink("LOADSCHEDULEDETAIL", (QBEBioBean)detailFocus);
				
					headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus); //HC

					
					
					
					try {
						BioCollection collection = headerBioBean.getBioCollection(detailBiocollection);
						_log.debug("LOG_SYSTEM_OUT","BIOCOLLECTION SIZE:"+collection.size(),100L);
						
						
						for(int idx =0; idx < collection.size(); idx++){
							Bio bio = collection.elementAt(idx);
							_log.debug("LOG_SYSTEM_OUT","BIO:"+idx+" type:"+bio.getTypeName(),100L);
						}
						
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "updating detail ******", SuggestedCategory.NONE);
			}
			
		}
		try
		{
			uow.saveUOW(true);
		} catch (UnitOfWorkException e)
		{
			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			
			if (nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "WMEXP_SAVE_FAILED", null);
			}
			
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			_log.error("LOG_ERROR_EXTENSION_CCSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_CCSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_CCSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_CCSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_CCSave", e.toString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		uow.clearState();
		result.setFocus(headerBioBean);
		
		return RET_CONTINUE;
	}

	private void linkSerials(RuntimeFormInterface detailForm, StateInterface state, DataBean detailFocus) {
		
		
		if(detailFocus.isTempBio())
			linkSerials(detailForm, state, (QBEBioBean) detailFocus);
		else
			linkSerials(detailForm, state, (BioBean) detailFocus);
	}
	private void linkSerials(RuntimeFormInterface detailForm, StateInterface state, QBEBioBean detailFocus) {
		
		QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		
		
		SlotInterface serialSlot = detailForm.getSubSlot(CCHelper.SLOT_SERIALINVENTORYLIST);
		RuntimeFormInterface slotForm = state.getRuntimeForm(serialSlot, null);
		DataBean serialFocus = slotForm.getFocus();
		
		try{
			//Just process if serial numbers present to process
			if(serialFocus!=null){
				
				BioCollectionBean serialTmpBioList = (BioCollectionBean)serialFocus;
				
				//addValidSerialsToCollection(serialTmpBioList, detailBioBean);
				//for each serial number tmp check if exists in serial inventory 
				for(int idx = 0; idx < serialTmpBioList.size(); idx++){
					BioBean serialTmpBio = (BioBean)serialTmpBioList.elementAt(idx);
					
					Object qtyObj = serialTmpBio.get("qty");
					Double qty = 0.00;
					
					if(qtyObj instanceof Double)
						qty = (Double)qtyObj;
					else if(qtyObj instanceof BigDecimal)
						qty = ((BigDecimal)qtyObj).doubleValue();
					
					if(qty!=null && qty.doubleValue() != 0.00){
						QBEBioBean newSerial;
						
						newSerial = populateSerial(uowb, serialTmpBio);
						detailBioBean.addBioCollectionLink("SERIALDETAILS", newSerial);
					}
				}//end for
			}
		} catch (DataBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		private void linkSerials(RuntimeFormInterface detailForm, StateInterface state, BioBean detailFocus) {
			
			BioBean detailBioBean = (BioBean)detailFocus;
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			
			_log.debug("LOG_SYSTEM_OUT","linkSerials:detailForm:"+detailForm.getName(),100L);
			
			SlotInterface serialSlot = detailForm.getSubSlot(CCHelper.SLOT_SERIALINVENTORYLIST);
			
			//test Begin
			if (serialSlot==null)
				_log.debug("LOG_SYSTEM_OUT","linkSerials:detailForm:slot is null",100L);
			//test End
			
			RuntimeFormInterface slotForm = state.getRuntimeForm(serialSlot, null);
			DataBean serialFocus = slotForm.getFocus();

			try{
				//Just process if serial numbers present to process
				if(serialFocus!=null){
					
					BioCollectionBean serialTmpBioList = (BioCollectionBean)serialFocus;
					
					//addValidSerialsToCollection(serialTmpBioList, detailBioBean);
					//for each serial number tmp check if exists in serial inventory 
					for(int idx = 0; idx < serialTmpBioList.size(); idx++){
						BioBean serialTmpBio = (BioBean)serialTmpBioList.elementAt(idx);
						
						Object qtyObj = serialTmpBio.get("qty");
						Double qty = 0.00;
						
						if(qtyObj instanceof Double)
							qty = (Double)qtyObj;
						else if(qtyObj instanceof BigDecimal)
							qty = ((BigDecimal)qtyObj).doubleValue();
						
						if(qty!=null && qty.doubleValue() != 0.00){
							QBEBioBean newSerial;
							
							newSerial = populateSerial(uowb, serialTmpBio);
							detailBioBean.addBioCollectionLink("SERIALDETAILS", newSerial);
						}
					}//end for
				}
			} catch (DataBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}

	private QBEBioBean populateSerial(UnitOfWorkBean uowb, BioBean serialTmpBio ) throws DataBeanException{
		QBEBioBean newSerial = uowb.getQBEBioWithDefaults("wm_ccdetailserial");
		
		
		newSerial.set("cckey", serialTmpBio.get("cckey") );
		newSerial.set("ccdetailkey", serialTmpBio.get("ccdetailkey"));
		newSerial.set("qty",serialTmpBio.get("qty"));
		newSerial.set("lot",serialTmpBio.get("lot").toString().trim());
		newSerial.set("loc",serialTmpBio.get("loc"));
		newSerial.set("serialnumber",serialTmpBio.get("serialnumber"));
		newSerial.set("serialnumberlong",serialTmpBio.get("serialnumberlong"));
		newSerial.set("id", serialTmpBio.get("id"));
		newSerial.set("storerkey", serialTmpBio.get("storerkey"));
		newSerial.set("sku", serialTmpBio.get("sku"));
		
		
		newSerial.set("data2", serialTmpBio.get("data2"));
		newSerial.set("data3", serialTmpBio.get("data3"));
		newSerial.set("data4", serialTmpBio.get("data4"));
		newSerial.set("data5", serialTmpBio.get("data5"));

		
		
		
		Double netweight = (Double)serialTmpBio.get("netweight");
		
		if(netweight==null )
			newSerial.set("netweight", new Double(0.0));
		else
			newSerial.set("netweight", serialTmpBio.get("netweight"));
		
		return newSerial;

	}

}
