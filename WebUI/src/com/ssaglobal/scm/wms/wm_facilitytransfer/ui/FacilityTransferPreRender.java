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
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class FacilityTransferPreRender extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityTransferPreRender.class);
	public FacilityTransferPreRender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		
		String widgetName = context.getActionObject().getName();
		RuntimeFormWidgetInterface asnReceiptykey = form.getFormWidgetByName("RECEIPTKEY");
		RuntimeFormWidgetInterface owner = form.getFormWidgetByName("STORERKEY");
		RuntimeFormWidgetInterface type = form.getFormWidgetByName("TYPE");
		RuntimeFormWidgetInterface receiptdate = form.getFormWidgetByName("RECEIPTDATE");
		
		try {
	           // Add your code here to process the event
			if (widgetName.equals("NEW"))
			{
				asnReceiptykey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				owner.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				receiptdate.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
			else{
				asnReceiptykey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				owner.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				receiptdate.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				
				String status = form.getFormWidgetByName("STATUS").getValue().toString();
				String rectype= form.getFormWidgetByName("TYPE").getValue().toString();
				if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
					disableFormWidgets(form);
					
				}
				else{
//					form.setBooleanProperty(RuntimeFormBasicInterface.PROP_READONLY, false);
				}
				if (status.equalsIgnoreCase("0")){
					if (rectype.equalsIgnoreCase("4")||rectype.equalsIgnoreCase("5")){
						type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					}
					else{
						type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
				}
				else {
					type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (rectype.equalsIgnoreCase("3")){
					type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				}
			
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;
		
	}
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws UserException {
		StateInterface state = context.getState();
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface currentForm = (RuntimeFormInterface) form;
		ArrayList parms1 = new ArrayList(); 
		ArrayList parms2 = new ArrayList();
		ArrayList parms3 = new ArrayList();
		ArrayList parms4 = new ArrayList();
		ArrayList parms5 = new ArrayList();
		parms1.add("Tab1");
		parms2.add("Tab2");
		parms3.add("tab3");
		parms4.add("tab4");
		parms5.add("tab5");
		RuntimeFormInterface ftHeader = FormUtil.findForm(currentForm,"wm_list_shell_facilitytransfer","wm_facilitytransfer_so_loading_view",parms1,context.getState());
		_log.debug("LOG_SYSTEM_OUT","ftHeader = "+ ftHeader,100L);
		RuntimeFormInterface ftMisc = FormUtil.findForm(currentForm,"wm_list_shell_facilitytransfer","wm_facilitytransfer_so_misc_view",parms2,context.getState());
		_log.debug("LOG_SYSTEM_OUT","ft Misc = "+ ftMisc,100L);
		RuntimeFormInterface ftPickdetail = FormUtil.findForm(currentForm,"wm_list_shell_facilitytransfer","wm_facilitytransfer_pickdetail_toggle_view",parms3,context.getState());
		_log.debug("LOG_SYSTEM_OUT","ft PickDetail= "+ ftPickdetail,100L);
		RuntimeFormInterface ftTaskDetail = FormUtil.findForm(currentForm,"wm_list_shell_facilitytransfer","wm_task_details_list_view",parms4,context.getState());
		_log.debug("LOG_SYSTEM_OUT","ft TaskDetail = "+ ftTaskDetail,100L);
		RuntimeFormInterface ftDemandAllocation = FormUtil.findForm(currentForm,"wm_list_shell_facilitytransfer","wm_shipmentorder_demandallocation_view",parms5,context.getState());
		_log.debug("LOG_SYSTEM_OUT","ftDemandAllocation = "+ ftDemandAllocation,100L);

		
		try {
	           // Add your code here to process the event
			if (widgetName.equals("NEW"))
			{
				}
			else{
				String status = form.getFormWidgetByName("STATUS").getValue().toString();

				if (status.equals("95")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(ftHeader);
					disableFormWidgets(ftMisc);
					disableFormWidgets(ftPickdetail);
					disableFormWidgets(ftTaskDetail);
					disableFormWidgets(ftDemandAllocation);
				}
			}

			
	        } catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	     } 	
		return RET_CONTINUE;
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
