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

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class CCHelper {
	private static final String BIOCLASS_CCDETAILSERIALTMP="wm_ccdetailserialtmp";
	private static final String PERSP_CCDETAILSERIALTMPLISTVIEW="wm_cyclecount_detailserialtmp_list_view_persp";
	
	public static final String FORM_WM_CYCLECOUNT_DETAIL_VIEW = "wm_cyclecount_detail_view";
	public static final String FORM_WM_CYCLECOUNT_DETAILSERIALTMP_LIST_VIEW = "wm_cyclecount_detailserialtmp_list_view";
	
	public static final String SLOT_SERIALINVENTORYLIST ="serialinventorylist";
	public static final String SLOT_LISTSLOT1 = "list_slot_1";

	public static final String WMSLISTSHELL ="wms_list_shell";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCHelper.class);
	
	public  void refreshCCDetailSerialList(UnitOfWorkBean uowb, RuntimeFormExtendedInterface form, 
			StateInterface state, SlotInterface slotSerialInventory, String cckey, String ccdetailkey){
		
		BioCollectionBean ccDetailSerialTmpList =getCCDetailSerialList(uowb, form, state, slotSerialInventory, cckey, ccdetailkey);
		try {
			form.setFocus(state, slotSerialInventory, "", ccDetailSerialTmpList, CCHelper.PERSP_CCDETAILSERIALTMPLISTVIEW);
		} catch (EpiException e) {
			_log.debug("LOG_SYSTEM_OUT","[refreshCCDetailSerialList]:Failed to refresh list perspective contents",100L);
			e.printStackTrace();
		}

	}
	
	public BioCollectionBean getCCDetailSerialList(UnitOfWorkBean uowb, RuntimeFormExtendedInterface form, 
			StateInterface state, SlotInterface slotSerialInventory, String cckey, String ccdetailkey){
		String whereClause = "wm_ccdetailserialtmp.CCKEY = '"+cckey+"' " +
			" AND wm_ccdetailserialtmp.CCDETAILKEY='"+ccdetailkey+"' ";
		
		Query queryCC = new Query(CCHelper.BIOCLASS_CCDETAILSERIALTMP, whereClause, null);
		
		BioCollectionBean ccDetailSerialTmpList = uowb.getBioCollectionBean(queryCC);
		return ccDetailSerialTmpList;
		
	}
	public void refreshQtySelected(UIRenderContext context)throws EpiException{
		StateInterface state = context.getState();
		
		/**
		RuntimeFormInterface detail = FormUtil.findForm(state.getCurrentRuntimeForm(),WMSLISTSHELL,
				CCHelper.FORM_WM_CYCLECOUNT_DETAIL_VIEW,state);
		**/
		RuntimeFormInterface detailForm = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state); 
		
		_log.debug("LOG_SYSTEM_OUT","[CCHelper]detailForm:"+detailForm.getName(),100L);
		
		/**
		RuntimeListFormInterface listForm = 
			(RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),WMSLISTSHELL,
					CCHelper.FORM_WM_CYCLECOUNT_DETAILSERIALTMP_LIST_VIEW, state); 
		
		**/
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getCurrentRuntimeForm().getParentForm(state);
		BioCollectionBean serialFocus = (BioCollectionBean)listForm.getFocus();
		Double qtyAdjusted = new Double(0.00);
		
		if(serialFocus!=null)
			for(int idx = 0 ; idx < serialFocus.size(); idx++){
				Bio serialBio = serialFocus.elementAt(idx);
				
				if(serialBio.get("QTY")!=null){
					
					BigDecimal serialQty = (BigDecimal)serialBio.get("QTY");
					
					qtyAdjusted += serialQty.doubleValue();
					_log.debug("LOG_SYSTEM_OUT","[CCHelper]Serial:"+serialBio.get("SERIALNUMBER").toString()+"QTY"+serialQty,100L);
				}
				
			}
		else
			_log.debug("LOG_SYSTEM_OUT","[CCHelper]No records in List form:"+listForm.getName(),100L);
		
		_log.debug("LOG_SYSTEM_OUT","[CCHelper]QTYSELECTED:"+detailForm.getFormWidgetByName("QTYSELECTED").getValue().toString(),100L);
		detailForm.getFormWidgetByName("QTYSELECTED").setValue(qtyAdjusted.toString());


    }

}
