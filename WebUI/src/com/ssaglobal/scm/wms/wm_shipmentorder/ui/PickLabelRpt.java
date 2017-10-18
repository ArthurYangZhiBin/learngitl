package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.ReportUtil;

@SuppressWarnings("unchecked")
public class PickLabelRpt extends ActionExtensionBase {
	private static final String SLOT = "SLOT";
	private static final String ORDERKEY = "ORDERKEY";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		
		String headerSlot = getParameter(SLOT) == null ? "list_slot_1" : getParameterString(SLOT);
		
		DataBean focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(headerSlot), null).getFocus();
		
		String orderKey = (String) focus.getValue(ORDERKEY);
		
		HashMap<String, String> parametersAndValues = new HashMap<String, String>();
		
		parametersAndValues.put("orderkey", orderKey);
		
		String rep_id = "CRPT94";

		StringBuffer reportURL = new StringBuffer();
		
		reportURL.append(ReportUtil.retrieveReportURLStart(state, rep_id));
		reportURL.append(ReportUtil.retrieveReportURLEnd(state, parametersAndValues));
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		
		userCtx.put(ReportUtil.REPORTURL, reportURL.toString());
		
		return RET_CONTINUE;
	}
}