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
package com.ssaglobal.scm.wms.wm_ws_defaults;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WSDefaultsUtil
{
	public static final String FILTER_DEFAULTS_KEY = "filter.defualts";
	public static final String LOCKED_OWNERS_KEY = "locked.owners";
	public static final String LOCKED_CUSTOMER_KEY = "locked.customer";
	public static final String LOCKED_CARRIER_KEY = "locked.carrier";
	public static final String LOCKED_BILL_TO_KEY = "locked.bill.to";
	public static final String LOCKED_VENDOR_KEY = "locked.vendor";
	public static final String DATA_ENTRY_DEFAULTS_KEY = "data.entry.defualts";	
	public static final String USER_SCREENS_KEY = "user.screens";
	public static String DB_CONNECTION = "dbConnectionName";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSDefaultsUtil.class);
	public WSDefaultsUtil()
    {
    }
	
	
	public static HashMap getCache(StateInterface state){
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","In getCache()",100L);					
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Exiting getCache()",100L);		
		return (HashMap)state.getRequest().getSession().getAttribute("WSDEFAULTSCACHE");
	}
	
	private static void setCache(HashMap cache, StateInterface state){		
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","In setCache()",100L);		
		state.getRequest().getSession().setAttribute("WSDEFAULTSCACHE",cache);		
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Exiting setCache()",100L);			
	}
	
	public static void destroyCache(StateInterface state){		
		setCache(null,state);		
	}
	
	public static void buildCache(StateInterface state,BioCollection globalDefaultCollection, BioCollection defaultCollection, Bio userDefaultBio, Bio globalDefaultBio, BioCollection ownerCollection
									, BioCollection customerCollection, BioCollection carrierCollection, BioCollection billToCollection, BioCollection vendorCollection){
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","In buildCache()",100L);		
		HashMap cache = new HashMap();
		if(userDefaultBio != null){
			HashMap userFilterDefaults = new HashMap();
			try {
				_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting From User Defaults...",100L);				
				userFilterDefaults.put("STORERKEY",userDefaultBio.get("STORERKEY"));
				userFilterDefaults.put("RECEIVELOC",userDefaultBio.get("RECEIVELOC"));
				userFilterDefaults.put("PACK",userDefaultBio.get("PACK"));
				userFilterDefaults.put("PICKCODE",userDefaultBio.get("PICKCODE"));				
				userFilterDefaults.put("STARTBAY",userDefaultBio.get("STARTBAY"));
				userFilterDefaults.put("ENDBAY",userDefaultBio.get("ENDBAY"));
				userFilterDefaults.put("STARTSLOT",userDefaultBio.get("STARTSLOT"));
				userFilterDefaults.put("ENDSLOT",userDefaultBio.get("ENDSLOT"));								
				userFilterDefaults.put("DYNAMICRECORDINGFLAG",userDefaultBio.get("DYNAMICRECORDINGFLAG"));
				userFilterDefaults.put("OWNERLOCKFLAG",userDefaultBio.get("OWNERLOCKFLAG"));
				cache.put(DATA_ENTRY_DEFAULTS_KEY,userFilterDefaults);
			} catch (EpiDataException e) {				
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(DATA_ENTRY_DEFAULTS_KEY,null);
		}
		
		if(globalDefaultBio != null){
			HashMap globalFilterDefaults = new HashMap();
			if(cache.get(DATA_ENTRY_DEFAULTS_KEY) != null)
				globalFilterDefaults = (HashMap)cache.get(DATA_ENTRY_DEFAULTS_KEY);
			try {
				if(userDefaultBio == null){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting From Global Defaults...",100L);					
					globalFilterDefaults.put("STORERKEY",globalDefaultBio.get("STORERKEY"));
					globalFilterDefaults.put("RECEIVELOC",globalDefaultBio.get("RECEIVELOC"));
					globalFilterDefaults.put("PACK",globalDefaultBio.get("PACK"));
					globalFilterDefaults.put("PICKCODE",globalDefaultBio.get("PICKCODE"));					
					globalFilterDefaults.put("STARTBAY",globalDefaultBio.get("STARTBAY"));
					globalFilterDefaults.put("ENDBAY",globalDefaultBio.get("ENDBAY"));
					globalFilterDefaults.put("STARTSLOT",globalDefaultBio.get("STARTSLOT"));
					globalFilterDefaults.put("ENDSLOT",globalDefaultBio.get("ENDSLOT"));					
					globalFilterDefaults.put("NUMBEROFRECORDSTOKEEP",globalDefaultBio.get("NUMBEROFRECORDSTOKEEP"));
					globalFilterDefaults.put("DYNAMICRECORDINGFLAG",globalDefaultBio.get("DYNAMICRECORDINGFLAG"));
					globalFilterDefaults.put("OWNERLOCKFLAG",globalDefaultBio.get("OWNERLOCKFLAG"));
				}
				else{
					globalFilterDefaults.put("NUMBEROFRECORDSTOKEEP",globalDefaultBio.get("NUMBEROFRECORDSTOKEEP"));
				}
				cache.put(DATA_ENTRY_DEFAULTS_KEY,globalFilterDefaults);
			} catch (EpiDataException e) {				
				e.printStackTrace();		
				setCache(null, state);
				return;
			}
		}
		else{
			cache.put(DATA_ENTRY_DEFAULTS_KEY,null);
		}
		
		if(defaultCollection != null){
			HashMap filterDefaults = new HashMap();
			try {
				for(int i = 0; i < defaultCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Filter Value...",100L);					
					Bio filterRecord = defaultCollection.elementAt(i);
					filterDefaults.put(filterRecord.get("TYPE"),filterRecord.get("FILTERVALUE"));
				}
				cache.put(FILTER_DEFAULTS_KEY,filterDefaults);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else if(globalDefaultCollection != null){
			HashMap filterDefaults = new HashMap();
			try {
				for(int i = 0; i < globalDefaultCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Filter Value...",100L);				
					Bio filterRecord = globalDefaultCollection.elementAt(i);
					filterDefaults.put(filterRecord.get("TYPE"),filterRecord.get("FILTERVALUE"));
				}
				cache.put(FILTER_DEFAULTS_KEY,filterDefaults);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(FILTER_DEFAULTS_KEY,null);
		}
		
		if(ownerCollection != null){
			ArrayList lockedOwners = new ArrayList();
			try {
				for(int i = 0; i < ownerCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Locked Owners...",100L);					
					Bio ownerRecord = ownerCollection.elementAt(i);
					lockedOwners.add(ownerRecord.get("FILTERVALUE"));
				}
				cache.put(LOCKED_OWNERS_KEY,lockedOwners);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(LOCKED_OWNERS_KEY,null);
		}
		
		if(customerCollection != null){
			ArrayList lockedCustomers = new ArrayList();
			try {
				for(int i = 0; i < customerCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Locked Customers...",100L);					
					Bio customerRecord = customerCollection.elementAt(i);
					lockedCustomers.add(customerRecord.get("FILTERVALUE"));
				}
				cache.put(LOCKED_CUSTOMER_KEY,lockedCustomers);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(LOCKED_CUSTOMER_KEY,null);
		}
		
		if(billToCollection != null){
			ArrayList lockedBillTo = new ArrayList();
			try {
				for(int i = 0; i < billToCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Locked Bill To...",100L);					
					Bio billToRecord = billToCollection.elementAt(i);
					lockedBillTo.add(billToRecord.get("FILTERVALUE"));
				}
				cache.put(LOCKED_BILL_TO_KEY,lockedBillTo);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(LOCKED_BILL_TO_KEY,null);
		}
		
		if(carrierCollection != null){
			ArrayList lockedCarriers = new ArrayList();
			try {
				for(int i = 0; i < carrierCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Locked Carrier...",100L);					
					Bio carrierRecord = carrierCollection.elementAt(i);
					lockedCarriers.add(carrierRecord.get("FILTERVALUE"));
				}
				cache.put(LOCKED_CARRIER_KEY,lockedCarriers);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(LOCKED_CARRIER_KEY,null);
		}
		
		if(vendorCollection != null){
			ArrayList lockedVendors = new ArrayList();
			try {
				for(int i = 0; i < vendorCollection.size(); i++){
					_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Setting Locked Vendor...",100L);					
					Bio vendorRecord = vendorCollection.elementAt(i);
					lockedVendors.add(vendorRecord.get("FILTERVALUE"));
				}
				cache.put(LOCKED_VENDOR_KEY,lockedVendors);
			} catch (EpiDataException e) {
				e.printStackTrace();		
				setCache(null,state);
				return;
			}
		}
		else{
			cache.put(LOCKED_VENDOR_KEY,null);
		}
		
		setCache(cache,state);
		_log.debug("LOG_DEBUG_CLASS_WSDEFAULTSUTIL","Exiting buildCache()...",100L);		
	}
		
	public static boolean isCacheAvaliable(StateInterface state){
		if(getCache(state) == null)
			return false;
		return true;
	}
	
	public static String getDefaultStorer(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){				
				return (String)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("STORERKEY");
			}
		}
		return null;
	}
	
	public static Integer getDynamicRecordeingFlag(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				return (Integer)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("DYNAMICRECORDINGFLAG");
			}
		}
		return null;
	}
	
	public static Integer getOwnerLockFlag(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				return (Integer)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("OWNERLOCKFLAG");
			}
		}
		return null;
	}
	
	public static String getDefaultRecieveLoc(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				return (String)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("RECEIVELOC");
			}
		}
		return null;
	}
	public static String getDefaultPack(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				return (String)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("PACK");
			}
		}
		return null;
	}
	public static String getDefaultPickCode(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				return (String)((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("PICKCODE");
			}
		}
		return null;
	}	
	public static String getDefaultStartBay(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){
				if(((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("STARTBAY") != null){
					return ((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("STARTBAY").toString();
				}
			}
		}
		return null;
	}
	public static String getDefaultEndBay(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){
				if(((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("ENDBAY") != null){
					return ((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("ENDBAY").toString();
				}				
			}
		}
		return null;
	}
	public static String getDefaultStartSlot(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				if(((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("STARTSLOT") != null){
					return ((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("STARTSLOT").toString();
				}				
			}
		}
		return null;
	}
	public static String getDefaultEndSlot(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){	
				if(((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("ENDSLOT") != null){
					return ((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("ENDSLOT").toString();
				}				
			}
		}
		return null;
	}
	public static String getNumberOfRecordsToKeep(StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(DATA_ENTRY_DEFAULTS_KEY) != null){		
				if(((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("NUMBEROFRECORDSTOKEEP") != null){
					return ((HashMap)getCache(state).get(DATA_ENTRY_DEFAULTS_KEY)).get("NUMBEROFRECORDSTOKEEP").toString();
				}				
			}
		}
		return null;
	}
	public static String getPreFilterValueByType(String type,StateInterface state){		
		if(getCache(state)!=null){
			if(getCache(state).get(FILTER_DEFAULTS_KEY) != null){	
				return (String)((HashMap)getCache(state).get(FILTER_DEFAULTS_KEY)).get(type);
			}
		}
		return null;
	}
	public static boolean doesCacheHaveType(String type,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(FILTER_DEFAULTS_KEY) != null){	
				return ((HashMap)getCache(state).get(FILTER_DEFAULTS_KEY)).containsKey(type);
			}
		}
		return false;
	}
	
	public static boolean canUserAccessOwner(String owner,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_OWNERS_KEY) != null){	
				if(((ArrayList)getCache(state).get(LOCKED_OWNERS_KEY)).contains(owner)){
					return true;
				}				
			}
		}
		
		return false;
	}
	public static boolean canUserAccessCarrier(String carrier,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_CARRIER_KEY) != null){	
				if(((ArrayList)getCache(state).get(LOCKED_CARRIER_KEY)).contains(carrier)){
					return true;
				}				
			}
		}
		
		return false;
	}
	public static boolean canUserAccessCustomer(String customer,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_CUSTOMER_KEY) != null){	
				if(((ArrayList)getCache(state).get(LOCKED_CUSTOMER_KEY)).contains(customer)){
					return true;
				}				
			}
		}
		
		return false;
	}
	public static boolean canUserAccessBillTo(String billTo,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_BILL_TO_KEY) != null){	
				if(((ArrayList)getCache(state).get(LOCKED_BILL_TO_KEY)).contains(billTo)){
					return true;
				}				
			}
		}
		
		return false;
	}
	public static boolean canUserAccessVendor(String vendor,StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_VENDOR_KEY) != null){	
				if(((ArrayList)getCache(state).get(LOCKED_VENDOR_KEY)).contains(vendor)){
					return true;
				}				
			}
		}
		
		return false;
	}
	public static ArrayList getLockedOwners(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_OWNERS_KEY) != null){					
				return (ArrayList)getCache(state).get(LOCKED_OWNERS_KEY);								
			}
		}
		
		return null;
	}
	public static ArrayList getLockedCarriers(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_CARRIER_KEY) != null){					
				return (ArrayList)getCache(state).get(LOCKED_CARRIER_KEY);								
			}
		}
		
		return null;
	}
	public static ArrayList getLockedBillTo(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_BILL_TO_KEY) != null){					
				return (ArrayList)getCache(state).get(LOCKED_BILL_TO_KEY);								
			}
		}
		
		return null;
	}
	public static ArrayList getLockedCustomers(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_CUSTOMER_KEY) != null){					
				return (ArrayList)getCache(state).get(LOCKED_CUSTOMER_KEY);								
			}
		}
		
		return null;
	}
	public static ArrayList getLockedVendors(StateInterface state){
		if(getCache(state)!=null){
			if(getCache(state).get(LOCKED_VENDOR_KEY) != null){					
				return (ArrayList)getCache(state).get(LOCKED_VENDOR_KEY);								
			}
		}
		
		return null;
	}
	public static String getLockedOwnersAsDelimetedList(StateInterface state, String token){
		String list = "";
		ArrayList lockedOwners = getLockedOwners(state);
		if(lockedOwners != null && lockedOwners.size() > 0){
				list += lockedOwners.get(0);
			for(int i = 1; i < lockedOwners.size(); i++){
				list += token + lockedOwners.get(i);
			}
		}
		return list;
	}
	public static boolean isOwnerLocked(StateInterface state){
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		boolean isOwnerLocked = (ownerLockFlag == null || ownerLockFlag.intValue() == 0)?false:true;
		
		return isOwnerLocked;
	}
	public static boolean isEnterpriseConnection(StateInterface state){		
		HttpSession session = state.getRequest().getSession();
		String dbName = (String)session.getAttribute(DB_CONNECTION);
		if(dbName.equalsIgnoreCase("enterprise"))	
			return true;
		else
			return false;
	}
}