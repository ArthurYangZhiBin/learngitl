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
package com.infor.scm.wms.util.validation.screen.asn;

import com.infor.scm.wms.util.validation.screen.BaseScreenVO;

public class ASNDetailVO extends BaseScreenVO {

	//Fields
	private String adddate = null;
	private String addwho = null;
	private String altsku = null;
	private String casecnt = null;
	private String conditioncode = null;
	private String containerkey = null;
	private String cube = null;
	private String datereceived = null;
	private String dispositioncode = null;
	private String dispositiontype = null;
	private String editdate = null;
	private String editwho = null;
	private String effectivedate = null;
	private String extendedprice = null;
	private String externallot = null;
	private String externlineno = null;
	private String externreceiptkey = null;
	private String forte_flag = null;
	private String grosswgt = null;
	private String id = null;
	private String innerpack = null;
	private String ipskey = null;
	private String lottable01 = null;
	private String lottable02 = null;
	private String lottable03 = null;
	private String lottable04 = null;
	private String lottable05 = null;
	private String lottable06 = null;
	private String lottable07 = null;
	private String lottable08 = null;
	private String lottable09 = null;
	private String lottable10 = null;
	private String lottable11 = null;
	private String lottable12 = null;
	private String matchlottable = null;
	private String netwgt = null;
	private String notes = null;
	private String otherunit1 = null;
	private String otherunit2 = null;
	private String packingslipqty = null;
	private String packkey = null;
	private String pallet = null;
	private String palletid = null;
	private String pokey = null;
	private String polinenumber = null;
	private String qcautoadjust = null;
	private String qcqtyinspected = null;
	private String qcqtyrejected = null;
	private String qcrejreason = null;
	private String qcrequired = null;
	private String qcstatus = null;
	private String qcuser = null;
	private String qtyadjusted = null;
	private String qtyexpected = null;
	private String qtyreceived = null;
	private String qtyrejected = null;
	private String reasoncode = null;
	private String receiptdetailid = null;
	private String receiptkey = null;
	private String receiptlinenumber = null;
	private String returncondition = null;
	private String returnreason = null;
	private String returntype = null;
	private String rma = null;
	private String serialkey = null;
	private String sku = null;
	private String status = null;
	private String storerkey = null;
	private String supplierkey = null;
	private String suppliername = null;
	private String susr1 = null;
	private String susr2 = null;
	private String susr3 = null;
	private String susr4 = null;
	private String susr5 = null;
	private String tariffkey = null;
	private String toid = null;
	private String toloc = null;
	private String tolot = null;
	private String type = null;
	private String unitprice = null;
	private String uom = null;
	private String vesselkey = null;
	private String voyagekey = null;
	private String whseid = null;
	private String xdockkey = null;
	
