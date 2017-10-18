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
package com.infor.scm.wms.util.validation.screen;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.query.AltSkuQueryRunner;
import com.infor.scm.wms.util.datalayer.query.AmstrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CCAdjRulesQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CCSetupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CartonizationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.CodelkupQueryRunner;
import com.infor.scm.wms.util.datalayer.query.HazmatQueryRunner;
import com.infor.scm.wms.util.datalayer.query.InventoryHoldCodeQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ItemQueryRunner;
import com.infor.scm.wms.util.datalayer.query.LocationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.LottableValidationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PackQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PurchaseOrderQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PutawayStrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.PutawayZoneQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ReceiptQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ReceiptValidationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.ReceiptdetailQueryRunner;
import com.infor.scm.wms.util.datalayer.query.SkuXLocationQueryRunner;
import com.infor.scm.wms.util.datalayer.query.StorerQueryRunner;
import com.infor.scm.wms.util.datalayer.query.StrategyQueryRunner;
import com.infor.scm.wms.util.datalayer.query.SubstituteSkuQueryRunner;
import com.infor.scm.wms.util.datalayer.query.TariffQueryRunner;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.resources.ResourceConstants;
import com.infor.scm.wms.util.validation.util.MessageUtil;

public abstract class BaseScreenValidator {
	public static final Integer RULE_GROUP_REQUIRED_FIELD = new Integer(0);
	public static final Integer RULE_GROUP_LENGTH_LIMIT = new Integer(1);
	public static final Integer RULE_ATTRIBUTE_DOMAIN_LIMIT = new Integer(2);
	public static final Integer RULE_OWNER = new Integer(3);
	public static final Integer RULE_ASSUME_DEFAULTS = new Integer(4);
	public static final Integer RULE_IN_TRANSACTION = new Integer(5); //This record is inserted as member of a transaction,
																	  //thus it is assumed that parent record will also be inserted
	
	private WMSValidationContext context = null;
	
	public BaseScreenValidator(WMSValidationContext context){
		this.context = context;
	}
	
	public WMSValidationContext getContext() {
		return context;
	}

	protected static BioCollection getBioCollection(String bioType, String bioQry, String sort) throws EpiException{
		BioServiceFactory serviceFactory = BioServiceFactory.getInstance();
		BioService bioService = serviceFactory.create("webui");
		UnitOfWork uow = bioService.getUnitOfWork();
		Query qry = new Query(bioType, bioQry, sort);
		return uow.findByQuery(qry);
	}
	
	protected static boolean doesExist(String bioType, String bioQry) throws EpiException{
		BioCollection records = getBioCollection(bioType, bioQry, null);
		if(records != null && records.size() > 0)
			return true;
		else
			return false;
	}
	
	protected static boolean greaterThanOrEqualToZeroValidation(String value) {
		if(value == null)
			return false;
		if(!isNumber(value))
			return false;
		
		double number = parseNumber(value);		
		if (number < 0) {
			return false;
		}
		
		return true;
	}
	
	protected static boolean isZeroOrOneValidation(String value) {
		if(value == null)
			return false;
		if(!isNumber(value))
			return false;
		
		double number = parseNumber(value);		
		if (number == 0 || number == 1 ) {
			return true;
		}
		
		return false;
	} 

	
	protected static boolean greaterThanZeroValidation(String value) {
		if(value == null)
			return false;
		if(!isNumber(value))
			return false;
		
		double number = parseNumber(value);		
		if (number > 0) {
			return true;
		}
		
		return false;
	}
	protected static boolean alphaNumericValidation(String value) {
		if(value == null)
			return false;
		if (!(value.matches("^[\\d\\w]*$")))
		{
			return false;
		}
		
		return true;
	}
	protected static boolean currencyValidation(String value) {
		if(value == null)
			return false;
		double parseCurrency = parseCurrency(value);
		if(Double.isNaN(parseCurrency))
		{
			return false;
		}
		
		return true;
	}
	protected static boolean isNumber(String value)
	{

		if(Double.isNaN(parseNumber(value)))
		{
			return false;
		}
		
		return true;
	}
	
