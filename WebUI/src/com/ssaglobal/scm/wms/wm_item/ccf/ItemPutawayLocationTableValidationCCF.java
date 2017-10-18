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

package com.ssaglobal.scm.wms.wm_item.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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
public class ItemPutawayLocationTableValidationCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemPutawayLocationTableValidationCCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{

		final String name = "Putaway Location";
		final String table = "LOC";
		final String attribute = "LOC";
		final String errorMessage = (getParameter("ERROR_MESSAGE") != null) ? getParameterString("ERROR_MESSAGE") : "Invalid "
				+ name;

		System.out.print("\n\n~!@ START OF " + name + " VALIDATION for ");
		String tempValue = params.get("fieldValue").toString();
		System.out.print(formWidget.getName() + ": " + tempValue + "\n");
		//

		_log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + formWidget.getName() + " Value: " + tempValue,100L);
		String query = "SELECT * " + " FROM " + table + " WHERE " + attribute + " = '" + tempValue + "' "
				+ " AND (LOCATIONTYPE NOT IN ('PICKTO','STAGED'))";
		_log.debug("LOG_SYSTEM_OUT","///QUERY " + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		//		// Query Bio to see if Attribute exists
		//		String queryStatement = null;
		//		Query query = null;
		//		BioCollection results = null;
		//
		//		UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
		//		queryStatement = table + "." + attribute + " = '" + tempValue + "'";
		//		_log.debug("LOG_SYSTEM_OUT","////query statement " + queryStatement,100L);
		//		query = new Query(table, queryStatement, null);
		//		results = uow.getBioCollectionBean(query);

		// If size equals 0, return RET_CANCEL
		if (results.getRowCount() == 0)
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Validation Failed",100L);
			setErrorMessage(formWidget, errorMessage);
			return RET_CANCEL;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Validation Passed - " + formWidget.getName() + ": " + tempValue,100L);
			setStatus(STATUS_SUCCESS);
			setErrorMessage(formWidget, "");
			return RET_CONTINUE;

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
}
