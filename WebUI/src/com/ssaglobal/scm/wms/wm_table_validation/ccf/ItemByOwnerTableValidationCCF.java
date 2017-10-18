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

package com.ssaglobal.scm.wms.wm_table_validation.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.CCFUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension, SendAllWidgetsValuesExtensionBase
 */
public class ItemByOwnerTableValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemByOwnerTableValidationCCF.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException 
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);

		final String name = getParameterString("NAME");
		final String table = "SKU"; //getParameterString("BIO");
		final String attribute = "SKU"; //getParameterString("ATTRIBUTE");
		final String errorMessage = (getParameter("ERROR_MESSAGE") != null) ? getParameterString("ERROR_MESSAGE")
				: "WMEXP_INVALID_ITEM_BY_OWNER";
		final String ownerWidgetName = getParameterString("OWNER_WIDGET");
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		String ownerValue = isEmpty(widgetNamesAndValues.get(ownerWidgetName)) ? null
				: widgetNamesAndValues.get(ownerWidgetName).toString().toUpperCase();

		System.out.print("\n\n~!@ START OF " + name + " ItemByOwnerTableValidationCCF for ");

		String widgetValue = params.get("fieldValue") == null ? null
				: params.get("fieldValue").toString().toUpperCase();

		if (isEmpty(widgetValue))
		{
			return RET_CANCEL;
		}


		_log.debug("LOG_DEBUG_EXTENSION", "!@# Validating Widget: " + formWidget.getName() + " Value: " + widgetValue, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "!@# By " + ownerWidgetName + " : " + ownerValue, SuggestedCategory.NONE);

		if (isEmpty(ownerValue))
		{
			String[] parameters = new String[1];
			parameters[0] = widgetValue;
			setErrorMessage(formWidget, getTextMessage("WMEXP_INVALID_ITEM", parameters, state.getLocale()));
			return RET_CANCEL;
		}

		String query = "SELECT * " + " FROM " + table + " WHERE " + attribute + " = '" + widgetValue + "' "
				+ " AND STORERKEY = '" + ownerValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		// Query Bio to see if Attribute exists
		//		String queryStatement = null;
		//		Query query = null;
		//		BioCollection results = null;
		//
		//		UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
		//		queryStatement = bio + "." + attribute + " = '" + widgetValue + "'" + " AND " + bio + ".STORERKEY = " + ownerValue;
		//		_log.debug("LOG_DEBUG_EXTENSION", "////query statement " + queryStatement, SuggestedCategory.NONE);
		//		query = new Query(bio, queryStatement, null);
		//		results = uow.getBioCollectionBean(query);

		// If size equals 0, return RET_CANCEL
		if (results.getRowCount() == 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "//// " + name + " Validation Failed", SuggestedCategory.NONE);
			String[] parameters = new String[4];
			parameters[0] = formWidget.getLabel("label", locale);
			parameters[1] = widgetValue;
			parameters[2] = form.getFormWidgetByName(ownerWidgetName).getLabel("label", locale);
			parameters[3] = ownerValue;
			setErrorMessage(formWidget, getTextMessage(errorMessage, parameters, state.getLocale()));
			return RET_CANCEL;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "//// " + name + " Validation Passed - " + formWidget.getName() + ": " + widgetValue, SuggestedCategory.NONE);
			setStatus(STATUS_SUCCESS);
			setErrorMessage(formWidget, "");
			return RET_CONTINUE;

		}

	}

	boolean isEmpty(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else if (attributeValue.toString().matches("null"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void listParams(HashMap params)
	{
		//		List Params
		_log.debug("LOG_DEBUG_EXTENSION", "Listing Paramters-----", SuggestedCategory.NONE);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_DEBUG_EXTENSION", key.toString() + " " + value.toString(), SuggestedCategory.NONE);
		}
	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}
}
