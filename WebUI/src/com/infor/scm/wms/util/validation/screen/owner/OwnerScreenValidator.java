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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import com.epiphany.shr.util.exceptions.UserException;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.query.AmstrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.BarcodeConfigQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CCAdjRulesQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CartonizationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.GldistributionQueryRunner;
import com.infor.scm.wms.util.datalayer.query.LocationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.OppOrderStrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PackingValidationTemplateQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PutawayStrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ReceiptValidationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.StorerQueryRunner;
import com.infor.scm.wms.util.datalayer.query.StrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.TaxgroupQueryRunner;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO.AltVO;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO.AssignLocationsVO;
import com.infor.scm.wms.util.validation.util.MessageUtil;
import com.ssaglobal.scm.wms.wm_gl_distribution.ValidateGLDistribution;

public class OwnerScreenValidator extends BaseScreenValidator{
		
	
	public OwnerScreenValidator(WMSValidationContext context) {
		super(context);		
	}

	public static final int RULE_OWNER_MUST_BE_UNIQUE = 0;
	public static final int RULE_SCAC_CODE_MUST_BE_ALPHNUMERIC = 1;

	public static final int RULE_CREDIT_LIMIT_MUST_BE_A_NUMBER = 2;
	public static final int RULE_CREDIT_LIMIT_GREATER_THAN_OR_EQUAL_ZERO = 2;

	public static final int RULE_KSHIP_CARRIER_MUST_BE_A_NUMBER = 451;
	public static final int RULE_KSHIP_CARRIER_MUST_BE_ZERO_OR_ONE = 452;

	
	public static final int RULE_MAXIMUM_ORDERS_MUST_BE_A_NUMBER = 3;
	public static final int RULE_MINIMUM_PERCENT_MUST_BE_A_NUMBER = 4;
	public static final int RULE_ORDER_DATE_START_DAYS_MUST_BE_A_NUMBER = 5;
	public static final int RULE_ORDER_DATE_END_DAYS_MUST_BE_A_NUMBER = 6;

	public static final int RULE_MAXIMUM_ORDERS_GREATER_THAN_OR_EQUAL_ZERO = 7;
	public static final int RULE_MINIMUM_PERCENT_GREATER_THAN_OR_EQUAL_ZERO = 8;
	public static final int RULE_ORDER_DATE_START_DAYS_GREATER_THAN_OR_EQUAL_ZERO = 9;
	public static final int RULE_ORDER_DATE_END_DAYS_GREATER_THAN_OR_EQUAL_ZERO = 10;
	
	public static final int RULE_CREATEOPPXDTASKS_MUST_BE_ZERO_OR_ONE = 459;
	public static final int RULE_ISSUEOPPXDTASKS_MUST_BE_ZERO_OR_ONE = 460;
	
	
	public static final int RULE_HI_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER = 11;
	public static final int RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_A_NUMBER = 12;
	public static final int RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_A_NUMBER = 13;
	public static final int RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER = 14;
	public static final int RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER = 15;
	public static final int RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_A_NUMBER = 16;
	public static final int RULE_AL_MINIMUM_CHARGE_MUST_BE_A_NUMBER = 17;	

	public static final int RULE_CREATEOPPXDTASKS_MUST_BE_A_NUMBER = 457;
	public static final int RULE_ISSUEOPPXDTASKS_MUST_BE_A_NUMBER = 458;
	
	
	
	public static final int RULE_HI_MINIMUM_RECEIPT_CHARGE_MUST_BE_VALID_CURRENCY = 18;
	public static final int RULE_HO_MINIMUM_SHIPMENT_CHARGE_MUST_BE_VALID_CURRENCY = 19;
	public static final int RULE_IS_MINIMUM_RECEIPT_CHARGE_MUST_BE_VALID_CURRENCY = 20;
	public static final int RULE_HI_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY = 21;
	public static final int RULE_IS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY = 22;
	public static final int RULE_RS_MINIMUM_INVOICE_CHARGE_MUST_BE_VALID_CURRENCY= 23;
	public static final int RULE_AL_MINIMUM_CHARGE_MUST_BE_VALID_CURRENCY = 24;	
	
	public static final int RULE_HI_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO = 25;
	public static final int RULE_HO_MINIMUM_SHIPMENT_CHARGE_GREATER_THAN_ZERO = 26;
	public static final int RULE_IS_MINIMUM_RECEIPT_CHARGE_GREATER_THAN_ZERO = 27;
	public static final int RULE_HI_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO = 28;
	public static final int RULE_IS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO = 29;
	public static final int RULE_RS_MINIMUM_INVOICE_CHARGE_GREATER_THAN_ZERO = 30;
	public static final int RULE_AL_MINIMUM_CHARGE_GREATER_THAN_ZERO = 31;
	
	public static final int RULE_DEFAULT_QC_LOC_MUST_BE_VALID_LOCATION = 32;
	public static final int RULE_DEFAULT_QC_LOC_OUT_MUST_BE_VALID_LOCATION = 33;
	public static final int RULE_DEFAULT_RETURNS_LOC_MUST_BE_VALID_LOCATION = 34;
	public static final int RULE_DEFAULT_PACKING_LOC_MUST_BE_VALID_LOCATION = 35;
	
	public static final int RULE_LPN_START_NUMBER_MUST_BE_A_NUMBER = 36;
	public static final int RULE_LPN_START_NUMBER_MUST_BE_GREATER_THAN_ZERO = 37;
	public static final int RULE_LPN_START_NUMBER_NOT_ALL_NINE = 38;
	
	public static final int RULE_UCC_VENDOR_NUMBER_MUST_BE_A_NUMBER = 39;
	public static final int RULE_UCC_VENDOR_NUMBER_MUST_BE_GREATER_THAN_ZERO = 40;
	public static final int RULE_NEXT_LPN_NUMBER_GREATER_THAN_OR_EQUAL_LPN_START_NUMBER = 41;
	public static final int RULE_LPN_ROLLBACK_NUMBER_GREATER_THAN_OR_EQUAL_NEXT_LPN_NUMBER = 42;
	public static final int RULE_LPN_START_NUMBER_COMPLIES_WITH_LPN_LENGTH = 43;
	public static final int RULE_NEXT_LPN_NUMBER_COMPLIES_WITH_LPN_LENGTH = 44;
	public static final int RULE_LPN_ROLLBACK_NUMBER_COMPLIES_WITH_LPN_LENGTH = 45;
	public static final int RULE_UCC_VENDOR_NUMBER_COMPLIES_WITH_BARCODE_FORMAT = 46;
	public static final int RULE_CONSIGNEE_KEY_MUST_BE_VALID_CUSTOMER = 47;

	
	//Required
	public static final int RULE_LPN_LENGTH_NOT_EMPTY = 100;
	public static final int RULE_STORER_KEY_NOT_EMPTY = 101;
	

	
	
	//Field Length Rules
	
	public static final int RULE_LENGTH_ADDRESS1_45 = 200;
	public static final int RULE_LENGTH_ADDRESS2_45 = 201;
	public static final int RULE_LENGTH_ADDRESS3_45 = 202;
	public static final int RULE_LENGTH_ADDRESS4_45 = 203;
	public static final int RULE_LENGTH_ALLOWAUTOCLOSEFORASN_1 = 205;
	public static final int RULE_LENGTH_ALLOWAUTOCLOSEFORPO_1 = 206;
	public static final int RULE_LENGTH_ALLOWAUTOCLOSEFORPS_1 = 207;
	public static final int RULE_LENGTH_ALLOWCOMMINGLEDLPN_1 = 208;
	public static final int RULE_LENGTH_ALLOWDUPLICATELICENSEPLATES_1 = 209;
	public static final int RULE_LENGTH_ALLOWOVERSHIPMENT_1 = 210;
	public static final int RULE_LENGTH_ALLOWSINGLESCANRECEIVING_1 = 211;
	public static final int RULE_LENGTH_ALLOWSYSTEMGENERATEDLPN_1 = 212;
	public static final int RULE_LENGTH_APPLICATIONID_2 = 213;
	public static final int RULE_LENGTH_APPORTIONRULE_10 = 214;
	public static final int RULE_LENGTH_AUTOCLOSEASN_1 = 215;
	public static final int RULE_LENGTH_AUTOCLOSEPO_1 = 216;
	public static final int RULE_LENGTH_AUTOPRINTLABELLPN_1 = 217;
	public static final int RULE_LENGTH_AUTOPRINTLABELPUTAWAY_1 = 218;
	public static final int RULE_LENGTH_B_ADDRESS1_45 = 219;
	public static final int RULE_LENGTH_B_ADDRESS2_45 = 220;
	public static final int RULE_LENGTH_B_ADDRESS3_45 = 221;
	public static final int RULE_LENGTH_B_ADDRESS4_45 = 222;
	public static final int RULE_LENGTH_B_CITY_45 = 223;
	public static final int RULE_LENGTH_B_COMPANY_45 = 224;
	public static final int RULE_LENGTH_B_CONTACT1_30 = 225;
	public static final int RULE_LENGTH_B_CONTACT2_30 = 226;
	public static final int RULE_LENGTH_B_COUNTRY_30 = 227;
	public static final int RULE_LENGTH_B_EMAIL1_60 = 228;
	public static final int RULE_LENGTH_B_EMAIL2_60 = 229;
	public static final int RULE_LENGTH_B_FAX1_18 = 230;
	public static final int RULE_LENGTH_B_FAX2_18 = 231;
	public static final int RULE_LENGTH_B_ISOCNTRYCODE_10 = 232;
	public static final int RULE_LENGTH_B_PHONE1_18 = 233;
	public static final int RULE_LENGTH_B_PHONE2_18 = 234;
	public static final int RULE_LENGTH_B_STATE_25 = 235; // Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
	public static final int RULE_LENGTH_B_ZIP_18 = 236;
	public static final int RULE_LENGTH_BARCODECONFIGKEY_18 = 237;
	public static final int RULE_LENGTH_CALCULATEPUTAWAYLOCATION_10 = 238;
	public static final int RULE_LENGTH_CARTONGROUP_10 = 239;
	public static final int RULE_LENGTH_CaseLabelType_10 = 240;
	public static final int RULE_LENGTH_CCADJBYRF_10 = 241;
	public static final int RULE_LENGTH_CCDISCREPANCYRULE_10 = 242;
	public static final int RULE_LENGTH_CCSKUXLOC_1 = 243;
	public static final int RULE_LENGTH_CITY_45 = 244;
	public static final int RULE_LENGTH_COMPANY_45 = 245;
	public static final int RULE_LENGTH_CONTACT1_30 = 246;
	public static final int RULE_LENGTH_CONTACT2_30 = 247;
	public static final int RULE_LENGTH_COUNTRY_30 = 248;
	public static final int RULE_LENGTH_CREATEPATASKONRFRECEIPT_10 = 249;
	public static final int RULE_LENGTH_CREDITLIMIT_18 = 250;
	public static final int RULE_LENGTH_CWOFLAG_1 = 251;
	public static final int RULE_LENGTH_DEFAULTPACKINGLOCATION_10 = 252;
	public static final int RULE_LENGTH_DEFAULTPUTAWAYSTRATEGY_10 = 253;
	public static final int RULE_LENGTH_DEFAULTQCLOC_10 = 254;
	public static final int RULE_LENGTH_DEFAULTQCLOCOUT_10 = 255;
	public static final int RULE_LENGTH_DEFAULTRETURNSLOC_10 = 256;
	public static final int RULE_LENGTH_DEFAULTROTATION_1 = 257;
	public static final int RULE_LENGTH_DEFAULTSKUROTATION_10 = 258;
	public static final int RULE_LENGTH_DEFAULTSTRATEGY_10 = 259;
	public static final int RULE_LENGTH_DESCRIPTION_50 = 260;
	public static final int RULE_LENGTH_DUPCASEID_1 = 261;
	public static final int RULE_LENGTH_EDITWHO_18 = 262;
	public static final int RULE_LENGTH_EMAIL1_60 = 263;
	public static final int RULE_LENGTH_EMAIL2_60 = 264;
	public static final int RULE_LENGTH_ENABLEOPPXDOCK_1 = 265;
	public static final int RULE_LENGTH_ENABLEPACKINGDEFAULT_1 = 266; 
	
