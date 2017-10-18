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

import java.io.IOException;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FileUtil;

public class UploadContent {

	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FileUtil.class);
	public UploadContent()
    {
    }
	
	public static String generateFileRequest(String fileId, StateInterface state, Boolean constructReletive) throws EpiDataException{
		String request = "";
		String mimeType = "";
		String downloadAs = "";
		_log.debug("LOG_DEBUG_UPLOAD","Executing generateFileRequest()...",100L);
		if(fileId == null || fileId.length() == 0){
			_log.debug("LOG_DEBUG_UPLOAD","fileId invalid, exiting...",100L);
			return request;
		}
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//Query bioQry = new Query("wm_fileupload","wm_fileupload.FILEUPLOADPK = '"+fileId+"'","");
		Query bioQry = new Query("wm_importfile","wm_importfile.IMPORTFILEID = '"+fileId+"'","");
		_log.debug("LOG_DEBUG_UPLOAD","retrieving Import fileRecords...",100L);
		BioCollection fileRecords = uow.getBioCollectionBean(bioQry);
		if(fileRecords == null || fileRecords.size() == 0){
			_log.debug("LOG_DEBUG_UPLOAD","fileRecords invalid, exiting...",100L);
			return request;
		}
		bioQry = new Query("content","content.content_id = '"+fileRecords.elementAt(0).get("content_id")+"'","");
		_log.debug("LOG_DEBUG_UPLOAD","retrieving contentRecords...",100L);
		BioCollection contentRecords = uow.getBioCollectionBean(bioQry);
		if(contentRecords == null || contentRecords.size() == 0 || contentRecords.elementAt(0).get("content_data_id") == null){
			_log.debug("LOG_DEBUG_UPLOAD","contentRecords invalid, exiting...",100L);
			return request;
		}
		fileId = contentRecords.elementAt(0).get("content_data_id").toString();
		String fileType = (String)fileRecords.elementAt(0).get("TYPE");
		_log.debug("LOG_DEBUG_UPLOAD","fileType:"+fileType,100L);
		if(fileType == null)
			return request;
		bioQry = new Query("wm_filetype","wm_filetype.FILETYPEPK = '"+fileRecords.elementAt(0).get("TYPE")+"'","");
		_log.debug("LOG_DEBUG_UPLOAD","retrieving fileTypes...",100L);
		BioCollection fileTypes = uow.getBioCollectionBean(bioQry);
		if(fileTypes == null || fileTypes.size() == 0){
			_log.debug("LOG_DEBUG_UPLOAD","fileTypes invalid, exiting...",100L);
			return request;
		}
		
		if(fileType.equals("D7B601E50A21652301A183D22F2CEC57")){
			downloadAs = "image";
		}else if (fileType.equals("D7B601E50A21652301A183D22F2CEC58")){
			downloadAs = "xls";
		}
		mimeType = (String)fileTypes.elementAt(0).get("MIMETYPE");
		_log.debug("LOG_DEBUG_UPLOAD","mimeType:"+mimeType,100L);
		if(constructReletive == null)
			constructReletive = Boolean.FALSE;
		
		if(constructReletive.booleanValue()){
			//jp.answerlink.288313.begin
			//request = "sc/wmsapp.ctrl?";
			request = "?";
			//jp.answerlink.288313.end

		}
		else{
			request = state.getRequest().getScheme()+"://"+state.getRequest().getServerName()+":"+state.getRequest().getServerPort()+state.getRequest().getContextPath()+"/sc/wmsapp.ctrl?";
		}
		request += "FILEDOWNLOAD_REQUEST=true&CONTENT_DATA_BIO_REF=0@content_data@";
		request += fileId;
		request += "&CONTENT_DATA_MIME_TYPE=";
		request += mimeType;
		request += "&FILE_NAME=";
		request += downloadAs;
		
		_log.debug("LOG_SYSTEM_OUT","\n\n URL:"+request+"\n\n",100L);
		
		return request;
	}
	
	public static void downloadFile(String fileId, StateInterface state) throws EpiDataException, IOException{
		String fileURL = generateFileRequest(fileId,state,Boolean.FALSE);
		state.getResponse().sendRedirect(fileURL);
	}
}
