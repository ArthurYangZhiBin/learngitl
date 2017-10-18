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
package com.infor.scm.wms.util.validation.screen.item;

import java.util.ArrayList;

import com.infor.scm.wms.util.datalayer.query.LocationQueryRunner;
import com.infor.scm.wms.util.validation.screen.BaseScreenVO;

public class ItemScreenVO extends BaseScreenVO{
	
	//db fields not on front end
	private String guid = null;
	private String whseid = null;
	private String manufacturersku = null;
	private String retailsku = null;
	private String altsku = null;
	private String cyclecountfrequency = null;
	private String ioflag = null;
	private String lotxiddetailotherlabel1 = null;
	private String lotxiddetailotherlabel2 = null;
	private String lotxiddetailotherlabel3 = null;
	private String eachkey = null;
	private String casekey = null;
	private String type = null;
	private String effecstartdate = null;
	private String effecenddate = null;
	private String hasimage = null;
	private String active = null;
	private String grosswgt = null;	
	private String innerpack = null;
	private String minimumshelflifeonrfpicking = null;
	private String pickcode = null;
	private String putcode = null;
	private String pickuom = null;
	private String receiptinspectionloc = null;
	private String zonedescr = null;	
	//general tab fields
	
	private String sku = null;
	private String storerkey = null;
	private String descr = null;
	private String packkey = null;
	private String cartongroup = null;
	private String tariffkey = null;
	private String stdcube = null;
	private String cube = null;
	private String itemreference = null;
	private String stdgrosswgt = null;
	private String stdnetwgt = null;
	private String netwgt = null;
	private String tare = null;
	private String cwflag = null;
	private String shelflifeindicator = null;
	private String shelflifecodetype = null;
	private String shelflife = null;
	private String outboundshelflife = null;
	
	private String tobestbydays = null;
	private String toexpiredays = null;
	private String todeliverbydays = null;
	private String nonstockedindicator = null;
	
	//recv/ship tab fields
	private String lottablevalidationkey = null;
	private String rfdefaultpack = null;
	private String onreceiptcopypackkey = null;
	private String rfdefaultuom = null;
	private String skugroup = null;
	private String skugroup2 = null;
	private String shippablecontainer = null;
	private String vert_storage = null;
	private String transportationmode = null;
	private String hazmatcodeskey = null;	
	private String freightclass = null;
	private String wmsclass = null;
	private String shelflifeonreceiving = null;
	
	
	//lottable/barcode tab fields	
	private String lottable01label = null;
	private String lottable02label = null;
	private String lottable03label = null;
	private String lottable04label = null;
	private String lottable05label = null;
	private String lottable06label = null;
	private String lottable07label = null;
	private String lottable08label = null;
	private String lottable09label = null;
	private String lottable10label = null;

	private String lottable11label = null;
	private String lottable12label = null;
	
	private String busr1 = null;
	private String busr2 = null;
	private String busr3 = null;
	private String busr4 = null;
	private String busr5 = null;
	private String busr6 = null;
	private String busr7 = null;
	private String busr8 = null;
	private String busr9 = null;
	private String busr10 = null;
	
	//weight/data tab fields
	private String icwflag = null;	
	private String icdflag = null;	
	private String ideweight = null;		
	private String avgcaseweight = null;
	private String tareweight = null;
	private String tolerancepct = null;
	private String icwby = null;		
	private String icdlabel1 = null;
	private String icdlabel2 = null;
	private String icdlabel3 = null;
	
	private String icdlabel4 = null;
	private String icdlabel5 = null;
	private String icd1unique = null;
	
	private String ocwflag = null;	
	private String ocdflag = null;	
	private String odeweight = null;	
	private String oacoverride = null;	
	private String oavgcaseweight = null;
	private String otareweight = null;
	private String otolerancepct = null;	
	private String ocwby = null;	
	private String ocdlabel1 = null;
	private String ocdlabel2 = null;
	private String ocdlabel3 = null;
	
	private String ocdlabel4 = null;
	private String ocdlabel5 = null;
	private String ocd1unique = null;
	
	private String ocdcatchwhen = null;	
	private String ocdcatchqty1 = null;
	private String ocdcatchqty2 = null;
	private String ocdcatchqty3 = null;
	
	//control tab fields
	private String receiptholdcode = null;
	private String skutype = null;
	private String receiptvalidationtemplate = null;
	private String stacklimit = null;
	private String maxpalletsperzone = null;
	private String manualsetuprequired = null;
	private String putawayzone = null;
	private String putawayloc = null;
	private String qclocout = null;
	private String qcloc = null;
	private String returnsloc = null;
	private String strategykey = null;
	private String defaultrotation = null;
	private String rotateby = null;
	private String datecodedays = null;
	private String serialnumberstart = null;
	private String serialnumbernext = null;
	private String serialnumberend = null;
	private String flowthruitem = null;	
	private String conveyable = null;
	private String verifylot04lot05 = null;
	private String minimumwaveqty = null;
	private String bulkcartongroup = null;
	private String allowconsolidation = null;
	private String putawaystrategykey = null;
	private String dapicksort = null;
	private String rplnsort = null;
	private String cartonizeft = null;
	
	
	//cycle/costs tab fields
	private String abc = null;
	private String lastcyclecount = null;
	private String ccdiscrepancyrule = null;
	private String reorderqty = null;
	private String stdordercost = null;
	private String cost = null;
	private String reorderpoint = null;
	private String price = null;
	private String carrycost = null;
	
	//notes Tab fields
	private String susr1 = null;
	private String susr2 = null;
	private String susr3 = null;
	private String susr4 = null;
	private String susr5 = null;
	private String susr6 = null;
	private String susr7 = null;
	private String susr8 = null;
	private String susr9 = null;
	private String susr10 = null;
	private String notes2 = null;
	private String notes1 = null;
	
	//Serial Number Tab fields
	
	private String snum_autoincrement = null;
	private String snum_delim_count = null;
	private String snum_delimiter = null;
	private String snum_endtoend = null;
	private String snum_incr_length = null;
	private String snum_incr_pos = null;
	private String snum_length = null;
	private String snum_mask = null;
	private String snum_position = null;
	private String snum_quantity = null;
	private String snumlong_delimiter = null;
	private String snumlong_fixed = null;
	
	private String voicegroupingid = null;
	private String countsequence = null;
	
	//SRG -- Catch Weight Capture Fields
	private String enableadvcwgt = null;	
	private String catchgrosswgt = null;	
	private String catchnetwgt = null;	
	private String catchtarewgt = null;	
	private String advcwttrackby = null;	
	private String tarewgt1 = null;	
	private String stdnetwgt1 = null;	
	private String stdgrosswgt1 = null;
	private String stduom = null;	
	private String zerodefaultwgtforpick = null;	
	
	//SRG: 9.2 Upgrade -- Start
	private String nmfcclass = null;	
	private String mateabilitycode = null;
	private String fillqtyuom = null;	
	private String ibsumcwflg = null;
	private String obsumcwflg = null;
	private String showrfcwontrans = null;	
	private String recurcode = null;
	private String wgtuom = null;
	private String dimenuom = null;
	private String cubeuom = null;
	private String storagetype = null;
	private String autoreleaselpnby = null;
	private String hourstoholdlpn = null;
	private String lotholdcode = null;
	private String autoreleaselotby = null;
	private String hourstoholdlot = null;
	private String amstrategykey = null;
	private String putawayclass = null;	
	private String tempforasn = null;
	//SRG: 9.2 Upgrade -- End
	
	//Assign Locs Tab Fields
	private ArrayList assignLocationsVOCollection = null;
	
	//Alt Tab Fields
	private ArrayList altVOCollection = null;
	
	//Substitute Tab Fields
	private ArrayList substituteVOCollection = null;
	
