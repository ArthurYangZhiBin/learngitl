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
package com.infor.scm.wms.util.validation.screen.owner;

import java.util.ArrayList;

import com.infor.scm.wms.util.validation.screen.BaseScreenVO;

public class OwnerScreenVO extends BaseScreenVO{
	
	//Fields Not On Front End
	private String allowautocloseforasn = null;
	private String allowautocloseforpo = null;
	private String allowautocloseforps = null;
	private String allowduplicatelicenseplates = null;
	private String autoprintlabellpn = null;
	private String autoprintlabelputaway = null;
	private String b_company = null;
	private String cwoflag = null;
	private String notes2 = null;
	private String pickcode = null;
	private String rollreceipt = null;
	private String status = null;
	private String title1 = null;
	private String title2 = null;
	private String whseid = null;
	
	
	
	//General Tab Fields	
	private String storerkey = null;
	private String company = null;
	private String description = null;	
	private String scac_code = null;
	private String type = null;
	private String vat = null;
	private String containerExchangeFlag = null;

	
	
	//Address Tab Fields
	private String address1 = null;
	private String address2 = null;
	private String address3 = null;
	private String address4 = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String country = null;	
	private String isocntrycode = null;
	private String contact1 = null;
	private String phone1 = null;
	private String fax1 = null;
	private String email1 = null;
	private String contact2 = null;
	private String phone2 = null;
	private String fax2 = null;
	private String email2 = null;
	
	//Bill Address Tab Fields
	private String b_address1 = null;
	private String b_address2 = null;
	private String b_address3 = null;
	private String b_address4 = null;	
	private String b_city = null;
	private String b_state = null;
	private String b_zip = null;
	private String b_country = null;	
	private String b_isocntrycode = null;	
	private String b_contact1 = null;	
	private String b_phone1 = null;
	private String b_fax1 = null;
	private String b_email1 = null;
	private String b_contact2 = null;
	private String b_phone2 = null;
	private String b_fax2 = null;
	private String b_email2 = null;
	
	//Misc Tab Fields
	private String susr1 = null;
	private String susr2 = null;
	private String susr3 = null;
	private String susr4 = null;
	private String susr5 = null;
	private String susr6 = null;
	private String creditlimit = null;
	private String cartongroup = null;
	private String notes1 = null;
	
	//Flow Thru Tab Fields	
	private String opporderstrategykey = null;
	private String enableoppxdock = null;
	private String allowovershipment = null;	
	private String apportionrule = null;
	private String minimumpercent = null;
	private String maximumorders = null;
	private String orderdatestartdays = null;		
	private String orderdateenddays = null;	
	private String ordertyperestrict01 = null;
	private String ordertyperestrict02 = null;
	private String ordertyperestrict03 = null;
	private String ordertyperestrict04 = null;
	private String ordertyperestrict05 = null;
	private String ordertyperestrict06 = null;
	private String cartonizeftdflt = null;
	private String defftlabelprint = null;
	private String deffttaskcontrol = null;
	
	//Billing Tab Fields
	private String invoiceNumberStrategy = null;
	private String billingGroup = null;
	private String lockWho = null;
	private String documentMinimumHICharge = null;
	private String documentMinimumHITaxgroup = null;
	private String documentMinimumHIGLDistribution = null;
	private String documentMinimumHOCharge = null;
	private String documentMinimumHOTaxgroup = null;
	private String documentMinimumHOGLDistribution = null;
	private String documentMinimumISCharge = null;
	private String documentMinimumISTaxgroup = null;
	private String documentMinimumISGLDistribution = null;
	private String HIMinimumInvoiceCharge = null;
	private String HIMinimumInvoiceTaxgroup = null;
	private String HIMinimumInvoiceGLDistribution = null;
	private String ISMinimumInvoiceCharge = null;
	private String ISMinimumInvoiceTaxgroup = null;
	private String ISMinimumInvoiceGLDistribution = null;
	private String RSMinimumInvoiceCharge = null;
	private String RSMinimumInvoiceTaxgroup = null;
	private String RSMinimumInvoiceGLDistribution = null;
	private String AllMinimumInvoiceCharge = null;
	private String AllMinimumInvoiceTaxgroup = null;
	private String AllMinimumInvoiceGLDistribution = null;
	
	
	//Task Tab Fields
	private String enablepackingdefault = null;
	private String inspectatpack = null;	
	private String multizoneplpa = null;	
	private String skusetuprequired = null;
	private String defaultskurotation = null;
	private String defaultrotation = null;
	private String defaultstrategy = null;
	private String defaultputawaystrategy = null;	
	private String createpataskonrfreceipt = null;
	private String calculateputawaylocation = null;
	private String orderbreakdefault = null;	
	private String defaultqcloc = null;
	private String defaultqclocout = null;	
	private String defaultreturnsloc = null;
	private String defaultpackinglocation = null;
	private String packingvalidationtemplate = null;
	private String generatepacklist = null;
	private String defdapicksort = null;
	private String defrplnsort = null;
	private String reqreasonshortship = null;
	
	//Labels Tab Fields
	private String lpnbarcodesymbology = null;
	private String lpnbarcodeformat = null;
	private String lpnlength = null;
	private String lpnstartnumber = null;
	private String nextlpnnumber = null;	
	private String lpnrollbacknumber = null;
	private String caselabeltype = null;
	private String applicationid = null;
	private String sscc1stdigit = null;	
	private String uccvendornumber  = null;
	
	//Processing Tab Fields	
	private String ccdiscrepancyrule = null;	
	private String ccadjbyrf = null;	
	private String ccskuxloc = null;	
	private String piskuxloc = null;	
	private String allowcommingledlpn = null;
	private String autocloseasn = null;
	private String autoclosepo = null;
	private String allowsystemgeneratedlpn = null;
	private String allowsinglescanreceiving = null;
	private String receiptvalidationtemplate = null;	
	private String barcodeconfigkey = null;	
	private String trackinventoryby = null;
	private String dupcaseid = null;
	private String explodelpnlength = null;

	//jp.bugaware.9437.begin
	private String kship_carrier =  null;
	private String createoppxdtasks = null;
	private String issueoppxdtasks = null;
	private String oppxdpickfrom = null;
	private String obxdstage = null;
	private String spsuomweight = null;
	private String spsuomdimension  = null;
	//jp.bugaware.9437.end
	
	//Assign Locs Tab Fields
	private ArrayList ownerLabelsVOCollection = null;
	
	//Alt Tab Fields
	private ArrayList udfLabelsVOCollection = null;
		
