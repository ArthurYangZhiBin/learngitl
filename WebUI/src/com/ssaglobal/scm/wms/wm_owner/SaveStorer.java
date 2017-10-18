/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_owner;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// TODO: Auto-generated Javadoc

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>.
 *
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SaveStorer extends SaveAction
{
	
	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveStorer.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 *
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws UserException the user exception
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		//		try
		{
			StateInterface state = context.getState();
			if (facilityIsEnterprise(state))
			{
				_log.debug("LOG_SYSTEM_OUT","=-=-= In Enterprise, Saving",100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","=-=-= In Warehouse, Saving",100L);
			}

			//Get Focii
			SlotInterface tabGroupSlot = getTabGroupSlot(state);
			DataBean parentFocus = getParentFocus(state, tabGroupSlot);
			DataBean ownerBillingFocus = getOwnerBillingFocus(state, tabGroupSlot);
			DataBean ownerLabelFocus = null;
			DataBean udfLabelFocus = null;
			DataBean cxStorerFocus = null;
			
			//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Starts
			DataBean OwnerBillingChargeCodesFocus = null;
			//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Ends

			if (!facilityIsEnterprise(state))
			{
				ownerLabelFocus = getOwnerLabelFocus(state, tabGroupSlot);
				udfLabelFocus = getUDFLabelFocus(state, tabGroupSlot);
				cxStorerFocus = getCxStorerFocus(state, tabGroupSlot);
				//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Starts
				OwnerBillingChargeCodesFocus = getOwnerBillingChargeCodesFocus(state, tabGroupSlot);
				//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Ends
			}
			
			DataBean spsSpecialSvcsFocus = null;
			spsSpecialSvcsFocus = getSPSSpecialSvcsFocus(state, tabGroupSlot);

			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioBean parentBioBean = null;
			BioBean OwnerBillingBean = null;
			QBEBioBean OwnerLabelQBEBioBean = null;
			QBEBioBean UDFLabelQBEBioBean = null;
			QBEBioBean cxStorerQBEBioBean = null;
			//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Starts
			QBEBioBean OwnerBillingChargeCodesQBEBioBean = null;
			//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Ends
			
			QBEBioBean specialServicesQBEBioBean = null;
			
			if (parentFocus.isTempBio())
			{
				//Inserting New Parent
				_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting New Parent",100L);
				try
				{
					parentBioBean = uow.getNewBio((QBEBioBean) parentFocus);
				} catch (EpiException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
				try
				{
					if(ownerBillingFocus != null) {
						OwnerBillingBean = uow.getNewBio((QBEBioBean) ownerBillingFocus);
						OwnerBillingBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					}
				} catch (EpiException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
				

				//				parentBioBean.addBioCollectionLink("STORERBILLING", (QBEBioBean) OwnerBillingFocus);

				if ((ownerLabelFocus != null))
				{
					OwnerLabelQBEBioBean = (QBEBioBean) ownerLabelFocus;
					OwnerLabelQBEBioBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					parentBioBean.addBioCollectionLink("STORER_LABELS", OwnerLabelQBEBioBean);

				}
				
				//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing -Starts
				if ((OwnerBillingChargeCodesFocus != null))
				{
					OwnerBillingChargeCodesQBEBioBean = (QBEBioBean) OwnerBillingChargeCodesFocus;
					OwnerBillingChargeCodesQBEBioBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					parentBioBean.addBioCollectionLink("STORERCHARGECODES", OwnerBillingChargeCodesQBEBioBean);

				}
				//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing -Ends

				if ((udfLabelFocus != null))
				{
					UDFLabelQBEBioBean = (QBEBioBean) ownerLabelFocus;
					UDFLabelQBEBioBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					parentBioBean.addBioCollectionLink("STORER_UDF", UDFLabelQBEBioBean);
				}

				if((cxStorerFocus != null))
				{
					cxStorerQBEBioBean = (QBEBioBean) cxStorerFocus;
					cxStorerQBEBioBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					cxStorerQBEBioBean.set("TYPE", parentBioBean.getValue("TYPE"));
					parentBioBean.addBioCollectionLink("STORERCONTAINEREXCHANGE", cxStorerQBEBioBean);
				}
				
				if ((spsSpecialSvcsFocus != null))
				{
					specialServicesQBEBioBean = (QBEBioBean) spsSpecialSvcsFocus;
					specialServicesQBEBioBean.set("STORERKEY", parentBioBean.getValue("STORERKEY"));
					specialServicesQBEBioBean.set("TYPE", parentBioBean.getValue("TYPE"));
					parentBioBean.addBioCollectionLink("SPSSPECIALSVCS", specialServicesQBEBioBean);

				}
			}
			else
			{
				//Updating Parent
				_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating Parent",100L);
				parentBioBean = (BioBean) parentFocus;

				if ((ownerBillingFocus != null))
				{
					if (ownerBillingFocus.isTempBio())
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting Billing",100L);
						parentBioBean.addBioCollectionLink("STORERBILLING", (QBEBioBean) ownerBillingFocus);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating Billing",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n@@@@Skipping Billing",100L);
				}

				if ((ownerLabelFocus != null))
				{
					if (ownerLabelFocus.isTempBio())
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting Owner Label",100L);
						parentBioBean.addBioCollectionLink("STORER_LABELS", (QBEBioBean) ownerLabelFocus);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating Owner Label",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n@@@@Skipping Owner Label",100L);
				}

				if ((udfLabelFocus != null))
				{
					if (udfLabelFocus.isTempBio())
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting UDF Label",100L);
						parentBioBean.addBioCollectionLink("STORER_UDF", (QBEBioBean) udfLabelFocus);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating UDF Label",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n@@@@Skipping UDF Label",100L);
				}

				if((cxStorerFocus != null))
				{
					if (cxStorerFocus.isTempBio())
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting Pallet Exchange",100L);
						parentBioBean.addBioCollectionLink("STORERCONTAINEREXCHANGE", (QBEBioBean) cxStorerFocus);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating Pallet Exchange",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n@@@@Skipping Pallet Exchange",100L);
				}
				
				if((spsSpecialSvcsFocus != null))
				{
					if (spsSpecialSvcsFocus.isTempBio())
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Inserting Special Services",100L);
						parentBioBean.addBioCollectionLink("SPSSPECIALSVCS", (QBEBioBean) spsSpecialSvcsFocus);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","\n@@@@Updating Special Services",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","\n@@@@Skipping Special Services",100L);
				}
			}
			_log.debug("LOG_SYSTEM_OUT","\n!\n!\n&&&& Saving UOW\n!\n!\n",100L);
			try
			{
				uow.saveUOW(true);
			} catch (EpiException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			_log.debug("LOG_SYSTEM_OUT","\n!\n!\n&&&& Clearing State\n!\n!\n",100L);
			uow.clearState();
			_log.debug("LOG_SYSTEM_OUT","\n!\n!\n&&&& Setting Focus\n!\n!\n",100L);
			result.setFocus(parentBioBean);

		}// catch (RuntimeException e)
		//		{
		//			// Handle Exceptions
		//			e.printStackTrace();
		//			return RET_CANCEL;
		//		}

		return RET_CONTINUE;
	}

	private DataBean getSPSSpecialSvcsFocus(StateInterface state,
			SlotInterface tabGroupSlot) {
		try {
			if ("skip".equals(getParameterString("SPSServices"))) {
				return null;
			}
			RuntimeFormInterface serviceToggle = state.getRuntimeForm(
					tabGroupSlot, getParameterString("SPSServices", "tab 8"));
			SlotInterface serviceToggleSlot = serviceToggle
					.getSubSlot("toggle");
			RuntimeFormInterface serviceDetail = state.getRuntimeForm(
					serviceToggleSlot, "sps_special_detail");
			DataBean spsSpecialServiceFocus = serviceDetail.getFocus();
			return spsSpecialServiceFocus;
		} catch (NullPointerException e) {
			_log.debug("LOG_SYSTEM_OUT",
					"!!!! No Special Services Form, returning null", 100L);
			return null;
		}
	}

	/**
	 * Gets the cx storer focus.
	 *
	 * @param state the state
	 * @param tabGroupSlot the tab group slot
	 * @return the cx storer focus
	 */
	private DataBean getCxStorerFocus(StateInterface state,
			SlotInterface tabGroupSlot) {
		try
		{
			
			if ("skip".equals(getParameterString("CxStorerTab"))) {
				return null;
			}
			RuntimeFormInterface cxStorerForm = state.getRuntimeForm(tabGroupSlot, getParameterString("CxStorerTab", "tab 14"));
			SlotInterface cxStorerToggleSlot = cxStorerForm.getSubSlot("wm_cxstorer_toggle_view");

			RuntimeFormInterface cxStorerDetailForm = state.getRuntimeForm(cxStorerToggleSlot,
			"DETAIL1");
			DataBean cxStorerFocus = cxStorerDetailForm.getFocus();


			return cxStorerFocus;
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!!!! No CXStorer Form found, returning null",100L);
			return null;
		}


	}

	/**
	 * Gets the parent focus.
	 *
	 * @param state the state
	 * @param tabGroupSlot the tab group slot
	 * @return the parent focus
	 */
	private DataBean getParentFocus(StateInterface state, SlotInterface tabGroupSlot)
	{
		//		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//		_log.debug("LOG_SYSTEM_OUT","\n1'''Current form  = " + shellToolbar.getName(),100L);
		//
		//		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		//		_log.debug("LOG_SYSTEM_OUT","\n2'''Current form  = " + shellForm.getName(),100L);
		//		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		//
		//		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		//		DataBean parentFocus = detailForm.getFocus();
		//Parent (SKU) Focus
		RuntimeFormInterface parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
		DataBean parentFocus = parentForm.getFocus();
		return parentFocus;

	}

	/**
	 * Gets the tab group slot.
	 *
	 * @param state the state
	 * @return the tab group slot
	 */
	private SlotInterface getTabGroupSlot(StateInterface state)
	{
		//Common 
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot(getParameterString("SLOT", "tbgrp_slot"));
		return tabGroupSlot;
	}

	/**
	 * Gets the owner billing focus.
	 *
	 * @param state the state
	 * @param tabGroupSlot the tab group slot
	 * @return the owner billing focus
	 */
	private DataBean getOwnerBillingFocus(StateInterface state, SlotInterface tabGroupSlot)
	{
		//Alternate Items
		try
		{
			if ("skip".equals(getParameterString("OwnerBillingTab"))) {
				return null;
			}
			RuntimeFormInterface OwnerBillingForm = state.getRuntimeForm(tabGroupSlot, getParameterString("OwnerBillingTab", "tab 5"));
			DataBean OwnerBillingFocus = OwnerBillingForm.getFocus();
			return OwnerBillingFocus;
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!!!! No Alternate Items Detail Form, returning null",100L);
			return null;
		}
	}

	/**
	 * Gets the owner label focus.
	 *
	 * @param state the state
	 * @param tabGroupSlot the tab group slot
	 * @return the owner label focus
	 */
	private DataBean getOwnerLabelFocus(StateInterface state, SlotInterface tabGroupSlot)
	{
		//Alternate Items
		try
		{
			if ("skip".equals(getParameterString("OwnerLabelTab"))) {
				return null;
			}
			RuntimeFormInterface OwnerLabelForm = state.getRuntimeForm(tabGroupSlot, getParameterString("OwnerLabelTab", "tab 10"));
			SlotInterface OwnerLabelToggleSlot = OwnerLabelForm.getSubSlot("wm_owner_labels_toggle_slot");

			RuntimeFormInterface OwnerLabelDetailForm = state.getRuntimeForm(OwnerLabelToggleSlot,
			"wm_owner_labels_detail_view");
			DataBean OwnerLabelDetailFocus = OwnerLabelDetailForm.getFocus();
			return OwnerLabelDetailFocus;
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!!!! No Alternate Items Detail Form, returning null",100L);
			return null;
		}
	}

	/**
	 * Gets the uDF label focus.
	 *
	 * @param state the state
	 * @param tabGroupSlot the tab group slot
	 * @return the uDF label focus
	 */
	private DataBean getUDFLabelFocus(StateInterface state, SlotInterface tabGroupSlot)
	{
		//Assign Locations
		try
		{
			if ("skip".equals(getParameterString("UDFLabelTab"))) {
				return null;
			}
			RuntimeFormInterface UDFLabelForm = state.getRuntimeForm(tabGroupSlot, getParameterString("UDFLabelTab", "tab 12"));
			SlotInterface UDFLabelToggleSlot = UDFLabelForm.getSubSlot("wm_owner_labels_toggle_slot");

			RuntimeFormInterface UDFLabelDetailForm = state.getRuntimeForm(UDFLabelToggleSlot,
			"wm_owner_udf_detail_view");
			DataBean UDFLabelDetailFocus = UDFLabelDetailForm.getFocus();
			return UDFLabelDetailFocus;
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!!!! No Assign Location Detail Form, returning null",100L);
			return null;
		}
	}

	/**
	 * Facility is enterprise.
	 *
	 * @param state the state
	 * @return true, if successful
	 */
	private boolean facilityIsEnterprise(StateInterface state)
	{
		HttpSession session = state.getRequest().getSession();
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

		String IsEnterprise = null;
		try
		{
			IsEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		} catch (java.lang.NullPointerException e)
		{
			IsEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		}

		if (IsEnterprise.equals("1"))
		{

			return true;

		}
		else
		{

			return false;

		}
	}
	
	//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Starts
	private DataBean getOwnerBillingChargeCodesFocus(StateInterface state, SlotInterface tabGroupSlot)
	{
		//Alternate Items
		try
		{
			if ("skip".equals(getParameterString("BillingChargeCodesTab"))) {
				return null;
			}
			RuntimeFormInterface OwnerChargeCodesForm = state.getRuntimeForm(tabGroupSlot, getParameterString("BillingChargeCodesTab","tab 18"));
			_log.debug("LOG_SYSTEM_OUT","::::::::::OwnerChargeCodesForm:::::::::::::::"+OwnerChargeCodesForm.getName(), 100L);
			SlotInterface OwnerChargeCodesToggleSlot = OwnerChargeCodesForm.getSubSlot("wm_owner_chargecodesforbilling_toggle");

			RuntimeFormInterface wm_owner_chargecodesforbilling_form = state.getRuntimeForm(OwnerChargeCodesToggleSlot,
			"wm_owner_chargecodesforbilling_toggle_DetailView");
			DataBean wm_owner_chargecodesforbilling_formfocus = wm_owner_chargecodesforbilling_form.getFocus();
			return wm_owner_chargecodesforbilling_formfocus;
		} catch (NullPointerException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!!!! No Alternate Items Detail Form, returning null",100L);
			return null;
		}
	}
	//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Ends

}
