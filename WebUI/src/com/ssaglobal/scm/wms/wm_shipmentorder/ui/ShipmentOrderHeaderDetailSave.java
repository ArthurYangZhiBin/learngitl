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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.Iterator;
import java.util.Set;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioMetadata;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class ShipmentOrderHeaderDetailSave extends SaveAction {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderHeaderDetailSave.class);

	public ShipmentOrderHeaderDetailSave() {
		_log.info("EXP_1", "HeaderDetailSave Instantiated!!!", SuggestedCategory.NONE);
	}

	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		// Get user entered criteria
		String shellSlot1 = (String) getParameter("shellSlot1");
		String shellSlot2 = (String) getParameter("shellSlot2");
		String toggleFormSlot = (String) getParameter("toggleSlot");
		String detailBiocollection = (String) getParameter("detailBiocollection");
		String detailFormTab = (String) getParameter("detailFormTab");
		// _log.debug("LOG_SYSTEM_OUT","********shellSlot1="+shellSlot1,100L);
		// _log.debug("LOG_SYSTEM_OUT","********shellSlot2="+shellSlot2,100L);
		// _log.debug("LOG_SYSTEM_OUT","********toggleFormSlot="+toggleFormSlot,100L);
		// _log.debug("LOG_SYSTEM_OUT","********detailBiocollection="+detailBiocollection,100L);
		// _log.debug("LOG_SYSTEM_OUT","********detailFormTab="+detailFormTab,100L);
		// try{
		StateInterface state = context.getState();

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);

		// get header data
		// SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1); // HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if (headerForm.isListForm()) {
			// Bugaware 8569. Shipment Order usability - fields not editable from list view
			try {
				uow.saveUOW(true);
			} catch (UnitOfWorkException e) {
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
				Throwable nested = (e).findDeepestNestedException();
				if (nested != null) {
					_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
								"Nested " + nested.getClass().getName(),
								SuggestedCategory.NONE);
					_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
								"Message " + nested.getMessage(),
								SuggestedCategory.NONE);
				}
				if (nested instanceof ServiceObjectException) {
					String reasonCode = nested.getMessage();
					throwUserException(e, reasonCode, null);
				} else {
					throwUserException(e, "ERROR_SAVING_BIO", null);
				}

			} catch (EpiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			// Set Navigation to List
			result.setFocus(headerForm.getFocus());
			context.setNavigation("listClickEvent1748");
			return RET_CONTINUE;

		}
		DataBean headerFocus = headerForm.getFocus();
		// default navigation
		context.setNavigation("clickEvent193");

		// get detail data
		// SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2); // HC
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

		BioBean headerBioBean = null;
		if (headerFocus.isTempBio()) {// it is for insert header
			// _log.debug("LOG_SYSTEM_OUT","inserting header ******",100L);
			try {
				headerBioBean = uow.getNewBio((QBEBioBean) headerFocus);
				setAuditGUID(headerBioBean);

			} catch (EpiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}

			DataBean detailFocus = detailForm.getFocus();
			// headerBioBean.addBioCollectionLink("LOADSCHEDULEDETAIL", (QBEBioBean)detailFocus);
			if (detailFocus != null) {
				headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus); // HC
				setAuditGUID(detailBiocollection, headerBioBean, detailFocus, uow);
			}

		} else {// it is for update header
			// _log.debug("LOG_SYSTEM_OUT","updating header ******",100L);
			headerBioBean = (BioBean) headerFocus;
			// SlotInterface toggleSlot = detailForm.getSubSlot("wm_load_schedule_detail_toggle");
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot); // HC
			// RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_load_shcedule_detail_toggle_tab");
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, detailFormTab); // HC
			DataBean detailFocus = detailTab.getFocus();

			if (detailFocus != null && detailFocus.isTempBio()) {
				// _log.debug("LOG_SYSTEM_OUT","*****detaiFocus is tempbio="+detailFocus.isTempBio(),100L);
				// _log.debug("LOG_SYSTEM_OUT","inserting detail ******",100L);
				// headerBioBean.addBioCollectionLink("LOADSCHEDULEDETAIL", (QBEBioBean)detailFocus);
				headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus); // HC

				setAuditGUID(detailBiocollection, headerBioBean, detailFocus, uow);

			} else {
				// _log.debug("LOG_SYSTEM_OUT","updating detail ******",100L);
			}

		}
		try {
			uow.saveUOW(true);
		} catch (UnitOfWorkException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
			Throwable nested = (e).findDeepestNestedException();
			if (nested != null) {
				_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
							"Nested " + nested.getClass().getName(),
							SuggestedCategory.NONE);
				_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
							"Message " + nested.getMessage(),
							SuggestedCategory.NONE);
			}
			if (nested instanceof ServiceObjectException) {
				String reasonCode = nested.getMessage();
				throwUserException(e, reasonCode, null);
			} else {
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		} catch (EpiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		uow.clearState();
		result.setFocus(headerBioBean);

		// }catch(Exception e){
		// e.printStackTrace();
		// }

		return RET_CONTINUE;

	}

	private void setAuditGUID(BioBean headerBioBean) {

		BioType bioType = Metadata.getInstance().getBioType(headerBioBean.getTypeName());

		String tablename = bioType.getPrimaryKeyRecordSetType().getRecordSetPhysicalName();

		String guidField = tablename + "ID";

		try {
			BioAttributeMetadata[] meta = headerBioBean.getBioAttributeMetadata();
			for (int idx = 0; idx < meta.length; idx++) {

				if (meta[idx].getName().equalsIgnoreCase(guidField)) {
					headerBioBean.set(meta[idx].getName(), GUIDFactory.getGUIDStatic());
					break;
				}
			}
		} catch (Exception e) {
			_log.debug("LOG_SYSTEM_OUT","[HeaderDetailSave]Failed to setAuditGUID",100L);
			e.printStackTrace();
		}

	}

	private void setAuditGUID(	String bioAttrCollectionName,
								BioBean headerBioBean,
								DataBean detailBioBean,
								UnitOfWorkBean uow) {

		try {
			BioAttributeMetadata[] meta = headerBioBean.getBioAttributeMetadata();
			for (int idx = 0; idx < meta.length; idx++) {

				if (meta[idx].getName().equalsIgnoreCase(bioAttrCollectionName)) {

					String bioTypeName = meta[idx].getTargetBioType();
					BioType bioType = Metadata.getInstance().getBioType(bioTypeName);
					String tablename = bioType.getPrimaryKeyRecordSetType().getRecordSetPhysicalName();

					String guidField = tablename + "ID";

					BioMetadata bioMeta = uow.getBioMetadata(bioTypeName);
					Set attributes = bioMeta.getAllAttributes();
					// Verify that GUID field exists in BIO detail
					for (Iterator itr = attributes.iterator(); itr.hasNext();) {

						String attributeName = (String) itr.next();

						if (attributeName.equalsIgnoreCase(guidField)) {

							QBEBioBean qbe = (QBEBioBean) detailBioBean;
							qbe.set(guidField.toUpperCase(), GUIDFactory.getGUIDStatic());
							break;
						}

					}// end for

					break;
				}// end if
			}
		} catch (Exception e) {
			_log.debug("LOG_SYSTEM_OUT","[HeaderDetailSave]Failed to setAuditGUID",100L);
			e.printStackTrace();
		}

	}

}
