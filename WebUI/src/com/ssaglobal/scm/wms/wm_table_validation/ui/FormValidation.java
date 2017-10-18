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
/**
 * 
 */
package com.ssaglobal.scm.wms.wm_table_validation.ui;

import java.text.NumberFormat;
import java.text.ParseException;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class FormValidation
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FormValidation.class);

	/**
	 * 
	 */

	protected RuntimeFormInterface form;

	protected StateInterface state;

	protected DataBean focus;

	protected boolean isUpdate;

	protected boolean isInsert;

	protected String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);

	protected MetaDataAccess mda = MetaDataAccess.getInstance();

	protected LocaleInterface locale = mda.getLocale(userLocale);

	public FormValidation(RuntimeFormInterface f, StateInterface st)
	{
		form = f;
		state = st;
		setFocus(f);
	}

	protected void setFocus(RuntimeFormInterface f)
	{
		focus = f.getFocus();
		if (focus instanceof BioBean)
		{
			focus = (BioBean) focus;
			isUpdate = true;
			isInsert = false;
		}
		else if (focus instanceof QBEBioBean)
		{
			focus = (QBEBioBean) focus;
			isUpdate = false;
			isInsert = true;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n\n\n\n\n\n\nClass " + focus.getClass().getName(), SuggestedCategory.NONE);
		}
	}

	protected boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	protected boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	boolean isUnchecked(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("0"))
		{
			return true;
		}
		else if (attributeValue.toString().matches("N"))
		{
			return true;
		}
		else if (attributeValue.toString().matches("n"))
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	protected String retrieveLabel(String widgetName)
	{

		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	protected String retrieveLabel(String widgetName, RuntimeFormInterface f)
	{
		return f.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	protected String retrieveFormTitle()
	{
		return form.getLabel("title", locale);
	}

	protected String retrieveFormTitle(RuntimeFormInterface f)
	{
		return f.getLabel("title", locale);
	}

	protected boolean verifyStorer(String attributeName, int type) throws DPException
	{
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue))
		{
			return true; //Do Nothing
		}
		attributeValue = attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM STORER WHERE (STORERKEY = '" + attributeValue + "') AND (TYPE = '" + type + "')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

	protected boolean verifySingleAttribute(String attributeName, String table, String tableAttribute) throws EpiDataException
	{
		Object attributeValue = focus.getValue(attributeName);
		//Food Enhancements - 3PL - Putaway - Srinivas Akula  - Dec-30-2010 – Starts
		// Verifying if SKU is MIXED or Not
		//if (isEmpty(attributeValue))
		if (isEmpty(attributeValue) || ( (table.equalsIgnoreCase("SKU")) && ("MIXED".equalsIgnoreCase(attributeValue.toString()))))
		//Food Enhancements - 3PL - Putaway - Srinivas Akula  - Dec-30-2010 – ends)
		{
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

	protected boolean verifySingleAttribute(String attributeName, String table) throws EpiDataException
	{
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue))
		{
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE " + attributeName + " = '" + attributeValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

	protected boolean verifyItemByOwner(String itemAttributeName, String ownerAttributeName) throws EpiDataException
	{
		Object itemAttributeValue = focus.getValue(itemAttributeName);
		Object ownerAttributeValue = focus.getValue(ownerAttributeName);
		//Food Enhancements - 3PL - Putaway - Srinivas Akula  - Dec-30-2010 – Starts
		// Verifying if SKU is MIXED or Not
		//if (isEmpty(itemAttributeValue) || (isEmpty(ownerAttributeValue)))
		if (isEmpty(itemAttributeValue) || (isEmpty(ownerAttributeValue)) || "MIXED".equalsIgnoreCase(itemAttributeValue.toString()))
		//Food Enhancements - 3PL - Putaway - Srinivas Akula  - Dec-30-2010 – Ends
		{
			return true; //Do Nothing
		}

		itemAttributeValue = itemAttributeValue == null ? null : itemAttributeValue.toString().toUpperCase();
		ownerAttributeValue = ownerAttributeValue == null ? null : ownerAttributeValue.toString().toUpperCase();
		String query = "SELECT * " + " FROM " + "SKU" + " WHERE " + "SKU" + " = '" + itemAttributeValue + "' "
				+ " AND STORERKEY = '" + ownerAttributeValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

	protected void greaterThanOrEqualToZeroValidation(String attributeName) throws UserException
	{
		numericValidation(attributeName);
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue))
		{
			return;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value))
		{
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
		}
		else if (value < 0)
		{
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
		}

	}

	//	protected void greaterThanZeroValidation(String attributeName) throws UserException
	//	{
	//		numericValidation(attributeName);
	//		//if it passes numericValidation, you can parse the number
	//		Object tempValue = focus.getValue(attributeName);
	//		if (isNull(tempValue))
	//		{
	//			return;
	//		}
	//		String attributeValue = tempValue.toString();
	//		double value = NumericValidationCCF.parseNumber(attributeValue);
	//		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
	//		if (value == Double.NaN)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle();
	//			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
	//		}
	//		else if (value <= 0)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle();
	//			throw new UserException("WMEXP_TAB_GREATER_THAN_ZERO", parameters);
	//		}
	//
	//	}

	//	protected void greaterThanZeroValidation(String attributeName, RuntimeFormInterface subForm) throws UserException
	//	{
	//		numericValidation(attributeName);
	//		//if it passes numericValidation, you can parse the number
	//		Object tempValue = focus.getValue(attributeName);
	//		if (isNull(tempValue))
	//		{
	//			return;
	//		}
	//		String attributeValue = tempValue.toString();
	//		double value = NumericValidationCCF.parseNumber(attributeValue);
	//		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
	//		if (value == Double.NaN)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName, subForm);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle(subForm);
	//			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
	//		}
	//		else if (value <= 0)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName, subForm);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle(subForm);
	//			throw new UserException("WMEXP_TAB_GREATER_THAN_ZERO", parameters);
	//		}
	//
	//	}

	//	protected void greaterThanOrEqualToZeroValidation(String attributeName, RuntimeFormInterface f) throws UserException
	//	{
	//		_log.debug("LOG_DEBUG_EXTENSION", "Performing >0 Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
	//		numericValidation(attributeName, f);
	//		//if it passes numericValidation, you can parse the number
	//		Object tempValue = focus.getValue(attributeName);
	//		if (isNull(tempValue))
	//		{
	//			return;
	//		}
	//		String attributeValue = tempValue.toString();
	//		double value = NumericValidationCCF.parseNumber(attributeValue);
	//		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
	//		if (value == Double.NaN)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName, f);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle(f);
	//			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
	//		}
	//		else if (value < 0)
	//		{
	//			String[] parameters = new String[3];
	//			parameters[0] = retrieveLabel(attributeName, f);
	//			parameters[1] = attributeValue.toString();
	//			parameters[2] = retrieveFormTitle(f);
	//			throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
	//		}
	//	}

	protected void numericValidation(String attributeName) throws UserException
	{
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false))
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
		}

	}

	public static double parseNumber(String value) throws ParseException, NumberFormatException
	{
		double widgetValue;
		try
		{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			nf.setMaximumFractionDigits(40);
			nf.setMaximumIntegerDigits(40);
			widgetValue = nf.parse(value.toString()).doubleValue();

		} catch (ParseException e)
		{
			throw e;
		} catch (NumberFormatException e)
		{
			throw e;
		}

		if (!(value.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*")))
		{
			throw new NumberFormatException();
		}
		return widgetValue;
	}

	protected void numericValidation(String attributeName, RuntimeFormInterface f) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "Performing # Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false))
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName, f);
			parameters[1] = attributeValue.toString();
			throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
		}

	}

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	protected boolean isAttributeNull(String attributeName)
	{
		Object attributeValue = focus.getValue(attributeName);
		return isNull(attributeValue);
	}

	protected boolean isAttributeEmpty(String attributeName)
	{
		Object attributeValue = focus.getValue(attributeName);
		return isEmpty(attributeValue);
	}

	protected void packValidation() throws EpiDataException, UserException
	{
		String attributeName = "PACKKEY";
		String table = "PACK";
		if (verifySingleAttribute(attributeName, table) == false)
		{
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}
	}

	protected void locValidation(String attributeName) throws EpiDataException, UserException
	{
		String table = "LOC";
		String tableAttribute = "LOC";
		if (verifySingleAttribute(attributeName, table, tableAttribute) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}
	}

	protected void validation(String attributeName, String table, String tableAttribute, RuntimeFormInterface subForm) throws EpiDataException, UserException
	{
		if (verifySingleAttribute(attributeName, table, tableAttribute) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName, subForm);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected void validation(String attributeName, String table, String tableAttribute) throws EpiDataException, UserException
	{
		if (verifySingleAttribute(attributeName, table, tableAttribute) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected void ownerValidation(String attributeName, RuntimeFormInterface subForm) throws EpiDataException, UserException
	{
		if (verifyStorer(attributeName, 1) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName, subForm);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected void ownerValidation(String attributeName) throws EpiDataException, UserException
	{
		if (verifyStorer(attributeName, 1) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected void carrierValidation(String attributeName) throws EpiDataException, UserException
	{
		if (verifyStorer(attributeName, 3) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected void customerValidation(String attributeName) throws DPException, UserException
	{
		if (verifyStorer(attributeName, 2) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}
	}
	
	protected void shipFromValidation(String attributeName) throws DPException, UserException
	{
		if (verifyStorer(attributeName, 12) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}
	}
	
	protected void billToValidation(String attributeName) throws DPException, UserException
	{
		if (verifyStorer(attributeName, 4) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}
	}

	protected void itemValidation(String itemAttribute, String ownerAttribute, RuntimeFormInterface f) throws EpiDataException, UserException
	{

		if (verifyItemByOwner(itemAttribute, ownerAttribute) == false)
		{
			//throw exception
			String[] parameters = new String[4];
			parameters[0] = retrieveLabel(itemAttribute,f);
			parameters[1] = focus.getValue(itemAttribute).toString();
			parameters[2] = retrieveLabel(ownerAttribute,f);
			parameters[3] = focus.getValue(ownerAttribute).toString();
			throw new UserException("WMEXP_INVALID_ITEM_BY_OWNER", parameters);
		}

	}
	
	protected void itemValidation(String itemAttribute, String ownerAttribute) throws EpiDataException, UserException
	{

		if (verifyItemByOwner(itemAttribute, ownerAttribute) == false)
		{
			//throw exception
			String[] parameters = new String[4];
			parameters[0] = retrieveLabel(itemAttribute);
			parameters[1] = focus.getValue(itemAttribute).toString();
			parameters[2] = retrieveLabel(ownerAttribute);
			parameters[3] = focus.getValue(ownerAttribute).toString();
			throw new UserException("WMEXP_INVALID_ITEM_BY_OWNER", parameters);
		}

	}

	public static double parseNumber(String value, String name) throws UserException
	{
		double widgetValue;
		try
		{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			widgetValue = nf.parse(value.toString()).doubleValue();

		} catch (ParseException e)
		{
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);
		} catch (NumberFormatException e)
		{
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);
		}

		if (!(value.toString().matches("[$]?[-+]{0,1}[\\d.,]*[eE]?[\\d]*")))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@@ Throwing Exception by regex", SuggestedCategory.NONE);
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NUMBER_PARSE_2", "WMEXP_NUMBER_PARSE_2", parameters);
		}
		return widgetValue;
	}

	static public boolean isNegative(String value, String name) throws EpiException
	{
		double widgetValue = FormValidation.parseNumber(value, name);

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Validating Widget: " + name + " Value: " + widgetValue, SuggestedCategory.NONE);

		// If value < 0, return RET_CANCEL
		if (widgetValue < 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "//// isNegative Validation Failed", SuggestedCategory.NONE);
			String[] parameters = new String[2];
			parameters[0] = value;
			parameters[1] = name;
			throw new UserException("WMEXP_NONNEGATIVE_VALIDATION_2", "WMEXP_NONNEGATIVE_VALIDATION", parameters);

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "//// isNegative Validation Passed - " + name + " : " + value, SuggestedCategory.NONE);
			//setErrorMessage(formWidget, "");
			return false;
		}
	}

	protected void pickToLocValidation(String attributeName) throws EpiDataException, UserException
	{

		if (verifyPickToAttribute(attributeName) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_NOTAVALID", parameters);
		}

	}

	protected boolean verifyPickToAttribute(String attributeName) throws EpiDataException
	{
		String table = "LOC";
		String tableAttribute = "LOC";

		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue))
		{
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE (" + tableAttribute + " = '" + attributeValue
				+ "' AND LOCATIONTYPE = 'PICKTO')";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
			return false;
		}

	}

}