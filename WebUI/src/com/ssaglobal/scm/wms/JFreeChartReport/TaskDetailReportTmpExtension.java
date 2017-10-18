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
package com.ssaglobal.scm.wms.JFreeChartReport;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class TaskDetailReportTmpExtension extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDetailReportTmpExtension.class);
	protected int execute(ActionContext context, ActionResult result){
		_log.debug("LOG_SYSTEM_OUT","*******it is in drill down SETION $$*****",100L);
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");		
		state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");
 
		try
		{
//Get the code from codelookup using the tooltip
			String qry1 = "codelkup.LISTNAME = 'TMSTATUS'and codelkup.DESCRIPTION = '"+ tooltip + "'";			
			Query codelkupQry = new Query("codelkup", qry1, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean codeLkupCollection = (BioCollectionBean)uow.getBioCollectionBean(codelkupQry);
			String Statuscode =  codeLkupCollection.get("0").get("CODE").toString();
			
			String qry = "wm_taskdetail.STATUS ='"+ Statuscode +"'";		
			if(WSDefaultsUtil.isOwnerLocked(state)){
				qry = "@[wm_taskdetail.STATUS] =\\'"+ Statuscode +"\\'";	
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		qry += " AND @[wm_taskdetail.STORERKEY] IN ( \\'"+lockedOwners.get(0)+"\\' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			qry += " , \\'"+lockedOwners.get(i)+"\\'";
  	   	    		}
  	   	    		qry += " ) ";
  	   	    	}  	 
  	   	    	String LDOrPKTaskFilterClause = getLDPKTaskTypeFilterClause(state, true);
  	   	    	String PATaskFilterClause = getPATaskTypeFilterClause(state, true);
  	   	    	//Clause to filter out tasks based on Order if task is of type LD or PK (requested by Julie) 
  	   	    	if(LDOrPKTaskFilterClause.length() > 0)
  	   	    		qry += " AND (@[wm_taskdetail.TASKDETAILKEY] NOT IN ("+LDOrPKTaskFilterClause+")) ";
  	   	    	//Clause to filter out tasks based on ASN if task is of type PA (requested by Julie)
  	   	    	if(PATaskFilterClause.length() > 0)
  	   	    		qry += " AND (@[wm_taskdetail.TASKDETAILKEY] NOT IN ("+PATaskFilterClause+")) ";
  	   	    	qry = ""+qry+"";
  	   	    	qry = "DPE('SQL','"+qry+"')";
			}
			Query loadBiosQry = new Query("wm_taskdetail", qry, null);

			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("wm_taskdetail",null,null));			
			QBEBioBean bio = createNewQBE(state,collection.getBioTypeName());
			BioCollectionBean newCollection = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());
			BioCollectionBean newCollectionB = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());		
			newCollectionB.copyFrom(collection);
			collection.copyFrom(newCollection);			
			newCollection = collection;			
			bio.setBaseBioCollectionForQuery(newCollectionB);
			newCollection.setQBEBioBean(bio);						
			newCollection.filterInPlace(loadBiosQry);
			result.setFocus(newCollection);
			return RET_CONTINUE;
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}

}
	
	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		}
		catch(DataBeanException ex)
		{
			Object args[] = {
					bioType
			};
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}
	
	private String getLDPKTaskTypeFilterClause(StateInterface state, boolean isDPE){
		   String sql = "";
		   String orderFilterSql = getOrderSql(state, isDPE);
		   if(orderFilterSql.length() == 0)
			   return "";
		   if(isDPE){
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE (TASKDETAIL.TASKTYPE = \\'LD\\' OR TASKDETAIL.TASKTYPE = \\'PK\\') AND (TASKDETAIL.ORDERKEY NOT IN ("+orderFilterSql+") OR TASKDETAIL.ORDERKEY IS NULL OR TASKDETAIL.ORDERKEY = \\'\\' OR TASKDETAIL.ORDERKEY = \\' \\')";
		   }
		   else{
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE (TASKDETAIL.TASKTYPE = 'LD' OR TASKDETAIL.TASKTYPE = 'PK') AND (TASKDETAIL.ORDERKEY NOT IN ("+orderFilterSql+") OR TASKDETAIL.ORDERKEY IS NULL OR TASKDETAIL.ORDERKEY = '' OR TASKDETAIL.ORDERKEY = ' ' )";
		   }
		   return sql;
	   }
	private String getPATaskTypeFilterClause(StateInterface state, boolean isDPE){
		   String sql = "";
		   String receiptFilterSql = getReceiptSql(state, isDPE);
		   if(receiptFilterSql.length() == 0)
			   return "";
		   if(isDPE){
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE TASKDETAIL.TASKTYPE = \\'PA\\' AND (TASKDETAIL.SOURCEKEY NOT IN ("+receiptFilterSql+") OR TASKDETAIL.SOURCEKEY IS NULL OR TASKDETAIL.SOURCEKEY = \\'\\' OR TASKDETAIL.SOURCEKEY = \\' \\')";
		   }
		   else{
			   sql = " SELECT TASKDETAILKEY FROM TASKDETAIL WHERE TASKDETAIL.TASKTYPE = 'PA' AND (TASKDETAIL.SOURCEKEY NOT IN ("+receiptFilterSql+") OR TASKDETAIL.SOURCEKEY IS NULL OR TASKDETAIL.SOURCEKEY = '' OR TASKDETAIL.SOURCEKEY = ' ')";
		   }
		   return sql;
	   }
	   
	   private String getReceiptSql(StateInterface state, boolean isDPE){
			
		   	String serverType = (String) state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_TYPE);
			String sql = "";
			ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);			
			ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
			ArrayList lockedVendors = WSDefaultsUtil.getLockedVendors(state);
			if( (lockedOwners != null && lockedOwners.size() > 0) ||
					(lockedCarriers != null && lockedCarriers.size() > 0) ||
					(lockedVendors != null && lockedVendors.size() > 0)){

				if(!isDPE){
					if (serverType.equalsIgnoreCase("MSS"))
					{
						sql = "SELECT RECEIPT.RECEIPTKEY + RECEIPTDETAIL.RECEIPTLINENUMBER FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					else{
						sql = "SELECT CONCAT(RECEIPT.RECEIPTKEY,RECEIPTDETAIL.RECEIPTLINENUMBER) FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (RECEIPT.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , '"+lockedOwners.get(i)+"'";
						}
						sql += ") ) ";
					}					
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0))
							sql += " AND (RECEIPT.CARRIERKEY IN ( '"+lockedCarriers.get(0)+"' ";
						else
							sql += "  (RECEIPT.CARRIERKEY IN ( '"+lockedCarriers.get(0)+"' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , '"+lockedCarriers.get(i)+"'";
						}
						sql += ") OR RECEIPT.CARRIERKEY IS NULL OR RECEIPT.CARRIERKEY = '' OR RECEIPT.CARRIERKEY = ' ') ";
					}  	   	    		
					if(lockedVendors != null && lockedVendors.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCarriers != null && lockedCarriers.size() > 0))
							sql += " AND (RECEIPT.EXTERNALRECEIPTKEY2 IN ( '"+lockedVendors.get(0)+"' ";
						else
							sql += " (RECEIPT.EXTERNALRECEIPTKEY2 IN ( '"+lockedVendors.get(0)+"' ";

						for(int i = 1; i < lockedVendors.size(); i++){
							sql += " , '"+lockedVendors.get(i)+"'";
						}
						sql += ") OR RECEIPT.EXTERNALRECEIPTKEY2 IS NULL OR RECEIPT.EXTERNALRECEIPTKEY2 = '' OR RECEIPT.EXTERNALRECEIPTKEY2 = ' ') ";
					}
					
				}
				else{
					if (serverType.equalsIgnoreCase("MSS"))
					{
						sql = "SELECT RECEIPT.RECEIPTKEY + RECEIPTDETAIL.RECEIPTLINENUMBER FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					else{
						sql = "SELECT CONCAT(RECEIPT.RECEIPTKEY,RECEIPTDETAIL.RECEIPTLINENUMBER) FROM RECEIPT INNER JOIN RECEIPTDETAIL ON RECEIPT.RECEIPTKEY = RECEIPTDETAIL.RECEIPTKEY WHERE ";
					}
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (RECEIPT.STORERKEY IN ( \\'"+lockedOwners.get(0)+"\\' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , \\'"+lockedOwners.get(i)+"\\'";
						}
						sql += ") ) ";
					}					
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0))
							sql += " AND (RECEIPT.CARRIERKEY IN ( \\'"+lockedCarriers.get(0)+"\\' ";
						else
							sql += "  (RECEIPT.CARRIERKEY IN ( \\'"+lockedCarriers.get(0)+"\\' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , \\'"+lockedCarriers.get(i)+"\\'";
						}
						sql += ") OR RECEIPT.CARRIERKEY IS NULL OR RECEIPT.CARRIERKEY = \\'\\' OR RECEIPT.CARRIERKEY = \\' \\') ";
					}  	   	    		
					if(lockedVendors != null && lockedVendors.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCarriers != null && lockedCarriers.size() > 0))
							sql += " AND (RECEIPT.EXTERNALRECEIPTKEY2 IN ( \\'"+lockedVendors.get(0)+"\\' ";
						else
							sql += " (RECEIPT.EXTERNALRECEIPTKEY2 IN ( \\'"+lockedVendors.get(0)+"\\' ";

						for(int i = 1; i < lockedVendors.size(); i++){
							sql += " , \\'"+lockedVendors.get(i)+"\\'";
						}
						sql += ") OR RECEIPT.EXTERNALRECEIPTKEY2 IS NULL OR RECEIPT.EXTERNALRECEIPTKEY2 = \\'\\' OR RECEIPT.EXTERNALRECEIPTKEY2 = \\' \\') ";
					}
				}
			}
			
			return sql;
	   }
	   
	   private String getOrderSql(StateInterface state, boolean isDPE){
			
			String sql = "";
			ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
			ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
			ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
			ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
			if( (lockedCustomers != null && lockedCustomers.size() > 0) ||
					(lockedCarriers != null && lockedCarriers.size() > 0) ||
					(lockedBillTo != null && lockedBillTo.size() > 0) ||
					(lockedOwners != null && lockedOwners.size() > 0)){

				if(!isDPE){
					sql = "SELECT ORDERS.ORDERKEY FROM ORDERS WHERE ";
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (ORDERS.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , '"+lockedOwners.get(i)+"'";
						}
						sql += ") ) ";
					}
					if(lockedCustomers != null && lockedCustomers.size() > 0){
						if(lockedOwners != null && lockedOwners.size() > 0)
							sql += " AND (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";
						else
							sql += "  (ORDERS.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";						
						for(int i = 1; i < lockedCustomers.size(); i++){
							sql += " , '"+lockedCustomers.get(i)+"'";
						}
						sql += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = '' OR ORDERS.CONSIGNEEKEY = ' ') ";
					}
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCustomers != null && lockedCustomers.size() > 0)	)
							sql += " AND (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
						else
							sql += "  (ORDERS.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , '"+lockedCarriers.get(i)+"'";
						}
						sql += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = '' OR ORDERS.CarrierCode = ' ') ";
					}  	   	    		
					if(lockedBillTo != null && lockedBillTo.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
								(lockedCustomers != null && lockedCustomers.size() > 0) ||
								(lockedCarriers != null && lockedCarriers.size() > 0))

							sql += " AND (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";
						else
							sql += " (ORDERS.BILLTOKEY IN ( '"+lockedBillTo.get(0)+"' ";

						for(int i = 1; i < lockedBillTo.size(); i++){
							sql += " , '"+lockedBillTo.get(i)+"'";
						}
						sql += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = '' OR ORDERS.BILLTOKEY = ' ') ";
					}
					
				}
				else{
					sql = "SELECT ORDERS.ORDERKEY FROM ORDERS WHERE ";
					if(lockedOwners != null && lockedOwners.size() > 0){
						sql += " (ORDERS.STORERKEY IN ( \\'"+lockedOwners.get(0)+"\\' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							sql += " , \\'"+lockedOwners.get(i)+"\\'";
						}
						sql += ") ) ";
					}
					if(lockedCustomers != null && lockedCustomers.size() > 0){
						if(lockedOwners != null && lockedOwners.size() > 0)
							sql += " AND (ORDERS.CONSIGNEEKEY IN ( \\'"+lockedCustomers.get(0)+"\\' ";
						else
							sql += "  (ORDERS.CONSIGNEEKEY IN ( \\'"+lockedCustomers.get(0)+"\\' ";	
						for(int i = 1; i < lockedCustomers.size(); i++){
							sql += " , \\'"+lockedCustomers.get(i)+"\\'";
						}
						sql += ") OR ORDERS.CONSIGNEEKEY IS NULL OR ORDERS.CONSIGNEEKEY = \\'\\' OR ORDERS.CONSIGNEEKEY = \\' \\') ";
					}
					if(lockedCarriers != null && lockedCarriers.size() > 0){
						if((lockedOwners != null && lockedOwners.size() > 0) ||
							(lockedCustomers != null && lockedCustomers.size() > 0)	)
								sql += " AND (ORDERS.CarrierCode IN ( \\'"+lockedCarriers.get(0)+"\\' ";
							else
								sql += "  (ORDERS.CarrierCode IN ( \\'"+lockedCarriers.get(0)+"\\' ";

						for(int i = 1; i < lockedCarriers.size(); i++){
							sql += " , \\'"+lockedCarriers.get(i)+"\\'";
						}
						sql += ") OR ORDERS.CarrierCode IS NULL OR ORDERS.CarrierCode = \\'\\' OR ORDERS.CarrierCode = \\' \\') ";
					}  	   	    		
					if(lockedBillTo != null && lockedBillTo.size() > 0){

						if((lockedOwners != null && lockedOwners.size() > 0) ||
								(lockedCustomers != null && lockedCustomers.size() > 0) ||
								(lockedCarriers != null && lockedCarriers.size() > 0))

							sql += " AND (ORDERS.BILLTOKEY IN ( \\'"+lockedBillTo.get(0)+"\\' ";
						else
							sql += " (ORDERS.BILLTOKEY IN ( \\'"+lockedBillTo.get(0)+"\\' ";

						for(int i = 1; i < lockedBillTo.size(); i++){
							sql += " , '"+lockedBillTo.get(i)+"'";
						}
						sql += ") OR ORDERS.BILLTOKEY IS NULL OR ORDERS.BILLTOKEY = \\'\\' OR ORDERS.BILLTOKEY = \\' \\') ";
					}
					
				}
			}
			
			
			return sql;
		}
}
