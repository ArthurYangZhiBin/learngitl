package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.ArrayList;
import java.util.Properties;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_waveplanning.PropertyUtil;

public class PackListSummaryRpt extends com.epiphany.shr.ui.action.ActionExtensionBase  {

	//装箱汇总单报表
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		String orderkey = "", wavekey = "";
		
		try{
			String headerSlot = getParameter("SLOT") == null ? "list_slot_1" : getParameterString("SLOT");
			DataBean focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(headerSlot), null).getFocus();
			orderkey = (String) focus.getValue("ORDERKEY");
		} catch(Exception e){
			ArrayList tabList = new ArrayList();
			tabList.add("tab1");
			RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
								(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
			wavekey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
		}
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		
		Properties props = System.getProperties();
		String filePathUrl = props.getProperty("jboss.server.config.url");
		String filePath = filePathUrl.substring(filePathUrl.indexOf("/")+1)+"customize-url.properties";
		String baseURL = PropertyUtil.readValue(filePath, "Pack_List_Summary_Report");
		
		String currentConnection = context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String userName = context.getState().getUser().getName();
		String wmWhseID = (userCtx.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		
		String url = baseURL+"?dataSource="+currentConnection+"&userid=" + userName+ "&whseid="+wmWhseID;
		if(orderkey!=null && !"".equals(orderkey)){
			url += "&orderkey="+orderkey;
		}else{
			url += "&wavekey="+wavekey;
		}
		userCtx.put(ReportUtil.REPORTURL, url);
		return RET_CONTINUE;
	}
	
}
