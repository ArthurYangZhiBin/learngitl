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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.text.NumberFormat;

import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class EnforcePrecision extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EnforcePrecision.class);
	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param widget The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(
			StateInterface state, RuntimeFormWidgetInterface widget)
	{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing EnforcePrecision",100L);
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW


		int maxFractionDigits = (getParameter("MAX_FRACTION_DIGITS") != null) ? getParameterInt("MAX_FRACTION_DIGITS")
				: nf.getMaximumFractionDigits();
		int minFractionDigits = (getParameter("MIN_FRACTION_DIGITS") != null) ? getParameterInt("MIN_FRACTION_DIGITS")
				: nf.getMinimumFractionDigits();
		int maxIntegerDigits = nf.getMaximumIntegerDigits();
		int minIntegerDigits = nf.getMinimumIntegerDigits();
		
		boolean grouping = false;
		String bioAttr = getParameter("BIO_ATTR") == null || getParameter("BIO_ATTR").toString().length() == 0?"":getParameter("BIO_ATTR").toString();

		setNumberFormat(nf, maxFractionDigits, minFractionDigits, maxIntegerDigits, minIntegerDigits, grouping);
		


		try
		{

		if (state.getFocus() instanceof BioBean)
		{
			//Old Record

			Object tempValue = widget.getValue();
			if (tempValue == null)
			{
				//_log.debug("LOG_SYSTEM_OUT","Value uninitialized, setting to default value",100L);
			}
			Number unformattedValue = nf.parse(tempValue.toString());
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Unformatted Val: " +unformattedValue,100L);
			String formattedValue = nf.format(unformattedValue);
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Formatted Value: " +formattedValue,100L);
			widget.setDisplayValue(formattedValue.toString());
			if(bioAttr.length() > 0)
				widget.getForm().getFocus().setValue(bioAttr,new Double(formattedValue.toString()));
		}
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;

		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting EnforcePrecision",100L);
		return RET_CONTINUE;

	}

	private void setNumberFormat(
			NumberFormat nf, int maxFractionDigits, int minFractionDigits, int maxIntegerDigits, int minIntegerDigits,
			boolean grouping)
	{
		//		Format
		nf.setMaximumFractionDigits(maxFractionDigits);
		nf.setMinimumFractionDigits(minFractionDigits);

		nf.setMaximumIntegerDigits(maxIntegerDigits);
		nf.setMinimumIntegerDigits(minIntegerDigits);

		nf.setGroupingUsed(grouping);
	}
}
