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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.driver.DataLayerColumnList;
import com.infor.scm.wms.util.datalayer.query.AltSkuQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CCSetupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ItemQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PackQueryRunner;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO.AltVO;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO.AssignLocationsVO;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO.SubstituteVO;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class ItemScreenValidator extends BaseScreenValidator{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemScreenValidator.class);		
	
	public ItemScreenValidator(WMSValidationContext context) {
		super(context);		
	}

	public static final int RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO = 0;
	public static final int RULE_STORER_MUST_EXIST = 1;
	public static final int RULE_PACK_MUST_EXIST = 2;
	public static final int RULE_ITEM_MUST_BE_UNIQUE = 3;
	public static final int RULE_ITEM_REFERENCE_MUST_BE_A_NUMBER = 4;
	public static final int RULE_GROSS_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 5;
	public static final int RULE_NET_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 6;
	public static final int RULE_TARE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 7;
	public static final int RULE_INBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO = 8;
	public static final int RULE_OUTBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO = 9;	
	public static final int RULE_INBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 10;
	public static final int RULE_INBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO = 11;
	public static final int RULE_INBOUND_ALL_LABELS_NOT_EMPTY = 12;
	public static final int RULE_OUTBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 13;
	public static final int RULE_OUTBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO = 14;
	public static final int RULE_OUTBOUND_ALL_LABELS_NOT_EMPTY = 15;	
	public static final int RULE_RF_DEFAULT_RECEIVING_PACK_NOT_EMPTY = 16;
	public static final int RULE_HAZMATCODE_MUST_EXIST = 17;
	public static final int RULE_DATE_CODE_DAYS_GREATER_THAN_OR_EQUAL_ZERO = 18;
	public static final int RULE_MAX_PALLETS_PER_ZONE_GREATER_THAN_OR_EQUAL_ZERO = 19;	
	public static final int RULE_PUTAWAY_LOCATION_MUST_EXIST = 20;
	public static final int RULE_INBOUND_QC_LOCATION_MUST_EXIST = 21;
	public static final int RULE_OUTBOUND_QC_LOCATION_MUST_EXIST = 22;	
	public static final int RULE_RETURN_LOCATION_MUST_EXIST = 23;	
	public static final int RULE_SERIAL_NUMBER_START_GREATER_THAN_ZERO = 24;
	public static final int RULE_SERIAL_NUMBER_NEXT_GREATER_THAN_SERIAL_NUMBER_START = 25;
	public static final int RULE_SERIAL_NUMBER_END_GREATER_THAN_SERIAL_NUMBER_NEXT = 26;	
	public static final int RULE_SERIAL_NUMBER_START_LESS_THAN_SERIAL_NUMBER_MAX = 27;
	public static final int RULE_SERIAL_NUMBER_NEXT_LESS_THAN_SERIAL_NUMBER_MAX = 28;
	public static final int RULE_SERIAL_NUMBER_END_LESS_THAN_SERIAL_NUMBER_MAX = 29;	
	public static final int RULE_SERIAL_NUMBER_START_MUST_BE_A_NUMBER = 30;
	public static final int RULE_SERIAL_NUMBER_NEXT_MUST_BE_A_NUMBER = 31;
	public static final int RULE_SERIAL_NUMBER_END_MUST_BE_A_NUMBER = 32;	
	public static final int RULE_QUANTITY_TO_REORDER_MUST_BE_A_NUMBER = 33;
	public static final int RULE_QUANTITY_TO_REORDER_GREATER_THAN_OR_EQUAL_ZERO = 34;
	public static final int RULE_COST_TO_ORDER_MUST_BE_A_NUMBER = 35;
	public static final int RULE_COST_TO_ORDER_GREATER_THAN_OR_EQUAL_ZERO = 36;
	public static final int RULE_REORDER_POINT_MUST_BE_A_NUMBER = 37;
	public static final int RULE_REORDER_POINT_GREATER_THAN_OR_EQUAL_ZERO = 38;
	public static final int RULE_RETAIL_PRICE_PER_UNIT_MUST_BE_A_NUMBER = 39;
	public static final int RULE_RETAIL_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO = 40;
	public static final int RULE_PURCHASE_PRICE_PER_UNIT_MUST_BE_A_NUMBER = 41;
	public static final int RULE_PURCHASE_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO = 42;	
	public static final int RULE_CARRYING_PER_UNIT_MUST_BE_A_NUMBER = 43;
	public static final int RULE_CARRYING_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO = 44;
	public static final int RULE_CUBE_MUST_BE_A_NUMBER = 45;
	public static final int RULE_GROSS_WEIGHT_MUST_BE_A_NUMBER = 46;
	public static final int RULE_NET_WEIGHT_MUST_BE_A_NUMBER = 47;
	public static final int RULE_TARE_WEIGHT_MUST_BE_A_NUMBER = 48;
	public static final int RULE_INBOUND_SHELF_LIFE_MUST_BE_A_NUMBER = 49;
	public static final int RULE_OUTBOUND_SHELF_LIFE_MUST_BE_A_NUMBER = 50;
	public static final int RULE_INBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER = 51;
	public static final int RULE_INBOUND_TOLERANCE_MUST_BE_A_NUMBER = 52;
	public static final int RULE_OUTBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER = 53;
	public static final int RULE_OUTBOUND_TOLERANCE_MUST_BE_A_NUMBER = 54;
	public static final int RULE_DATE_CODE_DAYS_MUST_BE_A_NUMBER = 55;
	public static final int RULE_MAX_PALLETS_PER_ZONE_MUST_BE_A_NUMBER = 56;
	
	public static final int RULE_SNUM_DELIM_COUNT_GREATER_THAN_OR_EQUAL_ZERO = 201;
	public static final int RULE_SNUM_POSITION_GREATER_THAN_OR_EQUAL_ZERO = 202;
	public static final int RULE_SNUM_DELIM_COUNT_MUST_BE_A_NUMBER = 203;
	public static final int RULE_SNUM_POSITION_MUST_BE_A_NUMBER = 204;
	public static final int RULE_SNUM_DELIM_AND_SNUM_POSITION_CONFLICT =205;
	public static final int RULE_SNUM_MASK_VALIDATION = 206;
	public static final int RULE_SNUM_AUTOINCREMENT_MUST_BE_ZERO_OR_ONE = 207;
	public static final int RULE_SNUM_QUANTITY_MUST_BE_A_NUMBER = 208;
	public static final int RULE_SNUM_QUANTITY_GREATER_THAN_ZERO_WHEN_AUTOINCREMENT_ON = 209;
	public static final int RULE_SNUM_INCR_LENGTH_MUST_BE_A_NUMBER = 210;
	public static final int RULE_SNUM_INCR_POS_MUST_BE_A_NUMBER = 211;
	public static final int RULE_SNUMLONG_FIXED_MUST_BE_A_NUMBER = 212;
	public static final int RULE_SNUM_LENGTH_MUST_BE_A_NUMBER = 213;
	public static final int RULE_SNUMLONG_FIXED_MUST_BE_GREATER_THAN_SNUM_LENGTH=214;
	
	public static final int RULE_TOEXPIREDAYS_GREATER_THAN_OR_EQUAL_ZERO = 215;
	public static final int RULE_TODELIVERBYDAYS_GREATER_THAN_OR_EQUAL_ZERO = 216;
	public static final int RULE_TOBESTBYDAYS_GREATER_THAN_OR_EQUAL_ZERO = 217;
	public static final int RULE_TOEXPIREDAYS_MUST_BE_A_NUMBER = 218;
	public static final int RULE_TODELIVERBYDAYS_MUST_BE_A_NUMBER = 219;
	public static final int RULE_TOBESTBYDAYS_MUST_BE_A_NUMBER = 220;
	public static final int RULE_NONSTOCKEDINDICATOR_MUST_BE_A_NUMBER = 221;
	public static final int RULE_NONSTOCKEDINDICATOR_MUST_BE_ZERO_OR_ONE = 222;
	public static final int RULE_ICD1UNIQUE_MUST_BE_A_NUMBER = 223;
	public static final int RULE_ICD1UNIQUE_MUST_BE_ZERO_OR_ONE = 224;
	public static final int RULE_OCD1UNIQUE_MUST_BE_A_NUMBER = 225;
	public static final int RULE_OCD1UNIQUE_MUST_BE_ZERO_OR_ONE = 226;
	
	public static final int RULE_CWFLAG_MUST_BE_A_NUMBER = 231;
	public static final int RULE_CWFLAG_MUST_BE_ZERO_OR_ONE = 232;
	
	//Required Field Rules
	public static final int RULE_STORER_NOT_EMPTY = 57;
	public static final int RULE_ITEM_NOT_EMPTY = 58;
	public static final int RULE_PACK_NOT_EMPTY = 59;
	public static final int RULE_CUBE_NOT_EMPTY = 60;
	public static final int RULE_GROSS_WEIGHT_NOT_EMPTY = 61;
	public static final int RULE_NET_WEIGHT_NOT_EMPTY = 62;
	public static final int RULE_TARE_WEIGHT_NOT_EMPTY = 63;
	public static final int RULE_CLASS_NOT_EMPTY = 64;
	public static final int RULE_LOTTABLE01_NOT_EMPTY = 65;
	public static final int RULE_LOTTABLE02_NOT_EMPTY = 66;
	public static final int RULE_LOTTABLE03_NOT_EMPTY = 67;
	public static final int RULE_LOTTABLE04_NOT_EMPTY = 68;
	public static final int RULE_LOTTABLE05_NOT_EMPTY = 69;
	public static final int RULE_LOTTABLE06_NOT_EMPTY = 70;
	public static final int RULE_LOTTABLE07_NOT_EMPTY = 71;
	public static final int RULE_LOTTABLE08_NOT_EMPTY = 72;
	public static final int RULE_LOTTABLE09_NOT_EMPTY = 73;
	public static final int RULE_LOTTABLE10_NOT_EMPTY = 74;

	public static final int RULE_LOTTABLE11_NOT_EMPTY = 197;
	public static final int RULE_LOTTABLE12_NOT_EMPTY = 198;
	
	//Field Length Rules
	public static final int RULE_LENGTH_STORER_15 = 75;
	public static final int RULE_LENGTH_ITEM_50 = 76;
	public static final int RULE_LENGTH_DESCRIPTION_60 = 77;
	public static final int RULE_LENGTH_PACK_50 = 78;
	public static final int RULE_LENGTH_CARTON_GROUP_10 = 79;
	public static final int RULE_LENGTH_TARIFF_10 = 80;
	public static final int RULE_LENGTH_ITEM_REFERENCE_8 = 81;
	public static final int RULE_LENGTH_SHELF_LIFE_CODE_TYPE_1 = 82;
	public static final int RULE_LENGTH_CATCH_WEIGHT_10 = 83;
	public static final int RULE_LENGTH_SHELF_LIFE_INDICATOR_10 = 84;
	public static final int RULE_LENGTH_LOTTABLE_VALIDATION_10 = 85;
	public static final int RULE_LENGTH_RF_DEFAULT_RECEIVING_PACK_50 = 86;
	public static final int RULE_LENGTH_ON_RECEIPT_COPY_PACK_10 = 87;
	public static final int RULE_LENGTH_RF_DEFAULT_RECEIVING_UOM_10 = 88;
	public static final int RULE_LENGTH_ITEM_GROUP_1_10 = 89;
	public static final int RULE_LENGTH_ITEM_GROUP_2_30 = 90;
	public static final int RULE_LENGTH_HAZMAT_CODE_10 = 91;
	public static final int RULE_LENGTH_SHIPPABLE_CONTAINER_10 = 92;
	public static final int RULE_LENGTH_VERTICAL_STORAGE_1 = 93;
	public static final int RULE_LENGTH_TRANSPORTATION_MODE_30 = 94;
	public static final int RULE_LENGTH_FREIGHT_CLASS_10 = 95;
	public static final int RULE_LENGTH_CLASS_10 = 96;
	public static final int RULE_LENGTH_LOTTABLE01_20 = 97;
	public static final int RULE_LENGTH_LOTTABLE02_20 = 98;
	public static final int RULE_LENGTH_LOTTABLE03_20 = 99;
	public static final int RULE_LENGTH_LOTTABLE04_20 = 100;
	public static final int RULE_LENGTH_LOTTABLE05_20 = 101;
	public static final int RULE_LENGTH_LOTTABLE06_20 = 102;
	public static final int RULE_LENGTH_LOTTABLE07_20 = 103;
	public static final int RULE_LENGTH_LOTTABLE08_20 = 104;
	public static final int RULE_LENGTH_LOTTABLE09_20 = 105;
	public static final int RULE_LENGTH_LOTTABLE10_20 = 106;
	
	public static final int RULE_LENGTH_LOTTABLE11_20 = 193;
	public static final int RULE_LENGTH_LOTTABLE12_20 = 194;
	
	public static final int RULE_LENGTH_BARCODE01_30 = 107;
	public static final int RULE_LENGTH_BARCODE02_30 = 108;
	public static final int RULE_LENGTH_BARCODE03_30 = 109;
	public static final int RULE_LENGTH_BARCODE04_30 = 110;
	public static final int RULE_LENGTH_BARCODE05_30 = 111;
	public static final int RULE_LENGTH_BARCODE06_30 = 112;
	public static final int RULE_LENGTH_BARCODE07_30 = 113;
	public static final int RULE_LENGTH_BARCODE08_30 = 114;
	public static final int RULE_LENGTH_BARCODE09_30 = 115;
	public static final int RULE_LENGTH_BARCODE10_30 = 116;
	public static final int RULE_LENGTH_INBOUND_CATCH_WEIGHT_1 = 117;
	public static final int RULE_LENGTH_INBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 = 118;
	public static final int RULE_LENGTH_INBOUND_CATCH_WEIGHT_BY_1 = 119;
	public static final int RULE_LENGTH_INBOUND_CATCH_DATA_1 = 120;
	public static final int RULE_LENGTH_INBOUND_SERIAL_NUMBER_5 = 121;
	public static final int RULE_LENGTH_INBOUND_OTHER_2_5 = 122;
	public static final int RULE_LENGTH_INBOUND_OTHER_3_5 = 123;
	public static final int RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_1 = 124;
	public static final int RULE_LENGTH_OUTBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 = 125;
	public static final int RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_BY_1 = 126;
	public static final int RULE_LENGTH_ALLOW_CUSTOMER_OVERRIDE_1 = 127;
	public static final int RULE_LENGTH_OUTBOUND_SERIAL_NUMBER_5 = 128;
	public static final int RULE_LENGTH_OUTBOUND_OTHER_2_5 = 129;
	public static final int RULE_LENGTH_OUTBOUND_OTHER_3_5 = 130;
	public static final int RULE_LENGTH_CATCH_WHEN_10 = 131;
	public static final int RULE_LENGTH_CATCH_QUANTITY_1_10 = 132;
	public static final int RULE_LENGTH_CATCH_QUANTITY_2_10 = 133;
	public static final int RULE_LENGTH_CATCH_QUANTITY_3_10 = 134;
	public static final int RULE_LENGTH_HOLD_CODE_ON_RF_RECEIPT_10 = 135;
	public static final int RULE_LENGTH_ITEM_TYPE_1 = 136;
	public static final int RULE_LENGTH_RECEIPT_VALIDATION_18 = 137;
	public static final int RULE_LENGTH_MANUAL_SETUP_REQUIRED_1 = 138;
	public static final int RULE_LENGTH_PUTAWAY_ZONE_10 = 139;
	public static final int RULE_LENGTH_PUTAWAY_LOCATION_10 = 140;
	public static final int RULE_LENGTH_INBOUND_QC_LOCATION_10 = 141;
	public static final int RULE_LENGTH_OUTBOUND_QC_LOCATION_10 = 142;
	public static final int RULE_LENGTH_RETURN_LOCATION_10 = 143;
	public static final int RULE_LENGTH_PUTAWAY_STRATEGY_10 = 144;
	public static final int RULE_LENGTH_STRATEGY_10 = 145;
	public static final int RULE_LENGTH_ROTATION_1 = 146;
	public static final int RULE_LENGTH_ROTATE_BY_10 = 147;
	public static final int RULE_LENGTH_OPPERTUNISTIC_1 = 148;
	public static final int RULE_LENGTH_CONVEYABLE_1 = 149;
	public static final int RULE_LENGTH_VERIFY_LOTTABLE_4_5_1 = 150;
	public static final int RULE_LENGTH_BULK_CARTON_GROUP_10 = 151;
	public static final int RULE_LENGTH_ALLOW_CONSOLIDATION_1 = 152;
	public static final int RULE_LENGTH_CYCLE_CLASS_5 = 153;
	public static final int RULE_LENGTH_CC_DISCREPANCY_HANDLING_RULE_10 = 154;
	public static final int RULE_LENGTH_UDF_1_18 = 155;
	public static final int RULE_LENGTH_UDF_2_18 = 156;
	public static final int RULE_LENGTH_UDF_3_18 = 157;
	public static final int RULE_LENGTH_UDF_4_18 = 158;
	public static final int RULE_LENGTH_UDF_5_18 = 159;
	public static final int RULE_LENGTH_UDF_6_30 = 160;
	public static final int RULE_LENGTH_UDF_7_30 = 161;
	public static final int RULE_LENGTH_UDF_8_30 = 162;
	public static final int RULE_LENGTH_UDF_9_30 = 163;
	public static final int RULE_LENGTH_UDF_10_30 = 164;
	public static final int RULE_LENGTH_PICKING_INSTRUCTIONS_2147483647 = 165;
	public static final int RULE_LENGTH_NOTES_2147483647 = 166;
	
	//jp begin.8879
	public static final int RULE_LENGTH_ICDLABEL4_5 = 227;
	public static final int RULE_LENGTH_ICDLABEL5_5 = 228;
	public static final int RULE_LENGTH_OCDLABEL4_5 = 229;
	public static final int RULE_LENGTH_OCDLABEL5_5 = 230;

	//jp end.8879
	
	//Attribute Domain Rules
	public static final int RULE_ATTR_DOM_TARIFF = 167;
	public static final int RULE_ATTR_DOM_CARTON_GROUP = 168;
	public static final int RULE_ATTR_DOM_SHELF_LIFE_CODE_TYPE = 169;
	public static final int RULE_ATTR_DOM_LOTTABLE_VALIDATION = 170;
	public static final int RULE_ATTR_DOM_ON_RECEIPT_COPY_PACK = 171;
	public static final int RULE_ATTR_DOM_RF_DEFAULT_RECEIVING_UOM = 172;
	public static final int RULE_ATTR_DOM_TRANSPORTATION_MODE = 173;
	public static final int RULE_ATTR_DOM_FREIGHT_CLASS = 174;
	public static final int RULE_ATTR_DOM_INBOUND_CATCH_WEIGHT_BY = 175;
	public static final int RULE_ATTR_DOM_OUTBOUND_CATCH_WEIGHT_BY = 176;
	public static final int RULE_ATTR_DOM_CATCH_WHEN = 177;
	public static final int RULE_ATTR_DOM_CATCH_QUANTITY_1 = 189;
	public static final int RULE_ATTR_DOM_CATCH_QUANTITY_2 = 190;
	public static final int RULE_ATTR_DOM_CATCH_QUANTITY_3 = 191;
	public static final int RULE_ATTR_DOM_HOLD_CODE_ON_RF_RECEIPT = 178;
	public static final int RULE_ATTR_DOM_ITEM_TYPE = 179;
	public static final int RULE_ATTR_DOM_RECEIPT_VALIDATION = 180;
	public static final int RULE_ATTR_DOM_PUTAWAY_ZONE = 181;
	public static final int RULE_ATTR_DOM_PUTAWAY_STRATEGY = 182;
	public static final int RULE_ATTR_DOM_STRATEGY = 183;
	public static final int RULE_ATTR_DOM_ROTATION = 184;
	public static final int RULE_ATTR_DOM_ROTATE_BY = 185;
	public static final int RULE_ATTR_DOM_BULK_CARTON_GROUP = 186;
	public static final int RULE_ATTR_DOM_CYCLE_CLASS = 187;
	public static final int RULE_ATTR_DOM_CC_DESCREPANCY_HANDLING_RULE = 188;
	public static final int RULE_ATTR_DOM_DAPICKSORT = 230;
	public static final int RULE_ATTR_DOM_RPLNSORT = 234;
	
	
	public static final int RULE_ATTR_DOM_VOICEGROUPINGID = 233;
	public static final int RULE_ATTR_DOM_CARTONIZEFT = 333;
	//SRG -- Catch Weight Capture -- Begin
	public static final int RULE_ENABLE_ADV_CWGT_MUST_BE_ZERO_OR_ONE = 300;
	public static final int RULE_LENGTH_ENABLE_ADV_CWGT_1 = 301;
	public static final int RULE_ENABLE_ADV_CWGT_MUST_BE_A_NUMBER = 302;
	public static final int RULE_CATCH_GROSS_WGT_MUST_BE_ZERO_OR_ONE = 303;
	public static final int RULE_LENGTH_CATCH_GROSS_WGT_1 = 304;
	public static final int RULE_CATCH_GROSS_WGT_MUST_BE_A_NUMBER = 305;
	public static final int RULE_CATCH_NET_WGT_MUST_BE_ZERO_OR_ONE = 306;
	public static final int RULE_LENGTH_CATCH_NET_WGT_1 = 307;
	public static final int RULE_CATCH_NET_WGT_MUST_BE_A_NUMBER = 308;
	public static final int RULE_CATCH_TARE_WGT_MUST_BE_ZERO_OR_ONE = 309;
	public static final int RULE_LENGTH_CATCH_TARE_WGT_1 = 310;
	public static final int RULE_CATCH_TARE_WGT_MUST_BE_A_NUMBER = 311;
	public static final int RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_ZERO_OR_ONE = 312;	
	public static final int RULE_LENGTH_ZERO_DEFAULT_WGT_FOR_PICK_1 = 313;
	public static final int RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_A_NUMBER = 314;
	public static final int RULE_ADV_CWT_TRACK_BY_GREATER_THAN_ZERO = 315;
	public static final int RULE_LENGTH_ADV_CWT_TRACK_BY_1 = 316;
	public static final int RULE_ADV_CWT_TRACK_BY_MUST_BE_A_NUMBER = 317;
	public static final int RULE_TARE_WGT1_GREATER_THAN_OR_EQUAL_ZERO = 318;
	public static final int RULE_TARE_WGT1_MUST_BE_A_NUMBER = 319;
	public static final int RULE_STD_NET_WGT1_GREATER_THAN_OR_EQUAL_ZERO = 320;
	public static final int RULE_STD_NET_WGT1_MUST_BE_A_NUMBER = 321;
	public static final int RULE_STD_GROSS_WGT1_GREATER_THAN_OR_EQUAL_ZERO = 322;
	public static final int RULE_STD_GROSS_WGT1_MUST_BE_A_NUMBER = 323;
	public static final int RULE_LENGTH_STD_UOM_10 = 324;	
	//SRG -- Catch Weight Capture -- End
	
	//SRG: 9.2 Upgrade -- Begin
	public static final int RULE_LENGTH_NMFCCLASS_40 = 335;
	public static final int RULE_LENGTH_MATEABILITYCODE_64 = 336;
	public static final int RULE_ATTR_DOM_FILL_QTY_UOM = 337;
	public static final int RULE_IBSUMCWFLG_MUST_BE_ZERO_OR_ONE = 338;
	public static final int RULE_OBSUMCWFLG_MUST_BE_ZERO_OR_ONE = 339;
	public static final int RULE_SHOWRFCWONTRANS_MUST_BE_ZERO_OR_ONE = 340;
	public static final int RULE_ATTR_DOM_STD_UOM = 341;
	public static final int RULE_LENGTH_RECURCODE_10 = 342;
	public static final int RULE_LENGTH_WGTUOM_20 = 343;
	public static final int RULE_LENGTH_DIMENUOM_20 = 344;
	public static final int RULE_LENGTH_CUBEUOM_20 = 345;
	public static final int RULE_LENGTH_STORAGETYPE_20 = 346;
	public static final int RULE_ATTR_DOM_AUTO_RELEASE_LPN_BY = 347;
	public static final int RULE_HOURS_TO_HOLD_LPN_MUST_BE_A_NUMBER = 348;
	public static final int RULE_HOURS_TO_HOLD_LPN_GREATER_THAN_OR_EQUAL_ZERO = 349;
	public static final int RULE_ATTR_DOM_LOT_HOLD_CODE = 350;
	public static final int RULE_ATTR_DOM_AUTO_RELEASE_LOT_BY = 351;
	public static final int RULE_HOURS_TO_HOLD_LOT_MUST_BE_A_NUMBER = 352;
	public static final int RULE_HOURS_TO_HOLD_LOT_GREATER_THAN_OR_EQUAL_ZERO = 353;
	public static final int RULE_ATTR_DOM_AMSTRATEGY_KEY = 354;
	public static final int RULE_ATTR_DOM_PUTAWAY_CLASS = 355;
	public static final int RULE_TEMP_FOR_ASN_MUST_BE_Y_OR_N = 356;
	public static final int RULE_CARTONIZEFT_MUST_BE_ZERO_OR_ONE = 357;	
	//SRG: 9.2 Upgrade -- End
	
	
	public ArrayList validate(ItemScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doAssumeDefaults = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ASSUME_DEFAULTS);
		
		
		//Validate Field Lengths
		if(doCheckFieldLength){			
			
			if(!validateStorerLengthIs15OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STORER_15));	
			}
			
			if(!validateItemLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ITEM_50 ));		
			}
			
			if(!validateDescriptionLengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DESCRIPTION_60 ));		
			}
			
			if(!validatePackLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACK_50 ));		
			}
			
			if(!validateCartonGroupLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTON_GROUP_10 ));	
			}
			
			if(!validateTariffLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TARIFF_10 ));		
			}
			
			if(!validateItemReferenceIs8OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ITEM_REFERENCE_8 ));		
			}
			
			if(!validateShelfLifeCodeTypeIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHELF_LIFE_CODE_TYPE_1 ));	
			}
			
			if(!validateCatchWeightLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_WEIGHT_10 ));	
			}
			
			if(!validateShelfLifeIndicatorLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHELF_LIFE_INDICATOR_10 ));		
			}
			
			if(!validateLottableValidationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE_VALIDATION_10 ));			
			}
			
			if(!validateRFDefaultReceivingPackLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RF_DEFAULT_RECEIVING_PACK_50 ));		
			}
			
			if(!validateOnReceiptCopyPackLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ON_RECEIPT_COPY_PACK_10 ));		
			}
			
			if(!validateRFDefaultReceivingUOMLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RF_DEFAULT_RECEIVING_UOM_10 ));			
			}
			
			if(!validateItemGroup1LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ITEM_GROUP_1_10 ));		
			}
			
			if(!validateItemGroup2LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ITEM_GROUP_2_30 ));			
			}
			
			if(!validateHazmatCodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_HAZMAT_CODE_10 ));			
			}
			
			if(!validateShippableContainerLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_SHIPPABLE_CONTAINER_10 ));		
			}
			
			if(!validateVerticalStorageLengthIs1rLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VERTICAL_STORAGE_1 ));			
			}
			
			if(!validateTransportationModeLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TRANSPORTATION_MODE_30 ));		
			}
			
			if(!validateFreightClassLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_FREIGHT_CLASS_10 ));		
			}
			
			if(!validateClassLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CLASS_10 ));			
			}
			
			if(!validateLottable01LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE01_20 ));			
			}
			
			if(!validateLottable02LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE02_20 ));		
			}
			
			if(!validateLottable03LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE03_20 ));		
			}
			
			if(!validateLottable04LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE04_20 ));			
			}
			
			if(!validateLottable05LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE05_20 ));		
			}
			
			if(!validateLottable06LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE06_20 ));			
			}
			
			if(!validateLottable07LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE07_20 ));			
			}
			
			if(!validateLottable08LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE08_20 ));		
			}
			 
			if(!validateLottable09LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE09_20 ));			
			}
			
			if(!validateLottable10LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE10_20 ));		
			}

			if(!validateLottable11LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE11_20 ));		
			}

			if(!validateLottable12LengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_LOTTABLE11_20 ));		
			}

			
			if(!validateBarCode01LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE01_30 ));	
			}
			
			if(!validateBarCode02LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE02_30 ));		
			}
			
			if(!validateBarCode03LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE03_30 ));		
			}
			
			if(!validateBarCode04LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE04_30 ));		
			}
			
			if(!validateBarCode05LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE05_30 ));		
			}
			
			if(!validateBarCode06LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE06_30 ));	
			}
			
			if(!validateBarCode07LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE07_30 ));		
			}
			
			if(!validateBarCode08LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE08_30 ));		
			}
			
			if(!validateBarCode09LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE09_30 ));		
			}
			
			if(!validateBarCode10LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BARCODE10_30 ));			
			}
			
			if(!validateInboundCatchWeightLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_CATCH_WEIGHT_1 ));		
			}
			
			if(!validateInboundNoEntryOfTotalWeightLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 ));		
			}
			
			if(!validateInboundCatchWeightByLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_CATCH_WEIGHT_BY_1 ));		
			}
			
			if(!validateInboundCatchDataLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_CATCH_DATA_1 ));		
			}
			
			if(!validateInboundSerialNumberLengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_SERIAL_NUMBER_5 ));			
			}
			
			if(!validateInboundOther2LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_OTHER_2_5 ));	
			}
			
			if(!validateInboundOther3LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_OTHER_3_5 ));	
			}
			
			if(!validateOutboundCatchWeightLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_1 ));		
			}
			
			if(!validateOutboundCatchWeightByLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_BY_1  ));		
			}
			
			if(!validateOutboundNoEntryOfTotalWeightLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 ));		
			}
			
			if(!validateAllowCustomerOverrideLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOW_CUSTOMER_OVERRIDE_1  ));		
			}
			
			if(!validateOutboundSerialNumberLengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_SERIAL_NUMBER_5  ));		
			}
			
			if(!validateOutboundOther2LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_OTHER_2_5 ));
			}
			
			if(!validateOutboundOther3LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_OTHER_3_5 ));	
			}
			
			if(!validateCatchWhenLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_WHEN_10 ));		
			}
			if(!validateCatchQuantity1LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_QUANTITY_1_10 ));			
			}
			if(!validateCatchQuantity2LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_QUANTITY_2_10 ));		
			}
			if(!validateCatchQuantity3LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_QUANTITY_3_10 ));	
			}
			if(!validateHoldCodeOnRFReceiptLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_HOLD_CODE_ON_RF_RECEIPT_10 ));		
			}
			if(!validateItemTypeLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ITEM_TYPE_1 ));	
			}
			
			if(!validateReceiptValidationLengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECEIPT_VALIDATION_18 ));			
			}
			
			if(!validateManualSetupRequiredLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_MANUAL_SETUP_REQUIRED_1 ));
			}
			
			if(!validatePutawayZoneLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PUTAWAY_ZONE_10 ));	
			}
			
			if(!validatePutawayLocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PUTAWAY_LOCATION_10 ));	
			}
			
			if(!validateInboundQCLocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INBOUND_QC_LOCATION_10));
			}
			
			if(!validateOutboundQCLocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTBOUND_QC_LOCATION_10 ));
			}
			
			if(!validateReturnLocationLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RETURN_LOCATION_10  ));	
			}
			
			if(!validatePutawayStrategyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PUTAWAY_STRATEGY_10 ));		
			}
			
			if(!validateStrategyLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STRATEGY_10 ));	
			}
			
			if(!validateRotationLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ROTATION_1 ));	
			}
			if(!validateRotateByLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ROTATE_BY_10 ));	
			}
			
			if(!validateOppertunisticLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OPPERTUNISTIC_1 ));	
			}
			
			if(!validateConveyableLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CONVEYABLE_1 ));	
			}
			if(!validateVerifyLottable4and5LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_VERIFY_LOTTABLE_4_5_1 ));		
			}
			
			if(!validateBulkCartonGroupLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_BULK_CARTON_GROUP_10 ));		
			}
			
			if(!validateAllowConsolidationLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ALLOW_CONSOLIDATION_1 ));	
			}
			
			if(!validateCycleClassLengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CYCLE_CLASS_5 ));			
			}
			
			if(!validateCcDiscrepancyHandlingRuleLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CC_DISCREPANCY_HANDLING_RULE_10 ));	
			}
			
			if(!validateUDF1LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_1_18 ));		
			}
			if(!validateUDF2LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_2_18 ));		
			}
			if(!validateUDF3LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_3_18 ));		
			}
			if(!validateUDF4LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_4_18 ));		
			}
			if(!validateUDF5LengthIs18OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_5_18 ));	
			}
			if(!validateUDF6LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_6_30 ));	
			}
			if(!validateUDF7LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_7_30 ));	
			}
			if(!validateUDF8LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_8_30 ));	
			}
			if(!validateUDF9LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_9_30 ));		
			}
			if(!validateUDF10LengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UDF_10_30 ));	
			}
			
			if(!validatePickingInstructionLengthIs2147483647OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PICKING_INSTRUCTIONS_2147483647 ));
			}
			if(!validateNotesLengthIs2147483647OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_NOTES_2147483647 ));	
			}
			
			if(!validateIcdlabel4LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ICDLABEL4_5 ));		
			}

			if(!validateIcdlabel5LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ICDLABEL5_5 ));		
			}
			
			if(!validateOcdlabel4LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OCDLABEL4_5 ));		
			}

			if(!validateOcdlabel5LengthIs5OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OCDLABEL5_5 ));		
			}
			
			//SRG -- Capture Catch Weight -- Begin			
			if(!validateStdUomLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STD_UOM_10 ));			
			}
