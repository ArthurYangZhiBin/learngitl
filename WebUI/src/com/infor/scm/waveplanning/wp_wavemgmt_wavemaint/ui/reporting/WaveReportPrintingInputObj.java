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

import java.util.ArrayList;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.state.StateInterface;

public class WaveReportPrintingInputObj {
	private StateInterface state;
	private String waveKey;
	private String reportId;
	private ArrayList<Order> orders;
	EpnyUserContext userCtx;
	public EpnyUserContext getUserCtx() {
		return userCtx;
	}
	public void setUserCtx(EpnyUserContext userCtx) {
		this.userCtx = userCtx;
	}
	public ArrayList<Order> getOrders() {
		return orders;
	}
	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getWaveKey() {
		return waveKey;
	}
	public void setWaveKey(String waveKey) {
		this.waveKey = waveKey;
	}
	public StateInterface getState() {
		return state;
	}
	public void setState(StateInterface state) {
		this.state = state;
	}
	
}
