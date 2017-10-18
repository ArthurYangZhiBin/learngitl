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

package com.ssaglobal.scm.wms.wm_indirect_activity.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.dbl.DBLQueryString;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.metadata.objects.Perspective;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;


import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_global_preferences.ui.ExportGlobalPreferencesToXLS;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
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

public class LDAPUserIDQueryAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LDAPUserIDQueryAction.class);
	Object firstNameFilter;

	Object lastNameFilter;

	protected Query getQuery(ActionContext context, QBEBioBean qbe) throws DataBeanException
	{
		return useWildcards() ? qbe.getQueryWithWildcards() : qbe.getQuery();
	}

	protected int setQueryResults(ActionContext context, ActionResult result, BioCollectionBean bioCollectionBean)
	{
		Navigation onOneNavigation = (Navigation) getParameter("single item navigation name");
		Perspective onOnePerspective = (Perspective) getParameter("single item perspective");
		if (onOneNavigation != null || onOnePerspective != null)
		{
			int collSize = bioCollectionBean.getSize();
			if (collSize == 1 && doSingleResultNavigation(context, result, bioCollectionBean))
				return 0;
		}
		return doResultNavigation(context, result, bioCollectionBean);
	}

	protected String getType(ActionContext context)
	{
		BioType bioType = (BioType) getParameter("type");
		if (bioType != null)
			return bioType.getName();
		else
			return null;
	}

	boolean containsWildCards(String value)
	{
		if (value.indexOf('*') != -1 || value.indexOf('%') != -1 || value.indexOf('?') != -1
				|| value.indexOf('_') != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	String stripWildCards(String value)
	{
		String result = value;
		//_log.debug("LOG_DEBUG_EXTENSION", "\n\t Before " + value + "\n", SuggestedCategory.NONE);
		result = result.replaceAll("(\\*|%|\\?|_)", "");
		//_log.debug("LOG_DEBUG_EXTENSION", "\n\t After " + result + "\n", SuggestedCategory.NONE);
		return result;

	}

	protected int doResultNavigation(ActionContext context, ActionResult result, BioCollectionBean bioCollectionBean)
	{
		//LDAP search
		//
		boolean search = false;
		try
		{
/*			UserManagementService ums = UMSHelper.getUMS();

			UserStore us = ums.getDefaultUserStore();

			SearchCriteria sc = new SearchCriteria();

			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Before Filter" + "\n", SuggestedCategory.NONE);
			if (firstNameFilter != null && lastNameFilter != null)
			{
				String firstName = firstNameFilter.toString();
				String lastName = lastNameFilter.toString();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "1" + "\n", SuggestedCategory.NONE);

				//filter on both
				if (containsWildCards(firstName))
				{
					firstName = stripWildCards(firstName);
					sc.addCondition(AttributeConstants.FIRSTNAME, firstName,
									com.ssaglobal.cs.ums.api.SearchCondition.LIKE);
				}
				else
				{
					sc.addCondition(AttributeConstants.FIRSTNAME, firstName,
									com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
				}

				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					sc.addCondition(AttributeConstants.LASTNAME, lastName,
									com.ssaglobal.cs.ums.api.SearchCondition.LIKE);
				}
				else
				{
					sc.addCondition(AttributeConstants.FIRSTNAME, lastName,
									com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
				}

				sc.setMatchAll(true);
				search = true;
			}
			else if (firstNameFilter != null)
			{
				String firstName = firstNameFilter.toString();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "2" + "\n", SuggestedCategory.NONE);
				//filter on firstName
				if (containsWildCards(firstName))
				{
					firstName = stripWildCards(firstName);
					sc.addCondition(AttributeConstants.FIRSTNAME, firstName,
									com.ssaglobal.cs.ums.api.SearchCondition.LIKE);
				}
				else
				{
					sc.addCondition(AttributeConstants.FIRSTNAME, firstName,
									com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
				}
				search = true;
			}
			else if (lastNameFilter != null)
			{
				String lastName = lastNameFilter.toString();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "3" + "\n", SuggestedCategory.NONE);
				//filter on lastName
				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					sc.addCondition(AttributeConstants.LASTNAME, lastName,
									com.ssaglobal.cs.ums.api.SearchCondition.LIKE);
				}
				else
				{
					sc.addCondition(AttributeConstants.FIRSTNAME, lastName,
									com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
				}
				search = true;
			}
*/
			
			//mark ma added ***************************************
			String firstName = null;
			String lastName = null;
			if(firstNameFilter != null){
				firstName = firstNameFilter.toString();
			}
			if(lastNameFilter != null){
				lastName = lastNameFilter.toString();
			}

			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			ArrayList<String> userIdsList = umsInterface.getUserIdsForSearch(firstName, lastName); 
			//end ***************************************************
//			if (search == true)
			if(userIdsList != null)
			{
/*				ArrayList userIDs = new ArrayList();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Querying LDAP..." + "\n", SuggestedCategory.NONE);
				Enumeration searchResults = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true,
														-1, -1);
				while (searchResults.hasMoreElements())
				{
					com.ssaglobal.cs.ums.api.User queryResult = (com.ssaglobal.cs.ums.api.User) searchResults.nextElement();
					_log.debug("LOG_DEBUG_EXTENSION", "Results " + queryResult + " " + queryResult.getId() + " "
							+ queryResult.getFirstName() + " " + queryResult.getLastName(), SuggestedCategory.NONE);
					userIDs.add(queryResult.getId());
				}
*/				
				
				//	Build Query
				StringBuffer qryBuilder = new StringBuffer();
				int size = userIdsList.size();
				for (int i = 0; i < size; i++)
				{
					if (i > 0)
					{
						qryBuilder.append(" or ");
					}
					qryBuilder.append(" " + getType(context) + ".USERID = '" + userIdsList.get(i) + "' ");
				}
				_log.debug("LOG_DEBUG_EXTENSION", "  " + qryBuilder.toString(), SuggestedCategory.NONE);
				//Query qry = new Query(getType(context), qryBuilder.toString(), null);
				String queryString;
				if (qryBuilder.toString().matches("\\s*"))
				{
					queryString = getType(context) + ".USERID = ''";
				}
				else
				{
					queryString = qryBuilder.toString();

				}
				Query qry = new Query(getType(context), queryString, null);
				bioCollectionBean.filterInPlace(qry);
			}

			result.setFocus(bioCollectionBean);
			Perspective perspective = (Perspective) getParameter("perspective");
			if (perspective != null)
				result.setPerspective(perspective.getName());
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		return 0;
	}

	protected boolean doSingleResultNavigation(ActionContext context, ActionResult result, BioCollectionBean bioCollectionBean)
	{
		Navigation onOneNavigation = (Navigation) getParameter("single item navigation name");
		Perspective onOnePerspective = (Perspective) getParameter("single item perspective");
		if (onOneNavigation == null && onOnePerspective == null)
			return false;
		DataBean bean = null;
		try
		{
			bean = bioCollectionBean.get("0");
		} catch (EpiDataException ex)
		{
			throw new EpiRuntimeException("EXP_RET_FIRST_BIO_FROM_COLL", "There was an error retrieving the first bio from the bio collection", ex);
		}
		result.setFocus(bean);
		if (onOneNavigation != null)
			context.setNavigation(onOneNavigation.getName());
		else
			result.setPerspective(onOnePerspective.getName());
		return true;
	}

	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "WMSQuery--------------------------------------" + "\n", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		DataBean focus = result.getFocus();
		DataBean modifiedFocus = focus;
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tOriginal " + focus.getValue("FIRSTNAME") + ", " + focus.getValue("LASTNAME") + "\n", SuggestedCategory.NONE);
		firstNameFilter = focus.getValue("FIRSTNAME");
		lastNameFilter = focus.getValue("LASTNAME");
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tModified Before " + modifiedFocus.getValue("FIRSTNAME") + ", "
				+ modifiedFocus.getValue("LASTNAME") + "\n", SuggestedCategory.NONE);
		modifiedFocus.setValue("FIRSTNAME", null);
		modifiedFocus.setValue("LASTNAME", null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\tModified After " + modifiedFocus.getValue("FIRSTNAME") + ", "
				+ modifiedFocus.getValue("LASTNAME") + "\n", SuggestedCategory.NONE);
		boolean qbeFocusPresent = true;
		String declaredType = getType(context);
		if (focus == null)
			qbeFocusPresent = false;
		else if (declaredType != null)
			if (focus.isTempBio())
			{
				String bioType = null;
				bioType = focus.getDataType();
				if (!bioType.equals(declaredType))
					qbeFocusPresent = false;
			}
			else
			{
				qbeFocusPresent = false;
			}
		boolean createNewQBE = getParameterBoolean("create new qbe");
		if (createNewQBE)
			qbeFocusPresent = false;
		if (!qbeFocusPresent)
		{
			if (declaredType == null)
				throw new EpiRuntimeException("EXP_MISSING_TYPE_PARAM_FOR_QUERY_ACTION", "The QueryAction extension should have a bio type associated with it since the current form does not have any QBE bio associated with it");
			focus = createNewQBE(state, declaredType);
		}
		Query query = null;
		try
		{
			query = getQuery(context, (QBEBioBean) modifiedFocus);
		} catch (DataBeanException ex)
		{
			throwUserException(ex, "ERROR_INVALID_QUERY_QACTION", null);
		}
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean bioCollectionBean = null;
		QBEBioBean qbe = (QBEBioBean) focus;
		DataBean listFocus = (DataBean) getTempSpaceHash().get("LIST_FOCUS");
		boolean isFilterSaved = false;
		try
		{
			String isFilterSavedStr = (String) state.getUser().getPreference("remember_last_filter");
			isFilterSaved = isFilterSavedStr.endsWith("true");
		} catch (DataBeanException e)
		{
			_logger.warn("LOG_COULD_NOT_GET_EPNY_LIST_REMEMBER_FILTERPREFERECE",
							"Could not save epnylistfilter preference", e);
		}
		RuntimeForm currForm = (RuntimeForm) state.getCurrentRuntimeForm();
		currForm.setBooleanProperty("showSortImage", false);
		boolean listSortDone = currForm.getBooleanProperty("listSortDone", false);
		if (qbe.getBaseBioCollectionBean() != null)
		{
			bioCollectionBean = qbe.getBaseBioCollectionBean();
			try
			{
				if ((listFocus != null) & (!qbeFocusPresent) || listSortDone)
					qbe.setUseUserPrefFilter(true);
				else
					qbe.setUseUserPrefFilter(false);
				bioCollectionBean.filterInPlace(query);
			} catch (EpiDataException ex)
			{
				throwUserException(ex, "The query {0} could not be executed on this collection",
									new String[] { query.getQueryExpression() });
			}
		}
		else
		{
			bioCollectionBean = uowb.getBioCollectionBean(query);
			if (!qbeFocusPresent || listSortDone)
				qbe.setUseUserPrefFilter(true);
			else
				qbe.setUseUserPrefFilter(false);
		}

		currForm.saveFilterInUserPreference(qbe, null);
		if (listFocus != null && listFocus.isBioCollection())
		{
			((BioCollectionBean) listFocus).copyFrom(bioCollectionBean);
			bioCollectionBean = (BioCollectionBean) listFocus;
		}
		bioCollectionBean.setQBEBioBean((QBEBioBean) focus);
		DBLQueryString objQueryString = (DBLQueryString) getParameter("query_string");
		String strQueryString = null;
		if (objQueryString != null)
			strQueryString = objQueryString.toString(context, result);
		boolean showEmptyList = getParameterBoolean("show empty list");
		if (showEmptyList)
		{
			bioCollectionBean.setEmptyList(true);
			if (qbe.getUseUserPrefFilter())
				bioCollectionBean.setEmptyListIfNoUserPrefFilter();
		}
		else
		{
			bioCollectionBean.setEmptyList(false);
		}
		if ("@PRINTANCESTORS".equalsIgnoreCase(strQueryString))
		{
			_log.debug("LOG_SYSTEM_OUT","------------------------Printing Ancestors------------------------",100L);
			if (currForm == null)
			{
				_log.debug("LOG_SYSTEM_OUT","Couldn't find a current form.  No ancestors.",100L);
				return 1;
			}
			int count = 0;
			do
			{
				if (currForm == null)
					break;
				try
				{
					_log.debug("LOG_SYSTEM_OUT","Ancestor " + count++ + ": ",100L);
					_log.debug("LOG_SYSTEM_OUT","\tForm: " + currForm.getName(),100L);
					if (currForm.getFocus() == null)
					{
						_log.debug("LOG_SYSTEM_OUT","\tNull Focus",100L);
					}
					else
					{
						DataBean db = currForm.getFocus();
						_log.debug("LOG_SYSTEM_OUT","\tFocus: " + currForm.getFocus().getClass().getName(),100L);
						if (db instanceof BioBean)
							_log.debug("LOG_SYSTEM_OUT","\tBioRef: " + ((BioBean) db).getBioRef().getBioRefString(),100L);
						else if (db instanceof BioCollectionBean)
							_log.debug("LOG_SYSTEM_OUT","\tBioCollectionRef: "	+ ((BioCollectionBean) db).getBioCollectionRef().getBioCollectionRefString(),100L);
							//System.out.println("\tBioCollectionRef: "
							//		+ ((BioCollectionBean) db).getBioCollectionRef().getBioCollectionRefString());
						else if (db instanceof QBEBioBean)
						{
							_log.debug("LOG_SYSTEM_OUT","\tBioType: " + ((QBEBioBean) db).getBioType().getName(),100L);
							_log.debug("LOG_SYSTEM_OUT","\t<Do *not* use this ancestor number for your ancestor focus>",100L);
						}
						_log.debug("LOG_SYSTEM_OUT","",100L);
					}
					currForm = (RuntimeForm) currForm.getParentForm(context.getState());
					if (currForm == null && (context instanceof ModalActionContext))
					{
						ModalActionContext mcontext = (ModalActionContext) context;
						try
						{
							currForm = (RuntimeForm) mcontext.getSourceForm();
						} catch (NullPointerException ex)
						{
							currForm = null;
						}
					}
				} catch (EpiException e)
				{
					e.printStackTrace(System.out);
					_log.debug("LOG_SYSTEM_OUT","Sorry, ran into an unexpected exception",100L);
				}
			} while (true);
			_log.debug("LOG_SYSTEM_OUT","------------------------Those are the Ancestors------------------------",100L);
			return 1;
		}
		String orderByClause = (String) getParameter("orderByClause");
		Query manualQuery = null;
		if (bioCollectionBean != null)
		{
			if (strQueryString != null || orderByClause != null)
				manualQuery = new Query(bioCollectionBean.getDataType(), strQueryString, orderByClause);
			else
				manualQuery = qbe.getManualQuery();
			if (manualQuery != null)
				try
				{
					bioCollectionBean.filterInPlace(manualQuery);
					qbe.setManualQuery(manualQuery);
				} catch (EpiDataException ex)
				{
					throwUserException(ex, "The sort order {1} or query string {2} was invalid for the bio {0}",
										new String[] { bioCollectionBean.getDataType(), orderByClause, strQueryString });
				}
		}
		if (!qbeFocusPresent)
			try
			{
				((QBEBioBean) focus).setBaseBioCollectionForQuery(bioCollectionBean);
			} catch (EpiDataException epiEx)
			{
			}
		return setQueryResults(context, result, bioCollectionBean);
	}

	protected boolean useWildcards()
	{
		return getParameterBoolean("use_wildcards", true);
	}

	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		} catch (DataBeanException ex)
		{
			Object args[] = { bioType };
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
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