//			if(!validateEnableAdvCwgtLengthIs1OrLess(screen)){
//				errors.add(new Integer(RULE_LENGTH_ENABLE_ADV_CWGT_1 ));			
//			}
			//SRG: Defect# 932 -- Start
			/*if(!validateCatchGrossWgtLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_GROSS_WGT_1 ));			
			}
			if(!validateCatchNetWgtLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_NET_WGT_1 ));			
			}
			if(!validateCatchTareWgtLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CATCH_TARE_WGT_1 ));			
			}
			if(!validateZeroDefaultWgtForPickLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ZERO_DEFAULT_WGT_FOR_PICK_1 ));			
			}*/
			//SRG: Defect# 932 -- End
//			if(!validateAdvCwtTrackByLengthIs1OrLess(screen)){
//				errors.add(new Integer(RULE_LENGTH_ADV_CWT_TRACK_BY_1 ));			
//			}			
			//SRG -- Capture Catch Weight -- End
			
			//SRG: 9.2 Upgrade -- Start
			if(!validateNmfcclassLengthIs40OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_NMFCCLASS_40 ));			
			}
			if(!validateMateabilitycodeLengthIs64OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_MATEABILITYCODE_64 ));			
			}
			if(!validateRecurcodeLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_RECURCODE_10 ));			
			}
			if(!validateWgtuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WGTUOM_20 ));			
			}
			if(!validateDimenuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DIMENUOM_20 ));			
			}
			if(!validateCubeuomLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CUBEUOM_20 ));			
			}
			if(!validateStoragetypeLengthIs20OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_STORAGETYPE_20 ));			
			}
			//SRG: 9.2 Upgrade -- End
			
		}
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
			if(!validateTariffInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TARIFF  ));		
			}
			
			if(!validateCartonGroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CARTON_GROUP  ));	
			}
			
			if(!validateShelfLifeCodeTypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_SHELF_LIFE_CODE_TYPE  ));	
			}
			
			if(!validateLottableValidationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOTTABLE_VALIDATION  ));			
			}
			
			if(!validateOnReceiptCopyPackInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ON_RECEIPT_COPY_PACK  ));			
			}
			
			if(!validateRFDefaultReceivingUOMInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RF_DEFAULT_RECEIVING_UOM  ));			
			}
			
			if(!validateTransportationModeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TRANSPORTATION_MODE  ));		
			}
			
			if(!validateFreightClassInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_FREIGHT_CLASS  ));			
			}
			
			if(!validateInboundCatchWeightByInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_INBOUND_CATCH_WEIGHT_BY  ));		
			}
			
			if(!validateOutboundCatchWeightByInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_OUTBOUND_CATCH_WEIGHT_BY  ));	
			}
			if(!validateOutboundCatchWhenInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CATCH_WHEN  ));			
			}
			if(!validateOutboundCatchQuantity1InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CATCH_QUANTITY_1  ));		
			}
			
			if(!validateOutboundCatchQuantity2InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CATCH_QUANTITY_2  ));		
			}
			
			if(!validateOutboundCatchQuantity3InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CATCH_QUANTITY_3  ));	
			}
			
			if(!validateHoldCodeOnRFReceiptInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_HOLD_CODE_ON_RF_RECEIPT  ));	
			}
			
			if(!validateItemTypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ITEM_TYPE  ));	
			}
			
			if(!validateReceiptValidationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RECEIPT_VALIDATION  ));	
			}
			
			if(!validatePutawayZoneInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PUTAWAY_ZONE  ));		
			}
			
			if(!validatePutawayStrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PUTAWAY_STRATEGY  ));			
			}
			
			if(!validateStrategyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_STRATEGY  ));	
			}
			
			if(!validateRotationInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ROTATION  ));		
			}
			
			if(!validateRotateByInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ROTATE_BY  ));		
			}
			
			if(!validateBulkCartonGroupInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_BULK_CARTON_GROUP  ));		
			}
			
			if(!validateCycleClassInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CYCLE_CLASS  ));				
			}
			
			if(!validateCCDescrepencyHandlingRuleAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CC_DESCREPANCY_HANDLING_RULE  ));		
			}
			
			if(!validateDapicksortInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_DAPICKSORT));
			}
			
			if(!validateRplnsortInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_RPLNSORT));
			}
				

			//jp.bugaware.9437.begin
			if(!validateVoicegroupingidInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_VOICEGROUPINGID  ));				
			}
			//jp.bugaware.9437.end
			
			if(!validateCartonizeftInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_CARTONIZEFT  ));				
			}
			
			//SRG: 9.2 Upgrade -- Start
			if(!validateFillqtyuomInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_FILL_QTY_UOM));	
			}
			
			if(!validateAutoreleaselpnbyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_AUTO_RELEASE_LPN_BY));	
			}
			
			if(!validatePutawayclassInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PUTAWAY_CLASS));	
			}			
			
			if(!validateAutoreleaselotbyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_AUTO_RELEASE_LOT_BY));	
			}
			
			if(!validateAmstrategykeyInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_AMSTRATEGY_KEY));		
			}
			
			if(!validateLotholdcodeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOT_HOLD_CODE));		
			}
			
			if(!validateStduomInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_STD_UOM  ));			
			}
			//SRG: 9.2 Upgrsde -- End
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateCubeNotEmpty(screen))
				errors.add(new Integer(RULE_CUBE_NOT_EMPTY));
			else{
				if(!validateCubeIsANumber(screen))
					errors.add(new Integer(RULE_CUBE_MUST_BE_A_NUMBER));
				else{
					if(!validateCubeGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO));
				}
			}
		}
		
		if(doCheckRequiredFields && !validateStorerNotEmpty(screen))
			errors.add(new Integer(RULE_STORER_NOT_EMPTY));
		else{
			if(!validateStorerDoesExist(screen))
				errors.add(new Integer(RULE_STORER_MUST_EXIST));
		}
		
		if(doCheckRequiredFields && !validatePackNotEmpty(screen))
			errors.add(new Integer(RULE_PACK_NOT_EMPTY));
		else{
			if(!validatePackDoesExist(screen))
				errors.add(new Integer(RULE_PACK_MUST_EXIST));
		}
		
		if(isInsert) {
			if(doCheckRequiredFields && !validateItemNotEmpty(screen))
				errors.add(new Integer(RULE_ITEM_NOT_EMPTY));
			else{
				if(!validateItemDoesNotExist(screen))
					errors.add(new Integer(RULE_ITEM_MUST_BE_UNIQUE));
			}
		}
		
		if(!validateItemReferenceIsANumber(screen))
			errors.add(new Integer(RULE_ITEM_REFERENCE_MUST_BE_A_NUMBER));
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateGrossWeightNotEmpty(screen))
				errors.add(new Integer(RULE_GROSS_WEIGHT_NOT_EMPTY));
			else{
				if(!validateGrossWeightIsANumber(screen))
					errors.add(new Integer(RULE_GROSS_WEIGHT_MUST_BE_A_NUMBER));
				else{
					if(!validateGrossWeightGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_GROSS_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
				}
			}
		}
		
		if(doCheckRequiredFields && !validateNetWeightNotEmpty(screen))
			errors.add(new Integer(RULE_NET_WEIGHT_NOT_EMPTY));
		else{
			if(!validateNetWeightIsANumber(screen))
				errors.add(new Integer(RULE_NET_WEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateNetWeightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_NET_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateTareWeightNotEmpty(screen))
			errors.add(new Integer(RULE_TARE_WEIGHT_NOT_EMPTY));
		else{
			if(!validateTareWeightIsANumber(screen))
				errors.add(new Integer(RULE_TARE_WEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateTareWeightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_TARE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateClassNotEmpty(screen))
			errors.add(new Integer(RULE_CLASS_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable01NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE01_NOT_EMPTY));
		
		if(!doCheckRequiredFields && validateLottable02NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE02_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable03NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE03_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable04NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE04_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable05NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE05_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable06NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE06_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable07NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE07_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable08NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE08_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable09NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE09_NOT_EMPTY));
		
		if(doCheckRequiredFields && !validateLottable10NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE10_NOT_EMPTY));

		if(doCheckRequiredFields && !validateLottable11NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE11_NOT_EMPTY));

		if(doCheckRequiredFields && !validateLottable12NotEmpty(screen))
			errors.add(new Integer(RULE_LOTTABLE12_NOT_EMPTY));

		if(doCheckRequiredFields && !validateInboundShelfLifeIsANumber(screen))
			errors.add(new Integer(RULE_INBOUND_SHELF_LIFE_MUST_BE_A_NUMBER));
		else{
			if(!validateInboundShelfLifeGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_INBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO));
		}
				
		if(!validateOutboundShelfLifeIsANumber(screen))
			errors.add(new Integer(RULE_OUTBOUND_SHELF_LIFE_MUST_BE_A_NUMBER));
		else{
			if(!validateOutboundShelfLifeGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_OUTBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		
		if(isChecked(screen.getCatchWeight())){
			if(isChecked(screen.getInboundCatchWeight())){
				if(!validateInboundAverageWeightIsANumber(screen))
					errors.add(new Integer(RULE_INBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER));
				else{
					if(!validateInboundAverageWeightGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_INBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
				}	
				if(!validateInboundToleranceIsANumber(screen))
					errors.add(new Integer(RULE_INBOUND_TOLERANCE_MUST_BE_A_NUMBER));
				else{
					if(!validateInboundToleranceGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_INBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO));
				}				
			}
			if(isChecked(screen.getInboundCatchData())){
				if(!validateAllInboundCatchDataLabelsNotEmpty(screen))
					errors.add(new Integer(RULE_INBOUND_ALL_LABELS_NOT_EMPTY));
			}
			if(isChecked(screen.getOutboundCatchWeight())){
				if(!validateOutboundAverageWeightIsANumber(screen))
					errors.add(new Integer(RULE_OUTBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER));
				else{
					if(!validateOutboundAverageWeightGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_OUTBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
				}	
				if(!validateOutboundToleranceIsANumber(screen))
					errors.add(new Integer(RULE_OUTBOUND_TOLERANCE_MUST_BE_A_NUMBER));
				else{
					if(!validateOutboundToleranceGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_OUTBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO));
				}												
			}
			if(isChecked(screen.getOutboundCatchData())){
				if(!validateAllOutboundCatchDataLabelsNotEmpty(screen))
					errors.add(new Integer(RULE_OUTBOUND_ALL_LABELS_NOT_EMPTY));
			}
		}
		
		if(!validateRFDefaultReceivingPackNotEmpty(screen))
			errors.add(new Integer(RULE_RF_DEFAULT_RECEIVING_PACK_NOT_EMPTY));
		
		if(!validateHazmatCodeDoesExist(screen))
			errors.add(new Integer(RULE_HAZMATCODE_MUST_EXIST));
		
		if(!validateDateCodeDaysIsANumber(screen))
			errors.add(new Integer(RULE_DATE_CODE_DAYS_MUST_BE_A_NUMBER));
		else{
			if(!validateDateCodeDaysGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_DATE_CODE_DAYS_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validateMaxPalletsPerZoneIsANumber(screen))
			errors.add(new Integer(RULE_MAX_PALLETS_PER_ZONE_MUST_BE_A_NUMBER));
		else{
			if(!validateMaxPalletsPerZoneGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_MAX_PALLETS_PER_ZONE_GREATER_THAN_OR_EQUAL_ZERO));
		}
						
		if(!validatePutawayLocationDoesExist(screen))
			errors.add(new Integer(RULE_PUTAWAY_LOCATION_MUST_EXIST));
		
		if(!validateInboundQCLocationDoesExist(screen))
			errors.add(new Integer(RULE_INBOUND_QC_LOCATION_MUST_EXIST));
		
		if(!validateOutboundQCLocationDoesExist(screen))
			errors.add(new Integer(RULE_OUTBOUND_QC_LOCATION_MUST_EXIST));
		
		if(!validateReturnLocationDoesExist(screen))
			errors.add(new Integer(RULE_RETURN_LOCATION_MUST_EXIST));
		
		if(!validateSerialNumberStartIsANumber(screen))
			errors.add(new Integer(RULE_SERIAL_NUMBER_START_MUST_BE_A_NUMBER));
		else{
			if(!validateSerialNumberStartGreaterThanZero(screen))
				errors.add(new Integer(RULE_SERIAL_NUMBER_START_GREATER_THAN_ZERO));
			
			if(!validateSerialNumberStartLessThanLongMax(screen)){
				errors.add(new Integer(RULE_SERIAL_NUMBER_START_LESS_THAN_SERIAL_NUMBER_MAX));
			}			
			if(validateSerialNumberNextIsANumber(screen)){
				if(!validateSerialNumberNextGreaterThanSerialNumberStart(screen))
					errors.add(new Integer(RULE_SERIAL_NUMBER_NEXT_GREATER_THAN_SERIAL_NUMBER_START));
			}
		}
		
		if(!validateSerialNumberNextIsANumber(screen))
			errors.add(new Integer(RULE_SERIAL_NUMBER_NEXT_MUST_BE_A_NUMBER));
		else{
			if(!validateSerialNumberNextLessThanLongMax(screen)){
				errors.add(new Integer(RULE_SERIAL_NUMBER_NEXT_LESS_THAN_SERIAL_NUMBER_MAX));
			}
			
			if(validateSerialNumberEndIsANumber(screen)){
				if(!validateSerialNumberEndGreaterThanSerialNumberNext(screen))
					errors.add(new Integer(RULE_SERIAL_NUMBER_END_GREATER_THAN_SERIAL_NUMBER_NEXT));
			}
		}
		if(!validateSerialNumberEndIsANumber(screen))
			errors.add(new Integer(RULE_SERIAL_NUMBER_END_MUST_BE_A_NUMBER));
		else{
			if(!validateSerialNumberEndLessThanLongMax(screen)){
				errors.add(new Integer(RULE_SERIAL_NUMBER_END_LESS_THAN_SERIAL_NUMBER_MAX));
			}
		}
																	
		if(!validateQuantityToReorderIsANumber(screen))
			errors.add(new Integer(RULE_QUANTITY_TO_REORDER_MUST_BE_A_NUMBER));
		else{
			if(!validateQuantityToReorderGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_QUANTITY_TO_REORDER_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validateCostToOrderIsANumber(screen))
			errors.add(new Integer(RULE_COST_TO_ORDER_MUST_BE_A_NUMBER));
		else{
			if(!validateCostToOrderGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_COST_TO_ORDER_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validateReorderPointIsANumber(screen))
			errors.add(new Integer(RULE_REORDER_POINT_MUST_BE_A_NUMBER));
		else{
			if(!validateReorderPointGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_REORDER_POINT_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validateRetailPricePerUnitIsANumber(screen))
			errors.add(new Integer(RULE_RETAIL_PRICE_PER_UNIT_MUST_BE_A_NUMBER));
		else{
			if(!validateRetailPricePerUnitGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_RETAIL_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validatePurchasePricePerUnitIsANumber(screen))
			errors.add(new Integer(RULE_PURCHASE_PRICE_PER_UNIT_MUST_BE_A_NUMBER));
		else{
			if(!validatePurchasePricePerUnitGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_PURCHASE_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		if(!validateCarryingPerUnitIsANumber(screen))
			errors.add(new Integer(RULE_CARRYING_PER_UNIT_MUST_BE_A_NUMBER));
		else{
			if(!validateCarryingPerUnitGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_CARRYING_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO));
		}

		if(!validateSnumDelimCountIsANumber(screen))
			errors.add(new Integer(RULE_SNUM_DELIM_COUNT_MUST_BE_A_NUMBER));
		else{
			if(!validatePurchasePricePerUnitGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_SNUM_DELIM_COUNT_GREATER_THAN_OR_EQUAL_ZERO));
		}

		if(!validateSnumPositionIsANumber(screen))
			errors.add(new Integer(RULE_SNUM_POSITION_MUST_BE_A_NUMBER));
		else{
			if(!validateSnumPositionGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_SNUM_POSITION_GREATER_THAN_OR_EQUAL_ZERO));
		}


		if(!isEmpty(screen.getSnum_mask())){
			if(!validateSnumMaskValidmask(screen))
				errors.add(new Integer(RULE_SNUM_MASK_VALIDATION));
			
		}
		
		
		if(!validateSnumAutoincrementIsZeroOrOne(screen))
				errors.add(new Integer(RULE_SNUM_AUTOINCREMENT_MUST_BE_ZERO_OR_ONE));
		
		//jp begin.8879
		if(!isEmpty(screen.getToexpiredays())){
			if(!validateToexpiredaysIsANumber(screen)){
				errors.add(new Integer(RULE_TOEXPIREDAYS_MUST_BE_A_NUMBER));
			}else if(!validateToexpiredaysGreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_TOEXPIREDAYS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(!isEmpty(screen.getTodeliverbydays())){
			if(!validateTodeliverbydaysIsANumber(screen)){
				errors.add(new Integer(RULE_TODELIVERBYDAYS_MUST_BE_A_NUMBER));
			}else if(!validateTodeliverbydaysGreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_TODELIVERBYDAYS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(!isEmpty(screen.getTobestbydays())){
			if(!validateTobestbydaysIsANumber(screen)){
				errors.add(new Integer(RULE_TOBESTBYDAYS_MUST_BE_A_NUMBER));
			}else if(!validateTobestbydaysGreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_TOBESTBYDAYS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(!isEmpty(screen.getNonstockedindicator())){
			if(!validateNonstockedindicatorIsANumber(screen)){
				errors.add(new Integer(RULE_NONSTOCKEDINDICATOR_MUST_BE_A_NUMBER));
			}else if(!validateNonstockedindicatorIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_NONSTOCKEDINDICATOR_MUST_BE_ZERO_OR_ONE));
			}
		}

		if(!isEmpty(screen.getIcd1unique())){
			if(!validateIcd1uniqueIsANumber(screen)){
				errors.add(new Integer(RULE_ICD1UNIQUE_MUST_BE_A_NUMBER));
			}else if(!validateIcd1uniqueIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_ICD1UNIQUE_MUST_BE_ZERO_OR_ONE));
			}
		}

		if(!isEmpty(screen.getOcd1unique())){
			if(!validateOcd1uniqueIsANumber(screen)){
				errors.add(new Integer(RULE_OCD1UNIQUE_MUST_BE_A_NUMBER));
			}else if(!validateOcd1uniqueIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_OCD1UNIQUE_MUST_BE_ZERO_OR_ONE));
			}
		}

		if(!isEmpty(screen.getCwflag())){
			if(!validateCwflagIsANumber(screen)){
				errors.add(new Integer(RULE_CWFLAG_MUST_BE_A_NUMBER));
			}else if(!validateCwflagIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_CWFLAG_MUST_BE_ZERO_OR_ONE));
			}
		}

		//jp end.8879
		//SRG -- Catch Weight Capture --- Begin
//		if(!isEmpty(screen.getEnableadvcwgt())){
//			if(!validateEnableAdvCwgtIsANumber(screen)){
//				errors.add(new Integer(RULE_ENABLE_ADV_CWGT_MUST_BE_A_NUMBER));
//			}else if(!validateEnableAdvCwgtIsZeroOrOne(screen)){
//				errors.add(new Integer(RULE_ENABLE_ADV_CWGT_MUST_BE_ZERO_OR_ONE));
//			}
//		}
		if(!isEmpty(screen.getCatchgrosswgt())){
			if(!validateCatchGrossWgtIsANumber(screen)){
				errors.add(new Integer(RULE_CATCH_GROSS_WGT_MUST_BE_A_NUMBER));
			}else if(!validateCatchGrossWgtIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_CATCH_GROSS_WGT_MUST_BE_ZERO_OR_ONE));
			}
		}
		if(!isEmpty(screen.getCatchnetwgt())){
			if(!validateCatchNetWgtIsANumber(screen)){
				errors.add(new Integer(RULE_CATCH_NET_WGT_MUST_BE_A_NUMBER));
			}else if(!validateCatchNetWgtIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_CATCH_NET_WGT_MUST_BE_ZERO_OR_ONE));
			}
		}
		if(!isEmpty(screen.getCatchtarewgt())){
			if(!validateCatchTareWgtIsANumber(screen)){
				errors.add(new Integer(RULE_CATCH_TARE_WGT_MUST_BE_A_NUMBER));
			}else if(!validateCatchTareWgtIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_CATCH_TARE_WGT_MUST_BE_ZERO_OR_ONE));
			}
		}
		if(!isEmpty(screen.getZerodefaultwgtforpick())){
			if(!validateZeroDefaultWgtForPickIsANumber(screen)){
				errors.add(new Integer(RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_A_NUMBER));
			}else if(!validateZeroDefaultWgtForPickIsZeroOrOne(screen)){
				errors.add(new Integer(RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_ZERO_OR_ONE));
			}
		}
