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
package com.ssaglobal.scm.wms.wm_storer.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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
import com.ssaglobal.scm.wms.wm_cxstorer.ui.CXStorerTab;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;

public class storerSaveValidationAction extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected static ILoggerCategory _log = LoggerFactory
			.getInstance(storerSaveValidationAction.class);

	@Override
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {

		_log.debug("LOG_SYSTEM_OUT", "!@# Start of StorerSaveValidationAction",
				100L);
		String shellFormName = getParameter("SHELLFORMNAME").toString();
		String generalFormTab = getParameter("GENERALFORMTAB").toString();
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		// General
		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add("tab 0");
		RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar,
				shellFormName, generalFormTab, tabIdentifiers, state);
		GeneralTab generalValidation = new GeneralTab(generalForm, state);
		generalValidation.run();

		// SPS Carrier Tab
		DataBean storerFocus = generalForm.getFocus();
		String type = BioAttributeUtil.getString(storerFocus, "TYPE");
		if ("3".equals(type)) {

			String scacCode = BioAttributeUtil.getString(storerFocus,
					"SCAC_CODE");
			int spsEnabled = BioAttributeUtil.getInt(storerFocus,
					"KSHIP_CARRIER");
			String carrier = BioAttributeUtil.getString(storerFocus,
					"STORERKEY");
			if (scacCode != null && !"".equalsIgnoreCase(scacCode)) {
				storerFocus.setValue("SCAC_CODE", scacCode.toUpperCase());
			}

			// RATING PARAMETERS
			tabIdentifiers.clear();
			tabIdentifiers.add("tab 4");
			RuntimeFormInterface spsForm = FormUtil.findForm(shellToolbar,
					shellFormName, "wm_carrier_sps_rating_view",
					tabIdentifiers, state);
			if (!StringUtils.isNull(spsForm)) {
				RatingTab ratingValidation = new RatingTab(spsForm, state,
						storerFocus);
				ratingValidation.run();

			}

			// //CARRIER SPECIAL SERVICES
			// tabIdentifiers.clear();
			// tabIdentifiers.add("tab 5");
			// RuntimeFormInterface serviceToggle =
			// FormUtil.findForm(shellToolbar, "wms_list_shell",
			// "wm_carrier_sps_services_toggle_view", tabIdentifiers, state);
			// if (!isNull(serviceToggle)) {
			// SlotInterface serviceToggleSlot =
			// serviceToggle.getSubSlot("toggle");
			// RuntimeFormInterface serviceDetail =
			// state.getRuntimeForm(serviceToggleSlot,
			// "wm_carrier_sps_services_detail");
			// if (!isNull(serviceDetail) &&
			// !(serviceDetail.getName().equals("Blank")) &&
			// !(serviceDetail.getFocus().isBioCollection())) {
			// ServiceTab serviceValidation = new ServiceTab(serviceDetail,
			// state, storerFocus);
			// serviceValidation.run();
			// } else {
			// _log.debug("LOG_DEBUG_EXTENSION", "!@# SPS Services Is Null",
			// SuggestedCategory.NONE);
			// }
			// }

			if (scacCode != null && !"".equalsIgnoreCase(scacCode)) {
				if (spsEnabled == 0) {// it is not sps carrier
					RuntimeFormInterface form = state.getCurrentRuntimeForm();
					UnitOfWorkBean uow = state.getDefaultUnitOfWork();
					// validate if there is any duplicate for scac code and
					// storerType=3 which is carrier (only for none sps carrier)
					Query qry = null;
					if(storerFocus.isTempBio()){//it is new carrier
						qry = new Query(
								"wm_storer",
								"wm_storer.SCAC_CODE='"
										+ scacCode
										+ "' AND wm_storer.TYPE='3' AND wm_storer.KSHIP_CARRIER=0",
								null);
					}else{//it is update
						qry = new Query(
								"wm_storer",
								"wm_storer.SCAC_CODE='"
										+ scacCode
										+ "' AND wm_storer.TYPE='3' AND wm_storer.KSHIP_CARRIER=0 AND wm_storer.STORERKEY != '"+carrier+"'",
								null);						
					}

					BioCollectionBean bc = uow.getBioCollectionBean(qry);
					if (bc.size() >= 1) {
						// duplicate found
						uow.clearState();
						String[] parameter = new String[1];
						parameter[0] = carrier;
						throw new UserException("SCAC_CODE_DUPLICATE",
								parameter);
					}
				}
			}

		}

		// CX Stuff
		tabIdentifiers.clear();
		tabIdentifiers.add(getParameterString("CXTab", "tab 7"));
		RuntimeFormInterface cxStorerToggleForm = FormUtil.findForm(
				shellToolbar, "wms_list_shell", "wm_cxstorer_toggle_view",
				tabIdentifiers, state);
		if (cxStorerToggleForm != null) {
			_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle"
					+ cxStorerToggleForm.getName(), SuggestedCategory.NONE);
			SlotInterface cxStorerToggleSlot = cxStorerToggleForm
					.getSubSlot("wm_cxstorer_toggle_view");
			RuntimeFormInterface cxStorerDetail = state.getRuntimeForm(
					cxStorerToggleSlot, "DETAIL1");
			if (!isNull(cxStorerDetail)
					&& !(cxStorerDetail.getName().equals("Blank"))
					&& !(cxStorerDetail.getFocus().isBioCollection())) {
				CXStorerTab cxStorerValidation = new CXStorerTab(
						cxStorerDetail, generalForm.getFocus(), state);
				cxStorerValidation.run();
			} else {
				_log.debug("LOG_DEBUG_EXTENSION", "! CX Storer is Null",
						SuggestedCategory.NONE);
			}
		}

		// Customer
		if ("2".equals(type)) {
			tabIdentifiers.clear();
			tabIdentifiers.add("tab 8");
			RuntimeFormInterface serviceToggle = FormUtil.findForm(
					shellToolbar, "wms_list_shell", "wm_spsspecialsvcs_toggle",
					tabIdentifiers, state);
			if (!isNull(serviceToggle)) {
				SlotInterface serviceToggleSlot = serviceToggle
						.getSubSlot("toggle");
				RuntimeFormInterface serviceDetail = state.getRuntimeForm(
						serviceToggleSlot, "sps_special_detail");
				if (!isNull(serviceDetail)
						&& !(serviceDetail.getName().equals("Blank"))
						&& !(serviceDetail.getFocus().isBioCollection())) {
					ServiceTab serviceValidation = new ServiceTab(
							serviceDetail, state, storerFocus);
					serviceValidation.run();
				} else {
					_log.debug("LOG_DEBUG_EXTENSION",
							"!@# SPS Services Is Null", SuggestedCategory.NONE);
				}
			}

		}

		return RET_CONTINUE;

	}

	class RatingTab extends Tab {
		DataBean storerFocus = null;

		public RatingTab(RuntimeFormInterface f, StateInterface st,
				DataBean storerFocus) {
			super(f, st);
			this.storerFocus = storerFocus;
		}

		public void run() {

			if (storerFocus.isTempBio()) {
				// insert parent
				focus.setValue("CARRIER", storerFocus.getValue("STORERKEY"));
			}
			if ("1".equalsIgnoreCase(storerFocus.getValue("KSHIP_CARRIER")
					.toString())) {
				storerFocus.setValue("SCAC_CODE", focus.getValue("SCAC_CODE"));
			}

		}

	}

	class GeneralTab extends Tab {
		String ownerValue;
		String type;

		public GeneralTab(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
			_log.debug("LOG_SYSTEM_OUT", "!@# Start of General Tab Validation "
					+ form.getName(), 100L);
			ownerValue = focus.getValue("STORERKEY").toString();
			type = focus.getValue("TYPE").toString();
		}

		void run() throws DPException, UserException {
			if (isInsert) {
				ownerDuplication();
			}
			alphaNumericValidation("SCAC_CODE");

		}

		private void alphaNumericValidation(String attributeName)
				throws UserException {
			Object attributeValue = focus.getValue(attributeName);
			if (isNull(attributeValue)) {
				return;
			}
			if (!(attributeValue.toString().matches("^[\\d\\w]*$"))) {
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_ALPHANUM", parameters);
			}

		}

		void ownerDuplication() throws DPException, UserException {

			String query = "SELECT * FROM STORER WHERE (STORERKEY = '"
					+ ownerValue + "' AND TYPE='"+type+"')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_DUPLICATE", parameters);
			}
		}

	}

	boolean isNull(Object attributeValue) {

		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}

	}

	boolean isEmpty(Object attributeValue) {

		if (attributeValue == null) {
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}

	}

}
