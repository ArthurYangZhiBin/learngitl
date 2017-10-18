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
package com.infor.scm.wms.util.validation.screen.location;

import java.util.ArrayList;
import java.util.Locale;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.LocationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PutawayZoneQueryRunner;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.screen.BaseScreenValidator;
import com.infor.scm.wms.util.validation.screen.owner.OwnerScreenVO;
import com.infor.scm.wms.util.validation.screen.zone.ZoneScreenVO;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public class LocationScreenValidator extends BaseScreenValidator{
	
	public LocationScreenValidator(WMSValidationContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//Numeric Rules
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LocationScreenValidator.class);
	public static final int RULE_CUBE_MUST_BE_A_NUMBER = 1;
	public static final int RULE_CUBICCAPACITY_MUST_BE_A_NUMBER = 2;
	public static final int RULE_FOOTPRINT_MUST_BE_A_NUMBER = 3;
	public static final int RULE_HEIGHT_MUST_BE_A_NUMBER = 4;
	public static final int RULE_LENGTH_MUST_BE_A_NUMBER = 5;
	public static final int RULE_LOCGROUPID_MUST_BE_A_NUMBER = 6;
	public static final int RULE_LOCLEVEL_MUST_BE_A_NUMBER = 7;
	public static final int RULE_STACKLIMIT_MUST_BE_A_NUMBER = 8;
	public static final int RULE_WEIGHTCAPACITY_MUST_BE_A_NUMBER = 9;
	public static final int RULE_WIDTH_MUST_BE_A_NUMBER = 10;
	public static final int RULE_XCOORD_MUST_BE_A_NUMBER = 11;
	public static final int RULE_YCOORD_MUST_BE_A_NUMBER = 12;
	public static final int RULE_ZCOORD_MUST_BE_A_NUMBER = 13;
	
	public static final int RULE_INTERLEAVINGSEQUENCE_MUST_BE_A_NUMBER = 101;
	public static final int RULE_ORIENTATION_MUST_BE_A_NUMBER = 102;

	public static final int RULE_AUTOSHIP_MUST_BE_A_NUMBER = 103;
	
	//Greater than zero rules
	public static final int RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO = 14;
	public static final int RULE_CUBICCAPACITY_GREATER_THAN_OR_EQUAL_ZERO = 15;
	public static final int RULE_FOOTPRINT_GREATER_THAN_OR_EQUAL_ZERO = 16;
	public static final int RULE_HEIGHT_GREATER_THAN_OR_EQUAL_ZERO = 17;
	public static final int RULE_LENGTH_GREATER_THAN_OR_EQUAL_ZERO = 18;
	public static final int RULE_LOCLEVEL_GREATER_THAN_OR_EQUAL_ZERO = 19;
	public static final int RULE_STACKLIMIT_GREATER_THAN_OR_EQUAL_ZERO = 20;
	public static final int RULE_WEIGHTCAPACITY_GREATER_THAN_OR_EQUAL_ZERO = 21;
	public static final int RULE_WIDTH_GREATER_THAN_OR_EQUAL_ZERO = 22;
	public static final int RULE_XCOORD_GREATER_THAN_OR_EQUAL_ZERO = 23;
	public static final int RULE_YCOORD_GREATER_THAN_OR_EQUAL_ZERO = 24;
	public static final int RULE_ZCOORD_GREATER_THAN_OR_EQUAL_ZERO = 25;
	
	public static final int RULE_AUTOSHIP_MUST_BE_ZERO_OR_ONE = 104;
	
	//	Unique
	public static final int RULE_LOC_MUST_BE_UNIQUE = 26;
	
	//Required fields rules
	public static final int RULE_ABC_NOT_EMPTY = 27;
	public static final int RULE_COMMINGLELOT_NOT_EMPTY = 28;
	public static final int RULE_COMMINGLESKU_NOT_EMPTY = 29;
	public static final int RULE_CUBE_NOT_EMPTY = 30;
	public static final int RULE_CUBICCAPACITY_NOT_EMPTY = 31;
	public static final int RULE_FACILITY_NOT_EMPTY = 32;
	public static final int RULE_FOOTPRINT_NOT_EMPTY = 33;
	public static final int RULE_HEIGHT_NOT_EMPTY = 34;
	public static final int RULE_LENGTH_NOT_EMPTY = 35;
	public static final int RULE_LOC_NOT_EMPTY = 36;
	public static final int RULE_LOCATIONCATEGORY_NOT_EMPTY = 37;
	public static final int RULE_LOCATIONFLAG_NOT_EMPTY = 38;
	public static final int RULE_LOCATIONHANDLING_NOT_EMPTY = 39;
	public static final int RULE_LOCATIONTYPE_NOT_EMPTY = 40;
	public static final int RULE_LOCGROUPID_NOT_EMPTY =41;
	public static final int RULE_LOCLEVEL_NOT_EMPTY = 42;
	public static final int RULE_LOGICALLOCATION_NOT_EMPTY = 43;
	public static final int RULE_LOSEID_NOT_EMPTY = 44;
	public static final int RULE_PICKMETHOD_NOT_EMPTY = 45;
	public static final int RULE_PICKZONE_NOT_EMPTY = 46;
	public static final int RULE_PUTAWAYZONE_NOT_EMPTY = 47;
	public static final int RULE_SECTION_NOT_EMPTY = 48;
	public static final int RULE_SECTIONKEY_NOT_EMPTY = 49;
	public static final int RULE_STACKLIMIT_NOT_EMPTY = 50;
	public static final int RULE_STATUS_NOT_EMPTY = 51;
	public static final int RULE_WEIGHTCAPACITY_NOT_EMPTY = 52;
	public static final int RULE_WIDTH_NOT_EMPTY = 53;
	public static final int RULE_XCOORD_NOT_EMPTY = 54;
	public static final int RULE_YCOORD_NOT_EMPTY = 55;
	public static final int RULE_ZCOORD_NOT_EMPTY = 56;
	
	
	
	//Length rules
	public static final int RULE_LENGTH_ABC_1 = 57;
	public static final int RULE_LENGTH_ADDWHO_18 = 58;
	public static final int RULE_LENGTH_COMMINGLELOT_1 = 59;
	public static final int RULE_LENGTH_COMMINGLESKU_1 = 60;
	public static final int RULE_LENGTH_EDITWHO_18 = 61;
	public static final int RULE_LENGTH_FACILITY_20 = 62;
	public static final int RULE_LENGTH_LOC_10 = 63;
	public static final int RULE_LENGTH_LOCATIONCATEGORY_10 = 64;
	public static final int RULE_LENGTH_LOCATIONFLAG_10 = 65;
	public static final int RULE_LENGTH_LOCATIONHANDLING_10 = 66;
	public static final int RULE_LENGTH_LOCATIONTYPE_10 = 67;
	public static final int RULE_LENGTH_LOGICALLOCATION_18 = 68;
	public static final int RULE_LENGTH_LOSEID_1 = 69;
	public static final int RULE_LENGTH_OPTLOC_10 = 70;
	public static final int RULE_LENGTH_PICKMETHOD_1 = 71;
	public static final int RULE_LENGTH_PICKZONE_10 = 72;
	public static final int RULE_LENGTH_PUTAWAYZONE_10 = 73;
	public static final int RULE_LENGTH_SECTION_10 = 74;
	public static final int RULE_LENGTH_SECTIONKEY_10 = 75;
	public static final int RULE_LENGTH_STATUS_10 = 76;
	public static final int RULE_LENGTH_WHSEID_30 = 77;

	
	//Attribute domain rules
	public static final int RULE_ATTR_DOM_ABC  = 78; //listname = 'LOCABC'
	public static final int RULE_ATTR_DOM_COMMINGLELOT  = 79; //listname='YESNO'
	public static final int RULE_ATTR_DOM_COMMINGLESKU  = 80; //listname='YESNO'
	public static final int RULE_ATTR_DOM_LOCATIONCATEGORY  = 81;	//LISTNAME = 'LOCCATEGRY'
	public static final int RULE_ATTR_DOM_LOCATIONFLAG  = 82;	//LISTNAME = 'LOCFLAG'  
	public static final int RULE_ATTR_DOM_LOCATIONHANDLING  = 83;	//LISTNAME = 'LOCHDLING'
	public static final int RULE_ATTR_DOM_LOCATIONTYPE  = 84;	// listname='LOCTYPE' 
	public static final int RULE_ATTR_DOM_PUTAWAYZONE  = 85;	
	//jp 9046.begin
	public static final int RULE_ATTR_DOM_LOCATIONSECTION  = 87;	// listname='LOCSECTION'
	//jp 9046.end	
	public static final int RULE_LOCGROUPID_GREATER_THAN_OR_EQUAL_ZERO = 89;//SRG: 9.2 Upgrade
	
	//If location type equals speed-pick or sort and LoseID = 0 block save and display LoseID is required
	public static final int RULE_LOSEID_REQUIRED_FOR_SPEEDPICK_SORT_LOCATION = 86;
	
	//Validations
	public ArrayList validate(LocationScreenVO screen, boolean isInsert, ArrayList disableRules) throws WMSDataLayerException {
		_log.debug("LOG_SYSTEM_OUT","\n\n Calling Location Validate \n\n",100L);
		ArrayList<Integer> errors = new ArrayList<Integer>();
		boolean doCheckRequiredFields = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_REQUIRED_FIELD);
		boolean doCheckFieldLength = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_GROUP_LENGTH_LIMIT);
		boolean doCheckAttributeDomain = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ATTRIBUTE_DOMAIN_LIMIT);
		boolean doAssumeDefaults  = disableRules == null?true:disableRules.contains(BaseScreenValidator.RULE_ASSUME_DEFAULTS);
		
		
		//Validate Field Lengths
		if(doCheckFieldLength){			
			
			
			
			if (!validateAbcLengthIs1OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_ABC_1));
			}
			if (!validateComminglelotLengthIs1OrLess(screen)){ 
				errors.add(new Integer(RULE_LENGTH_COMMINGLELOT_1));
			}
			if (!validateCommingleskuLengthIs1OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_COMMINGLESKU_1));
			}
			if (!validateFacilityLengthIs20OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_FACILITY_20));
			}
			if (!validateLocLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOC_10));
			}
			
			if (!validateLocationcategoryLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOCATIONCATEGORY_10));
			}
			if (!validateLocationflagLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOCATIONFLAG_10));
			}
			if (!validateLocationhandlingLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOCATIONHANDLING_10));
			}
			if (!validateLocationtypeLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOCATIONTYPE_10));
			}
			if (!validateLogicallocationLengthIs18OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOGICALLOCATION_18));
			}
			if (!validateLoseidLengthIs1OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_LOSEID_1));
			}
			if (!validateOptlocLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_OPTLOC_10));
			}
			if (!validatePickmethodLengthIs1OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_PICKMETHOD_1));
			}
			if (!validatePickzoneLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_PICKZONE_10));
			}
			if (!validatePutawayzoneLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_PUTAWAYZONE_10));
			}
			if (!validateSectionLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_SECTION_10));
			}
			if (!validateSectionkeyLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_SECTIONKEY_10));
			}
			if (!validateStatusLengthIs10OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_STATUS_10));
			}
			if (!validateWhseidLengthIs30OrLess(screen)){  
				errors.add(new Integer(RULE_LENGTH_WHSEID_30));
				
			}
			
		}
		
		
		//Validate Attribute Domain
		if(doCheckAttributeDomain){	
			
			if(!validateAbcInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_ABC  ));		
			}
			
			
			if(!validateComminglelotInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_COMMINGLELOT  ));		
			}
			
			if(!validateCommingleskuInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_COMMINGLESKU  ));		
			}
			
			if(!validateLocationcategoryInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOCATIONCATEGORY  ));		
			}
			
			if(!validateLocationflagInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOCATIONFLAG  ));		
			}
			
			if(!validateLocationhandlingInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOCATIONHANDLING  ));		
			}
			
			if(!validateLocationtypeInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOCATIONTYPE  ));		
			}
			
			if(!validatePutawayzoneInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_PUTAWAYZONE  ));		
			}

			//jp 9046.begin
			if(!validateLocationsectionInAttrDom(screen)){
				errors.add(new Integer(RULE_ATTR_DOM_LOCATIONSECTION  ));		
			}
			//jp 9046.end
			
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
		
		if(doCheckRequiredFields && !validateCubiccapacityNotEmpty(screen))
			errors.add(new Integer(RULE_CUBICCAPACITY_NOT_EMPTY));
		else{
			if(!validateCubiccapacityIsANumber(screen))
				errors.add(new Integer(RULE_CUBICCAPACITY_MUST_BE_A_NUMBER));
			else{
				if(!validateCubiccapacityGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_CUBICCAPACITY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateFootprintNotEmpty(screen))
			errors.add(new Integer(RULE_FOOTPRINT_NOT_EMPTY));
		else{
			if(!validateFootprintIsANumber(screen))
				errors.add(new Integer(RULE_FOOTPRINT_MUST_BE_A_NUMBER));
			else{
				if(!validateFootprintGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_FOOTPRINT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateHeightNotEmpty(screen))
			errors.add(new Integer(RULE_HEIGHT_NOT_EMPTY));
		else{
			if(!validateHeightIsANumber(screen))
				errors.add(new Integer(RULE_HEIGHT_MUST_BE_A_NUMBER));
			else{
				if(!validateHeightGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_HEIGHT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateLengthNotEmpty(screen))
			errors.add(new Integer(RULE_LENGTH_NOT_EMPTY));
		else{
			if(!validateLengthIsANumber(screen))
				errors.add(new Integer(RULE_LENGTH_MUST_BE_A_NUMBER));
			else{
				if(!validateLengthGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LENGTH_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateLoclevelNotEmpty(screen))
			errors.add(new Integer(RULE_LOCLEVEL_NOT_EMPTY));
		else{
			if(!validateLoclevelIsANumber(screen))
				errors.add(new Integer(RULE_LOCLEVEL_MUST_BE_A_NUMBER));
			else{
				if(!validateLoclevelGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_LOCLEVEL_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateStacklimitNotEmpty(screen))
			errors.add(new Integer(RULE_STACKLIMIT_NOT_EMPTY));
		else{
			if(!validateStacklimitIsANumber(screen))
				errors.add(new Integer(RULE_STACKLIMIT_MUST_BE_A_NUMBER));
			else{
				if(!validateStacklimitGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_STACKLIMIT_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateWeightcapacityNotEmpty(screen))
			errors.add(new Integer(RULE_WEIGHTCAPACITY_NOT_EMPTY));
		else{
			if(!validateWeightcapacityIsANumber(screen))
				errors.add(new Integer(RULE_WEIGHTCAPACITY_MUST_BE_A_NUMBER));
			else{
				if(!validateWeightcapacityGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WEIGHTCAPACITY_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		
		if(doCheckRequiredFields && !validateWidthNotEmpty(screen))
			errors.add(new Integer(RULE_WIDTH_NOT_EMPTY));
		else{
			if(!validateWidthIsANumber(screen))
				errors.add(new Integer(RULE_WIDTH_MUST_BE_A_NUMBER));
			else{
				if(!validateWidthGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_WIDTH_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateXcoordNotEmpty(screen))
			errors.add(new Integer(RULE_XCOORD_NOT_EMPTY));
		else{
			if(!validateXcoordIsANumber(screen))
				errors.add(new Integer(RULE_XCOORD_MUST_BE_A_NUMBER));
			else{
				if(!validateXcoordGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_XCOORD_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		
		if(doCheckRequiredFields && !validateYcoordNotEmpty(screen))
			errors.add(new Integer(RULE_YCOORD_NOT_EMPTY));
		else{
			if(!validateYcoordIsANumber(screen))
				errors.add(new Integer(RULE_YCOORD_MUST_BE_A_NUMBER));
			else{
				if(!validateYcoordGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_YCOORD_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		/*
		if(!doAssumeDefaults){
			if(doCheckRequiredFields  && !validateZcoordNotEmpty(screen))
				errors.add(new Integer(RULE_ZCOORD_NOT_EMPTY));
			else{
				if(!validateZcoordIsANumber(screen))
					errors.add(new Integer(RULE_ZCOORD_MUST_BE_A_NUMBER));
				else{
					if(!validateZcoordGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_ZCOORD_GREATER_THAN_OR_EQUAL_ZERO));
				}
			}
		}else{
			if(validateZcoordNotEmpty(screen)){
				if(!validateZcoordIsANumber(screen))
					errors.add(new Integer(RULE_ZCOORD_MUST_BE_A_NUMBER));
				else{
					if(!validateZcoordGreaterThanOrEqualZero(screen))
						errors.add(new Integer(RULE_ZCOORD_GREATER_THAN_OR_EQUAL_ZERO));
				}
				
			}
		}
		*/

		if(doCheckRequiredFields  && !validateZcoordNotEmpty(screen))
			errors.add(new Integer(RULE_ZCOORD_NOT_EMPTY));
		else{
			if(!validateZcoordIsANumber(screen))
				errors.add(new Integer(RULE_ZCOORD_MUST_BE_A_NUMBER));
			else{
				if(!validateZcoordGreaterThanOrEqualZero(screen))
					errors.add(new Integer(RULE_ZCOORD_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}

		if(!isEmpty(screen.getInterleavingsequence())){
			if(!validateInterleavingsequenceIsANumber(screen)){
				errors.add(new Integer(RULE_INTERLEAVINGSEQUENCE_MUST_BE_A_NUMBER));
			}
		}
		
		if(!isEmpty(screen.getOrientation())){
			if(!validateOrientationIsANumber(screen)){
				errors.add(new Integer(RULE_ORIENTATION_MUST_BE_A_NUMBER));
			}
		}
		
		
		
		//It's a number
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateLocgroupidNotEmpty(screen))
				errors.add(new Integer(RULE_LOCGROUPID_NOT_EMPTY));
			else{
				if(!validateLocgroupidIsANumber(screen))
					errors.add(new Integer(RULE_LOCGROUPID_MUST_BE_A_NUMBER));
			}
		}
		
		//Not empty
		if(doCheckRequiredFields && !validateAbcNotEmpty(screen)){
			errors.add(new Integer(RULE_ABC_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateComminglelotNotEmpty(screen)){
			errors.add(new Integer(RULE_COMMINGLELOT_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateCommingleskuNotEmpty(screen)){
			errors.add(new Integer(RULE_COMMINGLESKU_NOT_EMPTY));
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateFacilityNotEmpty(screen)){
				errors.add(new Integer(RULE_FACILITY_NOT_EMPTY));
			}
		}
		
		if(doCheckRequiredFields && !validateLocNotEmpty(screen)){
			errors.add(new Integer(RULE_LOC_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLocationcategoryNotEmpty(screen)){
			errors.add(new Integer(RULE_LOCATIONCATEGORY_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLocationflagNotEmpty(screen)){
			errors.add(new Integer(RULE_LOCATIONFLAG_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLocationhandlingNotEmpty(screen)){
			errors.add(new Integer(RULE_LOCATIONHANDLING_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLocationtypeNotEmpty(screen)){
			errors.add(new Integer(RULE_LOCATIONTYPE_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLogicallocationNotEmpty(screen)){
			errors.add(new Integer(RULE_LOGICALLOCATION_NOT_EMPTY));
		}
		
		if(doCheckRequiredFields && !validateLoseidNotEmpty(screen)){
			errors.add(new Integer(RULE_LOSEID_NOT_EMPTY));
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validatePickmethodNotEmpty(screen)){
				errors.add(new Integer(RULE_PICKMETHOD_NOT_EMPTY));
			}
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validatePickzoneNotEmpty(screen)){
				errors.add(new Integer(RULE_PICKZONE_NOT_EMPTY));
			}
		}
		
		if(doCheckRequiredFields && !validatePutawayzoneNotEmpty(screen)){
			errors.add(new Integer(RULE_PUTAWAYZONE_NOT_EMPTY));
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateSectionNotEmpty(screen)){
				errors.add(new Integer(RULE_SECTION_NOT_EMPTY));
			}
		}
		
		//if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateSectionkeyNotEmpty(screen)){
				errors.add(new Integer(RULE_SECTIONKEY_NOT_EMPTY));
			}
		//}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateStatusNotEmpty(screen)){
				errors.add(new Integer(RULE_STATUS_NOT_EMPTY));
			}
		}
		
		if(!doAssumeDefaults){
			if(doCheckRequiredFields && !validateLoseidRequiredForSpeedpickSortLocation(screen))	 {
				errors.add(new Integer(RULE_LOSEID_REQUIRED_FOR_SPEEDPICK_SORT_LOCATION));
			}
		}
		
		//jp.bugaware.9437.begin
		if(validateAutoshipdNotEmpty(screen)){
			if(!validateAutoshipIsANumber(screen)){
					errors.add(new Integer(RULE_AUTOSHIP_MUST_BE_A_NUMBER));
			}else{
				if(!validateAutoshipZeroOrOne(screen))
					errors.add(new Integer(RULE_AUTOSHIP_MUST_BE_ZERO_OR_ONE));
			}
		}
		//jp.bugaware.9437.end
	
		if(isInsert) {
			if(doCheckRequiredFields && !validateLocNotEmpty(screen))
				errors.add(new Integer(RULE_LOC_NOT_EMPTY));
			else{
				if(validateLocDoesExist(screen.getLoc(), getContext()))
					errors.add(new Integer(RULE_LOC_MUST_BE_UNIQUE));
			}
		}
		
		//SRG: 9.2 Upgrade -- Start
		if(!isEmpty(screen.getLocgroupid())){
			if(!validateLocgroupidGreaterThanOrEqualZero(screen)) {
				errors.add(new Integer(RULE_LOCGROUPID_GREATER_THAN_OR_EQUAL_ZERO));
			}
		}
		//SRG: 9.2 Upgrade -- End
		
		
		return errors;	
		
	}

	//If location type equals speed-pick or sort and LoseID = 0 block save and display LoseID is required
	private boolean validateLoseidRequiredForSpeedpickSortLocation(LocationScreenVO screen){
		
		String loseid = screen.getLoseid();
		if(loseid!=null){
			int nLoseid =Integer.parseInt(loseid);
			if (screen.getLocationtype().compareToIgnoreCase("SPEED-PICK")==0||screen.getLocationtype().compareToIgnoreCase("SORT")==0 && nLoseid==0){
				return true;
			}
		}
		
		return false;
			
	}
	private boolean validateAbcLengthIs1OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getAbc()))
			return true;
		return screen.getAbc().length() < 2;			
		
	}
	
	private boolean validateComminglelotLengthIs1OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getComminglelot()))
			return true;
		return screen.getComminglelot().length() < 2;			
		
	}
	private boolean validateCommingleskuLengthIs1OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getComminglesku()))
			return true;
		return screen.getComminglesku().length() < 2;			
		
	}
	private boolean validateFacilityLengthIs20OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getFacility()))
			return true;
		return screen.getFacility().length() < 21;			
		
	}
	
	private boolean validateLocLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLoc()))
			return true;
		return screen.getLoc().length() < 11;			
		
	}
	private boolean validateLocationcategoryLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLocationcategory()))
			return true;
		return screen.getLocationcategory().length() < 11;			
		
	}
	private boolean validateLocationflagLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLocationflag()))
			return true;
		return screen.getLocationflag().length() < 11;			
		
	}
	private boolean validateLocationhandlingLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLocationhandling()))
			return true;
		return screen.getLocationhandling().length() < 11;			
		
	}
	private boolean validateLocationtypeLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLocationtype()))
			return true;
		return screen.getLocationtype().length() < 11;			
		
	}
	private boolean validateLogicallocationLengthIs18OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLogicallocation()))
			return true;
		return screen.getLogicallocation().length() < 19;			
		
	}
	private boolean validateLoseidLengthIs1OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getLoseid()))
			return true;
		return screen.getLoseid().length() < 11;			
		
	}
	private boolean validateOptlocLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getOptloc()))
			return true;
		return screen.getOptloc().length() < 11;			
		
	}
	private boolean validatePickmethodLengthIs1OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getPickmethod()))
			return true;
		return screen.getPickmethod().length() < 2;			
		
	}
	private boolean validatePickzoneLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getPickzone()))
			return true;
		return screen.getPickzone().length() < 11;			
		
	}
	private boolean validatePutawayzoneLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getPutawayzone()))
			return true;
		return screen.getPutawayzone().length() < 11;			
		
	}
	private boolean validateSectionLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getSection()))
			return true;
		return screen.getSection().length() < 11;			
		
	}
	private boolean validateSectionkeyLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getSectionkey()))
			return true;
		return screen.getSectionkey().length() < 11;			
		
	}
	private boolean validateStatusLengthIs10OrLess(LocationScreenVO screen){
		if(isEmpty(screen.getStatus()))
			return true;
		return screen.getStatus().length() < 11;			
		
		
	}
	
	
	private boolean validateWhseidLengthIs30OrLess(LocationScreenVO screen) {
		if(isEmpty(screen.getWhseid()))
			return true;
		return screen.getWhseid().length() < 31;			
	}
	
	//Attribute validations
	private boolean validateAbcInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getAbc()))
			return true;
		return validateAbcDoesExist(screen.getAbc(), getContext());
	}
	
	private static boolean validateAbcDoesExist(String abc, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCABC", abc, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	private boolean validateComminglelotInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getComminglelot()))
			return true;
		return validateComminglelotDoesExist(screen.getComminglelot(), getContext());
	}
	
	private static boolean validateComminglelotDoesExist(String comminglelot, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("YESNO", comminglelot, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	private boolean validateCommingleskuInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getComminglesku()))
			return true;
		return validateCommingleskuDoesExist(screen.getComminglesku(), getContext());
	}
	
	private static boolean validateCommingleskuDoesExist(String comminglesku, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("YESNO", comminglesku, context).getSize() == 0)
			return false;
		else
			return true;	
	}

	private static boolean validateLocDoesExist(String loc, WMSValidationContext context) throws WMSDataLayerException{
		if (LocationQueryRunner.getLocationByKey(loc, context).getSize() == 0)
			return false;
		else
			return true;	
	}

	
	private boolean validateLocationcategoryInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLocationcategory()))
			return true;
		return validateLocationcategoryDoesExist(screen.getLocationcategory(), getContext());
	}
	
	private static boolean validateLocationcategoryDoesExist(String locationcategory, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCCATEGRY", locationcategory, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	
	private boolean validateLocationflagInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLocationflag()))
			return true;
		return validateLocationflagDoesExist(screen.getLocationflag(), getContext());
	}
	
	private static boolean validateLocationflagDoesExist(String locationflag, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCFLAG", locationflag, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	private boolean validateLocationhandlingInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLocationhandling()))
			return true;
		return validateLocationhandlingDoesExist(screen.getLocationhandling(), getContext());
	}
	
	private static boolean validateLocationhandlingDoesExist(String locationhandling, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCHDLING", locationhandling, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	private boolean validateLocationtypeInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getLocationtype()))
			return true;
		return validateLocationtypeDoesExist(screen.getLocationtype(), getContext());
	}
	
	private static boolean validateLocationtypeDoesExist(String locationtype, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCTYPE", locationtype, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	private boolean validatePutawayzoneInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getPutawayzone()))
			return true;
		return validatePutawayzoneDoesExist(screen.getPutawayzone(), getContext());
	}
	
	private static boolean validatePutawayzoneDoesExist(String putawayzone, WMSValidationContext context) throws WMSDataLayerException{
		if (PutawayZoneQueryRunner.getPutawayZoneByPutawayZone(putawayzone,  context).getSize() == 0)
			return false;
		else
			return true;
		
		
		
	}

	//jp 9046.begin
	private boolean validateLocationsectionInAttrDom(LocationScreenVO screen) throws WMSDataLayerException {
		if(isEmpty(screen.getSectionkey()))
			return true;
		return validateLocationsectionDoesExist(screen.getSectionkey(), getContext());
	}
	
	private static boolean validateLocationsectionDoesExist(String locationsection, WMSValidationContext context) throws WMSDataLayerException{
		if (CodelkupQueryRunner.getCodelkupByListnameAndCode("LOCSECTION", locationsection, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	//jp 9046.end
	//Not empty
	private boolean validateAbcNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getAbc()))
			return false;
		
		return true;
		
	}
	private boolean validateComminglelotNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getComminglelot()))
			return false;
		
		return true;
		
	}
	private boolean validateCommingleskuNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getComminglesku()))
			return false;
		
		return true;
		
	}
	private boolean validateCubeNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getCube()))
			return false;
		
		return true;
		
	}
	private boolean validateCubiccapacityNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getCubiccapacity()))
			return false;
		
		return true;
		
	}
	private boolean validateFacilityNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getFacility()))
			return false;
		
		return true;
		
	}
	private boolean validateFootprintNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getFootprint()))
			return false;
		
		return true;
		
	}
	private boolean validateHeightNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getHeight()))
			return false;
		
		return true;
		
	}
	private boolean validateLengthNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLength()))
			return false;
		
		return true;
		
	}
	private boolean validateLocNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLoc()))
			return false;
		
		return true;
		
	}
	
	
	private boolean validateLocationcategoryNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLocationcategory()))
			return false;
		
		return true;
		
	}
	private boolean validateLocationflagNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLocationflag()))
			return false;
		
		return true;
		
	}
	private boolean validateLocationhandlingNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLocationhandling()))
			return false;
		
		return true;
		
	}
	private boolean validateLocationtypeNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLocationtype()))
			return false;
		
		return true;
		
	}
	private boolean validateLocgroupidNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLocgroupid()))
			return false;
		
		return true;
		
	}

	private boolean validateLoclevelNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLoclevel()))
			return false;
		
		return true;
		
	}
	private boolean validateLogicallocationNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLogicallocation()))
			return false;
		
		return true;
		
	}
	private boolean validateLoseidNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getLoseid()))
			return false;
		
		return true;
		
	}
	private boolean validatePickmethodNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getPickmethod()))
			return false;
		
		return true;
		
	}
	private boolean validatePickzoneNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getPickzone()))
			return false;
		
		return true;
		
	}
	private boolean validatePutawayzoneNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getPutawayzone()))
			return false;
		
		return true;
		
	}
	private boolean validateSectionNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getSection()))
			return false;
		
		return true;
		
	}
	private boolean validateSectionkeyNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getSectionkey()))
			return false;
		
		return true;
		
	}
	
	private boolean validateStacklimitNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getStacklimit()))
			return false;
		
		return true;
		
	}
	private boolean validateStatusNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getStatus()))
			return false;
		
		return true;
		
	}
	private boolean validateWeightcapacityNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getWeightcapacity()))
			return false;
		
		return true;
		
	}
	private boolean validateWidthNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getWidth()))
			return false;
		
		return true;
		
	}
	private boolean validateXcoordNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getXcoord()))
			return false;
		
		return true;
		
	}
	private boolean validateYcoordNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getYcoord()))
			return false;
		
		return true;
		
	}
	private boolean validateZcoordNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getZcoord()))
			return false;
		
		return true;
		
	}
	

	private boolean validateAutoshipdNotEmpty(LocationScreenVO screen){
		if(isEmpty(screen.getAutoship()))
			return false;
		
		return true;
		
	}

	
	//It's a number
	private boolean validateCubeIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getCube()))
			return true;
		return isNumber(screen.getCube());
	}

	private boolean validateCubiccapacityIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getCubiccapacity()))
			return true;
		return isNumber(screen.getCubiccapacity());
	}

	private boolean validateFootprintIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getFootprint()))
			return true;
		return isNumber(screen.getFootprint());
	}
	
	private boolean validateHeightIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getHeight()))
			return true;
		return isNumber(screen.getHeight());
	}

	private boolean validateLengthIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getLength()))
			return true;
		return isNumber(screen.getLength());
	}

	private boolean validateLocgroupidIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getLocgroupid()))
			return true;
		return isNumber(screen.getLocgroupid());
	}

	private boolean validateLoclevelIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getLoclevel()))
			return true;
		return isNumber(screen.getLoclevel());
	}

	private boolean validateStacklimitIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getStacklimit()))
			return true;
		return isNumber(screen.getStacklimit());
	}

	
	private boolean validateWeightcapacityIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getWeightcapacity()))
			return true;
		return isNumber(screen.getWeightcapacity());
	}

	private boolean validateWidthIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getWidth()))
			return true;
		return isNumber(screen.getWidth());
	}

	private boolean validateXcoordIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getXcoord()))
			return true;
		return isNumber(screen.getXcoord());
	}

	private boolean validateYcoordIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getYcoord()))
			return true;
		return isNumber(screen.getYcoord());
	}

	private boolean validateZcoordIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getZcoord()))
			return true;
		return isNumber(screen.getZcoord());
	}

	private boolean validateOrientationIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getOrientation()))
			return true;
		return isNumber(screen.getOrientation());
	}

	
	private boolean validateInterleavingsequenceIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getInterleavingsequence()))
			return true;
		return isNumber(screen.getInterleavingsequence());
		
	}
	
	private boolean validateAutoshipIsANumber(LocationScreenVO screen){
		if(isEmpty(screen.getAutoship()))
			return true;
		return isNumber(screen.getAutoship());
		
	}
	
	
	//Greater than
	private boolean validateCubeGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getCube()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getCube());
	}

	private boolean validateCubiccapacityGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getCubiccapacity()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getCubiccapacity());
	}

	private boolean validateFootprintGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getFootprint()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getFootprint());
	}

	private boolean validateHeightGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getHeight()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getHeight());
	}

	private boolean validateLengthGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getLength()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLength());
	}

	private boolean validateLoclevelGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getLoclevel()))
			
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLoclevel());
	}

	private boolean validateStacklimitGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getStacklimit()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getStacklimit());
	}

	
	private boolean validateWeightcapacityGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getWeightcapacity()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getWeightcapacity());
	}

	private boolean validateWidthGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getWidth()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getWidth());
	}

	private boolean validateXcoordGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getXcoord()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getXcoord());
	}

	private boolean validateYcoordGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getYcoord()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getYcoord());
	}

	private boolean validateZcoordGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getZcoord()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getZcoord());
	}
	
	//SRG: 9.2 Upgrade -- Start
	private boolean validateLocgroupidGreaterThanOrEqualZero(LocationScreenVO screen){
		if(isEmpty(screen.getLocgroupid()))
			return true;
		return greaterThanOrEqualToZeroValidation(screen.getLocgroupid());
	}
	//SRG: 9.2 Upgrade -- End

	//Zero or One validation
	public boolean validateAutoshipZeroOrOne(LocationScreenVO screen){
		return isZeroOrOneValidation(screen.getAutoship());
	}
	
	public static String getErrorMessage(int errorCode, Locale locale, LocationScreenVO locationScreen){
		String errorMsg = "";
		String param[] = null;
		switch(errorCode){
		case RULE_CUBE_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBE, locale));
	
		case RULE_CUBICCAPACITY_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBICCAPACITY, locale));
			 
		case RULE_FOOTPRINT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_FOOTPRINT, locale));
			 
		case RULE_HEIGHT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_HEIGHT, locale));
			 
		case RULE_LENGTH_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LENGTH, locale));
			
		case RULE_LOCGROUPID_MUST_BE_A_NUMBER :
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCGROUPID, locale));
			 
		case RULE_LOCLEVEL_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCLEVEL, locale));
	
		case RULE_STACKLIMIT_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_STACKLIMIT, locale));
	
		case RULE_WEIGHTCAPACITY_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WEIGHTCAPACITY, locale));
	
		case RULE_WIDTH_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WIDTH, locale));
	
		case RULE_XCOORD_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_XCOORD, locale));
	
		case RULE_YCOORD_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_YCOORD, locale));
	
		case RULE_ZCOORD_MUST_BE_A_NUMBER:
			 return getNonNumericErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ZCOORD, locale));
	
			 
			 
			 //Greater than zero rules
		case RULE_CUBE_GREATER_THAN_OR_EQUAL_ZERO:
			 return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBE, locale));
			 
		case RULE_CUBICCAPACITY_GREATER_THAN_OR_EQUAL_ZERO :
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBICCAPACITY, locale));
			
		case RULE_FOOTPRINT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_FOOTPRINT, locale));
			
		case RULE_HEIGHT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_HEIGHT, locale));
			
		case RULE_LENGTH_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LENGTH, locale));
			
		case RULE_LOCLEVEL_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCLEVEL, locale));
			
		case RULE_STACKLIMIT_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_STACKLIMIT, locale));
			
		case RULE_WEIGHTCAPACITY_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WEIGHTCAPACITY, locale));
			
		case RULE_WIDTH_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WIDTH, locale));
			
		case RULE_XCOORD_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_XCOORD, locale));
			
		case RULE_YCOORD_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_YCOORD, locale));
			
		case RULE_ZCOORD_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ZCOORD, locale));
			
			//	Unique
		case RULE_LOC_MUST_BE_UNIQUE :
			param = new String[2];
			param[0] = MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOC, locale);
			param[1] = locationScreen.getLoc();
			return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_LOCATION_SCREEN_ERROR_DUPLICATE_LOCATION, locale, param);
			
			//Required fields rules
		case RULE_ABC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ABC, locale));
			
		case RULE_COMMINGLELOT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLELOT, locale));
			
		case RULE_COMMINGLESKU_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLESKU, locale));
			
		case RULE_CUBE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBE, locale));
			
		case RULE_CUBICCAPACITY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_CUBICCAPACITY, locale));
			
		case RULE_FACILITY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_FACILITY, locale));
			
		case RULE_FOOTPRINT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_FOOTPRINT, locale));
			
		case RULE_HEIGHT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_HEIGHT, locale));
			
		case RULE_LENGTH_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LENGTH, locale));
			
		case RULE_LOC_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOC, locale));
			
		case RULE_LOCATIONCATEGORY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONCATEGORY, locale));
			
		case RULE_LOCATIONFLAG_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONFLAG, locale));
			
		case RULE_LOCATIONHANDLING_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONHANDLING, locale));
			
		case RULE_LOCATIONTYPE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONTYPE, locale));
			
		case RULE_LOCGROUPID_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCGROUPID, locale));
			
		case RULE_LOCLEVEL_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCLEVEL, locale));
			
		case RULE_LOGICALLOCATION_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOGICALLOCATION, locale));
			
		case RULE_LOSEID_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOSEID, locale));
			
		case RULE_PICKMETHOD_NOT_EMPTY :
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PICKMETHOD, locale));
			
		case RULE_PICKZONE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PICKZONE, locale));
			
		case RULE_PUTAWAYZONE_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PUTAWAYZONE, locale));
			
		case RULE_SECTION_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_SECTION, locale));
			
		case RULE_SECTIONKEY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_SECTIONKEY, locale));
			
		case RULE_STACKLIMIT_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_STACKLIMIT, locale));
			
		case RULE_STATUS_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_STATUS, locale));
			
		case RULE_WEIGHTCAPACITY_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WEIGHTCAPACITY, locale));
			
		case RULE_WIDTH_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WIDTH, locale));
			
		case RULE_XCOORD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_XCOORD, locale));
			
		case RULE_YCOORD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_YCOORD, locale));
			
		case RULE_ZCOORD_NOT_EMPTY:
			return getRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ZCOORD, locale));
 
			
			//			Length rules
		case RULE_LENGTH_ABC_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ABC, locale), "1");
			
		case RULE_LENGTH_COMMINGLELOT_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLELOT, locale), "1");
			
		case RULE_LENGTH_COMMINGLESKU_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLESKU, locale), "1");

		case RULE_LENGTH_FACILITY_20:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_FACILITY, locale), "20");
			
		case RULE_LENGTH_LOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOC, locale), "10");
			
		case RULE_LENGTH_LOCATIONCATEGORY_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONCATEGORY, locale), "10");
			
		case RULE_LENGTH_LOCATIONFLAG_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONFLAG, locale), "10");
			
		case RULE_LENGTH_LOCATIONHANDLING_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONHANDLING, locale), "10");
			
		case RULE_LENGTH_LOCATIONTYPE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONTYPE, locale), "10");
			
		case RULE_LENGTH_LOGICALLOCATION_18:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOGICALLOCATION, locale), "18");
			
		case RULE_LENGTH_LOSEID_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOSEID, locale), "1");
			
		case RULE_LENGTH_OPTLOC_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_OPTLOC, locale), "10");
			
		case RULE_LENGTH_PICKMETHOD_1:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PICKMETHOD, locale), "1");
			
		case RULE_LENGTH_PICKZONE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PICKZONE, locale), "10");
			
		case RULE_LENGTH_PUTAWAYZONE_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PUTAWAYZONE, locale), "10");
			
		case RULE_LENGTH_SECTION_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_SECTION, locale), "10");
			
		case RULE_LENGTH_SECTIONKEY_10:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_SECTIONKEY, locale), "10");
			
		case RULE_LENGTH_STATUS_10	:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_STATUS, locale), "10");
			
		case RULE_LENGTH_WHSEID_30:
			return getLengthErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_WHSEID, locale), "30");
			
			
			
			//Attribute domain rules
		case RULE_ATTR_DOM_ABC:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ABC, locale), locationScreen.getAbc());
			
		case RULE_ATTR_DOM_COMMINGLELOT:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLELOT, locale), locationScreen.getComminglelot());
			
		case RULE_ATTR_DOM_COMMINGLESKU:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_COMMINGLESKU, locale), locationScreen.getComminglesku());
			
		case RULE_ATTR_DOM_LOCATIONCATEGORY:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONCATEGORY, locale), locationScreen.getLocationcategory());
			
		case RULE_ATTR_DOM_LOCATIONFLAG:  
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONFLAG, locale), locationScreen.getLocationflag());
			
		case RULE_ATTR_DOM_LOCATIONHANDLING:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONHANDLING, locale), locationScreen.getLocationhandling());
			
		case RULE_ATTR_DOM_LOCATIONTYPE: 
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCATIONTYPE, locale), locationScreen.getLocationtype());
			
		case RULE_ATTR_DOM_PUTAWAYZONE:	
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_PUTAWAYZONE, locale), locationScreen.getPutawayzone());
			
		//jp 9046.begin
		case RULE_ATTR_DOM_LOCATIONSECTION:
			return getAttrDomErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_SECTIONKEY, locale), locationScreen.getSectionkey());
		//jp 9046.end
			//If location type equals speed-pick or sort and LoseID = 0 block save and display LoseID is required
		case RULE_LOSEID_REQUIRED_FOR_SPEEDPICK_SORT_LOCATION:
			return getLoseIdRequiredErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOSEID, locale), locationScreen.getLoseid());
			
		case RULE_INTERLEAVINGSEQUENCE_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_INTERLEAVINGSEQUENCE, locale));

		case RULE_ORIENTATION_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_ORIENTATION, locale));

		case RULE_AUTOSHIP_MUST_BE_A_NUMBER:
			return getNonNumericErrorMessage(errorCode, locale,
					MessageUtil.getResourceBundleMessage(
							ResourceConstants.KEY_SCREEN_LOCATION_FIELD_AUTOSHIP, locale));

		case RULE_AUTOSHIP_MUST_BE_ZERO_OR_ONE:
			return getZeroOrOneErrorMessage(errorCode, locale, 
					MessageUtil.getResourceBundleMessage(
							ResourceConstants.KEY_SCREEN_LOCATION_FIELD_AUTOSHIP, locale), 
					locationScreen.getAutoship());
			
		//SRG: 9.2 Upgrade -- Start
		case RULE_LOCGROUPID_GREATER_THAN_OR_EQUAL_ZERO:
			return getGreaterThanOrEqualToErrorMessage(errorCode, locale, MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_SCREEN_LOCATION_FIELD_LOCGROUPID, locale));
		//SRG: 9.2 Upgrade -- End
		}		
		
		return errorMsg;
	}
	
	private static String getLoseIdRequiredErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_LOCATION_SCREEN_ERROR_LOSEID_REQUIRED_FOR_SPEEDPICK_OR_SORT_LOCATION_TYPE, locale,param);
	}

}
