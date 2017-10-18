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
package com.infor.scm.waveplanning.wp_query_builder.action;

//Import 3rd party packages and classes
import java.util.Calendar;
import java.util.GregorianCalendar;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_query_builder.action.WPQueryBuilderSaveQuery;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import  com.infor.scm.waveplanning.wp_query_builder.util.*;

public class WPQueryBuilderShowOrderList extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderShowOrderList.class);

	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderSaveQuery",100L);
		StateInterface state = context.getState();
		//String userId = WPUserUtil.getUserId(state);
		String interactionId = "";
		Object uniqueIdObj = state.getRequest().getSession().getAttribute(WPQueryBuilderNewTempRecord.HAS_DEFAULT_FILTER);
		if(uniqueIdObj!=null){//has default filter
			interactionId = uniqueIdObj.toString();
		}else{
			interactionId = context.getState().getInteractionId();			
		}

		
		
		 
		String qry = "querybuildertemp.INTERACTIONID = '"+interactionId+"'";
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		//Clean Out All Records Older Than 1 Day
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK,-1);
		Query query = new Query("querybuildertemp","querybuildertemp.DATEADDED < @DATE['"+gc.getTimeInMillis()+"']","");
		BioCollection bioColl = uowb.getBioCollectionBean(query);
		try {
			if(bioColl != null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","bc.size():"+bioColl.size(),100L);
				for(int i = 0; i < bioColl.size(); i++){
					bioColl.elementAt(i).delete();
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//Save any unsaved
		WPQueryBuilderSaveQuery.saveQueryBuilder(state, uowb);

		//Retrieve wave limits
		BioCollection headerCollection = uowb.getBioCollectionBean(new Query("querybuildertemp",qry,""));	
		try {
			if(headerCollection == null || headerCollection.size() == 0){
				String args[] = new String[0];
				String errorMsg = getTextMessage("WPEXP_QRY_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_QRY_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		try {
			//start added *****************************************************
			QueryBuilderInputObj input = new QueryBuilderInputObj();
			input.setCallType(QueryBuilderConstants.RETRIEVE_QUERY_CALL_TYPE);
			input.setUserTimeZone(WPQueryBuilderUtil.getTimeZone(state));
			input.setCtx(context);
			//end ***************************************************************
			qry = "wp_querybuilderdetail.INTERACTIONID = '"+interactionId+"'";
			BioCollection detailCollection = uowb.getBioCollectionBean(new Query("wp_querybuilderdetail",qry,""));
			Bio headerBio = headerCollection.elementAt(0);
			String includeOrders = "on";
			if(headerBio.get("INCLUDEORDERS") == null || headerBio.get("INCLUDEORDERS").toString().equals("0"))
				includeOrders = "off";
			String whereClause = (String)WPQueryBuilderUtil.getWhereClause(detailCollection, input).get(1); 	//"";

			//Build Query
			qry = WPQueryBuilderUtil.getQuery(/*(String)headerBio.get("QUERYTYPE"),*/whereClause,WPUtil.getFacility(state.getRequest()),detailCollection,includeOrders,(String)headerBio.get("INCLUDERF"), input);
			int maxOrders = headerBio.get("ORDERSMAX")==null?0:Integer.parseInt(headerBio.get("ORDERSMAX").toString());
			int maxOrderLines = headerBio.get("ORDERLINESMAX")==null?0:Integer.parseInt(headerBio.get("ORDERLINESMAX").toString());
			double maxCube = headerBio.get("CUBEMAX")==null?0:Double.parseDouble(headerBio.get("CUBEMAX").toString());
			double maxWeight = headerBio.get("WEIGHTMAX")==null?0:Double.parseDouble(headerBio.get("WEIGHTMAX").toString());
			double maxCases = -1;

			input.setMaxOrders(maxOrders);
			input.setMaxOrderLines(maxOrderLines);
			input.setMaxCube(maxCube);
			input.setMaxWeight(maxWeight);
			input.setState(state);
			input.setMinEachOrderLines(headerBio.get("TOTALLINESSTART")==null?0:Double.parseDouble(headerBio.get("TOTALLINESSTART").toString()));
			input.setMaxEachOrderLines(headerBio.get("TOTALLINESEND")==null?0:Double.parseDouble(headerBio.get("TOTALLINESEND").toString()));
			input.setMinEachOrderCube(headerBio.get("TOTALCUBESTART")==null?0:Double.parseDouble(headerBio.get("TOTALCUBESTART").toString()));
			input.setMaxEachOrderCube(headerBio.get("TOTALCUBEEND")==null?0:Double.parseDouble(headerBio.get("TOTALCUBEEND").toString()));
			input.setMinEachOrderWeight(headerBio.get("TOTALWEIGHTSTART")==null?0:Double.parseDouble(headerBio.get("TOTALWEIGHTSTART").toString()));
			input.setMaxEachOrderWeight(headerBio.get("TOTALWEIGHTEND")==null?0:Double.parseDouble(headerBio.get("TOTALWEIGHTEND").toString()));
			input.setMinEachOrderQty(headerBio.get("TOTALQTYSTART")==null?0:Double.parseDouble(headerBio.get("TOTALQTYSTART").toString()));
			input.setMaxEachOrderQty(headerBio.get("TOTALQTYEND")==null?0:Double.parseDouble(headerBio.get("TOTALQTYEND").toString()));

			if (com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.isOracle(state)){
				qry = this.convertToOracleSql(qry);
			}
			
			input.setUserQry(qry);

			
			
			
			if (WavePlanningUtils.wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
				maxCases = headerBio.get("CASES")==null?0:Double.parseDouble(headerBio.get("CASES").toString());
			}
//			BioCollectionBean bc = WPQueryBuilderUtil.getShipmentOrders(qry,maxOrders,maxOrderLines,maxCube,maxWeight,maxCases,state);
			BioCollectionBean bc = WPQueryBuilderUtil.getShipmentOrders(input);
			result.setFocus(bc);
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_QRY_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_QRY_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;
	}
	
	public String convertToOracleSql(String sql){
//		String s1 = sql.replaceAll("\\\\", "");
		String s2 = sql.replaceAll("\\\\'to_date", "to_date");
		String s3 = s2.replaceAll("''", "\\\\'");
		String s4 = s3.replaceAll("SS\\\\'\\)\\\\'", "SS\\\\'\\)");
//System.out.println("*** it is oracle and qb show order list after convert sql="+s4);
		return s4;
	}
}