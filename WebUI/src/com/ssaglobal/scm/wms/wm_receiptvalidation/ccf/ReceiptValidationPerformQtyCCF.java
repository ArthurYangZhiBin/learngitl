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

package com.ssaglobal.scm.wms.wm_receiptvalidation.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

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
public class ReceiptValidationPerformQtyCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
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
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{

		Object tempValue = params.get("fieldValue");
		String performQtyValidationValue = null;
		if (!isEmpty(tempValue))
		{
			performQtyValidationValue = tempValue.toString();
		}
		else
		{
			//treating as No
			performQtyValidationValue = "0";
		}
		//		Widgets
		RuntimeFormWidgetInterface PERFORMQTYVALIDATION = form.getFormWidgetByName("PERFORMQTYVALIDATION");
		RuntimeFormWidgetInterface OVERAGEMESSAGE = form.getFormWidgetByName("OVERAGEMESSAGE");
		RuntimeFormWidgetInterface OVERAGEHARDERROR = form.getFormWidgetByName("OVERAGEHARDERROR");
		RuntimeFormWidgetInterface OVERAGEHARDERRORPERCENT = form.getFormWidgetByName("OVERAGEHARDERRORPERCENT");
		RuntimeFormWidgetInterface OVERAGEOVERIDEPERCENT = form.getFormWidgetByName("OVERAGEOVERIDEPERCENT");
		RuntimeFormWidgetInterface OVERAGEOVERRIDE = form.getFormWidgetByName("OVERAGEOVERRIDE");

		if (performQtyValidationValue.equals("0"))
		{
			//No
			//Disable
//			setProperty(OVERAGEMESSAGE, RuntimeFormWidgetInterface.PROP_READONLY, "true");
//			setProperty(OVERAGEHARDERROR, RuntimeFormWidgetInterface.PROP_READONLY, "true");
//			setProperty(OVERAGEOVERRIDE, RuntimeFormWidgetInterface.PROP_READONLY, "true");
//			setProperty(OVERAGEHARDERRORPERCENT, RuntimeFormWidgetInterface.PROP_READONLY, "true");
//			setProperty(OVERAGEOVERIDEPERCENT, RuntimeFormWidgetInterface.PROP_READONLY, "true");
			//Set Values
			setValue(OVERAGEMESSAGE, "0");
			setValue(OVERAGEHARDERROR, "0");
			setValue(OVERAGEHARDERRORPERCENT, "");
			setValue(OVERAGEOVERIDEPERCENT, "");
			setValue(OVERAGEOVERRIDE, "0");
		}
		else
		{
			//Yes
			//Enable
//			setProperty(OVERAGEMESSAGE, RuntimeFormWidgetInterface.PROP_READONLY, "false");
//			setProperty(OVERAGEHARDERROR, RuntimeFormWidgetInterface.PROP_READONLY, "false");
//			setProperty(OVERAGEOVERRIDE, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			//Set Values
			setValue(OVERAGEMESSAGE, "1");
			
		}
		return RET_CONTINUE;

	}

	/*
	 * ----------------------------------------
	 PERFORMQTYVALIDATION 0
	 OVERAGEHARDERROR 0
	 OVERAGEHARDERRORPERCENT 10.00000
	 OVERAGEMESSAGE 1
	 OVERAGEOVERIDEPERCENT 10.00000
	 OVERAGEOVERRIDE 0
	 ----------------------------------------
	 */
	private boolean isNull(Object attributeValue)
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

	private boolean isEmpty(Object attributeValue)
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
