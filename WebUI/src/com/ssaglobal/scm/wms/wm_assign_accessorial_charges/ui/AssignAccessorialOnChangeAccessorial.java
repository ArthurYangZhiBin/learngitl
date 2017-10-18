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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_item.ui.ItemAssignLocationsTypeDropdownAction;

public class AssignAccessorialOnChangeAccessorial extends ActionExtensionBase{

	private static String VALID_ROUTINE = "ACCESSORIALDETAILKEY";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignAccessorialOnChangeAccessorial.class);
  


	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing AssignAccessorialOnChangeAccessorial",100L);
		  
		  double debit;
		  
		try
		{				
			StateInterface state = context.getState();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			DataBean currentFormFocus = state.getFocus();
			
			if (currentFormFocus instanceof BioBean)
				currentFormFocus = (BioBean) currentFormFocus;
			else
				currentFormFocus = (QBEBioBean) currentFormFocus;

			Object tempValue = currentFormFocus.getValue(VALID_ROUTINE);
			String keyVal = null;
			if (tempValue != null)
			{
				keyVal = tempValue.toString();
				_log.debug("LOG_SYSTEM_OUT","/// " + VALID_ROUTINE + " Dropdown, Value : " + keyVal,100L);
				String query = "wm_accessorialdetail.ACCESSORIALDETAILKEY ='" +keyVal +"'";
				Query qry = new Query("wm_accessorialdetail", query, null);
				BioCollectionBean resultBean = uow.getBioCollectionBean(qry);
				  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Size: " +resultBean.size(),100L);
				if(resultBean.size() ==1)
				{
					//get bio values
					String gl = resultBean.get("0").getValue("GLDISTRIBUTIONKEY").toString();
					String tax = resultBean.get("0").getValue("TAXGROUPKEY").toString();
					String rate = resultBean.get("0").getValue("RATE").toString();
					String base = resultBean.get("0").getValue("BASE").toString();
					String units = resultBean.get("0").getValue("MASTERUNITS").toString();
					//

					String key = resultBean.get("0").getValue("ACCESSORIALKEY").toString();
					Query hdrQry= new Query("wm_accessorial", "wm_accessorial.ACCESSORIALKEY='" +key +"'", null);
					BioCollectionBean hdrBean = uow.getBioCollectionBean(hdrQry);
					String servicekey = hdrBean.get("0").getValue("SERVICEKEY").toString();
					
					  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","GL: " +gl,100L);
					  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","tax: " +tax,100L);
					  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","rate: " +rate,100L);
					  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","base: " +base ,100L);
					  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","units: " +units ,100L);
					  _log.debug("LOG_SYSTEM_OUT","\n\n\n\n****&&^^%%%$$$$SERVICEKEY: " +servicekey,100L);

					debit = calcDebit(currentFormFocus, rate);
															
					currentFormFocus.setValue("GLDISTRIBUTIONKEY", gl);
					currentFormFocus.setValue("TAXGROUPKEY", tax);
					currentFormFocus.setValue("RATE", rate);
					currentFormFocus.setValue("BASE", base);
					currentFormFocus.setValue("MASTERUNITS", units);
					currentFormFocus.setValue("DEBIT", Double.toString(debit));
					currentFormFocus.setValue("SERVICEKEY", servicekey);
					
				}
				
				
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Dropdown is null " ,100L);
			}
			
		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting AssignAccessorialOnChangeAccessorial",100L);
		return RET_CONTINUE;
	}



	private double calcDebit(DataBean currentFormFocus, String rate) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In calcDebit" ,100L);
		double billedVal, debitVal;
		
		if(currentFormFocus.isTempBio())
			currentFormFocus = (QBEBioBean)currentFormFocus;
		else
			currentFormFocus = (BioBean)currentFormFocus;
		
		Object billedUnits = currentFormFocus.getValue("BILLEDUNITS");
		if(!isNull(billedUnits))			
			billedVal = Double.parseDouble(billedUnits.toString());
		else
			billedVal = 0.0;
		
		
		debitVal= billedVal * Double.parseDouble(rate);
		
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","billed Units: " +billedVal ,100L);
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Debit Val: " +debitVal ,100L);
		
		return debitVal;
	}
	
	  private boolean isNull(Object widget) {
			// TODO Auto-generated method stub  
			boolean value;
			if(widget == null)			
				value=true;				
			else if(widget.toString().matches("\\s*"))
				value=true;
			else
				value=false;
			
			
			return value;
		}
	
}
