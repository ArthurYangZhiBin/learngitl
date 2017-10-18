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
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.cm.bio.CMSHandle;
import com.epiphany.shr.cm.service.CMService;
import com.epiphany.shr.cm.util.CMSUtil;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.GUIDFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ImportFileSave extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private static final String STATUSNEW = "1";
	private static final String STATUSQUEUED = "2";
	private static final String EXCELTYPE = "application/vnd.ms-excel";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ImportFileSave.class);
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {

		StateInterface state = context.getState();
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		HttpSession session = state.getRequest().getSession();
		String templateId = (String)session.getAttribute("TEMPLATEID");
		session.removeAttribute("TEMPLATEID");
		
		String shell = getParameterString("Shell");
		String form = getParameterString("Form");
		boolean fromGeneric = getParameterBoolean("fromGeneric");
		
		String dbConnection =(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
		String dbEnterprise =(userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
		
		
		String theContentId=null;
		//RuntimeFormInterface ImportFileForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_upload_file",state);
		RuntimeFormInterface ImportFileForm = FormUtil.findForm(state.getCurrentRuntimeForm(), shell, form,state);
		
		
		_log.debug("LOG_SYSTEM_OUT","[ImportFileSave]Shell:"+shell+"Form:"+form,100L);
		_log.debug("LOG_SYSTEM_OUT","IMPORTFILESAVE Form name:"+ImportFileForm.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","[ImportFileSave]dbConnection:"+  (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(),100L);
		
		
		if(ImportFileForm == null)
			return RET_CONTINUE;
		DataBean contentBean = ImportFileForm.getFocus(); //wm_content
		if(contentBean == null)
			return RET_CONTINUE;
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		try {
			if(contentBean.isTempBio()){
				//Content
				QBEBioBean newContentBean = (QBEBioBean)contentBean;
				newContentBean.set("content_id",GUIDFactory.getGUIDStatic());
				newContentBean.set("data_source","FILE");
				newContentBean.set("revision_number","1");
				newContentBean.set("obsolete_flag","0");
				newContentBean.set("is_url_flag","0");
				newContentBean.set("content_num","0");
				DataBean fileRecord = (DataBean)newContentBean.get("content_data");
				if(fileRecord != null){
					_log.debug("LOG_SYSTEM_OUT","IMPORTFILE file was provided for upload!!",100L);
					newContentBean.set("content_data_id",fileRecord.getValue("content_data_id"));
				}			
				
				theContentId = (String)newContentBean.get("content_id");
				//ImportFile
				QBEBioBean newImportFileRecord = uow.getQBEBioWithDefaults("wm_importfile");
				newImportFileRecord.set("IMPORTFILEID",GUIDFactory.getGUIDStatic());
				newImportFileRecord.set("content_id",newContentBean.get("content_id"));
				newImportFileRecord.set("DESCRIPTION",newContentBean.get("description"));
				
				_log.debug("LOG_SYSTEM_OUT","IMPORTFILE wm_content.content_id:"+newContentBean.get("content_id"),100L);
				//String fileName = (String)ImportFileForm.getFormWidgetByName("FILE").getProperty("file name form widget");
				//_log.debug("LOG_SYSTEM_OUT","Filename:"+fileName,100L);
				//newImportFileRecord.set("FILENAME",fileName);
				newImportFileRecord.set("STATUS",STATUSNEW);
				newImportFileRecord.set("USERID", (userContext.get("logInUserId")).toString()); 
				if (templateId!=null)
					newImportFileRecord.set("TEMPLATEID",templateId); //to be fixed
				newContentBean.set("mime_type",EXCELTYPE);

				//newContentBean.set("DBNAME", dbConnection);
				//newContentBean.set("SUBMITTEDFROMENTERPRISE", dbEnterprise);
				newImportFileRecord.set("DBNAME", dbConnection);
				newImportFileRecord.set("SUBMITTEDFROMENTERPRISE", dbEnterprise);

				/**
				newImportFileRecord.set("TYPE",newContentBean.get("mime_type"));
								
				//Get MIME Type from FILETYPES table and load value in content.mime_type column
				Query bioQry = new Query("wm_filetype","wm_filetype.FILETYPEPK = '"+newContentBean.get("mime_type")+"'","");
				BioCollection fileTypes = uow.getBioCollectionBean(bioQry);
				
				if(fileTypes == null || fileTypes.size() == 0){
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				newContentBean.set("mime_type",fileTypes.elementAt(0).get("MIMETYPE"));
				**/
				
				
				String mimeType = UploadHelper.getMimeTypePK(EXCELTYPE,uow);
				if (mimeType == null){
					return RET_CANCEL;
				}else{
					newImportFileRecord.set("TYPE",mimeType);
				}
				
				
			}
			else{

				//Content
				_log.debug("LOG_SYSTEM_OUT","\n\nIMPORTFILE Update!!\n\n",100L);
				BioBean existingContentBean = (BioBean)contentBean;					
				DataBean fileRecord = (DataBean)existingContentBean.get("content_data");

				
				theContentId = (String)existingContentBean.get("content_id");

				if(fileRecord != null){
					//_log.debug("LOG_SYSTEM_OUT","\t[ImportFilesave] Step 1.Update IMPORTFILE file was provided for upload!!"+fileRecord.getValue("content_data_id").toString(),100L);
					existingContentBean.set("content_data_id",fileRecord.getValue("content_data_id"));
					if(ImportFileForm.getFormWidgetByName("FILE").getValue()==null){
						_log.debug("LOG_SYSTEM_OUT","\t[ImportFileSave]Step11. File missing.",100L);
						
						session.setAttribute("TEMPLATEID", templateId);
						throw new FormException("WMEXP_MISSING_FILE",null);
					}
				}else{
					_log.debug("LOG_SYSTEM_OUT","\t[ImportFileSave]Step2. File missing.",100L);
					
					session.setAttribute("TEMPLATEID", templateId);
					throw new FormException("WMEXP_MISSING_FILE",null);
				}
				
				//ImportFile
				Query bioQry = new Query("wm_importfile","wm_importfile.content_id = '"+existingContentBean.get("content_id")+"'","");
				BioCollection existingImportFileRecords = uow.getBioCollectionBean(bioQry); 
				BioBean existingImportFileRecord = (BioBean)existingImportFileRecords.elementAt(0);
				RuntimeFormWidgetInterface descriptionWidget = ImportFileForm.getFormWidgetByName("description");
				RuntimeFormWidgetInterface file_nameWidget = ImportFileForm.getFormWidgetByName("file_name");
			
				
				existingImportFileRecord.set("content_id",existingContentBean.get("content_id"));
				//If description was not provided default it to filename
				if (descriptionWidget==null || descriptionWidget.getDisplayValue()==null || descriptionWidget.getDisplayValue().trim()==""  ){
					if (file_nameWidget.getDisplayValue()!=null)
						existingImportFileRecord.set("DESCRIPTION", file_nameWidget.getDisplayValue());
				}else
					existingImportFileRecord.set("DESCRIPTION",existingContentBean.get("DESCRIPTION"));
				
				existingImportFileRecord.set("STATUS",STATUSQUEUED);
				existingImportFileRecord.set("USERID",(userContext.get("logInUserId")).toString());
				existingImportFileRecord.set("FILENAME", existingContentBean.get("file_name"));
				
				if (templateId!=null)
					existingImportFileRecord.set("TEMPLATEID",templateId);
				else {
					//Just throw error if extension was invoked from Generic screen
					if (fromGeneric)
						throw new FormException("WMEXP_MISSING_TEMPLATE",null);
				}
					
				existingImportFileRecord.set("DBNAME", dbConnection);
				existingImportFileRecord.set("SUBMITTEDFROMENTERPRISE", dbEnterprise);
				
				/**
				existingImportFileRecord.set("TYPE",existingContentBean.get("mime_type"));

				
				_log.debug("LOG_SYSTEM_OUT","Update IMPORTFILE content.mime_type:"+(String)existingContentBean.get("mime_type"),100L);
				    				
				//Get MIME Type from FILETYPES table and load value in content.mime_type column
				bioQry = new Query("wm_filetype","wm_filetype.FILETYPEPK = '"+existingContentBean.get("mime_type")+"'","");
				BioCollection fileTypes = uow.getBioCollectionBean(bioQry);
				
				if(fileTypes == null || fileTypes.size() == 0){
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				existingContentBean.set("mime_type",fileTypes.elementAt(0).get("MIMETYPE"));
				**/
				

			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			throwUserException(e1, "ERROR_SAVING_BIO", null);
		} catch (DataBeanException e) {
			e.printStackTrace();
			throwUserException(e, "ERROR_SAVING_BIO", null);
		} 
		
		 	
		try{
			_log.debug("LOG_SYSTEM_OUT","IMPORTFILE before saveUOW.",100L);
			
			uow.saveUOW(true);
			_log.debug("LOG_SYSTEM_OUT","IMPORTFILE changing FILE widget to active state.",100L);
			ImportFileForm.getFormWidgetByName("FILE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			_log.debug("LOG_SYSTEM_OUT","IMPORTFILE widget value:"+ImportFileForm.getFormWidgetByName("FILE").getProperty(RuntimeFormWidgetInterface.PROP_READONLY).toString(),100L);

			


		}catch (UnitOfWorkException e){
			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		} catch (EpiException e) {
			throwUserException(e, "ERROR_SAVING_BIO", null);
		}
		uow.clearState();
	    result.setFocus(contentBean);
	    
	    
		//FileUpload
	    Query query = new Query("wm_content", "wm_content.content_id='"+theContentId+"'","");
		BioBean savedContentBean = (BioBean)uow.getBioCollectionBean(query).elementAt(0); 
		_log.debug("LOG_SYSTEM_OUT","[ImportfileSave]File type:"+savedContentBean.getString("mime_type"),100L);

		
	    CMService cmservice = CMSUtil.getCMService();
		CMSHandle cmsHandle  = cmservice.getContentHandle(savedContentBean.getBioRef());
		String fileName2 = cmsHandle.getFileName();
		_log.debug("LOG_SYSTEM_OUT","[ImportfileSave] CM SERVICE FILENAME:"+fileName2,100L);
	    _log.debug("LOG_SYSTEM_OUT","ImportFileSave END",100L);


	return RET_CONTINUE;
   }
	
   
}
