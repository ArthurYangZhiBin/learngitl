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

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

public class InternalTransferSerialNumberQuery extends ActionExtensionBase{
	protected static String SERIAL_BIO = "wm_serialinventory";
	protected static String ERROR_MSG_EMPTY = "WMEXP_ADJ_NO_RECORDS";
	protected static String FROM_ITEM = "FROMSKU";
	protected static String FROM_OWNER = "FROMSTORERKEY";
	protected static String FROM_LOC = "FROMLOC";
	protected static String FROM_LOT = "FROMLOT";
	protected static String FROM_LPN = "FROMID";
	protected static String TO_OWNER = "TOSTORERKEY";
	protected static String TO_ITEM = "TOSKU";
	protected static String TO_LOC = "TOLOC";
	protected static String TO_LPN = "TOID";
	protected static String TO_QTY = "TOQTY";
	protected static String TO_PACKKEY = "TOPACKKEY";
	protected static String TO_UOM = "TOUOM";
	protected static String ITEM_BIO = "SKU";
	protected static String OWNER = "STORERKEY";
	protected static String ITEM = "SKU";
	protected static String LOC = "LOC";
	protected static String LOT = "LOT";
	protected static String LPN = "ID";
	protected static String QTY = "qty";
	protected static String QRY = "QUERY";
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	protected static String TRANSFER_LINE_NUMBER = "TRANSFERLINENUMBER";
	protected static String LLI_BIO = "wm_lotxlocxid"; // "wm_inventorybalanceslotxlocxlpn";
	protected static String ETE_FLAG = "SNUM_ENDTOEND";
	protected static String REFRESH_NAV = "refresh";
	protected static String MODAL_NAV = "modal";
	protected static String SHELL_FORM = "wms_list_shell";
	protected static String DETAIL_FORM = "wm_internal_transfer_detail_detail_view";
	protected static String SLOT1 = "list_slot_1";
	protected static String FROM_SLOT = "FromSlot";
	protected static String TO_SLOT = "ToSlot";
	protected static String SERIAL_SLOT = "SerialNumberSlot";
	protected static String EDIT = "EDIT";
	protected static String SERIAL_REFS = "SERIAL_REFS";
	protected static String TAB_0 = "tab 0";
	protected static String TAB_GROUP_SLOT = "tbgrp_slot";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiDataException, UserException{
		StateInterface state = context.getState();
		RuntimeFormInterface detailForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface serialForm;
		try{
			while(!detailForm.getName().equals(DETAIL_FORM)){
				detailForm = detailForm.getParentForm(state);
			}
			serialForm = state.getRuntimeForm(detailForm.getSubSlot(SERIAL_SLOT), null);
		}catch(NullPointerException e){
			serialForm = state.getCurrentRuntimeForm().getParentForm(state);
			detailForm = state.getRuntimeForm(serialForm.getParentForm(state).getSubSlot(TAB_GROUP_SLOT), TAB_0);
		}

		RuntimeFormInterface headerForm = detailForm;
		while(!headerForm.getName().equals(SHELL_FORM)){
			headerForm = headerForm.getParentForm(state);
		}
		headerForm = state.getRuntimeForm(headerForm.getSubSlot(SLOT1), null);
		RuntimeFormInterface toForm = state.getRuntimeForm(detailForm.getSubSlot(TO_SLOT), null);
		RuntimeFormInterface fromForm = state.getRuntimeForm(detailForm.getSubSlot(FROM_SLOT), null);
		String item = fromForm.getFormWidgetByName(FROM_ITEM).getDisplayValue();
		String owner = headerForm.getFormWidgetByName(FROM_OWNER).getDisplayValue();
		//Check for Serial Number Capture
		String displayQty = toForm.getFormWidgetByName(TO_QTY).getDisplayValue();
		String displayPackKey = toForm.getFormWidgetByName(TO_PACKKEY).getDisplayValue();
		String displayUom = toForm.getFormWidgetByName(TO_UOM).getDisplayValue();
		//double toQty = Double.parseDouble(convertUOMQty(displayUom, displayQty, displayPackKey));
		double toQty = Double.parseDouble(UOMMappingUtil.convertUOMQty(displayUom, UOMMappingUtil.UOM_EA, displayQty, displayPackKey, state, UOMMappingUtil.uowNull, true)); //AW 06871
		if (toQty > 0.0 && (displayUom != null && displayUom.matches("//s*")==false))
		{
			SkuSNConfDTO snInfo = SkuSNConfDAO.getSkuSNConf(owner, item); 
			if(snInfo.getSNum_EndToEnd().equals("1")){
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				String loc = fromForm.getFormWidgetByName(FROM_LOC).getDisplayValue();
				String lot = fromForm.getFormWidgetByName(FROM_LOT).getDisplayValue();
				String lpn = fromForm.getFormWidgetByName(FROM_LPN).getDisplayValue();
				String qryStr = LLI_BIO+"."+ITEM+"='"+item+"' AND "+LLI_BIO+"."+LOC+"='"+loc+"' AND "
					+LLI_BIO+"."+LOT+"='"+lot+"' AND "+LLI_BIO+"."+LPN+"='"+lpn+"'";
				Query qry = new Query(LLI_BIO, qryStr, null);
				BioCollectionBean bcBean = uowb.getBioCollectionBean(qry);
				if(bcBean.size()>0){
					double ibQty = Double.parseDouble(bcBean.elementAt(0).get(QTY).toString());
					if(toQty<=ibQty && toQty!=0){
						//Get related Serial Inventory records
						qryStr = SERIAL_BIO+"."+OWNER+"='"+owner+"' AND "+SERIAL_BIO+"."+ITEM+"='"+item+"' AND "+SERIAL_BIO+"."
								+LOC+"='"+loc+"' AND "+SERIAL_BIO+"."+LOT+"='"+lot+"' AND "+SERIAL_BIO+"."+LPN+"='"+lpn+"'";
						qry = new Query(SERIAL_BIO, qryStr, null);
						bcBean = uowb.getBioCollectionBean(qry);
						if(bcBean.size()>0){
							//Set bio collection for navigation
							result.setFocus(bcBean);
							context.setNavigation(MODAL_NAV);
							
							//Set extra data values to session
							SessionUtil.setInteractionSessionAttribute(TO_OWNER, headerForm.getFormWidgetByName(TO_OWNER).getDisplayValue(), state);
							SessionUtil.setInteractionSessionAttribute(TO_ITEM, toForm.getFormWidgetByName(TO_ITEM).getDisplayValue(), state);
							SessionUtil.setInteractionSessionAttribute(TO_LOC, toForm.getFormWidgetByName(TO_LOC).getDisplayValue(), state);
							SessionUtil.setInteractionSessionAttribute(TO_LPN, toForm.getFormWidgetByName(TO_LPN).getDisplayValue(), state);
							SessionUtil.setInteractionSessionAttribute(TO_QTY, ""+toQty, state);
							SessionUtil.setInteractionSessionAttribute(QRY, qryStr, state);
							SessionUtil.setInteractionSessionAttribute(TRANSFER_KEY, headerForm.getFormWidgetByName(TRANSFER_KEY).getDisplayValue(), state);
							SessionUtil.setInteractionSessionAttribute(TRANSFER_LINE_NUMBER, detailForm.getFormWidgetByName(TRANSFER_LINE_NUMBER).getDisplayValue(), state);
						}else{
							//No related records found, throw exception
							String[] param = new String[1];
							param[0] = colonStrip(readLabel(headerForm.getFormWidgetByName(FROM_OWNER)))
								+", "+colonStrip(readLabel(fromForm.getFormWidgetByName(FROM_ITEM)))
								+", "+colonStrip(readLabel(fromForm.getFormWidgetByName(FROM_LOC)))
								+", "+colonStrip(readLabel(fromForm.getFormWidgetByName(FROM_LOT)))
								+", "+readLabel(fromForm.getFormWidgetByName(FROM_LPN))
								+"  ("+owner+", "+item+", "+loc+", "+lot+", "+lpn+")";
							throw new UserException(ERROR_MSG_EMPTY, param);
						}
						if(context.getActionObject().getName().equals(EDIT)){
							BioCollectionBean serialBCB = (BioCollectionBean)serialForm.getFocus();
							SessionUtil.setInteractionSessionAttribute(SERIAL_REFS, serialBCB.getBioCollectionRef(), state);
						}
					}else{
						if(toQty==ibQty){
							clearTemp(headerForm.getFormWidgetByName(TRANSFER_KEY).getDisplayValue(), detailForm.getFormWidgetByName(TRANSFER_LINE_NUMBER).getDisplayValue());
						}
						context.setNavigation(REFRESH_NAV);
					}
				}else{
					context.setNavigation(REFRESH_NAV);
				}
			}else{
				context.setNavigation(REFRESH_NAV);
			}
		}else{
			context.setNavigation(REFRESH_NAV);
		}
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}

	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
	
	private void clearTemp(String transKey, String transLineNo) throws DPException{
		String delStr = "DELETE FROM TDSTEMP WHERE TRANSFERKEY='"+transKey+"' AND TRANSFERLINENUMBER='"+transLineNo+"'";
		WmsWebuiValidationDeleteImpl.delete(delStr);
	}

}