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

package com.ssaglobal.scm.wms.wm_pickdetail.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
//import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
//import com.epiphany.shr.ui.action.ccf.*;
//import com.epiphany.shr.ui.model.data.DataBean;
//import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class PickLoadValuesBasedOnOrderCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickLoadValuesBasedOnOrderCCF.class);

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

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

//		DataBean formFocus = form.getFocus();
		//retrieve Orderkey && OrderLine#
		Object tempOrderKeyValue = widgetNamesAndValues.get("ORDERKEY");
		Object tempOrderLineNumber = widgetNamesAndValues.get("ORDERLINENUMBER");
		if (isNull(tempOrderKeyValue) || isNull(tempOrderLineNumber))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Orderkey or OrderLineNumber is empty", SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		String orderKeyValue = tempOrderKeyValue == null ? null : tempOrderKeyValue.toString().toUpperCase();
		String orderLineNumberValue = tempOrderLineNumber == null ? null : tempOrderLineNumber.toString().toUpperCase();

		//query Orderdetail, retrieve STORERKEY, SKU, PACKKEY, CARTONGROUP

		UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
		final String bio = "wm_orderdetail";
		final String attribute1 = "ORDERKEY";
		final String attribute2 = "ORDERLINENUMBER";
		String queryStatement = bio + "." + attribute1 + " = '" + orderKeyValue + "'" + " and " + bio + "."
				+ attribute2 + " = '" + orderLineNumberValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "////query statement " + queryStatement, SuggestedCategory.NONE);
		Query query = new Query(bio, queryStatement, null);
		BioCollection results = uow.getBioCollectionBean(query);
		if (results.size() == 1)
		{
			Bio resultBio = results.elementAt(0);
			setValue(form.getFormWidgetByName("STORERKEY"), retrieveValue("STORERKEY", resultBio));
			setValue(form.getFormWidgetByName("SKU"), retrieveValue("SKU", resultBio));
			setValue(form.getFormWidgetByName("PACKKEY"), retrieveValue("PACKKEY", resultBio));
			setValue(form.getFormWidgetByName("CARTONGROUP"), retrieveValue("CARTONGROUP", resultBio));
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Order does not exist", SuggestedCategory.NONE);
		}

		return RET_CONTINUE;

	}

	private String retrieveValue(String attribute, Bio resultBio) throws EpiDataException
	{
		Object temp = resultBio.get(attribute);
		if (isNull(temp))
		{
			return null;
		}
		else
		{
			return temp.toString();
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

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}
}
