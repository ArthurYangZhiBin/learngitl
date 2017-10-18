package com.ssaglobal.scm.wms.wm_wave_labels.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;

@SuppressWarnings("unchecked")
public class WavePickLabel extends ActionExtensionBase {
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();

		ArrayList<String> tabList = new ArrayList<String>();
		
		tabList.add("tab1");
		
		RuntimeNormalFormInterface waveHeaderForm = (RuntimeNormalFormInterface) WPFormUtil.findForm(state.getCurrentRuntimeForm(), 
				"wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1", tabList, state);
		
		String wavekey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
		
		HashMap<String, String> parametersAndValues = new HashMap<String, String>();
		
		parametersAndValues.put("wavekey", wavekey);
		
		String rep_id = "CRPT95";

		StringBuffer reportURL = new StringBuffer();
		
		reportURL.append(ReportUtil.retrieveReportURLStart(state, rep_id));
		reportURL.append(ReportUtil.retrieveReportURLEnd(state, parametersAndValues));
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		
		userCtx.put(ReportUtil.REPORTURL, reportURL.toString());
		
		return RET_CONTINUE;
	}
}