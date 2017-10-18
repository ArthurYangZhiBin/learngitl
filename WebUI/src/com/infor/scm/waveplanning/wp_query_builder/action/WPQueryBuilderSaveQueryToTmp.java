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

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderConstants;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.epiphany.shr.ui.model.data.BioCollectionBean;

public class WPQueryBuilderSaveQueryToTmp extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderSaveQueryToTmp.class);

	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderSaveQueryToTmp",100L);
		StateInterface state = context.getState();
		BioBean headerBioBean = saveQueryBuilder(state, state.getDefaultUnitOfWork());
//		result.setFocus(headerBioBean);
		return RET_CONTINUE;
	}
	
	public static BioBean saveQueryBuilder(StateInterface state, UnitOfWorkBean uowb) throws UserException{
		//Initialize local variables
		System.out.println("***IT IS IN SAVE Query Builder*****");
		RuntimeFormInterface waveLimitsForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_wp_query_builder_initial_screen", state);
		RuntimeListFormInterface queryFilterForm = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_detail_list_screen",state);
		String uid = WPUserUtil.getUserId(state);
		String interactionId = state.getInteractionId();
		if(queryFilterForm != null){
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Filter Form:"+queryFilterForm.getName(),100L);
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Filter Form:Null",100L);
			return null;
		}
		//Check header
		DataBean waveLimitsFocus = waveLimitsForm.getFocus();
		
		//Get Quick Add Row
		QBEBioBean queryFilterFocus = queryFilterForm == null ? null : queryFilterForm.getQuickAddRowBean();
		

		BioBean headerBioBean = null;
		if(waveLimitsFocus.isTempBio()){
			waveLimitsFocus.setValue("INTERACTIONID",interactionId);
			waveLimitsFocus.setValue("USERID",uid);
			waveLimitsFocus.save();
			
			try{
				headerBioBean = uowb.getNewBio((QBEBioBean)waveLimitsFocus);
		    } catch(EpiException e){
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
		}else{
			headerBioBean = (BioBean)waveLimitsFocus;
		}
		headerBioBean.setValue("USERTIMEZONE", com.ssaglobal.scm.wms.util.ReportUtil.getTimeZone(state).getID());

		//Set Quick Add Row to BioCollection
		//mark ma added *****************************************************
		QueryBuilderInputObj input = new QueryBuilderInputObj();
		input.setCallType(QueryBuilderConstants.SAVE_QUERY_CALL_TYPE);
		input.setUserTimeZone(WPQueryBuilderUtil.getTimeZone(state));
		//end ***************************************************************
		if(queryFilterFocus != null && !queryFilterFocus.isEmpty()){
			Object firstValue = queryFilterFocus.get("FIRSTVALUE");
			Object secondValue = queryFilterFocus.get("SECONDVALUE");
			String orderField = queryFilterFocus.get("ORDERFIELD").toString();
			if("orderdate".equalsIgnoreCase(orderField)
					|| "rsd".equalsIgnoreCase(orderField)
					|| "dlvrdate".equalsIgnoreCase(orderField)
					|| "dlvrdate2".equalsIgnoreCase(orderField)){
				if(firstValue != null && !firstValue.toString().trim().startsWith("today")){
					queryFilterFocus.set("FIRSTVALUE", WPQueryBuilderUtil.convertToGMTString(dateTimeFormat(firstValue.toString().trim()), input.getUserTimeZone()));
				}
				if(secondValue != null && !secondValue.toString().trim().startsWith("today")){
					queryFilterFocus.set("SECONDVALUE", WPQueryBuilderUtil.convertToGMTString(dateTimeFormat(secondValue.toString().trim()), input.getUserTimeZone()));
				}
			}
			
			headerBioBean.addBioCollectionLink("QUERYBUILDERDETAIL", queryFilterFocus);
		}
		
		if(!queryFilterFocus.isEmpty()){
			//"Second Value" is only used for the BETWEEN and NOT BETWEEN operators
			String operator = queryFilterFocus.getValue("OPERATOR").toString();
			if(!operator.equalsIgnoreCase("BETWEEN") &&	!operator.equalsIgnoreCase("NOTBETWEEN")){
				String secondValue = (String)queryFilterFocus.getValue("SECONDVALUE");
				if(secondValue != null && secondValue.length() > 0){
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SECOND_VALUE_FOR_BINARY_OP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}

			//Tag the record with the current interaction ID
//			queryFilterFocus.setValue("INTERACTIONID",interactionId);
			Object uniqueIdObj = state.getRequest().getSession().getAttribute(WPQueryBuilderNewTempRecord.HAS_DEFAULT_FILTER);
			if(uniqueIdObj != null){//user has default filter
				queryFilterFocus.setValue("INTERACTIONID",uniqueIdObj.toString());
			}else{
				queryFilterFocus.setValue("INTERACTIONID",interactionId);			
			}
			queryFilterFocus.setValue("ADDWHO",uid);
			queryFilterFocus.save();
		}
		try {
			uowb.saveUOW(!queryFilterFocus.isEmpty());
			uowb.clearState();
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return headerBioBean;
	}
	
	 public static String dateTimeFormat(String dateTime){
			String [] split = dateTime.split(" ");
			String date = split[0];
			if(split.length == 1){
				date = date +" 00:00:00";					
			}else{
				String time = split[1].trim();
				if(!"00:00:00".equalsIgnoreCase(time)){							
					String [] timeList = time.split(":");
					if(timeList.length==2){
						time = time+":00";
					}
					date = date +" "+time;
				}else{
					date = date +" 00:00:00";
				}
			}
			return date;
		 
	 }

}