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
package com.infor.scm.waveplanning.wp_saved_filters.action;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderConstants;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;

public class WPFilterShowOrderList extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPFilterShowOrderList.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderSaveQuery",100L);		
		StateInterface state = context.getState();
		System.out.println("**** it is filter show orders list ******");
		//Get filter header form				
		RuntimeFormInterface filterDetailForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_saved_filters_filter_detail",state);	
		DataBean headerBio = null;
		if(filterDetailForm != null) {
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Found filterDetailsForm Form:"+filterDetailForm.getName(),100L);
			headerBio = filterDetailForm.getFocus();
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Found filterDetailsForm Form Form:Null",100L);
			//if normal form not found, look for list form
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) WPFormUtil.findForm(state.getCurrentRuntimeForm(), "", "wp_saved_filters_filter_list", state);
			if(listForm != null){
				if ((listForm).getAllSelectedItems() == null)	{
					_log.error("LOG_DEBUG_EXTENSION_DELETEFILTER", "No Filters Selected",
							SuggestedCategory.NONE);
					throw new UserException("WPEXP_NO_FILTER_SELECTED", new Object[] {});
				}
				else if ((listForm).getAllSelectedItems().size() > 1)	{
					throw new UserException("WPEXP_MULTI_FILTER_SELECTED", new Object[] {});
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER", "Called from List Form "
							+ listForm.getName(), SuggestedCategory.NONE);
					ArrayList allSelectedItems = listForm.getAllSelectedItems();
					for (Iterator it = allSelectedItems.iterator(); it.hasNext();)	{
						headerBio = (DataBean) it.next();
					}
					listForm.setSelectedItems(null);
				}
			}
			else {
				return RET_CANCEL;
			}
		}	

		if(headerBio == null){
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Detail Form Focus is null or temp... exiting",100L);
			return RET_CANCEL;
		}
	
		try {			
			BioCollection detailCollection = ((BioBean)headerBio).getBioCollection("FILTERDETAILS");			
			String includeOrders = "on";

			if(headerBio.getValue("INCLUDEORDERS") == null || headerBio.getValue("INCLUDEORDERS").toString().equals("0")) {
				includeOrders = "off";
			}
			
			 
			
			
			//start added *****************************************************
			QueryBuilderInputObj input = new QueryBuilderInputObj();
			input.setCallType(QueryBuilderConstants.RETRIEVE_QUERY_CALL_TYPE);
			input.setUserTimeZone(WPQueryBuilderUtil.getTimeZone(state));
			input.setCtx(context);
			state.getRequest().getSession().setAttribute("WAVE_PREFIX", headerBio.getValue("DESCPREFIX"));
			
			
			//end ***************************************************************

			String whereClause = "";												
			whereClause = (String)WPQueryBuilderUtil.getWhereClause(detailCollection, input).get(1);				

			//Build Query
			String query =WPQueryBuilderUtil.getQuery(/*"QRY",*/whereClause,WPUtil.getFacility(state.getRequest()),detailCollection,includeOrders,(String)headerBio.getValue("RFID_STND"), input);
			int maxOrders = headerBio.getValue("MAXORDERS")==null?0:Integer.parseInt(headerBio.getValue("MAXORDERS").toString());
			int maxOrderLines = headerBio.getValue("MAXORDERLINES")==null?0:Integer.parseInt(headerBio.getValue("MAXORDERLINES").toString());
			double maxCube = headerBio.getValue("MAXCUBE")==null?0:Double.parseDouble(headerBio.getValue("MAXCUBE").toString());
			double maxWeight = headerBio.getValue("MAXWEIGHT")==null?0:Double.parseDouble(headerBio.getValue("MAXWEIGHT").toString());
			double maxCases = -1;


			input.setMaxOrders(maxOrders);
			input.setMaxOrderLines(maxOrderLines);
			input.setMaxCube(maxCube);
			input.setMaxWeight(maxWeight);
			input.setState(state);
			input.setMinEachOrderLines(headerBio.getValue("TOTALLINESSTART")==null?0:Double.parseDouble(headerBio.getValue("TOTALLINESSTART").toString()));
			input.setMaxEachOrderLines(headerBio.getValue("TOTALLINESEND")==null?0:Double.parseDouble(headerBio.getValue("TOTALLINESEND").toString()));
			input.setMinEachOrderCube(headerBio.getValue("TOTALCUBESTART")==null?0:Double.parseDouble(headerBio.getValue("TOTALCUBESTART").toString()));
			input.setMaxEachOrderCube(headerBio.getValue("TOTALCUBEEND")==null?0:Double.parseDouble(headerBio.getValue("TOTALCUBEEND").toString()));
			input.setMinEachOrderWeight(headerBio.getValue("TOTALWEIGHTSTART")==null?0:Double.parseDouble(headerBio.getValue("TOTALWEIGHTSTART").toString()));
			input.setMaxEachOrderWeight(headerBio.getValue("TOTALWEIGHTEND")==null?0:Double.parseDouble(headerBio.getValue("TOTALWEIGHTEND").toString()));
			input.setMinEachOrderQty(headerBio.getValue("TOTALQTYSTART")==null?0:Double.parseDouble(headerBio.getValue("TOTALQTYSTART").toString()));
			input.setMaxEachOrderQty(headerBio.getValue("TOTALQTYEND")==null?0:Double.parseDouble(headerBio.getValue("TOTALQTYEND").toString()));

			
/*			ESDataInterface esDataIf = (ESDataInterface) ESUtilityFunctionsImpl.getServiceLocator().getServiceProvider(ESServiceLocatorInterface.HLP_DATA);				
			if (esDataIf.getDbType()==ESDataInterface.DB_TYPE_ORACLE){
				query = this.convertToOracleSql(query);
			}
*/			
			input.setUserQry(query);
			
			
			if (WavePlanningUtils.wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
				maxCases = headerBio.getValue("MAXCASES")==null?0:Double.parseDouble(headerBio.getValue("MAXCASES").toString());
			}

//			BioCollectionBean bc = WPQueryBuilderUtil.getShipmentOrders(query,maxOrders,maxOrderLines,maxCube,maxWeight,maxCases,state);
			BioCollectionBean bc = WPQueryBuilderUtil.getShipmentOrders(input);
			result.setFocus(bc);

			Hashtable bioRefTbl = (Hashtable)state.getRequest().getSession().getAttribute(WPFiltersOrdersBack.SESSION_KEY_FILTER_BIO_REF_TABLE);
			if(bioRefTbl == null){
				bioRefTbl = new Hashtable();
			}
			bioRefTbl.put(state.getInteractionId(), ((BioBean)headerBio).getBioRef());
			state.getRequest().getSession().setAttribute(WPFiltersOrdersBack.SESSION_KEY_FILTER_BIO_REF_TABLE, bioRefTbl);
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
System.out.println("*** it is oracle and qb show order list after convert sql="+s4);
		return s4;
	}
}