	public String getItem() {
		return sku;
	}
	public void setItem(String item) {
		this.sku = item;
	}
	public String getOwner() {
		return storerkey;
	}
	public void setOwner(String owner) {
		this.storerkey = owner;
	}
	public String getDescription() {
		return descr;
	}
	public void setDescription(String description) {
		this.descr = description;
	}
	public String getPack() {
		return packkey;
	}
	public void setPack(String pack) {
		this.packkey = pack;
	}
	public String getCartonGroup() {
		return cartongroup;
	}
	public void setCartonGroup(String cartonGroup) {
		this.cartongroup = cartonGroup;
	}
	public String getTariff() {
		return tariffkey;
	}
	public void setTariff(String tariff) {
		this.tariffkey = tariff;
	}
	public String getCube() {
		return cube;
	}
	public void setCube(String cube) {
		this.cube = cube;
	}
	public String getItemReference() {
		return itemreference;
	}
	public void setItemReference(String itemReference) {
		this.itemreference = itemReference;
	}
	public String getGrossWeight() {
		return grosswgt;
	}
	public void setGrossWeight(String grossWeight) {
		this.grosswgt = grossWeight;
	}
	public String getNetWeight() {
		return stdnetwgt;
	}
	public void setNetWeight(String netWeight) {
		this.stdnetwgt = netWeight;
	}
	public String getTareWeight() {
		return tare;
	}
	public void setTareWeight(String tareWeight) {
		this.tare = tareWeight;
	}
	public String getCatchWeight() {
		return cwflag;
	}
	public void setCatchWeight(String catchWeight) {
		this.cwflag = catchWeight;
	}
	public String getShelfLifeIndicator() {
		return shelflifeindicator;
	}
	public void setShelfLifeIndicator(String shelfLifeIndicator) {
		this.shelflifeindicator = shelfLifeIndicator;
	}
	public String getShelfLifeCodeType() {
		return shelflifecodetype;
	}
	public void setShelfLifeCodeType(String shelfLifeCodeType) {
		this.shelflifecodetype = shelfLifeCodeType;
	}
	public String getInboundShelfLife() {
		return shelflifeonreceiving;
	}
	public void setInboundShelfLife(String inboundShelfLife) {
		this.shelflifeonreceiving = inboundShelfLife;
	}
	public String getOutboundShelfLife() {
		return outboundshelflife;
	}
	public void setOutboundShelfLife(String outboundShelfLife) {
		this.outboundshelflife = outboundShelfLife;
	}
	public String getLottableValidation() {
		return lottablevalidationkey;
	}
	public void setLottableValidation(String lottableValidation) {
		this.lottablevalidationkey = lottableValidation;
	}
	public String getRfDefaultReceivingPack() {
		return rfdefaultpack;
	}
	public void setRfDefaultReceivingPack(String rfDefaultReceivingPack) {
		this.rfdefaultpack = rfDefaultReceivingPack;
	}
	public String getOnReceiptCopyPack() {
		return onreceiptcopypackkey;
	}
	public void setOnReceiptCopyPack(String onReceiptCopyPack) {
		this.onreceiptcopypackkey = onReceiptCopyPack;
	}
	public String getRfDefaultReceivingUOM() {
		return rfdefaultuom;
	}
	public void setRfDefaultReceivingUOM(String rfDefaultReceivingUOM) {
		this.rfdefaultuom = rfDefaultReceivingUOM;
	}
	public String getItemGroup1() {
		return skugroup;
	}
	public void setItemGroup1(String itemGroup1) {
		this.skugroup = itemGroup1;
	}
	public String getItemGroup2() {
		return skugroup2;
	}
	public void setItemGroup2(String itemGroup2) {
		this.skugroup2 = itemGroup2;
	}
	public String getShippableContainer() {
		return shippablecontainer;
	}
	public void setShippableContainer(String shippableContainer) {
		this.shippablecontainer = shippableContainer;
	}
	public String getVerticalStorage() {
		return vert_storage;
	}
	public void setVerticalStorage(String verticalStorage) {
		this.vert_storage = verticalStorage;
	}
	public String getTransportationMode() {
		return transportationmode;
	}
	public void setTransportationMode(String transportationMode) {
		this.transportationmode = transportationMode;
	}
	public String getHazmatCode() {
		return hazmatcodeskey;
	}
	public void setHazmatCode(String hazmatCode) {
		this.hazmatcodeskey = hazmatCode;
	}
	public String getFreightClass() {
		return freightclass;
	}
	public void setFreightClass(String freightClass) {
		this.freightclass = freightClass;
	}
	public String getShippingTabClass() {
		return wmsclass;
	}
	public void setShippingTabClass(String shippingTabClass) {
		this.wmsclass = shippingTabClass;
	}
	public String getLottable01() {
		return lottable01label;
	}
	public void setLottable01(String lottable01) {
		this.lottable01label = lottable01;
	}
	public String getLottable02() {
		return lottable02label;
	}
	public void setLottable02(String lottable02) {
		this.lottable02label = lottable02;
	}
	public String getLottable03() {
		return lottable03label;
	}
	public void setLottable03(String lottable03) {
		this.lottable03label = lottable03;
	}
	public String getLottable04() {
		return lottable04label;
	}
	public void setLottable04(String lottable04) {
		this.lottable04label = lottable04;
	}
	public String getLottable05() {
		return lottable05label;
	}
	public void setLottable05(String lottable05) {
		this.lottable05label = lottable05;
	}
	public String getLottable06() {
		return lottable06label;
	}
	public void setLottable06(String lottable06) {
		this.lottable06label = lottable06;
	}
	public String getLottable07() {
		return lottable07label;
	}
	public void setLottable07(String lottable07) {
		this.lottable07label = lottable07;
	}
	public String getLottable08() {
		return lottable08label;
	}
	public void setLottable08(String lottable08) {
		this.lottable08label = lottable08;
	}
	public String getLottable09() {
		return lottable09label;
	}
	public void setLottable09(String lottable09) {
		this.lottable09label = lottable09;
	}
	public String getLottable10() {
		return lottable10label;
	}
	public void setLottable10(String lottable10) {
		this.lottable10label = lottable10;
	}
	public String getBarcode01() {
		return busr1;
	}
	public void setBarcode01(String barcode01) {
		this.busr1 = barcode01;
	}
	public String getBarcode02() {
		return busr2;
	}
	public void setBarcode02(String barcode02) {
		this.busr2 = barcode02;
	}
	public String getBarcode03() {
		return busr3;
	}
	public void setBarcode03(String barcode03) {
		this.busr3 = barcode03;
	}
	public String getBarcode04() {
		return busr4;
	}
	public void setBarcode04(String barcode04) {
		this.busr4 = barcode04;
	}
	public String getBarcode05() {
		return busr5;
	}
	public void setBarcode05(String barcode05) {
		this.busr5 = barcode05;
	}
	public String getBarcode06() {
		return busr6;
	}
	public void setBarcode06(String barcode06) {
		this.busr6 = barcode06;
	}
	public String getBarcode07() {
		return busr7;
	}
	public void setBarcode07(String barcode07) {
		this.busr7 = barcode07;
	}
	public String getBarcode08() {
		return busr8;
	}
	public void setBarcode08(String barcode08) {
		this.busr8 = barcode08;
	}
	public String getBarcode09() {
		return busr9;
	}
	public void setBarcode09(String barcode09) {
		this.busr9 = barcode09;
	}
	public String getBarcode10() {
		return busr10;
	}
	public void setBarcode10(String barcode10) {
		this.busr10 = barcode10;
	}
	public String getInboundCatchWeight() {
		return icwflag;
	}
	public void setInboundCatchWeight(String inboundCatchWeight) {
		this.icwflag = inboundCatchWeight;
	}
	public String getInboundCatchData() {
		return icdflag;
	}
	public void setInboundCatchData(String inboundCatchData) {
		this.icdflag = inboundCatchData;
	}
	public String getInboundNoEntryOfTotalWeight() {
		return ideweight;
	}
	public void setInboundNoEntryOfTotalWeight(String inboundNoEntryOfTotalWeight) {
		this.ideweight = inboundNoEntryOfTotalWeight;
	}
	public String getInboundAverageWeight() {
		return avgcaseweight;
	}
	public void setInboundAverageWeight(String inboundAverageWeight) {
		this.avgcaseweight = inboundAverageWeight;
	}
	public String getInboundWeightTabTareWeight() {
		return tareweight;
	}
	public void setInboundWeightTabTareWeight(String inboundWeightTabTareWeight) {
		this.tareweight = inboundWeightTabTareWeight;
	}
	public String getInboundTolerance() {
		return tolerancepct;
	}
	public void setInboundTolerance(String inboundTolerance) {
		this.tolerancepct = inboundTolerance;
	}
	public String getInboundCatchWeightBy() {
		return icwby;
	}
	public void setInboundCatchWeightBy(String inboundCatchWeightBy) {
		this.icwby = inboundCatchWeightBy;
	}
	public String getInboundSerialNumber() {
		return icdlabel1;
	}
	public void setInboundSerialNumber(String inboundSerialNumber) {
		this.icdlabel1 = inboundSerialNumber;
	}
	public String getInboundOther2() {
		return icdlabel2;
	}
	public void setInboundOther2(String inboundOther2) {
		this.icdlabel2 = inboundOther2;
	}
	public String getInboundOther3() {
		return icdlabel3;
	}
	public void setInboundOther3(String inboundOther3) {
		this.icdlabel3 = inboundOther3;
	}
	public String getOutboundCatchWeight() {
		return ocwflag;
	}
	public void setOutboundCatchWeight(String outboundCatchWeight) {
		this.ocwflag = outboundCatchWeight;
	}
	public String getOutboundCatchData() {
		return ocdflag;
	}
	public void setOutboundCatchData(String outboundCatchData) {
		this.ocdflag = outboundCatchData;
	}
	public String getOutboundNoEntryOfTotalWeight() {
		return odeweight;
	}
	public void setOutboundNoEntryOfTotalWeight(String outboundNoEntryOfTotalWeight) {
		this.odeweight = outboundNoEntryOfTotalWeight;
	}
	public String getAllowCustomerOverride() {
		return oacoverride;
	}
	public void setAllowCustomerOverride(String allowCustomerOverride) {
		this.oacoverride = allowCustomerOverride;
	}
	public String getOutboundAverageWeight() {
		return oavgcaseweight;
	}
	public void setOutboundAverageWeight(String outboundAverageWeight) {
		this.oavgcaseweight = outboundAverageWeight;
	}
	public String getOutboundWeightTabTareWeight() {
		return otareweight;
	}
	public void setOutboundWeightTabTareWeight(String outboundWeightTabTareWeight) {
		this.otareweight = outboundWeightTabTareWeight;
	}
	public String getOutboundTolerance() {
		return otolerancepct;
	}
	public void setOutboundTolerance(String outboundTolerance) {
		this.otolerancepct = outboundTolerance;
	}
	public String getOutboundCatchWeightBy() {
		return ocwby;
	}
	public void setOutboundCatchWeightBy(String outboundCatchWeightBy) {
		this.ocwby = outboundCatchWeightBy;
	}
	public String getOutboundSerialNumber() {
		return ocdlabel1;
	}
	public void setOutboundSerialNumber(String outboundSerialNumber) {
		this.ocdlabel1 = outboundSerialNumber;
	}
	public String getOutboundOther2() {
		return ocdlabel2;
	}
	public void setOutboundOther2(String outboundOther2) {
		this.ocdlabel2 = outboundOther2;
	}
	public String getOutboundOther3() {
		return ocdlabel3;
	}
	public void setOutboundOther3(String outboundOther3) {
		this.ocdlabel3 = outboundOther3;
	}
	public String getCatchWhen() {
		return ocdcatchwhen;
	}
	public void setCatchWhen(String catchWhen) {
		this.ocdcatchwhen = catchWhen;
	}
	public String getCatchQuantity1() {
		return ocdcatchqty1;
	}
	public void setCatchQuantity1(String catchQuantity1) {
		this.ocdcatchqty1 = catchQuantity1;
	}
	public String getCatchQuantity2() {
		return ocdcatchqty2;
	}
	public void setCatchQuantity2(String catchQuantity2) {
		this.ocdcatchqty2 = catchQuantity2;
	}
	public String getCatchQuantity3() {
		return ocdcatchqty3;
	}
	public void setCatchQuantity3(String catchQuantity3) {
		this.ocdcatchqty3 = catchQuantity3;
	}
	public String getHoldCodeOnRFReceipt() {
		return receiptholdcode;
	}
	public void setHoldCodeOnRFReceipt(String holdCodeOnRFReceipt) {
		this.receiptholdcode = holdCodeOnRFReceipt;
	}
	public String getItemType() {
		return skutype;
	}
	public void setItemType(String itemType) {
		this.skutype = itemType;
	}
	public String getReceiptValidation() {
		return receiptvalidationtemplate;
	}
	public void setReceiptValidation(String receiptValidation) {
		this.receiptvalidationtemplate = receiptValidation;
	}
	public String getStackLimit() {
		return stacklimit;
	}
	public void setStackLimit(String stackLimit) {
		this.stacklimit = stackLimit;
	}
	public String getMaxPalletsPerZone() {
		return maxpalletsperzone;
	}
	public void setMaxPalletsPerZone(String maxPalletsPerZone) {
		this.maxpalletsperzone = maxPalletsPerZone;
	}
	public String getManualSetupRequired() {
		return manualsetuprequired;
	}
	public void setManualSetupRequired(String manualSetupRequired) {
		this.manualsetuprequired = manualSetupRequired;
	}
	public String getPutawayZone() {
		return putawayzone;
	}
	public void setPutawayZone(String putawayZone) {
		this.putawayzone = putawayZone;
	}
	public String getPutawayLocation() {
		return putawayloc;
	}
	public void setPutawayLocation(String putawayLocation) {
		this.putawayloc = putawayLocation;
	}
	public String getInboundQCLocation() {
		return qcloc;
	}
	public void setInboundQCLocation(String inboundQCLocation) {
		this.qcloc = inboundQCLocation;
	}
	public String getOutboundQCLocation() {
		return qclocout;
	}
	public void setOutboundQCLocation(String outboundQCLocation) {
		this.qclocout = outboundQCLocation;
	}
	public String getReturnLocation() {
		return returnsloc;
	}
	public void setReturnLocation(String returnLocation) {
		this.returnsloc = returnLocation;
	}
	public String getPutawayStrategy() {
		return putawaystrategykey;
	}
	public void setPutawayStrategy(String putawayStrategy) {
		this.putawaystrategykey = putawayStrategy;
	}
	public String getStrategy() {
		return strategykey;
	}
	public void setStrategy(String strategy) {
		this.strategykey = strategy;
	}
	public String getRotation() {
		return defaultrotation;
	}
	public void setRotation(String rotation) {
		this.defaultrotation = rotation;
	}
	public String getRotateBy() {
		return rotateby;
	}
	public void setRotateBy(String rotateBy) {
		this.rotateby = rotateBy;
	}
	public String getDateCodeDays() {
		return datecodedays;
	}
	public void setDateCodeDays(String dateCodeDays) {
		this.datecodedays = dateCodeDays;
	}
	public String getSerialNumberStart() {
		return serialnumberstart;
	}
	public void setSerialNumberStart(String serialNumberStart) {
		this.serialnumberstart = serialNumberStart;
	}
	public String getSerialNumberNext() {
		return serialnumbernext;
	}
	public void setSerialNumberNext(String serialNumberNext) {
		this.serialnumbernext = serialNumberNext;
	}
	public String getSerialNumberEnd() {
		return serialnumberend;
	}
	public void setSerialNumberEnd(String serialNumberEnd) {
		this.serialnumberend = serialNumberEnd;
	}
	public String getOppertunistic() {
		return flowthruitem;
	}
	public void setOppertunistic(String oppertunistic) {
		this.flowthruitem = oppertunistic;
	}
	public String getConveyable() {
		return conveyable;
	}
	public void setConveyable(String conveyable) {
		this.conveyable = conveyable;
	}
	public String getVerifyLottable4and5() {
		return verifylot04lot05;
	}
	public void setVerifyLottable4and5(String verifyLottable4and5) {
		this.verifylot04lot05 = verifyLottable4and5;
	}
	public String getMinimumWaveQuantity() {
		return minimumwaveqty;
	}
	public void setMinimumWaveQuantity(String minimumWaveQuantity) {
		this.minimumwaveqty = minimumWaveQuantity;
	}
	public String getBulkCartonGroup() {
		return bulkcartongroup;
	}
	public void setBulkCartonGroup(String bulkCartonGroup) {
		this.bulkcartongroup = bulkCartonGroup;
	}
	public String getAllowConsolidation() {
		return allowconsolidation;
	}
	public void setAllowConsolidation(String allowConsolidation) {
		this.allowconsolidation = allowConsolidation;
	}
	public String getCycleClass() {
		return abc;
	}
	public void setCycleClass(String cycleClass) {
		this.abc = cycleClass;
	}
	public String getLastCycleCount() {
		return lastcyclecount;
	}
	public void setLastCycleCount(String lastCycleCount) {
		this.lastcyclecount = lastCycleCount;
	}
	public String getCcDiscrepancyHandlingRule() {
		return ccdiscrepancyrule;
	}
	public void setCcDiscrepancyHandlingRule(String ccDiscrepancyHandlingRule) {
		this.ccdiscrepancyrule = ccDiscrepancyHandlingRule;
	}
	public String getQuantityToReorder() {
		return reorderqty;
	}
	public void setQuantityToReorder(String quantityToReorder) {
		this.reorderqty = quantityToReorder;
	}
	public String getCostToOrder() {
		return stdordercost;
	}
	public void setCostToOrder(String costToOrder) {
		this.stdordercost = costToOrder;
	}
	public String getPuchasePricePerUnit() {
		return cost;
	}
	public void setPuchasePricePerUnit(String puchasePricePerUnit) {
		this.cost = puchasePricePerUnit;
	}
	public String getReorderPoint() {
		return reorderpoint;
	}
	public void setReorderPoint(String reorderPoint) {
		this.reorderpoint = reorderPoint;
	}
	public String getRetailPricePerUnit() {
		return price;
	}
	public void setRetailPricePerUnit(String retailPricePerUnit) {
		this.price = retailPricePerUnit;
	}
	public String getCarryingPerUnit() {
		return carrycost;
	}
	public void setCarryingPerUnit(String carryingPerUnit) {
		this.carrycost = carryingPerUnit;
	}
	public String getUdf1() {
		return susr1;
	}
	public void setUdf1(String udf1) {
		this.susr1 = udf1;
	}
	public String getUdf2() {
		return susr2;
	}
	public void setUdf2(String udf2) {
		this.susr2 = udf2;
	}
	public String getUdf3() {
		return susr3;
	}
	public void setUdf3(String udf3) {
		this.susr3 = udf3;
	}
	public String getUdf4() {
		return susr4;
	}
	public void setUdf4(String udf4) {
		this.susr4 = udf4;
	}
	public String getUdf5() {
		return susr5;
	}
	public void setUdf5(String udf5) {
		this.susr5 = udf5;
	}
	public String getUdf6() {
		return susr6;
	}
	public void setUdf6(String udf6) {
		this.susr6 = udf6;
	}
	public String getUdf7() {
		return susr7;
	}
	public void setUdf7(String udf7) {
		this.susr7 = udf7;
	}
	public String getUdf8() {
		return susr8;
	}
	public void setUdf8(String udf8) {
		this.susr8 = udf8;
	}
	public String getUdf9() {
		return susr9;
	}
	public void setUdf9(String udf9) {
		this.susr9 = udf9;
	}
	public String getUdf10() {
		return susr10;
	}
	public void setUdf10(String udf10) {
		this.susr10 = udf10;
	}
	public String getPickingInstruction() {
		return notes2;
	}
	public void setPickingInstruction(String pickingInstruction) {
		this.notes2 = pickingInstruction;
	}
	public String getNotes() {
		return notes1;
	}
	public void setNotes(String notes) {
		this.notes1 = notes;
	}
	
