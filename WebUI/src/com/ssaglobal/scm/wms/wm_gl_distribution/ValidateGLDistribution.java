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
package com.ssaglobal.scm.wms.wm_gl_distribution;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateGLDistribution extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateGLDistribution.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Executing ValidateGLDistribution",100L);			
		StateInterface state = context.getState();					
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_gl_distribution_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("wm_gl_distribution_details");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_gl_distribution_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Found Detail Form:Null",100L);			
		String glDistributionKey = headerForm.getFormWidgetByName("GLDISTRIBUTIONKEY").getDisplayValue();
		if(headerForm.getFocus().isTempBio()){						
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Checking for duplicate key:"+glDistributionKey,100L);			
			Query loadBiosQry = new Query("wm_gldistribution", "wm_gldistribution.GLDISTRIBUTIONKEY = '"+glDistributionKey.toUpperCase()+"'", null);				
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);													
			try {
				if(bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","key in use...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);					
					String args[] = {glDistributionKey}; 
					String errorMsg = getTextMessage("WMEXP_GLDIST_DUP_CODE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {			
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		if(detailForm != null){
			Object percObj = detailForm.getFormWidgetByName("GLDISTRIBUTIONPCT");			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","percObj:"+percObj,100L);
			//Validate Percent
			if(percObj != null){				
				DataBean bio = detailForm.getFocus();
				String percStr = ((RuntimeWidget)percObj).getDisplayValue();
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","percStr:"+percStr,100L);				
				percStr = percStr.trim();
				Double perc = null;
				
				//Percent must be between 0 and 100 percent
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Test if percent is between 0 and 100",100L);				
				if(percStr.indexOf("%") > -1){
					String[] percStrAry = percStr.split("%");
					if(percStrAry.length > 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
						String[] args = {percStr};
						String errorMsg = getTextMessage("Perc_Validation",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);	
					}			
					try {
						double percPrim = Double.parseDouble(percStrAry[0]);
						if(percPrim >= 0 && percPrim <= 100){
							perc = new Double(percPrim/100);
							//bio.setValue("GLDISTRIBUTIONPCT",perc);
						}else{
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
							String[] args = {percStr};
							String errorMsg = getTextMessage("Perc_Validation",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
						String[] args = {percStr};
						String errorMsg = getTextMessage("Perc_Validation",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}  
				}
				else{
					try {
						double percPrim = Double.parseDouble(percStr);
						if(percPrim >= 0 && percPrim <= 100){
							perc = new java.lang.Double(percPrim/100);
							//bio.setValue("GLDISTRIBUTIONPCT",perc);
						}else{
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
							String[] errMsg = {percStr};
							throw new UserException("Perc_Validation",errMsg);
						}
					} catch (NumberFormatException e) {
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
						String[] args = {percStr};
						String errorMsg = getTextMessage("Perc_Validation",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);						
					}
				}
				
				//sum of detail percentages must be between 0 and 100				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Test if sum of percentages is between 0 and 100",100L);
				String glDistributionLine = detailForm.getFormWidgetByName("GLDISTRIBUTIONLINENUMBER").getDisplayValue();
				Query loadBiosQry = new Query("wm_gldistributiondetail", "wm_gldistributiondetail.GLDISTRIBUTIONKEY = '"+glDistributionKey.toUpperCase()+"' AND wm_gldistributiondetail.GLDISTRIBUTIONLINENUMBER != '"+glDistributionLine+"'", null);				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Gl Distribution Detail Records Found...",100L);						
						String sumStr = bioCollection.sum("GLDISTRIBUTIONPCT").toString();
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Pct Sum For Detail Records:"+sumStr,100L);						
						double sumPct = Double.parseDouble(sumStr);				
						if(perc != null){
							sumPct += perc.doubleValue();
						}
						if(0 <= sumPct && sumPct <= 1){
							bio.setValue("GLDISTRIBUTIONPCT",perc);
						}else{
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
							String[] args = {percStr};
							String errorMsg = getTextMessage("Perc_Validation_Sum",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
					else if(perc != null){
						bio.setValue("GLDISTRIBUTIONPCT",perc);
					}
				} catch (EpiDataException e) {			
					e.printStackTrace();
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEGLDIST","Exiting ValidateGLDistribution",100L);
		return RET_CONTINUE;
		
	}	
}