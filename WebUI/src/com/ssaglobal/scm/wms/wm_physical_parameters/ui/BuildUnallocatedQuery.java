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
package com.ssaglobal.scm.wms.wm_physical_parameters.ui;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class BuildUnallocatedQuery extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BuildUnallocatedQuery.class);

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		/*
		String query = "SELECT DISTINCT LOC.loc FROM PHYSICALPARAMETERS,LOC,AREADETAIL,SKUXLOC WHERE (LOC.LOC BETWEEN PHYSICALPARAMETERS.LocMin AND PHYSICALPARAMETERS.locMax ) AND ( LOC.LOCLEVEL BETWEEN PHYSICALPARAMETERS.LEVELMin AND PHYSICALPARAMETERS.LEVELMax ) AND ( LOC.PUTAWAYZONE BETWEEN PHYSICALPARAMETERS.ZONEMin AND PHYSICALPARAMETERS.ZONEMax ) AND ( LOC.PUTAWAYZONE =AREADETAIL.PUTAWAYZONE AND AREADETAIL.AREAKEY BETWEEN PHYSICALPARAMETERS.AREAMin AND PHYSICALPARAMETERS.AREAMax ) AND ( LOC.LOC = SKUXLOC.LOC AND SKUXLOC.STORERKEY BETWEEN PHYSICALPARAMETERS.STORERKEYMin AND PHYSICALPARAMETERS.STORERKEYMax)";
		_log.debug("LOG_SYSTEM_OUT","///QUERY " + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		ArrayList pickDetailLoc = new ArrayList();
		for (int i = 0; i < results.getRowCount(); i++)
		{
			results.getAttribValue(1).getAsString();
			pickDetailLoc.add(results.getAttribValue(1).getAsString());
			results.getNextRow();
		}
		
		//construct Query
		StringBuffer pickQuery = new StringBuffer();
		
//		pickQuery.append("(");
		pickQuery.append("((");//change for defect 150414
		for (int i = 0; i < pickDetailLoc.size(); i++)
		{
			if (i > 0)
			{
				pickQuery.append(" or ");
			}
			pickQuery.append(" wm_pickdetail.LOC = '" + pickDetailLoc.get(i) + "' ");
		}
//		pickQuery.append(")");
		pickQuery.append(") AND (wm_pickdetail.STATUS != '9') )");//change for defect 150414

		_log.debug("LOG_SYSTEM_OUT","\n\t" + pickQuery + "\n",100L);
		Query filterQuery = new Query("wm_pickdetail", pickQuery.toString(), "");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollection bioCollection = uow.getBioCollectionBean(filterQuery);
		try
		{
			_log.debug("LOG_SYSTEM_OUT","%@@total number of columns =" + bioCollection.size(),100L);
			result.setFocus((DataBean) bioCollection);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
		//jp.answerlink.212484.begin
		//Changed to handle other errors besides Allocation
		HttpSession session = state.getRequest().getSession();
		String errorId =
			session.getAttribute(PhysicalParametersConstants.ERROR_ID)==null?null:session.getAttribute(PhysicalParametersConstants.ERROR_ID).toString();
		
		session.removeAttribute(PhysicalParametersConstants.ERROR_ID);
		
		_log.debug("LOG_BLD_UNALLOC_QRY", "Error_id:"+errorId,100L);
		
		BioCollection bioCollection = null;
		if (errorId.equalsIgnoreCase(PhysicalParametersConstants.ERROR_ALLOCATION)){
			bioCollection = getPickdetailFocus(state);
			context.setNavigation("closeModalDialog88");
		}else if (errorId.equalsIgnoreCase(PhysicalParametersConstants.ERROR_PREALLOCATION)){
			bioCollection = getPreallocateFocus(state);
			context.setNavigation("closeModalDialogPre");
		}
			
		//jp.answerlink.212484.end
		

		try
		{
			if(bioCollection==null){
				_log.debug("LOG_BLD_UNALLOC_QRY", "Failed to build focus.", SuggestedCategory.NONE);
				return RET_CANCEL;
			}
				
			_log.debug("LOG_BLD_UNALLOC_QRY","%@@total number of columns =" + bioCollection.size(), SuggestedCategory.NONE);
			
			result.setFocus((DataBean) bioCollection);
		} catch (Exception e)
		{
			_log.debug("LOG_BLD_UNALLOC_QRY","BuildUnallocatedQuery:execute:error"+ e.getMessage(), SuggestedCategory.NONE);
			e.printStackTrace();
		}

		result.setFocus((DataBean) bioCollection);

		return RET_CONTINUE;
	}

	private BioCollection getPickdetailFocus(StateInterface state){
		String query = "SELECT DISTINCT LOC.loc FROM PHYSICALPARAMETERS,LOC,AREADETAIL,SKUXLOC WHERE (LOC.LOC BETWEEN PHYSICALPARAMETERS.LocMin AND PHYSICALPARAMETERS.locMax ) AND ( LOC.LOCLEVEL BETWEEN PHYSICALPARAMETERS.LEVELMin AND PHYSICALPARAMETERS.LEVELMax ) AND ( LOC.PUTAWAYZONE BETWEEN PHYSICALPARAMETERS.ZONEMin AND PHYSICALPARAMETERS.ZONEMax ) AND ( LOC.PUTAWAYZONE =AREADETAIL.PUTAWAYZONE AND AREADETAIL.AREAKEY BETWEEN PHYSICALPARAMETERS.AREAMin AND PHYSICALPARAMETERS.AREAMax ) AND ( LOC.LOC = SKUXLOC.LOC AND SKUXLOC.STORERKEY BETWEEN PHYSICALPARAMETERS.STORERKEYMin AND PHYSICALPARAMETERS.STORERKEYMax)";
		
		_log.debug("LOG_BLD_UNALLOC_QRY", "stmt:"+query,100L);
		
		EXEDataObject results;
		BioCollection bioCollection = null;
		
		try {
			results = WmsWebuiValidationSelectImpl.select(query);
			
			ArrayList pickDetailLoc = new ArrayList();
			for (int i = 0; i < results.getRowCount(); i++)
			{
				results.getAttribValue(1).getAsString();
				pickDetailLoc.add(results.getAttribValue(1).getAsString());
				results.getNextRow();
			}
			
			//construct Query
			StringBuffer pickQuery = new StringBuffer();
			//pickQuery.append("(");
			pickQuery.append("((");//change for defect 150414
			for (int i = 0; i < pickDetailLoc.size(); i++)
			{
				if (i > 0)
				{
					pickQuery.append(" or ");
				}
				pickQuery.append(" wm_pickdetail.LOC = '" + pickDetailLoc.get(i) + "' ");
			}
			//pickQuery.append(")");
			pickQuery.append(") AND (wm_pickdetail.STATUS != '9') )");//change for defect 150414
			
			System.out.println("\n\t" + pickQuery + "\n");
			Query filterQuery = new Query("wm_pickdetail", pickQuery.toString(), "");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			bioCollection = uow.getBioCollectionBean(filterQuery);

		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return bioCollection;
	}

	private BioCollection getPreallocateFocus(StateInterface state){
		String stmt = 
			" SELECT c.serialkey " + 
			" FROM orderdetail c " +  
			" WHERE EXISTS  " + 
				"(SELECT * " +
				" FROM preallocatePickdetail a, physicalParameters b " +
				" WHERE a.storerkey BETWEEN b.storerkeymin AND b.storerkeymax " +
				" AND a.sku BETWEEN b.skumin AND b.skumax " +
				" AND a.qty > 0 " +
				" AND c.orderkey = a.orderkey " +
				" AND c.orderlinenumber = a.orderlinenumber)";

		_log.debug("LOG_BLD_UNALLOC_QRY", "stmt:"+stmt,100L);
		
		BioCollection bioCollection = null;
		
		EXEDataObject results;
		try {
			results = WmsWebuiValidationSelectImpl.select(stmt);
			ArrayList<String> keys = new ArrayList<String>();

			for (int i = 0; i < results.getRowCount(); i++)
			{
				results.getAttribValue(1).getAsString();
				
				keys.add(results.getAttribValue(1).getAsString());
				results.getNextRow();
			}

			//Load data to BioCollection
			StringBuffer whereClause = new StringBuffer( "(");
			
			for (int idx = 0; idx < keys.size(); idx++){
				if (idx > 0){
					whereClause.append(",");
				}
				
				whereClause.append(keys.get(idx));
			}
			whereClause.append(")");
			
			
			Query query = new Query("wm_orderdetail", 
					"wm_orderdetail.SERIALKEY IN " + whereClause.toString() , 
					"wm_orderdetail.ORDERKEY, wm_orderdetail.ORDERLINENUMBER");

			
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			bioCollection = uow.getBioCollectionBean(query);
		} catch (DPException e) {
			// TODO Auto-generated catch block
			_log.debug("LOG_BLD_UNALLOC_QRY", "Error found:"+e.getErrorMessage(), SuggestedCategory.NONE);
			
			e.printStackTrace();
		}

		return bioCollection;

	}

}
