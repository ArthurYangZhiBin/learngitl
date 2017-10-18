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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class WorkOrderPacketRptAction extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WorkOrderPacketRptAction.class);

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
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		
		if (headerFocus instanceof BioCollection){			
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)headerForm;
			ArrayList selectedItems = headerListForm.getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0)
			{				
				Iterator bioBeanIter = selectedItems.iterator();
				BioBean bio=null;				
				String workOrderKeyFrom = "ZZZZZZZZZZZZZZZ";
				String workOrderKeyTo = "0";

				for(; bioBeanIter.hasNext();){					
					bio = (BioBean)bioBeanIter.next();					
					String currentWorkOrderKey = bio.getString("WORKORDERKEY");
					
					if ((currentWorkOrderKey.compareToIgnoreCase(workOrderKeyFrom) < 0)){
						workOrderKeyFrom = currentWorkOrderKey;
					}
					if ((currentWorkOrderKey.compareToIgnoreCase(workOrderKeyTo) > 0)){
						workOrderKeyTo = currentWorkOrderKey;
					}
				}

				_log.debug("LOG_SYSTEM_OUT","\n\t" + workOrderKeyFrom + " " + workOrderKeyTo + "\n",100L);				

				//DateFormat dateFormat= SimpleDateFormat.getDateInstance( DateFormat.SHORT);
				
				DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pStartDate= "";
				String pEndDate = "";
				if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
					pStartDate = "01/01/1900";
					pEndDate = dateFormat.format(currentDateTime);
				} else {
					pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
					pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
				}
				
				
				//String pStartDate = "01/01/00";
				

				HashMap paramsAndValues = new HashMap();				
			    paramsAndValues.put("p_woStart",  workOrderKeyFrom);
			    paramsAndValues.put("p_woEnd", workOrderKeyTo);
			    paramsAndValues.put("p_pStartDate",pStartDate);
			    paramsAndValues.put("p_pEndDate",pEndDate);
			    
			    
			    String report_url= ReportUtil.retrieveReportURL(state,"CRPT74", paramsAndValues);
				_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
				//Put URL into context
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				userCtx.put("REPORTURL", report_url);

				result.setSelectedItems(null);
				result.setFocus(headerFocus);
			}else{
				UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
	 	   		throw UsrExcp;

				
			}
		}else{			
			callActionFromNormalForm(context,headerFocus);
			result.setFocus(headerFocus);
		}
		return RET_CONTINUE;
	}
	
	
	private void callActionFromNormalForm(ActionContext context,DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
			StateInterface state = context.getState();
			headerFocus = headerFocus;
			String workOrderKeyFrom = headerFocus.getValue("WORKORDERKEY").toString();
			String workOrderKeyTo = headerFocus.getValue("WORKORDERKEY").toString();			
			
			DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
			Date currentDateTime = new Date(System.currentTimeMillis());

			String pStartDate = "";
			
			if(ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT))
				pStartDate = "01/01/1900"  ;
				else
				pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
			
			String pEndDate = dateFormat.format(currentDateTime);			
			
			HashMap paramsAndValues = new HashMap();			
			paramsAndValues.put("p_woStart",  workOrderKeyFrom);
			paramsAndValues.put("p_woEnd", workOrderKeyTo);
			paramsAndValues.put("p_pStartDate",pStartDate);
			paramsAndValues.put("p_pEndDate",pEndDate);			
			
			String report_url= ReportUtil.retrieveReportURL(state,"CRPT74", paramsAndValues);
			_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
			//Put URL into context
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userCtx.put("REPORTURL", report_url);
		}
	}
}



