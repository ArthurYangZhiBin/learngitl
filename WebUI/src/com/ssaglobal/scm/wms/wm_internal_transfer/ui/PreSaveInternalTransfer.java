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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.*;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

public class PreSaveInternalTransfer extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveInternalTransfer.class);
	protected String[] param = new String[4];
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PreSaveInternalTransfer",100L);
		String toOrFrom = null;

		ArrayList subSlots = (ArrayList) getParameter("subSlots");
		ArrayList toOrFromList = (ArrayList) getParameter("toOrFrom");
		
		String skuTable = "SKU";
		String lotTable = "LOT";
		String locTable = "LOC";
		String packTable = "PACK";
		String toQty= null;
		String fromQty= null;
		String convertedQty = null;			//7268
		String fromSku =null; 				
		String toSku =null;	
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewDetail = false;
	   	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	
		if (!(detailForm.getName().equals("wm_internal_transfer_detail_detail_view"))){
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_internal_transfer_detail_toggle_slot"), "Detail"  );	
	    }		
	
		if(!(detailForm.getName().equals("Blank"))){	
			for(int i = 0; i < subSlots.size(); i++){
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Slot Name: " +subSlots.get(i),100L);
				RuntimeFormInterface subSlotForm= getSubSlotForm(state, shellForm, subSlots.get(i).toString());
				DataBean slotFocus = subSlotForm.getFocus();
				isNewDetail = slotFocus.isTempBio();

				//get detail form
				if(isNewDetail){
					slotFocus= (QBEBioBean)slotFocus;
				}else{
					slotFocus= (BioBean)slotFocus;
				}

				String toFrom = (String)toOrFromList.get(i);
				if(toFrom.equals("FROM")){
					toOrFrom ="FROM";
				}else if(toFrom.equals("TO")){
					toOrFrom ="TO";
				}

				String storerKey = toOrFrom +"STORERKEY";
				String sku= toOrFrom +"SKU";
				String lot= toOrFrom +"LOT";
				String loc= toOrFrom +"LOC";
				String packKey= toOrFrom +"PACKKEY";
				String qty= toOrFrom +"QTY";
				String uom= toOrFrom+"UOM";				//7268

				String skuVal = slotFocus.getValue(sku).toString().toUpperCase();
				String lotVal = "";
				if(slotFocus.getValue(lot)!=null)	{	// If condition added to check the null as to lot is not an required field. ISSUE SDIS#5659
					lotVal = slotFocus.getValue(lot).toString().toUpperCase();
				}
				String locVal = slotFocus.getValue(loc).toString().toUpperCase();
				String packVal = slotFocus.getValue(packKey).toString().toUpperCase();
				String qtyVal = subSlotForm.getFormWidgetByName(qty).getValue().toString();	//7268
				String uomVal = slotFocus.getValue(uom).toString();					//7268

				String owner = ((String)headerFocus.getValue(storerKey)).toUpperCase();
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Storer Val: "+owner,100L);			
				validateItem(skuVal, owner, skuTable);
				validateTable(locVal, locTable, "loc");
				validateTable(packVal, packTable, "packkey");

				slotFocus.setValue(sku, skuVal);
				slotFocus.setValue(loc, locVal);
				slotFocus.setValue(packKey, packVal);

				if(!lot.equals("TOLOT")){
					validateTable(lotVal, lotTable, "lot");
					checkIfLotIsValid(owner, skuVal, lotVal, lotTable);
				}else{
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","To Lot Value: " +lotVal,100L);
					if(lotVal != null && !(lotVal.equalsIgnoreCase("")))	{ // If condition added to check the null as to lot is not an required field. ISSUE SDIS#5659
						if((lotVal.matches("\\s+"))){
							//checkIfLotExists(owner, skuVal, lotTable);
							//throw new UserException("WMEXP_INVALID_ENTRY", new Object[1]);
							UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
							String lottableValidationKey = getFieldValueFromSku(owner, skuVal, "LOTTABLEVALIDATIONKEY", uowb);
							BioCollectionBean lottableValidationCollection = getLottableValidation(lottableValidationKey, uowb);
							checkLottableValidations(lottableValidationCollection, owner, skuVal, slotFocus, state);
						}else{
							validateTable(lotVal, lotTable, "lot");

							//to check if lot exists in lotattribute for combination of toOwner, toSku and toLot
							checkIfLotIsValid(owner, skuVal, lotVal, "LOTATTRIBUTE");
							//checkIfLotIsValid(owner, skuVal, lotVal, lotTable);
						}
					}
				}
				convertedQty = UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uomVal, UOMMappingUtil.UOM_EA,qtyVal, packVal, state,UOMMappingUtil.uowNull,true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				if(qty.equals("TOQTY")){
					toQty = convertedQty;			//7268
				}else if(qty.equals("FROMQTY")){
					fromQty = convertedQty;			//7268
				}else{
					throw new UserException("WMEXP_NULL", new Object[1]);
				}

				slotFocus.setValue(sku, skuVal);
				slotFocus.setValue(loc, locVal);
				slotFocus.setValue(packKey, packVal);
				slotFocus.setValue(lot, lotVal);
				slotFocus.setValue(qty, convertedQty);			//7268
				headerFocus.setValue(storerKey, owner);	
				

				if(toFrom.equals("FROM"))
				{fromSku=skuVal;}
				else if(toFrom.equals("TO"))
				{toSku =skuVal;}

			}	
			// Skip qty validation if fromsku <> tosku 
			if(fromSku.equals(toSku)) 
				validateQty(fromQty, toQty);
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PreSaveInternalTransfer",100L);
		return RET_CONTINUE;
	}

	private void checkLottableValidations(BioCollectionBean lottableValidationCollection, String owner, String item, DataBean detailFocus, StateInterface state)throws EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing checkLottableValidations",100L);
		Object[] oLottable = new Object[10];
		Date[] lotFormattedDate = new Date[2]; 
		lotFormattedDate[0] = null;
		lotFormattedDate[1] = null;
		String[] lottable = new String[10];
		String[] lotMand = new String[10];
		String type = "";
		int index;
		String lottablValidationKey	= lottableValidationCollection.get("0").getValue("LOTTABLEVALIDATIONKEY").toString();
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Lottable Validation Key: " +lottablValidationKey ,100L);
		
		//Retrieve lottable objects from focus
		if (detailFocus instanceof QBEBioBean){
			QBEBioBean qbeDetailFocus = (QBEBioBean)detailFocus;
			for(index=0; index<10; index++){
				oLottable[index] = qbeDetailFocus.getValue(getLotString(index+1, true));
			}
		}else if(detailFocus instanceof BioBean){
			BioBean bioDetailFocus = (BioBean)detailFocus;
			for(index=0; index<10; index++){
				oLottable[index] = bioDetailFocus.getValue(getLotString(index+1, true));
			}
		}
		
		//Retrieve lottables on rf receipt manditory from focus
		for(index=0; index<10; index++){
			lotMand[index] = lottableValidationCollection.get("0").getValue(getLotString(index+1, true)+"ONRFRECEIPTMANDATORY").toString();			
		}

		//Validate if lottable is manditory, lottable is not empty
		Array params = new Array(); 
		boolean expfound = false;
		for(index=0; index<10; index++){
			if((lotMand[index].equalsIgnoreCase("1")) && ((oLottable[index] == null) || (oLottable[index].toString().trim().equals("")))){
				expfound = true;
				params.add(getLotString(index+1, false));
			}
		}

		//Encountered missing required lottable
		if(expfound){
			String missingLottables="";
			for (int i=1; i<=params.size() ; i++){
				missingLottables = i == params.size() ? missingLottables+params.get(i)+" " :missingLottables+params.get(i)+", ";
			}
			UserInterface user = state.getUser();
	        LocaleInterface locale = user.getLocale();
	        String strMsg = "";
			String[] ErrorParam = new String[4];
			//ErrorParem[0]= receiptLineNumber;
			ErrorParam[1] = owner;
			ErrorParam[2] = item;
			strMsg = (type.equalsIgnoreCase("4") || type.equalsIgnoreCase("5")) ? getTextMessage("WMEXP_LOTTABLE_LPNDETAIL_REQ",ErrorParam,locale) : getTextMessage("WMEXP_LOTTABLE_REQUIRED",ErrorParam,locale);
			strMsg = missingLottables + strMsg;
			throw new UserException(strMsg, new Object[]{});
		}	
			
		if(detailFocus.isTempBio()){
			detailFocus = (QBEBioBean)detailFocus;
		}else{
			detailFocus = (DataBean)detailFocus;
		}

		//Set lottables to bio
