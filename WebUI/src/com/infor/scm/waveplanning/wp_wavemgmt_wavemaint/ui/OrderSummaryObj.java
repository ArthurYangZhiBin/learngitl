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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

/**
 * TODO Document OrderSummaryObj class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class OrderSummaryObj {
	private String interactionId;
	private String waveKey;
	private String warehouseId;
	private String orderKey;
	private String externalOrderKey;
	private double totalEaches;
	private double totalCases;
	private double totalPallets;
	private double totalCube;
	private double totalNetWeight;
	private double totalGrossWeight;
	public String getInteractionId() {
		return interactionId;
	}
	public void setInteractionId(String interactionId) {
		this.interactionId = interactionId;
	}
	public String getWaveKey() {
		return waveKey;
	}
	public void setWaveKey(String waveKey) {
		this.waveKey = waveKey;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getOrderKey() {
		return orderKey;
	}
	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}
	public String getExternalOrderKey() {
		return externalOrderKey;
	}
	public void setExternalOrderKey(String externalOrderKey) {
		this.externalOrderKey = externalOrderKey;
	}
	public double getTotalEaches() {
		return totalEaches;
	}
	public void setTotalEaches(double totalEaches) {
		this.totalEaches = totalEaches;
	}
	public double getTotalCases() {
		return totalCases;
	}
	public void setTotalCases(double totalCases) {
		this.totalCases = totalCases;
	}
	public double getTotalPallets() {
		return totalPallets;
	}
	public void setTotalPallets(double totalPallets) {
		this.totalPallets = totalPallets;
	}
	public double getTotalCube() {
		return totalCube;
	}
	public void setTotalCube(double totalCube) {
		this.totalCube = totalCube;
	}
	public double getTotalNetWeight() {
		return totalNetWeight;
	}
	public void setTotalNetWeight(double totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}
	public double getTotalGrossWeight() {
		return totalGrossWeight;
	}
	public void setTotalGrossWeight(double totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}
	
	

}
