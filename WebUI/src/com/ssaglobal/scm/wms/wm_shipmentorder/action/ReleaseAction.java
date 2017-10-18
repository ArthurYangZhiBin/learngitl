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
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.Array;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class ReleaseAction extends ListSelector{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(KeyGenBioWrapper.class);
	protected static final String internalCaller = "internalCall";
	
	private final static String TAB_GROUP_SLOT = "tbgrp_slot"; 
	private final static String FAC_TRANS_SHELL = "wm_list_shell_facilitytransfer";
	
	private final static String WAVE_TABLE = "wm_wave";
	private final static String WD_TABLE = "wm_wavedetail";
	private final static String TASK_TABLE = "wm_taskdetail";
	
	private final static String WAVE = "WAVEKEY";
	private final static String WD = "WAVEDETAILKEY";
	private final static String ORDER = "ORDERKEY";
	
	private final static String PROC_NAME = "NSPRELEASEWAVE";
	
	protected int execute(ModalActionContext context, ActionResult result) throws UserException, EpiDataException{
		//Executes stored procedure name:NSPRELEASEWAVE params:wavekey, success, err, dorelease
		StateInterface state = context.getState();
		RuntimeFormInterface shell = context.getSourceForm().getParentForm(state);
		run(state, shell);
		return RET_CONTINUE;
	}
	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException{
		//Executes stored procedure name:NSPRELEASEWAVE params:wavekey, success, err, dorelease
		StateInterface state = context.getState();
		RuntimeFormInterface shell = context.getSourceWidget().getForm().getParentForm(state);
		run(state, shell);
		return RET_CONTINUE;
	}
	
	private void run(StateInterface state, RuntimeFormInterface shell) throws UserException, EpiDataException{
		String listSlot1 = null;
		String tabZero = null;
		if(shell.getName().equals(FAC_TRANS_SHELL)){
			listSlot1 = "slot1";
			tabZero = "Tab0";
		}else{
			listSlot1 = "list_slot_1";
			tabZero = "tab 0";
		}
		RuntimeFormInterface headerForm = state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(listSlot1), null).getSubSlot(TAB_GROUP_SLOT), tabZero);
		DataBean focus = headerForm.getFocus();
		String waveKey=null, success="0", err="0", doRelease="Y";
		
		//Search wavedetail for existing records matching orderkey
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String queryString = WD_TABLE+"."+ORDER+"='"+focus.getValue(ORDER).toString()+"'";
		Query waveQry = new Query(WD_TABLE, queryString, null);
		BioCollectionBean waveList = uow.getBioCollectionBean(waveQry);
		int size = waveList.size();
		
		if(size>0){
			int index=0;
			boolean notFound=true;
			BioCollectionBean temp;
			//Verify if any existing record only hold the matching orderkey
			while(index<size && notFound){
				queryString = WD_TABLE+"."+WAVE+"='"+waveList.get(""+index).get(WAVE)+"'";
				waveQry = new Query(WD_TABLE, queryString, null);
				temp = uow.getBioCollectionBean(waveQry);
				if(temp.size()==1){
					//If current orderkey is individual for wavekey record search task detail with orderkey and wavekey
					queryString = TASK_TABLE+"."+ORDER+"='"+waveList.get(""+index).get(ORDER)+"' and wm_taskdetail."+WAVE+"='"+waveList.get(""+index).get(WAVE)+"'";
					waveQry = new Query(TASK_TABLE, queryString, null);
					temp = uow.getBioCollectionBean(waveQry);
					//If no matching records found in task detail, use wavekey
					if(temp.size()==0){
						notFound=false;						
						waveKey=waveList.get(""+index).get(WAVE).toString();
					}
				}
				index++;
			}	
		}

		//If found use wavekey else create new and use
		if(waveKey==null){
			
			//8890 - clear the uow to prevent update of the orderdetail,
			// else the first order detail record's openqty and adjustedqty fields
			// incorrectly get updated based on the current display value
			uow.clearState();  

			BioBean bio = (BioBean)focus;
			UnitOfWork unitOfWork = bio.getUnitOfWork();
			Bio waveBio = unitOfWork.createBio(WAVE_TABLE);

			//Generate new wavekey
			KeyGenBioWrapper wrapper = new KeyGenBioWrapper();
			String newWaveKey = wrapper.getKey(WAVE);
   			
   			//Set required values
			waveBio.set(WAVE, newWaveKey);
			unitOfWork.save();
			
			//create wave detail
			UnitOfWork unitOfWork2 = bio.getUnitOfWork();
			Bio waveDetailBio = unitOfWork2.createBio(WD_TABLE);
			waveDetailBio.set(ORDER, focus.getValue(ORDER));
			waveDetailBio.set(WD, "0000000001");
			waveDetailBio.set(WAVE, newWaveKey);
			unitOfWork2.save();
			
			//Pass new wave key for stored procedure
			waveKey=newWaveKey;
		}
		
		//Execute Stored Procedure
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		//Store parameters for stored procedure call
		params.add(new TextData(waveKey));
		params.add(new TextData(success));
		params.add(new TextData(err));
		params.add(new TextData(doRelease));
		//Set actionProperties for stored procedure call
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName(PROC_NAME);
		try{
			//Run stored procedure
			WmsWebuiActionsImpl.doAction(actionProperties);
		}catch (WebuiException e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}
}