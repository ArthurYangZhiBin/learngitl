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
import com.infor.scm.wms.util.datalayer.query.PackQueryRunner;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class ASNDetailValidator extends BaseScreenValidator {

	public ASNDetailValidator(WMSValidationContext context) {
		super(context);
	}

	public static final int RULE_CASECNT_MUST_BE_A_NUMBER= 0 ;
	public static final int RULE_CUBE_MUST_BE_A_NUMBER= 1 ;
	public static final int RULE_EXTENDEDPRICE_MUST_BE_A_NUMBER= 2 ;
	public static final int RULE_GROSSWGT_MUST_BE_A_NUMBER= 3 ;
	public static final int RULE_INNERPACK_MUST_BE_A_NUMBER= 4 ;
	public static final int RULE_NETWGT_MUST_BE_A_NUMBER= 5 ;
	public static final int RULE_OTHERUNIT1_MUST_BE_A_NUMBER= 6 ;
	public static final int RULE_OTHERUNIT2_MUST_BE_A_NUMBER= 7 ;
	public static final int RULE_PACKINGSLIPQTY_MUST_BE_A_NUMBER= 8 ;
	public static final int RULE_PALLET_MUST_BE_A_NUMBER= 9 ;
	public static final int RULE_QCQTYINSPECTED_MUST_BE_A_NUMBER= 10 ;
	public static final int RULE_QCQTYREJECTED_MUST_BE_A_NUMBER= 11 ;
	public static final int RULE_QTYADJUSTED_MUST_BE_A_NUMBER= 12 ;
	public static final int RULE_QTYEXPECTED_MUST_BE_A_NUMBER= 13 ;
	public static final int RULE_QTYRECEIVED_MUST_BE_A_NUMBER= 14 ;
	public static final int RULE_QTYREJECTED_MUST_BE_A_NUMBER= 15 ;
	public static final int RULE_UNITPRICE_MUST_BE_A_NUMBER= 16 ;


	public static final int RULE_QTYEXPECTED_GREATER_THAN_ZERO= 17 ;

	public static final int RULE_CASECNT_GREATER_THAN_OR_EQUAL_ZERO= 18 ;
	public static final int RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO= 19 ;
	public static final int RULE_EXTENDEDPRICE_GREATER_THAN_OR_EQUAL_ZERO= 20 ;
	public static final int RULE_GROSSWGT_GREATER_THAN_OR_EQUAL_ZERO= 21 ;
	public static final int RULE_INNERPACK_GREATER_THAN_OR_EQUAL_ZERO= 22 ;
	public static final int RULE_NETWGT_GREATER_THAN_OR_EQUAL_ZERO= 23 ;
	public static final int RULE_OTHERUNIT1_GREATER_THAN_OR_EQUAL_ZERO= 24 ;
	public static final int RULE_OTHERUNIT2_GREATER_THAN_OR_EQUAL_ZERO= 25 ;
	public static final int RULE_PACKINGSLIPQTY_GREATER_THAN_OR_EQUAL_ZERO= 26 ;
	public static final int RULE_PALLET_GREATER_THAN_OR_EQUAL_ZERO= 27 ;

	public static final int RULE_QCQTYINSPECTED_GREATER_THAN_OR_EQUAL_ZERO = 28 ;
	public static final int RULE_QCQTYREJECTED_GREATER_THAN_OR_EQUAL_ZERO= 29 ;
	public static final int RULE_QTYADJUSTED_GREATER_THAN_OR_EQUAL_ZERO= 30 ;
	public static final int RULE_QTYRECEIVED_GREATER_THAN_OR_EQUAL_ZERO = 31 ;
	public static final int RULE_QTYREJECTED_GREATER_THAN_OR_EQUAL_ZERO = 32 ;
	public static final int RULE_UNITPRICE_GREATER_THAN_OR_EQUAL_ZERO = 33 ;
	
	public static final int RULE_RECEIPTKEY_AND_RECEIPTLINENUMBER_UNIQUE = 34;
	
	//Must exist
	public static final int RULE_PACKKEY_MUST_EXIST = 35;
	public static final int RULE_QCREQUIRED_MUST_EXIST = 36; //codelist, YESNO
	public static final int RULE_UOM_MUST_EXIST = 37;
	public static final int RULE_TOLOC_MUST_EXIST = 38;
	
	//Required
	public static final int RULE_ALTSKU_NOT_EMPTY = 39;
	public static final int RULE_CASECNT_NOT_EMPTY = 40;
	public static final int RULE_CONDITIONCODE_NOT_EMPTY = 41;
	public static final int RULE_CUBE_NOT_EMPTY = 42;
	public static final int RULE_DATERECEIVED_NOT_EMPTY = 43;
	public static final int RULE_EFFECTIVEDATE_NOT_EMPTY = 44;
	public static final int RULE_EXTENDEDPRICE_NOT_EMPTY = 45;
	public static final int RULE_EXTERNLINENO_NOT_EMPTY = 46;
	public static final int RULE_EXTERNRECEIPTKEY_NOT_EMPTY = 47;
	public static final int RULE_FORTE_FLAG_NOT_EMPTY = 48;
	
	public static final int RULE_GROSSWGT_NOT_EMPTY = 49;
	public static final int RULE_ID_NOT_EMPTY = 50; // defaulted to ''
	public static final int RULE_INNERPACK_NOT_EMPTY = 51;
	public static final int RULE_LOTTABLE01_NOT_EMPTY = 52;
	public static final int RULE_LOTTABLE02_NOT_EMPTY = 53;
	public static final int RULE_LOTTABLE03_NOT_EMPTY = 54;
	public static final int RULE_LOTTABLE06_NOT_EMPTY = 55;
	public static final int RULE_LOTTABLE07_NOT_EMPTY = 56;
	public static final int RULE_LOTTABLE08_NOT_EMPTY = 57;
	public static final int RULE_LOTTABLE09_NOT_EMPTY = 58;
	public static final int RULE_LOTTABLE10_NOT_EMPTY = 59;
	public static final int RULE_NETWGT_NOT_EMPTY = 60;

	public static final int RULE_OTHERUNIT1_NOT_EMPTY = 61;
	public static final int RULE_OTHERUNIT2_NOT_EMPTY = 62;
	public static final int RULE_PACKINGSLIPQTY_NOT_EMPTY = 63;
	public static final int RULE_PACKKEY_NOT_EMPTY = 64;
	public static final int RULE_PALLET_NOT_EMPTY = 65;
	public static final int RULE_POKEY_NOT_EMPTY = 66; //defaulted to ''
	public static final int RULE_QCREQUIRED_NOT_EMPTY = 67; 
	public static final int RULE_QTYADJUSTED_NOT_EMPTY = 68;
	public static final int RULE_QTYEXPECTED_NOT_EMPTY = 69;
	public static final int RULE_QTYRECEIVED_NOT_EMPTY = 70;
	
	public static final int RULE_QTYREJECTED_NOT_EMPTY = 71;
	public static final int RULE_RECEIPTKEY_NOT_EMPTY = 72;
	public static final int RULE_RECEIPTLINENUMBER_NOT_EMPTY = 73;
	public static final int RULE_SKU_NOT_EMPTY = 74;
	public static final int RULE_STATUS_NOT_EMPTY = 75;
	public static final int RULE_STORERKEY_NOT_EMPTY = 76;
	public static final int RULE_TARIFFKEY_NOT_EMPTY = 77;//detaulted to XXXXXX
	public static final int RULE_TOLOC_NOT_EMPTY = 78;
	public static final int RULE_UNITPRICE_NOT_EMPTY = 79;
	public static final int RULE_UOM_NOT_EMPTY = 80;

	//Attribute domain
	public static final int RULE_ATTR_DOM_ALTSKU = 81;
	public static final int RULE_ATTR_DOM_CONDITIONCODE = 82; //INVENTORYHOLDCODE 
	public static final int RULE_ATTR_DOM_DISPOSITIONTYPE = 83; //codelist, RETDISPTYP
	public static final int RULE_ATTR_DOM_DISPOSITIONCODE = 84; //codelist, RETDISPCOD
	public static final int RULE_ATTR_DOM_PACKKEY = 85; //pack 
	public static final int RULE_ATTR_DOM_POKEY = 86; //po
	public static final int RULE_ATTR_DOM_QCREQUIRED = 87; //codelist, YESNO
	public static final int RULE_ATTR_DOM_QCAUTOADJUST = 88; //codelist, YESNO
	public static final int RULE_ATTR_DOM_QCSTATUS = 89; //codelist, QCISTATUS
	public static final int RULE_ATTR_DOM_QCREJREASON = 90; //codelist,	QCREJRSN
	public static final int RULE_ATTR_DOM_RECEIPTKEY = 91; //receipt
	
	public static final int RULE_ATTR_DOM_SKU = 92; //sku
	public static final int RULE_ATTR_DOM_STATUS = 93; //codelist, RECSTATUS
	public static final int RULE_ATTR_DOM_STORERKEY = 94; //storer, 1
	public static final int RULE_ATTR_DOM_SUPPLIERKEY = 95; //storer, 
	public static final int RULE_ATTR_DOM_RETURNTYPE = 96; //codelist, RETTYPE
	public static final int RULE_ATTR_DOM_RETURNREASON = 97; //codelist, RETREASON
	public static final int RULE_ATTR_DOM_RETURNCONDITION = 98; //codelist, RETCOND
	public static final int RULE_ATTR_DOM_TARIFFKEY = 99; //tariff
	public static final int RULE_ATTR_DOM_TOLOC = 100; //loc
	public static final int RULE_ATTR_DOM_UOM = 101; //PACKUOM1..2..3..4..5..6..7

	//Length validations
	public static final int RULE_LENGTH_ALTSKU_50 = 102;
	public static final int RULE_LENGTH_CONDITIONCODE_10 = 103;
	public static final int RULE_LENGTH_CONTAINERKEY_18 = 104;
	public static final int RULE_LENGTH_DISPOSITIONCODE_10 = 105;
	public static final int RULE_LENGTH_DISPOSITIONTYPE_10 = 106;
	public static final int RULE_LENGTH_EXTERNALLOT_100 = 107;
	public static final int RULE_LENGTH_EXTERNLINENO_20 = 108;
	public static final int RULE_LENGTH_EXTERNRECEIPTKEY_32 = 109;
	public static final int RULE_LENGTH_FORTE_FLAG_6 = 110;
	
	public static final int RULE_LENGTH_ID_18 = 111;
	public static final int RULE_LENGTH_IPSKEY_10 = 112;
	public static final int RULE_LENGTH_LOTTABLE01_50 = 113;
	public static final int RULE_LENGTH_LOTTABLE02_50 = 114;
	public static final int RULE_LENGTH_LOTTABLE03_50 = 115;
	public static final int RULE_LENGTH_LOTTABLE06_50 = 116;
	public static final int RULE_LENGTH_LOTTABLE07_50 = 117;
	public static final int RULE_LENGTH_LOTTABLE08_50 = 118;
	public static final int RULE_LENGTH_LOTTABLE09_50 = 119;
	public static final int RULE_LENGTH_LOTTABLE10_50 = 120;

	public static final int RULE_LENGTH_MATCHLOTTABLE_1 = 121;
	public static final int RULE_LENGTH_NOTES_2000 = 122;
	public static final int RULE_LENGTH_PACKKEY_50 = 123;
	public static final int RULE_LENGTH_PALLETID_18 = 124;
	public static final int RULE_LENGTH_POKEY_18 = 125;
	public static final int RULE_LENGTH_POLINENUMBER_15 = 126;
	public static final int RULE_LENGTH_QCAUTOADJUST_1 = 127;
	public static final int RULE_LENGTH_QCREJREASON_10 = 128;
	public static final int RULE_LENGTH_QCREQUIRED_1 = 129;
	public static final int RULE_LENGTH_QCSTATUS_1 = 130;
	public static final int RULE_LENGTH_QCUSER_18 = 131;
	
	public static final int RULE_LENGTH_REASONCODE_20 = 132;
	public static final int RULE_LENGTH_RECEIPTDETAILID_32 = 133;
	public static final int RULE_LENGTH_RECEIPTKEY_10 = 134;
	public static final int RULE_LENGTH_RECEIPTLINENUMBER_5 = 135;
	public static final int RULE_LENGTH_RETURNCONDITION_10 = 136;
	public static final int RULE_LENGTH_RETURNREASON_10 = 137;
	public static final int RULE_LENGTH_RETURNTYPE_10 = 138;
	public static final int RULE_LENGTH_RMA_16 = 139;
	public static final int RULE_LENGTH_SKU_50 = 140;
	public static final int RULE_LENGTH_STATUS_10 = 141;
	public static final int RULE_LENGTH_STORERKEY_15 = 142;
	public static final int RULE_LENGTH_SUPPLIERKEY_10 = 143;
	public static final int RULE_LENGTH_SUPPLIERNAME_25 = 144;

	public static final int RULE_LENGTH_SUSR1_30 = 145;
	public static final int RULE_LENGTH_SUSR2_30 = 146;
	public static final int RULE_LENGTH_SUSR3_30 = 147;
	public static final int RULE_LENGTH_SUSR4_30 = 148;
	public static final int RULE_LENGTH_SUSR5_30 = 149;
	public static final int RULE_LENGTH_TARIFFKEY_10 = 150;
	public static final int RULE_LENGTH_TOID_18 = 151;
	public static final int RULE_LENGTH_TOLOC_10 = 152;
	public static final int RULE_LENGTH_TOLOT_10 = 153;
	public static final int RULE_LENGTH_TYPE_10 = 154;
	public static final int RULE_LENGTH_UOM_10 = 155;
	public static final int RULE_LENGTH_VESSELKEY_18 = 156;
	public static final int RULE_LENGTH_VOYAGEKEY_18 = 157;
	public static final int RULE_LENGTH_WHSEID_30 = 158;
	public static final int RULE_LENGTH_XDOCKKEY_18 =159;

	
	public ArrayList validate(ASNDetailVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doAssumeDefaults = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ASSUME_DEFAULTS);
		boolean doInTransaction = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_IN_TRANSACTION);
		
		//Validate Field Lengths
		if(doCheckFieldLength){			
			

			if(!validateAltskuLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALTSKU_50));
			}

			if(!validateConditioncodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONDITIONCODE_10));
			}

			if(!validateContainerkeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONTAINERKEY_18));
			}

			if(!validateDispositioncodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DISPOSITIONCODE_10));
			}

			if(!validateDispositiontypeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DISPOSITIONTYPE_10));
			}

			

			if(!validateExternallotLengthIs100OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXTERNALLOT_100));
			}

			if(!validateExternlinenoLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXTERNLINENO_20));
			}

			if(!validateExternreceiptkeyLengthIs32OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXTERNRECEIPTKEY_32));
			}

			if(!validateForte_flagLengthIs6OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_FORTE_FLAG_6));
			}

			if(!validateIdLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ID_18));
			}

			if(!validateIpskeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_IPSKEY_10));
			}

			if(!validateLottable01LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE01_50));
			}

			if(!validateLottable02LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE02_50));
			}

			if(!validateLottable03LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE03_50));
			}

			if(!validateLottable06LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE06_50));
			}

			if(!validateLottable07LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE07_50));
			}

			if(!validateLottable08LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE08_50));
			}

			if(!validateLottable09LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE09_50));
			}

			if(!validateLottable10LengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE10_50));
			}

			if(!validateMatchlottableLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_MATCHLOTTABLE_1));
			}

			if(!validateNotesLengthIs2000OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_NOTES_2000));
			}

			if(!validatePackkeyLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKKEY_50));
			}

			if(!validatePalletidLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PALLETID_18));
			}

			if(!validatePokeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_POKEY_18));
			}

			if(!validatePolinenumberLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_POLINENUMBER_15));
			}

			if(!validateQcautoadjustLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_QCAUTOADJUST_1));
			}

			if(!validateQcrejreasonLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_QCREJREASON_10));
			}

			if(!validateQcrequiredLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_QCREQUIRED_1));
			}

			if(!validateQcstatusLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_QCSTATUS_1));
			}

			if(!validateQcuserLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_QCUSER_18));
			}

			if(!validateReasoncodeLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REASONCODE_20));
			}

			if(!validateReceiptdetailidLengthIs32OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTDETAILID_32));
			}

			if(!validateReceiptkeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTKEY_10));
			}

			if(!validateReceiptlinenumberLengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPTLINENUMBER_5));
			}

			if(!validateReturnconditionLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RETURNCONDITION_10));
			}

			if(!validateReturnreasonLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RETURNREASON_10));
			}

			if(!validateReturntypeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RETURNTYPE_10));
			}

			if(!validateRmaLengthIs16OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RMA_16));
			}

			if(!validateSkuLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SKU_50));
			}

			if(!validateStatusLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STATUS_10));
			}

			if(!validateStorerkeyLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STORERKEY_15));
			}

			if(!validateSupplierkeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUPPLIERKEY_10));
			}

			if(!validateSuppliernameLengthIs25OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUPPLIERNAME_25));
			}

			if(!validateSusr1LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR1_30));
			}

			if(!validateSusr2LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR2_30));
			}

			if(!validateSusr3LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR3_30));
			}

			if(!validateSusr4LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR4_30));
			}

			if(!validateSusr5LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SUSR5_30));
			}

			if(!validateTariffkeyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TARIFFKEY_10));
			}

			if(!validateToidLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TOID_18));
			}

			if(!validateTolocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TOLOC_10));
			}

			if(!validateTolotLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TOLOT_10));
			}

			if(!validateTypeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TYPE_10));
			}

			if(!validateUomLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM_10));
			}

			if(!validateVesselkeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VESSELKEY_18));
			}

			if(!validateVoyagekeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VOYAGEKEY_18));
			}

			if(!validateWhseidLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WHSEID_30));
			}

			if(!validateXdockkeyLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_XDOCKKEY_18));
			}


		}
		
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
		
			
			

			if(!validateConditioncodeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CONDITIONCODE  ));		
			}

			if(!validateDispositiontypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DISPOSITIONTYPE  ));		
			}

			if(!validateDispositioncodeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DISPOSITIONCODE  ));		
			}

			if(!validatePackkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKKEY  ));		
			}

			if(!validatePokeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_POKEY  ));		
			}

			if(!validateQcrequiredInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_QCREQUIRED  ));		
			}

			if(!validateQcautoadjustInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_QCAUTOADJUST  ));		
			}

			if(!validateQcstatusInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_QCSTATUS  ));		
			}

			if(!validateQcrejreasonInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_QCREJREASON  ));		
			}

			if(!doInTransaction && !validateReceiptkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RECEIPTKEY  ));		
			}
		
			if(!validateSkuInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_SKU  ));		
			}

			if(!validateStatusInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_STATUS  ));		
			}

			if(!validateStorerkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_STORERKEY  ));		
			}

			if(!validateSupplierkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_SUPPLIERKEY  ));		
			}

			if(!validateReturntypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RETURNTYPE  ));		
			}

			if(!validateReturnreasonInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RETURNREASON  ));		
			}

			if(!validateReturnconditionInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RETURNCONDITION  ));		
			}

			if(!validateTariffkeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TARIFFKEY  ));		
			}

			if(!validateTolocInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TOLOC  ));		
			}

			if(!validateUomInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM  ));		
			}


		}//attr domain
		
		//Required
		if(doCheckRequiredFields && !validatePackkeyNotEmpty(screen))
			errors.add(new Integer(RULE_PACKKEY_NOT_EMPTY));
		else{
			if(!validatePackDoesExist(screen.getPackkey(), getContext()))
				errors.add(new Integer(RULE_PACKKEY_MUST_EXIST));
		}

		if(doCheckRequiredFields && !validateQcrequiredNotEmpty(screen))
			errors.add(new Integer(RULE_QCREQUIRED_NOT_EMPTY));
		else{
			if(!validateQcrequiredDoesExist(screen))
				errors.add(new Integer(RULE_QCREQUIRED_MUST_EXIST));
		}

		if(doCheckRequiredFields && !validateUomNotEmpty(screen))
			errors.add(new Integer(RULE_UOM_NOT_EMPTY));
		else{
			if(!validateUomDoesExist(screen))
				errors.add(new Integer(RULE_UOM_MUST_EXIST));
		}

		if(doCheckRequiredFields && !validateTolocNotEmpty(screen))
			errors.add(new Integer(RULE_TOLOC_NOT_EMPTY));
		else{
			if(!validateTolocDoesExist(screen))
				errors.add(new Integer(RULE_TOLOC_MUST_EXIST));
		}

		//not null
		if(doCheckRequiredFields && !validateCasecntNotEmpty(screen)){
			errors.add(new Integer(RULE_CASECNT_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateConditioncodeNotEmpty(screen)){
			errors.add(new Integer(RULE_CONDITIONCODE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateCubeNotEmpty(screen)){
			errors.add(new Integer(RULE_CUBE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateDatereceivedNotEmpty(screen)){
			errors.add(new Integer(RULE_DATERECEIVED_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateEffectivedateNotEmpty(screen)){
			errors.add(new Integer(RULE_EFFECTIVEDATE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateExtendedpriceNotEmpty(screen)){
			errors.add(new Integer(RULE_EXTENDEDPRICE_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateExternlinenoNotEmpty(screen)){
			errors.add(new Integer(RULE_EXTERNLINENO_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateExternreceiptkeyNotEmpty(screen)){
			errors.add(new Integer(RULE_EXTERNRECEIPTKEY_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateGrosswgtNotEmpty(screen)){
			errors.add(new Integer(RULE_GROSSWGT_NOT_EMPTY));
		}


		if(doCheckRequiredFields && !validateInnerpackNotEmpty(screen)){
			errors.add(new Integer(RULE_INNERPACK_NOT_EMPTY));
		}

		
		if(doCheckRequiredFields && !validateNetwgtNotEmpty(screen)){
			errors.add(new Integer(RULE_NETWGT_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateOtherunit1NotEmpty(screen)){
			errors.add(new Integer(RULE_OTHERUNIT1_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validateOtherunit2NotEmpty(screen)){
			errors.add(new Integer(RULE_OTHERUNIT2_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validatePackingslipqtyNotEmpty(screen)){
			errors.add(new Integer(RULE_PACKINGSLIPQTY_NOT_EMPTY));
		}

		if(doCheckRequiredFields && !validatePalletNotEmpty(screen)){
			errors.add(new Integer(RULE_PALLET_NOT_EMPTY));
		}


		if(doCheckRequiredFields && !validateQcrequiredNotEmpty(screen)){
			errors.add(new Integer(RULE_QCREQUIRED_NOT_EMPTY));
		}

		//Numeric validations
		if(doCheckRequiredFields && !validateCasecntNotEmpty(screen)){
			errors.add(new Integer(RULE_CASECNT_NOT_EMPTY));
		}else{
			if(!validateCasecntIsANumber(screen)){
				errors.add(RULE_CASECNT_MUST_BE_A_NUMBER);
			}else{
				if (!validateCasecntGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_CASECNT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateCubeNotEmpty(screen)){
			errors.add(new Integer(RULE_CASECNT_NOT_EMPTY));
		}else{
			if(!validateCubeIsANumber(screen)){
				errors.add(RULE_CUBE_MUST_BE_A_NUMBER);
			}else{
				if (!validateCubeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateExtendedpriceNotEmpty(screen)){
			errors.add(new Integer(RULE_EXTENDEDPRICE_NOT_EMPTY));
		}else{
			if(!validateExtendedpriceIsANumber(screen)){
				errors.add(RULE_EXTENDEDPRICE_MUST_BE_A_NUMBER);
			}else{
				if (!validateExtendedpriceGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_EXTENDEDPRICE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateGrosswgtNotEmpty(screen)){
			errors.add(new Integer(RULE_GROSSWGT_NOT_EMPTY));
		}else{
			if(!validateGrosswgtIsANumber(screen)){
				errors.add(RULE_GROSSWGT_MUST_BE_A_NUMBER);
			}else{
				if (!validateGrosswgtGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_GROSSWGT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateInnerpackNotEmpty(screen)){
			errors.add(new Integer(RULE_INNERPACK_NOT_EMPTY));
		}else{
			if(!validateInnerpackIsANumber(screen)){
				errors.add(RULE_INNERPACK_MUST_BE_A_NUMBER);
			}else{
				if (!validateInnerpackGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_INNERPACK_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && !validateNetwgtNotEmpty(screen)){
			errors.add(new Integer(RULE_NETWGT_NOT_EMPTY));
		}else{
			if(!validateNetwgtIsANumber(screen)){
				errors.add(RULE_NETWGT_MUST_BE_A_NUMBER);
			}else{
				if (!validateNetwgtGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_NETWGT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateOtherunit1NotEmpty(screen)){
			errors.add(new Integer(RULE_OTHERUNIT1_NOT_EMPTY));
		}else{
			if(!validateOtherunit1IsANumber(screen)){
				errors.add(RULE_OTHERUNIT1_MUST_BE_A_NUMBER);
			}else{
				if (!validateOtherunit1GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_OTHERUNIT1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateOtherunit2NotEmpty(screen)){
			errors.add(new Integer(RULE_OTHERUNIT2_NOT_EMPTY));
		}else{
			if(!validateOtherunit2IsANumber(screen)){
				errors.add(RULE_OTHERUNIT2_MUST_BE_A_NUMBER);
			}else{
				if (!validateOtherunit2GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_OTHERUNIT2_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePackingslipqtyNotEmpty(screen)){
			errors.add(new Integer(RULE_PACKINGSLIPQTY_NOT_EMPTY));
		}else{
			if(!validatePackingslipqtyIsANumber(screen)){
				errors.add(RULE_PACKINGSLIPQTY_MUST_BE_A_NUMBER);
			}else{
				if (!validatePackingslipqtyGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PACKINGSLIPQTY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePalletNotEmpty(screen)){
			errors.add(new Integer(RULE_PALLET_NOT_EMPTY));
		}else{
			if(!validatePalletIsANumber(screen)){
				errors.add(RULE_PALLET_MUST_BE_A_NUMBER);
			}else{
				if (!validatePalletGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLET_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && validateQcqtyinspectedNotEmpty(screen)){
			if(!validateQcqtyinspectedIsANumber(screen)){
				errors.add(RULE_QCQTYINSPECTED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQcqtyinspectedGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QCQTYINSPECTED_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && validateQcqtyrejectedNotEmpty(screen)){
			if(!validateQcqtyrejectedIsANumber(screen)){
				errors.add(RULE_QCQTYINSPECTED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQcqtyinspectedGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QCQTYINSPECTED_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && !validateQtyadjustedNotEmpty(screen)){
			errors.add(new Integer(RULE_QTYADJUSTED_NOT_EMPTY));
		}else{
			if(!validateQtyadjustedIsANumber(screen)){
				errors.add(RULE_QTYADJUSTED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQtyadjustedGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QTYADJUSTED_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateQtyexpectedNotEmpty(screen)){
			errors.add(new Integer(RULE_QTYEXPECTED_NOT_EMPTY));
		}else{
			if(!validateQtyexpectedIsANumber(screen)){
				errors.add(RULE_QTYEXPECTED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQtyexpectedGreaterThanZero(screen))
					errors.add(new Integer(RULE_QTYEXPECTED_GREATER_THAN_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateQtyreceivedNotEmpty(screen)){
			errors.add(new Integer(RULE_QTYRECEIVED_NOT_EMPTY));
		}else{
			if(!validateQtyreceivedIsANumber(screen)){
				errors.add(RULE_QTYRECEIVED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQtyreceivedGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QTYRECEIVED_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateQtyrejectedNotEmpty(screen)){
			errors.add(new Integer(RULE_QTYREJECTED_NOT_EMPTY));
		}else{
			if(!validateQtyrejectedIsANumber(screen)){
				errors.add(RULE_QTYREJECTED_MUST_BE_A_NUMBER);
			}else{
				if (!validateQtyrejectedGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QTYREJECTED_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(!doAssumeDefaults){
			if(!validateAltskuNotEmpty(screen)){
				errors.add(new Integer(RULE_ALTSKU_NOT_EMPTY));
			}else{
				if(!validateAltskuInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_ALTSKU  ));		
				}
			}

			if(doCheckRequiredFields && !validateIdNotEmpty(screen)){
				errors.add(new Integer(RULE_ID_NOT_EMPTY));
			}
	
			
			if(doCheckRequiredFields && !validateLottable01NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE01_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable02NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE02_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable03NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE03_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable06NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE06_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable07NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE07_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable08NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE08_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable09NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE09_NOT_EMPTY));
			}

			if(doCheckRequiredFields && !validateLottable10NotEmpty(screen)){
				errors.add(new Integer(RULE_LOTTABLE10_NOT_EMPTY));
			}

			
			if(doCheckRequiredFields && !validatePokeyNotEmpty(screen)){
				errors.add(new Integer(RULE_POKEY_NOT_EMPTY));
			}

		}

		if(isInsert) {
			if(doCheckRequiredFields && validateReceiptkeyNotEmpty(screen) && validateReceiptlinenumberNotEmpty(screen) ){
				if(validateASNDetailDoesExist(screen))
					errors.add(new Integer(RULE_RECEIPTKEY_AND_RECEIPTLINENUMBER_UNIQUE));
			}
		}


		return errors;
	}//end validate
	private boolean validateAltskuLengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getAltsku()))
			return true;
		return screen.getAltsku().length() < 51;
	}
	private boolean validateConditioncodeLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getConditioncode()))
			return true;
		return screen.getConditioncode().length() < 11;
	}
	private boolean validateContainerkeyLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getContainerkey()))
			return true;
		return screen.getContainerkey().length() < 19;
	}
	private boolean validateDispositioncodeLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getDispositioncode()))
			return true;
		return screen.getDispositioncode().length() < 11;
	}
	private boolean validateDispositiontypeLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getDispositiontype()))
			return true;
		return screen.getDispositiontype().length() < 11;
	}
	
	private boolean validateExternallotLengthIs100OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getExternallot()))
			return true;
		return screen.getExternallot().length() < 101;
	}
	private boolean validateExternlinenoLengthIs20OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getExternlineno()))
			return true;
		return screen.getExternlineno().length() < 21;
	}
	private boolean validateExternreceiptkeyLengthIs32OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getExternreceiptkey()))
			return true;
		return screen.getExternreceiptkey().length() < 33;
	}
	private boolean validateForte_flagLengthIs6OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getForte_flag()))
			return true;
		return screen.getForte_flag().length() < 7;
	}
	private boolean validateIdLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getId()))
			return true;
		return screen.getId().length() < 19;
	}
	private boolean validateIpskeyLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getIpskey()))
			return true;
		return screen.getIpskey().length() < 11;
	}
	private boolean validateLottable01LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable01()))
			return true;
		return screen.getLottable01().length() < 51;
	}
	private boolean validateLottable02LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable02()))
			return true;
		return screen.getLottable02().length() < 51;
	}
	private boolean validateLottable03LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable03()))
			return true;
		return screen.getLottable03().length() < 51;
	}
	private boolean validateLottable06LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable06()))
			return true;
		return screen.getLottable06().length() < 51;
	}
	private boolean validateLottable07LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable07()))
			return true;
		return screen.getLottable07().length() < 51;
	}
	private boolean validateLottable08LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable08()))
			return true;
		return screen.getLottable08().length() < 51;
	}
	private boolean validateLottable09LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable09()))
			return true;
		return screen.getLottable09().length() < 51;
	}
	private boolean validateLottable10LengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getLottable10()))
			return true;
		return screen.getLottable10().length() < 51;
	}
	private boolean validateMatchlottableLengthIs1OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getMatchlottable()))
			return true;
		return screen.getMatchlottable().length() < 2;
	}
	private boolean validateNotesLengthIs2000OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getNotes()))
			return true;
		return screen.getNotes().length() < 2001;
	}
	private boolean validatePackkeyLengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getPackkey()))
			return true;
		return screen.getPackkey().length() < 51;
	}
	private boolean validatePalletidLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getPalletid()))
			return true;
		return screen.getPalletid().length() < 19;
	}
	private boolean validatePokeyLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getPokey()))
			return true;
		return screen.getPokey().length() < 19;
	}
	private boolean validatePolinenumberLengthIs15OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getPolinenumber()))
			return true;
		return screen.getPolinenumber().length() < 16;
	}
	private boolean validateQcautoadjustLengthIs1OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getQcautoadjust()))
			return true;
		return screen.getQcautoadjust().length() < 2;
	}
	private boolean validateQcrejreasonLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getQcrejreason()))
			return true;
		return screen.getQcrejreason().length() < 11;
	}
	private boolean validateQcrequiredLengthIs1OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getQcrequired()))
			return true;
		return screen.getQcrequired().length() < 2;
	}
	private boolean validateQcstatusLengthIs1OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getQcstatus()))
			return true;
		return screen.getQcstatus().length() < 2;
	}
	private boolean validateQcuserLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getQcuser()))
			return true;
		return screen.getQcuser().length() < 19;
	}
	private boolean validateReasoncodeLengthIs20OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReasoncode()))
			return true;
		return screen.getReasoncode().length() < 21;
	}
	private boolean validateReceiptdetailidLengthIs32OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReceiptdetailid()))
			return true;
		return screen.getReceiptdetailid().length() < 33;
	}
	private boolean validateReceiptkeyLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReceiptkey()))
			return true;
		return screen.getReceiptkey().length() < 11;
	}
	private boolean validateReceiptlinenumberLengthIs5OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReceiptlinenumber()))
			return true;
		return screen.getReceiptlinenumber().length() < 6;
	}
	private boolean validateReturnconditionLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReturncondition()))
			return true;
		return screen.getReturncondition().length() < 11;
	}
	private boolean validateReturnreasonLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReturnreason()))
			return true;
		return screen.getReturnreason().length() < 11;
	}
	private boolean validateReturntypeLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getReturntype()))
			return true;
		return screen.getReturntype().length() < 11;
	}
	private boolean validateRmaLengthIs16OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getRma()))
			return true;
		return screen.getRma().length() < 17;
	}
	private boolean validateSkuLengthIs50OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSku()))
			return true;
		return screen.getSku().length() < 51;
	}
	private boolean validateStatusLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getStatus()))
			return true;
		return screen.getStatus().length() < 11;
	}
	private boolean validateStorerkeyLengthIs15OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getStorerkey()))
			return true;
		return screen.getStorerkey().length() < 16;
	}
	private boolean validateSupplierkeyLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSupplierkey()))
			return true;
		return screen.getSupplierkey().length() < 11;
	}
	private boolean validateSuppliernameLengthIs25OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSuppliername()))
			return true;
		return screen.getSuppliername().length() < 26;
	}
	private boolean validateSusr1LengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSusr1()))
			return true;
		return screen.getSusr1().length() < 31;
	}
	private boolean validateSusr2LengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSusr2()))
			return true;
		return screen.getSusr2().length() < 31;
	}
	private boolean validateSusr3LengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSusr3()))
			return true;
		return screen.getSusr3().length() < 31;
	}
	private boolean validateSusr4LengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSusr4()))
			return true;
		return screen.getSusr4().length() < 31;
	}
	private boolean validateSusr5LengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getSusr5()))
			return true;
		return screen.getSusr5().length() < 31;
	}
	private boolean validateTariffkeyLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getTariffkey()))
			return true;
		return screen.getTariffkey().length() < 11;
	}
	private boolean validateToidLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getToid()))
			return true;
		return screen.getToid().length() < 19;
	}
	private boolean validateTolocLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getToloc()))
			return true;
		return screen.getToloc().length() < 11;
	}
	private boolean validateTolotLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getTolot()))
			return true;
		return screen.getTolot().length() < 11;
	}
	private boolean validateTypeLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getType()))
			return true;
		return screen.getType().length() < 11;
	}
	private boolean validateUomLengthIs10OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getUom()))
			return true;
		return screen.getUom().length() < 11;
	}
	private boolean validateVesselkeyLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getVesselkey()))
			return true;
		return screen.getVesselkey().length() < 19;
	}
	private boolean validateVoyagekeyLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getVoyagekey()))
			return true;
		return screen.getVoyagekey().length() < 19;
	}
	private boolean validateWhseidLengthIs30OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getWhseid()))
			return true;
		return screen.getWhseid().length() < 31;
	}
	private boolean validateXdockkeyLengthIs18OrLess(ASNDetailVO screen){
		if(isEmpty(screen.getXdockkey()))
			return true;
		return screen.getXdockkey().length() < 19;
	}

	
	//Attribute Domains
	private boolean validateAltskuInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getAltsku()))
			return true;
		
		return validateAltSkuDoesExist(screen.getAltsku(), screen.getStorerkey(), getContext());			
	}

	private boolean validateConditioncodeInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getConditioncode()))
			return true;
		
		return validateInventoryHoldCodeDoesExist(screen.getConditioncode(), getContext());			
	}


	private boolean validateDispositiontypeInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getDispositiontype()))
			return true;
		
		return validateCodelkupDoesExist("RETDISPTYP", screen.getDispositiontype(), getContext());			
	}

	private boolean validateDispositioncodeInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getDispositioncode()))
			return true;
		
		return validateCodelkupDoesExist("RETDISPCOD", screen.getDispositioncode(), getContext());			
	}
	
	private boolean validatePackkeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackkey()))
			return true;
		
		return validatePackDoesExist(screen.getPackkey(), getContext());			
	}
	
	
	private boolean validatePokeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPokey()))
			return true;
		
		return validatePurchaseOrderDoesExist(screen.getPokey(), getContext());			
	}
	
		
	private boolean validateQcrequiredInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getQcrequired()))
			return true;
		
		return validateCodelkupDoesExist("YESNO", screen.getQcrequired(), getContext());			
	}

	private boolean validateQcautoadjustInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getQcautoadjust()))
			return true;
		
		return validateCodelkupDoesExist("YESNO", screen.getQcautoadjust(), getContext());			
	}

	private boolean validateQcstatusInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getQcstatus()))
			return true;
		
		return validateCodelkupDoesExist("QCISTATUS", screen.getQcstatus(), getContext());			
	}

	private boolean validateQcrejreasonInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getQcrejreason()))
			return true;
		
		return validateCodelkupDoesExist("QCREJRSN", screen.getQcrejreason(), getContext());			
	}

	private boolean validateReceiptkeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReceiptkey()))
			return true;
		
		return validateReceiptDoesExist(screen.getReceiptkey(), getContext());			
	}

	
	private boolean validateSkuInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getSku()))
			return true;
		
		return validateItemDoesExist(screen.getSku(), screen.getStorerkey(), getContext());			
	}
	
	private boolean validateStatusInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStatus()))
			return true;
		
		return validateCodelkupDoesExist("RECSTATUS", screen.getStatus(), getContext());			
	}

	private boolean validateStorerkeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStorerkey()))
			return true;
		
		return validateStorerDoesExist(screen.getStorerkey(), "1", getContext());			
	}

	private boolean validateSupplierkeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getSupplierkey()))
			return true;
		
		return validateStorerDoesExist(screen.getSupplierkey(), "5", getContext());			
	}

	private boolean validateReturntypeInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReturntype()))
			return true;
		
		return validateCodelkupDoesExist("RETTYPE", screen.getReturntype(), getContext());			
	}

	private boolean validateReturnreasonInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReturnreason()))
			return true;
		
		return validateCodelkupDoesExist("RETREASON", screen.getReturnreason(), getContext());			
	}

	private boolean validateReturnconditionInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReturncondition()))
			return true;
		
		return validateCodelkupDoesExist("RETCOND", screen.getReturncondition(), getContext());			
	}

	private boolean validateTariffkeyInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTariffkey()))
			return true;
		
		return validateTariffDoesExist(screen.getTariffkey(), getContext());			
	}
	
	private boolean validateTolocInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getToloc()))
			return true;
		
		return validateLocationDoesExist(screen.getToloc(), getContext());			
	}
	
	private boolean validateUomInAttrDom(ASNDetailVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getUom()))
			return true;
		
		
		return validateUomDoesExist(screen);
					
	}
	
	private boolean validateUomDoesExist(ASNDetailVO screen)throws WMSDataLayerException{
		DataLayerResultWrapper packs1 = PackQueryRunner.getPackByKeyAndUOM1(screen.getPackkey(), screen.getUom(), getContext());
		if (packs1.getSize()>0){
			return true;
		}
			
		DataLayerResultWrapper packs2 = PackQueryRunner.getPackByKeyAndUOM2(screen.getPackkey(), screen.getUom(), getContext());
		if (packs2.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs3 = PackQueryRunner.getPackByKeyAndUOM3(screen.getPackkey(), screen.getUom(), getContext());
		if (packs3.getSize()>0){
			return true;
		}
		
		
		
		DataLayerResultWrapper packs4 = PackQueryRunner.getPackByKeyAndUOM4(screen.getPackkey(), screen.getUom(), getContext());
		if (packs4.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs5 = PackQueryRunner.getPackByKeyAndUOM5(screen.getPackkey(), screen.getUom(), getContext());
		if (packs5.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs6 = PackQueryRunner.getPackByKeyAndUOM6(screen.getPackkey(), screen.getUom(), getContext());
		if (packs6.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs7 = PackQueryRunner.getPackByKeyAndUOM7(screen.getPackkey(), screen.getUom(), getContext());
		if (packs7.getSize()>0){
			return true;
		}

		
		DataLayerResultWrapper packs8 = PackQueryRunner.getPackByKeyAndUOM8(screen.getPackkey(), screen.getUom(), getContext());
		if (packs8.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs9 = PackQueryRunner.getPackByKeyAndUOM9(screen.getPackkey(), screen.getUom(), getContext());
		if (packs9.getSize()>0){
			return true;
		}


		return false;


	}

	
	
	//	Required - Not null validations
	public boolean validatePackkeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getPackkey());			
	}

	public boolean validateQcrequiredNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQcrequired());			
	}

	public boolean validateUomNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getUom());			
	}

	public boolean validateTolocNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getToloc());			
	}

	public boolean validateAltskuNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getAltsku());			
	}

	public boolean validateCasecntNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getCasecnt());			
	}

	public boolean validateConditioncodeNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getConditioncode());			
	}

	public boolean validateCubeNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getCube());			
	}

	public boolean validateDatereceivedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getDatereceived());			
	}

	public boolean validateEffectivedateNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getEffectivedate());			
	}

	public boolean validateExtendedpriceNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getExtendedprice());			
	}

	public boolean validateExternlinenoNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getExternlineno());			
	}

	public boolean validateExternreceiptkeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getExternreceiptkey());			
	}


	public boolean validateGrosswgtNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getGrosswgt());			
	}
	
	public boolean validateIdNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getId());			
	}


	public boolean validateInnerpackNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getInnerpack());			
	}
	
	public boolean validateLottable01NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable01());			
	}

	public boolean validateLottable02NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable02());			
	}

	public boolean validateLottable03NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable03());			
	}

	public boolean validateLottable06NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable06());			
	}

	public boolean validateLottable07NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable07());			
	}

	public boolean validateLottable08NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable08());			
	}

	public boolean validateLottable09NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable09());			
	}

	public boolean validateLottable10NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getLottable10());			
	}

	public boolean validateNetwgtNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getNetwgt());			
	}

	public boolean validateOtherunit1NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getOtherunit1());			
	}

	public boolean validateOtherunit2NotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getOtherunit2());			
	}
	
	public boolean validatePackingslipqtyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getPackingslipqty());			
	}

	public boolean validatePackkeytNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getPackkey());			
	}

	public boolean validatePalletNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getPallet());			
	}

	public boolean validatePokeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getPokey());			
	}

	public boolean validateQtyadjustedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQtyadjusted());			
	}

	public boolean validateQtyexpectedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQtyexpected());			
	}

	public boolean validateQtyreceivedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQtyreceived());			
	}

	public boolean validateQtyrejectedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQtyrejected());			
	}

	public boolean validateReceiptkeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getReceiptkey());			
	}

	public boolean validateReceiptlinenumberNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getReceiptlinenumber());			
	}

	public boolean validateSkuNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getSku());			
	}

	public boolean validateStatusNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getStatus());			
	}

	public boolean validateStorerkeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getStorerkey());			
	}

	public boolean validateTariffkeyNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getTariffkey());			
	}

	public boolean validateUnitpriceNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getUnitprice());			
	}

	public boolean validateQcqtyinspectedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQcqtyinspected());			
	}

	public boolean validateQcqtyrejectedNotEmpty(ASNDetailVO screen){
		return !isEmpty(screen.getQcqtyrejected());			
	}

	//
	public boolean validateQcrequiredDoesExist(ASNDetailVO screen) throws WMSDataLayerException{
		return validateCodelkupDoesExist("YESNO", screen.getQcrequired(), getContext());
	}
	
	private boolean validateStatusDoesExist(ASNDetailVO screen) throws WMSDataLayerException{
		return validateCodelkupDoesExist("RECSTATUS", screen.getStatus(), getContext());				
	}

	private boolean validateTolocDoesExist(ASNDetailVO screen) throws WMSDataLayerException{
		return validateLocationDoesExist(screen.getToloc(), getContext());				
	}
	
	//Numeric validations
	private boolean validateCasecntIsANumber(ASNDetailVO screen){
		return isNumber(screen.getCasecnt());
	}

	private boolean validateCubeIsANumber(ASNDetailVO screen){
		return isNumber(screen.getCube());
	}
	
	private boolean validateExtendedpriceIsANumber(ASNDetailVO screen){
		return isNumber(screen.getExtendedprice());
	}

	private boolean validateGrosswgtIsANumber(ASNDetailVO screen){
		return isNumber(screen.getGrosswgt());
	}

	private boolean validateInnerpackIsANumber(ASNDetailVO screen){
		return isNumber(screen.getInnerpack());
	}

	private boolean validateNetwgtIsANumber(ASNDetailVO screen){
		return isNumber(screen.getNetwgt());
	}

	private boolean validateOtherunit1IsANumber(ASNDetailVO screen){
		return isNumber(screen.getOtherunit1());
	}

	private boolean validateOtherunit2IsANumber(ASNDetailVO screen){
		return isNumber(screen.getOtherunit2());
	}

	private boolean validatePackingslipqtyIsANumber(ASNDetailVO screen){
		return isNumber(screen.getPackingslipqty());
	}

	private boolean validatePalletIsANumber(ASNDetailVO screen){
		return isNumber(screen.getPallet());
	}

	private boolean validateQcqtyinspectedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQcqtyinspected());
	}

	private boolean validateQcqtyrejectedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQcqtyrejected());
	}

	private boolean validateQtyadjustedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQtyadjusted());
	}

	private boolean validateQtyexpectedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQtyexpected());
	}

	private boolean validateQtyreceivedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQtyreceived());
	}

	private boolean validateQtyrejectedIsANumber(ASNDetailVO screen){
		return isNumber(screen.getQtyrejected());
	}


	
	
	private boolean validateCasecntGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getCasecnt());
	}

	private boolean validateCubeGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getCube());
	}

	private boolean validateExtendedpriceGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getExtendedprice());
	}

	private boolean validateGrosswgtGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getGrosswgt());
	}

	private boolean validateInnerpackGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getInnerpack());
	}
	
	private boolean validateNetwgtGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getNetwgt());
	}

	private boolean validateOtherunit1GreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getOtherunit1());
	}

	private boolean validateOtherunit2GreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getOtherunit2());
	}

	private boolean validatePackingslipqtyGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPackingslipqty());
	}

	private boolean validatePalletGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPallet());
	}

	private boolean validateQcqtyinspectedGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQcqtyinspected());
	}

	private boolean validateQcqtyrejectedGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQcqtyrejected());
	}

	private boolean validateQtyadjustedGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQtyadjusted());
	}

	private boolean validateQtyexpectedGreaterThanZero(ASNDetailVO screen){
		return greaterThanZeroValidation(screen.getQtyexpected());
	}

	private boolean validateQtyreceivedGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQtyreceived());
	}

	private boolean validateQtyrejectedGreaterThanOrEqualZero(ASNDetailVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQtyrejected());
	}


	private boolean validateASNDetailDoesExist(ASNDetailVO screen) throws WMSDataLayerException{
		return BaseScreenValidator.validateReceiptlinenumberDoesExist(screen.getReceiptkey(), 
				screen.getReceiptlinenumber(), getContext());
	}

	public static String getErrorMessage(int errorCode, Locale locale, ASNDetailVO asnScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){


		case RULE_CASECNT_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CASECNT, locale));

		case RULE_CUBE_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CUBE, locale));
			
		case RULE_EXTENDEDPRICE_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTENDEDPRICE, locale));

		case RULE_GROSSWGT_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_GROSSWGT, locale));

		case RULE_INNERPACK_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_INNERPACK, locale));

		case RULE_NETWGT_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_NETWGT, locale));

		case RULE_OTHERUNIT1_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT1, locale));

		case RULE_OTHERUNIT2_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT2, locale));

		case RULE_PACKINGSLIPQTY_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKINGSLIPQTY, locale));

		case RULE_PALLET_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLET, locale));
		case RULE_QCQTYINSPECTED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYINSPECTED, locale));

		case RULE_QCQTYREJECTED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYREJECTED, locale));

		case RULE_QTYADJUSTED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYADJUSTED, locale));

		case RULE_QTYEXPECTED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYEXPECTED, locale));

		case RULE_QTYRECEIVED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYRECEIVED, locale));

		case RULE_QTYREJECTED_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYREJECTED, locale));

		case RULE_UNITPRICE_MUST_BE_A_NUMBER:
			return BaseScreenValidator.getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UNITPRICE, locale));
			
		case RULE_QTYEXPECTED_GREATER_THAN_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYEXPECTED, locale));
			

		case RULE_CASECNT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CASECNT, locale));

		case RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CUBE, locale));

		case RULE_GROSSWGT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_GROSSWGT, locale));

		case RULE_INNERPACK_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_INNERPACK, locale));

		case RULE_NETWGT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_NETWGT, locale));

		case RULE_OTHERUNIT1_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT1, locale));

		case RULE_OTHERUNIT2_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT2, locale));


		case RULE_PACKINGSLIPQTY_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKINGSLIPQTY, locale));

		case RULE_PALLET_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLET, locale));

			
		case RULE_QCQTYINSPECTED_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYINSPECTED, locale));
			

		case RULE_QCQTYREJECTED_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYREJECTED, locale));

		case RULE_QTYADJUSTED_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYADJUSTED, locale));

		case RULE_QTYRECEIVED_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYRECEIVED, locale));

		case RULE_QTYREJECTED_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYREJECTED, locale));

		case RULE_UNITPRICE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UNITPRICE, locale));

			//Unique
		case RULE_RECEIPTKEY_AND_RECEIPTLINENUMBER_UNIQUE:
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTKEY, locale);
			param[1] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTLINENUMBER, locale);
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_RECEIPTDETAIL_SCREEN_ERROR_DUPLICATE_RECEIPT, locale, param);


			//Must exist
		case RULE_PACKKEY_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKKEY, locale),
					asnScreen.getPackkey());

		case RULE_QCREQUIRED_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREQUIRED, locale),
					asnScreen.getPackkey());

		case RULE_UOM_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UOM, locale),
					asnScreen.getPackkey());

		case RULE_TOLOC_MUST_EXIST:
			return BaseScreenValidator.getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOC, locale),
					asnScreen.getPackkey());
			
		case RULE_ALTSKU_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_ALTSKU, locale));

		case RULE_CASECNT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CASECNT, locale));

		case RULE_CONDITIONCODE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CONDITIONCODE, locale));

		case RULE_CUBE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CUBE, locale));

		case RULE_DATERECEIVED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_DATERECEIVED, locale));

		case RULE_EFFECTIVEDATE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EFFECTIVEDATE, locale));

		case RULE_EXTENDEDPRICE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTENDEDPRICE, locale));

		case RULE_EXTERNLINENO_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNLINENO, locale));

		case RULE_EXTERNRECEIPTKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNRECEIPTKEY, locale));

			
		case RULE_GROSSWGT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_GROSSWGT, locale));

		case RULE_ID_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_ID, locale));

		case RULE_INNERPACK_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_INNERPACK, locale));

		case RULE_LOTTABLE01_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE01, locale));

		case RULE_LOTTABLE02_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE02, locale));

		case RULE_LOTTABLE03_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE03, locale));

		case RULE_LOTTABLE06_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE06, locale));

		case RULE_LOTTABLE07_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE07, locale));

		case RULE_LOTTABLE08_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE08, locale));

		case RULE_LOTTABLE09_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE09, locale));


		case RULE_LOTTABLE10_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE10, locale));
			
		case RULE_NETWGT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_NETWGT, locale));
			
			
		case RULE_OTHERUNIT1_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT1, locale));
			
		case RULE_OTHERUNIT2_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT2, locale));
			
		case RULE_PACKINGSLIPQTY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKINGSLIPQTY, locale));
			
		case RULE_PACKKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKKEY, locale));
			
		case RULE_PALLET_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLET, locale));
			
		case RULE_POKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_POKEY, locale));
			
		case RULE_QCREQUIRED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREQUIRED, locale));
			
		case RULE_QTYADJUSTED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYADJUSTED, locale));
			
		case RULE_QTYEXPECTED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYEXPECTED, locale));
			
		case RULE_QTYRECEIVED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYRECEIVED, locale));
			
		case RULE_QTYREJECTED_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYREJECTED, locale));
			
		case RULE_RECEIPTKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTKEY, locale));
			
		case RULE_RECEIPTLINENUMBER_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTLINENUMBER, locale));
			
		case RULE_SKU_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SKU, locale));
			
		case RULE_STATUS_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STATUS, locale));
			
		case RULE_STORERKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STORERKEY, locale));
			
		case RULE_TARIFFKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TARIFFKEY, locale));
			
		case RULE_TOLOC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOC, locale));
			
		case RULE_UNITPRICE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UNITPRICE, locale));
			
		case RULE_UOM_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UOM, locale));
			
			
		case RULE_ATTR_DOM_ALTSKU:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_ALTSKU, locale),
					asnScreen.getAltsku());
			
		case RULE_ATTR_DOM_CONDITIONCODE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CONDITIONCODE, locale),
					asnScreen.getConditioncode());
			
		case RULE_ATTR_DOM_DISPOSITIONTYPE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONTYPE, locale),
					asnScreen.getDispositiontype());
			
		case RULE_ATTR_DOM_DISPOSITIONCODE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONCODE, locale),
					asnScreen.getDispositioncode());
			
		case RULE_ATTR_DOM_PACKKEY:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKKEY, locale),
					asnScreen.getPackkey());
			
		case RULE_ATTR_DOM_POKEY:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_POKEY, locale),
					asnScreen.getPokey());
			
		case RULE_ATTR_DOM_QCREQUIRED:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREQUIRED, locale),
					asnScreen.getQcrequired());
			
		case RULE_ATTR_DOM_QCAUTOADJUST:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCAUTOADJUST, locale),
					asnScreen.getQcautoadjust());
			
		case RULE_ATTR_DOM_QCSTATUS:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCSTATUS, locale),
					asnScreen.getQcstatus());
			
		case RULE_ATTR_DOM_QCREJREASON:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREJREASON, locale),
					asnScreen.getQcrejreason());
			
		case RULE_ATTR_DOM_RECEIPTKEY:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTKEY, locale),
					asnScreen.getReceiptkey());
			
		case RULE_ATTR_DOM_SKU:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SKU, locale),
					asnScreen.getSku());
			
		case RULE_ATTR_DOM_STATUS:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STATUS, locale),
					asnScreen.getStatus());
			
		case RULE_ATTR_DOM_STORERKEY:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STORERKEY, locale),
					asnScreen.getStorerkey());
			
		case RULE_ATTR_DOM_SUPPLIERKEY:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUPPLIERKEY, locale),
					asnScreen.getSupplierkey());
			
		case RULE_ATTR_DOM_RETURNTYPE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNTYPE, locale),
					asnScreen.getReturntype());
			
		case RULE_ATTR_DOM_RETURNREASON:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNREASON, locale),
					asnScreen.getReturnreason());
			
		case RULE_ATTR_DOM_RETURNCONDITION:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNCONDITION, locale),
					asnScreen.getReturncondition());
			
		case RULE_ATTR_DOM_TOLOC:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOC, locale),
					asnScreen.getToloc());
			
		case RULE_ATTR_DOM_UOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UOM, locale),
					asnScreen.getUom());
			
			
			//Length
		case RULE_LENGTH_ALTSKU_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_ALTSKU, locale),
					"50");

		case RULE_LENGTH_CONDITIONCODE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CONDITIONCODE, locale),
					"10");

		case RULE_LENGTH_CONTAINERKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_CONTAINERKEY, locale),
					"18");

		case RULE_LENGTH_DISPOSITIONCODE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONCODE, locale),
					"10");

		case RULE_LENGTH_DISPOSITIONTYPE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONTYPE, locale),
					"10");

		case RULE_LENGTH_EXTERNALLOT_100:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNALLOT, locale),
					"100");

		case RULE_LENGTH_EXTERNLINENO_20:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNLINENO, locale),
					"20");

		case RULE_LENGTH_EXTERNRECEIPTKEY_32:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNRECEIPTKEY, locale),
					"32");


			
			
		case RULE_LENGTH_ID_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_ID, locale),
					"18");

		case RULE_LENGTH_IPSKEY_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_IPSKEY, locale),
					"10");

		case RULE_LENGTH_LOTTABLE01_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE01, locale),
					"50");

		case RULE_LENGTH_LOTTABLE02_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE02, locale),
					"50");

		case RULE_LENGTH_LOTTABLE03_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE03, locale),
					"50");

		case RULE_LENGTH_LOTTABLE06_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE06, locale),
					"50");

		case RULE_LENGTH_LOTTABLE07_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE07, locale),
					"50");

		case RULE_LENGTH_LOTTABLE08_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE08, locale),
					"50");

		case RULE_LENGTH_LOTTABLE09_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE09, locale),
					"50");

		case RULE_LENGTH_LOTTABLE10_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE10, locale),
					"50");

			/*
			 * public static final int RULE_LENGTH_MATCHLOTTABLE_1 = 121;
	public static final int RULE_LENGTH_NOTES_2000 = 122;
	public static final int RULE_LENGTH_PACKKEY_50 = 123;
	public static final int RULE_LENGTH_PALLETID_18 = 124;
	public static final int RULE_LENGTH_POKEY_18 = 125;
	public static final int RULE_LENGTH_POLINENUMBER_15 = 126;
	public static final int RULE_LENGTH_QCAUTOADJUST_1 = 127;
	public static final int RULE_LENGTH_QCREJREASON_10 = 128;
	public static final int RULE_LENGTH_QCREQUIRED_1 = 129;
	public static final int RULE_LENGTH_QCSTATUS_1 = 130;
	public static final int RULE_LENGTH_QCUSER_18 = 131;
	
			 */

		case RULE_LENGTH_MATCHLOTTABLE_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_MATCHLOTTABLE, locale),
					"1");

		case RULE_LENGTH_NOTES_2000:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_NOTES, locale),
					"2000");

		case RULE_LENGTH_PACKKEY_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKKEY, locale),
					"50");

		case RULE_LENGTH_PALLETID_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLETID, locale),
					"18");

		case RULE_LENGTH_POKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_POKEY, locale),
					"18");

		case RULE_LENGTH_POLINENUMBER_15 :
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_POLINENUMBER, locale),
					"15");

		case RULE_LENGTH_QCAUTOADJUST_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCAUTOADJUST, locale),
					"1");

		case RULE_LENGTH_QCREJREASON_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREJREASON, locale),
					"10");

		case RULE_LENGTH_QCREQUIRED_1 :
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREQUIRED, locale),
					"1");

		case RULE_LENGTH_QCSTATUS_1:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCSTATUS, locale),
					"1");

		case RULE_LENGTH_QCUSER_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_QCUSER, locale),
					"18");
