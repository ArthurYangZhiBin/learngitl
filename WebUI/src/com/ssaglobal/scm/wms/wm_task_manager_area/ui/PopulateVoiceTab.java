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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;


import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_billofmaterial.ui.BillOfMaterialDataObject;

public class PopulateVoiceTab extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateVoiceTab.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing PopulateVoiceTab",100L);	

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface subForm = state.getCurrentRuntimeForm();		
		RuntimeFormInterface tabShellForm = subForm.getParentForm(state);		

		SlotInterface tabGroupSlot= tabShellForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface detailFormSubSlot= state.getRuntimeForm(tabGroupSlot, "tab 0");
		RuntimeFormInterface detailForm= state.getRuntimeForm(detailFormSubSlot.getSlot(), null);
		RuntimeFormInterface subSlotForm= state.getRuntimeForm(detailForm.getSubSlot("wm_task_manager_area_form_slot"), null ); 


		/*	
		//get header data
	    SlotInterface headerSlot = tabShellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewSubOwner = false;
		boolean isNewSubItem = false;

		 */

		if(!subSlotForm.isListForm())
		{
			DataBean detailFocus= subSlotForm.getFocus();
			if(detailFocus.isTempBio())
			{
				detailFocus = (QBEBioBean)detailFocus;
			}
			else
			{
				detailFocus = (BioBean)detailFocus;
			}
			if (detailFocus.getValue("AREAKEY") != null){
				String keyVal= detailFocus.getValue("AREAKEY").toString().toUpperCase();
				String qry = "wm_area.AREAKEY=" +"'" +keyVal+"'";
				Query BioQuery = new Query("wm_area", qry, null);
				BioCollectionBean headerVal = uow.getBioCollectionBean(BioQuery);
				try {
					if  (headerVal.size() > 0){
						if(subForm.getFocus().isTempBio())
						{
							QBEBioBean qbeHeader= (QBEBioBean)subForm.getFocus();
							qbeHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
							qbeHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
							qbeHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
							qbeHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
							qbeHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
							qbeHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
							qbeHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
							qbeHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
							qbeHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
							qbeHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
							qbeHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
							qbeHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
	//HC.b
							qbeHeader.set("DEFAULTCARTONTYPE", headerVal.get("0").getValue("DEFAULTCARTONTYPE"));
							qbeHeader.set("DROPIDREQUIRED", headerVal.get("0").getValue("DROPIDREQUIRED"));
							qbeHeader.set("PROMPTCARTONTYPE", headerVal.get("0").getValue("PROMPTCARTONTYPE"));
	//HC.e						
						}
						else
						{
							BioBean bioHeader= (BioBean)subForm.getFocus();
							bioHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
							bioHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
							bioHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
							bioHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
							bioHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
							bioHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
							bioHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
							bioHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
							bioHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
							bioHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
							bioHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
							bioHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
	//HC.b
							bioHeader.set("DEFAULTCARTONTYPE", headerVal.get("0").getValue("DEFAULTCARTONTYPE"));
							bioHeader.set("DROPIDREQUIRED", headerVal.get("0").getValue("DROPIDREQUIRED"));
							bioHeader.set("PROMPTCARTONTYPE", headerVal.get("0").getValue("PROMPTCARTONTYPE"));
	//HC.e						
						}

					}
				} catch (EpiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting PopulateVoiceTab",100L);
		return RET_CONTINUE;
	}
	
	
	
	
	
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws UserException {
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing PopulateVoiceTab",100L);	

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface subForm = state.getCurrentRuntimeForm();		
		RuntimeFormInterface tabShellForm = subForm.getParentForm(state);		

		SlotInterface tabGroupSlot= tabShellForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface detailFormSubSlot= state.getRuntimeForm(tabGroupSlot, "tab 0");
		RuntimeFormInterface detailForm= state.getRuntimeForm(detailFormSubSlot.getSlot(), null);
		RuntimeFormInterface subSlotForm= state.getRuntimeForm(detailForm.getSubSlot("wm_task_manager_area_form_slot"), null ); 


		/*	
		//get header data
	    SlotInterface headerSlot = tabShellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewSubOwner = false;
		boolean isNewSubItem = false;

		 */

		if(!subSlotForm.isListForm())
		{
			DataBean detailFocus= subSlotForm.getFocus();
			if(detailFocus.isTempBio())
			{
				detailFocus = (QBEBioBean)detailFocus;
			}
			else
			{
				detailFocus = (BioBean)detailFocus;
			}
			if (detailFocus.getValue("AREAKEY") != null){
				String keyVal= detailFocus.getValue("AREAKEY").toString().toUpperCase();
				String qry = "wm_area.AREAKEY=" +"'" +keyVal+"'";
				Query BioQuery = new Query("wm_area", qry, null);
				BioCollectionBean headerVal = uow.getBioCollectionBean(BioQuery);
				try {
					if  (headerVal.size() > 0){
						if(subForm.getFocus().isTempBio())
						{
							QBEBioBean qbeHeader= (QBEBioBean)subForm.getFocus();
							qbeHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
							qbeHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
							qbeHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
							qbeHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
							qbeHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
							qbeHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
							qbeHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
							qbeHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
							qbeHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
							qbeHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
							qbeHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
							qbeHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
	//HC.b
							qbeHeader.set("DEFAULTCARTONTYPE", headerVal.get("0").getValue("DEFAULTCARTONTYPE"));
							qbeHeader.set("DROPIDREQUIRED", headerVal.get("0").getValue("DROPIDREQUIRED"));
							qbeHeader.set("PROMPTCARTONTYPE", headerVal.get("0").getValue("PROMPTCARTONTYPE"));
	//HC.e						
						}
						else
						{
							BioBean bioHeader= (BioBean)subForm.getFocus();
							bioHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
							bioHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
							bioHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
							bioHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
							bioHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
							bioHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
							bioHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
							bioHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
							bioHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
							bioHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
							bioHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
							bioHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
	//HC.b
							bioHeader.set("DEFAULTCARTONTYPE", headerVal.get("0").getValue("DEFAULTCARTONTYPE"));
							bioHeader.set("DROPIDREQUIRED", headerVal.get("0").getValue("DROPIDREQUIRED"));
							bioHeader.set("PROMPTCARTONTYPE", headerVal.get("0").getValue("PROMPTCARTONTYPE"));
	//HC.e						
						}

					}
				} catch (EpiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting PopulateVoiceTab",100L);
		return RET_CONTINUE;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
