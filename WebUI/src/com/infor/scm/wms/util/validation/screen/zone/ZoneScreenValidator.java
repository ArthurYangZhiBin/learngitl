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
package com.infor.scm.wms.util.validation.screen.zone;

import java.util.ArrayList;
import java.util.Locale;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.EquipmentprofileQueryRunner;
import com.infor.scm.wms.util.datalayer.query.LocationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PazoneequipmentexcludedetailQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PutawayZoneQueryRunner;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO;
import com.infor.scm.wms.util.validation.screen.owner.OwnerScreenVO;
import com.infor.scm.wms.util.validation.util.MessageUtil;


public class ZoneScreenValidator extends BaseScreenValidator{

	public ZoneScreenValidator(WMSValidationContext context) {
		super(context);
	}

	//Numeric rules
	public static final int RULE_AISLEEND_MUST_BE_A_NUMBER = 1;
	public static final int RULE_AISLESTART_MUST_BE_A_NUMBER = 2;
	public static final int RULE_LABORMAXCASECNT_MUST_BE_A_NUMBER = 3;
	public static final int RULE_LABORMAXCUBE_MUST_BE_A_NUMBER = 4;
	public static final int RULE_LABORMAXWEIGHT_MUST_BE_A_NUMBER = 5;
	public static final int RULE_MAXCASECNT_MUST_BE_A_NUMBER = 6;
	public static final int RULE_MAXCUBE_MUST_BE_A_NUMBER = 7;
	public static final int RULE_MAXPALLETSPERSKU_MUST_BE_A_NUMBER = 8;
	public static final int RULE_MAXPICKINGCONTAINERS_MUST_BE_A_NUMBER = 9;
	public static final int RULE_MAXPICKLINES_MUST_BE_A_NUMBER = 10;
	public static final int RULE_MAXWEIGHT_MUST_BE_A_NUMBER = 11;
	public static final int RULE_QTYCC_MUST_BE_A_NUMBER = 12;
	public static final int RULE_SLOTEND_MUST_BE_A_NUMBER = 13;
	public static final int RULE_SLOTSTART_MUST_BE_A_NUMBER = 14;
	

	public static final int RULE_AUTOPRINTADDRLABEL_MUST_BE_A_NUMBER = 92;
	public static final int RULE_AUTOPRINTCARTONCONTENT_MUST_BE_A_NUMBER = 93;
	
	//Greater than zero rules
	public static final int RULE_AISLEEND_GREATER_THAN_ZERO = 15;
	public static final int RULE_AISLESTART_GREATER_THAN_ZERO = 16;
	
	public static final int RULE_LABORMAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO = 17;
	public static final int RULE_LABORMAXCUBE_GREATER_THAN_OR_EQUAL_ZERO = 18;
	public static final int RULE_LABORMAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 19;
	public static final int RULE_MAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO = 20;
	public static final int RULE_MAXCUBE_GREATER_THAN_OR_EQUAL_ZERO = 21;
	public static final int RULE_MAXPALLETSPERSKU_GREATER_THAN_OR_EQUAL_ZERO = 22;
	public static final int RULE_MAXPICKINGCONTAINERS_GREATER_THAN_OR_EQUAL_ZERO = 23;
	public static final int RULE_MAXPICKLINES_GREATER_THAN_OR_EQUAL_ZERO = 24;
	public static final int RULE_MAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 25;
	public static final int RULE_QTYCC_GREATER_THAN_OR_EQUAL_ZERO = 26;
	
	public static final int RULE_SLOTEND_GREATER_THAN_ZERO = 27;
	public static final int RULE_SLOTSTART_GREATER_THAN_ZERO = 28;
	
	public static final int RULE_AUTOPRINTADDRLABEL_MUST_BE_ZERO_OR_ONE = 95;
	public static final int RULE_AUTOPRINTCARTONCONTENT_MUST_BE_ZERO_OR_ONE = 96;
	
	
	//Unique
	public static final int RULE_PUTAWAYZONE_MUST_BE_UNIQUE = 29;
	
	//Required fields rules
	public static final int RULE_AISLEEND_NOT_EMPTY = 30;
	public static final int RULE_AISLESTART_NOT_EMPTY = 31;
	public static final int RULE_CREATEASSIGNMENTS_NOT_EMPTY = 32;
	public static final int RULE_DESCR_NOT_EMPTY = 33;
	public static final int RULE_INLOC_NOT_EMPTY = 34;
	public static final int RULE_LABORMAXCASECNT_NOT_EMPTY = 35;
	public static final int RULE_LABORMAXCUBE_NOT_EMPTY = 36;
	public static final int RULE_LABORMAXWEIGHT_NOT_EMPTY = 37;
	public static final int RULE_MAXCASECNT_NOT_EMPTY = 38;
	public static final int RULE_MAXCUBE_NOT_EMPTY = 39;
	public static final int RULE_MAXPALLETSPERSKU_NOT_EMPTY = 40;
	public static final int RULE_MAXPICKINGCONTAINERS_NOT_EMPTY = 41;
	public static final int RULE_MAXPICKLINES_NOT_EMPTY = 42;
	public static final int RULE_MAXWEIGHT_NOT_EMPTY = 43;
	public static final int RULE_OUTLOC_NOT_EMPTY = 44;
	public static final int RULE_PICKCC_NOT_EMPTY = 45;
	public static final int RULE_POSVERMETHOD_NOT_EMPTY = 46;
	public static final int RULE_PUTAWAYZONE_NOT_EMPTY = 47;
	public static final int RULE_QTYCC_NOT_EMPTY = 48;
	public static final int RULE_REPLENISHMENTMETHOD_NOT_EMPTY = 49;
	public static final int RULE_SLOTEND_NOT_EMPTY = 50;
	public static final int RULE_SLOTSTART_NOT_EMPTY = 51;
	public static final int RULE_UOM1PICKMETHOD_NOT_EMPTY = 52;
	public static final int RULE_UOM2PICKMETHOD_NOT_EMPTY = 53;
	public static final int RULE_UOM3PICKMETHOD_NOT_EMPTY = 54;
	public static final int RULE_UOM4PICKMETHOD_NOT_EMPTY = 55;
	public static final int RULE_UOM5PICKMETHOD_NOT_EMPTY = 56;
	public static final int RULE_UOM6PICKMETHOD_NOT_EMPTY = 57;
	public static final int RULE_ZONEBREAK_NOT_EMPTY = 58;
	
