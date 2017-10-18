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
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
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

public class FileUploadDelete extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(FileUploadDelete.class);

    
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	
		
		StateInterface state = context.getState();
		RuntimeListFormInterface fileUploadListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_file_upload_list_form",state);		
		ArrayList selectedItems = fileUploadListForm.getAllSelectedItems();
		if(selectedItems == null || selectedItems.size() == 0)
			return RET_CONTINUE;
		String failedDeleteMsg = "";
		String checkIfAttachedQry = "";
		for(int i = 0; i < selectedItems.size(); i++){
			Bio selectedItem = (Bio)selectedItems.get(i);
			if(checkIfAttachedQry.length() == 0)
				checkIfAttachedQry += " wm_fileattachmapping.FILEID = '"+selectedItem.get("FILEUPLOADPK")+"' ";
			else
				checkIfAttachedQry += " OR wm_fileattachmapping.FILEID = '"+selectedItem.get("FILEUPLOADPK")+"' ";
		}
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollection attachedFiles = uow.getBioCollectionBean(new Query("wm_fileattachmapping",checkIfAttachedQry,"wm_fileattachmapping.FILEID"));
		
		//If any of the selected files are in use then construct an error msg and display it to the user.
		if(attachedFiles.size() > 0){
			String currentFileId = (String)attachedFiles.elementAt(0).get("FILEID");
			failedDeleteMsg = (String)attachedFiles.elementAt(0).get("FILELABEL");
			for(int i = 0; i < attachedFiles.size(); i++){
				if(!attachedFiles.elementAt(i).get("FILEID").equals(currentFileId)){
					currentFileId = (String)attachedFiles.elementAt(i).get("FILEID");
					failedDeleteMsg += ", "+(String)attachedFiles.elementAt(i).get("FILELABEL");
				}
			}			
			String args[] = {failedDeleteMsg}; 			
			String errorMsg = getTextMessage("WMEXP_FILES_IN_USE",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//Get biocollections for content and content_data tables related to the selected items so that they can be deleted.
		String contentIdQry = "";
		for(int i = 0; i < selectedItems.size(); i++){
			Bio selectedItem = (Bio)selectedItems.get(i);			
			if(contentIdQry.length() == 0)
				contentIdQry += "content.content_id = '"+selectedItem.get("content_id")+"'";
			else
				contentIdQry += " OR content.content_id = '"+selectedItem.get("content_id")+"' ";
			selectedItem.delete();
		}
		BioCollection contentCollection = uow.getBioCollectionBean(new Query("content",contentIdQry,""));
		for(int i = 0; i < contentCollection.size(); i++){
			if(contentCollection.elementAt(i).getBio("content_data") != null)
				contentCollection.elementAt(i).getBio("content_data").delete();
			contentCollection.elementAt(i).delete();
		}
		_log.debug("LOG_SYSTEM_OUT","\n\nDeleteing!!!!!\n\n",100L);
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
		fileUploadListForm.setSelectedItems(null);
	    result.setSelectedItems(null);

	return RET_CONTINUE;

}
}
