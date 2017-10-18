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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.GUIDFactory;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.FormUtil;

public class FileUploadPopupSave extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(FileUploadPopupSave.class);

    
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	
		
		StateInterface state = context.getState();
		String fileType = (String)getParameter("TYPE");
		String screen = (String)getParameter("SCREEN");
		RuntimeFormInterface fileUploadDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_file_upload_from_implementing_screen_popup",state);
		//_log.debug("LOG_SYSTEM_OUT","\n\nFILE2:"+fileUploadDetailForm.getFormWidgetByName("FILE2").getValue()+"\n\n",100L);
		if(fileUploadDetailForm == null)
			return RET_CONTINUE;
		DataBean fileUploadBean = fileUploadDetailForm.getFocus();
		if(fileUploadBean == null)
			return RET_CONTINUE;
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		if(fileUploadBean.isTempBio()){			
			QBEBioBean newFileUploadRecord = (QBEBioBean)fileUploadBean;
			newFileUploadRecord.set("content_id",GUIDFactory.getGUIDStatic());
			newFileUploadRecord.set("data_source","FILE");
			newFileUploadRecord.set("revision_number","1");
			newFileUploadRecord.set("obsolete_flag","0");
			newFileUploadRecord.set("is_url_flag","0");
			newFileUploadRecord.set("content_num","0");
			DataBean fileRecord = (DataBean)newFileUploadRecord.get("content_data");
			if(fileRecord != null){				
				newFileUploadRecord.set("content_data_id",fileRecord.getValue("content_data_id"));
			}			
			
			QBEBioBean newWhseFileUploadRecord = uow.getQBEBioWithDefaults("wm_fileupload");
			newWhseFileUploadRecord.set("FILEUPLOADPK",GUIDFactory.getGUIDStatic());
			newWhseFileUploadRecord.set("content_id",newFileUploadRecord.get("content_id"));
			newWhseFileUploadRecord.set("SHORTTXT",newFileUploadRecord.get("content_name"));
			newWhseFileUploadRecord.set("LONGTXT",newFileUploadRecord.get("description"));
			newWhseFileUploadRecord.set("TYPE",fileType);
			newWhseFileUploadRecord.set("LOCALE",newFileUploadRecord.get("locale"));
			
			//Get MIME Type from FILETYPES table and load value in content.mime_type column
			Query bioQry = new Query("wm_filetype","wm_filetype.FILETYPEPK = '"+fileType+"'","");
			BioCollection fileTypes = uow.getBioCollectionBean(bioQry);
			_log.debug("LOG_SYSTEM_OUT","\n\nSAVING1!!!!\n\n",100L);
			if(fileTypes == null || fileTypes.size() == 0){
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			newFileUploadRecord.set("mime_type",fileTypes.elementAt(0).get("MIMETYPE"));
			_log.debug("LOG_SYSTEM_OUT","\n\nSAVING2!!!!\n\n",100L);
			//Save Screens That the File May Be Attached To
			
			QBEBioBean newScreenMappingRecord = uow.getQBEBioWithDefaults("wm_filescreenmapping");
			newScreenMappingRecord.set("FILESCREENMAPPINGPK",GUIDFactory.getGUIDStatic());
			newScreenMappingRecord.set("FILEID",newWhseFileUploadRecord.get("FILEUPLOADPK"));
			newScreenMappingRecord.set("SCREEN",screen);
			
		}
		else{			
			BioBean existingFileUploadRecord = (BioBean)fileUploadBean;
			DataBean fileRecord = (DataBean)existingFileUploadRecord.get("content_data");
			if(fileRecord != null){				
				existingFileUploadRecord.set("content_data_id",fileRecord.getValue("content_data_id"));
			}
			Query bioQry = new Query("wm_fileupload","wm_fileupload.content_id = '"+existingFileUploadRecord.get("content_id")+"'","");
			BioCollection existingWsheFileUploadRecords = uow.getBioCollectionBean(bioQry); 
			BioBean existingWsheFileUploadRecord = (BioBean)existingWsheFileUploadRecords.elementAt(0);
			existingWsheFileUploadRecord.set("content_id",existingFileUploadRecord.get("content_id"));
			existingWsheFileUploadRecord.set("SHORTTXT",existingFileUploadRecord.get("content_name"));
			existingWsheFileUploadRecord.set("LONGTXT",existingFileUploadRecord.get("description"));	
			if(existingFileUploadRecord.hasBeenUpdated("locale"))
				existingWsheFileUploadRecord.set("LOCALE",existingFileUploadRecord.get("locale"));
		
		}
		
		_log.debug("LOG_SYSTEM_OUT","\n\nSAVING!!!!\n\n",100L);	
		try{
			uow.saveUOW(true);
		}catch (UnitOfWorkException e){
			_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		}
		uow.clearState();
	    result.setFocus(fileUploadBean);


	return RET_CONTINUE;

}
}
