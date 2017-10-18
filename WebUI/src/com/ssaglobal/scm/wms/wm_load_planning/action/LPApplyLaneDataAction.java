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

package com.ssaglobal.scm.wms.wm_load_planning.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_load_planning.action.LPSaveValidation;
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

public class LPApplyLaneDataAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPApplyLaneDataAction.class);

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

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
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
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of LPApplyLaneDataAction", SuggestedCategory.NONE);
		;
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbarForm = context.getSourceForm();
		//_log.debug("LOG_DEBUG_EXTENSION", "Toolbar Form " + toolbarForm.getName(), SuggestedCategory.NONE);
		;
		RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		//_log.debug("LOG_DEBUG_EXTENSION", "Shell Form " + shellForm.getName(), SuggestedCategory.NONE);
		;
		SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		//_log.debug("LOG_DEBUG_EXTENSION", "Header Form " + headerForm.getName(), SuggestedCategory.NONE);
		;

		RuntimeListForm headerListForm = null;
		if (headerForm.isListForm())
		{
			headerListForm = (RuntimeListForm) headerForm;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Not a list form", SuggestedCategory.NONE);
			;
			return RET_CANCEL;
		}

		DataBean headerListFocus = headerListForm.getFocus();

		BioCollectionBean headerListFormCollection = null;
		if (headerListFocus.isBioCollection())
		{
			headerListFormCollection = (BioCollectionBean) headerListFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + headerListFormCollection.size(), SuggestedCategory.NONE);
			;
		}
		try
		{

			ArrayList items = headerListForm.getAllSelectedItems();
			//iterate items
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedLoad = (BioBean) it.next();
				String loadPlanningKey = selectedLoad.getValue("LOADPLANNINGKEY").toString();
				_log.debug("LOG_DEBUG_EXTENSION", "! Selected Item - OrderKey: " + selectedLoad.getValue("ORDERKEY")
						+ " +++ LoadPlanningKey" + loadPlanningKey, SuggestedCategory.NONE);

				//prepare stored procedure
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(loadPlanningKey));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPLPLANE");

				//run stored procedure
				EXEDataObject results = null;
				try
				{
					results = WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//handle results
				if (results.getColumnCount() != 0)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
					;
					for (int i = 1; i < results.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "
									+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						} catch (Exception e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
							;
							return RET_CANCEL;
						}
					}
					String laneKey = results.getAttribValue(1).getAsString();
					String remarks = results.getAttribValue(2).getAsString();
					_log.debug("LOG_DEBUG_EXTENSION", "LaneKey " + laneKey, SuggestedCategory.NONE);
					;
					_log.debug("LOG_DEBUG_EXTENSION", "Remarks " + remarks, SuggestedCategory.NONE);
					;
					if (!(laneKey.equalsIgnoreCase(loadPlanningKey)))
					{
						selectedLoad.setValue("OUTBOUNDLANE", laneKey);
						selectedLoad.setValue("REMARKS", remarks);
					}
					else
					{
						selectedLoad.setValue("REMARKS", remarks);
					}
					_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
					;

				}
				//Save Validation
				LPSaveValidation.saveValidation(headerListForm, selectedLoad);
			}
			//Save Results
			uow.saveUOW();
			uow.clearState();
			result.setFocus(headerListFocus);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/*
	 IF NOT( ls_result[1] = String(lds_source.object.loadplanningkey[ll_row])) THEN
	 lds_source.setitem(ll_row,"outboundlane", ls_result[1])
	 lds_source.setitem(ll_row,"remarks", ls_result[2])
	 ELSE 
	 lds_source.setitem(ll_row,"remarks", ls_result[2])
	 END IF


	 */

	//	protected void saveValidation(RuntimeListForm headerListForm, BioBean selectedLoad)
	//			throws EpiDataException, DPException, UserException
	//	{
	//
	//		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
	//		MetaDataAccess mda = MetaDataAccess.getInstance();
	//		LocaleInterface locale = mda.getLocale(userLocale);
	//		//Validate OutboundLane
	//		Object outboundLane = selectedLoad.getValue("OUTBOUNDLANE");
	//		if (!(isEmpty(outboundLane)) && !(outboundLane.toString().equals("N/A")))
	//		{
	//			String query = "SELECT * FROM " + "LANE" + " WHERE " + "LANEKEY" + " = '" + outboundLane.toString() + "'";
	//			EXEDataObject sqlResults = WmsWebuiValidationSelectImpl.select(query);
	//			if (sqlResults.getRowCount() == 0)
	//			{
	//				//Invalid Value, throw error
	//				String[] parameters = new String[2];
	//				parameters[0] = outboundLane.toString();
	//				parameters[1] = headerListForm.getFormWidgetByName("OUTBOUNDLANE").getLabel("label", locale);
	//				_log.debug("LOG_DEBUG_EXTENSION", "!@ " + parameters[0] + " -- " + parameters[1], SuggestedCategory.NONE);;
	//				throw new UserException("WMEXP_INVALID_VALUE", "WMEXP_INVALID_VALUE", parameters);
	//			}
	//		}
	//		else
	//		{
	//			//set to space
	//			selectedLoad.setValue("OUTBOUNDLANE", " ");
	//
	//		}
	//		//Check Route, Stop, OutboundLane, ExternalLoadId for Blanks
	//		Object route = selectedLoad.getValue("ROUTE");
	//		Object stop = selectedLoad.getValue("STOP");
	//		Object externalLoadId = selectedLoad.getValue("EXTERNALLOADID");
	//		if ((isEmpty(route)))
	//		{
	//			selectedLoad.setValue("ROUTE", " ");
	//		}
	//		if ((isEmpty(stop)))
	//		{
	//			selectedLoad.setValue("STOP", " ");
	//		}
	//		if ((isEmpty(externalLoadId)))
	//		{
	//			selectedLoad.setValue("EXTERNALLOADID", " ");
	//		}
	//
	//	}
	//
	//	private boolean isNull(Object attributeValue)
	//			throws EpiDataException
	//	{
	//
	//		if (attributeValue == null)
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//
	//	}
	//
	//	private boolean isEmpty(Object attributeValue)
	//			throws EpiDataException
	//	{
	//
	//		if (attributeValue == null)
	//		{
	//			return true;
	//		}
	//		else if (attributeValue.toString().matches("\\s*"))
	//		{
	//			return true;
	//		}
	//		else
	//		{
	//			return false;
	//		}
	//
	//	}
}
