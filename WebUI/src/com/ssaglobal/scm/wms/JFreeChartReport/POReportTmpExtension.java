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
package com.ssaglobal.scm.wms.JFreeChartReport;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class POReportTmpExtension extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(POReportTmpExtension.class);
	protected int execute(ActionContext context, ActionResult result){
		_log.debug("LOG_SYSTEM_OUT","*******it is in drill down SETION $$*****",100L);
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");		
		state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");
 
		try
		{
//Get the code from codelookup using the tooltip
			String qry1 = "codelkup.LISTNAME = 'POSTATUS'and codelkup.DESCRIPTION = '"+ tooltip + "'";
			_log.debug("LOG_SYSTEM_OUT","Status Query" + qry1,100L);
			Query codelkupQry = new Query("codelkup", qry1, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean codeLkupCollection = (BioCollectionBean)uow.getBioCollectionBean(codelkupQry);
			String Statuscode =  codeLkupCollection.get("0").get("CODE").toString();
			
			String qry = "wm_po.STATUS ='"+ Statuscode +"'";	
			if(WSDefaultsUtil.isOwnerLocked(state)){
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		qry += " AND wm_po.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			qry += " , '"+lockedOwners.get(i)+"'";
  	   	    		}
  	   	    		qry += " ) ";
  	   	    	}  	   	    	
  	   	    	ArrayList lockedVendors = WSDefaultsUtil.getLockedVendors(state);
	   	    	if(lockedVendors != null && lockedVendors.size() > 0){
	   	    		qry += " AND (wm_po.SELLERNAME IN ( '"+lockedVendors.get(0)+"' ";
	   	    		for(int i = 1; i < lockedVendors.size(); i++){
	   	    			qry += " , '"+lockedVendors.get(i)+"'";
	   	    		}
	   	    		qry += ") OR wm_po.SELLERNAME IS NULL OR wm_po.SELLERNAME = '' OR wm_po.SELLERNAME = ' ') ";
	   	    	}  	   	    	
  	   	    }
			Query loadBiosQry = new Query("wm_po", qry, null);

			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("wm_po",null,null));			
			QBEBioBean bio = createNewQBE(state,collection.getBioTypeName());
			BioCollectionBean newCollection = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());
			BioCollectionBean newCollectionB = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());		
			newCollectionB.copyFrom(collection);
			collection.copyFrom(newCollection);			
			newCollection = collection;			
			bio.setBaseBioCollectionForQuery(newCollectionB);
			newCollection.setQBEBioBean(bio);						
			newCollection.filterInPlace(loadBiosQry);
			result.setFocus(newCollection);
			return RET_CONTINUE;
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}

}
	
	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		}
		catch(DataBeanException ex)
		{
			Object args[] = {
					bioType
			};
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}
}
