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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

public class FacilityTransferPreSave extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityTransferPreSave.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		ArrayList parms = new ArrayList();
		parms.add("Tab0");
		RuntimeFormInterface headerForm =null;
		DataBean headerFocus = null;

		RuntimeFormInterface detailForm =null;
		DataBean detailFocus = null;
		
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		

		RuntimeFormInterface toolbar = context.getState().getCurrentRuntimeForm();				//get the toolbar
		headerForm = FormUtil.findForm(toolbar,"wm_list_shell_facilitytransfer","wm_facilitytransfer_so_header_view",parms,context.getState());
		if (headerFocus instanceof QBEBioBean){
			ArrayList parms1 = new ArrayList();
			parms1.add("Tab0");
			detailForm = FormUtil.findForm(toolbar,"wm_list_shell_facilitytransfer","wm_facilitytransfer_ftdetail1_view",parms1,context.getState());
			OrderkeyDuplicationCheck(headerFocus.getValue("ORDERKEY").toString());			
		}else{
			ArrayList parms1 = new ArrayList();
			parms1.add("Tab0");
			parms1.add("wm_facilitytransfer_orderdetail_detail_view");
			detailForm = FormUtil.findForm(toolbar,"wm_list_shell_facilitytransfer","wm_facilitytransfer_ftdetail1_view",parms1,context.getState());


			RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot("slot1"), null);
			SlotInterface headerTabSlot = header.getSubSlot("tbgrp_slot");
			RuntimeFormInterface pickDetail = state.getRuntimeForm(headerTabSlot, "tab3"); 
			SlotInterface pdToggleSlot = pickDetail.getSubSlot("wm_shipmentorder_pickdetail_toggle_view");
			int pdFormNumber = state.getSelectedFormNumber(pdToggleSlot);
			if(pdFormNumber==0)
			{
				//Pick Detail List Validation
				RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(pdToggleSlot, "wm_shipmentorderdetail_list_view");
				if(!isNull(listForm))
				{
					// Loop through the rows in the list
					BioCollectionBean listFormCollection = (BioCollectionBean) listForm.getFocus();
					for(int i = 0; i < listFormCollection.size(); i++)
					{
						BioBean pickDetailBean = (BioBean) listFormCollection.elementAt(i);
						
						// If status has been changed
						if(pickDetailBean.hasBeenUpdated("STATUS"))
						{
							// get before and after status
							int status = Integer.parseInt(pickDetailBean.get("STATUS").toString());
							int previousStatus = 0;

							//11/19/2010 FW: Added code to trim dropid (Incident4143977_Defect291835) -- Start
							String previousDropid = null;
							
							String query = "SELECT STATUS, Dropid FROM PICKDETAIL WHERE PICKDETAILKEY = '"+pickDetailBean.get("PICKDETAILKEY").toString()+"'";
							EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
							if(results.getRowCount() > 0){
								previousStatus = Integer.parseInt(results.getAttribValue(1).getAsString());
								previousDropid = results.getAttribValue(1).getAsString();
							}
							//11/19/2010 FW: Added code to trim dropid (Incident4143977_Defect291835) -- End
							
							Object tempToLocValue = pickDetailBean.get("TOLOC");
							
							if (5 <= status && status < 9 && previousStatus < 5)
							{
								// Are we setup to do moves when picked?
								String query2 = "SELECT CONFIGKEY, NSQLVALUE FROM NSQLCONFIG WHERE CONFIGKEY = 'DOMOVEWHENPICKED' AND NSQLVALUE = '0'";
								EXEDataObject results2 = WmsWebuiValidationSelectImpl.select(query2);
								if ((results2.getRowCount() == 0) && !isEmpty(tempToLocValue))
								{
									// Update the location
									String toLocValue = tempToLocValue.toString();
									pickDetailBean.set("LOC",toLocValue);
									pickDetailBean.set("TOLOC","");
								}
								
								//11/19/2010 FW: Added code to trim dropid (Incident4143977_Defect291835) -- Start
								Object tempDropid = pickDetailBean.get("Dropid");
								String dropid = tempDropid.toString();
								if (!(dropid == null) && !(dropid.equalsIgnoreCase("")) && !(dropid.equalsIgnoreCase(" ")) && !(dropid.equalsIgnoreCase(previousDropid))){
										pickDetailBean.set("Dropid",dropid.trim());
								}
								//11/19/2010 FW: Added code to trim dropid (Incident4143977_Defect291835) -- End
							}
						}	
					}
				}
			}
		}
		
		
		

		if (headerForm != null){
			headerFocus = headerForm.getFocus();			
		}
		if (detailForm != null){
			detailFocus = detailForm.getFocus();
			String packkey = detailFocus.getValue("PACKKEY").toString();
			String uom = detailFocus.getValue("UOM").toString();
			
			String qtyOpen = detailForm.getFormWidgetByName("OPENQTY").getDisplayValue();
			String qtyOrdered = detailForm.getFormWidgetByName("ORIGINALQTY").getDisplayValue();
			String qtyAllocated = detailForm.getFormWidgetByName("QTYALLOCATED").getDisplayValue();
			String qtyShipped = detailForm.getFormWidgetByName("SHIPPEDQTY").getDisplayValue();
			
			_log.debug("LOG_SYSTEM_OUT","Quantities: =" + qtyOpen+";"+ qtyOrdered +";"+qtyAllocated+";"+qtyShipped,100L);
			
			detailFocus.setValue("STORERKEY",headerForm.getFormWidgetByName("STORERKEY").getDisplayValue());
			
			detailFocus.setValue("OPENQTY",UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA,qtyOpen, packkey, context.getState(),UOMMappingUtil.uowNull,true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			detailFocus.setValue("ORIGINALQTY",UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA,qtyOrdered, packkey, context.getState(),UOMMappingUtil.uowNull,true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			detailFocus.setValue("QTYALLOCATED",UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA,qtyAllocated, packkey, context.getState(),UOMMappingUtil.uowNull,true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			detailFocus.setValue("SHIPPEDQTY",UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA,qtyShipped, packkey, context.getState(),UOMMappingUtil.uowNull,true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

		}
		
		
		
		
		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	
	//Non null verification
	private boolean isNull(Object attributeValue) throws EpiDataException{
		if(attributeValue == null){
			return true;
		}else{
			return false;
		}
	}
	
	void OrderkeyDuplicationCheck(String OrderKey) throws DPException, UserException
	{

		String query = "SELECT * FROM ORDERS WHERE (ORDERKEY = '" + OrderKey + "')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() >= 1)
		{
			String[] parameters = new String[1];
			parameters[0] = OrderKey;
			throw new UserException("WMEXP_DUP_ORDERKEY", parameters);
		}
	}
	

}
