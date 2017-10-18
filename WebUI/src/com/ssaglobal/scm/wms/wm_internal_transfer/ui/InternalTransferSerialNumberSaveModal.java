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
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.util.SessionUtil;

public class InternalTransferSerialNumberSaveModal extends ActionExtensionBase{
	private static String TO_QTY = "TOQTY";
	private static String QTYSELECT = "QTYSELECT";
	private static String TABLE_NAME = "TDSTEMP";
	private static String TRANSFER_KEY = "TRANSFERKEY";
	private static String TRANSFER_LINE_NUMBER = "TRANSFERLINENUMBER";
	private static String STORERKEY = "STORERKEY";
	private static String SKU = "SKU";
	private static String LOT = "LOT";
	private static String LOC = "LOC";
	private static String ID = "ID";
	private static String SERIALNUMBER = "SERIALNUMBER";
	private static String QTY = "QTY";
	private static String DATA2 = "DATA2";
	private static String DATA3 = "DATA3";
	private static String DATA4 = "DATA4";
	private static String DATA5 = "DATA5";
	private static String NETWEIGHT = "NETWEIGHT";
	private static String LIST_SLOT = "wm_internal_transfer_detail_serial_slot";
	private static String ERROR_MSG = "WMEXP_IT_SN_QTY_DIFFER";
	protected ArrayList selected;
	
	protected int execute(ActionContext context, ActionResult result) throws DPException, UserException{
		StateInterface state = context.getState();
		SlotInterface slot = state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(LIST_SLOT);
		RuntimeListFormInterface list = (RuntimeListFormInterface)state.getRuntimeForm(slot, null);
		selected = list.getAllSelectedItems();
		if(validateSelectedQty(state)){
			String transKey = (String)SessionUtil.getInteractionSessionAttribute(TRANSFER_KEY, state);
			String transLineNo = (String)SessionUtil.getInteractionSessionAttribute(TRANSFER_LINE_NUMBER, state);
			
			//Clear temp table
			WmsWebuiValidationDeleteImpl.delete("DELETE FROM TDSTEMP WHERE TRANSFERKEY='"+transKey+"' AND TRANSFERLINENUMBER='"+transLineNo+"'");

			//Build attribute string array
			String[] attr = new String[12];
			attr[0] = STORERKEY;
			attr[1] = SKU;
			attr[2] = LOT;
			attr[3] = ID;
			attr[4] = LOC;
			attr[5] = SERIALNUMBER;
			attr[6] = QTY;
			attr[7] = DATA2;
			attr[8] = DATA3;
			attr[9] = DATA4;
			attr[10] = DATA5;
			attr[11] = NETWEIGHT;
			
			//Set selected records to temp table
			for(int index=0; index<selected.size(); index++){
				BioBean bio = (BioBean)selected.get(index);
				String[] tdsTempStrings = new String[2]; 
				tdsTempStrings = buildInsertSubStrings(bio, attr);
				tdsTempStrings[0] = TRANSFER_KEY+", "+TRANSFER_LINE_NUMBER+", "+tdsTempStrings[0];
				tdsTempStrings[1] = "'"+transKey+"', '"+transLineNo+"', "+tdsTempStrings[1];
				WmsWebuiValidationInsertImpl.insert(buildInsertString(tdsTempStrings, TABLE_NAME));
			}			
		}else{
			throw new UserException(ERROR_MSG, new Object[]{});
		}		
		return RET_CONTINUE;
	}
	
	private boolean validateSelectedQty(StateInterface state){
		Double toQty = Double.parseDouble((String)SessionUtil.getInteractionSessionAttribute(TO_QTY, state));
		Double qty = 0.0;
		for(int index=0; index<selected.size(); index++){
			BioBean bio = (BioBean)selected.get(index);
			qty += Double.parseDouble(bio.get("QTY").toString());
		}
		if(toQty.compareTo(qty)==0){
			SessionUtil.setInteractionSessionAttribute(QTYSELECT, "0.00000", state);
			return true;
		}else{
			return false;
		}
	}
	
	protected String[] buildInsertSubStrings(BioBean bio, String[] attr){
		String[] rtrnStr = new String[2];
		rtrnStr[0] = "";
		rtrnStr[1] = "'";
		for(int index=0; index<attr.length; index++){
			rtrnStr[0] += attr[index];
			rtrnStr[1] += bio.get(attr[index]);
			if(index+1<attr.length){
				rtrnStr[0] += ", ";
				rtrnStr[1] += "', '";
			}else{
				rtrnStr[1] += "'";
			}
		}
		return rtrnStr;
	}
	
	protected String buildInsertString(String[] subStrs, String tableName){
		String qryString = "INSERT INTO "+tableName+" (";
		qryString += subStrs[0]+") VALUES ("+subStrs[1]+")";
		return qryString;
	}
}