/*
 * public static final int RULE_LENGTH_REASONCODE_20 = 132;
	public static final int RULE_LENGTH_RECEIPTDETAILID_32 = 133;
	public static final int RULE_LENGTH_RECEIPTKEY_10 = 134;
	public static final int RULE_LENGTH_RECEIPTLINENUMBER_5 = 135;
	public static final int RULE_LENGTH_RETURNCONDITION_10 = 136;
	public static final int RULE_LENGTH_RETURNREASON_10 = 137;
	public static final int RULE_LENGTH_RETURNTYPE_10 = 138;
	public static final int RULE_LENGTH_RMA_16 = 139;
	public static final int RULE_LENGTH_SKU_50 = 140;
	public static final int RULE_LENGTH_STATUS_10 = 141;
	public static final int RULE_LENGTH_STORERKEY_15 = 142;
	public static final int RULE_LENGTH_SUPPLIERKEY_10 = 143;
	public static final int RULE_LENGTH_SUPPLIERNAME_25 = 144;

 */

		case RULE_LENGTH_REASONCODE_20:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_REASONCODE, locale),
					"20");

		case RULE_LENGTH_RECEIPTDETAILID_32:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTDETAILID, locale),
					"32");

		case RULE_LENGTH_RECEIPTKEY_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTKEY, locale),
					"10");

		case RULE_LENGTH_RECEIPTLINENUMBER_5:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTLINENUMBER, locale),
					"5");

		case RULE_LENGTH_RETURNCONDITION_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNCONDITION, locale),
					"10");

		case RULE_LENGTH_RETURNREASON_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNREASON, locale),
					"10");

		case RULE_LENGTH_RETURNTYPE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNTYPE, locale),
					"10");

		case RULE_LENGTH_RMA_16:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_RMA, locale),
					"16");

		case RULE_LENGTH_SKU_50:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SKU, locale),
					"50");

		case RULE_LENGTH_STATUS_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STATUS, locale),
					"10");

		case RULE_LENGTH_STORERKEY_15:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_STORERKEY, locale),
					"15");

		case RULE_LENGTH_SUPPLIERKEY_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUPPLIERKEY, locale),
					"10");

		case RULE_LENGTH_SUPPLIERNAME_25:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUPPLIERNAME, locale),
					"25");
