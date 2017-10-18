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
package com.ssaglobal.scm.wms.wm_item.ui;


import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBase;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBioType;

public class PopulateItem extends PopulateBase {
	/**
	* 	Krishna Kuchipudi: 29-Jan-2010: 3PL Enhancements -Added the code for 3PL Advanced Catch Weight functionality.
	*   Now when user clicks duplicate the Advanced catch weight fields also will be copied.
	*/
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateItem.class);

	public PopulateBioType ValidateAndLoad(DataBean dupDataBean,
			QBEBioBean newBioBean) {
		// TODO Auto-generated method stub
		//Krishna Kuchipudi: 3PL Enhancements -Added the below code for 3PL Advanced Catch Weight functionality 

		PopulateBioType populateBioType = new PopulateBioType();
		populateBioType.setStatus(true);

		try{

			if (dupDataBean.getValue("ABC")!=null && dupDataBean.getValue("ABC").toString().trim().length()>0)
				newBioBean.set("ABC", dupDataBean.getValue("ABC"));
			
			if (dupDataBean.getValue("ACTIVE")!=null && dupDataBean.getValue("ACTIVE").toString().trim().length()>0)
				newBioBean.set("ACTIVE", dupDataBean.getValue("ACTIVE"));
			
			if (dupDataBean.getValue("ALLOWCONSOLIDATION")!=null && dupDataBean.getValue("ALLOWCONSOLIDATION").toString().trim().length()>0)
				newBioBean.set("ALLOWCONSOLIDATION", dupDataBean.getValue("ALLOWCONSOLIDATION"));
			
			if (dupDataBean.getValue("ALTSKU")!=null && dupDataBean.getValue("ALTSKU").toString().trim().length()>0)
				newBioBean.set("ALTSKU", dupDataBean.getValue("ALTSKU"));
			
			if (dupDataBean.getValue("AVGCASEWEIGHT")!=null)
				newBioBean.set("AVGCASEWEIGHT", dupDataBean.getValue("AVGCASEWEIGHT"));
			
			if (dupDataBean.getValue("BULKCARTONGROUP")!=null && dupDataBean.getValue("BULKCARTONGROUP").toString().trim().length()>0)
				newBioBean.set("BULKCARTONGROUP", dupDataBean.getValue("BULKCARTONGROUP"));
			
			if (dupDataBean.getValue("BUSR1")!=null && dupDataBean.getValue("BUSR1").toString().trim().length()>0)
				newBioBean.set("BUSR1", dupDataBean.getValue("BUSR1"));
			
			if (dupDataBean.getValue("BUSR10")!=null && dupDataBean.getValue("BUSR10").toString().trim().length()>0)
				newBioBean.set("BUSR10", dupDataBean.getValue("BUSR10"));
			
			if (dupDataBean.getValue("BUSR2")!=null && dupDataBean.getValue("BUSR2").toString().trim().length()>0)
				newBioBean.set("BUSR2", dupDataBean.getValue("BUSR2"));
			
			if (dupDataBean.getValue("BUSR3")!=null && dupDataBean.getValue("BUSR3").toString().trim().length()>0)
				newBioBean.set("BUSR3", dupDataBean.getValue("BUSR3"));
			
			if (dupDataBean.getValue("BUSR4")!=null && dupDataBean.getValue("BUSR4").toString().trim().length()>0)
				newBioBean.set("BUSR4", dupDataBean.getValue("BUSR4"));
			
			if (dupDataBean.getValue("BUSR5")!=null && dupDataBean.getValue("BUSR5").toString().trim().length()>0)
				newBioBean.set("BUSR5", dupDataBean.getValue("BUSR5"));
			
			if (dupDataBean.getValue("BUSR6")!=null && dupDataBean.getValue("BUSR6").toString().trim().length()>0)
				newBioBean.set("BUSR6", dupDataBean.getValue("BUSR6"));
			
			if (dupDataBean.getValue("BUSR7")!=null && dupDataBean.getValue("BUSR7").toString().trim().length()>0)
				newBioBean.set("BUSR7", dupDataBean.getValue("BUSR7"));
			
			if (dupDataBean.getValue("BUSR8")!=null && dupDataBean.getValue("BUSR8").toString().trim().length()>0)
				newBioBean.set("BUSR8", dupDataBean.getValue("BUSR8"));
			
			if (dupDataBean.getValue("BUSR9")!=null && dupDataBean.getValue("BUSR9").toString().trim().length()>0)
				newBioBean.set("BUSR9", dupDataBean.getValue("BUSR9"));
			
			if (dupDataBean.getValue("CARRYCOST")!=null )
				newBioBean.set("CARRYCOST", dupDataBean.getValue("CARRYCOST"));
			
			if (dupDataBean.getValue("CARTONGROUP")!=null && dupDataBean.getValue("CARTONGROUP").toString().trim().length()>0)
				newBioBean.set("CARTONGROUP", dupDataBean.getValue("CARTONGROUP"));
			
			if (dupDataBean.getValue("CASEKEY")!=null && dupDataBean.getValue("CASEKEY").toString().trim().length()>0)
				newBioBean.set("CASEKEY", dupDataBean.getValue("CASEKEY"));
			
			if (dupDataBean.getValue("CCDISCREPANCYRULE")!=null && dupDataBean.getValue("CCDISCREPANCYRULE").toString().trim().length()>0)
				newBioBean.set("CCDISCREPANCYRULE", dupDataBean.getValue("CCDISCREPANCYRULE"));
			
			if (dupDataBean.getValue("CLASS")!=null && dupDataBean.getValue("CLASS").toString().trim().length()>0)
				newBioBean.set("CLASS", dupDataBean.getValue("CLASS"));
			
			if (dupDataBean.getValue("CONVEYABLE")!=null && dupDataBean.getValue("CONVEYABLE").toString().trim().length()>0)
				newBioBean.set("CONVEYABLE", dupDataBean.getValue("CONVEYABLE"));
			
			if (dupDataBean.getValue("COST")!=null )
				newBioBean.set("COST", dupDataBean.getValue("COST"));
			
			if (dupDataBean.getValue("CUBE")!=null )
				newBioBean.set("CUBE", dupDataBean.getValue("CUBE"));
			
			if (dupDataBean.getValue("CWFLAG")!=null && dupDataBean.getValue("CWFLAG").toString().trim().length()>0)
				newBioBean.set("CWFLAG", dupDataBean.getValue("CWFLAG"));
			
			if (dupDataBean.getValue("CYCLECOUNTFREQUENCY")!=null )
				newBioBean.set("CYCLECOUNTFREQUENCY", dupDataBean.getValue("CYCLECOUNTFREQUENCY"));
			
			if (dupDataBean.getValue("DATECODEDAYS")!=null )
				newBioBean.set("DATECODEDAYS", dupDataBean.getValue("DATECODEDAYS"));
			
			if (dupDataBean.getValue("DEFAULTROTATION")!=null && dupDataBean.getValue("DEFAULTROTATION").toString().trim().length()>0)
				newBioBean.set("DEFAULTROTATION", dupDataBean.getValue("DEFAULTROTATION"));
			
			newBioBean.set("DESCR", null);
			
			if (dupDataBean.getValue("EACHKEY")!=null && dupDataBean.getValue("EACHKEY").toString().trim().length()>0)
				newBioBean.set("EACHKEY", dupDataBean.getValue("EACHKEY"));
			
			if (dupDataBean.getValue("EFFECENDDATE")!=null )
				newBioBean.set("EFFECENDDATE", dupDataBean.getValue("EFFECENDDATE"));
			
			if (dupDataBean.getValue("EFFECSTARTDATE")!=null )
				newBioBean.set("EFFECSTARTDATE", dupDataBean.getValue("EFFECSTARTDATE"));
			
			if (dupDataBean.getValue("FLOWTHRUITEM")!=null && dupDataBean.getValue("FLOWTHRUITEM").toString().trim().length()>0)
				newBioBean.set("FLOWTHRUITEM", dupDataBean.getValue("FLOWTHRUITEM"));
			
			if (dupDataBean.getValue("FREIGHTCLASS")!=null && dupDataBean.getValue("FREIGHTCLASS").toString().trim().length()>0)
				newBioBean.set("FREIGHTCLASS", dupDataBean.getValue("FREIGHTCLASS"));
			
			if (dupDataBean.getValue("GROSSWGT")!=null )
				newBioBean.set("GROSSWGT", dupDataBean.getValue("GROSSWGT"));
			
			
			//newBioBean.set("HASIMAGE", dupDataBean.getValue("HASIMAGE"));
			
			if (dupDataBean.getValue("HAZMATCODESKEY")!=null && dupDataBean.getValue("HAZMATCODESKEY").toString().trim().length()>0)
				newBioBean.set("HAZMATCODESKEY", dupDataBean.getValue("HAZMATCODESKEY"));
			
			if (dupDataBean.getValue("ICD1UNIQUE") != null && dupDataBean.getValue("ICD1UNIQUE").toString().trim().length() > 0)
				newBioBean.set("ICD1UNIQUE", dupDataBean.getValue("ICD1UNIQUE"));
			
			if (dupDataBean.getValue("ICDFLAG")!=null && dupDataBean.getValue("ICDFLAG").toString().trim().length()>0)
				newBioBean.set("ICDFLAG", dupDataBean.getValue("ICDFLAG"));
			
			if (dupDataBean.getValue("ICDLABEL1")!=null && dupDataBean.getValue("ICDLABEL1").toString().trim().length()>0)
				newBioBean.set("ICDLABEL1", dupDataBean.getValue("ICDLABEL1"));
			
			if (dupDataBean.getValue("ICDLABEL2")!=null && dupDataBean.getValue("ICDLABEL2").toString().trim().length()>0)
				newBioBean.set("ICDLABEL2", dupDataBean.getValue("ICDLABEL2"));
			
			if (dupDataBean.getValue("ICDLABEL3")!=null && dupDataBean.getValue("ICDLABEL3").toString().trim().length()>0)
				newBioBean.set("ICDLABEL3", dupDataBean.getValue("ICDLABEL3"));
			
			if (dupDataBean.getValue("ICDLABEL4") != null && dupDataBean.getValue("ICDLABEL4").toString().trim().length() > 0)
				newBioBean.set("ICDLABEL4", dupDataBean.getValue("ICDLABEL4"));

			if (dupDataBean.getValue("ICDLABEL5") != null && dupDataBean.getValue("ICDLABEL5").toString().trim().length() > 0)
				newBioBean.set("ICDLABEL5", dupDataBean.getValue("ICDLABEL5"));
			
			if (dupDataBean.getValue("ICWBY")!=null && dupDataBean.getValue("ICWBY").toString().trim().length()>0)
				newBioBean.set("ICWBY", dupDataBean.getValue("ICWBY"));
			
			if (dupDataBean.getValue("ICWFLAG")!=null && dupDataBean.getValue("ICWFLAG").toString().trim().length()>0)
				newBioBean.set("ICWFLAG", dupDataBean.getValue("ICWFLAG"));
			
			if (dupDataBean.getValue("IDEWEIGHT")!=null && dupDataBean.getValue("IDEWEIGHT").toString().trim().length()>0)
				newBioBean.set("IDEWEIGHT", dupDataBean.getValue("IDEWEIGHT"));
			
			if (dupDataBean.getValue("INNERPACK")!=null) 
				newBioBean.set("INNERPACK", dupDataBean.getValue("INNERPACK"));
			
			if (dupDataBean.getValue("IOFLAG")!=null && dupDataBean.getValue("IOFLAG").toString().trim().length()>0)
				newBioBean.set("IOFLAG", dupDataBean.getValue("IOFLAG"));
			
			if (dupDataBean.getValue("ITEMREFERENCE")!=null && dupDataBean.getValue("ITEMREFERENCE").toString().trim().length()>0)
				newBioBean.set("ITEMREFERENCE", dupDataBean.getValue("ITEMREFERENCE"));
			
			if (dupDataBean.getValue("LASTCYCLECOUNT")!=null) 
				newBioBean.set("LASTCYCLECOUNT", dupDataBean.getValue("LASTCYCLECOUNT"));
			
			if (dupDataBean.getValue("LOTTABLE01LABEL")!=null && dupDataBean.getValue("LOTTABLE01LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE01LABEL", dupDataBean.getValue("LOTTABLE01LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE02LABEL")!=null && dupDataBean.getValue("LOTTABLE02LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE02LABEL", dupDataBean.getValue("LOTTABLE02LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE03LABEL")!=null && dupDataBean.getValue("LOTTABLE03LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE03LABEL", dupDataBean.getValue("LOTTABLE03LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE04LABEL")!=null && dupDataBean.getValue("LOTTABLE04LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE04LABEL", dupDataBean.getValue("LOTTABLE04LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE05LABEL")!=null && dupDataBean.getValue("LOTTABLE05LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE05LABEL", dupDataBean.getValue("LOTTABLE05LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE06LABEL")!=null && dupDataBean.getValue("LOTTABLE06LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE06LABEL", dupDataBean.getValue("LOTTABLE06LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE07LABEL")!=null && dupDataBean.getValue("LOTTABLE07LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE07LABEL", dupDataBean.getValue("LOTTABLE07LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE08LABEL")!=null && dupDataBean.getValue("LOTTABLE08LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE08LABEL", dupDataBean.getValue("LOTTABLE08LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE09LABEL")!=null && dupDataBean.getValue("LOTTABLE09LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE09LABEL", dupDataBean.getValue("LOTTABLE09LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE10LABEL")!=null && dupDataBean.getValue("LOTTABLE10LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE10LABEL", dupDataBean.getValue("LOTTABLE10LABEL"));

			if (dupDataBean.getValue("LOTTABLE11LABEL")!=null && dupDataBean.getValue("LOTTABLE11LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE11LABEL", dupDataBean.getValue("LOTTABLE11LABEL"));
			
			if (dupDataBean.getValue("LOTTABLE12LABEL")!=null && dupDataBean.getValue("LOTTABLE12LABEL").toString().trim().length()>0)
				newBioBean.set("LOTTABLE12LABEL", dupDataBean.getValue("LOTTABLE12LABEL"));
			
			if (dupDataBean.getValue("LOTTABLEVALIDATIONKEY")!=null && dupDataBean.getValue("LOTTABLEVALIDATIONKEY").toString().trim().length()>0)
				newBioBean.set("LOTTABLEVALIDATIONKEY", dupDataBean.getValue("LOTTABLEVALIDATIONKEY"));
			
			if (dupDataBean.getValue("LOTXIDDETAILOTHERLABEL1")!=null && dupDataBean.getValue("LOTXIDDETAILOTHERLABEL1").toString().trim().length()>0)
				newBioBean.set("LOTXIDDETAILOTHERLABEL1", dupDataBean.getValue("LOTXIDDETAILOTHERLABEL1"));
			
			if (dupDataBean.getValue("LOTXIDDETAILOTHERLABEL2")!=null && dupDataBean.getValue("LOTXIDDETAILOTHERLABEL2").toString().trim().length()>0)
				newBioBean.set("LOTXIDDETAILOTHERLABEL2", dupDataBean.getValue("LOTXIDDETAILOTHERLABEL2"));
			
			if (dupDataBean.getValue("LOTXIDDETAILOTHERLABEL3")!=null && dupDataBean.getValue("LOTXIDDETAILOTHERLABEL3").toString().trim().length()>0)
				newBioBean.set("LOTXIDDETAILOTHERLABEL3", dupDataBean.getValue("LOTXIDDETAILOTHERLABEL3"));
			
			if (dupDataBean.getValue("MANUALSETUPREQUIRED")!=null && dupDataBean.getValue("MANUALSETUPREQUIRED").toString().trim().length()>0)
				newBioBean.set("MANUALSETUPREQUIRED", dupDataBean.getValue("MANUALSETUPREQUIRED"));
			
			if (dupDataBean.getValue("MANUFACTURERSKU")!=null && dupDataBean.getValue("MANUFACTURERSKU").toString().trim().length()>0)
				newBioBean.set("MANUFACTURERSKU", dupDataBean.getValue("MANUFACTURERSKU"));

			if (dupDataBean.getValue("MaxPalletsPerZone")!=null)
				newBioBean.set("MaxPalletsPerZone", dupDataBean.getValue("MaxPalletsPerZone"));
			
			if (dupDataBean.getValue("MINIMUMSHELFLIFEONRFPICKING")!=null)
				newBioBean.set("MINIMUMSHELFLIFEONRFPICKING", dupDataBean.getValue("MINIMUMSHELFLIFEONRFPICKING"));
			
			if (dupDataBean.getValue("MINIMUMWAVEQTY")!=null)
				newBioBean.set("MINIMUMWAVEQTY", dupDataBean.getValue("MINIMUMWAVEQTY"));
			
			if (dupDataBean.getValue("NETWGT")!=null)
				newBioBean.set("NETWGT", dupDataBean.getValue("NETWGT"));
			
			if (dupDataBean.getValue("NonStockedIndicator") != null && dupDataBean.getValue("NonStockedIndicator").toString().trim().length() > 0)
				newBioBean.set("NonStockedIndicator", dupDataBean.getValue("NonStockedIndicator"));
			
			if (dupDataBean.getValue("NOTES1")!=null && dupDataBean.getValue("NOTES1").toString().trim().length()>0)
				newBioBean.set("NOTES1", dupDataBean.getValue("NOTES1"));
			
			if (dupDataBean.getValue("NOTES2")!=null && dupDataBean.getValue("NOTES2").toString().trim().length()>0)
				newBioBean.set("NOTES2", dupDataBean.getValue("NOTES2"));
			
			if (dupDataBean.getValue("OACOVERRIDE")!=null && dupDataBean.getValue("OACOVERRIDE").toString().trim().length()>0)
				newBioBean.set("OACOVERRIDE", dupDataBean.getValue("OACOVERRIDE"));
			
			if (dupDataBean.getValue("OAVGCASEWEIGHT")!=null )
				newBioBean.set("OAVGCASEWEIGHT", dupDataBean.getValue("OAVGCASEWEIGHT"));
			
			
			if (dupDataBean.getValue("OCD1UNIQUE") != null && dupDataBean.getValue("OCD1UNIQUE").toString().trim().length() > 0)
				newBioBean.set("OCD1UNIQUE", ((String) dupDataBean.getValue("OCD1UNIQUE")).trim());
			
			if (dupDataBean.getValue("OCDCATCHQTY1")!=null && dupDataBean.getValue("OCDCATCHQTY1").toString().trim().length()>0)
				newBioBean.set("OCDCATCHQTY1", ((String) dupDataBean.getValue("OCDCATCHQTY1")).trim());
			
			if (dupDataBean.getValue("OCDCATCHQTY2")!=null && dupDataBean.getValue("OCDCATCHQTY2").toString().trim().length()>0)
				newBioBean.set("OCDCATCHQTY2", ((String) dupDataBean.getValue("OCDCATCHQTY2")).trim());
			
			if (dupDataBean.getValue("OCDCATCHQTY3")!=null && dupDataBean.getValue("OCDCATCHQTY3").toString().trim().length()>0)
				newBioBean.set("OCDCATCHQTY3", ((String)dupDataBean.getValue("OCDCATCHQTY3")).trim());
			
			if (dupDataBean.getValue("OCDCATCHWHEN")!=null && dupDataBean.getValue("OCDCATCHWHEN").toString().trim().length()>0)
				newBioBean.set("OCDCATCHWHEN", dupDataBean.getValue("OCDCATCHWHEN"));
			
			if (dupDataBean.getValue("OCDFLAG")!=null && dupDataBean.getValue("OCDFLAG").toString().trim().length()>0)
				newBioBean.set("OCDFLAG", dupDataBean.getValue("OCDFLAG"));	
			
			if (dupDataBean.getValue("OCDLABEL1")!=null && dupDataBean.getValue("OCDLABEL1").toString().trim().length()>0)
				newBioBean.set("OCDLABEL1", dupDataBean.getValue("OCDLABEL1"));
			
			if (dupDataBean.getValue("OCDLABEL2")!=null && dupDataBean.getValue("OCDLABEL2").toString().trim().length()>0)
				newBioBean.set("OCDLABEL2", dupDataBean.getValue("OCDLABEL2"));
			
			if (dupDataBean.getValue("OCDLABEL3")!=null && dupDataBean.getValue("OCDLABEL3").toString().trim().length()>0)
				newBioBean.set("OCDLABEL3", dupDataBean.getValue("OCDLABEL3"));
			
			if (dupDataBean.getValue("OCDLABEL4") != null && dupDataBean.getValue("OCDLABEL4").toString().trim().length() > 0)
				newBioBean.set("OCDLABEL4", dupDataBean.getValue("OCDLABEL4"));

			if (dupDataBean.getValue("OCDLABEL5") != null && dupDataBean.getValue("OCDLABEL5").toString().trim().length() > 0)
				newBioBean.set("OCDLABEL5", dupDataBean.getValue("OCDLABEL5"));
			
			if (dupDataBean.getValue("OCWBY")!=null && dupDataBean.getValue("OCWBY").toString().trim().length()>0)
				newBioBean.set("OCWBY", dupDataBean.getValue("OCWBY"));
			
			if (dupDataBean.getValue("OCWFLAG")!=null && dupDataBean.getValue("OCWFLAG").toString().trim().length()>0)
				newBioBean.set("OCWFLAG", dupDataBean.getValue("OCWFLAG"));
			
			if (dupDataBean.getValue("ODEWEIGHT")!=null && dupDataBean.getValue("ODEWEIGHT").toString().trim().length()>0)
				newBioBean.set("ODEWEIGHT", dupDataBean.getValue("ODEWEIGHT"));
			
			if (dupDataBean.getValue("ONRECEIPTCOPYPACKKEY")!=null && dupDataBean.getValue("ONRECEIPTCOPYPACKKEY").toString().trim().length()>0)
				newBioBean.set("ONRECEIPTCOPYPACKKEY", dupDataBean.getValue("ONRECEIPTCOPYPACKKEY"));
			
			if (dupDataBean.getValue("OTAREWEIGHT")!=null)
				newBioBean.set("OTAREWEIGHT", dupDataBean.getValue("OTAREWEIGHT"));
			
			if (dupDataBean.getValue("OTOLERANCEPCT")!=null)
				newBioBean.set("OTOLERANCEPCT", dupDataBean.getValue("OTOLERANCEPCT"));
			
			if (dupDataBean.getValue("PACKKEY")!=null && dupDataBean.getValue("PACKKEY").toString().trim().length()>0)
				newBioBean.set("PACKKEY", dupDataBean.getValue("PACKKEY"));
			
			if (dupDataBean.getValue("PICKCODE")!=null && dupDataBean.getValue("PICKCODE").toString().trim().length()>0)
				newBioBean.set("PICKCODE", dupDataBean.getValue("PICKCODE"));
			
			if (dupDataBean.getValue("PICKUOM")!=null && dupDataBean.getValue("PICKUOM").toString().trim().length()>0)
				newBioBean.set("PICKUOM", dupDataBean.getValue("PICKUOM"));
			
			if (dupDataBean.getValue("PRICE")!=null)
				newBioBean.set("PRICE", dupDataBean.getValue("PRICE"));
			
			if (dupDataBean.getValue("PUTAWAYLOC")!=null && dupDataBean.getValue("PUTAWAYLOC").toString().trim().length()>0)
				newBioBean.set("PUTAWAYLOC", dupDataBean.getValue("PUTAWAYLOC"));
			
			if (dupDataBean.getValue("PUTAWAYSTRATEGYKEY")!=null && dupDataBean.getValue("PUTAWAYSTRATEGYKEY").toString().trim().length()>0)
				newBioBean.set("PUTAWAYSTRATEGYKEY", dupDataBean.getValue("PUTAWAYSTRATEGYKEY"));
			
			if (dupDataBean.getValue("PUTAWAYZONE")!=null && dupDataBean.getValue("PUTAWAYZONE").toString().trim().length()>0)
				newBioBean.set("PUTAWAYZONE", dupDataBean.getValue("PUTAWAYZONE"));
			
			if (dupDataBean.getValue("PUTCODE")!=null && dupDataBean.getValue("PUTCODE").toString().trim().length()>0)
				newBioBean.set("PUTCODE", dupDataBean.getValue("PUTCODE"));
			
			if (dupDataBean.getValue("QCLOC")!=null && dupDataBean.getValue("QCLOC").toString().trim().length()>0)
				newBioBean.set("QCLOC", dupDataBean.getValue("QCLOC"));
			
			if (dupDataBean.getValue("QCLOCOUT")!=null && dupDataBean.getValue("QCLOCOUT").toString().trim().length()>0)
				newBioBean.set("QCLOCOUT", dupDataBean.getValue("QCLOCOUT"));
			
			if (dupDataBean.getValue("RECEIPTHOLDCODE")!=null && dupDataBean.getValue("RECEIPTHOLDCODE").toString().trim().length()>0)
				newBioBean.set("RECEIPTHOLDCODE", dupDataBean.getValue("RECEIPTHOLDCODE"));
			
			if (dupDataBean.getValue("RECEIPTINSPECTIONLOC")!=null && dupDataBean.getValue("RECEIPTINSPECTIONLOC").toString().trim().length()>0)
				newBioBean.set("RECEIPTINSPECTIONLOC", dupDataBean.getValue("RECEIPTINSPECTIONLOC"));
			
			if (dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE")!=null && dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE").toString().trim().length()>0)
				newBioBean.set("RECEIPTVALIDATIONTEMPLATE", dupDataBean.getValue("RECEIPTVALIDATIONTEMPLATE"));
			
			if (dupDataBean.getValue("REORDERPOINT")!=null)
				newBioBean.set("REORDERPOINT", dupDataBean.getValue("REORDERPOINT"));
			
			if (dupDataBean.getValue("REORDERQTY")!=null)
				newBioBean.set("REORDERQTY", dupDataBean.getValue("REORDERQTY"));
			
			if (dupDataBean.getValue("RETAILSKU")!=null && dupDataBean.getValue("RETAILSKU").toString().trim().length()>0)
				newBioBean.set("RETAILSKU", dupDataBean.getValue("RETAILSKU"));
			
			if (dupDataBean.getValue("RETURNSLOC")!=null && dupDataBean.getValue("RETURNSLOC").toString().trim().length()>0)
				newBioBean.set("RETURNSLOC", dupDataBean.getValue("RETURNSLOC"));
			
			if (dupDataBean.getValue("RFDEFAULTPACK")!=null && dupDataBean.getValue("RFDEFAULTPACK").toString().trim().length()>0)
				newBioBean.set("RFDEFAULTPACK", dupDataBean.getValue("RFDEFAULTPACK"));
			
			if (dupDataBean.getValue("RFDEFAULTUOM")!=null && dupDataBean.getValue("RFDEFAULTUOM").toString().trim().length()>0)
				newBioBean.set("RFDEFAULTUOM", dupDataBean.getValue("RFDEFAULTUOM"));
			
			if (dupDataBean.getValue("ROTATEBY")!=null && dupDataBean.getValue("ROTATEBY").toString().trim().length()>0)
				newBioBean.set("ROTATEBY", dupDataBean.getValue("ROTATEBY"));
			
			//newBioBean.set("SERIALKEY", dupDataBean.getValue("SERIALKEY"));
			if (dupDataBean.getValue("SERIALNUMBEREND")!=null)
				newBioBean.set("SERIALNUMBEREND", dupDataBean.getValue("SERIALNUMBEREND"));
			
			if (dupDataBean.getValue("SERIALNUMBERNEXT")!=null)
				newBioBean.set("SERIALNUMBERNEXT", dupDataBean.getValue("SERIALNUMBERNEXT"));
			
			if (dupDataBean.getValue("SERIALNUMBERSTART")!=null)
				newBioBean.set("SERIALNUMBERSTART", dupDataBean.getValue("SERIALNUMBERSTART"));
			
			if (dupDataBean.getValue("SHELFLIFE")!=null )
				newBioBean.set("SHELFLIFE", dupDataBean.getValue("SHELFLIFE"));
			
			if (dupDataBean.getValue("SHELFLIFECODETYPE")!=null && dupDataBean.getValue("SHELFLIFECODETYPE").toString().trim().length()>0)
				newBioBean.set("SHELFLIFECODETYPE", dupDataBean.getValue("SHELFLIFECODETYPE"));
			
			if (dupDataBean.getValue("SHELFLIFEINDICATOR")!=null && dupDataBean.getValue("SHELFLIFEINDICATOR").toString().trim().length()>0)
				newBioBean.set("SHELFLIFEINDICATOR", dupDataBean.getValue("SHELFLIFEINDICATOR"));
			
			if (dupDataBean.getValue("SHELFLIFEONRECEIVING")!=null )
				newBioBean.set("SHELFLIFEONRECEIVING", dupDataBean.getValue("SHELFLIFEONRECEIVING"));
			
			if (dupDataBean.getValue("SHIPPABLECONTAINER")!=null && dupDataBean.getValue("SHIPPABLECONTAINER").toString().trim().length()>0)
				newBioBean.set("SHIPPABLECONTAINER", dupDataBean.getValue("SHIPPABLECONTAINER"));
			/**
			if (dupDataBean.getValue("SKU")!=null && dupDataBean.getValue("SKU").toString().trim().length()>0)
				newBioBean.set("SKU", dupDataBean.getValue("SKU"));
			**/
			newBioBean.set("SKU","");
			
			if (dupDataBean.getValue("SKUGROUP")!=null && dupDataBean.getValue("SKUGROUP").toString().trim().length()>0)
				newBioBean.set("SKUGROUP", dupDataBean.getValue("SKUGROUP"));
			
			if (dupDataBean.getValue("SKUGROUP2")!=null && dupDataBean.getValue("SKUGROUP2").toString().trim().length()>0)
				newBioBean.set("SKUGROUP2", dupDataBean.getValue("SKUGROUP2"));
			
			if (dupDataBean.getValue("SKUTYPE")!=null && dupDataBean.getValue("SKUTYPE").toString().trim().length()>0)
				newBioBean.set("SKUTYPE", dupDataBean.getValue("SKUTYPE"));
			
			// Serial Num
			if (dupDataBean.getValue("SNUM_AUTOINCREMENT") != null)
				newBioBean.set("SNUM_AUTOINCREMENT", dupDataBean.getValue("SNUM_AUTOINCREMENT"));
			
			if (dupDataBean.getValue("SNUM_DELIM_COUNT") != null)
				newBioBean.set("SNUM_DELIM_COUNT", dupDataBean.getValue("SNUM_DELIM_COUNT"));
			
			if (dupDataBean.getValue("SNUM_DELIMITER") != null && dupDataBean.getValue("SNUM_DELIMITER").toString().trim().length() > 0)
				newBioBean.set("SNUM_DELIMITER", dupDataBean.getValue("SNUM_DELIMITER"));
			
			if (dupDataBean.getValue("SNUM_ENDTOEND") != null)
				newBioBean.set("SNUM_ENDTOEND", dupDataBean.getValue("SNUM_ENDTOEND"));

			if (dupDataBean.getValue("SNUM_INCR_LENGTH") != null)
				newBioBean.set("SNUM_INCR_LENGTH", dupDataBean.getValue("SNUM_INCR_LENGTH"));			
			
			if (dupDataBean.getValue("SNUM_INCR_POS") != null)
				newBioBean.set("SNUM_INCR_POS", dupDataBean.getValue("SNUM_INCR_POS"));
			
			if (dupDataBean.getValue("SNUM_LENGTH") != null)
				newBioBean.set("SNUM_LENGTH", dupDataBean.getValue("SNUM_LENGTH"));

			if (dupDataBean.getValue("SNUM_MASK") != null && dupDataBean.getValue("SNUM_MASK").toString().trim().length() > 0)
				newBioBean.set("SNUM_MASK", dupDataBean.getValue("SNUM_MASK"));

			if (dupDataBean.getValue("SNUM_POSITION") != null)
				newBioBean.set("SNUM_POSITION", dupDataBean.getValue("SNUM_POSITION"));

			if (dupDataBean.getValue("SNUM_QUANTITY") != null)
				newBioBean.set("SNUM_QUANTITY", dupDataBean.getValue("SNUM_QUANTITY"));
			
			if (dupDataBean.getValue("SNUMLONG_DELIMITER") != null && dupDataBean.getValue("SNUMLONG_DELIMITER").toString().trim().length() > 0)
				newBioBean.set("SNUMLONG_DELIMITER", dupDataBean.getValue("SNUMLONG_DELIMITER"));

			if (dupDataBean.getValue("SNUMLONG_FIXED") != null)
				newBioBean.set("SNUMLONG_FIXED", dupDataBean.getValue("SNUMLONG_FIXED"));
			// End Serial Num
			
			if (dupDataBean.getValue("StackLimit")!=null)
				newBioBean.set("StackLimit", dupDataBean.getValue("StackLimit"));
			
			if (dupDataBean.getValue("STDCUBE")!=null)
				newBioBean.set("STDCUBE", dupDataBean.getValue("STDCUBE"));
			
			if (dupDataBean.getValue("STDGROSSWGT")!=null)
				newBioBean.set("STDGROSSWGT", dupDataBean.getValue("STDGROSSWGT"));
			
			if (dupDataBean.getValue("STDNETWGT")!=null)
				newBioBean.set("STDNETWGT", dupDataBean.getValue("STDNETWGT"));
			
			if (dupDataBean.getValue("STDORDERCOST")!=null)
				newBioBean.set("STDORDERCOST", dupDataBean.getValue("STDORDERCOST"));
			
			if (dupDataBean.getValue("STORERKEY")!=null && dupDataBean.getValue("STORERKEY").toString().trim().length()>0)
				newBioBean.set("STORERKEY", dupDataBean.getValue("STORERKEY"));
			
			if (dupDataBean.getValue("STRATEGYKEY")!=null && dupDataBean.getValue("STRATEGYKEY").toString().trim().length()>0)
				newBioBean.set("STRATEGYKEY", dupDataBean.getValue("STRATEGYKEY"));
			
			if (dupDataBean.getValue("SUSR1")!=null && dupDataBean.getValue("SUSR1").toString().trim().length()>0)
				newBioBean.set("SUSR1", dupDataBean.getValue("SUSR1"));
			
			if (dupDataBean.getValue("SUSR10")!=null && dupDataBean.getValue("SUSR10").toString().trim().length()>0)
				newBioBean.set("SUSR10", dupDataBean.getValue("SUSR10"));
			
			if (dupDataBean.getValue("SUSR2")!=null && dupDataBean.getValue("SUSR2").toString().trim().length()>0)
				newBioBean.set("SUSR2", dupDataBean.getValue("SUSR2"));
			
			if (dupDataBean.getValue("SUSR3")!=null && dupDataBean.getValue("SUSR3").toString().trim().length()>0)
				newBioBean.set("SUSR3", dupDataBean.getValue("SUSR3"));
			
			if (dupDataBean.getValue("SUSR4")!=null && dupDataBean.getValue("SUSR4").toString().trim().length()>0)
				newBioBean.set("SUSR4", dupDataBean.getValue("SUSR4"));
			
			if (dupDataBean.getValue("SUSR5")!=null && dupDataBean.getValue("SUSR5").toString().trim().length()>0)
				newBioBean.set("SUSR5", dupDataBean.getValue("SUSR5"));
			
			if (dupDataBean.getValue("SUSR6")!=null && dupDataBean.getValue("SUSR6").toString().trim().length()>0)
				newBioBean.set("SUSR6", dupDataBean.getValue("SUSR6"));
			
			if (dupDataBean.getValue("SUSR7")!=null && dupDataBean.getValue("SUSR7").toString().trim().length()>0)
				newBioBean.set("SUSR7", dupDataBean.getValue("SUSR7"));
			
			if (dupDataBean.getValue("SUSR8")!=null && dupDataBean.getValue("SUSR8").toString().trim().length()>0)
				newBioBean.set("SUSR8", dupDataBean.getValue("SUSR8"));
			
			if (dupDataBean.getValue("SUSR9")!=null && dupDataBean.getValue("SUSR9").toString().trim().length()>0)
				newBioBean.set("SUSR9", dupDataBean.getValue("SUSR9"));
			
			if (dupDataBean.getValue("TARE")!=null)
				newBioBean.set("TARE", dupDataBean.getValue("TARE"));
			
			if (dupDataBean.getValue("TAREWEIGHT")!=null)
				newBioBean.set("TAREWEIGHT", dupDataBean.getValue("TAREWEIGHT"));
			
			if (dupDataBean.getValue("TARIFFKEY")!=null && dupDataBean.getValue("TARIFFKEY").toString().trim().length()>0)
				newBioBean.set("TARIFFKEY", dupDataBean.getValue("TARIFFKEY"));

			if (dupDataBean.getValue("TOBESTBYDAYS")!=null)
				newBioBean.set("TOBESTBYDAYS", dupDataBean.getValue("TOBESTBYDAYS"));

			if (dupDataBean.getValue("TODELIVERBYDAYS")!=null)
				newBioBean.set("TODELIVERBYDAYS", dupDataBean.getValue("TODELIVERBYDAYS"));
			
			if (dupDataBean.getValue("TOEXPIREDAYS")!=null)
				newBioBean.set("TOEXPIREDAYS", dupDataBean.getValue("TOEXPIREDAYS"));
			
			if (dupDataBean.getValue("TOLERANCEPCT")!=null)
				newBioBean.set("TOLERANCEPCT", dupDataBean.getValue("TOLERANCEPCT"));
			
			if (dupDataBean.getValue("TRANSPORTATIONMODE")!=null && dupDataBean.getValue("TRANSPORTATIONMODE").toString().trim().length()>0)
				newBioBean.set("TRANSPORTATIONMODE", dupDataBean.getValue("TRANSPORTATIONMODE"));
			
			if (dupDataBean.getValue("TYPE")!=null && dupDataBean.getValue("TYPE").toString().trim().length()>0)
				newBioBean.set("TYPE", dupDataBean.getValue("TYPE"));
			
			if (dupDataBean.getValue("VERIFYLOT04LOT05")!=null && dupDataBean.getValue("VERIFYLOT04LOT05").toString().trim().length()>0)
				newBioBean.set("VERIFYLOT04LOT05", dupDataBean.getValue("VERIFYLOT04LOT05"));
			
			if (dupDataBean.getValue("VERT_STORAGE")!=null && dupDataBean.getValue("VERT_STORAGE").toString().trim().length()>0)
				newBioBean.set("VERT_STORAGE", dupDataBean.getValue("VERT_STORAGE"));
			
			//07/09/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent3178227_Defect213486) -- Start
			/*
			if (dupDataBean.getValue("WHSEID")!=null && dupDataBean.getValue("WHSEID").toString().trim().length()>0)
				newBioBean.set("WHSEID", dupDataBean.getValue("WHSEID"));
			*/
            //07/09/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent3178227_Defect213486) -- End

			//Krishna Kuchipudi: 3PL Enhancements -Added the below code for 3PL Advanced Catch Weight functionality - Starts

//			if (dupDataBean.getValue("advcwttrackby")!=null && dupDataBean.getValue("advcwttrackby").toString().trim().length()>0)
//				newBioBean.set("advcwttrackby", dupDataBean.getValue("advcwttrackby"));
			if (dupDataBean.getValue("catchgrosswgt")!=null && dupDataBean.getValue("catchgrosswgt").toString().trim().length()>0)
				newBioBean.set("catchgrosswgt", dupDataBean.getValue("catchgrosswgt"));
			if (dupDataBean.getValue("catchnetwgt")!=null && dupDataBean.getValue("catchnetwgt").toString().trim().length()>0)
				newBioBean.set("catchnetwgt", dupDataBean.getValue("catchnetwgt"));		
			if (dupDataBean.getValue("catchtarewgt")!=null && dupDataBean.getValue("catchtarewgt").toString().trim().length()>0)
				newBioBean.set("catchtarewgt", dupDataBean.getValue("catchtarewgt"));
			if (dupDataBean.getValue("stdgrosswgt1")!=null && dupDataBean.getValue("stdgrosswgt1").toString().trim().length()>0)
				newBioBean.set("stdgrosswgt1", dupDataBean.getValue("stdgrosswgt1"));
			if (dupDataBean.getValue("stdnetwgt1")!=null && dupDataBean.getValue("stdnetwgt1").toString().trim().length()>0)
				newBioBean.set("stdnetwgt1", dupDataBean.getValue("stdnetwgt1"));
		    if (dupDataBean.getValue("stduom")!=null && dupDataBean.getValue("stduom").toString().trim().length()>0)
		    	newBioBean.set("stduom", dupDataBean.getValue("stduom"));
			if (dupDataBean.getValue("tarewgt1")!=null && dupDataBean.getValue("tarewgt1").toString().trim().length()>0)
				newBioBean.set("tarewgt1", dupDataBean.getValue("tarewgt1"));
//			if (dupDataBean.getValue("enableadvcwgt")!=null && dupDataBean.getValue("enableadvcwgt").toString().trim().length()>0)
//				newBioBean.set("enableadvcwgt", dupDataBean.getValue("enableadvcwgt"));
			
			//Krishna Kuchipudi: Added the below code for 3PL Advanced Catch Weight functionality - Ends
		}
		catch(Exception e){
			_log.debug("LOG_SYSTEM_OUT","Failed to load Item info. Field:"+e.getMessage(),100L);
			e.printStackTrace();
			populateBioType.setStatus(false);
		}
		populateBioType.setqbeBioBean(newBioBean);
		return populateBioType;
		
		
	}

	
}
