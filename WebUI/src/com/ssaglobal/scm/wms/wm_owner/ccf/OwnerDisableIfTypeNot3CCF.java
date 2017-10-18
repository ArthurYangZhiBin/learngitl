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
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_owner.bio.OwnerUDFLabelSaveValidation;

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
public class OwnerDisableIfTypeNot3CCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerDisableIfTypeNot3CCF.class);

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{

		_log.debug("LOG_SYSTEM_OUT","=-=-= OwnerDisableIfType3CCF start",100L);
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);

		String formWidgetName = params.get("").toString();
		String formWidgetValue = params.get("fieldValue").toString();
		formWidgetValue = formWidgetValue == null ? null : formWidgetValue.toString();

		_log.debug("LOG_SYSTEM_OUT","!@# Widget: " + formWidgetName + " - " + formWidgetValue,100L);

		// Get Owner Value

		// Query Bio/Table to determine if type = 3
		String query = "SELECT * FROM STORER WHERE (STORERKEY = " + formWidgetValue + "') AND (TYPE = 3)";
		_log.debug("LOG_SYSTEM_OUT","///QUERY " + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

		// If type != 3, disable else, enable
		if( results.getRowCount() != 1)
		{
			
		}
		else 
		{
			
		}
		

		

		return RET_CONTINUE;

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

	private void listParams(HashMap params)
	{
		//List Params
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
