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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing;

import java.util.ArrayList;

public class PrintLabelInputObj {
	private String printingType;
	private String wavekey;
	private ArrayList<String> assignmentNumber;
	private String standardPrinterName;
	private String rfidPrinterName;
	private String count="1";//copies default to 1
	public ArrayList<String> getAssignmentNumber() {
		return assignmentNumber;
	}
	public void setAssignmentNumber(ArrayList<String> assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}
	public String getRfidPrinterName() {
		return rfidPrinterName;
	}
	public void setRfidPrinterName(String rfidPrinterName) {
		this.rfidPrinterName = rfidPrinterName;
	}
	public String getStandardPrinterName() {
		return standardPrinterName;
	}
	public void setStandardPrinterName(String standardPrinterName) {
		this.standardPrinterName = standardPrinterName;
	}
	public String getWavekey() {
		return wavekey;
	}
	public void setWavekey(String wavekey) {
		this.wavekey = wavekey;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPrintingType() {
		return printingType;
	}
	public void setPrintingType(String printingType) {
		this.printingType = printingType;
	}
	
}
