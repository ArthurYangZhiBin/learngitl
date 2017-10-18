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
package com.ssaglobal.scm.wms.wm_load_planning.ui;

//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
//import com.epiphany.shr.util.logging.ILoggerCategory;
import java.util.Calendar;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;
public class LoadPlanningSearch extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadPlanningSearch.class);
	//	private ILoggerCategory logger = ApplicationUtil.getLogger(LoadPlanningSearch.class);
	private static final String LPSEARCHCRITERIA = "LPSEARCHCRITERIA";

	protected int execute(ActionContext context, ActionResult result)
	{
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		StateInterface state = context.getState();
		//RuntimeFormInterface toolbarForm = context.getSourceWidget().getForm();
		//RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		//SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
		//RuntimeFormInterface searchForm = state.getRuntimeForm(headerSlot, "wm_load_planning_search_view");		
		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_load_planning_template", "wm_load_planning_search_view", state);
		

		String sellerStart = searchForm.getFormWidgetByName("SELLERSTART").getDisplayValue();
		String sellerEnd = searchForm.getFormWidgetByName("SELLEREND").getDisplayValue();
		String customerStart = searchForm.getFormWidgetByName("CUSTOMERSTART").getDisplayValue();
		String customerEnd = searchForm.getFormWidgetByName("CUSTOMEREND").getDisplayValue();
		
		String ohTypeStart = getWidgetValue(searchForm, "ORDERHANDLINGSTART");
		String ohTypeEnd = getWidgetValue(searchForm, "ORDERHANDLINGEND");
		String orderStart = getWidgetValue(searchForm, "ORDERSTART");
		String orderEnd = getWidgetValue(searchForm, "ORDEREND");
		String flowThruStart = getWidgetValue(searchForm, "FLOWTHRUSTART");
		String flowThruEnd = getWidgetValue(searchForm, "FLOWTHRUEND");
		String transShipStart = getWidgetValue(searchForm, "TRANSSHIPSTART");
		String transShipEnd = getWidgetValue(searchForm, "TRANSSHIPEND");		
		
		String routeStart = getWidgetValue(searchForm, "ROUTESTART");
		String routeEnd = getWidgetValue(searchForm, "ROUTEEND");		
		String stopStart = getWidgetValue(searchForm, "STOPSTART");
		String stopEnd = getWidgetValue(searchForm, "STOPEND");		
		String loadIdStart = getWidgetValue(searchForm, "LOADIDSTART");
		String loadIdEnd = getWidgetValue(searchForm, "LOADIDEND");		
		String externalLoadIdStart = getWidgetValue(searchForm, "EXTERNALLOADIDSTART");
		String externalLoadIdEnd = getWidgetValue(searchForm, "EXTERNALLOADIDEND");		
		String outboundLaneStart = getWidgetValue(searchForm, "OUTBOUNDLANESTART");
		String outboundLaneEnd = getWidgetValue(searchForm, "OUTBOUNDLANEEND");		
		String transShipASNStart = getWidgetValue(searchForm, "TRANSSHIPASNSTART");
		String transShipASNEnd = getWidgetValue(searchForm, "TRANSSHIPASNEND");						
		
		Calendar orderDateStart = getWidgetCalendarValue(searchForm, "ORDERDATESTART");
		Calendar orderDateEnd = getWidgetCalendarValue(searchForm, "ORDERDATEEND");
		Calendar deliveryDateStart = getWidgetCalendarValue(searchForm, "DELIVERYDATESTART");
		Calendar deliveryDateEnd = getWidgetCalendarValue(searchForm, "DELIVERYDATEEND");
		
		String typeS = getWidgetCodeValue(searchForm, "TYPESTART");
		String typeE = getWidgetCodeValue(searchForm, "TYPEEND");
		
		

		if(WSDefaultsUtil.isOwnerLocked(state)){
			String lockedOwner = WSDefaultsUtil.getPreFilterValueByType("STORER", state);
			String lockedCustomer = WSDefaultsUtil.getPreFilterValueByType("CUSTOM", state);
			
			//If no default locked owner has been defined (by checking an owner pre-filter) then grab the first locked owner in the set.
			if(lockedOwner == null || lockedOwner.equals("")){
				if(WSDefaultsUtil.getLockedOwners(state) != null && WSDefaultsUtil.getLockedOwners(state).size() > 0){
					lockedOwner = (String)WSDefaultsUtil.getLockedOwners(state).get(0);
				}
			}
			
			//If no default locked customer has been defined (by checking a customer pre-filter) then grab the first locked customer in the set.
//			if(lockedCustomer == null || lockedCustomer.equals("")){
//				if(WSDefaultsUtil.getLockedCustomers(state) != null && WSDefaultsUtil.getLockedCustomers(state).size() > 0){
//					lockedCustomer = (String)WSDefaultsUtil.getLockedCustomers(state).get(0);
//				}
//			}
			
			if(lockedOwner != null && !lockedOwner.equals("")){
				//If no values were given for either side of the filter range then make both values the default locked owner
				//Else we set the empty filter to the value of the populated filter.
				//This eliminates any range on the locked filter criteria.
				//NOTE: this was added to fix a bug introduced by an OA fix that keeps widgets from flagging "dirty"
				//		if their display value is set. This resulted in screens like this - who have no focus - losing 
				//		extension-populated default values.
				if(sellerStart != null && sellerStart.equals("")){
					if(sellerEnd != null && sellerEnd.equals("")){
						sellerStart = lockedOwner;
						sellerEnd = lockedOwner;	
					}
					else{
						sellerStart = sellerEnd;
					}
				}
				else if(sellerEnd != null && sellerEnd.equals("")){
					if(sellerStart != null && sellerStart.equals("")){
						sellerStart = lockedOwner;
						sellerEnd = lockedOwner;	
					}
					else{
						sellerEnd = sellerStart;
					}
				}
			}
			
			
			
			if(lockedCustomer != null && !lockedCustomer.equals("")){
				if(customerStart != null && customerStart.equals("")){
					if(customerEnd != null && customerEnd.equals("")){
						customerStart = lockedCustomer;
						customerEnd = lockedCustomer;	
					}
					else{
						customerStart = customerEnd;
					}
				}
				else if(customerEnd != null && customerEnd.equals("")){
					if(customerStart != null && customerStart.equals("")){
						customerStart = lockedCustomer;
						customerEnd = lockedCustomer;	
					}
					else{
						customerEnd = customerStart;
					}
				}
			}
			
			
		}
		if(sellerStart == null)
			sellerStart = "";
		else
			sellerStart = sellerStart.toUpperCase();
		if(sellerEnd == null)
			sellerEnd = "";
		else
			sellerEnd = sellerEnd.toUpperCase();
		if(customerStart == null)
			customerStart = "";
		else
			customerStart = customerStart.toUpperCase();
		if(customerEnd == null)
			customerEnd = "";
		else
			customerEnd = customerEnd.toUpperCase();
		LPSearchCriteria lps = new LPSearchCriteria(sellerStart, sellerEnd, customerStart, customerEnd, ohTypeStart, ohTypeEnd, 
				orderStart, orderEnd, orderDateStart, orderDateEnd, deliveryDateStart, deliveryDateEnd, flowThruStart, flowThruEnd, 
				transShipStart, transShipEnd, routeStart, routeEnd, stopStart, stopEnd,
				loadIdStart, loadIdEnd, externalLoadIdStart, externalLoadIdEnd,
				outboundLaneStart, outboundLaneEnd, transShipASNStart, transShipASNEnd, typeS, typeE);

		String qry = null;
		
		//qry = "wm_loadplanning.LOADID IS NULL OR (wm_loadplanning.LOADID = '' OR wm_loadplanning.LOADID = ' ')  AND wm_loadplanning.ORDER_STATUS != 95 ";
		qry = "wm_loadplanning.ORDER_STATUS != 95 ";
		
		
		if (!("".equalsIgnoreCase(sellerStart) && "".equalsIgnoreCase(sellerEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND wm_loadplanning.SELLER >= '" + sellerStart + "' AND wm_loadplanning.SELLER <='" + sellerEnd + "'";
			}else
			{
				qry =" wm_loadplanning.SELLER >= '" + sellerStart + "' AND wm_loadplanning.SELLER <='" + sellerEnd + "'";
			}
		}
		if (!("".equalsIgnoreCase(customerStart) && "".equalsIgnoreCase(customerEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.SHIPTO >= '" + customerStart + "' AND wm_loadplanning.SHIPTO <='"
						+ customerEnd + "')";
			}
			else
			{
				qry = " (wm_loadplanning.SHIPTO >= '" + customerStart + "' AND wm_loadplanning.SHIPTO <='"
						+ customerEnd + "')";
			}
		}
		if (!("".equalsIgnoreCase(ohTypeStart) && "".equalsIgnoreCase(ohTypeEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.OHTYPE >= '" + ohTypeStart + "' AND wm_loadplanning.OHTYPE <='"
						+ ohTypeEnd + "')";
			}
			else
			{
				qry = " (wm_loadplanning.OHTYPE >= '" + ohTypeStart + "' AND wm_loadplanning.OHTYPE <='" + ohTypeEnd
						+ "')";
			}
		}
		if (!("".equalsIgnoreCase(orderStart) && "".equalsIgnoreCase(orderEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ORDERKEY >= '" + orderStart + "' AND wm_loadplanning.ORDERKEY <='"
						+ orderEnd + "') AND wm_loadplanning.SOURCETYPE='1' ";
			}
			else
			{
				qry = " (wm_loadplanning.ORDERKEY >= '" + orderStart + "' AND wm_loadplanning.ORDERKEY <='" + orderEnd
						+ "') AND wm_loadplanning.SOURCETYPE='1' ";
			}
		}
		if (orderDateStart != null && orderDateEnd != null)
		{
			long orderDateStartMil = orderDateStart.getTimeInMillis();
			long orderDateEndMil = orderDateEnd.getTimeInMillis();
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ORDERDATE >= @DATE['" + orderDateStartMil
						+ "'] AND wm_loadplanning.ORDERDATE <= @DATE['" + orderDateEndMil + "'])";
			}
			else
			{
				qry = " (wm_loadplanning.ORDERDATE >= @DATE['" + orderDateStartMil
						+ "'] AND wm_loadplanning.ORDERDATE <= @DATE['" + orderDateEndMil + "'])";
			}
		}

		if (deliveryDateStart != null && deliveryDateEnd != null)
		{
			long deliveryDateStartMil = deliveryDateStart.getTimeInMillis();
			long deliveryDateEndMil = deliveryDateEnd.getTimeInMillis();
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.DELIVERYDATE >= @DATE['" + deliveryDateStartMil
						+ "'] AND wm_loadplanning.DELIVERYDATE <= @DATE['" + deliveryDateEndMil + "'])";
			}
			else
			{
				qry = " (wm_loadplanning.DELIVERYDATE >= @DATE['" + deliveryDateStartMil
						+ "'] AND wm_loadplanning.DELIVERYDATE <= @DATE['" + deliveryDateEndMil + "'])";
			}
		}

		if (!("".equalsIgnoreCase(flowThruStart) && "".equalsIgnoreCase(flowThruEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ORDERKEY >= '" + flowThruStart
						+ "' AND wm_loadplanning.ORDERKEY <='" + flowThruEnd + "') AND wm_loadplanning.SOURCETYPE='2' ";
			}
			else
			{
				qry = " (wm_loadplanning.ORDERKEY >= '" + flowThruStart + "' AND wm_loadplanning.ORDERKEY <='"
						+ flowThruEnd + "') AND wm_loadplanning.SOURCETYPE='2' ";
			}
		}
		if (!("".equalsIgnoreCase(transShipStart) && "".equalsIgnoreCase(transShipEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ORDERKEY >= '" + transShipStart
						+ "' AND wm_loadplanning.ORDERKEY <='" + transShipEnd
						+ "') AND wm_loadplanning.SOURCETYPE='4' ";
			}
			else
			{
				qry = " (wm_loadplanning.ORDERKEY >= '" + transShipStart + "' AND wm_loadplanning.ORDERKEY <='"
						+ transShipEnd + "') AND wm_loadplanning.SOURCETYPE='4' ";
			}
		}

		if (!("".equalsIgnoreCase(routeStart) && "".equalsIgnoreCase(routeEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ROUTE >= '" + routeStart + "' AND wm_loadplanning.ROUTE <='"
						+ routeEnd + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.ROUTE >= '" + routeStart + "' AND wm_loadplanning.ROUTE <='"
				+ routeEnd + "') ";
			}
		}
 
		if (!("".equalsIgnoreCase(stopStart) && "".equalsIgnoreCase(stopEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.STOP >= '" + stopStart + "' AND wm_loadplanning.STOP <='"
						+ stopEnd + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.STOP >= '" + stopStart + "' AND wm_loadplanning.STOP <='"
				+ stopEnd + "') ";
			}
		}
		
		if (!("".equalsIgnoreCase(loadIdStart) && "".equalsIgnoreCase(loadIdEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.LOADID >= '" + loadIdStart + "' AND wm_loadplanning.LOADID <='"
						+ loadIdEnd + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.LOADID >= '" + loadIdStart + "' AND wm_loadplanning.LOADID <='"
				+ loadIdEnd + "') ";
			}
		}		
		
		if (!("".equalsIgnoreCase(externalLoadIdStart) && "".equalsIgnoreCase(externalLoadIdEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.EXTERNALLOADID >= '" + externalLoadIdStart + "' AND wm_loadplanning.EXTERNALLOADID <='"
						+ externalLoadIdEnd + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.EXTERNALLOADID >= '" + externalLoadIdStart + "' AND wm_loadplanning.EXTERNALLOADID <='"
				+ externalLoadIdEnd + "') ";
			}
		}

		if (!("".equalsIgnoreCase(outboundLaneStart) && "".equalsIgnoreCase(outboundLaneEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.OUTBOUNDLANE >= '" + outboundLaneStart + "' AND wm_loadplanning.OUTBOUNDLANE <='"
						+ outboundLaneEnd + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.OUTBOUNDLANE >= '" + outboundLaneStart + "' AND wm_loadplanning.OUTBOUNDLANE <='"
				+ outboundLaneEnd + "') ";
			}
		}
		
		if (!("".equalsIgnoreCase(typeS) && "".equalsIgnoreCase(typeE)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.TYPE >= '" + typeS + "' AND wm_loadplanning.TYPE <='"
						+ typeE + "') ";
			}
			else
			{
				qry = " (wm_loadplanning.TYPE >= '" + typeS + "' AND wm_loadplanning.TYPE <='"
				+ typeE + "') ";
			}
		}

		if (!("".equalsIgnoreCase(transShipASNStart) && "".equalsIgnoreCase(transShipASNEnd)))
		{
			if (qry != null)
			{
				qry = qry + " AND (wm_loadplanning.ORDERKEY >= '" + transShipASNStart
						+ "' AND wm_loadplanning.ORDERKEY <='" + transShipASNEnd
						+ "') AND wm_loadplanning.SOURCETYPE='3' ";
			}
			else
			{
				qry = " (wm_loadplanning.ORDERKEY >= '" + transShipASNStart + "' AND wm_loadplanning.ORDERKEY <='"
						+ transShipASNEnd + "') AND wm_loadplanning.SOURCETYPE='3' ";
			}
		}
		
		
		_log.debug("LOG_SYSTEM_OUT","[LoadPlanningSearch] Query:"+qry,100L);
		
		_log.debug("LOG_DEBUG_EXTENSION", "QUERY" + qry, SuggestedCategory.NONE);
		Query loadBiosQry = new Query("wm_loadplanning", qry, null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		result.setFocus((DataBean) uow.getBioCollectionBean(loadBiosQry));
		userContext.put(LPSEARCHCRITERIA, lps);
		return RET_CONTINUE;

	}
	
	private String getWidgetCodeValue(RuntimeFormInterface form,
			String widgetName) {
		if(form.getFormWidgetByName(widgetName).getValue() == null)
			return "";
		else
			return (String) form.getFormWidgetByName(widgetName).getValue();
	}

	private String getWidgetValue(RuntimeFormInterface form, String widgetName){
		if(form.getFormWidgetByName(widgetName).getDisplayValue() == null)
			return "";
		else
			return form.getFormWidgetByName(widgetName).getDisplayValue().toUpperCase();
	}
	
	private Calendar getWidgetCalendarValue(RuntimeFormInterface form, String widgetName){
		if(form.getFormWidgetByName(widgetName).getCalendarValue() == null)
			return null;
		else
			return form.getFormWidgetByName(widgetName).getCalendarValue();
	}
}