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

package com.ssaglobal.scm.wms.wm_assigned_work.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
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
import com.ssaglobal.scm.wms.wm_indirect_activity.ui.Name;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AssignedWorkSearch extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignedWorkSearch.class);

	private static final String LDAPNAMEHASH = "LDAPNAMEHASH";

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
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getTempUnitOfWork();
		//		UnitOfWork uow = uowb.getUOW();
		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_assigned_work_template", "wm_assigned_work_search_view", state);//context.getSourceWidget().getForm();
		String waveKey = getDisplayValue(searchForm, "wavekey").toUpperCase();
		String assignmentNumber = getDisplayValue(searchForm, "assignmentnumber").toUpperCase();
		String shipmentOrderNumber = getDisplayValue(searchForm, "orderkey").toUpperCase();
		String route = getDisplayValue(searchForm, "route").toUpperCase();
		String stop = getDisplayValue(searchForm, "stop").toUpperCase();
		String searchUserid = getDisplayValue(searchForm, "userid");

		AWSearch searchP = new AWSearch(waveKey, assignmentNumber, shipmentOrderNumber, route, stop, searchUserid);
		
		context.getServiceManager().getUserContext().put("AWSEARCHP", searchP);

		String loginId = "";
		//Delete Template Tables
		try
		{

			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			loginId = (String) userCtx.get("logInUserId");
			String deleteQuery = "DELETE FROM ASSIGNWORK WHERE LOGINID = '" + loginId + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work delete qry=" + deleteQuery, SuggestedCategory.NONE);
			WmsWebuiValidationDeleteImpl.delete(deleteQuery);

		} catch (Exception e1)
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

		if (!("".equalsIgnoreCase(searchUserid)))
		{
			if (trace == 0)
			{
				whereClause = whereClause.append(" userid like '" + searchUserid + "'");
				trace = 1;
			}
			else
			{
				whereClause = whereClause.append(" and userid like '" + searchUserid + "'");
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
				whereClause = whereClause.append(" vAssigned_Work_Details.ORDERKEY IN (SELECT a.ORDERKEY FROM ORDERS a INNER JOIN PICKDETAIL b ON a.ORDERKEY = b.ORDERKEY WHERE b.STORERKEY IN ("+lockedOwnersCommaDilimitedString+")) ");
				trace = 1;
			}
			else{
				whereClause = whereClause.append(" and vAssigned_Work_Details.ORDERKEY IN (SELECT a.ORDERKEY FROM ORDERS a INNER JOIN PICKDETAIL b ON a.ORDERKEY = b.ORDERKEY WHERE b.STORERKEY IN ("+lockedOwnersCommaDilimitedString+")) ");
			}
		}
		
		//Query View
		//TODO: Need to handle USERID stuff
		String qry = "select distinct wavekey, assignmentnumber, priority, sum(case when uom=2 then uomQty end) cases, "
				+ " sum(case when uom=6 then uomQty when uom=7 then uomQty end) pieces, sum(totalWeight) totalWeight, sum(totalCube) totalCube, "
				+ " userid " + " from vAssigned_Work_Details  ";

		if (whereClause != null && !"".equalsIgnoreCase(whereClause.toString()))
		{
			qry = qry + " where " + whereClause.toString();
		}
		qry = qry + " group by wavekey, assignmentnumber, priority, userid";
		_log.debug("LOG_DEBUG_EXTENSION", "%@@assigned work select qry=" + qry, SuggestedCategory.NONE);

		//		HelperBio helper = uow.createHelperBio("WM_assignwork");
		//		ArrayListBioRefSupplier newBioList = new ArrayListBioRefSupplier("WM_assignwork");

		try
		{
			EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\nInserting into assignwork,  Bio Collection of size:" + collection.getRowCount(), SuggestedCategory.NONE);
			String wavekey = null;
			String assignmentnumber = null;
			String priority = null;
			String cases = null;
			String pieces = null;
			String weight = null;
			String cube = null;
			String userid = null;
			Object tempObj = null;
			StringBuffer insertQry = null;

			//Get all USERIDs first, then query LDAP for all at once
			HashMap name;
			name = retrieveLDAPUsers(context, result, collection);
			collection.setRow(0);
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
				tempObj = collection.getAttribValue(new TextData("userid"));
				userid = tempObj != null ? tempObj.toString() : null;
				/*
				 * Bio temp = uow.createBio(helper);
				 * 
				 * temp.set("WAVEKEY", wavekey);
				 * temp.set("ASSIGNMENTNUMBER", assignmentnumber);
				 * temp.set("PRIORITY", priority);
				 * temp.set("CASES", round(cases));
				 * temp.set("PIECES", round(pieces));
				 * temp.set("WEIGHT", round(weight));
				 * temp.set("CUBE", round(cube));
				 * temp.set("USERID", userid);
				 * //Need to set FNAME and LNAME
				 * temp.set("FNAME", ((Name) name.get(userid)).first);
				 * temp.set("LNAME", ((Name) name.get(userid)).last);
				 * //
				 * temp.set("LOGINID", loginId);
				 * temp.set("ORDERKEY", shipmentOrderNumber);
				 * temp.set("ROUTE", route);
				 * temp.set("STOP", stop);
				 * //temp.set("STOP", stop == null || ((String)
				 * stop).toString().matches("") ? null : Integer.valueOf(stop));
				 * 
				 * newBioList.add(temp.getBioRef());
				 */
				
				insertQry = insertQry.append("INSERT INTO ASSIGNWORK (WAVEKEY, ASSIGNMENTNUMBER, PRIORITY, CASES, PIECES, "
												+ " WEIGHT, CUBE, USERID, FNAME, LNAME, LOGINID, ORDERKEY, ROUTE, STOP) VALUES(");
				buildInsertQuery(wavekey, insertQry);
				buildInsertQuery(assignmentnumber, insertQry);
				buildInsertQuery(priority, insertQry);
				buildInsertQueryRounding(cases, insertQry); //round
				buildInsertQueryRounding(pieces, insertQry); //round
				buildInsertQueryRounding(weight, insertQry); //round
				buildInsertQueryRounding(cube, insertQry); //round
				buildInsertQuery(userid, insertQry);
				insertQry.append("'" + ((Name) name.get(userid)).first + "',");
				insertQry.append("'" + ((Name) name.get(userid)).last + "',");
				insertQry.append("'" + loginId + "',");
				//adding on search parameters
				if (!("".equalsIgnoreCase(shipmentOrderNumber))) {
					insertQry.append("'" + shipmentOrderNumber + "',");
				} else {
					insertQry.append("NULL" + ",");
				}
				if (!("".equalsIgnoreCase(route))) {
					insertQry.append("'" + route + "',");
				} else {
					insertQry.append("NULL" + ",");
				}
				if (!("".equalsIgnoreCase(stop))) {
					insertQry.append("'" + stop + "')");
				} else {
					insertQry.append("NULL" + ")");
				}

				_log.debug("LOG_DEBUG_EXTENSION", "%@@assigned work insert qry=" + insertQry, SuggestedCategory.NONE);

				if (insertQry.toString() != null && insertQry.toString() != "") {
					WmsWebuiValidationInsertImpl.insert(insertQry.toString());
				}

				collection.getNextRow();
			}
		} catch (DPException e1)
		{
			e1.printStackTrace();
			throw new UserException("WMEXP_UNASSIGNWORK_SELECT_INSERT", new Object[1]);
		}
		/*
		 * BioCollection bc = uow.fetchBioCollection(newBioList);
		 * BioCollectionBean returnList =
		 * uowb.getBioCollection(bc.getBioCollectionRef());
		 * result.setFocus(returnList);
		 */

		String bioQuery = "WM_assignwork.LOGINID = '" + loginId + "'";
		_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkSearch_execute", "Query " + bioQuery, SuggestedCategory.NONE);
		Query loadBiosQry = new Query("WM_assignwork", bioQuery, "");
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "PERFORM QUERY" + "\n", SuggestedCategory.NONE);
		BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "AFTER PERFORM QUERY" + "\n", SuggestedCategory.NONE);
		try {
			_log.debug("LOG_DEBUG_EXTENSION", "%@@total number of columns =" + bioCollection.size(), SuggestedCategory.NONE);
			result.setFocus((DataBean) bioCollection);
		} catch (Exception e) {
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "bioCollection is null " + e.getClass().getName() + "\n", SuggestedCategory.NONE);
			e.printStackTrace();
		}

		return RET_CONTINUE;
	}

	private String getDisplayValue(RuntimeFormInterface searchForm, String widgetName) {
		return searchForm.getFormWidgetByName(widgetName).getDisplayValue() == null ? "" : searchForm.getFormWidgetByName(widgetName).getDisplayValue();
	}

	private HashMap retrieveLDAPUsers(ActionContext context, ActionResult result, EXEDataObject collection)
	{
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		return umsInterface.getUsers(context, result, collection);
/*		Object tempObj;
		HashMap name;
		{
			EpnyUserContext userContext = context.getServiceManager().getUserContext();
			if (userContext.containsKey(LDAPNAMEHASH))
			{
				//load hash from context
				name = (HashMap) userContext.get(LDAPNAMEHASH);
			}
			else
			{
				//create hash
				name = new HashMap();
			}
			try
			{
				UserManagementService ums = UMSHelper.getUMS();
				UserStore us = ums.getDefaultUserStore();
				SearchCriteria sc = new SearchCriteria();
				String firstName;
				String lastName;
				boolean search = false;
				for (int i = 0; i < collection.getRowCount(); i++)
				{
					tempObj = collection.getAttribValue(new TextData("userid"));
					String uid = tempObj != null ? tempObj.toString() : null;
					if (!name.containsKey(uid))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t Adding UID to search " + uid + "\n", SuggestedCategory.NONE);
						search = true;
						sc.addCondition(AttributeConstants.ID, uid, com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
						name.put(uid, new Name("", ""));
					}
					collection.getNextRow();
				}
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + ums.getDefaultUserStore().getRootHierarchy().getDN() + "\n", SuggestedCategory.NONE);

				if (search == true)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Querying LDAP..." + "\n", SuggestedCategory.NONE);
					Enumeration searchResults = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1, -1);
					while (searchResults.hasMoreElements())
					{
						com.ssaglobal.cs.ums.api.User user = (com.ssaglobal.cs.ums.api.User) searchResults.nextElement();
						firstName = user.getFirstName();
						lastName = user.getLastName();
						_log.debug("LOG_DEBUG_EXTENSION", "Results " + result + " " + firstName + " " + lastName, SuggestedCategory.NONE);
						name.put(user.getId(), new Name(firstName, lastName));
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();

			}
			userContext.put(LDAPNAMEHASH, name);
		}
		return name;*/
//		return null;
	}

	
	private String sanitizeSqlResult(String attribute)
	{
		if (attribute.equals("N/A"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkSearch", "SQL Result contains N/A, setting to 0 - "
					+ attribute, SuggestedCategory.NONE);
			attribute = "0";
		}
		return attribute;
	}
	
	private String round(String attribute)
	{
		if (attribute != null)
		{
			attribute = sanitizeSqlResult(attribute);
			
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			nf.setMaximumFractionDigits(5);
			nf.setMinimumFractionDigits(5);
			nf.setGroupingUsed(false);
			String roundedNumber;
			try
			{
				roundedNumber = nf.format(nf.parse(attribute));
			} catch (ParseException e)
			{
				roundedNumber = attribute;
			}
			return roundedNumber;
		}
		else
		{
			return attribute;
		}

	}

	private void buildInsertQueryRounding(String attribute, StringBuffer insertQry)
	{
		if (attribute != null)
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "\n\n Original: " +  attribute + " \t Rounded: " + roundedNumber, SuggestedCategory.NONE);
			insertQry.append("'" + round(attribute) + "',");
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

}
