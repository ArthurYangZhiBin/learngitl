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
package com.infor.scm.waveplanning.wp_wave.util;

import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
//import com.ssaglobal.scm.waveplanning.mAndA.WaveNotification;


public class WPWaveUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWaveUtil.class);
	public WPWaveUtil()
    {
    }
	
	public static synchronized int addOrdersToWave(int waveId, ArrayList orderKeys, String whseid, String type, StateInterface state) throws WavePlanningException{
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Executing addOrdersToWave...",100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter waveId:"+waveId,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter orderKeys:"+orderKeys,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter whseid:"+whseid,100L);
		int ordersAdded = 0;
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		//Get wave details
		Query waveDetailQry = new Query("wm_wp_wavedetail","wm_wp_wavedetail.WAVEKEY = '"+waveId+"' AND wm_wp_wavedetail.WHSEID = '"+whseid+"'","");
		BioCollection waveDetails = uow.getBioCollectionBean(waveDetailQry);		
		try {
			if(waveDetails != null && waveDetails.size() > 0){
				//remove orders that are currently attached to this wave from the orderKeys list
				for(int i = 0; i < waveDetails.size(); i++){
					orderKeys.remove(waveDetails.elementAt(i).get("ORDERKEY"));
				}
			}
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Could not retrieve wave... exiting!",100L);
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);			
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
			throw new WavePlanningException();
		}
		
		//Add each order
		try {
			for(int i = 0; i < orderKeys.size(); i++){
				QBEBioBean waveDetail = uow.getQBEBioWithDefaults("wm_wp_wavedetail");
				waveDetail.set("WAVEKEY", new Integer(waveId));
				waveDetail.set("WHSEID", whseid);
				waveDetail.set("ORDERKEY", orderKeys.get(i));
				waveDetail.set("SEGMENTID", new Integer(1));
				waveDetail.set("DC_ID","1");							
				waveDetail.save();
				uow.saveUOW(true);
				ordersAdded++;
			}
		} catch (DataBeanException e) {
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Adding Orders To Wave... exiting!",100L);
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
			throw new WavePlanningException();
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Adding Orders To Wave... exiting!",100L);
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
			_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
			throw new WavePlanningException();
		}
		
		//Flag Wave as "modified" if it is an existing wave
		if (!(type.equalsIgnoreCase("new")) && orderKeys.size() > 0) {
			Query waveQry = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY = '"+waveId+"' ","");
			BioCollection wave = uow.getBioCollectionBean(waveQry);	
			try {
				if(wave != null && wave.size() > 0){
					wave.elementAt(0).set("STATUS", new Integer(WavePlanningConstants.WAVE_STATUSES_MODIFIED));
					uow.saveUOW();
				}
			} catch (EpiDataException e) {
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Updating Wave Status To MODIFIED... exiting!",100L);
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
				throw new WavePlanningException();
			} catch (EpiException e) {
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Updating Wave Status To MODIFIED... exiting!",100L);
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
				throw new WavePlanningException();
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Leaving addOrdersToWave...",100L);
		return ordersAdded;
	}
	
	public static synchronized int addOrdersToWaveFor2000(int waveId, ArrayList orderKeys, ArrayList whsesList,ArrayList segmentsList ,String dcID, String type,StateInterface state) throws WavePlanningException{
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Executing addOrdersToWaveFor2000...",100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter waveId:"+waveId,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter orderKeys:"+orderKeys,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter whsesList:"+whsesList,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter segmentsList:"+segmentsList,100L);
		_log.debug("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Parameter dcID:"+dcID,100L);
		int ordersAdded = 0;
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		//Make sure each order has not already been added to the wave
		for(int i = 0; i < orderKeys.size(); i++){
			Query waveDetailQry = new Query("wm_wp_wavedetail","wm_wp_wavedetail.WAVEKEY = '"+waveId+"' AND wm_wp_wavedetail.WHSEID = '"+whsesList.get(i)+"' AND wm_wp_wavedetail.ORDERKEY = '"+orderKeys.get(i)+"'","");
			BioCollection waveDetails = uow.getBioCollectionBean(waveDetailQry);		
			try {
				if(waveDetails != null && waveDetails.size() > 0){
					//Order is already associated with this wave
					continue;
				}
			} catch (EpiDataException e) {
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Could not retrieve wave... exiting!",100L);
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);			
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
				throw new WavePlanningException();
			}

			//Add each order
			try { 

				QBEBioBean waveDetail = uow.getQBEBioWithDefaults("wm_wp_wavedetail");
				waveDetail.set("WAVEKEY", new Integer(waveId));
				waveDetail.set("WHSEID", whsesList.get(i));
				waveDetail.set("ORDERKEY", orderKeys.get(i));
				waveDetail.set("SEGMENTID", segmentsList.get(i));
				waveDetail.set("DC_ID",dcID);							
				waveDetail.save();
				uow.saveUOW(true);
				ordersAdded++;

			} catch (DataBeanException e) {
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Adding Orders To Wave... exiting!",100L);
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
				throw new WavePlanningException();
			} catch (EpiException e) {
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Adding Orders To Wave... exiting!",100L);
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
				_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
				throw new WavePlanningException();
			}
			
			//Flag Wave as "modified" if it is an existing wave
			if (!(type.equalsIgnoreCase("new"))) {
				Query waveQry = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY = '"+waveId+"' ","");
				BioCollection wave = uow.getBioCollectionBean(waveQry);	
				try {
					if(wave != null && wave.size() > 0){
						wave.elementAt(0).set("STATUS", new Integer(WavePlanningConstants.WAVE_STATUSES_MODIFIED));
						uow.saveUOW();
					}
				} catch (EpiDataException e) {
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Updating Wave Status To MODIFIED... exiting!",100L);
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
					throw new WavePlanningException();
				} catch (EpiException e) {
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE","Error While Updating Wave Status To MODIFIED... exiting!",100L);
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getErrorMessage(),100L);	
					_log.error("LOG_DEBUG_WAVE_ADD_ODR_WAVE",e.getStackTraceAsString(),100L);
					throw new WavePlanningException();
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Leaving addOrdersToWave...",100L);
		return ordersAdded;
	}
	
	public static synchronized String createWave(StateInterface state, String whseId, String wmsName, String description, String status, String dcID, BioCollection orders, Object wavePrefixObj) throws NumberFormatException, EpiException, WavePlanningException{
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Executing createWave...",100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter state:"+state,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter whseId:"+whseId,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter wmsName:"+wmsName,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter description:"+description,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter status:"+status,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter dcID:"+dcID,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter orders:"+orders,100L);
		
		QBEBioBean newWave = null;
		String facility = "";
		Integer waveKey = null;
		
		//Create New Wave				
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Creating new wp_wave QBEBioBean...",100L);
		newWave = state.getDefaultUnitOfWork().getQBEBioWithDefaults("wm_wp_wave");
				
		//Assign new wave key
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Assigning new wave key...",100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Getting existing waves...",100L);
		Query waveQry = new Query("wm_wp_wave","","");
		BioCollection waves = state.getDefaultUnitOfWork().getBioCollectionBean(waveQry);
		
		//If no waves exist assign wave key of 1
		if(waves == null || waves.size() == 0){
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","No existing waves... Assigning wave key = 1",100L);
			newWave.set("WAVEKEY", new Integer(1));
			waveKey = new Integer(1);
		}
		//If waves exist then determine wave key by getting the largest existing wave key and incrementing it.
		else{			
			Integer maxWaveKey = new Integer(waves.max("WAVEKEY").toString());
			waveKey = new Integer(maxWaveKey.intValue() + 1);
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Existing waves found... Assigning wave key = "+waveKey,100L);
			newWave.set("WAVEKEY", waveKey);
		}
		
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Populating new Wave data...",100L);
		newWave.set("WHSEID", whseId);
		newWave.set("DESTINATIONWMS", wmsName);
		if(description == null){
			if(wavePrefixObj == null){
				newWave.set("DESCRIPTION", "New" + waveKey);
			}else{
				newWave.set("DESCRIPTION", wavePrefixObj.toString()+" " + waveKey);
			}
		}else
			newWave.set("DESCRIPTION", description);
		newWave.set("STATUS", new Integer(status));
		newWave.set("CREATEDON", new GregorianCalendar());
		newWave.set("CREATEDBY", WPUserUtil.getUserId(state));
		
		if (wmsName.equals(WavePlanningConstants.WMS_2000)) {
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","2000 codeset detected...",100L);			
			newWave.set("DCID", dcID);			
			// DF# 108604.n
			facility = dcID;
		}
		newWave.save();
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Saving new wave...",100L);
		state.getDefaultUnitOfWork().saveUOW(true);
		
		//Add Orders To Wave
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Adding orders to wave...",100L);
		int ordersAdded = 0;
		if (wmsName.equals(WavePlanningConstants.WMS_2000)) {
			ArrayList orderKeys = new ArrayList();
			ArrayList whsesList = new ArrayList();
			ArrayList segmentsList = new ArrayList();
			if(orders != null){
				for(int i = 0; i < orders.size(); i++){
					orderKeys.add(orders.elementAt(i).get("ORDERKEY"));
					whsesList.add(orders.elementAt(i).get("WHSEID"));
					segmentsList.add(orders.elementAt(i).get("SEGMENTID"));
				}
			}
			ordersAdded = addOrdersToWaveFor2000(waveKey.intValue(), orderKeys, whsesList, segmentsList, dcID, "new", state);
		}
		else if (wmsName.equals(WavePlanningConstants.WMS_4000)){
			ArrayList orderKeys = new ArrayList();
			if(orders != null){
				for(int i = 0; i < orders.size(); i++){
					orderKeys.add(orders.elementAt(i).get("ORDERKEY"));
				}
			}
			ordersAdded = addOrdersToWave(waveKey.intValue(), orderKeys, whseId, "new",state);
		}
		
		//Send Email Alert
		Hashtable eventList = new Hashtable();
		eventList.put("event", "event.wavecreation");
		eventList.put("facility", facility);
		if (WPUserUtil.getUserId(state) != null) {
			eventList.put("user", WPUserUtil.getUser(state));
			if (WPUserUtil.getCurrentUserEmail(state) != null)
				eventList.put("emailId", WPUserUtil.getCurrentUserEmail(state));
			else
				eventList.put("emailId", "");
		}
		else {
			eventList.put("user", "");
			eventList.put("emailId", "");
		}
		eventList.put("eventType", "wave.creation.success");
		eventList.put("result", ""+ waveKey);
		eventList.put("result1", ""+ordersAdded);
//		WaveNotification.sendWPMailAlerts(eventList);
		return waveKey.toString();
	}
	
	
	/*public static synchronized String createWave(StateInterface state, String whseId, String wmsName, String description, String status, String dcID, BioCollection orders) throws NumberFormatException, EpiException, WavePlanningException{
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Executing createWave...",100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter state:"+state,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter whseId:"+whseId,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter wmsName:"+wmsName,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter description:"+description,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter status:"+status,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter dcID:"+dcID,100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Parameter orders:"+orders,100L);
		
		QBEBioBean newWave = null;
		String facility = "";
		Integer waveKey = null;
		
		//Create New Wave				
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Creating new wp_wave QBEBioBean...",100L);
		newWave = state.getDefaultUnitOfWork().getQBEBioWithDefaults("wp_wave");
				
		//Assign new wave key
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Assigning new wave key...",100L);
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Getting existing waves...",100L);
		Query waveQry = new Query("wp_wave","","");
		BioCollection waves = state.getDefaultUnitOfWork().getBioCollectionBean(waveQry);
		
		//If no waves exist assign wave key of 1
		if(waves == null || waves.size() == 0){
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","No existing waves... Assigning wave key = 1",100L);
			newWave.set("WAVEKEY", new Integer(1));
			waveKey = new Integer(1);
		}
		//If waves exist then determine wave key by getting the largest existing wave key and incrementing it.
		else{			
			Integer maxWaveKey = new Integer(waves.max("WAVEKEY").toString());
			waveKey = new Integer(maxWaveKey.intValue() + 1);
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Existing waves found... Assigning wave key = "+waveKey,100L);
			newWave.set("WAVEKEY", waveKey);
		}
		
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Populating new Wave data...",100L);
		newWave.set("WHSEID", whseId);
		newWave.set("DESTINATIONWMS", wmsName);
		if(description == null)
			newWave.set("DESCRIPTION", "New" + waveKey);
		else
			newWave.set("DESCRIPTION", description);
		newWave.set("STATUS", new Integer(status));
		newWave.set("CREATEDON", new GregorianCalendar());
		newWave.set("CREATEDBY", WPUserUtil.getUserId(state));
		
		if (wmsName.equals(WavePlanningConstants.WMS_2000)) {
			_log.debug("LOG_DEBUG_WP_CREATE_WAVE","2000 codeset detected...",100L);			
			newWave.set("DCID", dcID);			
			// DF# 108604.n
			facility = dcID;
		}
		newWave.save();
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Saving new wave...",100L);
		state.getDefaultUnitOfWork().saveUOW(true);
		
		//Add Orders To Wave
		_log.debug("LOG_DEBUG_WP_CREATE_WAVE","Adding orders to wave...",100L);
		int ordersAdded = 0;
		if (wmsName.equals(WavePlanningConstants.WMS_2000)) {
			ArrayList orderKeys = new ArrayList();
			ArrayList whsesList = new ArrayList();
			ArrayList segmentsList = new ArrayList();
			if(orders != null){
				for(int i = 0; i < orders.size(); i++){
					orderKeys.add(orders.elementAt(i).get("ORDERKEY"));
					whsesList.add(orders.elementAt(i).get("WHSEID"));
					segmentsList.add(orders.elementAt(i).get("SEGMENTID"));
				}
			}
			ordersAdded = addOrdersToWaveFor2000(waveKey.intValue(), orderKeys, whsesList, segmentsList, dcID, "new", state);
		}
		else if (wmsName.equals(WavePlanningConstants.WMS_4000)){
			ArrayList orderKeys = new ArrayList();
			if(orders != null){
				for(int i = 0; i < orders.size(); i++){
					orderKeys.add(orders.elementAt(i).get("ORDERKEY"));
				}
			}
			ordersAdded = addOrdersToWave(waveKey.intValue(), orderKeys, whseId, "new",state);
		}
		
		//Send Email Alert
		Hashtable eventList = new Hashtable();
		eventList.put("event", "event.wavecreation");
		eventList.put("facility", facility);
		if (WPUserUtil.getUserId(state) != null) {
			eventList.put("user", WPUserUtil.getUser(state));
			if (WPUserUtil.getCurrentUserEmail(state) != null)
				eventList.put("emailId", WPUserUtil.getCurrentUserEmail(state));
			else
				eventList.put("emailId", "");
		}
		else {
			eventList.put("user", "");
			eventList.put("emailId", "");
		}
		eventList.put("eventType", "wave.creation.success");
		eventList.put("result", ""+ waveKey);
		eventList.put("result1", ""+ordersAdded);
		WaveNotification.sendWPMailAlerts(eventList);
		return waveKey.toString();
	}*/
}