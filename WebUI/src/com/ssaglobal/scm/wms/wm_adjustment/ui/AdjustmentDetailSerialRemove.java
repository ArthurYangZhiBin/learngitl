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


package com.ssaglobal.scm.wms.wm_adjustment.ui;

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
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;

/**
Selected items will be updated with Qty=-1
*/

public class AdjustmentDetailSerialRemove extends com.epiphany.shr.ui.action.ActionExtensionBase {

	public static final String FORM_WM_ADJUSTMENT_DETAIL_VIEW = "wm_adjustment_detail_view";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailSerialRemove.class);
		
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialRemove]Start",100L);
		
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		//RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm =(RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(), "WMS_LIST_SHELL", 
					AdjustmentHelper.FORM_WM_ADJUSTMENTDETAILSERIALTMP_LIST_VIEW, state);
		
		
		
		List<BioBean> selected = listForm.getAllSelectedItems();
		
		if(selected==null)
			throw new UserException("WMEXP_NO_SELECTION", new Object[0]);
		
		for(BioBean adjDetailSerialBio : selected){
			AdjustmentDetailSerialTmpDTO adjDetailSerial = new AdjustmentDetailSerialTmpDTO();
			adjDetailSerial.setSerialnumber(adjDetailSerialBio.get("SERIALNUMBER").toString());
			adjDetailSerial.setAdjustmentkey(adjDetailSerialBio.get("ADJUSTMENTKEY").toString());
			adjDetailSerial.setAdjustmentlinenumber(adjDetailSerialBio.get("ADJUSTMENTLINENUMBER").toString());
			adjDetailSerial.setStorerkey(adjDetailSerialBio.get("STORERKEY").toString());
			adjDetailSerial.setSku(adjDetailSerialBio.get("SKU").toString());
			
			
			
			
			
			SerialInventoryDAO serialInventoryDAO = new SerialInventoryDAO();
			SerialInventoryDTO serialInventory = serialInventoryDAO.findSerialInventory(adjDetailSerial);
			if (serialInventory==null ){
				AdjustmentDetailSerialTmpDAO.deleteAdjustmentDetailSerialTmp(adjDetailSerial);
				//jp Begin - 8624
				uowb.removeBio(adjDetailSerialBio);
				//jp End - 8624
			}else if(serialInventory.getQty()!=null ){ 
					
				Double qty =(Double)(serialInventory.getQty()*-1);
				adjDetailSerial.setQty(qty.toString());
				AdjustmentDetailSerialTmpDAO.updateAdjustmentDetailSerialTmp(adjDetailSerial);
				uowb.removeBio(adjDetailSerialBio);
				//adjDetailSerialBio.set("QTY", qty.toString() );
				
			}
			/**
			}else{
				
				String[] params = new String[2];
				params[0] = serialInventory.getQty().toString();
				params[1] = adjDetailSerial.getQty();
				_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialRemove]params[0]:"+params[0]+" params[1]"+params[1],100L);
				throw new UserException("WMEXP_WRONG_QTY", new Object[0]);
			}
				**/
		}
		
		
		/**
		RuntimeFormExtendedInterface detailForm = (RuntimeFormExtendedInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"WMS_LIST_SHELL", AdjustmentHelper.FORM_WM_ADJUSTMENTDETAIL_DETAIL_VIEW, state);
		String adjustmentLineNumber = detailForm.getFormWidgetByName("ADJUSTMENTLINENUMBER").getValue().toString();
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"WMS_LIST_SHELL", FORM_WM_ADJUSTMENT_DETAIL_VIEW, state);
		String adjustmentKey = headerForm.getFormWidgetByName("ADJUSTMENTKEY").getValue().toString();
		SlotInterface serialSlot = detailForm.getSubSlot(AdjustmentHelper.SLOT_SERIALINVENTORYLIST);
		
		new AdjustmentHelper().refreshAdjustmentDetailSerialList(uowb, detailForm, state, serialSlot, adjustmentKey , adjustmentLineNumber);
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialRemove]End",100L);
		**/
		//result.setFocus(refreshAdjustmentDetailSerialList(uowb, detailForm, state, serialSlot, adjustmentKey , adjustmentLineNumber));
		
		//jp Begin - 8624
		listForm.setSelectedItems(null);
		//jp End - 8624
		
		return RET_CONTINUE;
	}
	

   
}
