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
package com.ssaglobal.scm.wms.formextensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;

public class MakePKFieldsReadOnly extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MakePKFieldsReadOnly.class);
	public MakePKFieldsReadOnly()
	{
	}
	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{	
		ArrayList attributeNames = null;		
		if(!form.getFocus().isTempBio()){
			try
			{				
				// Get Attributes
				attributeNames = (ArrayList) getParameter("ATTRTODISABLE");
				for (Iterator it = attributeNames.iterator(); it.hasNext();)
				{					
					String attributeName = it.next().toString();	
					_log.debug("LOG_EXTENSION_DEBUG_MAKEPKFIELDSREADONLY","Making {0} Read Only",100L,new Object[]{attributeName});					
					// disable widget
					form.getFormWidgetByName(attributeName).setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					
				}
				
			}  catch (Exception e)
			{
				// Handle Exceptions
				e.printStackTrace();
				_log.error("LOG_EXTENSION_ERROR_MAKEPKFIELDSREADONLY","Error Occured While Making Widget Read Only",e);				
				return RET_CANCEL;
			}
		}
		return RET_CONTINUE;
	}
	
}