/*
 * 	public static final int RULE_LENGTH_SUSR1_30 = 145;
	public static final int RULE_LENGTH_SUSR2_30 = 146;
	public static final int RULE_LENGTH_SUSR3_30 = 147;
	public static final int RULE_LENGTH_SUSR4_30 = 148;
	public static final int RULE_LENGTH_SUSR5_30 = 149;
	public static final int RULE_LENGTH_TARIFFKEY_10 = 150;
	public static final int RULE_LENGTH_TOID_18 = 151;
	public static final int RULE_LENGTH_TOLOC_10 = 152;
	public static final int RULE_LENGTH_TOLOT_10 = 153;
	public static final int RULE_LENGTH_TYPE_10 = 154;
	public static final int RULE_LENGTH_UOM_10 = 155;
	public static final int RULE_LENGTH_VESSELKEY_18 = 156;
	public static final int RULE_LENGTH_VOYAGEKEY_18 = 157;
	public static final int RULE_LENGTH_WHSEID_30 = 158;
	public static final int RULE_LENGTH_XDOCKKEY_18 =159;

 */

		case RULE_LENGTH_SUSR1_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR1, locale),
					"30");

		case RULE_LENGTH_SUSR2_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR2, locale),
					"30");

		case RULE_LENGTH_SUSR3_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR3, locale),
					"30");

		case RULE_LENGTH_SUSR4_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR4, locale),
					"30");

		case RULE_LENGTH_SUSR5_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR5, locale),
					"30");

		case RULE_LENGTH_TARIFFKEY_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TARIFFKEY, locale),
					"10");

		case RULE_LENGTH_TOID_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOID, locale),
					"18");

		case RULE_LENGTH_TOLOC_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOC, locale),
					"10");

		case RULE_LENGTH_TOLOT_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOT, locale),
					"10");

		case RULE_LENGTH_TYPE_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_TYPE, locale),
					"10");

		case RULE_LENGTH_UOM_10:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_UOM, locale),
					"10");

		case RULE_LENGTH_VESSELKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_VESSELKEY, locale),
					"18");

		case RULE_LENGTH_VOYAGEKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_VOYAGEKEY, locale),
					"18");

		case RULE_LENGTH_WHSEID_30:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_WHSEID, locale),
					"30");

		case RULE_LENGTH_XDOCKKEY_18:
			return BaseScreenValidator.getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_RECEIPTDETAIL_FIELD_XDOCKKEY, locale),
					"18");
		}
		
		return errorMsg;
	}
}