//		if(!isEmpty(screen.getAdvcwttrackby())){
//			if(!validateAdvCwtTrackByIsANumber(screen)){
//				errors.add(new Integer(RULE_ADV_CWT_TRACK_BY_MUST_BE_A_NUMBER));
//			}else if(!validateAdvCwtTrackByGreaterThanZero(screen)){
//				errors.add(new Integer(RULE_ADV_CWT_TRACK_BY_GREATER_THAN_ZERO));
//			}
//		}
		if(!isEmpty(screen.getTarewgt1())){
			if(!validateTareWgt1IsANumber(screen)){
				errors.add(new Integer(RULE_TARE_WGT1_MUST_BE_A_NUMBER));
			}else if(!validateTareWgt1GreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_TARE_WGT1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		if(!isEmpty(screen.getStdnetwgt1())){
			if(!validateStdNetWgt1IsANumber(screen)){
				errors.add(new Integer(RULE_STD_NET_WGT1_MUST_BE_A_NUMBER));
			}else if(!validateStdNetWgt1GreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_STD_NET_WGT1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		if(!isEmpty(screen.getStdgrosswgt1())){
			if(!validateStdGrossWgt1IsANumber(screen)){
				errors.add(new Integer(RULE_STD_GROSS_WGT1_MUST_BE_A_NUMBER));
			}else if(!validateStdGrossWgt1GreaterThanOrEqualZero(screen)){
				errors.add(new Integer(RULE_STD_GROSS_WGT1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}		
		//SRG -- Catch Weight Capture --- End
		if(!validateSnumQuantityIsANumber(screen))
			errors.add(new Integer(RULE_SNUM_QUANTITY_MUST_BE_A_NUMBER));
		else{
			if(!validateSnumQuantityGreaterThanOrEqualZero(screen))
				errors.add(new Integer(RULE_SNUM_POSITION_GREATER_THAN_OR_EQUAL_ZERO));
		}
		
		
		if(!isEmpty(screen.getSnum_autoincrement()) && !isEmpty(screen.getSnum_quantity())){
			if(!validateSnumQuantityIsGreaterThanZeroWhenAutoincrementIsOn(screen))
				errors.add(new Integer(RULE_SNUM_QUANTITY_GREATER_THAN_ZERO_WHEN_AUTOINCREMENT_ON));
		}
		

		if(!validateSnumIncrLengthIsANumber(screen)){
			errors.add(new Integer(RULE_SNUM_INCR_LENGTH_MUST_BE_A_NUMBER));
		}
		
		
		
		if(!validateSnumIncrPosIsANumber(screen)){
			errors.add(new Integer(RULE_SNUM_INCR_POS_MUST_BE_A_NUMBER));
		}

		if(!validateSnumlongFixedIsANumber(screen)){
			errors.add(new Integer(RULE_SNUMLONG_FIXED_MUST_BE_A_NUMBER));
		}

		if(!validateSnumLengthIsANumber(screen)){
			errors.add(new Integer(RULE_SNUM_LENGTH_MUST_BE_A_NUMBER));
		}

		if(!isEmpty(screen.getSnum_length()) && !isEmpty(screen.getSnumlong_fixed())){
			if(!validateSnumFixedLengthMustBeGreaterThanSnumLength(screen))
				errors.add(new Integer(RULE_SNUMLONG_FIXED_MUST_BE_GREATER_THAN_SNUM_LENGTH));

		}
		if(!isEmpty(screen.getSnum_delimiter()) &&
				!isEmpty(screen.getSnum_position()) ){
				
			if(!validateSnumDelimAndSnumPositionConflict(screen))
				errors.add(new Integer(RULE_SNUM_DELIM_AND_SNUM_POSITION_CONFLICT));
		}

		if(screen.getAssignLocationsVOCollection() != null){
			for(int i = 0; i < screen.getAssignLocationsVOCollection().size(); i++){
				errors.addAll(new AssignLocsValidator(getContext()).validate((AssignLocationsVO)screen.getAssignLocationsVOCollection().get(i), isInsert, disableRules));
			}
		}
		if(screen.getAltVOCollection() != null){
			for(int i = 0; i < screen.getAltVOCollection().size(); i++){
				errors.addAll(new AltValidator(getContext()).validate((AltVO)screen.getAltVOCollection().get(i), isInsert, disableRules));
			}
		}
		if(screen.getSubstituteVOCollection() != null){
			for(int i = 0; i < screen.getSubstituteVOCollection().size(); i++){
				errors.addAll(new SubstituteValidator(getContext()).validate((SubstituteVO)screen.getSubstituteVOCollection().get(i), isInsert, disableRules));
			}
		}
		
		//SRG: 9.2 Upgrade -- Start
		if(validateIbsumcwflgNotEmpty(screen)){			
			if(!validateIbsumcwflgZeroOrOne(screen))
				errors.add(new Integer(RULE_IBSUMCWFLG_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateObsumcwflgNotEmpty(screen)){			
			if(!validateObsumcwflgZeroOrOne(screen))
				errors.add(new Integer(RULE_OBSUMCWFLG_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateShowrfcwontransNotEmpty(screen)){			
			if(!validateShowrfcwontransZeroOrOne(screen))
				errors.add(new Integer(RULE_SHOWRFCWONTRANS_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateCartonizeftNotEmpty(screen)){			
			if(!validateCartonizeftZeroOrOne(screen))
				errors.add(new Integer(RULE_CARTONIZEFT_MUST_BE_ZERO_OR_ONE));
		}
		
		if(validateHourstoholdlpnNotEmpty(screen)){
			if(!validateHourstoholdlpnIsANumber(screen))
				errors.add(new Integer(RULE_HOURS_TO_HOLD_LPN_MUST_BE_A_NUMBER));
			else{
				if(!validateHourstoholdlpnGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HOURS_TO_HOLD_LPN_GREATER_THAN_OR_EQUAL_ZERO ));
			}
		}
		
		if(validateHourstoholdlotNotEmpty(screen)){
			if(!validateHourstoholdlotIsANumber(screen))
				errors.add(new Integer(RULE_HOURS_TO_HOLD_LOT_MUST_BE_A_NUMBER));
			else{
				if(!validateHourstoholdlotGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HOURS_TO_HOLD_LOT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(validateTempforasnNotEmpty(screen)){
			if(!validateTempforasnIsYesOrNo(screen)) {
				errors.add(new Integer(RULE_TEMP_FOR_ASN_MUST_BE_Y_OR_N));			
			}
		}
		//SRG: 9.2 Upgrade -- End
		
		return errors;
	}
	
	private boolean validateRplnsortInAttrDom(ItemScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getRplnsort()))
			return true;
		return validateRplnsortDoesExist(screen.getRplnsort(),getContext());
	}
	private boolean validateRplnsortDoesExist(String key,
			WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "RPLNSORT", key, context).getSize() == 0)
			return false;
		else
			return true;
		
	}

	private boolean validateDapicksortInAttrDom(ItemScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getDapicksort()))
			return true;
		return validateDapicksortDoesExist(screen.getDapicksort(),getContext());
	}
	private boolean validateDapicksortDoesExist(String key,
			WMSValidationContext context) throws WMSDataLayerException {
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode( "DAPICKSORT", key, context).getSize() == 0)
			return false;
		else
			return true;
		
	}

	private boolean validateCubeGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getCube());
	}
	private boolean validateCubeIsANumber(ItemScreenVO screen){
		return isNumber(screen.getCube());
	}
	private boolean validateStorerDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		return validateStorerDoesExist(screen.getOwner(), "1",getContext());				
	}
	
	private boolean validatePackDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		return validatePackDoesExist(screen.getPack(),getContext());			
	}
	
	private boolean validateItemDoesNotExist(ItemScreenVO screen) throws WMSDataLayerException{
//Incident3223560_Defect215571		return !validateItemDoesExist(screen.getItem(),getContext());			
		return !validateItemDoesExist(screen.getItem(),screen.getOwner(),getContext());	//Incident3223560_Defect215571
	}
	
	private boolean validateItemReferenceIsANumber(ItemScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getItemReference()))
			return isNumber(screen.getItemReference());		
		else 
			return true;
	}
	
	private boolean validateGrossWeightGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getGrossWeight());
	}
	private boolean validateGrossWeightIsANumber(ItemScreenVO screen){
		return isNumber(screen.getGrossWeight());
	}
	private boolean validateNetWeightGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getNetWeight());
	}
	private boolean validateNetWeightIsANumber(ItemScreenVO screen){
		return isNumber(screen.getNetWeight());
	}
	private boolean validateTareWeightGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getTareWeight());
	}
	private boolean validateTareWeightIsANumber(ItemScreenVO screen){
		return isNumber(screen.getTareWeight());
	}
	private boolean validateInboundShelfLifeGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getInboundShelfLife()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getInboundShelfLife());
	}
	private boolean validateInboundShelfLifeIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getInboundShelfLife()))
			return true;
		return isNumber(screen.getInboundShelfLife());
	}
	private boolean validateOutboundShelfLifeGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundShelfLife()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getOutboundShelfLife());
	}
	private boolean validateOutboundShelfLifeIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundShelfLife()))
			return true;
		return isNumber(screen.getOutboundShelfLife());
	}
	private boolean validateInboundAverageWeightGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getInboundAverageWeight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getInboundAverageWeight());
	}
	private boolean validateInboundAverageWeightIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getInboundAverageWeight()))
			return true;
		return isNumber(screen.getInboundAverageWeight());
	}
	private boolean validateInboundToleranceGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getInboundTolerance()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getInboundTolerance());
	}
	private boolean validateInboundToleranceIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getInboundTolerance()))
			return true;
		return isNumber(screen.getInboundTolerance());
	}
	private boolean validateAllInboundCatchDataLabelsNotEmpty(ItemScreenVO screen){
		if(	isEmpty(screen.getInboundSerialNumber()) &&
			isEmpty(screen.getInboundOther2()) &&
			isEmpty(screen.getInboundOther3()))
			return false;
		
		return true;
		
	}
	private boolean validateOutboundAverageWeightGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundAverageWeight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getOutboundAverageWeight());
	}
	private boolean validateOutboundAverageWeightIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundAverageWeight()))
			return true;
		return isNumber(screen.getOutboundAverageWeight());
	}
	private boolean validateOutboundToleranceGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundTolerance()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getOutboundTolerance());
	}
	private boolean validateOutboundToleranceIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundTolerance()))
			return true;
		return isNumber(screen.getOutboundTolerance());
	}
	private boolean validateAllOutboundCatchDataLabelsNotEmpty(ItemScreenVO screen){
		if(	isEmpty(screen.getOutboundSerialNumber()) &&
			isEmpty(screen.getOutboundOther2()) &&
			isEmpty(screen.getOutboundOther3()))
			return false;
		
		return true;
		
	}
	private boolean validateRFDefaultReceivingPackNotEmpty(ItemScreenVO screen){
		if(isEmpty(screen.getRfDefaultReceivingPack()))
			return false;
		
		return true;
		
	}
	
	private boolean validateHazmatCodeDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getHazmatCode()))
			return true;
		return validateHazmatCodeDoesExist(screen.getHazmatCode(),getContext());				
	}
	
	private boolean validateDateCodeDaysIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getDateCodeDays()))
			return true;
		return isNumber(screen.getDateCodeDays());
	}
	private boolean validateDateCodeDaysGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getDateCodeDays()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getDateCodeDays());
	}
	
	private boolean validateMaxPalletsPerZoneGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getMaxPalletsPerZone()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxPalletsPerZone());
	}
	private boolean validateMaxPalletsPerZoneIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getMaxPalletsPerZone()))
			return true;
		return isNumber(screen.getMaxPalletsPerZone());
	}
	private boolean validatePutawayLocationDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPutawayLocation()))
			return true;
		return validatePutawayLocationDoesExist(screen.getPutawayLocation(),getContext());				
	}
	private boolean validateInboundQCLocationDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getInboundQCLocation()))
			return true;
		return validateLocationDoesExist(screen.getInboundQCLocation(),getContext());				
	}
	private boolean validateOutboundQCLocationDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getOutboundQCLocation()))
			return true;
		return validateLocationDoesExist(screen.getOutboundQCLocation(),getContext());				
	}
	private boolean validateReturnLocationDoesExist(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReturnLocation()))
			return true;
		return validateLocationDoesExist(screen.getReturnLocation(),getContext());				
	}
	
	private boolean validateSerialNumberStartGreaterThanZero(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberStart()))
			return true;
		return greaterThanZeroValidation(screen.getSerialNumberStart());
	}
	
	private boolean validateSerialNumberNextGreaterThanSerialNumberStart(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberNext()))
			return true;
		return greaterThanValidation(screen.getSerialNumberNext(), screen.getSerialNumberStart());
	}
	
	private boolean validateSerialNumberEndGreaterThanSerialNumberNext(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberEnd()))
			return true;
		return greaterThanValidation(screen.getSerialNumberEnd(), screen.getSerialNumberNext());
	}
	private boolean validateSerialNumberStartIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberStart()))
			return true;
		return isNumber(screen.getSerialNumberStart());
	}
	private boolean validateSerialNumberStartLessThanLongMax(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberStart()))
			return true;
		return lessThanMaxLongValidation(screen.getSerialNumberStart());
	}
	private boolean validateSerialNumberNextIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberNext()))
			return true;
		return isNumber(screen.getSerialNumberNext());
	}
	private boolean validateSerialNumberNextLessThanLongMax(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberNext()))
			return true;
		return lessThanMaxLongValidation(screen.getSerialNumberNext());
	}
	private boolean validateSerialNumberEndIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberEnd()))
			return true;
		return isNumber(screen.getSerialNumberEnd());
	}
	private boolean validateSerialNumberEndLessThanLongMax(ItemScreenVO screen){
		if(isEmpty(screen.getSerialNumberEnd()))
			return true;
		return lessThanMaxLongValidation(screen.getSerialNumberEnd());
	}
	
	private boolean validateQuantityToReorderIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getQuantityToReorder()))
			return true;
		return isNumber(screen.getQuantityToReorder());
	}
	private boolean validateQuantityToReorderGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getQuantityToReorder()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getQuantityToReorder());
	}
	
	private boolean validateCostToOrderIsCurrency(ItemScreenVO screen){
		_log.debug("LOG_SYSTEM_OUT","[validateCostToOrderIsCurrency]CostToOrder:"+screen.getCostToOrder(),100L);
		return currencyValidation(screen.getCostToOrder());
	}
	private boolean validateCostToOrderIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCostToOrder()))
			return true;
		return isNumber(screen.getCostToOrder());
	}
	private boolean validateCostToOrderGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getCostToOrder()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getCostToOrder());
	}
	
	private boolean validateReorderPointIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getReorderPoint()))
			return true;
		return isNumber(screen.getReorderPoint());
	}
	private boolean validateReorderPointGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getReorderPoint()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getReorderPoint());
	}
	
	private boolean validateRetailPricePerUnitIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getRetailPricePerUnit()))
			return true;
		return isNumber(screen.getRetailPricePerUnit());
	}
	private boolean validateRetailPricePerUnitGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getRetailPricePerUnit()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getRetailPricePerUnit());
	}
	
	private boolean validatePurchasePricePerUnitIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getPuchasePricePerUnit()))
			return true;
		return isNumber(screen.getPuchasePricePerUnit());
	}
	private boolean validatePurchasePricePerUnitGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getPuchasePricePerUnit()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getPuchasePricePerUnit());
	}
	

	private boolean validateCarryingPerUnitIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCarryingPerUnit()))
			return true;
		return isNumber(screen.getCarryingPerUnit());
	}
	private boolean validateCarryingPerUnitGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getCarryingPerUnit()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getCarryingPerUnit());
	}


	private boolean validateSnumDelimCountIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_delim_count()))
			return true;
		return isNumber(screen.getSnum_delim_count());
	}
	private boolean validateSnumDelimCountGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_delim_count()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getSnum_delim_count());
	}
	


	private boolean validateSnumPositionIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_position()))
			return true;
		return isNumber(screen.getSnum_position());
	}

	//jp begin.8879
	private boolean validateToexpiredaysIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getToexpiredays()))
			return true;
		return isNumber(screen.getToexpiredays());
	}

	private boolean validateTodeliverbydaysIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getTodeliverbydays()))
			return true;
		return isNumber(screen.getTodeliverbydays());
	}

	private boolean validateTobestbydaysIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getTobestbydays()))
			return true;
		return isNumber(screen.getTobestbydays());
	}

	private boolean validateNonstockedindicatorIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getNonstockedindicator()))
			return true;
		return isNumber(screen.getNonstockedindicator());
	}

	private boolean validateIcd1uniqueIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getIcd1unique()))
			return true;
		return isNumber(screen.getIcd1unique());
	}

	private boolean validateOcd1uniqueIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getOcd1unique()))
			return true;
		return isNumber(screen.getOcd1unique());
	}

	private boolean validateCwflagIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCwflag()))
			return true;
		return isNumber(screen.getCwflag());
	}

	//jp end.8879
	private boolean validateSnumPositionGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_position()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getSnum_position());
	}

	private boolean validateToexpiredaysGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getToexpiredays()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getToexpiredays());
	}

	private boolean validateTodeliverbydaysGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getTodeliverbydays()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getTodeliverbydays());
	}

	private boolean validateTobestbydaysGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getTobestbydays()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getTobestbydays());
	}

	private boolean validateSnumMaskValidmask(ItemScreenVO screen){
		if(!isNumber(screen.getSnum_mask())){
			String val = screen.getSnum_mask().toUpperCase();
			Pattern pattern = Pattern.compile("[A-DN]*");
			Matcher matcher = pattern.matcher(val);
			if(matcher.matches()){
				return true;
			}
		}
		return false;
		
	}

	private boolean validateSnumAutoincrementIsZeroOrOne(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_autoincrement()))
			return true;
	
		if(!isNumber(screen.getSnum_autoincrement())){
			return false;
		}
		
		double autoincrement = Double.parseDouble(screen.getSnum_autoincrement());
		if(autoincrement==0 ||autoincrement==1)
			return true;
		
		return false;
	}

	//jp begin. 8879
	private boolean validateNonstockedindicatorIsZeroOrOne(ItemScreenVO screen){
		double nonstockedindicator = Double.parseDouble(screen.getNonstockedindicator());
		if(nonstockedindicator==0 ||nonstockedindicator==1)
			return true;
		
		return false;

	}

	private boolean validateIcd1uniqueIsZeroOrOne(ItemScreenVO screen){
		double icd1unique = Double.parseDouble(screen.getIcd1unique());
		if(icd1unique==0 ||icd1unique==1)
			return true;
		
		return false;

	}

	private boolean validateOcd1uniqueIsZeroOrOne(ItemScreenVO screen){
		double ocd1unique = Double.parseDouble(screen.getOcd1unique());
		if(ocd1unique==0 ||ocd1unique==1)
			return true;
		
		return false;

	}

	private boolean validateCwflagIsZeroOrOne(ItemScreenVO screen){
		double cwflag = Double.parseDouble(screen.getCwflag());
		if(cwflag==0 ||cwflag==1)
			return true;
		
		return false;

	}

	//jp end. 8879
	
	private boolean validateSnumQuantityIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_quantity()))
			return true;
		return isNumber(screen.getSnum_quantity());
	}

	private boolean validateSnumQuantityGreaterThanOrEqualZero(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_quantity()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getSnum_quantity());
		
	}

	private boolean validateSnumQuantityIsGreaterThanZeroWhenAutoincrementIsOn(ItemScreenVO screen){
		
		if(validateSnumQuantityIsANumber(screen) && 
				validateSnumQuantityGreaterThanOrEqualZero(screen) &&
				validateSnumAutoincrementIsZeroOrOne(screen)){
			
			double qty = Double.parseDouble(screen.getSnum_quantity());
			double autoincrement = Double.parseDouble(screen.getSnum_autoincrement());
			if(autoincrement==1 && qty>1)
				return true;
			else if (autoincrement==1 && qty<=1)
				return false;
			else if (autoincrement==0 && qty==1)
				return true;
			else
				return false;
			
		}else
			return true;
		
	}
	private boolean validateSnumIncrLengthIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_incr_length()))
			return true;
		return isNumber(screen.getSnum_incr_length());
		
	}
	
	private boolean validateSnumIncrPosIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_incr_pos()))
			return true;
		return isNumber(screen.getSnum_incr_pos());
		
	}
	
	
	private boolean validateSnumlongFixedIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnumlong_fixed()))
			return true;
		return isNumber(screen.getSnumlong_fixed());
		
	}
	
	
	private boolean validateSnumLengthIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getSnum_length()))
			return true;
		return isNumber(screen.getSnum_length());
		
	}
	
	private boolean validateSnumFixedLengthMustBeGreaterThanSnumLength(ItemScreenVO screen){
		if (validateSnumlongFixedIsANumber(screen) &&
				validateSnumLengthIsANumber(screen)){
			double fixedlength = Double.parseDouble(screen.getSnumlong_fixed());
			double length = Double.parseDouble(screen.getSnum_length());
			if (fixedlength>0 && length > fixedlength){
				return false;
			}
			return true;
		}else
			return true;
	}
	
	private boolean validateSnumDelimAndSnumPositionConflict(ItemScreenVO screen){
		if(!isNumber(screen.getSnum_position()))
			return true;
		
		Double position = Double.parseDouble(screen.getSnum_position());
		if(position>0)
			return false;
		return true;
	}
	
	private boolean validateStorerNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getOwner());			
	}
	private boolean validateItemNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getItem());			
	}
	private boolean validatePackNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getPack());			
	}
	private boolean validateCubeNotEmpty(ItemScreenVO screen){
		_log.debug("LOG_SYSTEM_OUT","[validateCubeNotEmpty]  This is the value in ItemScreenValidator:cube:"+screen.getCube(),100L);
		return !isEmpty(screen.getCube());			
	}
	private boolean validateGrossWeightNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getGrossWeight());			
	}
	private boolean validateNetWeightNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getNetWeight());			
	}
	private boolean validateTareWeightNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getTareWeight());			
	}
	private boolean validateClassNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getShippingTabClass());			
	}
	private boolean validateLottable01NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable01());			
	}
	private boolean validateLottable02NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable02());			
	}
	private boolean validateLottable03NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable03());			
	}
	private boolean validateLottable04NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable04());			
	}
	private boolean validateLottable05NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable05());			
	}
	private boolean validateLottable06NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable06());			
	}
	private boolean validateLottable07NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable07());			
	}
	private boolean validateLottable08NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable08());			
	}
	private boolean validateLottable09NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable09());			
	}
	private boolean validateLottable10NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable10());			
	}

	//jp begin - 8879
	private boolean validateLottable11NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable11label());			
	}

	private boolean validateLottable12NotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getLottable12label());			
	}
	//jp end - 8879
	private boolean validateStorerLengthIs15OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOwner()))
			return true;
		return screen.getOwner().length() < 16;			
	}
	
	private boolean validateItemLengthIs50OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getItem()))
			return true;
		return screen.getItem().length() < 51;			
	}
	
	private boolean validateDescriptionLengthIs60OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getDescription()))
			return true;
		return screen.getDescription().length() < 61;			
	}
	
	private boolean validatePackLengthIs50OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getPack()))
			return true;
		return screen.getPack().length() < 51;			
	}
	
	private boolean validateCartonGroupLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCartonGroup()))
			return true;
		return screen.getCartonGroup().length() < 11;			
	}
	
	private boolean validateTariffLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getTariff()))
			return true;
		return screen.getTariff().length() < 11;			
	}
	
	private boolean validateItemReferenceIs8OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getItemReference()))
			return true;
		return screen.getItemReference().length() < 9;			
	}
	
	private boolean validateShelfLifeCodeTypeIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getShelfLifeCodeType()))
			return true;
		return screen.getShelfLifeCodeType().length() < 2;			
	}
	
	private boolean validateCatchWeightLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchWeight()))
			return true;
		return screen.getCatchWeight().length() < 11;			
	}
	
	private boolean validateShelfLifeIndicatorLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getShelfLifeIndicator()))
			return true;
		return screen.getShelfLifeIndicator().length() < 11;			
	}
	
	private boolean validateLottableValidationLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottableValidation()))
			return true;
		return screen.getLottableValidation().length() < 11;			
	}
	
	private boolean validateRFDefaultReceivingPackLengthIs50OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getRfDefaultReceivingPack()))
			return true;
		return screen.getRfDefaultReceivingPack().length() < 51;			
	}
	
	private boolean validateOnReceiptCopyPackLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOnReceiptCopyPack()))
			return true;
		return screen.getOnReceiptCopyPack().length() < 11;			
	}
	
	private boolean validateRFDefaultReceivingUOMLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getRfDefaultReceivingUOM()))
			return true;
		return screen.getRfDefaultReceivingUOM().length() < 11;			
	}
	
	private boolean validateItemGroup1LengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getItemGroup1()))
			return true;
		return screen.getItemGroup1().length() < 11;			
	}
	
	private boolean validateItemGroup2LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getItemGroup2()))
			return true;
		return screen.getItemGroup2().length() < 31;			
	}
	
	private boolean validateHazmatCodeLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getHazmatCode()))
			return true;
		return screen.getHazmatCode().length() < 11;			
	}
	
	private boolean validateShippableContainerLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getShippableContainer()))
			return true;
		return screen.getShippableContainer().length() < 11;			
	}
	
	private boolean validateVerticalStorageLengthIs1rLess(ItemScreenVO screen){
		if(isEmpty(screen.getVerticalStorage()))
			return true;
		return screen.getVerticalStorage().length() < 2;			
	}
	
	private boolean validateTransportationModeLengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getTransportationMode()))
			return true;
		return screen.getTransportationMode().length() < 31;			
	}
	
	private boolean validateFreightClassLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getTransportationMode()))
			return true;
		return screen.getTransportationMode().length() < 11;			
	}
	
	private boolean validateClassLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getShippingTabClass()))
			return true;
		return screen.getShippingTabClass().length() < 11;			
	}
	
	private boolean validateLottable01LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable01()))
			return true;
		return screen.getLottable01().length() < 21;			
	}
	
	private boolean validateLottable02LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable02()))
			return true;
		return screen.getLottable02().length() < 21;			
	}
	
	private boolean validateLottable03LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable03()))
			return true;
		return screen.getLottable03().length() < 21;			
	}
	
	private boolean validateLottable04LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable04()))
			return true;
		return screen.getLottable04().length() < 21;			
	}
	
	private boolean validateLottable05LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable05()))
			return true;
		return screen.getLottable05().length() < 21;			
	}
	
	private boolean validateLottable06LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable06()))
			return true;
		return screen.getLottable06().length() < 21;			
	}
	
	private boolean validateLottable07LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable07()))
			return true;
		return screen.getLottable07().length() < 21;			
	}
	
	private boolean validateLottable08LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable08()))
			return true;
		return screen.getLottable08().length() < 21;			
	}
	 
	private boolean validateLottable09LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable09()))
			return true;
		return screen.getLottable09().length() < 21;			
	}
	
	private boolean validateLottable10LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable10()))
			return true;
		return screen.getLottable10().length() < 21;			
	}

	private boolean validateLottable11LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable11label()))
			return true;
		return screen.getLottable11label().length() < 21;			
	}

	private boolean validateLottable12LengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getLottable12label()))
			return true;
		return screen.getLottable12label().length() < 21;			
	}

	
	private boolean validateBarCode01LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode01()))
			return true;
		return screen.getBarcode01().length() < 31;			
	}
	
	private boolean validateBarCode02LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode02()))
			return true;
		return screen.getBarcode02().length() < 31;			
	}
	
	private boolean validateBarCode03LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode03()))
			return true;
		return screen.getBarcode03().length() < 31;			
	}
	
	private boolean validateBarCode04LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode04()))
			return true;
		return screen.getBarcode04().length() < 31;			
	}
	
	private boolean validateBarCode05LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode05()))
			return true;
		return screen.getBarcode05().length() < 31;			
	}
	
	private boolean validateBarCode06LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode06()))
			return true;
		return screen.getBarcode06().length() < 31;			
	}
	
	private boolean validateBarCode07LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode07()))
			return true;
		return screen.getBarcode07().length() < 31;			
	}
	
	private boolean validateBarCode08LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode08()))
			return true;
		return screen.getBarcode08().length() < 31;			
	}
	
	private boolean validateBarCode09LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode09()))
			return true;
		return screen.getBarcode09().length() < 31;			
	}
	
	private boolean validateBarCode10LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBarcode10()))
			return true;
		return screen.getBarcode10().length() < 31;			
	}
	
	private boolean validateInboundCatchWeightLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundCatchWeight	()))
			return true;
		return screen.getInboundCatchWeight().length() < 2;			
	}
	
	private boolean validateInboundNoEntryOfTotalWeightLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundNoEntryOfTotalWeight()))
			return true;
		return screen.getInboundNoEntryOfTotalWeight().length() < 2;			
	}
	
	private boolean validateInboundCatchWeightByLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundCatchWeightBy()))
			return true;
		return screen.getInboundCatchWeightBy().length() < 2;			
	}
	
	private boolean validateInboundCatchDataLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundCatchData()))
			return true;
		return screen.getInboundCatchData().length() < 2;			
	}
	
	private boolean validateInboundSerialNumberLengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundSerialNumber()))
			return true;
		return screen.getInboundSerialNumber().length() < 6;			
	}
	
	private boolean validateInboundOther2LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundOther2()))
			return true;
		return screen.getInboundOther2().length() < 6;			
	}
	
	private boolean validateInboundOther3LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundOther3()))
			return true;
		return screen.getInboundOther3().length() < 6;			
	}
	
	private boolean validateOutboundCatchWeightLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundCatchWeight()))
			return true;
		return screen.getOutboundCatchWeight().length() < 2;			
	}
	
	private boolean validateOutboundCatchWeightByLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundCatchWeightBy()))
			return true;
		return screen.getOutboundCatchWeightBy().length() < 2;			
	}
	
	private boolean validateOutboundNoEntryOfTotalWeightLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundNoEntryOfTotalWeight()))
			return true;
		return screen.getOutboundNoEntryOfTotalWeight().length() < 2;			
	}
	
	private boolean validateAllowCustomerOverrideLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getAllowCustomerOverride()))
			return true;
		return screen.getAllowCustomerOverride().length() < 2;			
	}
	
	private boolean validateOutboundSerialNumberLengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundSerialNumber()))
			return true;
		return screen.getOutboundSerialNumber().length() < 6;			
	}
	
	private boolean validateOutboundOther2LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundOther2()))
			return true;
		return screen.getOutboundOther2().length() < 6;			
	}
	
	private boolean validateOutboundOther3LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundOther3()))
			return true;
		return screen.getOutboundOther3().length() < 6;			
	}
	
	private boolean validateCatchWhenLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchWhen()))
			return true;
		return screen.getCatchWhen().length() < 11;			
	}
	private boolean validateCatchQuantity1LengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchQuantity1()))
			return true;
		return screen.getCatchQuantity1().length() < 11;			
	}
	private boolean validateCatchQuantity2LengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchQuantity2()))
			return true;
		return screen.getCatchQuantity2().length() < 11;			
	}
	private boolean validateCatchQuantity3LengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchQuantity3()))
			return true;
		return screen.getCatchQuantity3().length() < 11;			
	}
	private boolean validateHoldCodeOnRFReceiptLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getHoldCodeOnRFReceipt()))
			return true;
		return screen.getHoldCodeOnRFReceipt().length() < 11;			
	}
	private boolean validateItemTypeLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getItemType()))
			return true;
		return screen.getItemType().length() < 2;			
	}
	
	private boolean validateReceiptValidationLengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getReceiptValidation()))
			return true;
		return screen.getReceiptValidation().length() < 19;			
	}
	
	private boolean validateManualSetupRequiredLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getManualSetupRequired()))
			return true;
		return screen.getManualSetupRequired().length() < 2;			
	}
	
	private boolean validatePutawayZoneLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getPutawayZone()))
			return true;
		return screen.getPutawayZone().length() < 11;			
	}
	
	private boolean validatePutawayLocationLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getPutawayLocation()))
			return true;
		return screen.getPutawayLocation().length() < 11;			
	}
	
	private boolean validateInboundQCLocationLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getInboundQCLocation()))
			return true;
		return screen.getInboundQCLocation().length() < 11;			
	}
	
	private boolean validateOutboundQCLocationLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOutboundQCLocation()))
			return true;
		return screen.getOutboundQCLocation().length() < 11;			
	}
	
	private boolean validateReturnLocationLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getReturnLocation()))
			return true;
		return screen.getReturnLocation().length() < 11;			
	}
	
	private boolean validatePutawayStrategyLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getPutawayStrategy()))
			return true;
		return screen.getPutawayStrategy().length() < 11;			
	}
	
	private boolean validateStrategyLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getStrategy()))
			return true;
		return screen.getStrategy().length() < 11;			
	}
	
	private boolean validateRotationLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getRotation()))
			return true;
		return screen.getRotation().length() < 2;			
	}
	private boolean validateRotateByLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getRotateBy()))
			return true;
		return screen.getRotateBy().length() < 11;			
	}
	
	private boolean validateOppertunisticLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOppertunistic()))
			return true;
		return screen.getOppertunistic().length() < 2;			
	}
	
	private boolean validateConveyableLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getConveyable()))
			return true;
		return screen.getConveyable().length() < 2;			
	}
	private boolean validateVerifyLottable4and5LengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getVerifyLottable4and5()))
			return true;
		return screen.getVerifyLottable4and5().length() < 2;			
	}
	
	private boolean validateBulkCartonGroupLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getBulkCartonGroup()))
			return true;
		return screen.getBulkCartonGroup().length() < 11;			
	}
	
	private boolean validateAllowConsolidationLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getAllowConsolidation()))
			return true;
		return screen.getAllowConsolidation().length() < 2;			
	}
	
	private boolean validateCycleClassLengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCycleClass()))
			return true;
		return screen.getCycleClass().length() < 6;			
	}
	
	private boolean validateCcDiscrepancyHandlingRuleLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCcDiscrepancyHandlingRule()))
			return true;
		return screen.getCcDiscrepancyHandlingRule().length() < 11;			
	}
	
	private boolean validateUDF1LengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf1()))
			return true;
		return screen.getUdf1().length() < 19;			
	}
	private boolean validateUDF2LengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf2()))
			return true;
		return screen.getUdf2().length() < 19;			
	}
	private boolean validateUDF3LengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf3()))
			return true;
		return screen.getUdf3().length() < 19;			
	}
	private boolean validateUDF4LengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf4()))
			return true;
		return screen.getUdf4().length() < 19;			
	}
	private boolean validateUDF5LengthIs18OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf5()))
			return true;
		return screen.getUdf5().length() < 19;			
	}
	private boolean validateUDF6LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf6()))
			return true;
		return screen.getUdf6().length() < 31;			
	}
	private boolean validateUDF7LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf7()))
			return true;
		return screen.getUdf7().length() < 31;			
	}
	private boolean validateUDF8LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf8()))
			return true;
		return screen.getUdf8().length() < 31;			
	}
	private boolean validateUDF9LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf9()))
			return true;
		return screen.getUdf9().length() < 31;			
	}
	private boolean validateUDF10LengthIs30OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getUdf10()))
			return true;
		return screen.getUdf10().length() < 31;			
	}
	
	private boolean validatePickingInstructionLengthIs2147483647OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getPickingInstruction()))
			return true;
		return screen.getPickingInstruction().length() < 2147483648L;			
	}
	
	private boolean validateNotesLengthIs2147483647OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getNotes()))
			return true;
		return screen.getNotes().length() < 2147483648L;			
	}

	//jp begin.8879
	private boolean validateIcdlabel4LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getIcdlabel4()))
			return true;
		return screen.getIcdlabel4().length() < 6;			
	}

	private boolean validateIcdlabel5LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getIcdlabel5()))
			return true;
		return screen.getIcdlabel5().length() < 6;			
	}

	private boolean validateOcdlabel4LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOcdlabel4()))
			return true;
		return screen.getOcdlabel4().length() < 6;			
	}

	private boolean validateOcdlabel5LengthIs5OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getOcdlabel5()))
			return true;
		return screen.getOcdlabel5().length() < 6;			
	}

	//jp end.8879
	
	//SRG -- Catch Weight Capture --- Begin
	private boolean validateStdUomLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getStduom()))
			return true;
		return screen.getStduom().length() < 11;			
	}
