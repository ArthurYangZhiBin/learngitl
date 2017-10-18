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

package com.ssaglobal.scm.wms.wm_owner.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

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
public class OwnerDuplicateOwnerLabelValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerDuplicateOwnerLabelValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{

		_log.debug("LOG_SYSTEM_OUT","\n\n!@# Start of OwnerDuplicateOwnerLabelValidationCCF",100L);

		final String name = getParameterString("NAME");
		final String errorMessage = (getParameter("ERROR_MESSAGE") != null) ? getParameterString("ERROR_MESSAGE")
				: "Duplicate Entry";

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		//Retrieve Values of 
		//		Label Type – STORERLABELS.LABELTYPE
		//		Label Name – STORERLABELS.LABELNAME
		//		Customer – STORERLABELS.CONSIGNEEKEY

		Object tempLabelNameValue = widgetNamesAndValues.get("LABELNAME");
		Object tempLabelTypeValue = widgetNamesAndValues.get("LABELTYPE");
		Object tempConsigneeKeyValue = widgetNamesAndValues.get("CONSIGNEEKEY");
		String storerKeyValue = widgetNamesAndValues.get("STORERTEMP").toString();
		_log.debug("LOG_SYSTEM_OUT","^______________^",100L);

		boolean duplicateTypeAndCustomer;
		boolean duplicateTypeAndName;
		if (isNotNull(tempLabelNameValue, "LABELTYPE"))
		{
			//perform Type & Customer
			duplicateTypeAndCustomer = isDuplicateTypeAndCustomer(tempLabelTypeValue, tempConsigneeKeyValue, storerKeyValue);

			if (isNotNull(tempLabelTypeValue, "LABELNAME"))
			{
				//perform Type & Name
				duplicateTypeAndName = isDuplicateTypeAndName(tempLabelTypeValue, tempLabelNameValue, storerKeyValue);

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","\n!@# Label Name is Null, can't check for duplicates",100L);
				return RET_CONTINUE;
			}
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","\n!@# Label Type is Null, can't check for duplicates",100L);
			return RET_CONTINUE;

		}

		RuntimeFormWidgetInterface labelNameWidget = form.getFormWidgetByName("LABELNAME");
		RuntimeFormWidgetInterface customerWidget = form.getFormWidgetByName("CONSIGNEEKEY");
		if (duplicateTypeAndName)
		{
			_log.debug("LOG_SYSTEM_OUT","//// duplicateTypeAndName Duplicate Validation Failed",100L);
			setErrorMessage(labelNameWidget, "Duplicate Label Name");
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// duplicateTypeAndName Validation Passed - ",100L);
			setErrorMessage(labelNameWidget, "");
		}

		if (duplicateTypeAndCustomer)
		{
			_log.debug("LOG_SYSTEM_OUT","//// duplicateTypeAndCustomer Duplicate Validation Failed",100L);
			setErrorMessage(customerWidget, "Duplicate Customer");
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// duplicateTypeAndCustomer Duplicate Validation Passed - ",100L);
			setErrorMessage(customerWidget, "");
		}

		if (duplicateTypeAndCustomer || duplicateTypeAndName)
		{
			return RET_CANCEL;
		}
		else
		{
			return RET_CONTINUE;
		}

	}

	private boolean isDuplicateTypeAndName(Object tempLabelTypeValue, Object tempLabelNameValue, String storerKeyValue)
			throws EpiDataException
	{
		String labelNameValue = null;
		String labelTypeValue = null;
		labelNameValue = tempLabelNameValue.toString();
		labelTypeValue = tempLabelTypeValue.toString();

		//Query Table
		String query = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelTypeValue + "') "
				+ "AND (LABELNAME = '" + labelNameValue + "') " + "AND (STORERKEY = '" + storerKeyValue + "')";
		_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		//If results >=1. duplicate entry
		if (results.getRowCount() >= 1)
		{
			_log.debug("LOG_SYSTEM_OUT","@!# Duplicate Type - Name",100L);
			return true;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","@!# No Duplicate Type - Name",100L);
			return false;

		}

	}

	private boolean isDuplicateTypeAndCustomer(Object tempLabelTypeValue, Object tempConsigneeKeyValue, String storerKeyValue)
			throws EpiDataException
	{
		String labelTypeValue = null;
		String consigneeKeyValue = null;
		if (isNotNull(tempConsigneeKeyValue, "CONSIGNEEKEY"))
		{
			consigneeKeyValue = tempConsigneeKeyValue.toString();

		}
		else
		{
			consigneeKeyValue = "DEFAULT";
		}

		labelTypeValue = tempLabelTypeValue.toString();

		//Query Table
		String query = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelTypeValue + "') "
				+ "AND (CONSIGNEEKEY = '" + consigneeKeyValue + "')" + "AND (STORERKEY = '" + storerKeyValue + "')";
		_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		//If results >=1. duplicate entry
		if (results.getRowCount() >= 1)
		{
			_log.debug("LOG_SYSTEM_OUT","@!# Duplicate Type - Customer",100L);
			return true;

		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","@!# No Duplicate Type - Customer",100L);
			return false;

		}

	}

	private void listParams(HashMap params)
	{
		//		List Params
		_log.debug("LOG_SYSTEM_OUT","Listing Paramters-----",100L);
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_SYSTEM_OUT",key.toString() + " " + value.toString(),100L);
		}
	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_SYSTEM_OUT","# " + widgetNames[i] + " " + widgetValues[i],100L);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}

	private boolean isNotNull(Object attributeValue, String attribute)
			throws EpiDataException
	{

		if (attributeValue == null)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is null ",100L);
			return false;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is blank ",100L);
			return false;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is " + attributeValue.toString(),100L);
			return true;
		}

	}

}
