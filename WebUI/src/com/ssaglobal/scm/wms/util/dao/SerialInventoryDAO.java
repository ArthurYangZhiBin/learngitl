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
package com.ssaglobal.scm.wms.util.dao;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetDoubleOutputParam;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_serial_number.ui.ISerialNumber;

public class SerialInventoryDAO {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerialInventoryDAO.class);
	
	public SerialInventoryDTO findSerialInventory(AdjustmentDetailSerialTmpDTO adjDetailSerial)
	throws UserException{
	
		SerialInventoryDTO serialInventory = new SerialInventoryDTO();
		
		try {
			String stmt = "SELECT serialnumber, qty FROM serialInventory "+
			" WHERE serialnumber =" + AdjustmentDetailSerialTmpDAO.setQuote(adjDetailSerial.getSerialnumber()) +
			" AND storerkey = " + AdjustmentDetailSerialTmpDAO.setQuote(adjDetailSerial.getStorerkey()) +
			" AND sku = " + AdjustmentDetailSerialTmpDAO.setQuote(adjDetailSerial.getSku());
			
			EXEDataObject serialInventoryList = WmsWebuiValidationSelectImpl.select(stmt);
			
			if(serialInventoryList!=null && serialInventoryList.getRowCount()>0){
				Double qty = 0.00;
				
				GetDoubleOutputParam qtyParam = serialInventoryList.getDouble(new TextData("qty"), qty);
				qty = qtyParam.pResult;
				
				serialInventory.setQty(qty);
				
				String serialnumber=null;
				GetStringOutputParam serialParam = serialInventoryList.getString(new TextData("serialnumber"), serialnumber);
				serialnumber = serialParam.pResult;
				
				serialInventory.setSerialnumber(serialnumber);
			}else
				serialInventory=null;
			
		
		} catch (DPException e) {
			serialInventory=null;
			e.printStackTrace();
			throw new UserException("WMEXP_FAILED_SERIALINTVENTORY", new Object[0]);
		}
		
		return serialInventory;
	}

	public SerialInventoryDTO findSerialInventory(ISerialNumber detailSerial)
	throws UserException{
	
		SerialInventoryDTO serialInventory = new SerialInventoryDTO();
		
		try {
			String stmt = "SELECT serialnumber, qty FROM serialInventory "+
			" WHERE serialnumber =" + AdjustmentDetailSerialTmpDAO.setQuote(detailSerial.getSerialnumber()) +
			" AND storerkey = " + AdjustmentDetailSerialTmpDAO.setQuote(detailSerial.getStorerkey()) +
			" AND sku = " + AdjustmentDetailSerialTmpDAO.setQuote(detailSerial.getSku());
			
			EXEDataObject serialInventoryList = WmsWebuiValidationSelectImpl.select(stmt);
			
			if(serialInventoryList!=null && serialInventoryList.getRowCount()>0){
				Double qty = 0.00;
				
				GetDoubleOutputParam qtyParam = serialInventoryList.getDouble(new TextData("qty"), qty);
				qty = qtyParam.pResult;
				
				serialInventory.setQty(qty);
				
				String serialnumber=null;
				GetStringOutputParam serialParam = serialInventoryList.getString(new TextData("serialnumber"), serialnumber);
				serialnumber = serialParam.pResult;
				
				serialInventory.setSerialnumber(serialnumber);
			}else
				serialInventory=null;
			
		
		} catch (DPException e) {
			serialInventory=null;
			e.printStackTrace();
			throw new UserException("WMEXP_FAILED_SERIALINTVENTORY", new Object[0]);
		}
		
		return serialInventory;
	}
	/***
	 * Checks that given SerialNumber is part of SerialInventory table
	 * @param state
	 * @param storerkey
	 * @param sku
	 * @param lot
	 * @param loc
	 * @param id
	 * @param serialnumber
	 * @return
	 */
	public boolean isAvailable(StateInterface state, String storerkey, String sku, String lot, String loc, String id, String serialnumber) {
	    
	    boolean status = false;
	    
	    if (storerkey !=null && sku!=null && lot!=null && loc !=null && id!=null && serialnumber!=null){
		    String whereClause = "wm_serialinventory.STORERKEY = '" + storerkey + "' " +
				" and wm_serialinventory.SKU = '" + sku + "' " +
				" and wm_serialinventory.LOT = '" + lot + "' " +
				" and wm_serialinventory.LOC = '" + loc + "' " +
				" and wm_serialinventory.ID = '" + id + "' " + 
				" and wm_serialinventory.SERIALNUMBER = '" + serialnumber + "'";


		    Query query = new Query("wm_serialinventory",whereClause,null);
		    
		    BioCollectionBean bcb = state.getDefaultUnitOfWork().getBioCollectionBean(query);
		    try {
				if(bcb!=null && bcb.size()>0){
					status=true;
				}
			} catch (EpiDataException e) {
				_log.error("WMEXP_FAILED_SERIALINTVENTORY", "Error reading SerialInventory data", 100L);
			}
	    	
	    }
		
		return status;
	}


}
