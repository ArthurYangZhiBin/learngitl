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
package com.infor.scm.waveplanning.wp_query_builder.util;

import java.util.TimeZone;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.state.StateInterface;

public class QueryBuilderInputObj {
	private String callType;
	private TimeZone userTimeZone;
	//wave limit parameters
	private int maxOrders;
	private int maxOrderLines;
	private double maxCube;
	private double maxWeight;
	private double maxCases;
	private String userQry;
	//Order limit parameters
	private double maxEachOrderLines;
	private double maxEachOrderCube;
	private double maxEachOrderWeight;
	private double maxEachOrderQty;
	private StateInterface state;
	private double minEachOrderLines;
	private double minEachOrderCube;
	private double minEachOrderWeight;
	private double minEachOrderQty;
	
	private ActionContext ctx;
	
	private boolean fromSaveFilter;
	
	public boolean isFromSaveFilter() {
		return fromSaveFilter;
	}
	public ActionContext getCtx() {
		return ctx;
	}
	public void setCtx(ActionContext ctx) {
		this.ctx = ctx;
	}
	public void setFromSaveFilter(boolean fromSaveFilter) {
		this.fromSaveFilter = fromSaveFilter;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public TimeZone getUserTimeZone() {
		return userTimeZone;
	}
	public void setUserTimeZone(TimeZone userTimeZone) {
		this.userTimeZone = userTimeZone;
	}
	public double getMaxCases() {
		return maxCases;
	}
	public void setMaxCases(double maxCases) {
		this.maxCases = maxCases;
	}
	public double getMaxCube() {
		return maxCube;
	}
	public void setMaxCube(double maxCube) {
		this.maxCube = maxCube;
	}
	public double getMaxEachOrderCube() {
		return maxEachOrderCube;
	}
	public void setMaxEachOrderCube(double maxEachOrderCube) {
		this.maxEachOrderCube = maxEachOrderCube;
	}
	public double getMaxEachOrderLines() {
		return maxEachOrderLines;
	}
	public void setMaxEachOrderLines(double maxEachOrderLines) {
		this.maxEachOrderLines = maxEachOrderLines;
	}
	public double getMaxEachOrderQty() {
		return maxEachOrderQty;
	}
	public void setMaxEachOrderQty(double maxEachOrderQty) {
		this.maxEachOrderQty = maxEachOrderQty;
	}
	public double getMaxEachOrderWeight() {
		return maxEachOrderWeight;
	}
	public void setMaxEachOrderWeight(double maxEachOrderWeight) {
		this.maxEachOrderWeight = maxEachOrderWeight;
	}
	public int getMaxOrderLines() {
		return maxOrderLines;
	}
	public void setMaxOrderLines(int maxOrderLines) {
		this.maxOrderLines = maxOrderLines;
	}
	public int getMaxOrders() {
		return maxOrders;
	}
	public void setMaxOrders(int maxOrders) {
		this.maxOrders = maxOrders;
	}
	public double getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}
	public StateInterface getState() {
		return state;
	}
	public void setState(StateInterface state) {
		this.state = state;
	}
	public String getUserQry() {
		return userQry;
	}
	public void setUserQry(String userQry) {
		this.userQry = userQry;
	}
	public double getMinEachOrderCube() {
		return minEachOrderCube;
	}
	public void setMinEachOrderCube(double minEachOrderCube) {
		this.minEachOrderCube = minEachOrderCube;
	}
	public double getMinEachOrderLines() {
		return minEachOrderLines;
	}
	public void setMinEachOrderLines(double minEachOrderLines) {
		this.minEachOrderLines = minEachOrderLines;
	}
	public double getMinEachOrderQty() {
		return minEachOrderQty;
	}
	public void setMinEachOrderQty(double minEachOrderQty) {
		this.minEachOrderQty = minEachOrderQty;
	}
	public double getMinEachOrderWeight() {
		return minEachOrderWeight;
	}
	public void setMinEachOrderWeight(double minEachOrderWeight) {
		this.minEachOrderWeight = minEachOrderWeight;
	}

}