	public ArrayList getAssignLocationsVOCollection() {
		return assignLocationsVOCollection;
	}
	public void setAssignLocationsVOCollection(ArrayList assignLocationsVOCollection) {
		this.assignLocationsVOCollection = assignLocationsVOCollection;
	}
	public ArrayList getAltVOCollection() {
		return altVOCollection;
	}
	public void setAltVOCollection(ArrayList altVOCollection) {
		this.altVOCollection = altVOCollection;
	}
	public ArrayList getSubstituteVOCollection() {
		return substituteVOCollection;
	}
	public void setSubstituteVOCollection(ArrayList substituteVOCollection) {
		this.substituteVOCollection = substituteVOCollection;
	}
	

	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getWhseid() {
		return whseid;
	}
	public void setWhseid(String whseid) {
		this.whseid = whseid;
	}
	public String getManufacturersku() {
		return manufacturersku;
	}
	public void setManufacturersku(String manufacturersku) {
		this.manufacturersku = manufacturersku;
	}
	public String getRetailsku() {
		return retailsku;
	}
	public void setRetailsku(String retailsku) {
		this.retailsku = retailsku;
	}
	public String getAltsku() {
		return altsku;
	}
	public void setAltsku(String altsku) {
		this.altsku = altsku;
	}
	public String getCyclecountfrequency() {
		return cyclecountfrequency;
	}
	public void setCyclecountfrequency(String cyclecountfrequency) {
		this.cyclecountfrequency = cyclecountfrequency;
	}
	public String getIoflag() {
		return ioflag;
	}
	public void setIoflag(String ioflag) {
		this.ioflag = ioflag;
	}
	public String getLotxiddetailotherlabel1() {
		return lotxiddetailotherlabel1;
	}
	public void setLotxiddetailotherlabel1(String lotxiddetailotherlabel1) {
		this.lotxiddetailotherlabel1 = lotxiddetailotherlabel1;
	}
	public String getLotxiddetailotherlabel2() {
		return lotxiddetailotherlabel2;
	}
	public void setLotxiddetailotherlabel2(String lotxiddetailotherlabel2) {
		this.lotxiddetailotherlabel2 = lotxiddetailotherlabel2;
	}
	public String getLotxiddetailotherlabel3() {
		return lotxiddetailotherlabel3;
	}
	public void setLotxiddetailotherlabel3(String lotxiddetailotherlabel3) {
		this.lotxiddetailotherlabel3 = lotxiddetailotherlabel3;
	}
	public String getEachkey() {
		return eachkey;
	}
	public void setEachkey(String eachkey) {
		this.eachkey = eachkey;
	}
	public String getCasekey() {
		return casekey;
	}
	public void setCasekey(String casekey) {
		this.casekey = casekey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEffecstartdate() {
		return effecstartdate;
	}
	public void setEffecstartdate(String effecstartdate) {
		this.effecstartdate = effecstartdate;
	}
	public String getEffecenddate() {
		return effecenddate;
	}
	public void setEffecenddate(String effecenddate) {
		this.effecenddate = effecenddate;
	}
	public String getHasimage() {
		return hasimage;
	}
	public void setHasimage(String hasimage) {
		this.hasimage = hasimage;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getGrosswgt() {
		return grosswgt;
	}
	public void setGrosswgt(String grosswgt) {
		this.grosswgt = grosswgt;
	}
	public String getInnerpack() {
		return innerpack;
	}
	public void setInnerpack(String innerpack) {
		this.innerpack = innerpack;
	}
	public String getMinimumshelflifeonrfpicking() {
		return minimumshelflifeonrfpicking;
	}
	public void setMinimumshelflifeonrfpicking(String minimumshelflifeonrfpicking) {
		this.minimumshelflifeonrfpicking = minimumshelflifeonrfpicking;
	}
	public String getPickcode() {
		return pickcode;
	}
	public void setPickcode(String pickcode) {
		this.pickcode = pickcode;
	}
	public String getPutcode() {
		return putcode;
	}
	public void setPutcode(String putcode) {
		this.putcode = putcode;
	}
	public String getPickuom() {
		return pickuom;
	}
	public void setPickuom(String pickuom) {
		this.pickuom = pickuom;
	}
	public String getReceiptinspectionloc() {
		return receiptinspectionloc;
	}
	public void setReceiptinspectionloc(String receiptinspectionloc) {
		this.receiptinspectionloc = receiptinspectionloc;
	}
	public String getZonedescr() {
		return zonedescr;
	}
	public void setZonedescr(String zonedescr) {
		this.zonedescr = zonedescr;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getStorerkey() {
		return storerkey;
	}
	public void setStorerkey(String storerkey) {
		this.storerkey = storerkey;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getPackkey() {
		return packkey;
	}
	public void setPackkey(String packkey) {
		this.packkey = packkey;
	}
	public String getCartongroup() {
		return cartongroup;
	}
	public void setCartongroup(String cartongroup) {
		this.cartongroup = cartongroup;
	}
	public String getTariffkey() {
		return tariffkey;
	}
	public void setTariffkey(String tariffkey) {
		this.tariffkey = tariffkey;
	}
	public String getStdcube() {
		return stdcube;
	}
	public void setStdcube(String stdcube) {
		this.stdcube = stdcube;
	}
	public String getItemreference() {
		return itemreference;
	}
	public void setItemreference(String itemreference) {
		this.itemreference = itemreference;
	}
	public String getStdgrosswgt() {
		return stdgrosswgt;
	}
	public void setStdgrosswgt(String stdgrossweight) {
		this.stdgrosswgt = stdgrossweight;
	}
	public String getStdnetwgt() {
		return stdnetwgt;
	}
	public void setStdnetwgt(String stdnetwgt) {
		this.stdnetwgt = stdnetwgt;
	}
	public String getTare() {
		return tare;
	}
	public void setTare(String tare) {
		this.tare = tare;
	}
	public String getCwflag() {
		return cwflag;
	}
	public void setCwflag(String cwflag) {
		this.cwflag = cwflag;
	}
	public String getShelflifeindicator() {
		return shelflifeindicator;
	}
	public void setShelflifeindicator(String shelflifeindicator) {
		this.shelflifeindicator = shelflifeindicator;
	}
	public String getShelflifecodetype() {
		return shelflifecodetype;
	}
	public void setShelflifecodetype(String shelflifecodetype) {
		this.shelflifecodetype = shelflifecodetype;
	}
	public String getShelflife() {
		return shelflife;
	}
	public void setShelflife(String shelflife) {
		this.shelflife = shelflife;
	}
	public String getOutboundshelflife() {
		return outboundshelflife;
	}
	public void setOutboundshelflife(String outboundshelflife) {
		this.outboundshelflife = outboundshelflife;
	}
	public String getLottablevalidationkey() {
		return lottablevalidationkey;
	}
	public void setLottablevalidationkey(String lottablevalidationkey) {
		this.lottablevalidationkey = lottablevalidationkey;
	}
	public String getRfdefaultpack() {
		return rfdefaultpack;
	}
	public void setRfdefaultpack(String rfdefaultpack) {
		this.rfdefaultpack = rfdefaultpack;
	}
	public String getOnreceiptcopypackkey() {
		return onreceiptcopypackkey;
	}
	public void setOnreceiptcopypackkey(String onreceiptcopypackkey) {
		this.onreceiptcopypackkey = onreceiptcopypackkey;
	}
	public String getRfdefaultuom() {
		return rfdefaultuom;
	}
	public void setRfdefaultuom(String rfdefaultuom) {
		this.rfdefaultuom = rfdefaultuom;
	}
	public String getSkugroup() {
		return skugroup;
	}
	public void setSkugroup(String skugroup1) {
		this.skugroup = skugroup1;
	}
	public String getSkugroup2() {
		return skugroup2;
	}
	public void setSkugroup2(String skugroup2) {
		this.skugroup2 = skugroup2;
	}
	public String getShippablecontainer() {
		return shippablecontainer;
	}
	public void setShippablecontainer(String shippablecontainer) {
		this.shippablecontainer = shippablecontainer;
	}
	public String getVert_storage() {
		return vert_storage;
	}
	public void setVert_storage(String vert_storage) {
		this.vert_storage = vert_storage;
	}
	public String getTransportationmode() {
		return transportationmode;
	}
	public void setTransportationmode(String transportationmode) {
		this.transportationmode = transportationmode;
	}
	public String getHazmatcodeskey() {
		return hazmatcodeskey;
	}
	public void setHazmatcodeskey(String hazmatcodeskey) {
		this.hazmatcodeskey = hazmatcodeskey;
	}
	public String getFreightclass() {
		return freightclass;
	}
	public void setFreightclass(String freightclass) {
		this.freightclass = freightclass;
	}
	public String getWmsclass() {
		return wmsclass;
	}
	public void setWmsclass(String wmsclass) {
		this.wmsclass = wmsclass;
	}
	public String getShelflifeonreceiving() {
		return shelflifeonreceiving;
	}
	public void setShelflifeonreceiving(String shelflifeonreceiving) {
		this.shelflifeonreceiving = shelflifeonreceiving;
	}
	public String getLottable01label() {
		return lottable01label;
	}
	public void setLottable01label(String lottable01label) {
		this.lottable01label = lottable01label;
	}
	public String getLottable02label() {
		return lottable02label;
	}
	public void setLottable02label(String lottable02label) {
		this.lottable02label = lottable02label;
	}
	public String getLottable03label() {
		return lottable03label;
	}
	public void setLottable03label(String lottable03label) {
		this.lottable03label = lottable03label;
	}
	public String getLottable04label() {
		return lottable04label;
	}
	public void setLottable04label(String lottable04label) {
		this.lottable04label = lottable04label;
	}
	public String getLottable05label() {
		return lottable05label;
	}
	public void setLottable05label(String lottable05label) {
		this.lottable05label = lottable05label;
	}
	public String getLottable06label() {
		return lottable06label;
	}
	public void setLottable06label(String lottable06label) {
		this.lottable06label = lottable06label;
	}
	public String getLottable07label() {
		return lottable07label;
	}
	public void setLottable07label(String lottable07label) {
		this.lottable07label = lottable07label;
	}
	public String getLottable08label() {
		return lottable08label;
	}
	public void setLottable08label(String lottable08label) {
		this.lottable08label = lottable08label;
	}
	public String getLottable09label() {
		return lottable09label;
	}
	public void setLottable09label(String lottable09label) {
		this.lottable09label = lottable09label;
	}
	public String getLottable10label() {
		return lottable10label;
	}
	public void setLottable10label(String lottable10label) {
		this.lottable10label = lottable10label;
	}
	public String getBusr1() {
		return busr1;
	}
	public void setBusr1(String busr1) {
		this.busr1 = busr1;
	}
	public String getBusr2() {
		return busr2;
	}
	public void setBusr2(String busr2) {
		this.busr2 = busr2;
	}
	public String getBusr3() {
		return busr3;
	}
	public void setBusr3(String busr3) {
		this.busr3 = busr3;
	}
	public String getBusr4() {
		return busr4;
	}
	public void setBusr4(String busr4) {
		this.busr4 = busr4;
	}
	public String getBusr5() {
		return busr5;
	}
	public void setBusr5(String busr5) {
		this.busr5 = busr5;
	}
	public String getBusr6() {
		return busr6;
	}
	public void setBusr6(String busr6) {
		this.busr6 = busr6;
	}
	public String getBusr7() {
		return busr7;
	}
	public void setBusr7(String busr7) {
		this.busr7 = busr7;
	}
	public String getBusr8() {
		return busr8;
	}
	public void setBusr8(String busr8) {
		this.busr8 = busr8;
	}
	public String getBusr9() {
		return busr9;
	}
	public void setBusr9(String busr9) {
		this.busr9 = busr9;
	}
	public String getBusr10() {
		return busr10;
	}
	public void setBusr10(String busr10) {
		this.busr10 = busr10;
	}
	public String getIcwflag() {
		return icwflag;
	}
	public void setIcwflag(String icwflag) {
		this.icwflag = icwflag;
	}	
	public String getIdeweight() {
		return ideweight;
	}
	public void setIdeweight(String ideweight) {
		this.ideweight = ideweight;
	}
	public String getAvgcaseweight() {
		return avgcaseweight;
	}
	public void setAvgcaseweight(String avgcaseweight) {
		this.avgcaseweight = avgcaseweight;
	}
	public String getTareweight() {
		return tareweight;
	}
	public void setTareweight(String tareweight) {
		this.tareweight = tareweight;
	}
	public String getTolerancepct() {
		return tolerancepct;
	}
	public void setTolerancepct(String tolerancepct) {
		this.tolerancepct = tolerancepct;
	}
	public String getIcwby() {
		return icwby;
	}
	public void setIcwby(String icwby) {
		this.icwby = icwby;
	}
	public String getIcdlabel1() {
		return icdlabel1;
	}
	public void setIcdlabel1(String icdlabel1) {
		this.icdlabel1 = icdlabel1;
	}
	public String getIcdlabel2() {
		return icdlabel2;
	}
	public void setIcdlabel2(String icdlabel2) {
		this.icdlabel2 = icdlabel2;
	}
	public String getIcdlabel3() {
		return icdlabel3;
	}
	public void setIcdlabel3(String icdlabel3) {
		this.icdlabel3 = icdlabel3;
	}
	public String getOcwflag() {
		return ocwflag;
	}
	public void setOcwflag(String ocwflag) {
		this.ocwflag = ocwflag;
	}
	public String getOcdflag() {
		return ocdflag;
	}
	public void setOcdflag(String ocdflag) {
		this.ocdflag = ocdflag;
	}
	public String getOdeweight() {
		return odeweight;
	}
	public void setOdeweight(String odeweight) {
		this.odeweight = odeweight;
	}
	public String getOacoverride() {
		return oacoverride;
	}
	public void setOacoverride(String oacoverride) {
		this.oacoverride = oacoverride;
	}
	public String getOavgcaseweight() {
		return oavgcaseweight;
	}
	public void setOavgcaseweight(String oavgcaseweight) {
		this.oavgcaseweight = oavgcaseweight;
	}
	public String getOtareweight() {
		return otareweight;
	}
	public void setOtareweight(String otareweight) {
		this.otareweight = otareweight;
	}
	public String getOtolerancepct() {
		return otolerancepct;
	}
	public void setOtolerancepct(String otolerancepct) {
		this.otolerancepct = otolerancepct;
	}
	public String getOcwby() {
		return ocwby;
	}
	public void setOcwby(String ocwby) {
		this.ocwby = ocwby;
	}
	public String getOcdlabel1() {
		return ocdlabel1;
	}
	public void setOcdlabel1(String ocdlabel1) {
		this.ocdlabel1 = ocdlabel1;
	}
	public String getOcdlabel2() {
		return ocdlabel2;
	}
	public void setOcdlabel2(String ocdlabel2) {
		this.ocdlabel2 = ocdlabel2;
	}
	public String getOcdlabel3() {
		return ocdlabel3;
	}
	public void setOcdlabel3(String ocdlabel3) {
		this.ocdlabel3 = ocdlabel3;
	}
	public String getOcdcatchwhen() {
		return ocdcatchwhen;
	}
	public void setOcdcatchwhen(String ocdcatchwhen) {
		this.ocdcatchwhen = ocdcatchwhen;
	}
	public String getOcdcatchqty1() {
		return ocdcatchqty1;
	}
	public void setOcdcatchqty1(String ocdcatchqty1) {
		this.ocdcatchqty1 = ocdcatchqty1;
	}
	public String getOcdcatchqty2() {
		return ocdcatchqty2;
	}
	public void setOcdcatchqty2(String ocdcatchqty2) {
		this.ocdcatchqty2 = ocdcatchqty2;
	}
	public String getOcdcatchqty3() {
		return ocdcatchqty3;
	}
	public void setOcdcatchqty3(String ocdcatchqty3) {
		this.ocdcatchqty3 = ocdcatchqty3;
	}
	public String getReceiptholdcode() {
		return receiptholdcode;
	}
	public void setReceiptholdcode(String receiptholdcode) {
		this.receiptholdcode = receiptholdcode;
	}
	public String getSkutype() {
		return skutype;
	}
	public void setSkutype(String skutype) {
		this.skutype = skutype;
	}
	public String getReceiptvalidationtemplate() {
		return receiptvalidationtemplate;
	}
	public void setReceiptvalidationtemplate(String receiptvalidationtemplate) {
		this.receiptvalidationtemplate = receiptvalidationtemplate;
	}
	public String getStacklimit() {
		return stacklimit;
	}
	public void setStacklimit(String stacklimit) {
		this.stacklimit = stacklimit;
	}
	public String getMaxpalletsperzone() {
		return maxpalletsperzone;
	}
	public void setMaxpalletsperzone(String maxpalletsperzone) {
		this.maxpalletsperzone = maxpalletsperzone;
	}
	public String getManualsetuprequired() {
		return manualsetuprequired;
	}
	public void setManualsetuprequired(String manualsetuprequired) {
		this.manualsetuprequired = manualsetuprequired;
	}
	public String getPutawayzone() {
		return putawayzone;
	}
	public void setPutawayzone(String putawayzone) {
		this.putawayzone = putawayzone;
	}
	public String getPutawayloc() {
		return putawayloc;
	}
	public void setPutawayloc(String putawayloc) {
		this.putawayloc = putawayloc;
	}
	public String getQclocout() {
		return qclocout;
	}
	public void setQclocout(String qclocout) {
		this.qclocout = qclocout;
	}
	public String getQcloc() {
		return qcloc;
	}
	public void setQcloc(String qcloc) {
		this.qcloc = qcloc;
	}
	public String getReturnsloc() {
		return returnsloc;
	}
	public void setReturnsloc(String returnsloc) {
		this.returnsloc = returnsloc;
	}
	public String getStrategykey() {
		return strategykey;
	}
	public void setStrategykey(String strategykey) {
		this.strategykey = strategykey;
	}
	public String getDefaultrotation() {
		return defaultrotation;
	}
	public void setDefaultrotation(String defaultrotation) {
		this.defaultrotation = defaultrotation;
	}
	public String getRotateby() {
		return rotateby;
	}
	public void setRotateby(String rotateby) {
		this.rotateby = rotateby;
	}
	public String getDatecodedays() {
		return datecodedays;
	}
	public void setDatecodedays(String datecodedays) {
		this.datecodedays = datecodedays;
	}
	public String getSerialnumberstart() {
		return serialnumberstart;
	}
	public void setSerialnumberstart(String serialnumberstart) {
		this.serialnumberstart = serialnumberstart;
	}
	public String getSerialnumbernext() {
		return serialnumbernext;
	}
	public void setSerialnumbernext(String serialnumbernext) {
		this.serialnumbernext = serialnumbernext;
	}
	public String getSerialnumberend() {
		return serialnumberend;
	}
	public void setSerialnumberend(String serialnumberend) {
		this.serialnumberend = serialnumberend;
	}
	public String getFlowthruitem() {
		return flowthruitem;
	}
	public void setFlowthruitem(String flowthruitem) {
		this.flowthruitem = flowthruitem;
	}
	public String getVerifylot04lot05() {
		return verifylot04lot05;
	}
	public void setVerifylot04lot05(String verifylot04lot05) {
		this.verifylot04lot05 = verifylot04lot05;
	}
	public String getMinimumwaveqty() {
		return minimumwaveqty;
	}
	public void setMinimumwaveqty(String minimumwaveqty) {
		this.minimumwaveqty = minimumwaveqty;
	}
	public String getBulkcartongroup() {
		return bulkcartongroup;
	}
	public void setBulkcartongroup(String bulkcartongroup) {
		this.bulkcartongroup = bulkcartongroup;
	}
	public String getAllowconsolidation() {
		return allowconsolidation;
	}
	public void setAllowconsolidation(String allowconsolidation) {
		this.allowconsolidation = allowconsolidation;
	}
	public String getPutawaystrategykey() {
		return putawaystrategykey;
	}
	public void setPutawaystrategykey(String putawaystrategykey) {
		this.putawaystrategykey = putawaystrategykey;
	}
	public String getAbc() {
		return abc;
	}
	public void setAbc(String abc) {
		this.abc = abc;
	}
	public String getLastcyclecount() {
		return lastcyclecount;
	}
	public void setLastcyclecount(String lastcyclecount) {
		this.lastcyclecount = lastcyclecount;
	}
	public String getCcdiscrepancyrule() {
		return ccdiscrepancyrule;
	}
	public void setCcdiscrepancyrule(String ccdiscrepancyrule) {
		this.ccdiscrepancyrule = ccdiscrepancyrule;
	}
	public String getReorderqty() {
		return reorderqty;
	}
	public void setReorderqty(String reorderqty) {
		this.reorderqty = reorderqty;
	}
	public String getStdordercost() {
		return stdordercost;
	}
	public void setStdordercost(String stdordercost) {
		this.stdordercost = stdordercost;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getReorderpoint() {
		return reorderpoint;
	}
	public void setReorderpoint(String reorderpoint) {
		this.reorderpoint = reorderpoint;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCarrycost() {
		return carrycost;
	}
	public void setCarrycost(String carrycost) {
		this.carrycost = carrycost;
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
	public String getSusr6() {
		return susr6;
	}
	public void setSusr6(String susr6) {
		this.susr6 = susr6;
	}
	public String getSusr7() {
		return susr7;
	}
	public void setSusr7(String susr7) {
		this.susr7 = susr7;
	}
	public String getSusr8() {
		return susr8;
	}
	public void setSusr8(String susr8) {
		this.susr8 = susr8;
	}
	public String getSusr9() {
		return susr9;
	}
	public void setSusr9(String susr9) {
		this.susr9 = susr9;
	}
	public String getSusr10() {
		return susr10;
	}
	public void setSusr10(String susr10) {
		this.susr10 = susr10;
	}
	public String getNotes2() {
		return notes2;
	}
	public void setNotes2(String notes2) {
		this.notes2 = notes2;
	}
	public String getNotes1() {
		return notes1;
	}
	public void setNotes1(String notes1) {
		this.notes1 = notes1;
	}

	
	
	
	
	public class AssignLocationsVO extends BaseScreenVO{
		
		
		private String allowreplenishfrombulk  = null;
		private String allowreplenishfromcasepick  = null;
		private String allowreplenishfrompiecepick  = null;
		private String loc  = null;
		private String locationtype  = null;
		private String locationuom  = null;
		private String optadddelflag  = null;
		private String optbatchid  = null;
		private String optsequence  = null;
		private String qty  = null;
		private String qtyallocated  = null;
		private String qtyexpected  = null;
		private String qtylocationlimit  = null;
		private String qtylocationminimum  = null;
		private String qtypicked  = null;
		private String qtypickinprocess  = null;
		private String qtyreplenishmentoverride  = null;
		private String replenishmentcasecnt  = null;
		private String replenishmentpriority  = null;
		private String replenishmentqty  = null;
		private String replenishmentseverity  = null;
		private String replenishmentuom  = null;
		private String sku  = null;
		private String storerkey  = null;
		private String todouser  = null;
		private String whseid  = null;
		
		private String packkey = null;
		
		
		
		
		
		//private String locationType = null;
		//private String location = null;
		
		//private String replenishmentPriority = null; //replenihsmentpriority
		//private String replenishmentOverride = null;//qtyreplenishmentoverride
		//private String minimumCapacity = null;//qtylocationminimum
		//private String maximumCapacity = null; //qtylocationlimit
		//private String minimumReplenishment = null; //replenishmentuom
		//private String uomQtyToBeReplenished = null; //replenishmentseverity
		
		//private String allowReplenishmentFromCase = null;
		//private String allowReplenishmentFromBulk = null;
		//private String allowReplenishmentFromPiece = null;
		
		public String getLocationuom(){
			return locationuom;
		}
		public void setLocationUOM(String locationuom){
			this.locationuom = locationuom;
		}
		
		public String getOptadddelflag(){
			return this.optadddelflag;
		}
		public void setOptadddelflag(String optadddelflag){
			this.optadddelflag = optadddelflag;
		}
		
		public String getLocationType() {
			return locationtype;
		}
		public void setLocationType(String locationType) {
			this.locationtype = locationType;
		}
		
		public String getLocation() {
			return loc;
		}
		public void setLocation(String location) {
			this.loc = location;
		}
		public String getReplenishmentPriority() {
			return replenishmentpriority;
		}
		public void setReplenishmentPriority(String replenishmentPriority) {
			this.replenishmentpriority = replenishmentPriority;
		}
		public String getReplenishmentOverride() {
			return qtyreplenishmentoverride;
		}
		public void setReplenishmentOverride(String replenishmentOverride) {
			this.qtyreplenishmentoverride = replenishmentOverride;
		}
		public String getMinimumCapacity() {
			return qtylocationminimum;
		}
		public void setMinimumCapacity(String minimumCapacity) {
			this.qtylocationminimum = minimumCapacity;
		}
		public String getMaximumCapacity() {
			return qtylocationlimit;
		}
		public void setMaximumCapacity(String maximumCapacity) {
			this.qtylocationlimit = maximumCapacity;
		}
		public String getMinimumReplenishment() {
			return replenishmentuom;
		}
		public void setMinimumReplenishment(String minimumReplenishment) {
			this.replenishmentuom = minimumReplenishment;
		}
		public String getUomQtyToBeReplenished() {
			return replenishmentseverity;
		}
		public void setUomQtyToBeReplenished(String uomQtyToBeReplenished) {
			this.replenishmentseverity = uomQtyToBeReplenished;
		}
		public String getAllowReplenishmentFromCase() {
			return allowreplenishfromcasepick;
		}

		public String getAllowreplenishfromcasepick() {
			return this.allowreplenishfromcasepick;
		}

		public void setAllowReplenishmentFromCase(String allowReplenishmentFromCase) {
			this.allowreplenishfromcasepick = allowReplenishmentFromCase;
		}
		public void setAllowreplenishfromcasepick(String allowReplenishmentFromCase) {
			this.allowreplenishfromcasepick = allowReplenishmentFromCase;
		}
		
		public String getAllowReplenishmentFromBulk() {
			return allowreplenishfrombulk;
		}
		public String getAllowreplenishfrombulk() {
			return allowreplenishfrombulk;
		}

		public void setAllowReplenishmentFromBulk(String allowReplenishmentFromBulk) {
			this.allowreplenishfrombulk = allowReplenishmentFromBulk;
		}
		public void setAllowreplenishfrombulk(String allowReplenishmentFromBulk) {
			this.allowreplenishfrombulk = allowReplenishmentFromBulk;
		}

		
		public String getAllowReplenishmentFromPiece() {
			return allowreplenishfrompiecepick;
		}
		public String getAllowreplenishfrompiecepick() {
			return allowreplenishfrompiecepick;
		}

		
		public void setAllowReplenishmentFromPiece(String allowReplenishmentFromPiece) {
			this.allowreplenishfrompiecepick = allowReplenishmentFromPiece;
		}
		public void setAllowreplenishfrompiecepick(String allowReplenishmentFromPiece) {
			this.allowreplenishfrompiecepick = allowReplenishmentFromPiece;
		}

		
		public String getItem(){
			return sku;
		}
		public String getOwner(){
			return storerkey;
		}
		public void setPackkey(String packkey){
			this.packkey=packkey;
		}
		
		public String getPackkey(){
			return packkey;
		}
		public String getOptbatchid() {
			return optbatchid;
		}
		public void setOptbatchid(String optbatchid) {
			this.optbatchid = optbatchid;
		}
		public String getOptsequence() {
			return optsequence;
		}
		public void setOptsequence(String optsequence) {
			this.optsequence = optsequence;
		}
		public String getQty() {
			return qty;
		}
		public void setQty(String qty) {
			this.qty = qty;
		}
		public String getQtyallocated() {
			return qtyallocated;
		}
		public void setQtyallocated(String qtyallocated) {
			this.qtyallocated = qtyallocated;
		}
		public String getQtyexpected() {
			return qtyexpected;
		}
		public void setQtyexpected(String qtyexpected) {
			this.qtyexpected = qtyexpected;
		}
		public String getQtylocationlimit() {
			return qtylocationlimit;
		}
		public void setQtylocationlimit(String qtylocationlimit) {
			this.qtylocationlimit = qtylocationlimit;
		}
		public String getQtylocationminimum() {
			return qtylocationminimum;
		}
		public void setQtylocationminimum(String qtylocationminimum) {
			this.qtylocationminimum = qtylocationminimum;
		}
		public String getQtypicked() {
			return qtypicked;
		}
		public void setQtypicked(String qtypicked) {
			this.qtypicked = qtypicked;
		}
		public String getQtypickinprocess() {
			return qtypickinprocess;
		}
		public void setQtypickinprocess(String qtypickinprocess) {
			this.qtypickinprocess = qtypickinprocess;
		}
		public String getQtyreplenishmentoverride() {
			return qtyreplenishmentoverride;
		}
		public void setQtyreplenishmentoverride(String qtyreplenishmentoverride) {
			this.qtyreplenishmentoverride = qtyreplenishmentoverride;
		}
		public String getReplenishmentcasecnt() {
			return replenishmentcasecnt;
		}
		public void setReplenishmentcasecnt(String replenishmentcasecnt) {
			this.replenishmentcasecnt = replenishmentcasecnt;
		}
		public String getReplenishmentpriority() {
			return replenishmentpriority;
		}
		public void setReplenishmentpriority(String replenishmentpriority) {
			this.replenishmentpriority = replenishmentpriority;
		}
		public String getReplenishmentqty() {
			return replenishmentqty;
		}
		public void setReplenishmentqty(String replenishmentqty) {
			this.replenishmentqty = replenishmentqty;
		}
		public String getReplenishmentseverity() {
			return replenishmentseverity;
		}
		public void setReplenishmentseverity(String replenishmentseverity) {
			this.replenishmentseverity = replenishmentseverity;
		}
		public String getReplenishmentuom() {
			return replenishmentuom;
		}
		public void setReplenishmentuom(String replenishmentuom) {
			this.replenishmentuom = replenishmentuom;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		public String getStorerkey() {
			return storerkey;
		}
		public void setStorerkey(String storerkey) {
			this.storerkey = storerkey;
		}
		public String getTodouser() {
			return todouser;
		}
		public void setTodouser(String todouser) {
			this.todouser = todouser;
		}
		public String getWhseid() {
			return whseid;
		}
		public void setWhseid(String whseid) {
			this.whseid = whseid;
		}
	}


	
	
	public class AltVO extends BaseScreenVO {
		private String serialKey = null;
		private String storerkey = null;
		private String sku = null;
		private String altsku = null;
		private String packkey = null;
		private String vendor = null;
		private String defaultuom = null;
		private String type = null;
		private String udf1 = null;
		private String udf2 = null;
		private String udf3 = null;
		private String udf4 = null;
		private String udf5 = null;
		private String whseid  = null;
		public String getWhseid() {
			return whseid;
		}
		public void setWhseid(String whseid) {
			this.whseid = whseid;
		}
		public String getOwner() {
			return storerkey;
		}
		public void setOwner(String owner) {
			this.storerkey = owner;
		}
		public String getStorerkey() {
			return storerkey;
		}
		public void setStorerkey(String storerkey) {
			this.storerkey = storerkey;
		}

		
		public String getItem() {
			return sku;
		}
		public void setItem(String item) {
			this.sku = item;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}

		
		
		public String getAlternateItem() {
			return altsku;
		}
		public void setAlternateItem(String alternateItem) {
			this.altsku = alternateItem;
		}
		public String getAltsku() {
			return altsku;
		}
		public void setAltsku(String altsku) {
			this.altsku = altsku;
		}

		
		
		public String getPack() {
			return packkey;
		}
		public void setPack(String pack) {
			this.packkey = pack;
		}
		public String getPackkey() {
			return packkey;
		}
		public void setPackkey(String packkey) {
			this.packkey = packkey;
		}

		
		
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		public String getDefaultReceivingUOM() {
			return defaultuom;
		}
		public void setDefaultReceivingUOM(String defaultReceivingUOM) {
			this.defaultuom = defaultReceivingUOM;
		}
		public String getDefaultuom() {
			return defaultuom;
		}
		public void setDefaultuom(String defaultuom) {
			this.defaultuom = defaultuom;
		}
		
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getSerialKey() {
			return serialKey;
		}
		public void setSerialKey(String serialKey) {
			this.serialKey = serialKey;
		}
		public String getUdf1() {
			return udf1;
		}
		public void setUdf1(String udf1) {
			this.udf1 = udf1;
		}
		public String getUdf2() {
			return udf2;
		}
		public void setUdf2(String udf2) {
			this.udf2 = udf2;
		}
		public String getUdf3() {
			return udf3;
		}
		public void setUdf3(String udf3) {
			this.udf3 = udf3;
		}
		public String getUdf4() {
			return udf4;
		}
		public void setUdf4(String udf4) {
			this.udf4 = udf4;
		}
		public String getUdf5() {
			return udf5;
		}
		public void setUdf5(String udf5) {
			this.udf5 = udf5;
		}
	}
	
	public class SubstituteVO{
		private String owner = null;
		private String item = null;
		private String itemPack = null;
		private String itemUOM = null;
		private String itemUnits = null;
		private String itemMasterUnits = null;
		private String substituteItem = null;
		private String substitutePack = null;
		private String substituteUOM = null;
		private String substituteUnits = null;
		private String substituteMasterUnits = null;
		private String sequence = null;
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getItem() {
			return item;
		}
		public void setItem(String item) {
			this.item = item;
		}
		public String getItemPack() {
			return itemPack;
		}
		public void setItemPack(String itemPack) {
			this.itemPack = itemPack;
		}
		public String getItemUOM() {
			return itemUOM;
		}
		public void setItemUOM(String itemUOM) {
			this.itemUOM = itemUOM;
		}
		public String getItemUnits() {
			return itemUnits;
		}
		public void setItemUnits(String itemUnits) {
			this.itemUnits = itemUnits;
		}
		public String getItemMasterUnits() {
			return itemMasterUnits;
		}
		public void setItemMasterUnits(String itemMasterUnits) {
			this.itemMasterUnits = itemMasterUnits;
		}
		public String getSubstituteItem() {
			return substituteItem;
		}
		public void setSubstituteItem(String substituteItem) {
			this.substituteItem = substituteItem;
		}
		public String getSubstitutePack() {
			return substitutePack;
		}
		public void setSubstitutePack(String substitutePack) {
			this.substitutePack = substitutePack;
		}
		public String getSubstituteUOM() {
			return substituteUOM;
		}
		public void setSubstituteUOM(String substituteUOM) {
			this.substituteUOM = substituteUOM;
		}
		public String getSubstituteUnits() {
			return substituteUnits;
		}
		public void setSubstituteUnits(String substituteUnits) {
			this.substituteUnits = substituteUnits;
		}
		public String getSubstituteMasterUnits() {
			return substituteMasterUnits;
		}
		public void setSubstituteMasterUnits(String substituteMasterUnits) {
			this.substituteMasterUnits = substituteMasterUnits;
		}
		public String getSequence() {
			return sequence;
		}
		public void setSequence(String sequence) {
			this.sequence = sequence;
		}
	}

	public String getIcdflag() {
		return icdflag;
	}
	public void setIcdflag(String icdflag) {
		this.icdflag = icdflag;
	}
	
	public String getNetwgt() {
		return netwgt;
	}
	public void setNetwgt(String netwgt) {
		this.netwgt = netwgt;
	}
	public String getSnum_autoincrement() {
		return snum_autoincrement;
	}
	public void setSnum_autoincrement(String snum_autoincrement) {
		this.snum_autoincrement = snum_autoincrement;
	}
	public String getSnum_delim_count() {
		return snum_delim_count;
	}
	public void setSnum_delim_count(String snum_delim_count) {
		this.snum_delim_count = snum_delim_count;
	}
	public String getSnum_delimiter() {
		return snum_delimiter;
	}
	public void setSnum_delimiter(String snum_delimeter) {
		this.snum_delimiter = snum_delimeter;
	}
	public String getSnum_endtoend() {
		return snum_endtoend;
	}
	public void setSnum_endtoend(String snum_endtoend) {
		this.snum_endtoend = snum_endtoend;
	}
	public String getSnum_incr_length() {
		return snum_incr_length;
	}
	public void setSnum_incr_length(String snum_incr_length) {
		this.snum_incr_length = snum_incr_length;
	}
	public String getSnum_incr_pos() {
		return snum_incr_pos;
	}
	public void setSnum_incr_pos(String snum_incr_pos) {
		this.snum_incr_pos = snum_incr_pos;
	}
	public String getSnum_length() {
		return snum_length;
	}
	public void setSnum_length(String snum_length) {
		this.snum_length = snum_length;
	}
	public String getSnum_mask() {
		return snum_mask;
	}
	public void setSnum_mask(String snum_mask) {
		this.snum_mask = snum_mask;
	}
	public String getSnum_position() {
		return snum_position;
	}
	public void setSnum_position(String snum_position) {
		this.snum_position = snum_position;
	}
	public String getSnum_quantity() {
		return snum_quantity;
	}
	public void setSnum_quantity(String snum_quantity) {
		this.snum_quantity = snum_quantity;
	}
	public String getSnumlong_delimiter() {
		return snumlong_delimiter;
	}
	public void setSnumlong_delimeter(String snumlong_delimeter) {
		this.snumlong_delimiter = snumlong_delimeter;
	}
	public String getSnumlong_fixed() {
		return snumlong_fixed;
	}
	public void setSnumlong_fixed(String snumlong_fixed) {
		this.snumlong_fixed = snumlong_fixed;
	}
	public String getIcd1unique() {
		return icd1unique;
	}
	public void setIcd1unique(String icd1unique) {
		this.icd1unique = icd1unique;
	}
	public String getIcdlabel4() {
		return icdlabel4;
	}
	public void setIcdlabel4(String icdlabel4) {
		this.icdlabel4 = icdlabel4;
	}
	public String getIcdlabel5() {
		return icdlabel5;
	}
	public void setIcdlabel5(String icdlabel5) {
		this.icdlabel5 = icdlabel5;
	}
	public String getLottable11label() {
		return lottable11label;
	}
	public void setLottable11label(String lottable11label) {
		this.lottable11label = lottable11label;
	}
	public String getLottable12label() {
		return lottable12label;
	}
	public void setLottable12label(String lottable12label) {
		this.lottable12label = lottable12label;
	}
	public String getNonstockedindicator() {
		return nonstockedindicator;
	}
	public void setNonstockedindicator(String nonstockedindicator) {
		this.nonstockedindicator = nonstockedindicator;
	}
	public String getOcd1unique() {
		return ocd1unique;
	}
	public void setOcd1unique(String ocd1unique) {
		this.ocd1unique = ocd1unique;
	}
	public String getOcdlabel4() {
		return ocdlabel4;
	}
	public void setOcdlabel4(String ocdlabel4) {
		this.ocdlabel4 = ocdlabel4;
	}
	public String getOcdlabel5() {
		return ocdlabel5;
	}
	public void setOcdlabel5(String ocdlabel5) {
		this.ocdlabel5 = ocdlabel5;
	}
	public String getTobestbydays() {
		return tobestbydays;
	}
	public void setTobestbydays(String tobestbydays) {
		this.tobestbydays = tobestbydays;
	}
	public String getTodeliverbydays() {
		return todeliverbydays;
	}
	public void setTodeliverbydays(String todeliverbydays) {
		this.todeliverbydays = todeliverbydays;
	}
	public String getToexpiredays() {
		return toexpiredays;
	}
	public void setToexpiredays(String toexpirebydays) {
		this.toexpiredays = toexpirebydays;
	}
	public String getVoicegroupingid() {
		return voicegroupingid;
	}
	public void setVoicegroupingid(String voicegroupingid) {
		this.voicegroupingid = voicegroupingid;
	}
	public String getCountsequence() {
		return countsequence;
	}
	public void setCountsequence(String countsequence) {
		this.countsequence = countsequence;
	}
	public String getDapicksort() {
		return dapicksort;
	}
	public void setDapicksort(String dapicksort) {
		this.dapicksort = dapicksort;
	}
	public String getRplnsort() {
		return rplnsort;
	}
	public void setRplnsort(String rplnsort) {
		this.rplnsort = rplnsort;
	}	
	public String getEnableadvcwgt() {
		return enableadvcwgt;
	}
	public void setEnableadvcwgt(String enableadvcwgt) {
		this.enableadvcwgt = enableadvcwgt;
	}
	public String getCatchgrosswgt() {
		return catchgrosswgt;
	}
	public void setCatchgrosswgt(String catchgrosswgt) {
		this.catchgrosswgt = catchgrosswgt;
	}
	public String getCatchnetwgt() {
		return catchnetwgt;
	}
	public void setCatchnetwgt(String catchnetwgt) {
		this.catchnetwgt = catchnetwgt;
	}
	public String getCatchtarewgt() {
		return catchtarewgt;
	}
	public void setCatchtarewgt(String catchtarewgt) {
		this.catchtarewgt = catchtarewgt;
	}
//	public String getAdvcwttrackby() {
//		return advcwttrackby;
//	}
//	public void setAdvcwttrackby(String advcwttrackby) {
//		this.advcwttrackby = advcwttrackby;
//	}
	public String getTarewgt1() {
		return tarewgt1;
	}
	public void setTarewgt1(String tarewgt1) {
		this.tarewgt1 = tarewgt1;
	}
	public String getStdnetwgt1() {
		return stdnetwgt1;
	}
	public void setStdnetwgt1(String stdnetwgt1) {
		this.stdnetwgt1 = stdnetwgt1;
	}
	public String getStdgrosswgt1() {
		return stdgrosswgt1;
	}
	public void setStdgrosswgt1(String stdgrosswgt1) {
		this.stdgrosswgt1 = stdgrosswgt1;
	}
	public String getStduom() {
		return stduom;
	}
	public void setStduom(String stduom) {
		this.stduom = stduom;
	}
	public String getZerodefaultwgtforpick() {
		return zerodefaultwgtforpick;
	}
	public void setZerodefaultwgtforpick(String zerodefaultwgtforpick) {
		this.zerodefaultwgtforpick = zerodefaultwgtforpick;
	}
	public String getCartonizeft() {
		return cartonizeft;
	}
	public void setCartonizeft(String cartonizeft) {
		this.cartonizeft = cartonizeft;
	}
	
	//SRG: 9.2 Upgrade -- Start
	public String getNmfcclass() {
		return nmfcclass;
	}
	public void setNmfcclass(String nmfcclass) {
		this.nmfcclass = nmfcclass;
	}
	public String getMateabilitycode() {
		return mateabilitycode;
	}
	public void setMateabilitycode(String mateabilitycode) {
		this.mateabilitycode = mateabilitycode;
	}
	public String getFillqtyuom() {
		return fillqtyuom;
	}
	public void setFillqtyuom(String fillqtyuom) {
		this.fillqtyuom = fillqtyuom;
	}
	public String getIbsumcwflg() {
		return ibsumcwflg;
	}
	public void setIbsumcwflg(String ibsumcwflg) {
		this.ibsumcwflg = ibsumcwflg;
	}
	public String getObsumcwflg() {
		return obsumcwflg;
	}
	public void setObsumcwflg(String obsumcwflg) {
		this.obsumcwflg = obsumcwflg;
	}
	public String getShowrfcwontrans() {
		return showrfcwontrans;
	}
	public void setShowrfcwontrans(String showrfcwontrans) {
		this.showrfcwontrans = showrfcwontrans;
	}
	public String getRecurcode() {
		return recurcode;
	}
	public void setRecurcode(String recurcode) {
		this.recurcode = recurcode;
	}
	public String getWgtuom() {
		return wgtuom;
	}
	public void setWgtuom(String wgtuom) {
		this.wgtuom = wgtuom;
	}
	public String getDimenuom() {
		return dimenuom;
	}
	public void setDimenuom(String dimenuom) {
		this.dimenuom = dimenuom;
	}
	public String getCubeuom() {
		return cubeuom;
	}
	public void setCubeuom(String cubeuom) {
		this.cubeuom = cubeuom;
	}
	public String getStoragetype() {
		return storagetype;
	}
	public void setStoragetype(String storagetype) {
		this.storagetype = storagetype;
	}
	public String getAutoreleaselpnby() {
		return autoreleaselpnby;
	}
	public void setAutoreleaselpnby(String autoreleaselpnby) {
		this.autoreleaselpnby = autoreleaselpnby;
	}
	public String getHourstoholdlpn() {
		return hourstoholdlpn;
	}
	public void setHourstoholdlpn(String hourstoholdlpn) {
		this.hourstoholdlpn = hourstoholdlpn;
	}
	public String getLotholdcode() {
		return lotholdcode;
	}
	public void setLotholdcode(String lotholdcode) {
		this.lotholdcode = lotholdcode;
	}
	public String getAutoreleaselotby() {
		return autoreleaselotby;
	}
	public void setAutoreleaselotby(String autoreleaselotby) {
		this.autoreleaselotby = autoreleaselotby;
	}
	public String getHourstoholdlot() {
		return hourstoholdlot;
	}
	public void setHourstoholdlot(String hourstoholdlot) {
		this.hourstoholdlot = hourstoholdlot;
	}
	public String getAmstrategykey() {
		return amstrategykey;
	}
	public void setAmstrategykey(String amstrategykey) {
		this.amstrategykey = amstrategykey;
	}
	public String getPutawayclass() {
		return putawayclass;
	}
	public void setPutawayclass(String putawayclass) {
		this.putawayclass = putawayclass;
	}
	public String getTempforasn() {
		return tempforasn;
	}
	public void setTempforasn(String tempforasn) {
		this.tempforasn = tempforasn;
	}
	//SRG: 9.2 Upgrade -- End
	
}

