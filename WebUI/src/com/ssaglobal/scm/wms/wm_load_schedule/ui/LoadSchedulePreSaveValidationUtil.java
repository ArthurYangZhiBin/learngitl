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
package com.ssaglobal.scm.wms.wm_load_schedule.ui;
import com.ssaglobal.scm.wms.datalayer.*;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_ws_defaults.favorites.InteractionNameScreenTable;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class LoadSchedulePreSaveValidationUtil {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadSchedulePreSaveValidationUtil.class);
	
	public static boolean isValidLocation(LoadScheduleValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidLocation ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isValidLocation" ,100L);
		String location = data.getOutbound();
		String sql ="select * from LOC where LOC='"+location+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidLocation: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isOhtypeUnique(LoadScheduleValidationDataObject data) throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isOhtypeUnique ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isOhtypeUnique" ,100L);
		String loadScheduleKey = data.getLoadScheduleKey();
		String consigneeKey = data.getCustomer();
		String ohtype = data.getOrderHandlingType();
		String sql ="select distinct consigneekey, ohtype from loadscheduledetail where loadschedulekey='"
					+loadScheduleKey+"' AND consigneekey='"+consigneeKey+"' AND ohtype='"+ohtype+"'";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isOhtypeUnique: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	public static boolean isHeaderCombUnique(LoadScheduleValidationDataObject data) throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isHeaderCombUnique ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isHeaderCombUnique" ,100L);
		String owner = data.getOwner();
		String schType = data.getScheduleType();
		String dayOfWeek = data.getDayOfWeek();
		String route = data.getRoute();
  		String sql = "SELECT * " + "FROM loadschedule " 
		+ "WHERE (storer = '" + owner + "') "
		+ "AND (scheduletype = '" + schType + "') "
		+ "AND (dayofweek = '" + dayOfWeek + "') "
		+ "AND (route = '" + route + "') ";
		
		
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isHeaderCombUnique: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	public static boolean isDayOfWeekScheduled(LoadScheduleValidationDataObject data) throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isDayOfWeekScheduled ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isDayOfWeekScheduled" ,100L);
		String sql="";
		String dayOfWeek = data.getDayOfWeek();
		String storer = data.getOwner();
		String scheduleType = data.getScheduleType();
		String consigneeKey = data.getCustomer();
		String ohtype = data.getOrderHandlingType();
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** Day of the week is " +dayOfWeek +"****\n\n",100L);
		if(dayOfWeek.equalsIgnoreCase("7")){
			sql = "select loadscheduledetail.loadschedulekey from loadschedule, loadscheduledetail where " + 
			"loadschedule.loadschedulekey = loadscheduledetail.loadschedulekey and " + 
			"loadschedule.dayofweek = '7' and " + 
			"loadschedule.storer = '" + storer + "' and " + 
			"loadschedule.scheduletype = '" + scheduleType + "' and " + 
			"loadscheduledetail.Consigneekey = '" + consigneeKey + 
			"' and loadscheduledetail.ohtype = '" + ohtype + "'";
		}else{
			sql="select loadscheduledetail.loadschedulekey from loadschedule, loadscheduledetail where " +
			"loadschedule.loadschedulekey = loadscheduledetail.loadschedulekey and " + 
			"loadschedule.dayofweek != '7' and " + 
			"loadschedule.storer = '" + storer + "' and " + 
			"loadschedule.scheduletype = '" + scheduleType + "' and " + 
			"loadscheduledetail.Consigneekey = '" + consigneeKey +  
			"' and loadscheduledetail.ohtype = '" + ohtype + "'";
		}
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isDayOfWeekScheduled: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isValidCustomer(LoadScheduleValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidCustomer ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isValidCustomer" ,100L);
		String customer = data.getCustomer();
		String sql ="SELECT * FROM storer " 
			 		+ "WHERE (storerkey = '" + customer + "') "
			 		+ "AND (type = '2') ";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidCustomer: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValidOwner(LoadScheduleValidationDataObject data)throws DPException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n***** In isValidOwner ******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_UTIL","** In isValidOwner" ,100L);
		String owner = data.getOwner();
		String sql ="SELECT * FROM storer " 
			 		+ "WHERE (storerkey = '" + owner + "') "
			 		+ "AND (type = '1') ";
		//_log.debug("LOG_SYSTEM_OUT","\n\n** Query in isValidOwner: " +sql,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			return true;
		}else{
			return false;
		}
	}
}