	public String getAltsku() {
		return altsku;
	}
	public void setAltsku(String altsku) {
		this.altsku = altsku;
	}
	public String getCasecnt() {
		return casecnt;
	}
	public void setCasecnt(String casecnt) {
		this.casecnt = casecnt;
	}
	public String getConditioncode() {
		return conditioncode;
	}
	public void setConditioncode(String conditioncode) {
		this.conditioncode = conditioncode;
	}
	public String getContainerkey() {
		return containerkey;
	}
	public void setContainerkey(String containerkey) {
		this.containerkey = containerkey;
	}
	public String getCube() {
		return cube;
	}
	public void setCube(String cube) {
		this.cube = cube;
	}
	public String getDatereceived() {
		return datereceived;
	}
	public void setDatereceived(String datereceived) {
		this.datereceived = datereceived;
	}
	public String getDispositioncode() {
		return dispositioncode;
	}
	public void setDispositioncode(String dispositioncode) {
		this.dispositioncode = dispositioncode;
	}
	public String getDispositiontype() {
		return dispositiontype;
	}
	public void setDispositiontype(String dispositiontype) {
		this.dispositiontype = dispositiontype;
	}
	public String getEffectivedate() {
		return effectivedate;
	}
	public void setEffectivedate(String effectivedate) {
		this.effectivedate = effectivedate;
	}
	public String getExtendedprice() {
		return extendedprice;
	}
	public void setExtendedprice(String extendedprice) {
		this.extendedprice = extendedprice;
	}
	public String getExternallot() {
		return externallot;
	}
	public void setExternallot(String externallot) {
		this.externallot = externallot;
	}
	public String getExternlineno() {
		return externlineno;
	}
	public void setExternlineno(String externlineno) {
		this.externlineno = externlineno;
	}
	public String getExternreceiptkey() {
		return externreceiptkey;
	}
	public void setExternreceiptkey(String externreceiptkey) {
		this.externreceiptkey = externreceiptkey;
	}
	public String getForte_flag() {
		return forte_flag;
	}
	public void setForte_flag(String forte_flag) {
		this.forte_flag = forte_flag;
	}
	public String getGrosswgt() {
		return grosswgt;
	}
	public void setGrosswgt(String grosswgt) {
		this.grosswgt = grosswgt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInnerpack() {
		return innerpack;
	}
	public void setInnerpack(String innerpack) {
		this.innerpack = innerpack;
	}
	public String getIpskey() {
		return ipskey;
	}
	public void setIpskey(String ipskey) {
		this.ipskey = ipskey;
	}
	public String getLottable01() {
		return lottable01;
	}
	public void setLottable01(String lottable01) {
		this.lottable01 = lottable01;
	}
	public String getLottable02() {
		return lottable02;
	}
	public void setLottable02(String lottable02) {
		this.lottable02 = lottable02;
	}
	public String getLottable03() {
		return lottable03;
	}
	public void setLottable03(String lottable03) {
		this.lottable03 = lottable03;
	}
	public String getLottable04() {
		return lottable04;
	}
	public void setLottable04(String lottable04) {
		this.lottable04 = lottable04;
	}
	public String getLottable05() {
		return lottable05;
	}
	public void setLottable05(String lottable05) {
		this.lottable05 = lottable05;
	}
	public String getLottable06() {
		return lottable06;
	}
	public void setLottable06(String lottable06) {
		this.lottable06 = lottable06;
	}
	public String getLottable07() {
		return lottable07;
	}
	public void setLottable07(String lottable07) {
		this.lottable07 = lottable07;
	}
	public String getLottable08() {
		return lottable08;
	}
	public void setLottable08(String lottable08) {
		this.lottable08 = lottable08;
	}
	public String getLottable09() {
		return lottable09;
	}
	public void setLottable09(String lottable09) {
		this.lottable09 = lottable09;
	}
	public String getLottable10() {
		return lottable10;
	}
	public void setLottable10(String lottable10) {
		this.lottable10 = lottable10;
	}
	public String getLottable11() {
		return lottable11;
	}
	public void setLottable11(String lottable11) {
		this.lottable11 = lottable11;
	}
	public String getLottable12() {
		return lottable12;
	}
	public void setLottable12(String lottable12) {
		this.lottable12 = lottable12;
	}
	public String getMatchlottable() {
		return matchlottable;
	}
	public void setMatchlottable(String matchlottable) {
		this.matchlottable = matchlottable;
	}
	public String getNetwgt() {
		return netwgt;
	}
	public void setNetwgt(String netwgt) {
		this.netwgt = netwgt;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getOtherunit1() {
		return otherunit1;
	}
	public void setOtherunit1(String otherunit1) {
		this.otherunit1 = otherunit1;
	}
	public String getOtherunit2() {
		return otherunit2;
	}
	public void setOtherunit2(String otherunit2) {
		this.otherunit2 = otherunit2;
	}
	public String getPackingslipqty() {
		return packingslipqty;
	}
	public void setPackingslipqty(String packingslipqty) {
		this.packingslipqty = packingslipqty;
	}
	public String getPackkey() {
		return packkey;
	}
	public void setPackkey(String packkey) {
		this.packkey = packkey;
	}
	public String getPallet() {
		return pallet;
	}
	public void setPallet(String pallet) {
		this.pallet = pallet;
	}
	public String getPalletid() {
		return palletid;
	}
	public void setPalletid(String palletid) {
		this.palletid = palletid;
	}
	public String getPokey() {
		return pokey;
	}
	public void setPokey(String pokey) {
		this.pokey = pokey;
	}
	public String getPolinenumber() {
		return polinenumber;
	}
	public void setPolinenumber(String polinenumber) {
		this.polinenumber = polinenumber;
	}
	public String getQcautoadjust() {
		return qcautoadjust;
	}
	public void setQcautoadjust(String qcautoadjust) {
		this.qcautoadjust = qcautoadjust;
	}
	public String getQcqtyinspected() {
		return qcqtyinspected;
	}
	public void setQcqtyinspected(String qcqtyinspected) {
		this.qcqtyinspected = qcqtyinspected;
	}
	public String getQcqtyrejected() {
		return qcqtyrejected;
	}
	public void setQcqtyrejected(String qcqtyrejected) {
		this.qcqtyrejected = qcqtyrejected;
	}
	public String getQcrejreason() {
		return qcrejreason;
	}
	public void setQcrejreason(String qcrejreason) {
		this.qcrejreason = qcrejreason;
	}
	public String getQcrequired() {
		return qcrequired;
	}
	public void setQcrequired(String qcrequired) {
		this.qcrequired = qcrequired;
	}
	public String getQcstatus() {
		return qcstatus;
	}
	public void setQcstatus(String qcstatus) {
		this.qcstatus = qcstatus;
	}
	public String getQcuser() {
		return qcuser;
	}
	public void setQcuser(String qcuser) {
		this.qcuser = qcuser;
	}
	public String getQtyadjusted() {
		return qtyadjusted;
	}
	public void setQtyadjusted(String qtyadjusted) {
		this.qtyadjusted = qtyadjusted;
	}
	public String getQtyexpected() {
		return qtyexpected;
	}
	public void setQtyexpected(String qtyexpected) {
		this.qtyexpected = qtyexpected;
	}
	public String getQtyreceived() {
		return qtyreceived;
	}
	public void setQtyreceived(String qtyreceived) {
		this.qtyreceived = qtyreceived;
	}
	public String getQtyrejected() {
		return qtyrejected;
	}
	public void setQtyrejected(String qtyrejected) {
		this.qtyrejected = qtyrejected;
	}
	public String getReasoncode() {
		return reasoncode;
	}
	public void setReasoncode(String reasoncode) {
		this.reasoncode = reasoncode;
	}
	public String getReceiptdetailid() {
		return receiptdetailid;
	}
	public void setReceiptdetailid(String receiptdetailid) {
		this.receiptdetailid = receiptdetailid;
	}
	public String getReceiptkey() {
		return receiptkey;
	}
	public void setReceiptkey(String receiptkey) {
		this.receiptkey = receiptkey;
	}
	public String getReceiptlinenumber() {
		return receiptlinenumber;
	}
	public void setReceiptlinenumber(String receiptlinenumber) {
		this.receiptlinenumber = receiptlinenumber;
	}
	public String getReturncondition() {
		return returncondition;
	}
	public void setReturncondition(String returncondition) {
		this.returncondition = returncondition;
	}
	public String getReturnreason() {
		return returnreason;
	}
	public void setReturnreason(String returnreason) {
		this.returnreason = returnreason;
	}
	public String getReturntype() {
		return returntype;
	}
	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}
	public String getRma() {
		return rma;
	}
	public void setRma(String rma) {
		this.rma = rma;
	}
	public String getSerialkey() {
		return serialkey;
	}
	public void setSerialkey(String serialkey) {
		this.serialkey = serialkey;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStorerkey() {
		return storerkey;
	}
	public void setStorerkey(String storerkey) {
		this.storerkey = storerkey;
	}
	public String getSupplierkey() {
		return supplierkey;
	}
	public void setSupplierkey(String supplierkey) {
		this.supplierkey = supplierkey;
	}
	public String getSuppliername() {
		return suppliername;
	}
	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}
	public String getSusr1() {
		return susr1;
	}
	public void setSusr1(String susr1) {
		this.susr1 = susr1;
	}
	public String getSusr2() {
		return susr2;
	}
	public void setSusr2(String susr2) {
		this.susr2 = susr2;
	}
	public String getSusr3() {
		return susr3;
	}
	public void setSusr3(String susr3) {
		this.susr3 = susr3;
	}
	public String getSusr4() {
		return susr4;
	}
	public void setSusr4(String susr4) {
		this.susr4 = susr4;
	}
	public String getSusr5() {
		return susr5;
	}
	public void setSusr5(String susr5) {
		this.susr5 = susr5;
	}
	public String getTariffkey() {
		return tariffkey;
	}
	public void setTariffkey(String tariffkey) {
		this.tariffkey = tariffkey;
	}
	public String getToid() {
		return toid;
	}
	public void setToid(String toid) {
		this.toid = toid;
	}
	public String getToloc() {
		return toloc;
	}
	public void setToloc(String toloc) {
		this.toloc = toloc;
	}
	public String getTolot() {
		return tolot;
	}
	public void setTolot(String tolot) {
		this.tolot = tolot;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getVesselkey() {
		return vesselkey;
	}
	public void setVesselkey(String vesselkey) {
		this.vesselkey = vesselkey;
	}
	public String getVoyagekey() {
		return voyagekey;
	}
	public void setVoyagekey(String voyagekey) {
		this.voyagekey = voyagekey;
	}
	public String getWhseid() {
		return whseid;
	}
	public void setWhseid(String whseid) {
		this.whseid = whseid;
	}
	public String getXdockkey() {
		return xdockkey;
	}
	public void setXdockkey(String xdockkey) {
		this.xdockkey = xdockkey;
	}

	
}
