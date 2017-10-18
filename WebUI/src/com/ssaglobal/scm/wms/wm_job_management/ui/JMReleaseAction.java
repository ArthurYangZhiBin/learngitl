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
package com.ssaglobal.scm.wms.wm_job_management.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class JMReleaseAction extends ListSelector {
	private final String LISTVIEW = "closeModalDialog72";	
	private final String DETAILVIEW = "closeModalDialog71";	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JMReleaseAction.class);
	
	@Override
	protected int execute( ModalActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = context.getSourceForm(); 
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ shellForm.getName(),100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		if (headerFocus instanceof BioCollection){
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Release Job\n\n",100L);
			//Executes SP name:NSPRECEIPTCLOSE params:RECEIPTKEY
   
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)headerForm;
			ArrayList selectedItems = headerListForm.getSelectedItems();
			
			
			if(selectedItems != null && selectedItems.size() > 0)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
				Iterator bioBeanIter = selectedItems.iterator();

				BioBean bio=null;
				try
				{
					for(; bioBeanIter.hasNext();){
						_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
						bio = (BioBean)bioBeanIter.next();
						String groupKey = bio.getString("GROUPID");
						_log.debug("LOG_SYSTEM_OUT","\n\nGROUPID:"+groupKey+"\n\n",100L);
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(groupKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("nspwcorderprocess");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
				 	   		throw UsrExcp;
						}
//						if (checkRouteops(bio)){
							bio.set("ISRELEASED","1");
							bio.set("STATUS","1");
//						}
					}
					uowb.saveUOW();
					CheckAndPrintReports(context, bio);
					result.setSelectedItems(null);
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
				result.setFocus(headerFocus);
			}
		}else{
			_log.debug("LOG_SYSTEM_OUT","I am on a detail form",100L);
			callActionFromNormalForm(headerFocus);
			uowb.saveUOW();
			CheckAndPrintReports(context, (BioBean)headerFocus);
			result.setFocus(headerFocus);

		}
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
		return RET_CONTINUE;
		
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ActionContext ctx, ActionResult args) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Wrong Action1 \n\n",100L);
		try {
			// Add your code here to process the event
			
		} catch(Exception e) {
			
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		
		return RET_CONTINUE;
	}	
	private void callActionFromNormalForm(DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
				BioBean headerBio = (BioBean)headerFocus;
				String groupKey = headerBio.getValue("GROUPID").toString();
				_log.debug("LOG_SYSTEM_OUT","\n\nGROUPID:"+groupKey+"\n\n",100L);
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				parms.add(new TextData(groupKey));
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("nspwcorderprocess");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (Exception e) {
					e.getMessage();
					UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
		 	   		throw UsrExcp;
				}
//				if (checkRouteops(headerBio)){
					headerBio.set("ISRELEASED","1");
					headerBio.set("STATUS","1");
//				}
		}
	}
	public boolean checkRouteops(BioBean bio)throws EpiException{

		BioCollectionBean SelChildBiocollection = (BioCollectionBean)bio.get("WORKORDER");
		int i;
		for (i=0; i<SelChildBiocollection.size(); i++){
			
			BioBean WorkOrderbioBean = (BioBean)SelChildBiocollection.elementAt(i);
			BioCollectionBean RouteOpsBioCollection = (BioCollectionBean)WorkOrderbioBean.get("ROUTEOPS");
			int allCount = RouteOpsBioCollection.size();		//count of all the routops records for this workorderid
			_log.debug("LOG_SYSTEM_OUT","ALL Count = "+ allCount,100L);
			String sQueryString = "(wm_routeops.STATUS = '98')";
			_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
			Query BioQuery = new Query("wm_routeops",sQueryString,null);
			BioCollection filterBioCollection = RouteOpsBioCollection.filter(BioQuery);
			int completedCount = filterBioCollection.size();
			_log.debug("LOG_SYSTEM_OUT","COMPLETED Count = "+ completedCount,100L);
			if (allCount == completedCount){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	public void CheckAndPrintReports(ActionContext context, BioBean bio)throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","Inside Print reports",100L);
		StateInterface state = context.getState();
		HttpSession session =context.getState().getRequest().getSession();
		BioCollectionBean globalPrefCollection = null;
		String FieldValue = "";
		boolean printPickList = false;
		boolean printWorkorderPacket = false;
		boolean printWorkorderBom = false;
		boolean printWorkorderInstruction = false;
		boolean printOperationPacket = false;
		boolean printOperationInstruction = false;
		
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		String pStartDate= "";
		
		if(ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT))
		{
			pStartDate = "01/01/1900";
		}
		else
		{
			pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
		}

				
		
		Query BioQuery = new Query("wm_global_preferences",null,null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		globalPrefCollection = uow.getBioCollectionBean(BioQuery);
		int j;
		for (j=0; j<globalPrefCollection.size(); j++){
			BioBean globalPrefBio = (BioBean)globalPrefCollection.elementAt(j);
			String globalPrefKey = globalPrefBio.getValue("GLOBALPREFKEY").toString();
			String globalPrefValue = globalPrefBio.getValue("VALUE").toString();
			if (globalPrefKey.equalsIgnoreCase("PRINT_PICK_LIST") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printPickList = true;
			}
			if (globalPrefKey.equalsIgnoreCase("PRINT_WORKORDER_PACKET") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printWorkorderPacket = true;
			}
			if (globalPrefKey.equalsIgnoreCase("PRINT_WORKORDER_BOM") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printWorkorderBom = true;
			}
			if (globalPrefKey.equalsIgnoreCase("PRINT_WORKORDER_INSTRUCTIONS") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printWorkorderInstruction = true;
			}
			if (globalPrefKey.equalsIgnoreCase("PRINT_OPERATION_PACKET") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printOperationPacket = true;
			}
			if (globalPrefKey.equalsIgnoreCase("PRINT_OPERATION_INSTRUCTIONS") && globalPrefValue.equalsIgnoreCase("TRUE")){
				printOperationInstruction = true;
			}
		}
		if (printPickList){
			String groupKey = bio.getString("GROUPID");
			String query = "Select min(orderkey) MIN, max(orderkey) MAX  from opxshipord where operationid in (Select operationid from routeops where status <> '98' and workorderid in (Select workorderid from workorder where groupid = '" + groupKey + "'))";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			String startOrder = results.getAttribValue(1).getAsString();
			String endOrder = results.getAttribValue(2).getAsString();
			_log.debug("LOG_SYSTEM_OUT","Start Order = "+ startOrder,100L);
			_log.debug("LOG_SYSTEM_OUT","End Order = "+ endOrder,100L);
			
			
			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
			
			Date currentDateTime = new Date(System.currentTimeMillis());
			String pDate = "";
			if (ReportUtil.isBIRT(state) == true) {
				pDate = dateFormat.format(currentDateTime);
			} else {
				pDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
			}
			
			
			
			HashMap paramsAndValues = new HashMap();
			paramsAndValues.put("p_OrdStart",startOrder);
			paramsAndValues.put("p_OrdEnd", endOrder);
			paramsAndValues.put("p_WaveStart",  "0");
			paramsAndValues.put("p_WaveEnd", "zzzzzzzzzzzzzzz");
			paramsAndValues.put("p_AssignStart","0");
			paramsAndValues.put("p_AssignEnd","zzzzzzzzzzzzzzz");
			paramsAndValues.put("p_pDetails","Printing by Order");
			paramsAndValues.put("p_pDate",pDate);
			
			String report_url= ReportUtil.retrieveReportURL(state,"CRPT54", paramsAndValues);
			session.setAttribute("PickListReportURL", report_url);

		}
		if (printWorkorderPacket){
			BioCollectionBean WorkOrdercollection = (BioCollectionBean)bio.get("WORKORDER");
			if (WorkOrdercollection != null){
				String startWorkOrder = WorkOrdercollection.min("WORKORDERKEY").toString();
				String endWorkOrder = WorkOrdercollection.max("WORKORDERKEY").toString();

				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");				
				//String pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
				
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pEndDate = "";
				if (ReportUtil.isBIRT(state) == true) {
					pEndDate = dateFormat.format(currentDateTime);
				} else {
					pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
				}
				
				
				HashMap paramsAndValues = new HashMap();
				paramsAndValues.put("p_woStart",startWorkOrder);
				paramsAndValues.put("p_woEnd", endWorkOrder);
				paramsAndValues.put("p_pStartDate", pStartDate);
				paramsAndValues.put("p_pEndDate", pEndDate);
				
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT74", paramsAndValues);
				session.setAttribute("printWorkorderPacketURL", report_url);
			}
		}
		if (printWorkorderBom){
			BioCollectionBean WorkOrdercollection = (BioCollectionBean)bio.get("WORKORDER");
			if (WorkOrdercollection != null){
				String setartWorkOrder = WorkOrdercollection.min("WORKORDERKEY").toString();
				String endWorkOrder = WorkOrdercollection.max("WORKORDERKEY").toString();
				
				//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				//String pStartDate = "1900-01-01"+ "%2000%3a00%3a00.000";
				
				Date currentDateTime = new Date(System.currentTimeMillis());
				String pEndDate = "";
				if (ReportUtil.isBIRT(state) == true) {
					pEndDate = dateFormat.format(currentDateTime);
				} else {
					pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
				}
				
				
				HashMap paramsAndValues = new HashMap();
				paramsAndValues.put("p_woStart",setartWorkOrder);
				paramsAndValues.put("p_woEnd", endWorkOrder);
				paramsAndValues.put("p_pStartDate", pStartDate);
				paramsAndValues.put("p_pEndDate", pEndDate);
				
				String report_url= ReportUtil.retrieveReportURL(state,"CRPT72", paramsAndValues);

				session.setAttribute("printWorkorderBomURL", report_url);
				
			}
		}
	}
}
