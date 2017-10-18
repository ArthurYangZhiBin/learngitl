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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;

import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InternalTransferSerialNumberBranchListSelect extends ActionExtensionBase{
	protected static String SERIAL = "serial";
	protected static String NON_SERIAL = "nonSerial";
	protected static String SERIALTEMP = "serialTemp";
	protected static String TDSTEMP = "TDSTEMP";
	protected static String TDS = "TRANSFERDETAILSERIAL";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferSerialNumberBranchListSelect.class);
	
	protected int execute(ActionContext context, ActionResult result) throws FormException, EpiDataException{
        StateInterface state = context.getState();
        String bioRefString = state.getBucketValueString("listTagBucket");
        BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
        UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
        BioBean bioBean = null;
        try{
            bioBean = uowb.getBioBean(bioRef);
            result.setFocus(bioBean);
        }catch(BioNotFoundException bioEx){
            _logger.error(bioEx);
            throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
        }
		if(bioBean.getBioCollection(TDS).size()>0){
			_log.debug("LOG_SYSTEM_OUT","SHOULD BE TRANSFER DETAIL SERIAL",100L);
			context.setNavigation(SERIAL);
		}else if(bioBean.getBioCollection(TDSTEMP).size()>0){
			_log.debug("LOG_SYSTEM_OUT","SHOULD BE TRANSFER DETAIL SERIAL TEMP",100L);
			context.setNavigation(SERIALTEMP);
		}else{
			_log.debug("LOG_SYSTEM_OUT","SHOULD BE TRANSFER DETAIL",100L);
			context.setNavigation(NON_SERIAL);
		}
		return RET_CONTINUE;
	}
}