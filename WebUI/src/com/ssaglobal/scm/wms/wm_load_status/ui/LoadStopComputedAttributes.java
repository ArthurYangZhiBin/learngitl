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
package com.ssaglobal.scm.wms.wm_load_status.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.bio.*;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
public class LoadStopComputedAttributes implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadStopComputedAttributes.class);
	public Object get(Bio bio, String attributeName, boolean isOldValue)
			throws EpiDataException
	{
		int numberOfTasks = 0;
		int numberOfTasksCompleted = 0;
		double percentageCompleted = 0.0;
		String loadId = isNull(bio.get("LOADID")) ? "" : bio.get("LOADID").toString();
		String route = isNull(bio.get("ROUTE")) ? "" : bio.get("ROUTE").toString();
		//String status = isNull(bio.get("STATUS")) ? "" : bio.get("STATUS").toString();
		String door = isNull(bio.get("DOOR")) ? "" : bio.get("DOOR").toString();
		String stop = isNull(bio.get("STOP")) ? "" : bio.get("STOP").toString();
		_log.debug("LOG_DEBUG_EXTENSION", "------------" + attributeName +"------------", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n loadid " + loadId, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n route " + route, SuggestedCategory.NONE);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n status " + status, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n door " + door, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n stop " + stop, SuggestedCategory.NONE);

		String query = prepareDetailQuery(loadId, route, door, stop);
	
		_log.debug("LOG_DEBUG_EXTENSION", "QUERY\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		String total = "0"; //6
		String completed = "0"; //7
		String percentCompleted = "0"; //9

		if (results.getRowCount() == 1)
		{
			total = results.getAttribValue(6).getAsString();
			completed = results.getAttribValue(7).getAsString();
			percentCompleted = results.getAttribValue(9).getAsString();
		}

		if ("progress".equalsIgnoreCase(attributeName))
		{
			if (percentCompleted.equals("0"))
			{
				return "N/A";
			}
			else
			{
				return percentCompleted + "%";
			}
		}

		if ("numberoftasks".equalsIgnoreCase(attributeName))
		{
			return total;
			//			BioCollection taskdetailCollection = bio.getBioCollection("taskdetail");
			//			numberOfTasks = taskdetailCollection.size(false);
			//			return new Integer(numberOfTasks);
		}
		if ("numberoftaskscompleted".equalsIgnoreCase(attributeName))
		{
			return completed;
			//			numberOfTasksCompleted = bio.getBioCollection("taskdetail_completed").size(false);
			//			return new Integer(numberOfTasksCompleted);
		}
		if ("percentagecompleted".equalsIgnoreCase(attributeName))
		{
			if (percentCompleted.equals("0"))
			{
				return "N/A";
			}
			else
			{
				return percentCompleted + "%";
			}
			//			BioCollection taskdetailCollection = bio.getBioCollection("taskdetail");
			//			numberOfTasks = taskdetailCollection.size(false);
			//
			//			numberOfTasksCompleted = bio.getBioCollection("taskdetail_completed").size(false);
			//			if (numberOfTasks == 0)
			//			{
			//				return "N/A";
			//			}
			//			else
			//			{
			//				percentageCompleted = numberOfTasksCompleted / (double) numberOfTasks * 100;
			//				return String.valueOf(percentageCompleted) + "%";
			//			}
		}
		return null;
	}

	private String prepareDetailQuery(String loadId, String route, String door, String stop)
	{
		//jp.sdis.7317.begin
		//Changes in CASE clause for compatibility with Oracle using NVARCHAR2 data type
		/*

		String query = "SELECT loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime, COUNT(*) 'Total', SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) 'completed', COUNT(*) - SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) 'notcompleted', (100 * SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) / COUNT(*)) 'percentcompleted' "
				+ "FROM loadhdr, taskdetail, loadunitdetail, loadstop "
				+ "WHERE taskdetail.tasktype = 'LD' AND taskdetail.sourcekey = loadunitdetail.loadunitdetailid AND loadunitdetail.loadstopid = loadstop.loadstopid AND loadstop.loadid = loadhdr.loadid "
				+ "GROUP BY loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime, loadstop.stop ";
        */
		String query = "SELECT loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime, " +
				"COUNT(*) \"Total\", " +
				"SUM(CASE WHEN taskdetail.Status = '9' THEN 1 ELSE 0 END) \"completed\", " +
				"COUNT(*) - SUM(CASE WHEN taskdetail.Status = '9' THEN 1 ELSE 0 END) \"notcompleted\", " +
				"(100 * SUM(CASE WHEN taskdetail.Status = '9' THEN 1 ELSE 0 END) / COUNT(*)) \"percentcompleted\" "
			+ "FROM loadhdr, taskdetail, loadunitdetail, loadstop "
			+ "WHERE taskdetail.tasktype = 'LD' AND taskdetail.sourcekey = loadunitdetail.loadunitdetailid AND loadunitdetail.loadstopid = loadstop.loadstopid AND loadstop.loadid = loadhdr.loadid "
			+ "GROUP BY loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime, loadstop.stop ";
		//jp.sdis.7317.end

		
		query += "HAVING ";

		if (loadId.equals(""))
		{
			query += "loadhdr.loadid is null ";
		}
		else
		{
			query += "loadhdr.loadid = '" + loadId + "' ";
		}

		query += "AND ";
		if (route.equals(""))
		{
			query += "loadhdr.route is null ";
		}
		else
		{
			query += "loadhdr.route = '" + route + "' ";
		}

		query += "AND ";
		if (door.equals(""))
		{
			query += "loadhdr.door is null ";
		}
		else
		{
			query += "loadhdr.door = '" + door + "' ";
		}

		query += "AND ";
		if (stop.equals(""))
		{
			query += "loadstop.stop is null ";
		}
		else
		{
			query += "loadstop.stop = '" + stop + "' ";
		}
		return query;
	}

	private String prepareHeaderQuery(String loadId, String route, String door)
	{
		String query = "SELECT loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime, COUNT(*) 'Total', SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) 'completed', COUNT(*) - SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) 'notcompleted', (100 * SUM(CASE taskdetail.Status WHEN '9' THEN 1 ELSE 0 END) / COUNT(*)) 'percentcompleted'"
				+ "FROM loadhdr, taskdetail, loadunitdetail, loadstop "
				+ "WHERE taskdetail.tasktype = 'LD' AND taskdetail.sourcekey = loadunitdetail.loadunitdetailid AND loadunitdetail.loadstopid = loadstop.loadstopid AND loadstop.loadid = loadhdr.loadid "
				+ "GROUP BY loadhdr.loadid, loadhdr.route, loadhdr.status, loadhdr.door, loadhdr.departuretime";
		query += "HAVING ";

		if (loadId.equals(""))
		{
			query += "loadhdr.loadid is null ";
		}
		else
		{
			query += "loadhdr.loadid = '" + loadId + "' ";
		}

		query += "AND ";
		if (route.equals(""))
		{
			query += "loadhdr.route is null ";
		}
		else
		{
			query += "loadhdr.route = '" + route + "' ";
		}

		query += "AND ";
		if (door.equals(""))
		{
			query += "loadhdr.door is null ";
		}
		else
		{
			query += "loadhdr.door = '" + door + "' ";
		}

		return query;
	}

	public boolean supportsSet(String bioTypeName, String attributeName)
	{
		return true;
	}

	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue)
			throws EpiDataException
	{
		return;
	}

	private boolean isNull(Object attributeValue)
			throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private boolean isEmpty(Object attributeValue)
			throws EpiDataException
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
}
