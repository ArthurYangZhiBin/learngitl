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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Calendar;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LPResetSearch extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	private static final String LPSEARCHCRITERIA = "LPSEARCHCRITERIA";

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		LPSearchCriteria lps = (LPSearchCriteria) userContext.get(LPSEARCHCRITERIA);
		if(lps == null)
		{
			return RET_CONTINUE;
		}
		userContext.remove(LPSEARCHCRITERIA);
		try
		{

			form.getFormWidgetByName("SELLERSTART").setDisplayValue(lps.getSellerS());
			form.getFormWidgetByName("SELLEREND").setDisplayValue(lps.getSellerE());
			form.getFormWidgetByName("CUSTOMERSTART").setDisplayValue(lps.getCustS());
			form.getFormWidgetByName("CUSTOMEREND").setDisplayValue(lps.getCustE());
			form.getFormWidgetByName("ORDERHANDLINGSTART").setDisplayValue(lps.getOhTypeS());
			form.getFormWidgetByName("ORDERHANDLINGEND").setDisplayValue(lps.getOhTypeE());
			form.getFormWidgetByName("ORDERSTART").setDisplayValue(lps.getOrderS());
			form.getFormWidgetByName("ORDEREND").setDisplayValue(lps.getOrderE());
			form.getFormWidgetByName("FLOWTHRUSTART").setDisplayValue(lps.getFlowTS());
			form.getFormWidgetByName("FLOWTHRUEND").setDisplayValue(lps.getFlowTE());
			form.getFormWidgetByName("TRANSSHIPSTART").setDisplayValue(lps.getTransS());
			form.getFormWidgetByName("TRANSSHIPEND").setDisplayValue(lps.getTransE());
			form.getFormWidgetByName("ORDERDATESTART").setCalendarValue(lps.getODateS());
			form.getFormWidgetByName("ORDERDATEEND").setCalendarValue(lps.getODateS());
			form.getFormWidgetByName("DELIVERYDATESTART").setCalendarValue(lps.getDDateS());
			form.getFormWidgetByName("DELIVERYDATEEND").setCalendarValue(lps.getDDateE());

			form.getFormWidgetByName("ROUTESTART").setDisplayValue(lps.getRouteS());
			form.getFormWidgetByName("ROUTEEND").setDisplayValue(lps.getRouteE());
			form.getFormWidgetByName("STOPSTART").setDisplayValue(lps.getStopS());
			form.getFormWidgetByName("STOPEND").setDisplayValue(lps.getStopE());
			form.getFormWidgetByName("LOADIDSTART").setDisplayValue(lps.getLoadIdS());
			form.getFormWidgetByName("LOADIDEND").setDisplayValue(lps.getLoadIdE());
			form.getFormWidgetByName("EXTERNALLOADIDSTART").setDisplayValue(lps.getExternalLoadIdS());
			form.getFormWidgetByName("EXTERNALLOADIDEND").setDisplayValue(lps.getExternalLoadIdE());
			form.getFormWidgetByName("OUTBOUNDLANESTART").setDisplayValue(lps.getOutboundLaneS());
			form.getFormWidgetByName("OUTBOUNDLANEEND").setDisplayValue(lps.getOutboundLaneE());
			form.getFormWidgetByName("TRANSSHIPASNSTART").setDisplayValue(lps.getTransShipASNS());
			form.getFormWidgetByName("TRANSSHIPASNEND").setDisplayValue(lps.getTransShipASNE());
			
			form.getFormWidgetByName("TYPESTART").setValue(lps.getTypeS());
			form.getFormWidgetByName("TYPEEND").setValue(lps.getTypeE());



			
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