	protected static double parseNumber(String value)
	{
		double widgetValue;
		try
		{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(40);
			nf.setMaximumIntegerDigits(40);
			widgetValue = nf.parse(value.toString()).doubleValue();

		} catch (ParseException e)
		{
			return Double.NaN;
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
	
	protected static boolean validateStorerDoesExist(String owner, String type, WMSValidationContext context) throws WMSDataLayerException{
		if(StorerQueryRunner.getStorerByKeyAndType(owner, type,context).getSize() == 0)
			return false;
		else
			return true;
		
	}
	
	protected static boolean validatePackDoesExist(String packKey, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKey(packKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM1(String packKey, String packUOM1, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM1(packKey, packUOM1,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM2(String packKey, String packUOM2, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM2(packKey, packUOM2,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM3(String packKey, String packUOM3, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM3(packKey, packUOM3,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM4(String packKey, String packUOM4, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM4(packKey, packUOM4,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM5(String packKey, String packUOM5, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM5(packKey, packUOM5,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM6(String packKey, String packUOM6, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM6(packKey, packUOM6,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM7(String packKey, String packUOM7, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM7(packKey, packUOM7,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM8(String packKey, String packUOM8, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM8(packKey, packUOM8,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePackDoesExistUsingPackKeyAndPackUOM9(String packKey, String packUOM9, WMSValidationContext context) throws WMSDataLayerException{
		if(PackQueryRunner.getPackByKeyAndUOM9(packKey, packUOM9,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateItemDoesExist(String sku, WMSValidationContext context) throws WMSDataLayerException{
		if(ItemQueryRunner.getItemByKey(sku,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateItemDoesExist(String sku, String owner, WMSValidationContext context) throws WMSDataLayerException{
		if(ItemQueryRunner.getItemByKeyAndStorer(sku, owner,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateHazmatCodeDoesExist(String hazmatKey, WMSValidationContext context) throws WMSDataLayerException{
		if(HazmatQueryRunner.getHazmatCodeByKey(hazmatKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePutawayLocationDoesExist(String loc, WMSValidationContext context) throws WMSDataLayerException{
		if(LocationQueryRunner.getPutawayLocationByKey(loc,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateLocationDoesExist(String loc, WMSValidationContext context) throws WMSDataLayerException{
		if(LocationQueryRunner.getLocationByKey(loc,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateSkuXLocationDoesExist(String loc,String sku,String owner, WMSValidationContext context) throws WMSDataLayerException{
		if(SkuXLocationQueryRunner.getSkuXLocationByLocSkuOwner(loc,sku,owner,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateAltSkuDoesExist(String altSku,String owner, WMSValidationContext context) throws WMSDataLayerException{
		if(AltSkuQueryRunner.getAltSkuByAltSkuOwner(altSku, owner,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateSubstituteSkuDoesExist(String sku,String owner,String substituteSku, WMSValidationContext context) throws WMSDataLayerException{
		if(SubstituteSkuQueryRunner.getSubstituteSkuBySkuOwnerSubstituteSku(sku, owner,substituteSku,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateCodelkupDoesExist(String listname,String code, WMSValidationContext context) throws WMSDataLayerException{
		if(CodelkupQueryRunner.getCodelkupByListnameAndCode(listname, code,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateTariffDoesExist(String tariffKey, WMSValidationContext context) throws WMSDataLayerException{
		if(TariffQueryRunner.getTariffByTariffKey(tariffKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateCartonizationDoesExist(String cartonizationGroup, WMSValidationContext context) throws WMSDataLayerException{
		if(CartonizationQueryRunner.getCartonizationByCartonizationGroup(cartonizationGroup,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateLottableValidationDoesExist(String lottableValidationKey, WMSValidationContext context) throws WMSDataLayerException{
		if(LottableValidationQueryRunner.getLottableValidationByLottableValidationKey(lottableValidationKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateInventoryHoldCodeDoesExist(String code, WMSValidationContext context) throws WMSDataLayerException{
		if(InventoryHoldCodeQueryRunner.getInventoryHoldCodeByCode(code,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateReceiptValidationDoesExist(String receiptValidationKey, WMSValidationContext context) throws WMSDataLayerException{
		if(ReceiptValidationQueryRunner.getReceiptValidationByReceiptValidationKey(receiptValidationKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePutawayZoneDoesExist(String putawayZone, WMSValidationContext context) throws WMSDataLayerException{
		if(PutawayZoneQueryRunner.getPutawayZoneByPutawayZone(putawayZone,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validatePutawayStrategyDoesExist(String putawayStrategyKey, WMSValidationContext context) throws WMSDataLayerException{
		if(PutawayStrategyQueryRunner.getPutawayStrategyByPutawayStrategyKey(putawayStrategyKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateStrategyDoesExist(String strategyKey, WMSValidationContext context) throws WMSDataLayerException{
		if(StrategyQueryRunner.getStrategyByStrategyKey(strategyKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateCCSetupDoesExist(String classID, WMSValidationContext context) throws WMSDataLayerException{
		if(CCSetupQueryRunner.getCCSetupByClassId(classID,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	protected static boolean validateCCAdjRulesDoesExist(String ccAdjRulesKey, WMSValidationContext context) throws WMSDataLayerException{
		if(CCAdjRulesQueryRunner.getCCAdjRulesByCCAdjRulesKey(ccAdjRulesKey,context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	protected static boolean validatePurchaseOrderDoesExist(String pokey, WMSValidationContext context) throws WMSDataLayerException{
		if(PurchaseOrderQueryRunner.getPOByKey(pokey, context).getSize() == 0)
			return false;
		else
			return true;	
	}

	protected static boolean validateReceiptDoesExist(String receiptkey, WMSValidationContext context) throws WMSDataLayerException{
		if(ReceiptQueryRunner.getReceiptByKey(receiptkey, context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	protected static boolean validateReceiptlinenumberDoesExist(String receiptkey, String receiptlinenumber, WMSValidationContext context) throws WMSDataLayerException{
		if(ReceiptdetailQueryRunner.getReceiptlinenumberByKey(receiptkey, receiptlinenumber,  context).getSize() == 0)
			return false;
		else
			return true;	
	}
	
	//SRG: 9.2 Upgrade -- Start
	protected static boolean validateAmstrategyDoesExist(String amstrategykey, WMSValidationContext context) throws WMSDataLayerException{
		if (AmstrategyQueryRunner.getAmstrategyByAmstrategyKey(amstrategykey, context).getSize() == 0)			
			return false;
		else
			return true;	
	}
	//SRG: 9.2 Upgrade -- End

	
	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean isChecked(Object attributeValue) {
		if(isEmpty(attributeValue))
			return false;
			
		if(attributeValue.toString().equals("1"))
			return true;
		
		return false;
	}
	
	protected boolean greaterThanValidation(String leftSideArg, String rightSideArg) {		
						
		double otherValue = parseNumber(rightSideArg);
		double value = parseNumber(leftSideArg);
		if(value <= otherValue) {
			return false;
		}
		return true;
	}
	
	protected boolean lessThanMaxLongValidation(String number){
				
		long otherValue = 274877906943L;

		double value = parseNumber(number);
		if(value >= otherValue) {
			return false;
		}
		return true;
	}
	
	//SRG: 9.2 Upgrade -- Begin
	protected boolean isYesOrNo(String value){
		if (value.equalsIgnoreCase("Y")) {
			return true;
		} else if (value.equalsIgnoreCase("N")) {
			return true;
		} else {
			return false;
		}		
	}
	
	protected static String getYesOrNoErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_BE_YES_OR_NO, locale,param);
	}
	//SRG: 9.2 Upgrade -- End

	protected static String getBetween0And7ToErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_BE_BETWEEN_ZERO_AND_SEVEN, locale,param);
	}

	protected static String getBetween0And8ToErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_BE_BETWEEN_ZERO_AND_EIGHT, locale,param);
	}

	protected static String getGreaterThanOrEqualToErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO, locale,param);
	}

	protected static String getGreaterThanErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_BE_GREATER_THAN_ZERO, locale,param);
	}

	protected static String getDoesNotExistErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_DOES_NOT_EXIST, locale,param);
	}
	protected static String getMustNotExistErrorMessage(int errorCode, Locale locale, String field, String value, String fieldValidator){
		String[] param = new String[3];
		param[0] = field;
		param[1] = value;
		param[2] = fieldValidator;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_VALUE_MUST_NOT_EXIST, locale,param);
	}

	protected static String getNonNumericErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_NON_NUMERIC, locale,param);
	}
	protected static String getRequiredErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_REQUIRED, locale,param);
	}
	protected static String getField1GreaterThenField2ErrorMessage(int errorCode, Locale locale, String field1, String field2){
		String[] param = new String[2];
		param[0] = field1;
		param[1] = field2;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_FIELD1_GREATER_THAN_FIELD2, locale,param);
	}
	protected static String getFieldLessThenValueErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_FIELD_LESS_THAN_VALUE, locale,param);
	}
	protected static String getLengthErrorMessage(int errorCode, Locale locale, String field, String length){
		String[] param = new String[2];
		param[0] = field;
		param[1] = length;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_LENGTH, locale,param);
	}
	protected static String getAttrDomErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_ATTR_DOM, locale,param);
	}
	protected static String getAlphaNumericErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_NON_ALPHA_NUMERIC, locale,param);
	}

	protected static String getNonCurrencyErrorMessage(int errorCode, Locale locale, String field){
		String[] param = new String[1];
		param[0] = field;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_NON_CURRENCY, locale,param);
	}

	protected static String getNotAllNineErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_NOT_ALL_NINE, locale,param);
	}

	protected static String getLengthNotCompliesErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_LENGTH_NOT_COMPLIES, locale,param);
	}

	protected static String getNotCompliesFormatErrorMessage(int errorCode, Locale locale, String field, String value, String formatField){
		String[] param = new String[3];
		param[0] = field;
		param[1] = value;
		param[2] = formatField;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_NOT_COMPLIES_FORMAT, locale,param);
	}

	protected static String getZeroOrOneErrorMessage(int errorCode, Locale locale, String field, String value){
		String[] param = new String[2];
		param[0] = field;
		param[1] = value;
		return MessageUtil.getResourceBundleMessage(ResourceConstants.KEY_ERROR_MUST_BE_ZERO_OR_ONE, locale,param);
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
}