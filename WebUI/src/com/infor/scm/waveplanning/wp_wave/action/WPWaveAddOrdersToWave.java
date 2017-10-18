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
package com.infor.scm.waveplanning.wp_wave.action;
import java.util.ArrayList;

import java.util.Hashtable;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataRefOnUnsavedBioException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_query_builder.action.WPQueryBuilderAddToExistingWave;
import com.infor.scm.waveplanning.wp_wave.util.WPWaveUtil;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;


public class WPWaveAddOrdersToWave extends ActionExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWaveAddOrdersToWave.class);
	public static final String SESSION_KEY_WAVE = "wp.wave.session.key.wave";
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Executing WPWaveAddOrdersToWave",100L);		
		StateInterface state = context.getState();
		RuntimeListFormInterface ordersListForm = null;
		String listFormName = ((ModalActionContext)context).getSourceForm().getName();
		if("wm_wp_query_builder_shell_ordersl Toolbar".equalsIgnoreCase(listFormName)){
			ordersListForm = (RuntimeListFormInterface)WPFormUtil.findForm(((ModalActionContext)context).getSourceForm(),"","wm_wp_query_builder_shipment_orders_screen_1",state);	
		}
		if("wp_wavemgmt_wavemaint_orderheader_list_view_notinwave Toolbar".equalsIgnoreCase(listFormName)){
			ordersListForm = (RuntimeListFormInterface)WPFormUtil.findForm(((ModalActionContext)context).getSourceForm(),"","wp_wavemgmt_wavemaint_orderheader_list_view_notinwave",state);	
		}
		if(ordersListForm == null){
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","form is null... exiting.",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		java.util.ArrayList ordersSelected = ordersListForm.getAllSelectedItems();
//		BioCollectionBean ordersCollection = (BioCollectionBean)ordersListForm.getFocus();
				
			if(ordersSelected == null || ordersSelected.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","No orders Found...",100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_NO_ORDERS_TO_ADD_TO_WAVE",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
	
		//Get wave list form
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Getting Wave List Form...",100L);		
		RuntimeListFormInterface form = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_wave_list_1",state);		
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Got form:"+form,100L);

		if(form == null){
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","form is null... exiting.",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//Get selected waves
		ArrayList selectedWaves = form.getSelectedItems();
		
		//If nothing selected throw error
		if(selectedWaves == null || selectedWaves.size() == 0){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//If more than one record selected throw error
		if(selectedWaves.size() > 1){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_MORE_THAN_ONE_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		String waveKey = "";
		try {
			waveKey = ((DataBean)selectedWaves.get(0)).getValue("WAVEKEY").toString();
		} catch (NumberFormatException e) {
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Wave Key -"+((DataBean)selectedWaves.get(0)).getValue("WAVEKEY")+"- is not valid... exiting.",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}catch (NullPointerException e) {
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Wave Key is null... exiting.",100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		
		
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
		state.getRequest().getSession().setAttribute(SESSION_KEY_WAVE, waveKey+"");
		
		return RET_CONTINUE;	
	}
}
