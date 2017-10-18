/**************************************************************
*                                                             *                          
*                           NOTICE                            *
*                                                             *
*   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             *
*   CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   *
*   OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  *
*   WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       *
*   ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  *
*   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            *
*   ALL OTHER RIGHTS RESERVED.                                *
*                                                             *
*   (c) COPYRIGHT 2011 INFOR.  ALL RIGHTS RESERVED.           *
*   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            *
*   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          *
*   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        *
*   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         *
*   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  *
*                                                             *
***************************************************************/

package com.ssaglobal.scm.wms.wm_task_manager_user.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 */
public class LDAPUserAttribute extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LDAPUserAttribute.class);
	/**
	 * Fires whenever the values and/or labels for an attribute domain are requested.  The base list
	 * of labels and values are provided and may be filtered, modified, replaced, etc as desired.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext} exposes information
	 * about the context in which the attribute domain is being used, including the service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * @param context the {@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext}
	 * @param values the list of values to be modified
	 * @param labels the corresponding list of labels to be modified
	 */
	
	/**
	 * Modification History:
	 * AW		08/27/10		Defect:280578 Incident:3946801
	 * 							Issue: User is able to see all user and also, the information in the dropdown is not sorted.
	 * 
	 */
	@Override
	protected int execute(DropdownContentsContext context, List values, List labels) throws EpiException
	{
		
		try{
			_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","**Executing LDAPUserAttribute",100L);
			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			HashMap<String, String> taskManagerUsers = umsInterface.getTaskManagerUsers();

			LinkedHashMap<String, String> sortedUsers = sortHashMapByValues(taskManagerUsers);
			StateInterface state = context.getState();
			HttpSession session = state.getRequest().getSession();
			
			Object currFacility = session.getAttribute("dbConnectionName");			
			
			for (String key : sortedUsers.keySet())
			{				
				_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","** Current facility: " +currFacility,100L);
				
				if (checkIfUserHasPermissionsToCurrFacility(state, currFacility, key)) {
					//10/22/2010 FW: Added code to change userid to uppercase (Incident4070192_Defect287601) -- Start
					//values.add(key);
					labels.add(sortedUsers.get(key));
					values.add(key.toUpperCase());
		            //10/22/2010 FW: Added code to change userid to uppercase (Incident4070192_Defect287601) -- End

					_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","** Adding: [" +key +"," +sortedUsers.get(key) +"]",100L);
				}
				
				_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","**Exiting LDAPUserAttribute",100L);	
				
			
				
			}
			//			ArrayList<String> userIdsList = umsInterface.getAllUserIds(); 
			//			int size = 0;
			//			if(userIdsList != null){
			//				size = userIdsList.size();
			//			}
			//			for(int i=0; i< size; i++){
			//				values.add(userIdsList.get(i));
			//				labels.add(userIdsList.get(i));
			//			}
		}catch(Exception e){
			e.printStackTrace();
			return RET_CANCEL;
		}
		
		
		
		return RET_CONTINUE;
	}
	/**
	 * 
	 * @param passedMap
	 * @return
	 * @throws Exception
	 * 
	  * AW		08/27/10		Defect:280578 Incident:3946801
	  * 						Added in to sort hashmap
	  * 
	 */
	public LinkedHashMap<String, String> sortHashMapByValues(final HashMap<String, String> passedMap) throws Exception {
	    ArrayList<String> mapKeys = new ArrayList<String>(passedMap.keySet());
	    ArrayList<String> mapValues = new ArrayList<String>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);
	        
	    LinkedHashMap<String, String> sortedMap = 
	        new LinkedHashMap<String, String>();
	    
	    
	    Iterator<String> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Object val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();
	        
	        while (keyIt.hasNext()) {
	            Object key = keyIt.next();
	            String comp1 = passedMap.get(key).toString();
	            String comp2 = val.toString();	            
	            if (comp1.equals(comp2)) {
	                passedMap.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put((String) key, (String) val);
	                break;
	            }
	        }
	    }
	
	    return sortedMap;
	}
/**
 * 
 * @param state
 * @param currFacility
 * @param key
 * @return
 * @throws EpiDataException
 * 
 * AW		08/27/10	Defect:280578 Incident:3946801
 * 						Added in to sort hashmap
 * HC		03/07/10	Doesn't work with ADS authentication. changed call to 'getAllRolesForUserId'
 * 						with 'getOAUserRolesForUserId'
 */
	private boolean checkIfUserHasPermissionsToCurrFacility(
			StateInterface state, Object currFacility, String key) throws EpiDataException {
		// TODO Auto-generated method stub
		
		//String[] roles = state.getServiceManager().getAllRolesForUserId(key);
		String [] roles = state.getServiceManager().getOAUserRolesForUserId(key, true);
		//	System.out.println("\nroles size: " +roles.length);
		if (roles == null){
			return false;
		}
		_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","**Roles: " +roles.length,100L);
		boolean flag = false;

		if(SecurityUtil.isUserAdminForThisUser(state, key, roles)){											
			flag = true;
		}
		else {
			if(roles.length > 0){
				BioCollectionBean roleCollection = SecurityUtil.getUserRoleCollectionForThisUser(state, key, roles);
				String qry = "";

				if(roleCollection == null || roleCollection.size() == 0) {
					return false;
				}
				
				qry = " wm_facilityrolemapping.ROLEID = '"+roleCollection.elementAt(0).getString("user_role_id")+"' ";
				
				for(int i = 1; i < roleCollection.size(); i++){									
					qry += " OR wm_facilityrolemapping.ROLEID = '"+roleCollection.elementAt(i).getString("user_role_id")+"' ";
				}
				Query loadBiosQryA = new Query("wm_facilityrolemapping", qry, "");	
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean facilityMappings = uow.getBioCollectionBean(loadBiosQryA);				
				if(facilityMappings == null || facilityMappings.size() == 0)
					return false;
				qry = " (wm_pl_db.db_key = '"+facilityMappings.elementAt(0).getString("FACILITYID")+"' " +
				"AND wm_pl_db.db_name = '" +currFacility +"') ";
				//	System.out.println("\npermission tp: " +facilityMappings.elementAt(0).getString("FACILITYID"));
				_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","**Permission to:" +facilityMappings.elementAt(0).getString("FACILITYID"),100L);
				for(int i = 1; i < facilityMappings.size(); i++){
					qry += " OR (wm_pl_db.db_key = '"+facilityMappings.elementAt(i).getString("FACILITYID")+"' " +
					"AND wm_pl_db.db_name = '" +currFacility +"') ";
					//	System.out.println("\npermission to: " +facilityMappings.elementAt(i).getString("FACILITYID"));
					_log.debug("LOG_DEBUG_JAVACLASS_LDAPUserAttribute","**Permission to:" +facilityMappings.elementAt(0).getString("FACILITYID"),100L);
				}
				loadBiosQryA = new Query("wm_pl_db", qry, "");
				if(uow.getBioCollectionBean(loadBiosQryA).size() > 0) {
					flag = true;
				}

			}
		}
		return flag;
	}
	

}