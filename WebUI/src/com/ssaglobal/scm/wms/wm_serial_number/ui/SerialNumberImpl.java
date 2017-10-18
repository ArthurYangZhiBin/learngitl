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
package com.ssaglobal.scm.wms.wm_serial_number.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.dao.CCDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.util.dto.CCDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_serial_inventory.ui.SerialInventorySearch;

public class SerialNumberImpl {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerialNumberImpl.class);

	public ISerialNumber getSerialNumberInstance(StateInterface state, String sessionAttr){
		ISerialNumber detailSerial = null;
		if (sessionAttr.equalsIgnoreCase("CCDETAILSERIALTMP"))
			detailSerial= (CCDetailSerialTmpDTO)state.getRequest().getSession().getAttribute(sessionAttr);
		else if(sessionAttr.equalsIgnoreCase("ADJUSTMENTDETAILSERIALTMP"))
			detailSerial= (AdjustmentDetailSerialTmpDTO)state.getRequest().getSession().getAttribute(sessionAttr);
		else{
			_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialValidate]Cannot cast session attribute",100L);
		}

		return detailSerial;
    }
    
    public ArrayList getSerials(String storerkey, String sku, String serial){
	
		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);
		
		_log.debug("LOG_SYSTEM_OUT","[SerialNumberImpl]SKU SN:Position:"+skuConf.getSNum_Position(),100L);
		
		SerialNumberObj serialNumber = new SerialNumberObj(skuConf);
		serialNumber.setStorerkey(storerkey);
		serialNumber.setSku(sku);
		
		
		ArrayList list =serialNumber.getValidSerialNos(serial);
		return list;
	}
    
    public void insertSerial(ISerialNumber detailSerial){
    	
    	if(detailSerial instanceof AdjustmentDetailSerialTmpDTO)
    		AdjustmentDetailSerialTmpDAO.insertAdjustmentDetailSerialTmp((AdjustmentDetailSerialTmpDTO)detailSerial);
    	else if(detailSerial instanceof CCDetailSerialTmpDTO)
    		CCDetailSerialTmpDAO.insertCCDetailSerialTmp((CCDetailSerialTmpDTO)detailSerial);
    }

}
