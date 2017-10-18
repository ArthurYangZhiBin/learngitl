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
package com.ssaglobal.scm.wms.wm_file_upload;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;


public class FileUploadPopUpPrerenderAction extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FileUploadPopUpPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPNORMFRMPREREN","Executing FileUploadDetailFormPrerenderAction",100L);	
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();		
		if(focus == null)
			return RET_CONTINUE;
		
		if(focus.isTempBio()){
			form.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);			
			return RET_CONTINUE;
		}
		else{
			Query bioQry = new Query("wm_fileupload","wm_fileupload.content_id = '"+focus.getValue("content_id")+"'","");
			BioCollection fileUploadRecords = uow.getBioCollectionBean(bioQry);
			
			if(fileUploadRecords == null || fileUploadRecords.size() == 0)
				return RET_CONTINUE;
			
			focus.setValue("locale",fileUploadRecords.elementAt(0).get("LOCALE"));
		}
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPFRMPREREN","Exiting FileUploadDetailFormPrerenderAction",100L);
		return RET_CONTINUE;
	}
}