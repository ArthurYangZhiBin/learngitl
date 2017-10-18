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

package com.ssaglobal.scm.wms.wm_assigned_work.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRow;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AssignedWorkUnassignPicks extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignedWorkUnassignPicks.class);

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

		
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_assigned_work_template", "wm_assigned_work_pick_list_view", state);

		ArrayList items;
		try
		{
			items = listForm.getAllSelectedItems();
			if (isZero(items))
			{
		
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}

		//Query used to refresh list
		BioCollectionBean collection = (BioCollectionBean) listForm.getFocus();
		Query qry = collection.getQuery();
		String qryExpression = qry.getQueryExpression();

		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selected = (BioBean) it.next();

			_log.debug("LOG_DEBUG_EXTENSION", "\n\t PICKDETAILKEY " + selected.getValue("PICKDETAILKEY") + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t USERACTIVITYKEY " + selected.getValue("USERACTIVITYKEY").toString() + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t ASSIGNMENTNUMBER " + selected.getValue("ASSIGNMENTNUMBER").toString() + "\n", SuggestedCategory.NONE);
			String statusValue = selected.getValue("STATUS").toString();
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t STATUS " + statusValue + "\n", SuggestedCategory.NONE);

			if (statusValue.equals("9"))
			{
				requeryList(result, state, listForm, qryExpression);
				throw new UserException("WMEXP_AW_STATUS", new Object[] {});
			}

			//NSPTRANSFERPICKS", {"",ls_useractivitykey, ls_assignmentnumber}
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(""));
			params.add(new TextData(selected.getValue("USERACTIVITYKEY").toString()));
			params.add(new TextData(selected.getValue("ASSIGNMENTNUMBER").toString()));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPTRANSFERPICKS");

			//run stored procedure
			EXEDataObject results = null;
			try
			{
				results = WmsWebuiActionsImpl.doAction(actionProperties);
				//print results
				if (results.getColumnCount() != 0)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
					for (int i = 1; i < results.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						} catch (NullPointerException e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
							return RET_CANCEL;
						}
					}
					
				}

				//remove selected from Query
				String pickDetailKey = (String) selected.getValue("PICKDETAILKEY");

				String pattern = "'" + pickDetailKey + "'";
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + pattern + "\n", SuggestedCategory.NONE);
				qryExpression = qryExpression.replaceFirst(pattern, "'NULL'");

			} catch (WebuiException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\t\t" + e.getMessage(), SuggestedCategory.NONE);
				throw new UserException(e.getMessage(), new Object[] {});
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		//requery list
		requeryList(result, state, listForm, qryExpression);

		return RET_CONTINUE;
	}

	private void requeryList(ActionResult result, StateInterface state, RuntimeListFormInterface listForm, String qryExpression)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "Querying \n\t" + qryExpression + "\n", SuggestedCategory.NONE);

		Query newQry = new Query("wm_pickdetail", qryExpression, "");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean newColl = uow.getBioCollectionBean(newQry);
		//
		listForm.setSelectedItems(null);
		result.setSelectedItems(null);
		result.setFocus((DataBean) newColl);
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
