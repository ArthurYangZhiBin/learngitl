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

package com.ssaglobal.scm.wms.wm_unassigned_work.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_assigned_work.ui.AWSearch;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UnassignedWorkQuery extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnassignedWorkQuery.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		StateInterface state = context.getState();

		int status = getParameterInt("STATUS");

		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioBean selectedRecord = null;
		try
		{
			selectedRecord = uowb.getBioBean(bioRef);

		} catch (BioNotFoundException bioEx)
		{
			_logger.error(bioEx);
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}

		//get values from the selected record
		AWSearch queryParameters = (AWSearch) userContext.get("UNASSNWORKQUERY");
		
		if (queryParameters == null)
		{
			throw new UserException("WMEXP_AW_NORESULTS", new Object[] {});
		}

//		String waveKey = queryParameters.getWaveKey();
//		String assignmentNumber = queryParameters.getAssignmentNumber();
//		String orderKey = queryParameters.getShipmentOrderNumber();
//		String route = queryParameters.getRoute();
//		String stop = queryParameters.getStop();
		Object assignmentNumber = selectedRecord.getValue("ASSIGNMENTNUMBER");
		Object waveKey = selectedRecord.getValue("WAVEKEY");
		Object orderKey = selectedRecord.getValue("ORDERKEY");
		Object route = selectedRecord.getValue("ROUTE");
		Object stop = selectedRecord.getValue("STOP");
		Object oldPriority = selectedRecord.getValue("PRIORITY", true);
//		
		//query UserActivity for the PickDetailKey
		//		String query = "SELECT PICKDETAILKEY FROM USERACTIVITY WHERE ASSIGNMENTNUMBER = '" + assignmentNumber
		//				+ "' AND WAVEKEY = '" + waveKey + "' AND STATUS = '" + status + "'";
		//		if (status == 1)
		//		{
		//			Object userid = selectedRecord.getValue("USERID");
		//			//append userid
		//			query += " AND USERID = '" + userid + "'";
		//		}
		//		_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
		//		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		String awQuery = "wm_useractivity_bio.STATUS = '0' and wm_useractivity_bio.ASSIGNMENTNUMBER = '" +
							assignmentNumber +
							"' and wm_useractivity_bio.WAVEKEY = '" +
							waveKey +
							"' and wm_useractivity_bio.PRIORITY = '" +
							oldPriority +
							"'";
		_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkUpdatePriority_execute", awQuery, SuggestedCategory.NONE);
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean results = tuow.getBioCollectionBean(new Query("wm_useractivity_bio", awQuery, null));

		if (results.size() == 0)
		{
			//return empty biocollection
			Query filterQuery = new Query("wm_pickdetail", "wm_pickdetail.PICKDETAILKEY = 'null'", "");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollection bioCollection = uow.getBioCollectionBean(filterQuery);
			try
			{
				_log.debug("LOG_DEBUG_EXTENSION", "%@@total number of columns =" + bioCollection.size(), SuggestedCategory.NONE);
				result.setFocus((DataBean) bioCollection);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			result.setFocus((DataBean) bioCollection);
			return RET_CONTINUE;
		}

		//a wavekey and assignmentnumber reference >=1 pickdetailkey
		ArrayList pickDetailKeys = new ArrayList();
		for (int i = 0; i < results.size(); i++)
		{
			pickDetailKeys.add(results.get("" + i).getString("PICKDETAILKEY"));
		}

		//construct Query
		StringBuffer pickQuery = new StringBuffer();
		pickQuery.append("(");
		for (int i = 0; i < pickDetailKeys.size(); i++)
		{
			if (i > 0)
			{
				pickQuery.append(" or ");
			}
			pickQuery.append(" wm_pickdetail.PICKDETAILKEY = '" + pickDetailKeys.get(i) + "' ");
		}
		pickQuery.append(")");

		if (orderKey != null)
		{
			pickQuery.append(" and wm_pickdetail.ORDERKEY = '" + orderKey + "' ");
		}
		if (route != null)
		{
			pickQuery.append(" and wm_pickdetail.ORDERROUTE = '" + route + "' ");
		}
		if (stop != null)
		{
			pickQuery.append(" and wm_pickdetail.ORDERSTOP = '" + stop + "' ");
		}

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + pickQuery + "\n", SuggestedCategory.NONE);
		Query filterQuery = new Query("wm_pickdetail", pickQuery.toString(), "");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollection bioCollection = uow.getBioCollectionBean(filterQuery);
		try
		{
			_log.debug("LOG_DEBUG_EXTENSION", "%@@total number of columns =" + bioCollection.size(), SuggestedCategory.NONE);
			result.setFocus((DataBean) bioCollection);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		result.setFocus((DataBean) bioCollection);

		return RET_CONTINUE;
	}

}
