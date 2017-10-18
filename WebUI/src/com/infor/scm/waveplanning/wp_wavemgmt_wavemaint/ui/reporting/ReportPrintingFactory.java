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

public class ReportPrintingFactory {
	public static ReportingInterface getReportPrintingFacility(String reportType){
		if(WaveReportPrintingConstants.PICK_TICKETS.equalsIgnoreCase(reportType)){
			return new PickTicketsPrinting();
		}else if(WaveReportPrintingConstants.PICK_LISTS.equalsIgnoreCase(reportType)){
			return new PickListsPrinting();
		}else if(WaveReportPrintingConstants.ORDER_SHEETS.equalsIgnoreCase(reportType)){
				return new OrderSheetsPrinting();	
		}else if(WaveReportPrintingConstants.PACKING_LISTS.equalsIgnoreCase(reportType)){
				return new PackingListsPrinting();	
		}else{
			return null;
		}
	}
}
