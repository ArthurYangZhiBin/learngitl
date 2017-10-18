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
package com.ssaglobal.scm.wms.wm_facility_configuration.ui;


import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.LottableValidations;
import com.ssaglobal.scm.wms.wm_dropid.ui.DropIDGenerateDropIDContentsReportURL;

public class ValidateAndSetNav extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateAndSetNav.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		HttpSession session = context.getState().getRequest().getSession();
		RuntimeListFormInterface facilityListForm= (RuntimeListFormInterface) context.getState().getCurrentRuntimeForm().getParentForm(context.getState());
		BioCollectionBean detailList = (BioCollectionBean)facilityListForm.getFocus();
		_log.debug("LOG_SYSTEM_OUT","SIZE of the Biocollection"+ detailList.size(),100L);
		boolean isActiveUpdate = false;
		boolean db_aliasUpdated = false;
		for (int i = 0; i < detailList.size(); i++)
		{

			BioBean facilityBioBean = (BioBean)detailList.elementAt(i);
			if (facilityBioBean.getValue("db_alias").toString().trim().equalsIgnoreCase("")){
				throw new UserException("WM_EXP_INVALIDALIAS", new Object[]{});
			}
			if (facilityBioBean.hasBeenUpdated("isActive")){
				isActiveUpdate = true;
				if (facilityBioBean.getValue("isActive").toString().equalsIgnoreCase("1")){
					session.setAttribute("ERRORMSG1", "WM_EXP_FACILITY_WARN1");
					context.setNavigation("clickEvent1381");
					_log.debug("LOG_SYSTEM_OUT","Inside error1",100L);
				}else{
					session.setAttribute("ERRORMSG3", "WM_EXP_EACILITY_WARN3");
					context.setNavigation("clickEvent1381");
					_log.debug("LOG_SYSTEM_OUT","Inside error3",100L);
				}
			}else{
				if (facilityBioBean.hasBeenUpdated("db_alias")){
					db_aliasUpdated = true;
					_log.debug("LOG_SYSTEM_OUT","Inside db_alias update",100L);
				}
			}

			_log.debug("LOG_SYSTEM_OUT","Action out",100L);
		}
		if (! isActiveUpdate && ! db_aliasUpdated){
			_log.debug("LOG_SYSTEM_OUT","Inside noupdate",100L);
			context.setNavigation("clickEvent1609");
		}

		if (db_aliasUpdated && !isActiveUpdate){
			context.setNavigation("clickEvent1609");
			context.getState().getDefaultUnitOfWork().saveUOW(true);	
		}
        return RET_CONTINUE;
	}
}