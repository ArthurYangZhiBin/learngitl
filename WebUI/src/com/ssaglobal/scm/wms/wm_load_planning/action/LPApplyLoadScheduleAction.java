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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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

public class LPApplyLoadScheduleAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPApplyLoadScheduleAction.class);

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
	protected int execute(ModalActionContext context, ActionResult result)
			throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of LPApplyLoadScheduleAction", SuggestedCategory.NONE);
		try
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			
			RuntimeFormInterface toolbarForm = context.getSourceForm();
			
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			
			SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			

			RuntimeListForm headerListForm = null;
			if (headerForm.isListForm())
			{
				headerListForm = (RuntimeListForm) headerForm;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Not a list form", SuggestedCategory.NONE);
				return RET_CANCEL;
			}

			DataBean headerListFocus = headerListForm.getFocus();

			BioCollectionBean headerListFormCollection = null;
			if (headerListFocus.isBioCollection())
			{
				headerListFormCollection = (BioCollectionBean) headerListFocus;
				_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + headerListFormCollection.size(), SuggestedCategory.NONE);
			}

			//Schedule Type
			Object scheduleType = modalForm.getFormWidgetByName("SCHEDTYPE").getValue();
			if (isEmpty(scheduleType))
			{
				return RET_CANCEL;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@ Schedule Type " + scheduleType.toString(), SuggestedCategory.NONE);
			}

			ArrayList items = headerListForm.getAllSelectedItems();
			//iterate items
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedLoad = (BioBean) it.next();
				String loadPlanningKey = selectedLoad.getValue("LOADPLANNINGKEY").toString();
				Object outboundLane = selectedLoad.getValue("OUTBOUNDLANE");

				_log.debug("LOG_DEBUG_EXTENSION", "! Selected Item - OrderKey: " + selectedLoad.getValue("ORDERKEY") + " +++ LoadPlanningKey" + loadPlanningKey, SuggestedCategory.NONE);

				//Prepare Stored Procedure
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(loadPlanningKey));
				params.add(new TextData(scheduleType.toString()));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPLPLOADSCHED");

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
					for (int i = 1; i <= results.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " "+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						} catch (Exception e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getMessage(), SuggestedCategory.NONE);
							return RET_CANCEL;
						}
					}

					_log.debug("LOG_DEBUG_EXTENSION", "&^% 1", SuggestedCategory.NONE);
					if (!(results.getAttribValue(3).getAsString().equalsIgnoreCase("Not Updated")))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "&^% 2", SuggestedCategory.NONE);
						if (!(results.getAttribValue(7).getAsString().equalsIgnoreCase("Load Schedule Not Applied")))
						{
							_log.debug("LOG_DEBUG_EXTENSION", "&^% 3", SuggestedCategory.NONE);
							String deliveryTime = results.getAttribValue(3).getAsString();
							_log.debug("LOG_DEBUG_EXTENSION", "Delivery Time - " + deliveryTime, SuggestedCategory.NONE);
							//15-Aug-2006 16:58:53
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
							Date formattedDate = formatter.parse(deliveryTime);
							_log.debug("LOG_DEBUG_EXTENSION", "Formatted " + formattedDate.toString(), SuggestedCategory.NONE);
							long epoch = formattedDate.getTime();
							_log.debug("LOG_DEBUG_EXTENSION", "Unix " + epoch, SuggestedCategory.NONE);
							Calendar unixCal = Calendar.getInstance();
							unixCal.setTimeInMillis(epoch);
							_log.debug("LOG_DEBUG_EXTENSION", "Calendar " + unixCal, SuggestedCategory.NONE);
							selectedLoad.setValue("DELIVERYTIME", unixCal);
							selectedLoad.setValue("DELIVERYDATE", unixCal);

							selectedLoad.setValue("ROUTE", results.getAttribValue(4).getAsString());
							if (isEmpty(outboundLane))
							{
								_log.debug("LOG_DEBUG_EXTENSION", "&^% 4", SuggestedCategory.NONE);
								selectedLoad.setValue("OUTBOUNDLANE", results.getAttribValue(5).getAsString());
							}
							selectedLoad.setValue("STOP", results.getAttribValue(6).getAsString());
							selectedLoad.setValue("REMARKS", results.getAttribValue(7).getAsString());
						}
						else
						{
							_log.debug("LOG_DEBUG_EXTENSION", "&^% 5", SuggestedCategory.NONE);
							selectedLoad.setValue("REMARKS", results.getAttribValue(7).getAsString());
						}
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION", "&^% 6", SuggestedCategory.NONE);
						_log.debug("LOG_DEBUG_EXTENSION", "REMARKS : " + results.getAttribValue(3).getAsString(), SuggestedCategory.NONE);
						selectedLoad.setValue("REMARKS", results.getAttribValue(3).getAsString());
					}

					_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
				}

				//Save Validation
				LPSaveValidation.saveValidation(headerListForm, selectedLoad);
				//throw new UserExcpetion("WMEXP_PICK_QTY", "WMEXP_PICK_QTY", new Object[] {});
				//throw new EpiException("WMEXP_PICK_QTY", "WMEXP_PICK_QTY", new Object[] {});

			}
			//Saving Results
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Saving Changes", SuggestedCategory.NONE);
			uow.saveUOW();
			uow.clearState();

			result.setFocus(headerListFormCollection);


		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException
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

	private boolean isNull(Object attributeValue) throws EpiDataException
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

//
//IF NOT(ls_result[3]= "Not Updated") THEN
// IF NOT(ls_result[7] = "Load Schedule Not Applied") THEN
//  ls_deliverytime = ls_result[3]
//  ll_rtn = lds_source.setitem(ll_row,"deliverytime", datetime(lnv_conversion.of_date(ls_result[3]),lnv_conversion.of_time(ls_result[3])))
//  lds_source.setitem(ll_row,"route", ls_result[4])
//  If ISNULL(ls_outboundlane) or ls_outboundlane = '' or ls_outboundlane = ' ' THEN
//   lds_source.setitem(ll_row,"outboundlane", ls_result[5])
//  END IF
//  lds_source.setitem(ll_row,"stop", ls_result[6])
//  lds_source.setitem(ll_row,"remarks", ls_result[7])
// ELSE
//  lds_source.setitem(ll_row,"remarks", ls_result[7])
// END IF
//ELSE
// lds_source.setitem(ll_row,"remarks", ls_result[3])
//END IF	
