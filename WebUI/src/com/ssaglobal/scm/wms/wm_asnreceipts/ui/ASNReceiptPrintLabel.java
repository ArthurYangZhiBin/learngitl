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
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class ASNReceiptPrintLabel extends ListSelector {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNReceiptPrintLabel.class);
	protected int execute(ModalActionContext context, ActionResult result)
			throws EpiException {

		String labelName = "";
		String printerName = "";
		String copies = "";
		String slotNum = "";
		int slot = 9;

		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		RuntimeFormWidgetInterface labelWidget = form
				.getFormWidgetByName("Label");
		if (!((String) labelWidget.getValue() == null)
				&& !((String) labelWidget.getValue() == ("")))
			labelName = (String) labelWidget.getValue();

		RuntimeFormWidgetInterface printerWidget = form
				.getFormWidgetByName("Printer");
		if (!((String) printerWidget.getValue() == null)
				&& !((String) printerWidget.getValue() == ("")))
			printerName = (String) printerWidget.getValue();

		RuntimeFormWidgetInterface copiesWidget = form
				.getFormWidgetByName("Copies");
		if (!((String) copiesWidget.getValue() == null)
				&& !((String) copiesWidget.getValue() == ("")))
			copies = (String) copiesWidget.getValue();

		_log.debug("LOG_SYSTEM_OUT","labelName  " + labelName,100L);
		_log.debug("LOG_SYSTEM_OUT","printerName  " + printerName,100L);
		_log.debug("LOG_SYSTEM_OUT","copies  " + copies,100L);

		RuntimeFormInterface toolbar = context.getSourceForm(); // get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","SHEL: FORM......" + shellForm.getName(),100L);

		// if wms_list_shell then it is ASN Header
		if (shellForm.getName().equalsIgnoreCase("wms_list_shell")) {

			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1"); // get header slot
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,
					null); // header form
			DataBean headerFocus = headerForm.getFocus();

			_log.debug("LOG_SYSTEM_OUT","I  clicked on a ASN Header",100L);
			if (headerFocus instanceof BioCollection) { // if header list view
				_log.debug("LOG_SYSTEM_OUT","I am on a Header List form",100L);

				RuntimeListFormInterface headerListForm = (RuntimeListFormInterface) headerForm;
				ArrayList selectedItems = headerListForm.getSelectedItems();
				if (selectedItems != null && selectedItems.size() > 0) { // get list of selected records
					Iterator bioBeanIter = selectedItems.iterator();
					try {
						BioBean bio;
						for (; bioBeanIter.hasNext();) {

							bio = (BioBean) bioBeanIter.next();
							String ReceiptKey = bio.getString("RECEIPTKEY");

							//System.out.println("\n\nRECEIPTKEY:" + ReceiptKey
							//		+ "\n\n");
							_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:" + ReceiptKey + "\n\n",100L);

							WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
							Array parms = new Array();
							parms.add(new TextData(ReceiptKey));
							parms.add(new TextData(""));
							parms.add(new TextData(labelName));
							parms.add(new TextData(printerName));
							parms.add(new TextData(copies));
							actionProperties.setProcedureParameters(parms);
							actionProperties.setProcedureName("PrintASNLables");
							try {
								WmsWebuiActionsImpl.doAction(actionProperties);
							} catch (Exception e) {
								e.getMessage();
								UserException UsrExcp = new UserException(e
										.getMessage(), new Object[] {});
								throw UsrExcp;
							}

						}

					} catch (EpiException ex) {
						throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
					}
				}
			} else {
				_log.debug("LOG_SYSTEM_OUT","I am on a Header detail form",100L);

				if (headerFocus instanceof BioBean) {

					String ReceiptKey = headerFocus.getValue("RECEIPTKEY")
							.toString();
					try {
						_log.debug("LOG","\n\nRECEIPTKEY:" + ReceiptKey	+ "\n\n", 100L);
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array();
						parms.add(new TextData(ReceiptKey));
						parms.add(new TextData(""));
						parms.add(new TextData(labelName));
						parms.add(new TextData(printerName));
						parms.add(new TextData(copies));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("PrintASNLables");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e
									.getMessage(), new Object[] {});
							throw UsrExcp;
						}

					}

					catch (EpiException ex) {
						throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
					}
				}
			}

		}// else it is ASN Detail
		else {

			_log.debug("LOG_SYSTEM_OUT","I clicked on a ASN Detail",100L);
			SlotInterface toggleSlot = shellForm
					.getSubSlot("wm_receiptdetail_toggle");
			_log.debug("LOG_SYSTEM_OUT","toggleSlot = " + toggleSlot.getName(),100L);

			// get slotnumber from Context to find what tab user is on - list or form
			// slot number is put in context on Print label click
			if (state.getServiceManager().getUserContext().containsKey(
					"ASNLABELS")) {
				slotNum = (String) state.getServiceManager().getUserContext()
						.get("ASNLABELS");
				slot = Integer.parseInt(slotNum);
				_log.debug("LOG_SYSTEM_OUT","\n\n\n^^^^SLOTNUM: " + slot,100L);
				state.getServiceManager().getUserContext().remove("ASNLABELS");
			}

			if (slot == 0) {// it is ASN Detail List
				_log.debug("LOG_SYSTEM_OUT","I am on a Detail List form",100L);
				RuntimeFormInterface ListTab = state.getRuntimeForm(toggleSlot,
						"wm_receiptdetail_list_view");
				DataBean detailFocus = ListTab.getFocus();
				if (detailFocus instanceof BioCollection) { // if list view
					RuntimeListFormInterface detailListForm = (RuntimeListFormInterface) ListTab;
					ArrayList selectedItems = detailListForm.getSelectedItems();
					if (selectedItems != null && selectedItems.size() > 0) { // get list of selected records
						Iterator bioBeanIter = selectedItems.iterator();
						try {
							BioBean bio;

							for (; bioBeanIter.hasNext();) {

								bio = (BioBean) bioBeanIter.next();
								String ReceiptKey = bio.getString("RECEIPTKEY");
								String ReceiptlineNumber = bio
										.getString("RECEIPTLINENUMBER");

								//System.out.println("\n\nRECEIPTKEY:"
								//		+ ReceiptKey + "\n\n");

								//System.out.println("\n\nRECEIPTLine :"
								//		+ ReceiptlineNumber + "\n\n");
								_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:" + ReceiptKey + "\n\n",100L);
								_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTLine:" + ReceiptlineNumber + "\n\n",100L);

								WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
								Array parms = new Array();
								parms.add(new TextData(ReceiptKey));
								parms.add(new TextData(ReceiptlineNumber));
								parms.add(new TextData(labelName));
								parms.add(new TextData(printerName));
								parms.add(new TextData(copies));
								actionProperties.setProcedureParameters(parms);
								actionProperties
										.setProcedureName("PrintASNLables");
								try {
									WmsWebuiActionsImpl
											.doAction(actionProperties);
								} catch (Exception e) {
									e.getMessage();
									UserException UsrExcp = new UserException(e
											.getMessage(), new Object[] {});
									throw UsrExcp;
								}

							}

						} catch (EpiException ex) {
							throwUserException(ex, "ERROR_EXECUTING_ACTION",
									null);
						}
					}

				}

			} else {// it is ASN Detail Form
				_log.debug("LOG_SYSTEM_OUT","I am on a Detail  form",100L);
				RuntimeFormInterface detailTab = state.getRuntimeForm(
						toggleSlot, "wm_receiptdetail_detail_view");
				DataBean detailFocus = detailTab.getFocus();
				BioBean bio;
				if (detailFocus instanceof BioBean) {
					bio = (BioBean) detailFocus;
					String ReceiptKey = bio.getValue("RECEIPTKEY").toString();
					String ReceiptlineNumber = bio
							.getString("RECEIPTLINENUMBER");
					_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:" + ReceiptKey + "\n\n",100L);
					_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTLine:" + ReceiptlineNumber + "\n\n",100L);

					//System.out.println("\n\nRECEIPTLine :" + ReceiptlineNumber
					//		+ "\n\n");

					try {
						_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:" + ReceiptKey + "\n\n",100L);
						//System.out.println("\n\nRECEIPTKEY:" + ReceiptKey
						//		+ "\n\n");
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array();
						parms.add(new TextData(ReceiptKey));
						parms.add(new TextData(ReceiptlineNumber));
						parms.add(new TextData(labelName));
						parms.add(new TextData(printerName));
						parms.add(new TextData(copies));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("PrintASNLables");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e
									.getMessage(), new Object[] {});
							throw UsrExcp;
						}

					}

					catch (EpiException ex) {
						throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
					}
				}
			}
		}

		return RET_CONTINUE;
	}
}
