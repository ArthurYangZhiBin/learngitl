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

public class PickListsPrinting implements ReportingInterface{
	public void doReporting(WaveReportPrintingInputObj input) throws EpiException{
		StateInterface state = input.getState();
		String waveKey = input.getWaveKey();
		HashMap paramsAndValues = new HashMap();;			
		paramsAndValues.put("p_OrdStart","0000000000");
	    paramsAndValues.put("p_OrdEnd", "ZZZZZZZZZZ");
	    paramsAndValues.put("p_WaveStart",  waveKey);
	    paramsAndValues.put("p_WaveEnd", waveKey);
	    paramsAndValues.put("p_AssignStart","0000000000");
	    paramsAndValues.put("p_AssignEnd","ZZZZZZZZZZ");		    
	    String report_url= ReportUtil.retrieveReportURL(state,"CRPT54", paramsAndValues);			
System.out.println("***** report_url="+report_url); 
		EpnyUserContext userCtx = input.getUserCtx();
		userCtx.put("REPORTINGURL", report_url);	
	}
	
	
	
	
	
}
