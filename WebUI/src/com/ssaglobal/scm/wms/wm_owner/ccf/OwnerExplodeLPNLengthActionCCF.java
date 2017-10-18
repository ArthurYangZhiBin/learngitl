/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_owner.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// TODO: Auto-generated Javadoc

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>.
 *
 * @return int RET_CONTINUE, RET_CANCEL
 */

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class OwnerExplodeLPNLengthActionCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	
	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerExplodeLPNLengthActionCCF.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 *
	 * @param form the form
	 * @param formWidget the form widget
	 * @param params the params
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException the epi exception
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		//retrieve LPN Length
		Object tempValue = params.get("fieldValue");
		if (tempValue == null)
		{
			return RET_CANCEL;
		}
		int lpnLengthValue = Integer.parseInt(tempValue.toString());
		
		//Krishna Kuchipudi: Dec-16-2009: ASN Explode related code: Starts
		DataBean focus = form.getFocus();
		String OwnerPrefix = null;
		if(focus.getValue("OWNERPREFIX") != null)
		{OwnerPrefix = focus.getValue("OWNERPREFIX").toString();}
		int  OwnerPrefixLengh	= 0;	
		if(!(OwnerPrefix == null)){
			OwnerPrefixLengh = OwnerPrefix.length();
		}	
		if(OwnerPrefixLengh >= lpnLengthValue){
			
			_log.debug("LOG_DEBUG_EXTENSION", "OWNERPREFIX cann't be greater than or equal to EXPLODELPNLENGTH", SuggestedCategory.NONE);
			throw new UserException("WMEXP_IDLENGTH_LESS", new Object[] {});
		}
		lpnLengthValue = lpnLengthValue - OwnerPrefixLengh;
		
		//Krishna Kuchipudi: Dec-16-2009: ASN Explode related code: Ends

		NumberFormat lpnNF = NumberFormat.getIntegerInstance();
		lpnNF.setMinimumIntegerDigits(lpnLengthValue);
		lpnNF.setMaximumIntegerDigits(lpnLengthValue);
		lpnNF.setGroupingUsed(false);
		//set LPN Start
		String formattedLpnStart = lpnNF.format(1);
		if(OwnerPrefixLengh== 0)
		{
		setValue(form.getFormWidgetByName("EXPLODELPNSTARTNUMBER"), formattedLpnStart);
		}else{
			setValue(form.getFormWidgetByName("EXPLODELPNSTARTNUMBER"), formattedLpnStart);
		}
	
		
		//set LPN Next
		//retrieve current LPN Next
		//if null, set to 1
		//then format
		int lpnNextNumber;
		try
		{
			lpnNextNumber = Integer.parseInt(widgetNamesAndValues.get("EXPLODENEXTLPNNUMBER").toString());
		} catch (NullPointerException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Null value for lpn next, setting to 1", SuggestedCategory.NONE);
			lpnNextNumber = 1;
		}
		try
		{
			String formattedLpnNext = lpnNF.format(lpnNextNumber);
			if(OwnerPrefixLengh== 0)
			{
			setValue(form.getFormWidgetByName("EXPLODENEXTLPNNUMBER"), formattedLpnNext);
			}else{
				setValue(form.getFormWidgetByName("EXPLODENEXTLPNNUMBER"), formattedLpnNext);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		//set LPN Rollback
		StringBuffer formattedLpnRollback = new StringBuffer();
		for (int i = 0; i < lpnLengthValue; i++)
		{
			formattedLpnRollback.append("9");
		}
		if(OwnerPrefixLengh== 0)
		{
		setValue(form.getFormWidgetByName("EXPLODELPNROLLBACKNUMBER"),(formattedLpnRollback.toString()));
		}else{
			setValue(form.getFormWidgetByName("EXPLODELPNROLLBACKNUMBER"),formattedLpnRollback.toString());
		}
		return RET_CONTINUE;

	}

	/**
	 * Retrieve widget names and values.
	 *
	 * @param params the params
	 * @param widgetNamesAndValues the widget names and values
	 */
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

	/**
	 * List params.
	 *
	 * @param params the params
	 */
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
}
