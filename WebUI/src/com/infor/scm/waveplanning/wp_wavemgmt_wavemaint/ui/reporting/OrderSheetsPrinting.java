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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.reporting;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class OrderSheetsPrinting implements ReportingInterface{
	public void doReporting(WaveReportPrintingInputObj input) throws EpiException{
		StateInterface state = input.getState();
		String waveKey = input.getWaveKey();
		HashMap paramsAndValues = new HashMap();		
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //earlier cognos version
		Date bigDateTime = new Date(2*System.currentTimeMillis());
		String pStartDate = "";
		String pEndDate = "";
		if(ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)){
			pStartDate = "01/01/1900";
			pEndDate = dateFormat.format(bigDateTime);
		}else{
			pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
			pEndDate = dateFormat.format(bigDateTime)+ "%2000%3a00%3a00.000";
		}
		paramsAndValues.put("p_OrdStart","0000000000");
	    paramsAndValues.put("p_OrdEnd", "ZZZZZZZZZZ");
	    paramsAndValues.put("p_pStartDate",pStartDate);
	    paramsAndValues.put("p_pEndDate",pEndDate);		    
	    	    
	    paramsAndValues.put("p_pWaveStart",  waveKey);
	    paramsAndValues.put("p_pWaveEnd", waveKey);
	    paramsAndValues.put("p_src", "W");			//Incident4010793_Defect284325
	    String report_url= ReportUtil.retrieveReportURL(state,"CRPT43", paramsAndValues);			
System.out.println("***** report_url="+report_url); 
		EpnyUserContext userCtx = input.getUserCtx();
		userCtx.put("REPORTINGURL", report_url);	

		
		

	}
}