	//SRG: 9.2 Upgrade -- Begin
	private String address5 = null;	
	private String address6 = null;
	private String addressoverwriteindicator = null;
	private String arcorp = null;
	private String ardept = null;
	private String aracct = null;
	private String measurecode = null;
	private String wgtuom = null;
	private String dimenuom = null;
	private String cubeuom = null;
	private String currcode = null;
	private String taxexempt = null;
	private String taxexemptcode = null;
	private String recurcode = null;
	private String dunsid = null;
	private String taxid = null;
	private String qfsurcharge = null;
	private String bfsurcharge = null;
	private String invoiceterms = null;
	private String invoicelevel = null;
	private String nonneglevel = null;	
	private String amstrategykey = null;	
	private String spsaccountnum = null;
	private String spscostcenter = null;
	private String spsreturnlabel = null;
	private String ownerprefix = null;	
	private String explodenextlpnnumber = null;
	private String explodelpnrollbacknumber = null;
	private String explodelpnstartnumber = null;
	private String accountingentity = null;
	private String parent = null;
	//SRG: 9.2 Upgrade -- End
	
		
	public String getOwner() {
		return storerkey;
	}
	public void setOwner(String owner) {
		this.storerkey = owner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStandardAlphaCode() {
		return scac_code;
	}
	public void setStandardAlphaCode(String standardAlphaCode) {
		this.scac_code = standardAlphaCode;
	}
	public String getValueAddedTaxId() {
		return vat;
	}
	public void setValueAddedTaxId(String valueAddedTaxId) {
		this.vat = valueAddedTaxId;
	}
	public String getAddressLine1() {
		return address1;
	}
	public void setAddressLine1(String addressLine1) {
		this.address1 = addressLine1;
	}
	public String getAddressLine2() {
		return address2;
	}
	public void setAddressLine2(String addressLine2) {
		this.address2 = addressLine2;
	}
	public String getAddressLine3() {
		return address3;
	}
	public void setAddressLine3(String addressLine3) {
		this.address3 = addressLine3;
	}
	public String getAddressLine4() {
		return address4;
	}
	public void setAddressLine4(String addressLine4) {
		this.address4 = addressLine4;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIsoCountryCode() {
		return isocntrycode;
	}
	public void setIsoCountryCode(String isoCountryCode) {
		this.isocntrycode = isoCountryCode;
	}
	public String getContact1() {
		return contact1;
	}
	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}
	public String getContact1Phone() {
		return phone1;
	}
	public void setContact1Phone(String contact1Phone) {
		this.phone1 = contact1Phone;
	}
	public String getContact1Fax() {
		return fax1;
	}
	public void setContact1Fax(String contact1Fax) {
		this.fax1 = contact1Fax;
	}
	public String getContact1Email() {
		return email1;
	}
	public void setContact1Email(String contact1Email) {
		this.email1 = contact1Email;
	}
	public String getContact2() {
		return contact2;
	}
	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}
	public String getContact2Phone() {
		return phone2;
	}
	public void setContact2Phone(String contact2Phone) {
		this.phone2 = contact2Phone;
	}
	public String getContact2Fax() {
		return fax2;
	}
	public void setContact2Fax(String contact2Fax) {
		this.fax2 = contact2Fax;
	}
	public String getContact2Email() {
		return email2;
	}
	public void setContact2Email(String contact2Email) {
		this.email2 = contact2Email;
	}
	public String getBillAddressLine1() {
		return b_address1;
	}
	public void setBillAddressLine1(String billAddressLine1) {
		this.b_address1 = billAddressLine1;
	}
	public String getBillAddressLine2() {
		return b_address2;
	}
	public void setBillAddressLine2(String billAddressLine2) {
		this.b_address2 = billAddressLine2;
	}
	public String getBillAddressLine3() {
		return b_address3;
	}
	public void setBillAddressLine3(String billAddressLine3) {
		this.b_address3 = billAddressLine3;
	}
	public String getBillAddressLine4() {
		return b_address4;
	}
	public void setBillAddressLine4(String billAddressLine4) {
		this.b_address4 = billAddressLine4;
	}
	public String getBillCity() {
		return b_city;
	}
	public void setBillCity(String billCity) {
		this.b_city = billCity;
	}
	public String getBillState() {
		return b_state;
	}
	public void setBillState(String billState) {
		this.b_state = billState;
	}
	public String getBillZip() {
		return b_zip;
	}
	public void setBillZip(String billZip) {
		this.b_zip = billZip;
	}
	public String getBillCountry() {
		return b_country;
	}
	public void setBillCountry(String billCountry) {
		this.b_country = billCountry;
	}
	public String getBillIsoCountryCode() {
		return b_isocntrycode;
	}
	public void setBillIsoCountryCode(String billIsoCountryCode) {
		this.b_isocntrycode = billIsoCountryCode;
	}
	public String getBillContact1() {
		return b_contact1;
	}
	public void setBillContact1(String billContact1) {
		this.b_contact1 = billContact1;
	}
	public String getBillContact1Phone() {
		return b_phone1;
	}
	public void setBillContact1Phone(String billContact1Phone) {
		this.b_phone1 = billContact1Phone;
	}
	public String getBillContact1Fax() {
		return b_fax1;
	}
	public void setBillContact1Fax(String billContact1Fax) {
		this.b_fax1 = billContact1Fax;
	}
	public String getBillContact1Email() {
		return b_email1;
	}
	public void setBillContact1Email(String billContact1Email) {
		this.b_email1 = billContact1Email;
	}
	public String getBillContact2() {
		return b_contact2;
	}
	public void setBillContact2(String billContact2) {
		this.b_contact2 = billContact2;
	}
	public String getBillContact2Phone() {
		return b_phone2;
	}
	public void setBillContact2Phone(String billContact2Phone) {
		this.b_phone2 = billContact2Phone;
	}
	public String getBillContact2Fax() {
		return b_fax2;
	}
	public void setBillContact2Fax(String billContact2Fax) {
		this.b_fax2 = billContact2Fax;
	}
	public String getBillContact2Email() {
		return b_email2;
	}
	public void setBillContact2Email(String billContact2Email) {
		this.b_email2 = billContact2Email;
	}
	public String getCreditLimit() {
		return creditlimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditlimit = creditLimit;
	}
	public String getCartonGroup() {
		return cartongroup;
	}
	public void setCartonGroup(String cartonGroup) {
		this.cartongroup = cartonGroup;
	}
	public String getNotes() {
		return notes1;
	}
	public void setNotes(String notes) {
		this.notes1 = notes;
	}
	public String getOrderSequenceStrategy() {
		return opporderstrategykey;
	}
	public void setOrderSequenceStrategy(String orderSequenceStrategy) {
		this.opporderstrategykey = orderSequenceStrategy;
	}
	public String getEnableOpportunisticCrossdock() {
		return enableoppxdock;
	}
	public void setEnableOpportunisticCrossdock(String enableOpportunisticCrossdock) {
		this.enableoppxdock = enableOpportunisticCrossdock;
	}
	public String getAllowPartialShipments() {
		return allowovershipment;
	}
	public void setAllowPartialShipments(String allowPartialShipments) {
		this.allowovershipment = allowPartialShipments;
	}
	public String getAutomaticApportionmentRule() {
		return apportionrule;
	}
	public void setAutomaticApportionmentRule(String automaticApportionmentRule) {
		this.apportionrule = automaticApportionmentRule;
	}
	public String getMinimumAllowablePct() {
		return minimumpercent;
	}
	public void setMinimumAllowablePct(String minimumAllowablePct) {
		this.minimumpercent = minimumAllowablePct;
	}
	public String getMaximumNumberOfOrders() {
		return maximumorders;
	}
	public void setMaximumNumberOfOrders(String maximumNumberOfOrders) {
		this.maximumorders = maximumNumberOfOrders;
	}
	public String getNumberOfDaysFromToday() {
		return orderdatestartdays;
	}
	public void setNumberOfDaysFromToday(String numberOfDaysFromToday) {
		this.orderdatestartdays = numberOfDaysFromToday;
	}
	public String getNumberOfDaysToToday() {
		return orderdateenddays;
	}
	public void setNumberOfDaysToToday(String numberOfDaysToToday) {
		this.orderdateenddays = numberOfDaysToToday;
	}
	public String getOrderType1() {
		return ordertyperestrict01;
	}
	public void setOrderType1(String orderType1) {
		this.ordertyperestrict01 = orderType1;
	}
	public String getOrderType2() {
		return ordertyperestrict02;
	}
	public void setOrderType2(String orderType2) {
		this.ordertyperestrict02 = orderType2;
	}
	public String getOrderType3() {
		return ordertyperestrict03;
	}
	public void setOrderType3(String orderType3) {
		this.ordertyperestrict03 = orderType3;
	}
	public String getOrderType4() {
		return ordertyperestrict04;
	}
	public void setOrderType4(String orderType4) {
		this.ordertyperestrict04 = orderType4;
	}
	public String getOrderType5() {
		return ordertyperestrict05;
	}
	public void setOrderType5(String orderType5) {
		this.ordertyperestrict05 = orderType5;
	}
	public String getOrderType6() {
		return ordertyperestrict06;
	}
	public void setOrderType6(String orderType6) {
		this.ordertyperestrict06 = orderType6;
	}
	public String getInvoiceNumberStrategy() {
		return invoiceNumberStrategy;
	}
	public void setInvoiceNumberStrategy(String invoiceNumberStrategy) {
		this.invoiceNumberStrategy = invoiceNumberStrategy;
	}
	public String getBillingGroup() {
		return billingGroup;
	}
	public void setBillingGroup(String billingGroup) {
		this.billingGroup = billingGroup;
	}
	public String getLockWho() {
		return lockWho;
	}
	public void setLockWho(String lockWho) {
		this.lockWho = lockWho;
	}
	public String getDocumentMinimumHICharge() {
		return documentMinimumHICharge;
	}
	
