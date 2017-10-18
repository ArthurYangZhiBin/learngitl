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

import java.util.ArrayList;
import java.util.Locale;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.owner.OwnerScreenVO;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class ASNScreenValidator extends BaseScreenValidator{

	public ASNScreenValidator(WMSValidationContext context) {
		super(context);
		
	}

	
	
	//Data Type field rules
	public static final int RULE_BILLEDCONTAINERQTY_MUST_BE_A_NUMBER= 0 ;
	public static final int RULE_CONTAINERQTY_MUST_BE_A_NUMBER= 1 ;
	public static final int RULE_GROSSWEIGHT_MUST_BE_A_NUMBER= 2 ;
	public static final int RULE_OPENQTY_MUST_BE_A_NUMBER= 3 ;
	public static final int RULE_TOTALVOLUME_MUST_BE_A_NUMBER= 4 ;
	
	public static final int RULE_BILLEDCONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO= 5 ;
	public static final int RULE_CONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO= 6 ;
	public static final int RULE_GROSSWEIGHT_GREATER_THAN_OR_EQUAL_ZERO= 7 ;
	public static final int RULE_OPENQTY_GREATER_THAN_OR_EQUAL_ZERO = 8 ;
	public static final int RULE_TOTALVOLUME_GREATER_THAN_OR_EQUAL_ZERO = 9 ;
	
	//Foreign Keys that must exist ( not optional), intersection with not null fields
	public static final int RULE_STORERKEY_MUST_EXIST = 10; //storer
	public static final int RULE_STATUS_MUST_EXIST = 11;
	public static final int RULE_TYPE_MUST_EXIST = 12;

	
	
	//Required field rules - Not null
	public static final int RULE_STORERKEY_NOT_EMPTY = 13;
	public static final int RULE_STATUS_NOT_EMPTY = 14;
	public static final int RULE_TYPE_NOT_EMPTY = 15;
	
	public static final int RULE_RECEIPTKEY_NOT_EMPTY = 16;
	public static final int RULE_EXTERNRECEIPTKEY_NOT_EMPTY = 17;
	public static final int RULE_RECEIPTGROUP_NOT_EMPTY = 18;
	public static final int RULE_FORTE_FLAG_NOT_EMPTY = 19;
	public static final int RULE_EFFECTIVEDATE_NOT_EMPTY = 20;
	public static final int RULE_EXPECTEDRECEIPTDATE_NOT_EMPTY = 21;
	public static final int RULE_ARRIVALDATETIME_NOT_EMPTY = 22;
	public static final int RULE_LOTTABLEMATCHREQUIRED_NOT_EMPTY = 23;


	//Attribute Domain rules - Foreign keys that are optional
	public static final int RULE_POKEY_IN_ATTR_DOM = 24;
	public static final int RULE_TRACKINVENTORYBY_IN_ATTR_DOM = 25; //codelkup
	public static final int RULE_CARRIERKEY_IN_ATTR_DOM = 26; //storer
	public static final int RULE_TERMSNOTE_IN_ATTR_DOM = 27; //codelkup
	public static final int RULE_INCOTERMS_IN_ATTR_DOM = 28; //codelkup
	public static final int RULE_ORIGINCOUNTRY_IN_ATTR_DOM = 29;
	public static final int RULE_DESTINATIONCOUNTRY_IN_ATTR_DOM = 30;
	public static final int RULE_TRANSPORTATIONMODE_IN_ATTR_DOM = 31;
	public static final int RULE_CONTAINERTYPE_IN_ATTR_DOM = 32;
	public static final int RULE_SUPPLIERCODE_IN_ATTR_DOM = 33; //storer
	public static final int RULE_SHIPFROMISOCOUNTRY_IN_ATTR_DOM = 34; //storer

	//Unique
	public static final int RULE_RECEIPTKEY_MUST_BE_UNIQUE = 35;

	//Field Length Rules
	public static final int RULE_LENGTH_WHSEID_30 = 36;
	public static final int RULE_LENGTH_RECEIPTKEY_10 = 37;
	public static final int RULE_LENGTH_EXTERNRECEIPTKEY_32 = 38;
	public static final int RULE_LENGTH_RECEIPTGROUP_20 = 39;
	public static final int RULE_LENGTH_STORERKEY_15 = 40;
	public static final int RULE_LENGTH_POKEY_18 = 41;
	public static final int RULE_LENGTH_CARRIERKEY_15 = 42;
	public static final int RULE_LENGTH_CARRIERNAME_45 = 43;
	public static final int RULE_LENGTH_CARRIERADDRESS1_45 = 44;
	public static final int RULE_LENGTH_CARRIERADDRESS2_45 = 45;
	public static final int RULE_LENGTH_CARRIERCITY_45 = 46;
	public static final int RULE_LENGTH_CARRIERSTATE_2 = 47;
	public static final int RULE_LENGTH_CARRIERZIP_18 = 48;
	public static final int RULE_LENGTH_CARRIERREFERENCE_18 = 49;

	public static final int RULE_LENGTH_WAREHOUSEREFERENCE_18 = 50;
	public static final int RULE_LENGTH_ORIGINCOUNTRY_30 = 51;
	public static final int RULE_LENGTH_DESTINATIONCOUNTRY_30 = 101;
	public static final int RULE_LENGTH_VEHICLENUMBER_18 = 52;
	public static final int RULE_LENGTH_PLACEOFLOADING_18 = 53;
	public static final int RULE_LENGTH_PLACEOFDISCHARGE_30 = 54;
	public static final int RULE_LENGTH_PLACEOFDELIVERY_30 = 55;
	public static final int RULE_LENGTH_INCOTERMS_10 = 56;
	public static final int RULE_LENGTH_TERMSNOTE_18 = 57;
	public static final int RULE_LENGTH_CONTAINERKEY_18 = 58;
	public static final int RULE_LENGTH_SIGNATORY_18 = 59;

	public static final int RULE_LENGTH_PLACEOFISSUE_18 = 60;
	public static final int RULE_LENGTH_FORTE_FLAG_6 = 61;
	public static final int RULE_LENGTH_STATUS_10 = 62;
	public static final int RULE_LENGTH_CONTAINERTYPE_20 = 63;
	public static final int RULE_LENGTH_TRANSPORTATIONMODE_30 = 64;
	public static final int RULE_LENGTH_EXTERNALRECEIPTKEY2_20 = 65;
	public static final int RULE_LENGTH_SUSR1_30 = 66;
	public static final int RULE_LENGTH_SUSR2_30 = 67;
	public static final int RULE_LENGTH_SUSR3_30 = 68;
	public static final int RULE_LENGTH_SUSR4_30 = 69;
	public static final int RULE_LENGTH_SUSR5_30 = 70;
	public static final int RULE_LENGTH_TYPE_10 = 71;
	public static final int RULE_LENGTH_RMA_30 = 72;

	public static final int RULE_LENGTH_ALLOWAUTORECEIPT_1 = 73;
	public static final int RULE_LENGTH_TRACKINVENTORYBY_1 = 74;
	public static final int RULE_LENGTH_CARRIERCOUNTRY_30 = 75;
	public static final int RULE_LENGTH_CARRIERPHONE_18 = 76;
	public static final int RULE_LENGTH_DRIVERNAME_25 = 77;
	public static final int RULE_LENGTH_TRAILERNUMBER_25 = 78;
	public static final int RULE_LENGTH_TRAILEROWNER_25 = 79;
	public static final int RULE_LENGTH_LOTTABLEMATCHREQUIRED_1 = 80;
	public static final int RULE_LENGTH_ADVICENUMBER_30 = 81;
	public static final int RULE_LENGTH_PACKINGSLIPNUMBER_50 = 82;
	public static final int RULE_LENGTH_RECEIPTID_32 = 83;
	public static final int RULE_LENGTH_SUPPLIERCODE_15 = 84;
	public static final int RULE_LENGTH_SUPPLIERNAME_45 = 85;
	public static final int RULE_LENGTH_SHIPFROMADDRESSLINE1_45 = 86;
	public static final int RULE_LENGTH_SHIPFROMADDRESSLINE2_45 = 87;
	public static final int RULE_LENGTH_SHIPFROMADDRESSLINE3_45 = 88;
	public static final int RULE_LENGTH_SHIPFROMADDRESSLINE4_45 = 89;
	public static final int RULE_LENGTH_SHIPFROMCITY_45 = 90;
	public static final int RULE_LENGTH_SHIPFROMSTATE_2 = 91;
	public static final int RULE_LENGTH_SHIPFROMZIP_18 = 92;
	public static final int RULE_LENGTH_SHIPFROMISOCOUNTRY_3 = 93;
	public static final int RULE_LENGTH_SHIPFROMCONTACT_30 = 94;
	public static final int RULE_LENGTH_SHIPFROMPHONE_18 = 95;
	public static final int RULE_LENGTH_SHIPFROMEMAIL_60 = 96;	
	
	
	public ArrayList validate(ASNScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doAssumeDefaults = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ASSUME_DEFAULTS);
		
		
		//Validate Field Lengths
		if(doCheckFieldLength){			
			if(!validateAdvicenumberLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADVICENUMBER_30));	
			}
			
			if (!validateAllowautoreceiptLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOWAUTORECEIPT_1));
			}
			
			if (!validateCarrieraddress1LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERADDRESS1_45));
			}

			if (!validateCarrieraddress2LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERADDRESS2_45));
			}

			if (!validateCarriercityLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERCITY_45));
			}
			
			if (!validateCarriercountryLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERCOUNTRY_30));
			}
			
			if (!validateCarrierkeyLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERKEY_15));
			}
			
			if (!validateCarriernameLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERNAME_45));
			}


			if (!validateCarrierphoneLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERPHONE_18));
			}
			
			if (!validateCarrierreferenceLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERREFERENCE_18));
			}
			
			if (!validateCarrierstateLengthIs2OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERSTATE_2));
			}
			
			if (!validateCarrierzipLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARRIERZIP_18));
			}
			
			if (!validateContainerkeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONTAINERKEY_18));
			}
			
			if (!validateContainertypeLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONTAINERTYPE_20));
			}
			
			if (!validateDestinationcountryLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DESTINATIONCOUNTRY_30));
			}
			
			if (!validateDrivernameLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DRIVERNAME_25));
			}
			
			if (!validateExternalreceiptkey2LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXTERNALRECEIPTKEY2_20));
			}
			
			if (!validateExternreceiptkeyLengthIs32OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXTERNRECEIPTKEY_32));
			}
			
			if (!validateForte_flagLengthIs6OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_FORTE_FLAG_6));
			}
			
			if (!validateIncotermsLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INCOTERMS_10));
			}
			
			if (!validateLottablematchrequiredLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLEMATCHREQUIRED_1));
			}
			
			if (!validateOrigincountryLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ORIGINCOUNTRY_30));
			}
			
			if (!validatePackingslipnumberLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKINGSLIPNUMBER_50));
			}
			
			if (!validatePlaceofdeliveryLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PLACEOFDELIVERY_30));
			}
			
			if (!validatePlaceofdischargeLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PLACEOFDISCHARGE_30));
			}
			
			if (!validatePlaceofissueLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PLACEOFISSUE_18));
			}
			
			if (!validatePlaceofloadingLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PLACEOFLOADING_18));
			}
			
			if (!validatePokeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_POKEY_18));
			}
			
			if (!validateReceiptgroupLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTGROUP_20));
			}
			
			if (!validateReceiptidLengthIs32OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTID_32));
			}
			
			if (!validateReceiptkeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTKEY_10));
			}
			
			if (!validateRmaLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RMA_30));
			}
			
			if (!validateShipfromaddressline1LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMADDRESSLINE1_45));
			}
			
			if (!validateShipfromaddressline2LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMADDRESSLINE2_45));
			}
			
			if (!validateShipfromaddressline3LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMADDRESSLINE3_45));
			}
			
			if (!validateShipfromaddressline4LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMADDRESSLINE4_45));
			}
			
			if (!validateShipfromcityLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMCITY_45));
			}
			
			if (!validateShipfromcontactLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMCONTACT_30));
			}
			
			if (!validateShipfromemailLengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMEMAIL_60));
			}
			
			if (!validateShipfromisocountryLengthIs3OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMISOCOUNTRY_3));
			}
			
			if (!validateShipfromphoneLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMPHONE_18));
			}
			
			if (!validateShipfromstateLengthIs2OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMSTATE_2));
			}
			
			if (!validateShipfromzipLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPFROMZIP_18));
			}
			
			if (!validateSignatoryLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SIGNATORY_18));
			}
			
			if (!validateStatusLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STATUS_10));
			}
		
			if (!validateStorerkeyLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STORERKEY_15));
			}
			
			if (!validateSuppliercodeLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUPPLIERCODE_15));
			}
			
			if (!validateSuppliernameLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUPPLIERNAME_45));
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
			
			if (!validateTermsnoteLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TERMSNOTE_18));
			}
			
			if (!validateTrackinventorybyLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRACKINVENTORYBY_1));
			}
			
			if (!validateTrailernumberLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRAILERNUMBER_25));
			}
		
			if (!validateTrailerownerLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRAILEROWNER_25));
			}
			
			if (!validateTransportationmodeLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRANSPORTATIONMODE_30));
			}
		
			if (!validateTypeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TYPE_10));
			}
			
			if (!validateVehiclenumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VEHICLENUMBER_18));
			}
			
			if (!validateWarehousereferenceLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WAREHOUSEREFERENCE_18));
			}
			
			if (!validateWhseidLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WHSEID_30));
			}
		}
		
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
		
			
			if(!validatePokeyInAttrDom(screen)){
				errors.add(new Integer(RULE_POKEY_IN_ATTR_DOM  ));		
			}
			
			if(!validateTrackinventoryInAttrDom(screen)){
				errors.add(new Integer(RULE_TRACKINVENTORYBY_IN_ATTR_DOM));
			}
			
			if(!validateCarrierkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_CARRIERKEY_IN_ATTR_DOM));
			}
			
			if(!validateTermsnoteInAttrDom(screen)){
				errors.add(new Integer(RULE_TERMSNOTE_IN_ATTR_DOM));
			}
			
			if (!validateIncotermsInAttrDom(screen)){
				errors.add(new Integer(RULE_INCOTERMS_IN_ATTR_DOM));
			}
			
			if (!validateOrigincountryInAttrDom(screen)){
				errors.add(new Integer(RULE_ORIGINCOUNTRY_IN_ATTR_DOM));
			}
			
			if(!validateDestinationcountryInAttrDom(screen)){
				errors.add(new Integer(RULE_DESTINATIONCOUNTRY_IN_ATTR_DOM));
			}
			
			if(!validateTransportationmodeInAttrDom(screen)){
				errors.add(new Integer(RULE_TRANSPORTATIONMODE_IN_ATTR_DOM));
			}
			
			if(!validateContainertypeInAttrDom(screen)){
				errors.add(new Integer(RULE_CONTAINERTYPE_IN_ATTR_DOM));
			}
			
			if (!validateSuppliercodeInAttrDom(screen)){
				errors.add(new Integer(RULE_SUPPLIERCODE_IN_ATTR_DOM));
			}
			
			if(!validateShipfromisocountryInAttrDom(screen)){
				errors.add(new Integer(RULE_SHIPFROMISOCOUNTRY_IN_ATTR_DOM));
			}
		}
		
		
		if(doCheckRequiredFields && !validateStorerkeyNotEmpty(screen))
			errors.add(new Integer(RULE_STORERKEY_NOT_EMPTY));
		else{
			if(!validateStorerkeyDoesExist(screen))
				errors.add(new Integer(RULE_STORERKEY_MUST_EXIST));
		}

		if(doCheckRequiredFields && !validateStatusNotEmpty(screen))
			errors.add(new Integer(RULE_STATUS_NOT_EMPTY));
		else{
			if(!validateStatusDoesExist(screen))
				errors.add(new Integer(RULE_STATUS_MUST_EXIST));
		}
		

		if(doCheckRequiredFields && !validateTypeNotEmpty(screen))
			errors.add(new Integer(RULE_TYPE_NOT_EMPTY));
		else{
			if(!validateTypeDoesExist(screen))
				errors.add(new Integer(RULE_TYPE_MUST_EXIST));
		}

		if(doCheckRequiredFields && !validateReceiptkeyNotEmpty(screen)){
			errors.add(new Integer(RULE_RECEIPTKEY_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateExternreceiptkeyNotEmpty(screen)){
			errors.add(new Integer(RULE_EXTERNRECEIPTKEY_NOT_EMPTY));
		}

		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateReceiptgroupNotEmpty(screen)){
				errors.add(new Integer(RULE_RECEIPTGROUP_NOT_EMPTY));
			}
		}
		if(doCheckRequiredFields && !validateEffectivedateNotEmpty(screen)){
			errors.add(new Integer(RULE_EFFECTIVEDATE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateExpectedreceiptdateNotEmpty(screen)){
			errors.add(new Integer(RULE_EXPECTEDRECEIPTDATE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateArrivaldatetimeNotEmpty(screen)){
			errors.add(new Integer(RULE_ARRIVALDATETIME_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateLottablematchrequiredNotEmpty(screen)){
			errors.add(new Integer(RULE_LOTTABLEMATCHREQUIRED_NOT_EMPTY));
		}
		

		
		//Numeric validations
		if(doCheckRequiredFields && validateBillcontainerqtyNotEmpty(screen)){
			if(!validateBillcontainerqtyIsANumber(screen))
				errors.add(new Integer(RULE_BILLEDCONTAINERQTY_MUST_BE_A_NUMBER));
			else{
				if(!validateBillcontainerqtyGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_BILLEDCONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && validateContainerqtyNotEmpty(screen)){
			if(!validateContainerqtyIsANumber(screen))
				errors.add(new Integer(RULE_CONTAINERQTY_MUST_BE_A_NUMBER));
			else{
				if(!validateContainerqtyGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_CONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && validateGrossweightNotEmpty(screen)){
			if(!validateGrossweightIsANumber(screen))
				errors.add(new Integer(RULE_GROSSWEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateGrossweightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_GROSSWEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && validateOpenqtyNotEmpty(screen)){
			if(!validateOpenqtyIsANumber(screen))
				errors.add(new Integer(RULE_OPENQTY_MUST_BE_A_NUMBER));
			else{
				if(!validateOpenqtyGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_OPENQTY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && validateTotalvolumeNotEmpty(screen)){
			if(!validateTotalvolumeIsANumber(screen))
				errors.add(new Integer(RULE_TOTALVOLUME_MUST_BE_A_NUMBER));
			else{
				if(!validateTotalvolumeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_TOTALVOLUME_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		
		
		
		
		if(isInsert){
			if(doCheckRequiredFields && !validateReceiptkeyNotEmpty(screen))
				errors.add(new Integer(RULE_RECEIPTKEY_NOT_EMPTY));
			else{
				if(validateReceiptkeyDoesExist(screen))
					errors.add(new Integer(RULE_RECEIPTKEY_MUST_BE_UNIQUE));
			}
			
		}
		
		
		return errors;
	}//validate
	

	private boolean validateWhseidLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getWhseid()))
			return true;
		return screen.getWhseid().length() < 31;			
	}

	private boolean validateReceiptkeyLengthIs10OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getReceiptkey()))
			return true;
		return screen.getReceiptkey().length() < 11;			
	}

	private boolean validateExternreceiptkeyLengthIs32OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getExternreceiptkey()))
			return true;
		return screen.getExternreceiptkey().length() < 33;			
	}
	
	private boolean validateReceiptgroupLengthIs20OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getReceiptgroup()))
			return true;
		return screen.getReceiptkey().length() < 21;			
	}
	
	private boolean validateStorerkeyLengthIs15OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getStorerkey()))
			return true;
		return screen.getStorerkey().length() < 16;			
	}

	private boolean validatePokeyLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPokey()))
			return true;
		return screen.getPokey().length() < 19;			
	}


	private boolean validateCarrierkeyLengthIs15OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrierkey()))
			return true;
		return screen.getCarrierkey().length() < 16;			
	}
	
	private boolean validateCarriernameLengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarriername()))
			return true;
		return screen.getCarriername().length() < 46;			
	}
	
	private boolean validateCarrieraddress1LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrieraddress1()))
			return true;
		return screen.getCarrieraddress1().length() < 46;			
	}

	private boolean validateCarrieraddress2LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrieraddress2()))
			return true;
		return screen.getCarrieraddress2().length() < 46;			
	}

	private boolean validateCarriercityLengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarriercity()))
			return true;
		return screen.getCarriercity().length() < 46;			
	}

	private boolean validateCarrierstateLengthIs2OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarriercity()))
			return true;
		return screen.getCarriercity().length() < 3;
		
	}

	private boolean validateCarrierzipLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrierzip()))
			return true;
		return screen.getCarrierzip().length() < 19;			
	}

	private boolean validateCarrierreferenceLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrierreference()))
			return true;
		return screen.getCarrierreference().length() < 19;			
	}



	private boolean validateWarehousereferenceLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getWarehousereference()))
			return true;
		return screen.getWarehousereference().length() < 19;			
	}

	
	private boolean validateOrigincountryLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getOrigincountry()))
			return true;
		return screen.getOrigincountry().length() < 31;			
	}

	private boolean validateDestinationcountryLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getDestinationcountry()))
			return true;
		return screen.getDestinationcountry().length() < 31;			
	}

	private boolean validateVehiclenumberLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getVehiclenumber()))
			return true;
		return screen.getVehiclenumber().length() < 19;			
	}

	private boolean validatePlaceofloadingLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPlaceofloading()))
			return true;
		return screen.getPlaceofloading().length() < 19;
	}
	private boolean validatePlaceofdischargeLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPlaceofdischarge()))
			return true;
		return screen.getPlaceofdischarge().length() < 31;
	}
	private boolean validatePlaceofdeliveryLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPlaceofdelivery()))
			return true;
		return screen.getPlaceofdelivery().length() < 19;
	}
	private boolean validateIncotermsLengthIs10OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getIncoterms()))
			return true;
		return screen.getIncoterms().length() < 11;
	}
	private boolean validateTermsnoteLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getTermsnote()))
			return true;
		return screen.getTermsnote().length() < 19;
	}
	private boolean validateContainerkeyLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getContainerkey()))
			return true;
		return screen.getContainerkey().length() < 19;
	}
	private boolean validateSignatoryLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSignatory()))
			return true;
		return screen.getSignatory().length() < 19;
	}
	private boolean validatePlaceofissueLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPlaceofissue()))
			return true;
		return screen.getPlaceofissue().length() < 19;
	}
	private boolean validateForte_flagLengthIs6OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getForte_flag()))
			return true;
		return screen.getForte_flag().length() < 7;
	}
	private boolean validateStatusLengthIs10OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getStatus()))
			return true;
		return screen.getStatus().length() < 11;
	}
	private boolean validateContainertypeLengthIs20OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getContainertype()))
			return true;
		return screen.getContainertype().length() < 21;
	}
	private boolean validateTransportationmodeLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getTransportationmode()))
			return true;
		return screen.getTransportationmode().length() < 31;
	}
	private boolean validateExternalreceiptkey2LengthIs20OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getExternalreceiptkey2()))
			return true;
		return screen.getExternalreceiptkey2().length() < 21;
	}
	private boolean validateSusr1LengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSusr1()))
			return true;
		return screen.getSusr1().length() < 31;
	}
	private boolean validateSusr2LengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSusr2()))
			return true;
		return screen.getSusr2().length() < 31;
	}
	private boolean validateSusr3LengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSusr3()))
			return true;
		return screen.getSusr3().length() < 31;
	}
	private boolean validateSusr4LengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSusr4()))
			return true;
		return screen.getSusr4().length() < 31;
	}
	private boolean validateSusr5LengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSusr5()))
			return true;
		return screen.getSusr5().length() < 31;
	}
	private boolean validateTypeLengthIs10OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getType()))
			return true;
		return screen.getType().length() < 11;
	}
	private boolean validateRmaLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getRma()))
			return true;
		return screen.getRma().length() < 31;
	}

	private boolean validateAllowautoreceiptLengthIs1OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getAllowautoreceipt()))
			return true;
		return screen.getAllowautoreceipt().length() < 2;
	}
	private boolean validateTrackinventorybyLengthIs1OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getTrackinventoryby()))
			return true;
		return screen.getTrackinventoryby().length() < 2;
	}
	private boolean validateCarriercountryLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarriercountry()))
			return true;
		return screen.getCarriercountry().length() < 31;
	}
	private boolean validateCarrierphoneLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getCarrierphone()))
			return true;
		return screen.getCarrierphone().length() < 19;
	}
	private boolean validateDrivernameLengthIs25OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getDrivername()))
			return true;
		return screen.getDrivername().length() < 26;
	}
	private boolean validateTrailernumberLengthIs25OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getTrailernumber()))
			return true;
		return screen.getTrailernumber().length() < 26;
	}
	private boolean validateTrailerownerLengthIs25OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getTrailerowner()))
			return true;
		return screen.getTrailerowner().length() < 26;
	}
	private boolean validateLottablematchrequiredLengthIs1OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getLottablematchrequired()))
			return true;
		return screen.getLottablematchrequired().length() < 2;
	}
	private boolean validateAdvicenumberLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getAdvicenumber()))
			return true;
		return screen.getAdvicenumber().length() < 31;
	}
	private boolean validatePackingslipnumberLengthIs50OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getPackingslipnumber()))
			return true;
		return screen.getPackingslipnumber().length() < 51;
	}
	private boolean validateReceiptidLengthIs32OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getReceiptid()))
			return true;
		return screen.getReceiptid().length() < 33;
	}
	private boolean validateSuppliercodeLengthIs15OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSuppliercode()))
			return true;
		return screen.getSuppliercode().length() < 16;
	}
	private boolean validateSuppliernameLengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getSuppliername()))
			return true;
		return screen.getSuppliername().length() < 46;
	}
	private boolean validateShipfromaddressline1LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromaddressline1()))
			return true;
		return screen.getShipfromaddressline1().length() < 46;
	}
	private boolean validateShipfromaddressline2LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromaddressline2()))
			return true;
		return screen.getShipfromaddressline2().length() < 46;
	}
	private boolean validateShipfromaddressline3LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromaddressline3()))
			return true;
		return screen.getShipfromaddressline3().length() < 46;
	}
	private boolean validateShipfromaddressline4LengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromaddressline4()))
			return true;
		return screen.getShipfromaddressline4().length() < 46;
	}
	private boolean validateShipfromcityLengthIs45OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromcity()))
			return true;
		return screen.getShipfromcity().length() < 46;
	}
	private boolean validateShipfromstateLengthIs2OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromstate()))
			return true;
		return screen.getShipfromstate().length() < 3;
	}
	private boolean validateShipfromzipLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromzip()))
			return true;
		return screen.getShipfromzip().length() < 19;
	}
	private boolean validateShipfromisocountryLengthIs3OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromisocountry()))
			return true;
		return screen.getShipfromisocountry().length() < 4;
	}
	private boolean validateShipfromcontactLengthIs30OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromcontact()))
			return true;
		return screen.getShipfromcontact().length() < 31;
	}
	private boolean validateShipfromphoneLengthIs18OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromphone()))
			return true;
		return screen.getShipfromphone().length() < 19;
	}
	private boolean validateShipfromemailLengthIs60OrLess(ASNScreenVO screen){
		if(isEmpty(screen.getShipfromemail()))
			return true;
		return screen.getShipfromemail().length() < 61;
	}
	


	//Attribute Domains
	private boolean validatePokeyInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPokey()))
			return true;
		
		return validatePurchaseOrderDoesExist(screen.getPokey(),  getContext());			
	}

	private boolean validateTrackinventoryInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTrackinventoryby()))
			return true;
		
		return validateCodelkupDoesExist("TRACKINV", screen.getTrackinventoryby(), getContext());			
	}


	private boolean validateCarrierkeyInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCarrierkey()))
			return true;
		
		return BaseScreenValidator.validateStorerDoesExist(screen.getCarrierkey(), "3", getContext());			
	}

	private boolean validateTermsnoteInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTermsnote()))
			return true;
		
		return validateCodelkupDoesExist("PMTTERM", screen.getTermsnote(), getContext());			
	}

	private boolean validateIncotermsInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getIncoterms()))
			return true;
		
		return validateCodelkupDoesExist("INCOTERMS", screen.getTermsnote(), getContext());			
	}

	private boolean validateOrigincountryInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getOrigincountry()))
			return true;
		
		return validateCodelkupDoesExist("ISOCOUNTRY", screen.getOrigincountry(), getContext());			
	}

	private boolean validateDestinationcountryInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getDestinationcountry()))
			return true;
		
		return validateCodelkupDoesExist("ISOCOUNTRY", screen.getDestinationcountry(), getContext());			
	}

	private boolean validateTransportationmodeInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTransportationmode()))
			return true;
		
		return validateCodelkupDoesExist("TRANSPMODE", screen.getTransportationmode(), getContext());			
	}
	
	private boolean validateContainertypeInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getContainertype()))
			return true;
		
		return validateCodelkupDoesExist("CONTAINERT", screen.getContainertype(), getContext());			
	}
	

	private boolean validateSuppliercodeInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getSuppliercode()))
			return true;
		
		return BaseScreenValidator.validateStorerDoesExist(screen.getSuppliercode(), "5", getContext());			
	}

	private boolean validateShipfromisocountryInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getShipfromisocountry()))
			return true;
		
		return validateCodelkupDoesExist("ISOCOUNTRY", screen.getShipfromisocountry(), getContext());			
	}
	
	private boolean validateStatusInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStatus()))
			return true;
		
		return validateCodelkupDoesExist("RECSTATUS", screen.getStatus(), getContext());			
	}
	
	private boolean validateReceipttypeInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getType()))
			return true;
		
		return validateCodelkupDoesExist("RECEIPTYPE", screen.getType(), getContext());			
	}
	
	private boolean validateStorerkeyInAttrDom(ASNScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStorerkey()))
			return true;
		
		return BaseScreenValidator.validateStorerDoesExist(screen.getStorerkey(), "1", getContext());			
	}
	
	
	//Required - Not null validations
	public boolean validateStorerkeyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getStorerkey());			
	}

	public boolean validateStatusNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getStatus());			
	}
	
	public boolean validateTypeNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getType());			
	}

	public boolean validateReceiptkeyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getReceiptkey());			
	}

	public boolean validateExternreceiptkeyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getExternreceiptkey());			
	}

	public boolean validateReceiptgroupNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getReceiptgroup());			
	}

	public boolean validateEffectivedateNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getEffectivedate());			
	}

	public boolean validateExpectedreceiptdateNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getExpectedreceiptdate());			
	}

	public boolean validateArrivaldatetimeNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getArrivaldatetime());			
	}

	public boolean validateLottablematchrequiredNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getLottablematchrequired());			
	}

	public boolean validateBillcontainerqtyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getBilledcontainerqty());			
	}
	
	public boolean validateContainerqtyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getContainerqty());			
	}
	
	public boolean validateGrossweightNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getGrossweight());			
	}

	public boolean validateOpenqtyNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getOpenqty());			
	}
	
	public boolean validateTotalvolumeNotEmpty(ASNScreenVO screen){
		return !isEmpty(screen.getTotalvolume());			
	}
	
	//Exist
	private boolean validateStorerkeyDoesExist(ASNScreenVO screen) throws WMSDataLayerException{
		return validateStorerDoesExist(screen.getStorerkey(), "1",getContext());				
	}

	private boolean validateStatusDoesExist(ASNScreenVO screen) throws WMSDataLayerException{
		return validateCodelkupDoesExist("RECSTATUS", screen.getStatus(), getContext());				
	}

	private boolean validateTypeDoesExist(ASNScreenVO screen) throws WMSDataLayerException{
		return validateCodelkupDoesExist("RECEIPTYPE", screen.getType(), getContext());				
	}
	
	private boolean validateReceiptkeyDoesExist(ASNScreenVO screen) throws WMSDataLayerException{
		return validateReceiptDoesExist(screen.getReceiptkey(), getContext());				
	}
	
	//Numeric validations
	private boolean validateBillcontainerqtyIsANumber(ASNScreenVO screen){
		return isNumber(screen.getBilledcontainerqty());
	}
	
	private boolean validateBillcontainerqtyGreaterThanOrEqualZero(ASNScreenVO screen){
		if(isEmpty(screen.getBilledcontainerqty()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getBilledcontainerqty());
	}

	private boolean validateContainerqtyIsANumber(ASNScreenVO screen){
		return isNumber(screen.getContainerqty());
	}
	
	private boolean validateContainerqtyGreaterThanOrEqualZero(ASNScreenVO screen){
		if(isEmpty(screen.getContainerqty()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getContainerqty());
	}
	
	
	
	private boolean validateGrossweightIsANumber(ASNScreenVO screen){
		return isNumber(screen.getGrossweight());
	}
	
	private boolean validateGrossweightGreaterThanOrEqualZero(ASNScreenVO screen){
		if(isEmpty(screen.getGrossweight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getGrossweight());
	}


	
	private boolean validateOpenqtyIsANumber(ASNScreenVO screen){
		return isNumber(screen.getOpenqty());
	}
	
	private boolean validateOpenqtyGreaterThanOrEqualZero(ASNScreenVO screen){
		if(isEmpty(screen.getOpenqty()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getOpenqty());
	}


	
	private boolean validateTotalvolumeIsANumber(ASNScreenVO screen){
		return isNumber(screen.getTotalvolume());
	}
	
	private boolean validateTotalvolumeGreaterThanOrEqualZero(ASNScreenVO screen){
		if(isEmpty(screen.getTotalvolume()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getTotalvolume());
	}

	public static String getErrorMessage(int errorCode, Locale locale, ASNScreenVO asnScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){


		case RULE_BILLEDCONTAINERQTY_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_BILLEDCONTAINERQTY, locale));

		case RULE_CONTAINERQTY_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CONTAINERQTY, locale));

		case RULE_GROSSWEIGHT_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_GROSSWEIGHT, locale));

		case RULE_OPENQTY_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_OPENQTY, locale));

		case RULE_TOTALVOLUME_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TOTALVOLUME, locale));



		case RULE_BILLEDCONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO:
			return BaseScreenValidator.getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_BILLEDCONTAINERQTY,locale));

		case RULE_CONTAINERQTY_GREATER_THAN_OR_EQUAL_ZERO:
			return BaseScreenValidator.getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CONTAINERQTY,locale));

		case RULE_GROSSWEIGHT_GREATER_THAN_OR_EQUAL_ZERO:
			return BaseScreenValidator.getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_GROSSWEIGHT,locale));

		case RULE_OPENQTY_GREATER_THAN_OR_EQUAL_ZERO:
			return BaseScreenValidator.getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_OPENQTY,locale));

		case RULE_TOTALVOLUME_GREATER_THAN_OR_EQUAL_ZERO:
			return BaseScreenValidator.getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TOTALVOLUME,locale));


			
		case RULE_STORERKEY_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STORERKEY, locale),
					asnScreen.getStorerkey());

		case RULE_STATUS_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STATUS, locale),
					asnScreen.getStatus());

		case RULE_TYPE_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TYPE, locale),
					asnScreen.getType());
			
			
			
		case RULE_STORERKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STORERKEY, locale));

		case RULE_STATUS_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STATUS, locale));

		case RULE_TYPE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TYPE, locale));

		case RULE_RECEIPTKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTKEY, locale));

		case RULE_EXTERNRECEIPTKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_EXTERNRECEIPTKEY, locale));

		case RULE_RECEIPTGROUP_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTGROUP, locale));

		case RULE_EFFECTIVEDATE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_EFFECTIVEDATE, locale));

		case RULE_EXPECTEDRECEIPTDATE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_EXPECTEDRECEIPTDATE, locale));

		case RULE_ARRIVALDATETIME_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_ARRIVALDATETIME, locale));

		case RULE_LOTTABLEMATCHREQUIRED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_LOTTABLEMATCHREQUIRED, locale));


		
		//Domain
		case RULE_POKEY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_POKEY, locale),
					asnScreen.getPokey());

		case RULE_TRACKINVENTORYBY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRACKINVENTORYBY, locale),
					asnScreen.getTrackinventoryby());

		case RULE_CARRIERKEY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERKEY, locale),
					asnScreen.getCarrierkey());

		case RULE_TERMSNOTE_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TERMSNOTE, locale),
					asnScreen.getTermsnote());

		case RULE_INCOTERMS_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_INCOTERMS, locale),
					asnScreen.getIncoterms());

		case RULE_ORIGINCOUNTRY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_ORIGINCOUNTRY, locale),
					asnScreen.getOrigincountry());

		case RULE_DESTINATIONCOUNTRY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_DESTINATIONCOUNTRY, locale),
					asnScreen.getDestinationcountry());

		case RULE_TRANSPORTATIONMODE_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRANSPORTATIONMODE, locale),
					asnScreen.getTransportationmode());

		case RULE_CONTAINERTYPE_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CONTAINERTYPE, locale),
					asnScreen.getContainertype());

		case RULE_SUPPLIERCODE_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUPPLIERCODE, locale),
					asnScreen.getSuppliercode());

		case RULE_SHIPFROMISOCOUNTRY_IN_ATTR_DOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMISOCOUNTRY, locale),
						asnScreen.getShipfromisocountry());


			//Length
		case RULE_LENGTH_WHSEID_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_WHSEID, locale),
					"30");
			
		case RULE_LENGTH_RECEIPTKEY_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTKEY, locale),
					"10");

		case RULE_LENGTH_EXTERNRECEIPTKEY_32:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_EXTERNRECEIPTKEY, locale),
					"32");

		case RULE_LENGTH_RECEIPTGROUP_20:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTGROUP, locale),
					"20");

		case RULE_LENGTH_STORERKEY_15:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STORERKEY, locale),
					"15");

		case RULE_LENGTH_POKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_POKEY, locale),
					"18");

		case RULE_LENGTH_CARRIERKEY_15:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERKEY, locale),
					"15");

		case RULE_LENGTH_CARRIERNAME_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERNAME, locale),
					"45");

		case RULE_LENGTH_CARRIERADDRESS1_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERADDRESS1, locale),
					"45");

		case RULE_LENGTH_CARRIERADDRESS2_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERADDRESS2, locale),
					"45");

		case RULE_LENGTH_CARRIERCITY_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERCITY, locale),
					"45");

		case RULE_LENGTH_CARRIERSTATE_2:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERSTATE, locale),
					"2");

		case RULE_LENGTH_CARRIERZIP_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERZIP, locale),
					"18");

		case RULE_LENGTH_CARRIERREFERENCE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERREFERENCE, locale),
					"18");


		case RULE_LENGTH_WAREHOUSEREFERENCE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_WAREHOUSEREFERENCE, locale),
					"18");

		case RULE_LENGTH_ORIGINCOUNTRY_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_ORIGINCOUNTRY, locale),
					"30");

		case RULE_LENGTH_DESTINATIONCOUNTRY_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_DESTINATIONCOUNTRY, locale),
					"30");

		case RULE_LENGTH_VEHICLENUMBER_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_VEHICLENUMBER, locale),
					"18");

		case RULE_LENGTH_PLACEOFLOADING_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_PLACEOFLOADING, locale),
					"18");

		case RULE_LENGTH_PLACEOFDISCHARGE_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_PLACEOFDISCHARGE, locale),
					"30");

		case RULE_LENGTH_PLACEOFDELIVERY_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_PLACEOFDELIVERY, locale),
					"30");

		case RULE_LENGTH_INCOTERMS_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_INCOTERMS, locale),
					"10");

		case RULE_LENGTH_TERMSNOTE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TERMSNOTE, locale),
					"18");

		case RULE_LENGTH_CONTAINERKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CONTAINERKEY, locale),
					"18");

		case RULE_LENGTH_SIGNATORY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SIGNATORY, locale),
					"18");

	
		case RULE_LENGTH_PLACEOFISSUE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_PLACEOFISSUE, locale),
					"18");
		case RULE_LENGTH_STATUS_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_STATUS, locale),
					"10");

		case RULE_LENGTH_CONTAINERTYPE_20:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CONTAINERTYPE, locale),
					"20");

		case RULE_LENGTH_TRANSPORTATIONMODE_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRANSPORTATIONMODE, locale),
					"30");

		case RULE_LENGTH_EXTERNALRECEIPTKEY2_20:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_EXTERNALRECEIPTKEY2, locale),
					"20");

		case RULE_LENGTH_SUSR1_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUSR1, locale),
					"30");

		case RULE_LENGTH_SUSR2_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUSR2, locale),
					"30");

		case RULE_LENGTH_SUSR3_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUSR3, locale),
					"30");

		case RULE_LENGTH_SUSR4_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUSR4, locale),
					"30");

		case RULE_LENGTH_SUSR5_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUSR5, locale),
					"30");

		case RULE_LENGTH_TYPE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TYPE, locale),
					"10");

		case RULE_LENGTH_RMA_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RMA, locale),
					"30");

		case RULE_LENGTH_ALLOWAUTORECEIPT_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_ALLOWAUTORECEIPT, locale),
					"1");

		case RULE_LENGTH_TRACKINVENTORYBY_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRACKINVENTORYBY, locale),
					"1");

		case RULE_LENGTH_CARRIERCOUNTRY_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERCOUNTRY, locale),
					"30");

		case RULE_LENGTH_CARRIERPHONE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_CARRIERPHONE, locale),
					"18");

		case RULE_LENGTH_DRIVERNAME_25:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_DRIVERNAME, locale),
					"25");

		case RULE_LENGTH_TRAILERNUMBER_25:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRAILERNUMBER, locale),
					"25");

		case RULE_LENGTH_TRAILEROWNER_25:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_TRAILEROWNER, locale),
					"25");

		case RULE_LENGTH_LOTTABLEMATCHREQUIRED_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_LOTTABLEMATCHREQUIRED, locale),
					"1");

		case RULE_LENGTH_ADVICENUMBER_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_ADVICENUMBER, locale),
					"30");

		case RULE_LENGTH_PACKINGSLIPNUMBER_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_PACKINGSLIPNUMBER, locale),
					"50");

		case RULE_LENGTH_RECEIPTID_32:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTID, locale),
					"32");

		case RULE_LENGTH_SUPPLIERCODE_15:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUPPLIERCODE, locale),
					"15");

		case RULE_LENGTH_SUPPLIERNAME_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SUPPLIERNAME, locale),
					"45");

		case RULE_LENGTH_SHIPFROMADDRESSLINE1_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE1, locale),
					"45");

		case RULE_LENGTH_SHIPFROMADDRESSLINE2_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE2, locale),
					"45");

		case RULE_LENGTH_SHIPFROMADDRESSLINE3_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE3, locale),
					"45");

		case RULE_LENGTH_SHIPFROMADDRESSLINE4_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE4, locale),
					"45");

		case RULE_LENGTH_SHIPFROMCITY_45:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMCITY, locale),
					"45");

		case RULE_LENGTH_SHIPFROMSTATE_2:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMSTATE, locale),
					"2");

		case RULE_LENGTH_SHIPFROMZIP_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMZIP, locale),
					"18");

		case RULE_LENGTH_SHIPFROMISOCOUNTRY_3:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMISOCOUNTRY, locale),
					"3");

		case RULE_LENGTH_SHIPFROMCONTACT_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMCONTACT, locale),
					"30");

		case RULE_LENGTH_SHIPFROMPHONE_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMPHONE, locale),
					"18");

		case RULE_LENGTH_SHIPFROMEMAIL_60:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_SHIPFROMEMAIL, locale),
					"60");

			//Unique
		case RULE_RECEIPTKEY_MUST_BE_UNIQUE:
			param = new String[1];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPT_FIELD_RECEIPTKEY, locale);
			
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_RECEIPT_SCREEN_ERROR_DUPLICATE_RECEIPT, locale, param);
			

			
			
		}//end switch
		
		
		return errorMsg;
	}
}
