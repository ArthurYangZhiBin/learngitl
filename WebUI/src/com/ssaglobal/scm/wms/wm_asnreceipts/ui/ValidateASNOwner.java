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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ValidateASNOwner extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * 
	 * @param state
	 *            The StateInterface for this extension
	 * @param widget
	 *            The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory
			.getInstance(ValidateASNOwner.class);

	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {
		final String BIO = "wm_storer";
		final String TYPE = "1";
		Object receiptType = null;
		RuntimeFormInterface currentForm = context.getState()
				.getCurrentRuntimeForm();
		RuntimeFormInterface tabGroupShellForm = (currentForm
				.getParentForm(context.getState()));
		SlotInterface tabGroupSlot = tabGroupShellForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface ASNGeneralTab = context.getState().getRuntimeForm(
				tabGroupSlot, "tab 1");
		RuntimeFormWidgetInterface allowAutoReceipt = ASNGeneralTab
				.getFormWidgetByName("ALLOWAUTORECEIPT");
		RuntimeFormWidgetInterface trackInventoryBy = ASNGeneralTab
				.getFormWidgetByName("TRACKINVENTORYBY");
		DataBean receiptDataBean = currentForm.getFocus();
		if (receiptDataBean instanceof QBEBioBean) {
			QBEBioBean receiptQBEBioBean = (QBEBioBean) receiptDataBean;
			receiptType = receiptQBEBioBean.get("TYPE");
		} else {
			BioBean receiptBioBean = (BioBean) receiptDataBean;
			receiptType = receiptBioBean.get("TYPE");
		}
		int size = 0;
		String displayValue = context.getSourceWidget().getDisplayValue()
				.toString();
		BioCollectionBean listCollection = null;
		try {
			String sQueryString = "(wm_storer.STORERKEY = '" + displayValue
					+ "' AND  wm_storer.TYPE = '" + TYPE + "')";
			_log
					.debug("LOG_SYSTEM_OUT", "sQueryString = " + sQueryString,
							100L);
			Query bioQuery = new Query(BIO, sQueryString, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			listCollection = uow.getBioCollectionBean(bioQuery);
			size = listCollection.size();

		} catch (EpiException e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;

		}
		if (size == 0) {
			RuntimeFormInterface formName = context.getState()
					.getCurrentRuntimeForm();
			String widgetName = context.getSourceWidget().getName();
			String[] errorParam = new String[2];
			errorParam[0] = displayValue;
			FieldException UsrExcp = new FieldException(formName, widgetName,
					"WMEXP_OWNER_VALID", errorParam);
			throw UsrExcp;

		} else {
			if (receiptType == null) {
				UserException usrExcp = new UserException(
						"WMEXP_VALIDATETYPE_1", new Object[] {});
				throw usrExcp;
			}
			if (receiptType.toString().equalsIgnoreCase("4")
					|| receiptType.toString().equalsIgnoreCase("5")) {
				if (listCollection.get("0").get("ALLOWSINGLESCANRECEIVING")
						.equals("1")) {
					allowAutoReceipt.setProperty(
							RuntimeFormWidgetInterface.PROP_READONLY,
							Boolean.FALSE);
				} else {
					allowAutoReceipt.setProperty(
							RuntimeFormWidgetInterface.PROP_READONLY,
							Boolean.TRUE);
				}
				_log.debug("LOG_SYSTEM_OUT", "trackInventoryBy from storer = "
						+ listCollection.get("0").get("TRACKINVENTORYBY")
								.toString(), 100L);
				trackInventoryBy.setDisplayValue(listCollection.get("0").get(
						"TRACKINVENTORYBY").toString());
				// _log.debug("LOG_SYSTEM_OUT","trackInventoryBy from Form = "
				// +trackInventoryBy.getValue().toString(),100L);
			} else {
				allowAutoReceipt
						.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,
								Boolean.FALSE);
			}
			_log.debug("LOG_SYSTEM_OUT", "Entered Owner is Valid ", 100L);
			// Setting Owner as default for RECEIPTDETAIL form
			ArrayList<String> tabs = new ArrayList<String>();
			tabs.add("tab 0");
			RuntimeFormInterface detailForm = FormUtil.findForm(context
					.getState().getCurrentRuntimeForm(), "",
					"wms_ASN_Line_Detail_view", tabs, context.getState());
			if (detailForm != null) {
				detailForm.getFormWidgetByName("STORERKEY").setDisplayValue(displayValue);
				detailForm.getFormWidgetByName("SKU").setDisplayValue("");
				DataBean detailFocus = detailForm.getFocus();
				detailFocus.setValue("STORERKEY", displayValue);
				detailFocus.setValue("SKU", "");
			}
			result.setFocus(receiptDataBean);
		}
		return RET_CONTINUE;

	}

	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
