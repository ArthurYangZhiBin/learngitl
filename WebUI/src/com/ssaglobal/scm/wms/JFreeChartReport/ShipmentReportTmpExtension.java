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
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;


public class ShipmentReportTmpExtension extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentReportTmpExtension.class);
	protected int execute(ActionContext context, ActionResult result){
		_log.debug("LOG_SYSTEM_OUT","*******it is in drill down SETION $$*****",100L);
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");		
		state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");
/*		RuntimeFormInterface runtimeFormInterface = FormUtil.findForm(state.getCurrentRuntimeForm(),"wm_list_shell_shipmentorder","wm_shipmentorder_list_view",state);

		RuntimeListForm listForm = (RuntimeListForm)runtimeFormInterface;
		QBEBioBean filterBean = listForm.getFilterRowBean();
			
		Object seller = filterBean.get("STORERKEY");
		Object orderNumber = filterBean.get("ORDERKEY");
		Object status = filterBean.get("STATUS");
		Object priority = filterBean.get("PRIORITY");
		Object type = filterBean.get("TYPE");
		Object customerName = filterBean.get("C_COMPANY");
		Object customerOrder = filterBean.get("EXTERNORDERKEY");   
*/ 
		try
		{	
/*			BioCollectionBean newCollection = null;
			BioCollectionBean bBean = null;
			BioCollectionBean collection = (BioCollectionBean)listForm.getFocus();
			if(collection.getQBEBioBean() == null){
				bBean = null;
			}else{
				 bBean = collection.getQBEBioBean().getBaseBioCollectionBean();
			}
			BioCollectionBean bbean2 = ((RuntimeListForm)listForm).getFilterRowBean().getBaseBioCollectionBean();
			bBean = bbean2;
			
			
            QBEBioBean bio = state.getTempUnitOfWork().getQBEBio("wm_orders");//(state,collection.getBioTypeName());

            if(bBean != null){ 
            	collection = bBean.filterBean(new Query("wm_orders","",null));	
            }
            newCollection = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());
            _log.debug("LOG_SYSTEM_OUT","new collection="+newCollection.size(),100L);           
            BioCollectionBean newCollectionB = null;
            if(bBean == null){            	
            	newCollectionB = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());
            }
            else{
            	newCollectionB = bBean;
            	
            }
            _log.debug("LOG_SYSTEM_OUT","new collectionB="+newCollectionB.size(),100L);           

            newCollectionB.copyFrom(collection);

            collection.copyFrom(newCollection);                                

            newCollection = collection;

            bio.setBaseBioCollectionForQuery(newCollectionB);

            bio.set("STORERKEY",seller);
            bio.set("ORDERKEY",orderNumber);
            bio.set("STATUSDESC",tooltip);
            bio.set("STATUS",status);
            bio.set("PRIORITY",priority);
            bio.set("TYPE",type);
            bio.set("C_COMPANY",customerName);
            bio.set("EXTERNORDERKEY",customerOrder);

            
            
            _log.debug("LOG_SYSTEM_OUT","#%query="+bio.getQuery().getQueryExpression(),100L);           
            
            newCollection.setQBEBioBean(bio);                                                                    

            newCollection.filterInPlace(bio.getQueryWithWildcards());
			
			result.setFocus((DataBean) newCollection);

*/			
			String qry = "wm_orders.STATUSDESC='"+tooltip+"'";	
			if(WSDefaultsUtil.isOwnerLocked(state)){
  	   	    	ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
  	   	    	if(lockedOwners != null && lockedOwners.size() > 0){
  	   	    		qry += " AND wm_orders.STORERKEY IN ( '"+lockedOwners.get(0)+"' ";
  	   	    		for(int i = 1; i < lockedOwners.size(); i++){
  	   	    			qry += " , '"+lockedOwners.get(i)+"'";
  	   	    		}
  	   	    		qry += " ) ";
  	   	    	} 
  	   	    	ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
	   	    	if(lockedCustomers != null && lockedCustomers.size() > 0){
	   	    		qry += " AND (wm_orders.CONSIGNEEKEY IN ( '"+lockedCustomers.get(0)+"' ";
	   	    		for(int i = 1; i < lockedCustomers.size(); i++){
	   	    			qry += " , '"+lockedCustomers.get(i)+"'";
	   	    		}
	   	    		qry += ") OR wm_orders.CONSIGNEEKEY IS NULL OR wm_orders.CONSIGNEEKEY = '' OR wm_orders.CONSIGNEEKEY = ' ') ";
	   	    	}
	   	    	ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
	   	    	if(lockedCarriers != null && lockedCarriers.size() > 0){
	   	    		qry += " AND (wm_orders.CarrierCode IN ( '"+lockedCarriers.get(0)+"' ";
	   	    		for(int i = 1; i < lockedCarriers.size(); i++){
	   	    			qry += " , '"+lockedCarriers.get(i)+"'";
	   	    		}
	   	    		qry += ") OR wm_orders.CarrierCode IS NULL OR wm_orders.CarrierCode = '' OR wm_orders.CarrierCode = ' ') ";
	   	    	}
	   	    	ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
	   	    	if(lockedBillTo != null && lockedBillTo.size() > 0){
	   	    		qry += " AND (wm_orders.BILLTOKEY IN ('"+lockedBillTo.get(0)+"' ";
	   	    		for(int i = 1; i < lockedBillTo.size(); i++){
	   	    			qry += " , '"+lockedBillTo.get(i)+"'";
	   	    		}
	   	    		qry += ") OR wm_orders.BILLTOKEY IS NULL OR wm_orders.BILLTOKEY = '' OR wm_orders.BILLTOKEY = ' ') ";
	   	    	}
  	   	    }
			Query loadBiosQry = new Query("wm_orders", qry, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();

			BioCollectionBean collection = (BioCollectionBean)uow.getBioCollectionBean(new Query("wm_orders",null,null));
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
}
