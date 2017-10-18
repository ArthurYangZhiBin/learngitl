/**************************************************************
*                                                             *                          
*                           NOTICE                            *
*                                                             *
*   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             *
*   CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   *
*   OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  *
*   WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       *
*   ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  *
*   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            *
*   ALL OTHER RIGHTS RESERVED.                                *
*                                                             *
*   (c) COPYRIGHT 2011 INFOR.  ALL RIGHTS RESERVED.           *
*   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            *
*   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          *
*   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        *
*   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         *
*   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  *
*                                                             *
***************************************************************/
package com.infor.scm.waveplanning.wp_graphical_filters.util;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.jFreeChart.Datasource.WPJFreeChartDatasource;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;



public class WPGraphFilterUtil{
	private static final String ORDERDETAIL = "orderdetail";

	private static final String ORDERS = "orders";

	private static final String DETAIL_PREFIX = "D_";

	private static final String HEADER_PREFIX = "H_";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphFilterUtil.class);
	
	static Set<String> dateSet = new HashSet<String>();
	static Map<String, String> codelkupMap = new HashMap<String, String>();
	static {
		dateSet.add("H_ACTUALARRIVALDATE");
		dateSet.add("H_ACTUALSHIPDATE");
		dateSet.add("H_ACTUALDELVDATE");
		dateSet.add("H_DepDateTime");
		dateSet.add("H_EDITDATE");
		dateSet.add("H_DELIVERYDATE2");
		dateSet.add("H_EARLIESTDELIVERYDATE");
		dateSet.add("H_EARLIESTSHIPDATE");
		dateSet.add("D_LOTTABLE05");
		dateSet.add("D_LOTTABLE04");
		dateSet.add("H_ADDDATE");
		dateSet.add("H_ORDERDATE");
		dateSet.add("H_PLANNEDSHIPDATE");
		dateSet.add("H_PLANNEDDELVDATE");
		dateSet.add("H_PROMISEDDELVDATE");
		dateSet.add("H_PROMISEDSHIPDATE");
		dateSet.add("H_DELIVERYDATE");
		dateSet.add("H_REQUESTEDSHIPDATE");
		dateSet.add("H_SCHEDULEDDELVDATE");
		dateSet.add("H_SCHEDULEDSHIPDATE");
		
		codelkupMap.put("H_TYPE", "ORDERTYPE");
		codelkupMap.put("H_OHTYPE", "OHT");
	}
	
	public static void cleanTempTable(StateInterface state, String interactionId) throws EpiException{
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK,-1);			
		Query qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"' OR wp_graphicalfilter_temp.DATEADDED < @DATE['"+gc.getTimeInMillis()+"']","");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);		
		if(criteriaCollection != null){		
			for(int i = 0; i < criteriaCollection.size(); i++){
				criteriaCollection.elementAt(i).delete();
			}
		}
		uow.saveUOW();		
	}

	public static void loadTempTable( String criterion, StateInterface state) throws SQLException, EpiException{
		HashMap shipmentOrderTypeMap = null;
		HashMap ohTypeMap = null;	
		Query qry = null;
		if(criterion.equalsIgnoreCase("ITEM")){
			qry = new Query("wm_wp_orderdetail","DPE('SQL','@[orderdetail.SERIALKEY] IN ("+createQuery(criterion, state)+")')","");					
		}
		else if(criterion.equalsIgnoreCase("ITEMGRP")){
			qry = new Query("wm_sku","DPE('SQL','@[sku.SKU] IN ("+createQuery(criterion, state)+")')","");			
		}
		else if(criterion.equalsIgnoreCase("ITEMGRP1")){
			qry = new Query("wm_sku","DPE('SQL','@[sku.SKU] IN ("+createQuery(criterion, state)+")')","");					
		}
		else{
			qry = new Query("wm_wp_orders","DPE('SQL','@[orders.ORDERKEY] IN ("+createQuery(criterion, state)+")')","");
		}
		WPJFreeChartDatasource ds = new WPJFreeChartDatasource();	
		String facility = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
		List orders = ds.runQuery(facility.toUpperCase(), createQuery(criterion, state), new Object[0]);

//		List orders = ds.runQuery("wms4000", createQuery(criterion, state), new Object [0]);			
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT, WPUserUtil.getUserLocale(state));  
		updateGraphicalFilterTempTable(orders, criterion, uow, state);
	}


	private static HashMap buildShipmentOrderMap(StateInterface state) throws EpiDataException{
		HashMap shipmentOrderMap = new HashMap();
		BioCollection orderTypes = WPUtil.getCodeLkupCollection("ORDERTYPE", state);
		for(int i = 0; i < orderTypes.size(); i++){
			shipmentOrderMap.put(orderTypes.elementAt(i).get("CODE"), orderTypes.elementAt(i).get("DESCRIPTION"));
		}
		return shipmentOrderMap;
	}

	private static HashMap buildOrderHandlingTypeMap(StateInterface state) throws EpiDataException{
		HashMap orderTypeMap = new HashMap();
		BioCollection orderTypes = WPUtil.getCodeLkupCollection("OHT", state);
		for(int i = 0; i < orderTypes.size(); i++){
			orderTypeMap.put(orderTypes.elementAt(i).get("CODE"), orderTypes.elementAt(i).get("DESCRIPTION"));
		}
		return orderTypeMap;
	}

	
	/**
	 * Checks if is include orders on.
	 * 
	 * @param state
	 *            the state
	 * @return true, if is include orders on
	 */
	public static boolean isIncludeOrdersOn(StateInterface state) {
		// 8723. Wave Planning - graphical filter (2 issues)
		UnitOfWorkBean uowb = state.getTempUnitOfWork();
		boolean includeOrders = true;
		String qry = "querybuildertemp.INTERACTIONID = '" + state.getInteractionId() + "'";
		try {
			BioCollectionBean headerCollection = uowb.getBioCollectionBean(new Query("querybuildertemp", qry, ""));
			BioBean headerBio = headerCollection.get("" + 0);
			if (headerBio.getValue("INCLUDEORDERS") == null || "0".equals(headerBio.getString("INCLUDEORDERS"))) {
				includeOrders = false;
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return includeOrders;
	}
	

	/**
	 * Creates the query.
	 * 
	 * @param criterion
	 *            the criterion
	 * @param state
	 *            the state
	 * @return the string
	 */
	public static String createQuery(String criterion, StateInterface state) {		
			String reportSelectClause = createSelectClause(criterion, false, state);
			
			String reportFromClause = createFromClause(criterion);
	
			// 8723. Wave Planning - graphical filter (2 issues)
			boolean includeOrders = isIncludeOrdersOn(state);
			// 8779. Wave Planning - graphical selection criteria
			final String facility = WPUtil.getFacility(state.getRequest());
			
			String reportWhereClause = createWhereClause(includeOrders,
					facility, criterion);
			
			String facilityClause = createFacilityClause(facility);
			final String sqlQuery = reportSelectClause + reportFromClause + reportWhereClause + facilityClause;
			_log.info("LOG_INFO_EXTENSION_WPGraphicalFilterProceed_createQuery", "SQL:" + sqlQuery, SuggestedCategory.NONE);
			return sqlQuery;
		}

	/**
	 * Creates the facility clause.
	 * 
	 * @param facility
	 *            the facility
	 * @return the string
	 */
	private static String createFacilityClause(String facility) {
		// 8779. Wave Planning - graphical selection criteria
//		String facilityClause = " AND orders.WHSEID='" + facility + "' ";
		String facilityClause = " "; //WP is now in the warehouse schema
		_log.debug("WPGraphFilterUtil_createFacilityClause", facilityClause,
				SuggestedCategory.APP_EXTENSION);
		return facilityClause;
	}

	/**
	 * Creates the where clause.
	 * 
	 * @param includeOrders
	 *            the include orders
	 * @param facility
	 *            the facility
	 * @param criterion
	 *            the criterion
	 * @return the string
	 */
	private static String createWhereClause(boolean includeOrders,
			final String facility, String criterion) {
		String reportWhereClause  = " WHERE orders.STORERKEY IS NOT NULL ";//"WHERE rt.STATUS = lkp.CODE AND lkp.LISTNAME = 'RECSTATUS' ";

		if(criterion.startsWith(DETAIL_PREFIX)){
			reportWhereClause += " AND orders.ORDERKEY = orderdetail.ORDERKEY ";
		}
		
		if( columnIsDate(criterion)) {
			reportWhereClause += " AND " + fullName(criterion) + " IS NOT NULL ";
		}
		
//		Harish:Machine2475030_SDIS07549		reportWhereClause += " AND (a.status<=9 or a.status in (12, 14, 15, 16, 17, 19)) ";
		reportWhereClause += " AND (orders.status < 95 and orders.TYPE != '91' ) ";		//Harish:Machine2475030_SDIS07549
		// Include Orders is off, take out orders that are in waves
		if (includeOrders == false)
		{
//			reportWhereClause += " AND a.ORDERKEY NOT IN (SELECT wp_wavedetails.ORDERKEY FROM wp_wavedetails WHERE wp_wavedetails.WHSEID='" + facility + "') ";
			reportWhereClause += " AND orders.ORDERKEY NOT IN (SELECT wavedetail.ORDERKEY FROM wavedetail ) ";
		}
		_log.debug("WPGraphFilterUtil_createWhereClause", reportWhereClause,
				SuggestedCategory.APP_EXTENSION);
		return reportWhereClause;
	}

	
	/**
	 * Creates the from clause.
	 * 
	 * @param criterion
	 *            the criterion
	 * @return the string
	 */
	private static String createFromClause(String criterion) {
		
		String reportFromClause  = "FROM orders orders";

		if(criterion.startsWith(DETAIL_PREFIX)){
			reportFromClause += " , orderdetail orderdetail";
		}
		_log.debug("WPGraphFilterUtil_createFromClause", reportFromClause,
				SuggestedCategory.APP_EXTENSION);
		return reportFromClause;
	}

	/**
	 * Creates the select clause.
	 * 
	 * @param criterion
	 *            the criterion
	 * @param showDescription 
	 * @return the string
	 */
	private static String createSelectClause(String criterion,
			boolean showDescription, StateInterface state) {
		String reportSelectClause = "SELECT DISTINCT ";

		reportSelectClause += generateSelectParameter(criterion,
				showDescription, state);

		_log.debug("WPGraphFilterUtil_createSelectClause", reportSelectClause,
				SuggestedCategory.APP_EXTENSION);
		return reportSelectClause;
	}

	
	public static String generateSelectParameter(String criterion, boolean showDescription, StateInterface state) {
		String parameter = "";
		//if column name is date
		String shortName = shortName(criterion);
		String fullName = fullName(criterion);
		if(columnIsDate(criterion)){
			parameter += fullName + " " + shortName + " ";	

		} else if (columnIsCodelkup(criterion) && showDescription == true) { 
			if(com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.isOracle(state) == true ) {
				parameter += "(CASE WHEN codelkup.DESCRIPTION IS NULL THEN cast(' ' as NVARCHAR2(1)) ELSE codelkup.DESCRIPTION END) STORERKEY ";
			} else {
				parameter += shortName + " = CASE WHEN codelkup.DESCRIPTION IS NULL THEN '' ELSE codelkup.DESCRIPTION END ";
			}
		} else {
			if(com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.isOracle(state) == true ) {
				parameter += "(CASE WHEN "+fullName+" IS NULL THEN cast(' ' as NVARCHAR2(1)) ELSE " +fullName +" END) " + shortName + " ";
			} else {
				parameter += shortName + " = CASE WHEN " + fullName + " IS NULL THEN '' ELSE " + fullName + " END ";
			}
		}

		return parameter;
	}


	public static String shortName(String criterion) {
		return criterion.substring(2);
	}

	/**
	 * Column is date.
	 * 
	 * @param columnName
	 *            the column name
	 * @return true, if successful
	 */
	public static boolean columnIsDate(String columnName) {
		if(dateSet.contains(columnName)){
			return true;
		}
		return false;
	}
	
	/**
	 * Column is codelkup.
	 * 
	 * @param columnName
	 *            the column name
	 * @return true, if successful
	 */
	private static boolean columnIsCodelkup(String columnName) {
		if(codelkupMap.containsKey(columnName)){
			return true;
		}
		return false;
	}

	public static String createQuery(List<String> criterionList,
			BioCollectionBean tempRecords, StateInterface state,
			QueryBuilderInputObj orderLimitInput) throws EpiDataException {

		return createQuery(criterionList, tempRecords, state, orderLimitInput, false);
	}

	/**
	 * Creates the group by clause.
	 * 
	 * @param criterion
	 *            the criterion
	 * @param showDescription 
	 * @return the string
	 */
	private static String createGroupByClause(String criterion, boolean showDescription) {
		String reportGroupByClause  = " GROUP BY ";

		if(columnIsCodelkup(criterion) && showDescription == true) {
			reportGroupByClause += " codelkup.DESCRIPTION";
		} else {
			reportGroupByClause += fullName(criterion);
		}
		_log.debug("WPGraphFilterUtil_createGroupByClause", reportGroupByClause,
				SuggestedCategory.APP_EXTENSION);
		return reportGroupByClause;
	}

	private static String createWhereClause(String criterion,
			List<String> criterionList, BioCollectionBean tempRecords,
			QueryBuilderInputObj orderLimitInput, String facility,
			StateInterface state) throws EpiDataException {
		String reportWhereClause = " WHERE orders.STORERKEY IS NOT NULL ";// "WHERE rt.STATUS = lkp.CODE AND lkp.LISTNAME = 'RECSTATUS' ";

		for (String c : criterionList) {
			if (c.startsWith(DETAIL_PREFIX)) {
				reportWhereClause += " AND orders.ORDERKEY = orderdetail.ORDERKEY ";
			}
			if (codelkupMap.containsKey(c)) {
				reportWhereClause += " AND " + fullName(c) + " = codelkup.CODE "
						+ " AND codelkup.LISTNAME = '" + codelkupMap.get(c)
						+ "'";
			}
		}
		
		if(columnIsDate(criterion)){
			reportWhereClause += " AND " + fullName(criterion) + " IS NOT NULL ";
		}

		// Append previously selected criteria
		for (int i = 0; i < criterionList.size() - 1; i++) {
			BioCollection selectedTempRecordsByType = tempRecords
					.filter(new Query(
							"wp_graphicalfilter_temp",
							"wp_graphicalfilter_temp.ISSELECTED = '1' AND wp_graphicalfilter_temp.FILTERTYPE = '"
									+ criterionList.get(i) + "'", ""));
			reportWhereClause += " AND ( ";
			boolean appended = false;
			String oldCriterion = (String) criterionList.get(i);
			for (int j = 0; j < selectedTempRecordsByType.size(); j++) {
				if (appended) {
					reportWhereClause += " OR ";
				}

				Bio currentRecord = selectedTempRecordsByType.elementAt(j);
				if (columnIsDate(oldCriterion)) {
					DateFormat dateFormatShort = DateFormat
							.getDateInstance(DateFormat.SHORT);
					DateFormat dateFormatLong = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date firstValueDate;
					try {
						TimeZone userTimeZone = WPQueryBuilderUtil
								.getTimeZone(state);

						firstValueDate = new java.text.SimpleDateFormat(
								WavePlanningUtils.dbDateFormat)
								.parse((String) currentRecord
										.get("ADDITIONALFILTER"));
						Calendar cal = Calendar.getInstance(userTimeZone);
						cal.setTime(firstValueDate);
						reportWhereClause += " " + fullName(oldCriterion)
								+ " BETWEEN '"
								+ WPQueryBuilderUtil.convertToGMTString(dateFormatLong.format(cal.getTime()), userTimeZone)
								+ "' ";
						cal.add(Calendar.DATE, 1);
						reportWhereClause += " AND '"
								+ WPQueryBuilderUtil.convertToGMTString(dateFormatLong.format(cal.getTime()), userTimeZone)
								+ "' ";

						reportWhereClause += " AND " + fullName(oldCriterion)
								+ " IS NOT NULL ";
					} catch (ParseException e) {
						e.printStackTrace();
						_log.error("WPGraphFilterUtil_createWhereClause",
								e.getMessage(), e);
						reportWhereClause += " " + fullName(oldCriterion)
								+ " IS NOT NULL ";
					}

				} else {
					reportWhereClause += fullName(oldCriterion) + " = '"
							+ currentRecord.get("ADDITIONALFILTER") + "' ";
				}
				appended = true;

			}
			reportWhereClause += " ) ";
		}

		if (orderLimitInput != null) {
			// mark ma added for order
			// limits**************************************************
			if (orderLimitInput.getMinEachOrderCube() != 0) {
				reportWhereClause += "AND orders.TOTALCUBE >  "
						+ orderLimitInput.getMinEachOrderCube();
			}
			if (orderLimitInput.getMaxEachOrderCube() != 999999999) {
				reportWhereClause += "AND orders.TOTALCUBE <  "
						+ orderLimitInput.getMaxEachOrderCube();
			}
			if (orderLimitInput.getMinEachOrderQty() != 0) {
				reportWhereClause += "AND orders.TOTALQTY >  "
						+ orderLimitInput.getMinEachOrderQty();
			}
			if (orderLimitInput.getMaxEachOrderQty() != 99999) {
				reportWhereClause += "AND orders.TOTALQTY <  "
						+ orderLimitInput.getMaxEachOrderQty();
			}
			if (orderLimitInput.getMinEachOrderWeight() != 0) {
				reportWhereClause += "AND orders.TOTALGROSSWGT >  "
						+ orderLimitInput.getMinEachOrderWeight();
			}
			if (orderLimitInput.getMaxEachOrderWeight() != 999999999) {
				reportWhereClause += "AND orders.TOTALGROSSWGT <  "
						+ orderLimitInput.getMaxEachOrderWeight();
			}
			if (orderLimitInput.getMinEachOrderLines() != 0) {
				reportWhereClause += "AND orders.TOTALORDERLINES >  "
						+ orderLimitInput.getMinEachOrderLines();
			}
			if (orderLimitInput.getMaxEachOrderLines() != 99999) {
				reportWhereClause += "AND orders.TOTALORDERLINES <  "
						+ orderLimitInput.getMaxEachOrderLines();
			}
			// end*****************************************************************************
		}
		// 8779. Wave Planning - graphical selection criteria
		// Harish:Machine2475030_SDIS07549 reportWhereClause +=
		// " AND (a.status<=9 or a.status in (12, 14, 15, 16, 17, 19)) ";
		reportWhereClause += " AND (orders.status < 95 and orders.TYPE != '91') "; // Harish:Machine2475030_SDIS07549
		// 8723. Wave Planning - graphical filter (2 issues)
		boolean includeOrders = isIncludeOrdersOn(state);
		// Include Orders is off, take out orders that are in waves
		if (includeOrders == false) {
			reportWhereClause += " AND orders.ORDERKEY NOT IN (SELECT wavedetail.ORDERKEY FROM wavedetail ) ";
		}

		_log.debug("WPGraphFilterUtil_createWhereClause", reportWhereClause,
				SuggestedCategory.APP_EXTENSION);
		return reportWhereClause;
	}

	public static String fullName(String c) {
		if (c.startsWith(HEADER_PREFIX)) {
			return ORDERS + "." + c.substring(2);
		} else {
			return ORDERDETAIL + "." + c.substring(2);
		}

	}

	private static String createFromClause(List<String> criterionList,
			StateInterface state) {
		String reportFromClause = "FROM orders orders ";

		boolean checkedForOrderDetail = false;
		boolean checkedForCodelkup = false;
		for (String c : criterionList) {
			if (checkedForOrderDetail == false && c.startsWith(DETAIL_PREFIX)) {
				reportFromClause += " , orderdetail orderdetail";
				checkedForOrderDetail = true;
			}

			if (checkedForCodelkup == false && columnIsCodelkup(c) == true) {
				reportFromClause += " , codelkup codelkup ";
				checkedForCodelkup = true;
			}
			
			if(checkedForCodelkup == true && checkedForOrderDetail == true){
				break;
			}
		}
		_log.debug("WPGraphFilterUtil_createFromClause", reportFromClause,
				SuggestedCategory.APP_EXTENSION);
		return reportFromClause;
	}

	/**
	 * Creates the select clause. Includes count information
	 * 
	 * @param criterion
	 *            the criterion
	 * @param includeCount
	 *            the group by
	 * @param showDescription 
	 * @return the string
	 */
	private static String createSelectClause(String criterion,
			boolean includeCount, boolean showDescription, StateInterface state) {
		String reportSelectClause = createSelectClause(criterion, showDescription, state);
		if (includeCount == true) {
			reportSelectClause += "  ,COUNT(orders.ORDERKEY) Order_Count ";
		}
		_log.debug("WPGraphFilterUtil_createSelectClause", reportSelectClause,
				SuggestedCategory.APP_EXTENSION);
		return reportSelectClause;
	}

	public static void updateGraphicalFilterTempTable(List orders,
			String baseCriterion, UnitOfWorkBean uow, StateInterface state)
			throws DataBeanException, EpiException {
		if(orders != null){
			DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT, WPUserUtil.getUserLocale(state));
			HashMap<String, String> holder = new HashMap<String, String>();
			for (int i = 0; i < orders.size(); i++) {
				// check date value
				boolean skip = false;
				Map resultMap = (Map) orders.get(i);
				if (columnIsDate(baseCriterion)) {
					String tmpDate = null;
					Date date = (Date) (resultMap)
							.get(shortName(baseCriterion));
					if (date != null) {
						tmpDate = dateFormatShort.format(date);
						
						if ("true".equalsIgnoreCase(holder.get(tmpDate))) {
							skip = true;
						} else {
							holder.put(tmpDate, "true");
						}
					}

					
				}

				if (!skip) {
					QBEBioBean newTempRecord = uow
							.getQBEBioWithDefaults("wp_graphicalfilter_temp");
					newTempRecord.set("DODISPLAY", "1");
					newTempRecord.set("FILTERTYPE", baseCriterion);
					newTempRecord
							.set("INTERACTIONID", state.getInteractionId());
					newTempRecord.set("GRAPHICALFILTERTEMPID",
							GUIDFactory.getGUIDStatic());
					newTempRecord.set("USERID", WPUserUtil.getUserId(state));

					if (columnIsDate(baseCriterion)) {
						Date regularDate1 = (Date) (resultMap)
								.get(shortName(baseCriterion));
						Date regularDate2 = (Date) (resultMap)
								.get(shortName(baseCriterion));
						if (regularDate2 != null)
							newTempRecord.set("LABEL",
									dateFormatShort.format(regularDate2));
						else {
							newTempRecord.set("LABEL", regularDate2);
						}
						newTempRecord.set("ADDITIONALFILTER",
								WPUtil.getDBDependentDate(regularDate1));
					} else {
						newTempRecord.set("ADDITIONALFILTER",
								resultMap.get(shortName(baseCriterion)));
						newTempRecord.set("LABEL",
								resultMap.get(shortName(baseCriterion)));
					}

				}
			}
			uow.saveUOW(true);
		}
	}

	public static String createQuery(List<String> criterionList,
			BioCollectionBean tempRecords, StateInterface state,
			QueryBuilderInputObj orderLimitInput, boolean showDescription) throws EpiDataException {

		String facility =  WPUtil.getFacility(state.getRequest());
		
		String criterion = (String)criterionList.get(criterionList.size() - 1);
		String reportSelectClause  = createSelectClause(criterion, true, showDescription, state);

		String reportFromClause = createFromClause(criterionList, state);		   

		String reportWhereClause = createWhereClause(criterion,
				criterionList, tempRecords, orderLimitInput, facility, state);

		String reportGroupByClause = createGroupByClause(criterion, showDescription);

		// 8779. Wave Planning - graphical selection criteria
		String facilityClause = createFacilityClause(facility);
		String fullQuery = reportSelectClause + reportFromClause + reportWhereClause + facilityClause + reportGroupByClause;
		_log.debug("WPGraphFilterUtil_createQuery", fullQuery,
				SuggestedCategory.APP_EXTENSION);
		return fullQuery;
	}
}