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

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class PickDetailReportTmpExtension extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickDetailReportTmpExtension.class);
	private static final boolean useDPEQuery = true;
	protected int execute(ActionContext context, ActionResult result){
		_log.debug("LOG_SYSTEM_OUT","*******it is in drill down SETION $$*****",100L);
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");		
		state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");
 
		try
		{
//Get the code from codelookup using the tooltip
			String qry1 = "codelkup.LISTNAME = 'ORDRSTATUS'and codelkup.DESCRIPTION = '"+ tooltip + "'";			
			Query codelkupQry = new Query("codelkup", qry1, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean codeLkupCollection = (BioCollectionBean)uow.getBioCollectionBean(codelkupQry);
			String Statuscode =  codeLkupCollection.get("0").get("CODE").toString();
			String qry = "";
			if(useDPEQuery){
				qry = "@[wm_pickdetail.STATUS] = \\'"+ Statuscode +"\\'";	
				if(WSDefaultsUtil.isOwnerLocked(state)){
					ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
					if(lockedOwners != null && lockedOwners.size() > 0){
						qry += " AND @[wm_pickdetail.STORERKEY] IN ( \\'"+lockedOwners.get(0)+"\\' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							qry += " , \\'"+lockedOwners.get(i)+"\\'";
						}
						qry += " ) ";
					}  	

					String orderQry = getOrderSql(state,true);
					if(orderQry.length() > 0){
						qry += " AND @[wm_pickdetail.ORDERKEY] IN ( "+orderQry+" ";							
						qry += " ) ";
					}  
				}
				qry = "DPE('SQL','"+qry+"')";				
			}			
			else{
				qry = "wm_pickdetail.STATUS ='"+ Statuscode +"'";	
				if(WSDefaultsUtil.isOwnerLocked(state)){
					ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
					if(lockedOwners != null && lockedOwners.size() > 0){
						qry += " AND wm_pickdetail.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
						for(int i = 1; i < lockedOwners.size(); i++){
							qry += " , '"+lockedOwners.get(i)+"'";
						}
						qry += " ) ";
					}  	

					ArrayList lockedOrders = getViewableOrderKeys(state);
					if(lockedOrders != null && lockedOrders.size() > 0){
						qry += " AND wm_pickdetail.ORDERKEY IN ( '"+lockedOrders.get(0)+"' ";
						for(int i = 1; i < lockedOrders.size(); i++){
							qry += " , '"+lockedOrders.get(i)+"'";
						}
						qry += " ) ";
					}  

				}
			}
			Query loadBiosQry = new Query("wm_pickdetail", qry, null);

			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("wm_pickdetail",null,null));			
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

	private ArrayList getViewableOrderKeys(StateInterface state){
		ArrayList orderKeys = new ArrayList();

		String sql = getOrderSql(state,false);
		if(sql.length() == 0)
			return orderKeys;
		try {
			EXEDataObject orders = WmsWebuiValidationSelectImpl.select(sql);
			if(orders != null){
				for(int i = 0; i < orders.getRowCount(); i++){
					Object attrValue = orders.getAttribValue(new TextData("ORDERKEY"));
					if(attrValue != null)
						orderKeys.add(attrValue.toString());
				}
			}
		} catch (DPException e) {			
			e.printStackTrace();
		}
		return orderKeys;
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
