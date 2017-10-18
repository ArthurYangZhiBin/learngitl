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
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class TaskDetailTypeReportTmpExtension  extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected int execute( ActionContext context, ActionResult result ) throws EpiException
	{
		//_log.debug("LOG_SYSTEM_OUT","*******it is in TaskDetailTypeReportTmpExtension's 'Open Task Type Chart Widget' ClickEvent *******",100L);
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");		
		state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");

		try
		{
			//Get the code from codelookup using the tooltip
			String qry1 = "codelkup.LISTNAME = 'TASKTYPE' and codelkup.DESCRIPTION = '"+ tooltip + "'";			
			Query codelkupQry = new Query("codelkup", qry1, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean codeLkupCollection = (BioCollectionBean)uow.getBioCollectionBean(codelkupQry);
			String TaskType = codeLkupCollection.get("0").get("CODE").toString(); // Example: When DESCRIPTION=='Putaways', CODE='PA'

			String qry = "wm_taskdetail.TASKTYPE ='"+ TaskType +"'";		
			if ( WSDefaultsUtil.isOwnerLocked(state) )
			{
				qry = "@[wm_taskdetail.TASKTYPE] =\\'"+ TaskType +"\\'";	
				ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
				if(lockedOwners != null && lockedOwners.size() > 0)
				{
  	   	    		qry += " AND @[wm_taskdetail.STORERKEY] IN ( \\'"+lockedOwners.get(0)+"\\' ";
					for(int i = 1; i < lockedOwners.size(); i++)
					{
  	   	    			qry += " , \\'"+lockedOwners.get(i)+"\\'";
					}
					qry += " ) ";
				}  
  	   	    	qry = ""+qry+"";
  	   	    	qry = "DPE('SQL','"+qry+"')";
			}
			Query loadBiosQry = new Query("wm_taskdetail", qry, null);

			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("wm_taskdetail",null,null));			
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
		} 
		catch (Exception e)
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
			Object args[] = { bioType };
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}
}
