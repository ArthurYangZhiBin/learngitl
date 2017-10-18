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
import java.rmi.RemoteException;

import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetDoubleOutputParam;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.dao.CCDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.util.dto.CCDetailSerialTmpDTO;


public class PopulateCCDetailSerialTmp extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private static final String HEADERFORM ="wm_cyclecount_detail_header_view";
	private static final String SHELLFORM ="wms_list_shell";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateCCDetailSerialTmp.class);
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		

		_log.debug("LOG_SYSTEM_OUT","[PopulateCCDetailSerialTmp]: START",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface form  = state.getCurrentRuntimeForm();
		String storerkey = (String)form.getFormWidgetByName("STORERKEY").getValue();
		String sku = (String)form.getFormWidgetByName("SKU").getValue();
		
		Object qtyObj = form.getFormWidgetByName("QTY").getValue();
		Object sysqtyObj = form.getFormWidgetByName("SYSQTY").getValue();
		Double qty=0.0, sysqty=0.0;
		
		if(qtyObj instanceof Double)
			qty = (Double)qtyObj;
		else if(qtyObj instanceof BigDecimal)
			qty = ((BigDecimal)qtyObj).doubleValue();

		if(sysqtyObj instanceof Double)
			sysqty = (Double)sysqtyObj;
		else if(sysqtyObj instanceof BigDecimal)
			sysqty = ((BigDecimal)sysqtyObj).doubleValue();

		_log.debug("LOG_SYSTEM_OUT","[PopulateCCDetailSerialTmp]: Form:"+form.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","[PopulateCCDetailSerialTmp]: qty:"+qty+" sysqty:"+sysqty+ " storerkey:"+storerkey+" sku:"+sku,100L);
		if(qty>0 && SkuSNConfDAO.isSkuSerialNumberEndtoEnd(storerkey, sku) ){
			populateCCDetailSerialFromSerialInventory(context, context.getSourceWidget());
		}
		
		form.getFormWidgetByName("QTYSELECTED").setValue("0");
		
		return RET_CONTINUE;
	}
   
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
    
    
	private void populateCCDetailSerialFromSerialInventory(ActionContext context, RuntimeFormWidgetInterface source){
		StateInterface state = context.getState();
		RuntimeFormExtendedInterface form = (RuntimeFormExtendedInterface)source.getForm();
		SlotInterface slotSerialInventory = (SlotInterface)  form.getSubSlot(CCHelper.SLOT_SERIALINVENTORYLIST);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		String lot = (String) form.getFormWidgetByName("LOT").getValue();
		String loc = (String) form.getFormWidgetByName("LOC").getValue();
		String id =  (String) form.getFormWidgetByName("ID").getValue();
		String ccdetailkey = (String)form.getFormWidgetByName("CCDETAILKEY").getValue();

		_log.debug("LOG_SYSTEM_OUT","[PopulateCCDetailSerialTmp]Lot:"+lot+" Loc:"+loc+" Id:"+id,100L);
		
		RuntimeFormInterface parent = FormUtil.findForm((RuntimeFormInterface)form, SHELLFORM, SHELLFORM, state);
		SlotInterface headerSlot = parent.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,HEADERFORM);


		String cckey = headerForm.getFormWidgetByName("CCKEY").getValue().toString();
		
		try {
			
			//if(lot!=null && loc!=null && id!=null){
			if(loc!=null ){
				
				//Delete previous data
				String stmt ="DELETE FROM ccdetailserialtmp "+
					" WHERE ccdetailkey ='"+ ccdetailkey +"' ";
					
					
				_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
				new WmsDataProviderImpl().executeUpdateSql(stmt);
				
				//Select data from SerialInventory
				stmt="SELECT serialnumber, storerkey, sku, " +
					" lot, id, loc, qty, data2, data3, data4, data5, grossweight, netweight, serialnumberlong "+ 
					" FROM serialinventory " +
					" WHERE loc='"+ loc+ "'" ;
				
				if(lot!=null && lot.trim().length()>0)
					stmt +=" AND lot ='"+ lot + "' ";
				
				if(id!=null && id.trim().length()>0)
					stmt += " AND id ='"+ id + "'";

				_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
				//Populate ccDetailSerialTmp with data from SerialInventory
				EXEDataObject serialInventoryList = WmsWebuiValidationSelectImpl.select(stmt);
				for(int idx =0; idx < serialInventoryList.getRowCount(); idx++){
					buildInsertCCDetailSerial(serialInventoryList, cckey, ccdetailkey);
					
					serialInventoryList.getNextRow();
				}
				
				
				
				//Refresh list view of ccDetailSerialTmp
				new CCHelper().refreshCCDetailSerialList(uowb, form, state, slotSerialInventory,cckey, ccdetailkey);
			}
		} catch (EpiException e) {
			_log.debug("LOG_SYSTEM_OUT","[SNumber]:error when setting focus for serial inventory list",100L);
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void buildInsertCCDetailSerial(EXEDataObject serialInventoryList, String cckey, String ccdetailkey) {
		if (serialInventoryList == null || cckey==null || ccdetailkey==null)
			return;

		CCDetailSerialTmpDTO ccDetailSerial = new CCDetailSerialTmpDTO();
		
		ccDetailSerial.setCcKey(cckey);
		ccDetailSerial.setCcDetailKey(ccdetailkey);
		
		GetStringOutputParam param = null;
		GetDoubleOutputParam paramDouble = null;
		param = serialInventoryList.getString(new TextData("serialnumber"), ccDetailSerial.getSerialnumber());
		ccDetailSerial.setSerialnumber(param.pResult);
		
		param = serialInventoryList.getString(new TextData("storerkey"), ccDetailSerial.getStorerkey());
		ccDetailSerial.setStorerkey(param.pResult);
		
		param = serialInventoryList.getString(new TextData("sku"), ccDetailSerial.getSku());
		ccDetailSerial.setSku(param.pResult);

		param = serialInventoryList.getString(new TextData("lot"), ccDetailSerial.getLot());
		ccDetailSerial.setLot(param.pResult);

		param = serialInventoryList.getString(new TextData("id"), ccDetailSerial.getId());
		ccDetailSerial.setId(param.pResult);
		
		param = serialInventoryList.getString(new TextData("loc"), ccDetailSerial.getLoc());
		ccDetailSerial.setLoc(param.pResult);

		double qty=0.0;
		paramDouble = serialInventoryList.getDouble(new TextData("qty"), qty);
		ccDetailSerial.setQty((new Double(qty)).toString());

		param = serialInventoryList.getString(new TextData("data2"), ccDetailSerial.getData2());
		ccDetailSerial.setData2(param.pResult);
		
		param = serialInventoryList.getString(new TextData("data3"), ccDetailSerial.getData3());
		ccDetailSerial.setData3(param.pResult);

		
		param = serialInventoryList.getString(new TextData("data4"), ccDetailSerial.getData4());
		ccDetailSerial.setData4(param.pResult);

		param = serialInventoryList.getString(new TextData("data5"), ccDetailSerial.getData5());
		ccDetailSerial.setData5(param.pResult);

		
		paramDouble = serialInventoryList.getDouble(new TextData("grossweight"), qty);
		qty = paramDouble.pResult;
		ccDetailSerial.setGrossweight((new Double(qty)).toString());

		
		paramDouble = serialInventoryList.getDouble(new TextData("netweight"), qty);
		qty = paramDouble.pResult;
		ccDetailSerial.setNetweight((new Double(qty)).toString());

		param = serialInventoryList.getString(new TextData("serialnumberlong"), ccDetailSerial.getSerialnumberlong());
		ccDetailSerial.setSerialnumberlong(param.pResult);

		CCDetailSerialTmpDAO.insertCCDetailSerialTmp(ccDetailSerial);
		
	}


}
