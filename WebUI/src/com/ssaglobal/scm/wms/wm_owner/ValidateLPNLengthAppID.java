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
package com.ssaglobal.scm.wms.wm_owner;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.*;

import java.util.*;

public class ValidateLPNLengthAppID extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{

	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		RuntimeFormWidgetInterface AppId_widget = form.getFormWidgetByName("APPLICATIONID");
		RuntimeFormWidgetInterface lpnlength_widget = form.getFormWidgetByName("LPNLENGTH");
		RuntimeFormWidgetInterface sscc1stDigit = form.getFormWidgetByName("SSCC1STDIGIT");
		try
		{
			String displayValue = params.get("fieldValue").toString(); //If Value Null, throws NullPointerException

			if (displayValue.equals("1"))
			{
				setProperty(AppId_widget, RuntimeFormWidgetInterface.PROP_READONLY, "true");
				setProperty(lpnlength_widget, RuntimeFormWidgetInterface.PROP_READONLY, "true");
				setProperty(sscc1stDigit, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			}
			else if (displayValue.equals("0"))
			{
				setProperty(AppId_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
				setProperty(lpnlength_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
				setProperty(sscc1stDigit, RuntimeFormWidgetInterface.PROP_READONLY, "true");
			}
			else
			{
				setProperty(AppId_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
				setProperty(lpnlength_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
				setProperty(sscc1stDigit, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			}
		} catch (NullPointerException e)
		{
			//Catches NullPointerException, sets everything to editable
			setProperty(AppId_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			setProperty(lpnlength_widget, RuntimeFormWidgetInterface.PROP_READONLY, "false");
			setProperty(sscc1stDigit, RuntimeFormWidgetInterface.PROP_READONLY, "false");
		}
		return RET_CONTINUE;
	}
}