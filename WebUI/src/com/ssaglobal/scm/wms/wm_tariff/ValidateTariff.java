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
import java.math.BigDecimal;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.UIBeanManager;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateTariff extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateTariff.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Executing ValidateTariff",100L);		
		StateInterface state = context.getState();			
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_tariff_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("wm_tariff_detail");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_tariff_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Found Detail Form:Null",100L);			
		
		//Chk header required fields
		
		if(headerForm != null){
			Object tariffKeyObj = headerForm.getFormWidgetByName("TARIFFKEY");
			Object periodTypeObj = headerForm.getFormWidgetByName("PERIODTYPE");
			Object rsperiodTypeObj = headerForm.getFormWidgetByName("RSPERIODTYPE");
			Object initialStoragePeriodObj = headerForm.getFormWidgetByName("INITIALSTORAGEPERIOD");
			Object recurringStoragePeriodObj = headerForm.getFormWidgetByName("RECURRINGSTORAGEPERIOD");
			Object splitMonthDayObj = headerForm.getFormWidgetByName("SPLITMONTHDAY");
			Object splitMonthPercentObj = headerForm.getFormWidgetByName("SPLITMONTHPERCENT");
			Object splitMonthPercentBeforeObj = headerForm.getFormWidgetByName("SPLITMONTHPERCENTBEFORE");
			
			String tariffKey = tariffKeyObj == null || ((RuntimeWidget)tariffKeyObj).getDisplayValue() == null?"":((RuntimeWidget)tariffKeyObj).getDisplayValue();
			String periodType = periodTypeObj == null || ((RuntimeWidget)periodTypeObj).getValue() == null?"":((RuntimeWidget)periodTypeObj).getValue().toString();
			String rsperiodType = rsperiodTypeObj == null || ((RuntimeWidget)rsperiodTypeObj).getValue() == null?"":((RuntimeWidget)rsperiodTypeObj).getValue().toString();
			String initialStoragePeriod = initialStoragePeriodObj == null || ((RuntimeWidget)initialStoragePeriodObj).getDisplayValue() == null?"":((RuntimeWidget)initialStoragePeriodObj).getDisplayValue();
			String recurringStoragePeriod = recurringStoragePeriodObj == null || ((RuntimeWidget)recurringStoragePeriodObj).getDisplayValue() == null?"":((RuntimeWidget)recurringStoragePeriodObj).getDisplayValue();			
			String splitMonthDay = splitMonthDayObj == null || ((RuntimeWidget)splitMonthDayObj).getDisplayValue() == null?"":((RuntimeWidget)splitMonthDayObj).getDisplayValue();
			String splitMonthPercent = splitMonthPercentObj == null || ((RuntimeWidget)splitMonthPercentObj).getDisplayValue() == null?"":((RuntimeWidget)splitMonthPercentObj).getDisplayValue();
			String splitMonthPercentBefore = splitMonthPercentBeforeObj == null || ((RuntimeWidget)splitMonthPercentBeforeObj).getDisplayValue() == null?"":((RuntimeWidget)splitMonthPercentBeforeObj).getDisplayValue();			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","tariffKeyObj:"+tariffKeyObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rsperiodTypeObj:"+rsperiodTypeObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","initialStoragePeriodObj:"+initialStoragePeriodObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","recurringStoragePeriodObj:"+recurringStoragePeriodObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthDayObj:"+splitMonthDayObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthPercentObj:"+splitMonthPercentObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthPercentBeforeObj:"+splitMonthPercentBeforeObj,100L);
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","tariffKey:"+tariffKey,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","periodType:"+periodType,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rsperiodType:"+rsperiodType,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","initialStoragePeriod:"+initialStoragePeriod,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","recurringStoragePeriod:"+recurringStoragePeriod,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthDay:"+splitMonthDay,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthPercent:"+splitMonthPercent,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","splitMonthPercentBefore:"+splitMonthPercentBefore,100L);			
			
			//pass null for any hidden field
			if(periodType.equals("F")){				
				try {
					double isp = Double.parseDouble(initialStoragePeriod); 
					if(isp <= 0 || isp >= 999){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","isp invlaid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);						
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_ISP_INVALID_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","isp invlaid...",100L);	
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TARIFF_ISP_INVALID_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}else{
				headerForm.getFocus().setValue("INITIALSTORAGEPERIOD",null);
			}
			
			if(	periodType.equals("S") ||
					periodType.equals("C") ||
					rsperiodType.equals("S") ||
					rsperiodType.equals("C")){
				
				try {
					int splitMonthDayInt = Integer.parseInt(splitMonthDay);
					if(splitMonthDayInt < 1 || splitMonthDayInt > 31){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Split Month Day invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_SMD_INVALID_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","isp invlaid...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TARIFF_SMD_INVALID_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
				
				try {
					double splitMonthDayPctDouble = Double.parseDouble(splitMonthPercent);
					if(splitMonthDayPctDouble < 0 || splitMonthDayPctDouble > 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Split Month Day invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_SMDP_INVALID_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","isp invlaid...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TARIFF_SMDP_INVALID_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
				
				try {
					double splitMonthDayPctBeforeDouble = Double.parseDouble(splitMonthPercentBefore);	
					if(splitMonthDayPctBeforeDouble < 0 || splitMonthDayPctBeforeDouble > 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Split Month Day invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_SMDPB_INVALID_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","isp invlaid...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TARIFF_SMDPB_INVALID_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 													
			}
			else{				
				headerForm.getFocus().setValue("SPLITMONTHDAY",new Integer("15"));
				headerForm.getFocus().setValue("SPLITMONTHPERCENT",new BigDecimal(".5"));
				headerForm.getFocus().setValue("SPLITMONTHPERCENTBEFORE",new BigDecimal("1.0"));
			}
			
			if(rsperiodType.equals("F")){
				try {
					double rsp = Double.parseDouble(recurringStoragePeriod); 
					if(rsp <= 0 || rsp >= 999){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rsp invlaid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_RSP_INVALID_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rsp invlaid...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TARIFF_RSP_INVALID_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 									
			}
			else{
				headerForm.getFocus().setValue("RECURRINGSTORAGEPERIOD",null);
			}
			
			if(	!(periodType.equals("C") || periodType.equals("C"))){
				headerForm.getFocus().setValue("CALENDARGROUP",null);				
			}			
			
//			Validate Tariff Key
			if(headerForm.getFocus().isTempBio()){
				if(tariffKeyObj != null){								
					if(tariffKey != null){
						//tariffKey = tariffKey.trim();
						if(tariffKey.length() > 0){
							Query loadBiosQry = new Query("wm_tariff", "wm_tariff.TARIFFKEY = '"+tariffKey.toUpperCase()+"'", "");
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
							try {
								if(bioCollection.size() > 0){
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Key "+tariffKey+" in use...",100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Leaving ValidateTariff",100L);									
									String args[] = new String[1]; 						
									args[0] = tariffKey;
									String errorMsg = getTextMessage("WMEXP_TARIFFKEY_IN_USE",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (EpiDataException e) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Leaving ValidateTariff",100L);
								e.printStackTrace();
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}	
			}
		}
		
		//Chk detail required fields
		if(detailForm != null ){
			boolean validateDetail = true;
			if(detailForm.getFocus().isTempBio()){
				QBEBioBean bio = (QBEBioBean)detailForm.getFocus();
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","STATE:"+context.getState(),100L);				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Is Empty:"+bio.isEmpty(),100L);				
				if(bio.isEmpty())
					validateDetail = false;
			}
			else{
				BioBean bio = (BioBean)detailForm.getFocus();	
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","STATE:"+context.getState(),100L);				
				if(bio.getUpdatedAttributes() == null || bio.getUpdatedAttributes().size() == 0)
					validateDetail = false;
			}
			
			
			if(validateDetail){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOW:"+detailForm.getFocus().getUnitOfWorkBean(),100L);			
				Object costMasterUnitsObj = detailForm.getFormWidgetByName("COSTMASTERUNITS");
				Object costMasterRateObj = detailForm.getFormWidgetByName("COSTRATE");
				Object masterUnitsObj = detailForm.getFormWidgetByName("MASTERUNITS");
				Object minChargeObj = detailForm.getFormWidgetByName("MINIMUMCHARGE");
				Object rateObj = detailForm.getFormWidgetByName("RATE");
				
				Object chargeTypeObj = detailForm.getFormWidgetByName("CHARGETYPE");			
				Object uom1MultObj = detailForm.getFormWidgetByName("UOM1MULT");
				Object uom2MultObj = detailForm.getFormWidgetByName("UOM2MULT");
				Object uom3MultObj = detailForm.getFormWidgetByName("UOM3MULT");
				Object uom4MultObj = detailForm.getFormWidgetByName("UOM4MULT");
				
				String costMasterUnits = costMasterUnitsObj == null || ((RuntimeWidget)costMasterUnitsObj).getDisplayValue() == null?"":((RuntimeWidget)costMasterUnitsObj).getDisplayValue().toString();
				String costMasterRate = costMasterRateObj == null || ((RuntimeWidget)costMasterRateObj).getDisplayValue() == null?"":((RuntimeWidget)costMasterRateObj).getDisplayValue();
				String masterUnits = masterUnitsObj == null || ((RuntimeWidget)masterUnitsObj).getDisplayValue() == null?"":((RuntimeWidget)masterUnitsObj).getDisplayValue().toString();
				String minCharge = minChargeObj == null || ((RuntimeWidget)minChargeObj).getDisplayValue() == null?"":((RuntimeWidget)minChargeObj).getDisplayValue();
				String rate = rateObj == null || ((RuntimeWidget)rateObj).getDisplayValue() == null?"":((RuntimeWidget)rateObj).getDisplayValue().toString();
				
				String chargeType = chargeTypeObj == null || ((RuntimeWidget)chargeTypeObj).getValue() == null?"":((RuntimeWidget)chargeTypeObj).getValue().toString();
				String uom1Mult = uom1MultObj == null || ((RuntimeWidget)uom1MultObj).getDisplayValue() == null?"":((RuntimeWidget)uom1MultObj).getDisplayValue();
				String uom2Mult = uom2MultObj == null || ((RuntimeWidget)uom2MultObj).getDisplayValue() == null?"":((RuntimeWidget)uom2MultObj).getDisplayValue();
				String uom3Mult = uom3MultObj == null || ((RuntimeWidget)uom3MultObj).getDisplayValue() == null?"":((RuntimeWidget)uom3MultObj).getDisplayValue();
				String uom4Mult = uom4MultObj == null || ((RuntimeWidget)uom4MultObj).getDisplayValue() == null?"":((RuntimeWidget)uom4MultObj).getDisplayValue();
				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","costMasterUnitsObj:"+costMasterUnitsObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","costMasterRateObj:"+costMasterRateObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","masterUnitsObj:"+masterUnitsObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","minChargeObj:"+minChargeObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rateObj:"+rateObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","chargeTypeObj:"+chargeTypeObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom1MultObj:"+uom1MultObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom2MultObj:"+uom2MultObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom3MultObj:"+uom3MultObj,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom4MultObj:"+uom4MultObj,100L);
								
				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","costMasterUnits:"+costMasterUnits,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","costMasterRate:"+costMasterRate,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","masterUnits:"+masterUnits,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","minCharge:"+minCharge,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","rate:"+rate,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","chargeType:"+chargeType,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom1Mult:"+uom1Mult,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom2Mult:"+uom2Mult,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom3Mult:"+uom3Mult,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","uom4Mult:"+uom4Mult,100L);
				
				
				//validate cost master units
				if(costMasterUnits.length() > 0){
					try {
						double costMasterUnitsNumber = Double.parseDouble(costMasterUnits);
						if(costMasterUnitsNumber <= 0){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Cost Master Units invlaid...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_CMU_INVALID_A",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Cost Master Units invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_CMU_INVALID_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				//validate cost master rate
				if(costMasterRate.length() > 0){
					try {
						double costMasterRateNumber = Double.parseDouble(costMasterRate);
						if(costMasterRateNumber < 0){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Cost Master Rate invlaid...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_CMR_INVALID_A",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Cost Master Rate invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_CMR_INVALID_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				//validate master units
				if(masterUnits.length() > 0){
					try {
						double masterUnitsNumber = Double.parseDouble(masterUnits);
						if(masterUnitsNumber <= 0){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Master Units invlaid...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_MU_INVALID_A",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Master Units invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_MU_INVALID_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				//validate min charge
				if(minCharge.length() > 0){
					try {
						double minChargeNumber = Double.parseDouble(minCharge);
						if(minChargeNumber < 0){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Min Charge invlaid...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_MC_INVALID_A",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Min Charge invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_MC_INVALID_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
//				validate rate charge
				if(rate.length() > 0){
					try {
						double rateNumber = Double.parseDouble(rate);
						if(rateNumber < 0){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Rate invlaid...",100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_RATE_INVALID_A",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Rate invlaid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_RATE_INVALID_B",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
				
				if(chargeType.length() > 0){
					if(chargeType.equals("HI") || chargeType.equals("HO")){
						//validate uom1
						if(uom1Mult.length() > 0){
							try {
								double uom1MultNumber = Double.parseDouble(uom1Mult);
								if(uom1MultNumber <= 0){
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 1 invlaid...",100L);									
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
									String args[] = new String[0]; 
									String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM1_INVALID_A",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (NumberFormatException e) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Min Charge invlaid...",100L);								
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM1_INVALID_B",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
						//validate uom2
						if(uom2Mult.length() > 0){
							try {
								double uom2MultNumber = Double.parseDouble(uom2Mult);
								if(uom2MultNumber <= 0){
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 2 invlaid...",100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
									String args[] = new String[0]; 
									String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM2_INVALID_A",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (NumberFormatException e) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 2 invlaid...",100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM2_INVALID_B",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
						//validate uom3
						if(uom3Mult.length() > 0){
							try {
								double uom3MultNumber = Double.parseDouble(uom3Mult);
								if(uom3MultNumber <= 0){
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 3 invlaid...",100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
									String args[] = new String[0]; 
									String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM3_INVALID_A",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (NumberFormatException e) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 3 invlaid...",100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM3_INVALID_B",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
						//validate uom4
						if(uom4Mult.length() > 0){
							try {
								double uom4MultNumber = Double.parseDouble(uom4Mult);
								if(uom4MultNumber <= 0){
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 4 invlaid...",100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
									String args[] = new String[0]; 
									String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM4_INVALID_A",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							} catch (NumberFormatException e) {
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","UOM Multiplier 4 invlaid...",100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Exiting ValidateTariff",100L);
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_TARIFF_DETAIL_UOM4_INVALID_B",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						}
					}
				}
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETARIFF","Leaving ValidateTariff",100L);
		return RET_CONTINUE;
		
	}	
}