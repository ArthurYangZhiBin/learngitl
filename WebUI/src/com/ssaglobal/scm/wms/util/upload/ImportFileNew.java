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
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.GUIDFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

/**
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ImportFileNew extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private static final String EXCELTYPE = "application/vnd.ms-excel";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ImportFileNew.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		return setupImportFileNew(context, result);

	}

	public static int setupImportFileNew(ActionContext context, ActionResult result) throws DataBeanException, EpiDataException, EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		HttpSession session = state.getRequest().getSession();
		String templateId = (String) session.getAttribute("TEMPLATEID");
		session.removeAttribute("TEMPLATEID");

		String userId = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String dbConnection = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();

		_log.debug("LOG_SYSTEM_OUT", "[ImportFileNew]username:" + userId, 100L);
		//Content
		QBEBioBean newContentBean = uow.getQBEBioWithDefaults("wm_content");
		newContentBean.set("content_id", GUIDFactory.getGUIDStatic());
		newContentBean.set("data_source", "FILE");
		newContentBean.set("revision_number", "1");
		newContentBean.set("obsolete_flag", "0");
		newContentBean.set("is_url_flag", "0");
		newContentBean.set("content_num", "0");
		newContentBean.set("mime_type", EXCELTYPE);
		DataBean fileRecord = (DataBean) newContentBean.get("content_data");
		if (fileRecord != null) {
			_log.debug("LOG_SYSTEM_OUT", "IMPORTFILE file was provided for upload!!", 100L);
			newContentBean.set("content_data_id", fileRecord.getValue("content_data_id"));
		}

		//ImportFile
		QBEBioBean newImportFileRecord = uow.getQBEBioWithDefaults("wm_importfile");
		newImportFileRecord.set("IMPORTFILEID", GUIDFactory.getGUIDStatic());
		newImportFileRecord.set("content_id", newContentBean.get("content_id"));
		newImportFileRecord.set("DESCRIPTION", "");

		newImportFileRecord.set("STATUS", new String("1"));
		newImportFileRecord.set("USERID", new String(userId));
		newImportFileRecord.set("DBUSERID", new String(userId));
		newImportFileRecord.set("DBNAME", dbConnection);

		if (templateId != null)
			newImportFileRecord.set("TEMPLATEID", templateId);

		String whereClause = "wm_filetype.MIMETYPE ='" + EXCELTYPE + "'";
		Query query = new Query("wm_filetype", whereClause, "");
		BioCollection fileTypeList = uow.getBioCollectionBean(query);
		if (fileTypeList.size() > 0) {
			newImportFileRecord.set("TYPE", fileTypeList.elementAt(0).get("FILETYPEPK"));
		} else {
			//Failed to obtain XLS file type
			_log.debug("LOG_SYSTEM_OUT", "Failed to obtain XLS file type.", 100L);
			return RET_CANCEL;
		}

		uow.saveUOW(true);

		//Requerying
		Query queryContent = new Query("wm_content", "wm_content.content_id='" + newContentBean.get("content_id") + "'", "");
		BioCollectionBean contents = uow.getBioCollectionBean(queryContent);
		if (contents.size() > 0) {
			DataBean contentBean = (DataBean) contents.elementAt(0);
			result.setFocus(contentBean);
		} else {
			//
			_log.debug("LOG_SYSTEM_OUT", "[ImportFileNew]Could not find contentid:", 100L);
		}

		return RET_CONTINUE;
	}

}
