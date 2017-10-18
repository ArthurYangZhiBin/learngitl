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

package com.ssaglobal.scm.wms.wm_remaining_work.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class RemainingWorkQueryAction extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(RemainingWorkQueryAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		com.epiphany.shr.ui.model.data.BioBean remainingWorkGroup = null;
		try {
			remainingWorkGroup = uowb.getBioBean(bioRef);
			result.setFocus(remainingWorkGroup);
		} catch (BioNotFoundException bioEx) {
			_logger.error(bioEx);
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}

		// remaining Work Group
		// need to query taskdetail to get the records
		// Date - 00:00:00 to 23:59:59
		// TaskType
		// UOM
		// DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		// Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// startDate.setTimeInMillis(BioAttributeUtil.getCalendar(remainingWorkGroup, "ADDDATE").getTimeInMillis());
		// startDate.set(Calendar.HOUR_OF_DAY, 0);
		// startDate.set(Calendar.MINUTE, 0);
		// startDate.set(Calendar.SECOND, 0);
		// startDate.set(Calendar.MILLISECOND, 0);
		// Calendar endDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// endDate.setTimeInMillis(BioAttributeUtil.getCalendar(remainingWorkGroup, "ADDDATE").getTimeInMillis());
		// endDate.set(Calendar.HOUR_OF_DAY, 23);
		// endDate.set(Calendar.MINUTE, 59);
		// endDate.set(Calendar.SECOND, 59);
		// endDate.set(Calendar.MILLISECOND, 999);
		String taskType = BioAttributeUtil.getString(remainingWorkGroup, "TASKTYPE");
		String subtask = BioAttributeUtil.getString(remainingWorkGroup, "SUBTASK");
		
		_log.info(	"LOG_INFO_EXTENSION_RemainingWorkQueryAction_execute",
					"Going to query TaskDetail " + taskType + " " + subtask,
					SuggestedCategory.NONE);
		DataBean rs;
		if (StringUtils.isEmpty(subtask)) {
			 rs = uowb.getBioCollectionBean(new Query("wm_taskdetail", "wm_taskdetail.TASKTYPE = '" + taskType + "'  and wm_taskdetail.SUBTASK IS NULL and wm_taskdetail.STATUS IN ('0','3')", "wm_taskdetail.ADDDATE DESC"));
		} else {
			 rs = uowb.getBioCollectionBean(new Query("wm_taskdetail", "wm_taskdetail.TASKTYPE = '" + taskType + "' and wm_taskdetail.SUBTASK = '" + subtask + "' and wm_taskdetail.STATUS IN ('0','3')", "wm_taskdetail.ADDDATE DESC"));
		}
		
		
		result.setFocus(rs);

		return RET_CONTINUE;
	}
}
