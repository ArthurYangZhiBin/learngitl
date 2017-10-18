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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.util.Calendar;
import java.util.Date;

public class InternalTransferDetailObject{

	private String lineNum;
	private String fromSku;
	private String fromLoc;
	private String fromLot;
	private String fromId;
	private String fromQty;
	private String fromPack;
	private String fromUOM;
	private String toSku;
	private String toLoc;
	private String toLot;
	private String toId;
	private String toQty;
	private String toPack;
	private String toUOM;
	private String lott01;
	private String lott02;
	private String lott03;
	private String lott04;
	private String lott05;
	private String lott06;
	private String lott07;
	private String lott08;
	private String lott09;
	private String lott10;
	//added for 3PL
	private String grossWgt1;
	private String netWgt1;
	private String tareWgt1;
	
	public String getlineNum(){
		return this.lineNum;
	}
	public void setlineNum(String lineNum){
		this.lineNum = lineNum;
	}
	public String getFromSku(){
		return this.fromSku;
	}
	public void setFromSku(String fromSku){
		this.fromSku = fromSku;
	}
	public String getFromLoc(){
		return this.fromLoc;
	}
	public void setFromLoc(String fromLoc){
		this.fromLoc = fromLoc;
	}
	public String getfromLot(){
		return this.fromLot;
	}
	public void setFromLot(String fromLot){
		this.fromLot = fromLot;
	}		
	public String getFromID(){
		return this.fromId;
	}
	public void setFromID(String fromID){
		this.fromId = fromID;
	}
	public String getFromQty(){
		return this.fromQty;
	}
	public void setFromQty(String fromQty){
		this.fromQty = fromQty;
	}
	public String getFromPack(){
		return this.fromPack;
	}
	public void setFromPack(String fromPack){
		this.fromPack = fromPack;
	}
	public String getFromUOM(){
		return this.fromUOM;
	}
	public void setFromUOM(String fromUOM){
		this.fromUOM = fromUOM;
	}
	
	
	public String getToSku(){
		return this.toSku;
	}
	public void setToSku(String toSku){
		this.toSku = toSku;
	}
	public String getToLoc(){
		return this.toLoc;
	}
	public void setToLoc(String toLoc){
		this.toLoc = toLoc;
	}
	public String getToLot(){
		return this.toLot;
	}
	public void setToLot(String toLot){
		this.toLot = toLot;
	}		
	public String getToID(){
		return this.toId;
	}
	public void setToID(String toID){
		this.toId = toID;
	}
	public String getToQty(){
		return this.toQty;
	}
	public void setToQty(String toQty){
		this.toQty = toQty;
	}
	public String getToPack(){
		return this.toPack;
	}
	public void setToPack(String toPack){
		this.toPack = toPack;
	}
	public String getToUOM(){
		return this.toUOM;
	}
	public void setToUOM(String toUOM){
		this.toUOM = toUOM;
	}

	public String getLott01(){
		return this.lott01;
	}
	public void setLott01(String lott01){
		this.lott01 = lott01;
	}
	public String getLott02(){
		return this.lott02;
	}
	public void setLott02(String lott02){
		this.lott02 = lott02;
	}
	public String getLott03(){
		return this.lott03;
	}
	public void setLott03(String lott03){
		this.lott03 = lott03;
	}
	public String getLott06(){
		return this.lott06;
	}
	public void setLott06(String lott06){
		this.lott06 = lott06;
	}
	public String getLott07(){
		return this.lott07;
	}
	public void setLott07(String lott07){
		this.lott07 = lott07;
	}
	public String getLott08(){
		return this.lott08;
	}
	public void setLott08(String lott08){
		this.lott08 = lott08;
	}
	public String getLott09(){
		return this.lott09;
	}
	public void setLott09(String lott09){
		this.lott09 = lott09;
	}
	public String getLott10(){
		return this.lott10;
	}
	public void setLott10(String lott10){
		this.lott10 = lott10;
	}
	public String getLott04(){
		return this.lott04;
	}
	public void setLott04(String lott04){
		this.lott04 = lott04;
	}
	public String getLott05(){
		return this.lott05;
	}
	public void setLott05(String lott05){
		this.lott05 = lott05;
	}
	public String getGrossWgt1() {
		return grossWgt1;
	}
	public void setGrossWgt1(String grossWgt1) {
		this.grossWgt1 = grossWgt1;
	}
	public String getNetWgt1() {
		return netWgt1;
	}
	public void setNetWgt1(String netWgt1) {
		this.netWgt1 = netWgt1;
	}
	public String getTareWgt1() {
		return tareWgt1;
	}
	public void setTareWgt1(String tareWgt1) {
		this.tareWgt1 = tareWgt1;
	}


	
}
