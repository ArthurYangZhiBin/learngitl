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
package com.infor.scm.waveplanning.wp_query_builder.action;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wave.action.WPWaveAddOrdersToWave;
import com.infor.scm.waveplanning.wp_wave.util.WPWaveUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
//mport com.ssaglobal.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.*;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

public class WPQueryBuilderCreateWave extends ActionExtensionBase{
	public static final String SESSION_KEY_NEW_WAVE = "qry.builder.create.wave.session.key.wave";
	public static final String NEW_WAVE_DESC="WAVE_DESCRIPTION";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderCreateWave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException {	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Executing WPQueryBuilderCreateWave",100L);		
		StateInterface state = context.getState();
		Object wavePrefixObj = state.getRequest().getSession().getAttribute("WAVE_PREFIX");
		
		//Get Order List Form
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Getting Form wm_wp_query_builder_shipment_orders_screen...",100L);
		RuntimeListFormInterface form = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_shipment_orders_screen_1",state);
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Got form:"+form,100L);

		 
		//get wave desc
		String waveDesc=null;
		RuntimeNormalFormInterface totalForm = (RuntimeNormalFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_shipment_orders_totals_screen_1",state);
		if(totalForm != null){
			RuntimeFormWidgetInterface widget = totalForm.getFormWidgetByName("WAVEDESC");
			if(widget != null && !"".equalsIgnoreCase(widget.getDisplayValue())){
				waveDesc = widget.getDisplayValue();
//				state.getRequest().getSession().setAttribute(NEW_WAVE_DESC, waveDesc);
			}else{
				waveDesc = " ";
			}
		}
		
		
		//start validation if there is no orders
		WaveValidateInputObj validateInput = new WaveValidateInputObj();
		validateInput.setForm(form);
		WMWaveActionInterface action = WaveActionFactory.getWaveAction(WPConstants.CREATE_WAVE);
		if(action.validate(validateInput) != WPConstants.VALIDATION_PASSED){
			throw new UserException("WPEXP_CREATEWAVE_NO_ORDERS", new Object[0]);			
		}

		
		

		DataBean focus = form.getFocus(); 
		BioCollection orders = (BioCollection)focus;
		_log.info("LOG_INFO_EXTENSION_WAVECREATE","Focus Is Valid...",100L);
		
		//Create New Wave		
		WaveInputObj wpInput = new WaveInputObj();
		
		//create orderkey string
		StringBuffer orderSB = new StringBuffer();
		int size = validateInput.getOrderSize();
		String orderStr="";
		String errorMsg="";
		try{
			for(int i=0;i<size;i++){
				orderStr = orders.elementAt(i).getString("ORDERKEY");				
				if(i==size-1){
					orderSB.append(orderStr);
				}else{
					orderSB.append(orderStr+",");
				}
			}
			wpInput.setOrderKeys(orderSB.toString());
			wpInput.setWaveDesc(waveDesc);
			EXEDataObject edo = action.doWaveAction(wpInput);		 	
			TextData edoTD = (TextData)edo.getAttribValue(new TextData("WaveKey"));
			String waveKey = edoTD.getValue();

			
			EpnyUserContext userContext = context.getServiceManager().getUserContext();
			userContext.put("WAVEMGMT_WAVEKEY", waveKey);
			
			Query qry = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY='"+waveKey+"'", null);
	 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);			
			uowb.clearState();
			result.setFocus(resultCollection.get("0")); 
		}catch(EpiException e){
			e.printStackTrace();
			errorMsg = e.getMessage();
			throw new UserException(errorMsg, new Object[0]);
			
		}catch(WebuiException e){
			e.printStackTrace();
			errorMsg = e.getMessage();
			throw new UserException(errorMsg, new Object[0]);
		}
		
		
		
		
		
		
		
		
		
		
/*		try {
			if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000)) {
				newWaveKey = WPWaveUtil.createWave(state, "0", WavePlanningUtils.wmsName, null, WavePlanningConstants.WAVE_STATUSES_TOBECONFIRMED+"", WPUtil.getFacility(state.getRequest()), orders, wavePrefixObj);
			}
			else if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000)){
//				newWaveKey = WPWaveUtil.createWave(state, WPUtil.getFacility(state.getRequest()), WavePlanningUtils.wmsName, null, WavePlanningConstants.WAVE_STATUSES_TOBECONFIRMED+"", "0", orders, wavePrefixObj);
				newWaveKey = WPWaveUtil.createWave(state, WPUtil.getFacility(state.getRequest()), WavePlanningUtils.wmsName, waveDesc, WavePlanningConstants.WAVE_STATUSES_TOBECONFIRMED+"", "0", orders, wavePrefixObj);
			}
			state.getRequest().getSession().removeAttribute("WAVE_PREFIX");
			state.getRequest().getSession().setAttribute(WPWaveAddOrdersToWave.SESSION_KEY_WAVE, newWaveKey);
		} catch (NumberFormatException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while creating wave...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getMessage(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while creating wave...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (WavePlanningException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while creating wave...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getMessage(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
*/		
		return RET_CONTINUE;	
	}
}