	public static final int RULE_LENGTH_FAX1_18 = 267;
	public static final int RULE_LENGTH_FAX2_18 = 268;
	public static final int RULE_LENGTH_GENERATEPACKLIST_1 = 269; 
	public static final int RULE_LENGTH_INSPECTATPACK_1 = 270;
	
	public static final int RULE_LENGTH_ISOCNTRYCODE_10 = 271;
	public static final int RULE_LENGTH_LPNBARCODEFORMAT_60 = 272;
	public static final int RULE_LENGTH_LPNBARCODESYMBOLOGY_60 = 273;
	public static final int RULE_LENGTH_LPNROLLBACKNUMBER_18 = 274;
	public static final int RULE_LENGTH_LPNSTARTNUMBER_18 = 275;
	public static final int RULE_LENGTH_MULTIZONEPLPA_1 = 276;
	public static final int RULE_LENGTH_NEXTLPNNUMBER_18 = 277;
	public static final int RULE_LENGTH_NOTES1_2147483647 = 278;
	public static final int RULE_LENGTH_NOTES2_2147483647 = 279;
	public static final int RULE_LENGTH_OPPORDERSTRATEGYKEY_10 = 280;
	public static final int RULE_LENGTH_ORDERBREAKDEFAULT_1 = 281;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT01_10 = 282;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT02_10 = 283;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT03_10 = 284;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT04_10 = 285;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT05_10 = 286;
	public static final int RULE_LENGTH_ORDERTYPERESTRICT06_10 = 287;
	public static final int RULE_LENGTH_PACKINGVALIDATIONTEMPLATE_10 = 288;
	
	
	public static final int RULE_LENGTH_PHONE1_18 = 289;
	public static final int RULE_LENGTH_PHONE2_18 = 290;
	public static final int RULE_LENGTH_PICKCODE_10 = 291;
	public static final int RULE_LENGTH_PISKUXLOC_1 = 292;
	public static final int RULE_LENGTH_RECEIPTVALIDATIONTEMPLATE_18 = 293;
	public static final int RULE_LENGTH_ROLLRECEIPT_1 = 294;
	public static final int RULE_LENGTH_SCAC_CODE_4 = 295;
	public static final int RULE_LENGTH_SKUSETUPREQUIRED_1 = 296;
	public static final int RULE_LENGTH_STATE_25 = 297; // Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
	public static final int RULE_LENGTH_STATUS_18 = 298;
	public static final int RULE_LENGTH_STORERKEY_15 = 299;
	public static final int RULE_LENGTH_SUSR1_30 = 300;
	public static final int RULE_LENGTH_SUSR2_30 = 301;
	public static final int RULE_LENGTH_SUSR3_30 = 302;
	public static final int RULE_LENGTH_SUSR4_30 = 303;
	public static final int RULE_LENGTH_SUSR5_30 = 304;
	public static final int RULE_LENGTH_SUSR6_30 = 305;
	public static final int RULE_LENGTH_TITLE1_50 = 306;
	public static final int RULE_LENGTH_TITLE2_50 = 307;
	public static final int RULE_LENGTH_TRACKINVENTORYBY_1 = 308;
	public static final int RULE_LENGTH_TYPE_30 = 309;
	public static final int RULE_LENGTH_UCCVENDORNUMBER_9 = 310;
	public static final int RULE_LENGTH_VAT_18 = 311;
	public static final int RULE_LENGTH_WHSEID_30 = 312;
	public static final int RULE_LENGTH_ZIP_18 = 313;
	public static final int RULE_OPPXDPICKFROM_20 = 453;
	public static final int RULE_OBXDSTAGE_20 = 454;
	public static final int RULE_SPSUOMWEIGHT_10 = 455;
	public static final int RULE_SPSUOMDIMENSION_10 = 456;
	
