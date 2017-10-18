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
import java.util.GregorianCalendar;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderConstants;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class WPQueryBuilderSaveFilter extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderSaveFilter.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderSaveQuery",100L);
		System.out.println("**** it is in Filter save ******");
		StateInterface state = context.getState();
		RuntimeFormInterface queryFilterForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_save_filter_screen",state);
		String uid = WPUserUtil.getUserId(state);
		String interactionId = context.getState().getInteractionId();
		if(queryFilterForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Filter Form:"+queryFilterForm.getName(),100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Filter Form:Null",100L);


		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		DataBean queryFilterFocus = queryFilterForm == null?null:queryFilterForm.getFocus();

		//MARK.B
		Object uniqueIdObj = state.getRequest().getSession().getAttribute(WPQueryBuilderNewTempRecord.HAS_DEFAULT_FILTER);

		if(uniqueIdObj != null){
			interactionId = uniqueIdObj.toString();
		}


		String qry = "querybuildertemp.INTERACTIONID = '"+interactionId+"'";
		BioCollection headerRecords = uow.getBioCollectionBean(new Query("querybuildertemp",qry,""));
		qry = "wp_querybuilderdetail.INTERACTIONID = '"+interactionId+"'";
		BioCollection detailRecords = uow.getBioCollectionBean(new Query("wp_querybuilderdetail",qry,""));

		try {
			if(headerRecords == null || headerRecords.size() == 0){
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		try {
			Bio headerBio = headerRecords.elementAt(0);
			if(queryFilterFocus.getValue("USERDEFAULT") != null && queryFilterFocus.getValue("USERDEFAULT").toString().equalsIgnoreCase("true")){
				Query filterQry = new Query("wp_filter","wp_filter.USERDEFAULT = 'true' AND wp_filter.USERID = '"+WPUserUtil.getUserId(state)+"'","");
				BioCollectionBean defaultFilters = uow.getBioCollectionBean(filterQry);
				if(defaultFilters != null){
					for(int i = 0; i < defaultFilters.size(); i++){
						defaultFilters.elementAt(i).set("USERDEFAULT", Boolean.FALSE);
					}
				}
			}
			queryFilterFocus.setValue("MAXORDERS",headerBio.get("ORDERSMAX"));
			queryFilterFocus.setValue("MAXCASES","99999");
			queryFilterFocus.setValue("MAXORDERLINES",headerBio.get("ORDERLINESMAX"));
			queryFilterFocus.setValue("MAXCUBE",headerBio.get("CUBEMAX"));
			queryFilterFocus.setValue("MAXWEIGHT",headerBio.get("WEIGHTMAX"));
			queryFilterFocus.setValue("RFID_STND",headerBio.get("INCLUDERF"));
			queryFilterFocus.setValue("MODIFIEDBY",uid);
			queryFilterFocus.setValue("MODIFIEDON",new GregorianCalendar());
			queryFilterFocus.setValue("FACILITY",WPUtil.getFacility(state.getRequest()));


			//mark ma added ************************************
			queryFilterFocus.setValue("TOTALQTYSTART",headerBio.get("TOTALQTYSTART"));
			queryFilterFocus.setValue("TOTALQTYEND",headerBio.get("TOTALQTYEND"));
			queryFilterFocus.setValue("TOTALLINESSTART",headerBio.get("TOTALLINESSTART"));
			queryFilterFocus.setValue("TOTALLINESEND",headerBio.get("TOTALLINESEND"));
			queryFilterFocus.setValue("TOTALCUBESTART",headerBio.get("TOTALCUBESTART"));
			queryFilterFocus.setValue("TOTALCUBEEND",headerBio.get("TOTALCUBEEND"));
			queryFilterFocus.setValue("TOTALWEIGHTSTART",headerBio.get("TOTALWEIGHTSTART"));
			queryFilterFocus.setValue("TOTALWEIGHTEND",headerBio.get("TOTALWEIGHTEND"));
			queryFilterFocus.setValue("USERTIMEZONE", com.ssaglobal.scm.wms.util.ReportUtil.getTimeZone(state).getID());
			//end **********************************************

			//Query Filter = 0
			//SQL Filter = 2
			//Graphic Filter = 1
//			if(headerBio.get("QUERYTYPE").equals("QRY")){
			//mark ma added *****************************************************
			QueryBuilderInputObj input = new QueryBuilderInputObj();
			input.setCallType(QueryBuilderConstants.SAVE_QUERY_CALL_TYPE);
			input.setUserTimeZone(WPQueryBuilderUtil.getTimeZone(state));
			input.setFromSaveFilter(true);
			input.setCtx(context);
			//end ***************************************************************
				queryFilterFocus.setValue("WHERECLAUSE",WPQueryBuilderUtil.getWhereClause(detailRecords, input).get(2));
				queryFilterFocus.setValue("FILTERTYPE",new Integer(0));
				//queryFilterFocus.setValue("FROMCLAUSE","ODRHDR");
//			}else if(headerBio.get("QUERYTYPE").equals("SQL")){
//				queryFilterFocus.setValue("FILTERTYPE",new Integer(2));
//			}else{
//				queryFilterFocus.setValue("FILTERTYPE",new Integer(1));
				//queryFilterFocus.setValue("FROMCLAUSE","ODRHDR");
//			}
			String includeOrders = "on";
			if(headerBio.get("INCLUDEORDERS") == null || headerBio.get("INCLUDEORDERS").toString().equals("0")) {
				includeOrders = "off";
				queryFilterFocus.setValue("INCLUDEORDERS", "0");
			}
			else {
				queryFilterFocus.setValue("INCLUDEORDERS", "1");
			}

			String query =WPQueryBuilderUtil.getQuery(/*(String)headerBio.get("QUERYTYPE"),*/(String)queryFilterFocus.getValue("WHERECLAUSE"),(String)queryFilterFocus.getValue("FACILITY"),detailRecords,includeOrders,(String)headerBio.get("INCLUDERF"), input);
			//query = query.replaceAll("wp_orderheader","");
			queryFilterFocus.setValue("QUERY",query);
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (Exception e1) {
			e1.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		//build where clause


		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		try {
			if(queryFilterFocus.isTempBio() && detailRecords != null){
				QBEBioBean newFilter = (QBEBioBean)queryFilterFocus;
				BioBean newFilterBean = uow.getNewBio(newFilter);
				for (int i = 0; i < detailRecords.size(); i++){
					QBEBioBean newFilterDetail = uow.getQBEBioWithDefaults("wp_filterdetails");
					Bio tempFilterDetail = detailRecords.elementAt(i);
					newFilterDetail.set("LISTORDER", new Integer(i));
					newFilterDetail.set("FILTERID", newFilterBean.getValue("FILTERID"));
					newFilterDetail.set("FILTERDETAILSID", GUIDFactory.getGUIDStatic());
					newFilterDetail.set("ORDERFIELD", tempFilterDetail.get("ORDERFIELD"));
					newFilterDetail.set("OPERATOR", tempFilterDetail.get("OPERATOR"));
					newFilterDetail.set("FIRSTVALUE", tempFilterDetail.get("FIRSTVALUE"));
					newFilterDetail.set("SECONDVALUE", tempFilterDetail.get("SECONDVALUE"));
					newFilterDetail.set("ANDOR", tempFilterDetail.get("ANDOR"));
					newFilterDetail.set("USERID", WPUserUtil.getUserId(state));
					newFilterDetail.save();
				}

				Query filterqry = new Query("wp_filter","wp_filter.FILTERID = '"+newFilterBean.getValue("FILTERID")+"'","");
				result.setFocus(uow.getBioBean(uow.getBioCollectionBean(filterqry).elementAt(0).getBioRef()));
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		try {
			uow.saveUOW(true);
			uow.clearState();
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		BioCollectionBean fiterListBean = uow.getBioCollectionBean(new Query("wp_filter","wp_filter.FACILITY='"+wmWhseID+"'","wp_filter.FILTERID DESC"));
		result.setFocus(fiterListBean);

		return RET_CONTINUE;
	}
}