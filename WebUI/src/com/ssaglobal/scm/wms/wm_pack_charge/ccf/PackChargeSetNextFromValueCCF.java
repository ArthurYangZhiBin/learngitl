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

package com.ssaglobal.scm.wms.wm_pack_charge.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import java.util.regex.*;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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
public class PackChargeSetNextFromValueCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PackChargeSetNextFromValueCCF.class);
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

		RuntimeFormWidgetInterface toWidget = formWidget;
		String widgetName = toWidget.getName();
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + widgetName + "\n", SuggestedCategory.NONE);

		Pattern prefixAndColumn = Pattern.compile("(\\w*)TO(\\d)");
		Matcher m = prefixAndColumn.matcher(widgetName);
		String prefix = null;
		int column = 0;
		if (m.find())
		{
			prefix = m.group(1);
			column = Integer.parseInt(m.group(2));
		}
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tPrefix" + prefix + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tCol" + column + "\n", SuggestedCategory.NONE);

		//get widget value
		Object currentValue = params.get("fieldValue");
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tForm" + currentValue + "\n", SuggestedCategory.NONE);
		double parsedValue = 0;
		try
		{
			parsedValue = Double.parseDouble((String) currentValue);
		} catch (Exception e)
		{

		}
//		//Check To Value is Greater than Previous From Value
//		double previousValue = 0;
//		try
//		{
//			previousValue = Double.valueOf((String) widgetNamesAndValues.get(prefix + "FROM" + (column))).doubleValue();
//		} catch (Exception e)
//		{
//
//		}
//		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + previousValue + "\n", SuggestedCategory.NONE);
//		if (parsedValue <= previousValue)
//		{
//			String localizedError = getTextMessage("WMEXP_PC_GREATER", new Object[] {}, state.getLocale());
//			setErrorMessage(formWidget, localizedError);
//			return RET_CANCEL;
//		}
//		else
//		{
//			setErrorMessage(formWidget, "");
//		}

		//set from value
		int nextColumn = column + 1;
		if (nextColumn <= 3)
		{
			setValue(form.getFormWidgetByName(prefix + "FROM" + (nextColumn)), String.valueOf(parsedValue + 1));
		}

		//clear FROM,TO and CHARGE widgets in higher number columns
		for (int i = nextColumn + 1; i <= 3; i++)
		{
			//FROM
			setValue(form.getFormWidgetByName(prefix + "FROM" + i), "");
		}
		for (int i = nextColumn; i < 3; i++)
		{
			//TO
			setValue(form.getFormWidgetByName(prefix + "TO" + i), "");
		}
		for (int i = column; i <= 3; i++)
		{
			//CHARGE
			setValue(form.getFormWidgetByName(prefix + "CHARGE" + i), "");
		}

		return RET_CONTINUE;

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
