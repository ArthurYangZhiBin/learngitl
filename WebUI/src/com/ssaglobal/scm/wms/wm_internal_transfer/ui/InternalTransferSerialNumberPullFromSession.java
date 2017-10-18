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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollectionRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.ModalFormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.SessionUtil;

public class InternalTransferSerialNumberPullFromSession extends ModalFormExtensionBase{
	protected static String EDIT = "EDIT";
	protected static String QRY = "QUERY";
	protected static String TO_OWNER = "TOSTORERKEY";
	protected static String TO_ITEM = "TOSKU";
	protected static String TO_LOC = "TOLOC";
	protected static String TO_LPN = "TOID";
	protected static String TO_QTY = "TOQTY";
	protected static String SERIAL_NO = "SERIALNUMBER";
	protected static String SELECTED = "SELECTED";
	protected static String QTYSELECT = "QTYSELECT";
	protected static String SLOT = "wm_internal_transfer_detail_serial_slot";
	protected static String LIST_SLOT = "wm_internal_transfer_detail_serial_slot";
	protected static String PERSPECTIVE = "wm_serial_inventory_selector_list_view_perspective";
	protected static String SERIAL_REFS = "SERIAL_REFS";
	
	protected StateInterface state;
	protected String table, qryStr; 
	
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form){
		state = context.getState();
		form.getFormWidgetByName(TO_OWNER).setDisplayValue((String)SessionUtil.getInteractionSessionAttribute(TO_OWNER, state));
		form.getFormWidgetByName(TO_ITEM).setDisplayValue((String)SessionUtil.getInteractionSessionAttribute(TO_ITEM, state));
		form.getFormWidgetByName(TO_LOC).setDisplayValue((String)SessionUtil.getInteractionSessionAttribute(TO_LOC, state));
		form.getFormWidgetByName(TO_LPN).setDisplayValue((String)SessionUtil.getInteractionSessionAttribute(TO_LPN, state));
		form.getFormWidgetByName(TO_QTY).setDisplayValue((String)SessionUtil.getInteractionSessionAttribute(TO_QTY, state));
		String qtySelect = (String)SessionUtil.getInteractionSessionAttribute(QTYSELECT, state);
		if(!isEmpty(qtySelect)){
			form.getFormWidgetByName(QTYSELECT).setDisplayValue(qtySelect);
		}else{
			form.getFormWidgetByName(QTYSELECT).setDisplayValue("0.00000");
		}
		if(SessionUtil.getInteractionSessionAttribute(SELECTED, state)==null || context.getActionObject().getName().equals(EDIT)){
			qryStr = (String)SessionUtil.getInteractionSessionAttribute(QRY, state);
			table = (qryStr.split("\\.", 2))[0];
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean bcBean;
			Query qry = new Query(table, qryStr, null);
			bcBean = uowb.getBioCollectionBean(qry);
			RuntimeFormExtendedInterface exeForm = (RuntimeFormExtendedInterface)form.getParentForm(state);
			try{
				exeForm.setFocus(state, form.getParentForm(state).getSubSlot(SLOT), null, bcBean, PERSPECTIVE);
				if(SessionUtil.getInteractionSessionAttribute(SERIAL_REFS, state)!=null){
					BioCollectionRef serialRefs = (BioCollectionRef)SessionUtil.getInteractionSessionAttribute(SERIAL_REFS, state);
					RuntimeListFormInterface list = (RuntimeListFormInterface)state.getRuntimeForm(exeForm.getSubSlot(LIST_SLOT), null);
					list.setSelectedItems(findSelectable(serialRefs, bcBean));
				}
			}catch(EpiException e){
				e.printStackTrace();
			}
		}
		return RET_CONTINUE;
	}
	
	private boolean isEmpty(String value){
		if(value==null){
			return true;
		}else if(value.matches("\\s*")){
			return true;
		}else{
			return false;
		}
	}
	
	private ArrayList findSelectable(BioCollectionRef serialRefs, BioCollectionBean serials) throws EpiDataException{
		BioCollectionBean bcBean = state.getDefaultUnitOfWork().getBioCollection(serialRefs);
		String queryString = "";
		for(int index=0; index<bcBean.size(); index++){
			String serialStr = (String)bcBean.elementAt(index).get(SERIAL_NO);
			if(queryString.equalsIgnoreCase("")){
				queryString = "("+qryStr+" AND "+table+"."+SERIAL_NO+"='"+serialStr+"')";
			}else{
				queryString = queryString+" OR ("+qryStr+" AND "+table+"."+SERIAL_NO+"='"+serialStr+"')";
			}
		}
		Query query = new Query(table, queryString, null);
		BioCollectionBean filter = (BioCollectionBean)serials.filter(query);
		return filter.getBioRefs();
	}
}