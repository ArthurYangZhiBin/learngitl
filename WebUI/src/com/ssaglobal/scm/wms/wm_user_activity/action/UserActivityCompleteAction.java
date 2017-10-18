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

package com.ssaglobal.scm.wms.wm_user_activity.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UserActivityCompleteAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserActivityCompleteAction.class);
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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		
		
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();

		String userName = form.getFormWidgetByName("USERNAME").getDisplayValue();
		String password = form.getFormWidgetByName("PASSWORD").getDisplayValue();
		boolean login = false;
		if (ValidateCredentials.validateUser(userName, password) == false) {
			throw new UserException("EXP_LOGIN_FAILED", new Object[] {});
		}
		else {
			login = true;
		}
		form.getFormWidgetByName("PASSWORD").setValue("");


		if (login == true)
		{
			//Check for active record
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			Query activeRecordQuery = new Query("wm_userattendance", "wm_userattendance.USERID = '" + userName + "' and wm_userattendance.STATUS = '1'", null);
			BioCollectionBean timeInResults = tuow.getBioCollectionBean(activeRecordQuery);
			if (timeInResults.size() == 0) {
				throw new UserException("WMEXP_UA_ACTIVE", new Object[] {});
			}
			/*
			 * String activeQuery = "SELECT COUNT(*) FROM USERATTENDANCE WHERE USERID = '" + userName +
			 * "' AND STATUS = '1'"; _log.debug("LOG_DEBUG_EXTENSION", "\n\t" + activeQuery + "\n",
			 * SuggestedCategory.NONE);
			 * 
			 * EXEDataObject activeQueryResults = WmsWebuiValidationSelectImpl.select(activeQuery); int activeRecord =
			 * activeQueryResults.getAttribValue(1).asTextData().getIntegerValue(); if (activeRecord == 0) { //Exception
			 * throw new UserException("WMEXP_UA_ACTIVE", new Object[] {}); }
			 */
			RuntimeFormWidgetInterface assignmentNumberWidget = form.getFormWidgetByName("ASSIGNMENTNUMBER");
			String assignmentNumber = assignmentNumberWidget.getDisplayValue();
			if (!isEmpty(assignmentNumber))
			{
				//Prompts User in Modal then continues in another extension
				return RET_CONTINUE;

				//Confirm -- Break up
				//				if (true)
				//				{
				//					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				//					Array params = new Array();
				//					params.add(new TextData(userID));
				//					params.add(new TextData(assignmentNumber));
				//					actionProperties.setProcedureParameters(params);
				//					actionProperties.setProcedureName("NSPLABORCOMPLETEASSIGNMENT");
				//					//				run stored procedure
				//					EXEDataObject completeResults = null;
				//					try
				//					{
				//						completeResults = WmsWebuiActionsImpl.doAction(actionProperties);
				//					} catch (WebuiException e)
				//					{
				//						throw new UserException(e.getMessage(), new Object[] {});
				//					} catch (Exception e1)
				//					{
				//						// TODO Auto-generated catch block
				//						e1.printStackTrace();
				//					}
				//					displayResults(completeResults);
				//					//assignment completed
				//				}
			}
			else
			{
				//Exception
				throw new UserException("WMEXP_REQFIELD", new Object[] { removeTrailingColon(assignmentNumberWidget.getLabel(
																																"label",
																																context.getState().getLocale())) });
			}
		}
		else
		{
			//Unable to Login
			//clear
			return RET_CANCEL;
		}
		//clear

	}

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	private void displayResults(EXEDataObject results)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + results.getRowCount() + " x " + results.getColumnCount() + "\n", SuggestedCategory.NONE);
		if (results.getColumnCount() != 0)
		{

			_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
				} catch (Exception e)
				{
					_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
				}
			}
			
		}
	}

	protected boolean isEmpty(Object attributeValue)
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
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
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
}
