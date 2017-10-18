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

//3rd Party Classes
import java.util.ArrayList;
import java.util.Iterator;

//Epiphany Classes
import com.epiphany.shr.ui.action.UIRenderContext;
//import com.epiphany.shr.ui.exceptions.FieldException;
//import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
//import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
//import com.epiphany.shr.data.bio.Query;
//import com.epiphany.shr.metadata.interfaces.SlotInterface;
//import com.epiphany.shr.ui.model.data.BioBean;
//import com.epiphany.shr.ui.model.data.BioCollectionBean;
//import com.epiphany.shr.ui.model.data.DataBean;
//import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
//import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
//import com.epiphany.shr.util.exceptions.UserException;
//import com.epiphany.shr.ui.view.*;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ASNReceiptPreRender extends FormExtensionBase{
	
	public ASNReceiptPreRender(){
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
//		StateInterface state = context.getState();
		String widgetName = context.getActionObject().getName();
		RuntimeFormInterface currentForm = (RuntimeFormInterface) form;
//		RuntimeFormInterface tabGroupShellForm = (form.getParentForm(state));
//		_log.debug("LOG_SYSTEM_OUT","tabGroupShellForm = "+ tabGroupShellForm.getName(),100L);
		RuntimeFormWidgetInterface asnReceiptykey = form.getFormWidgetByName("RECEIPTKEY");
		RuntimeFormWidgetInterface owner = form.getFormWidgetByName("STORERKEY");
//		RuntimeFormWidgetInterface owner_lookup = form.getFormWidgetByName("owner_lookup");
		RuntimeFormWidgetInterface type = form.getFormWidgetByName("TYPE");
		RuntimeFormWidgetInterface receiptdate = form.getFormWidgetByName("RECEIPTDATE");
//		SlotInterface tabGroupSlot =null;

//		_log.debug("LOG_SYSTEM_OUT","tabGrpSlot = "+ tabGroupSlot,100L);
		ArrayList parms1 = new ArrayList(); 
		ArrayList parms2 = new ArrayList(); 
		ArrayList parms3 = new ArrayList(); 
		ArrayList parms4 = new ArrayList(); 
		parms1.add("tab 1");
		parms2.add("tab 3");
		parms3.add("tab 4");
		parms4.add("tab 2");
		RuntimeFormInterface ASNGeneralTab = FormUtil.findForm(currentForm,"wms_list_shell","receipt_general_view",parms1,context.getState());
//		_log.debug("LOG_SYSTEM_OUT","GENERAL TAB = "+ ASNGeneralTab,100L);
		RuntimeFormInterface ASNCarrierTab = FormUtil.findForm(currentForm,"wms_list_shell","receipt_carrier_tab_view",parms2,context.getState());
//		_log.debug("LOG_SYSTEM_OUT","CARRIER TAB = "+ ASNCarrierTab,100L);
		RuntimeFormInterface ASNLoadingTab = FormUtil.findForm(currentForm,"wms_list_shell","receipt_loading_view",parms3,context.getState());
		RuntimeFormInterface ASNSupplierTab = FormUtil.findForm(currentForm,"wms_list_shell","wm_asnreceipt_ship_from_view",parms4,context.getState());
//		_log.debug("LOG_SYSTEM_OUT","LOADING TAB = "+ ASNLoadingTab,100L);
		RuntimeFormWidgetInterface trackInventoryBy = ASNGeneralTab.getFormWidgetByName("TRACKINVENTORYBY");
		String rectype="";

		try {
	           // Add your code here to process the event
			if (widgetName.equals("NEW"))
			{
				asnReceiptykey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				owner.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//				owner_lookup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				receiptdate.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
				}
			else{
				String status = form.getFormWidgetByName("STATUS").getValue().toString();
				rectype= form.getFormWidgetByName("TYPE").getValue().toString();
				asnReceiptykey.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				owner.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//				owner_lookup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				receiptdate.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//				ValidateOwner(context, owner.getDisplayValue().toString(),rectype,AllowAutoReceipt, trackInventoryBy);
				if (status.equalsIgnoreCase("0")){
					if (rectype.equalsIgnoreCase("4")||rectype.equalsIgnoreCase("5")){
						
						type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						trackInventoryBy.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					}
					else{
						type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						trackInventoryBy.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
				}
				else {
					type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					trackInventoryBy.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (rectype.equalsIgnoreCase("3")){
					type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}
				if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
					disableFormWidgets((RuntimeFormInterface)form);
					disableFormWidgets(ASNGeneralTab);
					disableFormWidgets(ASNSupplierTab);
					disableFormWidgets(ASNCarrierTab);
//					disableFormWidgets(CarrierInfoForm);
//					disableFormWidgets(TrailerInfoForm);
					disableFormWidgets(ASNLoadingTab);
//					disableFormWidgets(ContainerInfoForm);
				}
				type.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
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
//		_log.debug("LOG_SYSTEM_OUT","!@# Disabling Form Widgets",100L);
		for (Iterator it = form.getFormWidgets(); it.hasNext();)
		{
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();
//			_log.debug("LOG_SYSTEM_OUT","Widget Type = " + widget.getType().toString(),100L);
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
		}
	}
//	private void ValidateOwner(UIRenderContext context, String Owner, String rectype, RuntimeFormWidgetInterface AllowAutoReceipt,RuntimeFormWidgetInterface trackInventoryBy )throws EpiException {
//		int size = 0;
//		BioCollectionBean listCollection = null;
//		try {
//    		String sQueryString = "(wm_storer.STORERKEY = '" + Owner + "' AND  wm_storer.TYPE = '1')";
//    	   _log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
//    	   Query BioQuery = new Query("wm_storer",sQueryString,null);
//    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
//           listCollection = uow.getBioCollectionBean(BioQuery);
//           size = listCollection.size();
//		} catch(EpiException e) {
//        
//        // Handle Exceptions 
//			e.printStackTrace();
//	    
//		} 
//		if (size == 0) {
//			RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
//			String WidgetName = context.getSourceWidget().getName();
//			String[] ErrorParem = new String[2];
// 	   		ErrorParem[0]= Owner;
// 	   		ErrorParem[1] = "Owner";
// 	   		FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
// 	   		throw UsrExcp;
//		}
//		else
//		{
//			if (rectype.equalsIgnoreCase("4") || rectype.equalsIgnoreCase("5")){
//				if (listCollection.get("0").get("ALLOWSINGLESCANRECEIVING").equals("1")){
//					AllowAutoReceipt.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//				}
//				else{
//					AllowAutoReceipt.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//				}
//				trackInventoryBy.setDisplayValue(listCollection.get("0").get("TRACKINVENTORYBY").toString());
//			}
//			else{
//				AllowAutoReceipt.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//			}
//		}
//		
//	}
}