//	private boolean validateEnableAdvCwgtLengthIs1OrLess(ItemScreenVO screen){
//		if(isEmpty(screen.getEnableadvcwgt()))
//			return true;
//		return screen.getEnableadvcwgt().length() < 2;			
//	}
	private boolean validateCatchGrossWgtLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchgrosswgt()))
			return true;
		return screen.getCatchgrosswgt().length() < 2;			
	}
	private boolean validateCatchNetWgtLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchnetwgt()))
			return true;
		return screen.getCatchnetwgt().length() < 2;			
	}
	private boolean validateCatchTareWgtLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCatchtarewgt()))
			return true;
		return screen.getCatchtarewgt().length() < 2;			
	}
	private boolean validateZeroDefaultWgtForPickLengthIs1OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getZerodefaultwgtforpick()))
			return true;
		return screen.getZerodefaultwgtforpick().length() < 2;			
	}
//	private boolean validateAdvCwtTrackByLengthIs1OrLess(ItemScreenVO screen){
//		if(isEmpty(screen.getAdvcwttrackby()))
//			return true;
//		return screen.getAdvcwttrackby().length() < 2;			
//	}
	
//	private boolean validateEnableAdvCwgtIsANumber(ItemScreenVO screen){
//		if(isEmpty(screen.getEnableadvcwgt()))
//			return true;
//		return isNumber(screen.getEnableadvcwgt());
//	}
	private boolean validateCatchGrossWgtIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCatchgrosswgt()))
			return true;
		return isNumber(screen.getCatchgrosswgt());
	}
	private boolean validateCatchNetWgtIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCatchnetwgt()))
			return true;
		return isNumber(screen.getCatchnetwgt());
	}
	private boolean validateCatchTareWgtIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getCatchtarewgt()))
			return true;
		return isNumber(screen.getCatchtarewgt());
	}
	private boolean validateZeroDefaultWgtForPickIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getZerodefaultwgtforpick()))
			return true;
		return isNumber(screen.getZerodefaultwgtforpick());
	}
