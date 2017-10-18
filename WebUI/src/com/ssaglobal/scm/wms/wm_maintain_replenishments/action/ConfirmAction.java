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

package com.ssaglobal.scm.wms.wm_maintain_replenishments.action;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.common.ui.EnforceDefaultValuePrecision;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserContextUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.sun.mail.handlers.message_rfc822;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ConfirmAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private static final String CONFIRM_COMPLETED = "CONFIRM_COMPLETED";

	private static final String CAINPUTQTY = "CAINPUTQTY";

	//	12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments starts
	private static final String CAGWTQTY = "CAGWTQTY";
	private static final String CANWTQTY = "CANWTQTY";
	private static final String CATWTQTY = "CATWTQTY";
	//	12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments ends

	EpnyUserContext userContext;
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmAction.class);
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
		if( context.getSourceWidget() == null ) {
			//being called from the serial window
			//set navigation to confirm popup
			context.setNavigation("closeModalDialog310");
			
			
			Object interactionUserContextAttribute = UserContextUtil.getInteractionUserContextAttribute(CONFIRM_COMPLETED, context.getState());
			UserContextUtil.setInteractionUserContextAttribute(CONFIRM_COMPLETED, null, context.getState());
			//could also be called after confirming replenishment
			if (interactionUserContextAttribute != null) {
				context.setNavigation("closeModalDialog38");
				return RET_CONTINUE;
			}
		}
		
		userContext = context.getServiceManager().getUserContext();
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "Start of ConfirmAction - Non Modal" + "\n", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																							state.getCurrentRuntimeForm(),
																							"wms_list_shell",
																							"wm_maintain_replenishment_list_view",
																							state);
		HttpSession session = context.getState().getRequest().getSession();
		ArrayList items = listForm.getSelectedItems();
		if (isZero(items))
		{
			
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		HashMap inputQtyHash = new HashMap();

		//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments starts
		HashMap grossWgt1Hash = new HashMap();
		HashMap netWgt1Hash = new HashMap();
		HashMap tareWgt1Hash = new HashMap();
		Object grossWgt1Value =null;
		Object netWgt1Value = null;
		Object tareWgt1Value = null;
		//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments ends



		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selected = (BioBean) it.next();
			Object inputQtyValue = selected.getValue("INPUTQTY");
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "Putting into Session " + selected.getValue("REPLENISHMENTGROUP") + selected.getValue("REPLENISHMENTKEY") + " , " + inputQtyValue, SuggestedCategory.NONE);

			//if InputQty is null, set it to 0
			if (isNull(inputQtyValue))
			{
				inputQtyValue = "0";
			}
			selected.setValue("INPUTQTY", "");
			//set INPUTQTY in the hash based on the primary key of the REPLENISHMENT table
			inputQtyHash.put(selected.getValue("REPLENISHMENTGROUP").toString()
					+ selected.getValue("REPLENISHMENTKEY").toString(), inputQtyValue);

			//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments starts

			grossWgt1Value = selected.getValue("GROSSWGT");
			netWgt1Value = selected.getValue("NETWGT");
			tareWgt1Value = selected.getValue("TAREWGT");

			if (isNull(grossWgt1Value))		grossWgt1Value = "0";
			if (isNull(netWgt1Value))		netWgt1Value = "0";
			if (isNull(tareWgt1Value))		tareWgt1Value = "0";
			selected.setValue("GROSSWGT", "");
			selected.setValue("NETWGT", "");
			selected.setValue("TAREWGT", "");

			//set INPUTQTY in the hash based on the primary key of the REPLENISHMENT table
			grossWgt1Hash.put(selected.getValue("REPLENISHMENTGROUP").toString()
					+ selected.getValue("REPLENISHMENTKEY").toString(), grossWgt1Value);

			netWgt1Hash.put(selected.getValue("REPLENISHMENTGROUP").toString()
					+ selected.getValue("REPLENISHMENTKEY").toString(), netWgt1Value);

			tareWgt1Hash.put(selected.getValue("REPLENISHMENTGROUP").toString()
					+ selected.getValue("REPLENISHMENTKEY").toString(), tareWgt1Value);

			//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments ends

			
			//Adding Serial Numbers
			DataBean lotxLocxId = getLotxLocxId(selected, context.getState());
			double originalQtyValue = BioAttributeUtil.getDouble(lotxLocxId, "QTY");
			double inputQtyToMove = NumericValidationCCF.parseNumber(inputQtyValue.toString());
			if (originalQtyValue != inputQtyToMove) {
				// not moving the full LPN qty. check if this is an end to end
				// serial sku
				String partStr = "SELECT SNUM_ENDTOEND from SKU where SKU = '"
						+ BioAttributeUtil.getString(selected, "SKU")
						+ "' and STORERKEY = '"
						+ BioAttributeUtil.getString(selected, "STORERKEY") + "'";
				_log.debug("LOG_SYSTEM_OUT", partStr, 100L);
				EXEDataObject resultsFromSku = WmsWebuiValidationSelectImpl
						.select(partStr);

				if (resultsFromSku.getRowCount() > 0
						&& resultsFromSku.getAttribValue(1).getAsString()
								.equals("1")) {
					// its end to end serial and a partial qty.
					// Check if we have TransferDetailSerial bios to match this
					// row.
					String storerkey = BioAttributeUtil.getString(selected,
							"STORERKEY");
					String sku = BioAttributeUtil.getString(selected, "SKU");
					String lot = BioAttributeUtil.getString(selected, "LOT");
					String loc = BioAttributeUtil.getString(selected, "FROMLOC");
					String id = BioAttributeUtil.getString(selected, "ID");
					String key = storerkey.trim() + "_" + sku.trim() + "_"
							+ lot.trim() + "_" + loc.trim() + "_" + id.trim()
							+ "_SERIALS";
					String cache = (String) session.getAttribute(key);

					if (cache == null) {
						// need to handle serial numbers for this line yet.
						session.setAttribute("InventoryMove_QTYTOMOVE",
								new Integer((new Double(inputQtyToMove).intValue())).toString());
						session.setAttribute("InventoryMove_QTYSELECTED", "0");
						context.setNavigation("menuClickEventSerials");
						//Get LotXLocXId record for selected record
						result.setFocus(lotxLocxId);
						return RET_CONTINUE;
					} else {
//						// TODO: clear out any other wm_serialmove records for
//						// this key.
//
						// user entered something on the other page. put it in
						// the DB
						String serials[] = cache.split("\\|");

						if (inputQtyToMove != serials.length) {
							throw new UserException(
									"WM_EXP_NOTENOUGH_SERIALS_SELECTED",
									new Object[] {});

						}
						UnitOfWorkBean uow = state.getDefaultUnitOfWork();
						for (int idx = 0; idx < serials.length; ++idx) {
							QBEBioBean serial = uow
									.getQBEBioWithDefaults("wm_serialmove");
							serial.set("STORERKEY", storerkey);
							serial.set("SKU", sku);
							serial.set("LOT", lot);
							serial.set("LOC", loc);
							serial.set("ID", id);
							serial.set("SERIALNUMBER", serials[idx]);
							serial.save();
						}

						session.removeAttribute(key);
						uow.saveUOW(true);
					}
				}
			}	


		}
		userContext.remove(CAINPUTQTY);
		//putting hash into context;
		//state.getRequest().getSession().setAttribute(CAINPUTQTY, inputQtyHash);
		userContext.put(CAINPUTQTY, inputQtyHash);

		//12/12/2009 sateesh-3PL Enhancements- adv catch weight replenishments starts
		userContext.remove(CAGWTQTY);
		userContext.put(CAGWTQTY, grossWgt1Hash);

		userContext.remove(CANWTQTY);
		userContext.put(CANWTQTY, netWgt1Hash);

		userContext.remove(CATWTQTY);
		userContext.put(CATWTQTY, tareWgt1Hash);
		//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments ends

		return RET_CONTINUE;
	}

	private DataBean getLotxLocxId(BioBean selected, StateInterface state) throws EpiDataException, UserException {
		String lot = BioAttributeUtil.getString(selected, "LOT");
		String loc = BioAttributeUtil.getString(selected, "FROMLOC");
		String id = BioAttributeUtil.getString(selected, "ID");
		String query = "wm_lotxlocxid.LOT = '" + lot
				+ "' and wm_lotxlocxid.LOC = '" + loc
				+ "' and wm_lotxlocxid.ID = '" + id + "'";
		_log.debug("ConfirmAction_getLotxLocxId", "Query " + query,
				SuggestedCategory.APP_EXTENSION);
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_lotxlocxid", query, null));
		for(int i = 0 ; i < rs.size(); i++) {
			return rs.get("" + i);
		}
		_log.error("ConfirmAction_getLotxLocxId", "No lotxlocxid records found",
				SuggestedCategory.APP_EXTENSION);
		throw new UserException("WMEXP_GR_NOLOTXLOCXID", new Object[]{});
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		userContext = ctx.getServiceManager().getUserContext();
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "Start of ConfirmAction - Modal" + "\n", SuggestedCategory.NONE);
		StateInterface state = ctx.getState();
		try
		{

			state.closeModal(true);

			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);


			String inputQtyLabel = readLabel(listForm.getFormWidgetByName("INPUTQTY"));
			String qtyLabel = readLabel(listForm.getFormWidgetByName("QTY"));

			ArrayList items = listForm.getSelectedItems();
			//retrieve hash from session
			//HashMap inputQtyHash = (HashMap) state.getRequest().getSession().getAttribute(CAINPUTQTY);
			HashMap inputQtyHash = (HashMap) userContext.get(CAINPUTQTY);
			//state.getRequest().getSession().removeAttribute(CAINPUTQTY);
			userContext.remove(CAINPUTQTY);


			//12/12/2009 sateesh-3PL Enhancements- adv catch weight replenishments starts
			String gwtLabel = readLabel(listForm.getFormWidgetByName("GROSSWGT"));
			String nwtLabel = readLabel(listForm.getFormWidgetByName("NETWGT"));
			String twtLabel = readLabel(listForm.getFormWidgetByName("TAREWGT"));

			HashMap grossWgt1Hash = (HashMap) userContext.get(CAGWTQTY);
			HashMap netWgt1Hash = (HashMap) userContext.get(CANWTQTY);
			HashMap tareWgt1Hash = (HashMap) userContext.get(CATWTQTY);
			userContext.remove(CAGWTQTY);
			userContext.remove(CANWTQTY);
			userContext.remove(CATWTQTY);
			//12/12/2009 sateesh-3PL Enhancements- adv catch weight replenishments ends


			for (Iterator it = items.iterator(); it.hasNext();)
			{

				BioBean selectedRecord = (BioBean) it.next();
				String key = selectedRecord.getValue("REPLENISHMENTGROUP").toString()
						+ selectedRecord.getValue("REPLENISHMENTKEY").toString();
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "Key " + key , SuggestedCategory.NONE);
				double qtyValue = Double.valueOf(selectedRecord.getValue("QTY").toString()).doubleValue();
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "QTY " + qtyValue + "\n", SuggestedCategory.NONE);

				//Get InputQty from Hash
				Object inputQty = inputQtyHash.get(key);
				double inputQtyValue = NumericValidationCCF.parseNumber(inputQty.toString());
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "INPUTQTY " + inputQtyValue + "\n", SuggestedCategory.NONE);

				////////////////////////////////////////////////////////////////////////////////
				if (Double.isNaN(inputQtyValue))
				{
					//throw new UserException("WMEXP_NON_NUMERIC", new Object[] { inputQtyLabel });
					String errorMessage = getTextMessage("WMEXP_NON_NUMERIC", new Object[] { inputQtyLabel },
															state.getLocale());
					//Set Error Message into Session
					state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				//qty tests
				if (inputQtyValue == 0)
				{
					inputQtyValue = qtyValue;
				}
				if (inputQtyValue < 0)
				{
					//raise error
					//throw new UserException("WMEXP_QTYGREATEREQUALZERO", new Object[] { inputQtyLabel });
					String errorMessage = getTextMessage("WMEXP_QTYGREATEREQUALZERO", new Object[] { inputQtyLabel },
															state.getLocale());
					//Set Error Message into Session
					//state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}
				if (qtyValue < inputQtyValue)
				{
					//raise error
					//throw new UserException("WMEXP_GREATER_THAN", new Object[] { inputQtyLabel, qtyLabel });
					String errorMessage = getTextMessage("WMEXP_GREATER_THAN", new Object[] { qtyLabel, inputQtyLabel },
															state.getLocale());
					//Set Error Message into Session
					//state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}


				//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments starts
				boolean enabledadvCatWgt=false;
				double adjGwgt=0;
				double adjNwgt=0;
				double adjTwgt=0;

				if(qtyValue != inputQtyValue)
				{
					//recalculate weights
					CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();

					String storerkey=selectedRecord.getValue("STORERKEY").toString();
					String sku=selectedRecord.getValue("SKU").toString();
					String uom=selectedRecord.getValue("UOM").toString();
					String packkey=selectedRecord.getValue("PACKKEY").toString();
					String lot=selectedRecord.getValue("LOT").toString();
					String loc=selectedRecord.getValue("FROMLOC").toString();
					String id=selectedRecord.getValue("ID").toString();

					String enabledAdvCatWght=helper.isAdvCatchWeightEnabled(storerkey,sku);
					if((enabledAdvCatWght!=null)&&(enabledAdvCatWght.equalsIgnoreCase("1")))
					{
						enabledadvCatWgt=true;
						//TODO calculate weights & assign to the variables

						   HashMap actualWgts = null;
						   actualWgts=helper.getCalculatedWeightsLPN( storerkey, sku,lot,loc,id,inputQtyValue, uom, packkey);

						   adjGwgt= ((Double)actualWgts.get("GROSSWEIGHT")).doubleValue();
        				   adjNwgt= ((Double)actualWgts.get("NETWEIGHT")).doubleValue();
        				   adjTwgt= ((Double)actualWgts.get("TAREWEIGHT")).doubleValue();

        				   if (adjTwgt==0) adjTwgt=adjGwgt-adjNwgt;
        				   if (adjNwgt==0) adjNwgt=adjGwgt-adjTwgt;
        				   if (adjGwgt==0) adjGwgt=adjNwgt+adjTwgt;


        				}
				}


				Object gwtQty = grossWgt1Hash.get(key);
				String strGWTQty="0";

				if((enabledadvCatWgt)&&(qtyValue != inputQtyValue))	strGWTQty=""+adjGwgt;
				else	strGWTQty=gwtQty.toString();


				double gwtQtyValue = NumericValidationCCF.parseNumber(strGWTQty);

				if (Double.isNaN(gwtQtyValue))
				{
					String errorMessage = getTextMessage("WMEXP_NON_NUMERIC", new Object[] { gwtLabel },
															state.getLocale());
					state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				if (gwtQtyValue < 0)
				{
					String errorMessage = getTextMessage("WMEXP_QTYGREATEREQUALZERO", new Object[] { gwtLabel },
															state.getLocale());
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				Object nwtQty = netWgt1Hash.get(key);

				String strNWTQty="0";
				//check if user input qty not equal to original replenish qty , reassign weights
				//else keep the weights in the list page
				if((enabledadvCatWgt)&&(qtyValue != inputQtyValue))	strNWTQty=""+adjNwgt;
				else	strNWTQty=nwtQty.toString();

				double nwtQtyValue = NumericValidationCCF.parseNumber(strNWTQty);

				if (Double.isNaN(nwtQtyValue))
				{
					String errorMessage = getTextMessage("WMEXP_NON_NUMERIC", new Object[] { nwtLabel },
															state.getLocale());
					state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				if (nwtQtyValue < 0)
				{
					String errorMessage = getTextMessage("WMEXP_QTYGREATEREQUALZERO", new Object[] { nwtLabel },
															state.getLocale());
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}


				Object twtQty = tareWgt1Hash.get(key);

				String strTWTQty="0";

				//check if user input qty not equal to original replenish qty , reassign weights
				//else keep the weights in the list page
				if((enabledadvCatWgt)&&(qtyValue != inputQtyValue))	strTWTQty=""+adjTwgt;
				else	strTWTQty=twtQty.toString();


				double twtQtyValue = NumericValidationCCF.parseNumber(strTWTQty);

				if (Double.isNaN(twtQtyValue))
				{
					String errorMessage = getTextMessage("WMEXP_NON_NUMERIC", new Object[] { twtLabel },
															state.getLocale());
					state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				if (twtQtyValue < 0)
				{
					String errorMessage = getTextMessage("WMEXP_QTYGREATEREQUALZERO", new Object[] { twtLabel },
															state.getLocale());
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}

				if(!(Math.round(gwtQtyValue) == Math.round(nwtQtyValue+twtQtyValue)))
				{
				/*
					String[] errorParam = new String[1];
					errorParam[0]= "null";
					throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", errorParam);
					*/
					String errText="Sum of Net and Tare weights must be equal to Gross Weight";
					String errorMessage = getTextMessage("WMEXP_VALIDATE_ADVCATCHWEIGHTS", new Object[] { errText },
							state.getLocale());
					userContext.put("CAERRORMESSAGE", errorMessage);
					return RET_CONTINUE; //exit
				}


				//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments ends

				/*
				 NSP_CONFIRMREPL
				 ls_str,ls_sku,ls_frmloc,ls_toloc,ls_frmid,ls_toid,ls_lot,ls_refer,ls_packkey,ls_uom,string(li_QtyMoved),string(li_QtyMoved)
				 */
				//prepare sp
				
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(selectedRecord.getValue("STORERKEY").toString()));
				params.add(new TextData(selectedRecord.getValue("SKU").toString()));
				params.add(new TextData(selectedRecord.getValue("FROMLOC").toString()));
				params.add(new TextData(selectedRecord.getValue("TOLOC").toString()));
				params.add(new TextData(selectedRecord.getValue("ID").toString()));
				params.add(new TextData(selectedRecord.getValue("TOID").toString()));
				params.add(new TextData(selectedRecord.getValue("LOT").toString()));
				params.add(new TextData(selectedRecord.getValue("REPLENISHMENTKEY").toString()));
				params.add(new TextData(selectedRecord.getValue("PACKKEY").toString()));
				params.add(new TextData(selectedRecord.getValue("UOM").toString()));
				params.add(new TextData(inputQtyValue));
				params.add(new TextData(inputQtyValue));
				//12/12/2009 sateesh -3PL Enhancements-adv catch weight replenishments starts
				params.add(new TextData(gwtQtyValue));
				params.add(new TextData(nwtQtyValue));
				params.add(new TextData(twtQtyValue));
				//12/12/2009 sateesh-3PL Enhancements- adv catch weight replenishments ends

				for (int i = 1; i <= params.size(); i++)
				{
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + i + " -- +" + params.get(i) + "|\n", SuggestedCategory.NONE);
				}
				
				//run sp
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSP_CONFIRMREPL");
				//		run stored procedure
				EXEDataObject results = null;
				try
				{
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "BEFORE" + "\n", SuggestedCategory.NONE);
					results = WmsWebuiActionsImpl.doAction(actionProperties);
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "AFTER" + "\n", SuggestedCategory.NONE);
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\n\t" + "Results " + results.getColumnCount() + "\n", SuggestedCategory.NONE);
					if (results.getColumnCount() != 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "---Results " + results.getColumnCount(), SuggestedCategory.NONE);
						for (int i = 1; i <= results.getColumnCount(); i++)
						{

							_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", " " + i + " @ " + results.getAttribute(i).name + " "+ results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);

						}
					}
				} catch (WebuiException e)
				{
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", "\t\t" + e.getMessage(), SuggestedCategory.NONE);
					//throw new UserException(e.getMessage(), new Object[] {});
					_log.debug("LOG_DEBUG_EXTENSION_ConfirmAction", ctx.getSourceForm().getName(), SuggestedCategory.NONE);
					//Set Error Message into Session
					//state.getRequest().getSession().setAttribute("CAERRORMESSAGE", e.getMessage());
					userContext.put("CAERRORMESSAGE", e.getMessage());
					return RET_CONTINUE; //exit
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//results
			}
			listForm.setSelectedItems(null);
			listForm.setSelectedItems(new ArrayList());

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			//state.getRequest().getSession().removeAttribute(CAINPUTQTY);
			userContext.remove(CAINPUTQTY);
			return RET_CANCEL;
		}
		//state.getRequest().getSession().removeAttribute(CAINPUTQTY);
		userContext.remove(CAINPUTQTY);
		
		//Letting everyone know the confirm is finished
		UserContextUtil.setInteractionUserContextAttribute(CONFIRM_COMPLETED, true, state);
		
		return RET_CONTINUE;
	}

	public String readLabel(RuntimeFormWidgetInterface widget)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label", locale);
	}

	boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	boolean isZero(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (((ArrayList) attributeValue).size() == 0)
		{
			return false;
		}
		else
		{
			return false;
		}
	}

}
