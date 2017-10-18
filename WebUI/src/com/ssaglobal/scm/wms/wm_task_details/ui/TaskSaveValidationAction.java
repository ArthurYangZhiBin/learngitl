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

package com.ssaglobal.scm.wms.wm_task_details.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TaskSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskSaveValidationAction.class);

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
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskSaveValidationAction", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		//Header Validation
		RuntimeListFormInterface taskHeader = (RuntimeListFormInterface) FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_task_details_list_view", state);
		BioCollectionBean taskListFocus = (BioCollectionBean) taskHeader.getFocus();

		int size = taskListFocus.size();
		for (int i = 0; i < size; i++)
		{
			BioBean taskListItem = taskListFocus.get(String.valueOf(i));
			if (taskListItem.hasBeenUpdated("USERKEY") && !isEmpty(taskListItem.getValue("USERKEY")))
			{
				//validate User
				userValidation("USERKEY", "TASKMANAGERUSER", "USERKEY", taskHeader, taskListItem);
			}
			Object tempStatus = taskListItem.getValue("STATUS");
			
			if (taskListItem.hasBeenUpdated("STATUS") && !isEmpty(tempStatus))
			{
				//check for defaults
				setTaskTypeDefaults(taskListItem);
			}
		}

		//Detail Validation
		RuntimeFormInterface taskDetail = FormUtil.findForm(shellToolbar, "wms_list_shell", "wms_tbgrp_shell", state); 