//	private boolean validateAdvCwtTrackByIsANumber(ItemScreenVO screen){
//		if(isEmpty(screen.getAdvcwttrackby()))
//			return true;
//		return isNumber(screen.getAdvcwttrackby());
//	}
	private boolean validateTareWgt1IsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getTarewgt1()))
			return true;
		return isNumber(screen.getTarewgt1());
	}
	private boolean validateStdNetWgt1IsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getStdnetwgt1()))
			return true;
		return isNumber(screen.getStdnetwgt1());
	}
	private boolean validateStdGrossWgt1IsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getStdgrosswgt1()))
			return true;
		return isNumber(screen.getStdgrosswgt1());
	}
	
//	private boolean validateEnableAdvCwgtIsZeroOrOne(ItemScreenVO screen){
//		double enableAdvCwgt = Double.parseDouble(screen.getEnableadvcwgt());
//		if(enableAdvCwgt==0 ||enableAdvCwgt==1)
//			return true;
		
//		return false;
//	}
	private boolean validateCatchGrossWgtIsZeroOrOne(ItemScreenVO screen){
		double catchGrossWgt = Double.parseDouble(screen.getCatchgrosswgt());
		if(catchGrossWgt==0 ||catchGrossWgt==1)
			return true;
		
		return false;
	}
	private boolean validateCatchNetWgtIsZeroOrOne(ItemScreenVO screen){
		double catchNetWgt = Double.parseDouble(screen.getCatchnetwgt());
		if(catchNetWgt==0 ||catchNetWgt==1)
			return true;
		
		return false;
	}
	private boolean validateCatchTareWgtIsZeroOrOne(ItemScreenVO screen){
		double catchTareWgt = Double.parseDouble(screen.getCatchtarewgt());
		if(catchTareWgt==0 ||catchTareWgt==1)
			return true;
		
		return false;
	}
	private boolean validateZeroDefaultWgtForPickIsZeroOrOne(ItemScreenVO screen){
		double zeroDefaultWgtForPick = Double.parseDouble(screen.getZerodefaultwgtforpick());
		if(zeroDefaultWgtForPick==0 ||zeroDefaultWgtForPick==1)
			return true;
		
		return false;
	}
	
