/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

/**
 * TODO Document ShipmentOrderSummary class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ShipmentOrderSummary {

	private   String orderKey;
	private   String extrenOrderKey;
	private   String totalPallets;
	private   String totalCases;
	private   String totalEaches;
	private   String totalCube;
	private   String totalNetWeight;
	private   String totalGrossWeight;

	
	public String getExtrenOrderKey() {
		return extrenOrderKey;
	}
	
	public void setExtrenOrderKey(String extrenOrderKey) {
		this.extrenOrderKey = extrenOrderKey;
	}
	
	public String getOrderKey() {
		return orderKey;
	}
	
	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}
	
	public String getTotalCases() {
		return totalCases;
	}
	
	public void setTotalCases(String totalCases) {
		this.totalCases = totalCases;
	}
	
	public String getTotalCube() {
		return totalCube;
	}
	
	public void setTotalCube(String totalCube) {
		this.totalCube = totalCube;
	}
	
	public String getTotalEaches() {
		return totalEaches;
	}
	
	public void setTotalEaches(String totalEaches) {
		this.totalEaches = totalEaches;
	}
	
	public String getTotalGrossWeight() {
		return totalGrossWeight;
	}
	
	public void setTotalGrossWeight(String totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}

	public String getTotalNetWeight() {
		return totalNetWeight;
	}
	
	public void setTotalNetWeight(String totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}
	
	public String getTotalPallets() {
		return totalPallets;
	}
	
	public void setTotalPallets(String totalPallets) {
		this.totalPallets = totalPallets;
	}
}

