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


package com.ssaglobal.scm.wms.util.upload;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.wm_file_upload.FileUploadDetailFormPrerenderAction;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class UploadFormPrerenderAction extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(UploadFormPrerenderAction.class);
	private static final String EXCELTYPE="application/vnd.ms-excel";
	
    /**
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

    	_log.debug("LOG_SYSTEM_OUT","IMPORTFILE UploadFormPrerenderAction UIRenderContext",100L);
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPNORMFRMPREREN","Executing UploadFormPrerenderAction",100L);		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();
		
		if(focus == null){
			_log.debug("LOG_SYSTEM_OUT","[UploadFormPrerender]focus is null.",100L);
			return RET_CONTINUE;
		}
			
		_log.debug("LOG_SYSTEM_OUT","[UploadFormPrerender]focus type:"+focus.getBeanType(),100L);
		
		if(focus.isTempBio()){
			form.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
	
			String whereClause = "wm_filetype.MIMETYPE ='"+ EXCELTYPE +"'";
			Query query = new Query("wm_filetype", whereClause, "");
			BioCollection fileTypeList = uow.getBioCollectionBean(query);
			if (fileTypeList.size()>0){
				focus.setValue("mime_type",fileTypeList.elementAt(0).get("FILETYPEPK"));
			}
	
		}else{
			_log.debug("LOG_SYSTEM_OUT","IMPORTFILE UploadFormPrerenderAction enabling FILE",100L);
			form.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			
			/**
			Query bioQry = new Query("wm_importfile","wm_importfile.content_id = '"+focus.getValue("content_id")+"'","");
			BioCollection fileUploadRecords = uow.getBioCollectionBean(bioQry);
			
			if(fileUploadRecords == null || fileUploadRecords.size() == 0)
				return RET_CONTINUE;
			
			
			_log.debug("LOG_SYSTEM_OUT","UploadFormPrerenderAction TYPE found:"+(String)fileUploadRecords.elementAt(0).get("TYPE"),100L); //jp
			
			focus.setValue("mime_type",fileUploadRecords.elementAt(0).get("TYPE"));
			**/
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPFRMPREREN","Exiting UploadFormPrerenderAction",100L);
		return RET_CONTINUE;
    }

    /**
     * Called in response to the pre-render event on a form in a modal window. Write code
     * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
     * to the end user
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

    	_log.debug("LOG_SYSTEM_OUT","IMPORTFILE UploadFormPrerenderAction ModalUIRender",100L);
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPNORMFRMPREREN","Executing UploadFormPrerenderAction",100L);		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();
		
		if(focus == null)
			return RET_CONTINUE;
		
		
		if(focus.isTempBio()){
			form.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			return RET_CONTINUE;
		}else{
			_log.debug("LOG_SYSTEM_OUT","IMPORTFILE UploadFormPrerenderAction enabling FILE",100L);
			form.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
		}
		Query bioQry = new Query("wm_importfile","wm_importfile.content_id = '"+focus.getValue("content_id")+"'","");
		BioCollection fileUploadRecords = uow.getBioCollectionBean(bioQry);
		
		if(fileUploadRecords == null || fileUploadRecords.size() == 0)
			return RET_CONTINUE;
		
		focus.setValue("mime_type",fileUploadRecords.elementAt(0).get("TYPE"));
		
		_log.debug("LOG_DEBUG_EXTENSION_FILEUPFRMPREREN","Exiting UploadFormPrerenderAction",100L);
		return RET_CONTINUE;
       
       
    }
}

