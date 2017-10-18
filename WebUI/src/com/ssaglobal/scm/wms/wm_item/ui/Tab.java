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
package com.ssaglobal.scm.wms.wm_item.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

public class Tab {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(Tab.class);

	protected RuntimeFormInterface form;
	protected StateInterface state;
	protected DataBean focus;
	protected boolean isUpdate;
	protected boolean isInsert;
	protected String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
	protected MetaDataAccess mda = MetaDataAccess.getInstance();
	protected LocaleInterface locale = mda.getLocale(userLocale);

	public Tab(RuntimeFormInterface f, StateInterface st) {
		form = f;
		state = st;
		_log.debug("LOG_DEBUG_EXTENSION_Tab", "Start of " + f.getName(), SuggestedCategory.NONE);
		setFocus(f);
	}

	protected void setFocus(RuntimeFormInterface f) {
		focus = f.getFocus();
		if (focus instanceof BioBean) {
			focus = (BioBean) focus;
			isUpdate = true;
			isInsert = false;
		} else if (focus instanceof QBEBioBean) {
			focus = (QBEBioBean) focus;
			isUpdate = false;
			isInsert = true;
		} else {
			_log.error("LOG_ERROR_EXTENSION", "\n*Class "+focus.getClass().getName()+"\n*", SuggestedCategory.NONE);
		}
	}

	protected boolean isNull(Object attributeValue) {
		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUnchecked(Object attributeValue) {
		if(!isEmpty(attributeValue)){
			if (attributeValue.toString().matches("[0Nn]")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	protected String retrieveLabel(String widgetName) {
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	protected String retrieveLabel(String widgetName, RuntimeFormInterface f) {
		return f.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	protected String retrieveFormTitle() {
		return form.getLabel("title", locale);
	}

	protected String retrieveFormTitle(RuntimeFormInterface f) {
		return f.getLabel("title", locale);
	}

	protected boolean verifyStorer(String attributeValue, int type) throws DPException {
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM STORER WHERE (STORERKEY = '"+attributeValue+"') AND (TYPE = '"+type+"')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1) {
			//value exists, verified
			return true;
		} else {
			//value does not exist
			return false;
		}
	}

	protected boolean verifySingleAttribute(String attributeName, String table, String tableAttribute) throws EpiDataException {
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue)) {
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM "+table+" WHERE "+tableAttribute+" = '"+attributeValue+"'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n"+query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1) {
			//value exists, verified
			return true;
		} else {
			//value does not exist
			return false;
		}
	}

	protected boolean verifySingleAttribute(String attributeName, String table) throws EpiDataException {
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue)) {
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM "+table+" WHERE "+attributeName+" = '"+attributeValue.toString()+"'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1) {
			//value exists, verified
			return true;
		} else {
			//value does not exist
			return false;
		}
	}

	protected boolean verifyItemByOwner(String itemAttributeName, String ownerAttributeName) throws EpiDataException {
		Object itemAttributeValue = focus.getValue(itemAttributeName);
		Object ownerAttributeValue = focus.getValue(ownerAttributeName);
		if (isEmpty(itemAttributeValue) || (isEmpty(ownerAttributeValue))) {
			return true; //Do Nothing
		}
		itemAttributeValue = itemAttributeValue == null ? null : itemAttributeValue.toString().toUpperCase();
		ownerAttributeValue = ownerAttributeValue == null ? null : ownerAttributeValue.toString().toUpperCase();
		String query = "SELECT SKU FROM SKU WHERE (SKU = '" + itemAttributeValue + "') AND (STORERKEY = '" + ownerAttributeValue + "')";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1) {
			//value exists, verified
			return true;
		} else {
			//value does not exist
			return false;
		}
	}

	protected void greaterThanOrEqualToZeroValidation(String attributeName) throws UserException {
		numericValidation(attributeName);
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue)) {
			return;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value)) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		} else if (value < 0) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
		}
	}

	protected void greaterThanZeroValidation(String attributeName) throws UserException {
		numericValidation(attributeName);
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue)) {
			return;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value)) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		} else if (value <= 0) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_TAB_GREATER_THAN_ZERO", parameters);
		}
	}

	protected void greaterThanZeroValidation(String attributeName, RuntimeFormInterface subForm) throws UserException {
		numericValidation(attributeName);
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue)) {
			return;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value)) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName, subForm);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle(subForm);
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		} else if (value <= 0) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName, subForm);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle(subForm);
			throw new UserException("WMEXP_TAB_GREATER_THAN_ZERO", parameters);
		}
	}

	protected void greaterThanOrEqualToZeroValidation(String attributeName, RuntimeFormInterface f) throws UserException {
		_log.debug("LOG_DEBUG_EXTENSION", "Performing >0 Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
		numericValidation(attributeName, f);
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue)) {
			return;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value)) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName, f);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle(f);
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		} else if (value < 0) {
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName, f);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle(f);
			throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
		}
	}

	protected void numericValidation(String attributeName) throws UserException {
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false)) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		}
	}

	protected void numericValidation(String attributeName, RuntimeFormInterface f) throws UserException {
		_log.debug("LOG_DEBUG_EXTENSION", "Performing # Validation on "+attributeName+" "+f.getName(), SuggestedCategory.NONE);
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false)) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName, f);
			parameters[1] = attributeValue.toString();
			parameters[2] = retrieveFormTitle(f);
			throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
		}
	}

	protected String removeTrailingColon(String label) {
		if (label.endsWith(":")) {
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	protected boolean isAttributeNull(String attributeName) {
		Object attributeValue = focus.getValue(attributeName);
		return isNull(attributeValue);
	}

	protected boolean isAttributeEmpty(String attributeName) {
		Object attributeValue = focus.getValue(attributeName);
		return isEmpty(attributeValue);
	}

	protected void packValidation() throws EpiDataException, UserException {
		String attributeName = "PACKKEY";
		String table = "PACK";
		if (verifySingleAttribute(attributeName, table) == false) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
		}
	}

	protected void locValidation(String attributeName) throws EpiDataException, UserException {
		String table = "LOC";
		String tableAttribute = "LOC";
		if (verifySingleAttribute(attributeName, table, tableAttribute) == false) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName);
			parameters[1] = focus.getValue(attributeName).toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
		}
	}

	protected void locValidation(String attributeName,RuntimeFormInterface f) throws EpiDataException, UserException {
		String table = "LOC";
		String tableAttribute = "LOC";
		if (verifySingleAttribute(attributeName, table, tableAttribute) == false) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel(attributeName,f);
			parameters[1] = focus.getValue(attributeName).toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
		}
	}
	
	protected void pickToLocValidation(String attributeName) throws EpiDataException, UserException {
		if (verifyPickToAttribute(attributeName) == false) {
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
			parameters[1] = focus.getValue(attributeName).toString();
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_PICKTO_REQD", parameters);
		}
	}

	protected boolean verifyPickToAttribute(String attributeName) throws EpiDataException {
		String table = "LOC";
		String tableAttribute = "LOC";

		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue)) {
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM "+table+" WHERE ("+tableAttribute+" = '"+attributeValue+"' AND LOCATIONTYPE = 'PICKTO')";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n"+query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1) {
			//value exists, verified
			return true;
		} else {
			//value does not exist
			return false;
		}
	}
}