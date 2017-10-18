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
package com.ssaglobal.scm.wms.wm_load_schedule.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class LoadSchedulePlanningRpt extends ListSelector {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadSchedulePlanningRpt.class);

	@Override
	protected int execute( ModalActionContext context, ActionResult result ) throws EpiException {
		return RET_CONTINUE;
		
	}


	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ shellForm.getName(),100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		if (headerFocus instanceof BioCollection){
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Load Schedule Planning Report \n\n",100L);
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Executing Load Schedule Planning Report",100L);	
			
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)headerForm;
			ArrayList selectedItems = headerListForm.getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Executing from list view",100L);
				Iterator bioBeanIter = selectedItems.iterator();
				BioBean bio=null;

				/*
				p_pOwnerFrom, p_pOwnerTo, 
				p_pRouteFrom, p_pRouteTo, 
				p_pSchTypeFrom, p_pSchTypeTo,
				p_pDayofWeekFrom, p_pDayofWeekTo, 
				p_pLdSchFrom, p_pLdSchTo, 
				p_pStartDate, p_pEndDate, 
				*/
				

				String LdSchStartS = "ZZZZZZZZZZZZZZZ";
				String LdSchEndS = "0";
				String ownerStartS = "ZZZZZZZZZZZZZZZ";
				String ownerEndS =  "0";
				String routeStartS = "ZZZZZZZZZZZZZZZ";
				String routeEndS = "0";
				String schTypeStartS = "ZZZZZZZZZZZZZZZ";
				String schTypeEndS = "0";
				String dayOfWeekStartS = "ZZZZZZZZZZZZZZZ";
				String dayOdWeekEndS = "0";
				

				for(; bioBeanIter.hasNext();){
					_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
					bio = (BioBean)bioBeanIter.next();
					String currentLdSchS = bio.getString("LOADSCHEDULEKEY");
					String currentOwnerS = bio.getString("STORER");
					String currentRouteS = bio.getString("ROUTE");
					String currentSchTypeS = bio.getString("SCHEDULETYPE");
					String currentDayOfWeekS = bio.getString("DAYOFWEEK");
					//_log.debug("LOG_SYSTEM_OUT","\n\nSTORER val :" +currentOwnerS,100L);
					
					if ((currentLdSchS.compareToIgnoreCase(LdSchStartS) < 0)){
						LdSchStartS = currentLdSchS;
					}
					if ((currentLdSchS.compareToIgnoreCase(LdSchEndS) > 0)){
						LdSchEndS = currentLdSchS;
					}
					
					if ((currentOwnerS.compareToIgnoreCase(ownerStartS) < 0)){
						ownerStartS = currentOwnerS;
					}
					if ((currentOwnerS.compareToIgnoreCase(ownerEndS) > 0)){
						ownerEndS = currentOwnerS;
					}
					if ((currentRouteS.compareToIgnoreCase(routeStartS) < 0)){
						routeStartS = currentRouteS;
					}
					if ((currentRouteS.compareToIgnoreCase(routeEndS) > 0)){
						routeEndS = currentRouteS;
					}
					if ((currentSchTypeS.compareToIgnoreCase(schTypeStartS) < 0)){
						schTypeStartS = currentSchTypeS;
					}
					if ((currentSchTypeS.compareToIgnoreCase(schTypeEndS) > 0)){
						schTypeEndS = currentSchTypeS;
					}
					if ((currentDayOfWeekS.compareToIgnoreCase(dayOfWeekStartS) < 0)){
						dayOfWeekStartS = currentDayOfWeekS;
					}
					if ((currentDayOfWeekS.compareToIgnoreCase(dayOdWeekEndS) > 0)){
						dayOdWeekEndS = currentDayOfWeekS;
					}
				}

				
				DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pStartDate = "";
				String pEndDate = "";

				if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
					pStartDate = "01/01/1900";
					pEndDate = dateFormat.format(currentDateTime);
				} else {
					pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
					pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
				}

				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							
				

	
				
				HashMap paramsAndValues = new HashMap();
					
				paramsAndValues.put("p_pOwnerFrom",ownerStartS);
			    paramsAndValues.put("p_OwnerTo", ownerEndS);
				paramsAndValues.put("p_pRouteFrom",routeStartS);
			    paramsAndValues.put("p_pRouteTo", routeEndS);
			    paramsAndValues.put("p_pSchTypeFrom",schTypeStartS);
			    paramsAndValues.put("p_pSchTypeTo", schTypeEndS);			    
			    paramsAndValues.put("p_pDayofWeekFrom",dayOfWeekStartS);
			    paramsAndValues.put("p_pDayofWeekTo", dayOdWeekEndS);
			    paramsAndValues.put("p_pLdSchFrom",LdSchStartS);
			    paramsAndValues.put("p_pLdSchTo", LdSchEndS);
			    paramsAndValues.put("p_pStartDate",pStartDate);
			    paramsAndValues.put("p_pEndDate", pEndDate);
			   
			    
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT35", paramsAndValues);
				//_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Report URL: " +report_url, 100L);
				//Put URL into context
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				userCtx.put("REPORTURL", report_url);

				result.setSelectedItems(null);
				result.setFocus(headerFocus);

			}
		}else{
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","On a detail form",100L);
			callActionFromNormalForm(context,headerFocus);
			result.setFocus(headerFocus);

		}

		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Exiting Load Schedule Planning Report",100L);

		return RET_CONTINUE;
	}	


	private void callActionFromNormalForm(ActionContext context,DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
			
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Inside callActionFromNormalForm",100L);
				headerFocus = headerFocus;
				StateInterface state = context.getState();
				headerFocus = headerFocus;

			
				String LdSchStartS =  headerFocus.getValue("LOADSCHEDULEKEY").toString();
				String LdSchEndS =  headerFocus.getValue("LOADSCHEDULEKEY").toString();
				String ownerStartS =  headerFocus.getValue("STORER").toString();
				String ownerEndS =   headerFocus.getValue("STORER").toString();
				String routeStartS =  headerFocus.getValue("ROUTE").toString();
				String routeEndS =  headerFocus.getValue("ROUTE").toString();
				String schTypeStartS =  headerFocus.getValue("SCHEDULETYPE").toString();
				String schTypeEndS =  headerFocus.getValue("SCHEDULETYPE").toString();
				String dayOfWeekStartS =  headerFocus.getValue("DAYOFWEEK").toString();
				String dayOdWeekEndS =  headerFocus.getValue("DAYOFWEEK").toString();
				
			
				DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pStartDate = "";
				String pEndDate = "";

				if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
					pStartDate = "01/01/1900";
					pEndDate = dateFormat.format(currentDateTime);
				} else {
					pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
					pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
				}
				
				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				
				
				
				HashMap paramsAndValues = new HashMap();
				paramsAndValues.put("p_pOwnerFrom",ownerStartS);
			    paramsAndValues.put("p_OwnerTo", ownerEndS);
				paramsAndValues.put("p_pRouteFrom",routeStartS);
			    paramsAndValues.put("p_pRouteTo", routeEndS);
			    paramsAndValues.put("p_pSchTypeFrom",schTypeStartS);
			    paramsAndValues.put("p_pSchTypeTo", schTypeEndS);			    
			    paramsAndValues.put("p_pDayofWeekFrom",dayOfWeekStartS);
			    paramsAndValues.put("p_pDayofWeekTo", dayOdWeekEndS);
			    paramsAndValues.put("p_pLdSchFrom",LdSchStartS);
			    paramsAndValues.put("p_pLdSchTo", LdSchEndS);
			    paramsAndValues.put("p_pStartDate",pStartDate);
			    paramsAndValues.put("p_pEndDate", pEndDate);
				
				
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT35", paramsAndValues);
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Report URL: " +report_url,100L);
				//Put URL into context
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				userCtx.put("REPORTURL", report_url);
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH_REPORT","Leaving callActionFromNormalForm",100L);
		}
	}



}