	public String getHiminimumreceiptcharge(){
		return documentMinimumHICharge;
	}
	
	public void setDocumentMinimumHICharge(String documentMinimumHICharge) {
		this.documentMinimumHICharge = documentMinimumHICharge;
	}
	
	public void setHiminimumreceiptcharge(String Himinimumreceiptcharge){
		this.documentMinimumHICharge = Himinimumreceiptcharge;
		
	}
	
	public String getDocumentMinimumHITaxgroup() {
		return documentMinimumHITaxgroup;
	}
	public String getHiminimumreceipttaxgroup() {
		return documentMinimumHITaxgroup;
	}

	public void setDocumentMinimumHITaxgroup(String documentMinimumHITaxgroup) {
		this.documentMinimumHITaxgroup = documentMinimumHITaxgroup;
	}
	public void setHiminimumreceipttaxgroup(String Himinimumreceipttaxgroup) {
		this.documentMinimumHITaxgroup = Himinimumreceipttaxgroup;
	}

	
	public String getDocumentMinimumHIGLDistribution() {
		return documentMinimumHIGLDistribution;
	}
	public String getHiminimumreceiptgldist() {
		return documentMinimumHIGLDistribution;
	}

	public void setDocumentMinimumHIGLDistribution(
			String documentMinimumHIGLDistribution) {
		this.documentMinimumHIGLDistribution = documentMinimumHIGLDistribution;
	}
	public void setHiminimumreceiptgldist(
			String Himinimumreceiptgldist) {
		this.documentMinimumHIGLDistribution = Himinimumreceiptgldist;
	}

	
	public String getDocumentMinimumHOCharge() {
		return documentMinimumHOCharge;
	}
	public String getHominimumshipmentcharge(){
		return documentMinimumHOCharge;
	}
	
	public void setDocumentMinimumHOCharge(String documentMinimumHOCharge) {
		this.documentMinimumHOCharge = documentMinimumHOCharge;
	}
	
	public void setHominimumshipmentcharge(String Hominimumshipmentcharge){
		this.documentMinimumHOCharge = Hominimumshipmentcharge;
	}
	
	public String getDocumentMinimumHOTaxgroup() {
		return documentMinimumHOTaxgroup;
	}
	public String getHominimumshipmenttaxgroup() {
		return documentMinimumHOTaxgroup;
	}
	
	public void setDocumentMinimumHOTaxgroup(String documentMinimumHOTaxgroup) {
		this.documentMinimumHOTaxgroup = documentMinimumHOTaxgroup;
	}
	public void setHominimumshipmenttaxgroup(String Hominimumshipmenttaxgroup) {
		this.documentMinimumHOTaxgroup = Hominimumshipmenttaxgroup;
	}

	public String getDocumentMinimumHOGLDistribution() {
		return documentMinimumHOGLDistribution;
	}
	public String getHominimumshipmentgldist() {
		return documentMinimumHOGLDistribution;
	}

	public void setDocumentMinimumHOGLDistribution(
			String documentMinimumHOGLDistribution) {
		this.documentMinimumHOGLDistribution = documentMinimumHOGLDistribution;
	}
	public void setHominimumshipmentgldist(
			String Hominimumshipmentgldist) {
		this.documentMinimumHOGLDistribution = Hominimumshipmentgldist;
	}

	
	public String getDocumentMinimumISCharge() {
		return documentMinimumISCharge;
	}

	public String getIsminimumreceiptcharge() {
		return documentMinimumISCharge;
	}

	public void setDocumentMinimumISCharge(String documentMinimumISCharge) {
		this.documentMinimumISCharge = documentMinimumISCharge;
	}
	
	public void setIsminimumreceiptcharge(String Isminimumreceiptcharge) {
		this.documentMinimumISCharge = Isminimumreceiptcharge;
	}

	public String getDocumentMinimumISTaxgroup() {
		return documentMinimumISTaxgroup;
	}
	public String getIsminimumreceipttaxgroup() {
		return documentMinimumISTaxgroup;
	}

	
	public void setDocumentMinimumISTaxgroup(String documentMinimumISTaxgroup) {
		this.documentMinimumISTaxgroup = documentMinimumISTaxgroup;
	}
	public void setIsminimumreceipttaxgroup(String Isminimumreceipttaxgroup) {
		this.documentMinimumISTaxgroup = Isminimumreceipttaxgroup;
	}

	public String getDocumentMinimumISGLDistribution() {
		return documentMinimumISGLDistribution;
	}
	public String getIsminimumreceiptgldist() {
		return documentMinimumISGLDistribution;
	}

	
	public void setDocumentMinimumISGLDistribution(
			String documentMinimumISGLDistribution) {
		this.documentMinimumISGLDistribution = documentMinimumISGLDistribution;
	}
	public void setIsminimumreceiptgldist(
			String Isminimumreceiptgldist) {
		this.documentMinimumISGLDistribution = Isminimumreceiptgldist;
	}

