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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
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
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.epiphany.shr.ui.view.RuntimeListForm;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemSave extends SaveAction{
	
	//Static strings for ease of updating tab identifier orders
	private static String ENTERPRISE_START_TAB_ID = "tab 0";
	private static String WAREHOUSE_START_TAB_ID = "tab 7";
	private static String ASSIGN_LOCATIONS_TAB_ID = "tab 12";
	private static String ALTERNATE_ITEMS_TAB_ID = "tab 14";
	private static String SUBSTITUTE_ITEMS_TAB_ID = "tab 16";
	private final String SHELL_LIST_SLOT_1 ="list_slot_1";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemSave.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws UserException 
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{

		StateInterface state = context.getState();
		boolean listOnly = false;

		if (facilityIsEnterprise(state)){
			_log.debug("LOG_DEBUG_EXTENSION", "=-=-= In Enterprise, Saving", SuggestedCategory.NONE);
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "=-=-= In Warehouse, Saving", SuggestedCategory.NONE);
		}

		//Get Focii
		SlotInterface tabGroupSlot = null;
		try{
			tabGroupSlot = getTabGroupSlot(state);
		} catch (NoDetailException e){
			listOnly = true;
			
//			e.printStackTrace();
//			_log.error("LOG_ERROR_EXTENSION_ItemSave", "No Detail Exception " + e.message, SuggestedCategory.NONE);
//			_log.error("LOG_ERROR_EXTENSION_ItemSave", "No Detail Exception " + e.getMessage(), SuggestedCategory.NONE);
//			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot(SHELL_LIST_SLOT_1); 
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if (headerForm.isListForm()) {
				QBEBioBean filter = ((RuntimeListForm) headerForm).getFilterRowBean();
				Object packKeyObj = filter.getValue("PACKKEY");
				if(packKeyObj == null)
					filter.setValue("PACKKEY", "");
			}
		if (!listOnly){
			DataBean parentFocus = getParentFocus(state, tabGroupSlot);
			DataBean assignLocationFocus = null;
			DataBean alternateItemsFocus = null;
			DataBean substituteItemsFocus = null;
	
			if (!facilityIsEnterprise(state) && !listOnly){
				assignLocationFocus = getAssignLocationsFocus(state, tabGroupSlot);
				alternateItemsFocus = getAlternateItemsFocus(state, tabGroupSlot);
				substituteItemsFocus = getSubstituteItemsFocus(state, tabGroupSlot);
			}
	
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioBean parentBioBean = null;
			if (parentFocus.isTempBio()){
				//Inserting New Parent
				_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Inserting New Parent", SuggestedCategory.NONE);
				try{
					parentBioBean = uow.getNewBio((QBEBioBean) parentFocus);
				} catch (EpiException e){
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_ItemSave", "Error from Inserting New Parent", SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
					parentBioBean.set("GUID", GUIDFactory.getGUIDStatic());
				if ((assignLocationFocus != null)){
					parentBioBean.addBioCollectionLink("wm_skuxloc_map", (QBEBioBean) assignLocationFocus);
				}
				if ((alternateItemsFocus != null)){
					parentBioBean.addBioCollectionLink("wm_altsku_map", (QBEBioBean) alternateItemsFocus);
				}
				if ((substituteItemsFocus != null)){
					parentBioBean.addBioCollectionLink("wm_substitutesku_bio_map", (QBEBioBean) substituteItemsFocus);
				}
			}else{
				//Updating Parent
				_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Updating Parent", SuggestedCategory.NONE);
				parentBioBean = (BioBean) parentFocus;
	
			
				if ((assignLocationFocus != null)){
					printDirty("ASSIGNLOC", assignLocationFocus);
	
					if (assignLocationFocus.isTempBio()){
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Inserting Assign Location", SuggestedCategory.NONE);
						parentBioBean.addBioCollectionLink("wm_skuxloc_map", (QBEBioBean) assignLocationFocus);
					}else{
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Updating Assign Location", SuggestedCategory.NONE);
	
					}
				}else{
					_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Skipping Assign Location", SuggestedCategory.NONE);
				}
	
				if ((alternateItemsFocus != null)){
					printDirty("ALTSKU", alternateItemsFocus);
	
					if (alternateItemsFocus.isTempBio()){
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Inserting Alternate Items", SuggestedCategory.NONE);
						parentBioBean.addBioCollectionLink("wm_altsku_map", (QBEBioBean) alternateItemsFocus);
					}else{
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Updating Alternate Items", SuggestedCategory.NONE);
					}
				}else{
					_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Skipping Alternate Items", SuggestedCategory.NONE);
				}
	
				if ((substituteItemsFocus != null)){
					printDirty("SUBSKU", substituteItemsFocus);
	
					if (substituteItemsFocus.isTempBio()){
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Inserting Substitute Items", SuggestedCategory.NONE);
						parentBioBean.addBioCollectionLink("wm_substitutesku_bio_map", (QBEBioBean) substituteItemsFocus);
					}else{
						_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Updating Substitute Items", SuggestedCategory.NONE);
					}
				}else{
					_log.debug("LOG_DEBUG_EXTENSION", "\n@@@@Skipping Substitute Items", SuggestedCategory.NONE);
				}
			}
			
			_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n&&&& Saving UOW\n!\n!\n", SuggestedCategory.NONE);
			try{
				uow.saveUOW(true);
			} catch (UnitOfWorkException e) {
				e.printStackTrace();
				Throwable nested = (e).findDeepestNestedException();
				if (nested instanceof ServiceObjectException) {
					String reasonCode = nested.getMessage();
					_log.error("LOG_ERROR_EXTENSION_ItemSave_execute", reasonCode, SuggestedCategory.NONE);
					throw new UserException(reasonCode, new Object[] {});
				} else {
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
			} catch (EpiException e) {
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getErrorMessage(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getErrorName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getFullErrorName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n&&&& Clearing State\n!\n!\n", SuggestedCategory.NONE);
			uow.clearState();
			
			_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n&&&& Setting Focus\n!\n!\n", SuggestedCategory.NONE);
			result.setFocus(parentBioBean);

		}else{
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n&&&& Saving UOW\n!\n!\n", SuggestedCategory.NONE);
			try{
				uow.saveUOW(true);
			} catch (UnitOfWorkException e) {
				e.printStackTrace();
				Throwable nested = (e).findDeepestNestedException();
				if (nested instanceof ServiceObjectException) {
					String reasonCode = nested.getMessage();
					_log.error("LOG_ERROR_EXTENSION_ItemSave_execute", reasonCode, SuggestedCategory.NONE);
					throw new UserException(reasonCode, new Object[] {});
				} else {
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
			} catch (EpiException e) {
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getErrorMessage(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getErrorName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getFullErrorName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_ItemSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n&&&& Clearing State\n!\n!\n", SuggestedCategory.NONE);
			uow.clearState();
		}

		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue){
		if (attributeValue == null){
			return true;
		}else{
			return false;
		}
	}

	private void printDirty(String name, DataBean bean){
		if (bean.isDirty()){
			_log.debug("LOG_DEBUG_EXTENSION", "!@# " + name + " is Dirty", SuggestedCategory.NONE);
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# " + name + " is not Dirty", SuggestedCategory.NONE);
		}
	}

	private DataBean getParentFocus(StateInterface state, SlotInterface tabGroupSlot){
		//		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//		_log.debug("LOG_DEBUG_EXTENSION", "\n1'''Current form  = " + shellToolbar.getName(), SuggestedCategory.NONE);
		//
		//		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		//		_log.debug("LOG_DEBUG_EXTENSION", "\n2'''Current form  = " + shellForm.getName(), SuggestedCategory.NONE);
		//		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		//
		//		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		//		DataBean parentFocus = detailForm.getFocus();
		//Parent (SKU) Focus
		RuntimeFormInterface parentForm;
		if (facilityIsEnterprise(state)){
			_log.debug("LOG_DEBUG_EXTENSION", "=-=-= In Enterprise, Setting focus to Tab0", SuggestedCategory.NONE);
			parentForm = state.getRuntimeForm(tabGroupSlot, ENTERPRISE_START_TAB_ID);
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + parentForm.getName(), SuggestedCategory.NONE);
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "=-=-= In Warehouse, Setting focus to Tab5", SuggestedCategory.NONE);
			parentForm = state.getRuntimeForm(tabGroupSlot, WAREHOUSE_START_TAB_ID);
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + parentForm.getName(), SuggestedCategory.NONE);
		}

		DataBean parentFocus = parentForm.getFocus();
		//_log.debug("LOG_DEBUG_EXTENSION", "Test Value, SKU:" + parentFocus.getValue("SKU").toString(), SuggestedCategory.NONE);
		return parentFocus;
	}

	private SlotInterface getTabGroupSlot(StateInterface state) throws NoDetailException{
		//Common 
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		_log.debug("LOG_DEBUG_EXTENSION", "\n1'''Current form  = " + shellToolbar.getName(), SuggestedCategory.NONE);

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		_log.debug("LOG_DEBUG_EXTENSION", "\n2'''Current form  = " + shellForm.getName(), SuggestedCategory.NONE);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		if (isNull(tabGroupSlot)){
			throw new NoDetailException("No Detail On Item Form");
		}
		return tabGroupSlot;
	}

	private DataBean getSubstituteItemsFocus(StateInterface state, SlotInterface tabGroupSlot){
		//Substitute Items
		try{
			RuntimeFormInterface substituteItemsForm = state.getRuntimeForm(tabGroupSlot, SUBSTITUTE_ITEMS_TAB_ID);
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + substituteItemsForm.getName(), SuggestedCategory.NONE);
			SlotInterface substituteItemsToggleSlot = substituteItemsForm.getSubSlot("wm_item_substitutesku_toggle");

			RuntimeFormInterface substituteItemsDetailForm = state.getRuntimeForm(substituteItemsToggleSlot, "wm_item_substitutesku_toggle_detail");
			DataBean substituteItemsDetailFocus = substituteItemsDetailForm.getFocus();
			//_log.debug("LOG_DEBUG_EXTENSION", "Test Value, QTY:" + substituteItemsDetailFocus.getValue("QTY").toString(), SuggestedCategory.NONE);
			return substituteItemsDetailFocus;
		} catch (NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!!!! No Substitute Items Detail Form, returning null", SuggestedCategory.NONE);
			return null;
		}
	}

	private DataBean getAlternateItemsFocus(StateInterface state, SlotInterface tabGroupSlot){
		//Alternate Items
		try{
			RuntimeFormInterface alternateItemsForm = state.getRuntimeForm(tabGroupSlot, ALTERNATE_ITEMS_TAB_ID);
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + alternateItemsForm.getName(), SuggestedCategory.NONE);
			SlotInterface alternateItemsToggleSlot = alternateItemsForm.getSubSlot("wm_item_alternate_toggle");

			RuntimeFormInterface alternateItemsDetailForm = state.getRuntimeForm(alternateItemsToggleSlot, "wm_item_alternate_toggle_detail_tab");
			DataBean alternateItemsDetailFocus = alternateItemsDetailForm.getFocus();
			//_log.debug("LOG_DEBUG_EXTENSION", "Test Value, SKU:" + alternateItemsDetailFocus.getValue("SKU").toString(), SuggestedCategory.NONE);
			return alternateItemsDetailFocus;
		} catch (NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!!!! No Alternate Items Detail Form, returning null", SuggestedCategory.NONE);
			return null;
		}
	}

	private DataBean getAssignLocationsFocus(StateInterface state, SlotInterface tabGroupSlot){
		//Assign Locations
		try{
			RuntimeFormInterface assignLocationsForm = state.getRuntimeForm(tabGroupSlot, ASSIGN_LOCATIONS_TAB_ID);
			_log.debug("LOG_DEBUG_EXTENSION", "\n4'''Current form  = " + assignLocationsForm.getName(), SuggestedCategory.NONE);
			SlotInterface assignLocationsToggleSlot = assignLocationsForm.getSubSlot("wm_assignlocations_formslot");

			RuntimeFormInterface assignLocationsDetailForm = state.getRuntimeForm(assignLocationsToggleSlot, "wm item assignlocation toggle detail tab");
			DataBean assignLocationsDetailFocus = assignLocationsDetailForm.getFocus();
			//_log.debug("LOG_DEBUG_EXTENSION", "Test Value, Loc:" + assignLocationsDetailFocus.getValue("LOC").toString(), SuggestedCategory.NONE);
			return assignLocationsDetailFocus;
		} catch (NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION", "!!!! No Assign Location Detail Form, returning null", SuggestedCategory.NONE);
			return null;
		}
	}

	private boolean facilityIsEnterprise(StateInterface state){
		HttpSession session = state.getRequest().getSession();

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();

		String facility = null;
		try{
			facility = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility: " + facility, SuggestedCategory.NONE);
		} catch (java.lang.NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Getting Facility from Session Instead", SuggestedCategory.NONE);
			facility = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			_log.debug("LOG_DEBUG_EXTENSION", "\n-Facility Session: " + facility, SuggestedCategory.NONE);
		}

		if (facility.equals("ENTERPRISE")){
			return true;
		}else{
			return false;
		}
	}

	public class NoDetailException extends Exception{
		String message;

		NoDetailException(String m){
			message = m;
		}

		@Override
		public String getMessage(){
			return message;
		}
	}
}