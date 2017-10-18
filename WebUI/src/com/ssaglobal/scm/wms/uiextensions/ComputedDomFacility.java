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

package com.ssaglobal.scm.wms.uiextensions;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.permission.UserRole;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class ComputedDomFacility extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ComputedDomFacility.class);
	public ComputedDomFacility()
	{
	}
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Executing ComputedDomFacility",100L);
		StateInterface state = context.getState();		
/*		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
		ArrayList roles = new ArrayList(rolesTemp.length);
		for(int i = 0; i < rolesTemp.length; i++){
			roles.add(rolesTemp[i]);
		}
*/
		UserRole [] userRoles = state.getServiceManager().getAllUserRolesForCurrentUser();
		if(SecurityUtil.isUserAdmin(state)){
			_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","User is admin, returning all facilities...",100L);			
			Query loadBiosQryA = new Query("wm_pl_db", "wm_pl_db.isActive = '1'", "");				//8050							
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
			BioCollectionBean facilities = uow.getBioCollectionBean(loadBiosQryA);		
			if(facilities != null){
				for(int i = 0; i < facilities.size(); i++){
					Bio facility = facilities.elementAt(i);
					value.add(facility.get("db_name"));
					labels.add(facility.get("db_alias"));
				}
			}
		}
		else{
			if(userRoles.length > 0){				
//				BioCollectionBean roleCollection = SecurityUtil.getRoleCollection(state);
				BioCollectionBean roleCollection = SecurityUtil.getUserRoleCollection(state);
				String qry = "";				
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Got "+roleCollection.size()+" Roles....",100L);
				
				if(roleCollection == null || roleCollection.size() == 0)
					return RET_CONTINUE;				
				qry = " wm_facilityrolemapping.ROLEID = '"+roleCollection.elementAt(0).getString("user_role_id")+"' ";
				
				for(int i = 1; i < roleCollection.size(); i++){
					qry += " OR wm_facilityrolemapping.ROLEID = '"+roleCollection.elementAt(i).getString("user_role_id")+"' ";
				}
				
				Query loadBiosQryA = new Query("wm_facilityrolemapping", qry, "");	
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Loading Facility Mappings...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Facility Mapping QRY:"+qry,100L);				
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean facilityMappings = uow.getBioCollectionBean(loadBiosQryA);
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Got "+facilityMappings.size()+" Facility Mappings...",100L);				
				if(facilityMappings == null || facilityMappings.size() == 0)
					return RET_CONTINUE;
				
				qry = " wm_pl_db.db_key = '"+facilityMappings.elementAt(0).getString("FACILITYID")+"' ";
				for(int i = 1; i < facilityMappings.size(); i++){
					qry += " OR wm_pl_db.db_key = '"+facilityMappings.elementAt(i).getString("FACILITYID")+"' ";
				}
				
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Loading Facilities...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Facility QRY:"+qry,100L);
				loadBiosQryA = new Query("wm_pl_db", qry, "");																			
				BioCollectionBean facilities = uow.getBioCollectionBean(loadBiosQryA);	
				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Got "+facilities.size()+" Facilities...",100L);				
				for(int i = 0; i < facilities.size(); i++){
					_log.debug("LOG_SYSTEM_OUT","\nHere6\n",100L);
					Bio facility = facilities.elementAt(i);					
					value.add(facility.get("db_name"));
					labels.add(facility.get("db_alias"));

				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Leaving ComputedDomFacility",100L);		
		return RET_CONTINUE;
	}	
}
