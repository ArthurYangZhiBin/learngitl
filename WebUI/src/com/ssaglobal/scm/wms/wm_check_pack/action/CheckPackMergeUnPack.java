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


package com.ssaglobal.scm.wms.wm_check_pack.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackResults;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackMergeUnPack extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String UOM_EA = "6";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackMergeUnPack.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface picksForm = FormUtil.findForm(toolbar, "wm_check_pack_shell", "wm_check_pack_results_pickdetail_form", state);
		if (picksForm != null && picksForm instanceof RuntimeListFormInterface) {
			RuntimeListFormInterface picksList = (RuntimeListFormInterface) picksForm;

			ArrayList<BioBean> selectedItems = picksList.getSelectedItems();

			if (selectedItems == null) {
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
			for (int i = 0; i < selectedItems.size(); i++) {
				//				BioBean pick = picksFocus.get("" + i);
				BioBean pick = selectedItems.get(i);

				String pickDetailKey = BioAttributeUtil.getString(pick, "PICKDETAILKEY");

				//				if (updatedAttributes.contains("QTY")) {
				//UOM Validation
				//UOM 6 = Each

				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				String uom = BioAttributeUtil.getString(pick, "UOM");
				String pack = getPack(BioAttributeUtil.getString(pick, "STORERKEY"), BioAttributeUtil.getString(pick, "SKU"), tuow);
				double qtyToUnPack = BioAttributeUtil.getDouble(pick, "QTY");
				double qty = BioAttributeUtil.getDouble(pick, "QTY", true);
				_log.debug("LOG_DEBUG_EXTENSION_CheckPackMergeUnPack_execute", "UOM for " + pickDetailKey + " = " + uom + " Pack " + pack, SuggestedCategory.NONE);
				if (qtyToUnPack == qty) {
					//You can unpack everything
					unpackPick(pickDetailKey, pick);
				} else {
					if (UOM_EA.equals(uom)) {
						//You can unpack any qty of eaches
						unpackPick(pickDetailKey, pick);
					}

					//if the qty to unpack is a multiple of the uom value
					//ex, 
					//PACK = 1-6-72
					//UOM = CSE
					//QTY = 18
					//QTYTOUNPACK = 6 or 12
					//that is allowed
					else if (qtyToUnpackIsMultipleOfUOMQty(qtyToUnPack, uom, pack, pick, tuow)) {
						unpackPick(pickDetailKey, pick);
					} else {
						throw new UserException("WMEXP_CHECK_PACK_MERGE_EA", new Object[] {});
					}
				}

				//				if (UOM_EA.equals(uom)) {
				//					unpackPick(pickDetailKey, pick);
				//				} else {
				//					throw new UserException("WMEXP_CHECK_PACK_MERGE_EA", new Object[] {});
				//				}
				//				} else {
				//					_log.debug("LOG_DEBUG_EXTENSION_CheckPackMergeUnPack_execute", "Skipping " + pickDetailKey + "with updated " + updatedAttributes.toString(), SuggestedCategory.NONE);
				//				}
			}
			picksList.setSelectedItems(null);

			RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface searchForm = FormUtil.findForm(currentRuntimeForm, "wm_check_pack_shell", "wm_check_pack_search_form", state);
			String container = searchForm.getFormWidgetByName("CONTAINER").getDisplayValue().toUpperCase();
			CheckPackResults results = CheckPackUtil.search(context, container);
			if (results.isFoundResults() == true) {
				context.setNavigation("results");
				result.setFocus(results.getPickDetails());
			} else {
				//throw error
				//blank out forms
				context.setNavigation("noresults");

				//SHow Message
				//			form.getFormWidgetByName(errorWidgetName).setLabel(RuntimeFormWidgetInterface.LABEL_VALUE, errorMessage);
				//			currentRuntimeForm.setError("WMEXP_CHECK_PACK_NO_CONTAINER", new Object[] { container });
				if (results.getType() == null) {
					setError(context, searchForm, "WMEXP_CHECK_PACK_NO_CONTAINER", new Object[] { container });
				} else {
					setError(context, searchForm, "WMEXP_CHECK_PACK_EMPTY_CONTAINER", new Object[] { container });
				}
			}
		}





		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate


		return RET_CONTINUE;
	}

	private void setError(ActionContext context, RuntimeFormInterface currentRuntimeForm, String error, Object[] parameters) {
		SlotInterface errorSlot = currentRuntimeForm.getSubSlot("error");
		RuntimeFormInterface errorForm = context.getState().getRuntimeForm(errorSlot, null);
		if (errorForm != null) {
			errorForm.setError(error, parameters);
		}
	}

	private boolean qtyToUnpackIsMultipleOfUOMQty(Double qtyToUnPack, String uom, String pack, BioBean pick, UnitOfWorkBean uow) throws EpiDataException {
		//get UOM Value

		//get Unit QTY
		Double uomQty = getUOMQty(pack, uom, uow);

		if ((qtyToUnPack.intValue() % uomQty.intValue()) == 0) {
			//qtyToUnPack is a multiple
			return true;
		}

		return false;
	}

	private double getUOMQty(String pack, String uom, UnitOfWorkBean uow) throws EpiDataException {

		double uomqty;

		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_pack", "wm_pack.PACKKEY = '" + pack + "'", null));

		double casecnt = 0;
		double pallet = 0;
		double cube = 0;
		double grosswgt = 0;
		double netwgt = 0;
		double otherunit1 = 0;
		double otherunit2 = 0;
		double innerpack = 0;
		String packuom1 = null;
		String packuom2 = null;
		String packuom3 = null;
		String packuom4 = null;
		String packuom5 = null;
		String packuom6 = null;
		String packuom7 = null;
		String packuom8 = null;
		String packuom9 = null;

		for (int i = 0; i < rs.size(); i++) {
			BioBean bio = rs.get("" + i);
			casecnt = BioAttributeUtil.getDouble(bio, "CASECNT");
			pallet = BioAttributeUtil.getDouble(bio, "PALLET");
			cube = BioAttributeUtil.getDouble(bio, "CUBE");
			grosswgt = BioAttributeUtil.getDouble(bio, "GROSSWGT");
			netwgt = BioAttributeUtil.getDouble(bio, "NETWGT");
			otherunit1 = BioAttributeUtil.getDouble(bio, "OTHERUNIT1");
			otherunit2 = BioAttributeUtil.getDouble(bio, "OTHERUNIT2");
			innerpack = BioAttributeUtil.getDouble(bio, "INNERPACK");
			packuom1 = BioAttributeUtil.getString(bio, "PACKUOM1");
			packuom2 = BioAttributeUtil.getString(bio, "PACKUOM2");
			packuom3 = BioAttributeUtil.getString(bio, "PACKUOM3");
			packuom4 = BioAttributeUtil.getString(bio, "PACKUOM4");
			packuom5 = BioAttributeUtil.getString(bio, "PACKUOM5");
			packuom6 = BioAttributeUtil.getString(bio, "PACKUOM6");
			packuom7 = BioAttributeUtil.getString(bio, "PACKUOM7");
			packuom8 = BioAttributeUtil.getString(bio, "PACKUOM8");
			packuom9 = BioAttributeUtil.getString(bio, "PACKUOM9");
		}

		if (uom.equalsIgnoreCase(packuom1) ||
			((uom != null) &&
			uom.equals("2"))) {
			uomqty = casecnt;

		} else if ((uom.equalsIgnoreCase(packuom2) || ((uom != null) && uom.equals("3")))) {
			uomqty = innerpack;

		} else if ((uom.equalsIgnoreCase(packuom3) ||
					((uom != null) &&
					uom.equals("6")) || ((uom != null) && uom.equals("7")))) {
			uomqty = 1;

		} else if ((uom.equalsIgnoreCase(packuom4) || ((uom != null) && uom.equals("1")))) {
			uomqty = pallet;

		} else if ((uom.equalsIgnoreCase(packuom5))) {
			uomqty = cube;

		} else if ((uom.equalsIgnoreCase(packuom6))) {
			uomqty = grosswgt;

		} else if ((uom.equalsIgnoreCase(packuom7))) {
			uomqty = netwgt;

		} else if ((uom.equalsIgnoreCase(packuom8) || ((uom != null) && uom.equals("4")))) {
			uomqty = otherunit1;

		} else if ((uom.equalsIgnoreCase(packuom9) || ((uom != null) && uom.equals("5")))) {
			uomqty = otherunit2;

		} else {
			uomqty = 1;
		}
		return uomqty;
	}

	private String getPack(String storer, String sku, UnitOfWorkBean uow) throws EpiDataException {
		String pack = "STD";
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + storer + "' and sku.SKU = '" + sku + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			pack = BioAttributeUtil.getString(rs.get("" + i), "PACKKEY");
		}
		return pack;
	}

	private void unpackPick(String pickDetailKey, BioBean pick) throws UserException {
		_log.debug("LOG_DEBUG_EXTENSION_CheckPackMergeUnPack_execute", "Processing " + pickDetailKey, SuggestedCategory.NONE);
		String toid = BioAttributeUtil.getString(pick, "TOID");
		String toidtype = BioAttributeUtil.getString(pick, "TOIDTYPE");
		double qty = BioAttributeUtil.getDouble(pick, "QTY");

		if (StringUtils.isEmpty(toid) || StringUtils.isEmpty(toidtype)) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackMergeUnPack_unpackPick", "TOID and TOIDTYPE are required values", SuggestedCategory.NONE);
			throw new UserException("WMEXP_CHECK_PACK_MERGE_REQ_FIELDS", new Object[] {});
		}

		toid = toid.toUpperCase();

		_log.debug("LOG_DEBUG_EXTENSION_CheckPackMergeUnPack_execute", "\t " + toid + " " + toidtype + " " + qty, SuggestedCategory.NONE);

		//pickdetailkey,dropid,cartontype,qty
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData(pickDetailKey));
		params.add(new TextData(toid));
		params.add(new TextData(toidtype));
		params.add(new TextData(qty));
		for (int j = 0; j < params.size(); j++) {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackProceed_execute", j + " " + params.get(j), SuggestedCategory.NONE);
		}
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("SplitCaseID");
		EXEDataObject spResult = null;
		try {
			spResult = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is
	 * fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

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
