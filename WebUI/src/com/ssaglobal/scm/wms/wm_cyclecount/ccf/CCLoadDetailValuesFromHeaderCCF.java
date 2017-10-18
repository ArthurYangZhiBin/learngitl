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

package com.ssaglobal.scm.wms.wm_cyclecount.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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
public class CCLoadDetailValuesFromHeaderCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCLoadDetailValuesFromHeaderCCF.class);

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

		_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@# Start of CCLoadDetailValuesFromHeaderCCF", SuggestedCategory.NONE);
		String sourceForm = form.getName();
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Source Form " + sourceForm, SuggestedCategory.NONE);
		String destinationWidgetName = getParameterString("WIDGET");
		_log.debug("LOG_DEBUG_EXTENSION", "Parameter Widget " + destinationWidgetName, SuggestedCategory.NONE);
		RuntimeFormWidgetInterface destinationWidget = null;
		Object possibleDestinationValue = null;

		Object headerSku = null;
		Object headerOwner = null;
		Object headerLoc = null;

		RuntimeFormWidgetInterface sysQtyWidget = null;
		RuntimeFormWidgetInterface adjQtyWidget = null;
		RuntimeFormWidgetInterface skuDescr = null;

		HashMap allWidgets = getWidgetsValues(params);

		//iterate through all the widgets
		for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
			Object value = entry.getValue();
			try
			{
				_log.debug("LOG_DEBUG_EXTENSION", "*& " + key.getName(), SuggestedCategory.NONE);
				//get the widget that has the right name but is on a different form

				if ((key.getName().equals(destinationWidgetName)) && !(key.getForm().getName().equals(sourceForm)))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "***Selecting this widget " + key.getName(), SuggestedCategory.NONE);
					destinationWidget = key;
					possibleDestinationValue = value;

				}

				//Header SKU
				if ((key.getName().equals("SKU")) && (key.getForm().getName().equals(sourceForm)))
				{
					headerSku = value;
				}

				//Header STORER
				if ((key.getName().equals("STORERKEY")) && (key.getForm().getName().equals(sourceForm)))
				{
					headerOwner = value;
				}

				//Header LOC
				if ((key.getName().equals("LOC")) && (key.getForm().getName().equals(sourceForm)))
				{
					headerLoc = value;
				}

				//SYSQTY
				if ((key.getName().equals("SYSQTY")))
				{
					sysQtyWidget = key;
				}

				//ADJQTY
				if ((key.getName().equals("ADJQTY")))
				{
					adjQtyWidget = key;
				}

				//SKU Descr
				if ((key.getName().equals("DESCRIPTION")))
				{
					skuDescr = key;
				}

			} catch (NullPointerException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Exception Caught" + e.getMessage(), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "No detail form", SuggestedCategory.NONE);
			}
		}

		//copy value to detail if the widget exists, it is a new record,
		if (((destinationWidget != null) && (!(destinationWidget.getForm().getFocus() instanceof BioBean))))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! New Detail, loading value ", SuggestedCategory.NONE);
			String destinationValue = isNull(params.get("fieldValue")) ? "" : params.get("fieldValue").toString();
			setValue(destinationWidget, destinationValue);

			//Need to calculate SYSQTY and ADJQTY
			if (!isNull(headerSku) && !isNull(headerOwner) && !isNull(headerLoc) && !isNull(sysQtyWidget)
					&& !isNull(adjQtyWidget))
			{
				headerSku = headerSku == null ? null : headerSku.toString().toUpperCase();
				headerLoc = headerLoc == null ? null : headerLoc.toString().toUpperCase();
				headerOwner = headerOwner == null ? null : headerOwner.toString().toUpperCase();
				//Query LOTXLOCXID
				String query = "SELECT SUM(QTY) " + "FROM LOTXLOCXID " + "WHERE (SKU = '" + headerSku
						+ "') AND (LOC = '" + headerLoc + "') AND (STORERKEY = '" + headerOwner + "') ";
				EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
				double sysQty = 0;
				if (results.getRowCount() == 1)
				{
					String result = results.getAttribValue(1).getAsString();
					_log.debug("LOG_DEBUG_EXTENSION", result, SuggestedCategory.NONE);
					if (!result.equalsIgnoreCase("N/A"))

					{
						sysQty = Double.parseDouble(result);
						_log.debug("LOG_DEBUG_EXTENSION", "New Sys Qty " + sysQty, SuggestedCategory.NONE);
					}
				}
				setValue(sysQtyWidget, String.valueOf(sysQty));
				setValue(adjQtyWidget, String.valueOf(sysQty));

			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Not calculating QTYs", SuggestedCategory.NONE);
			}

			//Need to set description only when item is updating
			//			if (destinationWidgetName.equals("SKU") && !isNull(skuDescr))
			//			{
			//				//Query SKU
			//				String query = "SELECT DESCR FROM SKU WHERE SKU = '" + headerSku + "'";
			//				EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			//				if(results.getRowCount() == 1 )
			//				{
			//					String description = results.getAttribValue(1).getAsString();
			//					_log.debug("LOG_DEBUG_EXTENSION", "Description  " + description, SuggestedCategory.NONE);
			//					setValue(skuDescr, description);
			//				}
			//			}
			//			else
			//			{
			//				_log.debug("LOG_DEBUG_EXTENSION", "!@# Didn't get description " + destinationWidgetName, SuggestedCategory.NONE);
			//			}

		}
		return RET_CONTINUE;

	}

	private boolean isNull(Object attributeValue) throws EpiDataException
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
}
