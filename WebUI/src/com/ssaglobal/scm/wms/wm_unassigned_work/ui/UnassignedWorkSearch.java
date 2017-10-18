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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_assigned_work.ui.AWSearch;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

//import com.epiphany.shr.util.logging.ILoggerCategory;
//import java.util.Calendar;  
public class UnassignedWorkSearch extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnassignedWorkSearch.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		EpnyUserContext userContext = context.getServiceManager().getUserContext();

		StateInterface state = context.getState();
		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_unassigned_work_template", "wm_unassigned_work_search_view", state);//context.getSourceWidget().getForm();
		String waveKey = getDisplayValue(searchForm, "wavekey").toUpperCase();
		String assignmentNumber = getDisplayValue(searchForm, "assignmentnumber").toUpperCase();
		String shipmentOrderNumber = getDisplayValue(searchForm, "shipmentordernumber").toUpperCase();
		String route = getDisplayValue(searchForm, "route").toUpperCase();
		String stop = getDisplayValue(searchForm, "stop").toUpperCase();

		AWSearch queryParameters = new AWSearch(waveKey, assignmentNumber, shipmentOrderNumber, route, stop);

		userContext.put("UNASSNWORKQUERY", queryParameters);
		String userId = "";
		//Delete Template Tables
		try
		{
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userId = (String) userCtx.get("logInUserId");
			WmsWebuiValidationDeleteImpl.delete("DELETE FROM UNASSIGNWORK WHERE USERID = '" + userId + "'");
			_log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work delete qry="
					+ "DELETE FROM UNASSIGNWORK WHERE USERID = '" + userId + "'", SuggestedCategory.NONE);
		} catch (DPException e1)
		{
			e1.printStackTrace();
			throw new UserException("WMEXP_UNASSIGNWORK_DELETE", new Object[1]);
		}

		//Construct WHERE clause for querying view
		StringBuffer whereClause = new StringBuffer();
		int trace = 0;
		if (!("".equalsIgnoreCase(waveKey)))
		{
			whereClause = whereClause.append(" wavekey like '" + waveKey + "'");
			trace = 1;
		}
		if (!"".equalsIgnoreCase(assignmentNumber))
		{
			if (trace == 0)
			{
				whereClause = whereClause.append(" assignmentnumber like '" + assignmentNumber + "'");
				trace = 1;
			}
			else
			{
				whereClause = whereClause.append(" and assignmentnumber like '" + assignmentNumber + "'");
			}
		}
		if (!("".equalsIgnoreCase(shipmentOrderNumber)))
		{
			if (trace == 0)
			{
				whereClause = whereClause.append(" orderkey like '" + shipmentOrderNumber + "'");
				trace = 1;
			}
			else
			{
				whereClause = whereClause.append(" and orderkey like '" + shipmentOrderNumber + "'");
			}
		}
		if (!("".equalsIgnoreCase(route)))
		{
			if (trace == 0)
			{
				whereClause = whereClause.append(" route like '" + route + "'");
				trace = 1;
			}
			else
			{
				whereClause = whereClause.append(" and route like '" + route + "'");
			}
		}

		if (!("".equalsIgnoreCase(stop)))
		{
			if (trace == 0)
			{
				whereClause = whereClause.append(" stop like '" + stop + "'");
				//whereClause = whereClause.append(" stop = " + new Integer(stop).intValue());
				trace = 1;
			}
			else
			{
				whereClause = whereClause.append(" and stop like '" + stop + "'");
				//whereClause = whereClause.append(" and stop = " + new Integer(stop).intValue());
			}
		}
		
		//Owner Locking Logic
		if(WSDefaultsUtil.isOwnerLocked(state)){
			String lockedOwnersCommaDilimitedString = "";
			ArrayList lockedOwnersList = WSDefaultsUtil.getLockedOwners(state);
			for(int i = 0; i < lockedOwnersList.size(); i++){
				if(i > 0)
					lockedOwnersCommaDilimitedString += ",";
				lockedOwnersCommaDilimitedString += "'"+lockedOwnersList.get(i)+"'";
			}
			if(trace == 0){
				whereClause = whereClause.append(" vUnassigned_Work_Details.ORDERKEY IN (SELECT a.ORDERKEY FROM ORDERS a INNER JOIN PICKDETAIL b ON a.ORDERKEY = b.ORDERKEY WHERE b.STORERKEY IN ("+lockedOwnersCommaDilimitedString+")) ");
				trace = 1;
			}
			else{
				whereClause = whereClause.append(" and vUnassigned_Work_Details.ORDERKEY IN (SELECT a.ORDERKEY FROM ORDERS a INNER JOIN PICKDETAIL b ON a.ORDERKEY = b.ORDERKEY WHERE b.STORERKEY IN ("+lockedOwnersCommaDilimitedString+")) ");
			}
		}
		
		//Query View
		String qry = "select distinct wavekey, assignmentnumber, priority, sum(case when uom=2 then uomQty end) cases, "
				+ " sum(case when uom=6 then uomQty when uom=7 then uomQty end) pieces, sum(totalWeight) totalWeight, sum(totalCube) totalCube "
				+ " from vUnassigned_Work_Details ";

		if (whereClause != null && !"".equalsIgnoreCase(whereClause.toString()))
		{
			qry = qry + " where " + whereClause.toString();
		}
		qry = qry + " group by wavekey, assignmentnumber, priority";
		_log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work select qry=" + qry, SuggestedCategory.NONE);
		//cleaning any existing data from this user ***********************
		try
		{
			EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
			_log.debug("LOG_DEBUG_EXTENSION", "Inserting into unassignwork,  Bio Collection of size:"
					+ collection.getRowCount(), SuggestedCategory.NONE);
			String wavekey = null;
			String assignmentnumber = null;
			String priority = null;
			String cases = null;
			String pieces = null;
			String weight = null;
			String cube = null;
			Object tempObj = null;
			StringBuffer insertQry = null;
			//Insert Data from View into USERACTIVITY Table
			for (int i = 0; i < collection.getRowCount(); i++)
			{
				insertQry = new StringBuffer();
				tempObj = collection.getAttribValue(new TextData("wavekey"));
				wavekey = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("assignmentnumber"));
				assignmentnumber = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("priority"));
				priority = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("cases"));
				cases = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("pieces"));
				pieces = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("totalWeight"));
				weight = tempObj != null ? tempObj.toString() : null;
				tempObj = collection.getAttribValue(new TextData("totalCube"));
				cube = tempObj != null ? tempObj.toString() : null;
				insertQry = insertQry.append("INSERT INTO UNASSIGNWORK (WAVEKEY, ASSIGNMENTNUMBER, PRIORITY, CASES, PIECES, " + " WEIGHT, CUBE, USERID, ORDERKEY, ROUTE, STOP) VALUES(");
				buildInsertQuery(wavekey, insertQry);
				buildInsertQuery(assignmentnumber, insertQry);
				buildInsertQuery(priority, insertQry);
				buildInsertQueryRounding(cases, insertQry); //round
				buildInsertQueryRounding(pieces, insertQry); //round
				buildInsertQueryRounding(weight, insertQry); //round
				buildInsertQueryRounding(cube, insertQry); //round
				insertQry.append("'" + userId + "',");
				//adding on search parameters
				if (!("".equalsIgnoreCase(shipmentOrderNumber)))
				{
					insertQry.append("'" + shipmentOrderNumber + "',");
				}
				else
				{
					insertQry.append("NULL" + ",");
				}
				if (!("".equalsIgnoreCase(route)))
				{
					insertQry.append("'" + route + "',");
				}
				else
				{
					insertQry.append("NULL" + ",");
				}
				if (!("".equalsIgnoreCase(stop)))
				{
					insertQry.append("'" + stop + "')");
				}
				else
				{
					insertQry.append("NULL" + ")");
				}
				_log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work insert qry=" + insertQry, SuggestedCategory.NONE);

				if (insertQry.toString() != null && insertQry.toString() != "")
				{
					WmsWebuiValidationInsertImpl.insert(insertQry.toString());
				}
				collection.getNextRow();
			}
		} catch (DPException e1)
		{
			e1.printStackTrace();
			throw new UserException("WMEXP_UNASSIGNWORK_SELECT_INSERT", new Object[1]);
		}
		_log.debug("LOG_DEBUG_EXTENSION", "%@@Create query object******WWWW", SuggestedCategory.NONE);
		Query loadBiosQry = new Query("WM_unassignwork", "WM_unassignwork.USERID = '" + userId + "'", "");
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
		try
		{
			_log.debug("LOG_DEBUG_EXTENSION", "%@@total number of columns =" + bioCollection.size(), SuggestedCategory.NONE);
			result.setFocus((DataBean) bioCollection);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return RET_CONTINUE;

	}

	private void buildInsertQuery(Integer priority, StringBuffer insertQry)
	{
		buildInsertQuery(priority.toString(), insertQry);

	}

	private String sanitizeSqlResult(String attribute)
	{
		if (attribute.equals("N/A"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "SQL Result contains N/A, setting to 0 - "
					+ attribute, SuggestedCategory.NONE);
			attribute = "0";
		}
		return attribute;
	}

	private void buildInsertQueryRounding(String attribute, StringBuffer insertQry)
	{
		if (attribute != null)
		{
			attribute = sanitizeSqlResult(attribute);
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			nf.setGroupingUsed(false);
			String roundedNumber;
			try
			{
				roundedNumber = nf.format(nf.parse(attribute));
			} catch (ParseException e)
			{
				//				_log.debug("LOG_DEBUG_EXTENSION_UnassignedWorkSearch", "Exception Caught trying to parse " + attribute, SuggestedCategory.NONE);
				//				roundedNumber = "0";
				roundedNumber = attribute;
			}
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n Original: " + attribute + " \t Rounded: " + roundedNumber, SuggestedCategory.NONE);
			insertQry.append("'" + roundedNumber + "',");
		}
		else
		{
			insertQry.append(attribute + ",");
		}

	}

	private void buildInsertQuery(String attribute, StringBuffer insertQry)
	{
		if (attribute != null)
		{
			insertQry.append("'" + attribute + "',");
		}
		else
		{
			insertQry.append(attribute + ",");
		}
	}

	private String getDisplayValue(RuntimeFormInterface searchForm, String widgetName) {
		return searchForm.getFormWidgetByName(widgetName).getDisplayValue() == null ? "" : searchForm.getFormWidgetByName(widgetName).getDisplayValue();
	}

}
