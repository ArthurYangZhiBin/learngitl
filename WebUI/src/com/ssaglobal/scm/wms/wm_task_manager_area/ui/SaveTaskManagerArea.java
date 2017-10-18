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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;



import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_routing.ui.GetSKUDetailOnChange;

public class SaveTaskManagerArea extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveTaskManagerArea.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {

		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing SaveTaskManagerArea",100L);
		String shellSlot2 = "list_slot_2";
		
		StateInterface state = context.getState();
		 
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
	 
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);		//HC
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

		SlotInterface tabGroupSlot = getTabGroupSlot(state);
		//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of shellForm is " +shellForm.getName() +"\n\n",100L);
		//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of detailForm is " +detailForm.getName() +"\n\n",100L);
		//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of tabGroup is " +tabGroupSlot.getName() +"\n\n",100L);
		
		RuntimeFormInterface areaDetailForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
		RuntimeFormInterface voicePropForm = state.getRuntimeForm(tabGroupSlot, "tab 1");
		
		//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of AreaDetailForm is " +areaDetailForm.getName() +"\n\n",100L);
		//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of voicePropForm is " +voicePropForm.getName() +"\n\n",100L);
		
		DataBean focusArea = areaDetailForm.getFocus();
		DataBean focus = voicePropForm.getFocus();

		
		
		if(focusArea.getValue("AREAKEY").toString() != null)
		{
			
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
        	Array params = new Array(); 
        	
        	String areaKey = focusArea.getValue("AREAKEY").toString();
        	String allowVoice = focus.getValue("ALLOWVOICE").toString();   
        	String autoAssign = "1";    	
        	String maxNumWorkID = focus.getValue("MAXNUMWORKIDS").toString();
        	String skipAll = "1";
        	String skipLocAllowed = focus.getValue("SKIPLOCALLOWED").toString();
        	String repickShips = focus.getValue("REPICKSKIPS").toString();
        	String speakDesc = focus.getValue("SPEAKDESCRIPTION").toString();
        	String pickPrompt = focus.getValue("PICKPROMPT").toString();
        	String speakWordID = focus.getValue("SPEAKWORKID").toString();
        	String signOffAllowed = focus.getValue("SIGNOFFALLOWED").toString();
        	String delivery = focus.getValue("DELIVERY").toString();
        	String qtyVerif = "1";
        	String workIDLength = "-1";
        	String noContTrack = "0"; //NOCONTAINERTRACKING
        	String currentAisle = focus.getValue("CURRENTAISLE").toString();
        	
        	String currentSlot = focus.getValue("CURRENTSLOT").toString();
        	String verifyPutQty = focus.getValue("VERIFYPUTQTY").toString();
        	
   
        
              
        	/*
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****areaKey " +areaKey +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****allowVoice " +allowVoice +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****autoAssign " +autoAssign +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****maxNumWorkID " +maxNumWorkID +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****skipAll " +skipAll +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****skipLocAllowed " +skipLocAllowed +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****repickShips " +repickShips +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****speakDesc " +speakDesc +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****pickPrompt " +pickPrompt +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****speakWordID " +speakWordID +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****signOffAllowed " +signOffAllowed +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****delivery " +delivery +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****workIDLength " +workIDLength +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****noContTrack " +noContTrack +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****currentAisle " +currentAisle +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****currentSlot " +currentSlot +"\n\n",100L);
        	_log.debug("LOG_SYSTEM_OUT","\n\n ****verifyPutQty " +verifyPutQty +"\n\n",100L);
        	*/
        	
        	
        	params.add(new TextData(areaKey));
        	params.add(new TextData(allowVoice));        	        	
            params.add(new TextData(autoAssign)); //AUTOASSIGN
        	params.add(new TextData(maxNumWorkID));        	        	
        	params.add(new TextData(skipAll)); //SKIPAISLEALLOWED
        	params.add(new TextData(skipLocAllowed));
        	params.add(new TextData(repickShips));
        	params.add(new TextData(speakDesc));
        	params.add(new TextData(pickPrompt));        	        	
        	params.add(new TextData(speakWordID));        	        	
        	params.add(new TextData(signOffAllowed));        	        	
        	params.add(new TextData(delivery));        	        	        	
        	params.add(new TextData(qtyVerif));  //QUANTITYVERIFICATION        	        	
        	params.add(new TextData(workIDLength)); //WORKIDLENGTH                	
        	params.add(new TextData(noContTrack));     	
        	params.add(new TextData(currentAisle));        	
        	params.add(new TextData(currentSlot));        	        
        	params.add(new TextData(verifyPutQty));
        	
         	actionProperties.setProcedureParameters(params);
        	actionProperties.setProcedureName("NSPUPDATEAREA");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException("System_Error",new Object[1] );	
		}
        	
		
		BioBean areaDetailBio = null;
		
		if (focusArea.isTempBio()) {//it is for insert header	    
	    QBEBioBean areaDetailQBE =(QBEBioBean)focusArea;

	    String putaway= areaDetailQBE.get("PUTAWAYZONE").toString();
	    
	    areaDetailQBE.set("AREAKEY", areaKey);
	    areaDetailQBE.set("PUTAWAYZONE",putaway);

	    //_log.debug("LOG_SYSTEM_OUT","######after ******",100L);

	    //_log.debug("LOG_SYSTEM_OUT","\n\nInserted",100L);
	    
		} else //it is for update header
		{
	    areaDetailBio = (BioBean)focusArea;
	    String putawayBio= areaDetailBio.get("PUTAWAYZONE").toString();
	    
	    areaDetailBio.set("AREAKEY", areaKey);
	    areaDetailBio.set("PUTAWAYZONE",putawayBio);	    
		} 
		
		
			
		}
		

		//save area detail

	/*
		BioBean areaDetailBioBean = null;
		
		if (focusArea.isTempBio()) {//it is for insert header
	    _log.debug("LOG_SYSTEM_OUT","inserting Area Detail ******",100L);
	    
	    
	    QBEBioBean areaDetailQBE =(QBEBioBean)focusArea;
	    //BioBean areaBioBean = uow.getNewBio(areaDetailQBE);
	    //areaBioBean.delete();
	    areaDetailQBE.set("AREAKEY", areaKey);
	    _log.debug("LOG_SYSTEM_OUT","######before******" ,100L);
	    QBEBioBean qbeArea = (QBEBioBean)areaDetailQBE.get("AREADATA");
	    _log.debug("LOG_SYSTEM_OUT","######after ******",100L);
	    uow.getNewBio(qbeArea).delete();
	    
	    //BioBean bio =(BioBean)areaDetailBioBean.get("AREADATA");
	    //bio.delete();
	    areaDetailBioBean = uow.getNewBio((QBEBioBean)focusArea);
	    _log.debug("LOG_SYSTEM_OUT","\n\nInserted",100L);
	    
		} else {//it is for update header
	    _log.debug("LOG_SYSTEM_OUT","Area Detail ******",100L);
	    areaDetailBioBean = (BioBean)focusArea;
		} 
	 */	
		//_log.debug("LOG_SYSTEM_OUT","Done.....\n",100L);
		uow.saveUOW(true);
		uow.clearState();
		result.setFocus(focus);
	
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting SaveTaskManagerArea",100L);
		return RET_CONTINUE;
	}

	private SlotInterface getTabGroupSlot(StateInterface state) {
		// TODO Auto-generated method stub

		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		return tabGroupSlot;
	}	 		
	
}