	//Length rules
	public static final int RULE_LENGTH_CLEAN_LOCATION_1 = 59;
	public static final int RULE_LENGTH_CREATEASSIGNMENTS_1 = 60;
	public static final int RULE_LENGTH_DESCR_60 = 61;
	public static final int RULE_LENGTH_INLOC_10 = 62;
	public static final int RULE_LENGTH_OUTLOC_10 = 63;
	public static final int RULE_LENGTH_PICKCC_1 = 64;
	public static final int RULE_LENGTH_PICKTOLOC_10 = 65;
	public static final int RULE_LENGTH_POSVERMETHOD_10 = 66;
	public static final int RULE_LENGTH_PUTAWAYZONE_10 = 67;
	public static final int RULE_LENGTH_REPLENISHMENT_FLAG_1 = 68;
	public static final int RULE_LENGTH_REPLENISHMENT_HOTLEVEL_1 = 69;
	public static final int RULE_LENGTH_REPLENISHMENTMETHOD_10 = 70;
	public static final int RULE_LENGTH_TOP_OFF_1 = 71;
	public static final int RULE_LENGTH_UOM1PICKMETHOD_1 = 72;
	public static final int RULE_LENGTH_UOM2PICKMETHOD_1 = 73;
	public static final int RULE_LENGTH_UOM3PICKMETHOD_1 = 74;
	public static final int RULE_LENGTH_UOM4PICKMETHOD_1 = 75;
	public static final int RULE_LENGTH_UOM5PICKMETHOD_1 = 76;
	public static final int RULE_LENGTH_UOM6PICKMETHOD_1 = 77;
	public static final int RULE_LENGTH_WHSEID_30 = 78;
	public static final int RULE_LENGTH_ZONEBREAK_1 = 79;
	
	public static final int RULE_LENGTH_DEFAULTLABELPRINTER_45 = 97;
	public static final int RULE_LENGTH_DEFAULTREPORTPRINTER_45 = 98;
	public static final int RULE_LENGTH_ABANDONLOC_10 = 99;
	
