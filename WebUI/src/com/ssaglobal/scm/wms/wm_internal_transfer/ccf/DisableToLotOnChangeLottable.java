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
package com.ssaglobal.scm.wms.wm_internal_transfer.ccf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.date.DateUtil;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_internal_transfer.ui.EnforcePrecision;

public class DisableToLotOnChangeLottable extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableToLotOnChangeLottable.class);
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
	throws EpiException
{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing DisableToLotOnChangeLottable",100L);
	Object tempValue = params.get("fieldValue");
	System.out.print("\n&&&" +formWidget.getName() + ": " + tempValue + "\n");

	boolean disableToLot= true;

	if((tempValue == null )|| (tempValue.toString().matches("[\t\n\r]+")))
		disableToLot= false;

	
	
	
	HashMap allWidgets = getWidgetsValues(params);
	
	for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
	{
		Map.Entry entry = (Map.Entry) it.next();
		RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
		try
		{
			if((key.getName().equals("TOLOT")) && (key.getForm().getName().equals("wm_internal_transfer_detail_detail_to_slot")))
			{
					if(disableToLot)
					{
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Disabling: "+key.getName(),100L);
					setProperty(key, RuntimeFormWidgetInterface.PROP_READONLY, "true");
					setValue(key, " ");
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Enabling: "+key.getName(),100L);	
						setProperty(key, RuntimeFormWidgetInterface.PROP_READONLY, "false");
					}
			}
		
		}	
		catch (NullPointerException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing Exception- Null Pointer ",100L);
			e.printStackTrace();
		}
		
	}
	_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting DisableToLotOnChangeLottable",100L);
	return RET_CONTINUE;
	}
	
}