//		DateFormat oaDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
//		SimpleDateFormat ejbFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		for(index=0; index<10; index++){
			if((index+1)==4 || (index+1)==5){
				//Lottable04 and lottable05
//				try{
//					if (oLottable[index] != null)
//					{
//						lotFormattedDate[index-3] = oaDateFormat.parse(oLottable[index].toString());
//						detailFocus.setValue(getLotString(index+1, true), ejbFormat.format(lotFormattedDate[index-3]));
//					}
//					else
//						lotFormattedDate[index-3] = null;
//				}catch(ParseException e1){
//					e1.printStackTrace();
//				}
			}else{
				if (oLottable[index] != null && !oLottable[index].toString().matches("\\s*")){
					lottable[index] = oLottable[index].toString().trim();			
					//detailFocus.setValue(getLotString(index+1, true), lottable[index]);
				}else{
					lottable[index] = " ";
				}
				detailFocus.setValue(getLotString(index+1, true), lottable[index]);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing checkLottableValidations",100L);
	}
		
	private String getLotString(int index, boolean isUpper){
		String temp = index<10 ? ("Lottable0" + Integer.toString(index)) : ("Lottable" + Integer.toString(index));
		if(isUpper)
			return temp.toUpperCase();
		else
			return temp;
	}
	
