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
package com.ssaglobal.scm.wms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.permission.UserRole;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ums.*;

public class SecurityUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SecurityUtil.class);
	public SecurityUtil()
    {
    }
	
	public static boolean canAccessReport(StateInterface state, String reportId) throws EpiDataException{
		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
		ArrayList roles = new ArrayList(rolesTemp.length);
		for(int i = 0; i < rolesTemp.length; i++){
			roles.add(rolesTemp[i]);
		}
		if(SecurityUtil.isAdmin(state)){
			_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","User is admin, returning all facilities...",100L);			
			return true;
		}
		if(rolesTemp.length > 0){ 
			String qry = " wm_reportsrolemapping.REPORTID = '"+reportId+"' AND (wm_reportsrolemapping.ROLENAME = '"+rolesTemp[0]+"' ";
			for(int i = 1; i < rolesTemp.length; i++){
				qry += " OR wm_reportsrolemapping.ROLENAME = '"+rolesTemp[i]+"' ";
			}
			Query loadBiosQryA = new Query("wm_reportsrolemapping", qry+")", "");									
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
			BioCollectionBean reportMappings = uow.getBioCollectionBean(loadBiosQryA);	
			if(reportMappings != null && reportMappings.size() > 0){
				return true;
			}
		}
		return false;
	}
		
	public static BioCollectionBean getAccessableReports(StateInterface state, String orderBy, String bioQry, DataBean focus) throws EpiDataException{
		
		
		String qry = "";
		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
		//If no roles present then return an empty BioCollection
		if(rolesTemp == null || rolesTemp.length == 0){
			Query loadBiosQryA = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.SERIALKEY = -1", "");																
			return uow.getBioCollectionBean(loadBiosQryA);
		}						
		
		if(!isAdmin(state)){
			BioCollectionBean roleCollection = getRoleCollection(state);
			if(roleCollection == null || roleCollection.size() == 0)
				return uow.getBioCollectionBean(new Query("wm_pbsrpt_reports","wm_pbsrpt_reports.RPT_ID = ''",""));				
				
			qry += " wm_reportsrolemapping.ROLEID= '"+roleCollection.elementAt(0).getString("user_role_id")+"' ";
			for(int i = 1; i < roleCollection.size(); i++){
				qry += " OR wm_reportsrolemapping.ROLEID = '"+roleCollection.elementAt(i).getString("user_role_id")+"' ";
			}
			Query loadBiosQryA = new Query("wm_reportsrolemapping", qry, "");																			
			BioCollectionBean reportsRoleMapping = uow.getBioCollectionBean(loadBiosQryA);
			if(reportsRoleMapping == null || reportsRoleMapping.size() == 0)
				return null;
			qry = "wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(0).getString("REPORTID")+"' ";
			for(int i = 0; i < reportsRoleMapping.size(); i++){
				qry += " OR wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(i).getString("REPORTID")+"' ";
			}
			loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);
			BioCollectionBean reports = null;			
			if(focus == null || !focus.isBioCollection() || !((BioCollectionBean)focus).getBioTypeName().equals("wm_pbsrpt_reports")){				
				reports = uow.getBioCollectionBean(loadBiosQryA);
			}
			else{				
				reports = (BioCollectionBean)focus;
				reports.filterInPlace(loadBiosQryA);
			}
			if(bioQry != null && bioQry.length() > 0){
				reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
			}
			return reports;
		}
		Query loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);		
		BioCollectionBean reports = null;
		if(focus == null || !focus.isBioCollection() || !((BioCollectionBean)focus).getBioTypeName().equals("wm_pbsrpt_reports")){
			reports = uow.getBioCollectionBean(loadBiosQryA);
		}
		else{
			reports = (BioCollectionBean)focus;
			reports.filterInPlace(loadBiosQryA);
		}		
		if(bioQry != null && bioQry.length() > 0){
			reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
		}
		return reports;
	}
	
	public static BioCollectionBean getRoleCollection(StateInterface state){
		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();		
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Loading user roles...",100L);
		String qry = " wm_meta_user_role.user_role_name = '"+rolesTemp[0]+"' ";
		for(int i = 1; i < rolesTemp.length; i++){
			qry += " OR wm_meta_user_role.user_role_name = '"+rolesTemp[i]+"' ";
		}
		Query loadBiosQryA = new Query("wm_meta_user_role", qry, "");
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","User Role QRY:"+qry,100L);		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollectionBean roleCollection = uow.getBioCollectionBean(loadBiosQryA);
		return roleCollection;
	}
	
	public static boolean isAdmin(StateInterface state){
		
/*		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();		
		for(int i = 0; i < rolesTemp.length; i++){
			_log.debug("LOG_SYSTEM_OUT","Role:"+rolesTemp[i],100L);
		}
		for(int i = 0; i < rolesTemp.length; i++){
			if(rolesTemp[i].equalsIgnoreCase("administrator")  
								|| rolesTemp[i].equalsIgnoreCase("superadministrator")
								|| rolesTemp[i].equalsIgnoreCase("admin")){
				return true;
			}				
		}
		return false;*/
		return isUserAdmin(state); 
	}
	
