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
package com.infor.scm.wms.util.validation.screen.shipfrom;

import java.util.ArrayList;
import java.util.Locale;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.owner.OwnerScreenVO;
import com.infor.scm.wms.util.validation.screen.owner.OwnerScreenValidator;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class ShipfromScreenValidator extends OwnerScreenValidator {
	
	public ShipfromScreenValidator(WMSValidationContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList validate(OwnerScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doCheckAsOwner = disableRules == null?false:disableRules.contains(BaseScreenValidator.RULE_OWNER);
				
		//Validate Field Lengths
		if(doCheckFieldLength){			
			if(!validateAddress1LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS1_45));	
			}			
			if (!validateAddress2LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS2_45));
			}
			if (!validateAddress3LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS3_45));
			}
			if (!validateAddress4LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS4_45));
			}
			if (!validateAllowautocloseforasnLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWAUTOCLOSEFORASN_1));
			}
			if (!validateAllowautocloseforpoLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWAUTOCLOSEFORPO_1));
			}
			if (!validateAllowautocloseforpsLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWAUTOCLOSEFORPS_1));
			}
			if (!validateAllowcommingledlpnLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWCOMMINGLEDLPN_1));
			}
			if (!validateAllowduplicatelicenseplateLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWDUPLICATELICENSEPLATES_1));
			}
			if (!validateAllowovershipmentLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWOVERSHIPMENT_1));
			}
			if (!validateAllowsinglescanreceivingLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWSINGLESCANRECEIVING_1));
			}
			if (!validateAllowsystemgeneratedlpnLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWSYSTEMGENERATEDLPN_1));
			}
			if (!validateApplicationidLengthIs2OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_APPLICATIONID_2));
			}
			if (!validateApportionruleLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_APPORTIONRULE_10));
			}
			if (!validateAutocloseasnLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_AUTOCLOSEASN_1));
			}
			if (!validateAutoclosepoLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_AUTOCLOSEPO_1));
			}
			if (!validateAutoprintlabellpnLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_AUTOPRINTLABELLPN_1));
			}
			if (!validateAutoprintlabelputawayLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_AUTOPRINTLABELPUTAWAY_1));
			}
			if (!validateB_address1LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ADDRESS1_45));
			}
			if (!validateB_address2LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ADDRESS2_45));
			}
			if (!validateB_address3LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ADDRESS3_45));
			}
			if (!validateB_address4LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ADDRESS4_45));
			}
			if (!validateB_cityLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_CITY_45));
			}
			if (!validateB_companyLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_COMPANY_45));
			}
			if (!validateB_contact1LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_CONTACT1_30));
			}
			if (!validateB_contact2LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_CONTACT2_30));
			}
			if (!validateB_countryLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_COUNTRY_30));
			}
			if (!validateB_email1LengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_EMAIL1_60));
			}
			if (!validateB_email2LengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_EMAIL2_60));
			}
			if (!validateB_fax1LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_FAX1_18));
			}
			if (!validateB_fax2LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_FAX2_18));
			}
			if (!validateB_isocntrycodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ISOCNTRYCODE_10));
			}
			if (!validateB_phone1LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_PHONE1_18));
			}
			if (!validateB_phone2LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_PHONE2_18));
			}
			// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
			if (!validateB_stateLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_STATE_25));
			}
			if (!validateB_zipLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_B_ZIP_18));
			}
			if (!validateBarcodeconfigkeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODECONFIGKEY_18));
			}
			if (!validateCalculateputawaylocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CALCULATEPUTAWAYLOCATION_10));
			}
			if (!validateCartongroupLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONGROUP_10));
			}
			if (!validateCaselabeltypeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CaseLabelType_10));
			}
			if (!validateCcadjbyrfLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CCADJBYRF_10));
			}
			if (!validateCcdiscrepancyruleLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CCDISCREPANCYRULE_10));
			}
			if (!validateCcskuxlocLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CCSKUXLOC_1));
			}
			if (!validateCityLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CITY_45));
			}
			if (!validateCompanyLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_COMPANY_45));
			}
			if (!validateContact1LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONTACT1_30));
			}
			if (!validateContact2LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONTACT2_30));
			}
			if (!validateCountryLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_COUNTRY_30));
			}
			if (!validateCreatepataskonrfreceiptLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CREATEPATASKONRFRECEIPT_10));
			}
			if (!validateCreditlimitLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CREDITLIMIT_18));
			}
			if (!validateCwoflagLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CWOFLAG_1));
			}
			if (!validateDefaultpackinglocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTPACKINGLOCATION_10));
			}
			if (!validateDefaultputawaystrategyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTPUTAWAYSTRATEGY_10));
			}
			if (!validateDefaultqclocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTQCLOC_10));
			}
			if (!validateDefaultqclocoutLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTQCLOCOUT_10));
			}
			if (!validateDefaultreturnslocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTRETURNSLOC_10));
			}
			if (!validateDefaultrotationLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTROTATION_1));
			}
			if (!validateDefaultskurotationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTSKUROTATION_10));
			}
			if (!validateDefaultstrategyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTSTRATEGY_10));
			}
			if (!validateDescriptionLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DESCRIPTION_50));
			}
			if (!validateDupcaseidLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DUPCASEID_1));
			}
			if (!validateEmail1LengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EMAIL1_60));
			}
			if (!validateEmail2LengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EMAIL2_60));
			}
			if (!validateEnableoppxdockLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ENABLEOPPXDOCK_1));
			}
			if (!validateEnablepackingdefaultLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ENABLEPACKINGDEFAULT_1));
			}
			if (!validateFax1LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_FAX1_18));
			}
			if (!validateFax2LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_FAX2_18));
			}
			if (!validateGeneratepacklistLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_GENERATEPACKLIST_1));
			}
			if (!validateInspectatpackLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INSPECTATPACK_1));
			}
			if (!validateIsocntrycodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISOCNTRYCODE_10));
			}
			if (!validateLpnbarcodeformatLengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LPNBARCODEFORMAT_60));
			}
			if (!validateLpnbarcodesymbologyLengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LPNBARCODESYMBOLOGY_60));
			}
			if (!validateLpnrollbacknumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LPNROLLBACKNUMBER_18));
			}
			if (!validateLpnstartnumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LPNSTARTNUMBER_18));
			}
			if (!validateMultizoneplpaLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_MULTIZONEPLPA_1));
			}
			if (!validateNextlpnnumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_NEXTLPNNUMBER_18));
			}
			if (!validateOpporderstrategykeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OPPORDERSTRATEGYKEY_10));
			}
			if (!validateOrderbreakdefaultLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERBREAKDEFAULT_1));
			}
			if (!validateOrdertyperestrict01LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT01_10));
			}
			if (!validateOrdertyperestrict02LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT02_10));
			}
			if (!validateOrdertyperestrict03LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT03_10));
			}
			if (!validateOrdertyperestrict04LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT04_10));
			}
			if (!validateOrdertyperestrict05LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT05_10));
			}
			if (!validateOrdertyperestrict06LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORDERTYPERESTRICT06_10));
			}
			if (!validatePackingvalidationtemplateLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKINGVALIDATIONTEMPLATE_10));
			}
			if (!validatePhone1LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PHONE1_18));
			}
			if (!validatePhone2LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PHONE2_18));
			}
			if (!validatePickcodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PICKCODE_10));
			}
			if (!validatePiskuxlocLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PISKUXLOC_1));
			}
			if (!validateReceiptvalidationtemplateLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTVALIDATIONTEMPLATE_18));
			}
			if (!validateRollreceiptLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ROLLRECEIPT_1));
			}
			if (!validateScac_codeLengthIs4OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SCAC_CODE_4));
			}
			if (!validateSkusetuprequiredLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SKUSETUPREQUIRED_1));
			}
			// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
			if (!validateStateLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STATE_25));
			}
			if (!validateStatusLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STATUS_18));
			}
			if (!validateStorerkeyLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STORERKEY_15));
			}
			if (!validateSusr1LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR1_30));
			}
			if (!validateSusr2LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR2_30));
			}
			if (!validateSusr3LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR3_30));
			}
			if (!validateSusr4LengthIs30OrLess(screen)){					
				errors.add(new Integer(RULE_LENGTH_SUSR4_30));
			}
			if (!validateSusr5LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR5_30));
			}
			if (!validateSusr6LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR6_30));
			}
			if (!validateTitle1LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TITLE1_50));
			}
			if (!validateTitle2LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TITLE2_50));
			}
			if (!validateTrackinventorybyLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRACKINVENTORYBY_1));
			}
			if (!validateTypeLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TYPE_30));
			}
			if (!validateUccvendornumberLengthIs9OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UCCVENDORNUMBER_9));
			}
			if (!validateVatLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VAT_18));
			}
			if (!validateWhseidLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WHSEID_30));
			}
			if (!validateZipLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ZIP_18));
			}
		}
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
			
			if(!validateIsocntrycodeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ISO_COUNTRY_CODE  ));		
			}
			
			
			if(!validateCartongroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CARTON_GROUP  ));		
			}
			
			if(!validateOrdersequencestrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_SEQUENCE_STRATEGY  ));		
			}
			
			if(!validateAutomaticapportionmentruleInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_AUTOMATIC_APPORTIONMENT_RULE  ));		
			}
			
			if(!validateOrdertyperestrict01InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_1  ));		
			}
			
			
			if(!validateOrdertyperestrict02InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_2  ));		
			}
			
			if(!validateOrdertyperestrict03InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_3  ));		
			}
			
			if(!validateOrdertyperestrict04InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_4  ));		
			}
			
			if(!validateOrdertyperestrict05InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_5  ));		
			}
			
			if(!validateOrdertyperestrict06InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ORDER_TYPE_6  ));		
			}
			
			if(!validateInvoicenumberstrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_INVOICE_NUMBER_STRATEGY  ));		
			}
			
			if(!validateBillinggroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_BILLING_GROUP  ));		
			}
			
			if(!validateHiminimumreceipttaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_1  ));		
			}
			
			
			if(!validateHominimumshipmenttaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_2  ));		
			}
			
			if(!validateIsminimumreceipttaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_3  ));		
			}
			
			if(!validateHiminimuminvoicetaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_4  ));		
			}
			
			if(!validateIsminimuminvoicetaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_5  ));		
			}
			
			if(!validateRsminimuminvoicetaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_6  ));		
			}
			
			if(!validateAlminimumtaxgroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TAX_GROUP_7  ));		
			}
			
			if(!validateHiminimumreceiptgldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_1  ));		
			}
			
			if(!validateHominimumshipmentgldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_2  ));		
			}
			
			if(!validateIsminimumreceiptgldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_3  ));		
			}
			
			if(!validateHiminimuminvoicegldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_4  ));		
			}
			
			if(!validateIsminimuminvoicegldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_5  ));		
			}
			
			if(!validateRsminimuminvoicegldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_6  ));		
			}
			
			if(!validateAlminimumgldistInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_GL_DISTRIBUTION_7  ));		
			}
			
			
			if(!validateDefaultskurotationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_ITEM_ROTATION  ));		
			}
			
			if(!validateDefaultrotationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_ROTATION  ));		
			}
			
			if(!validateDefaultstrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_STRATEGY  ));		
			}
			
			if(!validateDefaultputawaystrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_PUTAWAY_STRATEGY  ));		
			}
			
			if(!validateCreateputawaytaskonrfreceiptInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CREATE_PUTAWAY_TASK_ON_RF_RECEIPT  ));		
			}
			
			if(!validateCalculateputawaylocationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CALCULATE_PUTAWAY_LOCATION  ));		
			}
			
			if(!validateOrderbreakdefaultInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ASSIGNMENT_ORDER_BREAK_DEFAULT  ));		
			}
			
			if(!validatePackingvalidationtemplateInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKING_VALIDATION_TEMPLATE  ));		
			}
			
			if(!validateLpnbarcodesymbologyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LPN_BARCODE_SYMBOLOGY  ));		
			}
			
			if(!validateLpnbarcodeformatInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LPN_BARCODE_FORMAT  ));		
			}
			
			if(!validateLpnlengthInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LPN_LENGTH  ));		
			}
			
			if(!validateCaselabeltypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CASE_LABEL_TYPE  ));		
			}
			
			if(!validateSscc1stdigitInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_SSCC_1ST_DIGIT  ));		
			}
			
			if(!validateCcdiscrepancyruleInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_DISCREPANCY_HANDLING_RULE  ));		
			}
			
			if(!validateAllowcommingledlpnInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_COMMINGLED_LPN  ));		
			}
			
			if(!validateAllowautocloseforasnInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_ASN  ));		
			}
			
			if(!validateAllowautocloseforpoInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_PO  ));		
			}
			
			if(!validateAllowsystemgeneratedlpnInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_SYSTEM_GENERATED_LPN  ));		
			}
			
			if(!validateAllowsinglescanreceivingInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_SINGLE_SCAN_RECEIVING  ));		
			}
			
			if(!validateAllowsinglescanreceivingInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ALLOW_SINGLE_SCAN_RECEIVING  ));		
			}
			
			if(!validateReceiptvalidationtemplateInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RECEIPT_VALIDATION_TEMPLATE  ));		
			}
			
			if(!validateBarcodeconfigkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_BARCODE_CONFIGURATION_KEY  ));		
			}
			
			if(!validateTrackinventorybyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_TRACKING_FOR_CASE_RECEIPTS  ));		
			}
			
			if(!validateDupcaseidInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DUPLICATE_CASE_IDS  ));		
			}
			
		}
		
		
		if(doCheckRequiredFields && validateCreditlimitNotEmpty(screen)){
			if(!validateCreditlimitIsANumber(screen))
				errors.add(new Integer(RULE_CREDIT_LIMIT_MUST_BE_A_NUMBER));
			else{
				if(!validateCreditlimitGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_CREDIT_LIMIT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		
		if(doCheckRequiredFields && validateMaximumordersNotEmpty(screen)){
			if(!validateMaximumordersIsANumber(screen))
				errors.add(new Integer(RULE_MAXIMUM_ORDERS_MUST_BE_A_NUMBER));
			else{
				if(!validateMaximumordersGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXIMUM_ORDERS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		
		if(doCheckRequiredFields && validateMinimumpercentNotEmpty(screen)){
			if(!validateMinimumpercentIsANumber(screen))
				errors.add(new Integer(RULE_MINIMUM_PERCENT_MUST_BE_A_NUMBER));
			else{
				if(!validateMinimumpercentGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MINIMUM_PERCENT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && validateOrderdatestartdaysNotEmpty(screen)){
			if(!validateOrderdatestartdaysIsANumber(screen))
				errors.add(new Integer(RULE_ORDER_DATE_START_DAYS_MUST_BE_A_NUMBER));
			else{
				if(!validateOrderdatestartdaysGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_ORDER_DATE_START_DAYS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && validateOrderdateenddaysNotEmpty(screen)){
			if(!validateOrderdateenddaysIsANumber(screen))
				errors.add(new Integer(RULE_ORDER_DATE_END_DAYS_MUST_BE_A_NUMBER));
			else{
				if(!validateOrderdateenddaysGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_ORDER_DATE_END_DAYS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && validateHiminimumreceiptchargeNotEmpty(screen)){
			if(!validateHiminimumreceiptchargeIsANumber(screen))
				errors.add(new Integer(RULE_HI_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateHiminimumreceiptchargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_HI_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateHiminimumreceiptchargeIsCurrency(screen)){
			errors.add(new Integer(RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		
		if(doCheckRequiredFields && validateHominimumshipmentchargeNotEmpty(screen)){
			if(!validateHominimumshipmentchargeIsANumber(screen))
				errors.add(new Integer(RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateHominimumshipmentchargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_HO_MINIMUM_SHIPMENT_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateHominimumshipmentchargeIsCurrency(screen)){
			errors.add(new Integer(RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		if(doCheckRequiredFields && validateIsminimumreceiptchargeNotEmpty(screen)){
			if(!validateIsminimumreceiptchargeIsANumber(screen))
				errors.add(new Integer(RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateIsminimumreceiptchargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_IS_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateIsminimumreceiptchargeIsCurrency(screen)){
			errors.add(new Integer(RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		
		if(doCheckRequiredFields && validateHiminimuminvoicechargeNotEmpty(screen)){
			if(!validateHiminimuminvoicechargeIsANumber(screen))
				errors.add(new Integer(RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateHiminimuminvoicechargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_HI_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		
		if (doCheckRequiredFields  && !validateHiminimuminvoicechargeIsCurrency(screen)){
			errors.add(new Integer(RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		
		if(doCheckRequiredFields && validateIsminimuminvoicechargeNotEmpty(screen)){
			if(!validateIsminimuminvoicechargeIsANumber(screen))
				errors.add(new Integer(RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateIsminimuminvoicechargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_IS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateIsminimuminvoicechargeIsCurrency(screen)){
			errors.add(new Integer(RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		
		if(doCheckRequiredFields && validateRsminimuminvoicechargeNotEmpty(screen)){
			if(!validateRsminimuminvoicechargeIsANumber(screen))
				errors.add(new Integer(RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateRsminimuminvoicechargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_RS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateRsminimuminvoicechargeIsCurrency(screen)){
			errors.add(new Integer(RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		if(doCheckRequiredFields && validateAlminimumchargeNotEmpty(screen)){
			if(!validateAlminimumchargeIsANumber(screen))
				errors.add(new Integer(RULE_AL_MINIMUM_CHARGE_MUST_BE_A_NUMBER));
			else{
				if(!validateAlminimumchargeGreaterThanZero(screen))
					errors.add(new Integer(RULE_AL_MINIMUM_CHARGE_GREATER_THAN_ZERO));
			}
		}
		
		if (doCheckRequiredFields  && !validateAlminimumchargeIsCurrency(screen)){
			errors.add(new Integer(RULE_AL_MINIMUM_CHARGE_MUST_BE_VALID_CURRENCY));
		}
		
		if(validateDefaultqclocNotEmpty(screen)){
			if(!validateDefaultqclocInAttrDom(screen)){
				errors.add(new Integer(RULE_DEFAULT_QC_LOC_MUST_BE_VALID_LOCATION  ));		
			}
		}
		
		if(validateDefaultqclocoutNotEmpty(screen)){
			if(!validateDefaultqclocoutInAttrDom(screen)){
				errors.add(new Integer(RULE_DEFAULT_QC_LOC_OUT_MUST_BE_VALID_LOCATION  ));		
			}
		}
		
		if(validateDefaultreturnslocNotEmpty(screen)){
			if(!validateDefaultreturnslocInAttrDom(screen)){
				errors.add(new Integer(RULE_DEFAULT_RETURNS_LOC_MUST_BE_VALID_LOCATION  ));		
			}
		}
		
		if(validateDefaultpackinglocNotEmpty(screen)){
			if(!validateDefaultpackinglocInAttrDom(screen)){
				errors.add(new Integer(RULE_DEFAULT_PACKING_LOC_MUST_BE_VALID_LOCATION  ));		
			}
		}
		
		if(doCheckRequiredFields && validateLpnstartnumberNotEmpty(screen)){
			if(!validateLpnstartnumberIsANumber(screen))
				errors.add(new Integer(RULE_LPN_START_NUMBER_MUST_BE_A_NUMBER));
			else{
				if(!validateLpnstartnumberGreaterThanZero(screen))
					errors.add(new Integer(RULE_LPN_START_NUMBER_MUST_BE_GREATER_THAN_ZERO));
			}
		}
		
		if(doCheckRequiredFields && validateLpnstartnumberNotEmpty(screen)){
			if(allNinesValidation(screen.getLpnstartnumber())){
				errors.add(new Integer(RULE_LPN_START_NUMBER_NOT_ALL_NINE  ));
			}
		}
		
		if(doCheckRequiredFields && validateUccvendornumberNotEmpty(screen)){
			if(!validateUccvendornumberIsANumber(screen))
				errors.add(new Integer(RULE_UCC_VENDOR_NUMBER_MUST_BE_A_NUMBER));
			else{
				if(!validateUccvendornumberGreaterThanZero(screen))
					errors.add(new Integer(RULE_UCC_VENDOR_NUMBER_MUST_BE_GREATER_THAN_ZERO));
			}
		}
		
		
		if(doCheckRequiredFields && validateNextlpnnumberNotEmpty(screen) && validateLpnstartnumberNotEmpty(screen)){
			if(validateNextlpnnumberIsANumber(screen)&& validateLpnstartnumberIsANumber(screen)){
				if (!validateFirstGreaterThanOrEqualSecond(screen.getNextlpnnumber(), screen.getLpnstartnumber())){
					errors.add(new Integer(RULE_NEXT_LPN_NUMBER_GREATER_THAN_OR_EQUAL_LPN_START_NUMBER));
				}
				
			}
		}
		
		if(doCheckRequiredFields && validateLpnrollbacknumberNotEmpty(screen) && validateNextlpnnumberNotEmpty(screen)){
			if(validateLpnrollbacknumberIsANumber (screen) && validateNextlpnnumberIsANumber(screen)){
				if (!validateFirstGreaterThanOrEqualSecond(screen.getLpnrollbacknumber(), screen.getNextlpnnumber() )){
					errors.add(new Integer(RULE_LPN_ROLLBACK_NUMBER_GREATER_THAN_OR_EQUAL_NEXT_LPN_NUMBER));
				}
				
			}
		}
		
		if(doCheckRequiredFields && validateLpnlengthNotEmpty(screen) && validateLpnstartnumberNotEmpty(screen)){ 
			if(!validateLpnLength(screen.getLpnstartnumber(), screen.getLpnlength())){
				errors.add(new Integer(RULE_LPN_START_NUMBER_COMPLIES_WITH_LPN_LENGTH));
				
			}
		}
		
		if(doCheckRequiredFields && validateLpnlengthNotEmpty(screen) && validateNextlpnnumberNotEmpty(screen)){ 
			if(!validateLpnLength(screen.getNextlpnnumber(), screen.getLpnlength())){
				errors.add(new Integer(RULE_NEXT_LPN_NUMBER_COMPLIES_WITH_LPN_LENGTH));
			}
		}
		
		if(doCheckRequiredFields && validateLpnlengthNotEmpty(screen) && validateLpnrollbacknumberNotEmpty(screen)){ 
			if(!validateLpnLength(screen.getLpnrollbacknumber(), screen.getLpnlength())){
				errors.add(new Integer(RULE_LPN_ROLLBACK_NUMBER_COMPLIES_WITH_LPN_LENGTH));
			}
		}
		
		if(doCheckRequiredFields && validateUccvendornumberNotEmpty(screen)){ 
			if (!validateUccVendor(screen.getLpnbarcodeformat(), screen.getUccvendornumber())){
				errors.add(new Integer(RULE_UCC_VENDOR_NUMBER_COMPLIES_WITH_BARCODE_FORMAT));
			}
		}
		
		
		if (doCheckAsOwner && !validateLpnlengthNotEmpty(screen)){
			errors.add(new Integer(RULE_LPN_LENGTH_NOT_EMPTY));
		}
		
		if (!validateStorerkeyNotEmpty(screen)){
			errors.add(new Integer(RULE_STORER_KEY_NOT_EMPTY));
		}
		
		
		if(validateScac_codeNotEmpty(screen)){
			if(!validateScac_codeIsAlpha(screen))
				errors.add(new Integer(RULE_SCAC_CODE_MUST_BE_ALPHNUMERIC));
		}
		
		
		if(isInsert){
			if(doCheckRequiredFields && !validateStorerkeyNotEmpty(screen))
				errors.add(new Integer(RULE_STORER_KEY_NOT_EMPTY));
			else{
				if(validateStorerkeyDoesExist(screen.getStorerkey(), screen.getType(), getContext()))
					errors.add(new Integer(RULE_OWNER_MUST_BE_UNIQUE));
			}
			
		}
		
		
		/*
		 
		 public static final int RULE_CONSIGNEE_KEY_MUST_BE_VALID_CUSTOMER = 46;
		 
		 
		 */
		return errors;
	}//validate
	
	
	
	public static String getErrorMessage(int errorCode, Locale locale, OwnerScreenVO ownerScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){
		//Unique
		case RULE_OWNER_MUST_BE_UNIQUE:
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_SHIPFROM_FIELD_STORERKEY, locale);
			param[1] = ownerScreen.getStorerkey();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SHIPFROM_SCREEN_ERROR_DUPLICATE_SHIPFROM, locale, param);
			
		case RULE_SCAC_CODE_MUST_BE_ALPHNUMERIC:
			return getAlphaNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SCAC_CODE, locale));
			
		case RULE_MAXIMUM_ORDERS_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MAXIMUMORDERS, locale));
			
		case RULE_MINIMUM_PERCENT_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MINIMUMPERCENT, locale));
			
		case RULE_ORDER_DATE_START_DAYS_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERDATESTARTDAYS, locale));
			
		case RULE_ORDER_DATE_END_DAYS_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERDATEENDDAYS, locale));
			
		case RULE_MAXIMUM_ORDERS_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MAXIMUMORDERS, locale));
			
		case RULE_MINIMUM_PERCENT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MINIMUMPERCENT, locale));
			
		case RULE_ORDER_DATE_START_DAYS_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERDATESTARTDAYS, locale));
			
		case RULE_ORDER_DATE_END_DAYS_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERDATEENDDAYS, locale));
			
		case RULE_HI_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTCHARGE, locale));
			
		case RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICECHARGE, locale));
			
		case RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICECHARGE, locale));
			
		case RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICECHARGE, locale));
			
		case RULE_AL_MINIMUM_CHARGE_MUST_BE_A_NUMBER:	
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMCHARGE, locale));
			
		case RULE_HI_MINIMUM_RECEIPT_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTCHARGE, locale));
			
			
		case RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTCHARGE, locale));
			
		case RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICECHARGE, locale));
			
		case RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICECHARGE, locale));
			
		case RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY:
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICECHARGE, locale));
			
		case RULE_AL_MINIMUM_CHARGE_MUST_BE_VALID_CURRENCY:	
			return getNonCurrencyErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMCHARGE, locale));
			
		case RULE_HI_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_HO_MINIMUM_SHIPMENT_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTCHARGE, locale));
			
		case RULE_IS_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_HI_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICECHARGE, locale));
			
		case RULE_IS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTCHARGE, locale));
			
		case RULE_RS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICECHARGE, locale));
			
		case RULE_AL_MINIMUM_CHARGE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMCHARGE, locale));
			
		case RULE_DEFAULT_QC_LOC_MUST_BE_VALID_LOCATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOC, locale), ownerScreen.getDefaultqcloc());
			
		case RULE_DEFAULT_QC_LOC_OUT_MUST_BE_VALID_LOCATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOCOUT, locale), ownerScreen.getDefaultqclocout());
			
		case RULE_DEFAULT_RETURNS_LOC_MUST_BE_VALID_LOCATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTRETURNSLOC, locale), ownerScreen.getDefaultreturnsloc());
			
		case RULE_DEFAULT_PACKING_LOC_MUST_BE_VALID_LOCATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTPACKINGLOCATION, locale), ownerScreen.getDefaultpackinglocation());
			
		case RULE_LPN_START_NUMBER_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER, locale));
			
		case RULE_LPN_START_NUMBER_MUST_BE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER, locale));
			
		case RULE_LPN_START_NUMBER_NOT_ALL_NINE:
			return getNotAllNineErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER, locale),ownerScreen.getLpnstartnumber());
			
		case RULE_UCC_VENDOR_NUMBER_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_UCCVENDORNUMBER, locale));
			
		case RULE_UCC_VENDOR_NUMBER_MUST_BE_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_UCCVENDORNUMBER, locale));
			
		case RULE_NEXT_LPN_NUMBER_GREATER_THAN_OR_EQUAL_LPN_START_NUMBER:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER, locale));
			
		case RULE_LPN_ROLLBACK_NUMBER_GREATER_THAN_OR_EQUAL_NEXT_LPN_NUMBER:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MAXIMUMORDERS, locale));
			
		case RULE_LPN_START_NUMBER_COMPLIES_WITH_LPN_LENGTH:
			return getLengthNotCompliesErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER ,locale),ownerScreen.getLpnlength());
			
		case RULE_NEXT_LPN_NUMBER_COMPLIES_WITH_LPN_LENGTH:
			return getLengthNotCompliesErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_NEXTLPNNUMBER ,locale),ownerScreen.getLpnlength());
			
		case RULE_LPN_ROLLBACK_NUMBER_COMPLIES_WITH_LPN_LENGTH:
			return getLengthNotCompliesErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNROLLBACKNUMBER ,locale),ownerScreen.getLpnlength());
			
		case RULE_UCC_VENDOR_NUMBER_COMPLIES_WITH_BARCODE_FORMAT:
			return getNotCompliesFormatErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_UCCVENDORNUMBER,locale), 
					ownerScreen.getUccvendornumber(), MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BARCODECONFIGKEY,locale));
			
			/**
			 case RULE_CONSIGNEE_KEY_MUST_BE_VALID_CUSTOMER:
			 return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONSIGNEEKEY, locale), ownerScreen.getCon());
			 **/
			
			
		case RULE_LPN_LENGTH_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNLENGTH, locale));
			
		case RULE_STORER_KEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_STORERKEY, locale));
			
			//Field Length Rules
			
		case RULE_LENGTH_ADDRESS1_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS1, locale), "45");
			
		case RULE_LENGTH_ADDRESS2_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS2, locale), "45");
			
		case RULE_LENGTH_ADDRESS3_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS3, locale), "45");
			
		case RULE_LENGTH_ADDRESS4_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS4, locale), "45");
			
		case RULE_LENGTH_ALLOWAUTOCLOSEFORASN_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORASN, locale), "1");
			
		case RULE_LENGTH_ALLOWAUTOCLOSEFORPO_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORPO, locale), "1");
			
		case RULE_LENGTH_ALLOWAUTOCLOSEFORPS_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORPS, locale), "1");
			
		case RULE_LENGTH_ALLOWCOMMINGLEDLPN_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWCOMMINGLEDLPN, locale), "1");
			
		case RULE_LENGTH_ALLOWDUPLICATELICENSEPLATES_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWDUPLICATELICENSEPLATES, locale), "1");
			
		case RULE_LENGTH_ALLOWOVERSHIPMENT_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWOVERSHIPMENT, locale), "1");
			
		case RULE_LENGTH_ALLOWSINGLESCANRECEIVING_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWSINGLESCANRECEIVING, locale), "1");
			
		case RULE_LENGTH_ALLOWSYSTEMGENERATEDLPN_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWSYSTEMGENERATEDLPN, locale), "1");
			
		case RULE_LENGTH_APPLICATIONID_2:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_APPLICATIONID, locale), "2");
			
		case RULE_LENGTH_APPORTIONRULE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_APPORTIONRULE, locale), "10");
			
		case RULE_LENGTH_AUTOCLOSEASN_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_AUTOCLOSEASN, locale), "1");
			
		case RULE_LENGTH_AUTOCLOSEPO_1:			
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_AUTOCLOSEPO, locale), "1");
			
		case RULE_LENGTH_AUTOPRINTLABELLPN_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_AUTOPRINTLABELLPN, locale), "1");
			
		case RULE_LENGTH_AUTOPRINTLABELPUTAWAY_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_AUTOPRINTLABELPUTAWAY, locale), "1");
			
		case RULE_LENGTH_B_ADDRESS1_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_ADDRESS1, locale), "45");
			
		case RULE_LENGTH_B_ADDRESS2_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_ADDRESS2, locale), "45");
			
		case RULE_LENGTH_B_ADDRESS3_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_ADDRESS3, locale), "45");
			
		case RULE_LENGTH_B_ADDRESS4_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_ADDRESS4, locale), "45");
			
		case RULE_LENGTH_B_CITY_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_CITY, locale), "45");
			
		case RULE_LENGTH_B_COMPANY_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_COMPANY, locale), "45");
			
		case RULE_LENGTH_B_CONTACT1_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_CONTACT1, locale), "30");
			
		case RULE_LENGTH_B_CONTACT2_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_CONTACT2, locale), "30");
			
		case RULE_LENGTH_B_COUNTRY_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_COUNTRY, locale), "30");
			
		case RULE_LENGTH_B_EMAIL1_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_EMAIL1, locale), "60");
			
		case RULE_LENGTH_B_EMAIL2_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_EMAIL2, locale), "60");
			
		case RULE_LENGTH_B_FAX1_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_FAX1, locale), "18");
			
		case RULE_LENGTH_B_FAX2_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_FAX2, locale), "18");
			
		case RULE_LENGTH_B_ISOCNTRYCODE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISOCNTRYCODE, locale), "10");
			
		case RULE_LENGTH_B_PHONE1_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_PHONE1, locale), "18");
			
		case RULE_LENGTH_B_PHONE2_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_PHONE2, locale), "18");
		
		// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
		case RULE_LENGTH_B_STATE_25:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_STATE, locale), "25");
			
		case RULE_LENGTH_B_ZIP_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_B_ZIP, locale), "18");
			
		case RULE_LENGTH_BARCODECONFIGKEY_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BARCODECONFIGKEY, locale), "18");
			
		case RULE_LENGTH_CALCULATEPUTAWAYLOCATION_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CALCULATEPUTAWAYLOCATION, locale), "10");
			
		case RULE_LENGTH_CARTONGROUP_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CARTONGROUP, locale), "10");
			
		case RULE_LENGTH_CaseLabelType_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CASELABELTYPE, locale), "10");
			
		case RULE_LENGTH_CCADJBYRF_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CCADJBYRF, locale), "10");
			
		case RULE_LENGTH_CCDISCREPANCYRULE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CCDISCREPANCYRULE, locale), "10");
			
		case RULE_LENGTH_CCSKUXLOC_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CCSKUXLOC, locale), "1");
			
		case RULE_LENGTH_CITY_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CITY, locale), "45");
			
		case RULE_LENGTH_COMPANY_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_COMPANY, locale), "45");
			
		case RULE_LENGTH_CONTACT1_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONTACT1, locale), "30");
			
		case RULE_LENGTH_CONTACT2_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONTACT2, locale), "30");
			
		case RULE_LENGTH_COUNTRY_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_COUNTRY, locale), "30");
			
		case RULE_LENGTH_CREATEPATASKONRFRECEIPT_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CREATEPATASKONRFRECEIPT, locale), "10");
			
		case RULE_LENGTH_CREDITLIMIT_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CREDITLIMIT, locale), "18");
			
		case RULE_LENGTH_CWOFLAG_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CWOFLAG, locale), "1");
			
		case RULE_LENGTH_DEFAULTPACKINGLOCATION_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTPACKINGLOCATION, locale), "10");
			
		case RULE_LENGTH_DEFAULTPUTAWAYSTRATEGY_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTPUTAWAYSTRATEGY, locale), "10");
			
		case RULE_LENGTH_DEFAULTQCLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOC, locale), "10");
			
		case RULE_LENGTH_DEFAULTQCLOCOUT_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOCOUT, locale), "10");
			
		case RULE_LENGTH_DEFAULTRETURNSLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTRETURNSLOC, locale), "10");
			
		case RULE_LENGTH_DEFAULTROTATION_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTROTATION, locale), "1");
			
		case RULE_LENGTH_DEFAULTSKUROTATION_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTSKUROTATION, locale), "10");
			
		case RULE_LENGTH_DEFAULTSTRATEGY_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTSTRATEGY, locale), "10");
			
		case RULE_LENGTH_DESCRIPTION_50:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DESCRIPTION, locale), "50");
			
		case RULE_LENGTH_DUPCASEID_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DUPCASEID, locale), "1");
			
		case RULE_LENGTH_EMAIL1_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EMAIL1, locale), "60");
			
		case RULE_LENGTH_EMAIL2_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EMAIL2, locale), "60");
			
		case RULE_LENGTH_ENABLEOPPXDOCK_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ENABLEOPPXDOCK, locale), "1");
			
		case RULE_LENGTH_ENABLEPACKINGDEFAULT_1: 
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ENABLEPACKINGDEFAULT, locale), "1");
			
		case RULE_LENGTH_FAX1_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_FAX1, locale), "18");
			
		case RULE_LENGTH_FAX2_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_FAX2, locale), "18");
			
		case RULE_LENGTH_GENERATEPACKLIST_1: 
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_GENERATEPACKLIST, locale), "1");
			
		case RULE_LENGTH_INSPECTATPACK_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_INSPECTATPACK, locale), "1");
			
		case RULE_LENGTH_ISOCNTRYCODE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISOCNTRYCODE, locale), "10");
			
		case RULE_LENGTH_LPNBARCODEFORMAT_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNBARCODEFORMAT, locale), "60");
			
		case RULE_LENGTH_LPNBARCODESYMBOLOGY_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNBARCODESYMBOLOGY, locale), "60");
			
		case RULE_LENGTH_LPNROLLBACKNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNROLLBACKNUMBER, locale), "18");
			
		case RULE_LENGTH_LPNSTARTNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER, locale), "18");
			
		case RULE_LENGTH_MULTIZONEPLPA_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MULTIZONEPLPA, locale), "1");
			
		case RULE_LENGTH_NEXTLPNNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_NEXTLPNNUMBER, locale), "18");
			
		case RULE_LENGTH_NOTES1_2147483647:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_NOTES1, locale), "2147483647");
			
		case RULE_LENGTH_NOTES2_2147483647:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_NOTES2, locale), "2147483647");
			
		case RULE_LENGTH_OPPORDERSTRATEGYKEY_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_OPPORDERSTRATEGYKEY, locale), "10");
			
		case RULE_LENGTH_ORDERBREAKDEFAULT_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERBREAKDEFAULT, locale), "1");
			
		case RULE_LENGTH_ORDERTYPERESTRICT01_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT01, locale), "10");
			
		case RULE_LENGTH_ORDERTYPERESTRICT02_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT02, locale), "10");
			
		case RULE_LENGTH_ORDERTYPERESTRICT03_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT03, locale), "10");
			
		case RULE_LENGTH_ORDERTYPERESTRICT04_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT04, locale), "10");
			
		case RULE_LENGTH_ORDERTYPERESTRICT05_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT05, locale), "10");
			
		case RULE_LENGTH_ORDERTYPERESTRICT06_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT06, locale), "10");
			
		case RULE_LENGTH_PACKINGVALIDATIONTEMPLATE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PACKINGVALIDATIONTEMPLATE, locale), "10");
			
		case RULE_LENGTH_PHONE1_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PHONE1, locale), "18");
			
		case RULE_LENGTH_PHONE2_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PHONE2, locale), "18");
			
		case RULE_LENGTH_PICKCODE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PICKCODE, locale), "10");
			
		case RULE_LENGTH_PISKUXLOC_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PISKUXLOC, locale), "1");
			
		case RULE_LENGTH_RECEIPTVALIDATIONTEMPLATE_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RECEIPTVALIDATIONTEMPLATE, locale), "18");
			
		case RULE_LENGTH_ROLLRECEIPT_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ROLLRECEIPT, locale), "1");
			
		case RULE_LENGTH_SCAC_CODE_4:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SCAC_CODE, locale), "4");
			
		case RULE_LENGTH_SKUSETUPREQUIRED_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SKUSETUPREQUIRED, locale), "1");
			
		// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
		case RULE_LENGTH_STATE_25:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_STATE, locale), "25");
			
		case RULE_LENGTH_STATUS_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_STATUS, locale), "18");
			
		case RULE_LENGTH_STORERKEY_15:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_STORERKEY, locale), "15");
			
		case RULE_LENGTH_SUSR1_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR1, locale), "30");
			
		case RULE_LENGTH_SUSR2_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR2, locale), "30");
			
		case RULE_LENGTH_SUSR3_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR3, locale), "30");
			
		case RULE_LENGTH_SUSR4_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR4, locale), "30");
			
		case RULE_LENGTH_SUSR5_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR5, locale), "30");
			
		case RULE_LENGTH_SUSR6_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SUSR6, locale), "30");
			
		case RULE_LENGTH_TITLE1_50:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TITLE1, locale), "50");
			
		case RULE_LENGTH_TITLE2_50:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TITLE2, locale), "50");
			
		case RULE_LENGTH_TRACKINVENTORYBY_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TRACKINVENTORYBY, locale), "1");
			
			
		case RULE_LENGTH_TYPE_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TYPE, locale), "30");
			
		case RULE_LENGTH_UCCVENDORNUMBER_9:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_UCCVENDORNUMBER, locale), "9");
			
		case RULE_LENGTH_VAT_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_VAT, locale), "18");
			
		case RULE_LENGTH_WHSEID_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_WHSEID, locale), "30");
			
		case RULE_LENGTH_ZIP_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ZIP, locale), "18");
			
			//Attribute Domain Rules
		case RULE_ATTR_DOM_ISO_COUNTRY_CODE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISOCNTRYCODE, locale), ownerScreen.getIsocntrycode());
			
		case RULE_ATTR_DOM_CARTON_GROUP:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CARTONGROUP, locale), ownerScreen.getCartongroup());
			
		case RULE_ATTR_DOM_ORDER_SEQUENCE_STRATEGY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_OPPORDERSTRATEGYKEY, locale), ownerScreen.getOpporderstrategykey());
			
		case RULE_ATTR_DOM_AUTOMATIC_APPORTIONMENT_RULE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_APPORTIONRULE, locale), ownerScreen.getApportionrule());
			
		case RULE_ATTR_DOM_ORDER_TYPE_1:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT01, locale), ownerScreen.getOrdertyperestrict01());
			
		case RULE_ATTR_DOM_ORDER_TYPE_2:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT02, locale), ownerScreen.getOrdertyperestrict02());
			
		case RULE_ATTR_DOM_ORDER_TYPE_3:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT03, locale), ownerScreen.getOrdertyperestrict03());
			
		case RULE_ATTR_DOM_ORDER_TYPE_4:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT04, locale), ownerScreen.getOrdertyperestrict04());
			
		case RULE_ATTR_DOM_ORDER_TYPE_5:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT05, locale), ownerScreen.getOrdertyperestrict05());
			
		case RULE_ATTR_DOM_ORDER_TYPE_6:	
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT06, locale), ownerScreen.getOrdertyperestrict06());
			
		case RULE_ATTR_DOM_INVOICE_NUMBER_STRATEGY:	
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_INVOICENUMBERSTRATEGY, locale), ownerScreen.getInvoiceNumberStrategy());
			
		case RULE_ATTR_DOM_BILLING_GROUP:	
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BILLINGGROUP, locale), ownerScreen.getBillingGroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_1:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTTAXGROUP, locale), ownerScreen.getHiminimumreceipttaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_2:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTTAXGROUP, locale), ownerScreen.getHominimumshipmenttaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_3:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTTAXGROUP, locale), ownerScreen.getIsminimumreceipttaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_4:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICETAXGROUP, locale), ownerScreen.getHIMinimumInvoiceTaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_5:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICETAXGROUP, locale), ownerScreen.getISMinimumInvoiceTaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_6:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICETAXGROUP, locale), ownerScreen.getRSMinimumInvoiceTaxgroup());
			
		case RULE_ATTR_DOM_TAX_GROUP_7:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMTAXGROUP, locale), ownerScreen.getAllMinimumInvoiceTaxgroup());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_1:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTGLDIST, locale), ownerScreen.getHiminimumreceiptgldist());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_2:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTGLDIST, locale), ownerScreen.getHominimumshipmentgldist());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_3:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTGLDIST, locale), ownerScreen.getIsminimumreceiptgldist());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_4:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICEGLDIST, locale), ownerScreen.getHIMinimumInvoiceGLDistribution());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_5:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICEGLDIST, locale), ownerScreen.getISMinimumInvoiceGLDistribution());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_6:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICEGLDIST, locale), ownerScreen.getRSMinimumInvoiceGLDistribution());
			
		case RULE_ATTR_DOM_GL_DISTRIBUTION_7:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMGLDIST, locale), ownerScreen.getAllMinimumInvoiceGLDistribution());
			
		case RULE_ATTR_DOM_DEFAULT_ITEM_ROTATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTSKUROTATION, locale), ownerScreen.getDefaultItemRotation());
			
		case RULE_ATTR_DOM_DEFAULT_ROTATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTROTATION, locale), ownerScreen.getDefaultrotation());
			
		case RULE_ATTR_DOM_DEFAULT_STRATEGY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTSTRATEGY, locale), ownerScreen.getDefaultstrategy());
			
		case RULE_ATTR_DOM_DEFAULT_PUTAWAY_STRATEGY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFAULTPUTAWAYSTRATEGY, locale), ownerScreen.getDefaultputawaystrategy());
			
		case RULE_ATTR_DOM_CREATE_PUTAWAY_TASK_ON_RF_RECEIPT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CREATEPATASKONRFRECEIPT, locale), ownerScreen.getCreatepataskonrfreceipt());
			
		case RULE_ATTR_DOM_CALCULATE_PUTAWAY_LOCATION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CALCULATEPUTAWAYLOCATION, locale), ownerScreen.getCalculateputawaylocation());
			
		case RULE_ATTR_DOM_ASSIGNMENT_ORDER_BREAK_DEFAULT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ORDERBREAKDEFAULT, locale), ownerScreen.getOrderbreakdefault());
			
		case RULE_ATTR_DOM_PACKING_VALIDATION_TEMPLATE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_PACKINGVALIDATIONTEMPLATE, locale), ownerScreen.getPackingvalidationtemplate());
			
		case RULE_ATTR_DOM_LPN_BARCODE_SYMBOLOGY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNBARCODESYMBOLOGY, locale), ownerScreen.getLpnbarcodesymbology());
			
		case RULE_ATTR_DOM_LPN_BARCODE_FORMAT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNBARCODEFORMAT, locale), ownerScreen.getLpnbarcodeformat());
			
		case RULE_ATTR_DOM_LPN_LENGTH:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_LPNLENGTH, locale), ownerScreen.getLpnlength());
			
			
		case RULE_ATTR_DOM_CASE_LABEL_TYPE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CASELABELTYPE, locale), ownerScreen.getCaselabeltype());
			
		case RULE_ATTR_DOM_SSCC_1ST_DIGIT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SSCC1STDIGIT, locale), ownerScreen.getSscc1stdigit());
			
		case RULE_ATTR_DOM_DEFAULT_DISCREPANCY_HANDLING_RULE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CCDISCREPANCYRULE, locale), ownerScreen.getCcdiscrepancyrule());
			
		case RULE_ATTR_DOM_CYCLE_COUNT_ADJUSTING_DURING_RF:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CCADJBYRF, locale), ownerScreen.getCcadjbyrf());
			
		case RULE_ATTR_DOM_ALLOW_COMMINGLED_LPN:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWCOMMINGLEDLPN, locale), ownerScreen.getAllowcommingledlpn());
			
		case RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_ASN:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORASN, locale), ownerScreen.getAllowautocloseforasn());
			
		case RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_PO:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORPO, locale), ownerScreen.getAllowautocloseforpo());
			
		case RULE_ATTR_DOM_ALLOW_SYSTEM_GENERATED_LPN:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWSYSTEMGENERATEDLPN, locale), ownerScreen.getAllowsystemgeneratedlpn());
			
		case RULE_ATTR_DOM_ALLOW_SINGLE_SCAN_RECEIVING:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALLOWSINGLESCANRECEIVING, locale), ownerScreen.getAllowsinglescanreceiving());
			
		case RULE_ATTR_DOM_RECEIPT_VALIDATION_TEMPLATE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RECEIPTVALIDATIONTEMPLATE, locale), ownerScreen.getReceiptvalidationtemplate());
			
		case RULE_ATTR_DOM_BARCODE_CONFIGURATION_KEY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BARCODECONFIGKEY, locale), ownerScreen.getBarcodeconfigkey());
			
		case RULE_ATTR_DOM_DEFAULT_TRACKING_FOR_CASE_RECEIPTS:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TRACKINVENTORYBY, locale), ownerScreen.getTrackinventoryby());
			
		case RULE_ATTR_DOM_DUPLICATE_CASE_IDS:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DUPCASEID, locale), ownerScreen.getDupcaseid());
			
			
		}
		
		return errorMsg;
	}

}
