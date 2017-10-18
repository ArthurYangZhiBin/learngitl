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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;

import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ConvertDecimalToCurrency extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param widget The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertDecimalToCurrency.class);

	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n!@# Start of ConvertDecimalToCurrency",100L);
	
		try
		{
			Object tempValue = widget.getValue();
			if (tempValue == null)
			{
				_log.debug("LOG_SYSTEM_OUT","Value uninitialized, setting to default value",100L);
				tempValue = "0.00";

				double unformattedValue = Double.parseDouble(tempValue.toString());
				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Unformatted Value " + unformattedValue,100L);

				//Format
				String formattedValue = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0).format(unformattedValue);
				widget.setDisplayValue(formattedValue);

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@@@ Should be displayed as Currency already",100L);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;

		}

		return RET_CONTINUE;

	}
}
