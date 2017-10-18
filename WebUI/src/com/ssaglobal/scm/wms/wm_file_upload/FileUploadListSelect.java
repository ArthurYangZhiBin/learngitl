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
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
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

public class FileUploadListSelect extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(FileUploadListSelect.class);

    
    protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	
    	
    	StateInterface state = context.getState();
    	UnitOfWorkBean uow = state.getDefaultUnitOfWork();
    	DataBean selectedRecord = result.getFocus();
    	if(selectedRecord == null)
    		return RET_CONTINUE;
    	
    	Query bioQry = new Query("wm_content","wm_content.content_id = '"+selectedRecord.getValue("content_id")+"'","");
    	BioCollection contentRecords = uow.getBioCollectionBean(bioQry);
    	
    	if(contentRecords == null || contentRecords.size() == 0){
    		return RET_CONTINUE;
    	}
    	
    	result.setFocus((BioBean)contentRecords.elementAt(0));
    	
    	return RET_CONTINUE;
    	
    }
}
