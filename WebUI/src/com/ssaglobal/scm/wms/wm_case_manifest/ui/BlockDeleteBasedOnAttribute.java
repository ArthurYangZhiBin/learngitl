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

package com.ssaglobal.scm.wms.wm_case_manifest.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.objects.SQLAttributeDomain;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class BlockDeleteBasedOnAttribute extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		String attributeName = getParameterString("ATTRIBUTE");
		String value = getParameterString("VALUE");
		String listForm = getParameterString("LISTFORM");
		String errorMessage = getParameterString("ERRORMESSAGE");

		StateInterface state = context.getState();
		RuntimeListFormInterface deleteListForm = (RuntimeListFormInterface) FormUtil.findForm(
																								state.getCurrentRuntimeForm(),
																								"wms_list_shell",
																								listForm, state);

		ArrayList selectedDeletes = deleteListForm.getAllSelectedItems();

		if (isNull(selectedDeletes))
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			//return RET_CANCEL; //nothing to process
		}

		for (Iterator it = selectedDeletes.iterator(); it.hasNext();)
		{
			//get attribute
			BioBean selectedDelete = (BioBean) it.next();
			Object attributeValue = selectedDelete.getValue(attributeName);

			if (!isEmpty(attributeValue))
			{
				//compare it to value
				if (attributeValue.toString().equalsIgnoreCase(value))
				{
					//throw exception
					//block delete
					throw new UserException(errorMessage, new Object[] {});
				}
			}

		}
		return RET_CONTINUE;
	}



	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}
	
	protected String retrieveLabel(String widgetName, RuntimeFormInterface form)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);

		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
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
}
