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

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.extensions.IExtension;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

public class AdjustmentHelper {


	public static final String BIOCLASS_ADJUTMENTDETAILSERIALTMP = "wm_adjustmentdetailserialtmp";
	
	public static final String PERSP_ADJUSTMENTDETAILSERIALTMPLISTVIEW = "wm_adjustmentdetailserialtmp_list_view_persp";
	
	public static final String FORM_WM_ADJUSTMENTDETAIL_DETAIL_VIEW = "wm_adjustmentdetail_detail_view";
	
	public static final String FORM_WM_ADJUSTMENTDETAILSERIALTMP_LIST_VIEW = "wm_adjustmentdetailserialtmp_list_view";
	
	public static final String SLOT_SERIALINVENTORYLIST ="serialinventorylist";
	public static final String SLOT_LISTSLOT1 = "list_slot_1";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentHelper.class);
	

	public AdjustmentHelper(){
		
	}
	/*
	public boolean isSkuEndToEnd(){
		return true;
	}
	*/
	public int adjustmentSaveWrapper(ActionContext context, ActionResult result){
		AdjustmentSave adjustmentSave = new AdjustmentSave();
		
		try {
			return adjustmentSave.execute(context, result);
		} catch (UserException e) {
			_log.debug("LOG_SYSTEM_OUT","[adjustmentSaveWrapper]:SNumber:Error",100L);
			e.printStackTrace();
			return IExtension.RET_CANCEL;
		}
		
	}
	
	public void setLotLocIdInSession(ActionContext context){
		HttpSession session = context.getState().getRequest().getSession();
		//session.setAttribute("LOT",)
	}
	
	public  void refreshAdjustmentDetailSerialList(UnitOfWorkBean uowb, RuntimeFormExtendedInterface form, 
			StateInterface state, SlotInterface slotSerialInventory, String adjustmentKey, String adjustmentLineNumber){
		
		String whereClause = "wm_adjustmentdetailserialtmp.ADJUSTMENTKEY = '"+adjustmentKey+"' " +
							 " AND wm_adjustmentdetailserialtmp.ADJUSTMENTLINENUMBER='"+adjustmentLineNumber+"' ";
		
		Query queryAdj = new Query(AdjustmentHelper.BIOCLASS_ADJUTMENTDETAILSERIALTMP, whereClause, null);
		
		BioCollectionBean adjDetailSerialTmpList = uowb.getBioCollectionBean(queryAdj);
		try {
			form.setFocus(state, slotSerialInventory, "", adjDetailSerialTmpList, AdjustmentHelper.PERSP_ADJUSTMENTDETAILSERIALTMPLISTVIEW);
		} catch (EpiException e) {
			_log.debug("LOG_SYSTEM_OUT","[refreshAdjustmentDetailSerialList]:Failed to refresh list perspective contents",100L);
			e.printStackTrace();
		}

	}
	
	public ArrayList getSerials(String storerkey, String sku, String serial){
		//SerialNumberDTO serialNumberDTO = new SerialNumberDTO();
		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);
		/**
		serialNumberDTO.setSNum_Delim_Count(skuConf.getSnumDelimCount());
		serialNumberDTO.setSNum_Delimiter(skuConf.getSnumDelimiter());
		serialNumberDTO.setSNum_Incr_Length(skuConf.getSnumIncrLength());
		serialNumberDTO.setSNum_Incr_Pos(skuConf.getSnumIncrPos());
		serialNumberDTO.setSNum_Length(skuConf.getSnumLength());
		serialNumberDTO.setSNum_Mask(skuConf.getSnumMask());
		serialNumberDTO.setSNum_Position(skuConf.getSnumPosition());
		serialNumberDTO.setSNum_Quantity(skuConf.getSnumQuantity());
		**/
		
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentHelper]SKU SN:Position:"+skuConf.getSNum_Position(),100L);
		
		SerialNumberObj serialNumber = new SerialNumberObj(skuConf);
		serialNumber.setStorerkey(storerkey);
		serialNumber.setSku(sku);
		
		
		ArrayList list =serialNumber.getValidSerialNos(serial);
		return list;
	}
	
	public void refreshQtySelected(UIRenderContext context)throws EpiException{
		StateInterface state = context.getState();
		
		RuntimeFormInterface parent = FormUtil.findForm(state.getCurrentRuntimeForm(),"WMS_LIST_SHELL", 
				AdjustmentHelper.FORM_WM_ADJUSTMENTDETAIL_DETAIL_VIEW,state);
		_log.debug("LOG_SYSTEM_OUT","[AdjusmentHelper]parent:"+parent.getName(),100L);
		
		RuntimeListFormInterface form = 
			(RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"WMS_LIST_SHELL",
					AdjustmentHelper.FORM_WM_ADJUSTMENTDETAILSERIALTMP_LIST_VIEW, state); 
		BioCollectionBean serialFocus = (BioCollectionBean)form.getFocus();
		
		Double qtyAdjusted = new Double(0.00);
		
		if(serialFocus!=null)
			for(int idx = 0 ; idx < serialFocus.size(); idx++){
				Bio serialBio = serialFocus.elementAt(idx);
				
				if(serialBio.get("QTY")!=null){
					
					BigDecimal serialQty = (BigDecimal)serialBio.get("QTY");
					
					qtyAdjusted += serialQty.doubleValue();
					_log.debug("LOG_SYSTEM_OUT","[AdjusmentHelper]Serial:"+serialBio.get("SERIALNUMBER").toString()+"QTY"+serialQty,100L);
				}
				
			}
		else
			_log.debug("LOG_SYSTEM_OUT","[AdjustmentHelper]No records in List form:"+form.getName(),100L);
		
		
		parent.getFormWidgetByName("QTYSELECTED").setValue(qtyAdjusted.toString());
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentHelper]QTYSELECTED:"+parent.getFormWidgetByName("QTYSELECTED").getValue().toString(),100L);

    }
}