//	private boolean validateAdvCwtTrackByGreaterThanZero(ItemScreenVO screen){
//		if(isEmpty(screen.getAdvcwttrackby()))
//			return true;
//		return greaterThanZeroValidation(screen.getAdvcwttrackby());
//	}
	
	private boolean validateTareWgt1GreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getTarewgt1());
	}
	private boolean validateStdNetWgt1GreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getStdnetwgt1());
	}
	private boolean validateStdGrossWgt1GreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getStdgrosswgt1());
	}
	//SRG -- Catch Weight Capture --- End
	
	private boolean validateTariffInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTariff()))
			return true;
		return validateTariffDoesExist(screen.getTariff(),getContext());			
	}
	
	private boolean validateCartonGroupInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCartonGroup()))
			return true;
		return validateCartonizationDoesExist(screen.getCartonGroup(),getContext());			
	}
	
	private boolean validateShelfLifeCodeTypeInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getShelfLifeCodeType()))
			return true;
		return validateCodelkupDoesExist("RFRCODTYPE", screen.getShelfLifeCodeType(),getContext());			
	}
	
	private boolean validateLottableValidationInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getLottableValidation()))
			return true;
		return validateLottableValidationDoesExist(screen.getLottableValidation(),getContext());			
	}
	
	private boolean validateOnReceiptCopyPackInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getOnReceiptCopyPack()))
			return true;
		return validateCodelkupDoesExist("COPYPACKTO", screen.getOnReceiptCopyPack(),getContext());			
	}
	
	private boolean validateRFDefaultReceivingUOMInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getRfDefaultReceivingUOM()))
			return true;
		
		
		return validateRFDefaultReceivingUOMDoesExist(screen);
		//return screen.getRfDefaultReceivingUOM().toString().equals("EA");			
	}
	
	//SRG: 9.2 Upgrade -- Start
	public boolean validateIbsumcwflgNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getIbsumcwflg());			
	}
	
	public boolean validateIbsumcwflgZeroOrOne(ItemScreenVO screen){
		return isZeroOrOneValidation(screen.getIbsumcwflg());
	}
	
	public boolean validateObsumcwflgNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getObsumcwflg());			
	}
	
	public boolean validateObsumcwflgZeroOrOne(ItemScreenVO screen){
		return isZeroOrOneValidation(screen.getObsumcwflg());
	}
	
	public boolean validateShowrfcwontransNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getShowrfcwontrans());			
	}
	
	public boolean validateShowrfcwontransZeroOrOne(ItemScreenVO screen){
		return isZeroOrOneValidation(screen.getShowrfcwontrans());
	}
	
	public boolean validateCartonizeftNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getCartonizeft());			
	}
	
	public boolean validateCartonizeftZeroOrOne(ItemScreenVO screen){
		return isZeroOrOneValidation(screen.getCartonizeft());
	}	
	
	private boolean validateHourstoholdlpnIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getHourstoholdlpn()))
			return true;
		return isNumber(screen.getHourstoholdlpn());
	}
	private boolean validateHourstoholdlotIsANumber(ItemScreenVO screen){
		if(isEmpty(screen.getHourstoholdlot()))
			return true;
		return isNumber(screen.getHourstoholdlot());
	}
	
	private boolean validateHourstoholdlpnGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHourstoholdlpn());
	}
	
	private boolean validateHourstoholdlotGreaterThanOrEqualZero(ItemScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHourstoholdlot());
	}
	
	public boolean validateTempforasnNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getTempforasn());		
	}
	
	public boolean validateHourstoholdlpnNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getHourstoholdlpn());		
	}
	
	public boolean validateHourstoholdlotNotEmpty(ItemScreenVO screen){
		return !isEmpty(screen.getHourstoholdlot());		
	}
	
	public boolean validateTempforasnIsYesOrNo(ItemScreenVO screen){
		return isYesOrNo(screen.getTempforasn());			
	}
	
	private boolean validateNmfcclassLengthIs40OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getNmfcclass()))
			return true;
		return screen.getNmfcclass().length() < 41;			
	}
	
	private boolean validateMateabilitycodeLengthIs64OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getMateabilitycode()))
			return true;
		return screen.getMateabilitycode().length() < 65;			
	}
	
	private boolean validateRecurcodeLengthIs10OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getRecurcode()))
			return true;
		return screen.getRecurcode().length() < 11;			
	}
	
	private boolean validateWgtuomLengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getWgtuom()))
			return true;
		return screen.getWgtuom().length() < 21;			
	}
	
	private boolean validateDimenuomLengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getDimenuom()))
			return true;
		return screen.getDimenuom().length() < 21;			
	}
	
	private boolean validateCubeuomLengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getCubeuom()))
			return true;
		return screen.getCubeuom().length() < 21;			
	}
	
	private boolean validateStoragetypeLengthIs20OrLess(ItemScreenVO screen){
		if(isEmpty(screen.getStoragetype()))
			return true;
		return screen.getStoragetype().length() < 21;			
	}
	
	private boolean validateFillqtyuomInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getFillqtyuom()))
			return true;
		return validateCodelkupDoesExist("FILLQTYUOM", screen.getFillqtyuom(),getContext());			
	}
	
	private boolean validateAutoreleaselpnbyInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getAutoreleaselpnby()))
			return true;
		return validateCodelkupDoesExist("HOLDRLSBY", screen.getAutoreleaselpnby(),getContext());			
	}
	
	private boolean validateAutoreleaselotbyInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getAutoreleaselotby()))
			return true;
		return validateCodelkupDoesExist("HOLDRLSBY", screen.getAutoreleaselotby(),getContext());			
	}
	
	private boolean validatePutawayclassInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPutawayclass()))
			return true;
		return validateCodelkupDoesExist("PACLASS", screen.getPutawayclass(),getContext());			
	}
	
	public boolean validateAmstrategykeyInAttrDom(ItemScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAmstrategykey()))
			return true;
		return validateAmstrategyDoesExist(screen.getAmstrategykey(), getContext());
	}
	
	public boolean validateLotholdcodeInAttrDom(ItemScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLotholdcode()))
			return true;
		return validateInventoryHoldCodeDoesExist(screen.getLotholdcode(), getContext());
	}
	
	private boolean validateStduomInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStduom()))
			return true;		
		return validateStduomDoesExist(screen);					
	}
	
	private boolean validateStduomDoesExist(ItemScreenVO screen)throws WMSDataLayerException{
		DataLayerResultWrapper packs1 = PackQueryRunner.getPackByKeyAndUOM1(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs1.getSize()>0){
			return true;
		}
			
		DataLayerResultWrapper packs2 = PackQueryRunner.getPackByKeyAndUOM2(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs2.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs3 = PackQueryRunner.getPackByKeyAndUOM3(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs3.getSize()>0){
			return true;
		}
		
		//_log.debug("LOG_SYSTEM_OUT","Packkey :"+screen.getPackkey()+ " RFUOM:"+screen.getRfDefaultReceivingUOM(),100L);
		
		DataLayerResultWrapper packs4 = PackQueryRunner.getPackByKeyAndUOM4(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs4.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs5 = PackQueryRunner.getPackByKeyAndUOM5(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs5.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs6 = PackQueryRunner.getPackByKeyAndUOM6(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs6.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs7 = PackQueryRunner.getPackByKeyAndUOM7(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs7.getSize()>0){
			return true;
		}

		
		DataLayerResultWrapper packs8 = PackQueryRunner.getPackByKeyAndUOM8(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs8.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs9 = PackQueryRunner.getPackByKeyAndUOM9(screen.getPackkey(), screen.getStduom(), getContext());
		if (packs9.getSize()>0){
			return true;
		}

		return false;
	}
	//SRG: 9.2 Upgrade -- End
	
	private boolean validateRFDefaultReceivingUOMDoesExist(ItemScreenVO screen)throws WMSDataLayerException{
		DataLayerResultWrapper packs1 = PackQueryRunner.getPackByKeyAndUOM1(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs1.getSize()>0){
			return true;
		}
			
		DataLayerResultWrapper packs2 = PackQueryRunner.getPackByKeyAndUOM2(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs2.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs3 = PackQueryRunner.getPackByKeyAndUOM3(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs3.getSize()>0){
			return true;
		}
		
		//_log.debug("LOG_SYSTEM_OUT","Packkey :"+screen.getPackkey()+ " RFUOM:"+screen.getRfDefaultReceivingUOM(),100L);
		
		DataLayerResultWrapper packs4 = PackQueryRunner.getPackByKeyAndUOM4(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs4.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs5 = PackQueryRunner.getPackByKeyAndUOM5(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs5.getSize()>0){
			return true;
		}
		
		DataLayerResultWrapper packs6 = PackQueryRunner.getPackByKeyAndUOM6(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs6.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs7 = PackQueryRunner.getPackByKeyAndUOM7(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs7.getSize()>0){
			return true;
		}

		
		DataLayerResultWrapper packs8 = PackQueryRunner.getPackByKeyAndUOM8(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs8.getSize()>0){
			return true;
		}

		DataLayerResultWrapper packs9 = PackQueryRunner.getPackByKeyAndUOM9(screen.getPackkey(), screen.getRfDefaultReceivingUOM(), getContext());
		if (packs9.getSize()>0){
			return true;
		}


		return false;


	}
	
	private boolean validateTransportationModeInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getTransportationMode()))
			return true;
		return validateCodelkupDoesExist("TRANSPMODE", screen.getTransportationMode(),getContext());			
	}
	
	private boolean validateFreightClassInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getFreightClass()))
			return true;
		
		if(!validateCodelkupDoesExist("FREIGHTCLS", screen.getFreightClass(),getContext())){
			if(screen.getFreightclass().equals("FAK"))
				return true;
			else
				return false;
		}else
			return true;
			
	}
	
	private boolean validateInboundCatchWeightByInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getInboundCatchWeightBy()))
			return true;
		return validateCodelkupDoesExist("WDUOM", screen.getInboundCatchWeightBy(),getContext());			
	}
	
	private boolean validateOutboundCatchWeightByInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getOutboundCatchWeightBy()))
			return true;
		return validateCodelkupDoesExist("WDUOM", screen.getOutboundCatchWeightBy(),getContext());			
	}
	private boolean validateOutboundCatchWhenInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCatchWhen()))
			return true;
		return validateCodelkupDoesExist("CATCHWHEN", screen.getCatchWhen(),getContext());			
	}
	private boolean validateOutboundCatchQuantity1InAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCatchQuantity1()))
			return true;
		return validateCodelkupDoesExist("CATCHQTY", screen.getCatchQuantity1(),getContext());			
	}
	
	private boolean validateOutboundCatchQuantity2InAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCatchQuantity2()))
			return true;
		return validateCodelkupDoesExist("CATCHQTY", screen.getCatchQuantity2(),getContext());			
	}
	
	private boolean validateOutboundCatchQuantity3InAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCatchQuantity3()))
			return true;
		return validateCodelkupDoesExist("CATCHQTY", screen.getCatchQuantity3(),getContext());			
	}
	
	private boolean validateHoldCodeOnRFReceiptInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getHoldCodeOnRFReceipt()))
			return true;
		return validateInventoryHoldCodeDoesExist(screen.getHoldCodeOnRFReceipt(),getContext());			
	}
	
	private boolean validateItemTypeInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getItemType()))
			return true;
		return validateCodelkupDoesExist("SKUTYPE", screen.getItemType(),getContext());			
	}
	
	private boolean validateReceiptValidationInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getReceiptValidation()))
			return true;
		return validateReceiptValidationDoesExist(screen.getReceiptValidation(),getContext());			
	}
	
	private boolean validatePutawayZoneInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPutawayZone()))
			return true;
		return validatePutawayZoneDoesExist(screen.getPutawayZone(),getContext());			
	}
	
	private boolean validatePutawayStrategyInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPutawayStrategy()))
			return true;
		return validatePutawayStrategyDoesExist(screen.getPutawayStrategy(),getContext());			
	}
	
	private boolean validateStrategyInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getStrategy()))
			return true;
		return validateStrategyDoesExist(screen.getStrategy(),getContext());			
	}
	
	private boolean validateRotationInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getRotation()))
			return true;
		return validateCodelkupDoesExist("ROTATION", screen.getRotation(),getContext());			
	}
	
	private boolean validateRotateByInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getRotateBy()))
			return true;
		return validateCodelkupDoesExist("SKUROTATE", screen.getRotateBy(),getContext());			
	}
	
	private boolean validateBulkCartonGroupInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getBulkCartonGroup()))
			return true;
		return validateCartonizationDoesExist(screen.getBulkCartonGroup(),getContext());			
	}
	
	private boolean validateCycleClassInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCycleClass()))
			return true;
		return validateCCSetupDoesExist(screen.getCycleClass(),getContext());			
	}
	
	private boolean validateCCDescrepencyHandlingRuleAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCcDiscrepancyHandlingRule()))
			return true;
		return validateCCAdjRulesDoesExist(screen.getCcDiscrepancyHandlingRule(),getContext());			
	}
	
	
	private boolean validateVoicegroupingidInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getVoicegroupingid()))
			return true;
		return validateCodelkupDoesExist("VOICEGRID", screen.getVoicegroupingid(),getContext());			
	}
	
	private boolean validateCartonizeftInAttrDom(ItemScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getCartonizeft()))
			return true;
		return validateCodelkupDoesExist("YESNO", screen.getCartonizeft(),getContext());			
	}
	
	
	
	
	public class AssignLocsValidator extends BaseScreenValidator{
	
		public AssignLocsValidator(WMSValidationContext context) {
			super(context);			
		}
		public static final int RULE_LOCATION_SKU_X_LOC_MUST_BE_UNIQUE = 1001;
		public static final int RULE_LOCATION_MUST_EXIST = 1002;
		public static final int RULE_MAXIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO = 1003;
		public static final int RULE_MAXIMUM_CAPACITY_MUST_BE_A_NUMBER = 1004;
		public static final int RULE_MINIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO = 1005;
		public static final int RULE_MINIMUM_CAPACITY_MUST_BE_A_NUMBER = 1006;
		public static final int RULE_REPLENISHMENT_OVERRIDE_MUST_BE_A_NUMBER = 1007;
		public static final int RULE_REPLENISHMENT_OVERRIDE_GREATER_THAN_OR_EQUAL_ZERO = 1008;
		
		
		//Required Fields
		public static final int RULE_LOCATION_MUST_NOT_BE_EMPTY = 1009;
		public static final int RULE_LOCATION_TYPE_MUST_NOT_BE_EMPTY = 1010;
		
		//Field Length Rules
		public static final int RULE_LENGTH_LOCATION_TYPE_10 = 1011;
		public static final int RULE_LENGTH_LOCATION_10 = 1012;
		public static final int RULE_LENGTH_REPLENISHMENT_PRIORITY_5 = 1013;
		public static final int RULE_LENGTH_MINIMUM_REPLENISHMENT_10 = 1014;		
		
		//Attribute Domain Rules
		public static final int RULE_ATTR_DOM_LOCATION_TYPE = 1015;
		public static final int RULE_ATTR_DOM_MINIMUM_REPLENISHMENT_UOM = 1016;
		
		
		
		public ArrayList validate(AssignLocationsVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			ArrayList errors = new ArrayList();
			if(doCheckFieldLength){
				
				
				if(!validateLocationTypeLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_LOCATION_TYPE_10));			
				}
				
				if(!validateLocationLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_LOCATION_10));		
				}
				
				if(!validateReplenishmentPriorityLengthIs5OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_REPLENISHMENT_PRIORITY_5));		
				}
				
				if(!validateMinimumReplenishmentLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_MINIMUM_REPLENISHMENT_10));		
				}
			}
			
			if(doCheckAttributeDomain){	
				if(!validateLocationTypeInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_LOCATION_TYPE  ));		
				}
				
				if(!validateMinimumReplenishmentUOMInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_MINIMUM_REPLENISHMENT_UOM  ));	
				}
			}
			
			if(doCheckRequiredFields){
				if(!validateLocationTypeNotEmpty(screen)){
					errors.add(new Integer(RULE_LOCATION_TYPE_MUST_NOT_BE_EMPTY));			
				}
			}
			
			if(!validateLocationNotEmpty(screen)){
				errors.add(new Integer(RULE_LOCATION_MUST_NOT_BE_EMPTY));
			}
			else{						
				if(isInsert){				
					if(!validateSkuXLocDoesNotExist(screen))
						errors.add(new Integer(RULE_LOCATION_SKU_X_LOC_MUST_BE_UNIQUE));						
				}
			
				if(!validateLocationDoesExist(screen))
					errors.add(new Integer(RULE_LOCATION_MUST_EXIST));			
			}
			
			if(!validateMaximumCapacityIsANumber(screen))
				errors.add(new Integer(RULE_MAXIMUM_CAPACITY_MUST_BE_A_NUMBER));
			else{
				if(!validateMaximumCapacityGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO));
			}
			if(!validateMinimumCapacityIsANumber(screen))
				errors.add(new Integer(RULE_MINIMUM_CAPACITY_MUST_BE_A_NUMBER));
			else{
				if(!validateMinimumCapacityGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MINIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO));
			}
			if(!validateReplenishmentOverrideIsANumber(screen))
				errors.add(new Integer(RULE_REPLENISHMENT_OVERRIDE_MUST_BE_A_NUMBER));
			else{
				if(!validateReplenishmentOverrideGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_REPLENISHMENT_OVERRIDE_GREATER_THAN_OR_EQUAL_ZERO));
			}
			return errors;
		}
		
		private boolean validateLocationNotEmpty(AssignLocationsVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getLocation());			
		}
		private boolean validateLocationTypeNotEmpty(AssignLocationsVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getLocationType());			
		}
		private boolean validateSkuXLocDoesNotExist(AssignLocationsVO screen) throws WMSDataLayerException{
			return !validateSkuXLocationDoesExist(screen.getLocation(), screen.getItem(), screen.getOwner(),getContext());			
		}
		private boolean validateLocationDoesExist(AssignLocationsVO screen) throws WMSDataLayerException{
			
			
			return validateLocationDoesExist(screen.getLocation(),getContext());		
			
		}
		private boolean validateMaximumCapacityIsANumber(AssignLocationsVO screen){
			if(isEmpty(screen.getMaximumCapacity()))
				return true;
			return isNumber(screen.getMaximumCapacity());
		}
		private boolean validateMaximumCapacityGreaterThanOrEqualZero(AssignLocationsVO screen){
			if(isEmpty(screen.getMaximumCapacity()))
				return true;
			return greaterThanOrEqualToZeroValidation(screen.getMaximumCapacity());
		}
		private boolean validateMinimumCapacityIsANumber(AssignLocationsVO screen){
			if(isEmpty(screen.getMinimumCapacity()))
				return true;
			return isNumber(screen.getMinimumCapacity());
		}
		private boolean validateMinimumCapacityGreaterThanOrEqualZero(AssignLocationsVO screen){
			if(isEmpty(screen.getMinimumCapacity()))
				return true;
			return greaterThanOrEqualToZeroValidation(screen.getMinimumCapacity());
		}
		private boolean validateReplenishmentOverrideIsANumber(AssignLocationsVO screen){
			if(isEmpty(screen.getReplenishmentOverride()))
				return true;
			return isNumber(screen.getReplenishmentOverride());
		}
		private boolean validateReplenishmentOverrideGreaterThanOrEqualZero(AssignLocationsVO screen){
			if(isEmpty(screen.getReplenishmentOverride()))
				return true;
			return greaterThanOrEqualToZeroValidation(screen.getReplenishmentOverride());
		}
		
		private boolean validateLocationTypeLengthIs10OrLess(AssignLocationsVO screen){
			if(isEmpty(screen.getLocationType()))
				return true;
			return screen.getLocationType().length() < 11;			
		}
		
		private boolean validateLocationLengthIs10OrLess(AssignLocationsVO screen){
			if(isEmpty(screen.getLocation()))
				return true;
			return screen.getLocation().length() < 11;			
		}
		
		private boolean validateReplenishmentPriorityLengthIs5OrLess(AssignLocationsVO screen){
			if(isEmpty(screen.getReplenishmentPriority()))
				return true;
			return screen.getReplenishmentPriority().length() < 6;			
		}
		
		private boolean validateMinimumReplenishmentLengthIs10OrLess(AssignLocationsVO screen){
			if(isEmpty(screen.getMinimumReplenishment()))
				return true;
			return screen.getMinimumReplenishment().length() < 11;			
		}
		private boolean validateLocationTypeInAttrDom(AssignLocationsVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getLocationType()))
				return true;
			return validateCodelkupDoesExist("LOCTYPE", screen.getLocationType(),getContext());			
		}
		private boolean validateMinimumReplenishmentUOMInAttrDom(AssignLocationsVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getMinimumReplenishment()))
				return true;
			DataLayerResultWrapper codelkup = CodelkupQueryRunner.getCodelkupByListnameAndCode("CWUOM", screen.getMinimumReplenishment(),getContext());
			if(codelkup.getSize() == 0)
				return false;
			
			//String uomCode = (String)codelkup.getValue(0, DataLayerColumnList.COLUMN_CODELKUP_CODE);
			String uomCode = (String)codelkup.getValue(0, DataLayerColumnList.COLUMN_CODELKUP_DESCRIPTION);

			DataLayerResultWrapper sku  =ItemQueryRunner.getItemByKey(screen.getSku(), getContext());
			if(sku.getSize()==0){
				return false;
			}
			String packkey = (String)sku.getValue(0, DataLayerColumnList.COLUMN_ITEM_PACKKEY);
			screen.setPackkey(packkey);
			
			if(validatePackDoesExistUsingPackKeyAndPackUOM1(screen.getPackkey(),uomCode,getContext()))
				return true;
			if(validatePackDoesExistUsingPackKeyAndPackUOM2(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM3(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM4(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM5(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM6(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM7(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM8(screen.getPackkey(),uomCode,getContext()))
				return true;	
			if(validatePackDoesExistUsingPackKeyAndPackUOM9(screen.getPackkey(),uomCode,getContext()))
				return true;	
			return false;			
		}
	}
	
	public class AltValidator extends BaseScreenValidator{
		
		public AltValidator(WMSValidationContext context) {
			super(context);			
		}
		public static final int RULE_ALTERNATE_ITEM_MUST_BE_UNIQUE = 1103;
		public static final int RULE_ALTERNATE_ITEM_MUST_NOT_EXIST_IN_SKU_TABLE = 1104;
		
		//Required Fileds
		public static final int RULE_OWNER_MUST_NOT_BE_EMPTY = 1100;
		public static final int RULE_ITEM_MUST_NOT_BE_EMPTY = 1101;
		public static final int RULE_ALTERNATE_ITEM_MUST_NOT_BE_EMPTY = 1102;
		
		//Field Length Rules
		public static final int RULE_LENGTH_OWNER_15 = 1105;
		public static final int RULE_LENGTH_ITEM_50 = 1106;
		public static final int RULE_LENGTH_ALTERNATE_ITEM_50 = 1107;
		public static final int RULE_LENGTH_PACK_50 = 1108;
		public static final int RULE_LENGTH_DEFAULT_UOM_10 = 1109;
		public static final int RULE_LENGTH_TYPE_10 = 1110;
		public static final int RULE_LENGTH_VENDOR_15 = 1111;
		public static final int RULE_LENGTH_UDF1_32 = 1112;
		public static final int RULE_LENGTH_UDF2_32 = 1113;
		public static final int RULE_LENGTH_UDF3_32 = 1114;
		public static final int RULE_LENGTH_UDF4_32 = 1115;
		public static final int RULE_LENGTH_UDF5_32 = 1116;
		
		//Attribute Domain Rules
		public static final int RULE_ATTR_DOM_DEFAULT_UOM = 1117;
		public static final int RULE_ATTR_DOM_TYPE = 1118;
		
		public ArrayList validate(AltVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			ArrayList errors = new ArrayList();
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			
			if(doCheckFieldLength){
				if(!validateOwnerLengthIs15OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_OWNER_15));	
				}
				if(!validateItemLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_ITEM_50));		
				}
				if(!validateAlternateItemLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_ALTERNATE_ITEM_50));		
				}
				if(!validatePackLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_PACK_50));	
				}
				if(!validateDefaultUOMLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_DEFAULT_UOM_10));			
				}
				if(!validateTypeLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_TYPE_10));		
				}
				if(!validateVendorLengthIs15OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_VENDOR_15));			
				}
				if(!validateUDF1rLengthIs32OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_UDF1_32));		
				}
				if(!validateUDF2rLengthIs32OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_UDF2_32));			
				}
				if(!validateUDF3rLengthIs32OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_UDF3_32));			
				}
				if(!validateUDF4rLengthIs32OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_UDF4_32));		
				}
				if(!validateUDF5rLengthIs32OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_UDF5_32));	
				}	
			}
			
			if(doCheckAttributeDomain){	
				if(!validateDefaultUOMInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_DEFAULT_UOM  ));		
				}
				
				if(!validateTypeInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_TYPE));	
				}
			}
			
			if(doCheckRequiredFields && !validateOwnerNotEmpty(screen)){
				errors.add(new Integer(RULE_OWNER_MUST_NOT_BE_EMPTY));
			}
			if(doCheckRequiredFields && !validateItemNotEmpty(screen)){
				errors.add(new Integer(RULE_ITEM_MUST_NOT_BE_EMPTY));
			}
			if(doCheckRequiredFields && !validateAlternateItemNotEmpty(screen)){
				errors.add(new Integer(RULE_ALTERNATE_ITEM_MUST_NOT_BE_EMPTY));
			}
			if(isInsert){
				if(!validateAlternateItemDoesNotExist(screen)){
					errors.add(new Integer(RULE_ALTERNATE_ITEM_MUST_BE_UNIQUE));
				}
				if(!validateAlternateItemDoesNotExistInSkuTable(screen)){
					errors.add(new Integer(RULE_ALTERNATE_ITEM_MUST_NOT_EXIST_IN_SKU_TABLE));
				}
			}
			else{

					if(!validateAlternateItemDoesNotExistInSkuTable(screen)){
						errors.add(new Integer(RULE_ALTERNATE_ITEM_MUST_NOT_EXIST_IN_SKU_TABLE));
					}

			}
			return errors;
		}
		private boolean validateOwnerNotEmpty(AltVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getOwner());			
		}
		private boolean validateItemNotEmpty(AltVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getItem());			
		}
		private boolean validateAlternateItemNotEmpty(AltVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getAlternateItem());			
		}
		private boolean validateAlternateItemDoesNotExist(AltVO screen) throws WMSDataLayerException{
			return !validateAltSkuDoesExist(screen.getAlternateItem(), screen.getOwner(),getContext());			
		}
		private boolean validateAlternateItemDoesNotExistInSkuTable(AltVO screen) throws WMSDataLayerException{
			_log.debug("LOG_SYSTEM_OUT","\n\n\n\nENTERING validateAlternateItemDoesNotExistInSkuTable.....\n\n\n\n\n",100L);
			return !validateItemDoesExist(screen.getAlternateItem(),screen.getOwner(),getContext());			
		}
		private boolean wasAlternateItemChanged(AltVO screen) throws WMSDataLayerException{
			DataLayerResultWrapper records = AltSkuQueryRunner.getAltSkuBySerialKey(screen.getSerialKey(),getContext());
			if(records.getSize() > 0){
				Object currentSerialKeyObject = records.getValue(0, DataLayerColumnList.COLUMN_ALTSKU_SERIALKEY);
				if(currentSerialKeyObject != null && !currentSerialKeyObject.toString().equals(screen.getAlternateItem())){
					return true;
				}
			}
			return false;		
		}
		
		private boolean validateOwnerLengthIs15OrLess(AltVO screen){
			if(isEmpty(screen.getOwner()))
				return true;
			return screen.getOwner().length() < 16;			
		}
		private boolean validateItemLengthIs50OrLess(AltVO screen){
			if(isEmpty(screen.getItem()))
				return true;
			return screen.getItem().length() < 51;			
		}
		private boolean validateAlternateItemLengthIs50OrLess(AltVO screen){
			if(isEmpty(screen.getAlternateItem()))
				return true;
			return screen.getAlternateItem().length() < 51;			
		}
		private boolean validatePackLengthIs50OrLess(AltVO screen){
			if(isEmpty(screen.getPack()))
				return true;
			return screen.getPack().length() < 51;			
		}
		private boolean validateDefaultUOMLengthIs10OrLess(AltVO screen){
			if(isEmpty(screen.getDefaultReceivingUOM()))
				return true;
			return screen.getDefaultReceivingUOM().length() < 11;			
		}
		private boolean validateTypeLengthIs10OrLess(AltVO screen){
			if(isEmpty(screen.getType()))
				return true;
			return screen.getType().length() < 11;			
		}
		private boolean validateVendorLengthIs15OrLess(AltVO screen){
			if(isEmpty(screen.getVendor()))
				return true;
			return screen.getVendor().length() < 16;			
		}
		private boolean validateUDF1rLengthIs32OrLess(AltVO screen){
			if(isEmpty(screen.getUdf1()))
				return true;
			return screen.getUdf1().length() < 33;			
		}
		private boolean validateUDF2rLengthIs32OrLess(AltVO screen){
			if(isEmpty(screen.getUdf2()))
				return true;
			return screen.getUdf2().length() < 33;			
		}
		private boolean validateUDF3rLengthIs32OrLess(AltVO screen){
			if(isEmpty(screen.getUdf3()))
				return true;
			return screen.getUdf3().length() < 33;			
		}
		private boolean validateUDF4rLengthIs32OrLess(AltVO screen){
			if(isEmpty(screen.getUdf4()))
				return true;
			return screen.getUdf4().length() < 33;			
		}
		private boolean validateUDF5rLengthIs32OrLess(AltVO screen){
			if(isEmpty(screen.getUdf5()))
				return true;
			return screen.getUdf5().length() < 33;			
		}
		
		private boolean validateDefaultUOMInAttrDom(AltVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getDefaultReceivingUOM()))
				return true;
			return screen.getDefaultReceivingUOM().toString().equals("EA");			
		}
		private boolean validateTypeInAttrDom(AltVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getType()))
				return true;
			return validateCodelkupDoesExist("ALTSKUTYPE", screen.getType(),getContext());			
		}
	}
	
	public class SubstituteValidator extends BaseScreenValidator{
		
		public SubstituteValidator(WMSValidationContext context) {
			super(context);			
		}
		public static final int RULE_SEQUENCE_MUST_BE_A_NUMBER = 1204;
		public static final int RULE_SUBSTITUTE_ITEM_MUST_BE_UNIQUE = 1205;
		public static final int RULE_SUBSTITUTE_ITEM_MUST_BE_A_VALID_ITEM = 1206;
		public static final int RULE_ITEM_UNITS_MUST_BE_A_NUMBER = 1207;
		public static final int RULE_ITEM_UNITS_GREATER_THAN_OR_EQUAL_ZERO = 1208;
		public static final int RULE_SUBSTITUTE_UNITS_MUST_BE_A_NUMBER = 1209;
		public static final int RULE_SUBSTITUTE_UNITS_GREATER_THAN_OR_EQUAL_ZERO = 1210;
		
		//Required Fields
		public static final int RULE_OWNER_MUST_NOT_BE_EMPTY = 1200;
		public static final int RULE_ITEM_MUST_NOT_BE_EMPTY = 1201;
		public static final int RULE_SUBSTITUTE_ITEM_MUST_NOT_BE_EMPTY = 1202;
		public static final int RULE_SEQUENCE_MUST_NOT_BE_EMPTY = 1203;
		
		//Field Length Rules
		public static final int RULE_LENGTH_OWNER_15 = 1211;
		public static final int RULE_LENGTH_ITEM_50 = 1212;
		public static final int RULE_LENGTH_ITEM_PACK_50 = 1213;
		public static final int RULE_LENGTH_ITEM_UOM_10 = 1214;								
		public static final int RULE_LENGTH_SUBSTITUTE_ITEM_50 = 1215;
		public static final int RULE_LENGTH_SUBSTITUTE_PACK_50 = 1216;
		public static final int RULE_LENGTH_SUBSTITUTE_UOM_10 = 1217;			
		
		//Attribute Domain Rules
		public static final int RULE_ATTR_DOM_ITEM_UOM = 1218;
		public static final int RULE_ATTR_DOM_SUBSTITUTE_UOM = 1219;
		
		
		public ArrayList validate(SubstituteVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			ArrayList errors = new ArrayList();
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			
			if(doCheckFieldLength){
				if(!validateOwnerLengthIs15OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_OWNER_15));
				}
				if(!validateItemLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_ITEM_50));
				}
				if(!validateItemPackLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_ITEM_PACK_50));
				}
				if(!validateItemUOMLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_ITEM_UOM_10));
				}
				if(!validateSubstituteItemLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_SUBSTITUTE_ITEM_50));
				}
				if(!validateSubstituteItemPackLengthIs50OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_SUBSTITUTE_PACK_50));
				}
				if(!validateSubstituteUOMLengthIs10OrLess(screen)){
					errors.add(new Integer(RULE_LENGTH_SUBSTITUTE_UOM_10));
				}
			}
			
			if(doCheckAttributeDomain){	
				if(!validateItemUOMInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_ITEM_UOM  ));		
				}
				
				if(!validateSubstituteUOMInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_SUBSTITUTE_UOM));	
				}
			}
			
			if(doCheckRequiredFields && !validateOwnerNotEmpty(screen)){
				errors.add(new Integer(RULE_OWNER_MUST_NOT_BE_EMPTY));
			}
			else{
				if(validateSubstituteItemNotEmpty(screen)){
					if(!validateSubstituteItemExistsInSkuTable(screen)){
						errors.add(new Integer(RULE_SUBSTITUTE_ITEM_MUST_BE_A_VALID_ITEM));
					}	
					if(validateItemNotEmpty(screen)){
						if(isInsert){
							if(!validateSubstituteItemDoesNotExist(screen)){
								errors.add(new Integer(RULE_SUBSTITUTE_ITEM_MUST_BE_UNIQUE));
							}				
						}
					}
				}
			}
			if(doCheckRequiredFields && !validateItemNotEmpty(screen)){
				errors.add(new Integer(RULE_ITEM_MUST_NOT_BE_EMPTY));
			}
			if(doCheckRequiredFields && !validateSubstituteItemNotEmpty(screen)){
				errors.add(new Integer(RULE_SUBSTITUTE_ITEM_MUST_NOT_BE_EMPTY));
			}
			if(doCheckRequiredFields && !validateSequenceNotEmpty(screen)){
				errors.add(new Integer(RULE_SEQUENCE_MUST_NOT_BE_EMPTY));
			}
			else{
				if(!validateSequenceIsANumber(screen)){
					errors.add(new Integer(RULE_SEQUENCE_MUST_BE_A_NUMBER));
				}
			}
			if(!validateItemUnitsIsANumber(screen))
				errors.add(new Integer(RULE_ITEM_UNITS_MUST_BE_A_NUMBER));
			else{
				if(!validateItemUnitsGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_ITEM_UNITS_GREATER_THAN_OR_EQUAL_ZERO));
			}
			
			if(!validateSubstituteUnitsIsANumber(screen))
				errors.add(new Integer(RULE_SUBSTITUTE_UNITS_MUST_BE_A_NUMBER));
			else{
				if(!validateSubstituteUnitsGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_SUBSTITUTE_UNITS_GREATER_THAN_OR_EQUAL_ZERO));
			}
			return errors;
		}
		
		private boolean validateSubstituteItemDoesNotExist(SubstituteVO screen) throws WMSDataLayerException{
			return !validateSubstituteSkuDoesExist(screen.getItem(), screen.getOwner(), screen.getSubstituteItem(),getContext());			
		}
		private boolean validateItemNotEmpty(SubstituteVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getItem());			
		}
		private boolean validateOwnerNotEmpty(SubstituteVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getOwner());			
		}
		private boolean validateSubstituteItemNotEmpty(SubstituteVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getSubstituteItem());			
		}
		private boolean validateSequenceNotEmpty(SubstituteVO screen) throws WMSDataLayerException{
			return !isEmpty(screen.getSequence());			
		}
		private boolean validateSequenceIsANumber(SubstituteVO screen) throws WMSDataLayerException{
			return !isNumber(screen.getSequence());			
		}
		private boolean validateSubstituteItemExistsInSkuTable(SubstituteVO screen) throws WMSDataLayerException{
			return validateItemDoesExist(screen.getSubstituteItem(),screen.getOwner(),getContext());			
		}
		private boolean validateItemUnitsIsANumber(SubstituteVO screen){
			if(isEmpty(screen.getItemUnits()))
				return true;
			return isNumber(screen.getItemUnits());
		}
		private boolean validateItemUnitsGreaterThanOrEqualZero(SubstituteVO screen){
			if(isEmpty(screen.getItemUnits()))
				return true;
			return greaterThanOrEqualToZeroValidation(screen.getItemUnits());
		}
		private boolean validateSubstituteUnitsIsANumber(SubstituteVO screen){
			if(isEmpty(screen.getSubstituteUnits()))
				return true;
			return isNumber(screen.getSubstituteUnits());
		}
		private boolean validateSubstituteUnitsGreaterThanOrEqualZero(SubstituteVO screen){
			if(isEmpty(screen.getSubstituteUnits()))
				return true;
			return greaterThanOrEqualToZeroValidation(screen.getSubstituteUnits());
		}
		
		private boolean validateOwnerLengthIs15OrLess(SubstituteVO screen){
			if(isEmpty(screen.getOwner()))
				return true;
			return screen.getOwner().length() < 16;			
		}
		private boolean validateItemLengthIs50OrLess(SubstituteVO screen){
			if(isEmpty(screen.getItem()))
				return true;
			return screen.getItem().length() < 51;			
		}
		private boolean validateItemPackLengthIs50OrLess(SubstituteVO screen){
			if(isEmpty(screen.getItemPack()))
				return true;
			return screen.getItemPack().length() < 51;			
		}
		private boolean validateItemUOMLengthIs10OrLess(SubstituteVO screen){
			if(isEmpty(screen.getItemUOM()))
				return true;
			return screen.getItemUOM().length() < 10;			
		}
		private boolean validateSubstituteItemLengthIs50OrLess(SubstituteVO screen){
			if(isEmpty(screen.getSubstituteItem()))
				return true;
			return screen.getSubstituteItem().length() < 51;			
		}
		private boolean validateSubstituteItemPackLengthIs50OrLess(SubstituteVO screen){
			if(isEmpty(screen.getSubstitutePack()))
				return true;
			return screen.getSubstitutePack().length() < 51;			
		}
		private boolean validateSubstituteUOMLengthIs10OrLess(SubstituteVO screen){
			if(isEmpty(screen.getSubstituteUOM()))
				return true;
			return screen.getSubstituteUOM().length() < 11;			
		}
		private boolean validateItemUOMInAttrDom(SubstituteVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getItemUOM()))
				return true;
			return screen.getItemUOM().toString().equals("EA");			
		}
		private boolean validateSubstituteUOMInAttrDom(SubstituteVO screen) throws WMSDataLayerException{
			if(isEmpty(screen.getSubstituteUOM()))
				return true;
			return screen.getSubstituteUOM().toString().equals("EA");			
		}
	}
	

	public static String getErrorMessage(int errorCode, Locale locale, ItemScreenVO.AltVO altScreen){
		String errorMsg = new String();
		String param[] = null;
		switch(errorCode){
			
		case ItemScreenValidator.AltValidator.RULE_ALTERNATE_ITEM_MUST_BE_UNIQUE:
			param = new String[4];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_OWNER, locale);
			param[1] = altScreen.getStorerkey();
			param[2] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ALTERNATE_ITEM, locale);
			param[3] = altScreen.getAltsku();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_ALT_ITEM_SCREEN_ERROR_DUPLICATE_ALT_ITEM, locale, param);

		case ItemScreenValidator.AltValidator.RULE_ALTERNATE_ITEM_MUST_NOT_EXIST_IN_SKU_TABLE:
			return getMustNotExistErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ALTERNATE_ITEM, locale),
					altScreen.getAlternateItem(), 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ITEM, locale));
		
		//Required Fileds
		case ItemScreenValidator.AltValidator.RULE_OWNER_MUST_NOT_BE_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_OWNER, locale));
			
		case ItemScreenValidator.AltValidator.RULE_ITEM_MUST_NOT_BE_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ITEM, locale));

		case ItemScreenValidator.AltValidator.RULE_ALTERNATE_ITEM_MUST_NOT_BE_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ALTERNATE_ITEM, locale));

			//Field Length Rules
		case ItemScreenValidator.AltValidator.RULE_LENGTH_OWNER_15:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_OWNER, locale), "15");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_ITEM_50:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ITEM, locale), "50");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_ALTERNATE_ITEM_50:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_ALTERNATE_ITEM, locale), "50");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_PACK_50:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_PACK, locale), "50");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_DEFAULT_UOM_10:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_DEFAULT_RECEIVING_UOM, locale), "10");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_TYPE_10:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_TYPE, locale), "10");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_VENDOR_15:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_VENDOR, locale), "15");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_UDF1_32:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_UDF1, locale), "32");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_UDF2_32:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_UDF2, locale), "32");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_UDF3_32:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_UDF3, locale), "32");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_UDF4_32:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_UDF4, locale), "32");

		case ItemScreenValidator.AltValidator.RULE_LENGTH_UDF5_32:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_UDF5, locale), "32");

			//Attribute Domain Rules
		case ItemScreenValidator.AltValidator.RULE_ATTR_DOM_DEFAULT_UOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_DEFAULT_RECEIVING_UOM, locale), altScreen.getDefaultuom());

		case ItemScreenValidator.AltValidator.RULE_ATTR_DOM_TYPE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ALT_ITEM_TYPE, locale), altScreen.getType());


		}
		return errorMsg;
	}
	public static String getErrorMessage(int errorCode, Locale locale, ItemScreenVO.AssignLocationsVO alScreen){
		String errorMsg = new String();
		String param[] = null;
		switch(errorCode){
			
		case ItemScreenValidator.AssignLocsValidator.RULE_LOCATION_SKU_X_LOC_MUST_BE_UNIQUE:
			param = new String[6];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OWNER, locale);
			param[1] = alScreen.getStorerkey();
			param[2] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM, locale);
			param[3] = alScreen.getSku();
			param[4] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION, locale);
			param[5] = alScreen.getLocation();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_ASSIGN_LOC_SCREEN_ERROR_DUPLICATE_ASSIGN_LOC, locale, param);

		case ItemScreenValidator.AssignLocsValidator.RULE_LOCATION_MUST_EXIST:
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION, locale);
			param[1] = alScreen.getLocation();
			return getDoesNotExistErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION, locale), alScreen.getLocation());
			
		case ItemScreenValidator.AssignLocsValidator.RULE_MAXIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MAXIMUM_CAPACITY, locale));

		case ItemScreenValidator.AssignLocsValidator.RULE_MAXIMUM_CAPACITY_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MAXIMUM_CAPACITY, locale));
			
		case ItemScreenValidator.AssignLocsValidator.RULE_MINIMUM_CAPACITY_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_CAPACITY, locale));
		
		case ItemScreenValidator.AssignLocsValidator.RULE_MINIMUM_CAPACITY_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_CAPACITY, locale));

		case ItemScreenValidator.AssignLocsValidator.RULE_REPLENISHMENT_OVERRIDE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_REPLENISHMENT_OVERRIDE, locale));

		case ItemScreenValidator.AssignLocsValidator.RULE_REPLENISHMENT_OVERRIDE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_REPLENISHMENT_OVERRIDE, locale));

			//Required Fields
		case ItemScreenValidator.AssignLocsValidator.RULE_LOCATION_MUST_NOT_BE_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION, locale));
			
		case ItemScreenValidator.AssignLocsValidator.RULE_LOCATION_TYPE_MUST_NOT_BE_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION_TYPE, locale));

			//Field Length Rules
		case ItemScreenValidator.AssignLocsValidator.RULE_LENGTH_LOCATION_TYPE_10:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION_TYPE, locale), "10");
			
		case ItemScreenValidator.AssignLocsValidator.RULE_LENGTH_LOCATION_10:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION, locale), "10");
			
		case ItemScreenValidator.AssignLocsValidator.RULE_LENGTH_REPLENISHMENT_PRIORITY_5:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_REPLENISHMENT_PRIORITY, locale), "5");
			
		case ItemScreenValidator.AssignLocsValidator.RULE_LENGTH_MINIMUM_REPLENISHMENT_10:		
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_REPLENISHMENT_UOM, locale), "10");

			//Attribute Domain Rules
		case ItemScreenValidator.AssignLocsValidator.RULE_ATTR_DOM_LOCATION_TYPE:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION_TYPE, locale), alScreen.getLocationType());
			
		case ItemScreenValidator.AssignLocsValidator.RULE_ATTR_DOM_MINIMUM_REPLENISHMENT_UOM:
			return getAttrDomErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_REPLENISHMENT_UOM, locale), alScreen.getMinimumReplenishment());


		}
		return errorMsg;
	}
	
	public static String getErrorMessage(int errorCode, Locale locale, ItemScreenVO itemScreen){
		String errorMsg = new String();
		String param[] = null;
		switch(errorCode){
		case RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CUBE, locale));
			
		case RULE_STORER_MUST_EXIST :
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OWNER, locale), itemScreen.getOwner());
			
		case RULE_PACK_MUST_EXIST :
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PACK, locale), itemScreen.getPack());
			
		case RULE_ITEM_MUST_BE_UNIQUE :
			param = new String[4];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OWNER, locale);
			param[1] = itemScreen.getOwner();
			param[2] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM, locale);
			param[3] = itemScreen.getItem();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_ERROR_DUPLICATE_ITEM, locale, param);
			
		case RULE_ITEM_REFERENCE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_REFERENCE, locale));
			
		case RULE_GROSS_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO :			
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_GROSS_WEIGHT, locale));
			
		case RULE_NET_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NET_WEIGHT, locale));

		case RULE_TARE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TARE_WEIGHT, locale));
			
		case RULE_INBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_SHELF_LIFE, locale));

		case RULE_OUTBOUND_SHELF_LIFE_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_SHELF_LIFE, locale));
			
		case RULE_INBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_AVERAGE_WEIGHT, locale));
			
		case RULE_INBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_AVERAGE_WEIGHT, locale));
			
		case RULE_INBOUND_ALL_LABELS_NOT_EMPTY :
			param = new String[2];
			param[0] = "";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_SERIAL_NUMBER, locale) + " ";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_2, locale) + " ";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_3, locale) + " ";
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_ERROR_CW_LABEL_EMPTY, locale, param);
						
		case RULE_OUTBOUND_AVERAGE_WEIGHT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_AVERAGE_WEIGHT, locale));
			
		case RULE_OUTBOUND_TOLERANCE_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_TOLERANCE, locale));
			
		case RULE_OUTBOUND_ALL_LABELS_NOT_EMPTY :
			param = new String[2];
			param[0] = "";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_SERIAL_NUMBER, locale) + " ";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_2, locale) + " ";
			param[0] += MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_3, locale) + " ";
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_ERROR_CW_LABEL_EMPTY, locale, param);
				
		case RULE_RF_DEFAULT_RECEIVING_PACK_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_PACK, locale));
			
		case RULE_HAZMATCODE_MUST_EXIST :
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HAZMAT_CODE, locale), itemScreen.getHazmatCode());
			
		case RULE_DATE_CODE_DAYS_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_DATE_CODE_DAYS, locale));
			
		case RULE_MAX_PALLETS_PER_ZONE_GREATER_THAN_OR_EQUAL_ZERO :	
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_MAX_PALLETS_PER_ZONE, locale));
			
		case RULE_PUTAWAY_LOCATION_MUST_EXIST :
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_LOCATION, locale), itemScreen.getPutawayLocation());
			
		case RULE_INBOUND_QC_LOCATION_MUST_EXIST :
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_QC_LOCATION, locale), itemScreen.getInboundQCLocation());
			
		case RULE_OUTBOUND_QC_LOCATION_MUST_EXIST :	
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_QC_LOCATION, locale), itemScreen.getOutboundQCLocation());
			
		case RULE_RETURN_LOCATION_MUST_EXIST :	
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RETURN_LOCATION, locale), itemScreen.getReturnLocation());
			
		case RULE_SERIAL_NUMBER_START_GREATER_THAN_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_START, locale));
			
		case RULE_SERIAL_NUMBER_NEXT_GREATER_THAN_SERIAL_NUMBER_START :
			return getField1GreaterThenField2ErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_NEXT, locale), MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_START, locale));
			
		case RULE_SERIAL_NUMBER_END_GREATER_THAN_SERIAL_NUMBER_NEXT :
			return getField1GreaterThenField2ErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_END, locale), MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_NEXT, locale));
			
		case RULE_SERIAL_NUMBER_START_LESS_THAN_SERIAL_NUMBER_MAX :
			return getFieldLessThenValueErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_START, locale), "274877906943");
			
		case RULE_SERIAL_NUMBER_NEXT_LESS_THAN_SERIAL_NUMBER_MAX :
			return getFieldLessThenValueErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_NEXT, locale), "274877906943");
			
		case RULE_SERIAL_NUMBER_END_LESS_THAN_SERIAL_NUMBER_MAX :	
			return getFieldLessThenValueErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_END, locale), "274877906943");
						
		case RULE_SERIAL_NUMBER_START_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_START, locale));
			
		case RULE_SERIAL_NUMBER_NEXT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_NEXT, locale));
			
		case RULE_SERIAL_NUMBER_END_MUST_BE_A_NUMBER :	
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_END, locale));
			
		case RULE_QUANTITY_TO_REORDER_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_QTY_TO_REORDER, locale));
			
		case RULE_QUANTITY_TO_REORDER_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_QTY_TO_REORDER, locale));
			
			
		case RULE_COST_TO_ORDER_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_COST_TO_ORDER, locale));
			
		case RULE_COST_TO_ORDER_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_COST_TO_ORDER, locale));
			
		case RULE_REORDER_POINT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_REORDER_POINT, locale));
			
		case RULE_REORDER_POINT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_REORDER_POINT, locale));
			
		case RULE_RETAIL_PRICE_PER_UNIT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RETAIL_PRICE_PER_UNIT, locale));
			
		case RULE_RETAIL_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RETAIL_PRICE_PER_UNIT, locale));
			
		case RULE_PURCHASE_PRICE_PER_UNIT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PURCHASE_PRICE_PER_UNIT, locale));
			
		case RULE_PURCHASE_PRICE_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PURCHASE_PRICE_PER_UNIT, locale));
			
		case RULE_CARRYING_PER_UNIT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARRYING_PER_UNIT, locale));
			
		case RULE_CARRYING_PER_UNIT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARRYING_PER_UNIT, locale));
			
		case RULE_CUBE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CUBE, locale));
			
		case RULE_GROSS_WEIGHT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_GROSS_WEIGHT, locale));
			
		case RULE_NET_WEIGHT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NET_WEIGHT, locale));
			
		case RULE_TARE_WEIGHT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TARE_WEIGHT, locale));
			
		case RULE_INBOUND_SHELF_LIFE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_SHELF_LIFE, locale));
			
		case RULE_OUTBOUND_SHELF_LIFE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_SHELF_LIFE, locale));
			
		case RULE_INBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_AVERAGE_WEIGHT, locale));
			
		case RULE_INBOUND_TOLERANCE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_TOLERANCE, locale));
			
		case RULE_OUTBOUND_AVERAGE_WEIGHT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_AVERAGE_WEIGHT, locale));
			
		case RULE_OUTBOUND_TOLERANCE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_TOLERANCE, locale));
			
		case RULE_DATE_CODE_DAYS_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_DATE_CODE_DAYS, locale));
			
		case RULE_MAX_PALLETS_PER_ZONE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_MAX_PALLETS_PER_ZONE, locale));
			
		//Required Field Rules
		case RULE_STORER_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OWNER, locale));
			
		case RULE_ITEM_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM, locale));
			
		case RULE_PACK_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PACK, locale));
			
		case RULE_CUBE_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CUBE, locale));
			
		case RULE_GROSS_WEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_GROSS_WEIGHT, locale));
			
		case RULE_NET_WEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NET_WEIGHT, locale));
			
		case RULE_TARE_WEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TARE_WEIGHT, locale));
			
		case RULE_CLASS_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CLASS, locale));
			
		case RULE_LOTTABLE01_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE01, locale));
			
		case RULE_LOTTABLE02_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE02, locale));
			
		case RULE_LOTTABLE03_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE03, locale));
			
		case RULE_LOTTABLE04_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE04, locale));
			
		case RULE_LOTTABLE05_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE05, locale));
			
		case RULE_LOTTABLE06_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE06, locale));
			
		case RULE_LOTTABLE07_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE07, locale));
			
		case RULE_LOTTABLE08_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE08, locale));
			
		case RULE_LOTTABLE09_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE09, locale));
			
		case RULE_LOTTABLE10_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE10, locale));
		
		//Field Length Rules
		case RULE_LENGTH_STORER_15 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OWNER, locale), "15");
			
		case RULE_LENGTH_ITEM_50 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM, locale), "50");
			
		case RULE_LENGTH_DESCRIPTION_60 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_DESCRIPTION, locale), "60");
			
		case RULE_LENGTH_PACK_50 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PACK, locale), "50");
			
		case RULE_LENGTH_CARTON_GROUP_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARTON_GROUP, locale), "10");
			
		case RULE_LENGTH_TARIFF_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TARIFF, locale), "10");
			
		case RULE_LENGTH_ITEM_REFERENCE_8 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_REFERENCE, locale), "8");
			
		case RULE_LENGTH_SHELF_LIFE_CODE_TYPE_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SHELF_LIFE_CODE_TYPE, locale), "1");
			
		case RULE_LENGTH_CATCH_WEIGHT_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_WEIGHT, locale), "10");
			
		case RULE_LENGTH_SHELF_LIFE_INDICATOR_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_SHELF_LIFE, locale), "10");
			
		case RULE_LENGTH_LOTTABLE_VALIDATION_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE_VALIDATION, locale), "10");
			
		case RULE_LENGTH_RF_DEFAULT_RECEIVING_PACK_50 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_PACK, locale), "50");
			
		case RULE_LENGTH_ON_RECEIPT_COPY_PACK_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ON_RECEIPT_COPY_PACK, locale), "10");
			
		case RULE_LENGTH_RF_DEFAULT_RECEIVING_UOM_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_UOM, locale), "10");
			
		case RULE_LENGTH_ITEM_GROUP_1_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_GROUP_1, locale), "10");
			
		case RULE_LENGTH_ITEM_GROUP_2_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_GROUP_2, locale), "30");
			
		case RULE_LENGTH_HAZMAT_CODE_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HAZMAT_CODE, locale), "10");
			
		case RULE_LENGTH_SHIPPABLE_CONTAINER_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SHIPPABLE_CONTAINER, locale), "10");
			
		case RULE_LENGTH_VERTICAL_STORAGE_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_VERTICAL_STORAGE, locale), "1");
			
		case RULE_LENGTH_TRANSPORTATION_MODE_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TRANSPORTATION_MODE, locale), "30");
			
		case RULE_LENGTH_FREIGHT_CLASS_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_FREIGHT_CLASS, locale), "10");
			
		case RULE_LENGTH_CLASS_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CLASS, locale), "10");
			
		case RULE_LENGTH_LOTTABLE01_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE01, locale), "20");
			
		case RULE_LENGTH_LOTTABLE02_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE02, locale), "20");
			
		case RULE_LENGTH_LOTTABLE03_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE03, locale), "20");
			
		case RULE_LENGTH_LOTTABLE04_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE04, locale), "20");
			
		case RULE_LENGTH_LOTTABLE05_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE05, locale), "20");
			
		case RULE_LENGTH_LOTTABLE06_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE06, locale), "20");
			
		case RULE_LENGTH_LOTTABLE07_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE07, locale), "20");
			
		case RULE_LENGTH_LOTTABLE08_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE08, locale), "20");
			
		case RULE_LENGTH_LOTTABLE09_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE09, locale), "20");
			
		case RULE_LENGTH_LOTTABLE10_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE10, locale), "20");
			
		case RULE_LENGTH_BARCODE01_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE01, locale), "30");
			
		case RULE_LENGTH_BARCODE02_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE02, locale), "30");
			
		case RULE_LENGTH_BARCODE03_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE03, locale), "30");
			
		case RULE_LENGTH_BARCODE04_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE04, locale), "30");
			
		case RULE_LENGTH_BARCODE05_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE05, locale), "30");
			
		case RULE_LENGTH_BARCODE06_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE06, locale), "30");
			
		case RULE_LENGTH_BARCODE07_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE07, locale), "30");
			
		case RULE_LENGTH_BARCODE08_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE08, locale), "30");
			
		case RULE_LENGTH_BARCODE09_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE09, locale), "30");
			
		case RULE_LENGTH_BARCODE10_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BARCODE010, locale), "30");
			
		case RULE_LENGTH_INBOUND_CATCH_WEIGHT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_WEIGHT, locale), "1");
			
		case RULE_LENGTH_INBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_NO_ENTRY_OF_TOTAL_WEIGHT, locale), "1");
			
		case RULE_LENGTH_INBOUND_CATCH_WEIGHT_BY_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_WEIGHT_BY, locale), "1");			
			
		case RULE_LENGTH_INBOUND_CATCH_DATA_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_DATA, locale), "1");
			
		case RULE_LENGTH_INBOUND_SERIAL_NUMBER_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_SERIAL_NUMBER, locale), "5");
			
		case RULE_LENGTH_INBOUND_OTHER_2_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_2, locale), "5");
			
		case RULE_LENGTH_INBOUND_OTHER_3_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_3, locale), "5");
			
		case RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_WEIGHT, locale), "1");
			
		case RULE_LENGTH_OUTBOUND_NO_ENTRY_OF_TOTAL_WEIGHT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_NO_ENTRY_OF_TOTAL_WEIGHT, locale), "1");
			
		case RULE_LENGTH_OUTBOUND_CATCH_WEIGHT_BY_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_WEIGHT_BY, locale), "1");
			
		case RULE_LENGTH_ALLOW_CUSTOMER_OVERRIDE_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ALLOW_CUSTOMER_OVERRIDE, locale), "1");
			
		case RULE_LENGTH_OUTBOUND_SERIAL_NUMBER_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_SERIAL_NUMBER, locale), "5");
			
		case RULE_LENGTH_OUTBOUND_OTHER_2_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_2, locale), "5");
			
		case RULE_LENGTH_OUTBOUND_OTHER_3_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_3, locale), "5");
			
		case RULE_LENGTH_CATCH_WHEN_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_WHEN, locale), "10");
			
		case RULE_LENGTH_CATCH_QUANTITY_1_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY1, locale), "10");
			
		case RULE_LENGTH_CATCH_QUANTITY_2_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY2, locale), "10");
			
		case RULE_LENGTH_CATCH_QUANTITY_3_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY3, locale), "10");
			
		case RULE_LENGTH_HOLD_CODE_ON_RF_RECEIPT_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOLD_CODE_ON_RF_RECEIPT, locale), "10");
		
		case RULE_LENGTH_ITEM_TYPE_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_TYPE, locale), "1");
			
		case RULE_LENGTH_RECEIPT_VALIDATION_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RECEIPT_VALIDATION, locale), "18");
			
		case RULE_LENGTH_MANUAL_SETUP_REQUIRED_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_MANUAL_SETUP_REQUIRED, locale), "1");
			
		case RULE_LENGTH_PUTAWAY_ZONE_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_ZONE, locale), "10");
			
		case RULE_LENGTH_PUTAWAY_LOCATION_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_LOCATION, locale), "10");
			
		case RULE_LENGTH_INBOUND_QC_LOCATION_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_QC_LOCATION, locale), "10");
			
		case RULE_LENGTH_OUTBOUND_QC_LOCATION_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_QC_LOCATION, locale), "10");
			
		case RULE_LENGTH_RETURN_LOCATION_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RETURN_LOCATION, locale), "10");
			
		case RULE_LENGTH_PUTAWAY_STRATEGY_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_STATEGY, locale), "10");
			
		case RULE_LENGTH_STRATEGY_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STATEGY, locale), "10");
			
		case RULE_LENGTH_ROTATION_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ROTATION, locale), "1");
			
		case RULE_LENGTH_ROTATE_BY_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ROTATE_BY, locale), "10");
			
		case RULE_LENGTH_OPPERTUNISTIC_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OPPORTUNISTIC, locale), "1");
			
		case RULE_LENGTH_CONVEYABLE_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CONVEYABLE, locale), "1");
			
		case RULE_LENGTH_VERIFY_LOTTABLE_4_5_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_VERIFY_LOTTABLE_4_5, locale), "1");
			
		case RULE_LENGTH_BULK_CARTON_GROUP_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BULK_CARTON_GROUP, locale), "10");
			
		case RULE_LENGTH_ALLOW_CONSOLIDATION_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ALLOW_CONSOLIDATION, locale), "1");
			
		case RULE_LENGTH_CYCLE_CLASS_5 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CYCLE_CLASS, locale), "5");
			
		case RULE_LENGTH_CC_DISCREPANCY_HANDLING_RULE_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CC_DESCREPENCY_HANDLING_RULE, locale), "10");
			
		case RULE_LENGTH_UDF_1_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF1, locale), "18");
			
		case RULE_LENGTH_UDF_2_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF2, locale), "18");
			
		case RULE_LENGTH_UDF_3_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF3, locale), "18");
			
		case RULE_LENGTH_UDF_4_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF4, locale), "18");
			
		case RULE_LENGTH_UDF_5_18 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF5, locale), "18");
			
		case RULE_LENGTH_UDF_6_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF6, locale), "30");
			
		case RULE_LENGTH_UDF_7_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF7, locale), "30");
			
		case RULE_LENGTH_UDF_8_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF8, locale), "30");
			
		case RULE_LENGTH_UDF_9_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF9, locale), "30");
			
		case RULE_LENGTH_UDF_10_30 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_UDF10, locale), "30");
			
		case RULE_LENGTH_PICKING_INSTRUCTIONS_2147483647 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_PICKING_INSTRUCTION, locale), "2147483647");
			
		case RULE_LENGTH_NOTES_2147483647 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_NOTES, locale), "2147483647");
		
		//SRG -- Catch Weight Capture -- Start
