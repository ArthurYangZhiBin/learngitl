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
package com.ssaglobal.scm.wms.wm_tax_rates;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class TaxRatesValidation extends ActionExtensionBase{
	private static String ERROR_MESSAGE = "Invalid Percentage- ";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaxRatesValidation.class);
	
	public TaxRatesValidation()
	{
		_log.info("EXP_1", "SKUTableValidation has been instantiated...", SuggestedCategory.NONE);
		
	}
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Executing TaxRatesValidation",100L);		 	
		String[] errMsg = new String[1];
		try{
			StateInterface state = context.getState();
			
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			
			SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
			RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	
			_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Bean ---> "+detailForm.getFocus().getClass().getName(),100L);		    
			DataBean bio = detailForm.getFocus();				
			_log.info("EXP_1", "Start of TaxRateValidation", SuggestedCategory.NONE);
			
			String rateStr = (String) bio.getValue("Perc");
			errMsg[0] = rateStr;
			rateStr = rateStr.trim();
			java.lang.Double perc = null;
			if(rateStr.indexOf("%") > -1){
				String[] rateStrAry = rateStr.split("%");
				if(rateStrAry.length > 1){
					throw new UserException("Tax_Rate_Validation",errMsg);		
				}			
				try {
					double percPrim = java.lang.Double.parseDouble(rateStrAry[0]);
					if(percPrim >= 0 && percPrim < 100){
						perc = new java.lang.Double(percPrim/100);
						bio.setValue("RATE",perc);
					}else{
						throw new UserException("Tax_Rate_Validation",errMsg);
					}
				} catch (NumberFormatException e) {
					throw new UserException("Tax_Rate_Validation",errMsg);
				}  
			}
			else{
				try {
					double percPrim = java.lang.Double.parseDouble(rateStr);
					if(percPrim >= 0 && percPrim < 100){
						perc = new java.lang.Double(percPrim/100);
						bio.setValue("RATE",perc);
					}else{
						throw new UserException("Tax_Rate_Validation",errMsg);
					}
				} catch (NumberFormatException e) {
					throw new UserException("Tax_Rate_Validation",errMsg);
				}
			}
			if(detailForm.getFocus().isTempBio()){			
				String taxRateKey = detailForm.getFormWidgetByName("TAXRATEKEY").getDisplayValue();
				_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Checking for duplicate key:"+taxRateKey,100L);				
				Query loadBiosQry = new Query("wm_taxrate", "wm_taxrate.TAXRATEKEY = '"+taxRateKey.toUpperCase()+"'", null);				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
				try {
					if(bioCollection.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","key in use...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Exiting TaxRatesValidation",100L);						
						String args[] = {taxRateKey}; 
						String errorMsg = getTextMessage("WMEXP_TAXRATE_DUP_CODE",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {			
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Exiting TaxRatesValidation",100L);
			return RET_CONTINUE;		
		}catch(UserException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_TAXRATESVALIDATION","Exiting TaxRatesValidation",100L);
		return RET_CONTINUE;
		
	}
}
