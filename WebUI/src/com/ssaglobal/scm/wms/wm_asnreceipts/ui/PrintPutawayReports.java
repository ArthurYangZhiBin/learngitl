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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.ActiveFlag;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.WorkFlowConfigurationHelper;

public class PrintPutawayReports extends ListSelector {
	private final String LISTVIEW = "closeModalDialog72";	
	private final String DETAILVIEW = "closeModalDialog71";	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrintPutawayReports.class);
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
		_log.debug("LOG_SYSTEM_OUT","I am on a detail form",100L);
		callActionFromNormalForm(context,headerFocus);
		result.setFocus(headerFocus);

		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);

		return RET_CONTINUE;
	}	
	private void callActionFromNormalForm(ActionContext context,DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
			StateInterface state = context.getState();
			headerFocus = headerFocus;
			String receiptStartS = headerFocus.getValue("RECEIPTKEY").toString();
			String receiptEndS = headerFocus.getValue("RECEIPTKEY").toString();
			String ownerStartS = "0";
			String ownertEndS = "ZZZZZZZZZZZZZZZ";
			
			DateFormat dateFormat = ReportUtil.retrieveDateFormat(state);
			Date currentDateTime = new Date(System.currentTimeMillis());
			String pEndDate = "";
			String pStartDate = "";
			if (ReportUtil.isBIRT(state) == true) {
				pStartDate = "01/01/1900";
				pEndDate = dateFormat.format(currentDateTime);
			} else {
				pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
				pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
			}
			
			
			
			HashMap paramsAndValues = new HashMap();
//			paramsAndValues.put("p_pOwnerFrom",ownerStartS);
//			paramsAndValues.put("p_OwnerTo", ownertEndS);
			paramsAndValues.put("p_ReceiptStart",  receiptStartS);
			paramsAndValues.put("p_ReceiptEnd", receiptEndS); //corrected the parameter name
			
			//paramsAndValues.put("p_ReceiptTo", receiptEndS);
//			paramsAndValues.put("p_RDateFrom",pStartDate);
			paramsAndValues.put("p_pDate",pEndDate);
			
			ActiveFlag workflowFlag = WorkFlowConfigurationHelper.getTypeStatus(WorkFlowConfigurationHelper.Type.PRTPARPT, receiptStartS, context.getState().getTempUnitOfWork());
			String report_url= ReportUtil.retrieveReportURL(state,"CRPT58", paramsAndValues, workflowFlag);
			_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
			//Put URL into context
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userCtx.put("REPORTURL", report_url);
		}
	}
}