//		case RULE_LENGTH_ENABLE_ADV_CWGT_1 :
//			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ENABLEADVCWGT, locale), "1");
		case RULE_LENGTH_CATCH_GROSS_WGT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHGROSSWGT, locale), "1");
		case RULE_LENGTH_CATCH_NET_WGT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHNETWGT, locale), "1");
		case RULE_LENGTH_CATCH_TARE_WGT_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHTAREWGT, locale), "1");
		case RULE_LENGTH_ZERO_DEFAULT_WGT_FOR_PICK_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ZERODEFAULTWGTFORPICK, locale), "1");
//		case RULE_LENGTH_ADV_CWT_TRACK_BY_1 :
//			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ADVCWTTRACKBY, locale), "1");
		case RULE_LENGTH_STD_UOM_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDUOM, locale), "10");
//		case RULE_ENABLE_ADV_CWGT_MUST_BE_A_NUMBER :
//			return getNonNumericErrorMessage(errorCode, locale, 
//					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ENABLEADVCWGT, locale));
		case RULE_CATCH_GROSS_WGT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHGROSSWGT, locale));
		case RULE_CATCH_NET_WGT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHNETWGT, locale));
		case RULE_CATCH_TARE_WGT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHTAREWGT, locale));
		case RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ZERODEFAULTWGTFORPICK, locale));
