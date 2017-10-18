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
package com.ssaglobal.scm.wms.wm_inventory_transaction.ui;

import java.io.Serializable;

import com.ssaglobal.scm.wms.datalayer.*;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_load_schedule.ui.LoadScheduleValidationDataObject;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class ViewSourceDocUtil implements Serializable{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ViewSourceDocUtil.class);

	public static boolean setValues(ViewSourceDocDataObject data)throws DPException{
		_log.debug("LOG_SYSTEM_OUT","\n\n***** In checkDisplayScreen ******\n\n",100L);
		String srcType = data.getSourceType();
	    String query= null;
	    
	    if(srcType.equals("Receipt Line - Add"))
		{
	    	_log.debug("LOG_SYSTEM_OUT","\n\nIt's equal to Receipt Line - Add",100L);	  
	    	data.setkeyAtt("RECEIPTKEY");
	    	data.setLNAtt("RECEIPTLINENUMBER");
			data.setbio("receiptdetail");
			data.setPerspective("wm_inventory_transaction_receipt_detail_form");
	    	//data.setPerspective("detail view");
	    	
	    	query= "select * from receiptdetail where " +data.getkeyAtt()  +"='" +data.getKey() +"' and " +data.getLNAtt() +"='" +data.getLineNumber() +"'";
	    	_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
	    }
	    else if (srcType.equals("Adjustment Line - Add"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\nIt's equal to Adjustment Line - Add",100L);
	    	data.setkeyAtt("ADJUSTMENTKEY");
	    	data.setLNAtt("ADJUSTMENTLINENUMBER");
	    	data.setbio("wm_adjustmentdetail");	 
	    	data.setPerspective("wm_adjustmentdetail_detail_view_perspective");
	    	//data.setPerspective("wm_inventory_transaction_adjustment_detail");
	    	query= "select * from adjustmentdetail where " +data.getkeyAtt()  +"='" +data.getKey() +"' and " +data.getLNAtt() +"='" +data.getLineNumber() +"'";
	    	//_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
	    }
	    else if (srcType.equals("Receipt Reversal"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\nIt's equal to Receipt Reversal",100L);
	    	
	    	data.setkeyAtt("ADJUSTMENTKEY");
	    	data.setLNAtt("ADJUSTMENTLINENUMBER");	    	
	    	data.setbio("wm_adjustmentdetail");
	    	data.setPerspective("wm_adjustmentdetail_detail_view_perspective");
	    	query= "select * from adjustmentdetail where " +data.getkeyAtt()  +"='" +data.getKey() +"' and " +data.getLNAtt() +"='" +data.getLineNumber() +"'";
	    	//_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
	    }		
	    else if (srcType.equals("Transfer Line - Add"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\nIt's equal to Transfer Line - Add",100L);	    	
	    	data.setkeyAtt("TRANSFERKEY");
	    	data.setLNAtt("TRANSFERLINENUMBER");

	    	data.setbio("wm_internaltransferdetail");
	    	data.setPerspective("wm_internal_transfer_detail_detail_view_perspective");
	    	query= "select * from transferdetail where " +data.getkeyAtt()  +"='" +data.getKey() +"' and " +data.getLNAtt() +"='" +data.getLineNumber() +"'";
	    	//_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
	    }
	    else if (srcType.equals("Picking - Update"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\nIt's equal to Picking - Update",100L);	    	
	    	data.setkeyAtt("PICKDETAILKEY");
	    	data.setLNAtt("ORDERLINENUMBER");
	    	_log.debug("LOG_SYSTEM_OUT","\n\n*** LN Attribut set to: " +data.getLNAtt(),100L);
	    	data.setbio("wm_pickdetail");
	    	data.setPerspective("wm_pickdetail_detail_view");
	    	
	    	query= "select * from pickdetail where " +data.getkeyAtt()  +"='" +data.getKey() +"' and " +data.getLNAtt() +"='" +data.getLineNumber() +"'";
	    	//_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
	    	
	    }
	  	_log.debug("LOG_SYSTEM_OUT","\n\n *****query is ViewSourceDocUtil: " +query,100L);
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(query);
		if(dataObject.getCount()> 0){
			_log.debug("LOG_SYSTEM_OUT","\n\n Returning true",100L);
			return true;
		}else{
			_log.debug("LOG_SYSTEM_OUT","\n\n Returning false",100L);
			return false;
		}
	}
	
}
