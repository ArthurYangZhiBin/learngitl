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
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.util.CCFUtil;

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
public class TableValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TableValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		String widgetName = formWidget.getLabel("label", locale).trim();

		final String name = getParameterString("NAME");
		final String bio = getParameterString("BIO");
		final String attribute = getParameterString("ATTRIBUTE");
		final String errorMessage = (getParameter("ERROR_MESSAGE") != null) ? getParameterString("ERROR_MESSAGE")
				: "WMEXP_NOT_A_VALID";

		_log.debug("LOG_SYSTEM_OUT","\n\n~!@ START OF " + name + " VALIDATION",100L);

		Object temp = params.get("fieldValue");
		String tempValue;

		if (isNull(temp))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + name + " is null, doing nothing",100L);
			return RET_CANCEL;
		}
		else
		{
			tempValue = temp.toString().toUpperCase();
		}

		_log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + formWidget.getName() + " Value: " + tempValue,100L);

		// Query Bio to see if Attribute exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;

		UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
		queryStatement = bio + "." + attribute + " = '" + tempValue + "'";
		_log.debug("LOG_SYSTEM_OUT","////query statement " + queryStatement,100L);
		query = new Query(bio, queryStatement, null);
		results = uow.getBioCollectionBean(query);

		// If BioCollection size equals 0, return RET_CANCEL
		if (results.size() == 0)
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Validation Failed",100L);
			//setErrorMessage(formWidget, errorMessage);
			//setFormErrorMessage(errorMessage);
			String parameters[] = new String[2];
			parameters[0] = tempValue;
			parameters[1] = removeTrailingColon(widgetName);
			//setErrorMessage(formWidget, CCFUtil.constructErrorMessage(errorMessage, parameters));
			setErrorMessage(formWidget, getTextMessage(errorMessage, parameters, locale));
			return RET_CANCEL;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Validation Passed - " + formWidget.getName() + ": " + tempValue,100L);
			setFormErrorMessage("");
			setErrorMessage(formWidget, "");
			return RET_CONTINUE;

		}

	}

	private boolean isNull(Object attributeValue) throws EpiDataException
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

	private String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
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
}