//		case RULE_ADV_CWT_TRACK_BY_MUST_BE_A_NUMBER :
//			return getNonNumericErrorMessage(errorCode, locale, 
//					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ADVCWTTRACKBY, locale));
		case RULE_TARE_WGT1_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TAREWGT1, locale));
		case RULE_STD_NET_WGT1_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDNETWGT1, locale));
		case RULE_STD_GROSS_WGT1_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDGROSSWGT1, locale));
//		case RULE_ENABLE_ADV_CWGT_MUST_BE_ZERO_OR_ONE :
//			return getZeroOrOneErrorMessage(errorCode, locale, 
//					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ENABLEADVCWGT, locale),
//					itemScreen.getEnableadvcwgt());
		case RULE_CATCH_GROSS_WGT_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHGROSSWGT, locale),
					itemScreen.getCatchgrosswgt());
		case RULE_CATCH_NET_WGT_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHNETWGT, locale),
					itemScreen.getCatchnetwgt());
		case RULE_CATCH_TARE_WGT_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCHTAREWGT, locale),
					itemScreen.getCatchtarewgt());
		case RULE_ZERO_DEFAULT_WGT_FOR_PICK_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ZERODEFAULTWGTFORPICK, locale),
					itemScreen.getZerodefaultwgtforpick());
//		case RULE_ADV_CWT_TRACK_BY_GREATER_THAN_ZERO :
//			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ADVCWTTRACKBY, locale));
		case RULE_TARE_WGT1_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TAREWGT1, locale));
		case RULE_STD_NET_WGT1_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDNETWGT1, locale));
		case RULE_STD_GROSS_WGT1_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDGROSSWGT1, locale));
		//SRG -- Catch Weight Capture -- End
		
		//Attribute Domain Rules
		case RULE_ATTR_DOM_TARIFF :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TARIFF, locale), itemScreen.getTariff());
			
		case RULE_ATTR_DOM_CARTON_GROUP :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARTON_GROUP, locale), itemScreen.getCartonGroup());
			
		case RULE_ATTR_DOM_SHELF_LIFE_CODE_TYPE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SHELF_LIFE_CODE_TYPE, locale), itemScreen.getShelfLifeCodeType());
			
		case RULE_ATTR_DOM_LOTTABLE_VALIDATION :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE_VALIDATION, locale), itemScreen.getLottableValidation());
			
		case RULE_ATTR_DOM_ON_RECEIPT_COPY_PACK :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ON_RECEIPT_COPY_PACK, locale), itemScreen.getOnReceiptCopyPack());
			
		case RULE_ATTR_DOM_RF_DEFAULT_RECEIVING_UOM :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_UOM, locale), itemScreen.getRfDefaultReceivingUOM());
			
		case RULE_ATTR_DOM_TRANSPORTATION_MODE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TRANSPORTATION_MODE, locale), itemScreen.getTransportationMode());
			
		case RULE_ATTR_DOM_FREIGHT_CLASS :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_FREIGHT_CLASS, locale), itemScreen.getFreightClass());
			
		case RULE_ATTR_DOM_INBOUND_CATCH_WEIGHT_BY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_WEIGHT_BY, locale), itemScreen.getInboundCatchWeightBy());
			
		case RULE_ATTR_DOM_OUTBOUND_CATCH_WEIGHT_BY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_WEIGHT_BY, locale), itemScreen.getOutboundCatchWeightBy());
			
		case RULE_ATTR_DOM_CATCH_WHEN :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_WHEN, locale), itemScreen.getCatchWhen());
			
		case RULE_ATTR_DOM_CATCH_QUANTITY_1 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY1, locale), itemScreen.getCatchQuantity1());
			
		case RULE_ATTR_DOM_CATCH_QUANTITY_2 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY2, locale), itemScreen.getCatchQuantity2());
			
		case RULE_ATTR_DOM_CATCH_QUANTITY_3 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CATCH_QTY3, locale), itemScreen.getCatchQuantity3());
			
		case RULE_ATTR_DOM_HOLD_CODE_ON_RF_RECEIPT :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOLD_CODE_ON_RF_RECEIPT, locale), itemScreen.getHoldCodeOnRFReceipt());
			
		case RULE_ATTR_DOM_ITEM_TYPE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ITEM_TYPE, locale), itemScreen.getItemType());
			
		case RULE_ATTR_DOM_RECEIPT_VALIDATION :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RECEIPT_VALIDATION, locale), itemScreen.getReceiptValidation());
			
		case RULE_ATTR_DOM_PUTAWAY_ZONE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_ZONE, locale), itemScreen.getPutawayZone());
			
		case RULE_ATTR_DOM_PUTAWAY_STRATEGY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAY_STATEGY, locale), itemScreen.getPutawayStrategy());
			
		case RULE_ATTR_DOM_STRATEGY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STATEGY, locale), itemScreen.getStrategy());
			
		case RULE_ATTR_DOM_ROTATION :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ROTATION, locale), itemScreen.getRotation());

		case RULE_ATTR_DOM_DAPICKSORT :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_DAPICKSORT, locale), itemScreen.getDapicksort());

		case RULE_ATTR_DOM_RPLNSORT :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RPLNSORT, locale), itemScreen.getRplnsort());	
			
		case RULE_ATTR_DOM_ROTATE_BY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ROTATE_BY, locale), itemScreen.getRotateBy());
			
		case RULE_ATTR_DOM_BULK_CARTON_GROUP :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_BULK_CARTON_GROUP, locale), itemScreen.getBulkCartonGroup());
			
		case RULE_ATTR_DOM_CYCLE_CLASS :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CYCLE_CLASS, locale), itemScreen.getCycleClass());
			
		case RULE_ATTR_DOM_CC_DESCREPANCY_HANDLING_RULE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CC_DESCREPENCY_HANDLING_RULE, locale), itemScreen.getCcDiscrepancyHandlingRule());
			
		case RULE_ATTR_DOM_VOICEGROUPINGID:
			return getAttrDomErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_VOICEGROUPINGID, locale), 
					itemScreen.getVoicegroupingid());
		
		case RULE_ATTR_DOM_CARTONIZEFT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARTONIZEFT, locale), itemScreen.getCartonizeft());
			
		case RULE_SNUM_DELIM_COUNT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_DELIM_COUNT, locale));

		case RULE_SNUM_POSITION_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_POSITION, locale));

		case RULE_SNUM_DELIM_COUNT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_DELIM_COUNT, locale));
			
		case RULE_SNUM_POSITION_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_POSITION, locale));

		case RULE_SNUM_DELIM_AND_SNUM_POSITION_CONFLICT :
			param = new String[4];
			param[0] = itemScreen.getSnum_position();
			param[1] = itemScreen.getSnum_delimiter();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_SNUM_POSITION_AND_SNUM_DELIMETER_CONFLICT, locale, param);
			
			

		case RULE_SNUM_MASK_VALIDATION :
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_ERROR_SNUM_MASK_INVALID_FORMAT, locale, new String[0]);
			
		case RULE_SNUM_AUTOINCREMENT_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_AUTOINCREMENT, locale),
					itemScreen.getSnum_autoincrement());
			
		case RULE_SNUM_QUANTITY_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_QUANTITY, locale));

		case RULE_SNUM_QUANTITY_GREATER_THAN_ZERO_WHEN_AUTOINCREMENT_ON :
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ITEM_SCREEN_ERROR_SNUM_QUANTITY_GREATER_ZERO_WHEN_AUTOINCREMENT_ON, locale);
			
		case RULE_SNUM_INCR_LENGTH_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_INCR_LENGTH, locale));

		case RULE_SNUM_INCR_POS_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_INCR_POS, locale));

		case RULE_SNUMLONG_FIXED_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUMLONG_FIXED, locale));

		case RULE_SNUM_LENGTH_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SNUM_LENGTH, locale));

		case RULE_SNUMLONG_FIXED_MUST_BE_GREATER_THAN_SNUM_LENGTH:
			return MessageUtil.getResourceBundleMessage(
					ResourceConstants.KEY_ITEM_SCREEN_ERROR_SNUMLONG_FIXED_MUST_BE_GREATER_THAN_SNUM_LENGTH, locale);

		case RULE_LENGTH_LOTTABLE11_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE11, locale), "20");

		case RULE_LENGTH_LOTTABLE12_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE12, locale), "20");
		
		case RULE_LOTTABLE11_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE11, locale));
		
		case RULE_LOTTABLE12_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTTABLE12, locale));

		case RULE_TOEXPIREDAYS_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TOEXPIREDAYS, locale));

		case RULE_TODELIVERBYDAYS_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TODELIVERBYDAYS, locale));
		
		case RULE_TOBESTBYDAYS_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TOBESTBYDAYS, locale));

		case RULE_TOEXPIREDAYS_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TOEXPIREDAYS, locale));

		case RULE_TODELIVERBYDAYS_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TODELIVERBYDAYS, locale));

		case RULE_TOBESTBYDAYS_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TOBESTBYDAYS, locale));

		case RULE_NONSTOCKEDINDICATOR_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NONSTOCKEDINDICATOR, locale));

		case RULE_NONSTOCKEDINDICATOR_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NONSTOCKEDINDICATOR, locale),
					itemScreen.getNonstockedindicator());

		case RULE_ICD1UNIQUE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ICD1UNIQUE, locale));

		case RULE_ICD1UNIQUE_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_ICD1UNIQUE, locale),
					itemScreen.getIcd1unique());

		case RULE_OCD1UNIQUE_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OCD1UNIQUE, locale));

		case RULE_OCD1UNIQUE_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OCD1UNIQUE, locale),
					itemScreen.getOcd1unique());

		case RULE_CWFLAG_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CWFLAG, locale));

		case RULE_CWFLAG_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CWFLAG, locale),
					itemScreen.getCwflag());
			
		//SRG: 9.2 Upgrade -- Start
		case RULE_LENGTH_NMFCCLASS_40 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_NMFCCLASS, locale), "40");
			
		case RULE_LENGTH_MATEABILITYCODE_64 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_MATEABILITYCODE, locale), "64");
			
		case RULE_LENGTH_RECURCODE_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_RECURCODE, locale), "10");
			
		case RULE_LENGTH_WGTUOM_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_WGTUOM, locale), "20");
			
		case RULE_LENGTH_DIMENUOM_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_DIMENUOM, locale), "20");
			
		case RULE_LENGTH_CUBEUOM_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CUBEUOM, locale), "20");
			
		case RULE_LENGTH_STORAGETYPE_20 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STORAGETYPE, locale), "20");
			
		case RULE_HOURS_TO_HOLD_LPN_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLPN, locale));
			
		case RULE_HOURS_TO_HOLD_LOT_MUST_BE_A_NUMBER :
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLOT, locale));
			
		case RULE_HOURS_TO_HOLD_LPN_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLPN, locale));
			
		case RULE_HOURS_TO_HOLD_LOT_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLOT, locale));
			
		case RULE_TEMP_FOR_ASN_MUST_BE_Y_OR_N :
			return getYesOrNoErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_TEMPFORASN, locale));
			
		case RULE_IBSUMCWFLG_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_IBSUMCWFLG, locale),
					itemScreen.getIbsumcwflg());
			
		case RULE_OBSUMCWFLG_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_OBSUMCWFLG, locale),
					itemScreen.getObsumcwflg());
			
		case RULE_SHOWRFCWONTRANS_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_SHOWRFCWONTRANS, locale),
					itemScreen.getShowrfcwontrans());
			
		case RULE_CARTONIZEFT_MUST_BE_ZERO_OR_ONE :
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_CARTONIZEFT, locale),
					itemScreen.getCartonizeft());
			
		case RULE_ATTR_DOM_FILL_QTY_UOM :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_FILLQTYUOM, locale), itemScreen.getFillqtyuom());
			
		case RULE_ATTR_DOM_AUTO_RELEASE_LPN_BY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_AUTORELEASELPNBY, locale), itemScreen.getAutoreleaselpnby());
			
		case RULE_ATTR_DOM_AUTO_RELEASE_LOT_BY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_AUTORELEASELOTBY, locale), itemScreen.getAutoreleaselotby());
		
		case RULE_ATTR_DOM_PUTAWAY_CLASS :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_PUTAWAYCLASS, locale), itemScreen.getPutawayclass());
		
		case RULE_ATTR_DOM_AMSTRATEGY_KEY :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_AMSTRATEGYKEY, locale), itemScreen.getAmstrategykey());
			
		case RULE_ATTR_DOM_LOT_HOLD_CODE :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_LOTHOLDCODE, locale), itemScreen.getLotholdcode());
			
		case RULE_ATTR_DOM_STD_UOM :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ITEM_FIELD_STDUOM, locale), itemScreen.getStduom());
		//SRG: 9.2 Upgrade -- End

		}	
		
		return errorMsg;
	}
}


