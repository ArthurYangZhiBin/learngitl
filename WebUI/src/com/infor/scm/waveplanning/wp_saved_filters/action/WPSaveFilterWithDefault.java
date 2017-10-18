package com.infor.scm.waveplanning.wp_saved_filters.action;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderConstants;
import com.infor.scm.waveplanning.wp_query_builder.util.QueryBuilderInputObj;
import com.infor.scm.waveplanning.wp_query_builder.util.WPQueryBuilderUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class WPSaveFilterWithDefault extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPSaveFilterWithDefault.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		_log.debug("LOG_DEBUG_EXTENSION_SAVEFILTER","Executing WPSaveFilterWithDefault",100L);			
		StateInterface state = context.getState();	
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//Get initial form
		ArrayList tabs = new ArrayList();
		tabs.add("wp_saved_filters_filter_detail_tab");
		RuntimeListFormInterface filterDetailsForm = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_saved_filters_filter_detail_list",tabs,state);				
		
		RuntimeFormInterface filterHeaderForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_saved_filters_filter_detail",state);				
		
		try {
			if(filterHeaderForm != null){
				String filterId = filterHeaderForm.getFocus().getValue("FILTERID").toString();
				Boolean isDefaultValue = (Boolean)filterHeaderForm.getFocus().getValue("USERDEFAULT");
				if(isDefaultValue != null && isDefaultValue.booleanValue()){
					EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
					String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
					Query filterQry = new Query("wp_filter","wp_filter.USERDEFAULT = 'true' AND wp_filter.FACILITY = '"+wmWhseID+"'","");
					BioCollectionBean defaultFilters = state.getDefaultUnitOfWork().getBioCollectionBean(filterQry);
					if(defaultFilters != null){					
						String filterIdStr = "";
						for(int i = 0; i < defaultFilters.size(); i++){		
							filterIdStr = defaultFilters.elementAt(i).getString("FILTERID");
							if(!filterId.equalsIgnoreCase(filterIdStr)){
								defaultFilters.elementAt(i).set("USERDEFAULT", Boolean.FALSE);
							}
						}
					}
				}
			}
		} catch (EpiDataException e1) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving record",100L);										
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e1.getStackTraceAsString(),100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
			
		//Save Detail Form quick-add-row initial form if present		
		//start *****************************************************
		QueryBuilderInputObj input = new QueryBuilderInputObj();
		input.setCallType(QueryBuilderConstants.SAVE_QUERY_CALL_TYPE);
		input.setUserTimeZone(WPQueryBuilderUtil.getTimeZone(state));
		//end ***************************************************************
		if(filterDetailsForm != null){	
			//Get quick-add-row
			QBEBioBean newDetailRecord = filterDetailsForm.getQuickAddRowBean();
			_log.debug("LOG_DEBUG_EXTENSION_SAVEFILTER","Got newDetailRecord:"+newDetailRecord,100L);
			
			if(newDetailRecord != null && !newDetailRecord.isEmpty()){	
				_log.debug("LOG_DEBUG_EXTENSION_SAVEFILTER","User entered data in quick-add-row...",100L);
				
				//Get parent form
				RuntimeFormInterface parentForm = filterDetailsForm.getParentForm(state);				
				if(parentForm != null)
					_log.debug("LOG_DEBUG_EXTENSION_SAVEFILTER","Found parentForm Form:"+parentForm.getName(),100L);			
				else{
					_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error! Could not find parent form...",100L);										
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}

				
				//start added ******************************************************************
				Object firstValue = newDetailRecord.get("FIRSTVALUE");
				Object secondValue = newDetailRecord.get("SECONDVALUE");
				String orderField = newDetailRecord.get("ORDERFIELD").toString();
				if("ORDERDATE".equalsIgnoreCase(orderField)
						|| "RSD".equalsIgnoreCase(orderField)
						|| "DLVRDATE".equalsIgnoreCase(orderField)
						|| "DLVRDATE2".equalsIgnoreCase(orderField)){
					if(firstValue != null){
						newDetailRecord.set("FIRSTVALUE", WPQueryBuilderUtil.convertToGMTString(dateTimeFormat(firstValue.toString().trim()), input.getUserTimeZone()));
					}
					if(secondValue != null){
						newDetailRecord.set("SECONDVALUE", WPQueryBuilderUtil.convertToGMTString(dateTimeFormat(secondValue.toString().trim()), input.getUserTimeZone()));
					}
				}
				//end *************************************************************************
				
				//Get foreign key value (FILTERID) from parent focus
				newDetailRecord.set("FILTERID", parentForm.getFocus().getValue("FILTERID"));
				
				//Set primary key (FILTERDETAILSID) with a GUID
				newDetailRecord.set("FILTERDETAILSID", GUIDFactory.getGUIDStatic());
				
				//Get next list order and set it.
				BioCollection filterDetailsFocus = (BioCollection)filterDetailsForm.getFocus();
				if(filterDetailsFocus == null){
					newDetailRecord.set("LISTORDER", new Integer(0));
				}
				else{
					try {
						newDetailRecord.set("LISTORDER", new Integer(filterDetailsFocus.size()));
					} catch (EpiDataException e) {
						_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while setting list order...",100L);
						_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				newDetailRecord.set("USERID", WPUserUtil.getUserId(state));
				//Save new detail record
				newDetailRecord.save();				
			}
						
			
		}
		try {
			state.getDefaultUnitOfWork().saveUOW(true);	
		} catch (EpiException e) {										
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving record",100L);										
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Exiting WPSaveFilter",100L);
		return RET_CONTINUE;
		
		
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
