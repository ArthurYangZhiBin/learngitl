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
package com.infor.scm.wms.util.validation.screen.pack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.driver.DataLayerColumnList;
import com.infor.scm.wms.util.datalayer.driver.DataLayerQueryData;
import com.infor.scm.wms.util.datalayer.driver.DataLayerTableList;
import com.infor.scm.wms.util.datalayer.query.AltSkuQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.HazmatQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PackQueryRunner;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class PackScreenValidator extends BaseScreenValidator {

	public PackScreenValidator(WMSValidationContext context){
		super(context);
	}
	
	
	public static final int RULE_CASECNT_MUST_BE_A_NUMBER = 0;
	public static final int RULE_CUBE_MUST_BE_A_NUMBER = 1;
	public static final int RULE_CUBEUOM1_MUST_BE_A_NUMBER = 2;
	public static final int RULE_CUBEUOM2_MUST_BE_A_NUMBER = 3;
	public static final int RULE_CUBEUOM3_MUST_BE_A_NUMBER = 4;
	public static final int RULE_CUBEUOM4_MUST_BE_A_NUMBER = 5;
	public static final int RULE_FilterValueUOM1_MUST_BE_A_NUMBER = 6;
	public static final int RULE_FilterValueUOM2_MUST_BE_A_NUMBER = 7;
	public static final int RULE_FilterValueUOM3_MUST_BE_A_NUMBER = 8;
	public static final int RULE_FilterValueUOM4_MUST_BE_A_NUMBER = 9;
	public static final int RULE_FilterValueUOM5_MUST_BE_A_NUMBER = 10;
	public static final int RULE_FilterValueUOM6_MUST_BE_A_NUMBER = 11;
	public static final int RULE_FilterValueUOM7_MUST_BE_A_NUMBER = 12;
	public static final int RULE_FilterValueUOM8_MUST_BE_A_NUMBER = 13;
	public static final int RULE_FilterValueUOM9_MUST_BE_A_NUMBER = 14;
	public static final int RULE_GROSSWGT_MUST_BE_A_NUMBER = 15;
	public static final int RULE_HEIGHTUOM1_MUST_BE_A_NUMBER = 16;
	public static final int RULE_HEIGHTUOM2_MUST_BE_A_NUMBER = 17;
	public static final int RULE_HEIGHTUOM3_MUST_BE_A_NUMBER = 18;
	public static final int RULE_HEIGHTUOM4_MUST_BE_A_NUMBER = 19;
	public static final int RULE_HEIGHTUOM8_MUST_BE_A_NUMBER = 20;
	public static final int RULE_HEIGHTUOM9_MUST_BE_A_NUMBER = 21;
	public static final int RULE_IndicatorDigitUOM1_MUST_BE_A_NUMBER = 22;
	public static final int RULE_IndicatorDigitUOM2_MUST_BE_A_NUMBER = 23;
	public static final int RULE_IndicatorDigitUOM3_MUST_BE_A_NUMBER = 24;
	public static final int RULE_IndicatorDigitUOM4_MUST_BE_A_NUMBER = 25;
	public static final int RULE_IndicatorDigitUOM5_MUST_BE_A_NUMBER = 26;
	public static final int RULE_IndicatorDigitUOM6_MUST_BE_A_NUMBER = 27;
	public static final int RULE_IndicatorDigitUOM7_MUST_BE_A_NUMBER = 28;
	public static final int RULE_IndicatorDigitUOM8_MUST_BE_A_NUMBER = 29;
	public static final int RULE_IndicatorDigitUOM9_MUST_BE_A_NUMBER = 30;
	public static final int RULE_INNERPACK_MUST_BE_A_NUMBER = 31;
	public static final int RULE_LENGTHUOM1_MUST_BE_A_NUMBER = 32;
	public static final int RULE_LENGTHUOM2_MUST_BE_A_NUMBER = 33;
	public static final int RULE_LENGTHUOM3_MUST_BE_A_NUMBER = 34;
	public static final int RULE_LENGTHUOM4_MUST_BE_A_NUMBER = 35;
	public static final int RULE_LENGTHUOM8_MUST_BE_A_NUMBER = 36;
	public static final int RULE_LENGTHUOM9_MUST_BE_A_NUMBER = 37;
	public static final int RULE_NETWGT_MUST_BE_A_NUMBER = 38;
	public static final int RULE_OTHERUNIT1_MUST_BE_A_NUMBER = 39;
	public static final int RULE_OTHERUNIT2_MUST_BE_A_NUMBER = 40;
	public static final int RULE_PALLET_MUST_BE_A_NUMBER = 41;
	public static final int RULE_PALLETHI_MUST_BE_A_NUMBER = 42;
	public static final int RULE_PALLETTI_MUST_BE_A_NUMBER = 43;
	public static final int RULE_PALLETWOODHEIGHT_MUST_BE_A_NUMBER = 44;
	public static final int RULE_PALLETWOODLENGTH_MUST_BE_A_NUMBER = 45;
	public static final int RULE_PALLETWOODWIDTH_MUST_BE_A_NUMBER = 46;
	public static final int RULE_QTY_MUST_BE_A_NUMBER = 47;
	//public static final int RULE_SERIALKEY_MUST_BE_A_NUMBER = 48;
	public static final int RULE_WIDTHUOM1_MUST_BE_A_NUMBER = 49;
	public static final int RULE_WIDTHUOM2_MUST_BE_A_NUMBER = 50;
	public static final int RULE_WIDTHUOM3_MUST_BE_A_NUMBER = 51;
	public static final int RULE_WIDTHUOM4_MUST_BE_A_NUMBER = 52;
	public static final int RULE_WIDTHUOM8_MUST_BE_A_NUMBER = 53;
	public static final int RULE_WIDTHUOM9_MUST_BE_A_NUMBER = 54;
	
	public static final int RULE_PACKKEY_MUST_EXIST = 55;
	public static final int RULE_PACKKEY_MUST_BE_UNIQUE = 56;
	public static final int RULE_QTY_GREATER_THAN_OR_EQUAL_ZERO = 57;
	public static final int RULE_CASECNT_GREATER_THAN_OR_EQUAL_ZERO = 58;
	public static final int RULE_INNERPACK_GREATER_THAN_OR_EQUAL_ZERO = 59;
	public static final int RULE_PALLETTI_GREATER_THAN_OR_EQUAL_ZERO = 60;
	public static final int RULE_PALLETHI_GREATER_THAN_OR_EQUAL_ZERO = 61;
	public static final int RULE_PALLETWOODLENGTH_GREATER_THAN_OR_EQUAL_ZERO = 62;
	public static final int RULE_PALLETWOODHEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 63;
	public static final int RULE_PALLETWOODWIDTH_GREATER_THAN_OR_EQUAL_ZERO = 64;
	
		
	
	//Required Field Rules
	public static final int RULE_PACKKEY_NOT_EMPTY = 65;
	
	public static final int RULE_LENGTHUOM1_NOT_EMPTY = 66;
	public static final int RULE_LENGTHUOM2_NOT_EMPTY = 67;
	public static final int RULE_LENGTHUOM3_NOT_EMPTY = 68;
	public static final int RULE_LENGTHUOM4_NOT_EMPTY = 69;
	public static final int RULE_LENGTHUOM8_NOT_EMPTY = 70;
	public static final int RULE_LENGTHUOM9_NOT_EMPTY = 71;
	
	public static final int RULE_WIDTHUOM1_NOT_EMPTY = 72;
	public static final int RULE_WIDTHUOM2_NOT_EMPTY = 73;
	public static final int RULE_WIDTHUOM3_NOT_EMPTY = 74;
	public static final int RULE_WIDTHUOM4_NOT_EMPTY = 75;
	public static final int RULE_WIDTHUOM8_NOT_EMPTY = 76;
	public static final int RULE_WIDTHUOM9_NOT_EMPTY = 77;
	
	public static final int RULE_HEIGHTUOM1_NOT_EMPTY = 78;
	public static final int RULE_HEIGHTUOM2_NOT_EMPTY = 79;
	public static final int RULE_HEIGHTUOM3_NOT_EMPTY = 80;
	public static final int RULE_HEIGHTUOM4_NOT_EMPTY = 81;
	public static final int RULE_HEIGHTUOM8_NOT_EMPTY = 82;
	public static final int RULE_HEIGHTUOM9_NOT_EMPTY = 83;
	
	public static final int RULE_PALLETTI_NOT_EMPTY = 84;
	public static final int RULE_PALLETHI_NOT_EMPTY = 85;
	public static final int RULE_PALLETWOODLENGTH_NOT_EMPTY = 86;
	public static final int RULE_PALLETWOODHEIGHT_NOT_EMPTY = 87;
	public static final int RULE_PALLETWOODWIDTH_NOT_EMPTY = 88;
	
	public static final int RULE_PACKUOM_MUST_BE_UNIQUE = 89;
	public static final int RULE_LENGTHUOM3_GREATER_THAN_OR_EQUAL_ZERO = 90;
	public static final int RULE_HEIGHTUOM3_GREATER_THAN_OR_EQUAL_ZERO = 91;
	public static final int RULE_WIDTHUOM3_GREATER_THAN_OR_EQUAL_ZERO = 92;
	public static final int RULE_FILTERVALUEUOM3_BETWEEN_0_AND_7 = 93;
	public static final int RULE_INDICATORDIGITUOM3_BETWEEN_0_AND_8 = 94;
	
	public static final int RULE_LENGTHUOM1_GREATER_THAN_OR_EQUAL_ZERO = 95;
	public static final int RULE_HEIGHTUOM1_GREATER_THAN_OR_EQUAL_ZERO = 96;
	public static final int RULE_WIDTHUOM1_GREATER_THAN_OR_EQUAL_ZERO = 97;
	public static final int RULE_FILTERVALUEUOM1_BETWEEN_0_AND_7 = 98;
	public static final int RULE_INDICATORDIGITUOM1_BETWEEN_0_AND_8 = 99;
	
	public static final int RULE_LENGTHUOM2_GREATER_THAN_OR_EQUAL_ZERO = 100;
	public static final int RULE_HEIGHTUOM2_GREATER_THAN_OR_EQUAL_ZERO = 101;
	public static final int RULE_WIDTHUOM2_GREATER_THAN_OR_EQUAL_ZERO = 102;
	public static final int RULE_FILTERVALUEUOM2_BETWEEN_0_AND_7 = 103;
	public static final int RULE_INDICATORDIGITUOM2_BETWEEN_0_AND_8 = 104;
	
	public static final int RULE_LENGTHUOM4_GREATER_THAN_OR_EQUAL_ZERO = 105;
	public static final int RULE_HEIGHTUOM4_GREATER_THAN_OR_EQUAL_ZERO = 106;
	public static final int RULE_WIDTHUOM4_GREATER_THAN_OR_EQUAL_ZERO = 107;
	public static final int RULE_FILTERVALUEUOM4_BETWEEN_0_AND_7 = 108;
	public static final int RULE_INDICATORDIGITUOM4_BETWEEN_0_AND_8 = 109;

	public static final int RULE_LENGTHUOM8_GREATER_THAN_OR_EQUAL_ZERO = 110;
	public static final int RULE_HEIGHTUOM8_GREATER_THAN_OR_EQUAL_ZERO = 111;
	public static final int RULE_WIDTHUOM8_GREATER_THAN_OR_EQUAL_ZERO = 112;
	public static final int RULE_FILTERVALUEUOM8_BETWEEN_0_AND_7 = 113;
	public static final int RULE_INDICATORDIGITUOM8_BETWEEN_0_AND_8 = 114;
	
	public static final int RULE_LENGTHUOM9_GREATER_THAN_OR_EQUAL_ZERO = 115;
	public static final int RULE_HEIGHTUOM9_GREATER_THAN_OR_EQUAL_ZERO = 116;
	public static final int RULE_WIDTHUOM9_GREATER_THAN_OR_EQUAL_ZERO = 117;
	public static final int RULE_FILTERVALUEUOM9_BETWEEN_0_AND_7 = 118;
	public static final int RULE_INDICATORDIGITUOM9_BETWEEN_0_AND_8 = 119;

	
	public static final int RULE_FILTERVALUEUOM5_BETWEEN_0_AND_7 = 120;
	public static final int RULE_INDICATORDIGITUOM5_BETWEEN_0_AND_8 = 121;
	
	public static final int RULE_FILTERVALUEUOM6_BETWEEN_0_AND_7 = 122;
	public static final int RULE_INDICATORDIGITUOM6_BETWEEN_0_AND_8 = 123;
	
	public static final int RULE_FILTERVALUEUOM7_BETWEEN_0_AND_7 = 124;
	public static final int RULE_INDICATORDIGITUOM7_BETWEEN_0_AND_8 = 125;
	

	
	
	public static final int RULE_CUBE_NOT_EMPTY = 126;
	public static final int RULE_GROSS_WEIGHT_NOT_EMPTY = 127;
	public static final int RULE_NET_WEIGHT_NOT_EMPTY = 128;
	
	//Field Length Rules
	private static final int RULE_LENGTH_CARTONIZEUOM1_1 = 129;
	private static final int RULE_LENGTH_CARTONIZEUOM2_1 = 130;
	private static final int RULE_LENGTH_CARTONIZEUOM3_1 = 131;
	private static final int RULE_LENGTH_CARTONIZEUOM4_1 = 132;
	private static final int RULE_LENGTH_CARTONIZEUOM8_1 = 133;
	private static final int RULE_LENGTH_CARTONIZEUOM9_1 = 134;
	private static final int RULE_LENGTH_ISWHQTY1_1 = 135;
	private static final int RULE_LENGTH_ISWHQTY2_1 = 136;
	private static final int RULE_LENGTH_ISWHQTY3_1 = 137;
	private static final int RULE_LENGTH_ISWHQTY4_1 = 138;
	private static final int RULE_LENGTH_ISWHQTY5_1 = 139;
	private static final int RULE_LENGTH_ISWHQTY6_1 = 140;
	private static final int RULE_LENGTH_ISWHQTY7_1 = 141;
	private static final int RULE_LENGTH_ISWHQTY8_1 = 142;
	private static final int RULE_LENGTH_ISWHQTY9_1 = 143;
	private static final int RULE_LENGTH_PACKDESCR_45 = 144;
	private static final int RULE_LENGTH_PACKKEY_50 = 145;
	private static final int RULE_LENGTH_PACKUOM1_10 = 146;
	private static final int RULE_LENGTH_PACKUOM2_10 = 147;
	private static final int RULE_LENGTH_PACKUOM3_10 = 148;
	private static final int RULE_LENGTH_PACKUOM4_10 = 149;
	private static final int RULE_LENGTH_PACKUOM5_10 = 150;
	private static final int RULE_LENGTH_PACKUOM6_10 = 151;
	private static final int RULE_LENGTH_PACKUOM7_10 = 152;
	private static final int RULE_LENGTH_PACKUOM8_10 = 153;
	private static final int RULE_LENGTH_PACKUOM9_10 = 154;
	private static final int RULE_LENGTH_REPLENISHUOM1_1 = 155;
	private static final int RULE_LENGTH_REPLENISHUOM2_1 = 156;
	private static final int RULE_LENGTH_REPLENISHUOM3_1 = 157;
	private static final int RULE_LENGTH_REPLENISHUOM4_1 = 158;
	private static final int RULE_LENGTH_REPLENISHUOM8_1 = 159;
	private static final int RULE_LENGTH_REPLENISHUOM9_1 = 160;
	private static final int RULE_LENGTH_REPLENISHZONE1_10 = 170;
	private static final int RULE_LENGTH_REPLENISHZONE2_10 = 171;
	private static final int RULE_LENGTH_REPLENISHZONE3_10 = 172;
	private static final int RULE_LENGTH_REPLENISHZONE4_10 = 173;
	private static final int RULE_LENGTH_REPLENISHZONE8_10 = 174;
	private static final int RULE_LENGTH_REPLENISHZONE9_10 = 175;


	//Attribute Domain Rules
	public static final int RULE_ATTR_DOM_PACKUOM1 = 176;
	public static final int RULE_ATTR_DOM_PACKUOM2 = 177;
	public static final int RULE_ATTR_DOM_PACKUOM3 = 178;
	public static final int RULE_ATTR_DOM_PACKUOM4 = 179;
	public static final int RULE_ATTR_DOM_PACKUOM5 = 180;
	public static final int RULE_ATTR_DOM_PACKUOM6 = 181;
	public static final int RULE_ATTR_DOM_PACKUOM7 = 182;
	public static final int RULE_ATTR_DOM_PACKUOM8 = 183;
	public static final int RULE_ATTR_DOM_PACKUOM9 = 184;
	
	
	
	
	public ArrayList validate(PackScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		
		
		//Validate Field Lengths
		if(doCheckFieldLength){			

			if(!validateCartonizeuom1LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM1_1));
			}
			
			if(!validateCartonizeuom2LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM2_1));
			}

			if(!validateCartonizeuom3LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM3_1));
			}

			if(!validateCartonizeuom4LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM4_1));
			}

			if(!validateCartonizeuom8LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM8_1));
			}

			if(!validateCartonizeuom9LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CARTONIZEUOM9_1));
			}

			if(!validateIswhqty1LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY1_1));
			}

			if(!validateIswhqty2LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY2_1));
			}

			if(!validateIswhqty3LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY3_1));
			}

			if(!validateIswhqty4LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY4_1));
			}

			if(!validateIswhqty5LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY5_1));
			}

			if(!validateIswhqty6LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY6_1));
			}

			if(!validateIswhqty7LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY7_1));
			}

			if(!validateIswhqty8LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY8_1));
			}

			if(!validateIswhqty9LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ISWHQTY9_1));
			}

			if(!validatePackdescrLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKDESCR_45));
			}

			if(!validatePackkeyLengthIs50OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKKEY_50));	
			}

			if(!validatePackuom1LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM1_10));
			}

			if(!validatePackuom2LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM2_10));
			}

			if(!validatePackuom3LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM3_10));
			}

			if(!validatePackuom4LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM4_10));
			}

			if(!validatePackuom5LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM5_10));
			}

			if(!validatePackuom6LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM6_10));
			}

			if(!validatePackuom7LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM7_10));
			}

			if(!validatePackuom8LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM8_10));
			}

			if(!validatePackuom9LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PACKUOM9_10));
			}

			if(!validateReplenishuom1LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM1_1));
			}
			
			if(!validateReplenishuom2LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM2_1));
			}

			if(!validateReplenishuom3LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM3_1));
			}

			if(!validateReplenishuom4LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM4_1));
			}

			if(!validateReplenishuom8LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM8_1));
			}

			if(!validateReplenishuom9LengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHUOM9_1));
			}

			if(!validateReplenishzone1LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE1_10));
			}

			if(!validateReplenishzone2LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE2_10));
			}

			if(!validateReplenishzone3LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE3_10));
			}

			if(!validateReplenishzone4LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE4_10));
			}

			if(!validateReplenishzone8LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE8_10));
			}

			if(!validateReplenishzone9LengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHZONE9_10));
			}

		}
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
	
			if(!validatePackuom1InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM1));
			}

			if(!validatePackuom2InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM2));
			}

			if(!validatePackuom3InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM3));
			}

			if(!validatePackuom4InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM4));
			}

			if(!validatePackuom5InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM5));
			}

			if(!validatePackuom6InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM6));
			}

			if(!validatePackuom7InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM7));
			}

			if(!validatePackuom8InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM8));
			}

			if(!validatePackuom9InAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PACKUOM9));
			}

		}
		

		//FUNCTIONS
		if(isInsert) {
			if(doCheckRequiredFields && !validatePackkeyNotEmpty(screen))
				errors.add(new Integer(RULE_PACKKEY_NOT_EMPTY));
			else{
				if(!validatePackkeyDoesNotExist(screen))
					errors.add(new Integer(RULE_PACKKEY_MUST_BE_UNIQUE));
			}
		}

		if(doCheckRequiredFields && !validateLengthuom1NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM1_NOT_EMPTY));
		else{
			if(!validateLengthuom1IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM1_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom1GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateLengthuom2NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM2_NOT_EMPTY));
		else{
			if(!validateLengthuom2IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM2_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom2GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM2_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateLengthuom3NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM3_NOT_EMPTY));
		else{
			if(!validateLengthuom3IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM3_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom3GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM3_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLengthuom4NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM4_NOT_EMPTY));
		else{
			if(!validateLengthuom4IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM4_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom4GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM4_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLengthuom8NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM8_NOT_EMPTY));
		else{
			if(!validateLengthuom8IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM8_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom8GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM8_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLengthuom9NotEmpty(screen))
			errors.add(new Integer(RULE_LENGTHUOM9_NOT_EMPTY));
		else{
			if(!validateLengthuom9IsANumber(screen))
				errors.add(new Integer(RULE_LENGTHUOM9_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthuom9GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTHUOM9_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateHeightuom1NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM1_NOT_EMPTY));
		else{
			if(!validateHeightuom1IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM1_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom1GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateHeightuom2NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM2_NOT_EMPTY));
		else{
			if(!validateHeightuom2IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM2_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom2GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM2_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateHeightuom3NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM3_NOT_EMPTY));
		else{
			if(!validateHeightuom3IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM3_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom3GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM3_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		if(doCheckRequiredFields && !validateHeightuom4NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM4_NOT_EMPTY));
		else{
			if(!validateHeightuom4IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM4_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom4GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM4_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateHeightuom8NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM8_NOT_EMPTY));
		else{
			if(!validateHeightuom8IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM8_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom8GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM8_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateHeightuom9NotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHTUOM9_NOT_EMPTY));
		else{
			if(!validateHeightuom9IsANumber(screen))
				errors.add(new Integer(RULE_HEIGHTUOM9_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightuom9GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHTUOM9_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom1NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM1_NOT_EMPTY));
		else{
			if(!validateWidthuom1IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM1_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom1GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM1_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom2NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM2_NOT_EMPTY));
		else{
			if(!validateWidthuom2IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM2_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom2GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM2_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom3NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM3_NOT_EMPTY));
		else{
			if(!validateWidthuom3IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM3_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom3GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM3_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom4NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM4_NOT_EMPTY));
		else{
			if(!validateWidthuom4IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM4_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom4GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM4_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom8NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM8_NOT_EMPTY));
		else{
			if(!validateWidthuom8IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM8_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom8GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM8_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateWidthuom9NotEmpty(screen))
			errors.add(new Integer(RULE_WIDTHUOM9_NOT_EMPTY));
		else{
			if(!validateWidthuom9IsANumber(screen))
				errors.add(new Integer(RULE_WIDTHUOM9_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthuom9GreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTHUOM9_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePallettiNotEmpty(screen))
			errors.add(new Integer(RULE_PALLETTI_NOT_EMPTY));
		else{
			if(!validatePallettiIsANumber(screen))
				errors.add(new Integer(RULE_PALLETTI_MUST_BE_A_NUMBER));
			else{
				if(!validatePallettiGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLETTI_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePallethiNotEmpty(screen))
			errors.add(new Integer(RULE_PALLETHI_NOT_EMPTY));
		else{
			if(!validatePallethiIsANumber(screen))
				errors.add(new Integer(RULE_PALLETHI_MUST_BE_A_NUMBER));
			else{
				if(!validatePallethiGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLETHI_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePalletwoodlengthNotEmpty(screen))
			errors.add(new Integer(RULE_PALLETWOODLENGTH_NOT_EMPTY));
		else{
			if(!validatePalletwoodlengthIsANumber(screen))
				errors.add(new Integer(RULE_PALLETWOODLENGTH_MUST_BE_A_NUMBER));
			else{
				if(!validatePalletwoodlengthGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLETWOODLENGTH_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePalletwoodheightNotEmpty(screen))
			errors.add(new Integer(RULE_PALLETWOODHEIGHT_NOT_EMPTY));
		else{
			if(!validatePalletwoodheightIsANumber(screen))
				errors.add(new Integer(RULE_PALLETWOODHEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validatePalletwoodheightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLETWOODHEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validatePalletwoodwidthNotEmpty(screen))
			errors.add(new Integer(RULE_PALLETWOODWIDTH_NOT_EMPTY));
		else{
			if(!validatePalletwoodwidthIsANumber(screen))
				errors.add(new Integer(RULE_PALLETWOODWIDTH_MUST_BE_A_NUMBER));
			else{
				if(!validatePalletwoodwidthGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_PALLETWOODWIDTH_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		//PackUOM dups check
		if(doCheckRequiredFields ){
			HashMap packuoms = new HashMap();


			if(duplicateInMap(packuoms, screen.getPackuom1())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


			if(duplicateInMap(packuoms, screen.getPackuom2())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


			if(duplicateInMap(packuoms, screen.getPackuom3())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}

			

			if(duplicateInMap(packuoms, screen.getPackuom4())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


			if(duplicateInMap(packuoms, screen.getPackuom5())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}

			if(duplicateInMap(packuoms, screen.getPackuom6())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


			if(duplicateInMap(packuoms, screen.getPackuom7())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


			if(duplicateInMap(packuoms, screen.getPackuom8())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}
			

			if(duplicateInMap(packuoms, screen.getPackuom9())){
				errors.add(new Integer(PackScreenValidator.RULE_PACKUOM_MUST_BE_UNIQUE));
			}


		}

		if (validateFiltervalueuom3NotEmpty(screen) && validateFiltervalueuom3IsANumber(screen)){
		
			if(!validateFiltervalueuomBetween0And7(screen.getFiltervalueuom3())){
				errors.add(new Integer( RULE_FILTERVALUEUOM3_BETWEEN_0_AND_7));
			}
		}

		if(validateIndicatordigituom3NotEmpty(screen) && validateIndicatordigituom3IsANumber(screen)){
			if(!validateIndicatordigituomBetween0And8(screen.getIndicatordigituom3())){
				errors.add(new Integer(RULE_INDICATORDIGITUOM3_BETWEEN_0_AND_8));
			}
		}
		return errors;
	}
	
	
	
	
	
	
	
	//validators
	private boolean duplicateInMap(HashMap map, String key){
		
		if(key==null || key.trim().length()<=0)
			return false;
		
		if (map.containsKey(key))
			return true;
		else{
			map.put(key, null);
			return false;
		}
	}

	private boolean validateFiltervalueuomBetween0And7(String sFiltervalueuom){
		int filtervalueuom;
		try{
			filtervalueuom = Integer.parseInt(sFiltervalueuom);
		}catch(NumberFormatException e){
			filtervalueuom = -1;
		}
		
		if (filtervalueuom >=0 && filtervalueuom <= 7){
			return true;
		}
		
		return false;
	}

	private boolean validateIndicatordigituomBetween0And8(String sIndicatordigituom){
		int indicatordigituom;
		try{
			indicatordigituom = Integer.parseInt(sIndicatordigituom);
		}catch(NumberFormatException e){
			indicatordigituom = -1;
		}
		
		if (indicatordigituom >=0 && indicatordigituom <= 8){
			return true;
		}

		return false;
	}

	private boolean validateCasecntIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCasecnt()))
			return isNumber(screen.getCasecnt());		
		else 
			return true;
	}

	private boolean validateCubeIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCube()))
			return isNumber(screen.getCube());		
		else 
			return true;
	}
	
	private boolean validateCubeuom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCubeuom1()))
			return isNumber(screen.getCubeuom1());		
		else 
			return true;
	}

	private boolean validateCubeuom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCubeuom2()))
			return isNumber(screen.getCubeuom2());		
		else 
			return true;
	}

	private boolean validateCubeuom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCubeuom3()))
			return isNumber(screen.getCubeuom3());		
		else 
			return true;
	}

	private boolean validateCubeuom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getCubeuom4()))
			return isNumber(screen.getCubeuom4());		
		else 
			return true;
	}
	
	
	private boolean validateFiltervalueuom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom1()))
			return isNumber(screen.getFiltervalueuom1());		
		else 
			return true;
	}
	

	private boolean validateFiltervalueuom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom2()))
			return isNumber(screen.getFiltervalueuom2());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom3()))
			return isNumber(screen.getFiltervalueuom3());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom4()))
			return isNumber(screen.getFiltervalueuom4());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom5IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom5()))
			return isNumber(screen.getFiltervalueuom5());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom6IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom6()))
			return isNumber(screen.getFiltervalueuom6());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom7IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom7()))
			return isNumber(screen.getFiltervalueuom7());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom8IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom8()))
			return isNumber(screen.getFiltervalueuom8());		
		else 
			return true;
	}

	private boolean validateFiltervalueuom9IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getFiltervalueuom9()))
			return isNumber(screen.getFiltervalueuom9());		
		else 
			return true;
	}

	private boolean validateGrossWgtIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getGrosswgt()))
			return isNumber(screen.getGrosswgt());		
		else 
			return true;
	}

	private boolean validateHeightuom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom1()))
			return isNumber(screen.getHeightuom1());		
		else 
			return true;
	}

	private boolean validateHeightuom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom2()))
			return isNumber(screen.getHeightuom2());		
		else 
			return true;
	}

	private boolean validateHeightuom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom3()))
			return isNumber(screen.getHeightuom3());		
		else 
			return true;
	}

	private boolean validateHeightuom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom4()))
			return isNumber(screen.getHeightuom4());		
		else 
			return true;
	}
	private boolean validateHeightuom8IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom8()))
			return isNumber(screen.getHeightuom8());		
		else 
			return true;
	}

	private boolean validateHeightuom9IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getHeightuom9()))
			return isNumber(screen.getHeightuom9());		
		else 
			return true;
	}


	private boolean validateIndicatordigituom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom1()))
			return isNumber(screen.getIndicatordigituom1());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom2()))
			return isNumber(screen.getIndicatordigituom2());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom3()))
			return isNumber(screen.getIndicatordigituom3());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom4()))
			return isNumber(screen.getIndicatordigituom4());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom5IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom5()))
			return isNumber(screen.getIndicatordigituom5());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom6IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom6()))
			return isNumber(screen.getIndicatordigituom6());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom7IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom7()))
			return isNumber(screen.getIndicatordigituom7());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom8IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom8()))
			return isNumber(screen.getIndicatordigituom8());		
		else 
			return true;
	}

	private boolean validateIndicatordigituom9IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getIndicatordigituom9()))
			return isNumber(screen.getIndicatordigituom9());		
		else 
			return true;
	}

	private boolean validateInnerpackIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getInnerpack()))
			return isNumber(screen.getInnerpack());		
		else 
			return true;
	}

	private boolean validateLengthuom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom1()))
			return isNumber(screen.getLengthuom1());		
		else 
			return true;
	}

	private boolean validateLengthuom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom2()))
			return isNumber(screen.getLengthuom2());		
		else 
			return true;
	}

	private boolean validateLengthuom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom3()))
			return isNumber(screen.getLengthuom3());		
		else 
			return true;
	}

	private boolean validateLengthuom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom4()))
			return isNumber(screen.getLengthuom4());		
		else 
			return true;
	}

	private boolean validateLengthuom8IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom8()))
			return isNumber(screen.getLengthuom8());		
		else 
			return true;
	}

	private boolean validateLengthuom9IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getLengthuom9()))
			return isNumber(screen.getLengthuom9());		
		else 
			return true;
	}

	private boolean validateNetwgtIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getNetwgt()))
			return isNumber(screen.getNetwgt());		
		else 
			return true;
	}

	private boolean validateOtherunit1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getOtherunit1()))
			return isNumber(screen.getOtherunit1());		
		else 
			return true;
	}

	private boolean validateOtherunit2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getOtherunit2()))
			return isNumber(screen.getOtherunit2());		
		else 
			return true;
	}

	private boolean validatePalletIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPallet()))
			return isNumber(screen.getPallet());		
		else 
			return true;
	}

	private boolean validatePallethiIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPallethi()))
			return isNumber(screen.getPallethi());		
		else 
			return true;
	}

	private boolean validatePallettiIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPalletti()))
			return isNumber(screen.getPalletti());		
		else 
			return true;
	}

	private boolean validatePalletwoodheightIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPalletwoodheight()))
			return isNumber(screen.getPalletwoodheight());		
		else 
			return true;
	}

	private boolean validatePalletwoodlengthIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPalletwoodlength()))
			return isNumber(screen.getPalletwoodlength());		
		else 
			return true;
	}

	private boolean validatePalletwoodwidthIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getPalletwoodwidth()))
			return isNumber(screen.getPalletwoodwidth());		
		else 
			return true;
	}

	private boolean validateQtyIsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getQty()))
			return isNumber(screen.getQty());		
		else 
			return true;
	}

	private boolean validateWidthuom1IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom1()))
			return isNumber(screen.getWidthuom1());		
		else 
			return true;
	}

	private boolean validateWidthuom2IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom2()))
			return isNumber(screen.getWidthuom2());		
		else 
			return true;
	}

	private boolean validateWidthuom3IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom3()))
			return isNumber(screen.getWidthuom3());		
		else 
			return true;
	}

	private boolean validateWidthuom4IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom4()))
			return isNumber(screen.getWidthuom4());		
		else 
			return true;
	}

	private boolean validateWidthuom8IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom8()))
			return isNumber(screen.getWidthuom8());		
		else 
			return true;
	}

	private boolean validateWidthuom9IsANumber(PackScreenVO screen) throws WMSDataLayerException{
		if(!isEmpty(screen.getWidthuom9()))
			return isNumber(screen.getWidthuom9());		
		else 
			return true;
	}

	

	private boolean validateQtyGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getQty());
	}

	private boolean validateCasecntGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getCasecnt());
	}

	private boolean validatePalletGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getPallet());
	}

	private boolean validateCubeGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getCube());
	}

	private boolean validateInnerpackGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getInnerpack());
	}

	private boolean validateGrosswgtGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getGrosswgt());
	}

	private boolean validateNetwgtGreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getNetwgt());
	}

	private boolean validateOtherunit1GreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getOtherunit1());
	}

	private boolean validateOtherunit2GreaterThanZero(PackScreenVO screen){
		return greaterThanZeroValidation(screen.getOtherunit2());
	}

	private boolean validatePallettiGreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPalletti());
	}

	private boolean validatePallethiGreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPallethi());
	}

	private boolean validatePalletwoodlengthGreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPalletwoodlength());
	}

	private boolean validatePalletwoodheightGreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPalletwoodheight());
	}

	private boolean validatePalletwoodwidthGreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getPalletwoodwidth());
	}

	private boolean validateLengthuom1GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom1());
	}

	private boolean validateLengthuom2GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom2());
	}

	private boolean validateLengthuom3GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom3());
	}

	private boolean validateLengthuom4GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom4());
	}

	private boolean validateLengthuom8GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom8());
	}

	private boolean validateLengthuom9GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getLengthuom9());
	}

	private boolean validateHeightuom1GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom1());
	}

	private boolean validateHeightuom2GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom2());
	}

	private boolean validateHeightuom3GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom3());
	}

	private boolean validateHeightuom4GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom4());
	}

	private boolean validateHeightuom8GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom8());
	}

	private boolean validateHeightuom9GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getHeightuom9());
	}

	private boolean validateWidthuom1GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom1());
	}

	private boolean validateWidthuom2GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom2());
	}

	private boolean validateWidthuom3GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom3());
	}

	private boolean validateWidthuom4GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom4());
	}

	private boolean validateWidthuom8GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom8());
	}

	private boolean validateWidthuom9GreaterThanOrEqualZero(PackScreenVO screen){
		return greaterThanOrEqualToZeroValidation(screen.getWidthuom9());
	}

	private boolean validateLengthuom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom1());			
	}

	private boolean validateLengthuom2NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom2());			
	}

	private boolean validateLengthuom3NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom3());			
	}

	private boolean validateLengthuom4NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom4());			
	}

	private boolean validateLengthuom8NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom8());			
	}

	private boolean validateLengthuom9NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getLengthuom9());			
	}

	private boolean validateWidthuom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom1());			
	}

	private boolean validateWidthuom2NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom2());			
	}

	private boolean validateWidthuom3NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom3());			
	}

	private boolean validateWidthuom4NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom4());			
	}

	private boolean validateWidthuom8NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom8());			
	}

	private boolean validateWidthuom9NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getWidthuom9());			
	}

	private boolean validateHeightuom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom1());			
	}

	private boolean validateHeightuom2NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom2());			
	}


	private boolean validateHeightuom3NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom3());			
	}

	private boolean validateHeightuom4NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom4());			
	}

	private boolean validateHeightuom8NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom8());			
	}

	private boolean validateHeightuom9NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getHeightuom9());			
	}

	private boolean validatePallettiNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPalletti());			
	}

	private boolean validatePallethiNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPallethi());			
	}

	private boolean validatePalletwoodlengthNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPalletwoodlength());			
	}

	private boolean validatePalletwoodheightNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPalletwoodheight());			
	}

	private boolean validatePalletwoodwidthNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPalletwoodwidth());			
	}

	private boolean validatePackkeyNotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPackkey());			
	}

	private boolean validatePackuom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getPackuom1());			
	}

	private boolean validateFiltervalueuom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom1());			
	}

	private boolean validateFiltervalueuom2NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom2());			
	}

	private boolean validateFiltervalueuom3NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom3());			
	}

	private boolean validateFiltervalueuom4NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom4());			
	}

	private boolean validateFiltervalueuom5NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom5());			
	}

	private boolean validateFiltervalueuom6NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom6());			
	}

	private boolean validateFiltervalueuom7NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom7());			
	}

	private boolean validateFiltervalueuom8NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom8());			
	}

	private boolean validateFiltervalueuom9NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getFiltervalueuom9());			
	}


	private boolean validateIndicatordigituom1NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom1());			
	}

	private boolean validateIndicatordigituom2NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom2());			
	}

	private boolean validateIndicatordigituom3NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom3());			
	}

	private boolean validateIndicatordigituom4NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom4());			
	}

	private boolean validateIndicatordigituom5NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom5());			
	}

	private boolean validateIndicatordigituom6NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom6());			
	}

	private boolean validateIndicatordigituom7NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom7());			
	}

	private boolean validateIndicatordigituom8NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom8());			
	}

	private boolean validateIndicatordigituom9NotEmpty(PackScreenVO screen){
		return !isEmpty(screen.getIndicatordigituom9());			
	}

	
	private boolean validatePackkeyDoesNotExist(PackScreenVO screen) throws WMSDataLayerException{
		return !validatePackkeyDoesExist(screen.getPackkey(),getContext());			
	}

	private boolean validatePackkeyDoesExist(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackkey()))
			return true;
		return validatePackkeyDoesExist(screen.getPackkey(),getContext());				
	}

	protected static boolean validatePackkeyDoesExist(String packKey, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKey(packKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	//Attr1
	private boolean validatePackuom1InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom1()))
			return true;
		return validatePackuom1DoesExist(screen.getPackuom1(), getContext());			
	}

	protected static boolean validatePackuom1DoesExist(String packuom1, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PACKAGE", packuom1,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	private boolean validatePackuom2InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom2()))
			return true;
		return validatePackuom2DoesExist(screen.getPackuom2(), getContext());			
	}

	protected static boolean validatePackuom2DoesExist(String packuom2, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("QUANTITY", packuom2,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	private boolean validatePackuom3InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom3()))
			return true;
		return validatePackuom3DoesExist(screen.getPackuom3(), getContext());			
	}

	protected static boolean validatePackuom3DoesExist(String packuom3, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("QUANTITY", packuom3,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	private boolean validatePackuom4InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom4()))
			return true;
		return validatePackuom4DoesExist(screen.getPackuom4(), getContext());			
	}

	protected static boolean validatePackuom4DoesExist(String packuom4, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PACKAGE", packuom4,context).getSize() == 0)
			return false;
		else
			return true;	
	}


	private boolean validatePackuom5InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom5()))
			return true;
		return validatePackuom5DoesExist(screen.getPackuom5(), getContext());			
	}

	protected static boolean validatePackuom5DoesExist(String packuom5, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("VOLUME", packuom5,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	private boolean validatePackuom6InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom6()))
			return true;
		return validatePackuom6DoesExist(screen.getPackuom6(), getContext());			
	}

	protected static boolean validatePackuom6DoesExist(String packuom6, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("WEIGHT", packuom6,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	private boolean validatePackuom7InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom7()))
			return true;
		return validatePackuom7DoesExist(screen.getPackuom7(), getContext());			
	}

	protected static boolean validatePackuom7DoesExist(String packuom7, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("WEIGHT", packuom7,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	private boolean validatePackuom8InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom8()))
			return true;
		return validatePackuom8DoesExist(screen.getPackuom4(), getContext());			
	}

	protected static boolean validatePackuom8DoesExist(String packuom8, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("DIMS", packuom8,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	
	private boolean validatePackuom9InAttrDom(PackScreenVO screen) throws WMSDataLayerException{
		if(isEmpty(screen.getPackuom9()))
			return true;
		return validatePackuom9DoesExist(screen.getPackuom9(), getContext());			
	}

	protected static boolean validatePackuom9DoesExist(String packuom9, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("DIMS", packuom9,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	//Length validations
	private boolean validateCartonizeuom1LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom1()))
			return true;
		return screen.getCartonizeuom1().length() < 2;			
	}

	private boolean validateCartonizeuom2LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom2()))
			return true;
		return screen.getCartonizeuom2().length() < 2;			
	}

	private boolean validateCartonizeuom3LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom3()))
			return true;
		return screen.getCartonizeuom3().length() < 2;			
	}

	private boolean validateCartonizeuom4LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom4()))
			return true;
		return screen.getCartonizeuom4().length() < 2;			
	}

	private boolean validateCartonizeuom8LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom8()))
			return true;
		return screen.getCartonizeuom8().length() < 2;			
	}

	private boolean validateCartonizeuom9LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getCartonizeuom9()))
			return true;
		return screen.getCartonizeuom9().length() < 2;			
	}

	private boolean validateIswhqty1LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty1()))
			return true;
		return screen.getIswhqty1().length() < 2;			
	}

	private boolean validateIswhqty2LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty2()))
			return true;
		return screen.getIswhqty2().length() < 2;			
	}

	private boolean validateIswhqty3LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty3()))
			return true;
		return screen.getIswhqty3().length() < 2;			
	}

	private boolean validateIswhqty4LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty4()))
			return true;
		return screen.getIswhqty4().length() < 2;			
	}

	private boolean validateIswhqty5LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty5()))
			return true;
		return screen.getIswhqty5().length() < 2;			
	}

	private boolean validateIswhqty6LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty6()))
			return true;
		return screen.getIswhqty6().length() < 2;			
	}

	private boolean validateIswhqty7LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty7()))
			return true;
		return screen.getIswhqty7().length() < 2;			
	}

	private boolean validateIswhqty8LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty8()))
			return true;
		return screen.getIswhqty8().length() < 2;			
	}

	private boolean validateIswhqty9LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getIswhqty9()))
			return true;
		return screen.getIswhqty9().length() < 2;			
	}


	private boolean validatePackdescrLengthIs45OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackdescr()))
			return true;
		return screen.getPackdescr().length() < 46;			
	}

	private boolean validatePackkeyLengthIs50OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackkey()))
			return true;
		return screen.getPackkey().length() < 51;			
	}
	
	private boolean validatePackuom1LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom1()))
			return true;
		return screen.getPackuom1().length() < 11;			
	}

	private boolean validatePackuom2LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom2()))
			return true;
		return screen.getPackuom2().length() < 11;			
	}

	private boolean validatePackuom3LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom3()))
			return true;
		return screen.getPackuom3().length() < 11;			
	}

	private boolean validatePackuom4LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom4()))
			return true;
		return screen.getPackuom4().length() < 11;			
	}

	private boolean validatePackuom5LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom5()))
			return true;
		return screen.getPackuom5().length() < 11;			
	}

	private boolean validatePackuom6LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom6()))
			return true;
		return screen.getPackuom6().length() < 11;			
	}

	private boolean validatePackuom7LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom7()))
			return true;
		return screen.getPackuom7().length() < 11;			
	}

	private boolean validatePackuom8LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom8()))
			return true;
		return screen.getPackuom8().length() < 11;			
	}

	private boolean validatePackuom9LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getPackuom9()))
			return true;
		return screen.getPackuom9().length() < 11;			
	}

	private boolean validateReplenishuom1LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom1()))
			return true;
		return screen.getReplenishuom1().length() < 2;			
	}

	private boolean validateReplenishuom2LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom2()))
			return true;
		return screen.getReplenishuom2().length() < 2;			
	}

	private boolean validateReplenishuom3LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom3()))
			return true;
		return screen.getReplenishuom3().length() < 2;			
	}

	private boolean validateReplenishuom4LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom4()))
			return true;
		return screen.getReplenishuom4().length() < 2;			
	}

	private boolean validateReplenishuom8LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom8()))
			return true;
		return screen.getReplenishuom8().length() < 2;			
	}

	private boolean validateReplenishuom9LengthIs1OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishuom9()))
			return true;
		return screen.getReplenishuom9().length() < 2;			
	}

	private boolean validateReplenishzone1LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone1()))
			return true;
		return screen.getReplenishzone1().length() < 11;			
	}

	private boolean validateReplenishzone2LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone2()))
			return true;
		return screen.getReplenishzone2().length() < 11;			
	}

	private boolean validateReplenishzone3LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone3()))
			return true;
		return screen.getReplenishzone3().length() < 11;			
	}

	private boolean validateReplenishzone4LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone4()))
			return true;
		return screen.getReplenishzone4().length() < 11;			
	}

	private boolean validateReplenishzone8LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone8()))
			return true;
		return screen.getReplenishzone8().length() < 11;			
	}

	private boolean validateReplenishzone9LengthIs10OrLess(PackScreenVO screen){
		if(isEmpty(screen.getReplenishzone9()))
			return true;
		return screen.getReplenishzone9().length() < 11;			
	}

	
	
	public static String getErrorMessage(int errorCode, Locale locale, PackScreenVO packScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){
			
		case RULE_CASECNT_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CASECNT, locale));
		
		case RULE_CUBE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBE, locale));
		
		case RULE_CUBEUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBEUOM1, locale));
			
		case RULE_CUBEUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBEUOM2, locale));

		case RULE_CUBEUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBEUOM3, locale));

		case RULE_CUBEUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBEUOM4, locale));

		case RULE_FilterValueUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM1, locale));

		case RULE_FilterValueUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM2, locale));

		case RULE_FilterValueUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM3, locale));

		case RULE_FilterValueUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM4, locale));

		case RULE_FilterValueUOM5_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM5, locale));

		case RULE_FilterValueUOM6_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM6, locale));

		case RULE_FilterValueUOM7_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM7, locale));

		case RULE_FilterValueUOM8_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM8, locale));

		case RULE_FilterValueUOM9_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM9, locale));

		case RULE_GROSSWGT_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_GROSSWGT, locale));

		case RULE_HEIGHTUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM1, locale));

		case RULE_HEIGHTUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM2, locale));

		case RULE_HEIGHTUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM3, locale));

		case RULE_HEIGHTUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM4, locale));

		case RULE_HEIGHTUOM8_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM8, locale));

		case RULE_HEIGHTUOM9_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM9, locale));

		case RULE_IndicatorDigitUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM1, locale));

		case RULE_IndicatorDigitUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM2, locale));

		case RULE_IndicatorDigitUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM3, locale));

		case RULE_IndicatorDigitUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM4, locale));

		case RULE_IndicatorDigitUOM5_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM5, locale));

		case RULE_IndicatorDigitUOM6_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM6, locale));

		case RULE_IndicatorDigitUOM7_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM7, locale));

		case RULE_IndicatorDigitUOM8_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM8, locale));

		case RULE_IndicatorDigitUOM9_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM9, locale));

		case RULE_INNERPACK_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INNERPACK, locale));

		case RULE_LENGTHUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM1, locale));

		case RULE_LENGTHUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM2, locale));

		case RULE_LENGTHUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM3, locale));

		case RULE_LENGTHUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM4, locale));

		case RULE_LENGTHUOM8_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM8, locale));

		case RULE_LENGTHUOM9_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM9, locale));

		case RULE_NETWGT_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_NETWGT, locale));

		case RULE_OTHERUNIT1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_OTHERUNIT1, locale));

		case RULE_OTHERUNIT2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_OTHERUNIT2, locale));

		case RULE_PALLET_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLET, locale));

		case RULE_PALLETTI_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETTI, locale));

		case RULE_PALLETHI_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETHI, locale));

		case RULE_PALLETWOODHEIGHT_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODHEIGHT, locale));

		case RULE_PALLETWOODLENGTH_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODLENGTH, locale));

		case RULE_PALLETWOODWIDTH_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODWIDTH, locale));

		case RULE_QTY_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_QTY, locale));

		case RULE_WIDTHUOM1_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM1, locale));

		case RULE_WIDTHUOM2_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM2, locale));

		case RULE_WIDTHUOM3_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM3, locale));

		case RULE_WIDTHUOM4_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM4, locale));

		case RULE_WIDTHUOM8_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM8, locale));

		case RULE_WIDTHUOM9_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM9, locale));

		case RULE_PACKKEY_MUST_EXIST:
			return getDoesNotExistErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM9, locale), packScreen.getPackkey());

		case RULE_PACKKEY_MUST_BE_UNIQUE :
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKKEY, locale);
			param[1] = packScreen.getPackkey();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_PACK_SCREEN_ERROR_DUPLICATE_PACKKEY, locale, param);

		case RULE_QTY_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_QTY, locale));

		case RULE_CASECNT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CASECNT, locale));

		case RULE_INNERPACK_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INNERPACK, locale));

		case RULE_PALLETTI_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETTI, locale));

		case RULE_PALLETHI_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETHI, locale));

		case RULE_PALLETWOODLENGTH_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODLENGTH, locale));

		case RULE_PALLETWOODHEIGHT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODHEIGHT, locale));

		case RULE_PALLETWOODWIDTH_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODWIDTH, locale));

		//Required Field Rules
		case RULE_PACKKEY_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKKEY, locale));

		case RULE_LENGTHUOM1_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM1, locale));

		case RULE_LENGTHUOM2_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM2, locale));

		case RULE_LENGTHUOM3_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM3, locale));

		case RULE_LENGTHUOM4_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM4, locale));

		case RULE_LENGTHUOM8_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM8, locale));

		case RULE_LENGTHUOM9_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM9, locale));

		case RULE_WIDTHUOM1_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM1, locale));

		case RULE_WIDTHUOM2_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM2, locale));

		case RULE_WIDTHUOM3_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM3, locale));

		case RULE_WIDTHUOM4_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM4, locale));

		case RULE_WIDTHUOM8_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM8, locale));

		case RULE_WIDTHUOM9_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM9, locale));

		case RULE_HEIGHTUOM1_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM1, locale));

		case RULE_HEIGHTUOM2_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM2, locale));

		case RULE_HEIGHTUOM3_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM3, locale));

		case RULE_HEIGHTUOM4_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM4, locale));

		case RULE_HEIGHTUOM8_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM8, locale));

		case RULE_HEIGHTUOM9_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM9, locale));

		case RULE_PALLETTI_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETTI, locale));

		case RULE_PALLETHI_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETHI, locale));

		case RULE_PALLETWOODLENGTH_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODLENGTH, locale));

		case RULE_PALLETWOODHEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODHEIGHT, locale));

		case RULE_PALLETWOODWIDTH_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PALLETWOODWIDTH, locale));

		case RULE_PACKUOM_MUST_BE_UNIQUE :
			param = new String[18];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM1, locale);
			param[1] = packScreen.getPackuom1();
			param[2] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM2, locale);
			param[3] = packScreen.getPackuom2();
			param[4] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM3, locale);
			param[5] = packScreen.getPackuom3();
			param[6] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM4, locale);
			param[7] = packScreen.getPackuom4();
			param[8] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM5, locale);
			param[9] = packScreen.getPackuom5();
			param[10] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM6, locale);
			param[11] = packScreen.getPackuom6();
			param[12] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM7, locale);
			param[13] = packScreen.getPackuom7();
			param[14] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM8, locale);
			param[15] = packScreen.getPackuom8();
			param[16] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM9, locale);
			param[17] = packScreen.getPackuom9();

			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_PACK_SCREEN_ERROR_DUPLICATE_PACKUOM, locale, param);

		case RULE_LENGTHUOM1_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM1, locale));

		case RULE_LENGTHUOM2_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM2, locale));

		case RULE_LENGTHUOM3_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM3, locale));

		case RULE_LENGTHUOM4_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM4, locale));

		case RULE_LENGTHUOM8_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM8, locale));

		case RULE_LENGTHUOM9_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_LENGTHUOM9, locale));

		case RULE_HEIGHTUOM1_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM1, locale));

		case RULE_HEIGHTUOM2_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM2, locale));

		case RULE_HEIGHTUOM3_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM3, locale));

		case RULE_HEIGHTUOM4_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM4, locale));

		case RULE_HEIGHTUOM8_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM8, locale));

		case RULE_HEIGHTUOM9_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_HEIGHTUOM9, locale));

		case RULE_WIDTHUOM1_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM1, locale));

		case RULE_WIDTHUOM2_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM2, locale));

		case RULE_WIDTHUOM3_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM3, locale));

		case RULE_WIDTHUOM4_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM4, locale));

		case RULE_WIDTHUOM8_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM8, locale));

		case RULE_WIDTHUOM9_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_WIDTHUOM9, locale));

		case RULE_FILTERVALUEUOM1_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM1, locale));

		case RULE_FILTERVALUEUOM2_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM2, locale));

		case RULE_FILTERVALUEUOM3_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM3, locale));

		case RULE_FILTERVALUEUOM4_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM4, locale));

		case RULE_FILTERVALUEUOM5_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM5, locale));

		case RULE_FILTERVALUEUOM6_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM6, locale));

		case RULE_FILTERVALUEUOM7_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM7, locale));

		case RULE_FILTERVALUEUOM8_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM8, locale));

		case RULE_FILTERVALUEUOM9_BETWEEN_0_AND_7:
			return getBetween0And7ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM9, locale));

		case RULE_INDICATORDIGITUOM1_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM1, locale));

		case RULE_INDICATORDIGITUOM2_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM2, locale));

		case RULE_INDICATORDIGITUOM3_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM3, locale));

		case RULE_INDICATORDIGITUOM4_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM4, locale));

		case RULE_INDICATORDIGITUOM5_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM5, locale));

		case RULE_INDICATORDIGITUOM6_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM6, locale));

		case RULE_INDICATORDIGITUOM7_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM7, locale));

		case RULE_INDICATORDIGITUOM8_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM8, locale));

		case RULE_INDICATORDIGITUOM9_BETWEEN_0_AND_8:
			return getBetween0And8ToErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM9, locale));

		case RULE_CUBE_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CUBE, locale));

		case RULE_GROSS_WEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_GROSSWGT, locale));

		case RULE_NET_WEIGHT_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_NETWGT, locale));

		case RULE_LENGTH_CARTONIZEUOM1_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE1, locale), "1");

		case RULE_LENGTH_CARTONIZEUOM2_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE2, locale), "1");

		case RULE_LENGTH_CARTONIZEUOM3_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE3, locale), "1");

		case RULE_LENGTH_CARTONIZEUOM4_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE4, locale), "1");

		case RULE_LENGTH_CARTONIZEUOM8_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE8, locale), "1");

		case RULE_LENGTH_CARTONIZEUOM9_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_CARTONIZE9, locale), "1");

		case RULE_LENGTH_ISWHQTY1_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY1, locale), "1");

		case RULE_LENGTH_ISWHQTY2_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY2, locale), "1");

		case RULE_LENGTH_ISWHQTY3_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY3, locale), "1");

		case RULE_LENGTH_ISWHQTY4_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY4, locale), "1");

		case RULE_LENGTH_ISWHQTY5_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY5, locale), "1");

		case RULE_LENGTH_ISWHQTY6_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY6, locale), "1");

		case RULE_LENGTH_ISWHQTY7_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY7, locale), "1");

		case RULE_LENGTH_ISWHQTY8_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY8, locale), "1");

		case RULE_LENGTH_ISWHQTY9_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_ISWHQTY9, locale), "1");

		case RULE_LENGTH_PACKDESCR_45 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKDESCR, locale), "45");

		case RULE_LENGTH_PACKKEY_50 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKKEY, locale), "50");

		case RULE_LENGTH_PACKUOM1_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM1, locale), "10");

		case RULE_LENGTH_PACKUOM2_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM2, locale), "10");

		case RULE_LENGTH_PACKUOM3_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM3, locale), "10");

		case RULE_LENGTH_PACKUOM4_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM4, locale), "10");

		case RULE_LENGTH_PACKUOM5_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM5, locale), "10");

		case RULE_LENGTH_PACKUOM6_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM6, locale), "10");

		case RULE_LENGTH_PACKUOM7_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM7, locale), "10");

		case RULE_LENGTH_PACKUOM8_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM8, locale), "10");

		case RULE_LENGTH_PACKUOM9_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM9, locale), "10");

		case RULE_LENGTH_REPLENISHUOM1_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM1, locale), "1");

		case RULE_LENGTH_REPLENISHUOM2_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM2, locale), "1");

		case RULE_LENGTH_REPLENISHUOM3_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM3, locale), "1");

		case RULE_LENGTH_REPLENISHUOM4_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM4, locale), "1");

		case RULE_LENGTH_REPLENISHUOM8_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM8, locale), "1");

		case RULE_LENGTH_REPLENISHUOM9_1 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHUOM9, locale), "1");

		case RULE_LENGTH_REPLENISHZONE1_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE1, locale), "10");

		case RULE_LENGTH_REPLENISHZONE2_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE2, locale), "10");

		case RULE_LENGTH_REPLENISHZONE3_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE3, locale), "10");

		case RULE_LENGTH_REPLENISHZONE4_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE4, locale), "10");

		case RULE_LENGTH_REPLENISHZONE8_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE8, locale), "10");

		case RULE_LENGTH_REPLENISHZONE9_10 :
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_REPLENISHZONE9, locale), "10");

		//Attribute Domain Rules
		case RULE_ATTR_DOM_PACKUOM1 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM1, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM2 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM2, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM3 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM3, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM4 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM4, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM5 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM5, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM6 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM6, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM7 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM7, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM8 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM8, locale), packScreen.getPackuom1());

		case RULE_ATTR_DOM_PACKUOM9 :
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PACK_FIELD_PACKUOM9, locale), packScreen.getPackuom1());

			
		}//end switch
		return errorMsg;
	}

}
