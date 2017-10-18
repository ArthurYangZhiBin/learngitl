package com.ssaglobal.scm.wms.wm_reports.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class ReturnToListViewQuery extends com.epiphany.shr.ui.action.ActionExtensionBase{
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		   String orderBy = getParameterString("orderBy");
		   String bioQry = getParameterString("query");
		   StateInterface state = context.getState();

			String qry = "";
			String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
			//If no roles present then return an empty BioCollection
			if(rolesTemp == null || rolesTemp.length == 0){
				Query loadBiosQryA = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.SERIALKEY = -1", "");																
				result.setFocus(uow.getBioCollectionBean(loadBiosQryA));
				return RET_CONTINUE;
			}						
			
			if(!SecurityUtil.isUserAdmin(state)){
				BioCollectionBean roleCollection = SecurityUtil.getUserRoleCollection(state);
				if(roleCollection == null || roleCollection.size() == 0){
					result.setFocus(uow.getBioCollectionBean(new Query("wm_pbsrpt_reports","wm_pbsrpt_reports.RPT_ID = ''","")));	
					return RET_CONTINUE;
				}
					
				qry += " wm_reportsrolemapping.ROLEID= '"+roleCollection.elementAt(0).getString("user_role_id")+"' ";
				for(int i = 1; i < roleCollection.size(); i++){
					qry += " OR wm_reportsrolemapping.ROLEID = '"+roleCollection.elementAt(i).getString("user_role_id")+"' ";
				}
				Query loadBiosQryA = new Query("wm_reportsrolemapping", qry, "");																			
				BioCollectionBean reportsRoleMapping = uow.getBioCollectionBean(loadBiosQryA);
				if(reportsRoleMapping == null || reportsRoleMapping.size() == 0){
					result.setFocus(null);
					return RET_CONTINUE;
				}
				qry = "wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(0).getString("REPORTID")+"' ";
				for(int i = 0; i < reportsRoleMapping.size(); i++){
					qry += " OR wm_pbsrpt_reports.RPT_ID = '"+reportsRoleMapping.elementAt(i).getString("REPORTID")+"' ";
				}
				loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);
				BioCollectionBean reports = uow.getBioCollectionBean(loadBiosQryA);;			
				reports.filterInPlace(loadBiosQryA);
				if(bioQry != null && bioQry.length() > 0){
					reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
				}
				result.setFocus(reports);
				return RET_CONTINUE;
			}
			Query loadBiosQryA = new Query("wm_pbsrpt_reports", qry, orderBy);		
			BioCollectionBean reports = uow.getBioCollectionBean(loadBiosQryA);
			reports.filterInPlace(loadBiosQryA);
			if(bioQry != null && bioQry.length() > 0){
				reports.filterInPlace(new Query("wm_pbsrpt_reports", bioQry, orderBy));
			}
			result.setFocus(reports);		   
	       return RET_CONTINUE;
	    }
	   
	   
}
