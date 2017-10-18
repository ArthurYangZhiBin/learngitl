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


package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class CCDetailSerialPreValidate extends com.epiphany.shr.ui.action.ActionExtensionBase {

	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDetailSerialPreValidate.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		StateInterface state = context.getState();
		
		String rootForm = "wms_tbgrp_shell";
		RuntimeFormInterface detailForm = null;
		RuntimeFormInterface tbgrpForm  = FormUtil.findForm(state.getCurrentRuntimeForm(),rootForm, "wms_tbgrp_shell", state);
		if (tbgrpForm!=null){
			detailForm  = FormUtil.findForm(state.getCurrentRuntimeForm(),
					rootForm,"wm_cyclecount_detail_view", state);

		}else{
			detailForm = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		}
		
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialPreValidate]detailForm:"+detailForm.getName(),100L);
		
		
		String lot = null;
		String id = null;
		
		
		Bio ccdetailBean =null;
		DataBean bean = detailForm.getFocus();
		
		if(bean.isTempBio()){
			QBEBioBean qbeBean =(QBEBioBean)bean;
			lot = (String)qbeBean.get("LOT");
			id = (String)qbeBean.get("ID");
		}else{
			BioBean bioBean = (BioBean)bean;
			lot = (String)bioBean.get("LOT");
			id = (String)bioBean.get("ID");

		}
			
		//String lot = (String)ccdetailBean.get("LOT");
				
		
		//String lot = (String)detailForm.getFormWidgetByName("LOT").getValue();
		if(lot==null || lot.trim().length()==0){
			throw new UserException("WMEXP_CC_MISSING_FIELD",new String[]{"LOT"});
		}
		
		
		//String id = (String)detailForm.getFormWidgetByName("ID").getValue();
		//String id = (String)ccdetailBean.get("ID");
		
		if(id==null || id.trim().length()==0)
			throw new UserException("WMEXP_CC_MISSING_FIELD",new String[]{"LPN"});
		
		return RET_CONTINUE;
	}
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		try {
			// Add your code here to process the event
			
		} catch(Exception e) {
			
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		
		return RET_CONTINUE;
	}
}
