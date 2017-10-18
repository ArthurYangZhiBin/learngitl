package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.List;
import java.util.Properties;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_waveplanning.PropertyUtil;

public class WxjhdRpt extends com.epiphany.shr.ui.action.ActionExtensionBase {

	//外向交货单报表
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		String headerSlot = getParameter("SLOT") == null ? "list_slot_1" : getParameterString("SLOT");
		DataBean focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(headerSlot), null).getFocus();
		
		String orderKey = (String) focus.getValue("ORDERKEY");
		
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		Properties props = System.getProperties();

		String filePathUrl = props.getProperty("jboss.server.config.url");
		String filePath = filePathUrl.substring(filePathUrl.indexOf("/")+1)+"customize-url.properties";
		String baseURL = PropertyUtil.readValue(filePath, "WXJHD_Report");
		
		
		String currentConnection = context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String userName = context.getState().getUser().getName();
		String wmWhseID = (userCtx.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String url = baseURL+"?dataSource="+currentConnection+"&userid=" + userName+ "&whseid="+wmWhseID + "&orderkey="+orderKey;
		userCtx.put(ReportUtil.REPORTURL, url);
		return RET_CONTINUE;
		
	}
	
}
