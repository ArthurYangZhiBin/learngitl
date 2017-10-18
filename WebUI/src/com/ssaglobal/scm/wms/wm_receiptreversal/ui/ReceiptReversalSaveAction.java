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
package com.ssaglobal.scm.wms.wm_receiptreversal.ui;

//Import 3rd party packages and classes
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class ReceiptReversalSaveAction extends SaveAction{
	//Static form reference names
	private static final String SHELL_FORM = "wms_list_shell";
    private static final String SHELL_SLOT = "list_slot_1";
	private static final String SHELL_SLOT_2 = "list_slot_2";
	private static final String TOGGLE_SLOT = "wm_receiptreversaldetail_new_toggle_slot";
	private static final String LIST_TAB = "List";
	
    //Static table names
    private static final String RECEIPT_REVERSAL_DETAIL = "wm_receiptreversaldetail";
    
    //Static attribute names
    private static final String ADJ_KEY = "ADJUSTMENTKEY";
    private static final String ASN_KEY = "RECEIPTKEY";
    private static final String REASONCODE = "REASONCODE";
    private static final String ITEM = "SKU";
    private static final String PACK = "PACKKEY";
    private static final String ASN_LINE_NUM = "RECEIPTLINENUMBER"; 
    private static final String ADJ_LINE_NUM = "ADJUSTMENTLINENUMBER";
    private static final String UOM = "UOM";
    private static final String LOT = "LOT";
    private static final String ID = "ID";
    private static final String TEMPID = "TOID";
    private static final String QTY = "QTY";
    private static final String LOC = "LOC";
    private static final String TEMPLOC = "TOLOC";
    private static final String ITRN_KEY = "ITRNKEY";
    private static final String REFERENCE_KEY = "REFERENCEKEY";
    private static final String OWNER = "STORERKEY";
    
    //Static values
    private static final String UNRECEIVE = "UNRECEIVE";
    private static final String DETAIL_REL_LINK = "RECEIPTREVERSALDETAIL";
    private static final String DEC_FORMAT = "00000";
    
    //Static stored procedure names
    private static final String PROC_NAME = "NSP_RECEIPTREVERSAL";
    
    //Static error messages
    private static final String ERROR_MESSAGE_NO_SAVE = "";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		StateInterface state = context.getState();
       
        //Find header focus
		RuntimeFormInterface shell = state.getCurrentRuntimeForm();
		while(!shell.getName().equals(SHELL_FORM)){
			shell = shell.getParentForm(state);
		}
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		DataBean headerFocus = header.getFocus();
		
		//Get detail if available
		if(headerFocus.isTempBio()){
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			
			//Code for inserting new records
			BioBean headerBioBean = null;
			try {
				headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);				
			} catch (EpiException e1) {
				e1.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}			
			
			//Rebuild details
			String asnValue = header.getFormWidgetByName(ASN_KEY).getDisplayValue();
			if(asnValue != null)
				asnValue = asnValue.toUpperCase();
			String adjustment = header.getFormWidgetByName(ADJ_KEY).getDisplayValue();
			if(adjustment != null)
				adjustment = adjustment.toUpperCase();
			String owner = header.getFormWidgetByName(OWNER).getDisplayValue();
			if(owner != null)
				owner = owner.toUpperCase();

        
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array(); 
			params.add(new TextData(asnValue));	//from Qty
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName(PROC_NAME);

			Query query = new Query("wm_rr_temp", "", null); 
			BioCollectionBean saveThis = uowb.getBioCollectionBean(query);
			try {
				if(saveThis.size()<1){
					throw new FormException("WMEXP_MULTI_FAC_SETUP_MUST_CREATE_NEW_RECORD", null);
				}
			} catch (EpiDataException e1) {
				e1.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
			
			DecimalFormat decFormat = new DecimalFormat(DEC_FORMAT);
			
			RuntimeListFormInterface list = (RuntimeListFormInterface) state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT_2), null).getSubSlot(TOGGLE_SLOT), LIST_TAB);
			ArrayList selected = list.getAllSelectedItems();
			for(int index=0; index<selected.size(); index++){
				try {
					BioBean bio = (BioBean)selected.get(index);
					BioBean temp = uowb.getNewBio(RECEIPT_REVERSAL_DETAIL);
					temp.set(ADJ_KEY, adjustment);
					temp.set(REASONCODE, UNRECEIVE);
					temp.set(OWNER, owner);
					temp.set(ITEM, bio.get(ITEM));
					temp.set(PACK, bio.get(PACK));
					temp.set(ASN_KEY, bio.get(ASN_KEY));
					temp.set(ASN_LINE_NUM, bio.get(ASN_LINE_NUM));
					temp.set(UOM, bio.get(UOM));
					temp.set(LOT, bio.get(LOT));
					temp.set(ID, bio.get(TEMPID));
					BigDecimal holder = new BigDecimal("-"+bio.get(QTY).toString());
					temp.set(QTY, holder);
					temp.set(LOC, bio.get(TEMPLOC));
					String transKey = bio.get(ITRN_KEY).toString();
					temp.set(REFERENCE_KEY, transKey.trim());
					temp.set(ADJ_LINE_NUM, decFormat.format(index+1));
					headerBioBean.addToBioCollection(DETAIL_REL_LINK, temp);
				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			try{
				uowb.saveUOW(true);
				uowb.clearState();
			}catch (UnitOfWorkException e){
				Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				throwUserException(e, reasonCode, null);
			} catch (EpiException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		    result.setFocus(headerBioBean);
		}else{
			throw new FormException(ERROR_MESSAGE_NO_SAVE, null);
		}
		return RET_CONTINUE;
	}
}