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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;					//10-16-2008	SCM	Machine2049490-SDIS05140
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class AdjustmentPreSave extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException, EpiException{
		/**
		 * Modification History:
		 * 10/16/2008	SCM	Machine2049490-SDIS05140: Backend exception prevents save due to refresh 
		 * 04/23/2009	AW	SDIS:SCM-00000-06871 Machine:2353530
	 	 *					Changed code to allow qty values for the Currency Locale
	 	 *					something other than dollar.
     	 * 05/19/2009   AW  SDIS:SCM-00000-06871 Machine:2353530
	 	 * 					UOM conversion is now done in the front end.
	 	 *                  Changes were made accordingly.
	 	 * 03/22/2009	AW	Infor365:217417
	 	 * 					In certain locales, the precision was defaulting to 3                 
		 */
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		DataBean headerFocus = headerForm.getFocus();
		RuntimeFormInterface detailToggle = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);
		RuntimeFormInterface detailForm = null;
		
		HttpSession session = state.getRequest().getSession();
		String saveLPN = (String)session.getAttribute("SAVELPN");

		if(headerFocus.isTempBio()){
			detailForm = detailToggle;
		}else{
			int active = state.getSelectedFormNumber(detailToggle.getSubSlot("wm_adjustmentdetail_toggle_slot"));
			if(active==0){
				context.getSourceWidget().setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				throw new FormException("WMEXP_NO_SAVE_AVAILABLE", null);
			}
			detailForm = state.getRuntimeForm(detailToggle.getSubSlot("wm_adjustmentdetail_toggle_slot"), "Detail");

		}

		//Verify lot, loc, and id
		String lot = detailForm.getFormWidgetByName("LOT").getDisplayValue();
		String loc = detailForm.getFormWidgetByName("LOC").getDisplayValue();
		String parameters[]= new String[1];
		
		//Verify Loc
		if (!validateLoc(loc,uowb)){
			parameters[0]=loc;
			throw new FormException("LOC_VALIDATION", parameters);
		}
		
		//Verify Lot
		if (!validateLot(lot,uowb)){
			parameters[0]=lot;
			throw new FormException("LOT_VALIDATION", parameters);
		}

		String table = "wm_lotxlocxid";
		String id = detailForm.getFormWidgetByName("ID").getDisplayValue();
		if(id==null){
			id="";
		}
		String queryString = table+".LOT='"+lot+"' and "+table+".LOC='"+loc+"' and "+table+".ID='"+id+"'";
		Query qry = new Query(table, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if(list.size()<1){
			if(id.equals("")){
				id=null;
			}
			parameters[0]=lot+", "+loc+", "+id;

//			_log.debug("LOG_SYSTEM_OUT","[AdjustmentPreSave]Checking for saveLPN:"+saveLPN,100L);
			if (saveLPN == null || saveLPN.compareToIgnoreCase("N")==0)
				throw new FormException("WMEXP_ADJ_LLI_NOT_FOUND", parameters);	
			else{
				//session.setAttribute("SAVELPN","N"); //Resetting flag  
				//return RET_CANCEL;
			}
		}

		//Verify unique key
		if(headerFocus.isTempBio()){
			String key = headerForm.getFormWidgetByName("ADJUSTMENTKEY").getDisplayValue();
			if(key!=null){
				key = key.toUpperCase();
			}
			queryString = "wm_adjustment.ADJUSTMENTKEY='"+key+"'";
			qry = new Query("wm_adjustment", queryString, null);
			list = uowb.getBioCollectionBean(qry);
			if(list.size()>0){
				String parameter[]= new String[1];
				parameter[0]=colonStrip(readLabel(headerForm.getFormWidgetByName("ADJUSTMENTKEY")));
				throw new FormException("WMEXP_CCPS_EXISTS", parameter);
			}
			QBEBioBean headerQBE = (QBEBioBean)headerFocus;
			headerQBE.set("ADJUSTMENTKEY", key);
		}
	
		//Store quantities as EA
		if(detailForm.getFocus().isTempBio()){
			QBEBioBean qbe = (QBEBioBean)detailForm.getFocus();
			String toggle = detailForm.getFormWidgetByName("ISADJ").getValue().toString();
			String qty = null;
			String uom = detailForm.getFormWidgetByName("CHANGEQTYUOM").getValue().toString();
			String pack = qbe.get("PACKKEY").toString();
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uowb);//AW 10/03/2008 Machine#:2093019 SDIS:SCM-00000-05440
			if(toggle.equals("1")){
				qty = detailForm.getFormWidgetByName("ORIGCHANGEQTY").getValue().toString();
			}else{
				String target = detailForm.getFormWidgetByName("ORIGCHANGEQTY").getValue().toString();
				if("".equals(target))
				{
					target = "0";
				}
				String current = detailForm.getFormWidgetByName("ORIGCURRQTY").getValue().toString();
				if(!uom.equals(uom3)){//AW 10/03/2008 Machine#:2093019 SDIS:SCM-00000-05440
					target = UOMMappingUtil .convertUOMQty(uom, UOMMappingUtil.UOM_EA, target, pack, context.getState(),UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
				double tgt = Double.parseDouble(LocaleUtil.resetQtyToDecimalForBackend(target)); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				double cur = Double.parseDouble(LocaleUtil.resetQtyToDecimalForBackend(current)); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
				double adj = tgt - cur;
				qty = ""+adj;
				uom = "EA";
			}
			if(!uom.equals(uom3))//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				qty = UOMMappingUtil .convertUOMQty(uom, UOMMappingUtil.UOM_EA, qty, pack, context.getState(),UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			
			//10-16-2008	SCM		Validate quantity for new headers (Machine2049490-SDIS05140) - Start
			if(headerFocus.isTempBio()){
				String storerkey = detailForm.getFormWidgetByName("STORERKEY").getDisplayValue();
				String sku = detailForm.getFormWidgetByName("SKU").getDisplayValue();
				validateQty(qty, lot, loc, id, storerkey, sku, state.getDefaultUnitOfWork());
			}
			//10-16-2008	SCM		Validate quantity for new headers (Machine2049490-SDIS05140) - End
			qbe.set("QTY", LocaleUtil.checkLocaleAndParseQty(qty, LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10
			if(qbe.get("ID")==null){
				qbe.set("ID", "");
			}
		}
		//Serial Adjusment Qty
		validateSerialAdjQtys(detailForm, state);
		validateWeights(detailForm, state);		//defect1224
		return RET_CONTINUE;
		}
//defect1224.b
	private void validateWeights(RuntimeFormInterface detailForm, StateInterface state) throws UserException{
		double dblTrgGWGT = 0;
		double dblTrgNWGT = 0;
		double dblTrgTWGT = 0;
		double dblAdjGWGT = 0;
		double dblAdjNWGT = 0;
		double dblAdjTWGT = 0;
		Object trgGWGT = detailForm.getFormWidgetByName("ACTUALGROSSWGT").getValue();
		if (trgGWGT!=null && trgGWGT.toString().trim().length() > 0){
			dblTrgGWGT = (new Double(trgGWGT.toString())).doubleValue();
		}
		Object trgNWGT =detailForm.getFormWidgetByName("ACTUALNETWGT").getValue();
		if (trgNWGT!=null && trgNWGT.toString().trim().length() > 0){
			dblTrgNWGT = (new Double(trgNWGT.toString())).doubleValue();
		}
		Object trgTWGT =detailForm.getFormWidgetByName("ACTUALTAREWGT").getValue();
		if (trgTWGT!=null && trgTWGT.toString().trim().length() > 0){
			dblTrgTWGT = (new Double(trgTWGT.toString())).doubleValue();
		}
		Object adjGWGT =detailForm.getFormWidgetByName("ADJGROSSWGT").getValue();
		if (adjGWGT!=null && adjGWGT.toString().trim().length() > 0){
			dblAdjGWGT = (new Double(adjGWGT.toString())).doubleValue();
		}
		Object adjNWGT =detailForm.getFormWidgetByName("ADJNETWGT").getValue();
		if (adjNWGT!=null && adjNWGT.toString().trim().length() > 0){
			dblAdjNWGT = (new Double(adjNWGT.toString())).doubleValue();
		}
		Object adjTWGT =detailForm.getFormWidgetByName("ADJTAREWGT").getValue();
		if (adjTWGT!=null && adjTWGT.toString().trim().length() > 0){
			dblAdjTWGT = (new Double(adjTWGT.toString())).doubleValue();
		}
		if (dblTrgGWGT != dblTrgNWGT + dblTrgTWGT){
			throw new UserException("WMEXP_ITEM_WEIGHTSEQUALLCHECK", new Object[] {});
		}
		if (dblAdjGWGT != dblAdjNWGT + dblAdjTWGT){
			throw new UserException("WMEXP_ITEM_WEIGHTSEQUALLCHECK", new Object[] {});
		}
	}
//defect1224.e
	
	private void validateSerialAdjQtys(RuntimeFormInterface detailForm, StateInterface state) throws UserException{
		SlotInterface serialSlot = detailForm.getSubSlot(AdjustmentHelper.SLOT_SERIALINVENTORYLIST);
		RuntimeFormInterface slotForm = state.getRuntimeForm(serialSlot, null);
		DataBean serialFocus = slotForm.getFocus();

		//Just process if serial numbers present to process

		if(serialFocus!=null){
			Double qtySelected = Double.parseDouble((String)detailForm.getFormWidgetByName("QTYSELECTED").getValue());
			Double qtyToAdjust = ((BigDecimal)detailForm.getFormWidgetByName("QTY").getValue()).doubleValue();
			
			if (!qtyToAdjust.equals(qtySelected)){
				String[] params = new String[2];
				params[0] = detailForm.getFormWidgetByName("QTYSELECTED").getLabel(RuntimeFormWidgetInterface.LABEL_LABEL, null);
				params[0] += qtySelected.toString();
				params[1] = detailForm.getFormWidgetByName("QTY").getLabel(RuntimeFormWidgetInterface.LABEL_LABEL, null);
				params[1] += qtyToAdjust.toString();
				throw new UserException("WMEXP_ADJQTY_MISMATCH", params);
			}
		}
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
		
	private boolean validateLoc(String loc, UnitOfWorkBean uowb){
		String query = "wm_location.LOC='"+loc+"'";
		Query qry = new Query("wm_location", query, null);
		try{
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			if(list.size()<1){
				return false;
			}else{
				return true;
			}	
		}catch(EpiDataException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validateLot(String lot, UnitOfWorkBean uowb){
		String query = "wm_lot_ib.LOT='"+lot+"'";
		Query qry = new Query("wm_lot_ib", query, null);
		try{
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			if(list.size()<1){
				return false;
			}else{
				return true;
			}
		}catch(EpiDataException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void validateQty(String qty, String lot, String loc, String id, String storerkey, String sku, UnitOfWorkBean uowb) throws UserException{
        /********************************************************************************
         * Programmer:	Stuart Matthews
         * Created:		10/16/2008
         * Purpose:		Validate decremented quantity does not exceed available quantity (Machine2049490-SDIS05140)
         * 
         * If the qty is decreased then using the following Rules for adjustemnt of Allocated product
         * If the qty > (totol-allocated-preallocated-picked) in lot level then do not permit decrease.
         * If the ID is blank, then decrease are not permitted if the QTY is >= Available quantity
         * If the FROMID <> blank, DO NOT permit decrease of any product if the ID is allocated. 
         **/
		double qtyAdj = Double.parseDouble(qty);
		//Negative adjustment validations
		if(qtyAdj<0){
			double origQty, qtyAlloc, qtyPreAlloc, qtyPicked, qtyAvailable; 
			String[] params = new String[4];
			//Check against all inventory in lot
			String qryStr = "wm_lot_ib.LOT='"+lot+"' AND wm_lot_ib.STORERKEY='"+storerkey+"' AND wm_lot_ib.SKU='"+sku+"'";
			Query qry = new Query("wm_lot_ib", qryStr, null);
			BioCollectionBean bcBean = uowb.getBioCollectionBean(qry);
			BioBean element;
			try{
				element = (BioBean)bcBean.elementAt(0);
				origQty = Double.parseDouble(element.get("QTY").toString());
				qtyAlloc = Double.parseDouble(element.get("QTYALLOCATED").toString());
				qtyPreAlloc = Double.parseDouble(element.get("QTYPREALLOCATED").toString());
				qtyPicked = Double.parseDouble(element.get("QTYPICKED").toString());
				qtyAvailable = origQty-(qtyAlloc+qtyPreAlloc+qtyPicked);
				if(-qtyAdj > qtyAvailable){
					params[0] = qty;
					params[1] = ""+qtyAvailable;
					throw new UserException("WMEXP_ADJ_UNAVAILABLE1", params);
				}
			}catch(EpiDataException e){
				e.printStackTrace();
			}
			qryStr = "wm_lotxlocxid.LOT='"+lot+"' AND wm_lotxlocxid.LOC='"+loc+"' AND wm_lotxlocxid.ID='"+id+"' AND wm_lotxlocxid.STORERKEY='"+storerkey+"' AND wm_lotxlocxid.SKU='"+sku+"'";
			Query qry2 = new Query("wm_lotxlocxid", qryStr, null);
			bcBean = uowb.getBioCollectionBean(qry2);
			try{
				element = (BioBean)bcBean.elementAt(0);
				origQty = Double.parseDouble(element.get("QTY").toString());
				qtyAlloc = Double.parseDouble(element.get("QTYALLOCATED").toString());
				qtyPicked = Double.parseDouble(element.get("QTYPICKED").toString());
				qtyAvailable = origQty-(qtyAlloc+qtyPicked);
				if(id.matches("\\s*")){
					//No LPN
					if(-qtyAdj > qtyAvailable){
						params[0] = qty;
						params[1] = ""+qtyAvailable;
						params[2] = loc;
						throw new UserException("WMEXP_ADJ_UNAVAILABLE2", params);
					}
				}else{
					if(-qtyAdj > qtyAvailable){
						throw new UserException("WMEXP_ADJ_UNAVAILABLE3", params);
					}
					if(qtyAdj+origQty < 0){
						params[0] = qty;
						params[1] = ""+origQty;
						params[2] = loc;
						params[3] = id;
						throw new UserException("WMEXP_ADJ_UNAVAILABLE4", params);
					}
				}
			}catch(EpiDataException e){
				e.printStackTrace();
			}
		}
	}
}