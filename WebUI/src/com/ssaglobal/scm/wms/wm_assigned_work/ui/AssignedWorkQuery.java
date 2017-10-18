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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AssignedWorkQuery extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignedWorkQuery.class);

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

		int status = getParameterInt("STATUS");
		/*
		 * String bioRefString = state.getBucketValueString("listTagBucket");
		 * BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		 * 
		 * // BioCollection bioCollectionA = uow.findByQuery(new
		 * Query("WM_assignwork", null, null));
		 * //
		 * _log.debug("LOG_SYSTEM_OUT","****** size ="+bioCollectionA.size(),100L
		 * );
		 * 
		 * //rebuild BIOCOLLECTION
		 * AWSearch searchP = (AWSearch)
		 * context.getServiceManager().getUserContext().get("AWSEARCHP");
		 * if (searchP == null)
		 * {
		 * throw new UserException("WMEXP_AW_NORESULTS", new Object[] {});
		 * }
		 * String waveKey = searchP.getWaveKey();
		 * String assignmentNumber = searchP.getAssignmentNumber();
		 * String shipmentOrderNumber = searchP.getShipmentOrderNumber();
		 * 
		 * String route = searchP.getRoute();
		 * String stop = searchP.getStop();
		 * String searchUserId = searchP.getUserid();
		 * String loginId = (String)
		 * context.getServiceManager().getUserContext().get("logInUserId");
		 * // Construct WHERE clause for querying view
		 * String qry = buildSearchQuery(waveKey, assignmentNumber,
		 * shipmentOrderNumber, route, stop, searchUserId);
		 * _log.debug("LOG_SYSTEM_OUT","qry="+qry,100L);
		 * HelperBio helper = uow.createHelperBio("WM_assignwork");
		 * ArrayListBioRefSupplier newBioList = new
		 * ArrayListBioRefSupplier("WM_assignwork");
		 * 
		 * try
		 * {
		 * EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
		 * _log.debug("LOG_DEBUG_EXTENSION",
		 * "Inserting into assignwork,  Bio Collection of size:"
		 * + collection.getRowCount(), SuggestedCategory.NONE);
		 * String wavekey = null;
		 * String assignmentnumber = null;
		 * String cases = null;
		 * String pieces = null;
		 * String weight = null;
		 * String cube = null;
		 * String userid = null;
		 * Object tempObj = null;
		 * StringBuffer insertQry = null;
		 * 
		 * //Get all USERIDs first, then query LDAP for all at once
		 * HashMap name;
		 * name = retrieveLDAPUsers(context, result, collection);
		 * collection.setRow(0);
		 * //Insert Data from View into Temp BIO
		 * for (int i = 0; i < collection.getRowCount(); i++)
		 * {
		 * insertQry = new StringBuffer();
		 * tempObj = collection.getAttribValue(new TextData("wavekey"));
		 * wavekey = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new
		 * TextData("assignmentnumber"));
		 * assignmentnumber = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new TextData("cases"));
		 * cases = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new TextData("pieces"));
		 * pieces = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new TextData("totalWeight"));
		 * weight = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new TextData("totalCube"));
		 * cube = tempObj != null ? tempObj.toString() : null;
		 * tempObj = collection.getAttribValue(new TextData("userid"));
		 * userid = tempObj != null ? tempObj.toString() : null;
		 * 
		 * 
		 * Bio temp = uow.createBio(helper);
		 * 
		 * temp.set("WAVEKEY", wavekey);
		 * temp.set("ASSIGNMENTNUMBER", assignmentnumber);
		 * temp.set("CASES", round(cases));
		 * temp.set("PIECES", round(pieces));
		 * temp.set("WEIGHT", round(weight));
		 * temp.set("CUBE", round(cube));
		 * _log.debug("LOG_SYSTEM_OUT","****** in i="+i+
		 * "   ***********************10000",100L);
		 * temp.set("LOGINID", loginId);
		 * _log.debug("LOG_SYSTEM_OUT","****** in i="+i+
		 * "   ***********************11111",100L);
		 * temp.set("USERID", userid);
		 * //Need to set FNAME and LNAME
		 * _log.debug("LOG_SYSTEM_OUT","****** in i="+i+
		 * "   ***********************10111",100L);
		 * temp.set("FNAME", ((Name) name.get(userid)).first);
		 * temp.set("LNAME", ((Name) name.get(userid)).last);
		 * //
		 * _log.debug("LOG_SYSTEM_OUT","****** in i="+i+
		 * "   ***********************11222",100L);
		 * temp.set("ORDERKEY", shipmentOrderNumber);
		 * temp.set("ROUTE", route);
		 * temp.set("STOP", stop);
		 * //temp.set("STOP", stop == null || ((String)
		 * stop).toString().matches("") ? null : Integer.valueOf(stop));
		 * 
		 * newBioList.add(temp.getBioRef());
		 * 
		 * collection.getNextRow();
		 * }
		 * } catch (DPException e1)
		 * {
		 * e1.printStackTrace();
		 * throw new UserException("WMEXP_UNASSIGNWORK_SELECT_INSERT", new
		 * Object[1]);
		 * }
		 * 
		 * BioCollection bc = uow.fetchBioCollection(newBioList);
		 * BioCollectionBean returnList =
		 * uowb.getBioCollection(bc.getBioCollectionRef());
		 * 
		 * ArrayList brList = returnList.getBioRefs();
		 * boolean toggle = true;
		 * int index = 0;
		 * while (toggle)
		 * {
		 * if (brList.get(index).equals(bioRef.getBioRefString()))
		 * {
		 * toggle = false;
		 * }
		 * else
		 * {
		 * index++;
		 * //Data has changed and unable to match selected record
		 * if (index == brList.size())
		 * {
		 * throw new UserException("WMEXP_AW_NORESULTS", new Object[] {});
		 * }
		 * }
		 * }
		 * 
		 * BioBean selectedRecord = (BioBean) returnList.get("" + index);
		 */
		
		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
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
		Object selAssignmentNumber = selectedRecord.getValue("ASSIGNMENTNUMBER");
		Object selWaveKey = selectedRecord.getValue("WAVEKEY");
		Object selOrderKey = selectedRecord.getValue("ORDERKEY");
		Object selRoute = selectedRecord.getValue("ROUTE");
		Object selStop = selectedRecord.getValue("STOP");
		Object oldPriority = selectedRecord.getValue("PRIORITY", true);
		Object userid = selectedRecord.getValue("USERID");
		//query UserActivity for the PickDetailKey
		//		String query = "SELECT PICKDETAILKEY FROM USERACTIVITY WHERE ASSIGNMENTNUMBER = '" + selAssignmentNumber
		//				+ "' AND WAVEKEY = '" + selWaveKey + "' AND STATUS = '" + status + "'";
		//		if (status == 1)
		//		{
		//			Object userid = selectedRecord.getValue("USERID");
		//			//append userid
		//			query += " AND USERID = '" + userid + "'";
		//		}
		String awQuery = "wm_useractivity_bio.STATUS = '1' and wm_useractivity_bio.ASSIGNMENTNUMBER = '" +
							selAssignmentNumber +
							"' and wm_useractivity_bio.WAVEKEY = '" +
							selWaveKey +
							"' and wm_useractivity_bio.USERID = '" +
							userid +
							"' and wm_useractivity_bio.PRIORITY = '" +
							oldPriority +
							"'";
		_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkUpdatePriority_execute", awQuery, SuggestedCategory.NONE);
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean results = tuow.getBioCollectionBean(new Query("wm_useractivity_bio", awQuery, null));

		//		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		if (results.size() == 0)
		{
			//return empty biocollection
			Query filterQuery = new Query("wm_pickdetail", "wm_pickdetail.PICKDETAILKEY = 'null'", "");
			BioCollection bioCollection = uowb.getBioCollectionBean(filterQuery);
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

		if (!isEmpty(selOrderKey))
		{
			pickQuery.append(" and wm_pickdetail.ORDERKEY = '" + selOrderKey + "' ");
		}
		if (!isEmpty(selRoute))
		{
			pickQuery.append(" and wm_pickdetail.ORDERROUTE = '" + selRoute + "' ");
		}
		if (!isEmpty(selStop))
		{
			pickQuery.append(" and wm_pickdetail.ORDERSTOP = '" + selStop + "' ");
		}

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + pickQuery + "\n", SuggestedCategory.NONE);
		Query filterQuery = new Query("wm_pickdetail", pickQuery.toString(), "");

		BioCollection bioCollection = uowb.getBioCollectionBean(filterQuery);
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
	}

	boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private String sanitizeSqlResult(String attribute)
	{
		if (attribute.equals("N/A"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkQuery", "SQL Result contains N/A, setting to 0 - " + attribute, SuggestedCategory.NONE);
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

	private String buildSearchQuery(String waveKey, String assignmentNumber, String shipmentOrderNumber, String route, String stop, String searchUserid)
	{
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

		//Query View
		String qry = "select distinct wavekey, assignmentnumber, sum(case when uom=2 then uomQty end) cases, "
				+ " sum(case when uom=6 then uomQty when uom=7 then uomQty end) pieces, sum(totalWeight) totalWeight, sum(totalCube) totalCube, "
				+ " userid " + " from vAssigned_Work_Details  ";

		if (whereClause != null && !"".equalsIgnoreCase(whereClause.toString()))
		{
			qry = qry + " where " + whereClause.toString();
		}
		qry = qry + " group by wavekey, assignmentnumber, userid";
		_log.debug("LOG_DEBUG_EXTENSION", "%@@assigned work select qry=" + qry, SuggestedCategory.NONE);
		return qry;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
