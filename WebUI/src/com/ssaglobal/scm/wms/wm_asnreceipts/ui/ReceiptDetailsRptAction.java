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

public class ReceiptDetailsRptAction extends ListSelector {
	private final String LISTVIEW = "closeModalDialog72";	
	private final String DETAILVIEW = "closeModalDialog71";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptDetailsRptAction.class);
	private static final String REPORT_URL = "/cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Infor%20WM%27%5d%2freport%5b%40name%3d%27Receipt%20Detail%27%5d&prompt=false";
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
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Release Job\n\n",100L);
			//Executes SP name:NSPRECEIPTCLOSE params:RECEIPTKEY
			
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)headerForm;
			ArrayList selectedItems = headerListForm.getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
				Iterator bioBeanIter = selectedItems.iterator();
				BioBean bio=null;
				
				String receiptStartS = "ZZZZZZZZZZZZZZZ";
				String receiptEndS = "0";
				
				
				String ownerStartS = "ZZZZZZZZZZZZZZZ";
				String ownertEndS = "0";

				for(; bioBeanIter.hasNext();){
					_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
					bio = (BioBean)bioBeanIter.next();
					String currentReceiptS = bio.getString("RECEIPTKEY");
					String currentOwnerS = bio.getString("STORERKEY");
					
					if ((currentReceiptS.compareToIgnoreCase(receiptStartS) < 0)){
						receiptStartS = currentReceiptS;
					}
					if ((currentReceiptS.compareToIgnoreCase(receiptEndS) > 0)){
						receiptEndS = currentReceiptS;
					}
					
					if ((currentOwnerS.compareToIgnoreCase(ownerStartS) < 0)){
						ownerStartS = currentOwnerS;
					}
					if ((currentOwnerS.compareToIgnoreCase(ownertEndS) > 0)){
						ownertEndS = currentOwnerS;
					}

				}
				_log.debug("LOG_SYSTEM_OUT","\n\t" + receiptStartS + " " + receiptEndS + "\n",100L);
				

				//Previous cognos version
				/* 
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");				
				String pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
				*/
				
				//added date format compatibility for BIRT 
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
				
				
				
				
				HashMap paramsAndValues = new HashMap();
				paramsAndValues.put("p_pOwnerFrom",ownerStartS);
			    paramsAndValues.put("p_OwnerTo", ownertEndS);
			    paramsAndValues.put("p_ReceiptStart",  receiptStartS);
			    paramsAndValues.put("p_ReceiptTo", receiptEndS);
			    paramsAndValues.put("p_RDateFrom",pStartDate);
			    paramsAndValues.put("p_RDateTo",pEndDate);
			    
			    
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT59", paramsAndValues);
				_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
				//Put URL into context
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				userCtx.put("REPORTURL", report_url);

				result.setSelectedItems(null);
				result.setFocus(headerFocus);
//				context.setNavigation(LISTVIEW);
			}else{
				UserException UsrExcp = new UserException("WMEXP_NONE_SELECTED", new Object[]{});
	 	   		throw UsrExcp;

			}
		}else{
			_log.debug("LOG_SYSTEM_OUT","I am on a detail form",100L);
			callActionFromNormalForm(context,headerFocus);
			result.setFocus(headerFocus);
//			context.setNavigation(DETAILVIEW);
		}

		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);

		return RET_CONTINUE;
	}	
	private void callActionFromNormalForm(ActionContext context,DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
				headerFocus = headerFocus;
				StateInterface state = context.getState();
				headerFocus = headerFocus;
				String receiptStartS = headerFocus.getValue("RECEIPTKEY").toString();
				String receiptEndS = headerFocus.getValue("RECEIPTKEY").toString();
				String ownerStartS = headerFocus.getValue("STORERKEY").toString();//"0";
				String ownertEndS = headerFocus.getValue("STORERKEY").toString();//"ZZZZZZZZZZZZZZZ";
				
				//Previous cognos version
				/*
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");				
				String pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
				*/
				
				//added date format compatibility for BIRT 
				DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pStartDate = "";
				String pEndDate = "";
				if(ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT))
				{
				pStartDate = "01/01/1900";
				pEndDate = dateFormat.format(currentDateTime);
			} else {
				pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
				pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
			}
				

				
				
				HashMap paramsAndValues = new HashMap();
				paramsAndValues.put("p_pOwnerFrom",ownerStartS);
				paramsAndValues.put("p_OwnerTo", ownertEndS);
				paramsAndValues.put("p_ReceiptStart",  receiptStartS);
				paramsAndValues.put("p_ReceiptTo", receiptEndS);
				paramsAndValues.put("p_RDateFrom",pStartDate);
				paramsAndValues.put("p_RDateTo",pEndDate);
				
				
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT59", paramsAndValues);
				_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
				//Put URL into context
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				userCtx.put("REPORTURL", report_url);
		}
	}
}
