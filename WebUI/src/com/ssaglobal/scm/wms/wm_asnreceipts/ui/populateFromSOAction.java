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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class populateFromSOAction extends ActionExtensionBase {

    /**
     * This method gets triggered when OK button on order line items popup is clicked.
     * @param context
     * @param result
     * @return int
     * @throws EpiException
     */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(populateFromSOAction.class);
   protected int execute(ModalActionContext context, ActionResult result) throws EpiException {

        RuntimeFormInterface sourceForm = context.getSourceForm();    //gets the toolbar

        RuntimeFormInterface tabGroupShellForm = (sourceForm.getParentForm(context.getState()));
        
        _log.debug("LOG_SYSTEM_OUT","Source Form is = "+ sourceForm.getName(),100L);
        _log.debug("LOG_SYSTEM_OUT","Source Parent Form is = "+ sourceForm.getParentForm(context.getState()).getName(),100L);
        _log.debug("LOG_SYSTEM_OUT","Source g Parent Form is = "+ tabGroupShellForm.getName(),100L);
/*        try{
//            createOrderAndOrderLineItems(context, result, tabGroupShellForm);

        }catch(Exception e){
            e.printStackTrace();
        } */
        result.setFocus(createOrderAndOrderLineItems(context, result, tabGroupShellForm));
        return super.execute(context, result);
    }


    private DataBean createOrderAndOrderLineItems( ModalActionContext context, ActionResult result, RuntimeFormInterface tabGroupShellForm) throws EpiException {
    	
        UnitOfWorkBean uob = context.getState().getDefaultUnitOfWork();
        
        //RuntimeListFormInterface SOlistform = (RuntimeListFormInterface)context.getModalBodyForm(0);
        
        RuntimeFormInterface ModalBodyForm = context.getModalBodyForm(0);
        SlotInterface ordersListSlot = ModalBodyForm.getSubSlot("wm_shipmentorder_list");
        _log.debug("LOG_SYSTEM_OUT","Order List Form = "+ ordersListSlot.getName(),100L);
        RuntimeListFormInterface SOlistform = (RuntimeListFormInterface)context.getState().getRuntimeForm(ordersListSlot, null);
		_log.debug("LOG_SYSTEM_OUT","So List Form = "+ SOlistform.getName(),100L);

        SlotInterface orderDetailListSlot = ModalBodyForm.getSubSlot("wm_shipmentorderdetail_list");
        _log.debug("LOG_SYSTEM_OUT","So DEtail Slot = "+ orderDetailListSlot.getName(),100L);
        RuntimeListFormInterface SODetailListForm = null;
        _log.debug("LOG_SYSTEM_OUT","SODEtail form = "+ context.getState().getRuntimeForm(orderDetailListSlot, null).getName(),100L);
        if (!(context.getState().getRuntimeForm(orderDetailListSlot, null).getName().equalsIgnoreCase("blank"))){
        	SODetailListForm = (RuntimeListFormInterface)context.getState().getRuntimeForm(orderDetailListSlot, null);	
        }
        BioBean objBioBeanOrder = null;
        BioBean objBioBeanRelatedProduct = null;
        
        //05/18/2010 FW: Added code to check and exit if no recorder is selected (Incedent3779016_Defect272417) �Start
        ArrayList selectedOrders2 = SOlistform.getAllSelectedItems();
        if((selectedOrders2 == null) || selectedOrders2.size() == 0) {
        	throw new UserException("WMEXP_NO_SELECTION", new Object[]{});
        }
        //05/18/2010 FW: Added code to check and exit if no recorder is selected (Incedent3779016_Defect272417) -- End

		SlotInterface headerSlot = tabGroupShellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = context.getState().getRuntimeForm(headerSlot, null);
		_log.debug("LOG_SYSTEM_OUT","Header Form = "+ headerForm.getName(),100L);
		SlotInterface tabGroupSlot = headerForm.getSubSlot("tbgrp_slot");
		_log.debug("LOG_SYSTEM_OUT","tabGroupSlot Form = "+ tabGroupSlot.getName(),100L);
		RuntimeFormInterface ReceiptForm = context.getState().getRuntimeForm(tabGroupSlot, "tab 0");
		_log.debug("LOG_SYSTEM_OUT","ReceiptForm Form = "+ ReceiptForm.getName(),100L);

		String newLineNumber = null;
		boolean firstLine = true;
		DataBean receiptDetailDataBean =  null;
		QBEBioBean receiptDetailQBEBioBean =   null;
		int MaxLineNumber = 0;
		
		SlotInterface detailSlot = tabGroupShellForm.getSubSlot("list_slot_2");		//HC
		RuntimeFormInterface detailForm = context.getState().getRuntimeForm(detailSlot, null);
		_log.debug("LOG_SYSTEM_OUT","detailForm = "+ detailForm.getName(),100L);
		
		if (ReceiptForm.getFocus() instanceof QBEBioBean){
			SlotInterface detailTabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
			_log.debug("LOG_SYSTEM_OUT","detailTabGroupSlot Form = "+ detailTabGroupSlot.getName(),100L);
			RuntimeFormInterface ReceiptDetailForm = context.getState().getRuntimeForm(detailTabGroupSlot, "tab 0");
			receiptDetailDataBean =  ReceiptDetailForm.getFocus();
			receiptDetailQBEBioBean = 	(QBEBioBean)receiptDetailDataBean;
        }

		_log.debug("LOG_SYSTEM_OUT","header Focus = "+ ReceiptForm.getFocus().toString(),100L);
		WSDefaultsUtil wsdefaults = new WSDefaultsUtil();
        //create order line items

 		if (((SODetailListForm != null )&& (SODetailListForm.getAllSelectedItems()== null))||(SODetailListForm == null )){
 			ArrayList selectedOrders = SOlistform.getAllSelectedItems();
 			if(selectedOrders != null && selectedOrders.size() > 1){
 		       		UserException UsrExcp = new UserException("WMEXP_MORE_THAN_ONE_SELECTED", new Object[]{});
 	 	 	   		throw UsrExcp;
 	    	}
 			Iterator soBeanIter = selectedOrders.iterator();
 			BioBean selBio;
 			for(; soBeanIter.hasNext();){
 				selBio = (BioBean)soBeanIter.next();
 				if (ReceiptForm.getFocus() instanceof QBEBioBean){
 					//create order bio
 					objBioBeanOrder = createOrder(context, result, ReceiptForm, selBio);
 					MaxLineNumber = 0;
 				} else {
 					_log.debug("LOG_SYSTEM_OUT","In Side Else Header Focus" ,100L);
 					objBioBeanOrder = (BioBean)ReceiptForm.getFocus();
 					MaxLineNumber =  getMaxLineNumber(objBioBeanOrder);
 				}
 			}
 	        ArrayList selectedItems = SOlistform.getAllSelectedItems();
 	  		if(selectedItems != null && selectedItems.size() > 0){
 	       		Iterator bioBeanIter = selectedItems.iterator();
 	       		BioBean SOBio;
 				for(; bioBeanIter.hasNext();){
 					SOBio = (BioBean)bioBeanIter.next();
 					BioCollectionBean SelChildBiocollection = (BioCollectionBean)SOBio.get("ORDER_DETAIL");
 			        int j = 0;
 			        for (j=0; j<SelChildBiocollection.size(); j++){
 			        	
 			        	BioBean SOLine = (BioBean)SelChildBiocollection.elementAt(j);
 	 		        	if ((firstLine) & (ReceiptForm.getFocus() instanceof QBEBioBean)){
 	 		        		objBioBeanRelatedProduct = uob.getNewBio(receiptDetailQBEBioBean);
 	 		        		firstLine = false;
 	 		        	}else{
 	 		        		objBioBeanRelatedProduct = uob.getNewBio("receiptdetail");	
 	 		        	}
// 	 					objBioBeanRelatedProduct = uob.getNewBio("receiptdetail");
 	 					newLineNumber = GenerateNewNumber(MaxLineNumber, context);
 	 					_log.debug("LOG_SYSTEM_OUT","The Max is = "+ newLineNumber,100L);
 	 					objBioBeanRelatedProduct.set("RECEIPTKEY",  objBioBeanOrder.get("RECEIPTKEY"));
 	 					objBioBeanRelatedProduct.set("RECEIPTLINENUMBER",  newLineNumber);
 	 					objBioBeanRelatedProduct.set("TYPE","2");
 	 					objBioBeanRelatedProduct.set("SKU", SOLine.get("SKU"));
 	 					objBioBeanRelatedProduct.set("PACKKEY", SOLine.get("PACKKEY"));
 	 					objBioBeanRelatedProduct.set("STORERKEY", SOLine.get("STORERKEY"));
 	 					objBioBeanRelatedProduct.set("UOM", SOLine.get("UOM"));
 	 					objBioBeanRelatedProduct.set("QTYEXPECTED", SOLine.get("SHIPPEDQTY"));
 	 					objBioBeanRelatedProduct.set("QTYRECEIVED", "0.0");
 	 					objBioBeanRelatedProduct.set("QCREQUIRED", "0");
 	 					objBioBeanRelatedProduct.set("QCAUTOADJUST", "0");
 	 					objBioBeanRelatedProduct.set("TOLOC", wsdefaults.getDefaultRecieveLoc(context.getState()));
 	 					objBioBeanRelatedProduct.set("SUSR1", "");
 	 					objBioBeanRelatedProduct.set("SUSR2", "");
 	 					objBioBeanRelatedProduct.set("SUSR3", "");
 	 					objBioBeanRelatedProduct.set("SUSR4", "");
 	 					objBioBeanRelatedProduct.set("SUSR5", "");
 	 					objBioBeanRelatedProduct.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
 	 					objBioBeanOrder.addToBioCollection("RECEIPTDETAILS", objBioBeanRelatedProduct);
 	 					//03/17/2011 FW:  Added code to populate ORDERKEY, ORDERLINENUMBER into POKEY and POLineNumber fields (Incident4297978_Defect300643) -- Start
 	 					objBioBeanRelatedProduct.set("POKEY", SOLine.get("ORDERKEY")); 
 	 					objBioBeanRelatedProduct.set("POLineNumber", SOLine.get("ORDERLINENUMBER"));
 	 					//03/17/2011 FW:  Added code to populate ORDERKEY, ORDERLINENUMBER into POKEY and POLineNumber fields (Incident4297978_Defect300643) -- End

 	 					// 2012-08-13
 	 					// Modified by Will Pu
 	 					// 自出货单填充新增逻辑
 	 					try {
 	 						updateASNStatusUpdator(context, SOLine.get("ORDERKEY").toString(), objBioBeanOrder.get("RECEIPTKEY").toString());
 	 					} catch (UserException e) {
 	 						e.printStackTrace();
 	 						throw new UserException("", new Object[0]);
 	 					}
 	 					
 	 					MaxLineNumber+=1; 			        	
 			        }
 				}
 	       	}else{
 	       		UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
 	 	   		throw UsrExcp;
 	       	}
 		}
 		else{
 			BioBean selBio = uob.getBioBean(SOlistform.getSelectedListItem());
 			if (ReceiptForm.getFocus() instanceof QBEBioBean){
 				//create order bio
 				objBioBeanOrder = createOrder(context, result, ReceiptForm, selBio);
 				MaxLineNumber = 0;
 			} else {
 				_log.debug("LOG_SYSTEM_OUT","In Side Else Header Focus" ,100L);
 				objBioBeanOrder = (BioBean)ReceiptForm.getFocus();
 				MaxLineNumber =  getMaxLineNumber(objBioBeanOrder);
 			}
 	        ArrayList selectedItems = SODetailListForm.getAllSelectedItems();
 	  		if(selectedItems != null && selectedItems.size() > 0){
 	       		Iterator bioBeanIter = selectedItems.iterator();
 	       		BioBean SOLine;
 				for(; bioBeanIter.hasNext();){
 					SOLine = (BioBean)bioBeanIter.next();
 					
 		        	if ((firstLine) & (ReceiptForm.getFocus() instanceof QBEBioBean)){			//SCM-00000-03768
 		        		objBioBeanRelatedProduct = uob.getNewBio(receiptDetailQBEBioBean);
 		        		firstLine = false;
 		        	}else{
 		        		objBioBeanRelatedProduct = uob.getNewBio("receiptdetail");	
 		        	}

 					
// 					objBioBeanRelatedProduct = uob.getNewBio("receiptdetail");
 					newLineNumber = GenerateNewNumber(MaxLineNumber, context);
 					_log.debug("LOG_SYSTEM_OUT","The Max is = "+ newLineNumber,100L);
 					objBioBeanRelatedProduct.set("RECEIPTKEY",  objBioBeanOrder.get("RECEIPTKEY"));
 					objBioBeanRelatedProduct.set("RECEIPTLINENUMBER",  newLineNumber);
 					objBioBeanRelatedProduct.set("TYPE","2");
 					objBioBeanRelatedProduct.set("SKU", SOLine.get("SKU"));
 					objBioBeanRelatedProduct.set("PACKKEY", SOLine.get("PACKKEY"));
 					objBioBeanRelatedProduct.set("STORERKEY", SOLine.get("STORERKEY"));
 					objBioBeanRelatedProduct.set("UOM", SOLine.get("UOM"));
 					objBioBeanRelatedProduct.set("QTYEXPECTED", SOLine.get("SHIPPEDQTY"));
 					objBioBeanRelatedProduct.set("QTYRECEIVED", "0.0");
 					objBioBeanRelatedProduct.set("QCREQUIRED", "0");
 					objBioBeanRelatedProduct.set("QCAUTOADJUST", "0");
 					objBioBeanRelatedProduct.set("TOLOC", wsdefaults.getDefaultRecieveLoc(context.getState()));
 					objBioBeanRelatedProduct.set("SUSR1", "");
 					objBioBeanRelatedProduct.set("SUSR2", "");
 					objBioBeanRelatedProduct.set("SUSR3", "");
 					objBioBeanRelatedProduct.set("SUSR4", "");
 					objBioBeanRelatedProduct.set("SUSR5", "");
 					objBioBeanRelatedProduct.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
 					objBioBeanOrder.addToBioCollection("RECEIPTDETAILS", objBioBeanRelatedProduct);
 					// 2012-08-28
					// Modified by Will Pu
					// 自出货单填充新增逻辑
 					objBioBeanRelatedProduct.set("POKEY", SOLine.get("ORDERKEY"));
 					objBioBeanRelatedProduct.set("POLineNumber", SOLine.get("ORDERLINENUMBER"));
 					
 					// 2012-08-13
					// Modified by Will Pu
					// 自出货单填充新增逻辑
					try {
						updateASNStatusUpdator(context, SOLine.get("ORDERKEY").toString(), objBioBeanOrder.get("RECEIPTKEY").toString());
					} catch (UserException e) {
						e.printStackTrace();
						throw new UserException("", new Object[0]);
					}
	 					
 					 MaxLineNumber+=1;
 				}
 	       	}else{
 	       		UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
 	 	   		throw UsrExcp;
 	       	}
 		}
        uob.saveUOW();
      return (DataBean)objBioBeanOrder;
      }

    /**
     * This method creates order.
     * @param context
     * @param result
     * @param tabGroupShellForm
     * @return order BioBean
     * @throws EpiException
     */
    private BioBean createOrder( ModalActionContext context, ActionResult result, RuntimeFormInterface headerForm, BioBean selBio) throws EpiException {

        UnitOfWorkBean uob = context.getState().getDefaultUnitOfWork();
        DataBean objDataBeanOrder =  headerForm.getFocus();

        QBEBioBean objQBEBioBeanOrder =   (QBEBioBean)objDataBeanOrder;
        BioBean objBioBeanOrder = uob.getNewBio(objQBEBioBeanOrder);
        objBioBeanOrder.set("RECEIPTID", GUIDFactory.getGUIDStatic());		//auditing Fix
        objBioBeanOrder.set("STORERKEY",selBio.getValue("STORERKEY"));
        objBioBeanOrder.set("TYPE","2");
//        uob.saveUOW();
        return objBioBeanOrder;

    }
    private int getMaxLineNumber(BioBean objBioBeanOrder)throws EpiException{
    	
    	Object max = null;
    	BioCollectionBean ReceiptDetailBiocollection = (BioCollectionBean)objBioBeanOrder.get("RECEIPTDETAILS");
    	_log.debug("LOG_SYSTEM_OUT","The Size of the line = "+ ReceiptDetailBiocollection.size(),100L);
        if(ReceiptDetailBiocollection.size()!=0){
        	_log.debug("LOG_SYSTEM_OUT","Dynamically finding max",100L);
        	max = ReceiptDetailBiocollection.max("RECEIPTLINENUMBER");
        	max = findMax(ReceiptDetailBiocollection, "RECEIPTLINENUMBER");
        }else{
        	_log.debug("LOG_SYSTEM_OUT","Statically declared max",100L);
        	max = "0";
        }
        int size = Integer.parseInt(max.toString());
        return size;
    }
    
    
    private String GenerateNewNumber(int size,ModalActionContext context)throws EpiException{
    	String lineNumber = "";
        _log.debug("LOG_SYSTEM_OUT","The Max is = "+ size,100L);
		size+=1;
		
//		HC.b
		String zeroPadding=null;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
//		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query("wm_system_settings",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
		try {
			zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
		} catch (EpiDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DecimalFormat template = null;
		if (zeroPadding.equalsIgnoreCase("1")){
			if(size<10000){
				lineNumber+="0";
				if(size<1000){
					lineNumber+="0";
					if(size<100){
						lineNumber+="0";
						if(size<10){
							lineNumber+="0";
						}
					}
				}
			}
		}
//HC.e
		lineNumber+=size;
		return lineNumber;
	}
    
    private long findMax(BioCollection bioCollection, String field) throws EpiDataException{
		long max = 0;
		TreeSet tree = new TreeSet();
		for(int i = 0; i < bioCollection.size(); i++){				
			Object bioFieldValueObj = bioCollection.elementAt(i).get(field);
			try {
				long tempBioLongValue = Long.parseLong(bioFieldValueObj.toString());
				Long bioFieldValue = new Long(tempBioLongValue);
				tree.add(bioFieldValue);
			} catch (NumberFormatException e1) {					
			}
		}
		max = ((Long)tree.last()).longValue();
		return max;
	}
    
    /**
     * @author Will Pu
     * 修增逻辑--如果填充源SO单据状态为'98'-外部取消， 则更新当前RECEIPT类型为'14'-冲销收货
     * @param context
     * @param orderkey
     * @param receiptkey
     * @throws UserException
     */
	public void updateASNStatusUpdator(ActionContext context, String orderkey, String receiptkey) throws UserException {
		String status = "0";
		
		HttpSession session = context.getState().getRequest().getSession();
		
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String query = "SELECT STATUS FROM ORDERS WHERE ORDERKEY = ?";
		
    	try {
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		
    		appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
    		
    		ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);
    		
    		if (rs.next()) {
    			status = rs.getString(1).trim();
    		}
    		
    		rs.close();
    	} catch (SQLException e) {
    		// Do nothing
    	}
        
        if (status.equals("98")) {
        	query = "UPDATE RECEIPT SET TYPE = '14' WHERE RECEIPTKEY = ?";
            
        	try {
        		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
        		
        		appAccess.executeUpdate(facilityName.toUpperCase(), query, new Object[0]);
        		
        		ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);

        		rs.close();
        	} catch (SQLException e) {
        		// Do nothing
        	}
        }
	}
}