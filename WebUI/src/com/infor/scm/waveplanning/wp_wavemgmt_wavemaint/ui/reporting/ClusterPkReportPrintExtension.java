package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.reporting;

import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
/**
 * 集群拣货单
 */
@SuppressWarnings("unchecked")
public class ClusterPkReportPrintExtension extends ActionExtensionBase {
	private static final String SLOT = "SLOT";
	private static final String WAVEKEY = "WAVEKEY";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		
		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
		(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		String waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();

		HashMap<String, String> parametersAndValues = new HashMap<String, String>();
		
		parametersAndValues.put("V_WAVEKEY", waveKey);
		
		String rep_id = "CRPT87";

		StringBuffer reportURL = new StringBuffer();
		
		reportURL.append(ReportUtil.retrieveReportURLStart(state, rep_id));
		reportURL.append(ReportUtil.retrieveReportURLEnd(state, parametersAndValues));
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		
		userCtx.put(ReportUtil.REPORTURL, reportURL.toString());
		
		return RET_CONTINUE;
	}
}