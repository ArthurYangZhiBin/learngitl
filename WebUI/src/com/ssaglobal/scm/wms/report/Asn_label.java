package com.ssaglobal.scm.wms.report;


import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_waveplanning.PropertyUtil;

public class Asn_label extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		RuntimeFormInterface searchForm;
		StateInterface state = context.getState();
		
		searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "receipt_detail_view", state);

		String receiptkey = searchForm.getFormWidgetByName("RECEIPTKEY").getDisplayValue();
		
		HashMap parametersAndValues = new HashMap();
		
		parametersAndValues.put("asnno", receiptkey);
		
		String rep_id=getParameterString("report_type");
		
		if(rep_id!=null)
		{
			/*
			 * Asn_checkticket
				Asn_item_list
				Asn_item_labelByItem
				Asn_item_labelByNum
				Asn_bom_label
			 */
			if(("Asn_checkticket").equals(rep_id))
			{
				rep_id="CRPT89";
			}else if(("Asn_item_list").equals(rep_id))
			{
				rep_id="CRPT90";
			}else if(("Asn_item_labelByItem").equals(rep_id))
			{
				rep_id="CRPT91";
			}else if(("Asn_item_labelByNum").equals(rep_id))
			{
				//检查是否全收
				String validateAllocate=" select sum(r.qtyexpected),sum(r.qtyreceived) from receiptdetail r where r.receiptkey='"+receiptkey+"'";
        
                double qtyexpected=0;
                double qtyreceived=0;
                EXEDataObject results=null;
				try {
					results = WmsWebuiValidationSelectImpl.select(validateAllocate);
					
					if(results != null  && results.getRowCount()> 0)
					{
						qtyexpected=Double.parseDouble(results.getAttribValue(1).getAsString());
						qtyreceived=Double.parseDouble(results.getAttribValue(2).getAsString());
					}
				} catch (DPException e) {
					throw new UserException("", e.getMessage());
				}
				
				
				if(qtyexpected!=qtyreceived)
				{
					throw new UserException("", "请先完成所有货品收货");
				}
				
				EpnyUserContext userCtx = context.getServiceManager().getUserContext();
				Properties props = System.getProperties();
				String filePathUrl = props.getProperty("jboss.server.config.url");
				String filePath = filePathUrl.substring(filePathUrl.indexOf("/")+1)+"customize-url.properties";
				String checkURL="";
				List<List> list = PropertyUtil.readProperties(filePath);
				for(List listDetail:list){
					String key = listDetail.get(0).toString();
					if(key.equals("asnprint")){
						checkURL=listDetail.get(1).toString();
						break;
					}
				}
				String currentConnection = context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
				String userName = context.getState().getUser().getName();
				String wmWhseID = (userCtx.get(SetIntoHttpSessionAction.DB_USERID)).toString();
				String url=checkURL+"?dataSource="+currentConnection+"&userid=" + userName+ 
				"&whseid="+wmWhseID+"&receiptkey="+receiptkey;
				userCtx.put(ReportUtil.REPORTURL, url);
				return RET_CONTINUE;
				
			}else if(("Asn_bom_label").equals(rep_id))
			{
				rep_id="CRPT93";
			}
			StringBuffer reportURL = new StringBuffer();
			reportURL.append(ReportUtil.retrieveReportURLStart(state, rep_id));
			reportURL.append(ReportUtil.retrieveReportURLEnd(state, parametersAndValues));
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userCtx.put(ReportUtil.REPORTURL, reportURL.toString());
			
		}
		
		//String reportURL = ReportUtil.retrieveReportURL(state, "CRPT89", parametersAndValues);
		
		//reportURL.append(ReportUtil.retrieveReportURLStart(state, "CRPT89"));
		//EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		//userCtx.put(ReportUtil.REPORTURL, reportURL);
		
		
		return RET_CONTINUE;
	}
}
