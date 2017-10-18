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
package com.ssaglobal.scm.wms.wm_load_maintenance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class ValidateLoadMaintenance extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateLoadMaintenance.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException {
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Executing ValidateLoadMaintenance", 100L);
		StateInterface state = context.getState();

		// Get List Form Bugaware #8024
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_list_form", state);
		if (listForm != null) {
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found List Form:" + listForm.getName(), 100L);
		} else {
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found List Form:Null", 100L);
		}

		// Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_detail_form", state);
		if (headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Header Form:" + headerForm.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Header Form:Null", 100L);
		
		ArrayList<String> cTab = new ArrayList<String>();
		cTab.add("tab 1");
		RuntimeFormInterface capacityTab = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_detail_capacity_tab", cTab, state);
		if (capacityTab != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Capacity Form:" + capacityTab.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Capacity Form:Null", 100L);
		
		ArrayList tabs = new ArrayList();

		RuntimeFormInterface stopListForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_stop_list_form", state);
		if (stopListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Stop List Form:" + stopListForm.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Stop List Form:Null", 100L);

		RuntimeFormInterface stopForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_stop_detail_form", state);
		if (stopForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Stop Detail Form:" + stopForm.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Stop Detail Form:Null", 100L);

		tabs.add("tab 1");
		RuntimeFormInterface unitsForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_outbound_units_list_form", tabs, state);
		if (unitsForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Units Form:" + unitsForm.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found Units Form:Null", 100L);

		// validate list form fields
		if (listForm != null) {
			ArrayList<String> doorValidation = new ArrayList<String>();
			BioCollectionBean listBioCollectionBean = (BioCollectionBean) listForm.getFocus();
			for (int i = 0; i < listBioCollectionBean.size(); i++) {
				BioBean listRowBean = listBioCollectionBean.get("" + i);
				// Editable Fields on the List Form
				if (listRowBean.hasBeenUpdated("ROUTE") == true) {
					String route = listRowBean.getValue("ROUTE") == null || listRowBean.getValue("ROUTE").toString().matches("\\s*") ? "" : listRowBean.getValue("ROUTE").toString();
					if (route.length() > 0) {
						route = route.toUpperCase();
						listRowBean.setValue("ROUTE", route);
					}
				}
				if (listRowBean.hasBeenUpdated("DOOR") == true) {
					// validation code from below
					String door = listRowBean.getValue("DOOR") == null || listRowBean.getValue("DOOR").toString().matches("\\s*") ? "" : listRowBean.getValue("DOOR").toString();
					if (door.length() > 0) {
						door = door.toUpperCase();
						try {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Validating DOOR...", 100L);
							Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "'", "");
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
							if (bioCollection == null || bioCollection.size() == 0) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "DOOR " + door + " is not valid...", 100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
								String args[] = new String[1];
								args[0] = door;
								String errorMsg = getTextMessage("WMEXP_INVALID_LOC", args, state.getLocale());
								throw new UserException(errorMsg, new Object[0]);
							}
							loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "' AND (wm_location.LOCATIONTYPE = 'STAGED' or wm_location.LOCATIONTYPE = 'DOOR')", "");
							bioCollection = uow.getBioCollectionBean(loadBiosQry);
							if (bioCollection == null || bioCollection.size() == 0) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "DOOR " + door + " is not valid...", 100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
								String args[] = new String[1];
								args[0] = door;
								String errorMsg = getTextMessage("WMEXP_INVALID_DOOR", args, state.getLocale());
								throw new UserException(errorMsg, new Object[0]);
							}
						} catch (EpiDataException e) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							e.printStackTrace();
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}

						listRowBean.setValue("DOOR", door);
					}

				}
				if (listRowBean.hasBeenUpdated("DEPARTURETIME") == true) {

				}
				if (listRowBean.hasBeenUpdated("CARRIERID") == true) {
					String carrierId = listRowBean.getValue("CARRIERID") == null || listRowBean.getValue("CARRIERID").toString().matches("\\s*") ? "" : listRowBean.getValue("CARRIERID").toString();
					if (carrierId.length() > 0) {
						carrierId = carrierId.toUpperCase();
						validateCarrier(state, carrierId);
						listRowBean.setValue("CARRIERID", carrierId);
					}
				}
				if (listRowBean.hasBeenUpdated("TRAILERID") == true) {
					String trailerId = listRowBean.getValue("TRAILERID") == null || listRowBean.getValue("TRAILERID").toString().matches("\\s*") ? "" : listRowBean.getValue("TRAILERID").toString();
					if (trailerId.length() > 0) {
						trailerId = trailerId.toUpperCase();
						listRowBean.setValue("TRAILERID", trailerId);
					}
				}

				//Door validation
				//validate that for this door, there are not other loads with 1 <= status < 7 and have trailers
				validateDoor(listRowBean, doorValidation);

			}
		}

		// validate header form fields
		if (headerForm != null) {
			boolean shouldValidate = true;
			if (headerForm.getFocus().isTempBio()) {
				QBEBioBean bio = (QBEBioBean) headerForm.getFocus();
				if (bio.isEmpty()) {
					shouldValidate = false;
				}

			} else {
				BioBean bio = (BioBean) headerForm.getFocus();
				if (bio.getUpdatedAttributes() == null || bio.getUpdatedAttributes().size() == 0) {
					shouldValidate = false;
				}
			}
			if (shouldValidate) {
//				Object doorObj = headerForm.getFormWidgetByName("DOOR");
//				Object loadSeqObj = headerForm.getFormWidgetByName("LOADSEQUENCE");
//				Object statusObj = headerForm.getFormWidgetByName("STATUS");
//				Object externalIdObj = headerForm.getFormWidgetByName("EXTERNALID");
				DataBean focus = headerForm.getFocus();
				List updatedAttr = new ArrayList();
				if (!focus.isTempBio())
					updatedAttr = ((BioBean) focus).getUpdatedAttributes();
				// Iterator attrItr = focus.getUpdatedAttributes().iterator();

				// Validate carrier
				validateCarrier(state, BioAttributeUtil.getString(focus, "CARRIERID"));
				validateCustomer(state, BioAttributeUtil.getString(focus, "SHIPTO"));
				String door = BioAttributeUtil.getStringNoNull(focus, "DOOR"); //doorObj == null || ((RuntimeWidget) doorObj).getDisplayValue() == null ? "" : ((RuntimeWidget) doorObj).getDisplayValue();
				String loadSeq = BioAttributeUtil.getStringNoNull(focus, "LOADSEQUENCE"); //loadSeqObj == null || ((RuntimeWidget) loadSeqObj).getValue() == null ? "" : ((RuntimeWidget) loadSeqObj).getValue().toString();
				String status = BioAttributeUtil.getStringNoNull(focus, "STATUS"); //statusObj == null || ((RuntimeWidget) statusObj).getValue() == null ? "" : ((RuntimeWidget) statusObj).getValue().toString();
				// Load Enhancements
				String externalId = BioAttributeUtil.getStringNoNull(focus, "EXTERNALID"); //externalIdObj == null || ((RuntimeWidget) externalIdObj).getValue() == null ? "" : ((RuntimeWidget) externalIdObj).getValue().toString();

//				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "doorObj:" + doorObj, 100L);
//				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "loadSeqObj:" + loadSeqObj, 100L);
//				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "statusObj:" + statusObj, 100L);
//				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "externalIdObj:" + externalIdObj, 100L);

				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "door:" + door, 100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "loadSeq:" + loadSeq, 100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "status:" + status, 100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "externalId:" + externalId, 100L);

				// If ExternalID has been updated, make sure that the previous
				// value was blank
				// else throw an error
				if (updatedAttr.contains("EXTERNALID")) {
					String previousExternalID = (String) focus.getValue("EXTERNALID", true);
					_log.debug("LOG_DEBUG_EXTENSION_ValidateLoadMaintenance_execute", "Previous ExternalID" + previousExternalID, SuggestedCategory.NONE);
					if (!StringUtils.isEmpty(previousExternalID)) {
						throw new UserException("WMEXP_LOAD_EXTERNALID", new Object[] {});
					}

				}

				// Validate Door, if present must be present in LOC table and
				// Type STAGED
				if (door.length() > 0) {
					try {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Validating DOOR...", 100L);
						Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						if (bioCollection == null || bioCollection.size() == 0) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "DOOR " + door + " is not valid...", 100L);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							String args[] = new String[1];
							args[0] = door;
							String errorMsg = getTextMessage("WMEXP_INVALID_LOC", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
						loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "' AND (wm_location.LOCATIONTYPE = 'STAGED' OR wm_location.LOCATIONTYPE = 'DOOR')", "");
						bioCollection = uow.getBioCollectionBean(loadBiosQry);
						if (bioCollection == null || bioCollection.size() == 0) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "DOOR " + door + " is not valid...", 100L);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							String args[] = new String[1];
							args[0] = door;
							String errorMsg = getTextMessage("WMEXP_INVALID_DOOR", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
					} catch (EpiDataException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
						e.printStackTrace();
						String args[] = new String[0];
						String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
				}

				//validate that for this door, there are not other loads with status < 7 and have trailers
				validateDoor(state, focus);

				// Validate LoadSequence, if changed and status was '1' then it
				// cannot change to '2' or '3'
				if (!focus.isTempBio()) {
					if (loadSeq.length() > 0 && (loadSeq.equals("3") || loadSeq.equals("2")) && updatedAttr.contains("LOADSEQUENCE")) {
						try {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Validating LOADSEQUENCE...", 100L);
							Query loadBiosQry = new Query("wm_loadhdr_auto_pk", "wm_loadhdr_auto_pk.LOADID = '" + focus.getValue("LOADID") + "' AND wm_loadhdr_auto_pk.LOADSEQUENCE = '1'", "");
							UnitOfWorkBean uow = context.getState().getTempUnitOfWork();
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
							if (bioCollection != null && bioCollection.size() > 0) {

								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "LOADSEQUENCE was 1 so it cannot be changed to 2 or 3...", 100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
								String args[] = new String[0];
								String errorMsg = getTextMessage("WMEXP_CANNOT_CHANGE_LOADSEQ", args, state.getLocale());
								throw new UserException(errorMsg, new Object[0]);
							}
						} catch (EpiDataException e) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							e.printStackTrace();
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
					}

					// Validate Status, if changed and status was '2' then it
					// cannot change to '1' or '0'
					// also if status was 0 then cannot change to 9
					if (status.length() > 0 && (status.equals("1") || status.equals("0") || status.equals("9")) && updatedAttr.contains("STATUS")) {
						try {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Validating STATUS...", 100L);
							if (status.equals("1") || status.equals("0")) {
								Query loadBiosQry = new Query("wm_loadhdr_auto_pk", "wm_loadhdr_auto_pk.LOADID = '" + focus.getValue("LOADID") + "' AND wm_loadhdr_auto_pk.STATUS = '2'", "");
								UnitOfWorkBean uow = context.getState().getTempUnitOfWork();
								BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
								if (bioCollection != null && bioCollection.size() > 0) {
									focus.setValue("STATUS", "2");
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "STATUS was 2 so it cannot be changed to 1 or 0...", 100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
									String args[] = new String[0];
									String errorMsg = getTextMessage("WMEXP_CANNOT_CHANGE_SATAUS_1", args, state.getLocale());
									throw new UserException(errorMsg, new Object[0]);
								}
							} else {
								Query loadBiosQry = new Query("wm_loadhdr_auto_pk", "wm_loadhdr_auto_pk.LOADID = '" + focus.getValue("LOADID") + "' AND wm_loadhdr_auto_pk.STATUS = '0'", "");
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Executing Query:" + loadBiosQry.getQueryExpression(), 100L);
								UnitOfWorkBean uow = context.getState().getTempUnitOfWork();
								BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
								if (bioCollection != null && bioCollection.size() > 0) {
									focus.setValue("STATUS", "0");
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "STATUS was 0 so it cannot be changed to 9...", 100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
									String args[] = new String[0];
									String errorMsg = getTextMessage("WMEXP_CANNOT_CHANGE_SATAUS_2", args, state.getLocale());
									throw new UserException(errorMsg, new Object[0]);
								}
							}
						} catch (EpiDataException e) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							e.printStackTrace();
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
					}
					
					// when status is 9, set actual ship date
					if(updatedAttr.contains("STATUS") && "9".equals(status)){
						focus.setValue("ACTUALSHIPDATE", new GregorianCalendar(ReportUtil.getTimeZone(state)));
					}
				}
			}
		}
		// Validate Stop List
		if (stopListForm != null) {
			try {
				// Make sure STOPS are unique for this record
				BioCollectionBean stopCollection = (BioCollectionBean) stopListForm.getFocus();
				if (stopCollection != null && stopCollection.size() > 0) {
					HashMap stops = new HashMap();
					for (int i = 0; i < stopCollection.size(); i++) {
						Bio bio = stopCollection.elementAt(i);
						Object stopObj = bio.get("STOP");
						String stop = stopObj == null ? "" : stopObj.toString();
						if (stop != null && stop.length() > 0) {
							if (stops.containsKey(stop)) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "STOP is not Unique...", 100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
								String args[] = new String[0];
								String errorMsg = getTextMessage("WMEXP_STOP_NOT_UNIQUE", args, state.getLocale());
								throw new UserException(errorMsg, new Object[0]);
							} else {
								stops.put(stop, stop);
							}
						} else {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "STOP is required..", 100L);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_STOP_REQUIRED", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
					}
				}
			} catch (EpiDataException e) {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			}
		}

		// Validate Stop Detail
		if (stopForm != null) {
			if (headerForm != null) {
				boolean shouldValidate = true;
				if (stopForm.getFocus().isTempBio()) {
					QBEBioBean bio = (QBEBioBean) stopForm.getFocus();
					if (bio.isEmpty()) {
						shouldValidate = false;
					}

				} else {
					BioBean bio = (BioBean) stopForm.getFocus();
					if (bio.getUpdatedAttributes() == null || bio.getUpdatedAttributes().size() == 0) {
						shouldValidate = false;
					}
				}
				if (shouldValidate) {
					DataBean focus = headerForm.getFocus();
					DataBean stopFocus = stopForm.getFocus();
//					Object stopObj = stopForm.getFormWidgetByName("STOP");
//					Object loadIdObj = headerForm.getFormWidgetByName("LOADID");

					String stop = BioAttributeUtil.getStringNoNull(stopFocus, "STOP"); //stopObj == null || ((RuntimeWidget) stopObj).getValue() == null ? "" : ((RuntimeWidget) stopObj).getValue().toString();
					String loadId = BioAttributeUtil.getStringNoNull(focus, "LOADID"); //loadIdObj == null || ((RuntimeWidget) loadIdObj).getValue() == null ? "" : ((RuntimeWidget) loadIdObj).getValue().toString();

//					_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "stopObj:" + stopObj, 100L);
//					_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "loadIdObj:" + loadIdObj, 100L);

					_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "stop:" + stop, 100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "loadId:" + loadId, 100L);
					try {
						Query loadBiosQry = new Query("wm_loadstop_lm", "wm_loadstop_lm.LOADID = '" + loadId.toUpperCase() + "' AND wm_loadstop_lm.STOP = '" + stop.toUpperCase() + "'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
						BioCollection stopCollection = uow.getBioCollectionBean(loadBiosQry);
						// Make sure STOPS are unique for this record
						if (stopCollection != null && stopCollection.size() > 0) {
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "STOP is not Unique...", 100L);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_STOP_NOT_UNIQUE", args, state.getLocale());
							throw new UserException(errorMsg, new Object[0]);
						}
					} catch (EpiDataException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
						e.printStackTrace();
						String args[] = new String[0];
						String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
				}
			}
		}
		// Validate Units
		if (unitsForm != null) {
			try {
				// Make sure Units Sequences are unique for this record
				BioCollectionBean unitsCollection = (BioCollectionBean) unitsForm.getFocus();
				if (unitsCollection != null && unitsCollection.size() > 0) {
					HashMap unitsSeq = new HashMap();
					for (int i = 0; i < unitsCollection.size(); i++) {
						Bio bio = unitsCollection.elementAt(i);
						Object seqObj = bio.get("SEQUENCE");
						String seq = seqObj == null ? "" : seqObj.toString();
						if (seq != null && seq.length() > 0) {
							if (unitsSeq.containsKey(seq)) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "SEQUENCE is not Unique...", 100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance...", 100L);
								String args[] = new String[0];
								String errorMsg = getTextMessage("WMEXP_SEQ_NOT_UNIQUE", args, state.getLocale());
								throw new UserException(errorMsg, new Object[0]);
							} else {
								unitsSeq.put(seq, seq);
							}
						}
					}
				}
			} catch (EpiDataException e) {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Leaving ValidateLoadMaintenance", 100L);
		return RET_CONTINUE;

	}

	private void validateDoor(BioBean listRowBean, ArrayList<String> doorValidation) throws UserException {
		String statusString = BioAttributeUtil.getString(listRowBean, "STATUS");
		int status = Integer.parseInt(statusString);
		if (status >= 1 && status < 7) {
			String trailer = BioAttributeUtil.getString(listRowBean, "TRAILERID");
			String door = BioAttributeUtil.getString(listRowBean, "DOOR");
			if (!StringUtils.isEmpty(trailer) && !StringUtils.isEmpty(door)) {
				if (doorValidation.contains(door)) {
					_log.error("LOG_ERROR_EXTENSION_ValidateLoadMaintenance_validateDoor", "" + doorValidation, SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION_ValidateLoadMaintenance_validateDoor", "Found another load with the same door and a trailer with 1 <= status < 7", SuggestedCategory.NONE);
					throw new UserException("WMEXP_LOAD_DOOR_INPROGRESS", new Object[] { door });
				} else {
					doorValidation.add(door);
				}
			}
		}
	}

	private void validateDoor(StateInterface state, DataBean loadHdrFocus) throws EpiDataException, UserException {
		String statusString = BioAttributeUtil.getString(loadHdrFocus, "STATUS");
		int status = Integer.parseInt(statusString);

		if (status >= 1 && status < 7) {
			String trailer = BioAttributeUtil.getString(loadHdrFocus, "TRAILERID");
			String door = BioAttributeUtil.getString(loadHdrFocus, "DOOR");
			if (!StringUtils.isEmpty(trailer) && !StringUtils.isEmpty(door)) {
				String loadid = BioAttributeUtil.getString(loadHdrFocus, "LOADID");
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				String query = "wm_loadhdr_auto_pk.LOADID != '" + loadid + "' and wm_loadhdr_auto_pk.DOOR = '" + door
								+ "' and wm_loadhdr_auto_pk.STATUS < '7' and wm_loadhdr_auto_pk.STATUS >= '1' and (NOT(wm_loadhdr_auto_pk.TRAILERID IS NULL) OR wm_loadhdr_auto_pk.TRAILERID = '' OR wm_loadhdr_auto_pk.TRAILERID = ' ')";
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_loadhdr_auto_pk", query, null));
				if (rs.size() >= 1) {
					_log.error("LOG_ERROR_EXTENSION_ValidateLoadMaintenance_validateDoor", "Query " + query, SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION_ValidateLoadMaintenance_validateDoor", "There are more than 1 loads with 1 <= status < 7 assigned to the same door", SuggestedCategory.NONE);
					throw new UserException("WMEXP_LOAD_DOOR_INPROGRESS", new Object[] { door });
				}
			}
		}
	}

	private void validateCarrier(StateInterface state, String carrierId) throws UserException, EpiDataException {
		if (!StringUtils.isEmpty(carrierId)) {
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_storer", "wm_storer.TYPE = '3' and wm_storer.STORERKEY = '" + carrierId + "'", null));
			if (rs.size() == 0) {
				throw new UserException("WMEXP_INVALID_CARRIER", new Object[] { carrierId });
			}
		}

	}

	private void validateCustomer(StateInterface state, String customer) throws UserException, EpiDataException {
		if (!StringUtils.isEmpty(customer)) {
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_storer", "wm_storer.TYPE = '2' and wm_storer.STORERKEY = '" + customer + "'", null));
			if (rs.size() == 0) {
				throw new UserException("WMEXP_CUST_VALIDATION", new Object[] { customer });
			}
		}

	}
}