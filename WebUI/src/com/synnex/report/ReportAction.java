/**
 * 
 */
package com.synnex.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.UserUtil;

/**
 * @author jacobx
 * 
 */
public class ReportAction  extends ActionExtensionBase  {

	@Override
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {
		// TODO Auto-generated method stub
		//YSHD RKD 预收货单|入库单->ASN  SJJY->上架建议
		//JHD 拣货单->波次维护  PK->提总单  RP--补货单
		//FHD 发货单->出库单  
		//YSHDSH 预收货单上海
		String reportName = getParameterString("ReportName");
		StateInterface state = context.getState();
		String userid = state.getUser().getName();
		String currentConnection = state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		String contextPath = context.getState().getRequest().getScheme()+"://"+context.getState().getRequest().getServerName() + ":10000/sreport/preview.do";
		//StringBuffer reportURL = new StringBuffer(contextPath+"?data={reportPath:\""+reportName+".jasper\", jndi:\""+currentConnection.toUpperCase()+"\", previewType:\"PDF\", data:[{");
		StringBuffer reportURL = new StringBuffer("{reportPath:\""+reportName+".jasper\", jndi:\""+currentConnection.toUpperCase()+"\", previewType:\"PDF\", data:[{");
		boolean isBirt = false;
		HashMap<String, String> parametersAndValues = new HashMap<String, String>();
		if ("YSHD".equals(reportName) || "RKD".equals(reportName) || "SJJY".equals(reportName) || "FHD".equals(reportName)||"YSHDSH".equals(reportName)||"JHDORDERSH-010".equals(reportName.trim())) {
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			DataBean headerFocus = headerForm.getFocus();
			if ("YSHD".equals(reportName) || "RKD".equals(reportName) || "SJJY".equals(reportName)||"YSHDSH".equals(reportName)) {
				String receiptkeys = "";
				if (headerFocus instanceof BioCollection) {
					ArrayList<BioBean> selectedReceipts = ((RuntimeListFormInterface) (headerForm)).getSelectedItems();
					if (selectedReceipts != null && selectedReceipts.size() > 0) {
						for (BioBean r : selectedReceipts) {
							//reportURL.append(BioAttributeUtil.getString(r, "RECEIPTKEY"));
							receiptkeys = receiptkeys + "'"+ BioAttributeUtil.getString(r, "RECEIPTKEY") + "',";
						}
						receiptkeys = receiptkeys.substring(0, receiptkeys.length() - 1);
					}
					else
					{
						throw new UserException("WMEXP_NONE_SELECTED", new Object[]{});
					}
					
				} else {
					BioBean headerBio = (BioBean) headerFocus;
					receiptkeys = headerBio.get("RECEIPTKEY").toString();
				}
				reportURL.append("receiptkey:\""+receiptkeys + "\"");
			} else if ("FHD".equals(reportName)||"JHDORDERSH-010".equals(reportName.trim())){
				String orderkeys = "";
				System.out.println("热进来了："+reportName);
				if (headerFocus instanceof BioCollection) {
					ArrayList<BioBean> selectedReceipts = ((RuntimeListFormInterface) (headerForm)).getSelectedItems();
					if (selectedReceipts != null && selectedReceipts.size() > 0) {
						for (BioBean r : selectedReceipts) 
							orderkeys = orderkeys + "'"+ BioAttributeUtil.getString(r, "ORDERKEY") + "',";
						orderkeys = orderkeys.substring(0, orderkeys.length() - 1);
					}
					else
					{
						throw new UserException("WMEXP_NONE_SELECTED", new Object[]{});
					}
					
				} else {
					BioBean headerBio = (BioBean) headerFocus;
					orderkeys = headerBio.get("ORDERKEY").toString();
				}
				System.out.println("orderkeys"+orderkeys);
				reportURL.append("orderkey:\""+orderkeys + "\"");
			} 
		} else if ("JHD".equals(reportName) || "BAU0002".equals(reportName) || "BAU0001".equals(reportName)||"JHDWAVESH-010".equals(reportName)||"WAVEFHD".equals(reportName)){
			ArrayList tabList = new ArrayList();
			tabList.add("tab1"); 
			RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
			(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
			String wavekey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
			System.out.println("wavekey：："+wavekey);
			if ("JHD".equals(reportName)||"JHDWAVESH-010".equals(reportName)||"WAVEFHD".equals(reportName))
				reportURL.append("wavekey:\""+wavekey+"\"");
			else {
				parametersAndValues.put("v_wavekey", wavekey);
				isBirt = true;
			}
		} 
		if (isBirt) {
			String url= ReportUtil.retrieveReportURL(state,reportName, parametersAndValues);
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userCtx.put("REPORTURL",url);
			return RET_CONTINUE;
			
		}
		reportURL.append(",userid:\""+userid+"\",vr:\""+System.currentTimeMillis()+"\"}]}");
		
		try {
			SsaAccessBase appAccess = new SQLDPConnectionFactory();
			String oahome = appAccess.getValue("webUIConfig", "OAHome");
			String fileSeparator = System.getProperties().getProperty("file.separator");
			String path = oahome + fileSeparator + "shared" + fileSeparator + "webroot" + fileSeparator + "app";
			String fileName = UserUtil.getUserId(state) + "_" + reportName + "_" + System.currentTimeMillis() + ".html";
			File htmlFile = new File(path + fileSeparator + "jfreechartHTMLTmp" + fileSeparator + fileName);
			OutputStream outHtml = new BufferedOutputStream(new FileOutputStream(htmlFile));
			PrintWriter writer = new PrintWriter(outHtml);
			writer.println("<HTML>");
			writer.println("<body onload=\"submitForm()\">");
			writer.println("<form method=\"post\" action=\"" + contextPath + "\" name=\"rptHiddenForm\" id=\"rptHiddenForm\">");
			writer.println("<textarea name=\"data\" id=\"data\" style=\"display:none;\">" + reportURL +"</textarea>");
			writer.println("</form>");
			writer.println("<script type='text/javascript'>document.rptHiddenForm.submit();</script>");
			writer.println("</body>");
			writer.println("</HTML>");

			writer.flush();
			writer.close();

			outHtml.close();
			EpnyUserContext userCtx = context.getServiceManager().getUserContext();
			userCtx.put(ReportUtil.REPORTURL, "jfreechartHTMLTmp" + fileSeparator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RET_CONTINUE;
	}
	
	
	
}
