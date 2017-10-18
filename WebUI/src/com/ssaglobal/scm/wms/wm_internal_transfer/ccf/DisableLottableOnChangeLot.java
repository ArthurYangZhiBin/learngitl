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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_internal_transfer.ui.EnforcePrecision;

public class DisableLottableOnChangeLot extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableLottableOnChangeLot.class);
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
	throws EpiException
{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","DisableLottableOnChangeLot",100L);
		Object tempValue = params.get("fieldValue");

		boolean disableLottables= true;
		final String BLANK= "  ";
		
		//tempValue.toString().matches("[\t\n\r]+"))
		if((tempValue == null )|| (tempValue.toString().equals("null")) || (tempValue.toString().matches("[\t\n\r]+")))
			disableLottables= false;


		
		HashMap allWidgets = getWidgetsValues(params);
		
		for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
			try
			{
				if ((key.getForm().getName().equals("wm_internal_transfer_detail_detail_lottable_slot")))
				{
						if(disableLottables)
						{
						setProperty(key, RuntimeFormWidgetInterface.PROP_READONLY, "true");
						if(!(key.getName().equals("LOTTABLE04")) && !(key.getName().equals("LOTTABLE05")))
							setValue(key, " " );
							
						}
						else
						{
								setProperty(key, RuntimeFormWidgetInterface.PROP_READONLY, "false");
						}
				}
				else if((key.getName().equals("TOLOT")) && (key.getForm().getName().equals("wm_internal_transfer_detail_detail_to_slot")))
				{
					if(!disableLottables)
					{
						setValue(key, " ");
					}
				}
			
			}	
			catch (NullPointerException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing Exception",100L);
				e.printStackTrace();
			}
			
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting DisableLottableOnChangeLot",100L);
		return RET_CANCEL;
}
}