//mark ma added ****************************************	
	public static boolean isUserAdmin(StateInterface state){
		
		UserRole [] rolesTemp = state.getServiceManager().getAllUserRolesForCurrentUser();		
		for(int i = 0; i < rolesTemp.length; i++){
			if(rolesTemp[i].getName().equalsIgnoreCase("administrator")  
								|| rolesTemp[i].getName().equalsIgnoreCase("superadministrator")
								|| rolesTemp[i].getName().equalsIgnoreCase("admin")){
				return true;
			}				
		}
		return false;
	}	
	public static BioCollectionBean getUserRoleCollection(StateInterface state){
		UserRole [] rolesTemp = state.getServiceManager().getAllUserRolesForCurrentUser();		
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Loading user roles...",100L);
		String qry = " wm_meta_user_role.user_role_name in ('"+rolesTemp[0].getName()+"' ";
		for(int i = 1; i < rolesTemp.length; i++){
			qry += ",'"+rolesTemp[i].getName()+"' ";
		}
		qry=qry+")";
		Query loadBiosQryA = new Query("wm_meta_user_role", qry, "");
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","User Role QRY:"+qry,100L);		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollectionBean roleCollection = uow.getBioCollectionBean(loadBiosQryA);
		return roleCollection;
	}
	public static BioCollectionBean getUserAccessableReports(StateInterface state, String orderBy, String bioQry, DataBean focus) throws EpiDataException{
		
		
		String qry = "";
		String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
		//If no roles present then return an empty BioCollection
		if(rolesTemp == null || rolesTemp.length == 0){
			Query loadBiosQryA = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.SERIALKEY = -1", "");																
			return uow.getBioCollectionBean(loadBiosQryA);
		}						
		
		if(!isUserAdmin(state)){
			BioCollectionBean roleCollection = getUserRoleCollection(state);
			if(roleCollection == null || roleCollection.size() == 0)
				return uow.getBioCollectionBean(new Query("wm_pbsrpt_reports","wm_pbsrpt_reports.RPT_ID = ''",""));				
				
			qry += " wm_reportsrolemapping.ROLEID= '"+roleCollection.elementAt(0).getString("user_role_id")+"' ";
			for(int i = 1; i < roleCollection.size(); i++){
				qry += " OR wm_reportsrolemapping.ROLEID = '"+roleCollection.elementAt(i).getString("user_role_id")+"' ";
			}
			Query loadBiosQryA = new Query("wm_reportsrolemapping", qry, "");																			
			BioCollectionBean reportsRoleMapping = uow.getBioCollectionBean(loadBiosQryA);
			if(reportsRoleMapping == null || reportsRoleMapping.size() == 0)
				return null;
			qry = "wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(0).getString("REPORTID")+"' ";
			for(int i = 0; i < reportsRoleMapping.size(); i++){
				qry += " OR wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(i).getString("REPORTID")+"' ";
			}
			loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);
			BioCollectionBean reports = null;			
			if(focus == null || !focus.isBioCollection() || !((BioCollectionBean)focus).getBioTypeName().equals("wm_pbsrpt_reports")){				
				reports = uow.getBioCollectionBean(loadBiosQryA);
			}
			else{				
				reports = (BioCollectionBean)focus;
				reports.filterInPlace(loadBiosQryA);
			}
			if(bioQry != null && bioQry.length() > 0){
				reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
			}
			return reports;
		}
		Query loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);		
		BioCollectionBean reports = null;
		if(focus == null || !focus.isBioCollection() || !((BioCollectionBean)focus).getBioTypeName().equals("wm_pbsrpt_reports")){
			reports = uow.getBioCollectionBean(loadBiosQryA);
		}
		else{
			reports = (BioCollectionBean)focus;
			reports.filterInPlace(loadBiosQryA);
		}		
		if(bioQry != null && bioQry.length() > 0){
			reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
		}
		return reports;
	}
	
	/**
	 * 
	 * @param state
	 * @param user
	 * @param rolesTemp 
	 * @return
	 * 
	 * AW		08/27/10		Defect:280578 Incident:3946801
	 * HC		03/07/10	Doesn't work with ADS authentication. changed call to 'getAllRolesForUserId'
	 * 						with 'getOAUserRolesForUserId' 
	 */
	public static boolean isUserAdminForThisUser(StateInterface state, String user, String[] rolesTemp){
		//String [] rolesTemp = state.getServiceManager().getAllRolesForUserId(user);
//		String [] rolesTemp =state.getServiceManager().getOAUserRolesForUserId(user, true);
		for(int i = 0; i < rolesTemp.length; i++){
			if(rolesTemp[i].equalsIgnoreCase("administrator")  
								|| rolesTemp[i].equalsIgnoreCase("superadministrator")
								|| rolesTemp[i].equalsIgnoreCase("admin")){
				return true;
			}				
		}
		return false;
	}	
/**
 * 
 * @param state
 * @param user
 * @param roles 
 * @return
 * AW		08/27/10		Defect:280578 Incident:3946801
 * HC		03/07/10	Doesn't work with ADS authentication. changed call to 'getAllRolesForUserId'
 * 						with 'getOAUserRolesForUserId'
 */
	public static BioCollectionBean getUserRoleCollectionForThisUser(StateInterface state, String user, String[] rolesTemp){		
		//String [] rolesTemp = state.getServiceManager().getAllRolesForUserId(user);
//		String [] rolesTemp = state.getServiceManager().getOAUserRolesForUserId(user, true);
		
		
		String qry = " wm_meta_user_role.user_role_name in ('"+rolesTemp[0]+"' ";
		for(int i = 1; i < rolesTemp.length; i++){
			qry += ",'"+rolesTemp[i]+"' ";
		}
		qry=qry+")";
		Query loadBiosQryA = new Query("wm_meta_user_role", qry, "");
				
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollectionBean roleCollection = uow.getBioCollectionBean(loadBiosQryA);
		return roleCollection;
	}
//end ****************************************************	
	
}