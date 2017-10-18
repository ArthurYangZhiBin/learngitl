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

package com.ssaglobal.scm.wms.wm_table_validation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.TableValidationCCF;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DuplicateValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DuplicateValidationAction.class);

	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{

		final String BIO = getParameterString("BIO");
		final String ERROR_MESSAGE = getParameterString("ERROR_MESSAGE");
		final String NAME = getParameterString("NAME");
		ArrayList primaryKeyFieldNames = (ArrayList) getParameter("PRIMARY_KEY_FIELDS");

		_log.debug("LOG_SYSTEM_OUT","\n\n\n!@# Start of " + NAME + " DuplicateValidationAction \n\n\n",100L);

		RuntimeFormInterface form = context.getState().getCurrentRuntimeForm();
		DataBean focus = form.getFocus();

		HashMap attributesToValidate = new HashMap();

		// Get keys and values

		for (Iterator it = primaryKeyFieldNames.iterator(); it.hasNext();)
		{
			try
			{
				String keyName = it.next().toString();
				System.out.print("///Key: " + keyName);
				String keyValue = (String) focus.getValue(keyName);
				System.out.print(" Value: " + keyValue + "\n");
				attributesToValidate.put(keyName, keyValue);
			} catch (Exception e)
			{
				_log.debug("LOG_SYSTEM_OUT","Unable to perform check, blank value found",100L);
				return RET_CANCEL;
			}
		}

		// Query Bio to see if K,V exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;

		// Prepare Query Statement
		try
		{
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			queryStatement = prepareQueryStatement(BIO, attributesToValidate);
			_log.debug("LOG_SYSTEM_OUT","!@#! Query: " + queryStatement,100L);
			query = new Query(BIO, queryStatement, null);
			//			 Query Bio
			results = uow.getBioCollectionBean(query);
		} catch (Exception e)
		{

			e.printStackTrace();
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}

		if (results.size() >= 1)
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + NAME + " Duplication Check Failed\n\n",100L);
			String[] parameters = new String[2];
			parameters[0] = NAME;
			throw new FormException(ERROR_MESSAGE, parameters);

		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + NAME + " Duplication Check Passed\n\n",100L);
		}

		return RET_CONTINUE;

	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private String prepareQueryStatement(String bio, HashMap attributesToValidate)
	{
		StringBuffer query = new StringBuffer();

		//process attributesToValidate 
		//format: BIO.KEY1 = 'VALUE' and BIO.KEY2 = 'VALUE' ...
		int count = 0;
		for (Iterator it = attributesToValidate.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry keyValuePair = (Map.Entry) it.next();
			String key = (String) keyValuePair.getKey();
			String value = (String) keyValuePair.getValue();

			if (count >= 1)
			{
				query.append(" AND ");
			}

			query.append(bio + "." + key + " = '" + value + "'");

			count++;

		}
		return query.toString();
	}
}
