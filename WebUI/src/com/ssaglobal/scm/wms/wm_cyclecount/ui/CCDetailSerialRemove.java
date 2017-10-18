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
import java.util.List;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.dao.CCDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dto.CCDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;


public class CCDetailSerialRemove extends com.epiphany.shr.ui.action.ActionExtensionBase {


   protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDetailSerialRemove.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

		
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialRemove]Start",100L);
		
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
		//RuntimeListFormInterface listForm =(RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(), CCHelper.WMSLISTSHELL, 
			//		CCHelper.FORM_WM_CYCLECOUNT_DETAILSERIALTMP_LIST_VIEW, state);
		
		
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialRemove]listform:"+listForm.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialRemove]expected name:"+CCHelper.FORM_WM_CYCLECOUNT_DETAILSERIALTMP_LIST_VIEW,100L);
		
		List<BioBean> selected = listForm.getAllSelectedItems();
		
		if(selected==null)
			throw new UserException("WMEXP_NO_SELECTION", new Object[0]);
		
		for(BioBean ccDetailSerialBio : selected){
			CCDetailSerialTmpDTO ccDetailSerial = new CCDetailSerialTmpDTO();
			ccDetailSerial.setSerialnumber(ccDetailSerialBio.get("SERIALNUMBER").toString());
			ccDetailSerial.setCcKey(ccDetailSerialBio.get("CCKEY").toString());
			ccDetailSerial.setCcDetailKey(ccDetailSerialBio.get("CCDETAILKEY").toString());
			ccDetailSerial.setStorerkey(ccDetailSerialBio.get("STORERKEY").toString());
			ccDetailSerial.setSku(ccDetailSerialBio.get("SKU").toString());
			
			
			
			
			
			SerialInventoryDAO serialInventoryDAO = new SerialInventoryDAO();
			SerialInventoryDTO serialInventory = serialInventoryDAO.findSerialInventory(ccDetailSerial);
			if (serialInventory==null )
				CCDetailSerialTmpDAO.deleteCCDetailSerialTmp(ccDetailSerial);
			else if(serialInventory.getQty()!=null ){ 
					
				Double qty =(Double)(serialInventory.getQty()*-1);
				ccDetailSerial.setQty(qty.toString());
				CCDetailSerialTmpDAO.updateCCDetailSerialTmp(ccDetailSerial);
				uowb.removeBio(ccDetailSerialBio);
				//ccDetailSerialBio.set("QTY", qty.toString() );
				
			}
		}
		
		return RET_CONTINUE;
   }
   
}
