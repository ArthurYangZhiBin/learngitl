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

package com.ssaglobal.scm.wms.wm_task_manager_section.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
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

public class TaskManagerSectionSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskManagerSectionSaveValidation.class);

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
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface sectionForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_task_manager_section_detail_view", state);
		if (!isNull(sectionForm))
		{
			TMSection sectionValidation = new TMSection(sectionForm, state);
			sectionValidation.run();
		}
		else
		{
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	class TMSection extends FormValidation
	{
		String key;

		public TMSection(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			key = focus.getValue("SECTIONKEY") == null ? null : focus.getValue("SECTIONKEY").toString().toUpperCase();
		}

		public void run() throws EpiDataException, UserException
		{
			if (isInsert)
			{
				sectionDuplication();
			}

		}

		void sectionDuplication() throws DPException, UserException
		{

			String query = "SELECT * FROM SECTION WHERE (SECTIONKEY = '" + key + "')";
			_log.debug("LOG_DEBUG_EXTENSION", query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("SECTIONKEY");
				parameters[1] = key;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}
	}

	private boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