//	private BioCollectionBean getlottableValidationDetail(String LottableValidationKey, ActionContext context)throws EpiException {
//		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing getlottableValidationDetail "  ,100L);
//		BioCollectionBean listCollection = null;
//		String sQueryString = "(wm_lottablevalidationdetail.LOTTABLEVALIDATIONKEY = '"+LottableValidationKey+"')";
//		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Query: " +sQueryString  ,100L);
//		Query BioQuery = new Query("wm_lottablevalidationdetail",sQueryString,null);
//		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
//		listCollection = uow.getBioCollectionBean(BioQuery);
//		
//		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting getlottableValidationDetail "  ,100L);
//		if (listCollection.size()!= 0){
//			return listCollection;
//		}else{
//			return null;
//		}	
//	}

	private BioCollectionBean getLottableValidation(String lottableValidationKey, UnitOfWorkBean uowb)throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing getlottableValidation "  ,100L);
		String sQueryString = "(wm_lottablevalidation.LOTTABLEVALIDATIONKEY = '" + lottableValidationKey + "')";
		Query BioQuery = new Query("wm_lottablevalidation",sQueryString,null);
		BioCollectionBean listCollection = uowb.getBioCollectionBean(BioQuery);
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting getlottableValidation "  ,100L);
		if (listCollection.size()!= 0){
			return listCollection;
		}else{
			return null;
		}
	}
	
	private String getFieldValueFromSku(String Owner, String sku, String fieldName, UnitOfWorkBean uowb) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing getFieldValueFromSku "  ,100L);
		String qryString = "(sku.STORERKEY = '"+Owner+"' AND  sku.SKU = '"+sku+"')";
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Query: "+qryString,100L);
		Query qry = new Query("sku", qryString, null);
   	   	BioCollectionBean listCollection = uowb.getBioCollectionBean(qry);
   	   	if (listCollection.size()!= 0){
   	   		return listCollection.get("0").get(fieldName).toString();
   	   	}else{
      		return "";
   	   	}
	}	

	private void validateQty(String fromQty, String toQty) throws UserException {
		double from = Double.parseDouble(LocaleUtil.resetQtyToDecimalForBackend(fromQty));//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		double to = Double.parseDouble(LocaleUtil.resetQtyToDecimalForBackend(toQty));//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		int comp= Double.compare(to, from);
		if(comp > 0){
			throw new UserException("WMEXP_TOQTYFROMQTY_TRANSFERDETAIL_1", new Object[1]);
		}
	}
	
	private RuntimeFormInterface getSubSlotForm(StateInterface state, RuntimeFormInterface shellForm, String subSlot) {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing getSubSlotForm"  ,100L);
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		RuntimeFormInterface subSlotForm= null;

		if (detailForm.getName().equals("wm_internal_transfer_detail_detail_view")){
			subSlotForm = state.getRuntimeForm(detailForm.getSubSlot(subSlot), null );
		}else{
			//existing record 
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_internal_transfer_detail_toggle_slot"), "Detail");
			if(detailForm.getName().equals("wms_tbgrp_shell")){
				detailForm = state.getRuntimeForm(detailForm.getSubSlot("tbgrp_slot"), "tab 0");
			}
			subSlotForm = state.getRuntimeForm(detailForm.getSubSlot(subSlot), null );
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting getSubSlotForm "  ,100L);
		return subSlotForm;
	}

	private void checkIfLotIsValid (String owner, String skuVal, String lotVal, String lotTable) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing checkIfLotIsValid "  ,100L);
		String sql = "select * from " +lotTable +" where STORERKEY" +"='"+owner+"'" +" and sku" +"='"+skuVal+"'"+" and lot" +"='"+lotVal+"'";
		EXEDataObject dataObject;
		try {
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()<= 0){
				param[0]= owner;
				param[1]= skuVal;
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing exception- Invalid Lot "  ,100L);
				throw new UserException("WMEXP_INV_LOT_COMB", param);
			}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting checkIfLotIsValid "  ,100L);
	}

	private void validateTable(String field, String table, String attribute) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing validateTable "  ,100L);
		String sql ="select * from " +table +" where " +attribute +"='"+field+"'";
		EXEDataObject dataObject;
		try {
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()<= 0){
				param[0]= attribute;
				param[1]= field;
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing exception- Invalid data "  ,100L);
				throw new UserException("WMEXP_INVALID_ENTRY", param);
			}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting validateTable "  ,100L);
	}

	private void validateItem(String sku, String owner, String skuTable) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing validateItem "  ,100L);
		String sql ="select * from " +skuTable +" where sku" +"='"+sku+"'" +" and storerkey" +"='"+owner+"'";
		EXEDataObject dataObject;
		try {
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()<= 0){
				param[0]= sku;
				param[1]= owner;
				param[2]= "Item";
				param[3]= "Owner";
				throw new UserException("WMEXP_SKU_OWNER_COMB", param);
			}
		} catch (DPException e) {
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting validateItem "  ,100L);
	}
	

}