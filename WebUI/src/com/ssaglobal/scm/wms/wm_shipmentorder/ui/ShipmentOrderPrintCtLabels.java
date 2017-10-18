package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class ShipmentOrderPrintCtLabels extends com.epiphany.shr.ui.action.ActionExtensionBase{
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
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int execute(ModalActionContext context, ActionResult result)
			throws EpiException {
		
		StateInterface state = context.getState();
		RuntimeFormInterface headerForm = FormUtil.findForm(context.getSourceForm(), "", "wm_shipmentorder_header_view",
				state);
		DataBean focus = headerForm.getFocus();
		String order = BioAttributeUtil.getString(focus, "ORDERKEY"); 
		if (StringUtils.isEmpty(order)) {
			return RET_CANCEL;
		}

		String printerName = context.getSourceWidget().getForm().getFormWidgetByName("printers").getDisplayValue();
		String numberOfCopies = context.getSourceWidget().getForm().getFormWidgetByName("copies").getDisplayValue();
		// Set parameters for stored procedure
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData("ORD")); // keyType
		params.add(new TextData(order)); // theKey
		params.add(new TextData(printerName)); // printername
		params.add(new TextData("")); // rfidprintername
		params.add(new TextData(numberOfCopies)); // copies
		params.add(new TextData("")); // assignment   gfgfd

		// Initiate stored procedure
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("NSPPrintWaveLabel");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			throw new UserException(e.getMessage(), new Object[] {});
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}
}
