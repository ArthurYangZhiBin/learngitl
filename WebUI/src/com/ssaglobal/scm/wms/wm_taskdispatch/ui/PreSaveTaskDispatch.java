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
package com.ssaglobal.scm.wms.wm_taskdispatch.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

public class PreSaveTaskDispatch extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveTaskDispatch.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws DPException, UserException {
		StateInterface state = context.getState();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);

		// get header data
		DataBean headerFocus = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null).getFocus();

		// validate TTMSTRATEGY
		if (headerFocus.isTempBio()) {
			String keyVal = ((String) headerFocus.getValue("TTMSTRATEGYKEY"));
			String sql = "select * from STRATEGY where STRATEGYKEY='" + keyVal + "'";
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if (dataObject.getCount() > 0) {
				String[] param = new String[1];
				param[0] = keyVal;
				throw new UserException("WMEXP_TASKKEY_VALIDATION", param);
			}
		}
		String ttmkey = BioAttributeUtil.getString(headerFocus, "TTMSTRATEGYKEY");

		// Task Interleaving Changes
		// Detail

		RuntimeFormInterface toggleSlotForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
																"wms_list_shell",
																"wm_taskdispatchdetail_toggle_slot",
																state);
		// new header, new detail
		if (toggleSlotForm == null) {
			RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
																"wms_list_shell",
																"wm_taskdispatchdetail_detail_view",
																state);
			validateDetailForm(state, ttmkey, detailForm);
		} else {
			SlotInterface toggleSlot = toggleSlotForm.getSubSlot("wm_taskdispatchdetail_toggle_slot");
			if (FormUtil.isListFormVisibleInSlot(toggleSlot, state) == false) {

				ArrayList tabs = new ArrayList();
				tabs.add("Detail");
				RuntimeFormInterface detailForm = FormUtil.findForm(toggleSlotForm,
																	"wms_list_shell",
																	"wm_taskdispatchdetail_detail_view",
																	tabs,
																	state);

				validateDetailForm(state, ttmkey, detailForm);
			}
		}

		return RET_CONTINUE;
	}

	private void validateDetailForm(StateInterface state, String ttmkey, RuntimeFormInterface detailForm) throws UserException {
		DataBean detailFocus = detailForm.getFocus();
		if (detailFocus != null) {

			descrRule(state, detailForm);

			// if PROXIMITYINTERLEAVING checked, STEPNUMBER is required
			proximityCheck(state, ttmkey, detailForm, detailFocus);

		}
	}

	private void proximityCheck(StateInterface state,
								String ttmkey,
								RuntimeFormInterface detailForm,
								DataBean detailFocus) throws UserException {
		int proximityInterleaving = BioAttributeUtil.getInt(detailFocus, "PROXIMITYINTERLEAVING");
		if (proximityInterleaving == 1) {
			// STEPNUMBER Required
			Integer stepNumber = BioAttributeUtil.getInteger(detailFocus, "STEPNUMBER");
			if (stepNumber == null) {
				throw new UserException("WMEXP_REQ", new Object[] { detailForm.getFormWidgetByName("STEPNUMBER").getLabel(	"label",
																															state.getLocale()) });
			}

			// Overlap Rule
			if (overlapRule(state, ttmkey, detailFocus, stepNumber) == false) {
				// error message should contain
				// step number
				// description
				// prox group
				//
				throw new UserException("WMEXP_TD_OVERLAP", new Object[] { BioAttributeUtil.getInt(	detailFocus,
																									"STEPNUMBER"), BioAttributeUtil.getString(	detailFocus,
																																				"DESCR") });
			}

			// Overlap Sanity Check
			// the user can change the step number later on
			if (overlapRule2(state, ttmkey, detailFocus, stepNumber) == false) {
				throw new UserException("WMEXP_TD_OVERLAP", new Object[] { BioAttributeUtil.getInt(	detailFocus,
																									"STEPNUMBER"), BioAttributeUtil.getString(	detailFocus,
																																				"DESCR") });
			}

			// Nice method
			// if the proximity interleaving flag is checked
			// and there are other records with the step number
			// turn on the proximity interleaving flags for those records
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String linenum = BioAttributeUtil.getString(detailFocus, "TTMSTRATEGYLINENUMBER");
			String niceQuery = "wm_taskdispatchdetail.TTMSTRATEGYKEY = '" + ttmkey + "' and wm_taskdispatchdetail.STEPNUMBER = '" + stepNumber + "' and wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER != '" + linenum + "' and wm_taskdispatchdetail.PROXIMITYINTERLEAVING = '0'";
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_taskdispatchdetail", niceQuery, "wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER"));
			try {
				for (int i = 0; i < rs.size(); i++) {
					BioBean ttmrecord = rs.get("" + i);

					ttmrecord.setValue("PROXIMITYINTERLEAVING", new Integer(1));
					if (overlapRule(state, ttmkey, ttmrecord, stepNumber) == false) {
						// something failed, so undo the change
						ttmrecord.setValue("PROXIMITYINTERLEAVING", new Integer(0));
						continue;
					}
					if (overlapRule2(state, ttmkey, ttmrecord, stepNumber) == false) {
						// something failed, so undo the change
						ttmrecord.setValue("PROXIMITYINTERLEAVING", new Integer(0));
						continue;
					}
				}
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// // set prox group empty
			// detailFocus.setValue("PROXIMITYGROUP", "");
		}
	}

	private boolean overlapRule2(StateInterface state, String ttmkey, DataBean detailFocus, int stepNumber) {

		// get step number values values
		SortedMap<String, Integer> pg = getStepNumbers(ttmkey, state);
		// insert the current value
		pg.put(BioAttributeUtil.getString(detailFocus, "TTMSTRATEGYLINENUMBER"), stepNumber);
		// walk the results
		// the user can create a situation where they create an overlap after the fact
		// example:
		// Original Sequence of step numbers - 1,1,2,2,2
		// Modified Sequence of step numbers - 1,1,2,1,2

		ArrayList<Integer> walk = new ArrayList<Integer>();
		for (Entry<String, Integer> record : pg.entrySet()) {
			int currentValue = record.getValue();
			if (walk.contains(currentValue)) {

				int index = walk.lastIndexOf(currentValue);
				if (index != walk.size() - 1) {
					// there is overlap
					_log.error(	"LOG_ERROR_EXTENSION_PreSaveTaskDispatch_proximityCheck",
								"Overlap Sanity Walk Failed",
								SuggestedCategory.NONE);
					return false;
				}
			}
			walk.add(currentValue);
			_log.debug("LOG_DEBUG_EXTENSION_PreSaveTaskDispatch_overlapRule2", walk.toString(), SuggestedCategory.NONE);
		}
		return true;

	}

	private void descrRule(StateInterface state, RuntimeFormInterface detailForm) throws UserException {
		RuntimeFormWidgetInterface widget = detailForm.getFormWidgetByName("DESCR");
		if (widget != null) {
			String descVal = widget.getValue() == null ? "" : (String) widget.getValue();
			if (descVal.indexOf("'") != -1) {
				String args[] = new String[1];
				args[0] = descVal;
				String errorMsg = getTextMessage("WMEXP_NO_QUOTES_ALLOWED", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			}
		}
	}

	private boolean overlapRule(StateInterface state, String ttmkey, DataBean detailFocus, int stepNumber) {
		// make sure the previous record - step num is in "sequence"
		_log.debug("LOG_INFO_EXTENSION_PreSaveTaskDispatch_execute", "----" + ttmkey, SuggestedCategory.NONE);
		// get previous proximity group values
		String limit = BioAttributeUtil.getString(detailFocus, "TTMSTRATEGYLINENUMBER");
		SortedMap<String, Integer> pg = getPreviousStepNumbers(ttmkey, limit, state);
		// need to know the preceding group value
		// need to know previous prox group values
		int precedingStepNumValue = 0;
		Set<Integer> previousStepNumValuesValues = new HashSet<Integer>();
		for (Entry<String, Integer> record : pg.entrySet()) {
			_log.debug(	"LOG_INFO_EXTENSION_PreSaveTaskDispatch_execute",
						record.getKey() + " " + record.getValue(),
						SuggestedCategory.NONE);

			precedingStepNumValue = record.getValue();
			previousStepNumValuesValues.add(precedingStepNumValue);
		}
		// remove preceding value
		previousStepNumValuesValues.remove(precedingStepNumValue);

		// the current proxgroup value must either be the same as the preceding value
		// or
		// if it's not - it can't be one of the previous values
		if ((stepNumber == precedingStepNumValue) || !previousStepNumValuesValues.contains(stepNumber)) {
			// it's good
			_log.info(	"LOG_INFO_EXTENSION_PreSaveTaskDispatch_execute",
						stepNumber + " is valid",
						SuggestedCategory.NONE);
			return true;
		} else {
			// it's bad
			_log.info(	"LOG_INFO_EXTENSION_PreSaveTaskDispatch_execute",
						stepNumber + " is invalid",
						SuggestedCategory.NONE);
			return false;
		}
	}

	private SortedMap<String, Integer> getPreviousStepNumbers(String ttmkey, String limit, StateInterface state) {
		SortedMap<String, Integer> sn = getStepNumbers(ttmkey, state);
		if (sn.isEmpty()) {
			return sn;
		} else {
			// if the pg.firstKey > limit, this is an edge case - there are no records preceding
			if (limit.compareTo(sn.firstKey()) < 0) {
				sn.clear();
				return sn;
			} else {
				return sn.subMap(sn.firstKey(), limit);
			}
		}
	}

	private SortedMap<String, Integer> getStepNumbers(String ttmkey, StateInterface state) {
		SortedMap<String, Integer> sn = new TreeMap<String, Integer>();
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_taskdispatchdetail", "wm_taskdispatchdetail.TTMSTRATEGYKEY = '" + ttmkey + "'", "wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER"));
		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean tdrecord = rs.get("" + i);
				// int interleaving = BioAttributeUtil.getInt(tdrecord, "PROXIMITYINTERLEAVING");
				// if (interleaving == 1) // interleaving on
				// {
				sn.put(tdrecord.getString("TTMSTRATEGYLINENUMBER"), BioAttributeUtil.getInt(tdrecord, "STEPNUMBER"));
				// }
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sn;

	}

}