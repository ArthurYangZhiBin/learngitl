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
//import java.util.Iterator;
//import java.util.Map;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
//import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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
public class PickLoadToLocCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickLoadToLocCCF.class);

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

		//retrieve LOC
		//query LOC & PUTAWAYZONE
		//retrieve PICKTOLOC and populate TOLOC
		Object tempLocValue = params.get("fieldValue");
		RuntimeFormWidgetInterface toLocWidget = form.getFormWidgetByName("TOLOC");
		if (isEmpty(tempLocValue))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@ Empty location, unable to check", SuggestedCategory.NONE);
			setValue(toLocWidget, "");
			return RET_CANCEL;
		}
		String locValue = tempLocValue == null ? null : tempLocValue.toString().toUpperCase();

		String query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC "
				+ "FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE = PUTAWAYZONE.PUTAWAYZONE " + "WHERE LOC.LOC = '"
				+ locValue + "'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!! At " + results.getCurrentRow(), SuggestedCategory.NONE);
			String toLoc = results.getAttribValue(2).getAsString();
			_log.debug("LOG_DEBUG_EXTENSION", " ToLoc " + toLoc, SuggestedCategory.NONE);

			setValue(toLocWidget, toLoc);
			toLocWidget.setDisplayValue(toLoc);
			BioBean currentFormFocus = null;
			currentFormFocus = form.getFocus().getUnitOfWorkBean().getNewBio((QBEBioBean) form.getFocus());

			currentFormFocus.setValue("TOLOC", toLoc);

			Object readToLoc = currentFormFocus.getValue("TOLOC");
			_log.debug("LOG_DEBUG_EXTENSION", "!@ " + readToLoc.toString(), SuggestedCategory.NONE);

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Location Validation Failed", SuggestedCategory.NONE);
		}
		return RET_CONTINUE;

	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException
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

//	private void listParams(HashMap params)
//	{
//		//		List Params
//
//		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
//		{
//			Map.Entry entry = (Map.Entry) it.next();
//			Object key = entry.getKey();
//			Object value = entry.getValue();
//
//		}
//	}
}
