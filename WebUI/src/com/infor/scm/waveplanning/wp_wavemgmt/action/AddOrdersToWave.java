/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wavemgmt.action;

import java.util.ArrayList;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.wp_wave.action.WPWaveAddOrdersToWave;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

/**
 * TODO Document AddOrdersToWave class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class AddOrdersToWave extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddOrdersToWave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Executing AddOrdersToWave",100L);		
		StateInterface state = context.getState();
		
		
		RuntimeFormInterface toolBar = state.getCurrentRuntimeForm();
		String toolBarName = toolBar.getName();
		RuntimeListFormInterface ordersListForm = (RuntimeListFormInterface)toolBar.getParentForm(state);
		String listFormName = ordersListForm.getName();
		if(ordersListForm == null){
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","form is null... exiting.",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		java.util.ArrayList ordersSelected = ordersListForm.getAllSelectedItems();
				
		if(ordersSelected == null || ordersSelected.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","No orders Found...",100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_NO_ORDERS_TO_ADD_TO_WAVE",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
		}
	
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String waveKey = userContext.get("WAVEMGMT_WAVEKEY").toString();
	
		
		
		StringBuffer orderKeys = new StringBuffer();
		int size =  ordersSelected.size();
		BioBean selectedBB = null;

		for(int i = 0; i <size; i++){
				selectedBB = (BioBean)ordersSelected.get(i);
				if(i< size-1 ){
					orderKeys.append(selectedBB.get("ORDERKEY").toString()+",");
				}else{
					orderKeys.append(selectedBB.get("ORDERKEY").toString());
				}
		}

		try { 
			//call backend to do insertion
			WMWaveActionInterface action = WaveActionFactory.getWaveAction(WPConstants.CREATE_WAVE);
			WaveInputObj wpInput = new WaveInputObj();
			
			wpInput.setOrderKeys(orderKeys.toString());
			wpInput.setWaveKey(waveKey); 
			action.doWaveAction(wpInput);
		}
		catch(WebuiException e){
			e.printStackTrace();
			String [] ex = new String[1];
			ex[0]=e.getMessage();
			throw new UserException("WPEXE_APP_ERROR", ex);			
			
		}				
		
		return RET_CONTINUE;	
	}
}
