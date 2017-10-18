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

import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class facilityTransferDetailPreRender extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(facilityTransferDetailPreRender.class);
	public facilityTransferDetailPreRender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		_log.debug("LOG_SYSTEM_OUT","In side PreRenderForm for ASN RECEIPT Details",100L);
		String widgetName = context.getActionObject().getName();
		
		
		try {
	
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;
	}
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
     throws EpiException {

		try {
			// Add your code here to process the event
    
		} catch(Exception e) {
     
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}
	/**
	 * Modification History
	 * 04/14/2009	AW 		Machine#:2093019 SDIS:SCM-00000-05440
	 * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 						UOM conversion is now done in the front end.
	 *                      Changes were made accordingly.
	 */

	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws UserException {
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		_log.debug("LOG_SYSTEM_OUT","In side PreRenderForm for ASN RECEIPT Details",100L);
		String widgetName = context.getActionObject().getName();
		try {
	           // Add your code here to process the event
			String status = form.getFormWidgetByName("STATUS").getValue().toString();
			RuntimeFormInterface tabGroupShellForm = (form.getParentForm(context.getState()));
			SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");
			//Get all the forms
			RuntimeFormInterface ftDetail2 = context.getState().getRuntimeForm(tabGroupSlot, "Tab1");
			RuntimeFormInterface ftLottables = context.getState().getRuntimeForm(tabGroupSlot, "Tab2");
			RuntimeFormInterface ftUdf = context.getState().getRuntimeForm(tabGroupSlot, "tab3");
			RuntimeFormInterface ftVASDEtail = context.getState().getRuntimeForm(tabGroupSlot, "tab4");

			if (widgetName.equals("NEW"))
			{
			}
			else{
				BioBean orderDetail = (BioBean)form.getFocus();
				String openQqty = orderDetail.getValue("OPENQTY").toString();
				String orgQty = orderDetail.getValue("ORIGINALQTY").toString();
				String shippedQty = orderDetail.getValue("SHIPPEDQTY").toString();
				String allocatedQty = orderDetail.getValue("QTYALLOCATED").toString();
				String preAllocatedQty = orderDetail.getValue("QTYPREALLOCATED").toString();
				
				String uom = orderDetail.getValue("UOM").toString();
				String packKey = orderDetail.getValue("PACKKEY").toString();
			
			
				String screenOpenQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,openQqty, packKey,context.getState(),UOMMappingUtil.uowNull,true); 	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				String screenOrgQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,orgQty, packKey,context.getState(),UOMMappingUtil.uowNull,true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				String screenShippedQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,shippedQty, packKey,context.getState(),UOMMappingUtil.uowNull,true);	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				String screenAllocatedQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,allocatedQty, packKey,context.getState(),UOMMappingUtil.uowNull,true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				String screenPreAllocatedQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,preAllocatedQty, packKey,context.getState(),UOMMappingUtil.uowNull,true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

				
				form.getFormWidgetByName("OPENQTY").setValue(screenOpenQty);
				form.getFormWidgetByName("ORIGINALQTY").setValue(screenOrgQty);
				form.getFormWidgetByName("SHIPPEDQTY").setValue(screenShippedQty);
				form.getFormWidgetByName("QTYALLOCATED").setValue(screenAllocatedQty);
				form.getFormWidgetByName("PREALLOCATEDQTY").setValue(screenPreAllocatedQty);
				if (status.equalsIgnoreCase("95")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(ftDetail2);
					disableFormWidgets(ftLottables);
					disableFormWidgets(ftUdf);
					disableFormWidgets(ftVASDEtail);
				}

			}
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;

	}


	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}
	
	private void disableFormWidgets(RuntimeFormInterface form)
	{
		_log.debug("LOG_SYSTEM_OUT","!@# Disabling Form Widgets",100L);
		for (Iterator it = form.getFormWidgets(); it.hasNext();)
		{
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();
			_log.debug("LOG_SYSTEM_OUT","Widget Type = " + widget.getType().toString(),100L);
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}
}