	public String getHIMinimumInvoiceCharge() {
		return HIMinimumInvoiceCharge;
	}
	public void setHIMinimumInvoiceCharge(String minimumInvoiceCharge) {
		HIMinimumInvoiceCharge = minimumInvoiceCharge;
	}
	public String getHIMinimumInvoiceTaxgroup() {
		return HIMinimumInvoiceTaxgroup;
	}
	public void setHIMinimumInvoiceTaxgroup(String minimumInvoiceTaxgroup) {
		HIMinimumInvoiceTaxgroup = minimumInvoiceTaxgroup;
	}
	public String getHIMinimumInvoiceGLDistribution() {
		return HIMinimumInvoiceGLDistribution;
	}
	public void setHIMinimumInvoiceGLDistribution(
			String minimumInvoiceGLDistribution) {
		HIMinimumInvoiceGLDistribution = minimumInvoiceGLDistribution;
	}
	public String getISMinimumInvoiceCharge() {
		return ISMinimumInvoiceCharge;
	}
	public void setISMinimumInvoiceCharge(String minimumInvoiceCharge) {
		ISMinimumInvoiceCharge = minimumInvoiceCharge;
	}
	public String getISMinimumInvoiceTaxgroup() {
		return ISMinimumInvoiceTaxgroup;
	}
	public void setISMinimumInvoiceTaxgroup(String minimumInvoiceTaxgroup) {
		ISMinimumInvoiceTaxgroup = minimumInvoiceTaxgroup;
	}
	public String getISMinimumInvoiceGLDistribution() {
		return ISMinimumInvoiceGLDistribution;
	}
	public void setISMinimumInvoiceGLDistribution(
			String minimumInvoiceGLDistribution) {
		ISMinimumInvoiceGLDistribution = minimumInvoiceGLDistribution;
	}
	public String getRSMinimumInvoiceCharge() {
		return RSMinimumInvoiceCharge;
	}
	public void setRSMinimumInvoiceCharge(String minimumInvoiceCharge) {
		RSMinimumInvoiceCharge = minimumInvoiceCharge;
	}
	public String getRSMinimumInvoiceTaxgroup() {
		return RSMinimumInvoiceTaxgroup;
	}
	public void setRSMinimumInvoiceTaxgroup(String minimumInvoiceTaxgroup) {
		RSMinimumInvoiceTaxgroup = minimumInvoiceTaxgroup;
	}
	public String getRSMinimumInvoiceGLDistribution() {
		return RSMinimumInvoiceGLDistribution;
	}
	public void setRSMinimumInvoiceGLDistribution(
			String minimumInvoiceGLDistribution) {
		RSMinimumInvoiceGLDistribution = minimumInvoiceGLDistribution;
	}
	public String getAllMinimumInvoiceCharge() {
		return AllMinimumInvoiceCharge;
	}
	public void setAllMinimumInvoiceCharge(String allMinimumInvoiceCharge) {
		AllMinimumInvoiceCharge = allMinimumInvoiceCharge;
	}
	public String getAllMinimumInvoiceTaxgroup() {
		return AllMinimumInvoiceTaxgroup;
	}
	public void setAllMinimumInvoiceTaxgroup(String allMinimumInvoiceTaxgroup) {
		AllMinimumInvoiceTaxgroup = allMinimumInvoiceTaxgroup;
	}
	public String getAllMinimumInvoiceGLDistribution() {
		return AllMinimumInvoiceGLDistribution;
	}
	public void setAllMinimumInvoiceGLDistribution(
			String allMinimumInvoiceGLDistribution) {
		AllMinimumInvoiceGLDistribution = allMinimumInvoiceGLDistribution;
	}
	public String getPackingEnabled() {
		return enablepackingdefault;
	}
	public void setPackingEnabled(String packingEnabled) {
		this.enablepackingdefault = packingEnabled;
	}
	public String getQCInspectAtPack() {
		return inspectatpack;
	}
	public void setQCInspectAtPack(String inspectAtPack) {
		inspectatpack = inspectAtPack;
	}
	public String getAllowMultiZoneRainbowPallet() {
		return multizoneplpa;
	}
	public void setAllowMultiZoneRainbowPallet(String allowMultiZoneRainbowPallet) {
		this.multizoneplpa = allowMultiZoneRainbowPallet;
	}
	public String getManualSetupRequiredForImportedItem() {
		return skusetuprequired;
	}
	public void setManualSetupRequiredForImportedItem(
			String manualSetupRequiredForImportedItem) {
		skusetuprequired = manualSetupRequiredForImportedItem;
	}
	public String getDefaultItemRotation() {
		return defaultskurotation;
	}
	public void setDefaultItemRotation(String defaultItemRotation) {
		this.defaultskurotation = defaultItemRotation;
	}
	public String getDefaultRotation() {
		return defaultrotation;
	}
	public void setDefaultRotation(String defaultRotation) {
		this.defaultrotation = defaultRotation;
	}
	public String getDefaultStrategy() {
		return defaultstrategy;
	}
	public void setDefaultStrategy(String defaultStrategy) {
		this.defaultstrategy = defaultStrategy;
	}
	public String getDefaultPutawayStrategy() {
		return defaultputawaystrategy;
	}
	public void setDefaultPutawayStrategy(String defaultPutawayStrategy) {
		this.defaultputawaystrategy = defaultPutawayStrategy;
	}
	public String getCreatePutawayTaskonRFReceipt() {
		return createpataskonrfreceipt;
	}
	public void setCreatePutawayTaskonRFReceipt(String createPutawayTaskonRFReceipt) {
		this.createpataskonrfreceipt = createPutawayTaskonRFReceipt;
	}
	public String getCalculatePutawayLocation() {
		return calculateputawaylocation;
	}
	public void setCalculatePutawayLocation(String calculatePutawayLocation) {
		this.calculateputawaylocation = calculatePutawayLocation;
	}
	public String getAssignmentOrderBreakDefault() {
		return orderbreakdefault;
	}
	public void setAssignmentOrderBreakDefault(String assignmentOrderBreakDefault) {
		this.orderbreakdefault = assignmentOrderBreakDefault;
	}
	public String getDefaultInboundQCLocation() {
		return defaultqcloc;
	}
	public void setDefaultInboundQCLocation(String defaultInboundQCLocation) {
		this.defaultqcloc = defaultInboundQCLocation;
	}
	public String getDefaultOutboundQCLocation() {
		return defaultqclocout;
	}
	public void setDefaultOutboundQCLocation(String defaultOutboundQCLocation) {
		this.defaultqclocout = defaultOutboundQCLocation;
	}
	public String getDefaultReturnReceiptLocation() {
		return defaultreturnsloc;
	}
	public void setDefaultReturnReceiptLocation(String defaultReturnReceiptLocation) {
		this.defaultreturnsloc = defaultReturnReceiptLocation;
	}
	public String getDefaultPackingLocation() {
		return defaultpackinglocation;
	}
	public void setDefaultPackingLocation(String defaultPackingLocation) {
		this.defaultpackinglocation = defaultPackingLocation;
	}
	public String getPackingValidationTemplate() {
		return packingvalidationtemplate;
	}
	public void setPackingValidationTemplate(String packingValidationTemplate) {
		this.packingvalidationtemplate = packingValidationTemplate;
	}
	public String getLpnBarcodeSymbology() {
		return lpnbarcodesymbology;
	}
	public void setLpnBarcodeSymbology(String lpnBarcodeSymbology) {
		this.lpnbarcodesymbology = lpnBarcodeSymbology;
	}
	public String getLpnBarcodeFormat() {
		return lpnbarcodeformat;
	}
	public void setLpnBarcodeFormat(String lpnBarcodeFormat) {
		this.lpnbarcodeformat = lpnBarcodeFormat;
	}
	public String getLpnLength() {
		return lpnlength;
	}
	public void setLpnLength(String lpnLength) {
		this.lpnlength = lpnLength;
	}
	public String getLpnStartNumber() {
		return lpnstartnumber;
	}
	public void setLpnStartNumber(String lpnStartNumber) {
		this.lpnstartnumber = lpnStartNumber;
	}
	public String getLpnNextNumber() {
		return nextlpnnumber;
	}
	public void setLpnNextNumber(String lpnNextNumber) {
		this.nextlpnnumber = lpnNextNumber;
	}
	public String getLpnRollBackNumber() {
		return lpnrollbacknumber;
	}
	public void setLpnRollBackNumber(String lpnRollBackNumber) {
		this.lpnrollbacknumber = lpnRollBackNumber;
	}
	public String getCaseLabelType() {
		return caselabeltype;
	}
	public void setCaseLabelType(String caseLabelType) {
		this.caselabeltype = caseLabelType;
	}
	public String getApplicationID() {
		return applicationid;
	}
	public void setApplicationID(String applicationID) {
		this.applicationid = applicationID;
	}
	public String getSscc1stDigit() {
		return sscc1stdigit;
	}
	public void setSscc1stDigit(String sscc1stDigit) {
		this.sscc1stdigit = sscc1stDigit;
	}
	public String getUccVendor() {
		return uccvendornumber;
	}
	public void setUccVendor(String uccVendor) {
		this.uccvendornumber = uccVendor;
	}
	public String getDefaultDiscrepancyHandlingRule() {
		return ccdiscrepancyrule;
	}
	public void setDefaultDiscrepancyHandlingRule(
			String defaultDiscrepancyHandlingRule) {
		this.ccdiscrepancyrule = defaultDiscrepancyHandlingRule;
	}
	public String getAllowCycleCountAdjustingduringRF() {
		return ccadjbyrf;
	}
	public void setAllowCycleCountAdjustingduringRF(
			String allowCycleCountAdjustingduringRF) {
		this.ccadjbyrf = allowCycleCountAdjustingduringRF;
	}
	public String getCcPerformUsingItemAndLocationLevel() {
		return ccskuxloc;
	}
	public void setCcPerformUsingItemAndLocationLevel(
			String ccPerformUsingItemAndLocationLevel) {
		this.ccskuxloc = ccPerformUsingItemAndLocationLevel;
	}
	public String getPiPerformUsingItemAndLocationLevel() {
		return piskuxloc;
	}
	public void setPiPerformUsingItemAndLocationLevel(
			String piPerformUsingItemAndLocationLevel) {
		this.piskuxloc = piPerformUsingItemAndLocationLevel;
	}
	public String getAllowCommingleLPN() {
		return allowcommingledlpn;
	}
	public void setAllowCommingleLPN(String allowCommingleLPN) {
		this.allowcommingledlpn = allowCommingleLPN;
	}
	public String getAllowAutoCloseASN() {
		return autocloseasn;
	}
	public void setAllowAutoCloseASN(String allowAutoCloseASN) {
		this.autocloseasn = allowAutoCloseASN;
	}
	public String getAllowAutoClosePO() {
		return autoclosepo;
	}
	public void setAllowAutoClosePO(String allowAutoClosePO) {
		this.autoclosepo = allowAutoClosePO;
	}
	public String getAllowSystemGeneratedLPN() {
		return allowsystemgeneratedlpn;
	}
	public void setAllowSystemGeneratedLPN(String allowSystemGeneratedLPN) {
		this.allowsystemgeneratedlpn = allowSystemGeneratedLPN;
	}
	public String getAllowSingleScanReceiving() {
		return allowsinglescanreceiving;
	}
	public void setAllowSingleScanReceiving(String allowSingleScanReceiving) {
		this.allowsinglescanreceiving = allowSingleScanReceiving;
	}
	public String getReceiptValidationTemplate() {
		return receiptvalidationtemplate;
	}
	public void setReceiptValidationTemplate(String receiptValidationTemplate) {
		this.receiptvalidationtemplate = receiptValidationTemplate;
	}
	public String getBarcodeConfigurationKey() {
		return barcodeconfigkey;
	}
	public void setBarcodeConfigurationKey(String barcodeConfigurationKey) {
		this.barcodeconfigkey = barcodeConfigurationKey;
	}
	public String getDefaultTrackingForCaseReceipts() {
		return trackinventoryby;
	}
	public void setDefaultTrackingForCaseReceipts(
			String defaultTrackingForCaseReceipts) {
		this.trackinventoryby = defaultTrackingForCaseReceipts;
	}
	public String getDuplicateCaseIDs() {
		return dupcaseid;
	}
	public void setDuplicateCaseIDs(String duplicateCaseIDs) {
		this.dupcaseid = duplicateCaseIDs;
	}
	public ArrayList getOwnerLabelsVOCollection() {
		return ownerLabelsVOCollection;
	}
	public void setOwnerLabelsVOCollection(ArrayList ownerLabelsVOCollection) {
		this.ownerLabelsVOCollection = ownerLabelsVOCollection;
	}
	public ArrayList getUdfLabelsVOCollection() {
		return udfLabelsVOCollection;
	}
	public void setUdfLabelsVOCollection(ArrayList udfLabelsVOCollection) {
		this.udfLabelsVOCollection = udfLabelsVOCollection;
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
	
	
	
	
	
	public class OwnerLabelVO{
		private String labelType = null;
		private String labelName = null;
		private String customer = null;
		public String getLabelType() {
			return labelType;
		}
		public void setLabelType(String labelType) {
			this.labelType = labelType;
		}
		public String getLabelName() {
			return labelName;
		}
		public void setLabelName(String labelName) {
			this.labelName = labelName;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		
		
	}


	
	
	public class UDFLabelVO{
		private String udfLabel = null;
		private String udfValue = null;
		private String udfNotes = null;
		public String getUdfLabel() {
			return udfLabel;
		}
		public void setUdfLabel(String udfLabel) {
			this.udfLabel = udfLabel;
		}
		public String getUdfValue() {
			return udfValue;
		}
		public void setUdfValue(String udfValue) {
			this.udfValue = udfValue;
		}
		public String getUdfNotes() {
			return udfNotes;
		}
		public void setUdfNotes(String udfNotes) {
			this.udfNotes = udfNotes;
		}
		
		
	}




	public String getAllowautocloseforasn() {
		return allowautocloseforasn;
	}
	public void setAllowautocloseforasn(String allowautocloseforasn) {
		this.allowautocloseforasn = allowautocloseforasn;
	}
	public String getAllowautocloseforpo() {
		return allowautocloseforpo;
	}
	public void setAllowautocloseforpo(String allowautocloseforpo) {
		this.allowautocloseforpo = allowautocloseforpo;
	}
	public String getAllowautocloseforps() {
		return allowautocloseforps;
	}
	public void setAllowautocloseforps(String allowautocloseforps) {
		this.allowautocloseforps = allowautocloseforps;
	}
	public String getAllowduplicatelicenseplates() {
		return allowduplicatelicenseplates;
	}
	public void setAllowduplicatelicenseplates(String allowduplicatelicenseplates) {
		this.allowduplicatelicenseplates = allowduplicatelicenseplates;
	}
	public String getAutoprintlabellpn() {
		return autoprintlabellpn;
	}
	public void setAutoprintlabellpn(String autoprintlabellpn) {
		this.autoprintlabellpn = autoprintlabellpn;
	}
	public String getAutoprintlabelputaway() {
		return autoprintlabelputaway;
	}
	public void setAutoprintlabelputaway(String autoprintlabelputaway) {
		this.autoprintlabelputaway = autoprintlabelputaway;
	}
	public String getB_company() {
		return b_company;
	}
	public void setB_company(String b_company) {
		this.b_company = b_company;
	}
	public String getCwoflag() {
		return cwoflag;
	}
	public void setCwoflag(String cwoflag) {
		this.cwoflag = cwoflag;
	}
	public String getNotes2() {
		return notes2;
	}
	public void setNotes2(String notes2) {
		this.notes2 = notes2;
	}
	public String getPickcode() {
		return pickcode;
	}
	public void setPickcode(String pickcode) {
		this.pickcode = pickcode;
	}
	public String getRollreceipt() {
		return rollreceipt;
	}
	public void setRollreceipt(String rollreceipt) {
		this.rollreceipt = rollreceipt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getWhseid() {
		return whseid;
	}
	public void setWhseid(String whseid) {
		this.whseid = whseid;
	}
	public String getStorerkey() {
		return storerkey;
	}
	public void setStorerkey(String storerkey) {
		this.storerkey = storerkey;
	}
	public String getScac_code() {
		return scac_code;
	}
	public void setScac_code(String scac_code) {
		this.scac_code = scac_code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	public String getIsocntrycode() {
		return isocntrycode;
	}
	public void setIsocntrycode(String isocntrycode) {
		this.isocntrycode = isocntrycode;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getFax1() {
		return fax1;
	}
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getB_address1() {
		return b_address1;
	}
	public void setB_address1(String b_address1) {
		this.b_address1 = b_address1;
	}
	public String getB_address2() {
		return b_address2;
	}
	public void setB_address2(String b_address2) {
		this.b_address2 = b_address2;
	}
	public String getB_address3() {
		return b_address3;
	}
	public void setB_address3(String b_address3) {
		this.b_address3 = b_address3;
	}
	public String getB_address4() {
		return b_address4;
	}
	public void setB_address4(String b_address4) {
		this.b_address4 = b_address4;
	}
	public String getB_city() {
		return b_city;
	}
	public void setB_city(String b_city) {
		this.b_city = b_city;
	}
	public String getB_state() {
		return b_state;
	}
	public void setB_state(String b_state) {
		this.b_state = b_state;
	}
	public String getB_zip() {
		return b_zip;
	}
	public void setB_zip(String b_zip) {
		this.b_zip = b_zip;
	}
	public String getB_country() {
		return b_country;
	}
	public void setB_country(String b_country) {
		this.b_country = b_country;
	}
	public String getB_isocntrycode() {
		return b_isocntrycode;
	}
	public void setB_isocntrycode(String b_isocntrycode) {
		this.b_isocntrycode = b_isocntrycode;
	}
	public String getB_contact1() {
		return b_contact1;
	}
	public void setB_contact1(String b_contact1) {
		this.b_contact1 = b_contact1;
	}
	public String getB_phone1() {
		return b_phone1;
	}
	public void setB_phone1(String b_phone1) {
		this.b_phone1 = b_phone1;
	}
	public String getB_fax1() {
		return b_fax1;
	}
	public void setB_fax1(String b_fax1) {
		this.b_fax1 = b_fax1;
	}
	public String getB_email1() {
		return b_email1;
	}
	public void setB_email1(String b_email1) {
		this.b_email1 = b_email1;
	}
	public String getB_contact2() {
		return b_contact2;
	}
	public void setB_contact2(String b_contact2) {
		this.b_contact2 = b_contact2;
	}
	public String getB_phone2() {
		return b_phone2;
	}
	public void setB_phone2(String b_phone2) {
		this.b_phone2 = b_phone2;
	}
	public String getB_fax2() {
		return b_fax2;
	}
	public void setB_fax2(String b_fax2) {
		this.b_fax2 = b_fax2;
	}
	public String getB_email2() {
		return b_email2;
	}
	public void setB_email2(String b_email2) {
		this.b_email2 = b_email2;
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
	public String getCreditlimit() {
		return creditlimit;
	}
	public void setCreditlimit(String creditlimit) {
		this.creditlimit = creditlimit;
	}
	public String getCartongroup() {
		return cartongroup;
	}
	public void setCartongroup(String cartongroup) {
		this.cartongroup = cartongroup;
	}
	public String getNotes1() {
		return notes1;
	}
	public void setNotes1(String notes1) {
		this.notes1 = notes1;
	}
	public String getOpporderstrategykey() {
		return opporderstrategykey;
	}
	public void setOpporderstrategykey(String opporderstrategykey) {
		this.opporderstrategykey = opporderstrategykey;
	}
	public String getEnableoppxdock() {
		return enableoppxdock;
	}
	public void setEnableoppxdock(String enableoppxdock) {
		this.enableoppxdock = enableoppxdock;
	}
	public String getAllowovershipment() {
		return allowovershipment;
	}
	public void setAllowovershipment(String allowovershipment) {
		this.allowovershipment = allowovershipment;
	}
	public String getApportionrule() {
		return apportionrule;
	}
	public void setApportionrule(String apportionrule) {
		this.apportionrule = apportionrule;
	}
	public String getMinimumpercent() {
		return minimumpercent;
	}
	public void setMinimumpercent(String minimumpercent) {
		this.minimumpercent = minimumpercent;
	}
	public String getMaximumorders() {
		return maximumorders;
	}
	public void setMaximumorders(String maximumorders) {
		this.maximumorders = maximumorders;
	}
	public String getOrderdatestartdays() {
		return orderdatestartdays;
	}
	public void setOrderdatestartdays(String orderdatestartdays) {
		this.orderdatestartdays = orderdatestartdays;
	}
	public String getOrderdateenddays() {
		return orderdateenddays;
	}
	public void setOrderdateenddays(String orderdateenddays) {
		this.orderdateenddays = orderdateenddays;
	}
	public String getOrdertyperestrict01() {
		return ordertyperestrict01;
	}
	public void setOrdertyperestrict01(String ordertyperestrict01) {
		this.ordertyperestrict01 = ordertyperestrict01;
	}
	public String getOrdertyperestrict02() {
		return ordertyperestrict02;
	}
	public void setOrdertyperestrict02(String ordertyperestrict02) {
		this.ordertyperestrict02 = ordertyperestrict02;
	}
	public String getOrdertyperestrict03() {
		return ordertyperestrict03;
	}
	public void setOrdertyperestrict03(String ordertyperestrict03) {
		this.ordertyperestrict03 = ordertyperestrict03;
	}
	public String getOrdertyperestrict04() {
		return ordertyperestrict04;
	}
	public void setOrdertyperestrict04(String ordertyperestrict04) {
		this.ordertyperestrict04 = ordertyperestrict04;
	}
	public String getOrdertyperestrict05() {
		return ordertyperestrict05;
	}
	public void setOrdertyperestrict05(String ordertyperestrict05) {
		this.ordertyperestrict05 = ordertyperestrict05;
	}
	public String getOrdertyperestrict06() {
		return ordertyperestrict06;
	}
	public void setOrdertyperestrict06(String ordertyperestrict06) {
		this.ordertyperestrict06 = ordertyperestrict06;
	}
	public String getEnablepackingdefault() {
		return enablepackingdefault;
	}
	public void setEnablepackingdefault(String enablepackingdefault) {
		this.enablepackingdefault = enablepackingdefault;
	}
	public String getInspectatpack() {
		return inspectatpack;
	}
	public void setInspectatpack(String inspectatpack) {
		this.inspectatpack = inspectatpack;
	}
	public String getMultizoneplpa() {
		return multizoneplpa;
	}
	public void setMultizoneplpa(String multizoneplpa) {
		this.multizoneplpa = multizoneplpa;
	}
	public String getSkusetuprequired() {
		return skusetuprequired;
	}
	public void setSkusetuprequired(String skusetuprequired) {
		this.skusetuprequired = skusetuprequired;
	}
	public String getDefaultskurotation() {
		return defaultskurotation;
	}
	public void setDefaultskurotation(String defaultskurotation) {
		this.defaultskurotation = defaultskurotation;
	}
	public String getDefaultrotation() {
		return defaultrotation;
	}
	public void setDefaultrotation(String defaultrotation) {
		this.defaultrotation = defaultrotation;
	}
	public String getDefaultstrategy() {
		return defaultstrategy;
	}
	public void setDefaultstrategy(String defaultstrategy) {
		this.defaultstrategy = defaultstrategy;
	}
	public String getDefaultputawaystrategy() {
		return defaultputawaystrategy;
	}
	public void setDefaultputawaystrategy(String defaultputawaystrategy) {
		this.defaultputawaystrategy = defaultputawaystrategy;
	}
	public String getCreatepataskonrfreceipt() {
		return createpataskonrfreceipt;
	}
	public void setCreatepataskonrfreceipt(String createpataskonrfreceipt) {
		this.createpataskonrfreceipt = createpataskonrfreceipt;
	}
	public String getCalculateputawaylocation() {
		return calculateputawaylocation;
	}
	public void setCalculateputawaylocation(String calculateputawaylocation) {
		this.calculateputawaylocation = calculateputawaylocation;
	}
	public String getOrderbreakdefault() {
		return orderbreakdefault;
	}
	public void setOrderbreakdefault(String orderbreakdefault) {
		this.orderbreakdefault = orderbreakdefault;
	}
	public String getDefaultqcloc() {
		return defaultqcloc;
	}
	public void setDefaultqcloc(String defaultqcloc) {
		this.defaultqcloc = defaultqcloc;
	}
	public String getDefaultqclocout() {
		return defaultqclocout;
	}
	public void setDefaultqclocout(String defaultqclocout) {
		this.defaultqclocout = defaultqclocout;
	}
	public String getDefaultreturnsloc() {
		return defaultreturnsloc;
	}
	public void setDefaultreturnsloc(String defaultreturnsloc) {
		this.defaultreturnsloc = defaultreturnsloc;
	}
	public String getDefaultpackinglocation() {
		return defaultpackinglocation;
	}
	public void setDefaultpackinglocation(String defaultpackinglocation) {
		this.defaultpackinglocation = defaultpackinglocation;
	}
	public String getPackingvalidationtemplate() {
		return packingvalidationtemplate;
	}
	public void setPackingvalidationtemplate(String packingvalidationtemplate) {
		this.packingvalidationtemplate = packingvalidationtemplate;
	}
	public String getGeneratepacklist() {
		return generatepacklist;
	}
	public void setGeneratepacklist(String generatepacklist) {
		this.generatepacklist = generatepacklist;
	}
	public String getLpnbarcodesymbology() {
		return lpnbarcodesymbology;
	}
	public void setLpnbarcodesymbology(String lpnbarcodesymbology) {
		this.lpnbarcodesymbology = lpnbarcodesymbology;
	}
	public String getLpnbarcodeformat() {
		return lpnbarcodeformat;
	}
	public void setLpnbarcodeformat(String lpnbarcodeformat) {
		this.lpnbarcodeformat = lpnbarcodeformat;
	}
	public String getLpnlength() {
		return lpnlength;
	}
	public void setLpnlength(String lpnlength) {
		this.lpnlength = lpnlength;
	}
	public String getLpnstartnumber() {
		return lpnstartnumber;
	}
	public void setLpnstartnumber(String lpnstartnumber) {
		this.lpnstartnumber = lpnstartnumber;
	}
	public String getNextlpnnumber() {
		return nextlpnnumber;
	}
	public void setNextlpnnumber(String nextlpnnumber) {
		this.nextlpnnumber = nextlpnnumber;
	}
	public String getLpnrollbacknumber() {
		return lpnrollbacknumber;
	}
	public void setLpnrollbacknumber(String lpnrollbacknumber) {
		this.lpnrollbacknumber = lpnrollbacknumber;
	}
	public String getCaselabeltype() {
		return caselabeltype;
	}
	public void setCaselabeltype(String caselabeltype) {
		this.caselabeltype = caselabeltype;
	}
	public String getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	public String getSscc1stdigit() {
		return sscc1stdigit;
	}
	public void setSscc1stdigit(String sscc1stdigit) {
		this.sscc1stdigit = sscc1stdigit;
	}
	public String getUccvendornumber() {
		return uccvendornumber;
	}
	public void setUccvendornumber(String uccvendornumber) {
		this.uccvendornumber = uccvendornumber;
	}
	public String getCcdiscrepancyrule() {
		return ccdiscrepancyrule;
	}
	public void setCcdiscrepancyrule(String ccdiscrepancyrule) {
		this.ccdiscrepancyrule = ccdiscrepancyrule;
	}
	public String getCcadjbyrf() {
		return ccadjbyrf;
	}
	public void setCcadjbyrf(String ccadjbyrf) {
		this.ccadjbyrf = ccadjbyrf;
	}
	public String getCcskuxloc() {
		return ccskuxloc;
	}
	public void setCcskuxloc(String ccskuxloc) {
		this.ccskuxloc = ccskuxloc;
	}
	public String getPiskuxloc() {
		return piskuxloc;
	}
	public void setPiskuxloc(String piskuxloc) {
		this.piskuxloc = piskuxloc;
	}
	public String getAllowcommingledlpn() {
		return allowcommingledlpn;
	}
	public void setAllowcommingledlpn(String allowcommingledlpn) {
		this.allowcommingledlpn = allowcommingledlpn;
	}
	public String getAutocloseasn() {
		return autocloseasn;
	}
	public void setAutocloseasn(String autocloseasn) {
		this.autocloseasn = autocloseasn;
	}
	public String getAutoclosepo() {
		return autoclosepo;
	}
	public void setAutoclosepo(String autoclosepo) {
		this.autoclosepo = autoclosepo;
	}
	public String getAllowsystemgeneratedlpn() {
		return allowsystemgeneratedlpn;
	}
	public void setAllowsystemgeneratedlpn(String allowsystemgeneratedlpn) {
		this.allowsystemgeneratedlpn = allowsystemgeneratedlpn;
	}
	public String getAllowsinglescanreceiving() {
		return allowsinglescanreceiving;
	}
	public void setAllowsinglescanreceiving(String allowsinglescanreceiving) {
		this.allowsinglescanreceiving = allowsinglescanreceiving;
	}
	public String getReceiptvalidationtemplate() {
		return receiptvalidationtemplate;
	}
	public void setReceiptvalidationtemplate(String receiptvalidationtemplate) {
		this.receiptvalidationtemplate = receiptvalidationtemplate;
	}
	public String getBarcodeconfigkey() {
		return barcodeconfigkey;
	}
	public void setBarcodeconfigkey(String barcodeconfigkey) {
		this.barcodeconfigkey = barcodeconfigkey;
	}
	public String getTrackinventoryby() {
		return trackinventoryby;
	}
	public void setTrackinventoryby(String trackinventoryby) {
		this.trackinventoryby = trackinventoryby;
	}
	public String getDupcaseid() {
		return dupcaseid;
	}
	public void setDupcaseid(String dupcaseid) {
		this.dupcaseid = dupcaseid;
	}
	public String getKship_carrier() {
		return kship_carrier;
	}
	public void setKship_carrier(String kship_carrier) {
		this.kship_carrier = kship_carrier;
	}
	public String getCreateoppxdtasks() {
		return createoppxdtasks;
	}
	public void setCreateoppxdtasks(String createoppxdtasks) {
		this.createoppxdtasks = createoppxdtasks;
	}
	public String getIssueoppxdtasks() {
		return issueoppxdtasks;
	}
	public void setIssueoppxdtasks(String issueoppxdtasks) {
		this.issueoppxdtasks = issueoppxdtasks;
	}
	public String getOppxdpickfrom() {
		return oppxdpickfrom;
	}
	public void setOppxdpickfrom(String oppxdpickfrom) {
		this.oppxdpickfrom = oppxdpickfrom;
	}
	public String getObxdstage() {
		return obxdstage;
	}
	public void setObxdstage(String obxdstage) {
		this.obxdstage = obxdstage;
	}
	public String getSpsuomweight() {
		return spsuomweight;
	}
	public void setSpsuomweight(String spsuomweight) {
		this.spsuomweight = spsuomweight;
	}
	public String getSpsuomdimension() {
		return spsuomdimension;
	}
	public void setSpsuomdimension(String spsuomdimension) {
		this.spsuomdimension = spsuomdimension;
	}
	public String getDefdapicksort() {
		return defdapicksort;
	}
	public void setDefdapicksort(String defdapicksort) {
		this.defdapicksort = defdapicksort;
	}
	public String getDefrplnsort() {
		return defrplnsort;
	}
	public void setDefrplnsort(String defrplnsort) {
		this.defrplnsort = defrplnsort;
	}
	public String getReqreasonshortship() {
		return reqreasonshortship;
	}
	public void setReqreasonshortship(String reqreasonshortship) {
		this.reqreasonshortship = reqreasonshortship;
	}
	public String getExplodelpnlength() {
		return explodelpnlength;
	}
	public void setExplodelpnlength(String explodelpnlength) {
		this.explodelpnlength = explodelpnlength;
	}
	public String getContainerExchangeFlag() {
		return containerExchangeFlag;
	}
	public void setContainerExchangeFlag(String containerExchangeFlag) {
		this.containerExchangeFlag = containerExchangeFlag;
	}
	public String getCartonizeftdflt() {
		return cartonizeftdflt;
	}
	public void setCartonizeftdflt(String cartonizeftdflt) {
		this.cartonizeftdflt = cartonizeftdflt;
	}
	public String getDefftlabelprint() {
		return defftlabelprint;
	}
	public void setDefftlabelprint(String defftlabelprint) {
		this.defftlabelprint = defftlabelprint;
	}
	public String getDeffttaskcontrol() {
		return deffttaskcontrol;
	}
	public void setDeffttaskcontrol(String deffttaskcontrol) {
		this.deffttaskcontrol = deffttaskcontrol;
	}
	
	//SRG 9.2 Upgrade -- Begin
	public String getAddress5() {
		return address5;
	}
	public void setAddress5(String address5) {
		this.address5 = address5;
	}
	public String getAddress6() {
		return address6;
	}
	public void setAddress6(String address6) {
		this.address6 = address6;
	}
	public String getAddressoverwriteindicator() {
		return addressoverwriteindicator;
	}
	public void setAddressoverwriteindicator(String addressoverwriteindicator) {
		this.addressoverwriteindicator = addressoverwriteindicator;
	}
	public String getArcorp() {
		return arcorp;
	}
	public void setArcorp(String arcorp) {
		this.arcorp = arcorp;
	}
	public String getArdept() {
		return ardept;
	}
	public void setArdept(String ardept) {
		this.ardept = ardept;
	}
	public String getAracct() {
		return aracct;
	}
	public void setAracct(String aracct) {
		this.aracct = aracct;
	}
	public String getMeasurecode() {
		return measurecode;
	}
	public void setMeasurecode(String measurecode) {
		this.measurecode = measurecode;
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
	public String getCurrcode() {
		return currcode;
	}
	public void setCurrcode(String currcode) {
		this.currcode = currcode;
	}
	public String getTaxexempt() {
		return taxexempt;
	}
	public void setTaxexempt(String taxexempt) {
		this.taxexempt = taxexempt;
	}
	public String getTaxexemptcode() {
		return taxexemptcode;
	}
	public void setTaxexemptcode(String taxexemptcode) {
		this.taxexemptcode = taxexemptcode;
	}
	public String getRecurcode() {
		return recurcode;
	}
	public void setRecurcode(String recurcode) {
		this.recurcode = recurcode;
	}
	public String getDunsid() {
		return dunsid;
	}
	public void setDunsid(String dunsid) {
		this.dunsid = dunsid;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String getQfsurcharge() {
		return qfsurcharge;
	}
	public void setQfsurcharge(String qfsurcharge) {
		this.qfsurcharge = qfsurcharge;
	}
	public String getBfsurcharge() {
		return bfsurcharge;
	}
	public void setBfsurcharge(String bfsurcharge) {
		this.bfsurcharge = bfsurcharge;
	}
	public String getInvoiceterms() {
		return invoiceterms;
	}
	public void setInvoiceterms(String invoiceterms) {
		this.invoiceterms = invoiceterms;
	}
	public String getInvoicelevel() {
		return invoicelevel;
	}
	public void setInvoicelevel(String invoicelevel) {
		this.invoicelevel = invoicelevel;
	}
	public String getNonneglevel() {
		return nonneglevel;
	}
	public void setNonneglevel(String nonneglevel) {
		this.nonneglevel = nonneglevel;
	}
	public String getAmstrategykey() {
		return amstrategykey;
	}
	public void setAmstrategykey(String amstrategykey) {
		this.amstrategykey = amstrategykey;
	}
	public String getSpsaccountnum() {
		return spsaccountnum;
	}
	public void setSpsaccountnum(String spsaccountnum) {
		this.spsaccountnum = spsaccountnum;
	}
	public String getSpscostcenter() {
		return spscostcenter;
	}
	public void setSpscostcenter(String spscostcenter) {
		this.spscostcenter = spscostcenter;
	}
	public String getSpsreturnlabel() {
		return spsreturnlabel;
	}
	public void setSpsreturnlabel(String spsreturnlabel) {
		this.spsreturnlabel = spsreturnlabel;
	}
	public String getOwnerprefix() {
		return ownerprefix;
	}
	public void setOwnerprefix(String ownerprefix) {
		this.ownerprefix = ownerprefix;
	}
	public String getExplodenextlpnnumber() {
		return explodenextlpnnumber;
	}
	public void setExplodenextlpnnumber(String explodenextlpnnumber) {
		this.explodenextlpnnumber = explodenextlpnnumber;
	}
	public String getExplodelpnrollbacknumber() {
		return explodelpnrollbacknumber;
	}
	public void setExplodelpnrollbacknumber(String explodelpnrollbacknumber) {
		this.explodelpnrollbacknumber = explodelpnrollbacknumber;
	}
	public String getExplodelpnstartnumber() {
		return explodelpnstartnumber;
	}
	public void setExplodelpnstartnumber(String explodelpnstartnumber) {
		this.explodelpnstartnumber = explodelpnstartnumber;
	}
	public String getAccountingentity() {
		return accountingentity;
	}
	public void setAccountingentity(String accountingentity) {
		this.accountingentity = accountingentity;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	//SRG 9.2 Upgrade -- End

	
}


