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

package com.ssaglobal.scm.wms.wm_unassigned_work.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.User;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class UnassignedWorkMakeAssignments extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	
	String ldapUserId;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnassignedWorkMakeAssignments.class);

	String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);

	MetaDataAccess mda = MetaDataAccess.getInstance();

	LocaleInterface locale = mda.getLocale(userLocale);

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
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_unassigned_work_template", "wm_unassigned_work_list_view", state);
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		String userId = (String) userCtx.get("logInUserId");

		ArrayList<String> cleanup = new ArrayList<String>();
		ArrayList items = null;
		context.setNavigation("menuClickEvent241");
		try
		{
			items = listForm.getAllSelectedItems();
			if (isZero(items))
			{
				context.setNavigation("menuClickEvent565");
				return RET_CONTINUE;
				//throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			context.setNavigation("menuClickEvent565");
			return RET_CONTINUE;
			//throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}

		String userLabel = listForm.getFormWidgetByName("USERID").getLabel("label", locale);
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selected = (BioBean) it.next();
			String assignmentNumber = selected.getString("ASSIGNMENTNUMBER");
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + selected.getValue("TEMPUSERID") + "\n", SuggestedCategory.NONE);
			//validate useridkey
			requiredField("TEMPUSERID", selected, userLabel);
			userIdValidation(userLabel, selected, state);
			
			//Need to change the userid to what was returned by LDAP to prevent case issues
			selected.setValue("TEMPUSERID", ldapUserId);
			
			//"NSPUNASSIGNEDWORK", {ls_assignment,ls_userid}
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(selected.getValue("ASSIGNMENTNUMBER").toString()));
			params.add(new TextData(selected.getValue("TEMPUSERID").toString()));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPUNASSIGNEDWORK");

			//run stored procedure
			EXEDataObject results = null;
			try
			{
				results = WmsWebuiActionsImpl.doAction(actionProperties);
				//handle results
				if (results.getColumnCount() != 0)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
					for (int i = 1; i < results.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "
									+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						} catch (NullPointerException e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
							return RET_CANCEL;
						}
					}
					_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
				}
			} catch (WebuiException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\t\t" + e.getMessage(), SuggestedCategory.NONE);
				throw new UserException(e.getMessage(), new Object[] {});
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			cleanup.add(assignmentNumber);


//			selected.delete(); //remove row from the temp table - thus removing it from the list
			listForm.setSelectedItems(null);
			result.setSelectedItems(null);
			state.getDefaultUnitOfWork().clearState();
			
//			saveChanges(result, state, listForm);
		}

		// Cleanup
		for (String assignmentNumber : cleanup) {

			String deleteSql = "DELETE FROM UNASSIGNWORK WHERE ASSIGNMENTNUMBER = '" + assignmentNumber + "' and USERID='" + userId + "'";
			_log.debug("LOG_DEBUG_EXTENSION_UnassignedWorkMakeAssignments_execute", deleteSql, SuggestedCategory.NONE);
			WmsWebuiValidationDeleteImpl.delete(deleteSql);
		}

		//		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//		uow.saveUOW(false);
		return RET_CONTINUE;
	}
	
	
/*
	private void saveChanges(ActionResult result, StateInterface state, RuntimeListFormInterface listForm) throws EpiException, UserException, UnitOfWorkException
	{
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		try
		{
			uow.saveUOW(false);
			listForm.setSelectedItems(null);
			result.setSelectedItems(null);

		} catch (UnitOfWorkException ex)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "IN UnitOfWorkException" + "\n", SuggestedCategory.NONE);

			Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
			_log.debug("LOG_DEBUG_EXTENSION", "\tNested " + nested.getClass().getName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\tMessage " + nested.getMessage(), SuggestedCategory.NONE);

			if (nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throw new UserException(reasonCode, new Object[] {});
			}
			throw new UnitOfWorkException(ex);

		}
	}
*/
	void requiredField(String attribute, BioBean focus, String label) throws UserException
	{
		Object attributeValue = focus.getValue(attribute);
		_log.debug("LOG_DEBUG_EXTENSION", "" + attributeValue, SuggestedCategory.NONE);
		if (isEmpty(attributeValue))
		{
			//throw exception
			String[] parameters = new String[1];
			parameters[0] = label;
			throw new UserException("WMEXP_REQFIELD", parameters);
		}
	}

	private void userIdValidation(String userLabel, BioBean selected, StateInterface state) throws EpiDataException, UserException
	{
		Object userValue = selected.getValue("TEMPUSERID");
		//if (verifySingleAttribute(userValue, "wm_lp_usr", "usr_login", state) == false)
		if (verifyUserId(userValue) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = userLabel;
			parameters[1] = userValue.toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected boolean verifyUserId(Object userValue)
	{
		boolean found = false;
		if (isEmpty(userValue))
		{
			return true;
		}
		try
		{
/*			UserManagementService ums = UMSHelper.getUMS();
			UserStore us = ums.getDefaultUserStore();
			SearchCriteria sc = new SearchCriteria();
			sc.addCondition(AttributeConstants.ID, (String) userValue, com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Searching LDAP for " + userValue + "\n", SuggestedCategory.NONE);
			Enumeration searchResults = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1, -1);

			while (searchResults.hasMoreElements())
			{
				com.ssaglobal.cs.ums.api.User queryResult = (com.ssaglobal.cs.ums.api.User) searchResults.nextElement();
				_log.debug("LOG_DEBUG_EXTENSION", "Found " + queryResult.getId() + "", SuggestedCategory.NONE);
				ldapUserId = queryResult.getId();
				found = true;
			}
*/
			
			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			User user = umsInterface.getUser((String) userValue);
			if(user != null){
				ldapUserId = user.getUid();
				found = true;
			}
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return found;
	}

	protected boolean verifySingleAttribute(Object attributeValue, String bio, String bioAttribute, StateInterface state) throws EpiDataException
	{
		if (isEmpty(attributeValue))
		{
			return true; //Do Nothing
		}
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String queryStatement = bio + "." + bioAttribute + " = '" + attributeValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "!@| Query " + queryStatement, SuggestedCategory.NONE);
		Query query = new Query(bio, queryStatement, null);
		BioCollectionBean results = uow.getBioCollectionBean(query);
		//		String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue.toString() + "'";
		//		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		//		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.size() == 1)
		{
			//value exists, verified
			return true;
		}
		else
		{
			//value does not exist
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

	boolean isNull(Object attributeValue)
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

	boolean isZero(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (((ArrayList) attributeValue).size() == 0)
		{
			return false;
		}
		else
		{
			return false;
		}
	}

}
