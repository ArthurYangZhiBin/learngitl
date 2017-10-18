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

package com.ssaglobal.scm.wms.wm_indirect_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class IATest extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(IATest.class);
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
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_indirect_activity_list", state);

		_log.debug("LOG_DEBUG_EXTENSION", "Name: " + listForm.getName(), SuggestedCategory.NONE);

		BioCollectionBean list = (BioCollectionBean) listForm.getFocus();

		_log.debug("LOG_DEBUG_EXTENSION", "Start " + list.size(), SuggestedCategory.NONE);
		for (int i = 0; i < list.size(); i++)
		{
			BioBean sel = list.get("" + i);
			if (sel.hasBeenUpdated("USERATTENDANCEKEY"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Updated VALUE UAK" + sel.get("USERATTENDANCEKEY"), SuggestedCategory.NONE);
			}
			
			Bio selb = list.elementAt(i);
			if (selb.hasBeenUpdated("USERATTENDANCEKEY"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Updated VALUE UAK" + selb.get("USERATTENDANCEKEY"), SuggestedCategory.NONE);
			}
			

			if (sel.hasBeenUpdated("USERID"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Updated VALUE UID" + sel.get("USERID"), SuggestedCategory.NONE);
			}
			
			
			if (selb.hasBeenUpdated("USERID"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Updated VALUE UID" + selb.get("USERID"), SuggestedCategory.NONE);
			}
			
			
		}
		_log.debug("LOG_DEBUG_EXTENSION", "End", SuggestedCategory.NONE);

		return RET_CONTINUE;
	}

}
