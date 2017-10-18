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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;

public class WOMCombineAction extends ActionExtensionBase{
	private final static String IS_ALL = "isAll";
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String WOGROUP_KEY = "GROUPKEY";
	private final static String WOGROUP_ID = "GROUPID";
	private final static String STATUS = "STATUS";
	private final static String RELEASED = "ISRELEASED";
	private final static String COMBINED = "ISCOMBINED";
	private final static String WOGROUP_TABLE = "wm_jm_wogroup";
	private final static String WO_TABLE = "wm_workorder";
	private final static String WOGROUP = "WOGROUP";
	private final static String ERROR_MESSAGE_NO_LIST = "WMEXP_NO_ACCESS_VIA_FORM_CONTEXT";
	private final static String ERROR_MESSAGE_EMPTY_BC = "WMEXP_ALL_COMBINED";
	private final static String ERROR_MESSAGE_NOTHING_SELECTED = "WMEXP_NONE_SELECTED";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		boolean isAll = getParameterBoolean(IS_ALL);
		HashSet groupIds = new HashSet();
		StateInterface state = context.getState(); 
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		if(header.isListForm()){
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			if(isAll){
				String queryString = WO_TABLE+"."+COMBINED+"!='1' or "+WO_TABLE+"."+COMBINED+" is null";
				Query query = new Query(WO_TABLE, queryString, null);
				BioCollectionBean combineList = uowb.getBioCollectionBean(query);
				if(combineList.size()<1){
					throw new FormException(ERROR_MESSAGE_EMPTY_BC, null);
				}
				String newKey = createNewWorkOrderGroup(uowb);
				for(int index=0; index<combineList.size(); index++){
					BioBean bio = (BioBean)combineList.get(""+index);
					groupIds.add(bio.get("GROUPID").toString());
					bio.set(WOGROUP_ID, newKey);
					bio.set(COMBINED, "1");
					uowb.saveUOW(true);
				}
			}else{
				RuntimeListFormInterface headerList = (RuntimeListFormInterface)header;
				ArrayList selected = headerList.getSelectedItems();
				if(selected==null){
					throw new FormException(ERROR_MESSAGE_NOTHING_SELECTED, null);
				}
				String newKey = null;
				for(int index=0; index<selected.size(); index++){
					BioBean bio = (BioBean)selected.get(index);
					Object combine = bio.get(COMBINED);
					if(combine!=null){
						if(!combine.toString().equals("1")){
							groupIds.add(bio.get("GROUPID").toString());
							if(newKey==null){
								newKey = createNewWorkOrderGroup(uowb);
							}
							bio.set(WOGROUP_ID, newKey);
							bio.set(COMBINED, "1");
							uowb.saveUOW(true);
						}
					}else{
						groupIds.add(bio.get("GROUPID").toString());
						if(newKey==null){
							newKey = createNewWorkOrderGroup(uowb);
						}
						bio.set(WOGROUP_ID, newKey);
						bio.set(COMBINED, "1");
						uowb.saveUOW(true);
					}
				}
			}
			//for defect 134585
			this.deleteGroupIds(uowb, groupIds);
			//end *************

		}else{
			throw new FormException(ERROR_MESSAGE_NO_LIST, null);
		}
		return RET_CONTINUE;
	}
	
	private String createNewWorkOrderGroup(UnitOfWorkBean uowb) throws EpiException{
		KeyGenBioWrapper wrapper = new KeyGenBioWrapper();
		String newKey = wrapper.getKey(WOGROUP);
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		double newKeyNumber = Double.parseDouble(newKey);
		QBEBioBean newWOGroup = uowb.getQBEBioWithDefaults(WOGROUP_TABLE);
		newWOGroup.set(WOGROUP_ID, newKey);
		newWOGroup.set(WOGROUP_KEY, nf.format(newKeyNumber));
		newWOGroup.set(STATUS, "0");
		newWOGroup.set(RELEASED, "0");
		uowb.saveUOW(true);
		return newKey;
	}
	
	
	private void deleteGroupIds(UnitOfWorkBean uowb, HashSet groupIds) throws EpiException{
		int groupId = 0;
		Iterator iterator = groupIds.iterator();
		while(iterator.hasNext()){
			groupId = Integer.parseInt(iterator.next().toString());
			Query query = new Query("wm_workorder", "wm_workorder.GROUPID="+groupId, null);
			BioCollectionBean collection = uowb.getBioCollectionBean(query);
			int size = collection.size();
			if(size == 0){
				String qry = " DELETE FROM WOGROUP WHERE GROUPID="+groupId;
				WmsWebuiValidationDeleteImpl.delete(qry);
			}
			
		}

	}

}