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

package com.ssaglobal.scm.wms.wm_dropid.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DropIDGenerateDropIDContentsReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	String reportID = "CRPT19";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(DropIDGenerateDropIDContentsReportURL.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//Drop ID Contents:  p_DropIdStart, p_ DropIdEnd, p_DATABASE, p_SCHEMA, outputLocale
		RuntimeFormInterface header = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_dropid_detail_header_view", state);
		

		DataBean headerFocus = header.getFocus();
		
		String dropID = headerFocus.getValue("DROPID") == null ? null
				: headerFocus.getValue("DROPID").toString().toUpperCase();
		_log.debug("LOG_DEBUG_EXTENSION_DropIDGenerateDropIDContentsReportURL", dropID, SuggestedCategory.NONE);
		String listDropid = dropID;
		listDropid = getChildDropid(dropID,uow,listDropid);
		StringTokenizer st = new StringTokenizer(listDropid,",");
		HashMap parametersAndValues = new HashMap();
		parametersAndValues.put("p_DropIdStart", dropID);
		parametersAndValues.put("p_DropIdEnd", dropID);
//		parametersAndValues.put("p_DropIdRange", );
		while (st.hasMoreTokens()) {
			parametersAndValues.put("p_DropIdRange",st.nextToken() );
		}
		if (listDropid == dropID){
			parametersAndValues.put("p_DropIdRange",dropID);
		}
		String reportURL = ReportUtil.retrieveReportURL(state, reportID, parametersAndValues);
		_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ reportURL,100L);
		_log.debug("LOG_DEBUG_EXTENSION_DropIDGenerateDropIDContentsReportURL", reportURL, SuggestedCategory.NONE);
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put("REPORTURL", reportURL);
		return RET_CONTINUE;
	}
	
	public String getChildDropid(String dropID,UnitOfWorkBean uow, String listDropid) throws EpiDataException{
		
		String qry = "wm_dropiddetail.DROPID = '"+dropID+"' AND wm_dropiddetail.IDTYPE != '1' AND NOT (wm_dropiddetail.IDTYPE IS NULL)";
    	_log.debug("LOG_SYSTEM_OUT","This is the query: "+qry,100L);
   		Query loadBiosQry = new Query("wm_dropiddetail", qry, null);
   		BioCollectionBean dropidDetailBiocollection = uow.getBioCollectionBean(loadBiosQry);
   		_log.debug("LOG_SYSTEM_OUT","size of the bio collection "+dropidDetailBiocollection.size(),100L);
   		int i=0;
   		for (i=0; i<dropidDetailBiocollection.size(); i++){
   			_log.debug("LOG_SYSTEM_OUT","In side the bio collection",100L);
   			BioBean dropidDetail = (BioBean)dropidDetailBiocollection.elementAt(i);
   			listDropid = listDropid + "," + dropidDetail.get("CHILDID").toString();
   			_log.debug("LOG_SYSTEM_OUT","List of Child drop ids = "+ listDropid,100L);
   			getChildDropid (dropidDetail.getValue("CHILDID").toString(),uow,listDropid);
   			
   		}

        return listDropid;
	}

}