	//Attribute Domain Rules
	public static final int RULE_ATTR_DOM_ISO_COUNTRY_CODE = 400;
	public static final int RULE_ATTR_DOM_CARTON_GROUP = 401;
	public static final int RULE_ATTR_DOM_ORDER_SEQUENCE_STRATEGY = 402;
	public static final int RULE_ATTR_DOM_AUTOMATIC_APPORTIONMENT_RULE = 403;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_1 = 404;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_2 = 405;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_3 = 406;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_4 = 407;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_5 = 408;
	public static final int RULE_ATTR_DOM_ORDER_TYPE_6 = 409;	
	public static final int RULE_ATTR_DOM_INVOICE_NUMBER_STRATEGY = 410;	
	public static final int RULE_ATTR_DOM_BILLING_GROUP = 411;	
	public static final int RULE_ATTR_DOM_TAX_GROUP_1 = 412;
	public static final int RULE_ATTR_DOM_TAX_GROUP_2 = 413;
	public static final int RULE_ATTR_DOM_TAX_GROUP_3 = 414;
	public static final int RULE_ATTR_DOM_TAX_GROUP_4 = 415;
	public static final int RULE_ATTR_DOM_TAX_GROUP_5 = 416;
	public static final int RULE_ATTR_DOM_TAX_GROUP_6 = 417;
	public static final int RULE_ATTR_DOM_TAX_GROUP_7 = 418;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_1 = 419;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_2 = 420;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_3 = 421;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_4 = 422;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_5 = 423;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_6 = 424;
	public static final int RULE_ATTR_DOM_GL_DISTRIBUTION_7 = 425;
	public static final int RULE_ATTR_DOM_DEFAULT_ITEM_ROTATION = 426;
	public static final int RULE_ATTR_DOM_DEFAULT_ROTATION = 427;
	public static final int RULE_ATTR_DOM_DEFAULT_STRATEGY = 428;
	public static final int RULE_ATTR_DOM_DEFAULT_PUTAWAY_STRATEGY = 429;
	public static final int RULE_ATTR_DOM_CREATE_PUTAWAY_TASK_ON_RF_RECEIPT = 430;
	public static final int RULE_ATTR_DOM_CALCULATE_PUTAWAY_LOCATION = 431;
	public static final int RULE_ATTR_DOM_ASSIGNMENT_ORDER_BREAK_DEFAULT = 432;
	public static final int RULE_ATTR_DOM_PACKING_VALIDATION_TEMPLATE = 433;
	public static final int RULE_ATTR_DOM_LPN_BARCODE_SYMBOLOGY = 434;
	public static final int RULE_ATTR_DOM_LPN_BARCODE_FORMAT = 435;
	public static final int RULE_ATTR_DOM_LPN_LENGTH = 436;
	public static final int RULE_ATTR_DOM_CASE_LABEL_TYPE = 437;
	public static final int RULE_ATTR_DOM_SSCC_1ST_DIGIT = 438;
	public static final int RULE_ATTR_DOM_DEFAULT_DISCREPANCY_HANDLING_RULE = 439;
	public static final int RULE_ATTR_DOM_CYCLE_COUNT_ADJUSTING_DURING_RF = 440;
	public static final int RULE_ATTR_DOM_ALLOW_COMMINGLED_LPN = 441;
	public static final int RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_ASN = 442;
	public static final int RULE_ATTR_DOM_ALLOW_AUTO_CLOSE_PO = 443;
	public static final int RULE_ATTR_DOM_ALLOW_SYSTEM_GENERATED_LPN = 444;
	public static final int RULE_ATTR_DOM_ALLOW_SINGLE_SCAN_RECEIVING = 445;
	public static final int RULE_ATTR_DOM_RECEIPT_VALIDATION_TEMPLATE = 446;
	public static final int RULE_ATTR_DOM_BARCODE_CONFIGURATION_KEY = 447;
	public static final int RULE_ATTR_DOM_DEFAULT_TRACKING_FOR_CASE_RECEIPTS = 448;
	public static final int RULE_ATTR_DOM_DUPLICATE_CASE_IDS = 449;
	public static final int RULE_ATTR_DOM_DEFAULT_REPLENISHMENT_LOCATION_SORT = 470;
	public static final int RULE_ATTR_DOM_DEFAULT_DYNAMIC_PICKING_LOCATION_SORT = 471;
	public static final int RULE_ATTR_DOM_REQUIRE_REASON_FOR_SHORT_SHIPMENT = 472;
	public static final int RULE_ATTR_DOM_EXPLODE_LPN_LENGTH = 473;
	public static final int RULE_ATTR_DOM_CONTAINER_EXCHANGE_FLAG = 474;
	public static final int RULE_ATTR_DOM_FLOW_THRU_TASK_CONTROL = 475;
	public static final int RULE_ATTR_DOM_FLOW_THRU_LABEL_PRINT_CONTROL = 476;
	public static final int RULE_ATTR_DOM_CARTONIZE_FLOW_THRU_ORDERS_DEFAULT = 477;
	//SRG: 9.2 Upgrade -- Begin	
	public static final int RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_A_NUMBER = 478;
	public static final int RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_ZERO_OR_ONE = 479;
	public static final int RULE_LENGTH_ADDRESS5_45 = 480;
	public static final int RULE_LENGTH_ADDRESS6_45 = 481;	
	public static final int RULE_ADDRESS_OVERWRITE_INDICATOR_MUST_BE_ZERO_OR_ONE = 482;	
	public static final int RULE_LENGTH_ARCORP_40 = 483;
	public static final int RULE_LENGTH_ARDEPT_40 = 484;
	public static final int RULE_LENGTH_ARACCT_40 = 485;
	public static final int RULE_LENGTH_MEASURECODE_10 = 486;
	public static final int RULE_LENGTH_WGTUOM_20 = 487;
	public static final int RULE_LENGTH_DIMENUOM_20 = 488;
	public static final int RULE_LENGTH_CUBEUOM_20 = 489;
	public static final int RULE_LENGTH_CURRCODE_3 = 490;
	public static final int RULE_LENGTH_TAXEXEMPT_2 = 491;
	public static final int RULE_LENGTH_TAXEXEMPTCODE_40 = 492;
	public static final int RULE_LENGTH_RECURCODE_10 = 493;
	public static final int RULE_LENGTH_DUNSID_40 = 494;
	public static final int RULE_LENGTH_TAXID_40 = 495;
	public static final int RULE_QFSURCHARGE_MUST_BE_A_NUMBER = 496;
	public static final int RULE_QFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO = 497;	
	public static final int RULE_BFSURCHARGE_MUST_BE_A_NUMBER = 498;
	public static final int RULE_BFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO = 499;
	public static final int RULE_LENGTH_INVOICETERMS_10 = 500;
	public static final int RULE_LENGTH_INVOICELEVEL_20 = 501;
	public static final int RULE_LENGTH_NONNEGLEVEL_20 = 502;	
	public static final int RULE_DEFFT_TASK_CONTROL_MUST_BE_ZERO_OR_ONE = 503;	
	public static final int RULE_DEFFT_LABEL_PRINT_MUST_BE_ZERO_OR_ONE = 504;	
	public static final int RULE_CARTONIZE_FTDFLT_MUST_BE_ZERO_OR_ONE = 505;	
	public static final int RULE_SKU_SETUP_REQUIRED_MUST_BE_ZERO_OR_ONE = 506;
	public static final int RULE_ATTR_DOM_AMSTRATEGYKEY = 507;
	public static final int RULE_REQ_REASON_SHORT_SHIP_MUST_BE_A_NUMBER = 508;
	public static final int RULE_REQ_REASON_SHORT_SHIP_MUST_BE_ZERO_OR_ONE = 509;	
	public static final int RULE_LENGTH_SPSACCOUNTNUM_40 = 510;
	public static final int RULE_LENGTH_SPSCOSTCENTER_20 = 511;
	public static final int RULE_SPS_RETURN_LABEL_MUST_BE_Y_OR_N = 512;
	public static final int RULE_LENGTH_OWNERPREFIX_18 = 513;
	public static final int RULE_EXPLODE_LPN_LENGTH_MUST_BE_A_NUMBER = 514;
	public static final int RULE_EXPLODE_LPN_LENGTH_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO = 515;
	public static final int RULE_LENGTH_EXPLODENEXTLPNNUMBER_18 = 516;
	public static final int RULE_LENGTH_EXPLODELPNROLLBACKNUMBER_18 = 517;
	public static final int RULE_LENGTH_EXPLODELPNSTARTNUMBER_18 = 518;
	public static final int RULE_ATTR_DOM_CURRCODE = 519;
	public static final int RULE_LENGTH_ACCOUNTINGENTITY_64 = 520;
	public static final int RULE_PARENT_MUST_EXIST = 521;
	//SRG: 9.2 Upgrade -- End
	
	
	public ArrayList validate(OwnerScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		
		
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
			
			if (!validateOppxdpickfromIs20OrLess(screen)){
				errors.add(new Integer(RULE_OPPXDPICKFROM_20));
			}
			
			if (!validateObxdstageIs20OrLess(screen)){
				errors.add(new Integer(RULE_OBXDSTAGE_20));
			}
			
			if (!validateSpsuomweightIs10OrLess(screen)){
				errors.add(new Integer(RULE_SPSUOMWEIGHT_10));
			}
			
			if (!validateSpsuomdimensionIs10OrLess(screen)){
				errors.add(new Integer(RULE_SPSUOMDIMENSION_10));
			}
			
			//SRG: 9.2 Upgrade -- Start
			if (!validateAddress5LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS5_45));
			}
			if (!validateAddress6LengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ADDRESS6_45));
			}
			if (!validateArcorpLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ARCORP_40));
			}
			if (!validateArdeptLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ARDEPT_40));
			}
			if (!validateAracctLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ARACCT_40));
			}
			if (!validateMeasurecodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_MEASURECODE_10));
			}			
			if (!validateWgtuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WGTUOM_20));
			}
			if (!validateDimenuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DIMENUOM_20));
			}
			if (!validateCubeuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CUBEUOM_20));
			}
			if (!validateCurrcodeLengthIs3OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CURRCODE_3));
			}
			if (!validateTaxexemptLengthIs2OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TAXEXEMPT_2));
			}
			if (!validateTaxexemptcodeLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TAXEXEMPTCODE_40));
			}
			if (!validateRecurcodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECURCODE_10));
			}
			if (!validateDunsidLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DUNSID_40));
			}
			if (!validateTaxidLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TAXID_40));
			}
			if (!validateInvoicetermsLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INVOICETERMS_10));
			}
			if (!validateInvoicelevelLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INVOICELEVEL_20));
			}			
			if (!validateNonneglevelLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_NONNEGLEVEL_20));
			}
			if (!validateSpsaccountnumLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SPSACCOUNTNUM_40));
			}
			if (!validateSpscostcenterLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SPSCOSTCENTER_20));
			}
			if (!validateOwnerprefixLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OWNERPREFIX_18));
			}
			if (!validateExplodenextlpnnumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXPLODENEXTLPNNUMBER_18));
			}
			if (!validateExplodelpnrollbacknumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXPLODELPNROLLBACKNUMBER_18));
			}
			if (!validateExplodelpnstartnumberLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_EXPLODELPNSTARTNUMBER_18));
			}						
			//SRG: 9.2 Upgrade -- End
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
			
			if(!validateDefRplnSortInAttrDom(screen)) {
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_REPLENISHMENT_LOCATION_SORT));
			}
			
			if(!validateDefDaPickSortInAttrDom(screen)) {
				errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_DYNAMIC_PICKING_LOCATION_SORT));
			}
			
			if(!validateReqReasonShortShipInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_REQUIRE_REASON_FOR_SHORT_SHIPMENT));
			}
			
			if(!validateExplodeLpnLengthInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_EXPLODE_LPN_LENGTH));
			}
			
			if(!validateContainerExchangeFlagInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CONTAINER_EXCHANGE_FLAG));
			}
			
			if(!validateFlowThruTaskControlInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_FLOW_THRU_TASK_CONTROL));
			}
			
			if(!validateFlowThruTaskLabelPrintControlInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_FLOW_THRU_LABEL_PRINT_CONTROL));
			}
			
			if(!validateCartonizeFlowThruOrdersDefaultInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CARTONIZE_FLOW_THRU_ORDERS_DEFAULT));
			}
			
			//SRG: 9.2 Upgrade -- Start
			if(!validateAmstrategykeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_AMSTRATEGYKEY));		
			}
			
			if(!validateCurrcodeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CURRCODE));		
			}
			//SRG: 9.2 Upgrade -- End
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
		
		
		if (!validateLpnlengthNotEmpty(screen)){
			errors.add(new Integer(RULE_LPN_LENGTH_NOT_EMPTY));
		}
		
		if (!validateStorerkeyNotEmpty(screen)){
			errors.add(new Integer(RULE_STORER_KEY_NOT_EMPTY));
		}
		
		
		if(validateScac_codeNotEmpty(screen)){
			if(!validateScac_codeIsAlpha(screen))
				errors.add(new Integer(RULE_SCAC_CODE_MUST_BE_ALPHNUMERIC));
		}
		
		//jp.bugaware.9437.begin
		if(validateCreateoppxdtasksNotEmpty(screen)){
			if(!validateCreateoppxdtasksIsANumber(screen))
				errors.add(new Integer(RULE_CREATEOPPXDTASKS_MUST_BE_A_NUMBER));
			else{
				if(!validateCreateoppxdtasksZeroOrOne(screen))
					errors.add(new Integer(RULE_CREATEOPPXDTASKS_MUST_BE_ZERO_OR_ONE));
			}
		}

		if(validateIssueoppxdtasksNotEmpty(screen)){
			if(!validateIssueoppxdtasksIsANumber(screen))
				errors.add(new Integer(RULE_ISSUEOPPXDTASKS_MUST_BE_A_NUMBER));
			else{
				if(!validateIssueoppxdtasksZeroOrOne(screen))
					errors.add(new Integer(RULE_ISSUEOPPXDTASKS_MUST_BE_ZERO_OR_ONE));
			}
		}
		//jp.bugaware.9437.end
		if(isInsert){
			if(doCheckRequiredFields && !validateStorerkeyNotEmpty(screen))
				errors.add(new Integer(RULE_STORER_KEY_NOT_EMPTY));
			else{
				if(validateStorerkeyDoesExist(screen.getStorerkey(), screen.getType(), getContext()))
					errors.add(new Integer(RULE_OWNER_MUST_BE_UNIQUE));
			}
			
		}
		
		//SRG: 9.2 Upgrade -- Begin
		if(validateContainerexchangeflagNotEmpty(screen)){
			if(!validateContainerexchangeflagIsANumber(screen))
				errors.add(new Integer(RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_A_NUMBER));
			else{
				if(!validateContainerexchangeflagZeroOrOne(screen))
					errors.add(new Integer(RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_ZERO_OR_ONE));
			}
		}
		
		if(validateAddressoverwriteindicatorNotEmpty(screen)){			
			if(!validateAddressoverwriteindicatorZeroOrOne(screen))
				errors.add(new Integer(RULE_ADDRESS_OVERWRITE_INDICATOR_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateDeffttaskcontrolNotEmpty(screen)){			
			if(!validateDeffttaskcontrolZeroOrOne(screen))
				errors.add(new Integer(RULE_DEFFT_TASK_CONTROL_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateDefftlabelprintNotEmpty(screen)){			
			if(!validateDefftlabelprintZeroOrOne(screen))
				errors.add(new Integer(RULE_DEFFT_TASK_CONTROL_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateCartonizeftdfltNotEmpty(screen)){			
			if(!validateCartonizeftdfltZeroOrOne(screen))
				errors.add(new Integer(RULE_CARTONIZE_FTDFLT_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateSkusetuprequiredNotEmpty(screen)){			
			if(!validateSkusetuprequiredZeroOrOne(screen))
				errors.add(new Integer(RULE_CARTONIZE_FTDFLT_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateReqreasonshortshipNotEmpty(screen)){
			if(!validateReqreasonshortshipIsANumber(screen))
				errors.add(new Integer(RULE_REQ_REASON_SHORT_SHIP_MUST_BE_A_NUMBER));
			else{
				if(!validateReqreasonshortshipZeroOrOne(screen))
					errors.add(new Integer(RULE_REQ_REASON_SHORT_SHIP_MUST_BE_ZERO_OR_ONE));
			}
		}
		
		if(validateExplodelpnlengthNotEmpty(screen)){
			if(!validateExplodelpnlengthIsANumber(screen))
				errors.add(new Integer(RULE_EXPLODE_LPN_LENGTH_MUST_BE_A_NUMBER));
			else{
				if(!validateExplodelpnlengthGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_EXPLODE_LPN_LENGTH_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO));
			}			
		}
		
		if(validateQfsurchargeNotEmpty(screen)){
			if(!validateQfsurchargeIsANumber(screen))
				errors.add(new Integer(RULE_QFSURCHARGE_MUST_BE_A_NUMBER));			
			else{
				if(!validateQfsurchargeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(validateBfsurchargeNotEmpty(screen)){
			if(!validateBfsurchargeIsANumber(screen))
				errors.add(new Integer(RULE_BFSURCHARGE_MUST_BE_A_NUMBER));			
			else{
				if(!validateBfsurchargeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_BFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(validateSpsreturnlabelNotEmpty(screen)){
			if(!validateSpsreturnlabelIsYesOrNo(screen)) {
				errors.add(new Integer(RULE_SPS_RETURN_LABEL_MUST_BE_Y_OR_N));			
			}
		}
		//SRG: 9.2 Upgrade -- End
		
		
		/*
		 
		 public static final int RULE_CONSIGNEE_KEY_MUST_BE_VALID_CUSTOMER = 46;
		 
		 
		 */
		return errors;
	}//validate

	private boolean validateFlowThruTaskControlInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCartonizeftdflt()))
			return true;
		return validateFlowThruTaskControlDoesExist(screen.getCartonizeftdflt(), getContext());
	}
	private boolean validateFlowThruTaskControlDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "FTTASKS", key, context).getSize() == 0)
			return false;
		else
			return true;
	}
	
	private boolean validateFlowThruTaskLabelPrintControlInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefftlabelprint()))
			return true;
		return validateFlowThruTaskLabelPrintControlDoesExist(screen.getDefftlabelprint(), getContext());
	}
	private boolean validateFlowThruTaskLabelPrintControlDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "FTLABELCTL", key, context).getSize() == 0)
			return false;
		else
			return true;
	}
	
	private boolean validateCartonizeFlowThruOrdersDefaultInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCartonizeftdflt()))
			return true;
		return validateCartonizeFlowThruOrdersDefaultDoesExist(screen.getCartonizeftdflt(), getContext());
	}
	private boolean validateCartonizeFlowThruOrdersDefaultDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;
	}		



	private boolean validateContainerExchangeFlagInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getContainerExchangeFlag()))
			return true;
		return validateContainerExchangeFlagDoesExist(screen.getContainerExchangeFlag(), getContext());
	}
	private boolean validateContainerExchangeFlagDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;
	}

	private boolean validateExplodeLpnLengthInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getExplodelpnlength()))
			return true;
		return validateExplodeLpnLengthDoesExist(screen.getExplodelpnlength(), getContext());
	}
	private boolean validateExplodeLpnLengthDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "IDLENGTH", key, context).getSize() == 0)
			return false;
		else
			return true;
	}	
	
	private boolean validateReqReasonShortShipInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getReqreasonshortship()))
			return true;
		return validateReqReasonShortShipDoesExist(screen.getReqreasonshortship(), getContext());
	}
	private boolean validateReqReasonShortShipDoesExist(
			String key, WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;
	}


	private boolean validateDefDaPickSortInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefdapicksort()))
			return true;
		return validateDefDaPickSortDoesExist(screen.getDefdapicksort(), getContext());
	}
	private boolean validateDefDaPickSortDoesExist(String key,
			WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "DAPICKSORT", key, context).getSize() == 0)
			return false;
		else
			return true;
	}


	private boolean validateDefRplnSortInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefrplnsort()))
			return true;
		return validateDefRplnSortDoesExist(screen.getDefrplnsort(), getContext());
	}
	private boolean validateDefRplnSortDoesExist(String key,
			WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "RPLNSORT", key, context).getSize() == 0)
			return false;
		else
			return true;
	}


	//Helper functions
	public boolean allNinesValidation(String attributeValue) 
	{
		
		if (isEmpty(attributeValue))
		{
			return false;
		}
		String allNines = returnAllNines(attributeValue);
		if (attributeValue.trim().equals(allNines))
			return true;
		else
			return false;
		
	}
	
	public String returnAllNines(String attributeValue)
	{
		StringBuffer allNines = new StringBuffer();
		for (int i = 0; i < attributeValue.length(); i++)
		{
			allNines.append("9");
		}
		return (allNines.toString());
		
	}
	
	public boolean validateFirstGreaterThanOrEqualSecond(String first, String second){
		try{
			if(Integer.parseInt(first)>=Integer.parseInt(second)){
				return true;
			}
		}catch(NumberFormatException e){
			return false;	
		}
		return false;
		
	}
	
	public boolean validateLpnLength(String attributeValue, String lpnLength){ 
		
		int nLpnLength = Integer.parseInt(lpnLength);
		
		if (attributeValue.length() != nLpnLength)
			return false;
		else
			return true;
	}
	
	
	public boolean validateUccVendor(String barcodeFormat, String uccvendornumber) 
	{
		if (isEmpty(barcodeFormat))
		{
			return true;
		}
		if (((String) barcodeFormat).equals("1"))
		{
			//ensure UCCVENDORNUMBER.length >7
			if (uccvendornumber==null || uccvendornumber.trim().length() < 7)
				return false;
			else
				return true;
			
		}else
			return true;
		
	}
	
	
	public static boolean isCurrency(String value)
	{
		//Calls parseCurrency
		
		double parseCurrency = parseCurrency(value);
		if(Double.isNaN(parseCurrency))
		{
			return false;
		}
		return true;
		
		
	}
	
	
	public static double parseCurrency(String value)
	{
		double widgetValue;
		try
		{
			NumberFormat nf = NumberFormat.getInstance();
			widgetValue = nf.parse(value.toString()).doubleValue();
			
		} catch (ParseException e)
		{
			NumberFormat nfc = NumberFormat.getCurrencyInstance();
			try
			{
				widgetValue = nfc.parse(value.toString()).doubleValue();
			} catch (ParseException e1)
			{
				return Double.NaN;
			}
		} catch (NumberFormatException e)
		{
			return Double.NaN;
		}
		
		if (!(value.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*")))
		{
			return Double.NaN;
		}
		return widgetValue;
	}
	
	
	
	
	public boolean validateScac_codeIsAlpha(OwnerScreenVO screen){
		
		return alphaNumericValidation(screen.getScac_code());
	}
	
	public boolean validateHiminimumreceiptchargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getHiminimumreceiptcharge()))
			return true;
		
		return isCurrency(screen.getHiminimumreceiptcharge());
	}
	
	public boolean validateHominimumshipmentchargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getHominimumshipmentcharge()))
			return true;
		
		return isCurrency(screen.getHominimumshipmentcharge());
	}
	
	public boolean validateIsminimumreceiptchargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getIsminimumreceiptcharge()))
			return true;
		
		return isCurrency(screen.getIsminimumreceiptcharge());
	}
	
	
	public boolean validateHiminimuminvoicechargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getHIMinimumInvoiceCharge()))
			return true;
		
		return isCurrency(screen.getHIMinimumInvoiceCharge());
	}
	
	public boolean validateIsminimuminvoicechargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getISMinimumInvoiceCharge()))
			return true;
		
		return isCurrency(screen.getISMinimumInvoiceCharge());
	}
	
	public boolean validateRsminimuminvoicechargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getRSMinimumInvoiceCharge()))
			return true;
		
		return isCurrency(screen.getRSMinimumInvoiceCharge());
	}
	
	
	public boolean validateAlminimumchargeIsCurrency(OwnerScreenVO screen){
		
		if (isEmpty(screen.getAllMinimumInvoiceCharge()))
			return true;
		
		return isCurrency(screen.getAllMinimumInvoiceCharge());
	}
	
	//Required
	public boolean validateCreditlimitNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getCreditlimit());			
	}
	
	
	
	public boolean validateMaximumordersNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getMaximumorders());			
	}
	public boolean validateMinimumpercentNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getMinimumpercent());			
	}
	
	public boolean validateOrderdatestartdaysNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getOrderdatestartdays());			
	}
	
	public boolean validateOrderdateenddaysNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getOrderdateenddays());			
	}
	
	public boolean validateHiminimumreceiptchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getHiminimumreceiptcharge());			
	}
	
	public boolean validateHominimumshipmentchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getHominimumshipmentcharge());			
	}
	
	public boolean validateIsminimumreceiptchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getIsminimumreceiptcharge());			
	}
	public boolean validateHiminimuminvoicechargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getHIMinimumInvoiceCharge());			
	}
	
	public boolean validateIsminimuminvoicechargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getISMinimumInvoiceCharge());			
	}
	
	public boolean validateRsminimuminvoicechargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getRSMinimumInvoiceCharge());			
	}
	public boolean validateAlminimumchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getAllMinimumInvoiceCharge());			
	}
	public boolean validateDefaultqclocNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDefaultqcloc());			
	}
	public boolean validateDefaultqclocoutNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDefaultqclocout());			
	}
	public boolean validateDefaultreturnslocNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDefaultreturnsloc());			
	}
	
	public boolean validateDefaultpackinglocNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDefaultpackinglocation());			
	}
	public boolean validateLpnstartnumberNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getLpnstartnumber());			
	}
	public boolean validateUccvendornumberNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getUccVendor());			
	}
	public boolean validateNextlpnnumberNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getNextlpnnumber());			
	}
	public boolean validateLpnrollbacknumberNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getLpnrollbacknumber());			
	}
	public boolean validateLpnlengthNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getLpnlength());			
	}
	public boolean validateStorerkeyNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getStorerkey());			
	}
	public boolean validateScac_codeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getScac_code());			
	}

	public boolean validateKship_carrierNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getKship_carrier());			
	}


	public boolean validateCreateoppxdtasksNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getCreateoppxdtasks());			
	}

	public boolean validateIssueoppxdtasksNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getIssueoppxdtasks());			
	}	
	
	//Numeric
	public boolean validateCreditlimitGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getCreditlimit());
	}
	public boolean validateMaximumordersGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getMaximumorders());
	}
	public boolean validateMinimumpercentGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getMinimumpercent());
	}
	public boolean validateOrderdateenddaysGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getOrderdateenddays());
	}
	public boolean validateOrderdatestartdaysGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getOrderdatestartdays());
	}
	
	public boolean validateHiminimumreceiptchargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getHiminimumreceiptcharge());
	}
	
	public boolean validateHominimumshipmentchargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getHominimumshipmentcharge());
	}
	
	public boolean validateIsminimumreceiptchargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getIsminimumreceiptcharge());
	}
	public boolean validateHiminimuminvoicechargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getHIMinimumInvoiceCharge());
	}
	public boolean validateIsminimuminvoicechargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getISMinimumInvoiceCharge());
	}
	public boolean validateRsminimuminvoicechargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getRSMinimumInvoiceCharge());
	}
	public boolean validateAlminimumchargeGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getAllMinimumInvoiceCharge());
	}
	public boolean validateLpnstartnumberGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getLpnstartnumber());
	}
	public boolean validateUccvendornumberGreaterThanZero(OwnerScreenVO screen){
		return greaterThanZeroValidation(screen.getUccVendor());
	}
	
	
	
	public boolean validateKship_carrierZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getKship_carrier());
	}
	
	public boolean validateCreateoppxdtasksZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getCreateoppxdtasks());
	}
	

	public boolean validateIssueoppxdtasksZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getIssueoppxdtasks());
	}
	
	public boolean validateCreditlimitIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getCreditlimit());
	}
	
	public boolean validateMaximumordersIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getMaximumorders());
	}
	public boolean validateMinimumpercentIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getMinimumpercent());
	}
	public boolean validateOrderdateenddaysIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getOrderdateenddays());
	}
	public boolean validateOrderdatestartdaysIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getOrderdatestartdays());
	}
	
	public boolean validateHiminimumreceiptchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getHiminimumreceiptcharge());
	}
	public boolean validateHominimumshipmentchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getHominimumshipmentcharge());
	}
	
	public boolean validateIsminimumreceiptchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getIsminimumreceiptcharge());
	}
	public boolean validateHiminimuminvoicechargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getHIMinimumInvoiceCharge());
	}
	public boolean validateIsminimuminvoicechargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getISMinimumInvoiceCharge());
	}
	public boolean validateRsminimuminvoicechargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getRSMinimumInvoiceCharge());
	}
	public boolean validateAlminimumchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getAllMinimumInvoiceCharge());
	}
	public boolean validateLpnstartnumberIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getLpnstartnumber());
	}
	public boolean validateUccvendornumberIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getUccVendor());
	}
	public boolean validateNextlpnnumberIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getNextlpnnumber());
	}
	public boolean validateLpnrollbacknumberIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getLpnrollbacknumber());
	}
	
	public boolean validateKship_carrierIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getKship_carrier());
	}
	
	public boolean validateCreateoppxdtasksIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getCreateoppxdtasks());
	}
	
	
	public boolean validateIssueoppxdtasksIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getIssueoppxdtasks());
	}	
	
	//Length
	public boolean validateAddress1LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress1()))
			return true;
		return screen.getAddress1().length() < 46;			
	}
	
	public boolean validateAddress2LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress2()))
			return true;
		return screen.getAddress2().length() < 46;			
	}
	
	public boolean validateAddress3LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress3()))
			return true;
		return screen.getAddress3().length() < 46;			
	}
	
	public boolean validateAddress4LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress4()))
			return true;
		return screen.getAddress4().length() < 46;			
	}
	
	
	public boolean validateAllowautocloseforasnLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowAutoCloseASN()))
			return true;
		return screen.getAllowAutoCloseASN().length() < 2;			
		
	}
	
	
	public boolean validateAllowautocloseforpoLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowautocloseforpo()))
			return true;
		return screen.getAllowautocloseforpo().length() < 2;			
		
	}
	
	
	public boolean validateAllowautocloseforpsLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowautocloseforps()))
			return true;
		return screen.getAllowautocloseforps().length() < 2;			
		
	}
	public boolean validateAllowcommingledlpnLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowcommingledlpn()))
			return true;
		return screen.getAllowcommingledlpn().length() < 2;			
		
	}
	public boolean validateAllowduplicatelicenseplateLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowduplicatelicenseplates()))
			return true;
		return screen.getAllowduplicatelicenseplates().length() < 2;			
		
	}
	public boolean validateAllowovershipmentLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowovershipment()))
			return true;
		return screen.getAllowovershipment().length() < 2;			
		
	}
	public boolean validateAllowsinglescanreceivingLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowsinglescanreceiving()))
			return true;
		return screen.getAllowsinglescanreceiving().length() < 2;			
		
	}
	public boolean validateAllowsystemgeneratedlpnLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAllowsystemgeneratedlpn()))
			return true;
		return screen.getAllowsystemgeneratedlpn().length() < 2;			
		
	}
	public boolean validateApplicationidLengthIs2OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getApplicationid()))
			return true;
		return screen.getApplicationid().length() < 3;			
		
	}
	public boolean validateApportionruleLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getApportionrule()))
			return true;
		return screen.getApportionrule().length() < 11;			
		
	}
	public boolean validateAutocloseasnLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAutocloseasn()))
			return true;
		return screen.getAutocloseasn().length() < 2;			
		
	}
	public boolean validateAutoclosepoLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAutoclosepo()))
			return true;
		return screen.getAutoclosepo().length() < 2;			
		
	}
	public boolean validateAutoprintlabellpnLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAutoprintlabellpn()))
			return true;
		return screen.getAutoprintlabellpn().length() < 2;			
		
	}
	public boolean validateAutoprintlabelputawayLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAutoprintlabelputaway()))
			return true;
		return screen.getAutoprintlabelputaway().length() < 2;			
		
	}
	public boolean validateB_address1LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_address1()))
			return true;
		return screen.getB_address1().length() < 46;			
		
	}
	public boolean validateB_address2LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_address2()))
			return true;
		return screen.getB_address2().length() < 46;			
		
	}
	public boolean validateB_address3LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_address3()))
			return true;
		return screen.getB_address3().length() < 46;			
		
	}
	public boolean validateB_address4LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_address4()))
			return true;
		return screen.getB_address4().length() < 46;			
		
	}
	public boolean validateB_cityLengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_city()))
			return true;
		return screen.getB_city().length() < 46;			
		
	}
	public boolean validateB_companyLengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_company()))
			return true;
		return screen.getB_company().length() < 46;			
		
	}
	public boolean validateB_contact1LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_contact1()))
			return true;
		return screen.getB_contact1().length() < 31;			
		
	}
	public boolean validateB_contact2LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_contact2()))
			return true;
		return screen.getB_contact2().length() < 31;			
		
	}
	public boolean validateB_countryLengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_country()))
			return true;
		return screen.getB_country().length() < 31;			
		
	}
	public boolean validateB_email1LengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_email1()))
			return true;
		return screen.getB_email1().length() < 61;			
		
	}
	public boolean validateB_email2LengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_email2()))
			return true;
		return screen.getB_email2().length() < 61;			
		
	}
	public boolean validateB_fax1LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_fax1()))
			return true;
		return screen.getB_fax1().length() < 19;			
		
	}
	public boolean validateB_fax2LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_fax2()))
			return true;
		return screen.getB_fax2().length() < 19;			
		
	}
	public boolean validateB_isocntrycodeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_isocntrycode()))
			return true;
		return screen.getB_isocntrycode().length() < 11;			
		
	}
	public boolean validateB_phone1LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_phone1()))
			return true;
		return screen.getB_phone1().length() < 19;			
		
	}
	public boolean validateB_phone2LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_phone2()))
			return true;
		return screen.getB_phone2().length() < 19;			
		
	}
	// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
	public boolean validateB_stateLengthIs25OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_state()))
			return true;
		return screen.getB_state().length() < 26;			
		
	}
	public boolean validateB_zipLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getB_zip()))
			return true;
		return screen.getB_zip().length() < 19;			
		
	}
	public boolean validateBarcodeconfigkeyLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getBarcodeconfigkey()))
			return true;
		return screen.getBarcodeconfigkey().length() < 19;			
		
	}
	public boolean validateCalculateputawaylocationLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCalculateputawaylocation()))
			return true;
		return screen.getCalculateputawaylocation().length() < 11;			
		
	}
	public boolean validateCartongroupLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCartongroup()))
			return true;
		return screen.getCartongroup().length() < 11;			
		
	}
	public boolean validateCaselabeltypeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCaselabeltype()))
			return true;
		return screen.getCaselabeltype().length() < 11;			
		
	}
	public boolean validateCcadjbyrfLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCcadjbyrf()))
			return true;
		return screen.getCcadjbyrf().length() < 11;			
		
	}
	public boolean validateCcdiscrepancyruleLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCcdiscrepancyrule()))
			return true;
		return screen.getCcdiscrepancyrule().length() < 11;			
		
		
	}
	public boolean validateCcskuxlocLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCcskuxloc()))
			return true;
		return screen.getCcskuxloc().length() < 2;			
		
	}
	public boolean validateCityLengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCity()))
			return true;
		return screen.getCity().length() < 46;			
		
	}
	public boolean validateCompanyLengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCompany()))
			return true;
		return screen.getCompany().length() < 46;			
		
	}
	public boolean validateContact1LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getContact1()))
			return true;
		return screen.getContact1().length() < 31;			
		
	}
	public boolean validateContact2LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getContact2()))
			return true;
		return screen.getContact2().length() < 31;			
		
	}
	public boolean validateCountryLengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCountry()))
			return true;
		return screen.getCountry().length() < 31;			
		
	}
	public boolean validateCreatepataskonrfreceiptLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCreatepataskonrfreceipt()))
			return true;
		return screen.getCreatepataskonrfreceipt().length() < 11;			
		
	}
	public boolean validateCreditlimitLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCreditlimit()))
			return true;
		return screen.getCreditlimit().length() < 19;			
		
	}
	public boolean validateCwoflagLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCwoflag()))
			return true;
		return screen.getCwoflag().length() < 2;			
		
	}
	public boolean validateDefaultpackinglocationLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultpackinglocation()))
			return true;
		return screen.getDefaultpackinglocation().length() < 11;			
		
	}
	public boolean validateDefaultputawaystrategyLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultputawaystrategy()))
			return true;
		return screen.getDefaultputawaystrategy().length() < 11;			
		
	}
	public boolean validateDefaultqclocLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultqcloc()))
			return true;
		return screen.getDefaultqcloc().length() < 11;			
		
	}
	public boolean validateDefaultqclocoutLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultqclocout()))
			return true;
		return screen.getDefaultqclocout().length() < 11;			
		
	}
	public boolean validateDefaultreturnslocLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultreturnsloc()))
			return true;
		return screen.getDefaultreturnsloc().length() < 11;			
		
	}
	public boolean validateDefaultrotationLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultrotation()))
			return true;
		return screen.getDefaultrotation().length() < 2;			
		
	}
	public boolean validateDefaultskurotationLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultskurotation()))
			return true;
		return screen.getDefaultskurotation().length() < 11;			
		
	}
	public boolean validateDefaultstrategyLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDefaultstrategy()))
			return true;
		return screen.getDefaultstrategy().length() < 11;			
		
	}
	public boolean validateDescriptionLengthIs50OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDescription()))
			return true;
		return screen.getDescription().length() < 51;			
		
	}
	public boolean validateDupcaseidLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDupcaseid()))
			return true;
		return screen.getDupcaseid().length() < 2;			
		
	}
	public boolean validateEmail1LengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getEmail1()))
			return true;
		return screen.getEmail1().length() < 61;			
		
	}
	public boolean validateEmail2LengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getEmail2()))
			return true;
		return screen.getEmail2().length() < 61;			
		
	}
	public boolean validateEnableoppxdockLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getEnableoppxdock()))
			return true;
		return screen.getEnableoppxdock().length() < 2;			
		
	}
	public boolean validateEnablepackingdefaultLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getEnablepackingdefault()))
			return true;
		return screen.getEnablepackingdefault().length() < 2;			
		
	}
	public boolean validateFax1LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getFax1()))
			return true;
		return screen.getFax1().length() < 2;			
		
	}
	public boolean validateFax2LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getFax2()))
			return true;
		return screen.getFax2().length() < 19;			
		
	}
	public boolean validateGeneratepacklistLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getGeneratepacklist()))
			return true;
		return screen.getGeneratepacklist().length() < 2;			
		
	}
	public boolean validateInspectatpackLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getInspectatpack()))
			return true;
		return screen.getInspectatpack().length() < 2;			
		
	}
	
	public boolean validateIsocntrycodeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getIsoCountryCode()))
			return true;
		return screen.getIsoCountryCode().length() < 11;			
		
	}
	public boolean validateLpnbarcodeformatLengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getLpnbarcodeformat()))
			return true;
		return screen.getLpnbarcodeformat().length() < 61;			
		
	}
	public boolean validateLpnbarcodesymbologyLengthIs60OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getLpnbarcodesymbology()))
			return true;
		return screen.getLpnbarcodesymbology().length() < 61;			
		
	}
	public boolean validateLpnrollbacknumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getLpnrollbacknumber()))
			return true;
		return screen.getLpnrollbacknumber().length() < 19;			
		
	}
	public boolean validateLpnstartnumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getLpnstartnumber()))
			return true;
		return screen.getLpnstartnumber().length() < 19;			
		
	}
	public boolean validateMultizoneplpaLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getMultizoneplpa()))
			return true;
		return screen.getMultizoneplpa().length() < 2;			
		
	}
	public boolean validateNextlpnnumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getNextlpnnumber()))
			return true;
		return screen.getNextlpnnumber().length() < 19;			
		
	}
	public boolean validateOpporderstrategykeyLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOpporderstrategykey()))
			return true;
		return screen.getOpporderstrategykey().length() < 11;			
		
	}
	public boolean validateOrderbreakdefaultLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrderbreakdefault()))
			return true;
		return screen.getOrderbreakdefault().length() < 2;			
		
	}
	
	public boolean validateOrdertyperestrict01LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict01()))
			return true;
		return screen.getOrdertyperestrict01().length() < 11;			
		
	}
	
	public boolean validateOrdertyperestrict02LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict02()))
			return true;
		return screen.getOrdertyperestrict02().length() < 11;			
		
	}
	public boolean validateOrdertyperestrict03LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict03()))
			return true;
		return screen.getOrdertyperestrict03().length() < 11;			
		
	}
	public boolean validateOrdertyperestrict04LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict04()))
			return true;
		return screen.getOrdertyperestrict04().length() < 11;			
		
	}
	public boolean validateOrdertyperestrict05LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict05()))
			return true;
		return screen.getOrdertyperestrict05().length() < 11;			
		
	}
	public boolean validateOrdertyperestrict06LengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOrdertyperestrict06()))
			return true;
		return screen.getOrdertyperestrict06().length() < 11;			
		
	}
	public boolean validatePackingvalidationtemplateLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPackingvalidationtemplate()))
			return true;
		return screen.getPackingvalidationtemplate().length() < 11;			
		
	}
	public boolean validatePhone1LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPhone1()))
			return true;
		return screen.getPhone1().length() < 19;			
		
	}
	public boolean validatePhone2LengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPhone2()))
			return true;
		return screen.getPhone2().length() < 19;			
		
	}
	public boolean validatePickcodeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPickcode()))
			return true;
		return screen.getPickcode().length() < 11;			
		
	}
	public boolean validatePiskuxlocLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPiskuxloc()))
			return true;
		return screen.getPiskuxloc().length() < 2;			
		
	}
	public boolean validateReceiptvalidationtemplateLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getPhone1()))
			return true;
		return screen.getPhone1().length() < 19;			
		
	}
	public boolean validateRollreceiptLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getRollreceipt()))
			return true;
		return screen.getRollreceipt().length() < 2;			
		
	}
	public boolean validateScac_codeLengthIs4OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getScac_code()))
			return true;
		return screen.getScac_code().length() < 5;			
		
	}
	public boolean validateSkusetuprequiredLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSkusetuprequired()))
			return true;
		return screen.getSkusetuprequired().length() < 2;			
		
	}
	// Jan 20, 2009. BugAware 8880, state expanded from 2 to 25
	public boolean validateStateLengthIs25OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getState()))
			return true;
		return screen.getState().length() < 26;			
		
	}
	public boolean validateStatusLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getStatus()))
			return true;
		return screen.getStatus().length() < 19;			
		
	}
	public boolean validateStorerkeyLengthIs15OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getStorerkey()))
			return true;
		return screen.getStorerkey().length() < 16;			
		
	}
	public boolean validateSusr1LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr1()))
			return true;
		return screen.getSusr1().length() < 31;			
		
	}
	public boolean validateSusr2LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr2()))
			return true;
		return screen.getSusr2().length() < 31;			
		
	}
	public boolean validateSusr3LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr3()))
			return true;
		return screen.getSusr3().length() < 31;			
		
	}
	public boolean validateSusr4LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr4()))
			return true;
		return screen.getSusr4().length() < 31;			
		
	}
	public boolean validateSusr5LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr5()))
			return true;
		return screen.getSusr5().length() < 31;			
		
	}
	public boolean validateSusr6LengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSusr6()))
			return true;
		return screen.getSusr6().length() < 31;			
		
	}
	public boolean validateTitle1LengthIs50OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTitle1()))
			return true;
		return screen.getTitle1().length() < 51;			
		
	}
	public boolean validateTitle2LengthIs50OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTitle2()))
			return true;
		return screen.getTitle2().length() < 51;			
		
	}
	public boolean validateTrackinventorybyLengthIs1OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTrackinventoryby()))
			return true;
		return screen.getTrackinventoryby().length() < 2;			
		
	}
	public boolean validateTypeLengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getType()))
			return true;
		return screen.getType().length() < 31;			
		
	}
	public boolean validateUccvendornumberLengthIs9OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getUccvendornumber()))
			return true;
		return screen.getUccvendornumber().length() < 10;			
		
	}
	public boolean validateVatLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getVat()))
			return true;
		return screen.getVat().length() < 19;			
		
	}
	public boolean validateWhseidLengthIs30OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getWhseid()))
			return true;
		return screen.getWhseid().length() < 31;			
		
	}
	public boolean validateZipLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getZip()))
			return true;
		return screen.getZip().length() < 19;			
		
	}
	
	public boolean validateOppxdpickfromIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOppxdpickfrom()))
			return true;
		return screen.getOppxdpickfrom().length() < 21;			
		
	}
	
	
	public boolean validateObxdstageIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getObxdstage()))
			return true;
		return screen.getObxdstage().length() < 21;			
		
	}
	
	public boolean validateSpsuomweightIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSpsuomweight()))
			return true;
		return screen.getSpsuomweight().length() < 11;			
		
	}
	
	public boolean validateSpsuomdimensionIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSpsuomdimension()))
			return true;
		return screen.getSpsuomdimension().length() < 11;			
		
	}	
	//Attribute validations
	public boolean validateDefaultqclocInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultqcloc()))
			return true;
		return validateDefaultqclocDoesExist(screen.getDefaultqcloc(), getContext());
	}
	protected static boolean validateDefaultqclocDoesExist(String qcloc, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getLocationByKey(qcloc, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateDefaultqclocoutInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultqclocout()))
			return true;
		return validateDefaultqclocoutDoesExist(screen.getDefaultqclocout(), getContext());
	}
	
	protected static boolean validateDefaultqclocoutDoesExist(String qclocout, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getLocationByKey(qclocout, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateDefaultreturnslocInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultreturnsloc()))
			return true;
		return validateDefaultreturnslocDoesExist(screen.getDefaultreturnsloc(), getContext());
	}
	
	protected static boolean validateDefaultreturnslocDoesExist(String returnsloc, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getLocationByKey(returnsloc, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDefaultpackinglocInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultpackinglocation()))
			return true;
		return validateDefaultpackinglocDoesExist(screen.getDefaultpackinglocation(), getContext());
	}
	
	protected static boolean validateDefaultpackinglocDoesExist(String packinglocation, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getPicktoStagedLocationByKey(packinglocation, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateIsocntrycodeInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getIsocntrycode()))
			return true;
		return validateIsocntrycodeDoesExist(screen.getIsocntrycode(), getContext());
	}
	protected static boolean validateIsocntrycodeDoesExist(String isocntrycode, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ISOCOUNTRY",isocntrycode, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateCartongroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCartongroup()))
			return true;
		return validateCartongroupDoesExist(screen.getCartongroup(), getContext());
	}
	
	protected static boolean validateCartongroupDoesExist(String cartongroup, WMSValidationContext context) throws WMSDataLayerException{
		if (CartonizationQueryRunner.getCartonizationByCartonizationGroup(cartongroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdersequencestrategyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrderSequenceStrategy()))
			return true;
		return validateOrdersequencestrategyDoesExist(screen.getOrderSequenceStrategy(), getContext());
	}
	
	protected static boolean validateOrdersequencestrategyDoesExist(String ordersequencestrategy, WMSValidationContext context) throws WMSDataLayerException{
		if (OppOrderStrategyQueryRunner.getOppOrderStrategyKeyByKey(ordersequencestrategy, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAutomaticapportionmentruleInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAutomaticApportionmentRule()))
			return true;
		return validateAutomaticapportionmentruleDoesExist(screen.getAutomaticApportionmentRule(), getContext());
	}
	
	protected static boolean validateAutomaticapportionmentruleDoesExist(String rule, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("APPORTRULE",rule, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict01InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict01()))
			return true;
		return validateOrdertyperestrict01DoesExist(screen.getOrdertyperestrict01(), getContext());
	}
	
	protected static boolean validateOrdertyperestrict01DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict02InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict02()))
			return true;
		return validateOrdertyperestrict02DoesExist(screen.getOrdertyperestrict02(), getContext());
	}
	
	protected static boolean validateOrdertyperestrict02DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict03InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict03()))
			return true;
		return validateOrdertyperestrict03DoesExist(screen.getOrdertyperestrict03(), getContext());
	}
	protected static boolean validateOrdertyperestrict03DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict04InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict04()))
			return true;
		return validateOrdertyperestrict04DoesExist(screen.getOrdertyperestrict04(), getContext());
	}
	protected static boolean validateOrdertyperestrict04DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict05InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict05()))
			return true;
		return validateOrdertyperestrict05DoesExist(screen.getOrdertyperestrict05(), getContext());
	}
	protected static boolean validateOrdertyperestrict05DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateOrdertyperestrict06InAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrdertyperestrict06()))
			return true;
		return validateOrdertyperestrict06DoesExist(screen.getOrdertyperestrict06(), getContext());
	}
	protected static boolean validateOrdertyperestrict06DoesExist(String ordertype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ORDERTYPE", ordertype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateInvoicenumberstrategyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getInvoiceNumberStrategy()))
			return true;
		return validateInvoicenumberstrategyDoesExist(screen.getInvoiceNumberStrategy(), getContext());
	}
	protected static boolean validateInvoicenumberstrategyDoesExist(String invoicestrategy, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("INVSTRAT", invoicestrategy, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	
	public boolean validateBillinggroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getBillingGroup()))
			return true;
		return validateBillinggroupDoesExist(screen.getBillingGroup(), getContext());
	}
	protected static boolean validateBillinggroupDoesExist(String billinggroup, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("BILLGROUP", billinggroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateHiminimumreceipttaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHiminimumreceipttaxgroup()))
			return true;
		return validateHiminimumreceipttaxgroupDoesExist(screen.getHiminimumreceipttaxgroup(), getContext());
	}
	protected static boolean validateHiminimumreceipttaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateHominimumshipmenttaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHominimumshipmenttaxgroup()))
			return true;
		return validateHominimumshipmenttaxgroupDoesExist(screen.getHominimumshipmenttaxgroup(), getContext());
	}
	protected static boolean validateHominimumshipmenttaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateIsminimumreceipttaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getIsminimumreceipttaxgroup()))
			return true;
		return validateIsminimumreceipttaxgroupDoesExist(screen.getIsminimumreceipttaxgroup(), getContext());
	}
	protected static boolean validateIsminimumreceipttaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateHiminimuminvoicetaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHIMinimumInvoiceTaxgroup()))
			return true;
		return validateHiminimuminvoicetaxgroupDoesExist(screen.getHIMinimumInvoiceTaxgroup(), getContext());
	}
	protected static boolean validateHiminimuminvoicetaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateIsminimuminvoicetaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getISMinimumInvoiceTaxgroup()))
			return true;
		return validateIsminimuminvoicetaxgroupDoesExist(screen.getISMinimumInvoiceTaxgroup(), getContext());
	}
	protected static boolean validateIsminimuminvoicetaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateRsminimuminvoicetaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getRSMinimumInvoiceTaxgroup()))
			return true;
		return validateRsminimuminvoicetaxgroupDoesExist(screen.getRSMinimumInvoiceTaxgroup(), getContext());
	}
	protected static boolean validateRsminimuminvoicetaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAlminimumtaxgroupInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllMinimumInvoiceTaxgroup()))
			return true;
		return validateAlmiminimumtaxgroupDoesExist(screen.getAllMinimumInvoiceTaxgroup(), getContext());
	}
	protected static boolean validateAlmiminimumtaxgroupDoesExist(String taxgroup, WMSValidationContext context) throws WMSDataLayerException{
		if (TaxgroupQueryRunner.getTaxgroupByTaxgroupKey(taxgroup, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateHiminimumreceiptgldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHiminimumreceiptgldist()))
			return true;
		return validateHiminimumreceiptgldistDoesExist(screen.getHiminimumreceiptgldist(), getContext());
	}
	protected static boolean validateHiminimumreceiptgldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateHominimumshipmentgldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHominimumshipmentgldist()))
			return true;
		return validateHominimumshipmentgldistDoesExist(screen.getHominimumshipmentgldist(), getContext());
	}
	protected static boolean validateHominimumshipmentgldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateIsminimumreceiptgldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getIsminimumreceiptgldist()))
			return true;
		return validateIsminimumreceiptgldistDoesExist(screen.getIsminimumreceiptgldist(), getContext());
	}
	protected static boolean validateIsminimumreceiptgldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateHiminimuminvoicegldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getHIMinimumInvoiceGLDistribution()))
			return true;
		return validateHiminimuminvoicegldistDoesExist(screen.getHIMinimumInvoiceGLDistribution(), getContext());
	}
	protected static boolean validateHiminimuminvoicegldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateIsminimuminvoicegldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getISMinimumInvoiceGLDistribution()))
			return true;
		return validateIsminimuminvoicegldistDoesExist(screen.getISMinimumInvoiceGLDistribution(), getContext());
	}
	protected static boolean validateIsminimuminvoicegldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateRsminimuminvoicegldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getRSMinimumInvoiceGLDistribution()))
			return true;
		return validateRsminimuminvoicegldistDoesExist(screen.getRSMinimumInvoiceGLDistribution(), getContext());
	}
	protected static boolean validateRsminimuminvoicegldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAlminimumgldistInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllMinimumInvoiceGLDistribution()))
			return true;
		return validateAlminimumgldistDoesExist(screen.getAllMinimumInvoiceGLDistribution(), getContext());
	}
	protected static boolean validateAlminimumgldistDoesExist(String gldist, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(gldist, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDefaultitemrotationInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultItemRotation()))
			return true;
		return validateDefaultitemrotationDoesExist(screen.getDefaultItemRotation(), getContext());
	}
	protected static boolean validateDefaultitemrotationDoesExist(String defaultitemrotation, WMSValidationContext context) throws WMSDataLayerException{
		if (GldistributionQueryRunner.getGldistributionByGldistributionKey(defaultitemrotation, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateDefaultskurotationInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultItemRotation()))
			return true;
		return validateDefaultskurotationDoesExist(screen.getDefaultItemRotation(), getContext());
	}
	protected static boolean validateDefaultskurotationDoesExist(String defaultskurotation, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("SKUROTATE", defaultskurotation, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDefaultrotationInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultRotation()))
			return true;
		return validateDefaultrotationDoesExist(screen.getDefaultRotation(), getContext());
	}
	protected static boolean validateDefaultrotationDoesExist(String defaultrotation, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("ROTATION", defaultrotation, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDefaultstrategyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultstrategy()))
			return true;
		return validateDefaultstrategyDoesExist(screen.getDefaultstrategy(), getContext());
	}
	protected static boolean validateDefaultstrategyDoesExist(String defaultstrategy, WMSValidationContext context) throws WMSDataLayerException{
		if (StrategyQueryRunner.getStrategyByStrategyKey(defaultstrategy, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDefaultputawaystrategyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDefaultputawaystrategy()))
			return true;
		return validateDefaultputawaystrategyDoesExist(screen.getDefaultputawaystrategy(), getContext());
	}
	protected static boolean validateDefaultputawaystrategyDoesExist(String defaultputawaystrategy, WMSValidationContext context) throws WMSDataLayerException{
		if (PutawayStrategyQueryRunner.getPutawayStrategyByPutawayStrategyKey(defaultputawaystrategy, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateCreateputawaytaskonrfreceiptInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCreatepataskonrfreceipt()))
			return true;
		return validateCreateputawaytaskonrfreceiptDoesExist(screen.getCreatepataskonrfreceipt(), getContext());
	}
	protected static boolean validateCreateputawaytaskonrfreceiptDoesExist(String createpataskonrfreceipt, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("TMTSKRFRC", createpataskonrfreceipt, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateCalculateputawaylocationInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCalculateputawaylocation()))
			return true;
		return validateCalculateputawaylocationDoesExist(screen.getCalculateputawaylocation(), getContext());
	}
	protected static boolean validateCalculateputawaylocationDoesExist(String calculateputawaylocation, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("TMCALCPALO", calculateputawaylocation, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateOrderbreakdefaultInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getOrderbreakdefault()))
			return true;
		return validateOrderbreakdefaultDoesExist(screen.getOrderbreakdefault(), getContext());
	}
	protected static boolean validateOrderbreakdefaultDoesExist(String orderbreakdefault, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("YESNO", orderbreakdefault, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validatePackingvalidationtemplateInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getPackingvalidationtemplate()))
			return true;
		return validatePackingvalidationtemplateDoesExist(screen.getPackingvalidationtemplate(), getContext());
	}
	protected static boolean validatePackingvalidationtemplateDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (PackingValidationTemplateQueryRunner.getPackingValidationTemplateByKey( key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateLpnbarcodesymbologyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLpnbarcodesymbology()))
			return true;
		return validateLpnbarcodesymbologyDoesExist(screen.getLpnbarcodesymbology(), getContext());
	}
	protected static boolean validateLpnbarcodesymbologyDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "BARCSYMBOL", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateLpnbarcodeformatInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLpnbarcodeformat()))
			return true;
		return validateLpnbarcodeformatDoesExist(screen.getLpnbarcodeformat(), getContext());
	}
	protected static boolean validateLpnbarcodeformatDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "BARCDFORMT", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateLpnlengthInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLpnlength()))
			return true;
		return validateLpnlengthDoesExist(screen.getLpnlength(), getContext());
	}
	protected static boolean validateLpnlengthDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "LPNLENGTH", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateCaselabeltypeInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCaselabeltype()))
			return true;
		return validateCaselabeltypeDoesExist(screen.getCaselabeltype(), getContext());
	}
	protected static boolean validateCaselabeltypeDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "CASELABEL", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateSscc1stdigitInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getSscc1stdigit()))
			return true;
		return validateSscc1stdigitDoesExist(screen.getSscc1stdigit(), getContext());
	}
	protected static boolean validateSscc1stdigitDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "SSCCDIGIT", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateCcdiscrepancyruleInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCcdiscrepancyrule()))
			return true;
		return validateCcdiscrepancyruleDoesExist(screen.getCcdiscrepancyrule(), getContext());
	}
	protected static boolean validateCcdiscrepancyruleDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CCAdjRulesQueryRunner.getCCAdjRulesByCCAdjRulesKey( key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateCcadjbyrfInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCcadjbyrf()))
			return true;
		return validateCcadjbyrfDoesExist(screen.getCcadjbyrf(), getContext());
	}
	protected static boolean validateCcadjbyrfDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "RFCCADJ", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateAllowcommingledlpnInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllowcommingledlpn()))
			return true;
		return validateAllowcommingledlpnDoesExist(screen.getAllowcommingledlpn(), getContext());
	}
	protected static boolean validateAllowcommingledlpnDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateAllowautocloseforasnInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllowautocloseforasn()))
			return true;
		return validateAllowautocloseforasnDoesExist(screen.getAllowautocloseforasn(), getContext());
	}
	protected static boolean validateAllowautocloseforasnDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAllowautocloseforpoInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllowautocloseforpo()))
			return true;
		return validateAllowautocloseforpoDoesExist(screen.getAllowautocloseforpo(), getContext());
	}
	protected static boolean validateAllowautocloseforpoDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAllowsystemgeneratedlpnInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllowsystemgeneratedlpn()))
			return true;
		return validateAllowsystemgeneratedlpnDoesExist(screen.getAllowsystemgeneratedlpn(), getContext());
	}
	protected static boolean validateAllowsystemgeneratedlpnDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateAllowsinglescanreceivingInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAllowsinglescanreceiving()))
			return true;
		return validateAllowsinglescanreceivingDoesExist(screen.getAllowsinglescanreceiving(), getContext());
	}
	protected static boolean validateAllowsinglescanreceivingDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "YESNO", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateReceiptvalidationtemplateInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getReceiptvalidationtemplate()))
			return true;
		return validateReceiptvalidationtemplateDoesExist(screen.getReceiptvalidationtemplate(), getContext());
	}
	protected static boolean validateReceiptvalidationtemplateDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (ReceiptValidationQueryRunner.getReceiptValidationByReceiptValidationKey( key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateBarcodeconfigkeyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getBarcodeconfigkey()))
			return true;
		return validateBarcodeconfigkeyDoesExist(screen.getBarcodeconfigkey(), getContext());
	}
	protected static boolean validateBarcodeconfigkeyDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (BarcodeConfigQueryRunner.getBarcodeConfigByKey	( key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	
	public boolean validateTrackinventorybyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getTrackinventoryby()))
			return true;
		return validateTrackinventorybyDoesExist(screen.getTrackinventoryby(), getContext());
	}
	protected static boolean validateTrackinventorybyDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "TRACKINV", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	public boolean validateDupcaseidInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDupcaseid()))
			return true;
		return validateDupcaseidDoesExist(screen.getDupcaseid(), getContext());
	}
	protected static boolean validateDupcaseidDoesExist(String key, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "DUPCASE", key, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	//SRG: 9.2 Upgrade -- Changed getStorerByKey() to getStorerByKeyAndType()
	protected static boolean validateStorerkeyDoesExist(String storerkey, String type, WMSValidationContext context) throws WMSDataLayerException{
		if (StorerQueryRunner.getStorerByKeyAndType(storerkey, type, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	//SRG: 9.2 Upgrade -- Begin
	protected static boolean validateParentDoesExist(String parent, WMSValidationContext context) throws WMSDataLayerException{
		if (StorerQueryRunner.getStorerByKey(parent, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	public boolean validateContainerexchangeflagNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getContainerExchangeFlag());			
	}
	
	public boolean validateContainerexchangeflagIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getContainerExchangeFlag());		
	}
	
	public boolean validateContainerexchangeflagZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getContainerExchangeFlag());
	}
	
	public boolean validateAddressoverwriteindicatorNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getAddressoverwriteindicator());			
	}
	
	public boolean validateAddressoverwriteindicatorZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getAddressoverwriteindicator());
	}
	
	public boolean validateDeffttaskcontrolNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDeffttaskcontrol());			
	}
	
	public boolean validateDeffttaskcontrolZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getDeffttaskcontrol());
	}
	
	public boolean validateDefftlabelprintNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getDefftlabelprint());			
	}
	
	public boolean validateDefftlabelprintZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getDefftlabelprint());
	}
	
	public boolean validateCartonizeftdfltNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getCartonizeftdflt());			
	}
	
	public boolean validateCartonizeftdfltZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getCartonizeftdflt());
	}
	
	public boolean validateSkusetuprequiredNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getSkusetuprequired());			
	}
	
	public boolean validateSkusetuprequiredZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getSkusetuprequired());
	}
	
	public boolean validateReqreasonshortshipNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getReqreasonshortship());			
	}
	
	public boolean validateReqreasonshortshipIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getReqreasonshortship());		
	}
	
	public boolean validateReqreasonshortshipZeroOrOne(OwnerScreenVO screen){
		return isZeroOrOneValidation(screen.getReqreasonshortship());
	}
	
	public boolean validateExplodelpnlengthNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getExplodelpnlength());			
	}
	
	public boolean validateExplodelpnlengthIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getExplodelpnlength());		
	}
	
	public boolean validateExplodelpnlengthGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getExplodelpnlength());
	}
	
	public boolean validateQfsurchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getQfsurcharge());			
	}
	
	public boolean validateQfsurchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getQfsurcharge());		
	}
	
	public boolean validateQfsurchargeGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getQfsurcharge());
	}
	
	public boolean validateBfsurchargeNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getBfsurcharge());			
	}	
	
	public boolean validateBfsurchargeIsANumber(OwnerScreenVO screen){
		return isNumber(screen.getBfsurcharge());		
	}
	
	public boolean validateBfsurchargeGreaterThanOrEqualZero(OwnerScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getBfsurcharge());
	}
	
	public boolean validateSpsreturnlabelNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getSpsreturnlabel());		
	}
	
	public boolean validateParentNotEmpty(OwnerScreenVO screen){
		return !isEmpty(screen.getParent());			
	}
	
	public boolean validateSpsreturnlabelIsYesOrNo(OwnerScreenVO screen){
		return isYesOrNo(screen.getSpsreturnlabel());			
	}
	
	public boolean validateAmstrategykeyInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAmstrategykey()))
			return true;
		return validateAmstrategyDoesExist(screen.getAmstrategykey(), getContext());
	}	
	
	public boolean validateCurrcodeInAttrDom(OwnerScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getCurrcode()))
			return true;
		return validateCodelkupDoesExist("CURRCODE", screen.getCurrcode(), getContext());
	}		
	
	public boolean validateAddress5LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress5()))
			return true;
		return screen.getAddress5().length() < 46;		
	}
	
	public boolean validateAddress6LengthIs45OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAddress6()))
			return true;
		return screen.getAddress6().length() < 46;		
	}
	
	public boolean validateArcorpLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getArcorp()))
			return true;
		return screen.getArcorp().length() < 41;		
	}
	
	public boolean validateArdeptLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getArdept()))
			return true;
		return screen.getArdept().length() < 41;		
	}
	
	public boolean validateAracctLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAracct()))
			return true;
		return screen.getAracct().length() < 41;		
	}
	
	public boolean validateMeasurecodeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getMeasurecode()))
			return true;
		return screen.getMeasurecode().length() < 11;		
	}
	
	public boolean validateWgtuomLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getWgtuom()))
			return true;
		return screen.getWgtuom().length() < 21;		
	}
	
	public boolean validateDimenuomLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDimenuom()))
			return true;
		return screen.getDimenuom().length() < 21;		
	}
	
	public boolean validateCubeuomLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCubeuom()))
			return true;
		return screen.getCubeuom().length() < 21;		
	}
	
	public boolean validateCurrcodeLengthIs3OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getCurrcode()))
			return true;
		return screen.getCurrcode().length() < 31;		
	}
	
	public boolean validateTaxexemptLengthIs2OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTaxexempt()))
			return true;
		return screen.getTaxexempt().length() < 21;		
	}
	
	public boolean validateTaxexemptcodeLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTaxexemptcode()))
			return true;
		return screen.getTaxexemptcode().length() < 41;		
	}
	
	public boolean validateRecurcodeLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getRecurcode()))
			return true;
		return screen.getRecurcode().length() < 11;		
	}
	
	public boolean validateDunsidLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getDunsid()))
			return true;
		return screen.getDunsid().length() < 41;		
	}
	
	public boolean validateTaxidLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getTaxid()))
			return true;
		return screen.getTaxid().length() < 41;		
	}
	
	public boolean validateInvoicetermsLengthIs10OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getInvoiceterms()))
			return true;
		return screen.getInvoiceterms().length() < 11;		
	}
	
	public boolean validateInvoicelevelLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getInvoicelevel()))
			return true;
		return screen.getInvoicelevel().length() < 21;		
	}
	
	public boolean validateNonneglevelLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getNonneglevel()))
			return true;
		return screen.getNonneglevel().length() < 21;		
	}
	
	public boolean validateSpsaccountnumLengthIs40OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSpsaccountnum()))
			return true;
		return screen.getSpsaccountnum().length() < 41;		
	}
	
	public boolean validateSpscostcenterLengthIs20OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getSpscostcenter()))
			return true;
		return screen.getSpscostcenter().length() < 21;		
	}
	
	public boolean validateOwnerprefixLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getOwnerprefix()))
			return true;
		return screen.getOwnerprefix().length() < 19;		
	}
	
	public boolean validateExplodenextlpnnumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getExplodenextlpnnumber()))
			return true;
		return screen.getExplodenextlpnnumber().length() < 19;		
	}
	
	public boolean validateExplodelpnrollbacknumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getExplodelpnrollbacknumber()))
			return true;
		return screen.getExplodelpnrollbacknumber().length() < 19;		
	}
	
	public boolean validateExplodelpnstartnumberLengthIs18OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getExplodelpnstartnumber()))
			return true;
		return screen.getExplodelpnstartnumber().length() < 19;		
	}
	
	public boolean validateAccountingentityLengthIs64OrLess(OwnerScreenVO screen){
		if(isEmpty(screen.getAccountingentity()))
			return true;
		return screen.getAccountingentity().length() < 65;		
	}
	//SRG: 9.2 Upgrade -- End
	
	
	/*
	 * Inner classes
	 */
	
	
	public class OwnerLabelValidator extends BaseScreenValidator{
		
		public OwnerLabelValidator(WMSValidationContext context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		public static final int RULE_STORER_LABEL_MUST_BE_UINQUE = 1000;
		
		
		public ArrayList validate(AssignLocationsVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			ArrayList errors = new ArrayList();
			if(doCheckFieldLength){
				
				
			}
			
			if(doCheckAttributeDomain){	
				
			}
			
			
			return errors;
		}
		
	}
	
	public class UDFLabelValidator extends BaseScreenValidator{
		
		public UDFLabelValidator(WMSValidationContext context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		public static final int RULE_UDF_LABEL_MUST_BE_UINQUE = 1100;
		
		
		public ArrayList validate(AltVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			ArrayList errors = new ArrayList();
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			
			if(doCheckFieldLength){
				
			}
			
			if(doCheckAttributeDomain){	
				
			}
			
			return errors;
		}
		
	}
	
	
	
	public static String getErrorMessage(int errorCode, Locale locale, OwnerScreenVO ownerScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){
		//Unique
		case RULE_OWNER_MUST_BE_UNIQUE:
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_STORERKEY, locale);
			param[1] = ownerScreen.getStorerkey();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_OWNER_SCREEN_ERROR_DUPLICATE_OWNER, locale, param);
			
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
			
			
		case RULE_CREATEOPPXDTASKS_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CREATEOPPXDTASKS, locale),
					ownerScreen.getCreateoppxdtasks());
			
			
		case RULE_ISSUEOPPXDTASKS_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISSUEOPPXDTASKS, locale),
					ownerScreen.getIssueoppxdtasks());		
			
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
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_ALMINIMUMCHARGE, locale));
			
		case RULE_CREATEOPPXDTASKS_MUST_BE_A_NUMBER:	
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_CREATEOPPXDTASKS, locale));
			
		case RULE_ISSUEOPPXDTASKS_MUST_BE_A_NUMBER:	
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_ISSUEOPPXDTASKS, locale));		
			
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
			
		case RULE_KSHIP_CARRIER_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_CARRIER_FIELD_KSHIP_CARRIER, locale));
			
		case RULE_KSHIP_CARRIER_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_CARRIER_FIELD_KSHIP_CARRIER, locale), 
					ownerScreen.getKship_carrier() ); 
								
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
			
		case RULE_OPPXDPICKFROM_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_OPPXDPICKFROM, locale), "20");
			
		case RULE_OBXDSTAGE_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_OBXDSTAGE, locale), "20");
		
		case RULE_SPSUOMWEIGHT_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_SPSUOMWEIGHT, locale), "10");
		
		case RULE_SPSUOMDIMENSION_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_SCREEN_OWNER_FIELD_SPSUOMDIMENSION, locale), "10");
		
			
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
		
		case RULE_ATTR_DOM_DEFAULT_REPLENISHMENT_LOCATION_SORT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFRPLNSORT, locale), ownerScreen.getDefrplnsort());
			
		case RULE_ATTR_DOM_DEFAULT_DYNAMIC_PICKING_LOCATION_SORT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFDAPICKSORT, locale), ownerScreen.getDefdapicksort());
		
		case RULE_ATTR_DOM_REQUIRE_REASON_FOR_SHORT_SHIPMENT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_REQREASONSHORTSHIP, locale), ownerScreen.getReqreasonshortship());

		case RULE_ATTR_DOM_EXPLODE_LPN_LENGTH:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODELPNLENGTH, locale), ownerScreen.getExplodelpnlength());
			
		case RULE_ATTR_DOM_CONTAINER_EXCHANGE_FLAG:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONTAINEREXCHANGEFLAG, locale), ownerScreen.getContainerExchangeFlag());					


		case RULE_ATTR_DOM_FLOW_THRU_TASK_CONTROL:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_FLOWTHRUTASKCONTROL, locale), ownerScreen.getDeffttaskcontrol());					

		case RULE_ATTR_DOM_FLOW_THRU_LABEL_PRINT_CONTROL:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_FLOWTHRULABELPRINTCONTROL, locale), ownerScreen.getDefftlabelprint());					

		case RULE_ATTR_DOM_CARTONIZE_FLOW_THRU_ORDERS_DEFAULT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CARTONIZEFLOWTHRUORDERSDEFAULT, locale), ownerScreen.getCartonizeftdflt());					

		//SRG: 9.2 Upgrade -- Start			
		case RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONTAINEREXCHANGEFLAG, locale));
	  
		case RULE_CONTAINER_EXCHANGE_FLAG_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CONTAINEREXCHANGEFLAG, locale),
					ownerScreen.getContainerExchangeFlag());
			
		case RULE_ADDRESS_OVERWRITE_INDICATOR_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESSOVERWRITEINDICATOR, locale),
					ownerScreen.getAddressoverwriteindicator());
			
		case RULE_DEFFT_TASK_CONTROL_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFFTTASKCONTROL, locale),
					ownerScreen.getDeffttaskcontrol());
			
		case RULE_DEFFT_LABEL_PRINT_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DEFFTLABELPRINT, locale),
					ownerScreen.getDefftlabelprint());
			
		case RULE_CARTONIZE_FTDFLT_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CARTONIZEFTDFLT, locale),
					ownerScreen.getCartonizeftdflt());
			
		case RULE_SKU_SETUP_REQUIRED_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SKUSETUPREQUIRED, locale),
					ownerScreen.getSkusetuprequired());
			
		case RULE_REQ_REASON_SHORT_SHIP_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_REQREASONSHORTSHIP, locale));
	  
		case RULE_REQ_REASON_SHORT_SHIP_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_REQREASONSHORTSHIP, locale),
					ownerScreen.getReqreasonshortship());
			
		case RULE_EXPLODE_LPN_LENGTH_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODELPNLENGTH, locale));
	  
		case RULE_EXPLODE_LPN_LENGTH_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODELPNLENGTH, locale));
			
		case RULE_QFSURCHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_QFSURCHARGE, locale));
			
		case RULE_QFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_QFSURCHARGE, locale));
			
		case RULE_BFSURCHARGE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BFSURCHARGE, locale));
			
		case RULE_BFSURCHARGE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_BFSURCHARGE, locale));
			
		case RULE_SPS_RETURN_LABEL_MUST_BE_Y_OR_N:
			return getYesOrNoErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SPSRETURNLABEL, locale));
		
		case RULE_ATTR_DOM_AMSTRATEGYKEY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_AMSTRATEGYKEY, locale), ownerScreen.getAmstrategykey());
				
		case RULE_ATTR_DOM_CURRCODE:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CURRCODE, locale), ownerScreen.getCurrcode());		
		
		case RULE_LENGTH_ADDRESS5_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS5, locale), "45");
				
		case RULE_LENGTH_ADDRESS6_45:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ADDRESS6, locale), "45");
			
		case RULE_LENGTH_ARCORP_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ARCORP, locale), "40");
			
		case RULE_LENGTH_ARDEPT_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ARDEPT, locale), "40");
			
		case RULE_LENGTH_ARACCT_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_ARACCT, locale), "40");
			
		case RULE_LENGTH_MEASURECODE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_MEASURECODE, locale), "10");
			
		case RULE_LENGTH_WGTUOM_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_WGTUOM, locale), "20");
			
		case RULE_LENGTH_DIMENUOM_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DIMENUOM, locale), "20");
			
		case RULE_LENGTH_CUBEUOM_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CUBEUOM, locale), "20");
			
		case RULE_LENGTH_CURRCODE_3:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_CURRCODE, locale), "3");
			
		case RULE_LENGTH_TAXEXEMPT_2:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TAXEXEMPT, locale), "2");
			
		case RULE_LENGTH_TAXEXEMPTCODE_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TAXEXEMPTCODE, locale), "40");
			
		case RULE_LENGTH_RECURCODE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_RECURCODE, locale), "10");
			
		case RULE_LENGTH_DUNSID_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_DUNSID, locale), "40");
		
		case RULE_LENGTH_TAXID_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_TAXID, locale), "40");
			
		case RULE_LENGTH_INVOICETERMS_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_INVOICETERMS, locale), "10");
			
		case RULE_LENGTH_INVOICELEVEL_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_INVOICELEVEL, locale), "20");
			
		case RULE_LENGTH_NONNEGLEVEL_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_NONNEGLEVEL, locale), "20");
			
		case RULE_LENGTH_SPSACCOUNTNUM_40:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SPSACCOUNTNUM, locale), "40");
			
		case RULE_LENGTH_SPSCOSTCENTER_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_SPSCOSTCENTER, locale), "20");
			
		case RULE_LENGTH_OWNERPREFIX_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_OWNERPREFIX, locale), "18");
			
		case RULE_LENGTH_EXPLODENEXTLPNNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODENEXTLPNNUMBER, locale), "18");
			
		case RULE_LENGTH_EXPLODELPNROLLBACKNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODELPNROLLBACKNUMBER, locale), "18");
			
		case RULE_LENGTH_EXPLODELPNSTARTNUMBER_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_OWNER_FIELD_EXPLODELPNSTARTNUMBER, locale), "18");		
		//SRG: 9.2 Upgrade -- End
			
		}
		
		return errorMsg;
	}
}