	//Attribute domain rules
	public static final int RULE_ATTR_DOM_PICKCC = 80;
	public static final int RULE_ATTR_DOM_POSVERMETHOD = 81;
	public static final int RULE_ATTR_DOM_REPLENISHMENT_HOTLEVEL = 82;
	public static final int RULE_ATTR_DOM_TOP_OFF = 83;
	public static final int RULE_ATTR_DOM_UOM1PICKMETHOD  = 84; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_UOM2PICKMETHOD  = 85; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_UOM3PICKMETHOD  = 86; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_UOM4PICKMETHOD  = 87; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_UOM5PICKMETHOD  = 88; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_UOM6PICKMETHOD  = 89; //listname=PICKMETHOD
	public static final int RULE_ATTR_DOM_REPLENISHMENTMETHOD = 90; //listname ='REPLNMTHD'
	public static final int RULE_ATTR_DOM_PICKTOLOC = 91; 

	
	//Validations
	public ArrayList validate(ZoneScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		ArrayList errors = new ArrayList();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doAssumeDefaults = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ASSUME_DEFAULTS);
		
		
		//Validate Field Lengths
		if(doCheckFieldLength){			
			

			if (!validateClean_locationLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CLEAN_LOCATION_1));
			}
			
			if (!validateCreateassignmentsLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_CREATEASSIGNMENTS_1));
			}
			

			if (!validateDescrLengthIs60OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DESCR_60));
			}

			if (!validateInlocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_INLOC_10));
			}

			if (!validateOutlocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_OUTLOC_10));
			}

			if (!validatePickccLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PICKCC_1));
			}

			if (!validatePicktolocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PICKTOLOC_10));
			}

			if (!validatePosvermethodLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_POSVERMETHOD_10));
			}

			if (!validatePutawayzoneLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_PUTAWAYZONE_10));
			}

			if (!validateReplenishment_flagLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHMENT_FLAG_1));
			}

			if (!validateReplenishment_hotlevelLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHMENT_HOTLEVEL_1));
			}

			if (!validateReplenishmentmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_REPLENISHMENTMETHOD_10));
			}

			if (!validateTop_offLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_TOP_OFF_1));
			}

			if (!validateUom1pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM1PICKMETHOD_1));
			}

			if (!validateUom2pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM2PICKMETHOD_1));
			}

			if (!validateUom3pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM3PICKMETHOD_1));
			}

			if (!validateUom4pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM4PICKMETHOD_1));
			}

			if (!validateUom5pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM5PICKMETHOD_1));
			}

			if (!validateUom6pickmethodLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_UOM6PICKMETHOD_1));
			}

			if (!validateWhseidLengthIs30OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_WHSEID_30));
			}

			if (!validateZonebreakLengthIs1OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ZONEBREAK_1));
			}

			if (!validateDefaultlabelprinterLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTLABELPRINTER_45));
			}

			if (!validateDefaultreportprinterLengthIs45OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_DEFAULTREPORTPRINTER_45));
			}

			if (!validateAbandonlocLengthIs10OrLess(screen)){
				errors.add(new Integer(RULE_LENGTH_ABANDONLOC_10));
			}

		}
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
			
			if(!validatePickccInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PICKCC  ));		
			}
	
			if(!validatePosvermethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_POSVERMETHOD  ));		
			}

			if(!validateReplenishment_hotlevelInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_REPLENISHMENT_HOTLEVEL  ));		
			}
			
			if(!validateTop_offInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_TOP_OFF  ));		
			}

			if(!validateUom1pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM1PICKMETHOD  ));		
			}
			

			if(!validateUom2pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM2PICKMETHOD  ));		
			}

			if(!validateUom3pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM3PICKMETHOD  ));		
			}

			if(!validateUom4pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM4PICKMETHOD  ));		
			}

			if(!validateUom5pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM5PICKMETHOD  ));		
			}

			if(!validateUom6pickmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_UOM6PICKMETHOD  ));		
			}

			if(!validateReplenishmentmethodInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_REPLENISHMENTMETHOD  ));		
			}

			if (doCheckRequiredFields && !validatePicktolocNotEmpty(screen)){
				if (validatePicktolocInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_PICKTOLOC));
				}
			}

		}
		
		
		
		if(doCheckRequiredFields && !validateAisleendNotEmpty(screen))
			errors.add(new Integer(RULE_AISLEEND_NOT_EMPTY));
		else{
			if(!validateAisleendIsANumber(screen))
				errors.add(new Integer(RULE_AISLEEND_MUST_BE_A_NUMBER));
			else{
				if(!validateAisleendGreaterThanZero(screen))
					errors.add(new Integer(RULE_AISLEEND_GREATER_THAN_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateAislestartNotEmpty(screen))
			errors.add(new Integer(RULE_AISLESTART_NOT_EMPTY));
		else{
			if(!validateAislestartIsANumber(screen))
				errors.add(new Integer(RULE_AISLESTART_MUST_BE_A_NUMBER));
			else{
				if(!validateAislestartGreaterThanZero(screen))
					errors.add(new Integer(RULE_AISLESTART_GREATER_THAN_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLabormaxcasecntNotEmpty(screen))
			errors.add(new Integer(RULE_LABORMAXCASECNT_NOT_EMPTY));
		else{
			if(!validateLabormaxcasecntIsANumber(screen))
				errors.add(new Integer(RULE_LABORMAXCASECNT_MUST_BE_A_NUMBER));
			else{
				if(!validateLabormaxcasecntGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LABORMAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLabormaxcubeNotEmpty(screen))
			errors.add(new Integer(RULE_LABORMAXCUBE_NOT_EMPTY));
		else{
			if(!validateLabormaxcubeIsANumber(screen))
				errors.add(new Integer(RULE_LABORMAXCUBE_MUST_BE_A_NUMBER));
			else{
				if(!validateLabormaxcubeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LABORMAXCUBE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateLabormaxweightNotEmpty(screen))
			errors.add(new Integer(RULE_LABORMAXWEIGHT_NOT_EMPTY));
		else{
			if(!validateLabormaxweightIsANumber(screen))
				errors.add(new Integer(RULE_LABORMAXWEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateLabormaxweightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LABORMAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(doCheckRequiredFields && !validateMaxcasecntNotEmpty(screen))
			errors.add(new Integer(RULE_MAXCASECNT_NOT_EMPTY));
		else{
			if(!validateMaxcasecntIsANumber(screen))
				errors.add(new Integer(RULE_MAXCASECNT_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxcasecntGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateMaxcubeNotEmpty(screen))
			errors.add(new Integer(RULE_MAXCUBE_NOT_EMPTY));
		else{
			if(!validateMaxcubeIsANumber(screen))
				errors.add(new Integer(RULE_MAXCUBE_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxcubeGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXCUBE_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && !validateMaxpalletsperskuNotEmpty(screen))
			errors.add(new Integer(RULE_MAXPALLETSPERSKU_NOT_EMPTY));
		else{
			if(!validateMaxpalletsperskuIsANumber(screen))
				errors.add(new Integer(RULE_MAXPALLETSPERSKU_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxpalletsperskuGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXPALLETSPERSKU_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && !validateMaxpickingcontainersNotEmpty(screen))
			errors.add(new Integer(RULE_MAXPICKINGCONTAINERS_NOT_EMPTY));
		else{
			if(!validateMaxpickingcontainersIsANumber(screen))
				errors.add(new Integer(RULE_MAXPICKINGCONTAINERS_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxpickingcontainersGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXPICKINGCONTAINERS_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		
		if(doCheckRequiredFields && !validateMaxpicklinesNotEmpty(screen))
			errors.add(new Integer(RULE_MAXPICKLINES_NOT_EMPTY));
		else{
			if(!validateMaxpicklinesIsANumber(screen))
				errors.add(new Integer(RULE_MAXPICKLINES_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxpicklinesGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXPICKLINES_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateMaxweightNotEmpty(screen))
			errors.add(new Integer(RULE_MAXWEIGHT_NOT_EMPTY));
		else{
			if(!validateMaxweightIsANumber(screen))
				errors.add(new Integer(RULE_MAXWEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateMaxweightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_MAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateQtyccNotEmpty(screen))
			errors.add(new Integer(RULE_QTYCC_NOT_EMPTY));
		else{
			if(!validateQtyccIsANumber(screen))
				errors.add(new Integer(RULE_QTYCC_MUST_BE_A_NUMBER));
			else{
				if(!validateQtyccGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_QTYCC_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateSlotendNotEmpty(screen))
			errors.add(new Integer(RULE_SLOTEND_NOT_EMPTY));
		else{
			if(!validateSlotendIsANumber(screen))
				errors.add(new Integer(RULE_SLOTEND_MUST_BE_A_NUMBER));
			else{
				if(!validateSlotendGreaterThanZero(screen))
					errors.add(new Integer(RULE_SLOTEND_GREATER_THAN_ZERO));
			}
		}


		if(doCheckRequiredFields && !validateSlotstartNotEmpty(screen))
			errors.add(new Integer(RULE_SLOTSTART_NOT_EMPTY));
		else{
			if(!validateSlotstartIsANumber(screen))
				errors.add(new Integer(RULE_SLOTSTART_MUST_BE_A_NUMBER));
			else{
				if(!validateSlotstartGreaterThanZero(screen))
					errors.add(new Integer(RULE_SLOTSTART_GREATER_THAN_ZERO));
			}
		}

		//jp.bugaware.9437.begin
		if(validateAutoprintaddrlabelNotEmpty(screen)){
			if(!validateAutoprintaddrlabelIsANumber(screen))
				errors.add(new Integer(RULE_AUTOPRINTADDRLABEL_MUST_BE_A_NUMBER));
			else{
				if(!validateAutoprintaddrlabelZeroOrOne(screen))
					errors.add(new Integer(RULE_AUTOPRINTADDRLABEL_MUST_BE_ZERO_OR_ONE));
			}
		}
		
		if(validateAutoprintcartoncontentNotEmpty(screen)){
			if(!validateAutoprintcartoncontentIsANumber(screen))
				errors.add(new Integer(RULE_AUTOPRINTCARTONCONTENT_MUST_BE_A_NUMBER));
			else{
				if(!validateAutoprintcartoncontentZeroOrOne(screen))
					errors.add(new Integer(RULE_AUTOPRINTCARTONCONTENT_MUST_BE_ZERO_OR_ONE));
			}
		}

		//jp.bugaware.9437.end
		if(doCheckRequiredFields && !validateCreateassignmentsNotEmpty(screen))
			errors.add(new Integer(RULE_CREATEASSIGNMENTS_NOT_EMPTY));

		if(doCheckRequiredFields && !validateDescrNotEmpty(screen))
			errors.add(new Integer(RULE_DESCR_NOT_EMPTY));

		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateInlocNotEmpty(screen))
				errors.add(new Integer(RULE_INLOC_NOT_EMPTY));
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateOutlocNotEmpty(screen))
				errors.add(new Integer(RULE_OUTLOC_NOT_EMPTY));
		}
		if(doCheckRequiredFields && !validatePickccNotEmpty(screen))
			errors.add(new Integer(RULE_PICKCC_NOT_EMPTY));

		if(doCheckRequiredFields && !validatePosvermethodNotEmpty(screen))
			errors.add(new Integer(RULE_POSVERMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validatePutawayzoneNotEmpty(screen))
			errors.add(new Integer(RULE_PUTAWAYZONE_NOT_EMPTY));

		if(doCheckRequiredFields && !validateReplenishmentmethodNotEmpty(screen))
			errors.add(new Integer(RULE_REPLENISHMENTMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom1pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM1PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom2pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM2PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom3pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM3PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom4pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM4PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom5pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM5PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateUom6pickmethodNotEmpty(screen))
			errors.add(new Integer(RULE_UOM6PICKMETHOD_NOT_EMPTY));

		if(doCheckRequiredFields && !validateZonebreakNotEmpty(screen))
			errors.add(new Integer(RULE_ZONEBREAK_NOT_EMPTY));


		if(isInsert) {
			if(doCheckRequiredFields && !validatePutawayzoneNotEmpty(screen))
				errors.add(new Integer(RULE_PUTAWAYZONE_NOT_EMPTY));
			else{
				if(validatePutawayzoneDoesExist(screen.getPutawayzone(), getContext()))
					errors.add(new Integer(RULE_PUTAWAYZONE_MUST_BE_UNIQUE));
			}
		}

		return errors;
	}
	
	
	

	
	
	//Attribute validations
	private boolean validatePickccInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getPickcc()))
			return true;
		return validatePickccDoesExist(screen.getPickcc(), getContext());
	}

	protected static boolean validatePickccDoesExist(String pickcc, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKCC", pickcc, context).getSize() == 0)
			return false;
		else
			return true;	
	}


	private boolean validatePosvermethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getPosvermethod()))
			return true;
		return validatePosvermethodDoesExist(screen.getPosvermethod(), getContext());
	}

	protected static boolean validatePosvermethodDoesExist(String posvermethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PVTYPE", posvermethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	private boolean validateReplenishment_hotlevelInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getReplenishment_hotlevel()))
			return true;
		return validateReplenishment_hotlevelDoesExist(screen.getReplenishment_hotlevel(), getContext());
	}

	protected static boolean validateReplenishment_hotlevelDoesExist(String replenishmenthotlevel, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("TMPRIORITY", replenishmenthotlevel,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	private boolean validateTop_offInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getTop_off()))
			return true;
		return validateTop_offDoesExist(screen.getTop_off(), getContext());
	}

	protected static boolean validateTop_offDoesExist(String topoff, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("TOPOFF", topoff,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	private boolean validateUom1pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom1pickmethod()))
			return true;
		return validateUom1pickmethodDoesExist(screen.getUom1pickmethod(), getContext());
	}

	protected static boolean validateUom1pickmethodDoesExist(String uom1pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom1pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	


	private boolean validateUom2pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom2pickmethod()))
			return true;
		return validateUom2pickmethodDoesExist(screen.getUom2pickmethod(), getContext());
	}

	protected static boolean validateUom2pickmethodDoesExist(String uom2pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom2pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	


	private boolean validateUom3pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom3pickmethod()))
			return true;
		return validateUom3pickmethodDoesExist(screen.getUom3pickmethod(), getContext());
	}

	protected static boolean validateUom3pickmethodDoesExist(String uom3pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom3pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	

	private boolean validateUom4pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom4pickmethod()))
			return true;
		return validateUom4pickmethodDoesExist(screen.getUom4pickmethod(), getContext());
	}

	protected static boolean validateUom4pickmethodDoesExist(String uom4pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom4pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	

	private boolean validateUom5pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom5pickmethod()))
			return true;
		return validateUom5pickmethodDoesExist(screen.getUom5pickmethod(), getContext());
	}

	protected static boolean validateUom5pickmethodDoesExist(String uom5pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom5pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	


	private boolean validateUom6pickmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getUom6pickmethod()))
			return true;
		return validateUom6pickmethodDoesExist(screen.getUom6pickmethod(), getContext());
	}

	protected static boolean validateUom6pickmethodDoesExist(String uom6pickmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("PICKMETHOD", uom6pickmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	

	
	
	private boolean validateReplenishmentmethodInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getReplenishmentmethod()))
			return true;
		return validateReplenishmentmethodDoesExist(screen.getReplenishmentmethod(), getContext());
	}

	protected static boolean validateReplenishmentmethodDoesExist(String replenishmentmethod, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("REPLNMTHD", replenishmentmethod,context).getSize() == 0)
			return false;
		else
			return true;	
	}


	
	private boolean validatePicktolocInAttrDom(ZoneScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getPicktoloc()))
			return true;
		return validatePicktolocDoesExist(screen.getReplenishmentmethod(), getContext());
	}

	protected static boolean validatePicktolocDoesExist(String picktoloc, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getPicktoStagedLocationByKey(picktoloc, context).getSize() == 0)
			return false;
		else
			return true;	
	}





	//Length validations
	private boolean validateWhseidLengthIs30OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getWhseid()))
			return true;
		return screen.getWhseid().length() < 31;			
	}


	private boolean validateUom1pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom1pickmethod()))
			return true;
		return screen.getUom1pickmethod().length() < 2;			
	}

	private boolean validateUom2pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom2pickmethod()))
			return true;
		return screen.getUom2pickmethod().length() < 2;			
	}


	private boolean validateUom3pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom3pickmethod()))
			return true;
		return screen.getUom3pickmethod().length() < 2;			
	}


	private boolean validateUom4pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom4pickmethod()))
			return true;
		return screen.getUom4pickmethod().length() < 2;			
	}


	private boolean validateUom5pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom5pickmethod()))
			return true;
		return screen.getUom5pickmethod().length() < 2;			
	}



	private boolean validateUom6pickmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getUom6pickmethod()))
			return true;
		return screen.getUom6pickmethod().length() < 2;			
	}


	private boolean validateTop_offLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getTop_off()))
			return true;
		return screen.getTop_off().length() < 2;			
	}


	private boolean validateReplenishmentmethodLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getReplenishmentmethod()))
			return true;
		return screen.getReplenishmentmethod().length() < 2;			

	}

	private boolean validateReplenishment_hotlevelLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getReplenishment_hotlevel()))
			return true;
		return screen.getReplenishment_hotlevel().length() < 2;			
	}

	private boolean validateReplenishment_flagLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getReplenishment_flag()))
			return true;
		return screen.getReplenishment_flag().length() < 2;			
	}


	private boolean validatePutawayzoneLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getPutawayzone()))
			return true;
		return screen.getPutawayzone().length() < 11;			
	}



	private boolean validatePosvermethodLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getPosvermethod()))
			return true;
		return screen.getPosvermethod().length() <11;			
	}


	private boolean validatePicktolocLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getPicktoloc()))
			return true;
		return screen.getPicktoloc().length() < 11;			
	}


	private boolean validatePickccLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getPickcc()))
			return true;
		return screen.getPickcc().length() < 2;			
	}


	private boolean validateInlocLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getInloc()))
			return true;
		return screen.getInloc().length() < 11;			
	}






	private boolean validateOutlocLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getOutloc()))
			return true;
		return screen.getOutloc().length() < 11;			
	}






	private boolean validateDescrLengthIs60OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getDescr()))
			return true;
		return screen.getDescr().length() < 61;			
	}


	private boolean validateZonebreakLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getZonebreak()))
			return true;
		return screen.getZonebreak().length() < 2;			
	}










	private boolean validateCreateassignmentsLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getCreateassignments()))
			return true;
		return screen.getCreateassignments().length() < 2;			
	}






	private boolean validateClean_locationLengthIs1OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getClean_location()))
			return true;
		return screen.getClean_location().length() < 2;			
	}

	

	private boolean validateDefaultlabelprinterLengthIs45OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getDefaultlabelprinter()))
			return true;
		return screen.getDefaultlabelprinter().length() < 46;			
	}
	
	private boolean validateDefaultreportprinterLengthIs45OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getDefaultreportprinter()))
			return true;
		return screen.getDefaultreportprinter().length() < 46;			
	}
	
	private boolean validateAbandonlocLengthIs10OrLess(ZoneScreenVO screen) {
		if(isEmpty(screen.getAbandonloc()))
			return true;
		return screen.getAbandonloc().length() < 11;			
	}

	//Not empty
	private boolean validateAisleendNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getAisleend()))
			return false;
		
		return true;
		
	}

	private boolean validateAislestartNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getAislestart()))
			return false;
		
		return true;
		
	}

	private boolean validateCreateassignmentsNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getCreateassignments()))
			return false;
		
		return true;
		
	}

	private boolean validateDescrNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getDescr()))
			return false;
		
		return true;
		
	}

	private boolean validateInlocNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getInloc()))
			return false;
		
		return true;
		
	}
	
	private boolean validateLabormaxcasecntNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcasecnt()))
			return false;
		
		return true;
		
	}

	private boolean validateLabormaxcubeNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcube()))
			return false;
		
		return true;
		
	}

	private boolean validateLabormaxweightNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxweight()))
			return false;
		
		return true;
		
	}

	private boolean validateMaxcasecntNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcasecnt()))
			return false;
		
		return true;
		
	}


	private boolean validateMaxcubeNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcube()))
			return false;
		
		return true;
		
	}

	private boolean validateMaxpalletsperskuNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpalletspersku()))
			return false;
		
		return true;
		
	}

	private boolean validateMaxpickingcontainersNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpickingcontainers()))
			return false;
		
		return true;
		
	}

	private boolean validateMaxpicklinesNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpicklines()))
			return false;
		
		return true;
		
	}

	private boolean validateMaxweightNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxweight()))
			return false;
		
		return true;
		
	}


	private boolean validateOutlocNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getOutloc()))
			return false;
		
		return true;
		
	}

	private boolean validatePickccNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getPickcc()))
			return false;
		
		return true;
		
	}

	private boolean validatePosvermethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getPosvermethod()))
			return false;
		
		return true;
		
	}


	private boolean validatePutawayzoneNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getPutawayzone()))
			return false;
		
		return true;
		
	}

	private boolean validateQtyccNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getQtycc()))
			return false;
		
		return true;
		
	}

	private boolean validateReplenishmentmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getReplenishmentmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateSlotendNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotend()))
			return false;
		
		return true;
		
	}
	
	private boolean validateSlotstartNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotstart()))
			return false;
		
		return true;
		
	}


	private boolean validateUom1pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom1pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateUom2pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom2pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateUom3pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom3pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateUom4pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom4pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateUom5pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom5pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateUom6pickmethodNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getUom6pickmethod()))
			return false;
		
		return true;
		
	}

	private boolean validateZonebreakNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getZonebreak()))
			return false;
		
		return true;
		
	}

	private boolean validatePicktolocNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getPicktoloc()))
			return false;
		
		return true;
		
	}

	private boolean validateAutoprintaddrlabelNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getAutoprintaddrlabel()))
			return false;
		
		return true;
		
	}
	
	private boolean validateAutoprintcartoncontentNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getAutoprintcartoncontent()))
			return false;
		
		return true;
		
	}
	
	private boolean validateDefaultlabelprinterNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getDefaultlabelprinter()))
			return false;
		
		return true;
		
	}

	private boolean validateDefaultreportprinterNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getDefaultreportprinter()))
			return false;
		
		return true;
		
	}

	private boolean validateAbandonlocNotEmpty(ZoneScreenVO screen){
		if(isEmpty(screen.getAbandonloc()))
			return false;
		
		return true;
		
	}

	//It's a number
	private boolean validateAisleendIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getAisleend()))
			return true;
		return isNumber(screen.getAisleend());
	}
	

	private boolean validateAislestartIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getAislestart()))
			return true;
		return isNumber(screen.getAislestart());
	}

	private boolean validateLabormaxcasecntIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcasecnt()))
			return true;
		return isNumber(screen.getLabormaxcasecnt());
	}

	private boolean validateLabormaxcubeIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcube()))
			return true;
		return isNumber(screen.getLabormaxcube());
	}

	private boolean validateLabormaxweightIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxweight()))
			return true;
		return isNumber(screen.getLabormaxweight());
	}

	private boolean validateMaxcasecntIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcasecnt()))
			return true;
		return isNumber(screen.getMaxcasecnt());
	}

	private boolean validateMaxcubeIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcube()))
			return true;
		return isNumber(screen.getMaxcube());
	}

	private boolean validateMaxpalletsperskuIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpalletspersku()))
			return true;
		return isNumber(screen.getMaxpalletspersku());
	}

	private boolean validateMaxpickingcontainersIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpickingcontainers()))
			return true;
		return isNumber(screen.getMaxpickingcontainers());
	}

	private boolean validateMaxpicklinesIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpicklines()))
			return true;
		return isNumber(screen.getMaxpicklines());
	}

	private boolean validateMaxweightIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxweight()))
			return true;
		return isNumber(screen.getMaxweight());
	}

	private boolean validateQtyccIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getQtycc()))
			return true;
		return isNumber(screen.getQtycc());
	}

	private boolean validateSlotendIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotend()))
			return true;
		return isNumber(screen.getSlotend());
	}

	private boolean validateSlotstartIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotstart()))
			return true;
		return isNumber(screen.getSlotstart());
	}

	private boolean validateAutoprintaddrlabelIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getAutoprintaddrlabel()))
			return true;
		return isNumber(screen.getAutoprintaddrlabel());
	}

	private boolean validateAutoprintcartoncontentIsANumber(ZoneScreenVO screen){
		if(isEmpty(screen.getAutoprintcartoncontent()))
			return true;
		return isNumber(screen.getAutoprintcartoncontent());
	}

	
	//Greater than or equal to zero
	private boolean validateAisleendGreaterThanZero(ZoneScreenVO screen){
		if(isEmpty(screen.getAisleend()))
			return true;
		return greaterThanZeroValidation(screen.getAisleend());
	}

	private boolean validateAislestartGreaterThanZero(ZoneScreenVO screen){
		if(isEmpty(screen.getAislestart()))
			return true;
		return greaterThanZeroValidation(screen.getAislestart());
	}

	private boolean validateLabormaxcasecntGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcasecnt()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLabormaxcasecnt());
	}

	private boolean validateLabormaxcubeGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxcube()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLabormaxcube());
	}

	private boolean validateLabormaxweightGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getLabormaxweight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLabormaxweight());
	}

	private boolean validateMaxcasecntGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcasecnt()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxcasecnt());
	}

	private boolean validateMaxcubeGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxcube()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxcube());
	}

	private boolean validateMaxpalletsperskuGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpalletspersku()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxpalletspersku());
	}

	
	private boolean validateMaxpickingcontainersGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpickingcontainers()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxpickingcontainers());
	}


	private boolean validateMaxpicklinesGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxpicklines()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxpicklines());
	}

	private boolean validateMaxweightGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getMaxweight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getMaxweight());
	}

	private boolean validateQtyccGreaterThanOrEqualZero(ZoneScreenVO screen){
		if(isEmpty(screen.getQtycc()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getQtycc());
	}


	private boolean validateSlotendGreaterThanZero(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotend()))
			return true;
		return greaterThanZeroValidation(screen.getSlotend());
	}

	private boolean validateSlotstartGreaterThanZero(ZoneScreenVO screen){
		if(isEmpty(screen.getSlotstart()))
			return true;
		return greaterThanZeroValidation(screen.getSlotstart());
	}

	
	//Zero Or One validation
	public boolean validateAutoprintaddrlabelZeroOrOne(ZoneScreenVO screen){
		return isZeroOrOneValidation(screen.getAutoprintaddrlabel());
	}
	
	public boolean validateAutoprintcartoncontentZeroOrOne(ZoneScreenVO screen){
		return isZeroOrOneValidation(screen.getAutoprintcartoncontent());
	}

	protected static boolean validatePutawayzoneDoesNotExist(String putawayzone, WMSValidationContext context) throws WMSDataLayerException{
		return !validatePutawayzoneDoesExist(putawayzone, context);
	}

	protected static boolean validatePutawayzoneDoesExist(String putawayzone, WMSValidationContext context) throws WMSDataLayerException{
		if(PutawayZoneQueryRunner.getPutawayZoneByPutawayZone(putawayzone,context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	public static String getErrorMessage(int errorCode, Locale locale, ZoneScreenVO zoneScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){
		case RULE_AISLEEND_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLEEND, locale));

		case RULE_AISLESTART_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLESTART, locale));

		case RULE_LABORMAXCASECNT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCASECNT, locale));

		case RULE_LABORMAXCUBE_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCUBE, locale));

		case RULE_LABORMAXWEIGHT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXWEIGHT, locale));

		case RULE_MAXCASECNT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCASECNT, locale));

		case RULE_MAXCUBE_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCUBE, locale));

		case RULE_MAXWEIGHT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXWEIGHT, locale));

		case RULE_MAXPALLETSPERSKU_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPALLETSPERSKU, locale));

		case RULE_MAXPICKINGCONTAINERS_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKINGCONTAINERS, locale));

		case RULE_MAXPICKLINES_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKLINES, locale));

		case RULE_QTYCC_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_QTYCC, locale));

		case RULE_SLOTEND_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTEND, locale));

		case RULE_SLOTSTART_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTSTART, locale));

		case RULE_AUTOPRINTADDRLABEL_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, 
					 MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AUTOPRINTADDRLABEL, locale));
			 
		case RULE_AUTOPRINTCARTONCONTENT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, 
					 MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AUTOPRINTCARTONCONTENT, locale));
			 
		case RULE_AISLEEND_GREATER_THAN_ZERO:
			 return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLEEND, locale));

		case RULE_AISLESTART_GREATER_THAN_ZERO:
			 return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLESTART, locale));

		case RULE_LABORMAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCASECNT, locale));
			 
		case RULE_LABORMAXCUBE_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCUBE, locale));

		case RULE_LABORMAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXWEIGHT, locale));

		case RULE_MAXCASECNT_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCASECNT, locale));
			 
		case RULE_MAXCUBE_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCUBE, locale));

		case RULE_MAXWEIGHT_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXWEIGHT, locale));


		case RULE_MAXPALLETSPERSKU_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPALLETSPERSKU, locale));

		case RULE_MAXPICKINGCONTAINERS_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKINGCONTAINERS, locale));

		case RULE_MAXPICKLINES_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKLINES, locale));

		case RULE_QTYCC_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_QTYCC, locale));

		case RULE_SLOTEND_GREATER_THAN_ZERO:
			 return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTEND, locale));

		case RULE_SLOTSTART_GREATER_THAN_ZERO:
			 return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTSTART, locale));

		case RULE_AUTOPRINTADDRLABEL_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AUTOPRINTADDRLABEL, locale), 
					zoneScreen.getAutoprintaddrlabel());
			
		case RULE_AUTOPRINTCARTONCONTENT_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AUTOPRINTCARTONCONTENT, locale), 
					zoneScreen.getAutoprintcartoncontent());

		//Unique
		case RULE_PUTAWAYZONE_MUST_BE_UNIQUE:
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PUTAWAYZONE, locale);
			param[1] = zoneScreen.getPutawayzone();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ZONE_SCREEN_ERROR_DUPLICATE_PUTAWAYZONE, locale, param);


		case RULE_AISLEEND_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLEEND, locale));

		case RULE_AISLESTART_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_AISLESTART, locale));

		case RULE_CREATEASSIGNMENTS_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_CREATEASSIGNMENTS, locale));

		case RULE_DESCR_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_DESCR, locale));

		case RULE_INLOC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_INLOC, locale));

		case RULE_LABORMAXCASECNT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCASECNT, locale));

		case RULE_LABORMAXCUBE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXCUBE, locale));

		case RULE_LABORMAXWEIGHT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_LABORMAXWEIGHT, locale));

		case RULE_MAXCASECNT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCASECNT, locale));

		case RULE_MAXCUBE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXCUBE, locale));

		case RULE_MAXWEIGHT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXWEIGHT, locale));

		case RULE_MAXPALLETSPERSKU_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPALLETSPERSKU, locale));

		case RULE_MAXPICKINGCONTAINERS_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKINGCONTAINERS, locale));

		case RULE_MAXPICKLINES_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_MAXPICKLINES, locale));

		case RULE_OUTLOC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_OUTLOC, locale));

		case RULE_PICKCC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PICKCC, locale));

		case RULE_POSVERMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_POSVERMETHOD, locale));

		case RULE_PUTAWAYZONE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PUTAWAYZONE, locale));

		case RULE_QTYCC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_QTYCC, locale));

		case RULE_REPLENISHMENTMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENTMETHOD, locale));

		case RULE_SLOTEND_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTEND, locale));

		case RULE_SLOTSTART_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_SLOTSTART, locale));

		case RULE_UOM1PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM1PICKMETHOD, locale));

		case RULE_UOM2PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM2PICKMETHOD, locale));

		case RULE_UOM3PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM3PICKMETHOD, locale));

		case RULE_UOM4PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM4PICKMETHOD, locale));

		case RULE_UOM5PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM5PICKMETHOD, locale));

		case RULE_UOM6PICKMETHOD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM6PICKMETHOD, locale));

		case RULE_ZONEBREAK_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_ZONEBREAK, locale));

			//Length rules

		case RULE_LENGTH_CLEAN_LOCATION_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_CLEAN_LOCATION, locale), "1");

		case RULE_LENGTH_CREATEASSIGNMENTS_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_CREATEASSIGNMENTS, locale), "1");
			
		case RULE_LENGTH_DESCR_60:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_DESCR, locale), "60");

		case RULE_LENGTH_INLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_INLOC, locale), "10");

		case RULE_LENGTH_OUTLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_OUTLOC, locale), "10");

		case RULE_LENGTH_PICKCC_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PICKCC, locale), "1");

		case RULE_LENGTH_PICKTOLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PICKTOLOC, locale), "10");

		case RULE_LENGTH_PUTAWAYZONE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PUTAWAYZONE, locale), "10");

		case RULE_LENGTH_POSVERMETHOD_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_POSVERMETHOD, locale), "10");

		case RULE_LENGTH_REPLENISHMENT_FLAG_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_FLAG, locale), "1");

		case RULE_LENGTH_REPLENISHMENT_HOTLEVEL_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_HOTLEVEL, locale), "1");

		case RULE_LENGTH_REPLENISHMENTMETHOD_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENTMETHOD, locale), "10");
			
		case RULE_LENGTH_TOP_OFF_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_TOP_OFF, locale), "1");
	
		case RULE_LENGTH_UOM1PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM1PICKMETHOD, locale), "1");

		case RULE_LENGTH_UOM2PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM2PICKMETHOD, locale), "1");

		case RULE_LENGTH_UOM3PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM3PICKMETHOD, locale), "1");

		case RULE_LENGTH_UOM4PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM4PICKMETHOD, locale), "1");

		case RULE_LENGTH_UOM5PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM5PICKMETHOD, locale), "1");

		case RULE_LENGTH_UOM6PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM6PICKMETHOD, locale), "1");

		case RULE_LENGTH_WHSEID_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_WHSEID, locale), "30");

		case RULE_LENGTH_ZONEBREAK_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_ZONEBREAK, locale), "1");


		case RULE_LENGTH_DEFAULTLABELPRINTER_45:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_DEFAULTLABELPRINTER, locale), "45");

		case RULE_LENGTH_DEFAULTREPORTPRINTER_45:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_DEFAULTREPORTPRINTER, locale), "45");

		case RULE_LENGTH_ABANDONLOC_10:
			return getLengthErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_ABANDONLOC, locale), "10");

			//Attribute domain 
		case RULE_ATTR_DOM_PICKCC:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PICKCC, locale), zoneScreen.getPickcc());

		case RULE_ATTR_DOM_POSVERMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_POSVERMETHOD, locale), zoneScreen.getPosvermethod());

		case RULE_ATTR_DOM_REPLENISHMENT_HOTLEVEL:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_HOTLEVEL, locale), zoneScreen.getReplenishment_hotlevel());

		case RULE_ATTR_DOM_TOP_OFF:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_HOTLEVEL, locale), zoneScreen.getTop_off());

		case RULE_ATTR_DOM_UOM1PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM1PICKMETHOD, locale), zoneScreen.getUom1pickmethod());

		case RULE_ATTR_DOM_UOM2PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM2PICKMETHOD, locale), zoneScreen.getUom2pickmethod());

		case RULE_ATTR_DOM_UOM3PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM3PICKMETHOD, locale), zoneScreen.getUom3pickmethod());

		case RULE_ATTR_DOM_UOM4PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM4PICKMETHOD, locale), zoneScreen.getUom4pickmethod());

		case RULE_ATTR_DOM_UOM5PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM5PICKMETHOD, locale), zoneScreen.getUom5pickmethod());

		case RULE_ATTR_DOM_UOM6PICKMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_UOM6PICKMETHOD, locale), zoneScreen.getUom6pickmethod());

		case RULE_ATTR_DOM_REPLENISHMENTMETHOD:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_REPLENISHMENTMETHOD, locale), zoneScreen.getReplenishmentmethod());

		case RULE_ATTR_DOM_PICKTOLOC:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_ZONE_FIELD_PICKTOLOC, locale), zoneScreen.getPicktoloc());

		}
		
		return errorMsg;
	}
	
	
	public class PazoneequipmentexcludedetailScreenValidator extends BaseScreenValidator{

		public PazoneequipmentexcludedetailScreenValidator(WMSValidationContext context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		//Numeric rules
		//Unique
		public static final int RULE_PUTAWAYZONE_EQUIPMENTPROFILEKEY_MUST_BE_UNIQUE = 1;
		
		//Required rules
		public static final int RULE_PUTAWAYZONE_NOT_EMPTY = 2;
		public static final int RULE_EQUIPMENTPROFILEKEY_NOT_EMPTY = 3;
		public static final int RULE_DESCR_NOT_EMPTY = 4;
		

		//Lenght rules
		public static final int RULE_LENGTH_PUTAWAYZONE_10 = 5;
		public static final int RULE_LENGTH_EQUIPMENTPROFILEKEY_10 = 6;
		public static final int RULE_LENGTH_DESCR_60 = 7;
		
		//Attribute domain rules
		public static final int RULE_ATTR_DOM_PUTAWAYZONE = 8;
		public static final int RULE_ATTR_DOM_EQUIPMENTPROFILEKEY = 9;
		
		

		//Validations
		public ArrayList validate(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
			ArrayList errors = new ArrayList();
			boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
			boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
			boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
			
			
			//Validate Field Lengths
			if(doCheckFieldLength){			
				


					if (!validateDescrLengthIs60OrLess(screen)){
						errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_LENGTH_DESCR_60));
					}
					if (!validateEquipmentprofilekeyLengthIs10OrLess(screen)){
						errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_LENGTH_EQUIPMENTPROFILEKEY_10));
					}
					if (!validatePutawayzoneLengthIs10OrLess(screen)){
						errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_LENGTH_PUTAWAYZONE_10));
					}

			}
			
			//Validate Attribute Domain
			if(doCheckAttributeDomain){	
				
				if(!validatePutawayzoneInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_PUTAWAYZONE  ));		
				}
				
				if(!validateEquipmentprofilekeyInAttrDom(screen)){
					errors.add(new Integer(RULE_ATTR_DOM_EQUIPMENTPROFILEKEY  ));		
				}

			}
			
			
			//Required 
			if(doCheckRequiredFields && !validatePutawayzoneNotEmpty(screen))
				errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_PUTAWAYZONE_NOT_EMPTY));

			if(doCheckRequiredFields && !validateDescrNotEmpty(screen))
				errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_DESCR_NOT_EMPTY));

			if(doCheckRequiredFields && !validateEquipmentprofilekeyNotEmpty(screen))
				errors.add(new Integer(PazoneequipmentexcludedetailScreenValidator.RULE_EQUIPMENTPROFILEKEY_NOT_EMPTY));


			if(isInsert) {
				if(doCheckRequiredFields && !validatePutawayzoneNotEmpty(screen))
					errors.add(new Integer(RULE_PUTAWAYZONE_NOT_EMPTY));
				else{
					if(doCheckRequiredFields && !validateEquipmentprofilekeyNotEmpty(screen))
						errors.add(new Integer(RULE_EQUIPMENTPROFILEKEY_NOT_EMPTY));
					else{
						if(validatePutawayzoneEquipmentprofilekeyDoesExist(screen.getPutawayzone(), screen.getEquipmentprofilekey(), getContext()))
							errors.add(new Integer(RULE_PUTAWAYZONE_MUST_BE_UNIQUE));
					}
				}
			}

			return errors;
		}//validate
		
		
		private boolean validateDescrLengthIs60OrLess(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getDescr()))
				return true;
			return screen.getDescr().length() < 61;			
			
		}
		private boolean validateEquipmentprofilekeyLengthIs10OrLess(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getEquipmentprofilekey()))
				return true;
			return screen.getEquipmentprofilekey().length() < 11;			
			
			
		}
		private boolean validatePutawayzoneLengthIs10OrLess(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getPutawayzone()))
				return true;
			return screen.getPutawayzone().length() < 11;			
			
		}
		
		//Attribute validations
		private boolean validateEquipmentprofilekeyInAttrDom(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen) throws WMSDataLayerException {
			if(isEmpty(screen.getEquipmentprofilekey()))
				return true;
			return validateEquipmentprofilekeyDoesExist( screen.getEquipmentprofilekey(), getContext());
		}
		
		private boolean validateEquipmentprofilekeyDoesExist( String equipmentprofilekey, WMSValidationContext context) throws WMSDataLayerException{
			if (EquipmentprofileQueryRunner.getEquipmentProfileByKey( equipmentprofilekey, context).getSize() == 0)
				return false;
			else
				return true;	
		}

		private boolean validatePutawayzoneInAttrDom(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen) throws WMSDataLayerException {
			if(isEmpty(screen.getPutawayzone()))
				return true;
			return validatePutawayzoneDoesExist(screen.getPutawayzone(), getContext());
		}

		private boolean validatePutawayzoneDoesExist(String putawayzone, WMSValidationContext context) throws WMSDataLayerException{
			if(PutawayZoneQueryRunner.getPutawayZoneByPutawayZone(putawayzone,context).getSize() == 0)
				return false;
			else
				return true;	
		}

		
		private boolean validatePutawayzoneEquipmentprofilekeyDoesExist(String putawayzone, String equipmentprofilekey, WMSValidationContext context) throws WMSDataLayerException{
			if (PazoneequipmentexcludedetailQueryRunner.getPazoneequipmentexcludedetailByPutawayzoneAndEquipmentprofilekey( putawayzone, equipmentprofilekey, context).getSize() == 0)
				return false;
			else
				return true;	
		}

		//Not empty
		private boolean validatePutawayzoneNotEmpty(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getPutawayzone()))
				return false;
			
			return true;
			
		}

		private boolean validateDescrNotEmpty(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getDescr()))
				return false;
			
			return true;
			
		}

		private boolean validateEquipmentprofilekeyNotEmpty(ZoneScreenVO.PazoneequipmentexcludedetailScreenVO screen){
			if(isEmpty(screen.getEquipmentprofilekey()))
				return false;
			
			return true;
			
		}

		public String getErrorMessage(int errorCode, Locale locale, ZoneScreenVO.PazoneequipmentexcludedetailScreenVO pazoneequipmentexcludedetailScreen){
			String errorMsg = "";
			String param[] = null;
			switch(errorCode){
			case RULE_PUTAWAYZONE_NOT_EMPTY:
				 return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_DESCR,locale));
				 
			case RULE_EQUIPMENTPROFILEKEY_NOT_EMPTY:
				 return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_EQUIPMENTPROFILEKEY,locale));
				 
			case RULE_DESCR_NOT_EMPTY:
				 return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_PUTAWAYZONE,locale));
				 
			case RULE_LENGTH_PUTAWAYZONE_10:
				return getLengthErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_PUTAWAYZONE,locale) ,"10");
				
			case RULE_LENGTH_EQUIPMENTPROFILEKEY_10:
				return getLengthErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_EQUIPMENTPROFILEKEY,locale) ,"10");
				
			case RULE_LENGTH_DESCR_60:
				return getLengthErrorMessage(errorCode, locale,MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_DESCR,locale) ,"60");
				
				//Attribute domain rules
			case RULE_ATTR_DOM_PUTAWAYZONE:
				return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_PUTAWAYZONE,locale),pazoneequipmentexcludedetailScreen.getPutawayzone());
				
			case RULE_ATTR_DOM_EQUIPMENTPROFILEKEY:
				return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_EQUIPMENTPROFILEKEY,locale),pazoneequipmentexcludedetailScreen.getEquipmentprofilekey());
				
				//Unique
			case RULE_PUTAWAYZONE_EQUIPMENTPROFILEKEY_MUST_BE_UNIQUE:
				param = new String[4];
				param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_PUTAWAYZONE, locale);
				param[1] = pazoneequipmentexcludedetailScreen.getPutawayzone();
				param[2] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_EQUIPMENTPROFILEKEY, locale);
				param[3] = pazoneequipmentexcludedetailScreen.getEquipmentprofilekey();

				return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_PAZONEEQUIPMENTEXCLUDE_SCREEN_ERROR_DUPLICATE_PUTAWAYZONEEQUIPMENTPROFILEKEY, locale, param);

			}//switch

			return errorMsg;
		}
	}

}
