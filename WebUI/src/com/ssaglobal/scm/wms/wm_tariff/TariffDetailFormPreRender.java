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
package com.ssaglobal.scm.wms.wm_tariff;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class TariffDetailFormPreRender extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TariffDetailFormPreRender.class);
	public TariffDetailFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{	
		_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","Executing TariffDetailFormPreRender",100L);		
		String headerOrDetail = (String)getParameter("headerOrDetail");
		if(headerOrDetail.equals("header")){
			RuntimeFormWidgetInterface widgetCalendarGroup = form.getFormWidgetByName("CALENDARGROUP");
			RuntimeFormWidgetInterface widgetInitialStoragePeriod = form.getFormWidgetByName("INITIALSTORAGEPERIOD");		
			RuntimeFormWidgetInterface widgetRecurringStoragePeriod = form.getFormWidgetByName("RECURRINGSTORAGEPERIOD");
			RuntimeFormWidgetInterface widgetSplitMonthDay = form.getFormWidgetByName("SPLITMONTHDAY");	
			RuntimeFormWidgetInterface widgetSplitMonthPercent = form.getFormWidgetByName("SPLITMONTHPERCENT");
			RuntimeFormWidgetInterface widgetSplitMonthPercentBefore = form.getFormWidgetByName("SPLITMONTHPERCENTBEFORE");
			RuntimeFormWidgetInterface widgetISPeriod = form.getFormWidgetByName("PERIODTYPE");
			RuntimeFormWidgetInterface widgetRSPeriod = form.getFormWidgetByName("RSPERIODTYPE");
			String widgetISPeriodValue = widgetISPeriod.getValue() == null?"":widgetISPeriod.getValue().toString();
			String widgetRSPeriodValue = widgetRSPeriod.getValue() == null?"":widgetRSPeriod.getValue().toString();
			HttpSession session = context.getState().getRequest().getSession();
			if(session.getAttribute("PERIODTYPECHANGED") != null){				
				form.getFocus().setValue("PERIODTYPE",session.getAttribute("ISPPERIODTYPE"));				
			}		
			if(session.getAttribute("RSPERIODTYPECHANGED") != null){			
				form.getFocus().setValue("RSPERIODTYPE",session.getAttribute("ISPRSPERIODTYPE"));							
			}
			
			if(widgetISPeriod.getValue() == null && widgetRSPeriod.getValue() == null){
				widgetInitialStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetRecurringStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);			
				widgetSplitMonthDay.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetSplitMonthPercent.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetSplitMonthPercentBefore.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetCalendarGroup.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				form.getFocus().setValue("INITIALSTORAGEPERIOD","0");
				form.getFocus().setValue("RECURRINGSTORAGEPERIOD","0");
				form.getFocus().setValue("SPLITMONTHDAY","0");
				form.getFocus().setValue("SPLITMONTHPERCENT","0");
				form.getFocus().setValue("SPLITMONTHPERCENTBEFORE","0");
				form.getFocus().setValue("CALENDARGROUP","0");
				_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","Exiting TariffDetailFormPreRender",100L);				
				return RET_CONTINUE;
			}		
			
			if(widgetISPeriod.getValue() != null && widgetISPeriod.getValue().toString().equals("F")){
				widgetInitialStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
				widgetInitialStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
			else{
				widgetInitialStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetInitialStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFocus().setValue("INITIALSTORAGEPERIOD","0");
			}
			
			if(		widgetISPeriodValue.equals("S") ||
					widgetISPeriodValue.equals("C") ||					
					widgetRSPeriodValue.equals("S") ||
					widgetRSPeriodValue.equals("C")){
				
				widgetSplitMonthDay.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
				widgetSplitMonthDay.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				widgetSplitMonthPercent.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
				widgetSplitMonthPercent.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				widgetSplitMonthPercentBefore.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);	
				widgetSplitMonthPercentBefore.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
			else{
				widgetSplitMonthDay.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetSplitMonthDay.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				widgetSplitMonthPercent.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetSplitMonthPercent.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				widgetSplitMonthPercentBefore.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetSplitMonthPercentBefore.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFocus().setValue("SPLITMONTHDAY","0");
				form.getFocus().setValue("SPLITMONTHPERCENT","0");
				form.getFocus().setValue("SPLITMONTHPERCENTBEFORE","0");
			}
			
			if(widgetRSPeriod.getValue() != null && widgetRSPeriod.getValue().toString().equals("F")){
				widgetRecurringStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);	
				widgetRecurringStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
			else{
				widgetRecurringStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);	
				widgetRecurringStoragePeriod.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);	
				form.getFocus().setValue("RECURRINGSTORAGEPERIOD","0");
			}
			
			if(		widgetISPeriodValue.equals("C") ||				
					widgetRSPeriodValue.equals("C")){
				
				widgetCalendarGroup.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.FALSE);
				widgetCalendarGroup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
			else{
				widgetCalendarGroup.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
				widgetCalendarGroup.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFocus().setValue("CALENDARGROUP","0");	
			}
		}
		if(headerOrDetail.equals("detail")){
			RuntimeFormWidgetInterface widgetChargeType = form.getFormWidgetByName("CHARGETYPE");
			RuntimeFormWidgetInterface widgetMult1 = form.getFormWidgetByName("UOM1MULT");		
			RuntimeFormWidgetInterface widgetMult2 = form.getFormWidgetByName("UOM2MULT");		
			RuntimeFormWidgetInterface widgetMult3 = form.getFormWidgetByName("UOM3MULT");	
			RuntimeFormWidgetInterface widgetMult4 = form.getFormWidgetByName("UOM4MULT");
			RuntimeFormWidgetInterface widgetPackCharge = form.getFormWidgetByName("CHARGECODE");
			RuntimeFormWidgetInterface widgetRate = form.getFormWidgetByName("RATE");
			
			ArrayList chargeWidgets = new ArrayList();
			chargeWidgets.add("RATE");
			chargeWidgets.add("BASE");
			chargeWidgets.add("MASTERUNITS");
			chargeWidgets.add("UOMSHOW");
			chargeWidgets.add("ROUNDMASTERUNITS");
			chargeWidgets.add("MINIMUMCHARGE");
			chargeWidgets.add("MINIMUMGROUP");
			chargeWidgets.add("COSTRATE");
			chargeWidgets.add("COSTBASE");
			chargeWidgets.add("COSTMASTERUNITS");
			chargeWidgets.add("COSTUOMSHOW");
			chargeWidgets.add("UOM3MULT");
			chargeWidgets.add("UOM2MULT");
			chargeWidgets.add("UOM1MULT");
			chargeWidgets.add("UOM4MULT");
			
			

			if(widgetChargeType.getValue() != null){
				if(widgetChargeType.getValue().toString().equalsIgnoreCase("HI") || widgetChargeType.getValue().toString().equalsIgnoreCase("HO")){
					_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","1" + widgetChargeType.getValue(),100L);					
					widgetMult1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					widgetMult2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					widgetMult3.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					widgetMult4.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
					widgetPackCharge.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","2" + widgetChargeType.getValue(),100L);					
					widgetMult1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					widgetMult2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					widgetMult3.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					widgetMult4.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
					
					//disable CHARGECODE
					form.getFocus().setValue("CHARGECODE",null);
					widgetPackCharge.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					//enable widgets that may have been affected by CHARGECODE
					for(int i = 0; i < 11; i++){
						form.getFormWidgetByName((String)chargeWidgets.get(i)).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
				}
			}
			
			if((widgetPackCharge.getBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true) == false) ){
				if (widgetPackCharge.getValue() != null) {
					_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","3" + widgetPackCharge.getValue(),100L);					
					// set required on chargecode
					widgetPackCharge.setProperty("input type", "required");
					// remove required on rate
					widgetRate.setProperty("input type", "normal");
					
					// disable widgets
					for(int i = 0; i < chargeWidgets.size(); i++){
						form.getFormWidgetByName((String)chargeWidgets.get(i)).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					}
					
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","4" + widgetPackCharge.getValue(),100L);					
					// remove required on chargecode
					form.getFocus().setValue("CHARGECODE",null);
					widgetPackCharge.setProperty("input type", "normal");
					// set required on rate
					widgetRate.setProperty("input type", "required");
					// enable widgets
					for(int i = 0; i < chargeWidgets.size(); i++){
						form.getFormWidgetByName((String)chargeWidgets.get(i)).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
					
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","5" + widgetPackCharge.getValue(),100L);				
				//remove required on chargecode
				form.getFocus().setValue("CHARGECODE",null);
				widgetPackCharge.setProperty("input type", "normal");
				// set required on rate
				widgetRate.setProperty("input type", "required");
			}
			
	
		}
		_log.debug("LOG_DEBUG_EXTENSION_TARRIFDETFRMPREREN","Exiting TariffDetailFormPreRender",100L);
		return RET_CONTINUE;
	}
	
}