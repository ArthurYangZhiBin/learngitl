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
package com.infor.scm.waveplanning.wp_wm_wave.wave;

import com.agileitp.forte.framework.Array;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

/**
 * TODO Document WaveInputObj class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveInputObj {
	private String procedureName;
	private Array procedureParameters;
	private String orderKeys;
	private String waveDesc;
	

	public ActionResult getResult() {
		return result;
	}
	public void setResult(ActionResult result) {
		this.result = result;
	}

	//for mass upate 
	private String massUpdateAction;
	private String route;
	private String door;
	private String stop;
	private String stage;
	private String carrierCode;
	public String getDoor() {
		return door;
	}
	public void setDoor(String door) {
		this.door = door;
	}
	public String getMassUpdateAction() {
		return massUpdateAction;
	}
	public void setMassUpdateAction(String massUpdateAction) {
		this.massUpdateAction = massUpdateAction;
	}
	
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	

	
	
	
	
	public String getWaveDesc() {
		return waveDesc;
	}
	public void setWaveDesc(String waveDesc) {
		this.waveDesc = waveDesc;
	}

	private int orderSize;
	
	public int getOrderSize() {
		return orderSize;
	}
	public void setOrderSize(int orderSize) {
		this.orderSize = orderSize;
	}

	private Array dbNames;
	private ActionContext context;
	private ActionResult result;
	private RuntimeFormInterface form;
	
	public WaveInputObj(){
		procedureParameters = new Array();
	}
	
	//for create wave
	private String filterId;
	//for 
	private String waveKey="";
	public String getFilterId(){
		return this.filterId;
	}
	public void setFitlerId(String filterId){
		this.filterId = filterId;
	}
	public String getWaveKey(){
		return this.waveKey;
	}
	public void setWaveKey(String waveKey){
		this.waveKey = waveKey;
	}
	private String deleteFilter;
	
	public void setProcedureName(String procedureName){
		this.procedureName = procedureName;
	}
	public String getProcedureName(){
		return this.procedureName;
	}
	public void setProcedureParameters(Array procedureParameters){
		this.procedureParameters = procedureParameters;
	}
	public Array getProcedureParametes(){
		return this.procedureParameters;
	}
	public Array getDbNames() {
		return dbNames;
	}
	public void setDbNames(Array dbNames) {
		this.dbNames = dbNames;
	}
	
	public ActionContext getActionContext() {
		return context;
	}
	public void setActionContext(ActionContext context) {
		this.context = context;
	}
	public ActionResult getActionResult() {
		return result;
	}
	public void setActionResult(ActionResult result) {
		this.result = result;
	}	
	public RuntimeFormInterface getForm() {
		return form;
	}
	public void setForm(RuntimeFormInterface form) {
		this.form = form;
	}
	public String getDeleteFilter() {
		return deleteFilter;
	}
	public void setDeleteFilter(String deleteFilter) {
		this.deleteFilter = deleteFilter;
	}
	public String getOrderKeys() {
		return orderKeys;
	}
	public void setOrderKeys(String orderKeys) {
		this.orderKeys = orderKeys;
	}
	
	//for consolidate wave
	private String storerKey;
	private String sku;
	private String orderType;
	private String consolloc;
	private double maxlocqty;
	private double wavetotal;
	private double minwaveqty;
	private String pieceloc;
	private String caseloc;
	private String include;
	private String loctype;
	
	public String getStorerKey() {
		return storerKey;
	}
	public void setStorerKey(String storerKey) {
		this.storerKey = storerKey;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getConsolloc() {
		return consolloc;
	}
	public void setConsolloc(String consolloc) {
		this.consolloc = consolloc;
	}
	public double getMaxlocqty() {
		return maxlocqty;
	}
	public void setMaxlocqty(double maxlocqty) {
		this.maxlocqty = maxlocqty;
	}
	public double getWavetotal() {
		return wavetotal;
	}
	public void setWavetotal(double wavetotal) {
		this.wavetotal = wavetotal;
	}
	public double getMinwaveqty() {
		return minwaveqty;
	}
	public void setMinwaveqty(double minwaveqty) {
		this.minwaveqty = minwaveqty;
	}
	public String getPieceloc() {
		return pieceloc;
	}
	public void setPieceloc(String pieceloc) {
		this.pieceloc = pieceloc;
	}
	public String getCaseloc() {
		return caseloc;
	}
	public void setCaseloc(String caseloc) {
		this.caseloc = caseloc;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	public String getLoctype() {
		return loctype;
	}
	public void setLoctype(String loctype) {
		this.loctype = loctype;
	}

	
	
	
}
