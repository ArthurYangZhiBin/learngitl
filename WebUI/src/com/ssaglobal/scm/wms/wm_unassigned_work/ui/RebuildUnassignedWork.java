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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_assigned_work.ui.AWSearch;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class RebuildUnassignedWork extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(RebuildUnassignedWork.class);

	HashSet foundSet;

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

		AWSearch queryParameters = (AWSearch) userContext.get("UNASSNWORKQUERY");
		if (queryParameters == null)
		{
			throw new UserException("WMEXP_AW_NORESULTS", new Object[] {});
		}

		String waveKey = queryParameters.getWaveKey();
		String assignmentNumber = queryParameters.getAssignmentNumber();
		String shipmentOrderNumber = queryParameters.getShipmentOrderNumber();
		String route = queryParameters.getRoute();
		String stop = queryParameters.getStop();

		String userId = (String) context.getServiceManager().getUserContext().get("logInUserId");

		//Construct WHERE clause for querying view
		String qry = createWhereClause(waveKey, assignmentNumber, shipmentOrderNumber, route, stop);
		_log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work select qry=" + qry, SuggestedCategory.NONE);

		EXEDataObject sqlResults = WmsWebuiValidationSelectImpl.select(qry);

		Query loadBiosQry = new Query("WM_unassignwork", "WM_unassignwork.USERID = '" + userId + "'", "");
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean bioResults = uow.getBioCollectionBean(loadBiosQry); //copy to ArrayList
		ArrayList bioResultsList = copyToArrayList(bioResults); //bioResultsList contains a list of BioBeans from the Temp Table
		foundSet = new HashSet();
		{
			String wavekey = null;
			String assignmentnumber = null;
			String priority = null;
			String cases = null;
			String pieces = null;
			String weight = null;
			String cube = null;
			Object tempObj = null;
			StringBuffer insertQry = null;

			for (int i = 0; i < sqlResults.getRowCount(); i++)
			{
				insertQry = new StringBuffer();
				tempObj = sqlResults.getAttribValue(new TextData("wavekey"));
				wavekey = tempObj != null ? tempObj.toString() : null;
				tempObj = sqlResults.getAttribValue(new TextData("assignmentnumber"));
				assignmentnumber = tempObj != null ? tempObj.toString() : null;
				tempObj = sqlResults.getAttribValue(new TextData("priority"));
				priority = tempObj != null ? tempObj.toString() : null;
				tempObj = sqlResults.getAttribValue(new TextData("cases"));
				cases = tempObj != null ? round(tempObj.toString()) : null;
				tempObj = sqlResults.getAttribValue(new TextData("pieces"));
				pieces = tempObj != null ? round(tempObj.toString()) : null;
				tempObj = sqlResults.getAttribValue(new TextData("totalWeight"));
				weight = tempObj != null ? round(tempObj.toString()) : null;
				tempObj = sqlResults.getAttribValue(new TextData("totalCube"));
				cube = tempObj != null ? round(tempObj.toString()) : null;

				if (existsInBioCollection(bioResultsList, wavekey, assignmentnumber, priority, cases, pieces, weight, cube) == false)
				{
					//if not found - insert
					insertQry = insertQry.append("INSERT INTO UNASSIGNWORK (WAVEKEY, ASSIGNMENTNUMBER, PRIORITY, CASES, PIECES, "
							+ " WEIGHT, CUBE, USERID, ORDERKEY, ROUTE, STOP) VALUES(");
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
					_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "Inserting " + insertQry, SuggestedCategory.NONE);
					if (insertQry.toString() != null && insertQry.toString() != "")
					{
						WmsWebuiValidationInsertImpl.insert(insertQry.toString());
					}
				}

				sqlResults.getNextRow();
			}
		}

		//Remove Phase
		_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "Entering Remove Phase", SuggestedCategory.NONE);
		for (int i = 0; i < bioResults.size(); i++)
		{
			BioBean current = bioResults.get(String.valueOf(i));
			if (foundSet.contains(current.getBioRef().getBioRefString()))
			{
				_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "Skipping "
						+ current.getBioRef().getBioRefString(), SuggestedCategory.NONE);
				continue;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "Deleting "
						+ current.getBioRef().getBioRefString(), SuggestedCategory.NONE);
				current.delete();
			}
		}

		uow.saveUOW(true); //save changes

		//Requery and set focus
		result.setFocus(uow.getBioCollectionBean(loadBiosQry));

		return RET_CONTINUE;
	}

	private void buildInsertQuery(Integer priority, StringBuffer insertQry)
	{
		buildInsertQuery(priority.toString(), insertQry);

	}

	private ArrayList copyToArrayList(BioCollectionBean collection) throws EpiDataException
	{
		ArrayList list = new ArrayList(collection.size());
		for (int i = 0; i < collection.size(); i++)
		{
			list.add(collection.get(String.valueOf(i)));
		}
		return list;
	}

	private boolean existsInBioCollection(ArrayList bioResults, String wavekey, String assignmentnumber, String priority, String cases, String pieces, String weight, String cube) throws EpiDataException
	{
		final Integer priorityInt = new Integer(priority);
		for (int i = 0; i < bioResults.size(); i++)
		{
			BioBean current = (BioBean) bioResults.get((i));
			String bWaveKey = (String) current.get("WAVEKEY");
			String bAssignmentNumber = (String) current.get("ASSIGNMENTNUMBER");
			Integer bPriority = (Integer) current.get("priority");
			String bCases = round(current.get("CASES"));
			String bPieces = round(current.get("PIECES"));
			String bWeight = round(current.get("WEIGHT"));
			String bCube = round(current.get("CUBE"));
			//_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "BIO " + bWaveKey + " " + bAssignmentNumber + " "					+ bCases + " " + bPieces + " " + bWeight + " " + bCube, SuggestedCategory.NONE);
			//_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "SQL " + wavekey + " " + assignmentnumber + " "					+ cases + " " + pieces + " " + weight + " " + cube, SuggestedCategory.NONE);
			if (wavekey.equals(bWaveKey) && assignmentnumber.equals(bAssignmentNumber) && priorityInt.equals(bPriority) && cases.equals(bCases)
					&& pieces.equals(bPieces) && weight.equals(bWeight) && cube.equals(bCube))
			{
				_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "Found "
						+ current.getBioRef().getBioRefString(), SuggestedCategory.NONE);
				foundSet.add(current.getBioRef().getBioRefString());
				bioResults.remove(i);
				return true;
			}
		}
		return false;
	}

	private String round(Object attribute)
	{
		String roundedNumber;
		if (attribute != null)
		{
			
			attribute = sanitizeSqlResult(attribute);
			
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			nf.setGroupingUsed(false);

			try
			{
				roundedNumber = nf.format(nf.parse(attribute.toString()));
			} catch (ParseException e)
			{
				roundedNumber = (String) attribute;
			}
		}
		else
		{
			return (String) attribute;
		}

		return roundedNumber;
	}

	private Object sanitizeSqlResult(Object attribute)
	{
		if(attribute.toString().equals("N/A"))
		{
			_log.debug("LOG_DEBUG_EXTENSION_RebuildUnassignedWork", "SQL Result contains N/A, setting to 0 - " + attribute, SuggestedCategory.NONE);
			attribute = "0";
		}
		return attribute;
	}

	/*
	 protected int aexecute(ActionContext context, ActionResult result) throws EpiException
	 {
	 EpnyUserContext userContext = context.getServiceManager().getUserContext();

	 AWSearch queryParameters = (AWSearch) userContext.get("UNASSNWORKQUERY");
	 if (queryParameters == null)
	 {
	 throw new UserException("WMEXP_AW_NORESULTS", new Object[] {});
	 }

	 String waveKey = queryParameters.getWaveKey();
	 String assignmentNumber = queryParameters.getAssignmentNumber();
	 String shipmentOrderNumber = queryParameters.getShipmentOrderNumber();
	 String route = queryParameters.getRoute();
	 String stop = queryParameters.getStop();

	 String userId = (String) context.getServiceManager().getUserContext().get("logInUserId");

	 //		Delete Template Tables
	 try
	 {
	 WmsWebuiValidationDeleteImpl.delete("DELETE FROM UNASSIGNWORK WHERE USERID = '" + userId + "'");
	 System.out.println("%@@Unassigned work delete qry=" + "DELETE FROM UNASSIGNWORK WHERE USERID = '" + userId
	 + "'");
	 } catch (DPException e1)
	 {
	 e1.printStackTrace();
	 throw new UserException("WMEXP_UNASSIGNWORK_DELETE", new Object[1]);
	 }

	 //Construct WHERE clause for querying view
	 String qry = createWhereClause(waveKey, assignmentNumber, shipmentOrderNumber, route, stop);
	 _log.debug("LOG_DEBUG_EXTENSION", "%@@Unassigned work select qry=" + qry, SuggestedCategory.NONE);

	 try
	 {
	 EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
	 _log.debug("LOG_DEBUG_EXTENSION", "\n\nInserting into unassignwork,  Bio Collection of size:" + collection.getRowCount(, SuggestedCategory.NONE)
	 + "\n\n");
	 String wavekey = null;
	 String assignmentnumber = null;
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
	 tempObj = collection.getAttribValue(new TextData("cases"));
	 cases = tempObj != null ? tempObj.toString() : null;
	 tempObj = collection.getAttribValue(new TextData("pieces"));
	 pieces = tempObj != null ? tempObj.toString() : null;
	 tempObj = collection.getAttribValue(new TextData("totalWeight"));
	 weight = tempObj != null ? tempObj.toString() : null;
	 tempObj = collection.getAttribValue(new TextData("totalCube"));
	 cube = tempObj != null ? tempObj.toString() : null;
	 insertQry = insertQry.append("INSERT INTO UNASSIGNWORK (WAVEKEY, ASSIGNMENTNUMBER, CASES, PIECES, "
	 + " WEIGHT, CUBE, USERID, ORDERKEY, ROUTE, STOP) VALUES(");
	 buildInsertQuery(wavekey, insertQry);
	 buildInsertQuery(assignmentnumber, insertQry);
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
	 */
	private String createWhereClause(String waveKey, String assignmentNumber, String shipmentOrderNumber, String route, String stop)
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
		//Query View
		String qry = "select distinct wavekey, assignmentnumber, priority, sum(case when uom=2 then uomQty end) cases, "
				+ " sum(case when uom=6 then uomQty when uom=7 then uomQty end) pieces, sum(totalWeight) totalWeight, sum(totalCube) totalCube "
				+ " from vUnassigned_Work_Details ";

		if (whereClause != null && !"".equalsIgnoreCase(whereClause.toString()))
		{
			qry = qry + " where " + whereClause.toString();
		}
		qry = qry + " group by wavekey, assignmentnumber, priority";
		return qry;
	}

	private void buildInsertQueryRounding(String attribute, StringBuffer insertQry)
	{
		if (attribute != null)
		{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			nf.setGroupingUsed(false);
			String roundedNumber;
			try
			{
				roundedNumber = nf.format(nf.parse(attribute));
			} catch (ParseException e)
			{
				roundedNumber = attribute;
			}
			//_log.debug("LOG_DEBUG_EXTENSION", "\n\n Original: " +  attribute + " \t Rounded: " + roundedNumber, SuggestedCategory.NONE);
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
