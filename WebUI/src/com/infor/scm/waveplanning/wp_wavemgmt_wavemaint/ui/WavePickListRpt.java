package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

import java.util.ArrayList;
import java.util.Properties;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing.LabelPrintingFactory;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing.LabelPrintingUIExtension;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing.PrintLabelInputObj;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing.PrintLabelInterface;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_waveplanning.PropertyUtil;

public class WavePickListRpt extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LabelPrintingUIExtension.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_Wave_Label_Printing_execute", "Entering LabelPrintingUIExtension",
				SuggestedCategory.NONE); 
		String printingType = (String)getParameter("PrintingType");
		
		StateInterface state = context.getState();

		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		String waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		
		Properties props = System.getProperties();
		String filePathUrl = props.getProperty("jboss.server.config.url");
		String filePath = filePathUrl.substring(filePathUrl.indexOf("/")+1)+"customize-url.properties";
		String baseURL = PropertyUtil.readValue(filePath, "Wave_Pick_Report");
		
		String currentConnection = context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String userName = context.getState().getUser().getName();
		String wmWhseID = (userCtx.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		
		String url = baseURL+"?dataSource="+currentConnection+"&userid=" + userName+ "&whseid="+wmWhseID + "&wavekey="+waveKey;
		userCtx.put(ReportUtil.REPORTURL, url);
		
		return RET_CONTINUE;
	}
}
