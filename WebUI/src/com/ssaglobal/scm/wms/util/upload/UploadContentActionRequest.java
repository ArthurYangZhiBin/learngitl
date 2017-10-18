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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.export.ExcelExportHandler;
import com.epiphany.shr.ui.util.export.ExportHelper;
import com.epiphany.shr.ui.util.export.ExportInfo;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class UploadContentActionRequest extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {

	private static final String STATUSCOMPLETEDSUCCESSFULLY = "4";
	private static final String STATUSINPROCESS = "3";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UploadContentActionRequest.class);
	
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
		throws EpiException {

		
		
		//Select record from list form
		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		com.epiphany.shr.ui.model.data.BioBean importfileBean = null;
		try
		{
			importfileBean = uowb.getBioBean(bioRef);
		}
		catch(BioNotFoundException bioEx)
		{

			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}

		//Build URL with content_id
		//_log.debug("LOG_SYSTEM_OUT","[UploadContentActionRequest]Getting importfileid",100L);
		String importfileid = importfileBean.getString("IMPORTFILEID");
		
		_log.debug("LOG_SYSTEM_OUT","[UploadContentActionRequest]Getting importfileid:"+importfileid,100L);
		
		String url =UploadContent.generateFileRequest(importfileid, state, Boolean.TRUE);
		//_log.debug("LOG_SYSTEM_OUT","[UploadContentActionRequest]URL BEFORE:"+url,100L);
		//url ="http://usdaljpuente1.infor.com:80/infor/" + url;
		String requestURL = state.getRequestURL();
		//jp.answerlink.288313.begin
		//String prefix = buildPrefix (requestURL);
		//url = prefix + "/" + url;
		url = requestURL.trim() + url.trim() ;
		//jp.answerlink.288313.end

		_log.debug("LOG_SYSTEM_OUT","[UploadContentActionRequest]URL:"+url,100L);
	
		//_log.debug("LOG_SYSTEM_OUT","STATUS:"+importfileBean.getString("STATUS"),100L);

		String jsCode = null;
		if (importfileBean.getString("STATUS")!=null && importfileBean.getString("STATUS").compareToIgnoreCase(STATUSCOMPLETEDSUCCESSFULLY)==0){
			jsCode="alert('Job completed successfully.')";
			setResult("jsCode",jsCode);
			//return RET_CONTINUE;

		}else if (importfileBean.getString("STATUS")!=null && importfileBean.getString("STATUS").compareToIgnoreCase(STATUSINPROCESS)==0){
			jsCode="alert('Can not review contents while in-process. Please wait until job is complete.')";
			setResult("jsCode",jsCode);

		}else{
			
			//Javascript to open a new window.
			jsCode = " if (window.epnyProcessingWindow) {window.epnyProcessingWindow.close();} ";
			jsCode = jsCode + "window.open('" + url + "');";
			setResult("jsCode", jsCode);
			setStatus(JSCommandExtensionBase.STATUS_SUCCESS);
		}

		return RET_CONTINUE;
	
	}
	
	private String buildPrefix(String requestURL){
		
		//Since url must start with HTTP://
		if (requestURL.length()>7){
			_log.debug("LOG_SYSTEM_OUT",requestURL,100L);
			int position = requestURL.indexOf('/',7);
			position = requestURL.indexOf('/',position+1);
			
			return requestURL.substring(0,position);
		}else
			return "";
	}
	
	private boolean foundContentData(String importfileid){
		return false;
	}
}