//		RuntimeFormInterface taskDetail = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_task_details_template", state);
		if (!isNull(taskDetail))
		{
			TaskDetails taskValidation = new TaskDetails(taskDetail, state);
			taskValidation.run();
		}

		return RET_CONTINUE;
	}

	private void setTaskTypeDefaults(BioBean focus)
	{
		Object tempTask = focus.getValue("TASKTYPE");
		Object tempStatus = focus.getValue("STATUS");
		if (isEmpty(tempTask) || isEmpty(tempStatus))
		{
			return;
		}
		String taskTypeValue = tempTask.toString();
		String status = tempStatus.toString();
		//		if tasktype != PK and status != 9, put space for userkeyoverride, logicaltoloc, 
		//toid, fromid, finaltoloc, listkey, holdkey, userkey, statusmsg, reasonkey, toloc

		if (!(taskTypeValue.equalsIgnoreCase("PK")) && !(status.equalsIgnoreCase("9")))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting defaults", SuggestedCategory.NONE);
			//setting defaults to " "
			if (isEmpty(focus.getValue("USERKEYOVERRIDE")))
			{
				focus.setValue("USERKEYOVERRIDE", " ");
			}
			if (isEmpty(focus.getValue("LOGICALTOLOC")))
			{
				focus.setValue("LOGICALTOLOC", " ");
			}
			if (isEmpty(focus.getValue("TOID")))
			{
				focus.setValue("TOID", " ");
			}
			if (isEmpty(focus.getValue("FROMID")))
			{
				focus.setValue("FROMID", " ");
			}
			if (isEmpty(focus.getValue("FINALTOLOC")))
			{
				focus.setValue("FINALTOLOC", " ");
			}
			if (isEmpty(focus.getValue("LISTKEY")))
			{
				focus.setValue("LISTKEY", " ");
			}
			if (isEmpty(focus.getValue("HOLDKEY")))
			{
				focus.setValue("HOLDKEY", " ");
			}
			if (isEmpty(focus.getValue("USERKEY")))
			{
				focus.setValue("USERKEY", " ");
			}
			if (isEmpty(focus.getValue("STATUSMSG")))
			{
				focus.setValue("STATUSMSG", " ");
			}
			if (isEmpty(focus.getValue("REASONKEY")))
			{
				focus.setValue("REASONKEY", " ");
			}
			if (isEmpty(focus.getValue("TOLOC")))
			{
				focus.setValue("TOLOC", " ");
			}
		}
	}

	protected void numericValidation(String attributeName, RuntimeFormInterface f, BioBean focus) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "Performing # Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false))
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName, f);
			parameters[1] = attributeValue.toString();
			throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
		}

	}

	class TaskDetails extends com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation
	{

		RuntimeFormInterface statusSubForm;

		RuntimeFormInterface inventorySubForm;

		RuntimeFormInterface orderSubForm;

		public TaskDetails(RuntimeFormInterface f, StateInterface st)
		{
/*			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%% " + form.getName(), SuggestedCategory.NONE);

			SlotInterface statusToggle = form.getSubSlot("statusFormSlot");
			statusSubForm = state.getRuntimeForm(statusToggle, "wm_task_details_statusDetail_view");
			SlotInterface inventoryToggle = form.getSubSlot("inventoryFormSlot");
			inventorySubForm = state.getRuntimeForm(inventoryToggle, "wm_task_details_inventoryDetail_view");
			SlotInterface orderToggle = form.getSubSlot("orderPickFormSlot");
			orderSubForm = state.getRuntimeForm(orderToggle, "wm_task_details_orderPickDetail_view");
*/
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%% " + form.getName(), SuggestedCategory.NONE);
			ArrayList tabs = new ArrayList();
			tabs.add("tab 0");
			tabs.add("tab 1");
			tabs.add("tab 2");
			//SlotInterface statusToggle = form.getSubSlot("statusFormSlot");
			statusSubForm = FormUtil.findForm(form, "", "wm_task_details_statusDetail_view", tabs, state);
			//SlotInterface inventoryToggle = form.getSubSlot("inventoryFormSlot");
			inventorySubForm = FormUtil.findForm(form, "", "wm_task_details_inventoryDetail_view", tabs, state);
			//SlotInterface orderToggle = form.getSubSlot("orderPickFormSlot");
			orderSubForm = FormUtil.findForm(form, "", "wm_task_details_orderPickDetail_view", tabs, state);
			_log.debug("LOG_SYSTEM_OUT","\n\nstatusSubForm:"+statusSubForm+"\n\n",100L);
			_log.debug("LOG_SYSTEM_OUT","\n\ninventorySubForm:"+inventorySubForm+"\n\n",100L);
			_log.debug("LOG_SYSTEM_OUT","\n\nstatusSubForm:"+statusSubForm+"\n\n",100L);
			
		}

		public void run() throws EpiDataException, UserException
		{
			userValidation("USERKEY", "TASKMANAGERUSER", "USERKEY", statusSubForm, focus);
			validation("ORDERKEY", "ORDERS", "ORDERKEY", orderSubForm);
			ownerValidation("STORERKEY", inventorySubForm);
			validation("SKU", "SKU", "SKU", inventorySubForm);
			itemValidation("SKU", "STORERKEY");
			validation("FROMLOC", "LOC", "LOC", inventorySubForm);
			validation("TOLOC", "LOC", "LOC", inventorySubForm);

			taskTypeValidation();
			setTaskTypeDefaults();

		}

		private void setTaskTypeDefaults()
		{
			Object tempTask = focus.getValue("TASKTYPE");
			Object tempStatus = focus.getValue("STATUS");
			if (isEmpty(tempTask) || isEmpty(tempStatus))
			{
				return;
			}
			String taskTypeValue = tempTask.toString();
			String status = tempStatus.toString();
			//			if tasktype != PK and status != 9, put space for userkeyoverride, logicaltoloc, 
			//toid, fromid, finaltoloc, listkey, holdkey, userkey, statusmsg, reasonkey, toloc
			if (!(taskTypeValue.equalsIgnoreCase("PK")) && !(status.equalsIgnoreCase("9")))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting defaults", SuggestedCategory.NONE);
				//setting defaults to " "
				if (isEmpty(focus.getValue("USERKEYOVERRIDE")))
				{
					focus.setValue("USERKEYOVERRIDE", " ");
				}
				if (isEmpty(focus.getValue("LOGICALTOLOC")))
				{
					focus.setValue("LOGICALTOLOC", " ");
				}
				if (isEmpty(focus.getValue("TOID")))
				{
					focus.setValue("TOID", " ");
				}
				if (isEmpty(focus.getValue("FROMID")))
				{
					focus.setValue("FROMID", " ");
				}
				if (isEmpty(focus.getValue("FINALTOLOC")))
				{
					focus.setValue("FINALTOLOC", " ");
				}
				if (isEmpty(focus.getValue("LISTKEY")))
				{
					focus.setValue("LISTKEY", " ");
				}
				if (isEmpty(focus.getValue("HOLDKEY")))
				{
					focus.setValue("HOLDKEY", " ");
				}
				if (isEmpty(focus.getValue("USERKEY")))
				{
					focus.setValue("USERKEY", " ");
				}
				if (isEmpty(focus.getValue("STATUSMSG")))
				{
					focus.setValue("STATUSMSG", " ");
				}
				if (isEmpty(focus.getValue("REASONKEY")))
				{
					focus.setValue("REASONKEY", " ");
				}
				if (isEmpty(focus.getValue("TOLOC")))
				{
					focus.setValue("TOLOC", " ");
				}
			}

		}

		private void taskTypeValidation() throws EpiDataException, UserException
		{
			Object attributeValue = focus.getValue("TASKTYPE");
			if (isEmpty(attributeValue))
			{
				return;
			}
			String taskTypeValue = attributeValue.toString();
			//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -End to End - Catch Weight related code -Starts
			if (taskTypeValue.equalsIgnoreCase("PP")){
				String enableAdvCatchWeight=null;
				Object storerkeyValue = focus.getValue("STORERKEY");
				Object itemValue = focus.getValue("SKU");
				String storerkey = storerkeyValue.toString();
				String sku = itemValue.toString();
				CalculateAdvCatchWeightsHelper CalculateAdvCatchWeightsHelper1 = new CalculateAdvCatchWeightsHelper();
				enableAdvCatchWeight = CalculateAdvCatchWeightsHelper1.isAdvCatchWeightEnabled(storerkey,sku);
				if(enableAdvCatchWeight.equalsIgnoreCase("1")){

					Object TOGROSSWGT1 	 = focus.getValue("GROSSWGT");
					Object TONETWGT1	 = focus.getValue("NETWGT");
					Object TOTAREWGT1     = focus.getValue("TAREWGT");

					String ToGrossWGT1 = TOGROSSWGT1.toString();
					String ToNetWGT1 	= TONETWGT1.toString();
					String ToTareWGT1 = TOTAREWGT1.toString();



					double ToGROSSWT1 = NumericValidationCCF.parseNumber(ToGrossWGT1);
					double ToNETWT1 = NumericValidationCCF.parseNumber(ToNetWGT1);
					double ToTAREWT1 = NumericValidationCCF.parseNumber(ToTareWGT1);

					if(ToGROSSWT1 == ToNETWT1+ToTAREWT1){

					}else{

						String[] parameters = new String[1];
						throw new UserException("WMEXP_CC_MV_WGTS_EQU", parameters);

					}

				}
			}
			//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -End to End - Catch Weight related code -Ends
			if (taskTypeValue.equalsIgnoreCase("RP"))
			{
				throw new UserException("WMEXP_TASK_RP", new Object[] {});
			}
			else if (taskTypeValue.equalsIgnoreCase("PA"))
			{
				numericValidation("QTY", inventorySubForm);
				Object tempQty = focus.getValue("QTY");
				if (isNull(tempQty))
				{
					return;
				}
				double quantity = NumericValidationCCF.parseNumber(tempQty.toString());
				if (Double.isNaN(quantity))
				{
					String[] parameters = new String[2];
					parameters[0] = retrieveLabel("QTY", inventorySubForm);
					parameters[1] = tempQty.toString();
					throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
				}
				else if (quantity == 0)
				{
					String[] parameters = new String[2];
					parameters[0] = retrieveLabel("QTY", inventorySubForm);
					parameters[1] = tempQty.toString();
					throw new UserException("WMEXP_TASK_PA", parameters);
				}
			}
			else if (taskTypeValue.equalsIgnoreCase("PK"))
			{
				//check required fields
				//orderkey, orderlinenumber, pickmethod, pickdetailkey, caseid
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Checking Required Fields", SuggestedCategory.NONE);
				final String ORDERKEY = "ORDERKEY";
				final String CASEID = "CASEID";
				final String PICKDETAILKEY = "PICKDETAILKEY";
				final String ORDERLINENUMBER = "ORDERLINENUMBER";
				final String PICKMETHOD = "PICKMETHOD";

				Object orderKey = focus.getValue(ORDERKEY);
				Object orderLineNumber = focus.getValue(ORDERLINENUMBER);
				Object pickMethod = focus.getValue(PICKMETHOD);
				Object pickDetailKey = focus.getValue(PICKDETAILKEY);
				Object caseId = focus.getValue(CASEID);

				if (isEmpty(orderKey) || isEmpty(orderLineNumber) || isEmpty(pickMethod) || isEmpty(pickDetailKey)
						|| isEmpty(caseId))
				{
					throw new UserException("WMEXP_TASK_ORDER_REQD", new Object[] {});
				}
			}
			else if (taskTypeValue.equalsIgnoreCase("MV"))	{
				validation("LOT", "LOT", "LOT", inventorySubForm);
				Object lot = focus.getValue("LOT");
				if ( isNull(lot) || isEmpty(lot))	{
					String[] parameters = new String[2];
					parameters[0] = retrieveLabel("LOT", inventorySubForm);;
					throw new UserException("WMEXP_NULL_PRIMARY_FIELD", parameters);
				}
			}

		}

	}

	protected boolean verifySingleAttribute(String attributeName, String table, String tableAttribute, DataBean focus) throws EpiDataException
	{
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue))
		{
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
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

	protected void userValidation(String attributeName, String table, String tableAttribute, RuntimeFormInterface subForm, DataBean focus) throws EpiDataException, UserException
	{
		WmsUmsInterface users = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		String userName = BioAttributeUtil.getString(focus, attributeName);
		if(StringUtils.isEmpty(userName))
		{
			focus.setValue(attributeName, " ");
		}
		else
		{
			userName = userName.trim();
			if (users.isUserIdExist(userName) == false)
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = focus.getValue(attributeName).toString();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
			focus.setValue(attributeName, userName);
		}
		
	}
	
	protected void validation(String attributeName, String table, String tableAttribute, RuntimeFormInterface subForm, DataBean focus) throws EpiDataException, UserException
	{
		if (verifySingleAttribute(attributeName, table, tableAttribute, focus) == false)
		{
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = retrieveLabel(attributeName, subForm);
			parameters[1] = focus.getValue(attributeName).toString();
			throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
		}

	}

	protected String retrieveLabel(String widgetName, RuntimeFormInterface f)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return f.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	private boolean isNull(Object attributeValue)
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
}
