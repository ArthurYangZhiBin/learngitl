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
package com.ssaglobal.scm.wms.wm_demand_allocation.ui;

// import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * The DemandAllocationSearch will retrieve user entered search criteria and construct a query based on the values.
 * 
 * The clickEvent event triggers the DemandAllocationSearch.execute() method.
 * 
 * DemandAllocationSearch uses no parameters
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */
public class DemandAllocationSearch extends ActionExtensionBase {
	// private ILoggerCategory logger = ApplicationUtil.getLogger(LoadPlanningSearch.class);
	private static final String MIN_FILTER_VALUE = "00000000000000000000000000000000000000000000000000";

	private static final String MAX_FILTER_VALUE = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(DemandAllocationSearch.class);

	/**
	 * This method will retrieve user entered search criteria and construct a query based on the values.
	 * 
	 * This method is driven by no properties
	 * 
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) {

		// Get user entered criteria
		RuntimeFormInterface searchForm = context.getSourceWidget().getForm();
		String orderKeyEnd = searchForm.getFormWidgetByName("ORDERKEYEND").getDisplayValue();
		String orderKeyStart = searchForm.getFormWidgetByName("ORDERKEYSTART").getDisplayValue();
		String orderLineNumberEnd = searchForm.getFormWidgetByName("ORDERLINENUMBEREND").getDisplayValue();
		String orderLineNumberStart = searchForm.getFormWidgetByName("ORDERLINENUMBERSTART").getDisplayValue();
		String skuEnd = searchForm.getFormWidgetByName("SKUEND").getDisplayValue();
		String skuStart = searchForm.getFormWidgetByName("SKUSTART").getDisplayValue();
		String storerKeyEnd = searchForm.getFormWidgetByName("STORERKEYEND").getDisplayValue();
		String storerKeyStart = searchForm.getFormWidgetByName("STORERKEYSTART").getDisplayValue();
		//StageLoc can be empty
		String stageLocEnd = searchForm.getFormWidgetByName("STAGELOCEND").getDisplayValue();
		String stageLocStart = searchForm.getFormWidgetByName("STAGELOCSTART").getDisplayValue();
		boolean stageLocIncludeNulls = false;

		if (orderKeyEnd == null || orderKeyEnd.length() == 0) {
			orderKeyEnd = MAX_FILTER_VALUE;
		} else {
			orderKeyEnd = orderKeyEnd.toUpperCase();
		}

		if (orderKeyStart == null || orderKeyStart.length() == 0) {
			orderKeyStart = MIN_FILTER_VALUE;
		} else {
			orderKeyStart = orderKeyStart.toUpperCase();
		}

		if (orderLineNumberEnd == null || orderLineNumberEnd.length() == 0) {
			orderLineNumberEnd = MAX_FILTER_VALUE;
		} else {
			orderLineNumberEnd = orderLineNumberEnd.toUpperCase();
		}

		if (orderLineNumberStart == null || orderLineNumberStart.length() == 0) {
			orderLineNumberStart = MIN_FILTER_VALUE;
		} else {
			orderLineNumberStart = orderLineNumberStart.toUpperCase();
		}

		if (skuEnd == null || skuEnd.length() == 0) {
			skuEnd = MAX_FILTER_VALUE;
		} else {
			skuEnd = skuEnd.toUpperCase();
		}

		if (skuStart == null || skuStart.length() == 0) {
			skuStart = MIN_FILTER_VALUE;
		} else {
			skuStart = skuStart.toUpperCase();
		}

		if (storerKeyEnd == null || storerKeyEnd.length() == 0) {
			storerKeyEnd = MAX_FILTER_VALUE;
		} else {
			storerKeyEnd = storerKeyEnd.toUpperCase();
		}

		if (storerKeyStart == null || storerKeyStart.length() == 0) {
			storerKeyStart = MIN_FILTER_VALUE;
		} else {
			storerKeyStart = storerKeyStart.toUpperCase();
		}

		if (stageLocEnd == null || stageLocEnd.length() == 0) {
			stageLocEnd = MAX_FILTER_VALUE;
		} else {
			stageLocEnd = stageLocEnd.toUpperCase();
		}

		if (stageLocStart == null || stageLocStart.length() == 0) {
			stageLocIncludeNulls = true;
			//			stageLocStart = MIN_FILTER_VALUE;
			stageLocStart = ""; //StageLoc can be blank, it's an optional field
		} else {
			stageLocStart = stageLocStart.toUpperCase();
		}

		// build query string
		String qry = null;
		if (!("".equalsIgnoreCase(orderKeyStart) && "".equalsIgnoreCase(orderKeyEnd))) {
			qry = "wm_demandallocation.ORDERKEY >= '" + orderKeyStart + "' AND wm_demandallocation.ORDERKEY <='" + orderKeyEnd + "'";
		}
		if (!("".equalsIgnoreCase(orderLineNumberStart) && "".equalsIgnoreCase(orderLineNumberEnd))) {
			if (qry != null) {
				qry = qry + " AND (wm_demandallocation.ORDERLINENUMBER >= '" + orderLineNumberStart + "' AND wm_demandallocation.ORDERLINENUMBER <='" + orderLineNumberEnd + "')";
			} else {
				qry = " (wm_demandallocation.ORDERLINENUMBER >= '" + orderLineNumberStart + "' AND wm_demandallocation.ORDERLINENUMBER <='" + orderLineNumberEnd + "')";
			}
		}
		if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd))) {
			if (qry != null) {
				qry = qry + " AND (wm_demandallocation.SKU >= '" + skuStart + "' AND wm_demandallocation.SKU <='" + skuEnd + "')";
			} else {
				qry = " (wm_demandallocation.SKU >= '" + skuStart + "' AND wm_demandallocation.SKU <='" + skuEnd + "')";
			}
		}
		if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd))) {
			if (qry != null) {
				qry = qry + " AND (wm_demandallocation.STORERKEY >= '" + storerKeyStart + "' AND wm_demandallocation.STORERKEY <='" + storerKeyEnd + "') ";
			} else {
				qry = " (wm_demandallocation.STORERKEY >= '" + storerKeyStart + "' AND wm_demandallocation.STORERKEY <='" + storerKeyEnd + "') ";
			}
		}
		if (!("".equalsIgnoreCase(stageLocStart) && "".equalsIgnoreCase(stageLocEnd))) {
			if (qry != null) {
				if (stageLocIncludeNulls == false) {
					qry = qry + " AND (wm_demandallocation.STAGELOC >= '" + stageLocStart + "' AND wm_demandallocation.STAGELOC <='" + stageLocEnd + "') ";
				} else {
					qry = qry + " AND ((wm_demandallocation.STAGELOC >= '" + stageLocStart + "' AND wm_demandallocation.STAGELOC <='" + stageLocEnd + "') OR (wm_demandallocation.STAGELOC IS NULL)) ";
				}
			} else {
				if (stageLocIncludeNulls == false) {
					qry = " (wm_demandallocation.STAGELOC >= '" + stageLocStart + "' AND wm_demandallocation.STAGELOC <='" + stageLocEnd + "') ";
				} else {
					qry = " ((wm_demandallocation.STAGELOC >= '" + stageLocStart + "' AND wm_demandallocation.STAGELOC <='" + stageLocEnd + "') OR (wm_demandallocation.STAGELOC IS NULL)) ";
				}
			}
		}

		// filter biocollection based on the query
		_log.debug("LOG_DEBUG_EXTENSION_DemandAllocationSearch_execute", qry, SuggestedCategory.NONE);
		Query loadBiosQry = new Query("wm_demandallocation", qry, null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		result.setFocus(uow.getBioCollectionBean(loadBiosQry));
		return RET_CONTINUE;

	}
}