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
package com.ssaglobal.scm.wms.wm_owner.ui;

import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.duplicate.IDuplicate;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBase;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBioType;
import com.ssaglobal.scm.wms.wm_owner.ccf.OwnerLPNStartLimitValidationCCF;

public class PopulateOwner extends PopulateBase  {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateOwner.class);

	public PopulateBioType ValidateAndLoad(DataBean dupDataBean, QBEBioBean newBioBean) {
		// TODO Auto-generated method stub
		PopulateBioType populateBioType = new PopulateBioType();
		populateBioType.setStatus(true);

		try{

			//newBioBean.set("ADDDATE", dupDataBean.getValue("ADDDATE"));
			if (dupDataBean.getValue("ADDRESS1") != null && dupDataBean.getValue("ADDRESS1").toString().trim().length()>0)
				newBioBean.set("ADDRESS1", dupDataBean.getValue("ADDRESS1"));
			
			if (dupDataBean.getValue("ADDRESS2") != null && dupDataBean.getValue("ADDRESS2").toString().trim().length()>0)
				newBioBean.set("ADDRESS2", dupDataBean.getValue("ADDRESS2"));
			
			if (dupDataBean.getValue("ADDRESS3") != null && dupDataBean.getValue("ADDRESS3").toString().trim().length()>0)
				newBioBean.set("ADDRESS3", dupDataBean.getValue("ADDRESS3"));
			
			if (dupDataBean.getValue("ADDRESS4") != null && dupDataBean.getValue("ADDRESS4").toString().trim().length()>0)
				newBioBean.set("ADDRESS4", dupDataBean.getValue("ADDRESS4"));
			
			if (dupDataBean.getValue("ALLOWAUTOCLOSEFORASN") != null && dupDataBean.getValue("ALLOWAUTOCLOSEFORASN").toString().trim().length()>0)
				newBioBean.set("ALLOWAUTOCLOSEFORASN", dupDataBean.getValue("ALLOWAUTOCLOSEFORASN"));
			
			if (dupDataBean.getValue("ALLOWAUTOCLOSEFORPO") != null && dupDataBean.getValue("ALLOWAUTOCLOSEFORPO").toString().trim().length()>0)
				newBioBean.set("ALLOWAUTOCLOSEFORPO", dupDataBean.getValue("ALLOWAUTOCLOSEFORPO"));
			
			if (dupDataBean.getValue("ALLOWAUTOCLOSEFORPS") != null && dupDataBean.getValue("ALLOWAUTOCLOSEFORPS").toString().trim().length()>0)
				newBioBean.set("ALLOWAUTOCLOSEFORPS", dupDataBean.getValue("ALLOWAUTOCLOSEFORPS"));
			
			if (dupDataBean.getValue("ALLOWCOMMINGLEDLPN") != null && dupDataBean.getValue("ALLOWCOMMINGLEDLPN").toString().trim().length()>0)
				newBioBean.set("ALLOWCOMMINGLEDLPN", dupDataBean.getValue("ALLOWCOMMINGLEDLPN"));
			
			if (dupDataBean.getValue("ALLOWDUPLICATELICENSEPLATES") != null && dupDataBean.getValue("ALLOWDUPLICATELICENSEPLATES").toString().trim().length()>0)
				newBioBean.set("ALLOWDUPLICATELICENSEPLATES", dupDataBean.getValue("ALLOWDUPLICATELICENSEPLATES"));
			
			if (dupDataBean.getValue("ALLOWOVERSHIPMENT") != null && dupDataBean.getValue("ALLOWOVERSHIPMENT").toString().trim().length()>0)
				newBioBean.set("ALLOWOVERSHIPMENT", dupDataBean.getValue("ALLOWOVERSHIPMENT"));
			
			if (dupDataBean.getValue("ALLOWSINGLESCANRECEIVING") != null && dupDataBean.getValue("ALLOWSINGLESCANRECEIVING").toString().trim().length()>0)
				newBioBean.set("ALLOWSINGLESCANRECEIVING", dupDataBean.getValue("ALLOWSINGLESCANRECEIVING"));
			
			if (dupDataBean.getValue("ALLOWSYSTEMGENERATEDLPN") != null && dupDataBean.getValue("ALLOWSYSTEMGENERATEDLPN").toString().trim().length()>0)
				newBioBean.set("ALLOWSYSTEMGENERATEDLPN", dupDataBean.getValue("ALLOWSYSTEMGENERATEDLPN"));
			
			if (dupDataBean.getValue("APPLICATIONID") != null && dupDataBean.getValue("APPLICATIONID").toString().trim().length()>0)
				newBioBean.set("APPLICATIONID", dupDataBean.getValue("APPLICATIONID"));
			
			if (dupDataBean.getValue("APPORTIONRULE") != null && dupDataBean.getValue("APPORTIONRULE").toString().trim().length()>0)
				newBioBean.set("APPORTIONRULE", dupDataBean.getValue("APPORTIONRULE"));
			
			if (dupDataBean.getValue("AUTOCLOSEASN") != null && dupDataBean.getValue("AUTOCLOSEASN").toString().trim().length()>0)
				newBioBean.set("AUTOCLOSEASN", dupDataBean.getValue("AUTOCLOSEASN"));
			
			if (dupDataBean.getValue("AUTOCLOSEPO") != null && dupDataBean.getValue("AUTOCLOSEPO").toString().trim().length()>0)
				newBioBean.set("AUTOCLOSEPO", dupDataBean.getValue("AUTOCLOSEPO"));
			
			if (dupDataBean.getValue("AUTOPRINTLABELLPN") != null && dupDataBean.getValue("AUTOPRINTLABELLPN").toString().trim().length()>0)
				newBioBean.set("AUTOPRINTLABELLPN", dupDataBean.getValue("AUTOPRINTLABELLPN"));
			
			if (dupDataBean.getValue("AUTOPRINTLABELPUTAWAY") != null && dupDataBean.getValue("AUTOPRINTLABELPUTAWAY").toString().trim().length()>0)
				newBioBean.set("AUTOPRINTLABELPUTAWAY", dupDataBean.getValue("AUTOPRINTLABELPUTAWAY"));
			
			if (dupDataBean.getValue("B_ADDRESS1") != null && dupDataBean.getValue("B_ADDRESS1").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS1", dupDataBean.getValue("B_ADDRESS1"));
			
			if (dupDataBean.getValue("B_ADDRESS2") != null && dupDataBean.getValue("B_ADDRESS2").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS2", dupDataBean.getValue("B_ADDRESS2"));
			
			if (dupDataBean.getValue("B_ADDRESS3") != null && dupDataBean.getValue("B_ADDRESS3").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS3", dupDataBean.getValue("B_ADDRESS3"));
			
			if (dupDataBean.getValue("B_ADDRESS4") != null && dupDataBean.getValue("B_ADDRESS4").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS4", dupDataBean.getValue("B_ADDRESS4"));
			
			if (dupDataBean.getValue("B_CITY") != null && dupDataBean.getValue("B_CITY").toString().trim().length()>0)
				newBioBean.set("B_CITY", dupDataBean.getValue("B_CITY"));
			
			if (dupDataBean.getValue("B_COMPANY") != null && dupDataBean.getValue("B_COMPANY").toString().trim().length()>0)
				newBioBean.set("B_COMPANY", dupDataBean.getValue("B_COMPANY"));
			
			if (dupDataBean.getValue("B_CONTACT1") != null && dupDataBean.getValue("B_CONTACT1").toString().trim().length()>0)
				newBioBean.set("B_CONTACT1", dupDataBean.getValue("B_CONTACT1"));
			
			if (dupDataBean.getValue("B_CONTACT2") != null && dupDataBean.getValue("B_CONTACT2").toString().trim().length()>0)
				newBioBean.set("B_CONTACT2", dupDataBean.getValue("B_CONTACT2"));
			
			if (dupDataBean.getValue("B_COUNTRY") != null && dupDataBean.getValue("B_COUNTRY").toString().trim().length()>0)
				newBioBean.set("B_COUNTRY", dupDataBean.getValue("B_COUNTRY"));
			
			if (dupDataBean.getValue("B_EMAIL1") != null && dupDataBean.getValue("B_EMAIL1").toString().trim().length()>0)
				newBioBean.set("B_EMAIL1", dupDataBean.getValue("B_EMAIL1"));
			
			if (dupDataBean.getValue("B_EMAIL2") != null && dupDataBean.getValue("B_EMAIL2").toString().trim().length()>0)
				newBioBean.set("B_EMAIL2", dupDataBean.getValue("B_EMAIL2"));
			
			if (dupDataBean.getValue("B_FAX1") != null && dupDataBean.getValue("B_FAX1").toString().trim().length()>0)
				newBioBean.set("B_FAX1", dupDataBean.getValue("B_FAX1"));
			
			if (dupDataBean.getValue("B_FAX2") != null && dupDataBean.getValue("B_FAX2").toString().trim().length()>0)
				newBioBean.set("B_FAX2", dupDataBean.getValue("B_FAX2"));
			
			if (dupDataBean.getValue("B_ISOCNTRYCODE") != null && dupDataBean.getValue("B_ISOCNTRYCODE").toString().trim().length()>0)
				newBioBean.set("B_ISOCNTRYCODE", dupDataBean.getValue("B_ISOCNTRYCODE"));
			
			if (dupDataBean.getValue("B_PHONE1") != null && dupDataBean.getValue("B_PHONE1").toString().trim().length()>0)
				newBioBean.set("B_PHONE1", dupDataBean.getValue("B_PHONE1"));
			
			if (dupDataBean.getValue("B_PHONE2") != null && dupDataBean.getValue("B_PHONE2").toString().trim().length()>0)
				newBioBean.set("B_PHONE2", dupDataBean.getValue("B_PHONE2"));
			
			if (dupDataBean.getValue("B_STATE") != null && dupDataBean.getValue("B_STATE").toString().trim().length()>0)
				newBioBean.set("B_STATE", dupDataBean.getValue("B_STATE"));

			if (dupDataBean.getValue("B_ZIP") != null && dupDataBean.getValue("B_ZIP").toString().trim().length()>0)
				newBioBean.set("B_ZIP", dupDataBean.getValue("B_ZIP"));

			if (dupDataBean.getValue("BARCODECONFIGKEY") != null && dupDataBean.getValue("BARCODECONFIGKEY").toString().trim().length()>0)
				newBioBean.set("BARCODECONFIGKEY", dupDataBean.getValue("BARCODECONFIGKEY"));
			
			if (dupDataBean.getValue("CALCULATEPUTAWAYLOCATION") != null && dupDataBean.getValue("CALCULATEPUTAWAYLOCATION").toString().trim().length()>0)
				newBioBean.set("CALCULATEPUTAWAYLOCATION", dupDataBean.getValue("CALCULATEPUTAWAYLOCATION"));
			
			if (dupDataBean.getValue("CARTONGROUP") != null && dupDataBean.getValue("CARTONGROUP").toString().trim().length()>0)
				newBioBean.set("CARTONGROUP", dupDataBean.getValue("CARTONGROUP"));
			
			if (dupDataBean.getValue("CaseLabelType") != null && dupDataBean.getValue("CaseLabelType").toString().trim().length()>0)
				newBioBean.set("CaseLabelType", dupDataBean.getValue("CaseLabelType"));
			
			if (dupDataBean.getValue("CCADJBYRF") != null && dupDataBean.getValue("CCADJBYRF").toString().trim().length()>0)
				newBioBean.set("CCADJBYRF", dupDataBean.getValue("CCADJBYRF"));
			
			if (dupDataBean.getValue("CCDISCREPANCYRULE") != null && dupDataBean.getValue("CCDISCREPANCYRULE").toString().trim().length()>0)
				newBioBean.set("CCDISCREPANCYRULE", dupDataBean.getValue("CCDISCREPANCYRULE"));
			
			if (dupDataBean.getValue("CCSKUXLOC") != null && dupDataBean.getValue("CCSKUXLOC").toString().trim().length()>0)
				newBioBean.set("CCSKUXLOC", dupDataBean.getValue("CCSKUXLOC"));
			
			if (dupDataBean.getValue("CITY") != null && dupDataBean.getValue("CITY").toString().trim().length()>0)
				newBioBean.set("CITY", dupDataBean.getValue("CITY"));
			
			/**
			if (dupDataBean.getValue("COMPANY") != null && dupDataBean.getValue("COMPANY").toString().trim().length()>0)
				newBioBean.set("COMPANY", dupDataBean.getValue("COMPANY"));
			**/
			
			if (dupDataBean.getValue("CONTACT1") != null && dupDataBean.getValue("CONTACT1").toString().trim().length()>0)
				newBioBean.set("CONTACT1", dupDataBean.getValue("CONTACT1"));
			
			if (dupDataBean.getValue("CONTACT2") != null && dupDataBean.getValue("CONTACT2").toString().trim().length()>0)
				newBioBean.set("CONTACT2", dupDataBean.getValue("CONTACT2"));
			
			if (dupDataBean.getValue("COUNTRY") != null && dupDataBean.getValue("COUNTRY").toString().trim().length()>0)
				newBioBean.set("COUNTRY", dupDataBean.getValue("COUNTRY"));
			
			if (dupDataBean.getValue("CREATEPATASKONRFRECEIPT") != null && dupDataBean.getValue("CREATEPATASKONRFRECEIPT").toString().trim().length()>0)
				newBioBean.set("CREATEPATASKONRFRECEIPT", dupDataBean.getValue("CREATEPATASKONRFRECEIPT"));
			
			if (dupDataBean.getValue("CREDITLIMIT") != null && dupDataBean.getValue("CREDITLIMIT").toString().trim().length()>0)
				newBioBean.set("CREDITLIMIT", dupDataBean.getValue("CREDITLIMIT"));
			
			if (dupDataBean.getValue("CWOFLAG") != null && dupDataBean.getValue("CWOFLAG").toString().trim().length()>0)
				newBioBean.set("CWOFLAG", dupDataBean.getValue("CWOFLAG"));
			
			if (dupDataBean.getValue("DEFAULTPACKINGLOCATION") != null && dupDataBean.getValue("DEFAULTPACKINGLOCATION").toString().trim().length()>0)
				newBioBean.set("DEFAULTPACKINGLOCATION", dupDataBean.getValue("DEFAULTPACKINGLOCATION"));
			
			if (dupDataBean.getValue("DEFAULTPUTAWAYSTRATEGY") != null && dupDataBean.getValue("DEFAULTPUTAWAYSTRATEGY").toString().trim().length()>0)
				newBioBean.set("DEFAULTPUTAWAYSTRATEGY", dupDataBean.getValue("DEFAULTPUTAWAYSTRATEGY"));
			
			if (dupDataBean.getValue("DEFAULTQCLOC") != null && dupDataBean.getValue("DEFAULTQCLOC").toString().trim().length()>0)
				newBioBean.set("DEFAULTQCLOC", dupDataBean.getValue("DEFAULTQCLOC"));
			
			if (dupDataBean.getValue("DEFAULTQCLOCOUT") != null && dupDataBean.getValue("DEFAULTQCLOCOUT").toString().trim().length()>0)
				newBioBean.set("DEFAULTQCLOCOUT", dupDataBean.getValue("DEFAULTQCLOCOUT"));
			
			if (dupDataBean.getValue("DEFAULTRETURNSLOC") != null && dupDataBean.getValue("DEFAULTRETURNSLOC").toString().trim().length()>0)
				newBioBean.set("DEFAULTRETURNSLOC", dupDataBean.getValue("DEFAULTRETURNSLOC"));
			
			if (dupDataBean.getValue("DEFAULTROTATION") != null && dupDataBean.getValue("DEFAULTROTATION").toString().trim().length()>0)
				newBioBean.set("DEFAULTROTATION", dupDataBean.getValue("DEFAULTROTATION"));
			
			if (dupDataBean.getValue("DEFAULTSKUROTATION") != null && dupDataBean.getValue("DEFAULTSKUROTATION").toString().trim().length()>0)
				newBioBean.set("DEFAULTSKUROTATION", dupDataBean.getValue("DEFAULTSKUROTATION"));
			
			if (dupDataBean.getValue("DEFAULTSTRATEGY") != null && dupDataBean.getValue("DEFAULTSTRATEGY").toString().trim().length()>0)
				newBioBean.set("DEFAULTSTRATEGY", dupDataBean.getValue("DEFAULTSTRATEGY"));
			
			if (dupDataBean.getValue("DESCRIPTION") != null && dupDataBean.getValue("DESCRIPTION").toString().trim().length()>0)
				newBioBean.set("DESCRIPTION", dupDataBean.getValue("DESCRIPTION"));
			
			if (dupDataBean.getValue("DUPCASEID") != null && dupDataBean.getValue("DUPCASEID").toString().trim().length()>0)
				newBioBean.set("DUPCASEID", dupDataBean.getValue("DUPCASEID"));
			
			/**
			if (dupDataBean.getValue("EDITDATE") != null && dupDataBean.getValue("EDITDATE").toString().trim().length()>0)
				newBioBean.set("EDITDATE", dupDataBean.getValue("EDITDATE"));
			if (dupDataBean.getValue("EDITWHO") != null && dupDataBean.getValue("EDITWHO").toString().trim().length()>0)
				newBioBean.set("EDITWHO", dupDataBean.getValue("EDITWHO"));
			**/
			
			if (dupDataBean.getValue("EMAIL1") != null && dupDataBean.getValue("EMAIL1").toString().trim().length()>0)
				newBioBean.set("EMAIL1", dupDataBean.getValue("EMAIL1"));
			
			if (dupDataBean.getValue("EMAIL2") != null && dupDataBean.getValue("EMAIL2").toString().trim().length()>0)
				newBioBean.set("EMAIL2", dupDataBean.getValue("EMAIL2"));
			
			if (dupDataBean.getValue("ENABLEOPPXDOCK") != null && dupDataBean.getValue("ENABLEOPPXDOCK").toString().trim().length()>0)
				newBioBean.set("ENABLEOPPXDOCK", dupDataBean.getValue("ENABLEOPPXDOCK"));
			
			if (dupDataBean.getValue("ENABLEPACKINGDEFAULT") != null && dupDataBean.getValue("ENABLEPACKINGDEFAULT").toString().trim().length()>0)
				newBioBean.set("ENABLEPACKINGDEFAULT", dupDataBean.getValue("ENABLEPACKINGDEFAULT"));
			
			if (dupDataBean.getValue("FAX1") != null && dupDataBean.getValue("FAX1").toString().trim().length()>0)
				newBioBean.set("FAX1", dupDataBean.getValue("FAX1"));
			
			if (dupDataBean.getValue("FAX2") != null && dupDataBean.getValue("FAX2").toString().trim().length()>0)
				newBioBean.set("FAX2", dupDataBean.getValue("FAX2"));
			
			if (dupDataBean.getValue("GENERATEPACKLIST") != null && dupDataBean.getValue("GENERATEPACKLIST").toString().trim().length()>0)
				newBioBean.set("GENERATEPACKLIST", dupDataBean.getValue("GENERATEPACKLIST"));
			
			if (dupDataBean.getValue("INSPECTATPACK") != null && dupDataBean.getValue("INSPECTATPACK").toString().trim().length()>0)
				newBioBean.set("INSPECTATPACK", dupDataBean.getValue("INSPECTATPACK"));
			
			if (dupDataBean.getValue("ISOCNTRYCODE") != null && dupDataBean.getValue("ISOCNTRYCODE").toString().trim().length()>0)
				newBioBean.set("ISOCNTRYCODE", dupDataBean.getValue("ISOCNTRYCODE"));
			
			if (dupDataBean.getValue("LPNBARCODEFORMAT") != null && dupDataBean.getValue("LPNBARCODEFORMAT").toString().trim().length()>0)
				newBioBean.set("LPNBARCODEFORMAT", dupDataBean.getValue("LPNBARCODEFORMAT"));
			
			if (dupDataBean.getValue("LPNBARCODESYMBOLOGY") != null && dupDataBean.getValue("LPNBARCODESYMBOLOGY").toString().trim().length()>0)
				newBioBean.set("LPNBARCODESYMBOLOGY", dupDataBean.getValue("LPNBARCODESYMBOLOGY"));

			if (dupDataBean.getValue("LPNLENGTH") != null )
				newBioBean.set("LPNLENGTH", dupDataBean.getValue("LPNLENGTH"));
			
			if (dupDataBean.getValue("LPNROLLBACKNUMBER") != null && dupDataBean.getValue("LPNROLLBACKNUMBER").toString().trim().length()>0)
				newBioBean.set("LPNROLLBACKNUMBER", dupDataBean.getValue("LPNROLLBACKNUMBER"));
			
			if (dupDataBean.getValue("LPNSTARTNUMBER") != null && dupDataBean.getValue("LPNSTARTNUMBER").toString().trim().length()>0)
				newBioBean.set("LPNSTARTNUMBER", dupDataBean.getValue("LPNSTARTNUMBER"));
			
			if (dupDataBean.getValue("MAXIMUMORDERS") != null)
				newBioBean.set("MAXIMUMORDERS", dupDataBean.getValue("MAXIMUMORDERS"));
			
			if (dupDataBean.getValue("MINIMUMPERCENT") != null )
				newBioBean.set("MINIMUMPERCENT", dupDataBean.getValue("MINIMUMPERCENT"));
			
			if (dupDataBean.getValue("MULTIZONEPLPA") != null && dupDataBean.getValue("MULTIZONEPLPA").toString().trim().length()>0)
				newBioBean.set("MULTIZONEPLPA", dupDataBean.getValue("MULTIZONEPLPA"));
			
			if (dupDataBean.getValue("NEXTLPNNUMBER") != null && dupDataBean.getValue("NEXTLPNNUMBER").toString().trim().length()>0)
				newBioBean.set("NEXTLPNNUMBER", dupDataBean.getValue("NEXTLPNNUMBER"));
			
			if (dupDataBean.getValue("NOTES1") != null && dupDataBean.getValue("NOTES1").toString().trim().length()>0)
				newBioBean.set("NOTES1", dupDataBean.getValue("NOTES1"));
			
			if (dupDataBean.getValue("NOTES2") != null && dupDataBean.getValue("NOTES2").toString().trim().length()>0)
				newBioBean.set("NOTES2", dupDataBean.getValue("NOTES2"));
			
			if (dupDataBean.getValue("OPPORDERSTRATEGYKEY") != null && dupDataBean.getValue("OPPORDERSTRATEGYKEY").toString().trim().length()>0)
				newBioBean.set("OPPORDERSTRATEGYKEY", dupDataBean.getValue("OPPORDERSTRATEGYKEY"));
			
			if (dupDataBean.getValue("ORDERBREAKDEFAULT") != null && dupDataBean.getValue("ORDERBREAKDEFAULT").toString().trim().length()>0)
				newBioBean.set("ORDERBREAKDEFAULT", dupDataBean.getValue("ORDERBREAKDEFAULT"));
			
			if (dupDataBean.getValue("ORDERDATEENDDAYS") != null )
				newBioBean.set("ORDERDATEENDDAYS", dupDataBean.getValue("ORDERDATEENDDAYS"));
			
			if (dupDataBean.getValue("ORDERDATESTARTDAYS") != null )
				newBioBean.set("ORDERDATESTARTDAYS", dupDataBean.getValue("ORDERDATESTARTDAYS"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT01") != null && dupDataBean.getValue("ORDERTYPERESTRICT01").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT01", dupDataBean.getValue("ORDERTYPERESTRICT01"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT02") != null && dupDataBean.getValue("ORDERTYPERESTRICT02").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT02", dupDataBean.getValue("ORDERTYPERESTRICT02"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT03") != null && dupDataBean.getValue("ORDERTYPERESTRICT03").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT03", dupDataBean.getValue("ORDERTYPERESTRICT03"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT04") != null && dupDataBean.getValue("ORDERTYPERESTRICT04").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT04", dupDataBean.getValue("ORDERTYPERESTRICT04"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT05") != null && dupDataBean.getValue("ORDERTYPERESTRICT05").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT05", dupDataBean.getValue("ORDERTYPERESTRICT05"));
			
			if (dupDataBean.getValue("ORDERTYPERESTRICT06") != null && dupDataBean.getValue("ORDERTYPERESTRICT06").toString().trim().length()>0)
				newBioBean.set("ORDERTYPERESTRICT06", dupDataBean.getValue("ORDERTYPERESTRICT06"));
			
			if (dupDataBean.getValue("PACKINGVALIDATIONTEMPLATE") != null && dupDataBean.getValue("PACKINGVALIDATIONTEMPLATE").toString().trim().length()>0)
				newBioBean.set("PACKINGVALIDATIONTEMPLATE", dupDataBean.getValue("PACKINGVALIDATIONTEMPLATE"));
			
			if (dupDataBean.getValue("PHONE1") != null && dupDataBean.getValue("PHONE1").toString().trim().length()>0)
				newBioBean.set("PHONE1", dupDataBean.getValue("PHONE1"));
			
			if (dupDataBean.getValue("PHONE2") != null && dupDataBean.getValue("PHONE2").toString().trim().length()>0)
				newBioBean.set("PHONE2", dupDataBean.getValue("PHONE2"));
			
			if (dupDataBean.getValue("PICKCODE") != null && dupDataBean.getValue("PICKCODE").toString().trim().length()>0)
				newBioBean.set("PICKCODE", dupDataBean.getValue("PICKCODE"));
			
			if (dupDataBean.getValue("PISKUXLOC") != null && dupDataBean.getValue("PISKUXLOC").toString().trim().length()>0)
				newBioBean.set("PISKUXLOC", dupDataBean.getValue("PISKUXLOC"));
			
			if (dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE") != null && dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE").toString().trim().length()>0)
				newBioBean.set("RECEIPTVALIDATIONTEMPLATE", dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE"));
			
			if (dupDataBean.getValue("ROLLRECEIPT") != null && dupDataBean.getValue("ROLLRECEIPT").toString().trim().length()>0)
				newBioBean.set("ROLLRECEIPT", dupDataBean.getValue("ROLLRECEIPT"));
			
			if (dupDataBean.getValue("SCAC_CODE") != null && dupDataBean.getValue("SCAC_CODE").toString().trim().length()>0)
				newBioBean.set("SCAC_CODE", dupDataBean.getValue("SCAC_CODE"));
			
			/**
			if (dupDataBean.getValue("SERIALKEY") != null && dupDataBean.getValue("SERIALKEY").toString().trim().length()>0)
				newBioBean.set("SERIALKEY", dupDataBean.getValue("SERIALKEY"));
			**/
			
			if (dupDataBean.getValue("SKUSETUPREQUIRED") != null && dupDataBean.getValue("SKUSETUPREQUIRED").toString().trim().length()>0)
				newBioBean.set("SKUSETUPREQUIRED", dupDataBean.getValue("SKUSETUPREQUIRED"));
			
			if (dupDataBean.getValue("SSCC1STDIGIT") != null )
				newBioBean.set("SSCC1STDIGIT", dupDataBean.getValue("SSCC1STDIGIT"));
			
			if (dupDataBean.getValue("STATE") != null && dupDataBean.getValue("STATE").toString().trim().length()>0)
				newBioBean.set("STATE", dupDataBean.getValue("STATE"));
			
			if (dupDataBean.getValue("STATUS") != null && dupDataBean.getValue("STATUS").toString().trim().length()>0)
				newBioBean.set("STATUS", dupDataBean.getValue("STATUS"));
			
			/**
			if (dupDataBean.getValue("STORERKEY") != null && dupDataBean.getValue("STORERKEY").toString().trim().length()>0)
				newBioBean.set("STORERKEY", dupDataBean.getValue("STORERKEY"));
			**/
			
			if (dupDataBean.getValue("SUSR1") != null && dupDataBean.getValue("SUSR1").toString().trim().length()>0)
				newBioBean.set("SUSR1", dupDataBean.getValue("SUSR1"));
			
			if (dupDataBean.getValue("SUSR2") != null && dupDataBean.getValue("SUSR2").toString().trim().length()>0)
				newBioBean.set("SUSR2", dupDataBean.getValue("SUSR2"));
			
			if (dupDataBean.getValue("SUSR3") != null && dupDataBean.getValue("SUSR3").toString().trim().length()>0)
				newBioBean.set("SUSR3", dupDataBean.getValue("SUSR3"));
			
			if (dupDataBean.getValue("SUSR4") != null && dupDataBean.getValue("SUSR4").toString().trim().length()>0)
				newBioBean.set("SUSR4", dupDataBean.getValue("SUSR4"));
			
			if (dupDataBean.getValue("SUSR5") != null && dupDataBean.getValue("SUSR5").toString().trim().length()>0)
				newBioBean.set("SUSR5", dupDataBean.getValue("SUSR5"));
			
			if (dupDataBean.getValue("SUSR6") != null && dupDataBean.getValue("SUSR6").toString().trim().length()>0)
				newBioBean.set("SUSR6", dupDataBean.getValue("SUSR6"));
			
			if (dupDataBean.getValue("TITLE1") != null && dupDataBean.getValue("TITLE1").toString().trim().length()>0)
				newBioBean.set("TITLE1", dupDataBean.getValue("TITLE1"));
			
			if (dupDataBean.getValue("TITLE2") != null && dupDataBean.getValue("TITLE2").toString().trim().length()>0)
				newBioBean.set("TITLE2", dupDataBean.getValue("TITLE2"));
			
			if (dupDataBean.getValue("TRACKINVENTORYBY") != null && dupDataBean.getValue("TRACKINVENTORYBY").toString().trim().length()>0)
				newBioBean.set("TRACKINVENTORYBY", dupDataBean.getValue("TRACKINVENTORYBY"));
			
			if (dupDataBean.getValue("TYPE") != null && dupDataBean.getValue("TYPE").toString().trim().length()>0)
				newBioBean.set("TYPE", dupDataBean.getValue("TYPE"));
			
			if (dupDataBean.getValue("UCCVENDORNUMBER") != null && dupDataBean.getValue("UCCVENDORNUMBER").toString().trim().length()>0)
				newBioBean.set("UCCVENDORNUMBER", dupDataBean.getValue("UCCVENDORNUMBER"));
			
			if (dupDataBean.getValue("VAT") != null && dupDataBean.getValue("VAT").toString().trim().length()>0)
				newBioBean.set("VAT", dupDataBean.getValue("VAT"));

			//10/14/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent4000422_Defect284242) -- Start
			/*
			if (dupDataBean.getValue("WHSEID") != null && dupDataBean.getValue("WHSEID").toString().trim().length()>0)
				newBioBean.set("WHSEID", dupDataBean.getValue("WHSEID"));
			*/
			//10/14/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent4000422_Defect284242) -- End
						
			if (dupDataBean.getValue("ZIP") != null && dupDataBean.getValue("ZIP").toString().trim().length()>0)
				newBioBean.set("ZIP", dupDataBean.getValue("ZIP"));
			
		}catch(Exception e){
			
			_log.debug("LOG_SYSTEM_OUT","Error populating Owner."+e.getMessage(),100L);
			e.printStackTrace();
			populateBioType.setStatus(false);
		}
		
		populateBioType.setqbeBioBean(newBioBean);
		return populateBioType;
	}

	
}
