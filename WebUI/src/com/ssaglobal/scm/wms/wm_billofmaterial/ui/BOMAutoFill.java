/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.wm_billofmaterial.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ASNReceiptDetailPreRender;
import com.ssaglobal.scm.wms.wm_internal_transfer.ui.InternalTransferSetToSession;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class BOMAutoFill extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(BOMAutoFill.class);

	private static final String LIST_SHELL = "listShellForm";
	private static final String LIST_SLOT1 = "shellSlot1";
	private static final String LIST_SLOT2 = "shellSlot2";
	private static final String DETAIL_TOGGLE_SLOT = "detailToggleSlot";
	private static final String TAB_GROUP = "tbgrp_slot";
	private static final String TAB0 = "tab 0";
	private static final String TAB_DETAIL="Detail";
	
	//Table name constants
	private static final String TABLE_ITEM = "sku";
	private static final String DESCRIPTION = "DESCR";
	
	//Widget name constants
	private static final String OWNER = "STORERKEY";
	private static final String ITEM = "SKU";
	private static final String ITEM_DESCR = "DESCRIPTION";
	
	//Error message constants
	private static final String ERROR_MSG_OWNER = "WMEXP_ENTEROWNER";
	private static final String ERROR_MSG_ITEM = "WMEXP_VALIDATESKU";
	
	
	//Constant strings
	private static String ZERO = "0";
	
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
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String newItemValue = context.getSourceWidget().getDisplayValue();
		//Get the storer from form
		String shellSlot2 = getParameter(LIST_SLOT2).toString();
		String listShellForm = getParameter(LIST_SHELL).toString();
		String detailToggleSlot = getParameter(DETAIL_TOGGLE_SLOT).toString();
		String value = null;
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		
		System.out.println(">>>>>>>>ShellForm 1:" +shellForm.getName());
		
		_log.debug("LOG_DEBUG_EXTENSION_BOMAUTOFILL","ShellForm " +shellForm.getName(), 100L);
		
		while(!(shellForm.getName().equals(listShellForm))){
			shellForm = shellForm.getParentForm(state);
		}
		
		System.out.println(">>>>>>>>ShellForm 2:" +shellForm.getName());

		
		SlotInterface slot2 = shellForm.getSubSlot(shellSlot2);
		RuntimeFormInterface detailForm = state.getRuntimeForm(slot2, null);
		if (detailForm.getName().equalsIgnoreCase(detailToggleSlot)){
			SlotInterface toggleSlot = detailForm.getSubSlot(detailToggleSlot);
			detailForm = state.getRuntimeForm(toggleSlot,TAB_DETAIL);
		}

		System.out.println(">>>>>>>>>>>>DetailForm " +detailForm.getName());

		SlotInterface detailTbgrpSlot = detailForm.getSubSlot(TAB_GROUP);
		RuntimeFormInterface normalDetailForm = state.getRuntimeForm(detailTbgrpSlot, TAB0);
		value = normalDetailForm.getFormWidgetByName(OWNER).getDisplayValue();
		
		
		//Executes only if detail form is a new screen
		setSkuDesc(newItemValue, value, uow, state);
		
		return RET_CONTINUE;
	}
   
   
    
    
    public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get(ZERO).get(widgetName)){
			result=focus.get(ZERO).get(widgetName).toString();
		}
		return result;
	}
    
    public void setSkuDesc(String newItemValue, String value, UnitOfWorkBean uow, StateInterface state)
    	throws EpiException{

		String qry = TABLE_ITEM+"."+OWNER+"='"+value+"'";
		Query query = new Query(TABLE_ITEM, qry, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(query);
		//Throw exception for unrecognized value
		if(newFocus==null || newFocus.size()<1){
			throw new UserException(ERROR_MSG_OWNER, new Object[]{});
		} 

		//Query sku table for data points
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		qry = TABLE_ITEM+"."+OWNER+"='"+value+"' AND "+TABLE_ITEM+"."+ITEM+"='"+newItemValue+"'";
		query = new Query(TABLE_ITEM, qry, null);		
		BioCollectionBean itemBio = uow.getBioCollectionBean(query);
		if (itemBio == null || itemBio.size()<1){
			String[] parameters = new String[2];
			parameters[0]= newItemValue;
			parameters[1]= value;
			currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue("");
			throw new UserException(ERROR_MSG_ITEM, parameters);
		}else{
			currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue(isNull(itemBio, DESCRIPTION));
			/*
			try{
				currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue(isNull(itemBio, DESCRIPTION));
			}catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
			*/
		}

    }
}
